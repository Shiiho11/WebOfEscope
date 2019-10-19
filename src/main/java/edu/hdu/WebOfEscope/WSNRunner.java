package edu.hdu.WebOfEscope;

import edu.hdu.WebOfEscope.WSN.WSNMainApp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * CommandLineRunner类
 * 在启动Spring Boot后启动WSN程序
 */

@Component
public class WSNRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        WSNMainApp.WSNmain();//WSN程序
    }

}
