package piwattmetersync;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Sync implements GpioPinListenerDigital {

    private int ClockHighCount = 0;
    private int bitcounter = 0;
    private Boolean inSync = Boolean.FALSE;

    private final GpioPinDigitalInput gpioClockPin;

    public Sync(GpioPinDigitalInput gpioClockPin) {
        this.gpioClockPin = gpioClockPin;
    }

    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        
  

        if (event.getState().equals(PinState.HIGH)) {

            bitcounter++;

            if (!inSync) {

                // Keep track of number readed HIGH pinstates on CLK
                this.ClockHighCount = 0;

                // Measure Pin HIGH state
                while (this.gpioClockPin.getState().equals(PinState.HIGH)) {
                    ClockHighCount += 1;

                    // Sleep 30000 Nano seconds (=30 Microseconds)
                    try {
                        Thread.sleep(0, 30000);
                    } catch (InterruptedException ex) {
                    }

                }

                
                if (ClockHighCount >= 33 && ClockHighCount <= 67) {
                    inSync = Boolean.TRUE;

                    System.out.println("Synchronized [" + String.format("%2.2f", ClockHighCount * 0.03) + " ms] "
                            + String.format("%8d", bitcounter) + " bits  "
                            + String.format("%8d", bitcounter / 8) + " bytes  "
                            + String.format("%8d", bitcounter / 64) + " packages");

                    bitcounter = 0;
                    inSync = Boolean.FALSE;
                }

            } else {
                // DO nothing yet
            }

        }
    }

}
