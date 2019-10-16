package edu.hdu.WebOfEscope;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        return "API test";
    }
}
