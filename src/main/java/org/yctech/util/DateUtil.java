package org.yctech.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DateUtil {

	public static final String SIMPLE_PATTERN = "yyMMdd";
	public static final String PATTERN_DATE = "yyyy-MM-dd";
	public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

	private static SimpleDateFormat dateFormat = new SimpleDateFormat();

	/**
	 * 用默认格式格式化日期
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		dateFormat.applyPattern(PATTERN_DATE);
		return dateFormat.format(date);
	}

	public static String simpleFormat(Date date) {
		dateFormat.applyPattern(SIMPLE_PATTERN);
		return dateFormat.format(date);
	}
	
	public static String timeFormat(Date date) {
		dateFormat.applyPattern(PATTERN_DATE_TIME);
		return dateFormat.format(date);
	}

	/**
	 * 用指定格式格式化日期
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		dateFormat.applyPattern(pattern);
		return dateFormat.format(date);
	}

	/**
	 * 用默认格式解析日期
	 * 
	 * @param str
	 * @return 解析出错时返回null
	 */
	public static Date parse(String str) {
		Date date = null;
		dateFormat.applyPattern(PATTERN_DATE);
		dateFormat.setLenient(false);// 精确匹配，不支持2014-13-33
		try {
			date = dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 用指定格式解析日期
	 * 
	 * @param str
	 * @param pattern
	 * @return 解析出错时返回null
	 */
	public static Date parse(String str, String pattern) {
		Date date = null;
		dateFormat.applyPattern(pattern);
		dateFormat.setLenient(false);// 精确匹配，不支持2014-13-33
		try {
			date = dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 取得两个日期之间的天数
	 * 
	 * @param fDate
	 * @param tDate
	 * @return
	 */
	public static int getIntervalDays(Date fDate, Date tDate) {
		fDate = roundDate(fDate);
		tDate = roundDate(tDate);

		long intervalMilli = tDate.getTime() - fDate.getTime();
		return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	}

	/**
	 * 取得两个时间之间的分钟数
	 * 
	 * @param fDate
	 * @param tDate
	 * @return
	 */
	public static int getIntervalMinutes(Date fDate, Date tDate) {
		BigDecimal interval = new BigDecimal(tDate.getTime() - fDate.getTime());
		BigDecimal pmin = new BigDecimal(60 * 1000);

		BigDecimal min = interval.divide(pmin, 2, BigDecimal.ROUND_HALF_UP);
		return (new BigDecimal(Math.ceil(min.doubleValue()))).intValue();
	}

	/**
	 * 将日期时、分、秒清零
	 * 
	 * @param _d
	 * @return
	 */
	public static Date roundDate(Date _d) {
		Calendar c = Calendar.getInstance();
		c.setTime(_d);
		return roundCalendar(c).getTime();
	}

	/**
	 * 将日历时、分、秒清零
	 * 
	 * @param _c
	 * @return
	 */
	public static Calendar roundCalendar(Calendar _c) {
		_c.set(Calendar.HOUR_OF_DAY, 0);
		_c.set(Calendar.MINUTE, 0);
		_c.set(Calendar.SECOND, 0);
		_c.set(Calendar.MILLISECOND, 0);
		return _c;
	}

	/**
	 * 计算两个日期之间的时间间隔
	 * 
	 * @param startDate
	 *            起始时间
	 * @param endDate
	 *            截止时间
	 * @param flag
	 *            1：月；3：季度；6：半年
	 * @return
	 */
	public static int distanceForTwoDate(Date startDate, Date endDate, int flag) {
		if (startDate.after(endDate)) {
			return -1;
		}
		int distance = 0;
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int startYear = startCal.get(Calendar.YEAR);
		int startMonth = startCal.get(Calendar.MONTH) + 1;
		int endYear = endCal.get(Calendar.YEAR);
		int endMonth = endCal.get(Calendar.MONTH) + 1;
		if (endYear - startYear == 0) {
			distance = (endMonth - 1) / flag - (startMonth - 1) / flag + 1;
		} else if (endYear - startYear > 0) {
			distance = 11 / flag - (startMonth - 1) / flag + 1;
			while (endYear - startYear > 1) {
				distance += 12 / flag;
				endYear--;
			}
			distance += (endMonth - 1) / flag + 1;
		}
		return distance;
	}

	/**
	 * 获取日期对应的年
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 获取车龄货这驾龄 前十年所有的年份
	 * 
	 * @param date
	 * @return
	 */
	public static List<Integer> getCarYearList() {
		List<Integer> carYearList = new ArrayList<Integer>();
		int currentYear = getYear(new Date());
		for (int i = currentYear - 9; i <= currentYear; i++) {
			carYearList.add(i);
		}

		return carYearList;
	}

	/**
	 * 获取日期对应的月
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取日期对应的日
	 * 
	 * @param date
	 * @return
	 */
	public static int getDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DATE);
	}

	/**
	 * 将日期增加若干天
	 * 
	 * @param _date
	 *            日期
	 * @param _dayNum
	 *            天数
	 * @return
	 */
	public static Date addDays(Date _date, int _dayNum) {
		Calendar c = Calendar.getInstance();
		c.setTime(_date);
		c.add(Calendar.DATE, _dayNum);
		return c.getTime();
	}

	/**
	 * String类型日期 加减若干天 返回String日期 先将String日期转换Date日期 再调用addDays方法将日期增加或减若干天
	 * 最后转换String日期结果
	 * 
	 * @param _date
	 *            String类型日期
	 * @param _dayNum
	 *            天数
	 * @return
	 */
	public static String addDays(String _date, int _dayNum) {
		return format(addDays(parse(_date), _dayNum));
	}

	/**
	 * 取得本月的月初日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthStartDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * 取得本月的月末日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthEndDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * 根据传入的年份和月份获得这个月的最大天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMaxDaybyYearAndMonth(int year, int month) {
		int days[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (2 == month && 0 == (year % 4)
				&& (0 != (year % 100) || 0 == (year % 400))) {
			days[1] = 29;
		}
		return (days[month - 1]);
	}

	/**
	 * 根据输入的数字月份返回对应的英文月份
	 * 
	 * @param str
	 *            输入的数字月份
	 * @return
	 */
	public static String formatMonth(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		sdf = new SimpleDateFormat("MMMMM", Locale.US);
		return sdf.format(date);
	}

	/**
	 * 根据输入的数字月份返回对应的英文月份
	 * 
	 * @param month
	 *            输入的数字月份
	 * @return
	 */
	public static String formatMonth(Integer month) {
		if (month == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		Date date = null;
		try {
			date = sdf.parse(String.valueOf(month));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		sdf = new SimpleDateFormat("MMMMM", Locale.US);
		return sdf.format(date);
	}

	/**
	 * 获取月份对应第几季度
	 * 
	 * @param month
	 * @return
	 */
	public static Integer getQuarterByMonth(Integer month) {
		Integer quarter = 1;
		switch (month) {
		case 1:
		case 2:
		case 3:
			quarter = 1;
			break;
		case 4:
		case 5:
		case 6:
			quarter = 2;
			break;
		case 7:
		case 8:
		case 9:
			quarter = 3;
			break;
		case 10:
		case 11:
		case 12:
			quarter = 4;
			break;
		}
		return quarter;
	}
	
	/**
	 * 获取两个月份之间的月份集合 不允许跨年
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	public static List<Integer> getMonthList(Integer startMonth,Integer endMonth){
		List<Integer> monthList = new ArrayList<>();
		if(endMonth==null || startMonth==null || endMonth>12 || endMonth<1 || 
				startMonth>12 || startMonth<1 || endMonth<startMonth){
			return monthList;
		}
		for(int i=startMonth;i<=endMonth;i++){
			monthList.add(i);
		}
		return monthList;
	} 
	
	/**
	 * 获取两个月份之间对应的季度集合
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	public static Set<Integer> getQuarterByMonth(Integer startMonth,Integer endMonth){
		Set<Integer> set = new HashSet<>();
		List<Integer> monthList = getMonthList(startMonth,endMonth);
		for(Integer month : monthList){
			set.add(getQuarterByMonth(month));
		}
		return set;
	}
	
	/**
	 * 根据季度获取本季度的第一天
	 * @param quarter
	 * @return
	 * 		Format 01-01
	 */
	public static String getDateByQuarter(Integer quarter){
		String result = "";
		switch (quarter) {
		case 1:
			result = "01-01 00:00:00";
			break;
		case 2:
			result = "04-01 00:00:00";
			break;
		case 3:
			result = "07-01 00:00:00";
			break;
		case 4:
			result = "12-01 00:00:00";
			break;
		default:
			break;
		}
		return result;
	}
}
