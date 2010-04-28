package table;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ArduinoConnectionCSharpPipeImpl extends ArduinoConnection {
	
	private Socket _arduinoSocket;

	@Override
	public boolean connect() {
		System.out.println("connecting...");
		try {
			_arduinoSocket = new Socket(InetAddress.getByName("localhost"), 3007);
			_in = new DataInputStream(_arduinoSocket.getInputStream());
			_out = new DataOutputStream(_arduinoSocket.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
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
