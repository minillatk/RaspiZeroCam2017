/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raspizerocam2017;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 *
 * @author minillatk
 */
public class Reset implements GpioPinListenerDigital {

    private final RaspiZeroCam2017 raspizerocam2017; //Mainのメンバを使用するためのインスタンスを用意

    //コンストラクタ
    public Reset(RaspiZeroCam2017 aThis) {
        this.raspizerocam2017 = aThis; //Mainにあるメンバを使用するためRaspiCamTest型raspicamtestに入れる。
        System.out.println("Resetコンストラクタ実行");
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(
            GpioPinDigitalStateChangeEvent event) {

        this.raspizerocam2017.toggleReset();//トグル処理
        switch (this.raspizerocam2017.isReset()) {
            case 0:
                break;
            case 1:
                System.out.println("OFF");
                vidOff();
                break;
            case 2:
                break;
            case 3:
                System.out.println("ON");
                vidOn();
                break;
        }
    }

    private void vidOn() {
        Commands.startPreviewVid();
        this.raspizerocam2017.vidOnOffCheck_On();
    }

    private void vidOff() {
        Commands.killvid();
        Commands.killfbi();
        Commands.executeCommand(this.raspizerocam2017.Splash);
        this.raspizerocam2017.vidOnOffCheck_Off();
    }

}
