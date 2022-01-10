package dw.gbu.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;

public class StringUtils {
    public String nullToString(Object obj){
        if (null == obj){
            return "";
        }else{
            return obj.toString();
        }
    }

    public int toInteger(Object obj){
        Integer i = null;
        if(null != obj){
            i = new Integer(obj.toString());
        }
            return i;
    }
    public String toString(Object obj){
        String s = null;
        if(null != obj){
            s = obj.toString();
        }
        return s;
    }

    public BigDecimal toDecimal(Object obj){
        BigDecimal b = null;
        if(null != obj){
            b = new BigDecimal(obj.toString());;
        }
        return b;
    }

    public Double toDouble(Object obj,int i){
        Double d =null;
        if(null != obj){
            String format = "%."+i+"f";
            d = new Double(String.format(format, new Double(obj.toString())));
        }
        return d;
    }

    public Timestamp toTimestamp(Object obj){
        Timestamp t = null;
        if(null != obj){
            t = Timestamp.valueOf(obj.toString().substring(0,19));
        }
        return t;
    }

    public boolean isNotNull(Object obj) {
        if(null == obj){
            return false;
        }else{
            if("".equals(obj.toString().trim())){
                return false;
            }else{
                return true;
            }
        }
    }
}
