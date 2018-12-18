package Source.Console;
import Source.LogicFiles.ChatBot;
import Source.LogicFiles.ISender;
import Source.LogicFiles.InitializingManager;
import Source.LogicFiles.UserState;

import java.util.Scanner;

public class ConsoleRealisation implements ISender {

    private ChatBot chatBot = new ChatBot(new InitializingManager(this, 12000));

    public void consoleRun() {
        Scanner input = new Scanner(System.in);
        System.out.println("Как тебя зовут?");
        String userName = input.nextLine();
        UserState userState = new UserState(userName);
        int length = chatBot.questions.length;
        while (true) {
            if (userState.getQuestionNumber() < length) {
                String answer = input.nextLine().toLowerCase();
                var botAnswer = chatBot.analyzeAnswer(userState, answer);
                for (var bAnswer : botAnswer) {
                    if (bAnswer != null)
                        sendMessage(userState, bAnswer);
                }
            }
            if(userState.getQuestionNumber() == length){
                sendMessage(userState, chatBot.getSessionResult(userState));
                userState.restart();
            }
        }
    }

    @Override
    public void sendMessage(UserState userState, String message) {
        System.out.println(message);
    }
}
