package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import client.ClientHandler;

public class Server {
	private static final int PORT = 1450;
	static int player = 1;

	private static ArrayList<ClientHandler> clients = new ArrayList<>();

	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Server Started...");

		while (true) {
			Socket client = serverSocket.accept(); // waits for an incoming client connection
			ClientHandler clientThread = new ClientHandler(client, player, clients);
			player++;
			clients.add(clientThread);

			clientThread.start();
		}

	}

}
