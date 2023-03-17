package chatSystem.client.viewmodel;

import chatSystem.client.model.ModelManager;
import chatSystem.client.model.User;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class HelloViewModel implements PropertyChangeListener
{
  private ModelManager modelManager;

  private SimpleStringProperty name;

  private SimpleListProperty<User> users;

  private PropertyChangeSupport support;

  public HelloViewModel(ModelManager modelManager)
  {
    this.modelManager = modelManager;
    this.name = new SimpleStringProperty("");
    this.users = new SimpleListProperty<>(FXCollections.observableArrayList());
    this.modelManager.addPropertyChangeListener("users", this);
    this.modelManager.addPropertyChangeListener("messages", this);
    this.support = new PropertyChangeSupport(this);
  }

  public void bindName(StringProperty property)
  {
    property.bindBidirectional(name);
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

  public boolean addUser(User user)
  {
    boolean ok = modelManager.addUser(user);

    if (ok)
    {
      support.firePropertyChange("users", null, users);
      return true;
    }
    else
    {
      return false;
    }
  }

  public void update()
  {
    users.setAll(modelManager.getUsers());
  }

  public void reset()
  {
    name.set("");
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(() -> update());
  }

}
