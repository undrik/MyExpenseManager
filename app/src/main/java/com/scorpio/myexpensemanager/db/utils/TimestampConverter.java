package com.scorpio.myexpensemanager.db.utils;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by User on 06-02-2018.
 */

public class TimestampConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
