package table;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

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
public class ArduinoConnection implements Runnable {

	private enum MessageType {
		CONNECT, GET_PRESSURE, GET_VIBRATION, KEEP_ALIVE, SET_PRESSURE, 
		SET_VIBRATION, START, STOP
	}

	private enum Response {
		FAILURE, SUCCESS
	}

	private static final int BAUD_RATE = 9600;
	private static final int TIMEOUT_MS = 3000;

	private DataInputStream _in;
	private DataOutputStream _out;

	public ArduinoConnection() {

	}

	/**
	 * Connects to the Arduino. This will return true if successful and false
	 * otherwise.
	 * 
	 * @param comPortName
	 * 		This is the string of the com port we wish to connect to, such
	 * 		as "COM3", "COM5" or whatever the correct port is.
	 * 
	 * @return true on success, false on error.
	 */
	public boolean connect(String comPortName) {
		synchronized (this) {
			// Find the comm port.
			CommPortIdentifier portID;
			try {
				portID = CommPortIdentifier.getPortIdentifier(comPortName);
			} catch (NoSuchPortException e) {
				e.printStackTrace();
				return false;
			}

			if (portID.isCurrentlyOwned()) {
				System.out.println("Error: Port is currently in use");
			} else {
				CommPort commPort;

				// Open the port.
				try {
					commPort = portID.open(this.getClass().getName(),
							TIMEOUT_MS);
				} catch (PortInUseException e) {
					e.printStackTrace();
					return false;
				}

				if (commPort instanceof SerialPort) {
					// Configure the port.
					SerialPort serialPort = (SerialPort) commPort;
					try {
						serialPort.setSerialPortParams(BAUD_RATE,
								SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
								SerialPort.PARITY_NONE);
					} catch (UnsupportedCommOperationException e) {
						e.printStackTrace();
						return false;
					}

					try {
						_in = new DataInputStream(serialPort.getInputStream());
						_out = new DataOutputStream(serialPort
								.getOutputStream());
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				} else {
					System.out.println("Error: Is not a serial port.");
					return false;
				}
			}

			// Send the connect message.
			try {
				_out.write(MessageType.CONNECT.ordinal());
				return handleAck();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * Returns the actual pressure indicated by the table - this should be
	 * displayed somehow in the UI.
	 * 
	 * @return The indicated pressure.
	 */
	public int getIndicatedPressure() {
		synchronized (this) {
			try {
				_out.write(MessageType.GET_PRESSURE.ordinal());
				int pressure = (_in.read() & 0xFF);
				return pressure * 100 / 255;
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
		synchronized (this) {
			try {
				_out.write(MessageType.SET_VIBRATION.ordinal());
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
		synchronized (this) {
			try {
				_out.write(MessageType.SET_VIBRATION.ordinal());
				_out.write((percentage * 255 / 100) & 0xFF);
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
		synchronized (this) {
			try {
				_out.write(MessageType.START.ordinal());
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
		synchronized (this) {
			try {
				_out.write(MessageType.STOP.ordinal());
				return handleAck();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * Runs this thread which keeps the arduino connection alive - if this
	 * message is not sent often, the arduino will die.
	 */
	@Override
	public void run() {
		while (true) {
			keepAlive();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * Handles an acknowledgement from the arduino - returns false if a false
	 * response or no response is received.
	 * 
	 * @return true on sucess, false otherwise.
	 * @throws IOException
	 *             on exception
	 */
	private boolean handleAck() throws IOException {
		int response = _in.read();
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
	private boolean keepAlive() {
		synchronized (this) {
			try {
				_out.write(MessageType.KEEP_ALIVE.ordinal());
				return handleAck();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

}
