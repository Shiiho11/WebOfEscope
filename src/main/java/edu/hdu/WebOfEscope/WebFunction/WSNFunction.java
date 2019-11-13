package edu.hdu.WebOfEscope.WebFunction;

import edu.hdu.WebOfEscope.WSN.WSNcache;
import edu.hdu.WebOfEscope.WSN.WSNsqlCoonection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class WSNFunction {

    public static ArrayList<HashMap<String, Object>> getRealTime() {
        return WSNcache.getRealTime();
    }

    public static ArrayList<String> getOnline(){
        return WSNcache.getOnline();
    }

    public static ArrayList<HashMap<String, Object>> getLocation(){
        Connection connection = WSNsqlCoonection.getConnection();
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM sensor");
            while (rs.next()){
                HashMap<String, Object> map = new HashMap<>();
                map.put("address",rs.getString("address"));
                map.put("x",rs.getFloat("x"));
                map.put("y",rs.getFloat("y"));
                map.put("z",rs.getFloat("z"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void claerData(String table) {
        Connection connection = WSNsqlCoonection.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE from "+table);
            System.out.println("claer WSN "+table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
