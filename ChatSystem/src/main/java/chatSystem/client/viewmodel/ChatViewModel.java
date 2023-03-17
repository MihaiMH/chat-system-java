package chatSystem.client.viewmodel;

import chatSystem.client.model.Message;
import chatSystem.client.model.ModelManager;
import chatSystem.client.model.User;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ChatViewModel implements PropertyChangeListener
{
  private ModelManager modelManager;

  private SimpleStringProperty name;

  private SimpleListProperty<User> users;

  private SimpleListProperty<Message> messages;

  private PropertyChangeSupport support;

  public ChatViewModel(ModelManager modelManager)
  {
    this.modelManager = modelManager;
    this.name = new SimpleStringProperty("");
    this.users = new SimpleListProperty<>(FXCollections.observableArrayList());
    this.messages = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    this.modelManager.addPropertyChangeListener("users", this);
    this.modelManager.addPropertyChangeListener("messages", this);
    this.support = new PropertyChangeSupport(this);
  }

  public void bindName(StringProperty property)
  {
    property.bindBidirectional(name);
  }

  public void bindUsers(ObjectProperty<ObservableList<User>> property)
  {
    property.bindBidirectional(users);
  }

  public void bindMessages(ObjectProperty<ObservableList<Message>> proerty)
  {
    proerty.bindBidirectional(messages);
  }

  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(propertyName, listener);
  }

  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(propertyName, listener);
  }

  public boolean addMesage(User user, String text)
  {
    return modelManager.addMesage(new Message(user, text));
  }

  public User getUser()
  {
    return modelManager.getUser();
  }

  public void update()
  {
    users.setAll(modelManager.getUsers());
    messages.setAll(modelManager.getMessages());
    support.firePropertyChange("messages", null, messages);
    support.firePropertyChange("users", null, users);
  }

  public void removeUser(){
    modelManager.removeUser();
  }

  public void updateMessages()
  {
    modelManager.updateMessages();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(() -> update());
  }

}
