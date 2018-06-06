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
        SmsConfig sbiConfig1 = new SmsConfig(Constants.ATMSBI, Constants.CONTRA, Constants
                .SBI_ACCOUNT, null);
        sbiConfig1.setPareseFunction(1);
        sbiConfig1.setPattern("([r|R][s|S][\\s]*)([\\d\\.]*)([\\w\\s/,]+)(\\d{4})(on)" +
                "(\\d*[-:/]\\d*[-:/]\\d*)");
        smsConfigMapData.put(Constants.ATMSBI, sbiConfig1);
        SmsConfig sbiConfig2 = new SmsConfig(Constants.CBSSBI, Constants.RECEIPT, Constants
                .SBI_ACCOUNT, Constants.INCOME_OTHER);
        sbiConfig2.setPareseFunction(2);
        sbiConfig2.setPattern("([\\w\\s/]+)(\\d{4})([\\w\\s]*)([cC]redit[ed]*)" +
                "([\\w\\s]*[INR|Rs]\\s)([\\d]*[\\.,]*\\d*[\\.]\\d*)(\\son\\s)" +
                "(\\d*[-:/]\\d*[-:/]\\d*)");
        smsConfigMapData.put(Constants.CBSSBI, sbiConfig2);
        SmsConfig sbiConfig3 = new SmsConfig(Constants.CBSSBI, Constants.RECEIPT, Constants
                .SBI_ACCOUNT, null);
        sbiConfig3.setPareseFunction(3);
        sbiConfig3.setPattern("([\\w\\s/]+)(\\d{4})([\\w\\s]*)([cC]redit[ed]*)" +
                "([\\w\\s]*[INR|Rs]\\s)([\\d]*[\\.,]*\\d*[\\.]\\d*)(\\son\\s)" +
                "(\\d*[-:/]\\d*[-:/]\\d*)([\\w\\s-]*)(from\\s)([\\w\\s]+[\\.?\\s]*)([\\w\\s]*)");
        smsConfigMapData.put(Constants.CBSSBI, sbiConfig3);
        SmsConfig sbiConfig4 = new SmsConfig(Constants.CBSSBI, Constants.PAYMENT, Constants
                .EXPENSE_OTHER, Constants.SBI_ACCOUNT);
        sbiConfig4.setPareseFunction(4);
        sbiConfig4.setPattern("([\\w\\s/]+)(\\d{4})([\\w\\s]*)(debit*)([\\w\\s]*[INR|Rs]\\s)" +
                "([\\d]*[\\.,]*\\d*[\\.]\\d*)(\\son\\s)(\\d*[-:/]\\d*[-:/]\\d*)");
        smsConfigMapData.put(Constants.CBSSBI, sbiConfig4);
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
                //Process SBI Bank messages
                if (from.equalsIgnoreCase(Constants.ATMSBI)) {
                    processSbiBankSms(smsConfig, receivedOn, from, body);
                }
                if (from.equalsIgnoreCase(Constants.CBSSBI)) {
                    processSbiBankSms(smsConfig, receivedOn, from, body);
                }

                //processing Zeta Meal Voucher card messages
                if (from.equalsIgnoreCase(Constants.ZETAAA)) {
                    processZetaMealVoucherCardSms(smsConfig, receivedOn, from, body);
                }
            }
        }
    }

    private void processSbiBankSms(SmsConfig smsConfig, String receivedOn, String from, String
            body) {
        switch (smsConfig.getPareseFunction()) {
            case 1:
                processSbiBankSmsPattern1(smsConfig, receivedOn, from, body);
                break;
            case 2:
                processSbiBankSmsPattern2(smsConfig, receivedOn, from, body);
                break;
            case 3:
                processSbiBankSmsPattern3(smsConfig, receivedOn, from, body);
                break;
            case 4:
                processSbiBankSmsPattern4(smsConfig, receivedOn, from, body);
        }
    }

    private void processSbiBankSmsPattern4(SmsConfig smsConfig, String receivedOn, String from,
                                           String body) {
        String pattern = smsConfig.getPattern();
        if (null != pattern && null != body) {
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(body);
            if (matcher.find()) {
                String acNo = matcher.group(2);
                String amount = removeComma(matcher.group(6));
                String onDate = matcher.group(8);
//                atMechant = atMechant.trim();
                Ledger drLedger = ledgerVM.getCreateLedger(smsConfig.getDrLederName(),
                        Constants.EXPENSE_OTHER);
                Ledger crLedger = ledgerVM.getCreateLedger(smsConfig.getCrLedgerName() +
                        Constants.DASH + acNo, Constants.BANK_ACCOUNTS);
                VoucherType voucherType = voucherTypeVM.findVoucherTypeByName(smsConfig
                        .getVoucherType());
                saveVoucher(drLedger, crLedger, voucherType,
                        Util.convertToLocalDateFromPattern(Constants.DATE_FORMAT_SBI, onDate),
                        Double.parseDouble(amount), receivedOn, from, body);
            }
        }
    }

    private void processSbiBankSmsPattern3(SmsConfig smsConfig, String receivedOn, String from,
                                           String body) {
        String pattern = smsConfig.getPattern();
        if (null != pattern && null != body) {
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(body);
            if (matcher.find()) {
                String acNo = matcher.group(2);
                String amount = matcher.group(6);
                amount = removeComma(amount);
                String onDate = matcher.group(8);
                String salutation = matcher.group(11);
                String fromPerson = matcher.group(12);
                fromPerson = fromPerson.trim();
                Ledger crLedger;
                if (null != salutation) {
                    salutation = salutation.concat(fromPerson);
                    crLedger = ledgerVM.getCreateLedger(salutation, Constants.INDIRECT_INCOMES);
                } else {
                    crLedger = ledgerVM.getCreateLedger(fromPerson, Constants.INDIRECT_INCOMES);
                }
                Ledger drLedger = ledgerVM.getCreateLedger(smsConfig.getDrLederName() +
                        Constants.DASH + acNo, Constants.BANK_ACCOUNTS);
                VoucherType voucherType = voucherTypeVM.findVoucherTypeByName(smsConfig
                        .getVoucherType());
                saveVoucher(drLedger, crLedger, voucherType,
                        Util.convertToLocalDateFromPattern(Constants.DATE_FORMAT_SBI, onDate),
                        Double.parseDouble(amount), receivedOn, from, body);
            }
        }
    }

    private void processSbiBankSmsPattern2(SmsConfig smsConfig, String receivedOn, String from,
                                           String body) {
        String pattern = smsConfig.getPattern();
        if (null != pattern && null != body) {
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(body);
            if (matcher.find()) {
                String acNo = matcher.group(2);
                String amount = removeComma(matcher.group(6));
                String onDate = matcher.group(8);
//                atMechant = atMechant.trim();
                Ledger drLedger = ledgerVM.getCreateLedger(smsConfig.getDrLederName() +
                        Constants.DASH + acNo, Constants.BANK_ACCOUNTS);
                Ledger crLedger = ledgerVM.getCreateLedger(smsConfig.getCrLedgerName(), Constants
                        .INDIRECT_INCOMES);
                VoucherType voucherType = voucherTypeVM.findVoucherTypeByName(smsConfig
                        .getVoucherType());
                saveVoucher(drLedger, crLedger, voucherType,
                        Util.convertToLocalDateFromPattern(Constants.DATE_FORMAT_SBI, onDate),
                        Double.parseDouble(amount), receivedOn, from, body);
            }
        }
    }

    private void processSbiBankSmsPattern1(SmsConfig smsConfig, String receivedOn, String from,
                                           String body) {
        String pattern = smsConfig.getPattern();
        if (null != pattern && null != body) {
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(body);
            if (matcher.find()) {
                String amount = matcher.group(2);
                amount = removeComma(amount);
                String acNo = matcher.group(4);
                String onDate = matcher.group(6);
//                atMechant = atMechant.trim();
                Ledger crLedger = ledgerVM.getCreateLedger(smsConfig.getDrLederName() +
                        Constants.DASH + acNo, Constants.BANK_ACCOUNTS);
                Ledger drLedger = ledgerVM.getCreateLedger(Constants.CASH_IN_WALLET, Constants
                        .BANK_ACCOUNTS);
                VoucherType voucherType = voucherTypeVM.findVoucherTypeByName(smsConfig
                        .getVoucherType());
                saveVoucher(drLedger, crLedger, voucherType,
                        Util.convertToLocalDateFromPattern(Constants.DATE_FORMAT_SBI, onDate),
                        Double.parseDouble(amount), receivedOn, from, body);
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
                Ledger crLedger = ledgerVM.getCreateLedger(smsConfig.getDrLederName() +
                        Constants.DASH + cardNo, Constants.BANK_ACCOUNTS);
                Ledger drLedger = ledgerVM.getCreateLedger(Constants.CASH_IN_WALLET, Constants
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
