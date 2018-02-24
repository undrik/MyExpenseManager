package com.scorpio.myexpensemanager.db.converters;

import android.arch.persistence.room.TypeConverter;

import com.scorpio.myexpensemanager.db.vo.VoucherTypeEnum;

/**
 * Created by User on 24-02-2018.
 */

public class VoucherTypeEnumConverter {
    @TypeConverter
    public static Integer toOrdinal(VoucherTypeEnum voucherTypeEnum) {
        return voucherTypeEnum.ordinal();
    }

    @TypeConverter
    public static VoucherTypeEnum toVoucherType(Integer ordinal) {
        return VoucherTypeEnum.getEnum(ordinal);
    }
}
