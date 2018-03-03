package com.scorpio.myexpensemanager.commons.tally;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Xml;

import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.db.vo.AccountGroup;
import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.scorpio.myexpensemanager.db.vo.NatureOfGroup;
import com.scorpio.myexpensemanager.db.vo.Voucher;
import com.scorpio.myexpensemanager.db.vo.VoucherEntry;
import com.scorpio.myexpensemanager.db.vo.VoucherTypeEnum;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains the code to handle the Tally file
 * Created by hkundu on 01-03-2018.
 */

public class TallyFileHandler {
    //we don't user namespaces
    private static final String ns = null;
    private File file;
    private List<AccountGroup> accountGroups = new ArrayList<>();
    private List<Ledger> ledgers = new ArrayList<>();
    private Map<String, List<Ledger>> groupLedgerMap = new HashMap<>();

    private List<Voucher> vouchers = new ArrayList<>();
    private InputStream in = null;

    public TallyFileHandler() {
    }

    public TallyFileHandler(@NonNull File file) {
        this.file = file;
        try {
            this.in = new FileInputStream(this.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public TallyFileHandler(@NonNull String fileName) {
        this.file = new File(fileName);
        try {
            this.in = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public TallyFileHandler(@NonNull InputStream fileInputStream) {
        this.in = (FileInputStream) fileInputStream;
    }

    public void parse() throws IOException, XmlPullParserException {
        if (null == in) {
            return;
        }
        XmlPullParser parser = Xml.newPullParser();

//        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();

        readMessages(parser);
    }

    public List<AccountGroup> getAccountGroups() {
        return accountGroups;
    }

    public Map<String, List<Ledger>> getGroupLedgerMap() {
        return groupLedgerMap;
    }

    public List<Ledger> getLedgers() {
        return ledgers;
    }

    private void readMessages(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "ENVELOPE");
        while (XmlPullParser.END_DOCUMENT != parser.next()) {
            if (XmlPullParser.START_TAG != parser.getEventType()) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for TALLYMESSAGE tag
            if (name.equals("TALLYMESSAGE")) {
                readMessage(parser);
            } else {
                //  skip(parser);
            }
        }
    }


    private void readMessage(XmlPullParser parser) throws IOException,
            XmlPullParserException {
//        TallyMessage message = null;

        parser.require(XmlPullParser.START_TAG, ns, "TALLYMESSAGE");
        while (XmlPullParser.END_TAG != parser.next()) {
            if (XmlPullParser.START_TAG != parser.getEventType()) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for TALLYMESSAGE tag
            if (name.equals("GROUP")) {
                AccountGroup group = readGroup(parser);
                if (null != group) {
                    accountGroups.add(group);
                }
            } else if (name.equals("LEDGER")) {
                Ledger ledger = readLedger(parser);
                if (null != ledger) {
                    ledgers.add(ledger);
                }
            } else if (name.equals(Constants.VOUCHER)) {
                Voucher voucher = readVoucher(parser);
                if (null != voucher) {
                    vouchers.add(voucher);
                }
            } else {
                // skip(parser);
            }

        }
    }


    private AccountGroup readGroup(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        AccountGroup group = null;
        String groupName = null;
        String groupParent = null;
        boolean isRevenue = false;
        boolean isDeemedPositive = false;

        parser.require(XmlPullParser.START_TAG, ns, "GROUP");
        groupName = parser.getAttributeValue(null, "NAME");
        //int eventType = parser.next();
        //while (parser.next() != XmlPullParser.END_TAG && !parser.getName().equals("GROUP")) {
        //parser.next();
        boolean done = false;
        while (!done) {
            String name = parser.getName();
            if (parser.getEventType() == XmlPullParser.END_TAG && name.equals("GROUP")) {
                done = true;
            }
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                parser.next();
                continue;
            }
            if (name.equals("PARENT")) {
                //groupParent = readGroupParent(parser);
                groupParent = readText(parser);
            } else if (name.equals("ISREVENUE")) {
                //skip(parser);
                String text = readText(parser);
                switch (text) {
                    case "Yes":
                        isRevenue = true;
                        break;
                    case "No":
                        isRevenue = false;
                        break;
                }
            } else if (name.equals("ISDEEMEDPOSITIVE")) {
                String text = readText(parser);
                switch (text) {
                    case "Yes":
                        isDeemedPositive = true;
                        break;
                    case "No":
                        isDeemedPositive = false;
                        break;
                }
            }
            parser.next();
        }

        if (null != groupName) {
            group = new AccountGroup();
            group.setName(groupName);
            if (null == groupParent) {
                group.setParentName(Constants.PRIMARY);
            } else {
                group.setParentName(groupParent);
            }
            if (isRevenue) {
                if (isDeemedPositive) {
                    group.setType(NatureOfGroup.EXPENSES);
                } else {
                    group.setType(NatureOfGroup.INCOME);
                }
            } else {
                if (isDeemedPositive) {
                    group.setType(NatureOfGroup.ASSETS);
                } else {
                    group.setType(NatureOfGroup.LIABILITIES);
                }
            }
            group.setDeemedPositive(isDeemedPositive);
            group.setPredefined(false);
        }
        return group;
    }

    private String readGroupParent(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "PARENT");
        String parent = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "PARENT");
        return parent;
    }

    private Ledger readLedger(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        Ledger ledger = null;
        String ledgerName = null;
        String ledgerParent = null;
        double openingBalance = 0.0;

        parser.require(XmlPullParser.START_TAG, ns, "LEDGER");
        ledgerName = parser.getAttributeValue(null, "NAME");
        //String name = parser.getName();
        boolean done = false;
        while (!done) {
            String name = parser.getName();
            if (parser.getEventType() == XmlPullParser.END_TAG && name.equals("LEDGER")) {
                done = true;
            }
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                parser.next();
                continue;
            }
            if (name.equals("PARENT")) {
                ledgerParent = readGroupParent(parser);

            } else if (name.equals("OPENINGBALANCE")) {
                //skip(parser);
                openingBalance = readOpeningBalance(parser);
            }
            parser.next();
        }

        if (null != ledgerName && null != ledgerParent) {
            ledger = new Ledger();
            ledger.setName(ledgerName);
            ledger.setGroupName(ledgerParent);
            ledger.setOpeningBalance(openingBalance);
            ledger.setCurrentBalance(openingBalance);
            ledger.setActive(true);

            if (null == groupLedgerMap.get(ledgerParent)) {
                groupLedgerMap.put(ledgerParent, new ArrayList<>());
            }
            groupLedgerMap.get(ledgerParent).add(ledger);

            return ledger;
        }
        return ledger;
    }

