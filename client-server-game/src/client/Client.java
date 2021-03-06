package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	static final String HOST = "localhost";
	static final int PORT = 1450;

	public static void main(String[] args) throws IOException {

		Socket socket = null;
		socket = new Socket(HOST, PORT);

		DataInputStream input = new DataInputStream(socket.getInputStream());
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		Scanner sc = new Scanner(System.in);

		while (true) { 
			
			int player = input.readInt(); 	// 1 Receiving player #
			System.out.println("You are player "+player);
			
			while(true) {
				String msg = input.readUTF();
				System.out.print(msg); 	// 2 Receiving message
				if(msg.startsWith("Player 2")) continue;
				int n = sc.nextInt();
				output.writeInt(n);	// 3 Sending number
				if (n == -11) break;  //////
			}
			
			input.readUTF();	//////
			
			int number = sc.nextInt(); //////
			output.writeInt(number); //////
			
			if (number != 0) break; //////

		}
		sc.close();
		socket.close();

	}
}
