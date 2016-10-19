package org.yctech.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtil {
	
	/**
	 * 四舍五入保留小数位
	 * @param value
	 * @param scale 小数位数
	 * @return
	 */
	public static BigDecimal setScale(BigDecimal value, int scale) {
		return value.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 根据小数位格式化为字符
	 * @param value
	 * @param scale 小数位数
	 * @return
	 */
	public static String format(BigDecimal value, int scale) {
		if (value == null) {
			return "";
		} else {
			return setScale(value, scale).toString();
		}
	}
	
	public static String formatDecimal(BigDecimal value){
		return formatDecimal(value, "#,###.##");
	}
	public static String formatDecimal(BigDecimal value, String pattern){
		DecimalFormat df = (DecimalFormat)DecimalFormat.getInstance();
		df.applyPattern(pattern);
		return df.format(value);
	}
	
	
	/**
	 * 把数值转化为百分比  
	 * 四舍五入保留两位小数
	 * @param value
	 * @return
	 */
	public static String getDoubleToPercent(double value){
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);//设置保留小数位
		nf.setRoundingMode(RoundingMode.HALF_UP); //设置舍入模式
		return nf.format(value);
	}
}
