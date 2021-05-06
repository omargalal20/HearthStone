package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
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

@SuppressWarnings("serial")
public class WinnerView extends JFrame implements MouseListener{
	private JLabel winner;
	private JLabel bj;
	private JButton rematch;
	private JButton exit;
	private JLabel victory;
	
	private Clip clip;
    public WinnerView(){
    	this.setUndecorated(true);
  		this.setAlwaysOnTop(true);
  		this.setResizable(false);
  		this.setVisible(false);
  		Toolkit tk = Toolkit.getDefaultToolkit();
     	int xSize = ((int) tk.getScreenSize().getWidth());
     	int ySize = ((int) tk.getScreenSize().getHeight());
        this.setSize(xSize,ySize);
  		ImageIcon s= new ImageIcon("Images/choosehero.png");
  		Image s1 = s.getImage();
  		Image ns = s1.getScaledInstance(xSize, ySize, java.awt.Image.SCALE_SMOOTH);
  		s = new ImageIcon(ns);
  		bj = new JLabel(s);
  		bj.setSize(xSize, ySize);
  		this.setContentPane(bj);
  		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
  	    GraphicsDevice device = env.getDefaultScreenDevice();
  	    device.setFullScreenWindow(this);
  	    
  	    victory = new JLabel("Victory!");
  	    victory.setBounds(800,0,500, 500);
  	    victory.setForeground(Color.WHITE);
        victory.setFont(new Font("TimesRoman", Font.BOLD, 100));
  	    
  	    
  	    winner = new JLabel();
  	    winner.setBounds(720,300,500, 500);
  	    getContentPane().add(winner);
  	    
  	    ImageIcon s5= new ImageIcon("Images/ReMatch.png");
		Image s6 = s5.getImage();
		Image ns2 = s6.getScaledInstance(250, 200, java.awt.Image.SCALE_SMOOTH);
		s5 = new ImageIcon(ns2);
  	    
  	    rematch = new JButton(s5);
  	    rematch.setBounds(250,710,300,200);
  	    rematch.setOpaque(false);
	    rematch.setContentAreaFilled(false);
	    rematch.setBorderPainted(false);
	    rematch.setActionCommand("Redo");
	    rematch.addMouseListener(this);
  	    
  	    
  	    ImageIcon s3= new ImageIcon("Images/Exit1.png");
		Image s4 = s3.getImage();
		Image ns1 = s4.getScaledInstance(250, 200, java.awt.Image.SCALE_SMOOTH);
		s3 = new ImageIcon(ns1);
  	    
  	    exit = new JButton(s3);
  	    exit.setBounds(1400,710,300,200);
  	    exit.setActionCommand("Finish");
  	    exit.addMouseListener(this);
  	    exit.setOpaque(false);
 	    exit.setContentAreaFilled(false);
 	    exit.setBorderPainted(false);
  	    
  	    getContentPane().add(victory);
  	    getContentPane().add(rematch);
  	    getContentPane().add(exit);
  	    this.setLayout(null);
  	    this.revalidate();
		this.repaint();
		this.pack();
		this.setVisible(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
		WinnerView h = new WinnerView();
		h.setVisible(true);
	}

	public JLabel getWinner() {
		return winner;
	}

	public void setWinner(JLabel winner) {
		this.winner = winner;
	}

	public JButton getRematch() {
		return rematch;
	}

	public void setRematch(JButton rematch) {
		this.rematch = rematch;
	}

	public JButton getExit() {
		return exit;
	}

	public void setExit(JButton exit) {
		this.exit = exit;
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
