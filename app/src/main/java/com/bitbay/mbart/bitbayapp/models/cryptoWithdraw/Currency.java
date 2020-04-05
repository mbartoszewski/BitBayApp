package com.bitbay.mbart.bitbayapp.models.cryptoWithdraw;

public class Currency {
    private Long id;
    private String name;
    private String fullName;
    private String type;
    private Long precision;
    private Object enabledOperators;

    public Long getID() { return id; }
    public void setID(Long value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getFullName() { return fullName; }
    public void setFullName(String value) { this.fullName = value; }

    public String getType() { return type; }
    public void setType(String value) { this.type = value; }

    public Long getPrecision() { return precision; }
    public void setPrecision(Long value) { this.precision = value; }

    public Object getEnabledOperators() { return enabledOperators; }
    public void setEnabledOperators(Object value) { this.enabledOperators = value; }
}