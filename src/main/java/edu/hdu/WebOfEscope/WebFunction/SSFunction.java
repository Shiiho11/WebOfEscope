package edu.hdu.WebOfEscope.WebFunction;

import edu.hdu.WebOfEscope.ServerStatus.SScache;
import edu.hdu.WebOfEscope.ServerStatus.SSsqlCoonection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class SSFunction {

    public static ArrayList<HashMap<String, Object>> getRealTime(){
        return SScache.getRealTime();
    }

    public static ArrayList<HashMap<String, Object>> getLocation(){
        Connection connection = SSsqlCoonection.getConnection();
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM ServerInfo");
            while (rs.next()){
                HashMap<String, Object> map = new HashMap<>();
                map.put("ip",rs.getString("ip"));
                map.put("cabinet_id",rs.getInt("cabinet_id"));
                map.put("layer",rs.getInt("layer"));
                map.put("height",rs.getInt("height"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void claerData(String table) {
        Connection connection = SSsqlCoonection.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE from "+table);
            System.out.println("claer WSN "+table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
