package dw.gbu.test;



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


public class HbaseTwoPhaseCommitSink extends TwoPhaseCommitSinkFunction<ObjectNode, Connection,Void> {
    private static final Logger log = LoggerFactory.getLogger(HbaseTwoPhaseCommitSink.class);

    public HbaseTwoPhaseCommitSink(){
        super(new KryoSerializer<>(Connection.class,new ExecutionConfig()), VoidSerializer.INSTANCE);
    }

    /**
     * 执行数据库入库操作  task初始化的时候调用
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
        String sql_i = "UPSERT into \"gbuvoucher\".\"test1\"(\"id\",\"info\".\"cust_num\",\"info\".\"fee\",\"extra_info\".\"last_modified_time\") " +
                " values (?,?,?,?)";
        String sql_d = "DELETE FROM \"gbuvoucher\".\"test1\" WHERE \"id\" = ?";
        JSONObject jsonObjectData ;
/**
            if("INSERT".equals(type)){
                PreparedStatement ps = connection.prepareStatement(sql_i);
                for(Object data:datas){
                    log.info("objectNode-data:"+data);
                    jsonObjectData = JSONObject.parseObject(data.toString());
                    ps.setString(1,jsonObjectData.get("id").toString());
                    ps.setString(2,jsonObjectData.get("cust_num").toString());
                    ps.setBigDecimal(3,new BigDecimal(jsonObjectData.get("fee").toString()));
                    ps.setTimestamp(4,Timestamp.valueOf(jsonObjectData.get("last_modified_time").toString()));
                    log.info("objectNode-data-last_modified_time:"+jsonObjectData.get("last_modified_time").toString());
                    ps.execute();
                }
            }
            if("DELETE".equals(type)){
                PreparedStatement ps = connection.prepareStatement(sql_d);

                for(Object data:datas){
                    log.info("objectNode-data:"+data);
                    jsonObjectData = JSONObject.parseObject(data.toString());
                    ps.setString(1,jsonObjectData.get("id").toString());
                    log.info("objectNode-data-id:"+jsonObjectData.get("id").toString());
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
        System.out.println("start beginTransaction.......");
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
