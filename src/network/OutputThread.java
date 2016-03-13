package network;

import java.io.PrintWriter;

public class OutputThread implements Runnable {
		private PrintWriter out;
		private String updateOut;
		private boolean exit;
		
		public OutputThread(PrintWriter out) {
			this.out = out;
			updateOut = "";
			exit = false;
			
			new Thread(this).start();
		}
		
		public void sendMessage(String msg) {
			synchronized(updateOut) {
				updateOut = msg;
			}
		}
		
		public void killThread() {
			exit = true;
		}
		
		@Override
		public void run() {
			while (!exit) {
				synchronized (updateOut) {
					if (!updateOut.equals("")) {
						out.println(updateOut);
						updateOut = "";
					}
				}
			}
		}
	}