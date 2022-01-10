package dw.gbu.test;


import dw.gbu.utils.PhoenixConnectUtil;

import java.sql.*;
import java.text.SimpleDateFormat;

public class TestJSONArray {
    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        /**
        JSONArray datasUse = new JSONArray();
        datasUse.add(JSONObject.parseObject("{\"001\":\"bbb\"}"));
        datasUse.add(JSONObject.parseObject("{\"001\":\"ccc\"}"));
        datasUse.add(JSONObject.parseObject("{\"001\":\"aaa\"}"));
        System.out.println(datasUse.toString());
        Utils utils = new Utils();

        JSONObject jsonObjectData =null;
        for(Object data:datasUse){
            jsonObjectData = JSONObject.parseObject(data.toString());
           // System.out.println(jsonObjectData.get("001").toString());
            System.out.println("aaa:"+Types.NULL);
           // System.out.println("value:"+jsonObjectData.get("002") ==null?null:"002");
        }
       // System.out.println(Types.INTEGER);

        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
        System.out.println(date.toString());


        //System.out.println(sdf.format(new Date()));
         **/
        //SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");
        //System.out.println(new java.sql.Date((new java.util.Date().getTime())));
        //ps_oneday.setDate(1, new java.sql.Date(sdf_date.parse(String.valueOf(new java.util.Date())).getTime()));
        //ps_oneday.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
        //new java.sql.Date(sdf_date.parse("9999-12-31").getTime());

       // System.out.println(new java.util.Date());
       // System.out.println(new java.util.Date().before(sdf.parse("2020-12-29 00:00:00")));
       //System.out.println("2022-01-06 00:00:00".substring(0,19));
        //BigDecimal num1 = new BigDecimal("1.02");
        //BigDecimal num2 = new BigDecimal("100.99");
        //BigDecimal result2 = num1.subtract(num2);
        //System.out.println(result2);
       //new TestJSONArray().testGet();
        int i = 0;
       String format = "%."+i+"f";
       Double d = new Double(5.678);
        String result = String.format(format, d);
        Double d2 = new Double(result);
        System.out.println(d2);
    }

    public String test() throws Exception {
        Connection connection = PhoenixConnectUtil.getConnection();
        String sql = "select \"end_date\" from \"GBU_ODS_CREDIT\".\"ODS_T_EC_CREDIT_INFO\"";

        String sql2 = "UPSERT into \"GBU_ODS_CREDIT\".\"TEST\"(\"id\",\"i1\".\"end_date\") values ('0004',TO_TIMESTAMP('2021-11-24 00:00:00'))";
        PreparedStatement ps2 = connection.prepareStatement(sql2);
        ps2.execute();
        connection.commit();
        return "1";

    }
    public void testGet() throws Exception {
        Connection connection = PhoenixConnectUtil.getConnection();
        String sql = "select \"end_date\" from \"GBU_ODS_CREDIT\".\"TEST\"";
        PreparedStatement ps = connection.prepareStatement(sql);
        //ps1.execute();
        String sql2 = "UPSERT into \"GBU_ODS_CREDIT\".\"TEST\"(\"id\",\"i1\".\"end_date\") values ('0004',TO_TIMESTAMP('2021-11-24 00:00:00'))";
        PreparedStatement ps2 = connection.prepareStatement(sql2);
        ps2.execute();
        connection.commit();

        // ps.setString(1,bill_code);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
    }

}
