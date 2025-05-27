package SimpleChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;




public class ChatClient {
	private Socket clientSocket;
	private final int TIMEOUT = 2000;
	private boolean isRunning;


	public ChatClient() {
		isRunning = false;
	}
	
	
	public static void main(String[] args) {
		int port;
		if (args.length == 2) {
			try {
				port = Integer.parseInt(args[1]);
				if (1024 <= port && port <= 65535) {
					new ChatClient().startClient(args[0], port);
				} else {
					System.err.println("Incorrect port error.");
				}
			}
			catch (NumberFormatException e) {
				System.err.println("Incorrect port error.");
			}
		}
		else {
			System.out.println("Usage: java SimpleChat.ChatClient <address> <port>");
		}
	}


	public void startClient(String address, int port) {
		try {
			clientSocket = new Socket();
			clientSocket.connect(new InetSocketAddress(address,port), TIMEOUT);
			isRunning = true;
			System.out.println("Connected to "+clientSocket.getLocalAddress()+":"+port);

			
			new Thread(() -> receiveMessages()).start();
			// or new Thread(this::receiveMessages).start();

			// Main thread for sending messages
			sendMessages();

		}
		catch (SocketTimeoutException e) {
			System.err.println("Connection timeout.");
		}
		catch (IOException e) {
			System.err.println("Connection error: " + e.getMessage());
		} 
		finally {
			if (isRunning) {
				closeClientSocket();
			}
		}
	}


	private void receiveMessages() {
		try (BufferedReader inputClientStream = new BufferedReader(
				new InputStreamReader(clientSocket.getInputStream()))) {
			
			String serverMessage;
			while (isRunning && (serverMessage = inputClientStream.readLine()) != null) {
				System.out.println("\n"+serverMessage);
				System.out.print("you: ");
			}
		} catch (IOException e) {
			// if server stopped
			if (isRunning) {
				System.err.println("Error receiving message: " + e.getMessage());
			}
		} finally {
			closeClientSocket();
		}
	}



	private void sendMessages() {
		try (PrintWriter outputClientStream = 
				new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader userInputStream = new BufferedReader(
				new InputStreamReader(System.in))) {
			
			String message;
			
			while (isRunning && !(message = userInputStream.readLine()).equalsIgnoreCase("exit")) {
				outputClientStream.println(message);
				System.out.print("you: ");
			}
		} catch (IOException e) {
			System.err.println("Error sending message: " + e.getMessage());
		}
	}


	private void closeClientSocket() {
		isRunning = false;
		try {
			if (clientSocket != null) {
				clientSocket.close();
			}
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
