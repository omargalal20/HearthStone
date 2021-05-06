package Controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import model.cards.spells.Spell;
import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;
import View.Board;
import View.ChooseHero;
import View.GameView;
import View.PauseMenu;
import View.WinnerView;
import engine.Game;
import engine.GameListener;
import exceptions.FullHandException;

public class AiController implements ActionListener,GameListener,MouseListener{
	private Game model;
	private WinnerView winner;
	private Board Board;
	private ChooseHero h;
	private PauseMenu pause;
	private Hero p1;
	private Hero p2;
	private int c=0;
	private Hero p;
	private Clip clip;
	private JButton Temp = new JButton();
	private Spell Spell;
	private Hero Winner;
	private int CurrentFatigue;
	private int OpponentFatigue;
	private AiPlayer AI;
	
	private ArrayList<JButton> compC = new ArrayList<JButton>();
	private ArrayList<JButton> compO = new ArrayList<JButton>();
    public AiController() throws FullHandException, CloneNotSupportedException{
          h = new ChooseHero();
          Board = new Board();
          pause = new PauseMenu();
          winner = new WinnerView();
    	  addActionListeners();
    }
    public void addActionListeners(){
    	h.getHunter().addActionListener(this);
    	h.getHunter().addMouseListener(this);
    	h.getMage().addActionListener(this);
    	h.getPaladin().addActionListener(this);
    	h.getPriest().addActionListener(this);
    	h.getWarlock().addActionListener(this);
    	h.getSelected().addActionListener(this);
    	pause.getOption().addActionListener(this);
    	pause.getQuit().addActionListener(this);
    	pause.getResume().addActionListener(this);
    	Board.getCurrent().addActionListener(this);
    	Board.getOpponent().addActionListener(this);
    	Board.getEndturnButton().addActionListener(this);
    	Board.getCurrentHeroPower().addActionListener(this);
    	Board.getOpponentHeroPower().addActionListener(this);
    	Board.getPause().addActionListener(this);
    	winner.getRematch().addActionListener(this);
    	winner.getExit().addActionListener(this);
    }
    
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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
	@Override
	public void onGameOver() {
		Winner = model.getCurrentHero();
	    String name = Winner.getName();
		String v1 = "HeroImages/"+name+" 2.gif";
	  	ImageIcon v= new ImageIcon(v1);
		Image b1 = v.getImage();
		Image b2 = b1.getScaledInstance(500, 500, Image.SCALE_DEFAULT);
	    v = new ImageIcon(b2);
        winner.getWinner().setIcon(v);
  	    winner.setVisible(true);
		pause.dispose();
		Board.dispose();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		if(b.getActionCommand().equals("P1 is Hunter") ){
			try {
				p1 = new Hunter();		
			    AI = new AiPlayer();
			    h.setSelectedHero(b);
			    h.getSelected().setText("Start Game");
			    h.getSelected().setActionCommand("Start Game");
					
			}catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
			
		}
		if(b.getActionCommand().equals("P1 is Mage") ){
			try {
				p1 = new Mage();
				AI = new AiPlayer();
					h.setSelectedHero(b);
					h.getSelected().setActionCommand("Start Game");
				
			}catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
			
		}
		if(b.getActionCommand().equals("P1 is Paladin") ){
			try {
				p1 = new Paladin();
				AI = new AiPlayer();
					h.setSelectedHero(b);
					h.getSelected().setActionCommand("Start Game");
					
			} catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
			
		}
		if(b.getActionCommand().equals("P1 is Priest") ){
			try {
				p1 = new Priest();
				AI = new AiPlayer();
				h.setSelectedHero(b);
				h.getSelected().setActionCommand("Start Game");
				
			} catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
			
		}
		if(b.getActionCommand().equals("P1 is Warlock") ){
			try {
				p1 = new Warlock();
				AI = new AiPlayer();
				h.setSelectedHero(b);
				h.getSelected().setActionCommand("Start Game");
			} catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage()); 
			}
		}
		if(b.getActionCommand().equals("Start Game") && h.getSelectedHero()!=null){
			try {
				model = new Game(p1, AI.getAiHero());
				if(model.getCurrentHero().getName().equals(AI.getAiHero().getName())){
					c=1;
					Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
					Board.getHeroDownInfo().setText(model.getOpponent().toString());
					//
					String s1 = model.getCurrentHero().getName();
					String s2 = model.getOpponent().getName();
					
					String t1 = "HeroImages/"+s1+" 2.gif";
			    	String v1 = "HeroImages/"+s2+" 2.gif";
			    	
			    	ImageIcon t= new ImageIcon(v1);
			    	ImageIcon v= new ImageIcon(t1);
			  		Image n1 = t.getImage();
			  		Image n2 = n1.getScaledInstance(320, 370, Image.SCALE_DEFAULT);
			  		t = new ImageIcon(n2);
			  		
			    	
			    	Board.getCurrent().setIcon(t);
			    	Board.getCurrent().setOpaque(false);
			    	Board.getCurrent().setContentAreaFilled(false);
			    	Board.getCurrent().setBorderPainted(false);
					 
			    	Board.getCurrent().setRolloverIcon(new ImageIcon(n2));
			    	Board.getCurrent().setDisabledIcon(new ImageIcon(n2));
			    	Board.getCurrent().setPressedIcon(new ImageIcon(n2));
			    	Board.getCurrent().setSelectedIcon(new ImageIcon(n2));
			    	
			    	
			  		Image b1 = v.getImage();
			  		Image b2 = b1.getScaledInstance(320, 340, Image.SCALE_DEFAULT);
			  		t = new ImageIcon(b2);
			  		
			    	
			    	Board.getOpponent().setIcon(t);
			    	Board.getOpponent().setOpaque(false);
			    	Board.getOpponent().setContentAreaFilled(false);
			    	Board.getOpponent().setBorderPainted(false);
					 
			    	Board.getOpponent().setRolloverIcon(new ImageIcon(b2));
			    	Board.getOpponent().setDisabledIcon(new ImageIcon(b2));
			    	Board.getOpponent().setPressedIcon(new ImageIcon(b2));
			    	Board.getOpponent().setSelectedIcon(new ImageIcon(b2));
					
			    	
					ImageIcon s3= new ImageIcon("HeroPowerImages/"+s2+" Power.png");
			  		Image s4 = s3.getImage();
			  		Image ns1 = s4.getScaledInstance(250, 200, java.awt.Image.SCALE_SMOOTH);
			  		s3 = new ImageIcon(ns1);
					Board.getCurrentHeroPower().setIcon(s3);
					Board.getCurrentHeroPower().setOpaque(false);
					Board.getCurrentHeroPower().setContentAreaFilled(false);
					Board.getCurrentHeroPower().setBorderPainted(false);
					ImageIcon s5= new ImageIcon("HeroPowerImages/"+s1+" Power.png");
			  		Image s6 = s5.getImage();
			  		Image ns2 = s6.getScaledInstance(250, 200, java.awt.Image.SCALE_SMOOTH);
			  		s5 = new ImageIcon(ns2);
					Board.getOpponentHeroPower().setIcon(s5);
					Board.getOpponentHeroPower().setOpaque(false);
					Board.getOpponentHeroPower().setContentAreaFilled(false);
					Board.getOpponentHeroPower().setBorderPainted(false);
				}
				
				else{
				Board.getHeroUpInfo().setText(model.getOpponent().toString());
				Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
				//
				String s1 = model.getCurrentHero().getName();
				String s2 = model.getOpponent().getName();
				
				String t1 = "HeroImages/"+s1+" 2.gif";
		    	ImageIcon t= new ImageIcon(t1);
		  		Image n1 = t.getImage();
		  		Image n2 = n1.getScaledInstance(320, 370, Image.SCALE_DEFAULT);
		  		t = new ImageIcon(n2);
		  		
		    	
		    	Board.getCurrent().setIcon(t);
		    	Board.getCurrent().setOpaque(false);
		    	Board.getCurrent().setContentAreaFilled(false);
		    	Board.getCurrent().setBorderPainted(false);
				 
		    	Board.getCurrent().setRolloverIcon(new ImageIcon(n2));
		    	Board.getCurrent().setDisabledIcon(new ImageIcon(n2));
		    	Board.getCurrent().setPressedIcon(new ImageIcon(n2));
		    	Board.getCurrent().setSelectedIcon(new ImageIcon(n2));
		    	
		    	String v1 = "HeroImages/"+s2+" 2.gif";
		    	ImageIcon v= new ImageIcon(v1);
		  		Image b1 = v.getImage();
		  		Image b2 = b1.getScaledInstance(320, 340, Image.SCALE_DEFAULT);
		  		t = new ImageIcon(b2);
		  		
		    	
		    	Board.getOpponent().setIcon(t);
		    	Board.getOpponent().setOpaque(false);
		    	Board.getOpponent().setContentAreaFilled(false);
		    	Board.getOpponent().setBorderPainted(false);
				 
		    	Board.getOpponent().setRolloverIcon(new ImageIcon(b2));
		    	Board.getOpponent().setDisabledIcon(new ImageIcon(b2));
		    	Board.getOpponent().setPressedIcon(new ImageIcon(b2));
		    	Board.getOpponent().setSelectedIcon(new ImageIcon(b2));
				
				ImageIcon s3= new ImageIcon("HeroPowerImages/"+s1+" Power.png");
		  		Image s4 = s3.getImage();
		  		Image ns1 = s4.getScaledInstance(250, 200, java.awt.Image.SCALE_SMOOTH);
		  		s3 = new ImageIcon(ns1);
				Board.getCurrentHeroPower().setIcon(s3);
				Board.getCurrentHeroPower().setOpaque(false);
				Board.getCurrentHeroPower().setContentAreaFilled(false);
				Board.getCurrentHeroPower().setBorderPainted(false);
				ImageIcon s5= new ImageIcon("HeroPowerImages/"+s2+" Power.png");
		  		Image s6 = s5.getImage();
		  		Image ns2 = s6.getScaledInstance(250, 200, java.awt.Image.SCALE_SMOOTH);
		  		s5 = new ImageIcon(ns2);
				Board.getOpponentHeroPower().setIcon(s5);
				Board.getOpponentHeroPower().setOpaque(false);
				Board.getOpponentHeroPower().setContentAreaFilled(false);
				Board.getOpponentHeroPower().setBorderPainted(false);
				}
				Who();
				
				model.getCurrentHero().listenToMinions();
				model.getOpponent().listenToMinions();
				updateMinionStats();
			} catch (FullHandException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
		updateMinionStats();
		Board.revalidate();
		Board.repaint();
		h.dispose();
	    Board.setVisible(true);
	} 
		
		
		if(b.getActionCommand().equals("End Turn")){
			try {
				if(c==0){	
				p = model.getCurrentHero();
				if(model.getOpponent().getCurrentHP()<=OpponentFatigue){
					onGameOver();
				}
				else if(OpponentFatigue>0){
					OpponentFatigue++;
				}
				else if(model.getOpponent().getDeck().isEmpty()){
					OpponentFatigue=1;
				}
				else{
				p.endTurn();
				c=1;
				if(model.getCurrentHero().fieldContains("Chromaggus")&&model.getCurrentHero().getField().size()<8){
					updateMinionStats();
				}
					updateMinionStats();  
				Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
				Board.getHeroDownInfo().setText(model.getOpponent().toString());
				
				Who();
				}
				} 
				else
				{
					if(model.getOpponent().getCurrentHP()<=CurrentFatigue){
						onGameOver();
					}
					else if(CurrentFatigue>0){
						CurrentFatigue++;
					}
					else if(model.getOpponent().getDeck().isEmpty()){
						CurrentFatigue=1;
					}
					else{
					p = model.getCurrentHero();
					
					p.endTurn();
					c=0;
					if(model.getCurrentHero().fieldContains("Chromaggus")&&model.getCurrentHero().getField().size()<8){
						updateMinionStats();
					}
						updateMinionStats();
					Board.getHeroUpInfo().setText(model.getOpponent().toString());
					Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
					
					Who();
				}
				}
				Temp = new JButton();
				Board.revalidate();
				Board.repaint();
			} catch (FullHandException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, model.getCurrentHero().getDeck().get(0), "Hand is full here is burnt card", JOptionPane.PLAIN_MESSAGE);
				if(c==0){
					Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
					Board.getHeroDownInfo().setText(model.getOpponent().toString());
					c=1;
					Who();
				}
				else{
					Board.getHeroUpInfo().setText(model.getOpponent().toString());
					Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
					c=0;
					Who();
				}
			}	
		}
		
		
		
		
		
		if(b.getActionCommand().equals("Pause")){
        	pause.setVisible(true);
        }
        if(b.getActionCommand().equals("Resume")){
        	pause.setVisible(false);
        }
        if(b.getActionCommand().equals("Quit")){
        	pause.dispose();
            Board.dispose();
            winner.dispose();
        }
        if(b.getActionCommand().equals("Option")){
        	int input = JOptionPane.showConfirmDialog(pause,
                    "Exit to rematch?", "Options",JOptionPane.YES_NO_OPTION);
        	if(input==0){
        		pause.dispose();
        		Board.dispose();
        		winner.dispose();
        		new GameView();
        		
        	}
        	
        }
		
		if(b.getActionCommand().equals("Redo")){
        	new GameView();
        	winner.dispose();	
        }
        if(b.getActionCommand().equals("Finish")){
        	winner.dispose();
        }
	}
	public void Who(){
		if(c==0){
			Board.getUp().setForeground(Color.WHITE);
			Board.getDown().setForeground(Color.GREEN);
			Board.getUp().setText("Opponent");
			Board.getDown().setText("Current");
		}
		else{
			Board.getUp().setForeground(Color.GREEN);
			Board.getDown().setForeground(Color.WHITE);
			Board.getUp().setText("Current");
			Board.getDown().setText("Opponent");
		}
	}
	
	public void updateMinionStats(){
			Board.getCurrentHandPanel().removeAll();
			Board.getCurrentHandCards().clear();
			Board.getCurrentFieldPanel().removeAll();
			compC.clear();
			Board.getOpponentHandPanel().removeAll();
			Board.getOpponentHandCards().clear();
			Board.getOpponentFieldPanel().removeAll();
			compO.clear();
			if(c==0){
		    for (int i = 0;i < model.getCurrentHero().getHand().size();i++) {
		    	ImageIcon s5= new ImageIcon("Images/back.png");
				Image s6 = s5.getImage();
				Image ns2 = s6.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
				s5 = new ImageIcon(ns2);
		    	JButton j =new JButton(s5);
		    	j.setPreferredSize(new Dimension(200,50));
		    	j.setOpaque(false);
			    j.setContentAreaFilled(false);
			    j.setBorderPainted(false);
				j.addMouseListener(this);
				j.addActionListener(this);
				Board.getCurrentHandCards().add(j);
				Board.getCurrentHandPanel().add(j);	
		    }
		    for (int i = 0;i<model.getCurrentHero().getField().size();i++) {
		    	ImageIcon s5= new ImageIcon("Images/back.png");
				Image s6 = s5.getImage();
				Image ns2 = s6.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
				s5 = new ImageIcon(ns2);
		    	JButton j =new JButton(s5);
		    	j.setPreferredSize(new Dimension(200,50));
		    	j.setOpaque(false);
			    j.setContentAreaFilled(false);
			    j.setBorderPainted(false);
				j.addMouseListener(this);
				j.addActionListener(this);
				Board.getCurrentFieldPanel().add(j);
				compC.add(j);
		    }
		    for (int i = 0;i<model.getOpponent().getHand().size();i++) {
		    	ImageIcon s5= new ImageIcon("Images/back.png");
				Image s6 = s5.getImage();
				Image ns2 = s6.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
				s5 = new ImageIcon(ns2);
		    	JButton j =new JButton(s5);
		    	j.setPreferredSize(new Dimension(200,50));
		    	j.setOpaque(false);
			    j.setContentAreaFilled(false);
			    j.setBorderPainted(false);
				j.addMouseListener(this);
				j.addActionListener(this);
				Board.getOpponentHandCards().add(j);
				Board.getOpponentHandPanel().add(j);	
		    }
		    for (int i = 0;i<model.getOpponent().getField().size();i++) {
		    	ImageIcon s5= new ImageIcon("Images/back.png");
				Image s6 = s5.getImage();
				Image ns2 = s6.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
				s5 = new ImageIcon(ns2);
		    	JButton j =new JButton(s5);
		    	j.setPreferredSize(new Dimension(200,50));
		    	j.setOpaque(false);
			    j.setContentAreaFilled(false);
			    j.setBorderPainted(false);
				j.addMouseListener(this);
				j.addActionListener(this);
				Board.getOpponentFieldPanel().add(j);
				compO.add(j);
				
		    }
	}
			else{
				for (int i = 0;i<model.getCurrentHero().getHand().size();i++) {
					ImageIcon s5= new ImageIcon("Images/back.png");
					Image s6 = s5.getImage();
					Image ns2 = s6.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
					s5 = new ImageIcon(ns2);
			    	JButton j =new JButton(s5);
			    	j.setPreferredSize(new Dimension(200,50));
			    	j.setOpaque(false);
				    j.setContentAreaFilled(false);
				    j.setBorderPainted(false);
					j.addMouseListener(this);
					j.addActionListener(this);
					Board.getOpponentHandCards().add(j);
					Board.getOpponentHandPanel().add(j);
			    }
				for (int i = 0;i<model.getOpponent().getHand().size();i++) {
			    	ImageIcon s5= new ImageIcon("Images/back.png");
					Image s6 = s5.getImage();
					Image ns2 = s6.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
					s5 = new ImageIcon(ns2);
			    	JButton j =new JButton(s5);
			    	j.setPreferredSize(new Dimension(200,50));
			    	j.setOpaque(false);
				    j.setContentAreaFilled(false);
				    j.setBorderPainted(false);
					j.addMouseListener(this);
					j.addActionListener(this);
					Board.getCurrentHandCards().add(j);
					Board.getCurrentHandPanel().add(j);	
			    }
			    for (int i = 0;i<model.getCurrentHero().getField().size();i++) {
			    	ImageIcon s5= new ImageIcon("Images/back.png");
					Image s6 = s5.getImage();
					Image ns2 = s6.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
					s5 = new ImageIcon(ns2);
			    	JButton j =new JButton(s5);
			    	j.setPreferredSize(new Dimension(200,50));
			    	j.setOpaque(false);
				    j.setContentAreaFilled(false);
				    j.setBorderPainted(false);
					j.addMouseListener(this);
					j.addActionListener(this);
					compO.add(j);
					Board.getOpponentFieldPanel().add(j);
			    }
			    
			    for (int i = 0;i<model.getOpponent().getField().size();i++) {
			    	ImageIcon s5= new ImageIcon("Images/back.png");
					Image s6 = s5.getImage();
					Image ns2 = s6.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
					s5 = new ImageIcon(ns2);
			    	JButton j =new JButton(s5);
			    	j.setPreferredSize(new Dimension(200,50));
			    	j.setOpaque(false);
				    j.setContentAreaFilled(false);
				    j.setBorderPainted(false);
					j.addMouseListener(this);
					j.addActionListener(this);
					compC.add(j);
					Board.getCurrentFieldPanel().add(j);
			    }
			}
		
		Board.revalidate();
	    Board.repaint(); 
	}
}
