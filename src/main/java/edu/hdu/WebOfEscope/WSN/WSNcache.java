package edu.hdu.WebOfEscope.WSN;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class WSNcache {
    private static final CopyOnWriteArrayList<HashMap<String,Object>> RealTime = new CopyOnWriteArrayList<>();
    private static ArrayList<String> Online = new ArrayList<>();
    private static final ArrayList<HashMap<String,Object>> History = new ArrayList<>();
    private static final int HistoryMax = 5000;

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
            if(RealTime.get(i).get("address") == data.get("address")){
                RealTime.get(i).put((String)data.get("head"),data.get("value"));
                RealTime.get(i).put(data.get("head")+"time",data.get("time"));
                return;
            }
        }
        //新数据
        HashMap<String,Object> newData = new HashMap<>();
        newData.put("address",data.get("address"));
        newData.put((String)data.get("head"),data.get("value"));
        newData.put(data.get("head")+"time",data.get("time"));
        RealTime.add(newData);
    }

    public static ArrayList<HashMap<String,Object>> getRealTime(){
        return new ArrayList<>(RealTime);
    }

    public static void setOnline(ArrayList<String> online){
        Online = online;
    }

    public static ArrayList<String> getOnline() {
        return Online;
    }

    //class
    private static class WriteToDatabase implements Runnable {
        ArrayList<HashMap<String,Object>> data;

        public WriteToDatabase(ArrayList<HashMap<String,Object>> data){
            this.data = data;
        }

        @Override
        public void run(){
            System.out.println("WSN History data WriteToDatabase start");
            try{
                Connection sqlConnetion = WSNsqlCoonection.getConnection();
                Statement statement = sqlConnetion.createStatement();
                for(int i=0; i < data.size(); i++){
                    statement.addBatch("INSERT INTO "+data.get(i).get("head")+"(address,value,time) values("+data.get(i).get("address")+","+data.get(i).get("value")+","+data.get(i).get("time")+")");
                }
                statement.executeBatch();
                System.out.println("WSN History data WriteToDatabase finished");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
