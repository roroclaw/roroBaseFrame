package com.roroclaw.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.roroclaw.base.bean.PageBean;

/**
 * jdbc oracle 基础类
 */
public class BaseOracleDao extends BaseJDBCDao {

	@Override
	protected List queryListforPage(String moduleSql,
			Map<String, String> paramMap) {
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

		// 获取分页结果
		String sqlFormat = "SELECT * FROM ( SELECT subsql.*, ROWNUM RN FROM (%s) subsql WHERE ROWNUM <= %s ) WHERE RN >= %s";

		if (moduleSql.toUpperCase().indexOf("SELECT") < 0) {
			moduleSql = "select * from " + moduleSql;
		}

		String sql = String.format(sqlFormat, moduleSql, limit, start);

		logger.info("QuerySql:" + sql);
		list = this.namedParameterJdbcTemplate.queryForList(sql, paramMap);
		logger.info("QuerySql结果数:" + list.size());
		return list;
	}

	@Override
	public PageBean queryforPageBean(String moduleSql, PageBean pageBean,
			Map<String, String> queryparam) {
		Map resMap = new HashMap();
		List<Map<String, Object>> list = null;
		// 获取共有属性
		String start = String.valueOf(pageBean.getStartRowNum());// 起始行数
		String limit = String.valueOf(pageBean.getEndRowNum());// 每页显示数
		
		String sortCol = pageBean.getSortCol() ;
		String sortType = pageBean.getSortType() ;
		
		//组装排序
		if(sortCol != null && !"".equals(sortCol)){
			if(sortType != null && !"".equals(sortType)){
				if(moduleSql.toUpperCase().indexOf("ORDER BY") < 0){
					String sort = pageBean.SQL_SORT_TYPE_ASC.equals(sortType) ? " ASC ":" DESC ";
					moduleSql = moduleSql + " ORDER BY "+ sortCol + sort;
				}
			}
		}

		// 获取分页结果
		String sqlFormat = "SELECT * FROM ( SELECT A.*, ROWNUM RN FROM (%s) A WHERE ROWNUM <= %s ) WHERE RN >= %s";

		if (moduleSql.toUpperCase().indexOf("SELECT") < 0) {
			moduleSql = "select * from " + moduleSql;
		}

		String sql = String.format(sqlFormat, moduleSql, limit, start);

		logger.info("QuerySql:" + sql);
		list = this.namedParameterJdbcTemplate.queryForList(sql, queryparam);
		list = this.pageDataConvert.formateData(list);
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
	
	public Integer getSeqNextVal(String seqName){
		Integer nextVal = null;
		String sql = "select "+seqName+".nextVal from dual";
		nextVal = this.jdbcTemplate.queryForInt(sql);
		return nextVal;
	}

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

}
