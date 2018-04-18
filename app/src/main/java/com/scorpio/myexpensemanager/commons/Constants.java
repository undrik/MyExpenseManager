package com.scorpio.myexpensemanager.commons;

public interface Constants {
    String APP_NAME = "MYEXPENSE";
    String COMANY_OBJ = "companyobj";
    String LEDGER_OBJ = "ledgerobj";
    String VOUCHER_OBJ = "voucherobj";
    String GROUP_OBJ = "groupobj";
    String G_NAME_OBJ = "gNameObj";
    String CREATE_GROUP = "creategroup";
    int CREATE_ACTION = 10;
    int UPDATE_ACTION = 20;
    int CREATE_GROUP_VALUE = 10;
    String CREATE_LEDGER = "createledger";
    int CREATE_LEDGER_VALUE = 20;
    String REPORT_BALANCE_SHEET_STR = "BalanceSheet";
    int REPORT_BALANCE_SHEET = 40;
    String REPORT_INCOME_EXPENSE_STR = "incomeExpense";
    int REPORT_INCOME_EXPENSE = 50;
    int SUCCESS_CODE = 600;
    int ERROR_CODE_EMPTY = 610;
    int ERROR_CODE_EXISTS = 620;
    String VOUCHER_START_DATE = "vchStartDate";
    String VOUCHER_END_DATE = "vchEndDate";
    String VOUCHER_LEDGER = "vchLedger";
    String BOOKS_OF = "Books of : ";
    String QUIT_COMPANY = "Exit company : ";
    String RUPEE_SYMBOL = "\u20B9";
    long ONE_DAY = 86400000;
    boolean PREDEFINED_TRUE = true;
    boolean PREDEFINED_FALSE = false;
    Boolean ISDEEMEDPOSITIVE_TRUE = true;
    Boolean ISDEEMEDPOSITIVE_FALSE = false;
    String SUCCESSFUL_CREATE_MSG = "Successfully created : ";
    String FAILURE_UPDATE_MSG = "Failed to update!";
    String SUCCESSFUL_UPDATE_MSG = "Successfully updated.";
    String SUCCESSFUL_DELETE_MSG = "Successfully deleted : ";

    String VOUCHER_CREATE_MSG = " voucher created.";
    String VOUCHER_DR_CR_MISMATCH_MSG = "Debit/Credit total does not match.";
    String VOUCHER_NO_EMPTY_MSG = "Voucher no can not be empty";
    String WARNING_TITLE = "Warning!";
    String WARNING_MSG = "Do you want to quit ?";
    String DELETE_MSG = "Are you sure to delete?";
    String OVERWRITE_MSG = "Are you sure to overwrite?";
    String ITEM_CANNOT_EMPTY_MSG = "Item cannot be empty";
    String YES = "Yes";
    String NO = "No";
    String DR = "Dr";
    String CR = "Cr";
    int DEBIT = 0;
    int CREDIT = 1;
    String SINGLE_SPACE = " ";
    String FORMAT_ASON = "(As on {0,date,d-MMM})";
    String DATE_FORMAT_RANGE = "({0,date,d-MMM-yyyy} to {1,date,d-MMM-yyyy})";
    String FORMAT_BALANCE_SHEET = "(As on {0,date,d-MMM-yyyy})";
    String DATE_FORMAT_D_MMM_YYYY = "d-MMM-yyyy";
    String DATE_FORMAT_D_MMM_YY = "d-MMM-yy";
    String DATE_FORMAT_D_MMM = "d-MMM";
    String DATE_FORMAT_WITH_SHORT_WEEK = "d-MMM-yyyy (EEE)";
    String DATE_FORMAT_WITH_LONG_WEEK = "d-MMM-yyyy EEEE";


