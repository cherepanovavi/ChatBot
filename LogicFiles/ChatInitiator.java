package Source.LogicFiles;

import java.util.Timer;

public class ChatInitiator {
    private UserState userState;
    private ISender sender;
    private Timer timer;

    public ChatInitiator(UserState userState, ISender sender, long delayInMiliseconds) {
        this.userState = userState;
        this.sender = sender;
        timer = new Timer();
        timer.schedule(new Task(this.sender, this.userState), delayInMiliseconds, delayInMiliseconds);
    }

    public void cancel() {
        timer.cancel();
    }

}


