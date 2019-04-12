package client_code;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Manager {

    public static void main(String [] args){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        Random ran = new Random();
        String ip = "http://10.216.35.189:8080";
        for (int i=0; i < 1000; i++ ){
            MessengerService m = new MessengerService(i, ip);
            executor.schedule(m, 0 , TimeUnit.SECONDS);
        }
        executor.shutdown();
    }
}
