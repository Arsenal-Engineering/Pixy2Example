// Pixy2 I2C communication from FRC RoboRIO example
// James Grant, 1/12/2019 
package frc.robot;
import java.rmi.UnexpectedException;

import edu.wpi.first.wpilibj.I2C;

public class Pixy2 {
  I2C i2c = new I2C(I2C.Port.kOnboard, 0x54);  // default address of 0x54, turn on I2C communication in Pixy config

  public void setLamp(Boolean on)
  {
    byte[] send = new byte[6];
    byte[] get = new byte[10];

    send[0] = (byte) 174;  // Sync byte
    send[1] = (byte) 193;  // Sync byte
    send[2] = (byte) 22;   // setLamp
    send[3] = (byte) 2;    // Data size
    send[4] = (byte) (on ? 1 : 0);  // Upper two white LEDs
    send[5] = (byte) 0;    // All channels of lower RGB LED

    System.out.println("PIXY: Turning " + (on ? "on" : "off") +  " Pixy2 LED");
    i2c.transaction(send, send.length, get, get.length);

    // You should check the retval of transaction() call and verify the data coming back is valid
    //for (int i = 0; i < get.length; ++i)
      //System.out.println("PIXY_LED: get[" + i + "]: " + get[i]);
  }

  public void getLines()
  {
    byte[] send = new byte[6];
    byte[] get = new byte[20]; // PIXY_BUFFERSIZE from TPixy2.h is 260

    send[0] = (byte) 174; // Sync byte
    send[1] = (byte) 193; // Sync byte
    send[2] = (byte) 48;  // getMainFeatures
    send[3] = (byte) 2;   // size
    send[4] = (byte) 0;   // Main features
    send[5] = (byte) 1;   // Just vectors

    System.out.println("PIXY: Looking for vectors...");
    //i2c.transaction(send, send.length, get, get.length); 
    i2c.writeBulk(send, send.length);
    try{
    Thread.sleep(1);
    }
    catch (InterruptedException e){}
    i2c.read(0x54, get.length, get);

    // You should check the retval of transaction() call and verify the data coming back is valid
    for (int i = 0; i < 6; ++i)
      System.out.println("PIXY: get[" + i + "]: " + get[i]);
    System.out.println("Vector: x[0..78],y[0..51]: x0,y0,x1,y1,index,flags[2=invald,4=intersection]");
    for (int i = 0; i < 10; ++i)
      System.out.println("PIXY_LIN: get[" + (i + 6) + "]: " + get[i + 6]);
  }

  public void setMode()
  {
    byte[] send = new byte[5];
    byte[] get = new byte[10]; 

    send[0] = (byte) 174; // Sync byte
    send[1] = (byte) 193; // Sync byte
    send[2] = (byte) 54;  // setMode
    send[3] = (byte) 1;   // size
    send[4] = (byte) 0x80;   // White Line
   
    System.out.println("PIXY_MOD: set mode...");
    i2c.transaction(send, send.length, get, get.length);
  }

  public void getVersion()
  {
    byte[] send = new byte[4];
    byte[] get = new byte[20]; // PIXY_BUFFERSIZE from TPixy2.h

    send[0] = (byte) 174; // Sync byte
    send[1] = (byte) 193; // Sync byte
    send[2] = (byte) 14;  // setMode
    send[3] = (byte) 0;   // size
   
    System.out.println("PIXY: get version...");
    //i2c.transaction(send, send.length, get, get.length);
    i2c.writeBulk(send, send.length);
    try{
    Thread.sleep(200);
    }
    catch (InterruptedException e){}
    i2c.read(0x54, get.length, get);

    for (int i = 0; i < get.length; ++i)
      System.out.println("PIXY_VER: get[" + i + "]: " + get[i]);
  }
}