package com.payroll.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CompanySetting implements Serializable {

    private static final long serialVersionUID = 1L;

    private int settingId;
    private String companyName;
    private String companyAddress;
    private String companyEmail;
    private String companyPhone;
    private String currencySymbol;
    private int standardWorkingDays;
    private BigDecimal defaultHraPct;
    private BigDecimal defaultDaPct;
    private BigDecimal defaultPfPct;
    private BigDecimal defaultPtax;

    public CompanySetting() {
        this.currencySymbol = "₹";
        this.standardWorkingDays = 30;
    }

    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public int getStandardWorkingDays() {
        return standardWorkingDays;
    }

    public void setStandardWorkingDays(int standardWorkingDays) {
        this.standardWorkingDays = standardWorkingDays;
    }

    public BigDecimal getDefaultHraPct() {
        return defaultHraPct;
    }

    public void setDefaultHraPct(BigDecimal defaultHraPct) {
        this.defaultHraPct = defaultHraPct;
    }

    public BigDecimal getDefaultDaPct() {
        return defaultDaPct;
    }

    public void setDefaultDaPct(BigDecimal defaultDaPct) {
        this.defaultDaPct = defaultDaPct;
    }

    public BigDecimal getDefaultPfPct() {
        return defaultPfPct;
    }

    public void setDefaultPfPct(BigDecimal defaultPfPct) {
        this.defaultPfPct = defaultPfPct;
    }

    public BigDecimal getDefaultPtax() {
        return defaultPtax;
    }

    public void setDefaultPtax(BigDecimal defaultPtax) {
        this.defaultPtax = defaultPtax;
    }
}
