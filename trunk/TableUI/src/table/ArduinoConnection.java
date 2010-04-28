package table;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This class is for communicating with the Arduino microcontroller that is the
 * brains of the table. This class abstracts the underlying serial communication
 * into simple-to-use Java method calls.
 * 
 * The arduino responds to messages sent to it, but generates no events of its
 * own. It must be polled periodically to update data in the UI.
 * 
 * @author Jay Roltgen
 * 
 */
public abstract class ArduinoConnection {

	protected enum MessageType {
		CONNECT, GET_PRESSURE, KEEP_ALIVE, SET_PRESSURE, 
		SET_VIBRATION, SET_SPEED, START, STOP
	}

	protected enum Response {
		SUCCESS, FAILURE
	}

	protected DataInputStream _in;
	protected DataOutputStream _out;

	private static ArduinoConnection _instance = null;
	
	protected ArduinoConnection() {
		connect();
	}

	public static ArduinoConnection getInstance() {
		if (_instance == null) {
			_instance = new ArduinoConnectionCSharpPipeImpl();
		}
		return _instance;
	}
	

	/**
	 * Connects to the Arduino. This will return true if successful and false
	 * otherwise.  This method delegates the actual setting up of the data
	 * input and output streams to the subclasses, which determine the actual
	 * implementation of said set-up-age.
	 * 
	 * @return true on success, false on error.
	 */
	public abstract boolean connect();

	/**
	 * Returns the actual pressure indicated by the table - this should be
	 * displayed somehow in the UI.
	 * 
	 * @return The indicated pressure.
	 */
	public int getIndicatedPressure() {
		System.out.println("Getting indicated pressure.");
		synchronized (this) {
			try {
				_out.write(MessageType.GET_PRESSURE.ordinal());
				_out.write(0);
				int pressure = (_in.read() & 0xFF);
				return pressure;
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
		}
	}

	/**
	 * Sets the pressure in pounds for the table.
	 * 
	 * @param pounds
	 *            The desired pressure in pounds (should range from 0 to 30 or
	 *            so).
	 * 
	 * @return true on success, false on error.
	 */
	public boolean setPressure(int pounds) {
		System.out.println("Setting pressure: " + pounds);
		synchronized (this) {
			try {
				_out.write(MessageType.SET_PRESSURE.ordinal());
				_out.write(pounds);
				return handleAck();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	};

	/**
	 * Sets the vibration for the table - this is indicated as an integer
	 * percentage between 0 and 100 percent.
	 * 
	 * @param percentage
	 *            The percentage of vibration desired.
	 * 
	 * @return true on success, false on error.
	 */
	public boolean setVibration(int percentage) {
		System.out.println("Setting vibration: " + percentage);
		synchronized (this) {
			try {
				_out.write(MessageType.SET_VIBRATION.ordinal());
				_out.write(percentage);
				return handleAck();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public boolean setSpeed(int percentage) {
		System.out.println("Setting speed: " + percentage);
		synchronized (this) {
			try {
				_out.write(MessageType.SET_SPEED.ordinal());
				_out.write(percentage);
				return handleAck();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * Starts the table running.
	 * 
	 * @return true on success, false on error.
	 */
	public boolean start() {
		System.out.println("Starting...");
		synchronized (this) {
			try {
				_out.write(MessageType.START.ordinal());
				_out.write(0);
				return handleAck();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * Will immediately stop the table and lower the pressure arm.
	 * 
	 * @return true on success, false on error.
	 */
	public boolean stop() {
		System.out.println("Stopping.");
		synchronized (this) {
			try {
				_out.write(MessageType.STOP.ordinal());
				_out.write(0);
				return handleAck();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	/**
	 * Handles an acknowledgement from the arduino - returns false if a false
	 * response or no response is received.
	 * 
	 * @return true on sucess, false otherwise.
	 * @throws IOException
	 *             on exception
	 */
	protected boolean handleAck() throws IOException {
		//System.out.println("Handling ack.");
		int response = _in.read();
		//System.out.println("Got response: " + response);
		if (response == Response.SUCCESS.ordinal()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This method keeps the table alive by "feeding the watchdog" for the
	 * table. This is provided so that in the event that the UI or computer
	 * loses power or crashes, the table will stop running within 1 second.
	 * 
	 * Thus, this method MUST be called by the UI at least once per 500
	 * milliseconds or the table will stop running and reset.
	 * 
	 * @return
	 */
	public boolean keepAlive() {
		System.out.println("Stayin' alive:");
		synchronized (this) {
			try {
				_out.write(MessageType.KEEP_ALIVE.ordinal());
				_out.write(0);
				return handleAck();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	
	public static void main(String[] args) {
		ArduinoConnectionDirectImpl c = new ArduinoConnectionDirectImpl();
		c.connect();
		System.out.println("Done connecting.");
	}


}
