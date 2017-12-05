package com.roroclaw.base.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateKit {
	public static String stamp2Date(Long timestamp, String formate) {
		SimpleDateFormat sdf = new SimpleDateFormat(formate);
		String sd = sdf.format(new Date(timestamp));
		return sd;
	}

	public static String stamp2Date(Timestamp timestamp, String formate) {
		String tsStr = "";
		DateFormat sdf = new SimpleDateFormat(formate);
		try {
			tsStr = sdf.format(timestamp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}

	/***
	 * 得到年
	 * 
	 * @return
	 */
	public static String getYear() {
		Calendar cal = Calendar.getInstance();// 使用日历类
		int year = cal.get(Calendar.YEAR);// 得到年

		return String.valueOf(year);

	}

	/**
	 * @see 取得指定时间的给定格式()
	 * @return String
	 * @throws ParseException
	 */
	public static String SetDateFormat(Date myDate, String strFormat) {

		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		String sDate = sdf.format(myDate);
		System.err.println(sDate);
		return sDate;
	}

}
