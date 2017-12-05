package com.roroclaw.base.utils;

import au.com.bytecode.opencsv.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvUtil {

	public static List<Map> readCsv(String url) throws IOException {
		List<Map> dataList = new ArrayList<Map>();
		Map dataMap = new HashMap<String, String>();
		File file = new File(url);
		InputStreamReader isr = new InputStreamReader(
				new FileInputStream(file), "GBK");
		CSVReader csvReader = new CSVReader(isr);
		//获取全部内容
		List<String[]> list = csvReader.readAll();
		for (String[] rowStr : list) {
			Map<Integer, String> rowMap = new HashMap<Integer, String>();
			for (int i = 0; i < rowStr.length; i++) {
				String itemStr = rowStr[i];
				if (itemStr != null && !"".equals(itemStr)) {
					rowMap.put(i, itemStr);
				}
			}
			dataList.add(rowMap);
		}
		csvReader.close();
		return dataList;
	}
}
