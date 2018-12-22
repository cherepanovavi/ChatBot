package Tests;

import LogicFiles.InitiatingManager;
import LogicFiles.UserState;
import org.junit.jupiter.api.Test;

import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InitiatingManagerTest {
    @Test
    public void testAddUserState() throws InterruptedException {
        FakeSender sender = new FakeSender();
        UserState userState = new UserState("username");
        InitiatingManager manager = new InitiatingManager(sender, 100);
        manager.addUserState(userState);
        Thread.sleep(150);
        assertEquals(true, sender.getSendIsCalled());

    }

    @Test
    public void testAddAndDeleteUserState() throws InterruptedException {
        FakeSender sender = new FakeSender();
        UserState userState = new UserState("username");
        InitiatingManager manager = new InitiatingManager(sender, 100);
        manager.addUserState(userState);
        manager.deleteUserState(userState);
        Thread.sleep(150);
        assertEquals(false, sender.getSendIsCalled());
    }
}
