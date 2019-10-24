package edu.hdu.WebOfEscope;

import edu.hdu.WebOfEscope.WebFunction.WSNFunction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
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
    public ArrayList<HashMap<String, String>> WSNgetRealTime() throws SQLException {
        return WSNFunction.getRealTime();
    }

}
