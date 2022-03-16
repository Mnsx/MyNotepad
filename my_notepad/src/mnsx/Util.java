package mnsx;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description notepad的工具类
 * @Author Mnsx_x
 * @Email xx1527030652@gmail.com
 * @Version 1.8.1
 * @Date 2021--08--29   17:10
 */
public class Util {
/*
    @Test
    public void test(){
        System.out.println(getNowTime());
    }
*/

    public static String getNowTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
        return time;
    }
}
