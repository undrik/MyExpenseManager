package com.scorpio.myexpensemanager.commons;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * My Expense Manager Util class
 * Created by hkundu on 2/14/2015.
 */
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

    public static String convertToDDMMMYYYY(long timeInMils) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_D_MMM_YYYY,
                Locale.ENGLISH);
        return dateFormat.format(new Date(timeInMils));
    }

    public static Date convertToDateFromDDMMYYYY(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_D_MMM_YYYY, Locale.ENGLISH);

        try {
            return format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //if the parsing fail in that case return the current date
        return new Date();
    }

    public static long convertToTimeFromddMMMyyyy(String date) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_D_MMM_YYYY, Locale.ENGLISH);
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

//    public static String FormatDate(String format, long starDate, long endDate) {
//        return MessageFormat.format(format, new Date[]{new Date(starDate), new Date(endDate)});
//    }

    public static long convertToTimeFromddMMMyy(String date) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_D_MMM_YY, Locale.ENGLISH);
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
}
