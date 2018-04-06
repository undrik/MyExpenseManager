package com.scorpio.myexpensemanager.commons.sms;

import android.app.Application;

import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.Util;
import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.scorpio.myexpensemanager.db.vo.VoucherEntry;
import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;
import com.scorpio.myexpensemanager.viewmodels.LedgerVM;
import com.scorpio.myexpensemanager.viewmodels.VoucherVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessSms {
    //    Map<String, SmsConfigModel> smsConfigMap = new HashMap<>();
    private Map<String, SmsConfig> smsConfigMapData = new HashMap<>();
    private VoucherVM voucherVM;
    private LedgerVM ledgerVM;
//    HashMap<String, Config> smsConfig;
//    VoucherDao voucherDao;
//    LedgerDao ledgerDao;
//    Gson gson = new Gson();

    final String smsconfig = "";

    public ProcessSms(Application application) {
        initializeConfigData();
        voucherVM = new VoucherVM(application);
        ledgerVM = new LedgerVM(application);
    }

    private void initializeConfigData() {
        //config data for citibank sms
        SmsConfig citibkSmsConfig = new SmsConfig(Constants.CITIBK, Constants.PAYMENT, null,
                "Citibank Credit Card");
        citibkSmsConfig.getPatterns().add("(([r|R][s|S][\\s\\.]*)([\\d,]*[\\.]\\d*)" +
                "([\\s\\w]+\\s[\\s\\w]+)(\\d{4}X*\\d{4})(\\son\\s)" +
                "(\\d+[-:][a-zA-Z]+?[-:]\\d{2,4})(\\sat\\s)([\\w\\s]+[a-zA-Z\\.]+))");
        smsConfigMapData.put(Constants.CITIBK, citibkSmsConfig);
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
        if (null != smsConfigMapData.get(from)) {
            for (String pattern : smsConfigMapData.get(from).getPatterns()) {
                //Parsing Citibank Credit card messages
                if (null != pattern && from.equalsIgnoreCase(Constants.CITIBK)) {
                    processCitibankCreditCardSms(pattern, receivedOn, from, body);
                }
                //processing Zeta Meal Voucher card messages
                if (null != pattern && from.equalsIgnoreCase(Constants.ZETAAA)) {
                    processZetaMealVoucherCardSms(pattern, receivedOn, from, body);
                }
            }
        }
    }

    private final void processCitibankCreditCardSms(String pattern, String date, String address,
                                                    String body) {
        if (null != pattern && null != body) {
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(body);
            if (matcher.find()) {
                String amount = matcher.group(3);
                amount = amount.replace(",", "");
                String ccNo = matcher.group(5);
                String onDate = matcher.group(7);
                String atMerchent = matcher.group(9);
                if (null == voucherVM.getVoucherIdByNumber(date)) {
                    Ledger drLedger = ledgerVM.getCreateLedger(atMerchent, Constants
                            .DIRECT_EXPENSES);
                    SmsConfig citibkSmsConfig = smsConfigMapData.get(Constants.CITIBK);
                    Ledger crLedger = ledgerVM.getCreateLedger(citibkSmsConfig.getCrLedgerName(),
                            Constants.CREDIT_CARD);
                    if (null != drLedger && null != crLedger) {
                        VoucherWithEntries voucher = new VoucherWithEntries();
                        voucher.setVoucherEntries(new ArrayList<>());
                        voucher.setNumber(date);
                        voucher.setType(citibkSmsConfig.getVoucherType());
                        voucher.setLocalDate(Util.convertToLocalDateFromDMMMYY(onDate));
                        voucher.setNarration(body);
                        //handle dr part
                        VoucherEntry drVch = new VoucherEntry();
                        drVch.setLedgerName(drLedger.getName());
                        drVch.setDebitOrCredit(Constants.DEBIT);
                        Double drV = -1 * Double.parseDouble(amount);
                        //drVch.setAmount((-1) * Double.parseDouble(amount));
                        drVch.setAmount(drV);
                        voucher.getVoucherEntries().add(drVch);
                        //handle cr part
                        VoucherEntry crVch = new VoucherEntry();
                        crVch.setLedgerName(crLedger.getName());
                        crVch.setDebitOrCredit(Constants.CREDIT);
                        crVch.setAmount(Double.parseDouble(amount));
                        voucher.getVoucherEntries().add(crVch);
                        voucherVM.addVoucher(voucher);
                    }
                }
            }
        }
    }

    private final void processZetaMealVoucherCardSms(String pattern, String date, String address,
                                                     String body) {
        if (null != pattern && null != body && null != date && null != address) {
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(body);
            if (matcher.find()) {
                String amount = matcher.group(3);
                String atMerchent = matcher.group(5);
                String mealVoucher = matcher.group(7);
                if (null != mealVoucher && mealVoucher.equalsIgnoreCase("Meal Vouchers")) {
                    Ledger drLedger = ledgerVM.getCreateLedger(atMerchent, Constants
                            .DIRECT_EXPENSES);
                    SmsConfig zetaSmsConfig = smsConfigMapData.get(Constants.ZETAAA);
                    Ledger crLedger = ledgerVM.getCreateLedger(zetaSmsConfig.getCrLedgerName(),
                            Constants.CASH_IN_HAND);
                    saveVoucher(drLedger, crLedger, zetaSmsConfig.getVoucherType(), date,
                            address, body, date, amount);
                }
            }
        }
    }

    private final String removeComma(String amount) {
        return amount.replace(",", "");
    }

    private final void saveVoucher(Ledger drLedger, Ledger crLedger, String type, String date,
                                   String address, String body, String onDate, String amount) {
        if (null != drLedger && null != crLedger && null != type && null != date && null !=
                address && null !=
                body && null != onDate && null != amount) {
            VoucherWithEntries voucher = new VoucherWithEntries();
            voucher.setNumber(date);
            voucher.setType(type);
            voucher.setLocalDate(Util.convertToLocalDateFromDMMMYY(date));
            voucher.setNarration(body);
            //handle dr part
            VoucherEntry drVch = new VoucherEntry();
            drVch.setLedgerName(drLedger.getName());
            drVch.setDebitOrCredit(Constants.DEBIT);
            Double drV = -1 * Double.parseDouble(amount);
            //drVch.setAmount((-1) * Double.parseDouble(amount));
            drVch.setAmount(drV);
            voucher.getVoucherEntries().add(drVch);
            //handle cr part
            VoucherEntry crVch = new VoucherEntry();
            crVch.setLedgerName(crLedger.getName());
            crVch.setDebitOrCredit(Constants.CREDIT);
            crVch.setAmount(Double.parseDouble(amount));
            voucher.getVoucherEntries().add(crVch);
            voucherVM.addVoucher(voucher);
        }
    }
}
