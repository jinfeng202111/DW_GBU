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

public class OdsCreditInfoToHbase extends RichSinkFunction<ObjectNode> implements Serializable {
    PreparedStatement ps;
    private Connection connection;
    private static final Logger log = Logger.getLogger(OdsCreditInfoToHbase.class);
    public static SimpleDateFormat sdf_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");

    public OdsCreditInfoToHbase() {
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
        if (ps != null) {
            ps.close();
        }
        if (connection != null) {
            connection.close();
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

        String phoenix_credit_info_i = prop.getValueByKey("phoenix_credit_info_i");

        String sql_d = "DELETE FROM \"GBU_ODS_CREDIT\".\"ODS_T_EC_CREDIT_INFO\" WHERE \"id\" = ?";
       // String sql_all = "UPSERT into \"GBU_DWS_CREDIT\".\"DWS_EC_CREDIT_STATUS\"(\"credit_code\",\"mod_date\",\"i1\".\"comp_code\",\"i1\".\"confirm_time\",\"i1\".\"valid_status\",\"i1\".\"valid_time\") values (?,to_date(?),?,?,?,?)";
        //String sql_confirm = "UPSERT into \"GBU_DWS_CREDIT\".\"DWS_EC_CREDIT_STATUS\"(\"credit_code\",\"mod_date\",\"i1\".\"comp_code\",\"i1\".\"confirm_time\") values (?,to_date(?),?,?)";
        //String sql_valid = "UPSERT into \"GBU_DWS_CREDIT\".\"DWS_EC_CREDIT_STATUS\"(\"credit_code\",\"mod_date\",\"i1\".\"comp_code\",\"i1\".\"valid_status\",\"i1\".\"valid_time\") values (?,to_date(?),?,?,?)";
        JSONObject jsonObjectData;
        StringUtils stringUtils = new StringUtils();

        if ("INSERT".equals(type) || "UPDATE".equals(type)) {
            PreparedStatement ps = connection.prepareStatement(phoenix_credit_info_i);
            //String credit_status = "";
            //String confirm_status = "";
           // JSONArray olds = null;
            //JSONObject old = null;
            //int i = 0;
            for (Object data : datas) {
                log.info("objectNode-data:" + data);
                jsonObjectData = JSONObject.parseObject(data.toString());
                //credit_status = jsonObjectData.get("credit_status").toString();
                //confirm_status = jsonObjectData.get("confirm_status").toString();

                /**
                if ("UPDATE".equals(type)) {
                    olds = (JSONArray) jsonObject.get("old");
                    old = JSONObject.parseObject(olds.get(i).toString());
                    if("70".equals(credit_status) && "25".equals(confirm_status)&&"系统自动".equals(jsonObjectData.get("modified_name").toString())){
                        //核心企业确认了
                        if(null != old.get("credit_status") || null != old.get("confirm_status")
                                || null != old.get("yn")){
                            PreparedStatement ps_all = connection.prepareStatement(sql_all);
                            String valid = "1";
                            if(sdf_time.parse(jsonObjectData.get("end_date").toString()).before(new java.util.Date())){
                                valid = "0";
                            }
                            ps_all.setString(1, jsonObjectData.get("bill_code").toString());
                            ps_all.setString(2, sdf_date.format(new java.util.Date()));
                            ps_all.setString(3, jsonObjectData.get("comp_code").toString());
                            ps_all.setTimestamp(4, Timestamp.valueOf(sdf_time.format(new Date())));
                            ps_all.setString(5, valid);
                            ps_all.setTimestamp(6, Timestamp.valueOf(sdf_time.format(new Date())));
                            ps_all.execute();
                        }
                        //修改了授信额度
                        if(null != old.get("total_amt")){
                            PreparedStatement ps_confirm = connection.prepareStatement(sql_confirm);
                            ps_confirm.setString(1, jsonObjectData.get("bill_code").toString());
                            ps_confirm.setString(2, sdf_date.format(new java.util.Date()));
                            ps_confirm.setString(3, jsonObjectData.get("comp_code").toString());
                            ps_confirm.setTimestamp(4, Timestamp.valueOf(sdf_time.format(new Date())));
                            ps_confirm.execute();
                        }
                        //修改了授信截止日期
                        if(null != old.get("end_date")){
                            PreparedStatement ps_valid = connection.prepareStatement(sql_valid);
                            String valid = "1";
                            if(sdf_time.parse(jsonObjectData.get("end_date").toString()).before(new java.util.Date())){
                                valid = "0";
                            }
                            ps_valid.setString(1, jsonObjectData.get("bill_code").toString());
                            ps_valid.setString(2, sdf_date.format(new java.util.Date()));
                            ps_valid.setString(3, jsonObjectData.get("comp_code").toString());
                            ps_valid.setString(4, valid);
                            ps_valid.setTimestamp(5, Timestamp.valueOf(sdf_time.format(new Date())));
                            ps_valid.execute();
                        }
                    }
                }
**/

                ps.setString(1, stringUtils.toString(jsonObjectData.get("id")));
                ps.setString(2, stringUtils.toString(jsonObjectData.get("biz_code")));
                ps.setString(3, stringUtils.toString(jsonObjectData.get("bill_code")));
                ps.setString(4, stringUtils.toString(jsonObjectData.get("credit_name")));
                ps.setString(5, stringUtils.toString(jsonObjectData.get("comp_code")));
                ps.setString(6, stringUtils.toString((jsonObjectData.get("credit_type"))));
                ps.setString(7, stringUtils.toString(jsonObjectData.get("credit_use")));
                ps.setString(8, stringUtils.toString(jsonObjectData.get("guarantee_code")));
                ps.setDouble(9, stringUtils.toDouble(jsonObjectData.get("total_amt"),2));
                ps.setString(10, null==jsonObjectData.get("start_date")?null:stringUtils.toString(jsonObjectData.get("start_date")).substring(0,19));
                ps.setString(11, null==jsonObjectData.get("end_date")?null:stringUtils.toString(jsonObjectData.get("end_date")).substring(0,19));
                ps.setString(12, stringUtils.toString(jsonObjectData.get("credit_status")));
                ps.setString(13, stringUtils.toString(jsonObjectData.get("confirm_status")));
                ps.setString(14, stringUtils.toString(jsonObjectData.get("lock_status")));
                ps.setString(15, stringUtils.toString(jsonObjectData.get("enable_status")));
                ps.setString(16, stringUtils.toString(jsonObjectData.get("remark")));
                ps.setString(17, stringUtils.toString(jsonObjectData.get("created_code")));
                ps.setString(18, stringUtils.toString(jsonObjectData.get("created_name")));
                ps.setString(19, stringUtils.toString(jsonObjectData.get("created_mobile")));
                ps.setString(20, null==jsonObjectData.get("created_time")?null:stringUtils.toString(jsonObjectData.get("created_time")).substring(0,19));
                ps.setString(21, stringUtils.toString(jsonObjectData.get("modified_code")));
                ps.setString(22, stringUtils.toString(jsonObjectData.get("modified_name")));
                ps.setString(23, stringUtils.toString(jsonObjectData.get("modified_mobile")));
                ps.setString(24, null==jsonObjectData.get("modified_time")?null:stringUtils.toString(jsonObjectData.get("modified_time")).substring(0,19));
                ps.setString(25, stringUtils.toString(jsonObjectData.get("department_code")));
                ps.setString(26, stringUtils.toString(jsonObjectData.get("company_code")));
                ps.setString(27, stringUtils.toString(jsonObjectData.get("version")));
                ps.setString(28, stringUtils.toString(jsonObjectData.get("status")));
                ps.setString(29, stringUtils.toString(jsonObjectData.get("yn")));
                ps.setString(30, stringUtils.toString(jsonObjectData.get("auth")));
                ps.setString(31, stringUtils.toString(jsonObjectData.get("fact_code")));
                ps.setString(32, stringUtils.toString(jsonObjectData.get("funding_comp_code")));
                ps.setString(33, stringUtils.toString(jsonObjectData.get("funding_comp_role")));
                ps.execute();

                //i++;
            }
        }
        if ("DELETE".equals(type)) {
            PreparedStatement ps = connection.prepareStatement(sql_d);
            //String credit_status = "";
            //String confirm_status = "";
            //String yn = "";
            for (Object data : datas) {
                log.info("objectNode-data:" + data);
                jsonObjectData = JSONObject.parseObject(data.toString());
                //credit_status = jsonObjectData.get("credit_status").toString();
                //confirm_status = jsonObjectData.get("confirm_status").toString();
                //yn = jsonObjectData.get("yn").toString();
                ps.setString(1, jsonObjectData.get("id").toString());
                log.info("objectNode-data-id:" + jsonObjectData.get("id").toString());

                /**
                if ("70".equals(credit_status) && "25".equals(confirm_status) && "1".equals(yn)) {
                    PreparedStatement ps_confirm = connection.prepareStatement(sql_confirm);
                    ps_confirm.setString(1, jsonObjectData.get("bill_code").toString());
                    ps_confirm.setString(2, sdf_date.format(new java.util.Date()));
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
