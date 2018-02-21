package com.scorpio.myexpensemanager.commons;

public interface Constants {
    public static final String APP_NAME = "MYEXPENSE";
    public static final String COMANY_OBJ = "companyobj";
    public static final String LEDGER_OBJ = "ledgerobj";
    public static final String GROUP_OBJ = "groupobj";
    String G_NAME_OBJ = "gNameObj";
    public static final String CREATE_GROUP = "creategroup";
    public static final int CREATE_GROUP_VALUE = 10;
    public static final String CREATE_LEDGER = "createledger";
    public static final int CREATE_LEDGER_VALUE = 20;
    public static final String REPORT_BALANCE_SHEET_STR = "BalanceSheet";
    public static final int REPORT_BALANCE_SHEET = 40;
    public static final String REPORT_INCOME_EXPENSE_STR = "incomeExpense";
    public static final int REPORT_INCOME_EXPENSE = 50;
    String VOUCHER_START_DATE = "vchStartDate";
    String VOUCHER_END_DATE = "vchEndDate";
    String VOUCHER_LEDGER = "vchLedger";
    public static final String BOOKS_OF = "Books of : ";
    public static final String QUIT_COMPANY = "Exit company : ";
    public static final String RUPEE_SYMBOL = "\u20B9";
    public static final long ONE_DAY = 86400000;
    public static final boolean PREDEFINED_TRUE = true;
    public static final boolean PREDEFINED_FALSE = false;
    public static final Boolean ISDEEMEDPOSITIVE_TRUE = true;
    public static final Boolean ISDEEMEDPOSITIVE_FALSE = false;
    public static final String SUCCESSFUL_CREATE_MSG = "Successfully created : ";
    public static final String FAILURE_UPDATE_MSG = "Failed to update!";
    public static final String SUCCESSFUL_UPDATE_MSG = "Successfully updated.";
    public static final String SUCCESSFUL_DELETE_MSG = "Successfully deleted : ";

    public static final String VOUCHER_CREATE_MSG = " voucher created.";
    public static final String VOUCHER_DR_CR_MISMATCH_MSG = "Debit/Credit total does not match.";
    public static final String VOUCHER_NO_EMPTY_MSG = "Voucher no can not be empty";
    public static final String WARNING_TITLE = "Warning!";
    public static final String WARNING_MSG = "Do you want to quit ?";
    public static final String DELETE_MSG = "Are you sure to delete?";
    public static final String OVERWRITE_MSG = "Are you sure to overwrite?";
    public static final String ITEM_CANNOT_EMPTY_MSG = "Item cannot be empty";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String DR = "Dr";
    public static final String CR = "Cr";
    public static final String SINGLE_SPACE = " ";
    public static final String FORMAT_ASON = "(As on {0,date,d-MMM})";
    public static final String DATE_FORMAT_RANGE = "({0,date,d-MMM-yyyy} to {1,date,d-MMM-yyyy})";
    public static final String FORMAT_BALANCE_SHEET = "(As on {0,date,d-MMM-yyyy})";
    public static final String DATE_FORMAT_D_MMM_YYYY = "d-MMM-yyyy";
    public static final String DATE_FORMAT_D_MMM_YY = "d-MMM-yy";
    public static final String DATE_FORMAT_D_MMM = "d-MMM";
    public static final String DATE_FORMAT_WITH_SHORT_WEEK = "d-MMM-yyyy (EEE)";
    public static final String DATE_FORMAT_WITH_LONG_WEEK = "d-MMM-yyyy EEEE";


    public static final String DB_EXTENSION = ".db";
    public static final String DB_SEQUENCE_TABLE = "sqlite_sequence";
    public static final String DB_CREATE_TABLE = "CREATE TABLE ";
    public static final String DB_CREATE_INDEX = "CREATE INDEX ";
    public static final String DB_CREATE_INDEX_IF_NOT_EXISTS = "CREATE INDEX IF NOT EXISTS ";
    public static final String DB_CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";
    public static final String DB_CREATE_VIEW_IF_NOT_EXISTS = "CREATE VIEW IF NOT EXISTS ";
    public static final String DB_AS = " AS ";
    public static final String DB_WITH = "WITH ";
    public static final String DB_SELECT = " SELECT ";
    public static final String DB_FROM = " FROM ";
    public static final String DB_LEFT_JOIN = " LEFT JOIN ";
    public static final String DB_WHERE = " WHERE ";
    public static final String DB_EQUAL = " = ";
    public static final String DB_OPEN_BRACKET = " (";
    public static final String DB_CLOSE_BRACKET = ")";
    public static final String DB_INTEGER = " INTEGER";
    public static final String DB_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String DB_TEXT = " TEXT";
    public static final String DB_REAL = " REAL";
    String DB_BLOB = " BLOB";
    String DB_DATE = " DATE";
    String DB_CONSTRAINT = " CONSTRAINT ";

    public static final String DB_PRIMARY_KEY = " PRIMARY KEY";
    public static final String DB_UNIQUE = " UNIQUE ";
    public static final String DB_FOREIGN_KEY = " FOREIGN KEY";
    public static final String DB_REFERENCES = " REFERENCES ";
    public static final String DB_NOT_NULL = " NOT NULL";
    public static final String DB_IS_NOT_NULL = " IS NOT NULL";
    public static final String DB_IS_NULL = " IS NULL";
    public static final String DB_COMMA = ", ";
    public static final String DB_ON = " ON ";
    public static final String DB_ORDER_BY = " ORDER BY ";
    public static final String DB_GROUP_BY = " GROUP BY ";
    public static final String DB_HAVING = " HAVING ";

