/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raspizerocam2017;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author minillatk
 */
public class RaspiZeroCam2017 {

    public static final String VERSION = "RaspiZeroCam VERSION 0.1";

    // This is the controller.
    private GpioController gpio;

    // The current pin mapping
    private static final Pin greenPin = RaspiPin.GPIO_28;
    private static final Pin redPin = RaspiPin.GPIO_29;
    private static final Pin leverPin_A = RaspiPin.GPIO_21;
    private static final Pin leverPin_B = RaspiPin.GPIO_22;
    private static final Pin leverPin_T = RaspiPin.GPIO_23;
    private static final Pin rstPin = RaspiPin.GPIO_24;
    private static final Pin moviePin = RaspiPin.GPIO_25;

    // アウトプットピン設定 The pins to which we attach LEDs
    private final GpioPinDigitalOutput red;
    private final GpioPinDigitalOutput green;

    // インプットピン設定 this is going to be an input PULL_UP, see below.
    private final GpioPinDigitalInput leverBTN_A;
    private final GpioPinDigitalInput leverBTN_B;
    private final GpioPinDigitalInput leverBTN_T;
    private final GpioPinDigitalInput rstBTN;
    private final GpioPinDigitalInput movieOnOffSW;

    // キャプチャするときにtrueに設定 set to true when capturing
    private boolean capturing;

    //スチール用の変数
    private boolean shutterPressed;

    //RaspiZeroCam2017コンストラクタ
    public RaspiZeroCam2017() {

        this.gpio = GpioFactory.getInstance();
        this.red = gpio.provisionDigitalOutputPin(redPin);
        this.green = gpio.provisionDigitalOutputPin(greenPin);
        this.leverBTN_A = gpio.provisionDigitalInputPin(leverPin_A,
                PinPullResistance.PULL_UP);
        this.leverBTN_B = gpio.provisionDigitalInputPin(leverPin_B,
                PinPullResistance.PULL_UP);
        this.leverBTN_T = gpio.provisionDigitalInputPin(leverPin_T,
                PinPullResistance.PULL_UP);
        this.rstBTN = gpio.provisionDigitalInputPin(rstPin,
                PinPullResistance.PULL_UP);
        this.movieOnOffSW = gpio.provisionDigitalInputPin(moviePin,
                PinPullResistance.PULL_UP);
        
                //リスナーはカメラと赤色LEDのON/OFFを操作する
        //The listener takes care of turning on and off the camera and the red LED
        movieOnOffSW.addListener(new MovieOnOffStateListener(this));
        
        //スチールカメラ用ボタンleverBTN_Tの状態読み取り
        leverBTN_T.addListener(new Stillimg(this));
    }
     //スチールカメラ用メソッド
    public boolean stillCapturing() {
        System.out.println("shutterPressed:" + this.shutterPressed);
        return this.shutterPressed; //shutterPressedの値を渡す
    }

    public void togglestillCapture() {
        this.shutterPressed = !this.shutterPressed;//反転
        System.out.println("togglestillCapture:" + this.shutterPressed);
    }
    
    //
    public boolean isCapturing() {
        return this.capturing;
    }

    public void toggleCapture() {
        this.capturing = !this.capturing; //capturingの値を反転させる
    }
    
    public GpioPinDigitalOutput getRed() {
        return this.red;
    }

    public GpioPinDigitalOutput getGreen() {
        return this.green;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(VERSION);
        RaspiZeroCam2017 zerocam = new RaspiZeroCam2017(); //RaspiZeroCam2017をインスタンス化
        zerocam.getGreen().high();
        System.out.println("System start");
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RaspiZeroCam2017.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

        }
    }

}
