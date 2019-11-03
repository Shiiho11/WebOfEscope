package edu.hdu.WebOfEscope.ServerStatus;

public class SSmain {
    public static void SSmain() {
            new Thread(new GetServerStatus()).start();
    }
}
