package chatSystem.client.communication;

import java.io.IOException;
import java.net.*;
import java.nio.channels.AsynchronousCloseException;

public class MessageListener implements Runnable
{
  private final MulticastSocket multicastSocket;
  private final InetSocketAddress socketAddress;
  private final NetworkInterface networkInterface;

  private final ChatClientImplementation client;

  public MessageListener(ChatClientImplementation client, String groupAddress, int port)
      throws IOException
  {
    this.client = client;
    multicastSocket = new MulticastSocket(port);
    InetAddress group = InetAddress.getByName(groupAddress);
    socketAddress = new InetSocketAddress(group, port);
    networkInterface = NetworkInterface.getByInetAddress(group);
  }

  public void run(){
    try {
      listen();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void listen() throws IOException
  {
    multicastSocket.joinGroup(socketAddress, networkInterface);
    try
    {
      byte[] content = new byte[32768];
      while (true)
      {
        DatagramPacket packet = new DatagramPacket(content, content.length);
        multicastSocket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
        client.receiveBroadcast(message);
      }
    }
    catch (SocketException e)
    {
      if (!(e.getCause() instanceof AsynchronousCloseException))
        throw e;
    }
  }

  public void close() throws  IOException{
    multicastSocket.leaveGroup(socketAddress, networkInterface);
    multicastSocket.close();
  }
}
