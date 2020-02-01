package client;

import javax.swing.*;

public class ClientFramesPerSecond extends Thread {

    private long MIN_FRAME_DURATION = 1000000000 / 59;

    private JPanel panel;
    private long passedFrameTime, lastFrameTime, delta;
    private int framesPerSecond;


    public ClientFramesPerSecond(JPanel panel){

        this.panel = panel;

    }

    @Override
    public void run() {
        while(true){
            try {

                lastFrameTime = System.nanoTime();
                // panel.repaint();
                //this.wait();

                delta = System.nanoTime() - lastFrameTime;
                framesPerSecond++;
                passedFrameTime += delta;

                if (passedFrameTime > 1000000000) {
                    System.out.println(framesPerSecond);
                    framesPerSecond = 0;
                    passedFrameTime = 0;
                }

                if(delta < MIN_FRAME_DURATION){
                    long sleep = MIN_FRAME_DURATION - delta;
                    long millis = sleep / 1000000;
                    int nanos = (int) (sleep - millis * 1000000);
                    Thread.sleep(millis, nanos);
                    passedFrameTime += sleep;
                    delta += sleep;
                }

            } catch (Exception e){

                e.printStackTrace();

            }
        }
    }
}
