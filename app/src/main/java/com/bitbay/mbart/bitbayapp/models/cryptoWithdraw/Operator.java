package com.bitbay.mbart.bitbayapp.models.cryptoWithdraw;

public class Operator {
    private Long id;
    private String name;
    private String type;
    private Object image;
    private Object statement;

    public Long getID() { return id; }
    public void setID(Long value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getType() { return type; }
    public void setType(String value) { this.type = value; }

    public Object getImage() { return image; }
    public void setImage(Object value) { this.image = value; }

    public Object getStatement() { return statement; }
    public void setStatement(Object value) { this.statement = value; }
}
