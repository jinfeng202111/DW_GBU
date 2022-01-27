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
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 初始化有效/失效授信数据
 */
public class CreditValid {
    private static final Logger log = Logger.getLogger(CreditValid.class);
    public static void main(String[] args)  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Connection phoenixConn = null;
        PreparedStatement ps = null;

        try{
            PropertiesUtil prop = new PropertiesUtil("/sqls.properties");
            phoenixConn = PhoenixConnectUtil.getConnection();

            //获得最小的开始日期
            String init_get_start_date = prop.getValueByKey("init_get_start_date");
            ps = phoenixConn.prepareStatement(init_get_start_date);
            ResultSet result = ps.executeQuery();
            result.next();
            String startStr = result.getString(1);

            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            startDate.setTime(sdf.parse(startStr));

            CreditService credit = new CreditService();
            //循环每一天得到有效失效数据
            while (startDate.compareTo(endDate)<0) {
                log.info("初始化有效/失效授信数据，初始化日期："+sdf.format(startDate.getTime()));
                //记录指定日期指定核心企业的有效失效授信
                credit.creditValidDay(phoenixConn,ps,null,sdf.format(startDate.getTime()));

                //统计指定日期指定核心企业的有效失效授信
                credit.creditValidAmt(phoenixConn,ps,null,sdf.format(startDate.getTime()));

                //统计指定日期指定核心企业的有效失效授信比率
                credit.creditValidBl(phoenixConn,ps,null,sdf.format(startDate.getTime()));
                System.out.println(sdf.format(startDate.getTime()));
                // 天数加上1
                startDate.add(Calendar.DAY_OF_MONTH, 1);
            }

        }catch (Exception e){
            log.info("初始化有效/失效授信数据失败");
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
