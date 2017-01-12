/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raspizerocam2017;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kimuratadashi
 */
public class Shutdown implements GpioPinListenerDigital {

    private final RaspiZeroCam2017 raspizerocam2017; //Mainのメンバを使用するためのインスタンスを用意

    //コンストラクタ
    public Shutdown(RaspiZeroCam2017 aThis) {
        this.raspizerocam2017 = aThis; //Mainにあるメンバを使用するためRaspiCamTest型raspicamtestに入れる。
        System.out.println("Shutdownコンストラクタ実行");
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(
            GpioPinDigitalStateChangeEvent event) {
        boolean sense = this.raspizerocam2017.isShutdown();
        System.out.println(sense);
        if (sense) {//shutdown用のboolean変数がtrueであれば
            System.out.println("シャットダウンします");
            ledRedBlink();
            this.raspizerocam2017.gpio.shutdown();
            Commands.killvid();
            Commands.killfbcp();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Shutdown.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
            Commands.shutdown();

        } else {
            System.out.println("else側処理 " + sense);
            //this.raspizerocam2017.getRed().high();
            //Commands.killvid();
            this.raspizerocam2017.getRed().low();

        }
        this.raspizerocam2017.toggleShutdown();//トグル処理

    }

    private void ledRedBlink() {
        for (int i = 0; i < 5; i++) {
            this.raspizerocam2017.getRed().high();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Logger.getLogger(Shutdown.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

            this.raspizerocam2017.getRed().low();

            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Logger.getLogger(Shutdown.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }

    }

}
