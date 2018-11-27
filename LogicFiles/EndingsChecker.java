package Source.LogicFiles;

public class EndingsChecker {

    public String getEnding(int score) {
        int modScore = score % 10;
        if (modScore == 1)
            return "о";
        if (modScore > 1 && modScore < 5)
            return "а";
        return "ов";
    }
}
