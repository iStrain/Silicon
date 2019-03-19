
/*
 * Class Sound: Simple sound generator for Silicon Project.  It is simple ... Java does all the work.
 * The methods are threaded so you can go: new Thread(new Tone(int hz, int msecs)).start() to play a sound.
 * If you must, you can also set the volume: Sound.tone(int hz, int msecs, double vol),where "vol"
 * is between 0.0d and 1.0d.
 * This Class was adapted from some horrible (mainly non-functional) code at Stack Overflow.
 * Coding credits(!): "https://stackoverflow.com/a/6700039"
 * The original worked at the specific combination of sample rate and frequency it used 
 * (for a unique definition of "worked" -- ie. played a completely different frequency).
 * It also produced silence at the frequencies normal ears can handle.
 * And it just flicked method-specific errors up-the-chain (don't you hate that?).
 * Finally, it blocked the calling method until the sound was finished.
 * Our adaptation seems to work for frequencies up to about 11KHz, doesn't block, and handles its own errors.  Cough.
 */

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Tone implements Runnable {
    private static float SAMPLE_RATE = 22000f; // Don't mess with this number. You have been warned!
    private static int MULTIPLIER = 22; // Yes, it's an int equal to the sample rate divided by 1,000
    private int hz; // Frequency of sound to play
    private int msecs; // Milliseconds to play sound
    private double vol; // Volume (0.0 ... 1.0) for sound

    /*
     * This is the entry point for code that just wants to specify frequency and
     * duration. To play notes, see https://pages.mtu.edu/~suits/notefreqs.html
     */
    public Tone(int hz, int msecs) {
	this.hz = hz;
	this.msecs = msecs;
	this.vol = 1.0d;
    }

    /*
     * This is the entry point for code that wants to specify frequency, duration
     * *and* volume (0.0d ... 1.0d)
     */
    public Tone(int hz, int msecs, double vol) {
	this.hz = hz;
	this.msecs = msecs;
	this.vol = vol;
    }

    public void run() {
	byte[] buf = new byte[1];
	AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
	SourceDataLine sdl;

	// Sound output lines can be a bit scarce at times - worth a try/catch block
	try {
	    sdl = AudioSystem.getSourceDataLine(af);
	} catch (LineUnavailableException e) {
	    e.printStackTrace();
	    return;
	}

	/*
	 * We probably could have got away with flicking this one, 'cos there's not much
	 * chance it'll fire if we got the source-data-line, but meh, I've put it in a
	 * try/catch block anyway!
	 */
	try {
	    sdl.open(af);
	} catch (LineUnavailableException e) {
	    e.printStackTrace();
	    return;
	}

	/*
	 * When you consider that the original author doubtless cribbed the math from a
	 * textbook, the fact that he or she screwed up everything else, is really not
	 * very impressive. However, one obvious enhancement over the current version
	 * would be to include an ADSR amplitude envelope generator that varies vol in
	 * the loop, based on i. Any takers?
	 */
	sdl.start();
	for (int i = 0; i < msecs * MULTIPLIER; i++) {
	    double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
	    buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
	    sdl.write(buf, 0, 1);
	}

	// Nice safe close-down (yes, I actually liked these 3 lines - no data bytes
	// were harmed during the making of this exit from the method)
	sdl.drain();
	sdl.stop();
	sdl.close();
    }
}
