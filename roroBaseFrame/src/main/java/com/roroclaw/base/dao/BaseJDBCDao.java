package com.roroclaw.base.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.roroclaw.base.bean.PageBean;
import com.roroclaw.base.handler.PageDataConvert;
import com.roroclaw.base.utils.StringKit;

/**
 * @author dxz 基础类-DAO-持久层采用jdbc方式
 */
@SuppressWarnings("unchecked")
public abstract class BaseJDBCDao {
	protected static Logger logger = LoggerFactory.getLogger(BaseJDBCDao.class);
	
	protected JdbcTemplate jdbcTemplate = null;
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;

	public static final String DB_TYPE_ORACLE = "oracle";
	public static final String DB_TYPE_SQLITE = "sqlite";
	public static final String DB_TYPE_MYSQL = "mysql";
	
	public PageDataConvert pageDataConvert;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate);
	}

	/**
	 * 分页数据查询
	 * 
	 * @param mainSql 查询主体sql
	 * @param paramMap 分页参数以及查询条件
	 * @return
	 */
	protected abstract List queryListforPage(String moduleSql,
			Map<String, String> paramMap);
	
	/**
	 * 分页数据查询
	 * 
	 * @param mainSql  查询主体sql
	 * @param pageBean
	 * @param paramMap 查询条件
	 * @return
	 */
	protected abstract PageBean queryforPageBean(String moduleSql,PageBean pageBean,
			Map<String, String> queryparam);
	
	/**
	 * 分页数据查询
	 * 
	 * @param mainSql  查询主体sql
	 * @param pageBean(自带查询条件)
	 * @return
	 */
	public PageBean queryforPageBean(String moduleSql,PageBean pageBean){
		Map queryMap = pageBean.getQueryparam();
		return this.queryforPageBean(moduleSql, pageBean,queryMap);
	}


	/**
	 * 获取查询数据总数
	 * 
	 * @param moduleSql
	 * @return
	 */
	public int getDataCount(String moduleSql, Map paramMap){
		String countSql = "";
		if (moduleSql.toUpperCase().indexOf("SELECT") > -1) {
			countSql = "select count(*) from (" + moduleSql + ") subsql";
		} else {
			countSql = "select count(*) from " + moduleSql;
		}
		int count = this.namedParameterJdbcTemplate.queryForInt(countSql,
				paramMap);
		logger.info("纪录数:" + count);
		return count;
	}

