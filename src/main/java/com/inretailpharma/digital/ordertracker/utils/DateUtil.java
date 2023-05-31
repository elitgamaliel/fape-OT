package com.inretailpharma.digital.ordertracker.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.lang.Strings;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DateUtil extends org.apache.commons.lang3.time.DateUtils {

    private static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
    private static final String ISO_TIME_FORMAT = "HH:mm:ss";
    private static final String ISO_SHORT_DATE_TIME_FORMAT = "yyMMddhhmmss";
    private static final String DATE_PATTERN_WITH_FINAL_CERO = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.0";
   
    private DateUtil() {
    }

    public static Date currentDate() {
        return new Date(System.currentTimeMillis());
    }

    public static Long currentDateLong() {
        return System.currentTimeMillis();
    }

    public static Long timeBetween(Date startDate, Date endDate, DateUtil.TimeUnits units) {
        long difference = startDate.getTime() - endDate.getTime();
        switch (units) {
            case DAYS:
                return TimeUnit.MILLISECONDS.toDays(difference);
            case HOURS:
                return TimeUnit.MILLISECONDS.toHours(difference);
            case MINUTES:
                return TimeUnit.MILLISECONDS.toMinutes(difference);
            case SECONDS:
                return TimeUnit.MILLISECONDS.toSeconds(difference);
            default:
                return difference;
        }
    }

    public enum TimeUnits {
        DAYS,
        HOURS,
        MINUTES,
        SECONDS;

        private TimeUnits() {
        }
    }

    public static String getDateTimeFormatted(LocalDateTime dateTime, String pattern) {
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return Strings.replace(
                    Strings.replace(dateTime.format(formatter), "AM", "a.m."),
                    "PM", "p.m.");
        }
        return null;
    }

    public static String getDateTimeFormatted(LocalDateTime date) {
        return getDateTimeFormatted(date, ISO_DATE_TIME_FORMAT);
    }
    
    public static String getDateTimeFormatted(Long date) {
    	if (date != null) {
    		return getDateTimeFormatted(getDateTimeFromMillis(date), ISO_DATE_TIME_FORMAT);
    	}
        return null;
    }

    public static String getDateFormatted(LocalDate date, String pattern) {
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return date.format(formatter);
        }
        return null;
    }

    public static String getLocalDateTimeWithFormat(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(ISO_DATE_TIME_FORMAT));
    }

    public static String getDateFormatted(LocalDate date) {
        return getDateFormatted(date, ISO_DATE_FORMAT);
    }

    public static String getTimeFormatted(LocalTime time, String pattern) {
        if (time != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return time.format(formatter);
        }
        return null;
    }

    public static String getTimeFormatted(LocalTime time) {
        return getTimeFormatted(time, ISO_TIME_FORMAT);
    }
    
    public static LocalDateTime getDateTimeFromMillis(Long value) {
    	return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), TimeZone.getDefault().toZoneId());
    }

    public static String convertStringDateToIsoFormatDate(String dateString) throws ParseException {
        String newDateString=null;
        try {
        	if(StringUtils.isEmpty(dateString)) return dateString;       	
        	String dateFormat= dateString.matches(DATE_PATTERN_WITH_FINAL_CERO) ? ISO_DATE_TIME_FORMAT : ISO_SHORT_DATE_TIME_FORMAT;
        	DateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = sdf.parse(dateString);
            newDateString= new SimpleDateFormat(ISO_DATE_TIME_FORMAT).format(date);
        } catch (ParseException e) {
            log.error("convertStringDateToIsoFormatDate {} ", e.getMessage());
        }
        return newDateString;
    }
    
    public static Long convertDatetimeToMilliseconds(LocalDateTime datetime) {
        if (datetime != null) {
        	ZonedDateTime zdt = ZonedDateTime.of(datetime, ZoneId.systemDefault());
        	return zdt.toInstant().toEpochMilli();
        }
        return null;
    }
}
