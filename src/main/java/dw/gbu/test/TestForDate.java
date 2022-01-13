package dw.gbu.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestForDate {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(sdf.format(Calendar.getInstance().getTime()));
        //new TestForDate().forDate("2022-01-01");
    }

    public void forDate(String start,String end) {
        // 日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            startDate.setTime(sdf.parse(start));
            endDate.setTime(sdf.parse(end));

            while (startDate.compareTo(endDate)<0) {
                System.out.println(sdf.format(startDate.getTime()));
                // 天数加上1
                startDate.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
