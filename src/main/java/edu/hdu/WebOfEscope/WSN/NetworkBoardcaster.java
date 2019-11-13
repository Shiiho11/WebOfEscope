package edu.hdu.WebOfEscope.WSN;

import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.XBeeNetwork;
import com.digi.xbee.api.exceptions.TimeoutException;
import com.digi.xbee.api.exceptions.XBeeException;
import java.util.*;

/**
 * Network Boardcaster,sending the time sync and finding other device in network
 */
public class NetworkBoardcaster implements Runnable {

    private static final byte TIME_SYNC_ACK=0x5B;
    private static final byte TIME_SYNC=0x6A;

    private XBeeDevice device;


    public NetworkBoardcaster(XBeeDevice device){
        this.device = device;
    }

    @Override
    public void run(){
        Timer boardcastTimer = new Timer();
        boardcastTimer.schedule(new boardcastTask(device), 0, 10000);
    }

    /**
     * class boardcastTask extends TimerTask
     */
    private static class boardcastTask extends TimerTask{
        private XBeeDevice device;


        boardcastTask(XBeeDevice device){
            this.device = device;
        }

        @Override
        public void run(){

            byte [] dataToSend = new byte[5];
            Calendar nowTime = Calendar.getInstance();
            int day,hour,minite,second;
            day = nowTime.get(Calendar.DAY_OF_MONTH);
            hour = nowTime.get(Calendar.HOUR_OF_DAY);
            minite = nowTime.get(Calendar.MINUTE);
            second = nowTime.get(Calendar.SECOND);
            //System.out.println(nowTime.get(Calendar.YEAR)+" "+nowTime.get(Calendar.MONTH)+" "+day+" "+hour+" "+minite+" "+second);
            dataToSend[0] = TIME_SYNC;
            dataToSend[1] = (byte)day;
            dataToSend[2] = (byte)hour;
            dataToSend[3] = (byte)minite;
            dataToSend[4] = (byte)second;
//            System.out.println(dataToSend[1]+" "+dataToSend[2]+" "+dataToSend[3]+" "+dataToSend[4]);
            try {

                //device.setReceiveTimeout(65535);
                //    RemoteXBeeDevice rd=new RemoteXBeeDevice(loaclXBeeDevice,
                //          new XBee64BitAddress("0013A200407AB73B"));
                //   device.sendData(rd,dataToSend);
                //     System.out.println(device.getReceiveTimeout());
                device.sendBroadcastData(dataToSend);
                getNowNetwork();
            } catch (XBeeException e) {
                if(e instanceof TimeoutException) System.out.println("Boardcast complished!");
                else e.printStackTrace();
            }

        }

        private void getNowNetwork() {
            XBeeNetwork xBeeNetwork;
            xBeeNetwork = device.getNetwork();
            List<RemoteXBeeDevice> remoteXBeeDeviceList;
            remoteXBeeDeviceList = xBeeNetwork.getDevices();
            ArrayList<String> Online = new ArrayList<>();
            for(int i=0;i<remoteXBeeDeviceList.size();i++){
                System.out.println("16BitAddress:" + remoteXBeeDeviceList.get(i).get16BitAddress().toString());//debug line
                Online.add(remoteXBeeDeviceList.get(i).get16BitAddress().toString());
            }
            WSNcache.setOnline(Online);
        }

    }
}
