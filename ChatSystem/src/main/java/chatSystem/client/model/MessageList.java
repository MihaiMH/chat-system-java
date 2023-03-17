package chatSystem.client.model;

import chatSystem.client.communication.ChatClientImplementation;

import java.io.IOException;
import java.util.ArrayList;

public class MessageList
{
  private ArrayList<Message> messageList;
  private ChatClientImplementation chatClientImplementation;
  private static MessageList instance;

  public MessageList(ChatClientImplementation chatClientImplementation,
      ArrayList<Message> messageList)
  {
    this.messageList = messageList;
    this.chatClientImplementation = chatClientImplementation;
  }

  public static MessageList getInstance(
      ChatClientImplementation chatClientImplementation,
      ArrayList<Message> messageList)
  {
    if (instance == null)
    {
      instance = new MessageList(chatClientImplementation, messageList);
    }
    return instance;
  }

  public static MessageList getInstance()
  {
    return instance;
  }

  public boolean addMesage(Message message)
  {
    try
    {
      return chatClientImplementation.sendMessage(message);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public void setMessageList(ArrayList<Message> messageList)
  {
    this.messageList = messageList;
  }

  public void updateMessages()
  {
    try
    {
      chatClientImplementation.getMessages();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Message> getMessages()
  {

    return messageList;
  }

  public String toString()
  {
    String aux = "";

    for (int i = 0; i < messageList.size(); i++)
    {
      aux += messageList.get(i) + "\n";
    }

    return aux;
  }
}
