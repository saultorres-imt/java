package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {

	private DataInputStream input;
	private DataOutputStream output;
	private Socket client;
	private int player;
	private ArrayList<ClientHandler> clients;
	private int number = -1;
	private int guess = -1;

	public ClientHandler(Socket client, int player, ArrayList<ClientHandler> clients) throws IOException {
		this.client = client;
		this.player = player;
		this.clients = clients;
		input = new DataInputStream(client.getInputStream());
		output = new DataOutputStream(client.getOutputStream());
	}

	@Override
	public void run() {
		synchronized (clients) {
			try {
				if (player == 1) {
					System.out.println("Player " + player + " connected, waiting the second");
					output.writeInt(player); // 1 Sending player #
					clients.wait();	// 1
					while (true) {
						// sleep(100);
						while (clients.size() == 2 ) { // Not executed until Player 2 exists
							System.out.println("Waiting number from player 1");
							output.writeUTF("Please type a number to start to play: "); // 2 Sending message
							setNumber(input.readInt()); // 3 Receiving the number
							System.out.println("The number is: " + number);
							clients.notify();	// 2
							clients.wait();		// 3
						while(clients.size() == 2) {
							
							if(getGuess() == getNumber()) {
								output.writeUTF("Player 2 attempted with " + getGuess() + ", player 2 won");
								clients.notify(); 		// 4
								clients.wait();
							}else {
								output.writeUTF("Player 2 attempted with " + getGuess() + "\n");
								clients.notify(); 		// 4
								clients.wait(); 		// 5
								continue;
							}
						}
						}
					}

				} else {
					System.out.println("Player " + player + " connected, will start the game");
					output.writeInt(player); // 1 Sending player #
					clients.notify();	// 1
					clients.wait();		// 2
					
					output.writeUTF("Please guess the number of the player 1\nType a number: ");// 2 Sending message
					
					while (getClients().get(0).getNumber() != -1) {

						System.out.println("Waiting guess from player 2");
						setGuess(input.readInt()); // 3 Receiving number
						getClients().get(0).setGuess(getGuess()); // saving the guess in player 1
						System.out.print("The number from player 2 is: " + guess);
						clients.notify(); 		// 3 & 5
						clients.wait(); 		// 4
						if (guess > getClients().get(0).getNumber()) {
							output.writeUTF("Is greater than the number to guess\nType a number: "); // 7 Sending message																		
							System.out.print(". Is greater than the number\n");
							continue;
						} else if (guess < getClients().get(0).getNumber()) {
							output.writeUTF("Is lesser than the number to guess\nType a number: ");
							System.out.print(". Is lesser than the number\n");
							continue;
						} else {
							output.writeUTF("Congrats, you won!\nPlease type a number to start to play: ");
							System.out.print(". Player 2 won");
							break;
						}
					}

				}

			} catch (IOException | InterruptedException e) {
				System.err.println("IO exception in clien handler");
			} finally {
//			System.out.println("Closing server");
//			try {
//				client.close();
//				input.close();
//				output.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			}
		}

	}

	public ArrayList<ClientHandler> getClients() {
		return clients;
	}

	public void setClients(ArrayList<ClientHandler> clients) {
		this.clients = clients;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getGuess() {
		return guess;
	}

	public void setGuess(int guess) {
		this.guess = guess;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

}
