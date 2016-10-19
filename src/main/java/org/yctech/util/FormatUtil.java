package org.yctech.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author jack
 *
 */
public class FormatUtil {
    public static DecimalFormat df=new DecimalFormat("#,###.00");
    /**
     * ��BigDecimal��ʽ��Ϊ#,###.00���
     * @param n
     * @return
     */
    public static String formatCurrency(BigDecimal n){
        return df.format(n.doubleValue());
    }

    public static String formatInteger(int num, int length) {
        String number = (new Integer(num)).toString();
        while (number.length() < length) {
            number = "0" + number;
        }
        return number;
    }

    public static String formatLong(long num, int length) {
        String number = (new Long(num)).toString();
        while (number.length() < length) {
            number = "0" + number;
        }
        return number;
    }
    public static String formatDate(Date date) {
        return formatDate(date,"yyyy-MM-dd");
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat dateTimeFormat =
                (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        dateTimeFormat.applyPattern(pattern);
        return dateTimeFormat.format(date);
    }

    public static String[] getFormatedIncreasedString(String firstString, int number) {
        if (firstString == null) {
            return null;
        }
        Pattern p = Pattern.compile("(0*[0-9]+)$");
        Matcher m = p.matcher(firstString);


        if (m.find()) {

            int dIndex = m.start();
            String prefix = firstString.substring(0, dIndex);
            String suffix = firstString.substring(dIndex);
            int index = suffix.length();
            if(suffix.endsWith("0")){
                index = suffix.length()-1;
            }

            if (suffix.lastIndexOf("0",index) > 0) {
                prefix += suffix.substring(0, suffix.lastIndexOf("0",index));
                suffix = suffix.substring(suffix.lastIndexOf("0",index));
            }

            int suffixLenth = suffix.length();
            String increased = suffix;


            long in = Long.parseLong(increased);
            String[] value = new String[number];
            value[0] = firstString;
            for (int i = 1; i < number; i++) {
                String last = Long.toString(++in);
                if (last.length() < suffixLenth) {
                    last = formatLong(in, suffixLenth);
                }
                value[i] = prefix + last;

            }
            return value;
        }
        return null;
    }
}
