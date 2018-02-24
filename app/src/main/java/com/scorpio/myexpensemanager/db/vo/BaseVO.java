package com.scorpio.myexpensemanager.db.vo;

import java.io.Serializable;

/**
 * Created by hkundu on 2/19/2015.
 */
public class BaseVO implements Serializable {
    private boolean dirty = false;

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
