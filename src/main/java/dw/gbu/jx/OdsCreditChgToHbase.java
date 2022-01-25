package dw.gbu.jx;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dw.gbu.utils.PhoenixConnectUtil;
import dw.gbu.utils.PropertiesUtil;
import dw.gbu.utils.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.*;
import java.text.SimpleDateFormat;

public class OdsCreditChgToHbase extends RichSinkFunction<ObjectNode> implements Serializable {
    PreparedStatement ps;
    private Connection connection;
    private static final Logger log = Logger.getLogger(OdsCreditChgToHbase.class);

    public static SimpleDateFormat sdf_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");

    public OdsCreditChgToHbase() {
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
        //connection = getConnection();
        PhoenixConnectUtil phoenixConnectUtil = new PhoenixConnectUtil();
        connection = phoenixConnectUtil.getConnection();
    }

    @Override
    public void close() throws Exception {
        super.close();
        //关闭连接和释放资源

        if (connection != null) {
            connection.close();
        }
        if (ps != null) {
            ps.close();
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
        log.info("objectNode:" + objectNode);
        JSONObject jsonObject = JSONObject.parseObject(objectNode.get("value").toString());
        String type = jsonObject.get("type").toString();
        log.info("objectNode-type:" + type);
        JSONArray datas = (JSONArray) jsonObject.get("data");
        PropertiesUtil prop = new PropertiesUtil("/sqls.properties");

        String phoenix_credit_chg_i = prop.getValueByKey("phoenix_credit_chg_i");
        String sql_d = "DELETE FROM \"GBU_ODS_CREDIT\".\"ODS_T_EC_CREDIT_CHG\" WHERE \"id\" = ?";
        JSONObject jsonObjectData;
        StringUtils stringUtils = new StringUtils();

        if ("INSERT".equals(type) || "UPDATE".equals(type)) {
            for (Object data : datas) {
                log.info("objectNode-data:" + data);
                jsonObjectData = JSONObject.parseObject(data.toString());

                PreparedStatement ps = connection.prepareStatement(phoenix_credit_chg_i);
                ps.setString(1, stringUtils.toString(jsonObjectData.get("id")));
                ps.setString(2, stringUtils.toString(jsonObjectData.get("biz_code")));
                ps.setString(3, stringUtils.toString(jsonObjectData.get("credit_code")));
                ps.setString(4, stringUtils.toString(jsonObjectData.get("bill_code")));
                ps.setDouble(5, stringUtils.toDouble(jsonObjectData.get("before_amt"),2));
                ps.setString(6, stringUtils.toString((jsonObjectData.get("diff_flag"))));
                ps.setDouble(7, stringUtils.toDouble(jsonObjectData.get("diff_amt"),2));
                ps.setDouble(8, stringUtils.toDouble(jsonObjectData.get("after_amt"),2));
                ps.setString(9, stringUtils.toString(jsonObjectData.get("cchg_ver")));
                ps.setString(10, stringUtils.toString(jsonObjectData.get("cchg_status")));
                ps.setString(11, stringUtils.toString(jsonObjectData.get("confirm_status")));
                ps.setDouble(12, stringUtils.toDouble(jsonObjectData.get("before_ab_amt"),2));
                ps.setDouble(13, stringUtils.toDouble(jsonObjectData.get("after_ab_amt"),2));
                ps.setString(14, stringUtils.toString(jsonObjectData.get("cchg_reason")));
                ps.setString(15, stringUtils.toString(jsonObjectData.get("cchg_name")));
                ps.setString(16, stringUtils.toString(jsonObjectData.get("cchg_type")));
                ps.setString(17, null==jsonObjectData.get("diff_date")?null:stringUtils.toString(jsonObjectData.get("diff_date")).substring(0,19));
                ps.setString(18, stringUtils.toString(jsonObjectData.get("remark")));
                ps.setString(19, stringUtils.toString(jsonObjectData.get("created_code")));
                ps.setString(20, stringUtils.toString(jsonObjectData.get("created_name")));
                ps.setString(21, stringUtils.toString(jsonObjectData.get("created_mobile")));
                ps.setString(22, null==jsonObjectData.get("created_time")?null:stringUtils.toString(jsonObjectData.get("created_time")).substring(0,19));
                ps.setString(23, stringUtils.toString(jsonObjectData.get("modified_code")));
                ps.setString(24, stringUtils.toString(jsonObjectData.get("modified_name")));
                ps.setString(25, stringUtils.toString(jsonObjectData.get("modified_mobile")));
                ps.setString(26, null==jsonObjectData.get("modified_time")?null:stringUtils.toString(jsonObjectData.get("modified_time")).substring(0,19));
                ps.setString(27, stringUtils.toString(jsonObjectData.get("department_code")));
                ps.setString(28, stringUtils.toString(jsonObjectData.get("company_code")));
                ps.setString(29, stringUtils.toString(jsonObjectData.get("version")));
                ps.setString(30, stringUtils.toString(jsonObjectData.get("status")));
                ps.setString(31, stringUtils.toString(jsonObjectData.get("yn")));
                ps.setString(32, stringUtils.toString(jsonObjectData.get("auth")));
                ps.execute();
            }
        }

        if ("DELETE".equals(type)) {
            PreparedStatement ps = connection.prepareStatement(sql_d);
            String credit_status = "";
            String confirm_status = "";
            String yn = "";
            for (Object data : datas) {
                log.info("objectNode-data:" + data);
                jsonObjectData = JSONObject.parseObject(data.toString());
               // credit_status = jsonObjectData.get("credit_status").toString();
               // confirm_status = jsonObjectData.get("confirm_status").toString();
               // yn = jsonObjectData.get("yn").toString();
                ps.setString(1, jsonObjectData.get("id").toString());
                log.info("objectNode-data-id:" + jsonObjectData.get("id").toString());

                /**
                if ("70".equals(credit_status) && "25".equals(confirm_status) && "1".equals(yn)) {
                    PreparedStatement ps_confirm = connection.prepareStatement(sql_confirm);
                    ps_confirm.setString(1, jsonObjectData.get("bill_code").toString());
                    ps_confirm.setString(2, sdf_date.format(new Date()));
                    ps_confirm.setString(3, jsonObjectData.get("comp_code").toString());
                    ps_confirm.setTimestamp(4, Timestamp.valueOf(sdf_time.format(new Date())));
                    ps_confirm.execute();
                }
                 **/
                ps.execute();
            }
        }
        connection.commit();
    }

}
