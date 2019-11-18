package com.bcb.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@NoRepositoryBean
public interface AbstractDao<T,ID extends Serializable> extends JpaRepository<T,ID> {
	/**
	 * jpa hql 语句查询,仅返回唯一的结果
	 * @author G/2018年1月4日
	 * @param hql
	 * @return
	 */
	T findObjectByHql(final String hql);
	
	/**
	 * hql 获取list集合
	 * @author G/2018年1月5日
	 * @param hql
	 * @return
	 */
	List<T> findListByHql(final String hql);

	List<T> findListByHql(final String hql, final int pageSize, final int startIndex);
	
	/**
	 * hql计算总记录的方法,用于查询分页的统计
	 * 复杂查询可以去掉排序等字段，可以增加查询的灵活性
	 * 如：
	 * 分别计算
	 *       totalCount=getCountByHql(final String hql);
	 *       results = List<T> findListByHql(final String hql,f.getPageSize(),f.getStartIndex());
	 * 最后构造  return new Pages(results,totalCount,f.getPageSize(),f.getStartIndex());
	 * @author G/2018年1月5日
	 * @param hql
	 * @return
	 */
	Long getCountByHql(final String hql);
	
	
	/**
	 * sql 查询
	 * @author G/2018年1月4日
	 * @param sql
	 * @return
	 */
	List<Map<String,Object>> findMapBySql(final String sql);
	
	/**
	 * 带分页的sql 查询
	 * @author G/2018年1月4日
	 * @param sql
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<Map<String,Object>> findMapBySql(final String sql, final int pageSize, final int startIndex);
	
	/**
	 * hql查询返回分页的结果
	 * @author G/2018年1月5日
	 * @param hql
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	Page findPageByHql(final String hql, final int pageSize, final int startIndex);

	/**
	 * sql查询返回分页的结果
	 * @author G/2018年1月5日
	 * @param sql
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	Page findPageBySql(final String sql, final int pageSize, final int startIndex);
	
	/**
	 * 计算总记录的方法,用于查询分页的统计
	 * @author G/2018年1月5日
	 * @param sql
	 * @return
	 */
	Long getCountBySql(final String sql);
	
}