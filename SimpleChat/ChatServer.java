package SimpleChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import SimpleChat.ClientHandler;


public class ChatServer {
	private final int PORT = 7000;
	private ServerSocket serverSocket;
	private boolean isRunning;
	private static List<ClientHandler> clients;


	public ChatServer() {
		isRunning = false;
		clients = new CopyOnWriteArrayList<>();
	}


	public static void main(String[] args) {
		new ChatServer().startServer();
	}


	private void startServer() {
		try {
			serverSocket = new ServerSocket(PORT, 0); // backlog=0
			isRunning = true;
			System.out.println("Server started on port " + PORT);
			
			// Handle Ctrl-c
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				ChatServer.broadcast(null, "Server stopped.");
				closeServerSocket();
				for (ClientHandler client : clients) {
					client.closeClientSocket();
				}
			}));
			
			
			while (isRunning) {
				try {
					Socket clientSocket = serverSocket.accept();
					System.out.println("New connection: " 
						+clientSocket.getLocalAddress()+":"+clientSocket.getPort());

					ClientHandler clientHandler = new ClientHandler(clientSocket);
					clients.add(clientHandler);
					new Thread(clientHandler).start();
				} catch (IOException e) {
					System.err.println("Error accepting client: " + e.getMessage());
				}
			}
		} 
		catch (IOException e) {
			System.err.println("Server error: " + e.getMessage());
		} 
		finally {
			System.out.println("Server stopped.");
		}
	}

	
	public void closeServerSocket() {
		isRunning = false;
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			System.err.println("Error closing socket: " + e.getMessage());
		}
	}


	public static void broadcast(ClientHandler sender, String message) {
		for (ClientHandler client : clients) {
			if (sender != client) {
				client.sendMessage(message);
			}
		}
	}



	public static void removeClient(ClientHandler client) {
		clients.remove(client);
	}
}
