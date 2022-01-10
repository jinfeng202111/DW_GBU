package dw.gbu.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OdsTEcCreditInfo {
    private String id;
    private String bizCode;
    private String billCode;
    private String creditCame;
    private String compCode;
    private String creditType;
    private String creditUse;
    private String guaranteeCode;
    private Double totalAmt;
    private Timestamp startDate;
    private Timestamp endDate;
    private String creditStatus;
    private String confirmStatus;
    private String lockStatus;
    private String enableStatus;
    private String remark;
    private String createdCode;
    private String createdName;
    private String createdMobile;
    private Timestamp createdTime;
    private String modifiedCode;
    private String modifiedName;
    private String modifiedMobile;
    private Timestamp modifiedTime;
    private String departmentCode;
    private String companyCode;
    private String version;
    private String status;
    private String yn;
    private String auth;
    private String factCode;
    private String fundingCompCode;
    private String fundingCompRole;

    public OdsTEcCreditInfo() {
    }

    public OdsTEcCreditInfo(String id, String bizCode, String billCode, String creditCame, String compCode, String creditType, String creditUse, String guaranteeCode, Double totalAmt, Timestamp startDate, Timestamp endDate, String creditStatus, String confirmStatus, String lockStatus, String enableStatus, String remark, String createdCode, String createdName, String createdMobile, Timestamp createdTime, String modifiedCode, String modifiedName, String modifiedMobile, Timestamp modifiedTime, String departmentCode, String companyCode, String version, String status, String yn, String auth, String factCode, String fundingCompCode, String fundingCompRole) {
        this.id = id;
        this.bizCode = bizCode;
        this.billCode = billCode;
        this.creditCame = creditCame;
        this.compCode = compCode;
        this.creditType = creditType;
        this.creditUse = creditUse;
        this.guaranteeCode = guaranteeCode;
        this.totalAmt = totalAmt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.creditStatus = creditStatus;
        this.confirmStatus = confirmStatus;
        this.lockStatus = lockStatus;
        this.enableStatus = enableStatus;
        this.remark = remark;
        this.createdCode = createdCode;
        this.createdName = createdName;
        this.createdMobile = createdMobile;
        this.createdTime = createdTime;
        this.modifiedCode = modifiedCode;
        this.modifiedName = modifiedName;
        this.modifiedMobile = modifiedMobile;
        this.modifiedTime = modifiedTime;
        this.departmentCode = departmentCode;
        this.companyCode = companyCode;
        this.version = version;
        this.status = status;
        this.yn = yn;
        this.auth = auth;
        this.factCode = factCode;
        this.fundingCompCode = fundingCompCode;
        this.fundingCompRole = fundingCompRole;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getCreditCame() {
        return creditCame;
    }

    public void setCreditCame(String creditCame) {
        this.creditCame = creditCame;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public String getCreditUse() {
        return creditUse;
    }

    public void setCreditUse(String creditUse) {
        this.creditUse = creditUse;
    }

    public String getGuaranteeCode() {
        return guaranteeCode;
    }

    public void setGuaranteeCode(String guaranteeCode) {
        this.guaranteeCode = guaranteeCode;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(String enableStatus) {
        this.enableStatus = enableStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedCode() {
        return createdCode;
    }

    public void setCreatedCode(String createdCode) {
        this.createdCode = createdCode;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getCreatedMobile() {
        return createdMobile;
    }

    public void setCreatedMobile(String createdMobile) {
        this.createdMobile = createdMobile;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifiedCode() {
        return modifiedCode;
    }

    public void setModifiedCode(String modifiedCode) {
        this.modifiedCode = modifiedCode;
    }

    public String getModifiedName() {
        return modifiedName;
    }

    public void setModifiedName(String modifiedName) {
        this.modifiedName = modifiedName;
    }

    public String getModifiedMobile() {
        return modifiedMobile;
    }

    public void setModifiedMobile(String modifiedMobile) {
        this.modifiedMobile = modifiedMobile;
    }

    public Timestamp getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Timestamp modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYn() {
        return yn;
    }

    public void setYn(String yn) {
        this.yn = yn;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getFactCode() {
        return factCode;
    }

    public void setFactCode(String factCode) {
        this.factCode = factCode;
    }

    public String getFundingCompCode() {
        return fundingCompCode;
    }

    public void setFundingCompCode(String fundingCompCode) {
        this.fundingCompCode = fundingCompCode;
    }

    public String getFundingCompRole() {
        return fundingCompRole;
    }

    public void setFundingCompRole(String fundingCompRole) {
        this.fundingCompRole = fundingCompRole;
    }

    @Override
    public String toString() {
        return "OdsTEcCreditInfo{" +
                "id='" + id + '\'' +
                ", bizCode='" + bizCode + '\'' +
                ", billCode='" + billCode + '\'' +
                ", creditCame='" + creditCame + '\'' +
                ", compCode='" + compCode + '\'' +
                ", creditType='" + creditType + '\'' +
                ", creditUse='" + creditUse + '\'' +
                ", guaranteeCode='" + guaranteeCode + '\'' +
                ", totalAmt=" + totalAmt +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", creditStatus='" + creditStatus + '\'' +
                ", confirmStatus='" + confirmStatus + '\'' +
                ", lockStatus='" + lockStatus + '\'' +
                ", enableStatus='" + enableStatus + '\'' +
                ", remark='" + remark + '\'' +
                ", createdCode='" + createdCode + '\'' +
                ", createdName='" + createdName + '\'' +
                ", createdMobile='" + createdMobile + '\'' +
                ", createdTime=" + createdTime +
                ", modifiedCode='" + modifiedCode + '\'' +
                ", modifiedName='" + modifiedName + '\'' +
                ", modifiedMobile='" + modifiedMobile + '\'' +
                ", modifiedTime=" + modifiedTime +
                ", departmentCode='" + departmentCode + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", version='" + version + '\'' +
                ", status='" + status + '\'' +
                ", yn='" + yn + '\'' +
                ", auth='" + auth + '\'' +
                ", factCode='" + factCode + '\'' +
                ", fundingCompCode='" + fundingCompCode + '\'' +
                ", fundingCompRole='" + fundingCompRole + '\'' +
                '}';
    }
}
