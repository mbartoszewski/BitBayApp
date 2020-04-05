package com.bitbay.mbart.bitbayapp.models.cryptoWithdraw;

public class Data {
    private String uuid;
    private String userID;
    private String balanceID;
    private Currency currency;
    private Double amount;
    private Double withdrawalAmount;
    private Double lockAmount;
    private Long provision;
    private String app;
    private Operator operator;
    private String address;
    private Object custom;
    private String status;
    private Long time;
    private Long lastUpdate;
    private String comment;
    private Boolean internal;
    private Object info;
    private Object customParams;

    public String getUUID() { return uuid; }
    public void setUUID(String value) { this.uuid = value; }

    public String getUserID() { return userID; }
    public void setUserID(String value) { this.userID = value; }

    public String getBalanceID() { return balanceID; }
    public void setBalanceID(String value) { this.balanceID = value; }

    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency value) { this.currency = value; }

    public Double getAmount() { return amount; }
    public void setAmount(Double value) { this.amount = value; }

    public Double getWithdrawalAmount() { return withdrawalAmount; }
    public void setWithdrawalAmount(Double value) { this.withdrawalAmount = value; }

    public Double getLockAmount() { return lockAmount; }
    public void setLockAmount(Double value) { this.lockAmount = value; }

    public Long getProvision() { return provision; }
    public void setProvision(Long value) { this.provision = value; }

    public String getApp() { return app; }
    public void setApp(String value) { this.app = value; }

    public Operator getOperator() { return operator; }
    public void setOperator(Operator value) { this.operator = value; }

    public String getAddress() { return address; }
    public void setAddress(String value) { this.address = value; }

    public Object getCustom() { return custom; }
    public void setCustom(Object value) { this.custom = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    public Long getTime() { return time; }
    public void setTime(Long value) { this.time = value; }

    public Long getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(Long value) { this.lastUpdate = value; }

    public String getComment() { return comment; }
    public void setComment(String value) { this.comment = value; }

    public Boolean getInternal() { return internal; }
    public void setInternal(Boolean value) { this.internal = value; }

    public Object getInfo() { return info; }
    public void setInfo(Object value) { this.info = value; }

    public Object getCustomParams() { return customParams; }
    public void setCustomParams(Object value) { this.customParams = value; }
}