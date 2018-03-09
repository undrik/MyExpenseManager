package com.scorpio.myexpensemanager.commons;

import com.scorpio.myexpensemanager.db.vo.AccountGroup;
import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.scorpio.myexpensemanager.db.vo.NatureOfGroup;
import com.scorpio.myexpensemanager.db.vo.VoucherType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class contains all the default groups and ledgers
 * Created by User on 24-02-2018.
 */

public class PopulateDefaults {
    public static List<AccountGroup> predefinedGroups() {
        List<AccountGroup> predefinedGroups = new ArrayList<>();

        //String id = UUID.randomUUID().toString();
        predefinedGroups.add(new AccountGroup(1L, Constants.CAPITAL_ACCOUNT, Constants.PRIMARY,
                NatureOfGroup.LIABILITIES, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(2L, Constants.RESERVES_SURPLUS, Constants
                .CAPITAL_ACCOUNT, NatureOfGroup.LIABILITIES, Constants.PREDEFINED_TRUE,
                Constants.ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(3L, Constants.CURRENT_ASSETS, Constants.PRIMARY,
                NatureOfGroup.ASSETS, Constants.PREDEFINED_TRUE, Constants.ISDEEMEDPOSITIVE_TRUE));
        predefinedGroups.add(new AccountGroup(4L, Constants.BANK_ACCOUNTS, Constants
                .CURRENT_ASSETS, NatureOfGroup.ASSETS, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_TRUE));
        predefinedGroups.add(new AccountGroup(5L, Constants.CASH_IN_HAND, Constants
                .CURRENT_ASSETS, NatureOfGroup.ASSETS, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_TRUE));
        predefinedGroups.add(new AccountGroup(6L, Constants.DEPOSITS_ASSET, Constants
                .CURRENT_ASSETS, NatureOfGroup.ASSETS, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_TRUE));
        predefinedGroups.add(new AccountGroup(7L, Constants.LOANS_ADVANCES_ASSET, Constants
                .CURRENT_ASSETS, NatureOfGroup.ASSETS, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_TRUE));
        predefinedGroups.add(new AccountGroup(8L, Constants.STOCK_IN_HAND, Constants
                .CURRENT_ASSETS, NatureOfGroup.ASSETS, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_TRUE));
        predefinedGroups.add(new AccountGroup(9L, Constants.SUNDRY_DEBTORS, Constants
                .CURRENT_ASSETS, NatureOfGroup.ASSETS, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_TRUE));
        predefinedGroups.add(new AccountGroup(10L, Constants.CURRENT_LIABILITIES, Constants.PRIMARY,
                NatureOfGroup.LIABILITIES, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(11L, Constants.DUTIES_TAXES, Constants
                .CURRENT_LIABILITIES, NatureOfGroup.LIABILITIES, Constants.PREDEFINED_TRUE,
                Constants.ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(12L, Constants.PROVISIONS, Constants
                .CURRENT_LIABILITIES, NatureOfGroup.LIABILITIES, Constants.PREDEFINED_TRUE,
                Constants.ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(13L, Constants.SUNDRY_CREDITORS, Constants
                .CURRENT_LIABILITIES, NatureOfGroup.LIABILITIES, Constants.PREDEFINED_TRUE,
                Constants.ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(14L, Constants.DIRECT_EXPENSES, Constants.PRIMARY,
                NatureOfGroup.EXPENSES, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_TRUE));
        predefinedGroups.add(new AccountGroup(15L, Constants.INDIRECT_EXPENSES, Constants.PRIMARY,
                NatureOfGroup.EXPENSES, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_TRUE));
        predefinedGroups.add(new AccountGroup(16L, Constants.DIRECT_INCOMES, Constants.PRIMARY,
                NatureOfGroup.INCOME, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(17L, Constants.INDIRECT_INCOMES, Constants.PRIMARY,
                NatureOfGroup.INCOME, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(18L, Constants.INVESTMENTS, Constants.PRIMARY,
                NatureOfGroup.ASSETS, Constants.PREDEFINED_TRUE, Constants.ISDEEMEDPOSITIVE_TRUE));

        predefinedGroups.add(new AccountGroup(19L, Constants.LOANS_LIABILITY, Constants.PRIMARY,
                NatureOfGroup
                        .LIABILITIES, Constants.PREDEFINED_TRUE, Constants.ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(20L, Constants.BANK_OD_ACCOUNT, Constants
                .LOANS_LIABILITY, NatureOfGroup.LIABILITIES, Constants.PREDEFINED_TRUE,
                Constants.ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(21L, Constants.SECURED_LOANS, Constants
                .LOANS_LIABILITY, NatureOfGroup.LIABILITIES, Constants.PREDEFINED_TRUE,
                Constants.ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(22L, Constants.UNSECURED_LOANS, Constants
                .LOANS_LIABILITY, NatureOfGroup.LIABILITIES, Constants.PREDEFINED_TRUE,
                Constants.ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(23L, Constants.MISC_EXPENSES_ASSET, Constants.PRIMARY,
                NatureOfGroup.ASSETS, Constants.PREDEFINED_TRUE, Constants.ISDEEMEDPOSITIVE_TRUE));
        predefinedGroups.add(new AccountGroup(24L, Constants.PURCHASE_ACCOUNTS, Constants.PRIMARY,
                NatureOfGroup.EXPENSES, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_TRUE));
        predefinedGroups.add(new AccountGroup(25L, Constants.SALES_ACCOUNTS, Constants.PRIMARY,
                NatureOfGroup.INCOME, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(26L, Constants.CREDIT_CARD, Constants.PRIMARY,
                NatureOfGroup.LIABILITIES, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_FALSE));
        predefinedGroups.add(new AccountGroup(27L, Constants.FIXED_ASSETS, Constants.PRIMARY,
                NatureOfGroup.ASSETS, Constants.PREDEFINED_TRUE, Constants.ISDEEMEDPOSITIVE_TRUE));
        predefinedGroups.add(new AccountGroup(28L, Constants.SUSPENSE_ACCOUNT, Constants.PRIMARY,
                NatureOfGroup.LIABILITIES, Constants.PREDEFINED_TRUE, Constants
                .ISDEEMEDPOSITIVE_FALSE));

        return predefinedGroups;
    }

    public static List<Ledger> predefiniedLedgers() {
        List<Ledger> ledgers = new ArrayList<>();
        ledgers.add(new Ledger(Constants.CASH_IN_WALLET, Constants.CASH_IN_HAND, 0.0, Calendar
                .getInstance().getTimeInMillis()));
        return ledgers;
    }

    public static List<VoucherType> predefiniedVoucherTypes() {
        List<VoucherType> voucherTypes = new ArrayList<>();

        voucherTypes.add(new VoucherType(Constants.PAYMENT, 1L));
        voucherTypes.add(new VoucherType(Constants.RECEIPT, 1L));
        voucherTypes.add(new VoucherType(Constants.CONTRA, 1L));
        voucherTypes.add(new VoucherType(Constants.JOURNAL, 1L));
        voucherTypes.add(new VoucherType(Constants.PURCHASE, 1L));
        voucherTypes.add(new VoucherType(Constants.SALES, 1L));

        return voucherTypes;
    }
}