package dw.gbu.jx;

import dw.gbu.beans.OdsTEcCreditChg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class GetOdsTEcCreditChgInfo {
    private static final Logger log = LoggerFactory.getLogger(GetOdsTEcCreditChgInfo.class);

    public OdsTEcCreditChg getCreditChgInfo(Connection connection,String bill_code) {
        String sql = "SELECT * FROM \"GBU_ODS_CREDIT\".\"ODS_T_EC_CREDIT_CHG\" WHERE \"i1\".\"bill_code\" = ? ORDER BY \"id\" DESC LIMIT 1";
        PreparedStatement ps = null ;
        ResultSet resultSet = null;
        OdsTEcCreditChg creditChg = new OdsTEcCreditChg();
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1,bill_code);
            resultSet = ps.executeQuery();

            int i = 0;
            while(resultSet.next() && i==0){
                creditChg.setId(resultSet.getString(1));
                creditChg.setBizCode(resultSet.getString(2));
                creditChg.setCreditCode(resultSet.getString(3));
                creditChg.setBillCode(resultSet.getString(4));
                creditChg.setBeforeAmt(resultSet.getDouble(5));
                creditChg.setDiffFlag(resultSet.getString(6));
                creditChg.setDiffAmt(resultSet.getDouble(7));
                creditChg.setAfterAmt(resultSet.getDouble(8));
                creditChg.setCchgVer(resultSet.getString(9));
                creditChg.setCchgStatus(resultSet.getString(10));
                creditChg.setConfirmStatus(resultSet.getString(11));
                creditChg.setBeforeAbAmt(resultSet.getDouble(12));
                creditChg.setAfterAbAmt(resultSet.getDouble(13));
                creditChg.setCchgReason(resultSet.getString(14));
                creditChg.setCchgName(resultSet.getString(15));
                creditChg.setCchgType(resultSet.getString(16));
                creditChg.setDiffDate(null == resultSet.getString(17)?null:Timestamp.valueOf(resultSet.getString(17)));
                creditChg.setRemark(resultSet.getString(18));
                creditChg.setCreatedCode(resultSet.getString(19));
                creditChg.setCreatedName(resultSet.getString(20));
                creditChg.setCreatedMobile(resultSet.getString(21));
                creditChg.setCreatedTime(null == resultSet.getString(22)?null:Timestamp.valueOf(resultSet.getString(22)));
                creditChg.setModifiedCode(resultSet.getString(23));
                creditChg.setModifiedName(resultSet.getString(24));
                creditChg.setModifiedMobile(resultSet.getString(25));
                creditChg.setModifiedTime(null == resultSet.getString(26)?null:Timestamp.valueOf(resultSet.getString(26)));
                creditChg.setDepartmentCode(resultSet.getString(27));
                creditChg.setCompanyCode(resultSet.getString(28));
                creditChg.setVersion(resultSet.getString(29));
                creditChg.setStatus(resultSet.getString(30));
                creditChg.setYn(resultSet.getString(31));
                creditChg.setAuth(resultSet.getString(32));
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
        return creditChg;
    }
}
