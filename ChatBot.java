import java.util.Arrays;
import java.util.Scanner;


public class ChatBot {
	
	protected static String skipMessage = "Пропускаем этот вопрос."; 

    protected static Question[] questions = {new Question("На озере расцвела одна лилия. " +
            "Каждый день число цветков удваивалось, и на двадцатый день все "
            + "\nозеро покрылось цветами. На какой день покрылась цветами "
            + "\nполовина озера?", Arrays.asList("19"), 1),
            new Question("Сколько тонн земли находится в яме размера 2x2x2 метра",
                    Arrays.asList("0"), 1),
            new Question("Введите следующий символ последовательности С О Н Д Я Ф М А",
                    Arrays.asList("м"), 1),
            new Question("Обогнав в спринте третьего бегуна на каком месте вы окажетесь",
                    Arrays.asList("3", "третий", "третьем", "на третьем"), 1),
            new Question("Малыш спрятал от Карлсона банку с вареньем в "
                    + "\nодну из трех разноцветных коробок. На коробках"
                    + "\nМалыш сделал надписи: на красной – «Здесь варенья нет»; "
                    + "\nна синей – «Варенье - здесь»; на зеленой – "
                    + "\n«Варенье в синей коробке». Только одна из надписей п"
                    + "\nравдива. В какой коробке Малыш спрятал варенье?",
                    Arrays.asList("в зеленой", "в зелёной", "зеленой",
                            "зелёной"), 2)
    };
    

    private static String ask(UserState userState)
    {
        int n = userState.getQuestionNumber();
        return questions[n].question;
    }
    
    protected static String analyzeAnswer(UserState userState, String answer)
    {
    	answer = answer.toLowerCase();
    	String checkKeysRes = findKeys(userState, answer);
    	if (checkKeysRes != null)
    		//is it right check isNull?
    		return checkKeysRes;
    	return checkAnswer(userState, answer);
    }
    private static String findKeys(UserState userState, String answer)
    {
    	if (answer.contains("помощь") || answer.contains("справка") || answer.contains("?")
    			|| answer.contains("-h"))
    		return getHelp();
    	if (answer.equals(" ") || answer.contains("пропустить") || answer.contains("следующий"))
    		return skipQuestion(userState);
    	return null;
    }

    private static String checkAnswer(UserState userState, String input)
    {
        String output;
        int scores;
        int questionNumber = userState.getQuestionNumber();
        if (questions[questionNumber].answers.contains(input))
        {
            output = "Правильный ответ!";
            scores = questions[questionNumber].cost;
            userState.addScores(scores);
        }
        else
        	output = "Неправильный ответ";
        userState.moveToNextQuestion();
        return output;
    }
    //надо ли выносить изменение очков в отдельный метод?

    private static String skipQuestion(UserState userState)
    {
    	userState.moveToNextQuestion();
    	return skipMessage;
    }
    protected static String getHelp()
    {
    	return "Напиши свой ответ в следующей строке или введи \"-n\", чтобы пропустить вопрос. " +
                "Пока никаких фич у меня больше нет, простите";
    }
    public static void main(String[] args)
    {
        consoleRealisation();
    }

    private static String getWelcomeMessage(String userName)
    {
    	return String.format("Привет, %s! Предлагаю тебе ответить на несколько каверзных вопросов" +
                "\n Готов начать? Нажми клавишу Enter."+
    			"\n Хочешь узнать больше информации отправь мне сообщение, содержащее слово \"справка\" "
                , userName);
    }
    protected static String getEnding(int score)
    {
    	int modScore = score % 10;
    	if (modScore == 1)
    		return "о";
    	if (modScore > 1 && modScore < 5)
    		return "а";
    	return "ов";
    }
    private static String getSessionResult(UserState userState)
    {
    	int finalScore = userState.getScore();
        return String.format("Поздравляю, %s, ты набрал %d очк%s", userState.name, finalScore, getEnding(finalScore));
    }
    private static void consoleRealisation()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Как тебя зовут?");
        String userName = input.nextLine();
        UserState userState = new UserState(userName);
        System.out.println(getWelcomeMessage(userName));  
        String output = findKeys(userState, input.nextLine().toLowerCase());
        if (output != null)
        	System.out.println(output);
        int length = questions.length;
        while (userState.getQuestionNumber() < length)
        {
            System.out.println(ask(userState));
            String answer = input.nextLine().toLowerCase();
            System.out.println(analyzeAnswer(userState, answer));
        }
        System.out.println(getSessionResult(userState));
        input.close();
      }
}
