package com.kumlaplay.mew_;

public class StopWatch extends Thread {

	Thread t;

	public volatile static Thread timer;
	
	private static long h;
	private static long m;
	private static long s;

	private static long startTime;

	private static long sleepTime = 1000;
	
	private static String s_h;
	private static String s_m;
	private static String s_s;
	
	
	public void start() {
		h = 0;
		m = 0;
		s = 0;
		
		startTime = System.currentTimeMillis();
		
		t = new Thread(this);
		timer = Thread.currentThread();
		t.start();

	}

	public void run() {
		timer();

	}
	
	public static void threadInterrupt(){
		Thread.currentThread().interrupt();
		
	}

	public static void timer() {
		while (Main.streamOn) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			s = (s+1);
			
			if (m > 0){
				
			}
			
			if (s > 59){
				startTime = startTime - 60;
				s = 0;
				m++;
				
			}
			
			
			if (m > 59){
				m = 0;
				h++;
			}
			
			if (h > 23 && m > 59 && s > 59){
				h = 0;
				m = 0;
				s = 0;
				
			}
			
			s_h = String.valueOf(h);
			s_m = String.valueOf(m);
			s_s = String.valueOf(s);
			
			Main.label_1.setText(s_h + ":" + s_m + ":" + s_s);
			
		}
	}

	public static void main(String[] args) {

	}

}
