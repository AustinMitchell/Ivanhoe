package controller.network;

import java.io.PrintWriter;
import java.util.*;

public class OutputThread implements Runnable {
		private PrintWriter out;
		private Queue<String> updateOut;
		private boolean exit;
		
		public OutputThread(PrintWriter out) {
			this.out = out;
			updateOut = new LinkedList<String>();
			exit = false;
			
			new Thread(this).start();
		}
		
		public void sendMessage(String msg) {
			synchronized(updateOut) {
				updateOut.add(msg);
			}
		}
		
		public void killThread() {
			exit = true;
		}
		
		@Override
		public void run() {
			while (!exit) {
				synchronized (updateOut) {
					if (!updateOut.isEmpty()) {
						out.println(updateOut.poll());
					}
				}
			}
		}
	}