package chatSystem.client;

import chatSystem.client.communication.ChatClient;
import chatSystem.client.communication.ChatClientImplementation;
import chatSystem.client.model.*;
import chatSystem.client.view.ViewHandler;
import chatSystem.client.viewmodel.ViewModelFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

public class StartClient extends Application
{
  @Override public void start(Stage primaryStage) throws IOException
  {
    ArrayList<Message> messages = new ArrayList<Message>();
    ArrayList<User> users = new ArrayList<User>();

    ChatClient client = new ChatClientImplementation("10.154.204.81", 8080,
        "230.0.0.0", 8888);
    client.connect();
    MessageList messageList = MessageList.getInstance(
        (ChatClientImplementation) client, messages);
    UserList userList = UserList.getInstance((ChatClientImplementation) client,
        users);
    ModelManager model = new ModelManager();
    client.addPropertyChangeListener(new PropertyChangeListener()
    {
      @Override public void propertyChange(PropertyChangeEvent evt)
      {
        if (evt.getPropertyName().equals("user"))
        {
          model.setUserList((ArrayList<User>) evt.getNewValue());
        }
        else
        {
          model.setMessageList((ArrayList<Message>) evt.getNewValue());
        }
      }
    });

    ViewModelFactory viewModelFactory = new ViewModelFactory(model);
    ViewHandler viewHandler = new ViewHandler(viewModelFactory);
    viewHandler.start(primaryStage);

  }

  public static void main(String[] args)
  {
    launch();

  }

}
