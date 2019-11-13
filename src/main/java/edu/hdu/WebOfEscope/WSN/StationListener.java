package edu.hdu.WebOfEscope.WSN;

import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBeeMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class StationListener implements IDataReceiveListener {
    private static final byte HumidityCode=0x01; //湿度
    private static final byte TemperatureCode=0x02; //温度
    private static final byte LightCode=0x03; //光线
    private static final byte SmogCode=0x04; //烟雾
    private static final byte UltraCode=0x05;//红外入侵
    private static final byte CO2Code=0x06;//CO2
    private static final byte TIME_SYNC_ACK=0x5B;//时间同步
    private static final byte TIME_SYNC=0x6A;//时间同步确认
    private static final byte ErrorCode=0x00; //错误代码

    /**
     *  the data receive method
     * @param xbeeMessage
     */
    @Override
    public void dataReceived(XBeeMessage xbeeMessage) {
        Date nowTime=null;
        HashMap<String,Object> data = new HashMap<>();

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
                        float humidity = 0;
                        if (getMsg.length == 7) {//DHT11
                            String humidityString = String.format("%d.%d", getMsg[1], getMsg[2]);
                            humidity = Float.parseFloat(humidityString);
                        } else {
                        }//HDC1080
                        data.put("head","humidity");
                        data.put("time",nowTime);
                        data.put("value",humidity);
                        data.put("address",xbeeMessage.getDevice().get16BitAddress());
                        WSNcache.add(data);
                        break;
                    case TemperatureCode:
                        nowTime = getRecordTime(getMsg);
                        float temperature = 0;
                        if (getMsg.length == 7) {//DHT11
                            String temperatureString = String.format("%d.%d", getMsg[1], getMsg[2]);
                            temperature = Float.parseFloat(temperatureString);
                        } else {
                        }//HDC1080
                        data.put("head","temperature");
                        data.put("time",nowTime);
                        data.put("value",temperature);
                        data.put("address",xbeeMessage.getDevice().get16BitAddress());
                        WSNcache.add(data);
                        break;
                    case LightCode:
                        nowTime = getRecordTime(getMsg);
                        int light = ByteArrayToInt2(getMsg, 2);
                        data.put("head","light");
                        data.put("time",nowTime);
                        data.put("value",light);
                        data.put("address",xbeeMessage.getDevice().get16BitAddress());
                        WSNcache.add(data);
                        break;
                    case SmogCode:
                        nowTime = getRecordTime(getMsg);
                        int smog = ByteArrayToInt2(getMsg, 2);
                        data.put("head","smog");
                        data.put("time",nowTime);
                        data.put("value",smog);
                        data.put("address",xbeeMessage.getDevice().get16BitAddress());
                        WSNcache.add(data);
                        break;
                    case UltraCode:
                        nowTime = getRecordTime(getMsg);
                        int ultra = (int) getMsg[2];
                        data.put("head","ultra");
                        data.put("time",nowTime);
                        data.put("value",ultra);
                        data.put("address",xbeeMessage.getDevice().get16BitAddress());
                        WSNcache.add(data);
                        break;
                    case CO2Code:
                        nowTime = getRecordTime(getMsg);
                        float CO2 = 0;
                        if (getMsg.length == 7) {
                            String CO2String = String.format("%d.%d", getMsg[1], getMsg[2]);
                            CO2 = Float.parseFloat(CO2String);
                            data.put("head","CO2");
                            data.put("time",nowTime);
                            data.put("value",CO2);
                            data.put("address",xbeeMessage.getDevice().get16BitAddress());
                            WSNcache.add(data);
                        }
                        break;
                    case ErrorCode:
                        System.out.println("Remote Node Error");
                        break;
                    case TIME_SYNC:
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
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

}
