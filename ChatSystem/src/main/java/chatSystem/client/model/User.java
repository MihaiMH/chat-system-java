package chatSystem.client.model;

public class User
{
  private String name;

  public User(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public String toString()
  {
    return name;
  }

  public String toString(String name)
  {
    return name + ": \n";
  }
}
