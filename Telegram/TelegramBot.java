package Source.Telegram;

import Source.LogicFiles.ChatBot;
import Source.LogicFiles.UserState;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class TelegramBot extends TelegramLongPollingBot{
    private String token;
    private String botName;
    private ChatBot chatBot;
    private Map<Long, UserState> users;

    protected TelegramBot(DefaultBotOptions botOptions){
        super(botOptions);
        this.token = "674868490:AAHQzYxarifgBHFDrSIhPsmcwaEAkWIxtt0";
        this.botName = "logic_tasks_bot";
        this.chatBot = new ChatBot();
        this.users = new HashMap<Long, UserState>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        String text = msg.getText();
        UserState user = getUser(msg);
        if (text.equals("/start"))
            sendMsg(msg, chatBot.getWelcomeMessage(user.getName()));
        for (var answer: chatBot.analyzeAnswer(user, text))
            if (answer != null)
                sendMsg(msg, answer);
        if (user.getQuestionNumber() == chatBot.questions.length) {
            sendMsg(msg, chatBot.getSessionResult(user));
            updateUserState(msg);
        }
    }

    private void updateUserState(Message msg){
        Long id  = msg.getChatId();
        String userName = getUserName(msg);
        UserState user =  new UserState(userName);
        users.replace(id, user);
    }
// TODO использовать Concurrent HashMap
    private UserState getUser(Message msg) {
        Long id =msg.getChatId();
        String userName = getUserName(msg);
        if (!users.keySet().contains(id)){
            UserState user =  new UserState(userName);
            users.put(id, user);
            return user;
        }
        return users.get(id);

    }
// TODO доработать код
    private String getUserName(Message msg){
        var chat = msg.getChat();
        String firstName = chat.getFirstName();
        firstName = (firstName != null) ? firstName: "";
        String lastName = chat.getLastName();
        lastName =  (lastName != null) ? lastName + " ": "";
        String userName = chat.getUserName();
        String res = null;
        if (userName == null)
            res = firstName + " " + lastName;
        else
            return userName;
        if (" ".equals(res))
            return "User";
        return res.substring(0, res.length() - 1);
    }

    private void sendMsg(Message msg, String text) {
        SendMessage s = new SendMessage();
        s.setChatId(msg.getChatId()); // Боту может писать не один человек, и поэтому чтобы отправить сообщение, грубо говоря нужно узнать куда его отправлять
        s.setText(text);
        try { //Чтобы не крашнулась программа при вылете Exception
            execute(s);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