    String DB_EXTENSION = ".db";
    String DB_SEQUENCE_TABLE = "sqlite_sequence";
    String DB_CREATE_TABLE = "CREATE TABLE ";
    String DB_CREATE_INDEX = "CREATE INDEX ";
    String DB_CREATE_INDEX_IF_NOT_EXISTS = "CREATE INDEX IF NOT EXISTS ";
    String DB_CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";
    String DB_CREATE_VIEW_IF_NOT_EXISTS = "CREATE VIEW IF NOT EXISTS ";
    String DB_AS = " AS ";
    String DB_WITH = "WITH ";
    String DB_SELECT = " SELECT ";
    String DB_FROM = " FROM ";
    String DB_LEFT_JOIN = " LEFT JOIN ";
    String DB_WHERE = " WHERE ";
    String DB_EQUAL = " = ";
    String DB_OPEN_BRACKET = " (";
    String DB_CLOSE_BRACKET = ")";
    String DB_INTEGER = " INTEGER";
    String DB_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT";
    String DB_TEXT = " TEXT";
    String DB_REAL = " REAL";
    String DB_BLOB = " BLOB";
    String DB_DATE = " DATE";
    String DB_CONSTRAINT = " CONSTRAINT ";

    String DB_PRIMARY_KEY = " PRIMARY KEY";
    String DB_UNIQUE = " UNIQUE ";
    String DB_FOREIGN_KEY = " FOREIGN KEY";
    String DB_REFERENCES = " REFERENCES ";
    String DB_NOT_NULL = " NOT NULL";
    String DB_IS_NOT_NULL = " IS NOT NULL";
    String DB_IS_NULL = " IS NULL";
    String DB_COMMA = ", ";
    String DB_ON = " ON ";
    String DB_ORDER_BY = " ORDER BY ";
    String DB_GROUP_BY = " GROUP BY ";
    String DB_HAVING = " HAVING ";

    String DB_AND = " AND ";
    String DB_IN = " IN ";
    String DB_LIKE = " LIKE ";
    String DB_DELETE = " DELETE ";
    String DB_CASCADE = " CASCADE ";


    // Predfined Groups and sub-groups
    // +------+------------------------------+
    // | SrNo | 15 Primary Groups |
    // +------+------------------------------+
    // | 1 | Capital Account |
    // +------+------------------------------+
    // | 2 | Current Assets |
    // +------+------------------------------+
    // | 3 | Current Liabilities |
    // +------+------------------------------+
    // | 4 | Direct Expenses |
    // +------+------------------------------+
    // | 5 | Direct Incomes |
    // +------+------------------------------+
    // | 6 | Fixed Assets |
    // +------+------------------------------+
    // | 7 | Indirect Expenses |
    // +------+------------------------------+
    // | 8 | Indirect Incomes |
    // +------+------------------------------+
    // | 9 | Investments |
    // +------+------------------------------+
    // | 10 | Loans (Liability) |
    // +------+-------------------------------+
    // | 11 | Misc. Expenses (ASSET)|
    // +------+------------------------------+
    // | 12 | Purchase Accounts |
    // +------+------------------------------+
    // | 13 | Sales Accounts |
    // +------+------------------------------+
    // | 14 | Suspense A/c |
    // +------+------------------------------+
    // | 15 | Credit Card |
    // +------+------------------------------+

    // +--------+-------------------------------+------------------------+
    // | SrNo | Pre defined Sub Groups | Under |
    // +--------+-------------------------------+------------------------+
    // | 1 | Bank Accounts | Current Assets |
    // +--------+-------------------------------+------------------------+
    // | 2 | Bank OD A/c | Loans (Liability) |
    // +--------+-------------------------------+------------------------+
    // | 3 |Cash-in-hand | Current Assets |
    // +--------+-------------------------------+------------------------+
    // | 4 | Deposits (Asset) | Current Assets |
    // +--------+-------------------------------+------------------------+
    // | 5 | Duties & Taxes | Current Liabilities |
    // +--------+-------------------------------+------------------------+
    // | 6 | Loans & Advances (Asset)|Current Assets |
    // +--------+-------------------------------+------------------------+
    // | 7 | Provisions | Current Liabilities|
    // +--------+-------------------------------+------------------------+
    // | 8 | Reserves & Surplus | Capital Account |
    // +--------+-------------------------------+------------------------+
    // | 9 | Secured Loans | Loans (Liability) |
    // +--------+-------------------------------+------------------------+
    // | 10 | Stock-in-hand | Current Assets |
    // +--------+-------------------------------+------------------------+
    // | 11 | Sundry Creditors | Current Liabilities |
    // +--------+-------------------------------+------------------------+
    // | 12 | Sundry Debtors | Current Assets |
    // +--------+-------------------------------+------------------------+
    // | 13 | Unsecured Loans | Loans (Liability) |
    // +--------+-------------------------------+------------------------+

