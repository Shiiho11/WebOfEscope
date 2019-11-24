package edu.hdu.WebOfEscope.ServerStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SScache {
    private static final CopyOnWriteArrayList<HashMap<String,Object>> RealTime = new CopyOnWriteArrayList<>();
    private static final ArrayList<HashMap<String,Object>> History = new ArrayList<>();
    private static final int HistoryMax = 5000;
    private static final ArrayList<HashMap<String, String>> ServerInfo = new ArrayList<>();

    //通过此方法添加数据
    public static void add(HashMap<String,Object> data){
        updateRealTime(data);
        //同步
        synchronized(History){
            History.add(data);
            if(History.size() >= HistoryMax){
                ArrayList<HashMap<String,Object>> HistoryCopy = new ArrayList<>(History);
                History.clear();
                new Thread(new WriteToDatabase(HistoryCopy)).start();//开一个线程，后台写入数据库
            }
        }
    }

    //更新RealTime
    private static void updateRealTime(HashMap<String,Object> data){
        for(int i=0; i < RealTime.size(); i++){
            if(RealTime.get(i).get("ip") == data.get("ip")){
                RealTime.remove(i);
                break;
            }
        }
        RealTime.add(data);
    }

    public static ArrayList<HashMap<String,Object>> getRealTime(){
        return new ArrayList<>(RealTime);
    }

    public static void updateServerInfo(){
        //从数据库获得Server Info List
        try{
            Connection sqlConnection = SSsqlCoonection.getConnection();
            Statement statement = sqlConnection.createStatement();
            String sql = "SELECT * from ServerInfo";
            ResultSet rs = statement.executeQuery(sql);

            ServerInfo.clear();
            while (rs.next()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("ip",rs.getString("ip"));
                map.put("username",rs.getString("username"));
                map.put("password",rs.getString("password"));
                ServerInfo.add(map);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<HashMap<String, String>> getServerInfo(){
        return ServerInfo;
    }

    //class
    private static class WriteToDatabase implements Runnable {
        ArrayList<HashMap<String,Object>> data;
        private static final String sql = "INSERT INTO Record(ip,CPU_id,total_Mem,avail_Mem,CPU_avg_Temp,VIN,IIN,Total_Power,CPU_Watts,time) VALUE(?,?,?,?,?,?,?,?,?,?)";

        public WriteToDatabase(ArrayList<HashMap<String,Object>> data){
            this.data = data;
        }

        @Override
        public void run(){
            System.out.println("ServerStatus History data WriteToDatabase start");
            try{
                Connection sqlConnetion = SSsqlCoonection.getConnection();
                PreparedStatement statement = sqlConnetion.prepareStatement(sql);
                for(int i=0; i < data.size(); i++){
                    statement.setString(1, (String) data.get(i).get("ip"));
                    statement.setFloat(2, (Float) data.get(i).get("CPU_id"));
                    statement.setFloat(3, (Float) data.get(i).get("total_Mem"));
                    statement.setFloat(4, (Float) data.get(i).get("avail_Mem"));
                    statement.setFloat(5, (Float) data.get(i).get("CPU_avg_Temp"));
                    statement.setFloat(6, (Float) data.get(i).get("VIN"));
                    statement.setFloat(7, (Float) data.get(i).get("IIN"));
                    statement.setFloat(8, (Float) data.get(i).get("Total_Power"));
                    statement.setFloat(9, (Float) data.get(i).get("CPU_Watts"));
                    statement.setTime(10, (Time) data.get(i).get("time"));
                    statement.addBatch();
                }
                statement.executeBatch();
                System.out.println("ServerStatus History data WriteToDatabase finished");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
