package com.otzg.base;

import com.otzg.log.util.LogUtil;
import com.otzg.util.DateUtil;
import com.otzg.util.FuncUtil;
import com.dangdang.ddframe.rdb.sharding.keygen.KeyGenerator;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.redisson.Redisson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractServ extends BaseBean {

    @Autowired
    protected Redisson redisson;

    protected Long getId() {
        return UUID.randomUUID().getLeastSignificantBits();
    }

    //自动生成订单号
    protected String getPayOrderNo(String unitId) {
        return DateUtil.yearMonthDayTimeShort() + FuncUtil.getRandInt(0001, 9999) + Math.abs(unitId.hashCode());
    }

    protected final void rollBack() {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }



    @PersistenceContext
    EntityManager em;
    /**
     * 计算总记录的方法
     * 复杂查询可以去掉排序等字段，可以增加查询的灵活性
     * 如：
     * 分别计算
     * totalCount=getCountByHql(final String hql);
     * results = List<T> findListByHql(final String hql,f.getPageSize(),f.getStartIndex());
     * 最后构造  return new Pages(results,totalCount,f.getPageSize(),f.getStartIndex());
     *
     * @param hql
     * @return
     * @author G/2016-4-28 下午2:44:04
     */
    protected Long getCountByHql(final String hql) {
        try {
            @SuppressWarnings("unchecked")
            List entities = em.createQuery(hql).getResultList();
            if (entities != null && entities.size() > 0) {
                return (Long) entities.get(0);
            }
        } catch (IllegalArgumentException e) {
            log("getCountByHql is wrong:" + e);
            log("hql sentence:" + hql);
        }
        return new Long(0);
    }

    /**
     * 原生sql查询方式,用于单独计算总记录的方法
     *
     * @param sql
     * @return
     * @author G/2016-4-28 下午4:15:09
     */
    protected Long getCountBySql(final String sql) {
        try {
            @SuppressWarnings("unchecked")
            List<BigInteger> entities = em.createNativeQuery(sql).getResultList();
            if (entities != null && entities.size() > 0) {
                return entities.get(0).longValue();
            }
        } catch (IllegalArgumentException e) {
            log("countMapBySql is wrong:" + e);
            log("Sql sentence:" + sql);
        }
        return new Long(0);
    }

    /**
     * 原生sql查询方式,可支持所有sql的函数
     * 注意 做sql投影查询的时候，数据库字段不能上text,blob类型字段
     *
     * @param sql
     * @return
     * @author G/2016-4-28 下午4:16:09
     */
    protected List<Map<String, Object>> findMapBySql(final String sql) {
        try {
            return (List<Map<String, Object>>) em
                    .createNativeQuery(sql)
                    .unwrap(SQLQuery.class)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                    .list();
        } catch (IllegalArgumentException e) {
            log("findMapBySql is wrong:" + e);
            log("Sql sentence:" + sql);
        }
        return null;
    }

    /**
     * 原生sql查询方式,可支持所有sql的函数,并支持分页
     * 注意返回值Map.get("数据库字段") 字段大小写敏感，所以必须与数据表字段一致
     *
     * @param sql
     * @param pageSize
     * @param startIndex
     * @return
     * @author G/2016-4-28 下午4:14:58
     */
    protected List<Map<String, Object>> findMapBySql(final String sql, final int pageSize, final int startIndex) {
        try {
            return (List<Map<String, Object>>) em
                    .createNativeQuery(sql)
                    .unwrap(SQLQuery.class)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                    .setFirstResult(startIndex)
                    .setMaxResults(pageSize).list();
        } catch (IllegalArgumentException e) {
            log("findMapBySql is wrong:" + e);
            log("Sql sentence:" + sql);
        }
        return null;
    }

    protected List findListByHql(String hql, int pageSize, int startIndex) {
        try {
            return em.createQuery(hql).setFirstResult(startIndex).setMaxResults(pageSize).getResultList();
        } catch (IllegalArgumentException e) {
            log("findListByHql is wrong:" + e);
            log("hql sentence:" + hql);
        }
        return null;
    }

    /**
     * hql分页功能，支持hibernate limit 分页功能
     * 方法适合不带复杂关联的查询及分页
     * 不适合一对多关联时加载并统计多方，一方的结果会出现重复，计算总数时候会给出多方的合计数量。
     * (G20141015)select count(*) 不支持 fetch抓取，需要去掉fetch语句
     * (G20160114)支持 group by 语句 count(*) 统计
     *
     * @param hql
     * @param pageSize
     * @param startIndex
     * @return
     * @author G/2016-4-28 下午4:12:35
     */
    protected Page findPageByHql(String hql, final int pageSize, final int startIndex) {
        try {

            List<?> results = em.createQuery(hql).setFirstResult(startIndex).setMaxResults(pageSize).getResultList();

            //select count(*) 时候不能fetch抓取,去掉fetch
            hql = "select COUNT(*) " + hql.substring(hql.indexOf("from")).replace("fetch", "");

            long totalCount = ((List<Long>) em.createQuery(hql).getResultList()).stream().reduce(0l,Long::sum);

            return new Page(results, totalCount, pageSize, startIndex);
        } catch (IllegalArgumentException e) {
            log("findPageByHql is wrong:" + e);
            log("hql sentence:" + hql);
        }
        return null;
    }

    /**
     * 带分页功能的原生sql查询方式,可支持所有sql的函数
     * 注意返回值Map.get("数据库字段") 字段大小写敏感，所以必须与数据表字段一致
     *
     * @param sql
     * @param pageSize
     * @param startIndex
     * @return 返回分页包含Map
     * @author G/2016-4-28 下午4:22:35
     */
    protected Page findPageBySql(final String sql, final int pageSize, final int startIndex) {
        try {
            //计算结果总数
            long totalCount = 0;
            String query = "select COUNT(*) " + sql.substring(sql.indexOf("from"));
            List<BigInteger> rl = (List<BigInteger>) em.createNativeQuery(query).getResultList();
            if (rl != null) {
                for (BigInteger r : rl) {
                    totalCount = totalCount + r.longValue();
                }
            }
            //取出当前页面
            List<Map<String, Object>> results = em.createNativeQuery(sql).setFirstResult(startIndex).setMaxResults(pageSize).getResultList();
            return new Page(results, totalCount, pageSize, startIndex);
        } catch (IllegalArgumentException e) {
            log("findPageBySql is wrong:" + e);
            log("sql sentence:" + sql);
        }
        return null;
    }

    /**
     * 调试回显,日志记录
     *
     * @param
     * @return
     * @author:G/2017年12月30日
     */
    void log(Object str) {
        LogUtil.print(str);
    }



}
