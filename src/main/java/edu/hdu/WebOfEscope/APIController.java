package edu.hdu.WebOfEscope;

import edu.hdu.WebOfEscope.WebFunction.SSFunction;
import edu.hdu.WebOfEscope.WebFunction.WSNFunction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controller类
 * 供前端调用
 */

@RestController
@RequestMapping("/api")
public class APIController {
    int i = 0;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        return "API test: " + (i++);
    }

    @RequestMapping(value = "/WSN/getRealTime", method = RequestMethod.GET)
    public ArrayList<HashMap<String, Object>> WSNgetRealTime(){
        return WSNFunction.getRealTime();
    }

    @RequestMapping(value = "/WSN/getLocation", method = RequestMethod.GET)
    public ArrayList<HashMap<String, Object>> WSNgetLocation(){
        return WSNFunction.getLocation();
    }

    @RequestMapping(value = "/WSN/getOnline", method = RequestMethod.GET)
    public ArrayList<String> WSNgetOnline(){
        return WSNFunction.getOnline();
    }

    @RequestMapping(value = "/SS/getRealTime", method = RequestMethod.GET)
    public ArrayList<HashMap<String, Object>> SSgetRealTime(){
        return SSFunction.getRealTime();
    }

    @RequestMapping(value = "/SS/getLocation", method = RequestMethod.GET)
    public ArrayList<HashMap<String, Object>> SSgetLocation(){
        return SSFunction.getLocation();
    }

}