    private double readOpeningBalance(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "OPENINGBALANCE");
        String value = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "OPENINGBALANCE");
        return (null != value ? Double.parseDouble(value) : 0.0);
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private VoucherEntry readLedgerEntry(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        VoucherEntry ve = null;
        String ledgerName = null;
        Boolean deemedPositive = null;
        String amount = null;

        parser.require(XmlPullParser.START_TAG, ns, Constants.ALLLEDGERENTRIES_LIST);
        boolean done = false;
        while (!done) {
            String name = parser.getName();
            if (parser.getEventType() == XmlPullParser.END_TAG && name.equals(Constants
                    .ALLLEDGERENTRIES_LIST)) {
                done = true;
                continue;
            }
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                parser.next();
                continue;
            }
            if (name.equals(Constants.LEDGERNAME)) {
                ledgerName = readText(parser);

            } else if (name.equals(Constants.ISDEEMEDPOSITIVE)) {
                String text = readText(parser);
                switch (text) {
                    case "Yes":
                        deemedPositive = true;
                        break;
                    case "No":
                        deemedPositive = false;
                        break;
                }
            } else if (name.equals(Constants.AMOUNT)) {
                amount = readText(parser);
            }
            parser.next();
        }

        if (null != ledgerName && null != deemedPositive && null != amount) {

            ve = new VoucherEntry(ledgerName, ((deemedPositive == true) ? Constants.DEBIT :
                    Constants.CREDIT), Double.valueOf(amount));

        } else {
            Log.e(Constants.APP_NAME, ledgerName + " --> does not exits. Please create/import" +
                    " Account");
        }

        return ve;
    }

    @SuppressLint("NewApi")
    private Voucher readVoucher(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        Voucher voucher = null;
        String vDate = null;
        String vGuid = null;
        String vType = null;
        String vNarration = null;


        parser.require(XmlPullParser.START_TAG, ns, Constants.VOUCHER);
        boolean done = false;
        while (!done) {
            String name = parser.getName();
            if (parser.getEventType() == XmlPullParser.END_TAG && name.equals(Constants.VOUCHER)) {
                done = true;
                continue;
            }
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                parser.next();
                continue;
            }
            if (name.equals(Constants.V_DATE)) {
                vDate = readText(parser);
            } else if (name.equals(Constants.VOUCHERTYPENAME)) {
                vType = readText(parser);
            } else if (name.equals(Constants.GUID)) {
                vGuid = readText(parser);
            } else if (name.equals(Constants.NARRATION)) {
                vNarration = readText(parser);
            } else if (name.equals(Constants.ALLLEDGERENTRIES_LIST)) {
                VoucherEntry ve = readLedgerEntry(parser);
                if (null != voucher && null != ve) {
                    voucher.getVoucherEntryList().add(ve);
                }
            }
            if (null == voucher && null != vDate && null != vType) {
                voucher = new Voucher();
                DateTimeFormatter pattern = DateTimeFormatter.ofPattern(Constants
                        .DATE_FORMAT_TALLY_YYYYMMDD);
                try {
                    voucher.setLocalDate(LocalDate.parse(vDate, pattern));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (vType) {
                    case "Payment":
                        voucher.setTypeId((long) VoucherTypeEnum.Payment.ordinal() + 1);
                        break;
                    case "Receipt":
                        voucher.setTypeId((long) VoucherTypeEnum.Receipt.ordinal() + 1);
                        break;
                    case "Journal":
                        voucher.setTypeId((long) VoucherTypeEnum.Journal.ordinal() + 1);
                        break;
                    case "Contra":
                        voucher.setTypeId((long) VoucherTypeEnum.Contra.ordinal() + 1);
                        break;
                }
            }
            parser.next();
        }

        if (null != vGuid) {
            voucher.setGuid(vGuid);
        }
        if (null != vNarration) {
            voucher.setNarration(vNarration);
        }

        return voucher;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
