package dw.gbu.jx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dw.gbu.utils.StringUtils;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.sql.*;

public class CreditAuditOdsSchema implements SerializationSchema<ObjectNode> {
    private static final Logger log = LoggerFactory.getLogger(CreditInfoOdsHbase.class);
    @Override
    public byte[] serialize(ObjectNode jsonNode) {

        byte[] datas_byte = null;
        log.info("objectNode:"+jsonNode);
        JSONObject jsonObject = JSONObject.parseObject(jsonNode.get("value").toString());

        log.info("objectNode-data:"+jsonObject.get("data"));
        log.info("objectNode-data:"+jsonObject.get("data").toString());
        StringUtils stringUtils = new StringUtils();
        JSONArray dates = (JSONArray) jsonObject.get("data");
        String data_all = "";
        JSONObject jsonObjectData =null;
        JSONArray datasUse = new JSONArray();  //有效的数据
        String cchg_type = "";
        for(Object data:dates) {
            jsonObjectData = JSONObject.parseObject(data.toString());
            String oper_type = jsonObjectData.get("oper_type").toString();
            String after_status = jsonObjectData.get("after_status").toString();
            if ("2".equals(oper_type) && "70".equals(after_status)) { //创建授信并且核心企业确认了
                cchg_type = "0";
                datasUse.add(data);
                //data_all = "{\"cchg_type\":\"1\",\"data\":"+datasUse.toString()+"}";
            } else if ("3".equals(oper_type) && "70".equals(after_status)) {
                cchg_type = this.getChgType(jsonObjectData.get("source_code").toString());
                if ("1".equals(cchg_type)) { //修改授信额度
                    cchg_type = "1";
                    datasUse.add(data);
                }
                if ("2".equals(cchg_type)) { //修改授信截止日期
                    cchg_type = "2";
                    datasUse.add(data);
                }
            }
        }
        if(datasUse.size()==0){
            return "{\"data\":\" \"}".getBytes(StandardCharsets.UTF_8);
        }

        data_all = "{\"cchg_type\":\""+cchg_type+"\",\"data\":"+datasUse.toString()+"}";

        /**
        if("INSERT".equals(type) || "DELETE".equals(type)){
            JSONArray dates = (JSONArray) jsonObject.get("data");
            JSONArray datasUse = new JSONArray();  //有效的数据
            JSONObject jsonObjectData =null;
            for(Object data:dates){
                jsonObjectData = JSONObject.parseObject(data.toString());
                String credit_status = jsonObjectData.get("credit_status").toString();
                String confirm_status = jsonObjectData.get("confirm_status").toString();
                String yn = jsonObjectData.get("yn").toString();
                String modified_name = jsonObjectData.get("modified_name").toString();

                //当操作类型是INSERT或DELETE时，需要系统自动操作的数据才触发汇总操作
                if("70".equals(credit_status) && "25".equals(confirm_status)  && "1".equals(yn) && "系统自动".equals(modified_name)
                ){
                    datasUse.add(data);
                }
            }
            if(datasUse.size()==0){
                return "{\"data\":\" \"}".getBytes(StandardCharsets.UTF_8);
               //return new byte[0];
            }
            data_all = "{\"type\":\""+type+"\",\"data\":"+datasUse.toString()+"}";
        }
        if("UPDATE".equals(type)){

            JSONArray olds = (JSONArray) jsonObject.get("old");
            JSONArray datasUse = new JSONArray();  //有效的数据
            JSONArray oldsUse = new JSONArray();  //有效的数据
            JSONObject jsonObjectData =null;
            JSONObject old = null;
            int i=0;
            for(Object data:dates){
                jsonObjectData = JSONObject.parseObject(data.toString());
                String credit_status = jsonObjectData.get("credit_status").toString();
                String confirm_status = jsonObjectData.get("confirm_status").toString();
                String yn = jsonObjectData.get("yn").toString();
                old = JSONObject.parseObject(olds.get(i).toString());
                String credit_status_old = utils.nullToString(old.get("credit_status")).toString();
                String confirm_status_old = utils.nullToString(old.get("confirm_status")).toString();
                String yn_old = utils.nullToString(old.get("yn")).toString();
                String modified_name = jsonObjectData.get("modified_name").toString();

                //当操作类型是update时，数据只有从有效到无效或者从无效到有效，才需要将数据传递给下游
                //当credit_status=70并且confirm_status=25并且yn=1时才是有效数据
                if("系统自动".equals(modified_name) &&
                        ("70".equals(credit_status) || "70".equals(credit_status_old)) &&
                        ("25".equals(confirm_status) || "25".equals(confirm_status_old)) &&
                        ("1".equals(yn) || "1".equals(yn_old))
                ){
                    datasUse.add(data);
                    oldsUse.add(old);
                }
                i++;
            }

            if(datasUse.size()==0){
                return "{\"data\":\" \"}".getBytes(StandardCharsets.UTF_8);
                //return new byte[0];
            }
            //当操作类型是update时，需要将修改前和修改后的数据都传给下游
            data_all = "{\"type\":\""+type+"\",\"data\":"+datasUse.toString()+",\"old\":"+oldsUse.toString()+"}";
        }
        **/
        datas_byte = data_all.getBytes(StandardCharsets.UTF_8);

        return datas_byte;
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
            log.info("获取连接:{} 成功...", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //设置手动提交
        conn.setAutoCommit(false);
        return conn;
    }

    public String getChgType(String bill_code) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        String cchg_type = null;
        try{
            connection = getConnection();
            String sql = "SELECT cchg_type FROM \"GBU_ODS_CREDIT\".\"ODS_T_EC_CREDIT_CHG\" WHERE \"i1\".\"bill_code\" = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1,bill_code);
            resultSet = ps.executeQuery();
            if(null !=resultSet){
                cchg_type = resultSet.getString(1);
            }
        }catch (Exception e){
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
            if(null != connection){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return cchg_type;
    }
}
