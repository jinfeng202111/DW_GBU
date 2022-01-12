package dw.gbu.init;

import dw.gbu.service.CreditService;
import dw.gbu.utils.MysqlConnectUtil;
import dw.gbu.utils.PhoenixConnectUtil;
import dw.gbu.utils.PropertiesUtil;
import dw.gbu.utils.StringUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditStatus {
    private static final org.apache.log4j.Logger log = Logger.getLogger(CreditStatus.class);
    public static void main(String[] args)  {

        Connection phoenixConn = null;
        PreparedStatement ps = null;

        try{
            PropertiesUtil prop = new PropertiesUtil("/sqls.properties");
            phoenixConn = PhoenixConnectUtil.getConnection();

            //先初始化授信创建后的数据
            String init_credit_status_create = prop.getValueByKey("init_credit_status_create");
            ps = phoenixConn.prepareStatement(init_credit_status_create);
            phoenixConn.commit();

            //再初始化授信信息修改后的数据
            String init_get_credit_chg = prop.getValueByKey("init_get_credit_chg");
            ps = phoenixConn.prepareStatement(init_get_credit_chg);
            ResultSet chgResult = ps.executeQuery();

            StringUtils stringUtils = new StringUtils();
            String credit_code = null;
            String approval_date = null;
            String cchg_type = null;
            Double after_amt = null;
            Double before_amt = null;
            String end_time = null;

            CreditService credit = new CreditService();

            while (chgResult.next()){
                credit_code = chgResult.getString(1);
                approval_date = null==chgResult.getString(2)?null:stringUtils.toString(chgResult.getString(2)).substring(0,19);
                cchg_type = chgResult.getString(3);
                after_amt = stringUtils.toDouble(chgResult.getDouble(4),2);
                before_amt = stringUtils.toDouble(chgResult.getDouble(5),2);
                end_time = null==chgResult.getString(6)?null:stringUtils.toString(chgResult.getString(6)).substring(0,19);
                if("1".equals(cchg_type)){ //修改授信额度
                    //记录授信额度修改后的信息
                    credit.creditStatusAmtchg(phoenixConn,ps,credit_code,approval_date,after_amt,stringUtils.toDouble(after_amt-before_amt,2));
                }
                if("1".equals(cchg_type)){ //修改授信截止日期
                    //记录授信截止日期修改后的信息
                    credit.creditStatusEndtimechg(phoenixConn,ps,credit_code,approval_date, end_time);
                }
            }


        }catch (Exception e){
            log.info("初始化授信状态信息失败");
            e.printStackTrace();
        }finally {
            if(null != ps){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
