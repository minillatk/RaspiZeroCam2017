# RaspiZeroCam2017
RaspiZero video camera recoder for JAVA

This is NetBeans Project file.

PCのNetBeansでプロジェクトを作成し、RaspiZeroにjarファイルを転送することを前提に制作しています。

### 準備
/home/pi/にディレクトリをあらかじめ作成してください。   
` $ mkdir capture Pictures `   
* capture    
ムービーを格納するディレクトリ
* Picture    
静止画を格納するディレクトリ

RaspiZeroCam2017に附属するraspizerocam.pngをcaptureに格納してください。`RaspiZeroCam2017/src/img/raspizerocam.png`

RaspiZeroからの起動方法：   
` $sudo java -jar /home/pi/NetBeansProject/RaspiZeroCam2017/dist/RaspiZeroCam2017.jar
`
