package com.spring.scheduler.admin.common.convert;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * ClassName: DateConvert 
 * @Description: 时间转换器
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月23日
 */
public class DateConvert implements Converter<String, Date> {
	private static final String FROMDATE[] = new String[] { "yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd","yyyy.MM.dd"};
	private static final String FROMTIME[] = new String[] { "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss","yyyy.MM.dd HH:mm:ss"};
	private static final String FROMDATETIME = "yyyyMMdd";
	private static final String FROM_DATE_YEAR = "yyyy";
    @Override
    public Date convert(String values) {
    	if (values == null) {
			return null;
		}
		Date date = null;
		String dateString = values;
		if (StringUtils.isNotEmpty(dateString)) {
			String fmt = FROMDATE[0];
			if (dateString.indexOf("-") != -1) {
				if (dateString.trim().length() > 12) {
					fmt=FROMTIME[1];
				}else{
					fmt=FROMDATE[1];
				}
			}else if(dateString.indexOf("/") != -1){
				if (dateString.trim().length() > 12) {
					fmt=FROMTIME[2];
				}else{
					fmt=FROMDATE[2];
				}
			}else if(dateString.indexOf(".") != -1){
				if (dateString.trim().length() > 12) {
					fmt=FROMTIME[3];
				}else{
					fmt=FROMDATE[3];
				}
			}else{
				if (dateString.trim().length() > 12) {
					fmt=FROMTIME[0];
				}else if(dateString.trim().length()==4){
					fmt=FROM_DATE_YEAR;
				}else{
					fmt=FROMDATE[0];
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			try {
				date = sdf.parse(dateString);
			} catch (ParseException e) {
			}
		}
		return date;
    }
}