    String PRIMARY = "Primary";
    String CAPITAL_ACCOUNT = "Capital Account";
    String CURRENT_ASSETS = "Current Assets";
    String CURRENT_LIABILITIES = "Current Liabilities";
    String DIRECT_EXPENSES = "Direct Expenses";
    String DIRECT_INCOMES = "Direct Incomes";
    String FIXED_ASSETS = "Fixed Assets";
    String INDIRECT_EXPENSES = "Indirect Expenses";
    String INDIRECT_INCOMES = "Indirect Incomes";
    String INVESTMENTS = "Investments";
    String LOANS_LIABILITY = "Loans (Liability)";
    String MISC_EXPENSES_ASSET = "Misc. Expenses (ASSET)";
    String PURCHASE_ACCOUNTS = "Purchase Accounts";
    String SALES_ACCOUNTS = "Sales Accounts";
    String SUSPENSE_ACCOUNT = "Suspense A/c";
    String BANK_ACCOUNTS = "Bank Accounts";
    String BANK_OD_ACCOUNT = "Bank OD A/c";
    String CASH_IN_HAND = "Cash-in-hand";
    String DEPOSITS_ASSET = "Deposits (Asset)";
    String DUTIES_TAXES = "Duties & Taxes";
    String LOANS_ADVANCES_ASSET = "Loans & Advances (Asset)";
    String PROVISIONS = "Provisions";
    String RESERVES_SURPLUS = "Reserves & Surplus";
    String SECURED_LOANS = "Secured Loans";
    String STOCK_IN_HAND = "Stock-in-hand";
    String SUNDRY_CREDITORS = "Sundry Creditors";
    String SUNDRY_DEBTORS = "Sundry Debtors";
    String UNSECURED_LOANS = "Unsecured Loans";
    String CREDIT_CARD = "Credit Card";

    //Default Ledger
    String CASH_IN_WALLET = "Cash-in-Wallet";

    //Voucher type
    String PAYMENT = "Payment";
    String RECEIPT = "Receipt";
    String CONTRA = "Contra";
    String JOURNAL = "Journal";
    String PURCHASE = "Purchase";
    String SALES = "Sales";


    //Tally voucher xml tag constants
    String ENVELOPE = "ENVELOPE";
    String HEADER = "HEADER";
    String TALLYREQUEST = "TALLYREQUEST";
    String REQUEST_IMPORT = "Import";
    String TYPE = "TYPE";
    String REQUEST_TYPE = "Data";
    String BODY = "BODY";
    String DATA = "DATA";
    String TALLYMESSAGE = "TALLYMESSAGE";
    String VOUCHER = "VOUCHER";
    String V_ATTR_REMOTEID = "REMOTEID";
    String V_DATE = "DATE";
    String GUID = "GUID";
    String DATE_FORMAT_TALLY_YYYYMMDD = "yyyyMMdd";
    String NARRATION = "NARRATION";
    String VOUCHERTYPENAME = "VOUCHERTYPENAME";
    String ALLLEDGERENTRIES_LIST = "ALLLEDGERENTRIES.LIST";
    String LEDGERNAME = "LEDGERNAME";
    String ISDEEMEDPOSITIVE = "ISDEEMEDPOSITIVE";
    String AMOUNT = "AMOUNT";

