package com.example.myapplication;

public class ReturnData {
    private Integer idtableEmail;
    private String tableEmailEmailAddress;
    private Boolean tableEmailValidate;

    public ReturnData(Integer idtableEmail, String tableEmailEmailAddress, Boolean tableEmailValidate) {
        this.idtableEmail = idtableEmail;
        this.tableEmailEmailAddress = tableEmailEmailAddress;
        this.tableEmailValidate = tableEmailValidate;
    }

    public ReturnData() {
    }

    public Integer getIdtableEmail() {
        return idtableEmail;
    }

    public void setIdtableEmail(Integer idtableEmail) {
        this.idtableEmail = idtableEmail;
    }

    public String getTableEmailEmailAddress() {
        return tableEmailEmailAddress;
    }

    public void setTableEmailEmailAddress(String tableEmailEmailAddress) {
        this.tableEmailEmailAddress = tableEmailEmailAddress;
    }

    public Boolean getTableEmailValidate() {
        return tableEmailValidate;
    }

    public void setTableEmailValidate(Boolean tableEmailValidate) {
        this.tableEmailValidate = tableEmailValidate;
    }
}

