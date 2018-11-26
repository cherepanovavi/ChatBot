package Source.LogicFiles;


import java.util.ArrayList;
import java.util.List;

public class ChatBot {

    public final Question[] questions = Parser.parseQuestions();
    public static int attemptsCount = 3;

    public String getSkipMessage() {
        return "Пропускаем этот вопрос.";
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
        else if (qN1 == -1) {
            userState.moveToNextQuestion();
        } else
            result.add(checkAnswer(userState, answer));
        var qN2 = userState.getQuestionNumber();
        if ((qN2 > qN1) && (qN2 < questions.length))
            result.add(ask(userState));
        return result;
    }

    private String findKeys(UserState userState, String answer) {
        if (answer.contains("помощь") || answer.contains("справка") || answer.contains("?")
                || answer.contains("-h"))
            return getHelp();
        if (answer.equals(" ") || answer.contains("пропустить") || answer.contains("следующий"))
            return skipQuestion(userState);
        if (answer.equals("подсказка") || answer.equals("hint"))
            return questions[userState.getQuestionNumber()].getHint();
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
            output = String.format("Неправильный ответ, у тебя осталось %d попытки", userState.getQuestionAttempts() - 1);
            userState.spendAnAttempt();
        } else {
            output = String.format("У тебя закончились попытки. \n%s \nПереходим к следующему вопросу",
                    questions[questionNumber].getExplanation());
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
        EndingsChecker endingsChecker = new EndingsChecker();
        return String.format("Поздравляю, %s, ты набрал %d очк%s",
                userState.getName(), finalScore, endingsChecker.getEnding(finalScore));
    }
}
