package com.bitbay.mbart.bitbayapp.models.transactionHistory;

import java.math.BigDecimal;
import java.util.Map;

public class Item {
    private String id;
    private String market;
    private String time;
    private BigDecimal amount;
    private BigDecimal rate;
    private String initializedBy;
    private boolean wasTaker;
    private String userAction;
    private String offerID;

    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public String getMarket() { return market; }
    public void setMarket(String value) { this.market = value; }

    public String getTime() { return time; }
    public void setTime(String value) { this.time = value; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal value) { this.amount = value; }

    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal value) { this.rate = value; }

    public String getInitializedBy() { return initializedBy; }
    public void setInitializedBy(String value) { this.initializedBy = value; }

    public boolean getWasTaker() { return wasTaker; }
    public void setWasTaker(boolean value) { this.wasTaker = value; }

    public String getUserAction() { return userAction; }
    public void setUserAction(String value) { this.userAction = value; }

    public String getOfferID() { return offerID; }
    public void setOfferID(String value) { this.offerID = value; }
}