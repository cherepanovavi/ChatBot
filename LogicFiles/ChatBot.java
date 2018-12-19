package Source.LogicFiles;


import java.util.ArrayList;
import java.util.List;

public class ChatBot{

    private InitiatingManager manager;

    public final Question[] questions = Parser.parseQuestionsFromFile();
    public static int attemptsCount = 3;

    public String getSkipMessage() {
        return "Пропускаем этот вопрос.";
    }

    public ChatBot(){

    }
    public ChatBot(InitiatingManager manager){
        this.manager = manager;
    }


    private String ask(UserState userState) {
        int n = userState.getQuestionNumber();
        return questions[n].getQuestion();
    }

    public List<String> analyzeAnswer(UserState userState, String answer) {
        var result = new ArrayList<String>();
        var qN1 = userState.getQuestionNumber();
        answer = answer.toLowerCase();
        String checkKeysRes = findKeys(userState, answer);
        if (checkKeysRes != null)
            result.add(checkKeysRes);
        else if (qN1 == -1){
            result.add(getWelcomeMessage(userState.getName()));
            userState.moveToNextQuestion();
        } else
            result.add(checkAnswer(userState, answer));
        var qN2 = userState.getQuestionNumber();
        if ((qN2 > qN1) && (qN2 < questions.length))
            result.add(ask(userState));
        return result;
    }

    private String findKeys(UserState userState, String answer) {
        if (answer.contains("помощь") || answer.contains("справк") || answer.contains("?")
                || answer.contains("-h"))
            return getHelp();
        if (answer.contains("пропустить") || answer.contains("следующий"))
            return skipQuestion(userState);
        if (answer.contains("подсказк") || answer.contains("hint"))
            return questions[userState.getQuestionNumber()].getHint();
        if (answer.contains("отписаться")||answer.contains("отписка")) {
            manager.deleteUserState(userState);
            return("Простите, больше не будем вас беспокоить");
        }
        if (answer.contains("подписаться")||answer.contains("подписка")) {
            manager.addUserState(userState);
            return("Скоро увидимся:)");
        }
        return null;
    }

    private String checkAnswer(UserState userState, String input) {
        String output;
        int scores;
        int questionNumber = userState.getQuestionNumber();
        if (questions[questionNumber].isRightAnswer(input)) {
            output = "Правильный ответ!";
            scores = questions[questionNumber].getCost();
            userState.addScores(scores);
            userState.moveToNextQuestion();
        } else if (userState.getQuestionAttempts() > 1) {
            output = String.format("Неправильный ответ, у тебя осталось %d попытки. \nНужна подсказка? Отправь мне \"подсказка\"", userState.getQuestionAttempts() - 1);
            userState.spendAnAttempt();
        } else {
            String next = (userState.getQuestionNumber() < questions.length - 1) ? "\nПереходим к следующему вопросу" : "";
            output = String.format("У тебя закончились попытки. \n%s %s",
                    questions[questionNumber].getExplanation(), next);
            userState.moveToNextQuestion();
        }
        return output;
    }

    private String skipQuestion(UserState userState) {
        userState.moveToNextQuestion();
        return getSkipMessage();
    }

    public String getHelp() {
        return "Напиши свой ответ в следующей строке или введи \"пропустить\\следующий\", чтобы пропустить вопрос. " +
                "\nТы можешь попросить подсказку по вопросу, для этого напиши \"подсказка\\hint\"" +
                "\nПока никаких фич у меня больше нет, простите";
    }

    public String getWelcomeMessage(String userName) {
        return String.format("Привет, %s! Предлагаю тебе ответить на несколько каверзных вопросов" +
                        "\n Готов начать? Отправь любое сообщение" +
                        "\n Хочешь узнать больше информации отправь мне сообщение, содержащее слово \"справка\" "
                , userName);
    }

    public String getSessionResult(UserState userState) {
        int finalScore = userState.getScore();
        return String.format("Поздравляю, %s, ты набрал %d очк%s! Хочешь, чтобы мы напомнили тебе об игре?" +
                        "\n Отправь сообщение, содержащее слово \"подписка\".",
                userState.getName(), finalScore, EndingsChecker.getEnding(finalScore));
    }
}
