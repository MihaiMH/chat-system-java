package chatSystem.client.Utils;

import javax.sound.sampled.*;


// CLASS NOT MADE BY THE CREATOR OF THIS PROJECT
// CREDITS TO: https://www.rgagnon.com/javadetails/java-0632.html
public class SoundUtils {

  public static float SAMPLE_RATE = 8000f;

  public static void tone(int hz, int msecs)
      throws LineUnavailableException
  {
    tone(hz, msecs, 1.0);
  }

  public static void tone(int hz, int msecs, double vol)
      throws LineUnavailableException
  {
    byte[] buf = new byte[1];
    AudioFormat af =
        new AudioFormat(
            SAMPLE_RATE, // sampleRate
            8,           // sampleSizeInBits
            1,           // channels
            true,        // signed
            false);      // bigEndian
    SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
    sdl.open(af);
    sdl.start();
    for (int i=0; i < msecs*8; i++) {
      double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
      buf[0] = (byte)(Math.sin(angle) * 127.0 * vol);
      sdl.write(buf,0,1);
    }
    sdl.drain();
    sdl.stop();
    sdl.close();
  }

  public static void main(String[] args) throws Exception {

    Thread.sleep(1000);
    SoundUtils.tone(100,1000);
    Thread.sleep(1000);
    SoundUtils.tone(5000,100);
    Thread.sleep(1000);
    SoundUtils.tone(400,500);
    Thread.sleep(1000);
    SoundUtils.tone(400,500, 0.2);

  }
}