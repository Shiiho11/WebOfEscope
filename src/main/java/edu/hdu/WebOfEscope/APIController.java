package edu.hdu.WebOfEscope;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        return "API test" + (i++);
    }
}
