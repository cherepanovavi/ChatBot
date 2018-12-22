package Tests;

import LogicFiles.UserState;

public class FakeSender implements LogicFiles.ISender{
    private boolean sendIsCalled = false;

    @Override
    public void sendMessage(UserState userState, String message) {
        sendIsCalled = true;
    }

    public boolean getSendIsCalled(){
        return sendIsCalled;
    }
}
