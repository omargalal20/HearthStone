package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

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
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Board extends JFrame implements MouseListener, Serializable{
	 private JLabel bj;
	 private int xSize;
	 private int ySize;
	 ///////
	 private JPanel ShowCardPanel;
	 private JLabel ShowCard;
	 private JTextArea CardInfo;
	 private JPanel WholeBoard;
	 //////
	 private JPanel ShowHeroPanel;
	 private JTextArea HeroDownInfo;
	 private JTextArea HeroUpInfo;
	 private JPanel Who;
	 private JLabel Down;
	 private JLabel Up;
	 //////
	 private JPanel OpponentFieldPanel;
	 private JPanel CurrentFieldPanel;
	 private JPanel OpponentHandPanel;
	 private JPanel CurrentHandPanel;
	 ///////
	 private ArrayList<JButton> CurrentHandCards;
	 private ArrayList<JButton> OpponentHandCards;
	 ///////
	 private JButton EndturnButton;
	 private JButton Current;
	 private JButton Opponent;
	 private JButton CurrentHeroPower;
	 private JButton OpponentHeroPower;
	 private JButton Pause;
	 private Clip clip;
	 
     public Board(){
    	this.setUndecorated(true);
  		this.setAlwaysOnTop(true);
  		this.setResizable(false);
  		this.setVisible(false);
  		this.pack();
  		Toolkit tk = Toolkit.getDefaultToolkit();
        xSize = ((int) tk.getScreenSize().getWidth());
        ySize = ((int) tk.getScreenSize().getHeight());
        this.setSize(xSize,ySize);
  		ImageIcon s= new ImageIcon("Images/Uldaman_Board.PNG");
  		Image s1 = s.getImage();
  		Image ns = s1.getScaledInstance(xSize, ySize, java.awt.Image.SCALE_SMOOTH);
  		s = new ImageIcon(ns);
  		bj = new JLabel(s);
  		bj.setSize(xSize, ySize);
  		this.setContentPane(bj);
  		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
  	    GraphicsDevice device = env.getDefaultScreenDevice();
  	    device.setFullScreenWindow(this);
  	    
  	    WholeBoard = new JPanel();
  	    WholeBoard.setLayout(null);
    	
    	Current = new JButton();
    	Current.setActionCommand("Attack Down");
    	Current.setBounds(528, 688, 255, 230);
		 
    	Opponent = new JButton();
    	Opponent.setActionCommand("Attack Upper");
    	Opponent.setBounds(520, 130, 270, 190);
    	
    	CurrentHeroPower = new JButton();
    	CurrentHeroPower.setActionCommand("Current Hero Power");
    	CurrentHeroPower.setBounds(230, 700, 270, 240);
    	
    	
    	OpponentHeroPower = new JButton();
    	OpponentHeroPower.setActionCommand("Opponent Hero Power");
    	OpponentHeroPower.setBounds(230, 130, 270, 190);
 
    	
    	OpponentFieldPanel=new JPanel();
    	OpponentFieldPanel.setBounds(100, 320, 1150, 190);
    	OpponentFieldPanel.setOpaque(false); 
    	CurrentFieldPanel=new JPanel();
    	CurrentFieldPanel.setBounds(100, 525, 1150, 190);
    	CurrentFieldPanel.setOpaque(false);
    	
    	
    	OpponentHandPanel=new JPanel();
    	OpponentHandPanel.setLayout(new FlowLayout());
    	OpponentHandPanel.setOpaque(false);
    	OpponentHandPanel.setBounds(50, 10, 1250, 120);
    	
    	
    	CurrentHandPanel=new JPanel();
    	CurrentHandPanel.setOpaque(false);
    	CurrentHandPanel.setLayout(new FlowLayout());
    	CurrentHandPanel.setBounds(50, 950, 1250, 120);
    	
    	
    	CurrentHandCards = new ArrayList<JButton>();
    	OpponentHandCards = new ArrayList<JButton>();
    	EndturnButton = new JButton();
    	EndturnButton.setOpaque(false);
   	    EndturnButton.setContentAreaFilled(false);
   	    EndturnButton.setBorderPainted(false);
    	EndturnButton.setActionCommand("End Turn");
    	EndturnButton.addMouseListener(this);
    	EndturnButton.setBounds(1130, 450, 200, 100);
    	
    	
    	Pause = new JButton("Pause");
    	Pause.setOpaque(false);
    	Pause.setPreferredSize(new Dimension(300,100));
    	Pause.setFont(new Font("TimesRoman", Font.BOLD, 40));
        Pause.setForeground(Color.WHITE);
        Pause.setOpaque(false);
   	    Pause.setContentAreaFilled(false);
   	    Pause.setBorderPainted(false);
    	Pause.setActionCommand("Pause");
    	Pause.addMouseListener(this);
    	
    	
    	ShowCardPanel = new JPanel();
    	ShowCardPanel.setPreferredSize(new Dimension(300,this.getHeight()));
    	ShowCardPanel.setLayout(new BorderLayout());
    	ShowCard = new JLabel();
    	CardInfo = new JTextArea();
    	CardInfo.setPreferredSize(new Dimension(300,400));
    	CardInfo.setOpaque(false);
    	CardInfo.setEditable(false);
    	CardInfo.setForeground(Color.WHITE);
    	CardInfo.setFont(new Font("TimesRoman", Font.BOLD, 20)); 
    	ShowCard.setPreferredSize(new Dimension(300,this.getHeight()));
    	ShowCard.setOpaque(false);
    	ShowCardPanel.add(Pause,BorderLayout.NORTH);
    	ShowCardPanel.add(ShowCard,BorderLayout.CENTER);
    	ShowCardPanel.add(CardInfo,BorderLayout.SOUTH);
    	ShowCardPanel.setOpaque(false);
    	
    	ShowHeroPanel = new JPanel();
    	ShowHeroPanel.setPreferredSize(new Dimension(300,this.getHeight()));
    	ShowHeroPanel.setLayout(new BorderLayout());
    	ShowHeroPanel.setOpaque(false);
    	HeroDownInfo = new JTextArea();
    	HeroDownInfo.setPreferredSize(new Dimension(300,300));
    	HeroDownInfo.setEditable(false);
    	HeroDownInfo.setOpaque(false);
    	HeroDownInfo.setForeground(Color.WHITE);
    	HeroDownInfo.setFont(new Font("TimesRoman", Font.BOLD, 20));
    	HeroUpInfo = new JTextArea();
    	HeroUpInfo.setPreferredSize(new Dimension(300,300));
    	HeroUpInfo.setEditable(false);
    	HeroUpInfo.setOpaque(false);
    	HeroUpInfo.setForeground(Color.WHITE);
    	HeroUpInfo.setFont(new Font("TimesRoman", Font.BOLD, 20));
    	Who = new JPanel();
    	Who.setLayout(null);
    	Who.setOpaque(false);
        Up = new JLabel();
        Up.setFont(new Font("TimesRoman", Font.BOLD, 35));
        Up.setForeground(Color.WHITE);
        Up.setBounds(50, 50, 150, 150);
        
        Down = new JLabel();
        Down.setForeground(Color.WHITE);
        Down.setFont(new Font("TimesRoman", Font.BOLD, 35));
        Down.setBounds(50,200, 150, 150);
        
        Who.add(Up);
        Who.add(Down);
    	ShowHeroPanel.add(HeroUpInfo,BorderLayout.NORTH);
    	ShowHeroPanel.add(Who,BorderLayout.CENTER);
    	ShowHeroPanel.add(HeroDownInfo,BorderLayout.SOUTH);
    	
    	
    	
		WholeBoard.add(OpponentFieldPanel);
		WholeBoard.add(CurrentFieldPanel);
		WholeBoard.add(Current);
		WholeBoard.add(Opponent);
		WholeBoard.add(EndturnButton);
		WholeBoard.add(CurrentHeroPower);
		WholeBoard.add(OpponentHeroPower);
		WholeBoard.add(CurrentHandPanel);
		WholeBoard.add(OpponentHandPanel);
		
		WholeBoard.setOpaque(false);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(ShowCardPanel,BorderLayout.WEST);
		getContentPane().add(WholeBoard,BorderLayout.CENTER);
		getContentPane().add(ShowHeroPanel,BorderLayout.EAST);
		this.revalidate();
		this.repaint();
		this.pack();
 		this.setVisible(false);
 		this.setLocationRelativeTo(null);
 		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
     }
     public static void main(String[] args) {
		//Board h = new Board();
		//h.setVisible(true);
		
	}
	public JPanel getOpponentFieldPanel() {
		return OpponentFieldPanel;
	}
	public JPanel getCurrentFieldPanel() {
		return CurrentFieldPanel;
	}
	public JPanel getOpponentHandPanel() {
		return OpponentHandPanel;
	}
	public JPanel getCurrentHandPanel() {
		return CurrentHandPanel;
	}
	public ArrayList<JButton> getCurrentHandCards() {
		return CurrentHandCards;
	}
	public ArrayList<JButton> getOpponentHandCards() {
		return OpponentHandCards;
	}
	public JButton getEndturnButton() {
		return EndturnButton;
	}
	public void setEndturnButton(JButton endturnButton) {
		EndturnButton = endturnButton;
	}
	public JButton getCurrent() {
		return Current;
	}
	public void setCurrent(JButton current) {
		Current = current;
	}
	public JButton getOpponent() {
		return Opponent;
	}
	public void setOpponent(JButton opponent) {
		Opponent = opponent;
	}
		public JButton getCurrentHeroPower() {
		return CurrentHeroPower;
	}
	public void setCurrentHeroPower(JButton currentHeroPower) {
		CurrentHeroPower = currentHeroPower;
	}
	public JButton getOpponentHeroPower() {
		return OpponentHeroPower;
	}
	public void setOpponentHeroPower(JButton opponentHeroPower) {
		OpponentHeroPower = opponentHeroPower;
	}	
	public JButton getPause() {
		return Pause;
	}
	public void setPause(JButton pause) {
		Pause = pause;
	}
	public JPanel getShowCardPanel() {
		return ShowCardPanel;
	}
	public void setShowCardPanel(JPanel showCardPanel) {
		ShowCardPanel = showCardPanel;
	}
	public JLabel getShowCard() {
		return ShowCard;
	}
	public void setShowCard(JLabel showCard) {
		ShowCard = showCard;
	}
	public JPanel getWholeBoard() {
		return WholeBoard;
	}
	public void setWholeBoard(JPanel wholeBoard) {
		WholeBoard = wholeBoard;
	}
	public JTextArea getCardInfo() {
		return CardInfo;
	}
	public void setCardInfo(JTextArea cardInfo) {
		CardInfo = cardInfo;
	}
	
	public JTextArea getHeroDownInfo() {
		return HeroDownInfo;
	}
	public void setHeroDownInfo(JTextArea heroDownInfo) {
		HeroDownInfo = heroDownInfo;
	}
	public JTextArea getHeroUpInfo() {
		return HeroUpInfo;
	}
	public void setHeroUpInfo(JTextArea heroUpInfo) {
		HeroUpInfo = heroUpInfo;
	}
	public JLabel getDown() {
		return Down;
	}
	public void setDown(JLabel down) {
		Down = down;
	}
	public JLabel getUp() {
		return Up;
	}
	public void setUp(JLabel up) {
		Up = up;
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
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
