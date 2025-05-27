# Java Chat Application (Client-Server)



https://github.com/user-attachments/assets/9cafc50f-ae0c-42f6-885e-5a7d5a4ec200



A simple Android chat application demonstrating client-server architecture using Java multithreading. The project consists of two parts: a **server** (handles connections and messaging) and a **client** (user interface for chatting).

## Key Features

### Server
- **Active application** (not a background service)
- **Real-time logging** of all activities
- **Read-only mode** (cannot send messages to clients)
- **Handles multiple clients simultaneously** using multithreading
- **Graceful shutdown** (notifies all clients before stopping)
- **Connection events** broadcasts when clients join/leave
- **Handles Ctrl-c termination**
- **Handles empty username input**


### Client
- **Real-time messaging**
- **User presence notifications** (join/leave alerts)
- **Duplicate message handling** (consecutive identical messages allowed)
- **Server disconnect handling** (receives termination notice)
- **Handles `exit` command**
- **Handles incorrect port**
- **Handles interrupted message input**


