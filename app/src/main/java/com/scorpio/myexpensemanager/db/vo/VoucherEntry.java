package com.scorpio.myexpensemanager.db.vo;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 *
 * @generated
 */

@Entity(foreignKeys = {
        @ForeignKey(entity = Ledger.class,
                parentColumns = "id",
                childColumns = "ledgerId"
        ),
        @ForeignKey(entity = Voucher.class,
                parentColumns = "id",
                childColumns = "voucherId",
                onDelete = ForeignKey.CASCADE)
},
        indices = {@Index("ledgerId"), @Index("voucherId")
        }
)
public class VoucherEntry extends BaseVO implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    public Long ledgerId;
    public Long voucherId;
    public Integer debitOrCredit;
    public Double amount;
    private String narration;
    public Long date;


    public VoucherEntry() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public Long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }

    public Integer getDebitOrCredit() {
        return debitOrCredit;
    }

    public void setDebitOrCredit(Integer debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {

        if (o != null && o instanceof VoucherEntry) {
            VoucherEntry ve = (VoucherEntry) o;
            if (this.ledgerId == ve.ledgerId && this.voucherId == ve.voucherId && this.amount ==
                    ve.amount) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (ledgerId != null) {
            dest.writeLong(ledgerId);
        }
        if (voucherId != null) {
            dest.writeLong(voucherId);
        }
        if (debitOrCredit != null) {
            dest.writeInt(debitOrCredit);
        }
        if (amount != null) {
            dest.writeDouble(amount);
        }
    }

    public static final Creator<VoucherEntry> CREATOR
            = new Creator<VoucherEntry>() {
        public VoucherEntry createFromParcel(Parcel in) {
            return new VoucherEntry(in);
        }

        public VoucherEntry[] newArray(int size) {
            return new VoucherEntry[size];
        }

    };

    private VoucherEntry(Parcel in) {
        ledgerId = in.readLong();
        voucherId = in.readLong();
        debitOrCredit = in.readInt();
        amount = in.readDouble();

    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }
}

