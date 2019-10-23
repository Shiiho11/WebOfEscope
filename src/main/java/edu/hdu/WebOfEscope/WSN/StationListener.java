package edu.hdu.WebOfEscope.WSN;

import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBeeMessage;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class StationListener implements IDataReceiveListener {
    private static final byte HumidityCode=0x01; //湿度
    private static final byte TemperatureCode=0x02; //温度
    private static final byte LightCode=0x03; //光线
    private static final byte SmogCode=0x04; //烟雾
    private static final byte UltraCode=0x05;//红外入侵
    private static final byte TIME_SYNC_ACK=0x5B;//时间同步
    private static final byte TIME_SYNC=0x6A;//时间同步确认
    private static final byte ErrorCode=0x00; //错误代码

    private Connection sqlConnetion;

    public StationListener(Connection connection){
        sqlConnetion=connection;
    }

    /**
     *  the data receive method
     * @param xbeeMessage
     */
    @Override
    public void dataReceived(XBeeMessage xbeeMessage) {
        Date nowTime=null;

        if(!xbeeMessage.isBroadcast()){
            byte [] getMsg;
            getMsg=xbeeMessage.getData();
            System.out.println("lenth:"+getMsg.length+" "+Arrays.toString(getMsg));

            try {
                switch (getMsg[0]) {
                    case TIME_SYNC_ACK:
                        break;
                    case HumidityCode:
                        nowTime = getRecordTime(getMsg);
                        double humidity = 0;
                        if (getMsg.length == 7) {//DHT11
                            String humidityString = String.format("%d.%d", getMsg[1], getMsg[2]);
                            humidity = Float.parseFloat(humidityString);
                        } else {
                        }//HDC1080
                        writeToDatabase("humidity", nowTime, humidity, xbeeMessage.getDevice());
                        break;
                    case TemperatureCode:
                        nowTime = getRecordTime(getMsg);
                        double temperature = 0;
                        if (getMsg.length == 7) {//DHT11
                            String temperatureString = String.format("%d.%d", getMsg[1], getMsg[2]);
                            temperature = Float.parseFloat(temperatureString);
                        } else {
                        }//HDC1080
                        writeToDatabase("temperature", nowTime, temperature, xbeeMessage.getDevice());
                        break;
                    case LightCode:
                        nowTime = getRecordTime(getMsg);
                        int light = ByteArrayToInt2(getMsg, 2);
                        writeToDatabase("light", nowTime, light, xbeeMessage.getDevice());
                        break;
                    case SmogCode:
                        nowTime = getRecordTime(getMsg);
                        int smog = ByteArrayToInt2(getMsg, 2);
                        writeToDatabase("smog", nowTime, smog, xbeeMessage.getDevice());
                        break;
                    case UltraCode:
                        nowTime = getRecordTime(getMsg);
                        int ultra = (int) getMsg[2];
                        writeToDatabase("ultra", nowTime, ultra, xbeeMessage.getDevice());
                        break;
                    case ErrorCode:
                        System.out.println("Remote Node Error");
                        break;
                    case TIME_SYNC:
                        break;
                    default:
                        break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * getRecordTime 获取记录时间
     * @param message
     * @return
     */
    private Date getRecordTime(byte[] message){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy MM dd HH mm ss");
        String timeString=String.format("%d %d %d %d %d %d",calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,message[message.length-4],
                message[message.length-3],message[message.length-2],message[message.length-1]);
        Date date=null;
        try{
            date=simpleDateFormat.parse(timeString);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    /**
     * ByteArrayToInt2 将byte转换为int
     * @param bArr 收到的byte数据
     * @return
     */
    private int ByteArrayToInt2(byte[] bArr,int start) {
        if(bArr.length-start<2){
            return -1;
        }
        return (int) (((bArr[start-1] & 0xff) << 8)|((bArr[start] & 0xff) << 0));
    }

    /**
     * writeToDatabase
     * @param head
     * @param time
     * @param value
     * @param remoteXBeeDevice
     */
    private void writeToDatabase(String head, Date time, int value, RemoteXBeeDevice remoteXBeeDevice) throws SQLException {
        execSQL("insert into "+head+"(value,time,address) VALUES ('"+value+"','"+new Timestamp(time.getTime())+"','"+remoteXBeeDevice.get16BitAddress()+"')");
        execSQL("INSERT INTO SensorValue(value,sensor,time,address) VALUES ('"+value+"','"+head+"','"+new Timestamp(time.getTime())+"','"+remoteXBeeDevice.get16BitAddress()+"')");

        if(execSQL("SELECT address from RealTime WHERE address='"+remoteXBeeDevice.get16BitAddress()+"'") > 0) {
            execSQL("UPDATE RealTime SET "+head+"="+value+","+head+"Time='"+new Timestamp(time.getTime())+"' WHERE address='"+remoteXBeeDevice.get16BitAddress()+"'");
        } else {
            execSQL("INSERT INTO RealTime(address,"+head+","+head+"Time) VALUES('"+remoteXBeeDevice.get16BitAddress()+"',"+value+",'"+new Timestamp(time.getTime())+"')");
        }
    }
    private void writeToDatabase(String head, Date time, double value, RemoteXBeeDevice remoteXBeeDevice) throws SQLException {
        execSQL("insert into "+head+"(value,time,address) VALUES ('"+value+"','"+new Timestamp(time.getTime())+"','"+remoteXBeeDevice.get16BitAddress()+"')");
        execSQL("INSERT INTO SensorValue(value,sensor,time,address) VALUES ('"+value+"','"+head+"','"+new Timestamp(time.getTime())+"','"+remoteXBeeDevice.get16BitAddress()+"')");

        if(execSQL("SELECT address from RealTime WHERE address='"+remoteXBeeDevice.get16BitAddress()+"'") > 0) {
            execSQL("UPDATE RealTime SET "+head+"="+value+","+head+"Time='"+new Timestamp(time.getTime())+"' WHERE address='"+remoteXBeeDevice.get16BitAddress()+"'");
        } else {
            execSQL("INSERT INTO RealTime(address,"+head+","+head+"Time) VALUES('"+remoteXBeeDevice.get16BitAddress()+"',"+value+",'"+new Timestamp(time.getTime())+"')");
        }
    }

    /**
     * execSQL
     * @param sql
     * @return
     */
    private int execSQL(String sql) throws SQLException {
        Statement statement = null;
        try {
            int size = 0;
            statement = this.sqlConnetion.createStatement();
            boolean hasResult = statement.execute(sql);
            if (hasResult) {
                ResultSet rs = statement.getResultSet();
                while (rs.next()) {
                    System.out.println("rs:"+rs.getShort(1) + "\t");
                    size++;
                }
                return size;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            //if (statement != null) statement.close();
            //if(sqlconnection != null) sqlconnection.close();
        }
    }

}
