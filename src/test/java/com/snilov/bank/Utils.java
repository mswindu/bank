package com.snilov.bank;

public class Utils {

    public static String createAccountJson(String currency, String balance, String type) {
        return "{ \"currency\": \"" + currency + "\", " +
                "\"balance\": \"" + balance + "\", " +
                "\"type\": \"" + type + "\"}";
    }

    public static String createAccountWithIncorrectParametersJson(String currency, String balance, String type) {
        return "{ \"currency1\": \"" + currency + "\", " +
                "\"balance1\": \"" + balance + "\", " +
                "\"type1\": \"" + type + "\"}";
    }

    public static String createCardsJson(String blocked, String number, String type, String accountUuid) {
        return "{ \"blocked\": \"" + blocked + "\", " +
                "\"number\": \"" + number + "\", " +
                "\"type\": \"" + type + "\", " +
                "\"accountUuid\": \"" + accountUuid + "\"}";
    }

    public static String createCardsJson(String blocked, String number, String type) {
        return "{ \"blocked\": \"" + blocked + "\", " +
                "\"number\": \"" + number + "\", " +
                "\"type\": \"" + type + "\"}";
    }

    public static String createCardsWithIncorrectParametersJson(String blocked, String number, String type) {
        return "{ \"blocked1\": \"" + blocked + "\", " +
                "\"number1\": \"" + number + "\", " +
                "\"type1\": \"" + type + "\"}";
    }

    public static String createTransactionsJson(String uuidCard, String typeTransaction, String transactionAmount) {
        return "{ \"uuidCard\": \"" + uuidCard + "\", " +
                "\"typeTransaction\": \"" + typeTransaction + "\", " +
                "\"transactionAmount\": \"" + transactionAmount + "\"}";
    }

    public static String createTransactionsWithIncorrectParametersJson(String uuidCard, String typeTransaction, String transactionAmount) {
        return "{ \"uuidCard1\": \"" + uuidCard + "\", " +
                "\"typeTransaction1\": \"" + typeTransaction + "\", " +
                "\"transactionAmount1\": \"" + transactionAmount + "\"}";
    }

    public static String createTransferJson(String typeTransfer, String uuidPayer, String uuidPayee, String amount) {
        return "{ \"typeTransferEnum\": \"" + typeTransfer + "\", " +
                "\"uuidPayer\": \"" + uuidPayer + "\", " +
                "\"uuidPayee\": \"" + uuidPayee + "\", " +
                "\"amount\": \"" + amount + "\"}";
    }
}
