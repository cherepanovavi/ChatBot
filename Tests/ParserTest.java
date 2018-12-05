package Source.Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Source.LogicFiles.ChatBot;
import Source.LogicFiles.EndingsChecker;
import Source.LogicFiles.Parser;
import Source.LogicFiles.UserState;
import org.junit.jupiter.api.Test;

import java.io.File;


public class ParserTest {
    @Test
    public void testGetOnlyFirstParagraph() {
        String page = Parser.readFromFile(new File("src/Source/Tests/HTML_test.txt"));
        String result = Parser.readFromFile(new File("src/Source/Tests/HTML_test_paragraph.txt"));
        assertEquals(Parser.getOnlyFirstParagraph(page), result);
    }
    @Test
    public void testParseFirstParagraph() {
        String paragraph = Parser.readFromFile(new File("src/Source/Tests/HTML_test_paragraph.txt"));
        String result = Parser.readFromFile(new File("src/Source/Tests/HTML_test_result.txt"));
        assertEquals(Parser.parseParagraph(paragraph), result);
    }

}
