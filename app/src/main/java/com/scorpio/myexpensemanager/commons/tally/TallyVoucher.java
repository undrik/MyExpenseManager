package com.scorpio.myexpensemanager.commons.tally;

import android.util.Log;
import android.util.Xml;

import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.Util;
import com.scorpio.myexpensemanager.db.vo.VoucherEntry;
import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TallyVoucher {

    private String fileName;
    private File file;

    public TallyVoucher(String path) {
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy_HHmmssSSS");
        String fName = format.format(Calendar.getInstance().getTime());
        String name = "V_" + fName + ".xml";
        this.fileName = path + "/" + name;
    }

    public void export(final List<VoucherWithEntries> voucherWithEntries) {

        try {
            file = new File(this.fileName);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");

            serializer.startTag(null, Constants.ENVELOPE);
            serializer.startTag(null, Constants.HEADER);
            serializer.startTag(null, Constants.TALLYREQUEST);
            serializer.text(Constants.REQUEST_IMPORT);
            serializer.endTag(null, Constants.TALLYREQUEST);
            serializer.startTag(null, Constants.TYPE);
            serializer.text(Constants.REQUEST_TYPE);
            serializer.endTag(null, Constants.TYPE);
            serializer.endTag(null, Constants.HEADER);
            serializer.startTag(null, Constants.BODY);
            serializer.startTag(null, Constants.DATA);
            serializer.startTag(null, Constants.TALLYMESSAGE);

//            VoucherTypeDao voucherTypeDao = new VoucherTypeDaoImpl(DbHelper
//                    .getCurComReadDbHandle());
//            LedgerDao ledgerDao = new LedgerDaoImpl(DbHelper.getCurComReadDbHandle());
            for (VoucherWithEntries voucher : voucherWithEntries) {
                serializer.startTag(null, Constants.VOUCHER);
                serializer.attribute(null, Constants.V_ATTR_REMOTEID, voucher.getGuid());
                serializer.startTag(null, Constants.V_DATE);
//                SimpleDateFormat dateFormat = new SimpleDateFormat(Constants
//                        .DATE_FORMAT_TALLY_YYYYMMDD, Locale.ENGLISH);
                serializer.text(Util.convertLocalDateToString(Constants
                        .DATE_FORMAT_TALLY_YYYYMMDD, voucher.getLocalDate()));
                serializer.endTag(null, Constants.V_DATE);
                serializer.startTag(null, Constants.GUID);
                serializer.text(voucher.getGuid());
                serializer.endTag(null, Constants.GUID);
                serializer.startTag(null, Constants.NARRATION);
                if (null != voucher.getNarration()) {
                    serializer.text(voucher.getNarration());
                }
                serializer.endTag(null, Constants.NARRATION);
                serializer.startTag(null, Constants.VOUCHERTYPENAME);
//                VoucherType voucherType = voucherTypeDao.getById(voucher.getTypeId());
//                if (null != voucherType) {
                serializer.text(voucher.getType());
//                }
                serializer.endTag(null, Constants.VOUCHERTYPENAME);

                for (VoucherEntry ve : voucher.getVoucherEntries()) {
                    if (Constants.DEBIT == ve.getDebitOrCredit()) {
                        serializer.startTag(null, Constants.ALLLEDGERENTRIES_LIST);
                        serializer.startTag(null, Constants.LEDGERNAME);
                        serializer.text(ve.getLedgerName());
                        serializer.endTag(null, Constants.LEDGERNAME);
                        serializer.startTag(null, Constants.ISDEEMEDPOSITIVE);
                        serializer.text(Constants.YES);
                        serializer.endTag(null, Constants.ISDEEMEDPOSITIVE);
                        serializer.startTag(null, Constants.AMOUNT);
                        serializer.text(ve.getAmount().toString());
                        serializer.endTag(null, Constants.AMOUNT);
                        serializer.endTag(null, Constants.ALLLEDGERENTRIES_LIST);
                    }
                }

                //the below code should be optimized
                for (VoucherEntry ve : voucher.getVoucherEntries()) {
                    if (Constants.CREDIT == ve.getDebitOrCredit()) {
                        serializer.startTag(null, Constants.ALLLEDGERENTRIES_LIST);
                        serializer.startTag(null, Constants.LEDGERNAME);
                        serializer.text(ve.getLedgerName());
                        serializer.endTag(null, Constants.LEDGERNAME);
                        serializer.startTag(null, Constants.ISDEEMEDPOSITIVE);
                        serializer.text(Constants.NO);
                        serializer.endTag(null, Constants.ISDEEMEDPOSITIVE);
                        serializer.startTag(null, Constants.AMOUNT);
                        serializer.text(ve.getAmount().toString());
                        serializer.endTag(null, Constants.AMOUNT);
                        serializer.endTag(null, Constants.ALLLEDGERENTRIES_LIST);
                    }
                }
                serializer.endTag(null, Constants.VOUCHER);
            }
            serializer.endTag(null, Constants.TALLYMESSAGE);
            serializer.endTag(null, Constants.DATA);
            serializer.endTag(null, Constants.BODY);
            serializer.endTag(null, Constants.ENVELOPE);
            serializer.flush();
            fos.close();
        } catch (IOException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
        }
    }
}
