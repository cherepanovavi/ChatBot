package Source.Tests;

import Source.LogicFiles.UserState;

public class FakeSender implements Source.LogicFiles.ISender{
    private boolean sendIsCalled = false;

    @Override
    public void sendMessage(UserState userState, String message) {
        sendIsCalled = true;
    }

    public boolean getSendIsCalled(){
        return sendIsCalled;
    }
}
