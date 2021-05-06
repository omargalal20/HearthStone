package View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import exceptions.FullHandException;
import Controller.AiController;
import Controller.Controller;

@SuppressWarnings("serial")
public class GameView extends JFrame implements ActionListener,MouseListener{
	private JButton j1;
	private JPanel opp;
	private JLabel CurrentHero;
	private Clip clip;
	private JLabel bj;

	public GameView(){
		this.setUndecorated(true);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setVisible(true);
		Toolkit tk = Toolkit.getDefaultToolkit();
   	    int xSize = ((int) tk.getScreenSize().getWidth());
   	    int ySize = ((int) tk.getScreenSize().getHeight());
   	    this.setSize(xSize,ySize);
		ImageIcon s= new ImageIcon("Images/cropped.jpg");
		Image s1 = s.getImage();
		Image ns = s1.getScaledInstance(xSize, ySize, java.awt.Image.SCALE_SMOOTH);
		s = new ImageIcon(ns);
		bj = new JLabel(s);
		bj.setSize(xSize, ySize);
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice device = env.getDefaultScreenDevice();
	    device.setFullScreenWindow(this);
		//this.setLocation(150,150);Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor((new ImageIcon("Images/normalCursor.png")).getImage(),new Point(), "Normal Cursor");
		//this.setCursor(cursor);
		//this.setLayout(new FlowLayout(FlowLayout.LEFT,700,150));
	    ImageIcon sr= new ImageIcon("Images/Exit1.png");
		Image r4 = sr.getImage();
		Image rs1 = r4.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
		sr = new ImageIcon(rs1);
	    
		JButton exitButton = new JButton(sr);
		exitButton.setOpaque(false);
        exitButton.setContentAreaFilled(false);
   	    exitButton.setBorderPainted(false);
		exitButton.addActionListener(this);
		exitButton.setActionCommand("Exit");
		exitButton.addMouseListener(this);
		exitButton.setBounds(50, 50, 200, 200);
		this.getContentPane().setLayout(null);
		ImageIcon s2= new ImageIcon("Images/play2.png");
  		Image s3 = s2.getImage();
  		Image ns1 = s3.getScaledInstance(300, 200, java.awt.Image.SCALE_SMOOTH);
  		s2 = new ImageIcon(ns1);
		j1 = new JButton(s2);
		j1.setOpaque(false);
   	    j1.setContentAreaFilled(false);
   	    j1.setBorderPainted(false);
   	    j1.setBounds(780,710,350,270);
		j1.addActionListener(this);
		j1.setActionCommand("Start");
		j1.addMouseListener(this);
		getContentPane().add(exitButton);
		getContentPane().add(j1);
		File soundFile = new File("Images/Hearthstone Soundtrack - Main Title.wav");
		try {
			AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
			clip = AudioSystem.getClip();
			 clip.open(x);
				clip.loop(10000);
		} catch (UnsupportedAudioFileException | IOException e) {
		}
	     catch (LineUnavailableException e) {
		}
		
		JButton AiMode = new JButton("AI");
		AiMode.addActionListener(this);
		AiMode.setActionCommand("AI");
		AiMode.addMouseListener(this);
	    AiMode.setBounds(1300,710,200,200);
	    getContentPane().add(AiMode);
		
		this.getContentPane().add(bj);
		this.revalidate();
		this.repaint();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public static void main(String[] args) throws FullHandException, CloneNotSupportedException {
		new GameView();
	}
	public JPanel getOpp() {
		return opp;
	}

	public void setOpp(JPanel opp) {
		this.opp = opp;
	}

	public JLabel getCurrentHero() {
		return CurrentHero;
	}

	public void setCurrentHero(JLabel currentHero) {
		CurrentHero = currentHero;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		if(b.getActionCommand().equals("Start")){
			ChooseType ct = new ChooseType();
			ct.setVisible(true);
			this.setVisible(false);
		}
		
		if(b.getActionCommand().equals("Exit")){
			clip.stop();
			this.dispose();
		}
	}
	
	public JButton getJ1() {
		return j1;
	}

	public Clip getClip() {
		return clip;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		File soundFile = new File("GameSounds/Click.wav");
		try {
			AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
			clip = AudioSystem.getClip();
			 clip.open(x);
			 clip.start();
		} catch (UnsupportedAudioFileException | IOException e2) {
		}
	     catch (LineUnavailableException e2) {
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		File soundFile = new File("GameSounds/Click.wav");
		try {
			AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
			clip = AudioSystem.getClip();
			 clip.open(x);
			 clip.start();
		} catch (UnsupportedAudioFileException | IOException e2) {
		}
	     catch (LineUnavailableException e2) {
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}