    public static final String DB_AND = " AND ";
    public static final String DB_IN = " IN ";
    public static final String DB_LIKE = " LIKE ";
    public static final String DB_DELETE = " DELETE ";
    public static final String DB_CASCADE = " CASCADE ";


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

    public static final String PRIMARY = "Primary";
    public static final String CAPITAL_ACCOUNT = "Capital Account";
    public static final String CURRENT_ASSETS = "Current Assets";
    public static final String CURRENT_LIABILITIES = "Current Liabilities";
    public static final String DIRECT_EXPENSES = "Direct Expenses";
    public static final String DIRECT_INCOMES = "Direct Incomes";
    public static final String FIXED_ASSETS = "Fixed Assets";
    public static final String INDIRECT_EXPENSES = "Indirect Expenses";
    public static final String INDIRECT_INCOMES = "Indirect Incomes";
    public static final String INVESTMENTS = "Investments";
    public static final String LOANS_LIABILITY = "Loans (Liability)";
    public static final String MISC_EXPENSES_ASSET = "Misc. Expenses (ASSET)";
    public static final String PURCHASE_ACCOUNTS = "Purchase Accounts";
    public static final String SALES_ACCOUNTS = "Sales Accounts";
    public static final String SUSPENSE_ACCOUNT = "Suspense A/c";
    public static final String BANK_ACCOUNTS = "Bank Accounts";
    public static final String BANK_OD_ACCOUNT = "Bank OD A/c";
    public static final String CASH_IN_HAND = "Cash-in-hand";
    public static final String DEPOSITS_ASSET = "Deposits (Asset)";
    public static final String DUTIES_TAXES = "Duties & Taxes";
    public static final String LOANS_ADVANCES_ASSET = "Loans & Advances (Asset)";
    public static final String PROVISIONS = "Provisions";
    public static final String RESERVES_SURPLUS = "Reserves & Surplus";
    public static final String SECURED_LOANS = "Secured Loans";
    public static final String STOCK_IN_HAND = "Stock-in-hand";
    public static final String SUNDRY_CREDITORS = "Sundry Creditors";
    public static final String SUNDRY_DEBTORS = "Sundry Debtors";
    public static final String UNSECURED_LOANS = "Unsecured Loans";
    public static final String CREDIT_CARD = "Credit Card";


    //Tally voucher xml tag constants
    public static final String ENVELOPE = "ENVELOPE";
    public static final String HEADER = "HEADER";
    public static final String TALLYREQUEST = "TALLYREQUEST";
    public static final String REQUEST_IMPORT = "Import";
    public static final String TYPE = "TYPE";
    public static final String REQUEST_TYPE = "Data";
    public static final String BODY = "BODY";
    public static final String DATA = "DATA";
    public static final String TALLYMESSAGE = "TALLYMESSAGE";
    public static final String VOUCHER = "VOUCHER";
    public static final String V_ATTR_REMOTEID = "REMOTEID";
    public static final String V_DATE = "DATE";
    public static final String GUID = "GUID";
    public static final String DATE_FORMAT_TALLY_YYYYMMDD = "yyyyMMdd";
    public static final String NARRATION = "NARRATION";
    public static final String VOUCHERTYPENAME = "VOUCHERTYPENAME";
    public static final String ALLLEDGERENTRIES_LIST = "ALLLEDGERENTRIES.LIST";
    public static final String LEDGERNAME = "LEDGERNAME";
    public static final String ISDEEMEDPOSITIVE = "ISDEEMEDPOSITIVE";
    public static final String AMOUNT = "AMOUNT";

    public static final String[] PAYMENT_DEBIT_GROUPS = new String[]{Constants
            .CURRENT_LIABILITIES, Constants.DIRECT_EXPENSES, Constants.INDIRECT_EXPENSES,
            Constants.LOANS_LIABILITY, Constants.SUSPENSE_ACCOUNT};
    String[] PAYMENT_CREDIT_GROUPS = new String[]{Constants.BANK_ACCOUNTS, Constants
            .CASH_IN_HAND, Constants.CREDIT_CARD, Constants.BANK_OD_ACCOUNT};

    public static final String[] RECEIVE_CREDIT_GROUPS = new String[]{Constants
            .CURRENT_LIABILITIES, Constants.DIRECT_EXPENSES, Constants.INDIRECT_EXPENSES,
            Constants.LOANS_LIABILITY, Constants.SUSPENSE_ACCOUNT, Constants.DIRECT_INCOMES,
            Constants.INDIRECT_INCOMES};
    String[] REDEIVE_DEBIT_GROUPS = new String[]{Constants.BANK_ACCOUNTS, Constants.CASH_IN_HAND,
            Constants.CREDIT_CARD, Constants.BANK_OD_ACCOUNT};

    String[] CONTRA_GROUPS = new String[]{Constants.BANK_ACCOUNTS, Constants.BANK_OD_ACCOUNT,
            Constants.CASH_IN_HAND};
    String[] My_BANK_ACCOUNTS = new String[]{Constants.BANK_ACCOUNTS, Constants.BANK_OD_ACCOUNT};

    //Supported SMS
    static final String CITIBK = "CITIBK";
    static final String ZETAAA = "ZETAAA";
    //Citibank pattern
    //(([r|R][s|S][\s\.])([\d,]*[\.]\d*)([\s\w]+\s[\s\w]+)(\d{4}X*\d{4})(\son\s)
    // (\d+[-:][a-zA-Z]+?[-:]\d{2,4})(\sat\s)([\w\s]+[a-zA-Z\.]+))
    public static final String SMSCONFIGDATA = " [" +
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
