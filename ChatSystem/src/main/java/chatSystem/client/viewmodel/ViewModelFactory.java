package chatSystem.client.viewmodel;

import chatSystem.client.model.ModelManager;

public class ViewModelFactory
{
  private ChatViewModel chatViewModel;
  private HelloViewModel helloViewModel;

  public ViewModelFactory(ModelManager modelManager)
  {
    this.chatViewModel = new ChatViewModel(modelManager);
    this.helloViewModel = new HelloViewModel(modelManager);
  }

  public ChatViewModel getChatViewModel()
  {
    return chatViewModel;
  }

  public HelloViewModel getHelloViewModel()
  {
    return helloViewModel;
  }
}
