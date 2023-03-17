package chatSystem.client.model;

import java.util.ArrayList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ModelManager
{
  private UserList users;
  private MessageList messages;
  private PropertyChangeSupport support;

  private User user;

  public ModelManager()
  {
    this.users = UserList.getInstance();
    this.messages = MessageList.getInstance();
    this.support = new PropertyChangeSupport(this);
  }

  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(propertyName, listener);
  }

  public void removePropertChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(propertyName, listener);
  }

  public boolean addUser(User user)
  {
    boolean ok = users.addUser(user);

    if (ok)
    {
      this.user = user;
      support.firePropertyChange("users", null, users);
      return true;
    }
    else
    {
      return false;
    }
  }

  public boolean addMesage(Message message)
  {
    boolean ok = messages.addMesage(message);
    if (ok)
    {
      support.firePropertyChange("messages", null, messages);
      return true;
    }
    else
      return false;
  }

  public void setUserList(ArrayList<User> userList)
  {
    users.setUserList(userList);
    support.firePropertyChange("users", null, users);
  }

  public void setMessageList(ArrayList<Message> messageList)
  {
    messages.setMessageList(messageList);
    support.firePropertyChange("messages", null, users);
  }

  public void updateMessages()
  {
    messages.updateMessages();
  }

  public void removeUser(){
    users.removeUser(user);
  }

  public User getUser()
  {
    return user;
  }

  public ArrayList<Message> getMessages()
  {
    return messages.getMessages();
  }

  public ArrayList<User> getUsers()
  {
    return users.getUsers();
  }
}
