package com.kumlaplay.mew_;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class StreamingThread extends Thread{

	public static AudioInputStream din;
	public static SourceDataLine line;
	
	Thread t;
	
	public volatile static Thread streamThread;
	
	public void start() {
		t = new Thread(this);
		streamThread = Thread.currentThread();
		t.start();
		
	}
	
	public void run(){
		testConnection();
		
	}
	
	public static void testConnection(){
		Main.console.setText("Testing connection...");
		
		din = null;
		try {
			
			AudioInputStream in;
			
//			if (link == null) {
//				console.setText("Error: Entered URL is not valid.");
//				streamOn = false;
//				return;
			
//			new URL(Main.link)
			
//			} else {
			in = AudioSystem.getAudioInputStream(Main.testfile);
//			}
			
			
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
			line = (SourceDataLine) AudioSystem.getLine(info);
			if(line != null) {
				line.open(decodedFormat);
				byte[] data = new byte[4096];
				// Start
				line.start();
				int nBytesRead;
				while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
					line.write(data, 0, nBytesRead);
				}
			}

		}
		catch(Exception e) {
			streamThread.interrupt();
			System.out.println("NOT CORRECT URL OR STREAM DOES NOT EXIST");
			e.printStackTrace();
		}
		finally {
			if(din != null) {
				try { din.close(); 
				streamThread.interrupt();
				} catch(IOException e) { }
			}
		}
	}
	
	public static void main(String[] args){
		
	}
	
}
