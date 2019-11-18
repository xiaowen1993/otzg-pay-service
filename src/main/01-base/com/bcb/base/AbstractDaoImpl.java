package com.bcb.base;

import com.bcb.log.util.LogUtil;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;


public class AbstractDaoImpl<T, ID extends Serializable> extends SimpleJpaRepository<T,ID> implements AbstractDao<T,ID> {


	private final EntityManager entityManager;

    //父类没有不带参数的构造方法，这里手动构造父类W
//    public AbstractDaoImpl(Class<T> domainClass, EntityManager entityManager) {
//        super(domainClass, entityManager);
//        this.entityManager = entityManager;
//    }

	public AbstractDaoImpl(JpaEntityInformation<T, ID> entityInformation,
						   EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

    /**
	 * 计算总记录的方法
	 * 复杂查询可以去掉排序等字段，可以增加查询的灵活性
	 * 如：
	 * 分别计算
	 *       totalCount=getCountByHql(final String hql);
	 *       results = List<T> findListByHql(final String hql,f.getPageSize(),f.getStartIndex());
	 * 最后构造  return new Pages(results,totalCount,f.getPageSize(),f.getStartIndex());
	 * 
	 * @author G/2016-4-28 下午2:44:04
	 * @param hql
	 * @return
	 */
	public Long getCountByHql(final String hql){
    	try{
        	@SuppressWarnings("unchecked")
			List<T> entities = entityManager.createQuery(hql).getResultList();
    		if(entities!=null && entities.size()>0) {
    			return  (Long) entities.get(0);
    		}
		}catch(IllegalArgumentException e) {
			log("getCountByHql is wrong:"+e);
			log("hql sentence:"+hql);
		}
    	return new Long(0);
	}
    
	/**
  	 * 原生sql查询方式,用于单独计算总记录的方法
  	 * @author G/2016-4-28 下午4:15:09
  	 * @param sql
  	 * @return
  	 */
	public Long getCountBySql(final String sql){
    	try{
    		@SuppressWarnings("unchecked")
			List<BigInteger> entities = entityManager.createNativeQuery(sql).getResultList();
    		if(entities!=null && entities.size()>0) {
    			return entities.get(0).longValue();
    		}
		}catch(IllegalArgumentException e) {
			log("countMapBySql is wrong:"+e);
			log("Sql sentence:"+sql);
		}
    	return new Long(0);
	}
    
	/**
	 * 原生sql查询方式,可支持所有sql的函数
	 * 注意 做sql投影查询的时候，数据库字段不能上text,blob类型字段
	 * @author G/2016-4-28 下午4:16:09
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findMapBySql(final String sql){
		try {
			return (List<Map<String,Object>>) entityManager
												.createNativeQuery(sql)
												.unwrap(SQLQuery.class)
												.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
												.list();
		} catch (IllegalArgumentException e) {
			log("findMapBySql is wrong:"+e);
			log("Sql sentence:"+sql);
		}
		return null;
 	}
	
    /**
	 * 原生sql查询方式,可支持所有sql的函数,并支持分页
     * 注意返回值Map.get("数据库字段") 字段大小写敏感，所以必须与数据表字段一致
	 * @author G/2016-4-28 下午4:14:58
	 * @param sql
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findMapBySql(final String sql,final int pageSize,final int startIndex){
		try{
			 return (List<Map<String,Object>>)entityManager
					 							.createNativeQuery(sql)
					 							.unwrap(SQLQuery.class)
					 							.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
					 							.setFirstResult(startIndex)
					 							.setMaxResults(pageSize).list();
		}catch(IllegalArgumentException e) {
			log("findMapBySql is wrong:"+e);
			log("Sql sentence:"+sql);
		}
		return null;
  	}
	
    /**
	 * 通过hql获取唯一对象没有则发返回null
	 * @author G/2016-4-28 下午4:07:09
	 * @param hql
	 * @return
	 */
	public T findObjectByHql(final String hql){
		try{
			@SuppressWarnings("unchecked")
			List<T> ol=entityManager.createQuery(hql).setFirstResult(0).setMaxResults(1).getResultList();
			if(ol!=null && ol.size()>0){
				return (T)ol.get(0);
			}
		}catch(IllegalArgumentException e) {
			log("findObjectByHql is wrong:"+e);
			log("hql sentence:"+hql);
		}
		return null;
	}
    
	/**
	 * 通过hql获取List<T>
	 * @author G/2016-4-28 下午4:07:09
	 * @param hql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findListByHql(final String hql){
		try{
			return entityManager.createQuery(hql).getResultList();
		}catch(IllegalArgumentException e) {
			log("findListByHql is wrong:"+e);
			log("hql sentence:"+hql);
		}
		return null;
	}

	@Override
	public List<T> findListByHql(String hql, int pageSize, int startIndex) {
		try{
			return (List<T>)entityManager.createQuery(hql).setFirstResult(startIndex).setMaxResults(pageSize).getResultList();
		}catch(IllegalArgumentException e) {
			log("findListByHql is wrong:"+e);
			log("hql sentence:"+hql);
		}
		return null;
	}

	/**
	 *  hql分页功能，支持hibernate limit 分页功能
	 *  方法适合不带复杂关联的查询及分页
	 *  不适合一对多关联时加载并统计多方，一方的结果会出现重复，计算总数时候会给出多方的合计数量。
	 * (G20141015)select count(*) 不支持 fetch抓取，需要去掉fetch语句
	 * (G20160114)支持 group by 语句 count(*) 统计 
	 * @author G/2016-4-28 下午4:12:35
	 * @param hql
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	public Page findPageByHql(final String hql, final int pageSize, final int startIndex){
		try{
			long totalCount=0;
	    	String query=hql;
	    	//select count(*) 时候不能fetch抓取
	    	query = "select COUNT(*) "+query.substring(query.indexOf("from")).replace("fetch","");
	    	@SuppressWarnings("unchecked")
			List<Long> rl=(List<Long>)entityManager.createQuery(query).getResultList();
	    	if(rl!=null){
	    		for(Long r:rl){
	    			totalCount=totalCount+r;
		    	}
	    	}
	        List<?> results = entityManager.createQuery(hql).setFirstResult(startIndex).setMaxResults(pageSize).getResultList();
			return new Page(results,totalCount, pageSize, startIndex);
		}catch(IllegalArgumentException e) {
			log("findPageByHql is wrong:"+e);
			log("hql sentence:"+hql);
		}
		return null;
	}

	/**
	 * 带分页功能的原生sql查询方式,可支持所有sql的函数
	 * 注意返回值Map.get("数据库字段") 字段大小写敏感，所以必须与数据表字段一致
	 * @author G/2016-4-28 下午4:22:35
	 * @param sql
	 * @param pageSize
	 * @param startIndex
	 * @return 返回分页包含Map
	 */
	public Page findPageBySql(final String sql,final int pageSize,final int startIndex){
		try{
			//计算结果总数
			long totalCount=0;
			String query="select COUNT(*) "+sql.substring(sql.indexOf("from"));
			List<BigInteger> rl=(List<BigInteger>)entityManager.createNativeQuery(query).getResultList();
			if(rl!=null){
				for(BigInteger r:rl){
					totalCount = totalCount+r.longValue();
				}
			}
			//取出当前页面
			List<Map<String,Object>> results = entityManager.createNativeQuery(sql).setFirstResult(startIndex).setMaxResults(pageSize).getResultList();
			return new Page(results,totalCount, pageSize, startIndex);
		}catch(IllegalArgumentException e) {
			log("findPageBySql is wrong:"+e);
			log("sql sentence:"+sql);
		}
		return null;
	}
    /**
	 * 调试回显,日志记录
	 * @author:G/2017年12月30日
	 * @param 
	 * @return
	 */
	protected static void log(Object str) {
		LogUtil.print(str);
	}
    
}
