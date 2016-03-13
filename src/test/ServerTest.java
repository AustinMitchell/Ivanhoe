package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;

import network.*;
import org.junit.Test;
import java.net.*;
import java.io.*;

public class ServerTest {
	Server server;
	
	//@Before
	public void setupServer() throws IOException {
		server = new Server(5000, 2); // port, maxPlayers
		Socket socket = new Socket("localhost", 5000);
	    ArrayList<PrintWriter> out = new ArrayList<PrintWriter>();
	    ArrayList<BufferedReader> in = new ArrayList<BufferedReader>();
	}
	
	/*@Test
	public void getNumOfPlayersTest() {
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
	public void createGameTest() {
		fail("Not yet implemented");
	}*/
	
	@Test
	public void setuptPlayersTest() {
		try {
			setupServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(server.getNumOfPlayers(), 2);
	}
	
	@Test
	public void acceptPlayerTest() {
		try {
			setupServer();
			assertEquals("localhost", server.acceptPlayer());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

