package chatSystem.server;

import chatSystem.client.model.Message;
import chatSystem.client.model.MessageList;
import chatSystem.client.model.User;
import chatSystem.client.model.UserList;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer
{
  public static void main(String[] args) throws IOException
  {
    ArrayList<User> users= new ArrayList<>();
    ArrayList<Message> messages= new ArrayList<>();
    ServerSocket serverSocket = new ServerSocket(8080);
    UDPBroadcaster broadcaster = new UDPBroadcaster("230.0.0.0", 8888);
    while (true)
    {
      System.out.println("Server is ready for input port 8080");
      Socket socket = serverSocket.accept();
      ChatCommunicator communicator = new ChatCommunicator(socket, broadcaster,
          messages, users);
      Thread communicatorThread = new Thread(communicator);
      communicatorThread.start();
    }
  }
}
