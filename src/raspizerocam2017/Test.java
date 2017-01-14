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
 * @author kimuratadashi
 */
public class Test implements GpioPinListenerDigital {

    private final RaspiZeroCam2017 raspizerocam2017; //Mainのメンバを使用するためのインスタンスを用意

    //コンストラクタ
    public Test(RaspiZeroCam2017 aThis) {
        this.raspizerocam2017 = aThis; //Mainにあるメンバを使用するためRaspiCamTest型raspicamtestに入れる。
        System.out.println("Resetコンストラクタ実行");
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(
            GpioPinDigitalStateChangeEvent event) {
        this.raspizerocam2017.TestCheck();
        switch (this.raspizerocam2017.isTestCheck()) {
            case 0:
                break;
            case 1:
                System.out.println("OFF");
                break;
            case 2:
                break;
            case 3:
                System.out.println("ON");
                break;
        }
        System.out.println("テストクラスのチェンジイベント作動 : " + this.raspizerocam2017.
                isTestCheck());
    }

}
