package dw.gbu.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OdsTEcCreditChg {
    private String id;
    private String bizCode;
    private String creditCode;
    private String billCode;
    private Double beforeAmt;
    private String diffFlag      ;
    private Double diffAmt       ;
    private Double afterAmt      ;
    private String cchgVer       ;
    private String cchgStatus    ;
    private String confirmStatus ;
    private Double beforeAbAmt   ;
    private Double afterAbAmt    ;
    private String cchgReason    ;
    private String cchgName      ;
    private String cchgType      ;
    private Timestamp diffDate      ;
    private String remark        ;
    private String createdCode   ;
    private String createdName   ;
    private String createdMobile ;
    private Timestamp createdTime   ;
    private String modifiedCode ;
    private String modifiedName  ;
    private String modifiedMobile;
    private Timestamp modifiedTime  ;
    private String departmentCode;
    private String companyCode   ;
    private String version       ;
    private String status        ;
    private String yn            ;
    private String auth          ;

    public OdsTEcCreditChg() {
    }

    public OdsTEcCreditChg(String id, String bizCode, String creditCode, String billCode, Double beforeAmt, String diffFlag, Double diffAmt, Double afterAmt, String cchgVer, String cchgStatus, String confirmStatus, Double beforeAbAmt, Double afterAbAmt, String cchgReason, String cchgName, String cchgType, Timestamp diffDate, String remark, String createdCode, String createdName, String createdMobile, Timestamp createdTime, String modifiedCode, String modifiedName, String modifiedMobile, Timestamp modifiedTime, String departmentCode, String companyCode, String version, String status, String yn, String auth) {
        this.id = id;
        this.bizCode = bizCode;
        this.creditCode = creditCode;
        this.billCode = billCode;
        this.beforeAmt = beforeAmt;
        this.diffFlag = diffFlag;
        this.diffAmt = diffAmt;
        this.afterAmt = afterAmt;
        this.cchgVer = cchgVer;
        this.cchgStatus = cchgStatus;
        this.confirmStatus = confirmStatus;
        this.beforeAbAmt = beforeAbAmt;
        this.afterAbAmt = afterAbAmt;
        this.cchgReason = cchgReason;
        this.cchgName = cchgName;
        this.cchgType = cchgType;
        this.diffDate = diffDate;
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

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Double getBeforeAmt() {
        return beforeAmt;
    }

    public void setBeforeAmt(Double beforeAmt) {
        this.beforeAmt = beforeAmt;
    }

    public String getDiffFlag() {
        return diffFlag;
    }

    public void setDiffFlag(String diffFlag) {
        this.diffFlag = diffFlag;
    }

    public Double getDiffAmt() {
        return diffAmt;
    }

    public void setDiffAmt(Double diffAmt) {
        this.diffAmt = diffAmt;
    }

    public Double getAfterAmt() {
        return afterAmt;
    }

    public void setAfterAmt(Double afterAmt) {
        this.afterAmt = afterAmt;
    }

    public String getCchgVer() {
        return cchgVer;
    }

    public void setCchgVer(String cchgVer) {
        this.cchgVer = cchgVer;
    }

    public String getCchgStatus() {
        return cchgStatus;
    }

    public void setCchgStatus(String cchgStatus) {
        this.cchgStatus = cchgStatus;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public Double getBeforeAbAmt() {
        return beforeAbAmt;
    }

    public void setBeforeAbAmt(Double beforeAbAmt) {
        this.beforeAbAmt = beforeAbAmt;
    }

    public Double getAfterAbAmt() {
        return afterAbAmt;
    }

    public void setAfterAbAmt(Double afterAbAmt) {
        this.afterAbAmt = afterAbAmt;
    }

    public String getCchgReason() {
        return cchgReason;
    }

    public void setCchgReason(String cchgReason) {
        this.cchgReason = cchgReason;
    }

    public String getCchgName() {
        return cchgName;
    }

    public void setCchgName(String cchgName) {
        this.cchgName = cchgName;
    }

    public String getCchgType() {
        return cchgType;
    }

    public void setCchgType(String cchgType) {
        this.cchgType = cchgType;
    }

    public Timestamp getDiffDate() {
        return diffDate;
    }

    public void setDiffDate(Timestamp diffDate) {
        this.diffDate = diffDate;
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

    @Override
    public String toString() {
        return "OdsTEcCreditChg{" +
                "id='" + id + '\'' +
                ", bizCode='" + bizCode + '\'' +
                ", creditCode='" + creditCode + '\'' +
                ", billCode='" + billCode + '\'' +
                ", beforeAmt=" + beforeAmt +
                ", diffFlag='" + diffFlag + '\'' +
                ", diffAmt=" + diffAmt +
                ", afterAmt=" + afterAmt +
                ", cchgVer='" + cchgVer + '\'' +
                ", cchgStatus='" + cchgStatus + '\'' +
                ", confirmStatus='" + confirmStatus + '\'' +
                ", beforeAbAmt=" + beforeAbAmt +
                ", afterAbAmt=" + afterAbAmt +
                ", cchgReason='" + cchgReason + '\'' +
                ", cchgName='" + cchgName + '\'' +
                ", cchgType='" + cchgType + '\'' +
                ", diffDate=" + diffDate +
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
                '}';
    }
}
