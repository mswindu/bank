package com.snilov.bank.utils;

public class Utils {

    public static String createAccountJson(String currency, String balance, String type) {
        return "{\n" +
                "  \"currency\": \"" + currency + "\",\n" +
                "  \"balance\": \"" + balance + "\",\n" +
                "  \"type\": \"" + type + "\"\n" +
                "}";
    }

    public static String createAccountWithIncorrectParametersJson(String currency, String balance, String type) {
        return "{\n" +
                "  \"currency1\": \"" + currency + "\",\n" +
                "  \"balance1\": \"" + balance + "\",\n" +
                "  \"type1\": \"" + type + "\"\n" +
                "}";
    }

    public static String createCardsJson(String blocked, String number, String type, String accountUuid) {
        return "{\n" +
                "  \"blocked\": \"" + blocked + "\",\n" +
                "  \"pan\": \"" + number + "\",\n" +
                "  \"type\": \"" + type + "\",\n" +
                "  \"accountUuid\": \"" + accountUuid + "\"\n" +
                "}";
    }

    public static String createCardsJson(String blocked, String number, String type) {
        return "{\n" +
                "  \"blocked\": \"" + blocked + "\",\n" +
                "  \"pan\": \"" + number + "\",\n" +
                "  \"type\": \"" + type + "\"\n" +
                "}";
    }

    public static String createCardsWithIncorrectParametersJson(String blocked, String number, String type) {
        return "{\n" +
                "  \"blocked1\": \"" + blocked + "\",\n" +
                "  \"number1\": \"" + number + "\",\n" +
                "  \"type1\": \"" + type + "\"\n" +
                "}";
    }

    public static String createTransactionsJson(String uuidCard, String typeTransaction, String transactionAmount) {
        return "{\n" +
                "  \"uuidCard\": \"" + uuidCard + "\",\n" +
                "  \"typeTransaction\": \"" + typeTransaction + "\",\n" +
                "  \"transactionAmount\": \"" + transactionAmount + "\"\n" +
                "}";
    }

    public static String createTransactionsWithIncorrectParametersJson(String uuidCard, String typeTransaction, String transactionAmount) {
        return "{\n" +
                "  \"uuidCard1\": \"" + uuidCard + "\",\n" +
                "  \"typeTransaction1\": \"" + typeTransaction + "\",\n" +
                "  \"transactionAmount1\": \"" + transactionAmount + "\"\n" +
                "}";
    }

    public static String createTransferJson(String typeTransfer, String uuidPayer, String uuidPayee, String amount) {
        return "{\n" +
                "  \"typeTransferEnum\": \"" + typeTransfer + "\",\n" +
                "  \"uuidPayer\": \"" + uuidPayer + "\",\n" +
                "  \"uuidPayee\": \"" + uuidPayee + "\",\n" +
                "  \"amount\": \"" + amount + "\"\n" +
                "}";
    }
}
