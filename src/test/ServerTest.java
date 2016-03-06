package test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;

import network.*;

import org.junit.Test;

public class ServerTest {
	Server server;
	
	@Before
	public void setupServer() throws IOException {
		server = new Server(5000, 2); // port, maxPlayers
	}
	
	@Test
	public void getNumOfPlayersTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void getPlayerPortTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void getPlayerInetAddressTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void getPlayerClientPortTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void getPlayerClientInetAddressTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void acceptPlayerTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void setuptPlayersTest() {
		assertEquals(server.getNumOfPlayers(), server.setupGame());
	}
	
	@Test
	public void createGameTest() {
		fail("Not yet implemented");
	}

}

