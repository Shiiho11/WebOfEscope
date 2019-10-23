package edu.hdu.WebOfEscope.WSN;

import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;

import java.sql.Connection;

public class WSNMainApp {

    //private static final String PORT="dev/ttyUSB0";//Linux
    private static final String PORT = "COM4";//Rort
    private static final int BAUD_RATE = 9600;//Baud rate

    public static void WSNmain(){
        XBeeDevice device = new XBeeDevice(PORT,BAUD_RATE);
        Connection sqlConnection = WSNSQLCoonection.getConnection();
        try {
            device.open();
            NetworkBoardcaster thdStationBoardcast=new NetworkBoardcaster(device, sqlConnection);
            new Thread(thdStationBoardcast).start();
            device.addDataListener(new StationListener(sqlConnection));
        } catch (XBeeException e) {
            e.printStackTrace();
        } finally {
            //device.close();
        }
    }
}
