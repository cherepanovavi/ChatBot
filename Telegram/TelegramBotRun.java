package Source.Telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotRun {

    private static String proxyHost = "78.155.193.236";
    private static Integer proxyPort = 8444;

    public static void main(String[] args) {
        try {
            ApiContextInitializer.init(); // Инициализируем апи
            TelegramBotsApi botapi = new TelegramBotsApi();

            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
            //botOptions.setProxyHost(proxyHost);
//            botOptions.setProxyPort(proxyPort);
//            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

            botapi.registerBot(new TelegramBot(botOptions));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
