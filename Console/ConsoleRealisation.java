package Source.Console;
import Source.LogicFiles.ChatBot;
import Source.LogicFiles.UserState;

import java.util.Scanner;

public class ConsoleRealisation {

    private ChatBot chatBot = new ChatBot();

    public void consoleRun() {
        Scanner input = new Scanner(System.in);
        System.out.println("Как тебя зовут?");
        String userName = input.nextLine();
        UserState userState = new UserState(userName);
        System.out.println(chatBot.getWelcomeMessage(userName));
        int length = chatBot.questions.length;
        while (userState.getQuestionNumber() < length) {
            String answer = input.nextLine().toLowerCase();
            var botAnswer = chatBot.analyzeAnswer(userState, answer);
            for (var bAnswer : botAnswer) {
                if (bAnswer != null)
                    System.out.println(bAnswer);
            }
        }
        System.out.println(chatBot.getSessionResult(userState));
        input.close();
    }
}
