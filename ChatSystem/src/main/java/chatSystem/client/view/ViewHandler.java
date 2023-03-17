package chatSystem.client.view;

import chatSystem.client.model.ModelManager;
import chatSystem.client.viewmodel.ViewModelFactory;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ViewHandler
{
  public static final String CHAT = "chat";
  public static final String HELLO = "hello";
  private Scene currentScene;
  private Stage primaryStage;
  private final ViewFactory viewFactory;

  private PropertyChangeSupport support;

  public ViewHandler(ViewModelFactory viewModelFactory)
  {
    this.viewFactory = new ViewFactory(this, viewModelFactory);
    this.currentScene = new Scene(new Region());

    this.support = new PropertyChangeSupport(this);

  }

  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(propertyName, listener);
  }

  public void start(Stage primaryStage)
  {
    this.primaryStage = primaryStage;
    primaryStage.setResizable(false);
    this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
    {
      @Override public void handle(WindowEvent event)
      {
        support.firePropertyChange("close", null, 1);
        support.firePropertyChange("close2", null, 1);
      }
    });

    primaryStage.focusedProperty().addListener(this::focusChanged);
    openView(HELLO);
  }

  private void focusChanged(ObservableValue<? extends Boolean> property,
      Boolean wasFocused, Boolean isFocused)
  {
    if (isFocused)
    {
      support.firePropertyChange("sound", null, 0);
    } else {
      support.firePropertyChange("sound", null, 1);
    }
  }

  public void openView(String id)
  {
    Region root = switch (id)
        {
          case CHAT -> viewFactory.loadChatViewController();
          case HELLO -> viewFactory.loadHelloViewController();
          default -> throw new IllegalArgumentException("Unknown view: " + id);
        };
    currentScene.setRoot(root);

    if (root.getUserData() == null)
    {
      primaryStage.setTitle("");
    }
    else
    {
      primaryStage.setTitle(root.getUserData().toString());
    }

    primaryStage.setScene(currentScene);
    primaryStage.sizeToScene();

    primaryStage.show();
  }

  public void closeView()
  {
    support.firePropertyChange("close", null, 1);
    support.firePropertyChange("close2", null, 1);
    primaryStage.close();
  }
}
