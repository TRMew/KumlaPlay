package com.kumlaplay.mew_;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Options {

	private static JFrame optionFrame;
	private static JTextField textField;
	
	public static void main(String[] args){
		loadFrame();
		
	}
	
	public static void loadFrame(){
		optionFrame = new JFrame("Options");
		optionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		optionFrame.setSize(500, 260);
		optionFrame.setResizable(false);
		
		JPanel panel = new JPanel();
		optionFrame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[160.00][160.00,grow][160.00]", "[30.00][30.00][30.00][29.00][30.00][30.00][30.00][-29.00]"));
		
		JLabel lblStreamUrl = new JLabel("Stream URL");
		panel.add(lblStreamUrl, "cell 0 0,alignx center,aligny center");
		
		textField = new JTextField();
		panel.add(textField, "cell 1 0,growx");
		textField.setColumns(10);
		
		JButton btnOk = new JButton("OK");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (Main.streamOn){
					Main.console.setText("Error: Cannot change URL while stream is running.");
				} else {
					Main.link = textField.getText();
					
				}
			}
		});
		panel.add(btnOk, "cell 2 0,alignx center,aligny center");
		
		JLabel lblSnartOpenSource = new JLabel("Snart open source :)");
		panel.add(lblSnartOpenSource, "cell 0 6,alignx center,aligny center");
		
		optionFrame.setVisible(true);
		
	}
	
}
