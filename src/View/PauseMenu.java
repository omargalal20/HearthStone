package View;

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
public class PauseMenu extends JFrame implements MouseListener{
	private JLabel bj;
	private JButton Option;
	private JButton Quit;
	private JButton Resume;
	private Clip clip;
	
    public PauseMenu(){
    	this.setUndecorated(true);
  		this.setAlwaysOnTop(true);
  		this.setResizable(false);
  		this.setVisible(false);
  		Toolkit tk = Toolkit.getDefaultToolkit();
     	int xSize = ((int) tk.getScreenSize().getWidth());
     	int ySize = ((int) tk.getScreenSize().getHeight());
        this.setSize(xSize,ySize);
  		ImageIcon s= new ImageIcon("Images/GameMenu.jpg");
  		Image s1 = s.getImage();
  		Image ns = s1.getScaledInstance(xSize, ySize, java.awt.Image.SCALE_SMOOTH);
  		s = new ImageIcon(ns);
  		bj = new JLabel(s);
  		bj.setSize(xSize, ySize);
  		this.setContentPane(bj);
  		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
  	    GraphicsDevice device = env.getDefaultScreenDevice();
  	    device.setFullScreenWindow(this);
    	this.setLayout(null);
    	placeButtons();
    	this.revalidate();
		this.repaint();
		this.pack();
 		this.setVisible(false);
 		this.setLocationRelativeTo(null);
 		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void placeButtons(){
    	Option = new JButton();
    	Option.setBounds(820, 390, 300, 50);
    	Option.setOpaque(false);
   	    Option.setContentAreaFilled(false);
   	    Option.setBorderPainted(false);
    	Option.setActionCommand("Option");
    	Option.addMouseListener(this);
    	Quit = new JButton();
    	Quit.setBounds(820, 480, 300, 50);
    	Quit.setOpaque(false);
   	    Quit.setContentAreaFilled(false);
   	    Quit.setBorderPainted(false);
    	Quit.setActionCommand("Quit");
    	Quit.addMouseListener(this);
    	Resume = new JButton();
    	Resume.setBounds(820, 625, 300, 50);
    	Resume.setOpaque(false);
   	    Resume.setContentAreaFilled(false);
   	    Resume.setBorderPainted(false);
    	Resume.setActionCommand("Resume");
    	Resume.addMouseListener(this);
    	this.getContentPane().add(Option);
    	this.getContentPane().add(Quit);
    	this.getContentPane().add(Resume);
    }
    public static void main(String[] args) {
		PauseMenu h = new PauseMenu();
		h.setVisible(true);
	}
	public JButton getOption() {
		return Option;
	}

	public void setOption(JButton option) {
		Option = option;
	}

	public JButton getQuit() {
		return Quit;
	}

	public void setQuit(JButton quit) {
		Quit = quit;
	}

	public JButton getResume() {
		return Resume;
	}

	public void setResume(JButton resume) {
		Resume = resume;
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
