package chatSystem.client.communication;

import chatSystem.client.model.*;
import com.google.gson.Gson;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ChatClientImplementation implements ChatClient
{

  private final Socket socket;
  private final PrintWriter output;
  private final BufferedReader input;
  private final Gson gson;
  private final PropertyChangeSupport support;
  private final MessageListener listener;

  private User user;

  public ChatClientImplementation(String host, int port, String groupAddress,
      int groupPort) throws IOException
  {
    this.socket = new Socket(host, port);
    this.output = new PrintWriter(socket.getOutputStream());
    this.input = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));
    this.gson = new Gson();
    this.support = new PropertyChangeSupport(this);

    listener = new MessageListener(this, groupAddress, groupPort);
    Thread thread = new Thread(listener);
    thread.start();
  }

  @Override public void connect() throws IOException
  {
    Data data = new Data("connect");
    String data_json = gson.toJson(data);
    output.println(data_json);
    output.flush();
  }

  @Override public boolean login(User user) throws IOException
  {
    Data data = new Data("login", user);
    String data_json = gson.toJson(data);
    output.println(data_json);
    output.flush();
    String resultJson = input.readLine();
    boolean result = gson.fromJson(resultJson, boolean.class);
    if (result)
      this.user = user;
    return result;
  }

  @Override public boolean sendMessage(Message message) throws IOException
  {
    Data data = new Data("message", message);
    String data_json = gson.toJson(data);
    output.println(data_json);
    output.flush();
    String resultJson = input.readLine();
    boolean result = gson.fromJson(resultJson, boolean.class);
    return result;
  }

  @Override public void getMessages() throws IOException
  {
    Data data = new Data("getChat");
    String data_json = gson.toJson(data);
    output.println(data_json);
    output.flush();
  }

  @Override public void removeUser(User user) throws IOException
  {
    Data data = new Data("remove", user);
    String data_json = gson.toJson(data);
    output.println(data_json);
    output.flush();
  }

  public void close() throws IOException
  {
    listener.close();
    Data data;
    if (user == null)
    {
      data = new Data("exit");
    }
    else
    {
      data = new Data("exit", user);
    }
    String data_json = gson.toJson(data);
    output.println(data_json);
    output.flush();
    socket.close();
  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  @Override public void removePropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(listener);
  }

  public void receiveBroadcast(String message)
  {
    Result result = gson.fromJson(message, Result.class);
    if (result.getResponse().equals("userlist"))
    {
      support.firePropertyChange("user", null, result.getUsers());
    }
    else
    {
      support.firePropertyChange("message", null, result.getMessages());
    }

  }

}
