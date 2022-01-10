package dw.gbu.utils;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlConnectUtil {
    private static final Logger log = LoggerFactory.getLogger(MysqlConnectUtil.class);
    private static BasicDataSource dataSource;

    private static PropertiesUtil prop;

    static {
        prop = new PropertiesUtil("/sqls.properties");
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(prop.getValueByKey("mysql_driver_name"));
        dataSource.setUrl(prop.getValueByKey("mysql_url"));
        dataSource.setUsername(prop.getValueByKey("mysql_user"));
        dataSource.setPassword(prop.getValueByKey("mysql_password"));

        dataSource.setMaxTotal(Integer.parseInt(prop.getValueByKey("mysql_maxTotal")));
        dataSource.setInitialSize(Integer.parseInt(prop.getValueByKey("mysql_initialSize")));
        dataSource.setMinIdle(Integer.parseInt(prop.getValueByKey("mysql_minIdle")));
        dataSource.setMaxIdle(Integer.parseInt(prop.getValueByKey("mysql_maxIdle")));
        dataSource.setMaxWaitMillis(Integer.parseInt(prop.getValueByKey("mysql_maxWaitMillis")));
        dataSource.setRemoveAbandonedTimeout(Integer.parseInt(prop.getValueByKey("mysql_removeAbandonedTimeout")));
        dataSource.setRemoveAbandonedOnMaintenance(Boolean.parseBoolean(prop.getValueByKey("mysql_removeAbandonedOnMaintenance")));
        dataSource.setRemoveAbandonedOnBorrow(Boolean.parseBoolean(prop.getValueByKey("mysql_removeAbandonedOnBorrow")));
        dataSource.setDefaultAutoCommit(Boolean.parseBoolean(prop.getValueByKey("mysql_defaultAutoCommit")));

        log.info("配置mysql连接池成功");
    }

    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(Boolean.parseBoolean(prop.getValueByKey("mysql_defaultAutoCommit")));
            log.info("获取mysql连接成功");
        } catch (Exception e) {
            log.error("获取mysql连接失败");
            e.printStackTrace();
        }
        return connection;
    }

}
