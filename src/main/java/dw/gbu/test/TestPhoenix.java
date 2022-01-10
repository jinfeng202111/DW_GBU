package dw.gbu.test;

import java.sql.*;

public class TestPhoenix {
    public static void main(String[] args) throws Exception {
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        Connection conn = DriverManager.getConnection("jdbc:phoenix:slave002:2181");
        conn.setAutoCommit(false);
        //Connection connection = PhoenixConnectUtil.getConnection("jdbc:phoenix:slave002:2181","","");
        String sql = "UPSERT into \"test\".\"user_info\"(\"id\",\"info\".\"name\",\"info\".\"age\",\"info\".\"fee\",\"extra_info\".\"birthday\") values ('003','金锋',28,9999990.9967,TO_DATE('20211124'))";
        //PreparedStatement ps = conn.prepareStatement(sql);
        //ps.setString(1,value_str);

        //ps.setTimestamp(2,value_time);

        //执行insert语句
       // ps.execute();
        Statement stat = conn.createStatement();
        stat.executeUpdate(sql);
        String sql2 = "UPSERT into \"test\".\"user_info\"(\"id\",\"info\".\"name\",\"info\".\"age\",\"info\".\"fee\",\"extra_info\".\"birthday\") values ('004','金锋',28,9999990.9967,TO_DATE('20211124'))";
        stat.executeUpdate(sql2);
        conn.commit();
    }
}
