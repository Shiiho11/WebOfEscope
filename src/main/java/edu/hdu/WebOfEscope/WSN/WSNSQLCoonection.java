package edu.hdu.WebOfEscope.WSN;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class WSNSQLCoonection {

    //private static String driver="com.mysql.cj.jdbc.Driver"; //Mysql驱动
    private static String url   ="jdbc:mysql://localhost:3306/WirelessSensorRecord?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC"; //指定数据库
    private static String user  ="root";
    private static String passwd="hdu17052223";

    public static Connection getConnection() {
        Connection conn = null;
        try{
            //Class.forName(driver);//加载
            conn = DriverManager.getConnection(url, user, passwd);
        //} catch (ClassNotFoundException e) {
        //    e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }
}
