package chatSystem.client.communication;

import chatSystem.client.model.Message;
import chatSystem.client.model.User;

public class Data
{
  private final String request;

  private final User user;

  private final Message message;

  public Data(String req)
  {
    request = req;
    this.user = null;
    this.message = null;
  }

  public Data(String req, User user)
  {
    request = req;
    this.user = user;
    message = null;
  }

  public Data(String req, Message message)
  {
    request = req;
    this.message = message;
    user = null;
  }

  public User getUser()
  {
    return user;
  }

  public Message getMessage()
  {
    return message;
  }

  public String getRequest()
  {
    return request;
  }
}
