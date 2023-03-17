package chatSystem.client.communication;

import chatSystem.client.model.Message;
import chatSystem.client.model.MessageList;
import chatSystem.client.model.User;
import chatSystem.client.model.UserList;

import java.util.ArrayList;

public class Result
{
  private final String response;

  private final ArrayList<User> users;

  private final ArrayList<Message> messages;

  public Result(String res, ArrayList<User> users, int x)
  {
    response = res;
    this.users = users;
    messages = null;
  }

  public Result(String res, ArrayList<Message> messages)
  {
    response = res;
    this.messages = messages;
    users = null;
  }

  public ArrayList<User> getUsers()
  {
    return users;
  }

  public ArrayList<Message> getMessages()
  {
    return messages;
  }

  public String getResponse()
  {
    return response;
  }
}
