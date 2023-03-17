package chatSystem.client.communication;

import chatSystem.client.model.Message;
import chatSystem.client.model.User;

import java.beans.PropertyChangeListener;
import java.io.Closeable;
import java.io.IOException;

public interface ChatClient extends Closeable
{
  void connect() throws IOException;
  boolean login(User user) throws IOException;

  boolean sendMessage(Message message) throws IOException;

  void getMessages() throws IOException;

  void removeUser(User user) throws IOException;

  void addPropertyChangeListener(PropertyChangeListener listener);

  void removePropertyChangeListener(PropertyChangeListener listener);
}
