package LogicFiles;

import java.util.TimerTask;

public class Task extends TimerTask {
    private ISender sender;
    private UserState userState;

    public Task(ISender sender, UserState userState) {
        super();
        this.sender = sender;
        this.userState = userState;
    }

    @Override
    public void run() {
        if (userState.getQuestionNumber() == -1)
            sender.sendMessage(userState, String.format("Привет, %s! Давно не играли, сыграем?", userState.getName()));
    }
}
