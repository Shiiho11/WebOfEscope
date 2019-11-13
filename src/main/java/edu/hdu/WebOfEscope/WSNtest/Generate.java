//package edu.hdu.WebOfEscope.WSNtest;
//
//import java.sql.*;
//import java.util.*;
//import java.util.Date;
//
//public class Generate extends TimerTask {
//
//    private static final HashMap<String,Integer>  config = new HashMap<String, Integer>(){{
//        put("humidity_min",0);
//        put("humidity_max",100);
//        put("humidity_change",10);//每次最大改变量 -10 ~ +10
//        put("temperature_min",0);
//        put("temperature_max",100);
//        put("temperature_change",10);
//        put("light_min",0);
//        put("light_max",1000);
//        put("light_change",100);
//        put("smog_min",0);
//        put("smog_max",1000);
//        put("smog_change",100);
//        put("ultra_min",0);
//        put("ultra_max",1);
//        put("ultra_change",2);
//    }};
//
//    private Connection sqlConnetion;
//
//    Generate(){
//        this.sqlConnetion = WSNsqlCoonection.getConnection();
//    }
//
//    @Override
//    public void run(){
//        try{
//            System.out.println("Generate data start at:" + new Date());
//            Statement statement = this.sqlConnetion.createStatement();
//            String sql = "SELECT * from RealTime";
//            ResultSet rs = statement.executeQuery(sql);
//            Date date;
//            while (rs.next()){
//                date = new Date();
//                change(rs.getString("address"),"humidity", rs.getInt("humidity"),date);
//                change(rs.getString("address"),"temperature", rs.getInt("temperature"),date);
//                change(rs.getString("address"),"light", rs.getInt("light"),date);
//                change(rs.getString("address"),"smog", rs.getInt("smog"),date);
//                change(rs.getString("address"),"ultra", rs.getInt("ultra"),date);
//            }
//            System.out.println("Generate data and write to database finished at:" + new Date());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * writeToDatabase
//     * @param head
//     * @param time
//     * @param value
//     * @param address
//     */
//    private void writeToDatabase(String head, Date time, int value, String address) {
//        execSQL("UPDATE RealTime SET "+head+"="+value+","+head+"Time='"+new Timestamp(time.getTime())+"' WHERE address='"+address+"'");
//
//    }
//    private void writeToDatabase(String head, Date time, double value, String address) {
//        execSQL("UPDATE RealTime SET "+head+"="+value+","+head+"Time='"+new Timestamp(time.getTime())+"' WHERE address='"+address+"'");
//    }
//
//    /**
//     * execSQL
//     * @param sql
//     * @return
//     */
//    private int execSQL(String sql) {
//        Statement statement = null;
//        try {
//            int size = 0;
//            statement = this.sqlConnetion.createStatement();
//            boolean hasResult = statement.execute(sql);
//            if (hasResult) {
//                ResultSet rs = statement.getResultSet();
//                while (rs.next()) {
//                    System.out.println("rs:"+rs.getShort(1) + "\t");
//                    size++;
//                }
//                return size;
//            } else {
//                return -1;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return -1;
//        } finally {
//            //if (statement != null) statement.close();
//            //if(sqlconnection != null) sqlconnection.close();
//        }
//    }
//
//    /**
//     * 改变值并写入数据库
//     * @param address
//     * @param head
//     * @param value
//     * @param date
//     */
//    private void change(String address, String head, int value, Date date) {
//        int min = config.get(head+"_min");
//        int max = config.get(head+"_max");
//        int change = config.get(head+"_change");
//        Random r = new Random();
//
//        value += (int)(r.nextDouble() * 2 * change - change);
//        if(value < min) value = min;
//        else if(value > max) value = max;
//
//        writeToDatabase(head, date, value, address);
//    }
//
////    private void change(String address, String head, Double value, Date date) {
////        int min = config.get(head+"_min");
////        int max = config.get(head+"_max");
////        int change = config.get(head+"_change");
////        Random r = new Random();
////
////        value += r.nextDouble() * 2 * change - change;
////        if(value < min) value = (double)min;
////        else if(value > max) value = (double)max;
////
////        writeToDatabase(head, date, value, address);
////    }
//
//}
