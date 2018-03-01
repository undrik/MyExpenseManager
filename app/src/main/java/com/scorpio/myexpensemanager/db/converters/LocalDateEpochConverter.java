package com.scorpio.myexpensemanager.db.converters;

import android.annotation.SuppressLint;
import android.arch.persistence.room.TypeConverter;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by User on 06-02-2018.
 */
@SuppressLint("NewApi")
public class LocalDateEpochConverter {
    @TypeConverter
    public static LocalDate fromEpochToLocalDate(Long epochDay) {
        return (epochDay == null) ? null : LocalDate.ofEpochDay(epochDay);
    }

    @TypeConverter
    public static Long localDateToEpoch(LocalDate localDate) {
        return localDate == null ? null : localDate.toEpochDay();
    }
}
