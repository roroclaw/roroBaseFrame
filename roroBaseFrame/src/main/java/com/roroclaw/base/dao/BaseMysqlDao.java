package com.roroclaw.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.roroclaw.base.bean.PageBean;
import com.roroclaw.base.handler.PageDataConvert;

/**
 * jdbc mysql 基础类
 */
public class BaseMysqlDao extends BaseJDBCDao {

//	@Override
//	public int getDataCount(String moduleSql, Map paramMap) {
//		String countSql = "";
//		if (moduleSql.toUpperCase().indexOf("SELECT") > -1) {
//			countSql = "select count(*) from (" + moduleSql + ") subsql";
//		} else {
//			countSql = "select count(*) from " + moduleSql;
//		}
//		int count = this.namedParameterJdbcTemplate.queryForInt(countSql,
//				paramMap);
//		logger.info("纪录数:" + count);
//		return count;
//	}

	@Override
	protected List queryListforPage(String moduleSql, Map<String,String> paramMap) {
		Map resMap = new HashMap();
		List<Map<String, Object>> list = null;
		// 获取共有属性
		String start = (String) paramMap.get(PageBean.SQL_PARAM_START);// 起始行数
		String limit = (String) paramMap.get(PageBean.SQL_PARAM_LIMIT);// 每页显示数

		// 初始化共有属性默认值
		if (start == null || "".equals(start.trim())) {
			start = "0";
		}
		
		if (limit == null || "".equals(limit)) {
			limit = String.valueOf(Integer.valueOf(start)
					+ Integer.valueOf(PageBean.SQL_PARAM_LIMIT_DEFAULTVALUE));
		} 

		// // 获取分页结果
		StringBuffer sb_q = new StringBuffer();
		if (moduleSql.toUpperCase().indexOf("SELECT") < 0) {
			sb_q.append("select * from " + moduleSql);
		}else{
			sb_q.append(moduleSql);
		}
		if (start != null && limit != null) {
			sb_q.append(" limit " + start + "," + limit);
		}
		String sql = sb_q.toString();
		logger.info("QuerySql:" + sql);
		list = this.namedParameterJdbcTemplate.queryForList(sql, paramMap);
		logger.info("QuerySql结果数:" + list.size());
		return list;
	}
	
	
	
	/**
	 * 获取上次插入数据的生成的id
	 * @return
	 */
	public int getLastId(){
		int id = 0;
		String sql = "SELECT LAST_INSERT_ID()";
		id = this.jdbcTemplate.queryForInt(sql);
		return id;
	}

	@Override
	public PageBean queryforPageBean(String moduleSql,PageBean pageBean,
			Map<String, String> queryparam) {
		Map resMap = new HashMap();
		List<Map<String, Object>> list = null;
		// 获取共有属性
		String start = String.valueOf(pageBean.getStartRowNum()) ;// 起始行数
		String limit = String.valueOf(pageBean.getRecordsPerpage());// 每页显示数
		
		// // 获取分页结果
		StringBuffer sb_q = new StringBuffer();
		if (moduleSql.toUpperCase().indexOf("SELECT") < 0) {
			sb_q.append("select * from " + moduleSql);
		}else{
			sb_q.append(moduleSql);
		}
		
		sb_q.append(" limit " + start + "," + limit);
		
		String sql = sb_q.toString();
		logger.info("QuerySql:" + sql);
		list = this.namedParameterJdbcTemplate.queryForList(sql, queryparam);
		list = PageDataConvert.formateData(list);
		logger.info("QuerySql结果数:" + list.size());
		pageBean.setData(list);
		pageBean.setTotalRecords(this.getDataCount(moduleSql, queryparam));
		return pageBean;
	}
	
	/**
	 * 保存记录到数据库
	 * @param idSeqName oracle主键序列
	 * @param tableName 数据库表名
	 * @param paramMap 参数Map对象,参数key必须与数据库字段名一致
	 * @return 影响记录数
	 */
	public int save(String tableName,String idSeqName,Map<String, Object> paramMap) {
		String sqlFormat = "insert into %s(id,%s) values("+idSeqName+".NEXTVAL,%s)";
		return this.saveBySqlFormat(sqlFormat,tableName, paramMap);
	}

}
