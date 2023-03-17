package chatSystem.server;

import chatSystem.client.communication.Data;
import chatSystem.client.communication.Result;
import chatSystem.client.model.Message;
import chatSystem.client.model.MessageList;
import chatSystem.client.model.User;
import chatSystem.client.model.UserList;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ChatCommunicator implements Runnable
{
  private final Socket socket;
  private final UDPBroadcaster broadcaster;
  private final Gson gson;

  private DefaultLog logs;

  private ArrayList<Message> messageList;
  private ArrayList<User> userList;

  public ChatCommunicator(Socket socket, UDPBroadcaster broadcaster,
      ArrayList<Message> messageList, ArrayList<User> userList)
  {
    this.socket = socket;
    this.broadcaster = broadcaster;
    this.gson = new Gson();
    this.messageList = messageList;
    this.userList = userList;
    logs = DefaultLog.getInstance();
  }

  public void run()
  {
    try
    {
      communicate();
    }
    catch (Exception e)
    {
      System.out.println("Connection reseted");
      e.printStackTrace();
      try
      {
        logs.log("Connection reseted");
      }
      catch (IOException ex)
      {
        throw new RuntimeException(ex);
      }
    }
  }

  private void communicate() throws IOException
  {
    try
    {
      InputStream inputStream = socket.getInputStream();
      OutputStream outputStream = socket.getOutputStream();

      BufferedReader input = new BufferedReader(
          new InputStreamReader(inputStream));
      PrintWriter output = new PrintWriter(outputStream);

      loop:
      while (true)
      {
        String jsonRequest = input.readLine();
        Data data = gson.fromJson(jsonRequest, Data.class);
        String ip = socket.getRemoteSocketAddress().toString();
        switch (data.getRequest())
        {
          case "login":
          {

            System.out.println(
                "User: " + data.getUser().getName() + " tries to log in (IP: "
                    + ip + " )");
            logs.log(
                "User: " + data.getUser().getName() + " tries to log in (IP: "
                    + ip + " )");

            boolean r = false;
            boolean aux = true;

            for (int i = 0; i < userList.size(); i++)
            {
              if (data.getUser().getName().equals(userList.get(i).getName()))
              {
                aux = false;
                break;
              }
            }

            if (aux && !(data.getUser().getName().trim()).equals(""))
            {
              r = true;
              userList.add(data.getUser());

              Result result = new Result("userlist", userList, 0);
              String data_json = gson.toJson(result);
              broadcaster.broadcast(data_json);

              Message message = new Message(
                  data.getUser().getName() + " has entered the chat");
              messageList.add(message);
              Result result2 = new Result("messagelist", messageList);
              String data_json2 = gson.toJson(result2);
              broadcaster.broadcast(data_json2);

              System.out.println("User: " + data.getUser().getName()
                  + " has logged in successfully (IP: " + ip + " )");
              logs.log("User: " + data.getUser().getName()
                  + " has logged in successfully (IP: " + ip + " )");
            }
            else
            {
              System.out.println("User: " + data.getUser().getName()
                  + " has failed to log in. Reason: Empty or taken username (IP: "
                  + ip + " )");
              logs.log("User: " + data.getUser().getName()
                  + " has failed to log in. Reason: Empty or taken username (IP: "
                  + ip + " )");
            }
            String json = gson.toJson(r);
            output.println(json);
            output.flush();

            break;
          }
          case "message":
          {
            boolean r = false;
            Message message = data.getMessage();
            System.out.println("User: " + data.getMessage().getUser().getName()
                + " tries to send a message (IP: " + ip + " )");
            logs.log("User: " + data.getMessage().getUser().getName()
                + " tries to send a message (IP: " + ip + " )");
            if (!(message.getText().trim()).equals(""))
            {
              r = true;
              messageList.add(message);
              Result result = new Result("messagelist", messageList);
              String data_json = gson.toJson(result);
              broadcaster.broadcast(data_json);
              System.out.println("User: " + data.getMessage().getUser().getName()
                  + " has sent a message successfully (IP: " + ip
                  + " ) \n Content: " + data.getMessage().getText());
              logs.log("User: " + data.getMessage().getUser().getName()
                  + " has sent a message successfully (IP: " + ip
                  + " ) \n Content: " + data.getMessage().getText());
            }
            else
            {
              System.out.println("User: " + data.getMessage().getUser().getName()
                  + " has failed to send the message. Reason: Empty message (IP: "
                  + ip + " )");
              logs.log("User: " + data.getMessage().getUser().getName()
                  + " has failed to send the message. Reason: Empty message (IP: "
                  + ip + " )");
            }
            String json = gson.toJson(r);
            output.println(json);
            output.flush();
            break;
          }
          case "getChat":
          {
            System.out.println("A global broadcast for messages has been executed. (Requested by IP: " + ip
                + " )");
            logs.log("A global broadcast for messages has been executed. (Requested by IP: " + ip
                + " )");
            Result result = new Result("messagelist", messageList);
            String data_json = gson.toJson(result);
            broadcaster.broadcast(data_json);

            break;
          }
          case "remove":
          {
            for (int i = 0; i < userList.size(); i++)
            {
              if (data.getUser().getName().equals(userList.get(i).getName()))
              {
                userList.remove(userList.get(i));
                Result result = new Result("userlist", userList, 0);
                String data_json = gson.toJson(result);
                broadcaster.broadcast(data_json);

                Message message = new Message(
                    data.getUser().getName() + " has left the chat");
                messageList.add(message);
                Result result2 = new Result("messagelist", messageList);
                String data_json2 = gson.toJson(result2);
                broadcaster.broadcast(data_json2);

                System.out.println("User: " + data.getUser().getName()
                    + " has disconnected (IP: " + ip + " )");
                logs.log("User: " + data.getUser().getName()
                    + " has disconnected  (IP: " + ip + " )");

                break;
              }
            }
            break;
          }
          case "connect":
          {
            System.out.println(ip + " has connected succesfully");
            logs.log(ip + " has connected succesfully");
            output.flush();
            break;
          }
          case "exit":
            System.out.println("Exiting");
            break loop;
        }
      }
    }
    finally
    {
      synchronized (broadcaster)
      {
        socket.close();
      }

    }
  }
}
