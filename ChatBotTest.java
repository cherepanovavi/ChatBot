import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ChatBotTest {

	@Test
	void testGetEnding()
	{
		assertEquals(ChatBot.getEnding(0), "ов");
		assertEquals(ChatBot.getEnding(5), "ов");
		assertEquals(ChatBot.getEnding(46), "ов");
		assertEquals(ChatBot.getEnding(21), "о");
		assertEquals(ChatBot.getEnding(1), "о");
		assertEquals(ChatBot.getEnding(22), "а");
		assertEquals(ChatBot.getEnding(3), "а");
		assertEquals(ChatBot.getEnding(104), "а");
		
	}
	
	@Test
	void testAnalyzeData()
	{
		assertEquals(true, false);
	}

}
