package dw.gbu.service;

import dw.gbu.utils.PhoenixConnectUtil;
import dw.gbu.utils.PropertiesUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CreditService {
    private static final Logger log = LoggerFactory.getLogger(CreditService.class);

    private static PropertiesUtil prop;

    static {
        prop = new PropertiesUtil("/sqls.properties");
    }


    /**
     *授信创建核心企业审批通过后，插入授信状态信息
     * @param connection 连接
     * @param ps 预编译
     * @param creditCode 授信编号
     * @param lastModTimeAmt 授信额度的最后修改时间
     * @param lastModTimeEndtime 授信截止日期的最后修改时间
     */
    public void creditStatus(Connection connection,PreparedStatement ps, String creditCode, String lastModTimeAmt, String lastModTimeEndtime) {
        String credit_status = prop.getValueByKey("phoenix_credit_status");
        try{
            ps = connection.prepareStatement(credit_status);
            ps.setString(1, creditCode);
            ps.setString(2, lastModTimeAmt);
            ps.setString(3, lastModTimeEndtime);
            ps.setString(4, lastModTimeAmt);
            ps.setString(5, lastModTimeAmt.substring(0,10)+" 00:00:00");
            ps.setString(6, creditCode);
            ps.execute();
            connection.commit();
        }catch (Exception e){
            log.error("授信创建核心企业审批通过后，插入授信状态信息失败：【creditCode："+creditCode+"】");
            e.printStackTrace();
        }
    }

    /**
     * 授信统计
     * @param connection 连接
     * @param ps 预编译
     * @param compCode 核心企业编号
     * @param cntDate 统计日期
     * @param startTime 统计开始时间
     * @param endTime 统计结束时间
     */
    public void creditCount(Connection connection,PreparedStatement ps, String compCode, String cntDate, String startTime, String endTime) {
        String credit_count = prop.getValueByKey("phoenix_credit_count");
        try{
            ps = connection.prepareStatement(credit_count);
            ps.setString(1, cntDate);
            ps.setString(2, compCode);
            ps.setString(3, startTime);
            ps.setString(4, endTime);
            ps.setString(5, compCode);
            ps.setString(6, startTime);
            ps.setString(7, endTime);
            ps.execute();
            connection.commit();
        }catch (Exception e){
            log.error("授信统计失败：【compCode："+compCode+",cntDate:"+cntDate+"】");
            e.printStackTrace();
        }
    }

    /**
     * 授信统计比率
     * @param connection 连接
     * @param ps 预编译
     * @param compCode 核心企业
     * @param cntDate 统计日期
     */
    public void creditCountBl(Connection connection,PreparedStatement ps, String compCode, String cntDate) {
        String credit_count_bl = prop.getValueByKey("phoenix_credit_count_bl");
        try{
            ps = connection.prepareStatement(credit_count_bl);
            ps.setString(1, cntDate);
            ps.setString(2, compCode);
            ps.setString(3, cntDate);
            ps.execute();
            connection.commit();
        }catch (Exception e){
            log.error("授信统计比率失败：【compCode："+compCode+",cntDate:"+cntDate+"】");
            e.printStackTrace();
        }
    }

    /**
     *记录每日有效失效授信
     * @param connection 连接
     * @param ps 预编译
     * @param compCode 核心企业编号
     * @param cntDate 统计日期
     */
    public void creditValidDay(Connection connection, PreparedStatement ps, String compCode, String cntDate) {
        String credit_valid_day = prop.getValueByKey("phoenix_credit_valid_day_all");
        if(null != compCode){
            credit_valid_day = prop.getValueByKey("phoenix_credit_valid_day");
        }
        int i =1;
        try{
            ps = connection.prepareStatement(credit_valid_day);
            ps.setString(i++,cntDate);
            ps.setString(i++,cntDate+" 00:00:00");
            ps.setString(i++,cntDate+" 00:00:00");
            ps.setString(i++,cntDate+" 23:59:59");
            if(null != compCode){
                ps.setString(i++, compCode);
            }
            ps.setString(i++,cntDate+" 00:00:00");
            ps.setString(i++,cntDate+" 23:59:59");
            if(null != compCode){
                ps.setString(i++, compCode);
            }

            ps.execute();
            connection.commit();
        }catch (Exception e){
            log.error("记录每日有效失效授信失败：【compCode："+compCode+",cntDate:"+cntDate+"】");
            e.printStackTrace();
        }
    }

    /**
     * 统计指定日期指定核心企业的有效失效授信
     * @param connection 连接
     * @param ps 预编译
     * @param compCode 核心企业编号
     * @param cntDate 统计日期
     */
    public void creditValid(Connection connection, PreparedStatement ps, String compCode, String cntDate) {
        String phoenix_credit_valid = prop.getValueByKey("phoenix_credit_valid_all");
        if(null != compCode){
            phoenix_credit_valid = prop.getValueByKey("phoenix_credit_valid");
        }
        try{
            ps = connection.prepareStatement(phoenix_credit_valid);
            ps.setString(1,cntDate);
            ps.setString(2,cntDate);
            if(null != compCode){
                ps.setString(3, compCode);
            }
            ps.execute();
            connection.commit();
        }catch (Exception e){
            log.error("统计指定日期指定核心企业的有效失效授信失败：【compCode："+compCode+",cntDate:"+cntDate+"】");
            e.printStackTrace();
        }
    }

    /**
     * 统计指定日期指定核心企业的有效失效授信比率
     * @param connection 连接
     * @param ps 预编译
     * @param compCode 核心企业编号
     * @param cntDate 统计日期
     */
    public void creditValidBl(Connection connection, PreparedStatement ps, String compCode, String cntDate) {
        String phoenix_credit_valid_bl = prop.getValueByKey("phoenix_credit_valid_bl_all");
        if(null != compCode){
            phoenix_credit_valid_bl = prop.getValueByKey("phoenix_credit_valid_bl");
        }
        try{
            ps = connection.prepareStatement(phoenix_credit_valid_bl);
            ps.setString(1,cntDate);
            ps.setString(2,cntDate);
            if(null != compCode){
                ps.setString(3,compCode);
            }
            ps.execute();
            connection.commit();
        }catch (Exception e){
            log.error("统计指定日期指定核心企业的有效失效授信比率失败：【compCode："+compCode+",cntDate:"+cntDate+"】");
            e.printStackTrace();
        }
    }

    /**
     * 记录授信额度修改后的信息
     * @param connection 连接
     * @param ps 预编译
     * @param creditCode 授信编号
     * @param lastModTimeAmt 授信额度最后修改时间
     * @param afterAmt 修改后的授信额度
     * @param getAmt 获得授信额度
     */
    public void creditStatusAmtchg(Connection connection, PreparedStatement ps, String creditCode, String lastModTimeAmt, Double afterAmt, Double getAmt) {
        String phoenix_credit_status_amtchg = prop.getValueByKey("phoenix_credit_status_amtchg");
        try{
            ps = connection.prepareStatement(phoenix_credit_status_amtchg);
            ps.setString(1, lastModTimeAmt);
            ps.setDouble(2, afterAmt);
            ps.setDouble(3, getAmt);
            ps.setString(4, creditCode);
            ps.execute();
            connection.commit();
        }catch (Exception e){
            log.error("记录授信额度修改后的信息失败：【compCode："+creditCode+",lastModTimeAmt:"+lastModTimeAmt+"】");
            e.printStackTrace();
        }
    }

    /**
     * 记录授信截止日期修改后的信息
     * @param connection 连接
     * @param ps 预编译
     * @param creditCode 授信编号
     * @param lastModTimeEndtime 授信截止日期最后修改时间
     * @param endTime 修改后的授信截止日期
     */
    public void creditStatusEndtimechg(Connection connection, PreparedStatement ps, String creditCode, String lastModTimeEndtime, String endTime) {
        String phoenix_credit_status_endtimechg = prop.getValueByKey("phoenix_credit_status_endtimechg");
        try{
            ps = connection.prepareStatement(phoenix_credit_status_endtimechg);
            ps.setString(1,lastModTimeEndtime);
            ps.setString(2,endTime);
            ps.setString(3, creditCode);
            ps.execute();
            connection.commit();
        }catch (Exception e){
            log.error("记录授信截止日期修改后的信息失败：【compCode："+creditCode+",lastModTimeEndtime:"+lastModTimeEndtime+"】");
            e.printStackTrace();
        }
    }
}
