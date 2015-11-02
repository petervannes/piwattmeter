package piwattmetersync;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

public final class PiWattMeterSync {

    private static final int CLOCKGPIO = 0; // pin 11
    private Pin gpioClockPin;

    private final GpioController gpio;
    private final GpioPinDigitalInput clockInputPin;

    private Sync sync;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        PiWattMeterSync pwmSync = new PiWattMeterSync();
        pwmSync.Init();
    }

    public PiWattMeterSync() {

        // create gpio controller
        this.gpio = GpioFactory.getInstance();

        // Clockpin 
        this.gpioClockPin = getPinByNumber(CLOCKGPIO);

        clockInputPin = gpio.provisionDigitalInputPin(this.gpioClockPin, PinPullResistance.PULL_DOWN);

        this.sync = new Sync(clockInputPin);

    }

    public void Init() {

        // create and register gpio pin listener
        clockInputPin.addListener(this.sync);

        // keep program running until user aborts (CTRL-C)
        for (;;) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }

    }

    public Pin getPinByNumber(int pinNumber) {

        String gpioPinName = "GPIO " + pinNumber;

        return RaspiPin.getPinByName(gpioPinName);

    }

}
