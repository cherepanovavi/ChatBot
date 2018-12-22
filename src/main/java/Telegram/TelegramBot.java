package main.java.Telegram;

import main.java.LogicFiles.*;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TelegramBot extends TelegramLongPollingBot implements ISender {
    private String token;
    private String botName;
    private ChatBot chatBot;
    private Map<Long, UserState> users;

    protected TelegramBot(DefaultBotOptions botOptions){
        super(botOptions);
        this.token = "674868490:AAHQzYxarifgBHFDrSIhPsmcwaEAkWIxtt0";
        this.botName = "logic_tasks_bot";
        this.chatBot = new ChatBot(new InitiatingManager(this, 12000));
        this.users = new ConcurrentHashMap<Long, UserState>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        String text = msg.getText();
        UserState user = getUser(msg);
        if (text.equals("/start"))
            sendMsg(msg.getChatId(), chatBot.getWelcomeMessage(user.getName()));
        for (var answer: chatBot.analyzeAnswer(user, text))
            if (answer != null)
                sendMsg(msg.getChatId(), answer);
        if (user.getQuestionNumber() == chatBot.questions.length) {
            sendMsg(msg.getChatId(), chatBot.getSessionResult(user));
            updateUserState(msg);
        }
    }

    private void updateUserState(Message msg){
        Long id  = msg.getChatId();
        users.get(id).restart();
    }

    private UserState getUser(Message msg) {
        Long id = msg.getChatId();
        String userName = getUserName(msg);
        return  users.computeIfAbsent(id, f -> (new UserState(userName, id)));
    }

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

    private void sendMsg(long id, String text) {
        SendMessage s = new SendMessage();
        s.setChatId(id); // Боту может писать не один человек, и поэтому чтобы отправить сообщение, грубо говоря нужно узнать куда его отправлять
        s.setText(Parser.decorate(text));
        s.setParseMode("HTML");
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

    @Override
    public void sendMessage(UserState userState, String message) {
        sendMsg(userState.getId(), message);
    }
}
