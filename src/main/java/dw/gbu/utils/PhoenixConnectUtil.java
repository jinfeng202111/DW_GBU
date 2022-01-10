package dw.gbu.utils;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Connection;

public class PhoenixConnectUtil implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(PhoenixConnectUtil.class);
    private static BasicDataSource dataSource;

    private static PropertiesUtil prop;

    static {
        prop = new PropertiesUtil("/sqls.properties");
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(prop.getValueByKey("phoenix_driver_name"));
        dataSource.setUrl(prop.getValueByKey("phoenix_url"));
        dataSource.setUsername(prop.getValueByKey("phoenix_user"));
        dataSource.setPassword(prop.getValueByKey("phoenix_password"));

        dataSource.setMaxTotal(Integer.parseInt(prop.getValueByKey("phoenix_maxTotal")));
        dataSource.setInitialSize(Integer.parseInt(prop.getValueByKey("phoenix_initialSize")));
        dataSource.setMinIdle(Integer.parseInt(prop.getValueByKey("phoenix_minIdle")));
        dataSource.setMaxIdle(Integer.parseInt(prop.getValueByKey("phoenix_maxIdle")));
        dataSource.setMaxWaitMillis(Integer.parseInt(prop.getValueByKey("phoenix_maxWaitMillis")));
        dataSource.setRemoveAbandonedTimeout(Integer.parseInt(prop.getValueByKey("phoenix_removeAbandonedTimeout")));
        dataSource.setRemoveAbandonedOnMaintenance(Boolean.parseBoolean(prop.getValueByKey("phoenix_removeAbandonedOnMaintenance")));
        dataSource.setRemoveAbandonedOnBorrow(Boolean.parseBoolean(prop.getValueByKey("phoenix_removeAbandonedOnBorrow")));
        dataSource.setDefaultAutoCommit(Boolean.parseBoolean(prop.getValueByKey("phoenix_defaultAutoCommit")));

        log.info("配置phoenix连接池成功");
    }

    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(Boolean.parseBoolean(prop.getValueByKey("phoenix_defaultAutoCommit")));
            log.info("获取phoenix连接成功");
        } catch (Exception e) {
            log.error("获取phoenix连接失败");
            e.printStackTrace();
        }
        return connection;
    }

}