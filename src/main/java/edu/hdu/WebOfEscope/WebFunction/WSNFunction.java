package edu.hdu.WebOfEscope.WebFunction;

import edu.hdu.WebOfEscope.WSN.WSNSQLCoonection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


public class WSNFunction {

    public static ArrayList<HashMap<String, String>> getRealTime() {
        try {
            Connection sqlConnection = WSNSQLCoonection.getConnection();
            Statement statement = sqlConnection.createStatement();
            String sql = "SELECT * from RealTime";
            ResultSet rs = statement.executeQuery(sql);

            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            while(rs.next()){
                HashMap<String, String> map = new HashMap<>();
                map.put("address",rs.getString("address"));
                map.put("humidity",rs.getString("humidity"));
                map.put("humidityTime",rs.getString("humidityTime"));
                map.put("temperature",rs.getString("temperature"));
                map.put("temperatureTime",rs.getString("temperatureTime"));
                map.put("light",rs.getString("light"));
                map.put("lightTime",rs.getString("lightTime"));
                map.put("smog",rs.getString("smog"));
                map.put("smogTime",rs.getString("smogTime"));
                map.put("ultra",rs.getString("smog"));
                map.put("ultraTime",rs.getString("smogTime"));
                list.add(map);
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void ClaerRealTime(Connection sqlConnetion) throws SQLException {
        Statement statement = null;
        try {
            statement = sqlConnetion.createStatement();
            statement.executeUpdate("DELETE from RealTime");
            System.out.println("claer WSN real time");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //if(statement != null) statement.close();
            //if(sqlconnection != null) sqlconnection.close();
        }
    }

}
