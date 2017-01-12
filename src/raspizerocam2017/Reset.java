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
        boolean check = this.raspizerocam2017.isVidOnOffCheck();
        if (!check) {//resetボタン用のboolean変数がtrueであれば
            Commands.killvid();
            Commands.executeCommand(this.raspizerocam2017.Splash);
            this.raspizerocam2017.vidOnOffCheck_Off();
        } else {
            Commands.startDemoVid();
            this.raspizerocam2017.vidOnOffCheck_On();
        }
        
        this.raspizerocam2017.toggleReset();//トグル処理

    }
}
