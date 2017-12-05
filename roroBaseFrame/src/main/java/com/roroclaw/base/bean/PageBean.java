package com.roroclaw.base.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * 分页组装对象
 * 
 * @author dxz
 * 
 */
@XmlRootElement
public class PageBean {
	
	/**************** 分页常量 start******************/
	public static String SQL_PARAM_START = "start";
	public static String SQL_PARAM_LIMIT = "limit";
	public static String SQL_PARAM_DIRECTION = "dir";
	public static String SQL_PARAM_ORDERFIELD = "sort";

	public static int SQL_PARAM_LIMIT_DEFAULTVALUE = 10; // 默认当页数量
	public static String RESULT_MAP_DATA = "Rows";// 结果分页数据
	public static String RESULT_MAP_TOTALSIZE = "Total";// 结果总数�?
	public static String RESULT_MAP = "resultMap";
	public static String RESULT_LIST = "resultList";
	/**************** 分页常量 end******************/
	
	public static String SQL_SORT_TYPE_ASC = "up";
	public static String SQL_SORT_TYPE_DESC = "down";
	
	private int curPage = 1;
	private int totalRecords = 0;
	private int totalPage = 0;
	private int recordsPerpage = SQL_PARAM_LIMIT_DEFAULTVALUE;
	private int startRowNum = 0;
	private int endRowNum = recordsPerpage;
	private List data;
	private Map queryparam = new HashMap();
	private String sortCol; //排序�?
	private String sortType; //排序类型
	
	//jquery->datatable参数
//	private List aaData ;
//	private int sEcho = 1;
//	private int draw = 0;//datatable提供
//	private int iTotalRecords = 0;//datatable提供
//	private int iTotalDisplayRecords = 0;//datatable提供
	
	
	public PageBean(int curPage, int totalRecords, List data) {
		this.curPage = curPage;
		this.totalRecords = totalRecords;
		this.data = data;
	}
	
	public PageBean() {
		super();
	}

	
//	public int getiTotalRecords() {
//		return this.totalRecords;
//	}
//
//	public int getiTotalDisplayRecords() {
//		return this.totalRecords;
//	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
//		this.draw = curPage;
	}


//	public int getDraw() {
//		return draw;
//	}
//
//	public void setDraw(int draw) {
//		this.draw = draw;
//	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List getData() {
		return data;
	}
	
	public void setData(List data) {
		this.data = data;
	}

	public int getRecordsPerpage() {
		return recordsPerpage;
	}

	public void setRecordsPerpage(int recordsPerpage) {
		this.recordsPerpage = recordsPerpage;
	}

	public Map getQueryparam() {
		return queryparam;
	}

	public void setQueryparam(Map queryparam) {
		this.queryparam = queryparam;
	}

	public void addQueryparam(String key,String val) {
		this.queryparam.put(key, val);
	}

	public int getStartRowNum() {
		if(curPage > 1 && startRowNum == 0){ //不是第一�?计算�?��记录�?
			startRowNum = (curPage-1)*recordsPerpage;
		}
		return startRowNum;
	}

	public void setStartRowNum(int startRowNum) {
		this.startRowNum = startRowNum;
	}

	public int getEndRowNum() {
		
		endRowNum = curPage*recordsPerpage;
		return endRowNum;
	}

	public void setEndRowNum(int endRowNum) {
		this.endRowNum = endRowNum;
	}

	public int getTotalPage() {
		//计算页数
		totalPage =  (int) Math.ceil((double)totalRecords/recordsPerpage);
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public String getSortCol() {
		return sortCol;
	}

	public void setSortCol(String sortCol) {
		this.sortCol = sortCol;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

//	public int getsEcho() {
//		return sEcho;
//	}
//
//	public void setsEcho(int sEcho) {
//		this.sEcho = sEcho;
////		this.curPage = sEcho;
////		this.draw = sEcho;
//	}

//	public List getAaData() {
//		return data;
//	}
	
//	public static void main(String[] args) {
//		PageBean pageBean = new PageBean();
//		pageBean.setTotalRecords(101);
//		System.out.println(pageBean.getTotalPage());
//	}

	
}
