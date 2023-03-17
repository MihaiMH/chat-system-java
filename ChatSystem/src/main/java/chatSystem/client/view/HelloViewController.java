package chatSystem.client.view;

import chatSystem.client.model.User;
import chatSystem.client.viewmodel.HelloViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HelloViewController implements PropertyChangeListener
{
  @FXML public TextField nameInput;
  @FXML public Button confirmButton;

  private ViewHandler viewHandler;
  private HelloViewModel helloViewModel;
  private Region root;

  private PropertyChangeListener close;

  @FXML public void onEnterName()
  {
    boolean ok = helloViewModel.addUser(new User(nameInput.getText()));
    if (ok)
    {
      viewHandler.openView(ViewHandler.CHAT);
      helloViewModel.reset();
    }
    else
    {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Empty name or name already taken");
      alert.setHeaderText("Empty name or name already taken");
      alert.showAndWait();
    }
  }

  @FXML public void onEnterSend(KeyEvent ae)
  {
    if (ae.getCode() == KeyCode.ENTER)
    {
      boolean ok = helloViewModel.addUser(new User(nameInput.getText()));
      if (ok)
      {
        viewHandler.openView(ViewHandler.CHAT);
        helloViewModel.reset();
      }
      else
      {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Empty name or name already taken");
        alert.setHeaderText("Empty name or name already taken");
        alert.showAndWait();
      }
    }
  }

  public void init(ViewHandler viewHandler, HelloViewModel helloViewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.helloViewModel = helloViewModel;
    this.root = root;

    this.helloViewModel.bindName(nameInput.textProperty());

    close = new PropertyChangeListener()
    {
      @Override public void propertyChange(PropertyChangeEvent evt)
      {
        System.exit(0);
      }
    };

    this.viewHandler.addPropertyChangeListener("close2", close);

  }

  public Region getRoot()
  {
    return root;
  }

  public void update()
  {
    helloViewModel.update();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    helloViewModel = (HelloViewModel) evt.getSource();
  }

}
