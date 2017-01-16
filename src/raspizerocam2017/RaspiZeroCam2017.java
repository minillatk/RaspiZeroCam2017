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

    public static final String VERSION = "RaspiZeroCam VERSION 0.1h";
    public static final String Splash = "fbi -nocomments -noverbose -a -T 10 -d /dev/fb1 /home/pi/Pictures/raspizerocam.png";//スプラッシュ画像のディレクトリ

    // This is the controller.
    public static GpioController gpio;

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

    private boolean movieCapturringPressed;// ムービー撮影するときにtrueに設定 set to true when capturing_toggle
    private boolean shutterPressed;//スチール用のブーリアン変数トグル処理に使う
    private int resetPressed;//リセット用の変数トグル処理に使う
    private boolean vidOnOffCheck;//プレビューRaspividのトグルに使う
    private boolean shutdownPressed;//シャットダウン用のブーリアン変数トグル処理に使う
    private int testCheck;//testクラス用

    //RaspiZeroCam2017コンストラクタ
    public RaspiZeroCam2017() {

        //this.testCheck = 0;

        Commands.executeCommand(Splash);

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

        Commands.startfbcp();
        Commands.startPreviewVid();

        //各々のボタン類をリスナーで監視する。ここでボタンと命令がひもつけされる。
        //リスナーはカメラと赤色LEDのON/OFFを操作する
        //トグルスイッチの状態を監視し、MovieOnOffStateListenerを作動させる。
        movieOnOffSW.addListener(new MovieOnOffStateListener(this));
        //Stillimgクラス作動。スチールカメラ用ボタンleverBTN_Tの状態読み取り。
        leverBTN_T.addListener(new Stillimg(this));
        //Resetクラス作動。leverBTN_Aの状態読み取り。
        leverBTN_A.addListener(new Reset(this));
        //Shutdownクラス作動。リセットボタンの状態読み取り。
        rstBTN.addListener(new Shutdown(this));
        //テストクラス作動
        leverBTN_B.addListener(new Test(this));

    }

    /**
     * スチール撮影用トリガー変数のトグル状態を返す
     *
     * @return shutterPressed
     */
    public boolean stillCapturing() {
        return this.shutterPressed; //shutterPressedの値を渡す
    }

    /**
     * スチール撮影用トリガー変数のトグル処理
     */
    public void toggleStillCapture() {
        this.shutterPressed = !this.shutterPressed;//反転
    }

    //ムービー撮影のトグル状態を返す
    public boolean isMovieCapturing() {
        return this.movieCapturringPressed;
    }

    //静止画キャプチャーのトグル処理
    public void toggleMovieCapture() {
        this.movieCapturringPressed = !this.movieCapturringPressed; //capturingの値を反転させる
    }

    //リセット処理のためのトグル状態を返す
    public int isReset() {
        return this.resetPressed;
    }

    //リセット処理のトグル
    public void toggleReset() {//数値ごとに命令を出すため、カウントさせる
        this.resetPressed++;
        this.resetPressed = this.resetPressed <= 3 ? this.resetPressed++ : 0;
    }

    public boolean isVidOnOffCheck() {
        return this.vidOnOffCheck;
    }

    public void vidOnOffCheck_On() {
        this.vidOnOffCheck = true;
    }

    public void vidOnOffCheck_Off() {
        this.vidOnOffCheck = false;
    }

    //シャットダウン処理のためのトリガー
    public boolean isShutdown() {
        return this.shutdownPressed;
    }

    public void toggleShutdown() {
        this.shutdownPressed = !this.shutdownPressed;//反転
    }

    //Testクラス用
    public int isTestCheck() {
        return this.testCheck;
    }

    public void TestCheck() {//数値ごとに命令を出すため、カウントさせる
        this.testCheck++;
        this.testCheck = this.testCheck <= 3 ? this.testCheck++ : 0;
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
        zerocam.getRed().low();
        zerocam.getGreen().low();
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
