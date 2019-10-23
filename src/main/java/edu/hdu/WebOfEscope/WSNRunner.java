package edu.hdu.WebOfEscope;

import edu.hdu.WebOfEscope.WSN.StationListener;
import edu.hdu.WebOfEscope.WSN.WSNMainApp;
import edu.hdu.WebOfEscope.WSN.WSNSQLCoonection;
import edu.hdu.WebOfEscope.WebFunction.WSNFunction;
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
        WSNFunction.ClaerRealTime(WSNSQLCoonection.getConnection());//claer WSN resl time
        WSNMainApp.WSNmain();//WSN程序
    }

}
