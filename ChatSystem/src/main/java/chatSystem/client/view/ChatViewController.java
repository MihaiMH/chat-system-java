package chatSystem.client.view;

import chatSystem.client.Utils.SoundUtils;
import chatSystem.client.model.User;
import chatSystem.client.model.UserList;
import chatSystem.client.viewmodel.ChatViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ChatViewController implements PropertyChangeListener
{
  @FXML ListView userList;
  @FXML ListView chatList;
  @FXML Button exitButton;
  @FXML TextArea messageInput;
  @FXML Button messageSend;
  @FXML Button soundButton;

  private ViewHandler viewHandler;
  private ChatViewModel chatViewModel;
  private Region root;

  private PropertyChangeListener users;
  private PropertyChangeListener messages;

  private PropertyChangeListener close;
  private PropertyChangeListener sound;

  private boolean play;
  private boolean definePlay;

  @FXML public void onExit()
  {
    viewHandler.closeView();
  }

  @FXML public void onSend()
  {
    boolean ok = chatViewModel.addMesage(chatViewModel.getUser(),
        messageInput.getText());
    if (ok == true)
    {
      messageInput.setText("");
      chatList.scrollTo(chatList.getItems().size() - 1);
    }
    else
    {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Empty message");
      alert.setHeaderText("Empty message");
      alert.setContentText("Your message can't be empty");

      alert.showAndWait();
    }
  }

  @FXML public void onEnterSend(KeyEvent ae)
  {
    if (ae.getCode() == KeyCode.ENTER)
    {
      boolean ok = chatViewModel.addMesage(chatViewModel.getUser(),
          messageInput.getText());
      if (ok == true)
      {
        messageInput.setText("");
        chatList.scrollTo(chatList.getItems().size() - 1);
      }
      else
      {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Empty message");
        alert.setHeaderText("Empty message");
        alert.setContentText("Your message can't be empty");

        alert.showAndWait();
      }
    }
  }

  @FXML public void controlSound()
  {
    if (definePlay)
    {
      definePlay = false;
      soundButton.setText("ENABLE SOUND");
    }
    else
    {
      definePlay = true;
      soundButton.setText("DISABLE SOUND");
    }
  }

  public void init(ViewHandler viewHandler, ChatViewModel chatViewModels,
      Region root)
  {

    this.viewHandler = viewHandler;
    this.chatViewModel = chatViewModels;
    this.root = root;
    this.chatViewModel.updateMessages();
    this.chatViewModel.bindMessages(chatList.itemsProperty());
    this.chatViewModel.bindUsers(userList.itemsProperty());
    this.definePlay = true;

    close = new PropertyChangeListener()
    {
      @Override public void propertyChange(PropertyChangeEvent evt)
      {
        chatViewModel.removeUser();
        System.exit(0);
      }
    };

    users = new PropertyChangeListener()
    {
      @Override public void propertyChange(PropertyChangeEvent evt)
      {

      }
    };

    messages = new PropertyChangeListener()
    {
      @Override public void propertyChange(PropertyChangeEvent evt)
      {
        chatList.scrollTo(chatList.getItems().size() - 1);
        try
        {
          if (play && definePlay)
            SoundUtils.tone(370, 300, 0.5);
        }
        catch (LineUnavailableException e)
        {
          throw new RuntimeException(e);
        }
      }
    };

    sound = new PropertyChangeListener()
    {
      @Override public void propertyChange(PropertyChangeEvent evt)
      {
        if ((int) evt.getNewValue() == 0)
        {
          play = false;
        }
        else
        {
          play = true;
        }
      }
    };

    this.viewHandler.addPropertyChangeListener("close", close);
    this.chatViewModel.addPropertyChangeListener("messages", messages);
    this.chatViewModel.addPropertyChangeListener("users", users);
    this.viewHandler.addPropertyChangeListener("sound", sound);

  }

  public void update()
  {
    chatViewModel.update();
  }

  public Region getRoot()
  {
    return root;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    chatViewModel = (ChatViewModel) evt.getSource();
    chatList.scrollTo(chatList.getItems().size() - 1);

  }

}
