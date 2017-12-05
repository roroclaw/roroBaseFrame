package com.roroclaw.base.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


/**
 * apache String 工具类的补充
 * 
 * @author dxz
 * 
 */
public class StringKit extends StringUtils {
	
	/**
	 * 将数组对象中值进行格式化
	 * @param array 数组对象, 
	 * @param format 格式化模板
	 * @return 格式化之后的数组对象
	 */
	public static String [] format(Object[] array, String format) {
		String[] resArray = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			String value = array[i].toString();
			resArray[i] = String.format(format, value);
		}
		return resArray;
	}

	public static String getListArr(List<Map> curList,String valKey){
		StringBuffer arr=new StringBuffer();
		for(int i=0;i<curList.size();i++){
			Map map= curList.get(i);
			if(i<curList.size()-1){
			arr.append("'"+map.get(valKey).toString()+"',");
			}else if(i==curList.size()-1){
				String str=map.get(valKey)!=null?map.get(valKey).toString():"";
				arr.append("'"+str+"'");
			}
			
		}
		
		return arr.toString();
		
	}
}
