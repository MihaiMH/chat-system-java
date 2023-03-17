package chatSystem.client.model;

import chatSystem.client.communication.ChatClientImplementation;

import java.io.IOException;
import java.util.ArrayList;

public class UserList
{
  private ArrayList<User> userList;
  private ChatClientImplementation chatClientImplementation;

  private static UserList instance;

  private UserList(ChatClientImplementation chatClientImplementation,
      ArrayList<User> userList)
  {
    this.userList = userList;
    this.chatClientImplementation = chatClientImplementation;
  }

  public static UserList getInstance(
      ChatClientImplementation chatClientImplementation,
      ArrayList<User> userList)
  {
    if (instance == null)
    {
      instance = new UserList(chatClientImplementation, userList);
    }
    return instance;
  }

  public static UserList getInstance()
  {
    return instance;
  }

  public boolean addUser(User user)
  {
    try
    {
      return chatClientImplementation.login(user);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

  }

  public void removeUser(User user)
  {
    try
    {
      chatClientImplementation.removeUser(user);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public void setUserList(ArrayList<User> userList)
  {
    this.userList = userList;
  }

  public ArrayList<User> getUsers()
  {
    return userList;
  }

  public String toString()
  {
    String aux = "";

    for (int i = 0; i < userList.size(); i++)
    {
      aux += userList.get(i) + "\n";
    }

    return aux;
  }

}
