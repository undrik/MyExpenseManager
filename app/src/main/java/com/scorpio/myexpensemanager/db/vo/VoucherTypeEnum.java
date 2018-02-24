package com.scorpio.myexpensemanager.db.vo;

/**
 * Created by hkundu on 1/16/2015.
 */
public enum VoucherTypeEnum {
    Payment(1), Receipt(2), Contra(3), Journal(4), Purchase(5), Sales(6);
    private Integer vchTypeId;

    VoucherTypeEnum(int vchTypeId) {
        this.vchTypeId = vchTypeId;
    }

    Integer getIntValue() {
        return this.vchTypeId;
    }

    public static VoucherTypeEnum getEnum(int val) {
        for (VoucherTypeEnum typeEnum : VoucherTypeEnum.values()) {
            if (typeEnum.vchTypeId == val) {
                return typeEnum;
            }
        }
        return null;
    }
}
