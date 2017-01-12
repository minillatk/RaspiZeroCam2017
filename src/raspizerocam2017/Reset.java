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
        // display pin state on console
        if (this.raspizerocam2017.isReset()) {//resetボタン用のboolean変数がtrueであれば
            System.out.println("Killing raspivid");
            Commands.startDemoVid();
        } else {
            System.out.println("Starting raspivid");
            //this.raspizerocam2017.getRed().high();
            Commands.killvid();
            
        }
        this.raspizerocam2017.toggleReset();//トグル処理

    }
}
