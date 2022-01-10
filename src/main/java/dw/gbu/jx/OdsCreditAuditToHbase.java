package dw.gbu.jx;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dw.gbu.beans.OdsTEcCreditChg;
import dw.gbu.beans.OdsTEcCreditInfo;
import dw.gbu.service.CreditService;
import dw.gbu.utils.PhoenixConnectUtil;
import dw.gbu.utils.PropertiesUtil;
import dw.gbu.utils.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;

public class OdsCreditAuditToHbase extends RichSinkFunction<ObjectNode> implements Serializable {
    PreparedStatement ps;
    private Connection connection;
    private static final Logger log = Logger.getLogger(OdsCreditAuditToHbase.class);
    public static SimpleDateFormat sdf_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");

    public OdsCreditAuditToHbase() {
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
        PhoenixConnectUtil phoenixConnectUtil = new PhoenixConnectUtil();
        this.connection = phoenixConnectUtil.getConnection();
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

        String phoenix_credit_audit_i = prop.getValueByKey("phoenix_credit_audit_i");
        String sql_credit_audit_d = "DELETE FROM \"GBU_ODS_CREDIT\".\"ODS_T_EC_CREDIT_AUDIT\" WHERE \"id\" = ?";

        JSONObject jsonObjectData;
        StringUtils stringUtils = new StringUtils();

        if ("INSERT".equals(type) || "UPDATE".equals(type)) {

            for (Object data : datas) {
                log.info("objectNode-data:" + data);
                jsonObjectData = JSONObject.parseObject(data.toString());
                String oper_type = jsonObjectData.get("oper_type").toString();
                String after_status = jsonObjectData.get("after_status").toString();
                String approval_date = jsonObjectData.get("approval_date").toString();
                OdsTEcCreditInfo creditInfo = new OdsTEcCreditInfo();
                CreditService credit = new CreditService();

                PreparedStatement ps = connection.prepareStatement(phoenix_credit_audit_i);
                ps.setString(1, stringUtils.toString(jsonObjectData.get("id")));
                ps.setString(2, stringUtils.toString(jsonObjectData.get("biz_code")));
                ps.setString(3, stringUtils.toString(jsonObjectData.get("source_code")));
                ps.setString(4, stringUtils.toString(jsonObjectData.get("oper_type")));
                ps.setString(5, stringUtils.toString(jsonObjectData.get("approval_code")));
                ps.setString(6, null==jsonObjectData.get("approval_date")?null:stringUtils.toString(jsonObjectData.get("approval_date")).substring(0,19));
                ps.setString(7, stringUtils.toString(jsonObjectData.get("approval_memo")));
                ps.setString(8, stringUtils.toString(jsonObjectData.get("approval_role")));
                ps.setString(9, stringUtils.toString(jsonObjectData.get("after_status")));
                ps.setString(10, stringUtils.toString(jsonObjectData.get("before_status")));
                ps.setString(11, stringUtils.toString(jsonObjectData.get("created_code")));
                ps.setString(12, stringUtils.toString(jsonObjectData.get("created_name")));
                ps.setString(13, stringUtils.toString(jsonObjectData.get("created_mobile")));
                ps.setString(14, null==jsonObjectData.get("created_time")?null:stringUtils.toString(jsonObjectData.get("created_time")).substring(0,19));
                ps.setString(15, stringUtils.toString(jsonObjectData.get("modified_code")));
                ps.setString(16, stringUtils.toString(jsonObjectData.get("modified_name")));
                ps.setString(17, stringUtils.toString(jsonObjectData.get("modified_mobile")));
                ps.setString(18, null==jsonObjectData.get("modified_time")?null:stringUtils.toString(jsonObjectData.get("modified_time")).substring(0,19));
                ps.setString(19, stringUtils.toString(jsonObjectData.get("department_code")));
                ps.setString(20, stringUtils.toString(jsonObjectData.get("company_code")));
                ps.setString(21, stringUtils.toString(jsonObjectData.get("version")));
                ps.setString(22, stringUtils.toString(jsonObjectData.get("status")));
                ps.setString(23, stringUtils.toString(jsonObjectData.get("yn")));
                ps.setString(24, stringUtils.toString(jsonObjectData.get("auth")));
                ps.execute();
                connection.commit();

                if("2".equals(oper_type) && "70".equals(after_status)){ //创建授信并且核心企业确认了，记录状态信息
                    String creditCode = jsonObjectData.get("source_code").toString();
                    creditInfo = new GetOdsTEcCreditInfo().getCreditChgInfo(this.connection,creditCode);

                    //创建授信并且核心企业确认后，记录授信状态信息
                    credit.creditStatus(connection,ps,jsonObjectData.get("source_code").toString(),stringUtils.toString(approval_date).substring(0,19)
                            ,stringUtils.toString(approval_date).substring(0,19));

                    //按照确认日期统计指定核心企业授信
                    credit.creditCount(connection,ps,creditInfo.getCompCode(),approval_date.substring(0,10),approval_date.substring(0,10)+" 00:00:00",
                            approval_date.substring(0,10)+" 23:59:59" );
                    //统计所有的指定核心企业授信
                    credit.creditCount(connection,ps,creditInfo.getCompCode(),"9999-12-31","1000-01-01 00:00:00","9999-12-31 23:59:59");

                    //按照确认日期统计指定核心企业授信比率
                    credit.creditCountBl(connection,ps,creditInfo.getCompCode(),approval_date.substring(0,10));
                    //统计所有的指定核心企业授信比率
                    credit.creditCountBl(connection,ps,creditInfo.getCompCode(),"9999-12-31");

                    //记录指定日期指定核心企业的有效失效授信
                    credit.creditValidDay(connection,ps,creditInfo.getCompCode(),approval_date.substring(0,10));

                    //统计指定日期指定核心企业的有效失效授信
                    credit.creditValid(connection,ps,creditInfo.getCompCode(),approval_date.substring(0,10));

                    //统计指定日期指定核心企业的有效失效授信比率
                    credit.creditValidBl(connection,ps,creditInfo.getCompCode(),approval_date.substring(0,10));

                }
                if("3".equals(oper_type) && "70".equals(after_status)){//修改了授信信息，并且审批通过了，记录状态信息
                    OdsTEcCreditChg creditChg = new GetOdsTEcCreditChgInfo().getCreditChgInfo(this.connection,jsonObjectData.get("source_code").toString());
                    String cchg_type=creditChg.getCchgType();
                    String credit_code = creditChg.getCreditCode();
                    creditInfo = new GetOdsTEcCreditInfo().getCreditChgInfo(this.connection,credit_code);
                    if("1".equals(cchg_type)){ //修改授信额度
                        Double after_amt = creditChg.getAfterAmt();
                        Double before_amt = creditChg.getBeforeAmt();

                        //记录授信额度修改后的信息
                        credit.creditStatusAmtchg(connection,ps,credit_code,stringUtils.toString(approval_date).substring(0,19),
                                stringUtils.toDouble(after_amt,2),stringUtils.toDouble(after_amt-before_amt,2));
                        //按照确认日期统计指定核心企业授信
                        credit.creditCount(connection,ps,creditInfo.getCompCode(),approval_date.substring(0,10),approval_date.substring(0,10)+" 00:00:00",
                                approval_date.substring(0,10)+" 23:59:59" );
                        //统计所有的指定核心企业授信
                        credit.creditCount(connection,ps,creditInfo.getCompCode(),"9999-12-31","1000-01-01 00:00:00","9999-12-31 23:59:59");

                        //按照确认日期统计指定核心企业授信比率
                        credit.creditCountBl(connection,ps,creditInfo.getCompCode(),approval_date.substring(0,10));
                        //统计所有的指定核心企业授信比率
                        credit.creditCountBl(connection,ps,creditInfo.getCompCode(),"9999-12-31");
                    }
                    if("2".equals(cchg_type)){//修改授信截止日期
                        //记录授信截止日期修改后的信息
                        credit.creditStatusEndtimechg(connection,ps,credit_code,stringUtils.toString(approval_date).substring(0,19),
                                stringUtils.toString(creditChg.getDiffDate()).substring(0,19));

                        //记录指定日期指定核心企业的有效失效授信
                        credit.creditValidDay(connection,ps,creditInfo.getCompCode(),approval_date.substring(0,10));

                        //统计指定日期指定核心企业的有效失效授信
                        credit.creditValid(connection,ps,creditInfo.getCompCode(),approval_date.substring(0,10));

                        //统计指定日期指定核心企业的有效失效授信比率
                        credit.creditValidBl(connection,ps,creditInfo.getCompCode(),approval_date.substring(0,10));
                    }
                }
            }
        }

        if("DELETE".equals(type)){
            ps = connection.prepareStatement(sql_credit_audit_d);
            for (Object data : datas) {
                log.info("objectNode-data:" + data);
                jsonObjectData = JSONObject.parseObject(data.toString());
                ps.setString(1, jsonObjectData.get("id").toString());
                log.info("objectNode-data-id:" + jsonObjectData.get("id").toString());
                ps.execute();
            }
            connection.commit();
        }
    }

}
