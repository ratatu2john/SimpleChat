
all: client_handler server client



client_handler:
	javac ./SimpleChat/ClientHandler.java

server:
	javac ./SimpleChat/ChatServer.java

client:
	javac ./SimpleChat/ChatClient.java

clean:
	$(RM) ./SimpleChat/ClientHandler.class
	$(RM) ./SimpleChat/ChatServer.class
	$(RM) ./SimpleChat/ChatClient.class
