package edu.hdu.WebOfEscope.ServerStatus;

import java.util.*;

public class GetServerStatus implements Runnable {
    private ArrayList<HashMap<String, String>> ServerInfo;

    public GetServerStatus(){
        SScache.updateServerInfo();
        ServerInfo = SScache.getServerInfo();
    }

    @Override
    public void run(){
        //遍历Server Info List
        for(int i = 0; i < ServerInfo.size(); i++) {
            Timer getone = new Timer();
            getone.schedule(new GetOne(ServerInfo.get(i).get("ip"),ServerInfo.get(i).get("username"),ServerInfo.get(i).get("password")),0,10000);//为每个ip开启一个定时任务
        }
    }


    //单个ip的任务
    private class GetOne extends TimerTask {
        private String ip;
        private String username;
        private String password;
        private static final String command = "date; top -b -n 1 | grep -e \"Cpu\" -e \"Mem\"; ipmitool sdr | grep -e \"CPU0_Temp\" -e \"CPU1_Temp\" -e \"PSU1_VIN\" -e \"PSU1_IIN\" -e \"PSU2_VIN\" -e \"PSU2_IIN\" -e \"Total_Power\" -e \"CPU_Watts\"";

        GetOne(String ip, String username, String password){
            this.ip = ip;
            this.username = username;
            this.password = password;
        }

        @Override
        public void run() {

            Shell shell = new Shell(ip, username, password);
            shell.execute(command);
            ArrayList<String> stdout = shell.getOutput();

            System.out.println("ip:" + ip);
            for (int i = 0; i < stdout.size(); i++) {
                System.out.println(stdout.get(i));
            }

            //数据处理(专用)
            HashMap<String, Object> data = new HashMap<>();
            data.put("ip",ip);
            data.put("time",stdout.get(0));//这里可能需要字符串转换成时间
            data.put("CPU_id", StF(stdout.get(1), 35, 40));
            data.put("total_Mem", StF(stdout.get(2), 9, 18));
            data.put("avail_Mem", StF(stdout.get(3), 55, 64));
            data.put("CPU_avg_Temp", (StF(stdout.get(4), 19, 37) + StF(stdout.get(5), 19, 37)) / 2.0f);
            data.put("VIN", StF(stdout.get(6), 19, 37) + StF(stdout.get(8), 19, 37));
            data.put("IIN", StF(stdout.get(7), 19, 37) + StF(stdout.get(9), 19, 37));
            data.put("Total_Power", StF(stdout.get(10), 19, 37));
            data.put("CPU_Watts", StF(stdout.get(11), 19, 37));
            System.out.println(data);

            SScache.add(data);
        }

        /**
         * 把字符串中的数字提取出来,String to Float.若字符串中没有数字，则输出0.
         * @param str
         * @param start
         * @param end
         * @return
         */
        private Float StF(String str, int start, int end) {
            String restr = "0";
            str = str.substring(start,end);
            for(int i=0;i<str.length();i++){
                if((str.charAt(i)>=48 && str.charAt(i)<=57) || str.charAt(i)==46){
                    restr += str.charAt(i);
                }
            }
            return Float.parseFloat(restr);
        }

    }

}
