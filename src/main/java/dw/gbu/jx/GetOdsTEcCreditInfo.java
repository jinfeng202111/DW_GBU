package dw.gbu.jx;

import dw.gbu.beans.OdsTEcCreditChg;
import dw.gbu.beans.OdsTEcCreditInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class GetOdsTEcCreditInfo {
    private static final Logger log = LoggerFactory.getLogger(GetOdsTEcCreditInfo.class);

    public OdsTEcCreditInfo getCreditInfo(Connection connection,String bill_code) throws SQLException {
        String sql = "SELECT * FROM \"GBU_ODS_CREDIT\".\"ODS_T_EC_CREDIT_INFO\" WHERE \"i1\".\"credit_status\"='70'  and \"i1\".\"yn\"='1' and " +
                " \"i1\".\"bill_code\" = ? ORDER BY \"id\" DESC LIMIT 1";
        PreparedStatement ps = null;
        ResultSet resultSet =null;
        OdsTEcCreditInfo creditInfo = new OdsTEcCreditInfo();
        try{
            ps = connection.prepareStatement(sql);
            ps.setString(1,bill_code);
            resultSet = ps.executeQuery();
            int i = 0;
            while(resultSet.next() && i==0){
                creditInfo.setId(resultSet.getString(1));
                creditInfo.setBizCode(resultSet.getString(2));
                creditInfo.setBillCode(resultSet.getString(3));
                creditInfo.setCreditCame(resultSet.getString(4));
                creditInfo.setCompCode(resultSet.getString(5));
                creditInfo.setCreditType(resultSet.getString(6));
                creditInfo.setCreditUse(resultSet.getString(7));
                creditInfo.setGuaranteeCode(resultSet.getString(8));
                creditInfo.setTotalAmt(resultSet.getDouble(9));
                creditInfo.setStartDate(null == resultSet.getString(10)?null:Timestamp.valueOf(resultSet.getString(10)));
                creditInfo.setEndDate(null == resultSet.getString(11)?null:Timestamp.valueOf(resultSet.getString(11)));
                creditInfo.setCreditStatus(resultSet.getString(12));
                creditInfo.setConfirmStatus(resultSet.getString(13));
                creditInfo.setLockStatus(resultSet.getString(14));
                creditInfo.setEnableStatus(resultSet.getString(15));
                creditInfo.setRemark(resultSet.getString(16));
                creditInfo.setCreatedCode(resultSet.getString(17));
                creditInfo.setCreatedName(resultSet.getString(18));
                creditInfo.setCreatedMobile(resultSet.getString(19));
                creditInfo.setCreatedTime(null == resultSet.getString(20)?null:Timestamp.valueOf(resultSet.getString(20)));
                creditInfo.setModifiedCode(resultSet.getString(21));
                creditInfo.setModifiedName(resultSet.getString(22));
                creditInfo.setModifiedMobile(resultSet.getString(23));
                creditInfo.setModifiedTime(null == resultSet.getString(24)?null:Timestamp.valueOf(resultSet.getString(24)));
                creditInfo.setDepartmentCode(resultSet.getString(25));
                creditInfo.setCompanyCode(resultSet.getString(26));
                creditInfo.setVersion(resultSet.getString(27));
                creditInfo.setStatus(resultSet.getString(28));
                creditInfo.setYn(resultSet.getString(29));
                creditInfo.setAuth(resultSet.getString(30));
                creditInfo.setFactCode(resultSet.getString(31));
                creditInfo.setFundingCompCode(resultSet.getString(32));
                creditInfo.setFundingCompRole(resultSet.getString(33));
                i++;
            }

        }catch (Exception e){
            log.error("查询授信变更信息失败");
            e.printStackTrace();
        }finally {
            if(null != resultSet){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(null != ps){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return creditInfo;
    }
}
