package com.scorpio.myexpensemanager.commons.sms;

import android.annotation.SuppressLint;
import android.app.Application;

import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.Util;
import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.scorpio.myexpensemanager.db.vo.VoucherEntry;
import com.scorpio.myexpensemanager.db.vo.VoucherType;
import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;
import com.scorpio.myexpensemanager.viewmodels.LedgerVM;
import com.scorpio.myexpensemanager.viewmodels.VoucherTypeVM;
import com.scorpio.myexpensemanager.viewmodels.VoucherVM;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.CompletionService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("NewApi")
public class ProcessSms {
    //    Map<String, SmsConfigModel> smsConfigMap = new HashMap<>();
    private MultiValuedMap<String, SmsConfig> smsConfigMapData = new ArrayListValuedHashMap<>();
    private VoucherVM voucherVM;
    private LedgerVM ledgerVM;
    private VoucherTypeVM voucherTypeVM;

    public ProcessSms(Application application) {
        initializeConfigData();
        voucherVM = new VoucherVM(application);
        ledgerVM = new LedgerVM(application);
        voucherTypeVM = new VoucherTypeVM(application);
    }

    private void initializeConfigData() {
        //config data for citibank sms
        SmsConfig citibkSmsConfig = new SmsConfig(Constants.CITIBK, Constants.PAYMENT, null,
                Constants.CITIBANK_CREDIT_CARD);
        citibkSmsConfig.setPareseFunction(1);
        citibkSmsConfig.setPattern("(([r|R][s|S][\\s\\.]*)([\\d,]*[\\.]\\d*)" +
                "([\\s\\w]+\\s[\\s\\w]+)(\\d{4}X*\\d{4})(\\son\\s)" +
                "(\\d+[-:][a-zA-Z]+?[-:]\\d{2,4})(\\sat\\s)([\\w\\s]+[a-zA-Z]+))");
        smsConfigMapData.put(Constants.CITIBK, citibkSmsConfig);

        SmsConfig hdfcBankCardConfig = new SmsConfig(Constants.HDFCBK, Constants.CONTRA, Constants
                .HDFC_BANK_CARD, null);
        hdfcBankCardConfig.setPareseFunction(1);
        hdfcBankCardConfig.setPattern("(([r|R][s|S][\\s\\.]*)([\\d]*[\\\\.]\\d*)([\\s\\w]+)" +
                "(\\swithdrawn\\s)([\\s\\w]+)(\\d{4})(\\son\\s)(\\d+[-:][\\d]*[-:]\\d{2,4}))");
        smsConfigMapData.put(Constants.HDFCBK, hdfcBankCardConfig);
        SmsConfig hdfcBankConfig = new SmsConfig(Constants.HDFCBK, Constants.RECEIPT, Constants
                .HDFC_BANK, null);
        hdfcBankConfig.setPareseFunction(2);
        hdfcBankConfig.setPattern("(([\\d,]*[\\\\.]\\d*)([\\s\\w/]+)(\\d{4})([\\s\\w]+)" +
                "(-[\\w\\s]+-)([\\w\\s]+)([-\\w\\s]+)(\\d{2}[-:][a-zA-Z]+?[-:]\\d{2,4}))");
        smsConfigMapData.put(Constants.HDFCBK, hdfcBankConfig);

        SmsConfig hdfcBankConfig2 = new SmsConfig(Constants.HDFCBK, Constants.PAYMENT, null,
                Constants.HDFC_BANK);
        hdfcBankConfig2.setPareseFunction(3);
        hdfcBankConfig2.setPattern("([\\w\\s]+[r|R][s|S][\\s\\.]*)([\\d,]*[\\\\.]\\d*)([\\w\\s]+)" +
                "(\\d{4})([\\w\\s]+using)([\\w\\s]+)");
        smsConfigMapData.put(Constants.HDFCBK, hdfcBankConfig2);
    }

    public final boolean isSupportedSms(String address) {
        return ((null != smsConfigMapData.get(address) ? true : false));
    }

