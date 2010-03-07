This code uses the RXTX serial library, available here: http://www.rxtx.org/

It uses JNI to talk to Windows serial ports and requires two files:
 - RXTXcomm.jar
 - rxtxSerial.dll
 
These two files are included here.  They need to be placed in the following
directories in the root of your Java install directory in order for you to be 
able to execute this code:

 - RXTXcomm.jar:
   $JAVA_HOME/jre/lib/ext
   
 - rxtxSerial.dll:
   $JAVA_HOME/jre/bin
   
 Note: I haven't tested this myself yet, but plan to on Monday night 3/8.
