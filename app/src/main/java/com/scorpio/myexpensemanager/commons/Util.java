package com.scorpio.myexpensemanager.commons;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * My Expense Manager Util class
 * Created by hkundu on 2/14/2015.
 */
@SuppressLint("NewApi")
public class Util {
    public static String convertAmount(Double amount) {
        Locale locale = new Locale("en", "IN");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        String pattern = Constants.RUPEE_SYMBOL + " ###,##,##,##,##0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        if (amount < 0) {
            amount = -1 * amount;
        }
        //return nf.format(amount);
        return decimalFormat.format(amount);
    }

    public static String convertAmountWithSign(Double amount) {
        String pattern;
        Locale locale = new Locale("en", "IN");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        if (amount < 0) {
            pattern = "(" + Constants.RUPEE_SYMBOL + " ###,##,##,##,##0.00" + ")";
        } else {
            pattern = Constants.RUPEE_SYMBOL + " ###,##,##,##,##0.00";
        }
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        //return nf.format(amount);
        return decimalFormat.format(amount);
    }

    public static String getToday() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_D_MMM_YYYY);

        return LocalDate.now().format(pattern);
    }

    public static String getTodayWithDay() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(Constants
                .DATE_FORMAT_WITH_SHORT_WEEK);
        return LocalDate.now().format(pattern);
    }

    public static LocalDate convertDateToLocalDate(final Date date) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        return instant.atZone(defaultZoneId).toLocalDate();
    }

    public static Date convertLocalDateToDate(final LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String convertToDDMMYYYEEE(LocalDate localDate) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(Constants
                .DATE_FORMAT_WITH_SHORT_WEEK);

        return localDate.format(pattern);
    }

    public static String convertToDDMMYYY(LocalDate localDate) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(Constants
                .DATE_FORMAT_D_MMM_YYYY);

        return localDate.format(pattern);
    }

    public static String convertLocalDateToString(final String pattern, final LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(formatter);
    }

    public static String convertToDDMMMYYYY(long timeInMils) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_D_MMM_YYYY,
                Locale.ENGLISH);
        return dateFormat.format(new Date(timeInMils));
    }

    public static Date convertToDateFromDDMMYYYY(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_D_MMM_YYYY, Locale
                .ENGLISH);

        try {
            return format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //if the parsing fail in that case return the current epochDay
        return new Date();
    }

    public static LocalDate convertToLocalDateFromDMMMYY(String strDate) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                // case insensitive to parse JAN and FEB
                .parseCaseInsensitive()
                // add pattern
                .appendPattern(Constants.DATE_FORMAT_D_MMM_YY)
                // create formatter (use English Locale to parse month names)
                .toFormatter(Locale.ENGLISH);
        return LocalDate.parse(strDate, formatter);
    }

    public static LocalDate convertToLocalDateFromPattern(String pattern, String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(strDate, formatter);
    }

    public static LocalDate convertToLocalDateFromPattern(DateTimeFormatter pattern, String
            strDate) {
        return LocalDate.parse(strDate, pattern);
    }

    public static LocalDate convertToLocalDateFromTimeInMills(String timeInMills) {
        return Instant.ofEpochMilli(Long.parseLong(timeInMills)).atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static long convertToTimeFromddMMMyyyy(String date) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_D_MMM_YYYY, Locale
                .ENGLISH);
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String FormatDate(String format, long timeInMils) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormat.format(new Date(timeInMils));
    }

    public static String FormatDate(String format, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public static String FormatDate(String format, Date startDate, Date endDate) {
        return MessageFormat.format(format, startDate, endDate);
    }

    public static long convertToTimeFromddMMMyy(String date) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_D_MMM_YY, Locale
                .ENGLISH);
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getMidnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static long getMidnight(long input) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(input);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static long getMidnightInEpoc(long input) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(input);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis() / 1000;
    }

    public static int validateName(@NonNull String name) {
        if (name.trim().isEmpty()) {
            return Constants.ERROR_CODE_EMPTY;
        }
        return Constants.SUCCESS_CODE;
    }

    public static int validateName(@NonNull String name, List<String> names) {
        if (name.trim().isEmpty()) {
            return Constants.ERROR_CODE_EMPTY;
        } else if (null != names && names.contains(name)) {
            return Constants.ERROR_CODE_EXISTS;
        }
        return Constants.SUCCESS_CODE;
    }

    public static void main(String[] args) {
        String strDate = "05-APR-18";
        System.out.println(Util.convertToTimeFromddMMMyy(strDate));
        System.out.println(Util.convertToLocalDateFromDMMMYY(strDate).toString());
        String receivedOn = "1522822629838";
        System.out.println("Local Date : " + Util.convertToLocalDateFromTimeInMills(receivedOn));
        String strDate1 = "07/04/18";

        System.out.println("Local date : " + Util.convertToLocalDateFromPattern("dd/MM/yy",
                strDate1));

        System.out.println(Util.FormatDate(Constants.DATE_FORMAT_RANGE, new Date(), new Date()));

    }
}
