package dw.gbu.init;

import dw.gbu.utils.MysqlConnectUtil;
import dw.gbu.utils.PhoenixConnectUtil;
import dw.gbu.utils.PropertiesUtil;
import dw.gbu.utils.StringUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CreditAudit {
    private static final org.apache.log4j.Logger log = Logger.getLogger(CreditAudit.class);
    public static void main(String[] args)  {

        Connection mysqlConnection = null;
        Connection phoenixConn = null;
        PreparedStatement ps_mysql_init_credit_audit = null;
        PreparedStatement ps_phoenix_credit_audit_i = null;
        try{
            PropertiesUtil prop = new PropertiesUtil("/sqls.properties");
            MysqlConnectUtil mysqlUtil = new MysqlConnectUtil();
            mysqlConnection = mysqlUtil.getConnection();
            String mysql_init_credit_audit = prop.getValueByKey("mysql_init_credit_audit");
            String phoenix_credit_audit_i = prop.getValueByKey("phoenix_credit_audit_i");
            log.info("phoenix_credit_audit_i:"+phoenix_credit_audit_i);
            ps_mysql_init_credit_audit = mysqlConnection.prepareStatement(mysql_init_credit_audit);
            phoenixConn = PhoenixConnectUtil.getConnection();
            ps_phoenix_credit_audit_i = phoenixConn.prepareStatement(phoenix_credit_audit_i);
            ResultSet mysql_resultSet = ps_mysql_init_credit_audit.executeQuery();
            StringUtils stringUtils = new StringUtils();
            int i=0;
            while (mysql_resultSet.next() ){
                ps_phoenix_credit_audit_i.setString(1, stringUtils.toString(mysql_resultSet.getString(1)));
                ps_phoenix_credit_audit_i.setString(2, stringUtils.toString(mysql_resultSet.getString(2)));
                ps_phoenix_credit_audit_i.setString(3, stringUtils.toString(mysql_resultSet.getString(3)));
                ps_phoenix_credit_audit_i.setString(4, stringUtils.toString(mysql_resultSet.getString(4)));
                ps_phoenix_credit_audit_i.setString(5, stringUtils.toString(mysql_resultSet.getString(5)));
                ps_phoenix_credit_audit_i.setString(6, null==mysql_resultSet.getString(6)?null:stringUtils.toString(mysql_resultSet.getString(6)).substring(0,19));
                ps_phoenix_credit_audit_i.setString(7, stringUtils.toString(mysql_resultSet.getString(7)));
                ps_phoenix_credit_audit_i.setString(8, stringUtils.toString(mysql_resultSet.getString(8)));
                ps_phoenix_credit_audit_i.setString(9, stringUtils.toString(mysql_resultSet.getString(9)));
                ps_phoenix_credit_audit_i.setString(10, stringUtils.toString(mysql_resultSet.getString(10)));
                ps_phoenix_credit_audit_i.setString(11, stringUtils.toString(mysql_resultSet.getString(11)));
                ps_phoenix_credit_audit_i.setString(12, stringUtils.toString(mysql_resultSet.getString(12)));
                ps_phoenix_credit_audit_i.setString(13, stringUtils.toString(mysql_resultSet.getString(13)));
                ps_phoenix_credit_audit_i.setString(14, null==mysql_resultSet.getString(14)?null:stringUtils.toString(mysql_resultSet.getString(14)).substring(0,19));
                ps_phoenix_credit_audit_i.setString(15, stringUtils.toString(mysql_resultSet.getString(15)));
                ps_phoenix_credit_audit_i.setString(16, stringUtils.toString(mysql_resultSet.getString(16)));
                ps_phoenix_credit_audit_i.setString(17, stringUtils.toString(mysql_resultSet.getString(17)));
                ps_phoenix_credit_audit_i.setString(18, null==mysql_resultSet.getString(18)?null:stringUtils.toString(mysql_resultSet.getString(18)).substring(0,19));
                ps_phoenix_credit_audit_i.setString(19, stringUtils.toString(mysql_resultSet.getString(19)));
                ps_phoenix_credit_audit_i.setString(20, stringUtils.toString(mysql_resultSet.getString(20)));
                ps_phoenix_credit_audit_i.setString(21, stringUtils.toString(mysql_resultSet.getString(21)));
                ps_phoenix_credit_audit_i.setString(22, stringUtils.toString(mysql_resultSet.getString(22)));
                ps_phoenix_credit_audit_i.setString(23, stringUtils.toString(mysql_resultSet.getString(23)));
                ps_phoenix_credit_audit_i.setString(24, stringUtils.toString(mysql_resultSet.getString(24)));
                ps_phoenix_credit_audit_i.execute();
                i++;
                log.info("插入授信审批信息id:"+mysql_resultSet.getString(1));
                if(0 == i%100){ //100条记录提交一次
                    phoenixConn.commit();
                }
            }
            phoenixConn.commit();
        }catch (Exception e){
            log.info("插入授信审批信息失败");
            e.printStackTrace();
        }finally {
            if(null != ps_mysql_init_credit_audit){
                try {
                    ps_mysql_init_credit_audit.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(null != ps_phoenix_credit_audit_i){
                try {
                    ps_phoenix_credit_audit_i.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if(null != mysqlConnection){
            try {
                mysqlConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(null != phoenixConn){
            try {
                phoenixConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
