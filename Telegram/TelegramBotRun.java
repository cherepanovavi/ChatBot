package Source.Telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotRun {
    public static void main(String[] args) {
        try {
            ApiContextInitializer.init(); // Инициализируем апи
            TelegramBotsApi botapi = new TelegramBotsApi();
            botapi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
