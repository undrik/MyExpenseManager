package com.scorpio.myexpensemanager.commons;

import android.support.annotation.NonNull;

import com.scorpio.myexpensemanager.db.vo.Company;

/**
 * Created by User on 24-02-2018.
 */

public class Cache {

    private static Cache CACHE = null;

    private static Company companyObj = null;

    private Cache() {
    }

    public static Cache getInstance() {
        if (null == CACHE) {
            CACHE = new Cache();
        }
        return CACHE;
    }

    public static void setCompany(@NonNull final Company company) {
        companyObj = company;
    }

    public static Company getCompany() {
        return companyObj;
    }
}
