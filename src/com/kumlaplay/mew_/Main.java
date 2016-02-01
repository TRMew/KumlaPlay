package com.kumlaplay.mew_;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class Main
{

	public static URI tawk;
	
	public static File testfile = new File("C:/Users/Mattias Wiklund/Desktop/Leekspin.wav");
	
	public static boolean streamOn = false;
	public static String link; 
	
	public static int volume;
	public static long time;
	
	static JFrame frame = new JFrame("KumlaPlay");
	public static JTextPane console = new JTextPane();
	public static JLabel label_1;
	
	public static SourceDataLine line;
	public static AudioInputStream din;


	public static void main(String[] args) {
		initFrame();
		
	}
	
	private static void initFrame() {
		// Setting settings for the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 260);
		frame.setResizable(false);
		
		console.setEditable(false);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{160, 160, 160, 0};
		gbl_panel_1.rowHeights = new int[]{40, 30, 30, 29, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JButton connect = new JButton("STREAM OFF");
		GridBagConstraints gbc_connect = new GridBagConstraints();
		gbc_connect.fill = GridBagConstraints.BOTH;
		gbc_connect.insets = new Insets(0, 0, 5, 5);
		gbc_connect.gridx = 1;
		gbc_connect.gridy = 0;
		panel_1.add(connect, gbc_connect);
		connect.setFont(new Font("Xirod", Font.PLAIN, 11));
		connect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (streamOn) {
					streamOn = false;
					
					// Stop
					try {
						StreamingThread.in.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					StreamingThread.line.drain();
					StreamingThread.line.stop();
					StreamingThread.line.close();
					console.setText("Stream closed.");
					StreamingThread.streamThread.interrupt();
					try {
						din.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				} else {
					console.setText("Stream opened.");
					streamOn = true;
					StopWatch timer = new StopWatch();
					timer.start();
					StreamingThread player = new StreamingThread();
					player.start();
					
				}
			}
		});
		
		JLabel lblVolume = new JLabel("Volume");
		GridBagConstraints gbc_lblVolume = new GridBagConstraints();
		gbc_lblVolume.insets = new Insets(0, 0, 5, 5);
		gbc_lblVolume.gridx = 0;
		gbc_lblVolume.gridy = 1;
		panel_1.add(lblVolume, gbc_lblVolume);
		
		JLabel label = new JLabel("50%");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 2;
		gbc_label.gridy = 1;
		panel_1.add(label, gbc_label);
		
		JSlider slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			@SuppressWarnings({ "static-access", "null" })
			public void stateChanged(ChangeEvent arg0) {
				volume = slider.getValue();
				label.setText(volume + "%");
				
				Port lineIn = null;
				
				FloatControl setVolume = null;
				setVolume.setValue(volume);
				
				try {
					AudioSystem mixer = (AudioSystem) AudioSystem.getMixer(null);
					lineIn = (Port)mixer.getLine(Port.Info.LINE_IN);
					lineIn.open();
					
					setVolume = (FloatControl) lineIn.getControl(
							FloatControl.Type.VOLUME);
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.insets = new Insets(0, 0, 5, 5);
		gbc_slider.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider.gridx = 1;
		gbc_slider.gridy = 1;
		panel_1.add(slider, gbc_slider);
		
		JLabel lblTimeListened = new JLabel("Time Listened");
		GridBagConstraints gbc_lblTimeListened = new GridBagConstraints();
		gbc_lblTimeListened.insets = new Insets(0, 0, 5, 5);
		gbc_lblTimeListened.gridx = 0;
		gbc_lblTimeListened.gridy = 2;
		panel_1.add(lblTimeListened, gbc_lblTimeListened);
		
		JProgressBar progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.insets = new Insets(0, 0, 5, 5);
		gbc_progressBar.gridx = 1;
		gbc_progressBar.gridy = 2;
		panel_1.add(progressBar, gbc_progressBar);
		
		label_1 = new JLabel("00:00:00");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 0);
		gbc_label_1.gridx = 2;
		gbc_label_1.gridy = 2;
		panel_1.add(label_1, gbc_label_1);
		
		JButton btnPlay = new JButton("STOP");
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				// Stop
				line.drain();
				line.stop();
				line.close();
				try {
					din.close();
				} catch (IOException f) {
					f.printStackTrace();
				}
				
			}
		});
		GridBagConstraints gbc_btnPlay = new GridBagConstraints();
		gbc_btnPlay.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPlay.insets = new Insets(0, 0, 5, 5);
		gbc_btnPlay.gridx = 1;
		gbc_btnPlay.gridy = 3;
		panel_1.add(btnPlay, gbc_btnPlay);
		btnPlay.setFont(new Font("Xirod", Font.PLAIN, 12));
		
		GridBagConstraints gbc_console = new GridBagConstraints();
		gbc_console.gridwidth = 3;
		gbc_console.gridheight = 2;
		gbc_console.insets = new Insets(0, 0, 5, 0);
		gbc_console.fill = GridBagConstraints.BOTH;
		gbc_console.gridx = 0;
		gbc_console.gridy = 4;
		panel_1.add(console, gbc_console);
		
		JLabel lblPlayerMadeBy = new JLabel("Player made by Mew_");
		frame.getContentPane().add(lblPlayerMadeBy, BorderLayout.SOUTH);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFilre = new JMenu("File");
		menuBar.add(mnFilre);
		
		JMenuItem mntmOptions = new JMenuItem("Options");
		mntmOptions.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("Clicked Options");
				
				Options.loadFrame();
				
			}
		});
		mnFilre.add(mntmOptions);
		
		JMenuItem mntmRequestSong = new JMenuItem("Request Song");
		mntmRequestSong.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				console.setText("Trying to establish connection to request server...");
				try {
					tawk = new URI("https://tawk.to/0783161d0465f443c06b6c98250bf140f0969457/popout/default/?$_tawk_popout=true&$_tawk_sk=56ae1cfef60b7a92845822a1&$_tawk_tk=a43d5a082d79d58d9fe14c215e4c6c1a&v=458");
				} catch (URISyntaxException e) {
					console.setText("Error: Connection not established");
					e.printStackTrace();
				}
				try {
					java.awt.Desktop.getDesktop().browse(tawk);
					console.setText("Connection to request server established");
				} catch (IOException e) {
					console.setText("Error: Connection not established");
					e.printStackTrace();
				}
				
			}
		});
		mnFilre.add(mntmRequestSong);

		frame.setVisible(true);
		
	}
}