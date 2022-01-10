package dw.gbu.jx;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dw.gbu.utils.PhoenixConnectUtil;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeutils.base.VoidSerializer;
import org.apache.flink.api.java.typeutils.runtime.kryo.KryoSerializer;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.functions.sink.TwoPhaseCommitSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;


public class CreditInfoOdsHbase extends TwoPhaseCommitSinkFunction<ObjectNode, Connection,Void> {
    private static final Logger log = LoggerFactory.getLogger(CreditInfoOdsHbase.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public CreditInfoOdsHbase(){
        super(new KryoSerializer<>(Connection.class,new ExecutionConfig()), VoidSerializer.INSTANCE);
    }

    /**
     * 执行数据库入库操作
     * @param connection
     * @param objectNode
     * @param context
     * @throws Exception
     */
    @Override
    protected void invoke(Connection connection, ObjectNode objectNode, Context context) throws Exception{
        log.info("start invoke...");

        log.info("objectNode:"+objectNode);
        JSONObject jsonObject = JSONObject.parseObject(objectNode.get("value").toString());
        String type = jsonObject.get("type").toString();
        log.info("objectNode-type:" + type);
        JSONArray datas = (JSONArray) jsonObject.get("data");
        /**
        String sql_i = "UPSERT into \"GBU_ODS_CREDIT\".\"ODS_T_EC_CREDIT_INFO\"(\"i2\".\"id\" ," +
                "\"i2\".\"biz_code\" ," +
                "\"bill_code\" ," +
                "\"i1\".\"credit_name\" ," +
                "\"i1\".\"comp_code\" ," +
                "\"i1\".\"credit_type\" ," +
                "\"i1\".\"credit_use\" ," +
                "\"i1\".\"guarantee_code\" ," +
                "\"i1\".\"total_amt\" ," +
                "\"i1\".\"start_date\" ," +
                "\"i1\".\"end_date\" ," +
                "\"i1\".\"credit_status\" ," +
                "\"i1\".\"confirm_status\" ," +
                "\"i1\".\"lock_status\" ," +
                "\"i1\".\"enable_status\" ," +
                "\"i2\".\"remark\" ," +
                "\"i2\".\"created_code\" ," +
                "\"i2\".\"created_name\" ," +
                "\"i2\".\"created_mobile\" ," +
                "\"i2\".\"created_time\" ," +
                "\"i2\".\"modified_code\" ," +
                "\"i2\".\"modified_name\" ," +
                "\"i2\".\"modified_mobile\" ," +
                "\"i1\".\"modified_time\" ," +
                "\"i2\".\"department_code\" ," +
                "\"i2\".\"company_code\" ," +
                "\"i2\".\"version\" ," +
                "\"i2\".\"status\" ," +
                "\"i1\".\"yn\"  ," +
                "\"i2\".\"auth\" ," +
                "\"i2\".\"fact_code\" ," +
                "\"i1\".\"funding_comp_code\" ," +
                "\"i1\".\"funding_comp_role\") " +
                " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sql_d = "DELETE FROM \"GBU_ODS_CREDIT\".\"ODS_T_EC_CREDIT_INFO\" WHERE \"bill_code\" = ?";
        String sql_last_mod = "UPSERT into \"GBU_DWS_CREDIT\".\"DWS_EC_CREDIT_LAST_MOD\" values (?,?,?)";
        JSONObject jsonObjectData ;
        Utils utils = new Utils();

        if("INSERT".equals(type) || "UPDATE".equals(type)){
            PreparedStatement ps = connection.prepareStatement(sql_i);
            String credit_status = "";
            String confirm_status = "";
            String credit_status_old = "";
            String confirm_status_old = "";
            JSONArray olds =null;
            JSONObject old = null;
            int i=0;
            for(Object data:datas){
                log.info("objectNode-data:"+data);
                jsonObjectData = JSONObject.parseObject(data.toString());
                credit_status = jsonObjectData.get("credit_status").toString();
                confirm_status = jsonObjectData.get("confirm_status").toString();
                if("UPDATE".equals(type)){
                    olds = (JSONArray) jsonObject.get("old");
                    old = JSONObject.parseObject(olds.get(i).toString());
                    credit_status_old = utils.NullToString(old.get("credit_status"));
                    confirm_status_old = utils.NullToString(old.get("confirm_status"));
                    if("70".equals(credit_status) && "25".equals(confirm_status) &&
                            (null!=old.get("credit_status") || null!=old.get("confirm_status")
                                    || null!=old.get("yn") || null!=old.get("total_amt"))){
                        PreparedStatement ps_last_mod = connection.prepareStatement(sql_last_mod);
                        ps_last_mod.setString(1,jsonObjectData.get("bill_code").toString());
                        ps_last_mod.setString(2,jsonObjectData.get("comp_code").toString());
                        ps_last_mod.setTimestamp(3, Timestamp.valueOf(sdf.format(new Date())));
                        ps_last_mod.execute();
                    }
                }

                ps.setInt(1, Integer.parseInt(jsonObjectData.get("id").toString()));
                ps.setString(2, jsonObjectData.get("biz_code").toString());
                ps.setString(3, jsonObjectData.get("bill_code").toString());
                ps.setString(4, jsonObjectData.get("credit_name").toString());
                ps.setString(5, jsonObjectData.get("comp_code").toString());
                ps.setInt(6, Integer.parseInt(jsonObjectData.get("credit_type").toString()));
                ps.setInt(7, Integer.parseInt(jsonObjectData.get("credit_use").toString()));
                ps.setString(8, jsonObjectData.get("guarantee_code").toString());
                ps.setBigDecimal(9, new BigDecimal(jsonObjectData.get("total_amt").toString()));
                ps.setTimestamp(10, Timestamp.valueOf(jsonObjectData.get("start_date").toString()));
                ps.setTimestamp(11, Timestamp.valueOf(jsonObjectData.get("end_date").toString()));
                ps.setInt(12, Integer.parseInt(jsonObjectData.get("credit_status").toString()));
                ps.setInt(13, Integer.parseInt(jsonObjectData.get("confirm_status").toString()));
                ps.setInt(14, Integer.parseInt(jsonObjectData.get("lock_status").toString()));
                ps.setInt(15, Integer.parseInt(jsonObjectData.get("enable_status").toString()));
                ps.setString(16, jsonObjectData.get("remark").toString());
                ps.setString(17, jsonObjectData.get("created_code").toString());
                ps.setString(18, jsonObjectData.get("created_name").toString());
                ps.setString(19, jsonObjectData.get("created_mobile").toString());
                ps.setTimestamp(20, Timestamp.valueOf(jsonObjectData.get("created_time").toString()));
                ps.setString(21, jsonObjectData.get("modified_code").toString());
                ps.setString(22, jsonObjectData.get("modified_name").toString());
                ps.setString(23, jsonObjectData.get("modified_mobile").toString());
                ps.setTimestamp(24, Timestamp.valueOf(jsonObjectData.get("modified_time").toString()));
                ps.setString(25, jsonObjectData.get("department_code").toString());
                ps.setString(26, jsonObjectData.get("company_code").toString());
                ps.setInt(27, Integer.parseInt(jsonObjectData.get("version").toString()));
                ps.setInt(28, Integer.parseInt(jsonObjectData.get("status").toString()));
                ps.setInt(29, Integer.parseInt(jsonObjectData.get("yn").toString()));
                ps.setString(30, jsonObjectData.get("auth").toString());
                ps.setString(31, jsonObjectData.get("fact_code").toString());
                ps.setString(32, jsonObjectData.get("funding_comp_code").toString());
                ps.setInt(33, Integer.parseInt(jsonObjectData.get("funding_comp_role").toString()));
                ps.execute();
                i++;
                }
        }
        if("DELETE".equals(type)){
                PreparedStatement ps = connection.prepareStatement(sql_d);

                for(Object data:datas){
                    log.info("objectNode-data:"+data);
                    jsonObjectData = JSONObject.parseObject(data.toString());
                    ps.setString(1,jsonObjectData.get("bill_code").toString());
                    log.info("objectNode-data-id:"+jsonObjectData.get("bill_code").toString());
                    ps.execute();
                }
        }
         **/
        commit(connection);

    }

    /**
     * 获取连接，开启手动提交事物（getConnection方法中）
     * @return
     * @throws Exception
     */
    @Override
    protected Connection beginTransaction() throws Exception {
        log.info("start beginTransaction.......");
        //String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false&autoReconnect=true";
        Connection connection = PhoenixConnectUtil.getConnection();
        return connection;
    }

    /**
     *预提交，这里预提交的逻辑在invoke方法中
     * @param connection
     * @throws Exception
     */
    @Override
    protected void preCommit(Connection connection) throws Exception {
        log.info("start preCommit...");
    }

    /**
     * 如果invoke方法执行正常，则提交事务
     * @param connection
     */
    @Override
    protected void commit(Connection connection) {
        log.info("start commit...");
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果invoke执行异常则回滚事物，下一次的checkpoint操作也不会执行
     * @param connection
     */
    @Override
    protected void abort(Connection connection) {
        log.info("start abort rollback...");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
