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
class Stillimg implements GpioPinListenerDigital {
//RaspiCamTestのメンバ参照用インスタンス
    private RaspiZeroCam2017 raspizerocam2017;
    
    //ファイルネームとかのフィールド
	private final String height = "2464";
	private final String width = "3280";
        private final String rotation = "270";
	private final String destDir = "/home/pi/Pictures/";
        
        // ファイル名と拡張子を追加することを忘れないでください！
        private final String startInstruction = "/usr/bin/raspistill -t 1000 -h "+height+ " -w "+width+ " -rot " + rotation + " -o "+destDir;


    public Stillimg(RaspiZeroCam2017 aThis) {
                System.out.println("Stillimgのコンストラクタ実行");
        this.raspizerocam2017 = aThis;
    }
    
       @Override
    public void handleGpioPinDigitalStateChangeEvent(
            GpioPinDigitalStateChangeEvent event) {
        		// display pin state on console
		System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
		if (this.raspizerocam2017.stillCapturing()) {
		System.out.println("こっちがオフ");
		} else {
		System.out.println("こっちオン！");
		still();
		//test("ぬっぴょろん"); //
		}
		this.raspizerocam2017.togglestillCapture();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    	private void still() {
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMdd-HHmm-ss");
		String filename = this.startInstruction + dateFormat.format(date) + ".jpeg";
		executeCommand(filename);
		System.out.println("stillメソッドだよ？");
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
