package com.roroclaw.base.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.roroclaw.base.utils.DateKit;

public class PageDataConvert {
	public static  List formateData(List<Map<String, Object>> list) {
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> elMap = list.get(i);
			for (String dataKey : elMap.keySet()) {
				Object obj = elMap.get(dataKey);
				if(obj instanceof Date){
					Date time = (Date) obj;
					elMap.put(dataKey, DateKit.stamp2Date(time.getTime(), "yyyy-MM-dd"));
				}
			}
		}

		return list;
	}
}
