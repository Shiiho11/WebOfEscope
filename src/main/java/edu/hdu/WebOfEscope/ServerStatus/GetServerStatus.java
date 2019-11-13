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

            while (rs.next()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("ip",rs.getString("ip"));
                map.put("username",rs.getString("username"));
                map.put("password",rs.getString("password"));
                ServerInfo.add(map);
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
        //遍历Server Info List
        for(int i = 0; i < ServerInfo.size(); i++) {
            Timer getdata = new Timer();
            getdata.schedule(new GetData(ServerInfo.get(i).get("ip"),ServerInfo.get(i).get("username"),ServerInfo.get(i).get("password")),0,10000);//为每个ip开启一个定时任务
        }
    }


    //单个ip的任务
    private class GetData extends TimerTask {
        private String ip;
        private String username;
        private String password;
        private final String command = "top -b -n 1 | grep -e \"Cpu\" -e \"Mem\" ; ipmitool sdr | grep -e \"CPU0_Temp\" -e \"CPU1_Temp\" -e \"PSU1_VIN\" -e \"PSU1_IIN\" -e \"PSU2_VIN\" -e \"PSU2_IIN\" -e \"Total_Power\" -e \"CPU_Watts\"";

        GetData(String ip, String username, String password){
            this.ip = ip;
            this.username = username;
            this.password = password;
        }

        @Override
        public void run(){

            Shell shell = new Shell(ip, username, password);
            shell.execute(command);
            ArrayList<String> stdout = shell.getOutput();

            System.out.println("ip:"+ip);
            for(int i = 0;i < stdout.size(); i++){
                System.out.println(stdout.get(i));
            }

            //数据处理(专用)
            Map<String,Double> data = new HashMap<>();
            data.put("CPU_id",StD(stdout.get(0),35,40));
            data.put("total_Mem",StD(stdout.get(1),9,18));
            data.put("avail_Mem",StD(stdout.get(2),55,64));
            data.put("CPU_avg_Temp",(StD(stdout.get(3),19,37)+StD(stdout.get(4),19,37))/2.0);
            data.put("VIN",StD(stdout.get(5),19,37)+StD(stdout.get(7),19,37));
            data.put("IIN",StD(stdout.get(6),19,37)+StD(stdout.get(8),19,37));
            data.put("Total_Power",StD(stdout.get(9),19,37));
            data.put("CPU_Watts",StD(stdout.get(10),19,37));
            System.out.println(data);


            //数据库
//            try{
//                Connection sqlconnection = SSsqlCoonection.getConnection();
//                Statement statement = sqlconnection.createStatement();
//                String sql = "";
//                statement.executeUpdate(sql);
//                //statement.close();
//                //sqlconnection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } finally {
//            }

        }

        /**
         * 把字符串中的数据提取出来,String to double.若字符串中没有数字，则输出0.
         * @param str
         * @param start
         * @param end
         * @return
         */
        private double StD(String str, int start, int end) {
            String restr = "0";
            str = str.substring(start,end);
            for(int i=0;i<str.length();i++){
                if((str.charAt(i)>=48 && str.charAt(i)<=57) || str.charAt(i)==46){
                    restr += str.charAt(i);
                }
            }
            return Double.parseDouble(restr);
        }

    }

}