    public final void process(String from, String receivedOn, String body) {
//        String pattern = gson.fromJson(smsConfig.get(from).getValue(), SmsPatternModel.class)
//                .getPattern();
//        String pattern = "(([r|R][s|S][\\.])([\\d,]*[\\.]\\d*)([\\s\\w]+\\sCredit " +
//                "Card\\s[\\s\\w]+)(\\d{4}X*\\d{4})(\\son\\s)(\\d+[-:][a-zA-Z]+?[-:]\\d{2,4})" +
//                "(\\sat\\s)([\\w\\s]+[\\.]))";
        if (null != smsConfigMapData.get(from) && null == voucherVM.getVoucherIdBySmdId
                (receivedOn)) {
            for (SmsConfig smsConfig : smsConfigMapData.get(from)) {
                //Parsing Citibank Credit card messages
                if (from.equalsIgnoreCase(Constants.CITIBK)) {
                    processCitibankCreditCardSms(smsConfig, receivedOn, from, body);
                }
                //Process HDFC Bank messages
                if (from.equalsIgnoreCase(Constants.HDFCBK)) {
                    processHdfcBankSms(smsConfig, receivedOn, from, body);
                }

                //processing Zeta Meal Voucher card messages
                if (from.equalsIgnoreCase(Constants.ZETAAA)) {
                    processZetaMealVoucherCardSms(smsConfig, receivedOn, from, body);
                }
            }
        }
    }

    private void processHdfcBankSms(SmsConfig smsConfig, String receivedOn, String from, String
            body) {
        switch (smsConfig.getPareseFunction()) {
            case 1:
                processHdfcBankSmsPattern1(smsConfig, receivedOn, from, body);
                break;
            case 2:
                processHdfcBankSmsPattern2(smsConfig, receivedOn, from, body);
                break;
            case 3:
                processHdfcBankSmsPattern3(smsConfig, receivedOn, from, body);
                break;
        }

    }

    private void processHdfcBankSmsPattern3(SmsConfig smsConfig, String receivedOn, String from,
                                            String body) {
        String pattern = smsConfig.getPattern();
        if (null != pattern && null != body) {
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(body);
            if (matcher.find()) {
                String amount = matcher.group(2);
                amount = removeComma(amount);
                String acNo = matcher.group(4);
                String atMechant = matcher.group(6);
                atMechant = atMechant.trim();
                Ledger drLedger = ledgerVM.getCreateLedger(atMechant, Constants.DIRECT_EXPENSES);
                Ledger crLedger = ledgerVM.getCreateLedger(smsConfig.getCrLedgerName() +
                        Constants.DASH + acNo, Constants.BANK_ACCOUNTS);
                VoucherType voucherType = voucherTypeVM.findVoucherTypeByName(smsConfig
                        .getVoucherType());
                saveVoucher(drLedger, crLedger, voucherType,
                        Util.convertToLocalDateFromTimeInMills(receivedOn),
                        Double.parseDouble(amount), receivedOn, from, body);
            }
        }
    }

    private void processHdfcBankSmsPattern2(SmsConfig smsConfig, String receivedOn, String from,
                                            String body) {
        String pattern = smsConfig.getPattern();
        if (null != pattern && null != body) {
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(body);
            if (matcher.find()) {
                String amount = matcher.group(2);
                amount = removeComma(amount);
                String acNo = matcher.group(4);
                String atMechant = matcher.group(7);
                atMechant = atMechant.trim();
                String onDate = matcher.group(9);
                Ledger drLedger = ledgerVM.getCreateLedger(smsConfig.getDrLederName() +
                        Constants.DASH + acNo, Constants.BANK_ACCOUNTS);
                Ledger crLedger = ledgerVM.getCreateLedger(atMechant, Constants.DIRECT_INCOMES);
                VoucherType voucherType = voucherTypeVM.findVoucherTypeByName(smsConfig
                        .getVoucherType());
                saveVoucher(drLedger, crLedger, voucherType, Util.convertToLocalDateFromDMMMYY
                        (onDate), Double.parseDouble(amount), receivedOn, from, body);
            }
        }
    }


    private void processHdfcBankSmsPattern1(SmsConfig smsConfig, String receivedOn, String from,
                                            String body) {
        String pattern = smsConfig.getPattern();
        if (null != pattern && null != body) {
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(body);
            if (matcher.find()) {
                String amount = matcher.group(3);
                amount = removeComma(amount);
                String cardNo = matcher.group(7);
                String onDate = matcher.group(9);
                Ledger drLedger = ledgerVM.getCreateLedger(smsConfig.getDrLederName() +
                        Constants.DASH + cardNo, Constants.BANK_ACCOUNTS);
                Ledger crLedger = ledgerVM.getCreateLedger(Constants.CASH_IN_WALLET, Constants
                        .CASH_IN_HAND);
                VoucherType voucherType = voucherTypeVM.findVoucherTypeByName(smsConfig
                        .getVoucherType());
                saveVoucher(drLedger, crLedger, voucherType, Util.convertToLocalDateFromPattern
                        (DateTimeFormatter.ISO_LOCAL_DATE, onDate), Double.parseDouble
                        (amount), receivedOn, from, body);
            }
        }
    }

