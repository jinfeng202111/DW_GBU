package dw.gbu.init;

import dw.gbu.service.CreditService;
import dw.gbu.utils.PhoenixConnectUtil;
import dw.gbu.utils.PropertiesUtil;
import dw.gbu.utils.StringUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditCount {
    private static final Logger log = Logger.getLogger(CreditCount.class);
    public static void main(String[] args)  {

        Connection phoenixConn = null;
        PreparedStatement ps = null;

        try{
            PropertiesUtil prop = new PropertiesUtil("/sqls.properties");
            phoenixConn = PhoenixConnectUtil.getConnection();

            //初始化授信统计数据
            String init_get_credit_count = prop.getValueByKey("init_get_credit_count");
            ps = phoenixConn.prepareStatement(init_get_credit_count);
            ResultSet result = ps.executeQuery();

            StringUtils stringUtils = new StringUtils();
            String comp_code = null;
            String cnt_date = null;

            CreditService credit = new CreditService();

            while (result.next()){
                comp_code = result.getString(1);
                cnt_date = null==result.getString(2)?null:stringUtils.toString(result.getString(2)).substring(0,10);

                log.info("初始化授信统计数据:【comp_code："+comp_code+",cnt_date:"+cnt_date+"】");
                //按照确认日期统计指定核心企业授信
                credit.creditCount(phoenixConn,ps,comp_code,cnt_date,cnt_date+" 00:00:00", cnt_date+" 23:59:59" );
                //统计所有的指定核心企业授信
                credit.creditCount(phoenixConn,ps,comp_code,"9999-12-31","1000-01-01 00:00:00","9999-12-31 23:59:59");

                //按照确认日期统计指定核心企业授信比率
                credit.creditCountBl(phoenixConn,ps,comp_code,cnt_date);
                //统计所有的指定核心企业授信比率
                credit.creditCountBl(phoenixConn,ps,comp_code,"9999-12-31");
            }

        }catch (Exception e){
            log.info("初始化授信统计数据失败");
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