//	/**
//	 * 保存数据库记录
//	 * 拼接插入 sql
//	 * @param tableName
//	 * @param paramMap
//	 * @return
//	 */
//	public int saveRecord(String tableName, Map paramMap) {
//		StringBuffer tableNamePart = new StringBuffer("insert into ")
//				.append(tableName);
//		Set keySet = paramMap.keySet();
//		StringBuffer colNamePart = new StringBuffer("(");
//		StringBuffer colValuePart = new StringBuffer("(");
//		int size = keySet.size();
//		int index = 0;
//		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
//			String colName = (String) iterator.next();
//			if (index != size - 1) {
//				colNamePart.append(colName).append(",");
//				colValuePart.append(":").append(colName).append(",");
//			} else {
//				colNamePart.append(colName).append(")");
//				colValuePart.append(":").append(colName).append(")");
//			}
//			index++;
//		}
//		String sql = tableNamePart.append(colNamePart).append(" values")
//				.append(colValuePart).toString();
//		return namedParameterJdbcTemplate.update(sql, paramMap);
//	}

	/**
	 * 分页获取数据
	 * 
	 * @param moduleSql
	 * @param paramMap
	 * @return
	 */
	public Map getResultMapForPage(String moduleSql,
			Map<String, String> paramMap) {
		Map resMap = new HashMap();
		List dataList = null;
		dataList = this.queryListforPage(moduleSql, paramMap);
		int dataCount = this.getDataCount(moduleSql, paramMap);
		resMap.put(PageBean.RESULT_MAP_DATA, dataList);
		resMap.put(PageBean.RESULT_MAP_TOTALSIZE, dataCount);
		return resMap;
	}

	/**
	 * 分页获取数据
	 * 
	 * @param moduleSql
	 * @param paramMap
	 * @return
	 */
	public Map getResultMapForPage(String moduleSql, String page,
			String pagesize) {
		Map resMap = new HashMap();
		List dataList = null;
		Map paramMap = this.createPageParam(page!=null?page:"1", pagesize != null ? pagesize
				: String.valueOf(PageBean.SQL_PARAM_LIMIT_DEFAULTVALUE));
		dataList = this.queryListforPage(moduleSql, paramMap);
		int dataCount = this.getDataCount(moduleSql, paramMap);
		resMap.put(PageBean.RESULT_MAP_DATA, dataList);
		resMap.put(PageBean.RESULT_MAP_TOTALSIZE, dataCount);
		return resMap;
	}
	
	/**
	 * 分页获取数据
	 * 
	 * @param moduleSql
	 * @param paramMap
	 * @return
	 */
	public Map getResultMapForPage(String moduleSql, String page,
			String pagesize,Map paramMap) {
		Map resMap = new HashMap();
		List dataList = null;
		 paramMap.putAll(this.createPageParam(page!=null?page:"1", pagesize != null ? pagesize
				: String.valueOf(PageBean.SQL_PARAM_LIMIT_DEFAULTVALUE)));
		dataList = this.queryListforPage(moduleSql, paramMap);
		int dataCount = this.getDataCount(moduleSql, paramMap);
		resMap.put(PageBean.RESULT_MAP_DATA, dataList);
		resMap.put(PageBean.RESULT_MAP_TOTALSIZE, dataCount);
		return resMap;
	}
	

	/**
	 * 
	 * @param page
	 *            页索引参数
	 * @param pagesize
	 *            页记录数参数
	 * @return
	 */
	private Map createPageParam(String page, String pagesize) {
		Map paramMap = new HashMap();
		int start = (Integer.parseInt(page)-1) * Integer.parseInt(pagesize);
		paramMap.put(PageBean.SQL_PARAM_START, String.valueOf(start));
		paramMap.put(PageBean.SQL_PARAM_LIMIT, pagesize);
		return paramMap;
	}
	
	
	/**
	 * 保存记录到数据库
	 * 
	 * @param tableName 数据库表名
	 * @param paramMap 参数Map对象,参数key必须与数据库字段名一致
	 * @return 影响记录数
	 */
	public int save(String tableName, Map<String, Object> paramMap) {
		String sqlFormat = "insert into %s(%s) values(%s)";
		Set<String> keySet = paramMap.keySet();
		String columns = StringKit.join(keySet, ", ");
		String values = formateSqlVal(keySet, ":%1$s");
		String sql = String.format(sqlFormat, tableName, columns, values);
		return namedParameterJdbcTemplate.update(sql, paramMap);
	}
	
	
	/**
	 * 保存记录到数据库
	 * @param sqlFormat sql拼装格式
	 * @param tableName 数据库表名
	 * @param paramMap 参数Map对象,参数key必须与数据库字段名一致
	 * @return 影响记录数
	 */
	public int saveBySqlFormat(String sqlFormat , String tableName, Map<String, Object> paramMap) {
		Set<String> keySet = paramMap.keySet();
		String columns = StringKit.join(keySet, ", ");
		String values = formateSqlVal(keySet, ":%1$s");
		String sql = String.format(sqlFormat, tableName, columns, values);
		return namedParameterJdbcTemplate.update(sql, paramMap);
	}
	
	/**
	 * 根据主键更新数据到数据库表
	 * 
	 * @param tableName 数据库表名
	 * @param paramMap 参数Map对象,参数key必须与数据库字段名一致
	 * @return 影响记录数
	 */
	public int update(String tableName, Map<String, Object> paramMap) {
		String sqlFormat = "update %s set %s where id=:id";
		Object id = paramMap.get("id");
		Set<String> keySet = paramMap.keySet();
		keySet.remove("id");
		String setSql = formateSqlVal(keySet, "%1$s=:%1$s");
		paramMap.put("id", id);
		String sql = String.format(sqlFormat, tableName, setSql);
		return namedParameterJdbcTemplate.update(sql, paramMap);
	}
	
	/**
	 * 执行sql语句
	 * 
	 */
	public Object excuteSql(String sql, Map<String, Object> paramMap) {
		return namedParameterJdbcTemplate.update(sql, paramMap);
	}
	
	/**
	 * 执行sql语句
	 * 
	 */
	public Object excuteSql(String sql) {
		return jdbcTemplate.update(sql);
	}
	
	/**
	 * 执行sql语句
	 * 
	 */
	public Object batchExcuteSql(String sql, Map<String, Object>[] paramMap) {
		return namedParameterJdbcTemplate.batchUpdate(sql, paramMap);
	}
	
	/**
	 * 自装参数map的val字符串,编译nameJdbc使用
	 * @param collection
	 * @return
	 */
	private String formateSqlVal(Collection<String> paramCollect,final String format){
		String valStr = "";
		int count = 1;
		int size = paramCollect.size();
		for (String key : paramCollect) {
			valStr += String.format(format, key);
			if(count < size){
				valStr += ",";
			}
			count++;
		}
		return valStr;
	}
	
	/**
	 * 根据主键从数据库表删除数据
	 * 
	 * @param tableName 数据库表名
	 * @param paramMap 参数Map对象
	 * @return 影响记录数
	 */
	public int delete(String tableName, Map<String, Object> paramMap) {
		String sqlFormat = "delete from %s where id=:id";
		String sql = String.format(sqlFormat, tableName);
		return namedParameterJdbcTemplate.update(sql, paramMap);
	}
	
	/**
	 * 根据主键从数据库表删除数据
	 * 
	 * @param tableName 数据库表名
	 * @param id 主键id
	 * @return 影响记录数
	 */
	public int delete(String tableName, Object id) {
		Map paramMap = new HashMap();
		paramMap.put("id", id);
		return this.delete(tableName, paramMap);
	}
	
	/**
	 * 根据多个主键Id删除数据库表记录
	 * @param tableName 数据库表名
	 * @param idValues 主键Id值
	 * @return 影响记录数
	 */
	public int deleteByIds(String tableName, Object[] idValues) {
		String sqlFormat = "delete from %s where id in %s";
		String inValues = "( " + StringKit.join(StringKit.format(idValues, "?"), ", ") + " )";
		String sql = String.format(sqlFormat, tableName, inValues);
		return jdbcTemplate.update(sql, idValues);
	}
	
	public List<Map<String, Object>> sqlQuery(String sql, Map<String, Object> paramMap) {
		logger.debug("querySql="+sql);
		return this.namedParameterJdbcTemplate.queryForList(sql, paramMap);
	}

	public List<Map<String, Object>> sqlQuery(String sql) {
		return this.jdbcTemplate.queryForList(sql);
	}
	
	public int sqlQuery4Int(String sql) {
		return this.jdbcTemplate.queryForInt(sql);
	}
	
	public int sqlQuery4Int(String sql,Map paramMap) {
		return this.namedParameterJdbcTemplate.queryForInt(sql,paramMap);
	}
	
	public Map sqlQuery4Map(String sql) {
		return this.jdbcTemplate.queryForMap(sql);
	}
	
	public Map sqlQuery4Map(String sql,Map paramMap) {
		return this.namedParameterJdbcTemplate.queryForMap(sql,paramMap);
	}
	
	public long sqlQuery4Long(String sql) {
		return this.jdbcTemplate.queryForLong(sql);
	}

	public List<Map<String, Object>> sqlQuery(String sql, Object... args) {
		return this.jdbcTemplate.queryForList(sql, args);
	}
}
