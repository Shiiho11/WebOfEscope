package edu.hdu.WebOfEscope.ServerStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SSsqlCoonection {

    //private static String driver="com.mysql.cj.jdbc.Driver"; //Mysql驱动
    private static String url   ="jdbc:mysql://localhost:3306/ServerStatus?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true"; //指定数据库
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
