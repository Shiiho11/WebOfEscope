package edu.hdu.WebOfEscope.ServerStatus;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Shell {

    private String ip;
    private String username;
    private String password;
    private static final int DEFAULT_SSH_PORT = 22;
    private static final int timeout = 10000;

    private ArrayList<String> stdout;

    public Shell(String ip, String username, String password) {
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.stdout = new ArrayList<String>();
    }

    public void execute(String command) {

        JSch jsch = new JSch();

        try{
            Session session = jsch.getSession(username, ip, DEFAULT_SSH_PORT);
            session.setPassword(password);
            //session.setUserInfo(new MyUserInfo());//实现UserInfo接口
            session.setConfig("StrictHostKeyChecking", "no");//跳过检查
            session.setTimeout(timeout);
            session.connect();

            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);
            channelExec.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
            channelExec.connect();
            System.out.println("Command:" + command);

            String line;
            while((line = input.readLine()) != null) {
                stdout.add(line);
            }

            input.close();
            channelExec.disconnect();
            session.disconnect();
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        } finally {
        }

    }

    public ArrayList<String> getOutput() {
        return stdout;
    }

}
