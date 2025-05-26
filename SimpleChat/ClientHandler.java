package SimpleChat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import SimpleChat.ChatServer;



public class ClientHandler implements Runnable {
	private final Socket clientSocket;
	private PrintWriter outputClientStream;
	private String username;
	private boolean isRunning;

	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
		isRunning = true;
	}

	

	public void run() {
		try (BufferedReader inputClientStream = new BufferedReader(
				new InputStreamReader(clientSocket.getInputStream()))) {
			
			outputClientStream = new PrintWriter(clientSocket.getOutputStream(), true);
			outputClientStream.println("Enter your username: ");
			username = inputClientStream.readLine();
			// client disconnected without entering a username
			if (username == null) {
				username = "Unknown";
			}
			
			while (isRunning && username.equals("")) {
				System.out.println("Client entered empty username.");
				outputClientStream.println("Enter your username: ");
				username = inputClientStream.readLine();
				if (username == null) {
					username = "Unknown";
				}
			}
			 
			
			System.out.println(username + " connected!");
			ChatServer.broadcast(this, username + " connected!");

			String clientMessage;
			while (isRunning && (clientMessage = inputClientStream.readLine()) != null) {
				System.out.println(username + ": " + clientMessage);
				ChatServer.broadcast(this, username + ": " + clientMessage);
			}
		} 
		catch (IOException e) {
			// client disconnected without entering a username
			if (username == null) {
				username = "Unknown";
			}
			System.err.println(username + " disconnected abruptly!");
		} 
		finally {
			ChatServer.removeClient(this);
			// Stopped server set isRunning false
			if (isRunning) {
				ChatServer.broadcast(null, username + " left the chat.");
				System.out.println(username + " left the chat.");
			}
			closeClientSocket();
		}
	}


	public void closeClientSocket() {
		isRunning = false;
		try {
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Error closing socket: " + e.getMessage());
		}
	}


	public void sendMessage(String message) {
		outputClientStream.println(message);
	}
}
