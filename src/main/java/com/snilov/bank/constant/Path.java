package com.snilov.bank.constant;

public class Path {
    public static final String ACCOUNTS = "/accounts";
    public static final String ACCOUNT = "/{uuidAccount}";
    public static final String ACCOUNT_FIND_CARD = ACCOUNT + "/findCard";

    public static final String CARDS = "/cards";
    public static final String CARD = "/{uuidCard}";
    public static final String CARD_BLOCK = CARD + "/block";
    public static final String CARD_UNBLOCK = CARD + "/unblock";

    public static final String TRANSACTIONS = "/transactions";
    public static final String TRANSACTION = "/{uuidTransaction}";
    public static final String TRANSACTION_ROLLBACK = TRANSACTION + "/rollback";
    public static final String TRANSFER = "/transfer";

}
