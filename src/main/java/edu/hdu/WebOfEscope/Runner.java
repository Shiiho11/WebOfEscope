package edu.hdu.WebOfEscope;

import edu.hdu.WebOfEscope.ServerStatus.SSmain;
import edu.hdu.WebOfEscope.WSN.WSNMainApp;
import edu.hdu.WebOfEscope.WebFunction.WSNFunction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * CommandLineRunner类
 * 在启动Spring Boot后自动启动程序
 */

@Component
public class Runner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        //SSmain.SSmain();//Server Status
        WSNFunction.claerData("RealTime");//claer data (table: RealTime)
        WSNMainApp.WSNmain();//WSN程序
    }

}
