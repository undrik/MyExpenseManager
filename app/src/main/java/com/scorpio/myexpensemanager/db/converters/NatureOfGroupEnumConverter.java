package com.scorpio.myexpensemanager.db.converters;

import android.arch.persistence.room.TypeConverter;

import com.scorpio.myexpensemanager.db.vo.NatureOfGroup;

/**
 * Created by User on 24-02-2018.
 */

public class NatureOfGroupEnumConverter {
    @TypeConverter
    public static NatureOfGroup toGroup(Integer ordinal) {
        return NatureOfGroup.values()[ordinal];
    }

    @TypeConverter
    public static Integer toOrdinal(NatureOfGroup natureOfGroup) {
        return natureOfGroup.ordinal();
    }

}
