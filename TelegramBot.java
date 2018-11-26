package Source;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class TelegramBot extends TelegramLongPollingBot{
    private String token = "674868490:AAHQzYxarifgBHFDrSIhPsmcwaEAkWIxtt0";
    private String botName = "logic_tasks_bot";
    private ChatBot chatBot = new ChatBot();
    private Map<Long, UserState> users = new HashMap<Long, UserState>();

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
    }

    private UserState getUser(Message msg) {
        Long id =msg.getChatId();
        String userName = msg.getChat().getUserName();
        if (!users.keySet().contains(id)){
            UserState user =  new UserState(userName);
            users.put(id,user);
            return user;
        }
        return users.get(id);

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
