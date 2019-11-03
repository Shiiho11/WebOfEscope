package edu.hdu.WebOfEscope.ServerStatus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class GetServerStatus implements Runnable {
    private ArrayList<HashMap<String, String>> ServerInfo = new ArrayList<>();

    public GetServerStatus(){
        //从数据库获得Server Info List
        try{
            Connection sqlConnection = SSsqlCoonection.getConnection();
            Statement statement = sqlConnection.createStatement();
            String sql = "SELECT * from ServerInfo";
            ResultSet rs = statement.executeQuery(sql);

            HashMap<String, String> map = new HashMap<>();
            while (rs.next()) {
                map.put("ip",rs.getString("ip"));
                map.put("username",rs.getString("username"));
                map.put("password",rs.getString("password"));
                ServerInfo.add(map);
                map.clear();
            }

            //rs.close();
            //statement.close();
            //sqlConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run(){
        HashMap<String, String> map;
        //遍历Server Info List
        for(int i = 0; i < ServerInfo.size(); i++) {
            map = ServerInfo.get(i);//直接用=,使map指向ServerInfo.get(i),所以不要对map进行任何改变.
            Timer getdata = new Timer();
            getdata.schedule(new GetData(map.get("ip"),map.get("username"),map.get("password")),0,5000);//为每个ip开启一个定时任务
        }
    }


    //单个ip的任务
    private class GetData extends TimerTask {
        private String ip;
        private String username;
        private String password;
        private final String command = "";

        GetData(String ip, String username, String password){
            this.ip = ip;
            this.username = username;
            this.password = password;
        }

        @Override
        public void run(){
            Shell shell = new Shell(ip, username, password);//建立连接
            shell.execute(command);//执行指令
            ArrayList<String> stdout = shell.getOutput();//获得结果
            System.out.println(stdout);

            //数据处理(专用)
            Map<String,String> data = new HashMap<String, String>();
            data.put("0",stdout.get(0));
            data.put("1",stdout.get(1));


            //数据库
            try{
                Connection sqlconnection = SSsqlCoonection.getConnection();
                Statement statement = sqlconnection.createStatement();
                String sql = "";
                statement.executeUpdate(sql);
                //statement.close();
                //sqlconnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
            }

        }

        //把ipmitool sdr list字符串中的数据提取出来(专用)
        private double ipmiStD(String str) {
            String restr = new String();
            str = str.substring(19,37);
            for(int i=0;i<str.length();i++){
                if((str.charAt(i)>=48 && str.charAt(i)<=57) || str.charAt(i)==46){
                    restr += str.charAt(i);
                }
            }
            return Double.parseDouble(restr);
        }

    }



}
