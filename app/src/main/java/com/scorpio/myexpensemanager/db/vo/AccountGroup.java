package com.scorpio.myexpensemanager.db.vo;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.db.converters.NatureOfGroupEnumConverter;

import java.io.Serializable;

@Entity
@TypeConverters(NatureOfGroupEnumConverter.class)
public class AccountGroup implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id = null;
    private String name = null;
    //    private Long parentId = null;
    private String parentName = null;
    private NatureOfGroup type = null;
    private Boolean predefined = null;
    private Boolean revenue = null;
    private Boolean deemedPositive = null;

    public AccountGroup() {
        super();
    }

    @Ignore
    public AccountGroup(Long id, String name, String parentName, NatureOfGroup type, Boolean
            predefined, Boolean deemedPositive) {
        this.id = id;
        this.name = name;
        this.parentName = parentName;
        this.type = type;
        this.predefined = predefined;
        this.deemedPositive = deemedPositive;
    }

    @Ignore
    public AccountGroup(String name, String parentName, NatureOfGroup type, Boolean predefined,
                        Boolean deemedPositive) {
        this.name = name;
        this.parentName = parentName;
        this.type = type;
        this.predefined = predefined;
        this.deemedPositive = deemedPositive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NatureOfGroup getType() {
        return type;
    }

    public void setType(NatureOfGroup type) {
        this.type = type;
    }

    public Boolean isPredefinied() {
        return predefined;
    }

    public void setPredefined(boolean predefined) {
        this.predefined = predefined;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Boolean isRevenue() {
        return revenue;
    }

    public void setRevenue(Boolean revenue) {
        this.revenue = revenue;
    }

    public Boolean isDeemedPositive() {
        return deemedPositive;
    }

    public void setDeemedPositive(boolean deemedPositive) {
        this.deemedPositive = deemedPositive;
    }

    public boolean isPrimary() {
        return parentName.equalsIgnoreCase(Constants.PRIMARY);
    }
}

