package com.scorpio.myexpensemanager.db.vo;


import com.myexpense.common.Constants;

import java.io.Serializable;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 *
 * @generated
 */

public class AccountGroup implements Serializable {

    private Long id = null;
    private String name = null;
    //    private Long parentId = null;
    private String parentName = null;
    private NatureOfGroup type = null;
    private Boolean predefinied = null;
    private Boolean revenue = null;
    private Boolean deemedPositive = null;

    public AccountGroup() {
        super();
    }

    public AccountGroup(String name, String parentName, NatureOfGroup type, Boolean predefinied,
                        Boolean deemedPositive) {
        this.name = name;
        this.parentName = parentName;
        this.type = type;
        this.predefinied = predefinied;
        this.deemedPositive = deemedPositive;
    }

    public AccountGroup(String name) {
        this.name = name;
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
        return predefinied;
    }

    public void setPredefinied(boolean predefinied) {
        this.predefinied = predefinied;
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
    public boolean isPrimary(){
        return parentName.equalsIgnoreCase(Constants.PRIMARY);
    }
}

