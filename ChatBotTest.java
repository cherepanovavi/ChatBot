import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ChatBotTest {

	@Test
	void testGetEnding()
	{
		assertEquals(ChatBot.getEnding(0), "��");
		assertEquals(ChatBot.getEnding(5), "��");
		assertEquals(ChatBot.getEnding(46), "��");
		assertEquals(ChatBot.getEnding(21), "�");
		assertEquals(ChatBot.getEnding(1), "�");
		assertEquals(ChatBot.getEnding(22), "�");
		assertEquals(ChatBot.getEnding(3), "�");
		assertEquals(ChatBot.getEnding(104), "�");
		
	}
	
	@Test
	void testAnalyzeData()
	{
		assertEquals(true, false);
	}

}
