package Source.LogicFiles;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class InitiatingManager {
    private ArrayList<UserState> userStates;
    private ConcurrentHashMap<UserState, ChatInitiator> initiators;
    private ISender sender;
    private long delayInMiliseconds;

    public InitiatingManager(ISender sender, long delayInMiliseconds) {
        this.sender = sender;
        this.delayInMiliseconds = delayInMiliseconds;
        this.userStates = new ArrayList<UserState>();
        this.initiators = new ConcurrentHashMap<>();
    }

    public void addUserState(UserState userState) {
        if (!userStates.contains(userState)) {
            userStates.add(userState);
            initiators.put(userState, new ChatInitiator(userState, sender, delayInMiliseconds));
        }
    }

    public void deleteUserState(UserState userState) {
        if (userStates.contains(userState)) {
            initiators.get(userState).cancel();
            initiators.remove(userState);
            userStates.remove(userState);
        }
    }
}
