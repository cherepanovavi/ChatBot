package Source.Telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class TelegramBotRun {

    private static String proxyHost = "odinmillion-vpn.cloudapp.net";
    private static Integer proxyPort = 31337;

    public static void main(String[] args) {
        try {
            ApiContextInitializer.init(); // Инициализируем апи
            TelegramBotsApi botapi = new TelegramBotsApi();

            Authenticator.setDefault(new Authenticator()
            {
                @Override
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication("sockduser", "fuck_rkn_2018".toCharArray());
                }
            });
            
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
            botOptions.setProxyHost(proxyHost);
            botOptions.setProxyPort(proxyPort);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

            botapi.registerBot(new TelegramBot(botOptions));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
