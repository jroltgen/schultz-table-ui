package table;

/**
 * This class is for communicating with the Arduino microcontroller that is
 * the brains of the table.  This class abstracts the underlying serial
 * communication into simple-to-use Java method calls.
 * 
 * The arduino responds to messages sent to it, but generates no events of its
 * own.  It must be polled periodically to update data in the UI.
 * 
 * @author Jay Roltgen
 *
 */
public class ArduinoConnection {

	/**
	 * Connects to the Arduino.  This will return true if successful and
	 * false otherwise.
	 * 
	 * @return
	 */
	public boolean connect() {
		// TODO implement
		return false;
	}
	
	public float getIndicatedPressure() {
		// TODO implement
		return 0.0f;
	}
	
	public boolean setVibration(float percentage) {
		// TODO implement
		return false;
	}
	
	public boolean setPressure(int pounds) {
		// TODO implement
		return false;
	}
	
	public boolean stop() {
		// TODO implement
		return false;
	};
	
	public boolean start() {
		// TODO implement
		return false;
	};
	
	/**
	 * This method keeps the table alive by "feeding the watchdog" for the
	 * table.  This is provided so that in the event that the UI or computer
	 * loses power or crashes, the table will stop running within 1 second.
	 * 
	 * Thus, this method MUST be called by the UI at least once per 500
	 * milliseconds or the table will stop running and reset.
	 * 
	 * @return
	 */
	private boolean keepAlive() {
		// TODO implement - Jay will do this w/ a thread in this class
		// so the UI does not have to worry about it.
		return false;
	}
	
	
	
	
	
}
