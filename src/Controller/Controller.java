package Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import model.cards.Card;
import model.cards.minions.Minion;
import model.cards.spells.AOESpell;
import model.cards.spells.FieldSpell;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.KillCommand;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.Pyroblast;
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
import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;

public class Controller implements ActionListener,GameListener,MouseListener{
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
	
	private ArrayList<JButton> compC = new ArrayList<JButton>();
	private ArrayList<JButton> compO = new ArrayList<JButton>();
    public Controller() throws FullHandException, CloneNotSupportedException{
          h = new ChooseHero();
          h.setVisible(true);
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
					h.setSelectedHero(b);																																					
					
			}catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
			
		}
		if(b.getActionCommand().equals("P1 is Mage") ){
			try {
				p1 = new Mage();
					h.setSelectedHero(b);
				
			}catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
			
		}
		if(b.getActionCommand().equals("P1 is Paladin") ){
			try {
				p1 = new Paladin();
					h.setSelectedHero(b);
					
			} catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
			
		}
		if(b.getActionCommand().equals("P1 is Priest") ){
			try {
				p1 = new Priest();
					h.setSelectedHero(b);
				
			} catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
			
		}
		if(b.getActionCommand().equals("P1 is Warlock") ){
			try {
				p1 = new Warlock();
				h.setSelectedHero(b);
			} catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage()); 
			}
		}
		if(b.getActionCommand().equals("Choose Player 1") && h.getSelectedHero()!=null){
			h.getP().setText("Choose Player 2");
			h.getHunter().setActionCommand("P2 is Hunter");
			h.getMage().setActionCommand("P2 is Mage");
			h.getPaladin().setActionCommand("P2 is Paladin");
			h.getPriest().setActionCommand("P2 is Priest");
			h.getWarlock().setActionCommand("P2 is Warlock");
			h.getSelected().setActionCommand("Choose Player 2");
			h.setSelectedHero(null);
		}
		if(b.getActionCommand().equals("Choose Player 2") && h.getSelectedHero()!=null){
			h.getSelected().setText("Start Game");
			h.getSelected().setActionCommand("Start Game");
		}
		if(b.getActionCommand().equals("P2 is Hunter") ){
			try {
					p2 = new Hunter();
					h.setSelectedHero(b);
			} catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
		}
		
		if(b.getActionCommand().equals("P2 is Mage") ){
			try {
					p2 = new Mage();
					h.setSelectedHero(b);
				
				
			} catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
		}
		if(b.getActionCommand().equals("P2 is Paladin") ){
			try {
				
					p2 = new Paladin();
					h.setSelectedHero(b);
				
				
			} catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
		}
		if(b.getActionCommand().equals("P2 is Priest") ){
			try {
				
					p2 = new Priest();
					h.setSelectedHero(b);
				
			} catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
		}
		if(b.getActionCommand().equals("P2 is Warlock") ){
			try {
				
					p2 = new Warlock();
					h.setSelectedHero(b);
				
			} catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
		}
		
		if(b.getActionCommand().equals("Start Game")){
        		try {
    				model = new Game(p1, p2);
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
    				
    				Who();
    				
    				model.getCurrentHero().listenToMinions();
    				model.getOpponent().listenToMinions();
    				updateMinionStats();
    			} catch (FullHandException | CloneNotSupportedException e1) {
    				JOptionPane.showMessageDialog(Board, e1.getMessage());
    			}
        	//}
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
		
		if(Board.getCurrentHandCards().contains(b)){
			int index=Board.getCurrentHandCards().indexOf(b);
			Card c1 = model.getCurrentHero().getHand().get(index);
			//model.getCurrentHero().listenToMinions();
			if(c1 instanceof Minion){
				try{
					if(c==0){
					model.getCurrentHero().playMinion((Minion) c1);
					String s = c1.getName();
					File soundFile = new File("MinionSounds/"+s+" Summon.wav");
					try {
						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
						clip = AudioSystem.getClip();
						 clip.open(x);
						 clip.start();
					} catch (UnsupportedAudioFileException | IOException e2) {
					}
				     catch (LineUnavailableException e2) {
					}
					Board.getHeroUpInfo().setText(model.getOpponent().toString());
					Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
					Board.revalidate();
					Board.repaint();
					}
					else
						model.getOpponent().playMinion((Minion) c1);
				} catch (NotYourTurnException | NotEnoughManaException
						| FullFieldException e1) {
					JOptionPane.showMessageDialog(Board, e1.getMessage());
				}
				updateMinionStats();
			}
		if(c1 instanceof Spell){
				if(c1 instanceof FieldSpell){
					try {
						if(c==0){
						model.getCurrentHero().castSpell((FieldSpell) c1);
						updateMinionStats();
						}
						else
							model.getOpponent().castSpell((FieldSpell) c1);
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
					
				}
				if(c1 instanceof AOESpell){
					try {
						model.getCurrentHero().castSpell((AOESpell) c1,model.getOpponent().getField());
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
					updateMinionStats();
				}
				if(c1 instanceof MinionTargetSpell){
					Temp = b;
					Temp.setActionCommand("MinionTargetSpell");
					Spell = (Spell) c1;
				}
				if(c1 instanceof HeroTargetSpell){
					Temp = b;
					Temp.setActionCommand("HeroTargetSpell");
					Spell = (Spell) c1;
				}
				if(c1 instanceof LeechingSpell){
					 Temp=b;
					 Temp.setActionCommand("LeechingSpell");
					 Spell = (Spell) c1;
				}
			}
			Board.getHeroUpInfo().setText(model.getOpponent().toString());
			Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
			
			Board.revalidate();
			Board.repaint();
		}
		if(Board.getOpponentHandCards().contains(b)){
			int index=Board.getOpponentHandCards().indexOf(b);
			Card c1 = model.getCurrentHero().getHand().get(index);
			model.getOpponent().listenToMinions();
			if(c1 instanceof Minion){
				try {	
					if(c==1){
					model.getCurrentHero().playMinion((Minion) c1);
					String s = c1.getName();
					File soundFile = new File("MinionSounds/"+s+" Summon.wav");
					try {
						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
						clip = AudioSystem.getClip();
						 clip.open(x);
						 clip.start();
					} catch (UnsupportedAudioFileException | IOException e2) {
					}
				     catch (LineUnavailableException e2) {
					}
					Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
					Board.getHeroDownInfo().setText(model.getOpponent().toString());
					Board.revalidate();
					Board.repaint();
					}
					else 
						model.getOpponent().playMinion((Minion) c1);
				} catch (NotYourTurnException | NotEnoughManaException
						| FullFieldException e1) {
					JOptionPane.showMessageDialog(Board, e1.getMessage());
				}
				updateMinionStats();
			}
			if(c1 instanceof Spell){
				if(c1 instanceof FieldSpell){
					try {
						if(c==1){
						model.getCurrentHero().castSpell((FieldSpell) c1);
						
						updateMinionStats();
						}
						else
							model.getOpponent().castSpell((FieldSpell) c1);
						
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
					
				}
				if(c1 instanceof AOESpell){
					
					try {
						if(c==1){
						model.getCurrentHero().castSpell((AOESpell) c1,model.getOpponent().getField());
						updateMinionStats();
						}
						else
							model.getOpponent().castSpell((AOESpell) c1,model.getOpponent().getField());
						
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
					updateMinionStats();
				}
				if(c1 instanceof MinionTargetSpell){
					Temp = b;
					Temp.setActionCommand("MinionTargetSpell");
					Spell = (Spell) c1;
				}
				if(c1 instanceof HeroTargetSpell){
					Temp = b;
					Temp.setActionCommand("HeroTargetSpell");
					Spell = (Spell) c1;
				}
				if(c1 instanceof LeechingSpell){
					 Temp=b;
					 Temp.setActionCommand("LeechingSpell");
					 Spell = (Spell) c1;
				}
			}
			Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
			Board.getHeroDownInfo().setText(model.getOpponent().toString());
			
			Board.revalidate();
			Board.repaint();
		}
		if(b.getActionCommand().equals("Current Hero Power")){
			 try {
			 if(c==0){
				if(model.getCurrentHero() instanceof Hunter){
				if(model.getOpponent().getCurrentHP()<=2){
					File soundFile = new File("MinionSounds/Victory.wav");
					try {
						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
						clip = AudioSystem.getClip();
						 clip.open(x);
						 clip.start();
					} catch (UnsupportedAudioFileException | IOException e2) {
					}
				     catch (LineUnavailableException e2) {
					}
					onGameOver();
					
				}
				else{
				model.getCurrentHero().useHeroPower();
				Board.getHeroUpInfo().setText(model.getOpponent().toString());
				Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
				}
				}
				if(model.getCurrentHero() instanceof Mage){
					Temp = b;
					Temp.setActionCommand("Current Hero Power");
				}
				if(model.getCurrentHero() instanceof Priest){
    				Temp = b;
    				Temp.setActionCommand("Current Hero Power");
    			}
				if(model.getCurrentHero() instanceof Warlock){
					Warlock w = (Warlock) model.getCurrentHero();
    				w.useHeroPower();
    				Board.getHeroUpInfo().setText(model.getOpponent().toString());
				    Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
				    updateMinionStats();
					model.getCurrentHero().listenToMinions();
					model.getOpponent().listenToMinions();
    			}
				if(model.getCurrentHero() instanceof Paladin){
					Paladin p = (Paladin) model.getCurrentHero();
					p.useHeroPower();
					Board.getHeroUpInfo().setText(model.getOpponent().toString());
				    Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
				    updateMinionStats();
					model.getCurrentHero().listenToMinions();
					model.getOpponent().listenToMinions();
				}
			 }
			 else{
				 model.getOpponent().useHeroPower();
			 }
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
			 updateMinionStats();
			 Board.revalidate();
			 Board.repaint(); 
		}
        if(b.getActionCommand().equals("Opponent Hero Power")){
        	try {
        		if(c==1){
        			if(model.getCurrentHero() instanceof Hunter){
        				if(model.getOpponent().getCurrentHP()<=2){
        				File soundFile = new File("MinionSounds/Victory.wav");
    					try {
    						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
    						clip = AudioSystem.getClip();
    						 clip.open(x);
    						 clip.start();
    					} catch (UnsupportedAudioFileException | IOException e2) {
    					}
    				     catch (LineUnavailableException e2) {
    					}
    					onGameOver();
        		     	}
        				else{
        				model.getCurrentHero().useHeroPower();
        				Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
    				    Board.getHeroDownInfo().setText(model.getOpponent().toString());
        				}
        				}
        			if(model.getCurrentHero() instanceof Mage){
        					Temp = b;
        					Temp.setActionCommand("Opponent Hero Power");
        				}
        			if(model.getCurrentHero() instanceof Priest){
        				Temp = b;
        				Temp.setActionCommand("Opponent Hero Power");
        			}
        			if(model.getCurrentHero() instanceof Warlock){
        				Warlock w = (Warlock) model.getCurrentHero();
        				w.useHeroPower();
        				Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
    				    Board.getHeroDownInfo().setText(model.getOpponent().toString());
    				    updateMinionStats();
    					model.getCurrentHero().listenToMinions();
    					model.getOpponent().listenToMinions();
    				    
        			}
        			if(model.getCurrentHero() instanceof Paladin){
    					Paladin p = (Paladin) model.getCurrentHero();
    					p.useHeroPower();
    					Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
    				    Board.getHeroDownInfo().setText(model.getOpponent().toString());
    				    updateMinionStats();
    					model.getCurrentHero().listenToMinions();
    					model.getOpponent().listenToMinions();
    				}
        		}
        		else
        			model.getOpponent().useHeroPower();
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
        	updateMinionStats();
        	Board.revalidate();
		    Board.repaint(); 
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
        if(compC.contains(b)){
        	if(c==0){
        		if(Temp.getActionCommand().equals("Current Hero Power") && model.getCurrentHero() instanceof Priest){
        			int index=compC.indexOf(b);
        			Minion c1 = model.getCurrentHero().getField().get(index);
        			Priest m = (Priest) model.getCurrentHero();
					try {
						m.useHeroPower(c1);
						updateMinionStats();
             		    Board.revalidate();
         			    Board.repaint();
					} catch (NotEnoughManaException
							| HeroPowerAlreadyUsedException
							| NotYourTurnException | FullHandException
							| FullFieldException | CloneNotSupportedException e1) {
						    JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
        		}
        		if(Temp.getActionCommand().equals("MinionTargetSpell")){
            		int index=compO.indexOf(b);
        			Minion c1 = model.getOpponent().getField().get(index);
        			try {
						model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
						Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
				    Board.getHeroUpInfo().setText(model.getOpponent().toString());
        			updateMinionStats();
        			Board.revalidate();
        			Board.repaint();
					} catch (NotYourTurnException | NotEnoughManaException
							| InvalidTargetException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
					
            	}
        		else{
            	Temp = b;
        		}
            }
            else{
            	if(Temp.getActionCommand().equals("Current Hero Power") && model.getCurrentHero() instanceof Mage){
        			int index=compC.indexOf(b);
        			Minion c1 = model.getOpponent().getField().get(index);
        			try {
        				   if(c1.getCurrentHP()==1){
        				    Mage m = (Mage) model.getCurrentHero();
    						m.useHeroPower(c1);
    						updateMinionStats();
                			}
                			else{
                			Mage m = (Mage) model.getCurrentHero();
        					m.useHeroPower(c1);
        					updateMinionStats();
                			}
        				   Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
       					   Board.getHeroDownInfo().setText(model.getOpponent().toString());
                		    Board.revalidate();
            			    Board.repaint(); 
					} catch (NotEnoughManaException
							| HeroPowerAlreadyUsedException
							| NotYourTurnException | FullHandException
							| FullFieldException
							| CloneNotSupportedException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
        			
        		}
            	if(Temp.getActionCommand().equals("MinionTargetSpell")){
            		int index=compC.indexOf(b);
        			Minion c1 = model.getOpponent().getField().get(index);
        			try {
        				if(c1.getCurrentHP()<=5 && Spell instanceof KillCommand){
        					model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
        					updateMinionStats();
    						
        				}
        				else if(c1.getCurrentHP()<=10 && Spell instanceof Pyroblast){
        					model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
        					updateMinionStats();
        				}
        				else{
						  model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
        				}
        				Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
					Board.getHeroDownInfo().setText(model.getOpponent().toString());
        			updateMinionStats();
        			Board.revalidate();
        			Board.repaint();
					} catch (NotYourTurnException | NotEnoughManaException
							| InvalidTargetException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
            	}
            	
            	
            	if(Temp.getActionCommand().equals("LeechingSpell")){
            		int index=compC.indexOf(b);
        			Minion c1 = model.getOpponent().getField().get(index);
        			try {
						model.getCurrentHero().castSpell((LeechingSpell)Spell, c1);
						updateMinionStats();
        			    Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
             	}
            	
                else{
        			int index=compO.indexOf(Temp);
        			Minion c1 = model.getCurrentHero().getField().get(index);
        			int index2=compC.indexOf(b);
        			Minion c2 = model.getOpponent().getField().get(index2);
            		try {
            			if(c1.isDivine()||c2.isDivine()){
            				if(c1.isDivine()&&c2.isDivine()){
            					model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
            					String s = c1.getName();
            					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
            					try {
            						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
            						clip = AudioSystem.getClip();
            						 clip.open(x);
            						 clip.start();
            					} catch (UnsupportedAudioFileException | IOException e2) {
            					}
            				     catch (LineUnavailableException e2) {
            					}
            					updateMinionStats();
            				}
            				else if(c1.isDivine()){
            					if(c1.getAttack()>c2.getCurrentHP()){
            						model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
                					String s = c1.getName();
                					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
                					try {
                						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
                						clip = AudioSystem.getClip();
                						 clip.open(x);
                						 clip.start();
                					} catch (UnsupportedAudioFileException | IOException e2) {
                					}
                				     catch (LineUnavailableException e2) {
                					}
                					updateMinionStats();
            					}
            				    else{
            					model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
            					String s = c1.getName();
            					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
            					try {
            						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
            						clip = AudioSystem.getClip();
            						 clip.open(x);
            						 clip.start();
            					} catch (UnsupportedAudioFileException | IOException e2) {
            					}
            				     catch (LineUnavailableException e2) {
            					}
            					updateMinionStats();
            				}
            				}
            				else{
            					if(c2.getAttack()>c1.getCurrentHP()){
            						model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
                					String s = c1.getName();
                					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
                					try {
                						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
                						clip = AudioSystem.getClip();
                						 clip.open(x);
                						 clip.start();
                					} catch (UnsupportedAudioFileException | IOException e2) {
                					}
                				     catch (LineUnavailableException e2) {
                					}
                					updateMinionStats();
            					}
            					else{
            					model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
            					String s = c1.getName();
            					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
            					try {
            						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
            						clip = AudioSystem.getClip();
            						 clip.open(x);
            						 clip.start();
            					} catch (UnsupportedAudioFileException | IOException e2) {
            					}
            				     catch (LineUnavailableException e2) {
            					}
            					updateMinionStats();
            					
            				}
            				}
            			}
            		   else{
            			if(c1.getAttack()>c2.getCurrentHP()){
            				if(c2.getAttack()>c1.getCurrentHP()){
            					model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
            					String s = c1.getName();
            					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
            					try {
            						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
            						clip = AudioSystem.getClip();
            						 clip.open(x);
            						 clip.start();
            					} catch (UnsupportedAudioFileException | IOException e2) {
            					}
            				     catch (LineUnavailableException e2) {
            					}
            					updateMinionStats();
            				}
            				else{
    					model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
    					String s = c1.getName();
    					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
    					try {
    						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
    						clip = AudioSystem.getClip();
    						 clip.open(x);
    						 clip.start();
    					} catch (UnsupportedAudioFileException | IOException e2) {
    					}
    				     catch (LineUnavailableException e2) {
    					}
    					updateMinionStats();
            				}
            				
            			}
            			else{ 
            				if(c2.getAttack()>c1.getCurrentHP()){
            					model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
            					String s = c1.getName();
            					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
            					try {
            						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
            						clip = AudioSystem.getClip();
            						 clip.open(x);
            						 clip.start();
            					} catch (UnsupportedAudioFileException | IOException e2) {
            					}
            				     catch (LineUnavailableException e2) {
            					}
            					updateMinionStats();
            				}
            			else{
            		    model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
            		    String s = c1.getName();
    					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
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
            			}
            			Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
    					   Board.getHeroDownInfo().setText(model.getOpponent().toString());
    					   updateMinionStats();
            		    Board.revalidate();
        			    Board.repaint();
            		}
    				} catch (CannotAttackException | NotYourTurnException
    						| TauntBypassException | InvalidTargetException
    						| NotSummonedException e1) {
    					JOptionPane.showMessageDialog(Board, e1.getMessage());
    				}
                  }
            		 updateMinionStats();
            		 Board.revalidate();
        			 Board.repaint(); 
            	}
   		 Board.revalidate();
			 Board.repaint();
        }
        if(compO.contains(b)){
        	if(c==1){
        		if(Temp.getActionCommand().equals("Current Hero Power") && model.getCurrentHero() instanceof Priest){
        			int index=compO.indexOf(b);
        			Minion c1 = model.getCurrentHero().getField().get(index);
        			Priest pr = (Priest) model.getCurrentHero();
					try {
						pr.useHeroPower(c1);
						updateMinionStats();
             		    Board.revalidate();
         			    Board.repaint();
					} catch (NotEnoughManaException
							| HeroPowerAlreadyUsedException
							| NotYourTurnException | FullHandException
							| FullFieldException | CloneNotSupportedException e1) {
						    JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
        		}
        		if(Temp.getActionCommand().equals("MinionTargetSpell")){
            		int index=compO.indexOf(b);
        			Minion c1 = model.getOpponent().getField().get(index);
        			try {
						model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
						Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
				    Board.getHeroDownInfo().setText(model.getOpponent().toString());
        			updateMinionStats();
        			Board.revalidate();
        			Board.repaint();
					} catch (NotYourTurnException | NotEnoughManaException
							| InvalidTargetException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
					
            	}
        		else{
            	Temp = b;
        		}
            	}
            else{
            		if(Temp.getActionCommand().equals("Current Hero Power") && model.getCurrentHero() instanceof Mage){
            			int index2=compO.indexOf(b);
            			Minion c2 = model.getOpponent().getField().get(index2);
            			try {
            				if(c2.getCurrentHP()==1){
            				    Mage m = (Mage) model.getCurrentHero();
        						m.useHeroPower(c2);
            					updateMinionStats();
                    			}
                    			else{
                    			Mage m = (Mage) model.getCurrentHero();
            					m.useHeroPower(c2);
            					updateMinionStats();
                    			}
            				   Board.getHeroUpInfo().setText(model.getOpponent().toString());
        					   Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
                    		    Board.revalidate();
                			    Board.repaint(); 
						} catch (NotEnoughManaException
								| HeroPowerAlreadyUsedException
								| NotYourTurnException | FullHandException
								| FullFieldException
								| CloneNotSupportedException e1) {
							JOptionPane.showMessageDialog(Board, e1.getMessage());
						}
            			
            		}
            		if(Temp.getActionCommand().equals("MinionTargetSpell")){
                    		int index=compO.indexOf(b);
                			Minion c1 = model.getOpponent().getField().get(index);
                			try {
                				if(c1.getCurrentHP()<=5 && Spell instanceof KillCommand){
                					model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
                					updateMinionStats();
                				}
                				else if(c1.getCurrentHP()<=10 && Spell instanceof Pyroblast){
                					model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
                					updateMinionStats();
                				}
                				else{
        						model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
                				}
                				Board.getHeroUpInfo().setText(model.getOpponent().toString());
     					    Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
     					    
          			        
                			updateMinionStats();
                			Board.revalidate();
                			Board.repaint();
        					} catch (NotYourTurnException | NotEnoughManaException
        							| InvalidTargetException e1) {
        						JOptionPane.showMessageDialog(Board, e1.getMessage());
        					}
        					
                    	}
                	if(Temp.getActionCommand().equals("LeechingSpell")){
                		int index=compO.indexOf(b);
            			Minion c1 = model.getOpponent().getField().get(index);
            			try {
    						model.getCurrentHero().castSpell((LeechingSpell)Spell, c1);
    						
            			    Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
            			    updateMinionStats();
    					} catch (NotYourTurnException | NotEnoughManaException e1) {
    						JOptionPane.showMessageDialog(Board, e1.getMessage());
    					}
               	}
            		else{
            		int index=compC.indexOf(Temp);
        			Minion c1 = model.getCurrentHero().getField().get(index);
        			int index2=compO.indexOf(b);
        			Minion c2 = model.getOpponent().getField().get(index2);
            		try {
            			if(c1.isDivine()||c2.isDivine()){
            				if(c1.isDivine()&&c2.isDivine()){
            					model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
            					String s = c1.getName();
            					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
            					try {
            						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
            						clip = AudioSystem.getClip();
            						 clip.open(x);
            						 clip.start();
            					} catch (UnsupportedAudioFileException | IOException e2) {
            					}
            				     catch (LineUnavailableException e2) {
            					}
            					updateMinionStats();
            				}
            				else if(c1.isDivine()){
            					if(c1.getAttack()>c2.getCurrentHP()){
        						model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
            					String s = c1.getName();
            					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
            					try {
            						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
            						clip = AudioSystem.getClip();
            						 clip.open(x);
            						 clip.start();
            					} catch (UnsupportedAudioFileException | IOException e2) {
            					}
            				     catch (LineUnavailableException e2) {
            					}
            				updateMinionStats();
        					}
            					
        				    else{
            					model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
            					String s = c1.getName();
            					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
            					try {
            						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
            						clip = AudioSystem.getClip();
            						 clip.open(x);
            						 clip.start();
            					} catch (UnsupportedAudioFileException | IOException e2) {
            					}
            				     catch (LineUnavailableException e2) {
            					}
            					updateMinionStats();
            				}
            				}
            				else{
            					if(c2.getAttack()>c1.getCurrentHP()){
            						model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
                					String s = c1.getName();
                					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
                					try {
                						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
                						clip = AudioSystem.getClip();
                						 clip.open(x);
                						 clip.start();
                					} catch (UnsupportedAudioFileException | IOException e2) {
                					}
                				     catch (LineUnavailableException e2) {
                					}
                				updateMinionStats();
            					}
            				    else{
            					model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
            					String s = c1.getName();
            					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
            					try {
            						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
            						clip = AudioSystem.getClip();
            						 clip.open(x);
            						 clip.start();
            					} catch (UnsupportedAudioFileException | IOException e2) {
            					}
            				     catch (LineUnavailableException e2) {
            					}
            					updateMinionStats();
            					}
            				}
            				
            			}
            		else{
            			    if(c1.getAttack()>c2.getCurrentHP()){
            			    	if(c2.getAttack()>c1.getCurrentHP()){
                					model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
                					String s = c1.getName();
                					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
                					try {
                						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
                						clip = AudioSystem.getClip();
                						 clip.open(x);
                						 clip.start();
                					} catch (UnsupportedAudioFileException | IOException e2) {
                					}
                				     catch (LineUnavailableException e2) {
                					}
                					updateMinionStats();
                				}
            			    else{
        					model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
        					String s = c1.getName();
        					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
        					try {
        						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
        						clip = AudioSystem.getClip();
        						 clip.open(x);
        						 clip.start();
        					} catch (UnsupportedAudioFileException | IOException e2) {
        					}
        				     catch (LineUnavailableException e2) {
        					}
        					updateMinionStats();
            			    }
                			}
                			else{
                				if(c2.getAttack()>c1.getCurrentHP()){
                					model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
                					String s = c1.getName();
                					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
                					try {
                						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
                						clip = AudioSystem.getClip();
                						 clip.open(x);
                						 clip.start();
                					} catch (UnsupportedAudioFileException | IOException e2) {
                					}
                				     catch (LineUnavailableException e2) {
                					}
                					updateMinionStats();
                				}
                			else{
                		    model.getCurrentHero().attackWithMinion((Minion) c1, (Minion) c2);
                		    String s = c1.getName();
        					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
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
                			}
            		    Board.revalidate();
        			    Board.repaint(); 
            		}
    				} catch (CannotAttackException | NotYourTurnException
    						| TauntBypassException | InvalidTargetException
    						| NotSummonedException e1) {
    					JOptionPane.showMessageDialog(Board, e1.getMessage());
    				}
            		
            		}
            		updateMinionStats();
           		    Board.revalidate();
       			    Board.repaint();
            	}
   		     Board.revalidate();
			 Board.repaint();
        }
        if(b.getActionCommand().equals("Attack Down")){
        	if(c==1){
        		if(Temp.getActionCommand().equals("Opponent Hero Power") && model.getCurrentHero() instanceof Mage){
        			try {
        				if(model.getOpponent().getCurrentHP()==1){
        					File soundFile = new File("MinionSounds/Victory.wav");
        					try {
        						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
        						clip = AudioSystem.getClip();
        						 clip.open(x);
        						 clip.start();
        					} catch (UnsupportedAudioFileException | IOException e2) {
        					}
        				     catch (LineUnavailableException e2) {
        					}
    						onGameOver();
        				}
        				else{
        					Mage m = (Mage) model.getCurrentHero();
    						m.useHeroPower(model.getOpponent());
    						Board.getHeroDownInfo().setText(model.getOpponent().toString());
						    Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
        				}
					} catch (NotEnoughManaException
							| HeroPowerAlreadyUsedException
							| NotYourTurnException | FullHandException
							| FullFieldException
							| CloneNotSupportedException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
        		}
        		if(Temp.getActionCommand().equals("HeroTargetSpell")){
        			try {
        				if(model.getOpponent().getCurrentHP()<=3 && Spell instanceof KillCommand){
        					File soundFile = new File("MinionSounds/Victory.wav");
        					try {
        						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
        						clip = AudioSystem.getClip();
        						 clip.open(x);
        						 clip.start();
        					} catch (UnsupportedAudioFileException | IOException e2) {
        					}
        				     catch (LineUnavailableException e2) {
        					}
        					onGameOver();
        				}
        				else if(model.getOpponent().getCurrentHP()<=10 && Spell instanceof Pyroblast){
        					File soundFile = new File("MinionSounds/Victory.wav");
        					try {
        						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
        						clip = AudioSystem.getClip();
        						 clip.open(x);
        						 clip.start();
        					} catch (UnsupportedAudioFileException | IOException e2) {
        					}
        				     catch (LineUnavailableException e2) {
        					}
        					onGameOver();
        				}
        				else{
						model.getCurrentHero().castSpell((HeroTargetSpell)(Spell), model.getOpponent());
						updateMinionStats();
						Board.getHeroDownInfo().setText(model.getOpponent().toString());
					    Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
        				}
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
        		}
        	else{
        		int index=compO.indexOf(Temp);
    			Minion c1 = model.getCurrentHero().getField().get(index);
        		try { 
        			if(model.getOpponent().getCurrentHP()<=c1.getAttack()){
        				File soundFile = new File("MinionSounds/Victory.wav");
    					try {
    						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
    						clip = AudioSystem.getClip();
    						 clip.open(x);
    						 clip.start();
    					} catch (UnsupportedAudioFileException | IOException e2) {
    					}
    				     catch (LineUnavailableException e2) {
    					}
        				onGameOver();
        			}
        			
        		else{
    				model.getCurrentHero().attackWithMinion((Minion) c1, model.getOpponent());
				    Board.getHeroDownInfo().setText(model.getOpponent().toString());
				    String s = c1.getName();
					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
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
        		    Board.revalidate();
    			    Board.repaint(); 
				} catch (CannotAttackException | NotYourTurnException
						| TauntBypassException | InvalidTargetException
						| NotSummonedException e1) {
					JOptionPane.showMessageDialog(Board, e1.getMessage());
				}
        	}	
        	}
        	else{
        	if(Temp.getActionCommand().equals("Current Hero Power") && model.getCurrentHero() instanceof Priest){
				try {
					Priest pr = (Priest) model.getCurrentHero();
					pr.useHeroPower(model.getCurrentHero());
					Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
					Board.getHeroUpInfo().setText(model.getOpponent().toString());
         		    Board.revalidate();
     			    Board.repaint();
				} catch (NotEnoughManaException
						| HeroPowerAlreadyUsedException
						| NotYourTurnException | FullHandException
						| FullFieldException | CloneNotSupportedException e1) {
					    JOptionPane.showMessageDialog(Board, e1.getMessage());
				}
    		}
        	}
       		    Board.revalidate();
   			    Board.repaint();
        }
        if(b.getActionCommand().equals("Attack Upper")){
        	if(c==0){
        		if(Temp.getActionCommand().equals("Current Hero Power") && model.getCurrentHero() instanceof Mage){
    			try {
    			if(model.getOpponent().getCurrentHP()==1){
    				File soundFile = new File("MinionSounds/Victory.wav");
					try {
						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
						clip = AudioSystem.getClip();
						 clip.open(x);
						 clip.start();
					} catch (UnsupportedAudioFileException | IOException e2) {
					}
				     catch (LineUnavailableException e2) {
					}
					onGameOver();
    				}
    			else{
    				Mage m = (Mage) model.getCurrentHero();
					m.useHeroPower(model.getOpponent());
					Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
					Board.getHeroUpInfo().setText(model.getOpponent().toString());
    			}	
				} catch (NotEnoughManaException
						| HeroPowerAlreadyUsedException
						| NotYourTurnException | FullHandException
						| FullFieldException
						| CloneNotSupportedException e1) {
					JOptionPane.showMessageDialog(Board, e1.getMessage());
				}
    		}
        		if(Temp.getActionCommand().equals("HeroTargetSpell")){
        			try {
        				if(model.getOpponent().getCurrentHP()<=3 && Spell instanceof KillCommand){
        					File soundFile = new File("MinionSounds/Victory.wav");
        					try {
        						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
        						clip = AudioSystem.getClip();
        						 clip.open(x);
        						 clip.start();
        					} catch (UnsupportedAudioFileException | IOException e2) {
        					}
        				     catch (LineUnavailableException e2) {
        					}
        					onGameOver();
        				}
        				else if(model.getOpponent().getCurrentHP()<=10 && Spell instanceof Pyroblast){
        					File soundFile = new File("MinionSounds/Victory.wav");
        					try {
        						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
        						clip = AudioSystem.getClip();
        						 clip.open(x);
        						 clip.start();
        					} catch (UnsupportedAudioFileException | IOException e2) {
        					}
        				     catch (LineUnavailableException e2) {
        					}
        					onGameOver();
        				}
        				else{
						model.getCurrentHero().castSpell((HeroTargetSpell)(Spell), model.getOpponent());
						updateMinionStats();
						Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
						Board.getHeroUpInfo().setText(model.getOpponent().toString());
        				}
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
        		}
        	else{
        		int index=compC.indexOf(Temp);
    			Minion c1 = model.getCurrentHero().getField().get(index);
        		try { 
        			if(model.getOpponent().getCurrentHP()<=c1.getAttack()){
        				File soundFile = new File("MinionSounds/Victory.wav");
    					try {
    						AudioInputStream x = AudioSystem.getAudioInputStream(soundFile);
    						clip = AudioSystem.getClip();
    						 clip.open(x);
    						 clip.start();
    					} catch (UnsupportedAudioFileException | IOException e2) {
    					}
    				     catch (LineUnavailableException e2) {
    					}
        				onGameOver();
        			}
        		else{
    				model.getCurrentHero().attackWithMinion((Minion) c1, model.getOpponent());
    				Board.getHeroUpInfo().setText(model.getOpponent().toString());
    				String s = c1.getName();
					File soundFile = new File("MinionSounds/"+s+" Attack.wav");
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
        		    Board.revalidate();
    			    Board.repaint(); 
				} catch (CannotAttackException | NotYourTurnException
						| TauntBypassException | InvalidTargetException
						| NotSummonedException e1) {
					JOptionPane.showMessageDialog(Board, e1.getMessage());
				}
        //		updateMinionStats();
       		    Board.revalidate();
   			    Board.repaint();
        	}
        	}
        	else{
        		if(Temp.getActionCommand().equals("Current Hero Power") && model.getCurrentHero() instanceof Priest){
					try {
						Priest pr = (Priest) model.getCurrentHero();
						pr.useHeroPower(model.getCurrentHero());
						Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
						Board.getHeroDownInfo().setText(model.getOpponent().toString());
             		    Board.revalidate();
         			    Board.repaint();
					} catch (NotEnoughManaException
							| HeroPowerAlreadyUsedException
							| NotYourTurnException | FullHandException
							| FullFieldException | CloneNotSupportedException e1) {
						    JOptionPane.showMessageDialog(Board, e1.getMessage());
					}
        		}
        	}
        	//updateMinionStats();
   		 Board.revalidate();
			 Board.repaint();
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
	public static void main(String[] args) throws FullHandException, CloneNotSupportedException {
		new Controller();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
   @Override
   public void mouseEntered(MouseEvent e) {
			JButton button = (JButton)e.getSource();
			String name=button.getText();
			if(c==0){
			if(Board.getCurrentHandCards().contains(button)){
				int index=Board.getCurrentHandCards().indexOf(button);
				Card c1 = model.getCurrentHero().getHand().get(index);
				if(c1 instanceof Minion){
					name=((Minion)c1).getName();
					String x = "MinionImages/"+name+".png";
					Board.getShowCard().setIcon(new ImageIcon(x));
				    Board.getCardInfo().setText(((Minion)c1).toString());
					}
			     if(c1 instanceof Spell){
			    	 if(c1.getName().equals("Shadow Word: Death")){
							name="Shadow Word Death";
							String x = "SpellImages/"+name+".png";
							Board.getShowCard().setIcon(new ImageIcon(x));
							   Board.getCardInfo().setText(((Spell)c1).toString());
						}
					else{
						name=((Spell)c1).getName();
						String x = "SpellImages/"+name+".png";
						Board.getShowCard().setIcon(new ImageIcon(x));
						   Board.getCardInfo().setText(((Spell)c1).toString());
					}
			}
			}	
		}
			
		    if(compC.contains(button)){
				int index=compC.indexOf(button);
				if(c==0){
					Card c1 = model.getCurrentHero().getField().get(index);
					if(c1 instanceof Minion){
					name=((Minion)c1).getName();
					String x = "MinionImages/"+name+".png";
					Board.getShowCard().setIcon(new ImageIcon(x));
				    Board.getCardInfo().setText(((Minion)c1).toString());
					}
				}
				else{
				Card c1 = model.getOpponent().getField().get(index);
				if(c1 instanceof Minion){
				name=((Minion)c1).getName();
				String x = "MinionImages/"+name+".png";
				Board.getShowCard().setIcon(new ImageIcon(x));
			    Board.getCardInfo().setText(((Minion)c1).toString());
				}
				}
			}
		    
			if(c==1){
			if(Board.getOpponentHandCards().contains(button)){
					int index=Board.getOpponentHandCards().indexOf(button);
					Card c1 = model.getCurrentHero().getHand().get(index);
					if(c1 instanceof Minion){
						name=((Minion)c1).getName();
						String x = "MinionImages/"+name+".png";
						Board.getShowCard().setIcon(new ImageIcon(x));
					    Board.getCardInfo().setText(((Minion)c1).toString());
						}
					if(c1 instanceof Spell){
						if(c1.getName().equals("Shadow Word: Death")){
							name="Shadow Word Death";
							String x = "SpellImages/"+name+".png";
							Board.getShowCard().setIcon(new ImageIcon(x));
							   Board.getCardInfo().setText(((Spell)c1).toString());
						}
					else{
							name=((Spell)c1).getName();
							String x = "SpellImages/"+name+".png";
							Board.getShowCard().setIcon(new ImageIcon(x));
							   Board.getCardInfo().setText(((Spell)c1).toString());
					}
						}
				}
			}
			
			if(compO.contains(button)){
				int index=compO.indexOf(button);
				if(c==0){
					Card c1 = model.getOpponent().getField().get(index);
					if(c1 instanceof Minion){
					name=((Minion)c1).getName();
					String x = "MinionImages/"+name+".png";
					Board.getShowCard().setIcon(new ImageIcon(x));
				    Board.getCardInfo().setText(((Minion)c1).toString());
					}
				}
				else{
				Card c1 = model.getCurrentHero().getField().get(index);
				if(c1 instanceof Minion){
				name=((Minion)c1).getName();
				String x = "MinionImages/"+name+".png";
				Board.getShowCard().setIcon(new ImageIcon(x));
			    Board.getCardInfo().setText(((Minion)c1).toString());
				}
				}
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
	public void mouseExited(MouseEvent e) {
		Board.getShowCard().setIcon(null);
		Board.getCardInfo().setText(null);
		
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