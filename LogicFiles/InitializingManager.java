package Source.LogicFiles;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class InitializingManager {
    private ArrayList<UserState> userStates;
    private ConcurrentHashMap<UserState, ChatInitializer> initilizers;
    private ISender sender;
    private long delayInMiliseconds;

    public InitializingManager(ISender sender, long delayInMiliseconds) {
        this.sender = sender;
        this.delayInMiliseconds = delayInMiliseconds;
        this.userStates = new ArrayList<UserState>();
        this.initilizers = new ConcurrentHashMap<>();
    }

    public void addUserState(UserState userState) {
        if (!userStates.contains(userState)) {
            userStates.add(userState);
            initilizers.put(userState, new ChatInitializer(userState, sender, delayInMiliseconds));
        }
    }

    public void deleteUserState(UserState userState) {
        if (userStates.contains(userState)) {
            initilizers.get(userState).cancel();
            initilizers.remove(userState);
            userStates.remove(userState);
        }
    }
}