    private void processCitibankCreditCardSms(SmsConfig smsConfig, String
            receivedOn, String address, String body) {
        String pattern = smsConfig.getPattern();
        if (null != pattern && null != body) {
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(body);
            if (matcher.find()) {
                String amount = matcher.group(3);
                amount = removeComma(amount);
//                String ccNo = matcher.group(5);
                String onDate = matcher.group(7);
                String atMerchent = matcher.group(9);
                atMerchent = atMerchent.trim();
                Ledger drLedger = ledgerVM.getCreateLedger(atMerchent, Constants
                        .DIRECT_EXPENSES);
                SmsConfig citibkSmsConfig = smsConfig;
                Ledger crLedger = ledgerVM.getCreateLedger(citibkSmsConfig.getCrLedgerName(),
                        Constants.CREDIT_CARD);
                VoucherType voucherType = voucherTypeVM.findVoucherTypeByName(citibkSmsConfig
                        .getVoucherType());
                saveVoucher(drLedger, crLedger, voucherType, Util.convertToLocalDateFromDMMMYY
                        (onDate), Double.parseDouble(amount), receivedOn, address, body);
            }
        }
    }

    private void processZetaMealVoucherCardSms(SmsConfig smsConfig, String
            receivedOn, String address, String body) {
        String pattern = smsConfig.getPattern();
        if (null != pattern && null != body && null != receivedOn && null != address) {
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(body);
            if (matcher.find()) {
                String amount = matcher.group(3);
                String atMerchent = matcher.group(5);
                String mealVoucher = matcher.group(7);
                if (null != mealVoucher && mealVoucher.equalsIgnoreCase("Meal Vouchers")) {
                    Ledger drLedger = ledgerVM.getCreateLedger(atMerchent, Constants
                            .DIRECT_EXPENSES);
                    SmsConfig zetaSmsConfig = smsConfig;
                    Ledger crLedger = ledgerVM.getCreateLedger(zetaSmsConfig.getCrLedgerName(),
                            Constants.CASH_IN_HAND);
                    VoucherType voucherType = voucherTypeVM.findVoucherTypeByName(zetaSmsConfig
                            .getVoucherType());
//                    saveVoucher(drLedger, crLedger, voucherType, receivedOn, address, body,
//                            receivedOn, amount);
                }
            }
        }
    }

    private final String removeComma(String amount) {
        return amount.replace(",", "");
    }

    private final void saveVoucher(Ledger drLedger, Ledger crLedger, VoucherType voucherType,
                                   LocalDate voucherDate, Double amount, String receivedOn,
                                   String address, String body) {
        if (null != drLedger && null != crLedger && null != voucherType && null != voucherDate &&
                null != receivedOn && null != address && null != body && null != voucherDate &&
                null != amount) {
            VoucherWithEntries voucher = new VoucherWithEntries();
            voucher.setVoucherEntries(new ArrayList<>());
            voucher.setNumber(voucherType.getCurrentVoucherNo().toString());
            voucher.setVoucherType(voucherType);
            voucher.setType(voucherType.getName());
            voucher.setSmsid(receivedOn);
            voucher.setLocalDate(voucherDate);
            voucher.setNarration(body);
            //handle dr part
            VoucherEntry drVch = new VoucherEntry();
            drVch.setLedgerName(drLedger.getName());
            drVch.setDebitOrCredit(Constants.DEBIT);
            Double drV = -1 * (amount);
            //drVch.setAmount((-1) * Double.parseDouble(amount));
            drVch.setAmount(drV);
            voucher.getVoucherEntries().add(drVch);
            //handle cr part
            VoucherEntry crVch = new VoucherEntry();
            crVch.setLedgerName(crLedger.getName());
            crVch.setDebitOrCredit(Constants.CREDIT);
            crVch.setAmount(amount);
            voucher.getVoucherEntries().add(crVch);
            voucherVM.addVoucher(voucher);
        }
    }
}
