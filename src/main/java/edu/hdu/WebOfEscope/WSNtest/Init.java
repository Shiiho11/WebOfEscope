//package edu.hdu.WebOfEscope.WSNtest;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.*;
//
///**
// * Network Boardcaster,sending the time sync and finding other device in network
// */
//public class Init {
//
//    private Connection sqlConnection;
//    private ArrayList<Map<String,String>> nodes;
//
//    private static final int MAX_Nodes = 500;
//    private static final int DEFAULT_humidity = 50;
//    private static final int DEFAULT_temperature = 20;
//    private static final int DEFAULT_lingt = 300;
//    private static final int DEFAULT_smog = 100;
//    private static final int DEFAULT_ultra = 0;
//
//    //初始化，生成随机节点
//    Init() throws SQLException {
//        //this.sqlConnection = WSNsqlCoonection.getConnection();
//        nodes = new ArrayList<>();
//        claerData("sensortable");
//        claerData("online");
//        claerData("realtime");
//
//        System.out.println("creat nodes start at:" + new Date());
//        int num = 1;
//        int add = 1000;//模拟16进制地址,从1000开始,为了方便,不会生成有字母的数字
//        Random exist = new Random();//用于随机坐标处是否有节点
//        for(int z = 0;z < 5;z++){
//            for(int y = 0;y < 10;y++){
//                for(int x = 0;x < 10;x++){
//                    if(num > MAX_Nodes) {
//                        System.out.println("creat nodes(max) finished at:" + new Date());
//                        return;
//                    }
//                    if(exist.nextBoolean()) {
//                        HashMap<String, String> node = new HashMap<>();
//                        node.put("Address",String.valueOf(add++));
//                        node.put("x",String.valueOf(x));
//                        node.put("y",String.valueOf(y));
//                        node.put("z",String.valueOf(z));
//                        nodes.add(node);
//                        num++;
//                    }
//                }
//            }
//        }
//        System.out.println("creat nodes(not max) finished at:" + new Date());
//    }
//
//    //写入数据库
//    void run() throws SQLException {
//        //写入sensortable
//        //写入online
//        //写入realtime
//        System.out.println("nodes write to database start at:" + new Date());
//        for(int i = 0;i < nodes.size();i++){
//            execSQL("INSERT into sensortable(Address,x,y,z) VALUE('"+nodes.get(i).get("Address")+"',"+nodes.get(i).get("x")+","+nodes.get(i).get("y")+","+nodes.get(i).get("z")+")");
//            execSQL("INSERT into online(Address) VALUE('"+nodes.get(i).get("Address")+"')");
//            execSQL("INSERT into realtime(address,humidity,temperature,light,smog,ultra,x,y,z) VALUE('"+nodes.get(i).get("Address")+"',"+DEFAULT_humidity+","+DEFAULT_temperature+","+DEFAULT_lingt+","+DEFAULT_smog+","+DEFAULT_ultra+","+nodes.get(i).get("x")+","+nodes.get(i).get("y")+","+nodes.get(i).get("z")+")");
//        }
//        System.out.println("nodes write to database finished at:" + new Date());
//    }
//
//
//    private boolean execSQL(String sQL) throws SQLException {
//            Statement statement = null;
//            try {
//                statement = this.sqlConnection.createStatement();
//                boolean hasResult = statement.execute(sQL);
//                if (hasResult) {
//                    ResultSet rs = statement.getResultSet();
//                    while (rs.next()) {
//                        System.out.println(rs.getShort(1) + "\t");
//                    }
//                    System.out.println();
//                    return true;
//                } else {
//                    return false;
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                return false;
//            } finally {
//                //if(statement != null) statement.close();
//                //if(sqlconnection != null) sqlconnection.close();
//            }
//    }
//
//    private static void claerData(String table) throws SQLException {
//        Connection connection = WSNsqlCoonection.getConnection();
//        Statement statement = null;
//        try {
//            statement = connection.createStatement();
//            statement.executeUpdate("DELETE from "+table);
//            System.out.println("claer WSN "+table);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            //if(statement != null) statement.close();
//            //if(sqlconnection != null) sqlconnection.close();
//        }
//    }
//}
