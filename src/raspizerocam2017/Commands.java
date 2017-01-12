/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raspizerocam2017;

import java.io.IOException;

/**
 *
 * @author maruhachi
 */
public class Commands {

    public static void executeCommand(String cmd) {
        Runtime r = Runtime.getRuntime();
        try {
            r.exec(cmd);
        } catch (IOException e) {
        }
    }
    public static void startfbcp(){
        executeCommand("killall fbcp");
    }

}
