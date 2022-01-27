package dw.gbu.jobs;

import dw.gbu.service.CreditService;
import dw.gbu.utils.PhoenixConnectUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Credit {

    private static final Logger log = Logger.getLogger(Credit.class);

    public static void main(String[] args) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        String funName = args[0];

        if("creditValidDay".equals(funName)){  //每日跑出有效/失效授信信息
            String cntDate = args[1];
            log.info("每日跑批[跑批内容：每日跑出有效/失效授信信息]");
            log.info("参数1[跑批函数："+funName+"]");
            log.info("参数2[数据日期："+cntDate+"]");
            connection = PhoenixConnectUtil.getConnection();

            CreditService creditService = new CreditService();

            log.info("creditValidDay开始执行");
            //记录指定日期指定核心企业的有效失效授信
            creditService.creditValidDay(connection,ps,null,cntDate);
            log.info("creditValidDay执行完成");
        }

        if("creditValid".equals(funName)){  //每日统计有效/失效授信信息

            String cntDate = args[1];
            log.info("每日跑批[跑批内容：每日统计有效/失效授信信息]");
            log.info("参数1[跑批函数："+funName+"]");
            log.info("参数2[数据日期："+cntDate+"]");
            connection = PhoenixConnectUtil.getConnection();
            CreditService creditService = new CreditService();

            log.info("creditValid开始执行");
            //统计指定日期指定核心企业的有效失效授信额度
            creditService.creditValidAmt(connection,ps,null,cntDate);

            //统计指定日期指定核心企业的有效失效授信比率
            creditService.creditValidBl(connection,ps,null,cntDate);

            log.info("creditValid执行完成");

        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
