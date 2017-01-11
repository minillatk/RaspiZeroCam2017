/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raspizerocam2017;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author minillatk
 */
class MovieOnOffStateListener implements GpioPinListenerDigital {

    private final RaspiZeroCam2017 raspizerocam2017; //Mainのメンバを使用するためのインスタンスを用意

    private final String width = "1280";
    private final String height = "720";
    private final String fps = "30";
    private final String rotation = "270";
    private final String quolity = "100"; //jpegのクオリティー
    private final String destDir = "/home/pi/capture/";

    // Remember to add filename and extension!
    private final String startInstruction = "/usr/bin/raspivid -ISO 100 -awb auto -t 5000 " + "-q" + quolity + " -h " + height + " -w " + width + " -rot " + rotation + " -o " + destDir;

    private final String killInstruction = "killall raspivid";

    //コンストラクタ
    public MovieOnOffStateListener(RaspiZeroCam2017 aThis) {
        this.raspizerocam2017 = aThis; //Mainにあるメンバを使用するためRaspiCamTest型raspicamtestに入れる。
        System.out.println("MovieOnOffStateListenerコンストラクタ実行");
    }
    
    @Override
public void handleGpioPinDigitalStateChangeEvent(
            GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        if (this.raspizerocam2017.isCapturing()) {
            System.out.println("Killing raspivid");
            this.raspizerocam2017.getRed().low();
            killCapture();
        } else {
            System.out.println("Starting raspivid");
            this.raspizerocam2017.getRed().high();
            startCapture();
        }
        this.raspizerocam2017.toggleCapture();

    }

    private void killCapture() {
        executeCommand(this.killInstruction);
    }

    private void startCapture() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MMdd_HHmm_ss");
        String filename = this.startInstruction + "vid-" + dateFormat.format(
                date) + ".h264";
        executeCommand(filename);
    }

    private void executeCommand(String cmd) {
        Runtime r = Runtime.getRuntime();
        try {
            r.exec(cmd);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }   
}
