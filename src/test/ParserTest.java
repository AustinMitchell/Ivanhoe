package test;

import static org.junit.Assert.*;

import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import network.*;
import rulesengine.RulesEngine;

public class ParserTest {
	Queue<String> queue;
	
	@Before
	public void setupClient() {
		Client client = new Client();
		queue = client.getGuiFlags();
	}

	@Test
	public void guiSplitterTest() {
		Parser.guiSplitter(queue, ("command 1" + RulesEngine.NEW_COM + "command 2"));
		assertEquals(queue.peek(), "command 1");
	}

}
