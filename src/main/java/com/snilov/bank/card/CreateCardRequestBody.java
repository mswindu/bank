package com.snilov.bank.card;

public class CreateCardRequestBody {
    private String number;
    private Card.TypeCard type;
    private Boolean blocked;
    private String accountUuid;

    CreateCardRequestBody() {}

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Card.TypeCard getType() {
        return type;
    }

    public void setType(Card.TypeCard type) {
        this.type = type;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public String getAccountUuid() {
        return accountUuid;
    }

    public void setAccountUuid(String accountUuid) {
        this.accountUuid = accountUuid;
    }

    @Override
    public String toString() {
        return "CreateCardRequestBody{" +
                "number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", blocked=" + blocked +
                ", accountUuid='" + accountUuid + '\'' +
                '}';
    }
}
