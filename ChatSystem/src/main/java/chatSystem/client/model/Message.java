package chatSystem.client.model;

public class Message
{
  private User user;
  private String text;

  private boolean announcement;

  public Message(User user, String text)
  {
    this.user = user;
    this.text = text;
    announcement = false;
  }

  public Message(String text)
  {
    this.user = null;
    this.text = text;
    announcement = true;
  }

  public User getUser()
  {
    return user;
  }

  public String getText()
  {
    return text;
  }

  @Override public String toString()
  {
    if (announcement)
    {
      return text;
    }
    else
    {
      return user.toString(user.getName()) + text;
    }
  }
}
