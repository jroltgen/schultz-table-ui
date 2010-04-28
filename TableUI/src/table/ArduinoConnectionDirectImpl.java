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
public class ArduinoConnectionDirectImpl extends ArduinoConnection {

	private static final int BAUD_RATE = 9600;
	private static final int TIMEOUT_MS = 3000;


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
	public boolean connect() {
		synchronized (this) {
			// Find the comm port.
			CommPortIdentifier portID;
			try {
				portID = CommPortIdentifier.getPortIdentifier("COM3");
			} catch (NoSuchPortException e) {
				e.printStackTrace();
				return false;
			}
			System.out.println("Connecting");
			if (portID.isCurrentlyOwned()) {
				System.out.println("Error: Port is currently in use");
			} else {
				CommPort commPort;
				System.out.println("Opening comm port");
				// Open the port.
				try {
					commPort = portID.open(this.getClass().getName(),
							TIMEOUT_MS);
				} catch (PortInUseException e) {
					e.printStackTrace();
					return false;
				}
				System.out.println("Opened.");
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
					System.out.println("Initing data streams.");
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
			System.out.println("Sending message.");
			// Send the connect message.
			try {
				_out.write(MessageType.CONNECT.ordinal());
				_out.write(0);
				return handleAck();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
}