    String[] PAYMENT_DEBIT_GROUPS = new String[]{Constants
            .CURRENT_LIABILITIES, Constants.DIRECT_EXPENSES, Constants.INDIRECT_EXPENSES,
            Constants.LOANS_LIABILITY, Constants.SUSPENSE_ACCOUNT};
    String[] PAYMENT_CREDIT_GROUPS = new String[]{Constants.BANK_ACCOUNTS, Constants
            .CASH_IN_HAND, Constants.CREDIT_CARD, Constants.BANK_OD_ACCOUNT};

    String[] RECEIVE_CREDIT_GROUPS = new String[]{Constants
            .CURRENT_LIABILITIES, Constants.DIRECT_EXPENSES, Constants.INDIRECT_EXPENSES,
            Constants.LOANS_LIABILITY, Constants.SUSPENSE_ACCOUNT, Constants.DIRECT_INCOMES,
            Constants.INDIRECT_INCOMES};
    String[] REDEIVE_DEBIT_GROUPS = new String[]{Constants.BANK_ACCOUNTS, Constants.CASH_IN_HAND,
            Constants.CREDIT_CARD, Constants.BANK_OD_ACCOUNT};

    String[] CONTRA_GROUPS = new String[]{Constants.BANK_ACCOUNTS, Constants.BANK_OD_ACCOUNT,
            Constants.CASH_IN_HAND};
    String[] My_BANK_ACCOUNTS = new String[]{Constants.BANK_ACCOUNTS, Constants.BANK_OD_ACCOUNT};

    //Separator
    String DASH = " - ";

    //Supported SMS
    String CITIBK = "CITIBK";
    String CITIBANK_CREDIT_CARD = "Citibank Credit Card";

    String HDFCBK = "HDFCBK";
    String HDFC_BANK_CARD = "HDFC Bank Card";
    String HDFC_BANK = "HDFC Bank";


    String ZETAAA = "ZETAAA";
    //Citibank pattern
    //(([r|R][s|S][\s\.])([\d,]*[\.]\d*)([\s\w]+\s[\s\w]+)(\d{4}X*\d{4})(\son\s)
    // (\d+[-:][a-zA-Z]+?[-:]\d{2,4})(\sat\s)([\w\s]+[a-zA-Z\.]+))
    String SMSCONFIGDATA = " [" +
            "    {" +
            "      \"category\": \"CREDITCARD\"," +
            "      \"address\": \"CITIBK\"," +
            "      \"pattern\": \"(([r|R][s|S][\\\\s\\\\.])([\\\\d,]*[\\\\.]\\\\d*)" +
            "([\\\\s\\\\w]+\\\\s[\\\\s\\\\w]+)(\\\\d{4}X*\\\\d{4})(\\\\son\\\\s)" +
            "(\\\\d+[-:][a-zA-Z]+?[-:]\\\\d{2,4})(\\\\sat\\\\s)([\\\\w\\\\s]+[a-zA-Z\\\\.]+))\"," +
            "      \"crLedgerName\": \"Citibank Credit Card\"," +
            "      \"crLedgerId\": null," +
            "      \"drLedgerName\": null," +
            "      \"drLedgerId\": null," +
            "      \"vchTypeId\": 1" +
            "    }," +
            "{" +
            "      \"category\": \"MEALCARD\"," +
            "      \"address\": \"ZETAAA\"," +
            "      \"pattern\": \"([\\\\s\\\\w']+\\\\s[\\\\s\\\\w]+)([r|R][s|S][\\\\s\\\\" +
            ".\\\\s]+)((?:\\\\d*\\\\.)?\\\\d+)(\\\\sto\\\\s)([\\\\w\\\\s]+[a-zA-Z\\\\.]+)" +
            "(\\\\susing\\\\s)(Meal Vouchers)\"," +
            "      \"crLedgerName\": \"Zeta Meal Voucher Card\"," +
            "      \"crLedgerId\": null," +
            "      \"drLedgerName\": null," +
            "      \"drLedgerId\": null," +
            "      \"vchTypeId\": 1" +
            "    }" +
            "  ]";
}
