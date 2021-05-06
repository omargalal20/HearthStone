package View;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class ChooseHero extends JFrame implements ActionListener,MouseListener{
	 private JPanel opp;
	 private JButton Hunter;
	 private JButton Mage;
	 private JButton Paladin;
	 private JButton Priest;
	 private JButton Warlock;
	 private JButton selected;
	 private JButton selectedHero;
	 private JLabel bj;
	 private JLabel p;
	 private Clip clip;
     public ChooseHero(){
    	 opp = new JPanel();
    	 selected = new JButton(new ImageIcon("Images/Choose.png"));
    	 selected.setOpaque(false);
    	 selected.setContentAreaFilled(false);
    	 selected.setBorderPainted(false);
    	 selected.setSize(300,400);
    	 selected.setActionCommand("Choose Player 1");
    	 this.setUndecorated(true);
 		 this.setAlwaysOnTop(true);
 		 this.setResizable(false);
 		 this.setVisible(true);
 		 Toolkit tk = Toolkit.getDefaultToolkit();
    	 int xSize = ((int) tk.getScreenSize().getWidth());
    	 int ySize = ((int) tk.getScreenSize().getHeight());
    	 this.setSize(xSize,ySize);
    	 p = new JLabel("Choose Player 1");
       	 p.setOpaque(false);
     	 p.setPreferredSize(new Dimension(300,100));
     	 p.setFont(new Font("TimesRoman", Font.BOLD, 40));
         p.setForeground(Color.WHITE);
         p.setOpaque(false);
     	 p.addMouseListener(this);
 		 ImageIcon s= new ImageIcon("Images/Win.jpg");
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
 	     p.setBounds(xSize-1400, ySize-1100, 1700, 300);
		 selected.setBounds(xSize-1200, ySize-300, 450, 300);
    	 opp.setLayout(new GridLayout(1,6));
    	 opp.setBounds(xSize-1650, ySize-1000, 1400, 1000);
    	 opp.setOpaque(false);
    	 initializeButtons();
    	 assignButtons();
    	 getContentPane().add(p);
    	 getContentPane().add(selected);
    	 getContentPane().add(opp);
    	 this.revalidate();
 		 this.repaint();
 		 this.setVisible(false);
 		 this.setLocationRelativeTo(null);
 		 this.setDefaultCloseOperation(EXIT_ON_CLOSE);
     }
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	public void initializeButtons(){
   	 Hunter = new JButton();
   	 Hunter.setSize(300, 400);
   	 Hunter.setOpaque(false);
   	 Hunter.setContentAreaFilled(false);
   	 Hunter.setBorderPainted(false);
   	 Hunter.setActionCommand("P1 is Hunter");
   	 Hunter.addActionListener(this);
   	 Hunter.addMouseListener(this);
     Mage = new JButton();
     Mage.setSize(300,400);
     Mage.setOpaque(false);
   	 Mage.setContentAreaFilled(false);
   	 Mage.setBorderPainted(false);
   	 Mage.setActionCommand("P1 is Mage");
   	 Mage.addActionListener(this);
   	 Mage.addMouseListener(this);
      	 Priest = new JButton();
      	 Priest.setSize(300,400);
      	 Priest.setOpaque(false);
  	     Priest.setContentAreaFilled(false);
  	     Priest.setBorderPainted(false);
  	     Priest.setActionCommand("P1 is Priest");
  	     Priest.addActionListener(this);
      	 Priest.addMouseListener(this);
      	 Paladin = new JButton();
      	 Paladin.setSize(300, 400);
      	 Paladin.setOpaque(false);
  	     Paladin.setContentAreaFilled(false);
  	     Paladin.setBorderPainted(false);
  	     Paladin.setActionCommand("P1 is Paladin");
  	     Paladin.addActionListener(this);
      	 Paladin.addMouseListener(this);
      	 Warlock = new JButton();
      	 Warlock.setSize(300,400);
      	 Warlock.setOpaque(false);
  	     Warlock.setContentAreaFilled(false);
  	     Warlock.setBorderPainted(false);
  	     Warlock.setActionCommand("P1 is Warlock");
  	     Warlock.addActionListener(this); 
      	 Warlock.addMouseListener(this);
	}
	public void assignButtons(){
		Hunter.setText("Rexxar");
		 String s = "HeroImages/Rexxar1.gif";
		 Hunter.setIcon(new ImageIcon(s));
		 Hunter.setRolloverIcon(new ImageIcon(s));
		 Hunter.setDisabledIcon(new ImageIcon(s));
		 Hunter.setPressedIcon(new ImageIcon(s));
		 Hunter.setSelectedIcon(new ImageIcon(s));
		 opp.add(Hunter);
		  
		 Mage.setText("Jaina Proudmoore");
		 String m = "HeroImages/JainaProudmoore.gif";
		 Mage.setIcon(new ImageIcon(m));
		 Mage.setRolloverIcon(new ImageIcon(m));
		 Mage.setDisabledIcon(new ImageIcon(m));
		 Mage.setPressedIcon(new ImageIcon(m));
		 Mage.setSelectedIcon(new ImageIcon(m));
		 opp.add(Mage);
		 
		 Priest.setText("Anduin Wrynn");
		 String pr = "HeroImages/AnduinWrynn.gif";
		 Priest.setIcon(new ImageIcon(pr));
		 Priest.setRolloverIcon(new ImageIcon(pr));
		 Priest.setDisabledIcon(new ImageIcon(pr));
		 Priest.setPressedIcon(new ImageIcon(pr));
		 Priest.setSelectedIcon(new ImageIcon(pr));
		 opp.add(Priest);
		 
		 Paladin.setText("Uther Lightbringer");
		 String p = "HeroImages/UtherLightbringer.gif";
		 Paladin.setIcon(new ImageIcon(p));
		 Paladin.setRolloverIcon(new ImageIcon(p));
		 Paladin.setDisabledIcon(new ImageIcon(p));
		 Paladin.setPressedIcon(new ImageIcon(p));
		 Paladin.setSelectedIcon(new ImageIcon(p));
		 opp.add(Paladin);
		 
		 Warlock.setText("Gul'dan");
		 String w = "HeroImages/Guldan1.gif";
		 Warlock.setIcon(new ImageIcon(w));
		 Warlock.setRolloverIcon(new ImageIcon(w));
		 Warlock.setDisabledIcon(new ImageIcon(w));
		 Warlock.setPressedIcon(new ImageIcon(w));
		 Warlock.setSelectedIcon(new ImageIcon(w));
		 opp.add(Warlock);
		
		 
    }
   public static void main(String[] args) {
	ChooseHero h = new ChooseHero();
	h.setVisible(true);
}
public JButton getHunter() {
	return Hunter;
}
public void setHunter(JButton hunter) {
	Hunter = hunter;
}
public JButton getMage() {
	return Mage;
}
public void setMage(JButton mage) {
	Mage = mage;
}
public JButton getPaladin() {
	return Paladin;
}
public void setPaladin(JButton paladin) {
	Paladin = paladin;
}
public JButton getPriest() {
	return Priest;
}
public void setPriest(JButton priest) {
	Priest = priest;
}
public JButton getWarlock() {
	return Warlock;
}
public void setWarlock(JButton warlock) {
	Warlock = warlock;
}
@Override
public void mouseClicked(MouseEvent e) {
	JButton b = (JButton) e.getSource();
	if(b.getActionCommand().equals("Back")){
		this.dispose();
	}
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
public void mouseEntered(MouseEvent arg0) {
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
	
}
@Override
public void mousePressed(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void mouseReleased(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}
public JButton getSelected() {
	return selected;
}
public JButton getSelectedHero() {
	return selectedHero;
}
public void setSelectedHero(JButton selectedHero) {
	this.selectedHero = selectedHero;
}
public JLabel getP() {
	return p;
}
public void setP(JLabel p) {
	this.p = p;
}
}
