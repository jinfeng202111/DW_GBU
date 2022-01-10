package dw.gbu.jx;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dw.gbu.utils.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.*;
import java.text.SimpleDateFormat;

public class DwsCreditInfoCountToHbase extends RichSinkFunction<ObjectNode> implements Serializable {
    PreparedStatement ps_oneday;
    PreparedStatement ps_allday;
    private Connection connection;
    private static final Logger log = LoggerFactory.getLogger(OdsCreditInfoToHbase.class);
    public static  SimpleDateFormat sdf_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static  SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");
    public DwsCreditInfoCountToHbase() {
        super();


    }
    /**
     * open() 方法中建立连接，这样不用每次 invoke 的时候都要建立连接和释放连接
     *
     * @param parameters
     * @throws Exception
     */

    @Override
    public void open(Configuration parameters) throws Exception {
        connection = getConnection();
    }

    @Override
    public void close() throws Exception {
        super.close();
        //关闭连接和释放资源

        if (connection != null) {
            connection.close();
        }
        if (ps_oneday != null) {
            ps_oneday.close();
        }
        if (ps_allday != null) {
            ps_allday.close();
        }

    }

    /**
     * 每条数据的插入都要调用一次 invoke() 方法
     *
     * @param objectNode
     * @param context
     * @throws Exception
     */
    @Override
    public void invoke(ObjectNode objectNode, Context context) throws Exception {
        log.info("汇总层objectNode:"+objectNode);
        JSONObject jsonObject = JSONObject.parseObject(objectNode.get("value").toString());
        StringUtils stringUtils = new StringUtils();
        if(stringUtils.isNotNull(jsonObject.get("data"))){
            log.info("汇总层jsonObject:"+jsonObject.toString());
            JSONArray dates = (JSONArray) jsonObject.get("data");
            String sql_i = "UPSERT INTO \"GBU_DWS_CREDIT\".\"DWS_EC_CREDIT_COUNT\" (\"comp_code\",\"cnt_date\",\"i1\".\"zjf_pt_cnt\",\"i1\".\"zjf_pt_amt\",\"i1\".\"zjf_yh_cnt\",\"i1\".\"zjf_yh_amt\",\"i1\".\"use_xh_cnt\",\"i1\".\"use_xh_amt\",\"i1\".\"use_dy_cnt\",\"i1\".\"use_dy_amt\") " +
                    "select b.\"comp_code\",to_date(?),sum(zjf_pt_cnt),sum(zjf_pt_amt),sum(zjf_yh_cnt),sum(zjf_yh_amt),sum(use_xh_cnt),sum(use_xh_amt),sum(use_dy_cnt),sum(use_dy_amt) from  " +
                    "( " +
                    "select a.\"comp_code\", " +
                    "case when a.\"funding_comp_role\"='1' then count(1) else 0 end zjf_pt_cnt, " +
                    "case when a.\"funding_comp_role\"='1' then sum(a.\"total_amt\") else 0.00 end zjf_pt_amt, " +
                    "case when a.\"funding_comp_role\"='2' then count(1) else 0 end zjf_yh_cnt, " +
                    "case when a.\"funding_comp_role\"='2' then sum(a.\"total_amt\") else 0.00 end zjf_yh_amt, " +
                    "case when a.\"credit_use\"='7' then count(1) else 0 end use_xh_cnt, " +
                    "case when a.\"credit_use\"='7' then sum(a.\"total_amt\") else 0.00 end use_xh_amt, " +
                    "case when a.\"credit_use\"='8' then count(1) else 0 end use_dy_cnt, " +
                    "case when a.\"credit_use\"='8' then sum(a.\"total_amt\") else 0.00 end use_dy_amt  " +
                    "from  " +
                    "(select \"bill_code\",\"i1\".\"comp_code\",\"i1\".\"total_amt\",\"i1\".\"funding_comp_role\",\"i1\".\"credit_use\" from \"GBU_ODS_CREDIT\".\"ODS_T_EC_CREDIT_INFO\"  " +
                    "where \"i1\".\"comp_code\"=? and \"credit_status\"='70' and \"confirm_status\"='25' and \"yn\"='1' " +
                    " and \"bill_code\"  " +
                    " in( " +
                    " select \"credit_code\" from \"GBU_DWS_CREDIT\".\"DWS_EC_CREDIT_STATUS\" where \"i1\".\"comp_code\"=? and (\"i1\".\"confirm_time\" between ? and ?) " +
                    " ) " +
                    ")a group by a.\"comp_code\",a.\"funding_comp_role\",a.\"credit_use\" " +
                    ")b group by b.\"comp_code\"";
            ps_oneday = connection.prepareStatement(sql_i);
            ps_allday = connection.prepareStatement(sql_i);
            JSONObject jsonObjectData = null;
            for(Object data:dates){
                log.info("汇总层data:"+data.toString());
                jsonObjectData = JSONObject.parseObject(data.toString());
                Date date = new Date(new java.util.Date().getTime());
                ps_oneday.setString(1, sdf_date.format(new java.util.Date()));
                ps_allday.setString(1, "9999-12-31");
                ps_oneday.setString(2, stringUtils.toString(jsonObjectData.get("comp_code")));
                ps_allday.setString(2, stringUtils.toString(jsonObjectData.get("comp_code")));
                ps_oneday.setString(3, stringUtils.toString(jsonObjectData.get("comp_code")));
                ps_allday.setString(3, stringUtils.toString(jsonObjectData.get("comp_code")));
                ps_oneday.setTimestamp(4, Timestamp.valueOf(date.toString()+" 00:00:00"));
                ps_allday.setTimestamp(4, Timestamp.valueOf("1000-01-01 00:00:00"));
                ps_oneday.setTimestamp(5, Timestamp.valueOf(date.toString()+" 23:59:59"));
                ps_allday.setTimestamp(5, Timestamp.valueOf("9999-12-31 23:59:59"));
                ps_oneday.execute();
                ps_allday.execute();
            }
        }
        connection.commit();
    }

    private static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        } catch (ClassNotFoundException e) {
            log.error("获取org.apache.phoenix.jdbc.PhoenixDriver失败");
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection("jdbc:phoenix:slave002,slave003,slave004:2181", "", "");
            log.info("获取连接:{} 成功...",conn);
        }catch (SQLException  e){
            e.printStackTrace();
        }

        //设置手动提交
        conn.setAutoCommit(false);
        return conn;

    }

}
