package com.payroll.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class SalaryStructure implements Serializable {

    private static final long serialVersionUID = 1L;

    private int structureId;
    private String gradeName;
    private BigDecimal hraPercentage;
    private BigDecimal daPercentage;
    private BigDecimal pfPercentage;
    private BigDecimal taxPercentage;
    private BigDecimal professionalTax;

    public SalaryStructure() {
        this.hraPercentage = new BigDecimal("20.00");
        this.daPercentage = new BigDecimal("10.00");
        this.pfPercentage = new BigDecimal("12.00");
        this.taxPercentage = new BigDecimal("5.00");
        this.professionalTax = new BigDecimal("200.00");
    }

    public int getStructureId() {
        return structureId;
    }

    public void setStructureId(int structureId) {
        this.structureId = structureId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public BigDecimal getHraPercentage() {
        return hraPercentage;
    }

    public void setHraPercentage(BigDecimal hraPercentage) {
        this.hraPercentage = hraPercentage;
    }

    public BigDecimal getDaPercentage() {
        return daPercentage;
    }

    public void setDaPercentage(BigDecimal daPercentage) {
        this.daPercentage = daPercentage;
    }

    public BigDecimal getPfPercentage() {
        return pfPercentage;
    }

    public void setPfPercentage(BigDecimal pfPercentage) {
        this.pfPercentage = pfPercentage;
    }

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public BigDecimal getProfessionalTax() {
        return professionalTax;
    }

    public void setProfessionalTax(BigDecimal professionalTax) {
        this.professionalTax = professionalTax;
    }
}
