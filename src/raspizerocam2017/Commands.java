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
    public static void killfbi(){
        executeCommand("killall fbi");
    }
    
    public static void startfbcp(){
        executeCommand("fbcp");
    }
    public static void killfbcp(){
        executeCommand("killall fbcp");
    }

    public static void killvid(){
        executeCommand("killall raspivid");
    }
    
    public static void startPreviewVid(){
        executeCommand("/usr/bin/raspivid -w 192 -h 128 -t 0 -e -rot 270");
        
    }
    
    public static void killjava(){
        executeCommand("killall java");
    }
    
    public static void shutdown(){
        executeCommand("shutdown -h now");
    }
}
