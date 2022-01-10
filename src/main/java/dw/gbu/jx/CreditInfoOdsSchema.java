package dw.gbu.jx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dw.gbu.utils.StringUtils;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class CreditInfoOdsSchema implements SerializationSchema<ObjectNode> {
    private static final Logger log = LoggerFactory.getLogger(CreditInfoOdsHbase.class);

    @Override
    public byte[] serialize(ObjectNode jsonNode) {

        byte[] datas_byte = null;
        log.info("objectNode:"+jsonNode);
        JSONObject jsonObject = JSONObject.parseObject(jsonNode.get("value").toString());
        String type = jsonObject.get("type").toString();
        log.info("objectNode-type:" + type);
        log.info("objectNode-data:"+jsonObject.get("data"));
        log.info("objectNode-data:"+jsonObject.get("data").toString());
        StringUtils stringUtils = new StringUtils();

        String data_all = "";

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
            JSONArray dates = (JSONArray) jsonObject.get("data");
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
                String credit_status_old = stringUtils.nullToString(old.get("credit_status")).toString();
                String confirm_status_old = stringUtils.nullToString(old.get("confirm_status")).toString();
                String yn_old = stringUtils.nullToString(old.get("yn")).toString();
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

        datas_byte = data_all.getBytes(StandardCharsets.UTF_8);

        return datas_byte;
    }
}
