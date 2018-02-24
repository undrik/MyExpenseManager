package com.scorpio.myexpensemanager.db.vo;


/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 * @generated
 */

public class VoucherType
{
	public Long id;
	public String name;
	public Long currentVoucherNo;
    public String prefix;
    public String postfix;
    private VoucherTypeEnum type;

	public VoucherType(){
		super();
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

    public Long getCurrentVoucherNo() {
        return currentVoucherNo;
    }

    public void setCurrentVoucherNo(Long currentVoucherNo) {
        this.currentVoucherNo = currentVoucherNo;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public VoucherTypeEnum getType() {
        return type;
    }

    public void setType(VoucherTypeEnum type) {
        this.type = type;
    }
}

