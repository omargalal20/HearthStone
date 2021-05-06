package Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

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
import View.HostorClient;
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

public class SocketController implements ActionListener,GameListener,MouseListener{
	private Game model;
	private WinnerView winner;
	private Board Board;
	private ChooseHero h;
	private PauseMenu pause;
	private HostorClient hc;
	private Hero p1;
	private Hero p2;
	private Card c1;
	private Card c2;
	private Card cRecieve;
	private Card cRecieve2;
	private int c=0;
	private Hero p;
	private Clip clip;
	private JButton Temp = new JButton();
	private Spell Spell;
	private Hero Winner;
	private int CurrentFatigue;
	private int OpponentFatigue;
	private int attacker;
	private int attacked;
	
	private ServerSocket serversocket;
	private int port = 5000;
	private Scanner scanner = new Scanner(System.in);
	private InetAddress ip;
	private String user;
	
	private String m = "not ip";
	private frame f;
	
	private JLabel label = new JLabel();
	private JOptionPane op;
	private Thread t;
	
	private Socket s;
	private Socket clientsocket;
	private ObjectOutputStream dos;
	private ObjectInputStream dis;
	private ObjectOutputStream dosclient;
	private ObjectInputStream disclient;
	
	//DataInputStream din; 
	//DataOutputStream dout;
	//DataInputStream dinclient; 
	//DataOutputStream doutclient;
	
	private ArrayList<JButton> compC = new ArrayList<JButton>();
	private ArrayList<JButton> compO = new ArrayList<JButton>();
	
	
    public SocketController() throws FullHandException, CloneNotSupportedException{
    	  hc = new HostorClient();
          h = new ChooseHero();
          Board = new Board();
          pause = new PauseMenu();
          winner = new WinnerView();
    	  addActionListeners();
    }
    
    public String RecieveAction() throws IOException, ClassNotFoundException, FullHandException, CloneNotSupportedException, NotYourTurnException, NotEnoughManaException, FullFieldException, HeroPowerAlreadyUsedException, InvalidTargetException, CannotAttackException, TauntBypassException, NotSummonedException{
    	String action = null;
    	if(user.equals("server")){
		    action = (String)dis.readObject();
		    System.out.println("Client did " + action);
		    if(action.equals("End Turn")){
		    	model.getCurrentHero().endTurn();
		    }
		    else if(action.equals("Play Minion")){
		    	cRecieve = (Minion) dis.readObject();
		    	model.getCurrentHero().playMinion((Minion) cRecieve);
		    	String s = cRecieve.getName();
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
		    	System.out.println(cRecieve.getName() + "was played");
		    	for(Card m:model.getCurrentHero().getHand()){
	    			if(m.getName().equals(cRecieve.getName())){
	    				model.getCurrentHero().getHand().remove(m);
	    				break;
	    			}
	    			int i = model.getCurrentHero().getField().indexOf(cRecieve);
					//model.getCurrentHero().getField().remove(i);
					//model.getCurrentHero().getField().add((Minion) cRecieve);
					System.out.println("index of " + cRecieve.getName() + "is" + i);
	    	}
		    }
		    else if(action.equals("Attack Hero With Minion")){
		    	c1 = (Minion)dis.readObject();
		    	System.out.println("I reached Attacking part in server 1");
		    	if(model.getOpponent().getCurrentHP()<=((Minion) c1).getAttack()){
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
		    	System.out.println("I reached Attacking part in server 2");
		    }
		    else if(action.equals("Play Field Spell")){
		    	cRecieve = (Spell)dis.readObject();
		    	model.getCurrentHero().castSpell((FieldSpell) cRecieve);
		    }
		    else if(action.equals("Play Aoe Spell")){
		    	cRecieve = (Spell)dis.readObject();
		    	model.getCurrentHero().castSpell((AOESpell) cRecieve,model.getOpponent().getField());
		    }

		    else if(action.equals("Chose MT Spell")){
		    	cRecieve = (Spell)dis.readObject();
		    }
		    else if(action.equals("Play MT Spell")){
		    	attacked = (int)dis.readObject();
		    	Minion c1 = model.getOpponent().getField().get(attacked);
    			try {
    				if(c1.getCurrentHP()<=5 && Spell instanceof KillCommand){
    					model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
    					String s = c1.getName();
    					File soundFile = new File("MinionSounds/"+s+" Death.wav");
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
    				else if(c1.getCurrentHP()<=10 && Spell instanceof Pyroblast){
    					model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
    					String s = c1.getName();
    					File soundFile = new File("MinionSounds/"+s+" Death.wav");
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
    				else{
					  model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
    				}
    			}
    			catch (NotYourTurnException | NotEnoughManaException
						| InvalidTargetException e1) {
					JOptionPane.showMessageDialog(Board, e1.getMessage());
				} 
		    }
		    else if(action.equals("Chose HT Spell")){
		    	cRecieve = (Spell)dis.readObject();
		    	if(model.getOpponent().getCurrentHP()<=3 && cRecieve instanceof KillCommand){
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
				else if(model.getOpponent().getCurrentHP()<=10 && cRecieve instanceof Pyroblast){
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
				model.getCurrentHero().castSpell((HeroTargetSpell)(cRecieve), model.getOpponent());
				updateMinionStats();
				Board.getHeroDownInfo().setText(model.getOpponent().toString());
			    Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
				}
		    
		    }
		    else if(action.equals("Chose LS Spell")){
		    	cRecieve = (Spell)dis.readObject();
		    }
		    else if(action.equals("Play LS Spell")){
		    	attacked = (int)dis.readObject();
		    	model.getCurrentHero().castSpell((MinionTargetSpell)cRecieve, (Minion)model.getOpponent().getField().get(attacked));
		    }
		    else if(action.equals("Hunter Hero Power")){
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
		    	}
		    	
		    }
		    else if(action.equals("Priest Hero Power")){
		    	Priest pr = (Priest) model.getCurrentHero();
				pr.useHeroPower(model.getCurrentHero());
		    }
		    else if(action.equals("Warlock Hero Power")){
		    	model.getCurrentHero().useHeroPower();
		    }
		    else if(action.equals("Paladin Hero Power")){
		    	model.getCurrentHero().useHeroPower();
		    }
		    else if(action.equals("Mage Hero Power")){
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
		    else if(action.equals("Minion Mage Power")){

		    	attacked = (int)dis.readObject();
		    	Mage m = (Mage) model.getCurrentHero();
		    	if(model.getOpponent().getField().get(attacked).getCurrentHP()==1){
		    		m.useHeroPower((Minion)model.getOpponent().getField().get(attacked));
					String s = c1.getName();
					File soundFile = new File("MinionSounds/"+s+" Death.wav");
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
		    	else{
		    		m.useHeroPower((Minion)model.getOpponent().getField().get(attacked));
		    	}
				
				
		    }
		    else if(action.equals("Minion Priest Power")){
		    	attacked = (int)dis.readObject();
		    	Priest m = (Priest) model.getCurrentHero();
				m.useHeroPower((Minion)model.getCurrentHero().getField().get(attacked));
		    }
		    else if(action.equals("Attack With Minion")){
		        attacker = (int)dis.readObject();
		    	attacked = (int)dis.readObject();
		    	System.out.println(attacker);
		    	System.out.println(attacked);
		    	System.out.println();
		    	/*for(Card m:model.getCurrentHero().getField()){
					System.out.print(m.getName());
				}
				System.out.println();
				for(Card m:model.getOpponent().getField()){
					System.out.print(m.getName());
				}
				System.out.println();
				for(Card m:model.getOpponent().getHand()){
					System.out.print(m.getName());
				}
				System.out.println();
				int j = model.getOpponent().getField().indexOf(c1);
				int i = model.getOpponent().getField().indexOf(c2);
				System.out.println("index of "+ c1.getName() + "is "+j);
				System.out.println("index of "+ c2.getName() + "is "+i);*/
    			if(((Minion) model.getCurrentHero().getField().get(attacker)).isDivine()||((Minion) model.getOpponent().getField().get(attacked)).isDivine()){
    				if(((Minion) model.getCurrentHero().getField().get(attacker)).isDivine()&&((Minion) model.getOpponent().getField().get(attacked)).isDivine()){
    					model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
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
    				else if(((Minion) model.getCurrentHero().getField().get(attacker)).isDivine()){
    					if(((Minion) model.getCurrentHero().getField().get(attacker)).getAttack()>=((Minion) model.getOpponent().getField().get(attacked)).getCurrentHP()){
						Board.getOpponentFieldPanel().remove(attacked);;
    					compO.remove(attacked);
    					model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
    					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
    					model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
    					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
    					if(((Minion) model.getOpponent().getField().get(attacked)).getAttack()>=((Minion) model.getCurrentHero().getField().get(attacker)).getCurrentHP()){
    						model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
        					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
    					model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
    					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
    			    if(((Minion) model.getCurrentHero().getField().get(attacker)).getAttack()>=((Minion) model.getOpponent().getField().get(attacked)).getCurrentHP()){
    			    	if(((Minion) model.getOpponent().getField().get(attacked)).getAttack()>=((Minion) model.getCurrentHero().getField().get(attacker)).getCurrentHP()){
    			    		Board.getOpponentFieldPanel().remove(attacked);
        					compO.remove(attacked);
        					model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
        					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
					model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
					Board.getOpponentFieldPanel().remove(attacked);
        					compO.remove(attacked);
					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
        				if(((Minion) model.getOpponent().getField().get(attacked)).getAttack()>=((Minion) model.getCurrentHero().getField().get(attacker)).getCurrentHP()){
        					model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
        					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
        		    model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
        		    String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
    			 System.out.println();
    		    	for(Card m:model.getCurrentHero().getField()){
    					System.out.print(m.getName());
    				}
    				System.out.println();
    				for(Card m:model.getOpponent().getField()){
    					System.out.print(m.getName());
    				}
    				System.out.println();
		    }
		    
		    updateMinionStats();
			Who();
			System.out.println("Reachedrs");
    	}
    	
    	else if(user.equals("client")){
    		action = (String)disclient.readObject();
		    System.out.println("Server did " + action);
		    if(action.equals("End Turn")){
		    	model.getCurrentHero().endTurn();
		    }
		    else if(action.equals("Play Minion")){
		    	cRecieve = (Minion)disclient.readObject();
		    	model.getCurrentHero().playMinion((Minion) cRecieve);
		    	model.getCurrentHero().listenToMinions();
		    	String s = cRecieve.getName();
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
				for(Card m:model.getCurrentHero().getHand()){
	    			if(m.getName().equals(cRecieve.getName())){
	    				model.getCurrentHero().getHand().remove(m);
	    				break;
	    			}
	    	}
				int i = model.getCurrentHero().getField().indexOf(cRecieve);
				//model.getCurrentHero().getField().remove(i);
				//model.getCurrentHero().getField().add((Minion) cRecieve);
				System.out.println("index of " + cRecieve.getName() + "is" + i);
		    }

		    else if(action.equals("Attack Hero With Minion")){
		    	System.out.println("I reached Attacking part in client 1");
		    	c1 = (Minion)disclient.readObject();
		    	if(model.getOpponent().getCurrentHP()<=((Minion) c1).getAttack()){
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
		    	System.out.println("I reached Attacking part in client 2");
		    }
		    
		    else if(action.equals("Play Field Spell")){
		    	cRecieve = (Spell)disclient.readObject();
		    	model.getCurrentHero().castSpell((FieldSpell) cRecieve);
		    	
		    }
		    else if(action.equals("Play Aoe Spell")){
		    	cRecieve = (Spell)disclient.readObject();
		    	model.getCurrentHero().castSpell((AOESpell) cRecieve,model.getOpponent().getField());
		    }
		    else if(action.equals("Chose MT Spell")){
		    	cRecieve = (Spell)disclient.readObject();
		    }
		    else if(action.equals("Play MT Spell")){
		    	attacked = (int)disclient.readObject();
		    	Minion c1 = model.getOpponent().getField().get(attacked);
    			try {
    				if(c1.getCurrentHP()<=5 && Spell instanceof KillCommand){
    					model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
    					String s = c1.getName();
    					File soundFile = new File("MinionSounds/"+s+" Death.wav");
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
    				else if(c1.getCurrentHP()<=10 && Spell instanceof Pyroblast){
    					model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
    					String s = c1.getName();
    					File soundFile = new File("MinionSounds/"+s+" Death.wav");
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
    				else{
					  model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
    				}
    			}
    			catch (NotYourTurnException | NotEnoughManaException
						| InvalidTargetException e1) {
					JOptionPane.showMessageDialog(Board, e1.getMessage());
				} 
		    }
		    else if(action.equals("Chose HT Spell")){
		    	cRecieve = (Spell)disclient.readObject();
		    	
		    }
		    else if(action.equals("Play HT Spell")){
		    	if(model.getOpponent().getCurrentHP()<=3 && cRecieve instanceof KillCommand){
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
				else if(model.getOpponent().getCurrentHP()<=10 && cRecieve instanceof Pyroblast){
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
				model.getCurrentHero().castSpell((HeroTargetSpell)(cRecieve), model.getOpponent());
				updateMinionStats();
				Board.getHeroDownInfo().setText(model.getOpponent().toString());
			    Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
				}
		    }
		    else if(action.equals("Chose LS Spell")){
		    	cRecieve = (Spell)disclient.readObject();
		    }
		    else if(action.equals("Play LS Spell")){
		    	attacked = (int)disclient.readObject();
		    	model.getCurrentHero().castSpell((MinionTargetSpell)cRecieve, (Minion)model.getOpponent().getField().get(attacked));
		    }
		    else if(action.equals("Hunter Hero Power")){
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
		    	}
		    	
		    }
		    else if(action.equals("Priest Hero Power")){
		    	Priest pr = (Priest) model.getCurrentHero();
				pr.useHeroPower(model.getCurrentHero());
		    }
		    else if(action.equals("Warlock Hero Power")){
		    	model.getCurrentHero().useHeroPower();
		    }
		    else if(action.equals("Paladin Hero Power")){
		    	model.getCurrentHero().useHeroPower();
		    }
		    else if(action.equals("Mage Hero Power")){
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
		    else if(action.equals("Minion Mage Power")){

		    	attacked = (int)disclient.readObject();
		    	Mage m = (Mage) model.getCurrentHero();
				m.useHeroPower((Minion)model.getOpponent().getField().get(attacked));
		    }
		    else if(action.equals("Minion Priest Power")){

		    	attacked = (int)disclient.readObject();;
		    	Priest m = (Priest) model.getCurrentHero();
				m.useHeroPower((Minion)model.getCurrentHero().getField().get(attacked));
		    }
		    else if(action.equals("Attack With Minion")){
		    	attacker = (int)disclient.readObject();
		    	attacked = (int)disclient.readObject();
		    	System.out.println(attacker);
		    	System.out.println(attacked);
		    	System.out.println();
				/*for(Card m:model.getCurrentHero().getField()){
					System.out.print(m.getName());
				}
				System.out.println();
				for(Card m:model.getOpponent().getField()){
					if(m.getName().equals(c2.getName())){
						int i = model.getOpponent().getField().indexOf(m);
						Minion t = model.getOpponent().getField().get(i);
						t=(Minion) m;
					}
					System.out.print(m.getName());
				}
				System.out.println();
				for(Card m:model.getOpponent().getHand()){
					System.out.print(m.getName());
				}
				System.out.println();
				
				int j = model.getOpponent().getField().indexOf(c1);
				int i = model.getOpponent().getField().indexOf(c2);
				
				System.out.println("index of "+ c1.getName() + "is "+j);
				System.out.println("index of "+ c2.getName() + "is "+i);*/
		    	
    			if(((Minion) model.getCurrentHero().getField().get(attacker)).isDivine()||((Minion) model.getOpponent().getField().get(attacked)).isDivine()){
    				if(((Minion) model.getCurrentHero().getField().get(attacker)).isDivine()&&((Minion) model.getOpponent().getField().get(attacked)).isDivine()){
    					model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
    					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
    				else if(((Minion) model.getCurrentHero().getField().get(attacker)).isDivine()){
    					if(((Minion) model.getCurrentHero().getField().get(attacker)).getAttack()>=((Minion) model.getOpponent().getField().get(attacked)).getCurrentHP()){
						model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
    					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
    					model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
    					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
    					if(((Minion) model.getOpponent().getField().get(attacked)).getAttack()>=((Minion) model.getCurrentHero().getField().get(attacker)).getCurrentHP()){
    						model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
        					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
    					model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
    					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
    			    if(((Minion) c1).getAttack()>((Minion) model.getOpponent().getField().get(attacked)).getCurrentHP()){
    			    	if(((Minion) model.getOpponent().getField().get(attacked)).getAttack()>=((Minion) model.getCurrentHero().getField().get(attacker)).getCurrentHP()){
        					model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
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
					model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
        				if(((Minion) model.getOpponent().getField().get(attacked)).getAttack()>=((Minion) model.getCurrentHero().getField().get(attacker)).getCurrentHP()){
        					model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
        					String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
        		    model.getCurrentHero().attackWithMinion((Minion) model.getCurrentHero().getField().get(attacker), (Minion) model.getOpponent().getField().get(attacked));
        		    String s = (String) model.getCurrentHero().getField().get(attacker).getName();
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
    		
    		
    		}System.out.println();
    		    	for(Card m:model.getCurrentHero().getField()){
    					System.out.print(m.getName());
    				}
    				System.out.println();
    				for(Card m:model.getOpponent().getField()){
    					System.out.print(m.getName());
    				}
    				System.out.println();
		    }
		    
		    updateMinionStats();
			Who();
			System.out.println("Reachedrc");
    	}
    	return action;
    }
    public void SendAction(String s) throws IOException{
    	if(user.equals("server")){
    		System.out.println("Server sent " + s);
		    dos.writeObject(s);
		    dos.flush(); 
		    System.out.println("Reached Server Send Action");
    	}
    	else if(user.equals("client")){
    		System.out.println("Client sent " + s);
    		dosclient.writeObject(s);
    		dosclient.flush();
    		System.out.println("Reached Client Send Action");
    	}
    }
    
    
    public void updateTurn() throws FullHandException, CloneNotSupportedException, NotYourTurnException, NotEnoughManaException, FullFieldException, HeroPowerAlreadyUsedException, InvalidTargetException, CannotAttackException, TauntBypassException, NotSummonedException{
				try {
					RecieveAction();
					updateMinionStats();
				    Who();
				    System.out.println("Reached");
				} catch (ClassNotFoundException | IOException e) {
		        	int input = JOptionPane.showConfirmDialog(pause,
		                    "Connection was disturbed, want rematch?", "Options",JOptionPane.YES_NO_OPTION);
		        	if(input==0){
		        		pause.dispose();
		        		Board.dispose();
		        		winner.dispose();
		        		try {
		        			if(user.equals("server")){
		        				s.close();
		        			}
		        			else if(user.equals("client")){
		        				clientsocket.close();
		        			}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		        		
		        		new GameView();
		        	}
		        	
		        
				}
				
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
    	hc.getJoin().addActionListener(this);
    	hc.getHost().addActionListener(this);
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
  	    
  	    try {
  	    	dos.close();
			dis.close();
			dosclient.close();
  	    disclient.close();
  	    s.close();
  	    clientsocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	    
		pause.dispose();
		Board.dispose();
	}
	
	Thread tHost = new Thread(new Runnable(){

		@Override
		public void run() {
			while(p1==null||p2==null){
				if(p1==null){
					h.getP().setText("Choose Hero Player 1");
				}
				else{
					if(p2==null)
						h.getP().setText("You Chose " +p1.getName()+  " Waiting For Player 2");
				}
					
			}
			System.out.println("Done Thread tHost");
			System.out.println("Client Chose" + p2.getName());
			h.getP().setText("Press Start Game");
		}
		
	});
	Thread tClient = new Thread(new Runnable(){

		@Override
		public void run() {
			while(p1==null||p2==null){
				if(p2==null){
					h.getP().setText("Choose Hero Player 2");
				}
				else{
					if(p1==null)
						h.getP().setText("You Chose " +p2.getName()+  " Waiting For Player 1");
				}
					
			}
			System.out.println("Done Thread tClient");
			System.out.println("Server Chose" + p1.getName());
			ClientWaitingForStartGame.start();
			
		}
		
	});
	
	Thread ClientWaitingForStartGame = new Thread(new Runnable(){

		@Override
		public void run() {
			while(model==null){
				h.getP().setText("Wait for Host to Start Game");
			}
			h.getP().setText("Press Start Game");
			
		}
		
	});
	
	public class frame extends JFrame{
		public JLabel getJ() {
			return j;
		}
		public void setJ(JLabel j) {
			this.j = j;
		}
		private JLabel j;
		public frame(){
	        this.setUndecorated(true);
	        setSize(200, 100);
	        j = new JLabel();
	        j.setHorizontalAlignment(JLabel.CENTER);
	        j.setVerticalAlignment(JLabel.CENTER);
	        j.setBackground(Color.WHITE);
	        this.setLayout(new BorderLayout());
	        this.add(j, BorderLayout.CENTER);
	        this.setLocationRelativeTo(null);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setVisible(true);
	        setResizable(false);
	        revalidate();
	        repaint();
	    
		}
		private void closeIt(){
	        this.getContentPane().setVisible(false);
	        this.dispose();

	    }
	}
	
	Thread EnterIpAdd = new Thread(new Runnable(){

		@Override
		public void run() {
			try {
				ip = InetAddress.getLocalHost();
			} catch (UnknownHostException e2) {
				e2.printStackTrace();
			}
			
			while(!m.equals(ip.getHostAddress())){
				m = JOptionPane.showInputDialog(hc,"Enter Host Ip Address");
			}
			
		}
		
	});
	Thread ShowIpAdd = new Thread(new Runnable(){
		JFrame f;
		JLabel j;
		@Override
		public void run() {
			try {
				ip = InetAddress.getLocalHost();
			} catch (UnknownHostException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			f =new JFrame();
	        f.setUndecorated(true);
	        f.setSize(200, 100);
	        j = new JLabel();
	        j.setHorizontalAlignment(JLabel.CENTER);
	        j.setVerticalAlignment(JLabel.CENTER);
	        j.setBackground(Color.WHITE);
	        f.setLayout(new BorderLayout());
	        f.add(j, BorderLayout.CENTER);
	        f.setLocationRelativeTo(null);
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        f.setAlwaysOnTop(true);
	        f.setVisible(true);
	        f.setResizable(false);
	        f.revalidate();
	        f.repaint();
	        System.out.println("Here is Thread");
			j.setText("Ip is " + ip.getHostAddress());
			while(!m.equals(ip.getHostAddress())){
			}
			f.dispose();
		}
		
	});
	@Override
	//192.168.1.4
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		if(b.getActionCommand().equals("Host")){
			//ShowIpAdd.start();
			try {
				serversocket=new ServerSocket(port);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} 
			

			try {
				ip = InetAddress.getLocalHost();
			} catch (UnknownHostException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			System.out.println("Server:");
			System.out.println("Waiting for Connection" + "\n" + "IP Address: "+ip.getHostAddress() + "\n" + port);
			
				JOptionPane.showMessageDialog(hc,"Ip Address: " + ip.getHostAddress());
            try {
				s=serversocket.accept();
			} catch  (IOException e1) {
				e1.printStackTrace();
			}
			try {
            		dos= new ObjectOutputStream((s.getOutputStream()));
            		dis= new ObjectInputStream((s.getInputStream()));
					//din=new DataInputStream(s.getInputStream());
					//dout=new DataOutputStream(s.getOutputStream());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            	
			System.out.println("Connected to player");
			user="server";
			hc.dispose();
			h.setVisible(true);
			System.out.println("Before Thread");
			Thread t = new Thread(new Runnable(){

				@Override
				public void run() {
					try {
						p2 = (Hero) dis.readObject();
					} catch (ClassNotFoundException
							| IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Done Thread");
					
				}
				
			});
			
			t.start();
			tHost.start();
			System.out.println("Thread Started");
			
		}
		
        if(b.getActionCommand().equals("Client")){
        	System.out.println("Client:");
        	//EnterIpAdd.start();
        	
        	try {
				ip = InetAddress.getLocalHost();
			} catch (UnknownHostException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
        	while(!m.equals(ip.getHostAddress())){
        		m = JOptionPane.showInputDialog(hc,"Enter Host Ip Address");
			}
        	try {
				clientsocket=new Socket(m,port);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        	
        	     try {
        	    	dosclient= new ObjectOutputStream((clientsocket.getOutputStream()));
             		disclient= new ObjectInputStream((clientsocket.getInputStream()));
 					//dinclient=new DataInputStream(clientsocket.getInputStream());
 					//doutclient=new DataOutputStream(clientsocket.getOutputStream());
 				} catch (IOException e1) {
 					// TODO Auto-generated catch block
 					e1.printStackTrace();
 				}
        	System.out.println("Connection Established");
			hc.dispose();
			h.setVisible(true);
			user="client";
			System.out.println("Before Thread");
			Thread t = new Thread(new Runnable(){

				@Override
				public void run() {
					try {
						p1 = (Hero) disclient.readObject();
					} catch (ClassNotFoundException
							| IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Done Thread");
				}
				
			});
			t.start();
			tClient.start();
			
			System.out.println("Thread Started");
        	
        	
        }
        
		if(b.getActionCommand().equals("P1 is Hunter") ){
			System.out.println("Choosing Hero");
			//h.getSelected().setText("Start Game");
			h.getSelected().setActionCommand("Start Game");
			try {
				if(user.equals("server")){
					p1 = new Hunter();		
					h.setSelectedHero(b);
					try {
					dos.writeObject(p1);
				    dos.flush(); 
				    } catch (IOException e1) {
						e1.printStackTrace();
					}  
					
					System.out.println("Server Chose Hunter");
						System.out.println("Waiting for client");
						
						
				}
				else if(user.equals("client")){
					p2 = new Hunter();		
					h.setSelectedHero(b);
					try {
						//dosclient= new ObjectOutputStream(new BufferedOutputStream(clientsocket.getOutputStream()));
					dosclient.writeObject(p2);
				    dosclient.flush();
				    } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					System.out.println("Client Chose Hunter");
					System.out.println("Waiting for Server");
					
					
						
				}
																																									
					
			}catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
			
		}
		if(b.getActionCommand().equals("P1 is Mage") ){

			System.out.println("Choosing Hero");
			//h.getSelected().setText("Start Game");
			h.getSelected().setActionCommand("Start Game");
			try {
				if(user.equals("server")){
					p1 = new Mage();
					h.setSelectedHero(b);
					try {
						//dos= new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
					dos.writeObject(p1);
				    dos.flush(); 
				    } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}  
					
					System.out.println("Server Chose Mage");
					
					System.out.println("Waiting for client");
					
					
					
				}
				else if(user.equals("client")){
					p2 = new Mage();
					h.setSelectedHero(b);
					try {
						//dosclient= new ObjectOutputStream(new BufferedOutputStream(clientsocket.getOutputStream()));
					dosclient.writeObject(p2);
				    dosclient.flush();
				    } catch (IOException e1) {
						e1.printStackTrace();
					}
					
					System.out.println("Client Chose Mage");
					
					System.out.println("Waiting for Server");
					
					
						
				}
																																									
					
			}catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
			
		}
		if(b.getActionCommand().equals("P1 is Paladin") ){


			System.out.println("Choosing Hero");
			//h.getSelected().setText("Start Game");
			h.getSelected().setActionCommand("Start Game");
			try {
				if(user.equals("server")){
					p1 = new Paladin();
					h.setSelectedHero(b);
					try {
				    //dos= new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
					dos.writeObject(p1);
				    dos.flush(); 
				    } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}  
					
					System.out.println("Server Chose Paladin");
					

					System.out.println("Waiting for client");
					
					
					
				}
				else if(user.equals("client")){
					p2 = new Paladin();
					h.setSelectedHero(b);
					try {
						//dosclient= new ObjectOutputStream(new BufferedOutputStream(clientsocket.getOutputStream()));
					dosclient.writeObject(p2);
				    dosclient.flush();
				    } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					System.out.println("Client Chose Paladin");
					

					System.out.println("Waiting for Server");
					
					
						
				}
																																									
					
			}catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
			
		}
		if(b.getActionCommand().equals("P1 is Priest") ){
			
			System.out.println("Choosing Hero");
			//h.getSelected().setText("Start Game");
			h.getSelected().setActionCommand("Start Game");
			try {
				if(user.equals("server")){
					p1 = new Priest();
					h.setSelectedHero(b);
					try {
						//dos= new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
					dos.writeObject(p1);
				    dos.flush(); 
		
				    } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}  
					
					System.out.println("Server Chose Priest");
					

					System.out.println("Waiting for client");
					
				}
				else if(user.equals("client")){
					p2 = new Priest();
					h.setSelectedHero(b);
					try {
						//dosclient= new ObjectOutputStream(new BufferedOutputStream(clientsocket.getOutputStream()));
					dosclient.writeObject(p2);
				    dosclient.flush();
				    } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					System.out.println("Client Chose Priest");


					System.out.println("Waiting for Server");
					
					
						
				}
																																									
					
			}catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
			
		}
		if(b.getActionCommand().equals("P1 is Warlock") ){
			System.out.println("Choosing Hero");
			//h.getSelected().setText("Start Game");
			h.getSelected().setActionCommand("Start Game");
			try {
				if(user.equals("server")){
					p1 = new Warlock();
					h.setSelectedHero(b);
					try {
						//dos= new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
					dos.writeObject(p1);
				    dos.flush(); 
				    } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}  
					
					System.out.println("Server Chose Warlock");


					System.out.println("Waiting for client");
					
					
				}
				else if(user.equals("client")){
					p2 = new Warlock();
					h.setSelectedHero(b);
					try {
						//dosclient= new ObjectOutputStream(new BufferedOutputStream(clientsocket.getOutputStream()));
					dosclient.writeObject(p2);
				    dosclient.flush();
				    } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					System.out.println("Client Chose Warlock");
					System.out.println("Waiting for Server");
					
					
				}
																																									
					
			}catch (IOException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			}
		}
		if(b.getActionCommand().equals("Start Game")){
        		try {
    				if(user.equals("server")){
    					model = new Game(p1, p2);
    					try {
						dos= new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
					    dos.writeObject(model);
				        dos.flush(); 
				        System.out.println("I sent Game");
				        System.out.println("Current is " + model.getCurrentHero().getName() + " and Opponent is " + model.getOpponent().getName());
				        
				    } catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}  
    					if(model.getOpponent().equals(p1)){
    						Board.getHeroDownInfo().setText(model.getOpponent().toString());
            				Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
            				//
            				String s1 = model.getCurrentHero().getName();
            				String s2 = model.getOpponent().getName();
            				
            				String t1 = "HeroImages/"+s1+" 2.gif";
            		    	ImageIcon t= new ImageIcon(t1);
            		  		Image n1 = t.getImage();
            		  		Image n2 = n1.getScaledInstance(320, 370, Image.SCALE_DEFAULT);
            		  		t = new ImageIcon(n2);
            		  		
            		    	
            		    	Board.getOpponent().setIcon(t);
            		    	Board.getOpponent().setOpaque(false);
            		    	Board.getOpponent().setContentAreaFilled(false);
            		    	Board.getOpponent().setBorderPainted(false);
            				 
            		    	Board.getOpponent().setRolloverIcon(new ImageIcon(n2));
            		    	Board.getOpponent().setDisabledIcon(new ImageIcon(n2));
            		    	Board.getOpponent().setPressedIcon(new ImageIcon(n2));
            		    	Board.getOpponent().setSelectedIcon(new ImageIcon(n2));
            		    	
            		    	String v1 = "HeroImages/"+s2+" 2.gif";
            		    	ImageIcon v= new ImageIcon(v1);
            		  		Image b1 = v.getImage();
            		  		Image b2 = b1.getScaledInstance(320, 340, Image.SCALE_DEFAULT);
            		  		t = new ImageIcon(b2);
            		  		
            		    	
            		    	Board.getCurrent().setIcon(t);
            		    	Board.getCurrent().setOpaque(false);
            		    	Board.getCurrent().setContentAreaFilled(false);
            		    	Board.getCurrent().setBorderPainted(false);
            				 
            		    	Board.getCurrent().setRolloverIcon(new ImageIcon(b2));
            		    	Board.getCurrent().setDisabledIcon(new ImageIcon(b2));
            		    	Board.getCurrent().setPressedIcon(new ImageIcon(b2));
            		    	Board.getCurrent().setSelectedIcon(new ImageIcon(b2));
            				
            				ImageIcon s3= new ImageIcon("HeroPowerImages/"+s1+" Power.png");
            		  		Image s4 = s3.getImage();
            		  		Image ns1 = s4.getScaledInstance(250, 200, java.awt.Image.SCALE_SMOOTH);
            		  		s3 = new ImageIcon(ns1);
            				Board.getOpponentHeroPower().setIcon(s3);
            				Board.getOpponentHeroPower().setOpaque(false);
            				Board.getOpponentHeroPower().setContentAreaFilled(false);
            				Board.getOpponentHeroPower().setBorderPainted(false);
            				ImageIcon s5= new ImageIcon("HeroPowerImages/"+s2+" Power.png");
            		  		Image s6 = s5.getImage();
            		  		Image ns2 = s6.getScaledInstance(250, 200, java.awt.Image.SCALE_SMOOTH);
            		  		s5 = new ImageIcon(ns2);
            				Board.getCurrentHeroPower().setIcon(s5);
            				Board.getCurrentHeroPower().setOpaque(false);
            				Board.getCurrentHeroPower().setContentAreaFilled(false);
            				Board.getCurrentHeroPower().setBorderPainted(false);
            				
            				Who();
            				
            				model.getCurrentHero().listenToMinions();
            				model.getOpponent().listenToMinions();
            				System.out.println("Getting ready");
            				updateMinionStats();
            				System.out.println("Done Building");
    					}
    					else if(model.getCurrentHero().equals(p1)){
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
            				System.out.println("Getting ready");
            				updateMinionStats();
            				System.out.println("Done Building");
    					}
    					//dos= new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
					   // dos.writeObject(model);
				       // dos.flush();
        				}
    				
    				    if(user.equals("client")){
    				   // disclient2=new ObjectInputStream(new BufferedInputStream(clientsocket.getInputStream()));
    				   // dosclient2=new ObjectOutputStream(new BufferedOutputStream(clientsocket.getOutputStream()));
    					disclient=new ObjectInputStream(new BufferedInputStream(clientsocket.getInputStream()));
						model = (Game) disclient.readObject();
						System.out.println("I recieved Game");
						System.out.println("Current is " + model.getCurrentHero().getName() + " and Opponent is " + model.getOpponent().getName());
						System.out.println("p1 is "+p1.getName()+"and p2 is "+p2.getName());
						if(model.getOpponent().getName().equals(p2.getName())){
							System.out.println("Gedd");
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
            				System.out.println("Getting ready");
            				updateMinionStats();
            				System.out.println("Done Building");
						}
						else if(model.getCurrentHero().getName().equals(p2.getName())){
							System.out.println("Gedd");
							Board.getHeroDownInfo().setText(model.getOpponent().toString());
            				Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
            				//
            				String s1 = model.getCurrentHero().getName();
            				String s2 = model.getOpponent().getName();
            				
            				String t1 = "HeroImages/"+s1+" 2.gif";
            		    	ImageIcon t= new ImageIcon(t1);
            		  		Image n1 = t.getImage();
            		  		Image n2 = n1.getScaledInstance(320, 370, Image.SCALE_DEFAULT);
            		  		t = new ImageIcon(n2);
            		  		
            		    	
            		    	Board.getOpponent().setIcon(t);
            		    	Board.getOpponent().setOpaque(false);
            		    	Board.getOpponent().setContentAreaFilled(false);
            		    	Board.getOpponent().setBorderPainted(false);
            				 
            		    	Board.getOpponent().setRolloverIcon(new ImageIcon(n2));
            		    	Board.getOpponent().setDisabledIcon(new ImageIcon(n2));
            		    	Board.getOpponent().setPressedIcon(new ImageIcon(n2));
            		    	Board.getOpponent().setSelectedIcon(new ImageIcon(n2));
            		    	
            		    	String v1 = "HeroImages/"+s2+" 2.gif";
            		    	ImageIcon v= new ImageIcon(v1);
            		  		Image b1 = v.getImage();
            		  		Image b2 = b1.getScaledInstance(320, 340, Image.SCALE_DEFAULT);
            		  		t = new ImageIcon(b2);
            		  		
            		    	
            		    	Board.getCurrent().setIcon(t);
            		    	Board.getCurrent().setOpaque(false);
            		    	Board.getCurrent().setContentAreaFilled(false);
            		    	Board.getCurrent().setBorderPainted(false);
            				 
            		    	Board.getCurrent().setRolloverIcon(new ImageIcon(b2));
            		    	Board.getCurrent().setDisabledIcon(new ImageIcon(b2));
            		    	Board.getCurrent().setPressedIcon(new ImageIcon(b2));
            		    	Board.getCurrent().setSelectedIcon(new ImageIcon(b2));
            				
            				ImageIcon s3= new ImageIcon("HeroPowerImages/"+s1+" Power.png");
            		  		Image s4 = s3.getImage();
            		  		Image ns1 = s4.getScaledInstance(250, 200, java.awt.Image.SCALE_SMOOTH);
            		  		s3 = new ImageIcon(ns1);
            				Board.getOpponentHeroPower().setIcon(s3);
            				Board.getOpponentHeroPower().setOpaque(false);
            				Board.getOpponentHeroPower().setContentAreaFilled(false);
            				Board.getOpponentHeroPower().setBorderPainted(false);
            				ImageIcon s5= new ImageIcon("HeroPowerImages/"+s2+" Power.png");
            		  		Image s6 = s5.getImage();
            		  		Image ns2 = s6.getScaledInstance(250, 200, java.awt.Image.SCALE_SMOOTH);
            		  		s5 = new ImageIcon(ns2);
            				Board.getCurrentHeroPower().setIcon(s5);
            				Board.getCurrentHeroPower().setOpaque(false);
            				Board.getCurrentHeroPower().setContentAreaFilled(false);
            				Board.getCurrentHeroPower().setBorderPainted(false);
            				
            				Who();
            				
            				model.getCurrentHero().listenToMinions();
            				model.getOpponent().listenToMinions();
            				System.out.println("Getting ready");
            				updateMinionStats();
            				System.out.println("Done Building");
						}
    				}
    				    
    			} catch (FullHandException | CloneNotSupportedException | IOException | ClassNotFoundException e1) {
    				JOptionPane.showMessageDialog(Board, e1.getMessage());
    			}
        		
        		System.out.println("before thread");
        		Thread t = new Thread(new Runnable(){
        			public void run(){
        				int i = 0;
        				while(true){
        					System.out.println(i);
        					try {
								updateTurn();
							} catch (FullHandException
									| CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NotYourTurnException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NotEnoughManaException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (FullFieldException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (HeroPowerAlreadyUsedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvalidTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CannotAttackException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (TauntBypassException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NotSummonedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
        					System.out.println("Current is " + model.getCurrentHero().getName() + " and Opponent is " + model.getOpponent().getName());
        					updateMinionStats();
        					Who();
        					i++;
        				}
        				
        			}
        		});
        		t.start();
        		
			updateMinionStats();
			Board.revalidate();
			Board.repaint();
			h.dispose();
		    Board.setVisible(true);
		} 
		
		if(b.getActionCommand().equals("End Turn")){
				try {
					if(user.equals("server")){	
						if(model.getCurrentHero().getName().equals(p1.getName())){
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
					//c=1;
					if(model.getCurrentHero().fieldContains("Chromaggus")&&model.getCurrentHero().getField().size()<8){
						updateMinionStats();
					}
						updateMinionStats();  
					Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
					Board.getHeroDownInfo().setText(model.getOpponent().toString());
					
					Who();
					}
					try {
						SendAction("End Turn");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						}
						else if(model.getOpponent().getName().equals(p1.getName())){
							JOptionPane.showMessageDialog(Board,"Not Your Turn");
						}
			
					} 
					else if(user.equals("client"))
					{
						if(model.getCurrentHero().getName().equals(p2.getName())){
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
							//c=0;
							if(model.getCurrentHero().fieldContains("Chromaggus")&&model.getCurrentHero().getField().size()<8){
								updateMinionStats();
							}
								updateMinionStats();
							Board.getHeroUpInfo().setText(model.getOpponent().toString());
							Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
							
							Who();
						}
							try {
								SendAction("End Turn");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
						else if(model.getOpponent().getName().equals(p2.getName())){
							JOptionPane.showMessageDialog(Board,"Not Your Turn");
						}
					}
					
					Temp = new JButton();
					Board.revalidate();
					Board.repaint();
				} catch (FullHandException | CloneNotSupportedException e1) {
					JOptionPane.showMessageDialog(Board, model.getCurrentHero().getDeck().get(0), "Hand is full here is burnt card", JOptionPane.PLAIN_MESSAGE);
					if(user.equals("server")){
						if(model.getOpponent().getName().equals(p1.getName())){
							Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
							Board.getHeroDownInfo().setText(model.getOpponent().toString());
							//c=1;
							Who();
						}
						else if (model.getCurrentHero().getName().equals(p1.getName())){
							Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
						    Board.getHeroUpInfo().setText(model.getOpponent().toString());
						    //c=1;
						    Who();
						}
						
					}
					else if(user.equals("client")){
						if(model.getOpponent().getName().equals(p2.getName())){
							Board.getHeroUpInfo().setText(model.getOpponent().toString());
							Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
							//c=1;
							Who();
						}
						else if (model.getCurrentHero().getName().equals(p2.getName())){
							Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
						    Board.getHeroUpInfo().setText(model.getOpponent().toString());
						    //c=1;
						    Who();
						}
					}
				}	
				
		}
		/////TODO
		if(Board.getCurrentHandCards().contains(b)){
			int index=Board.getCurrentHandCards().indexOf(b);
		    c1 = model.getCurrentHero().getHand().get(index);
			model.getCurrentHero().listenToMinions();
			if(user.equals("server")){
				
				if(model.getCurrentHero().getName().equals(p1.getName())){
					
					if(c1 instanceof Minion){
				try{
					if(user.equals("server")){
						if(model.getCurrentHero().getName().equals(p1.getName())){
							
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
					SendAction("Play Minion");
					dos.writeObject(c1);
			        dos.flush();
					Who();
					Board.revalidate();
					Board.repaint();
						}
						else if(model.getOpponent().getName().equals(p1.getName())){
							model.getOpponent().playMinion((Minion) c1);
						}
					
					}
					else if(user.equals("client")){
						if(model.getCurrentHero().getName().equals(p2.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Cards`");
						}
						else if(model.getOpponent().getName().equals(p2.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
						
					}
				} catch (NotYourTurnException | NotEnoughManaException
						| FullFieldException e1) {
					JOptionPane.showMessageDialog(Board, e1.getMessage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateMinionStats();
			}
		if(c1 instanceof Spell){
				if(c1 instanceof FieldSpell){
					try {
						if(user.equals("server")){
							if(model.getCurrentHero().getName().equals(p1.getName())){
								model.getCurrentHero().castSpell((FieldSpell) c1);
						        updateMinionStats();
						        SendAction("Play Field Spell");
						        dos.writeObject(c1);
						        dos.flush();
							}
							else if(model.getOpponent().getName().equals(p1.getName())){
								model.getOpponent().castSpell((FieldSpell) c1);
							}
						
						}
						else if(user.equals("client")){
							if(model.getCurrentHero().getName().equals(p2.getName())){
								JOptionPane.showMessageDialog(Board, "Not Your Cards`");
							}
							else if(model.getOpponent().getName().equals(p2.getName())){
								JOptionPane.showMessageDialog(Board, "Not Your Turn");
							}
							
						}
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				if(c1 instanceof AOESpell){
					try {
						if(user.equals("server")){
							if(model.getCurrentHero().getName().equals(p1.getName())){
							model.getCurrentHero().castSpell((AOESpell) c1,model.getOpponent().getField());
							updateMinionStats();
					        SendAction("Play Aoe Spell");
					        dos.writeObject(c1);
					        dos.flush();
						}
						else if(model.getOpponent().getName().equals(p1.getName())){
							model.getOpponent().castSpell((AOESpell) c1,model.getOpponent().getField());
						}
						}
						else if(user.equals("client")){
							if(model.getCurrentHero().getName().equals(p2.getName())){
								JOptionPane.showMessageDialog(Board, "Not Your Cards`");
							}
							else if(model.getOpponent().getName().equals(p2.getName())){
								JOptionPane.showMessageDialog(Board, "Not Your Turn");
							}
							
						}
						
						
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if(c1 instanceof MinionTargetSpell){
					if(user.equals("server")){
						if(model.getCurrentHero().getName().equals(p1.getName())){
							Temp = b;
					Temp.setActionCommand("MinionTargetSpell");
					Spell = (Spell) c1;
					try {
						SendAction("Chose MT Spell");
						dos.writeObject(c1);
			        dos.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
						}
						else if(model.getOpponent().getName().equals(p1.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
					}
					else if(user.equals("client")){
						if(model.getCurrentHero().getName().equals(p2.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Cards`");
						}
						else if(model.getOpponent().getName().equals(p2.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
						
					}
					
				}
				if(c1 instanceof HeroTargetSpell){

					if(user.equals("server")){
						if(model.getCurrentHero().getName().equals(p1.getName())){
							Temp = b;
							Temp.setActionCommand("HeroTargetSpell");
							Spell = (Spell) c1;
					try {
						SendAction("Chose HT Spell");
						dos.writeObject(c1);
			        dos.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
						}
						else if(model.getOpponent().getName().equals(p1.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
					}
					else if(user.equals("client")){
						if(model.getCurrentHero().getName().equals(p2.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Cards`");
						}
						else if(model.getOpponent().getName().equals(p2.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
						
					}
					
				}
				if(c1 instanceof LeechingSpell){
					
					if(user.equals("server")){
						if(model.getCurrentHero().getName().equals(p1.getName())){
							Temp=b;
							 Temp.setActionCommand("LeechingSpell");
							 Spell = (Spell) c1;
					try {
						SendAction("Chose LS Spell");
						dos.writeObject(c1);
			        dos.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
						}
						else if(model.getOpponent().getName().equals(p1.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
					}
					else if(user.equals("client")){
						if(model.getCurrentHero().getName().equals(p2.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Cards`");
						}
						else if(model.getOpponent().getName().equals(p2.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
						
					}
					
				}
			}
		Who();
			updateMinionStats();
			Board.revalidate();
			Board.repaint();
				}
				else if(model.getOpponent().getName().equals(p1.getName())){
					JOptionPane.showMessageDialog(Board, "Not Your Turn");
				}
				
			}
			else if(user.equals("client")){
				if(model.getCurrentHero().getName().equals(p2.getName())){
					JOptionPane.showMessageDialog(Board, "Not Your Cards`");
				}
				else if(model.getOpponent().getName().equals(p2.getName())){
					JOptionPane.showMessageDialog(Board, "Not Your Turn");
				}
				
			}
			
		}
		if(Board.getOpponentHandCards().contains(b)){
			int index=Board.getOpponentHandCards().indexOf(b);
		    c1 = model.getCurrentHero().getHand().get(index);
			model.getCurrentHero().listenToMinions();
			if(user.equals("client")){
				
				if(model.getCurrentHero().getName().equals(p2.getName())){
					
					if(c1 instanceof Minion){
				try{
					if(user.equals("client")){
						if(model.getCurrentHero().getName().equals(p2.getName())){
							
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
					SendAction("Play Minion");
					dosclient.writeObject(c1);
			        dosclient.flush();
					Who();
					Board.revalidate();
					Board.repaint();
						}
						else if(model.getOpponent().getName().equals(p2.getName())){
							model.getOpponent().playMinion((Minion) c1);
						}
					
					}
					else if(user.equals("server")){
						if(model.getCurrentHero().getName().equals(p1.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Cards`");
						}
						else if(model.getOpponent().getName().equals(p1.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
					}
				} catch (NotYourTurnException | NotEnoughManaException
						| FullFieldException e1) {
					JOptionPane.showMessageDialog(Board, e1.getMessage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateMinionStats();
			}
		if(c1 instanceof Spell){
				if(c1 instanceof FieldSpell){
					try {
						if(user.equals("client")){
							if(model.getCurrentHero().getName().equals(p2.getName())){
								model.getCurrentHero().castSpell((FieldSpell) c1);
						        updateMinionStats();
						        SendAction("Play Field Spell");
						        dosclient.writeObject(c1);
						        dosclient.flush();
							}
							else if(model.getOpponent().getName().equals(p2.getName())){
								model.getOpponent().castSpell((FieldSpell) c1);
							}
						
						}
						else if(user.equals("server")){
							if(model.getCurrentHero().getName().equals(p1.getName())){
								JOptionPane.showMessageDialog(Board, "Not Your Cards`");
							}
							else if(model.getOpponent().getName().equals(p1.getName())){
								JOptionPane.showMessageDialog(Board, "Not Your Turn");
							}
						}
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				if(c1 instanceof AOESpell){
					try {
						if(user.equals("client")){
							if(model.getCurrentHero().getName().equals(p2.getName())){
							model.getCurrentHero().castSpell((AOESpell) c1,model.getOpponent().getField());
							updateMinionStats();
					        SendAction("Play Aoe Spell");
					        dosclient.writeObject(c1);
					        dosclient.flush();
						}
						else if(model.getOpponent().getName().equals(p2.getName())){
							model.getOpponent().castSpell((AOESpell) c1,model.getOpponent().getField());
						}
						}
						else if(user.equals("server")){
							if(model.getCurrentHero().getName().equals(p1.getName())){
								JOptionPane.showMessageDialog(Board, "Not Your Cards`");
							}
							else if(model.getOpponent().getName().equals(p1.getName())){
								JOptionPane.showMessageDialog(Board, "Not Your Turn");
							}
						}
						
						
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if(c1 instanceof MinionTargetSpell){
					if(user.equals("client")){
						if(model.getCurrentHero().getName().equals(p2.getName())){
							Temp = b;
					Temp.setActionCommand("MinionTargetSpell");
					Spell = (Spell) c1;
					try {
						SendAction("Chose MT Spell");
						dosclient.writeObject(c1);
			        dosclient.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
						}
						else if(model.getOpponent().getName().equals(p2.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
					}
					else if(user.equals("server")){
						if(model.getCurrentHero().getName().equals(p1.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Cards`");
						}
						else if(model.getOpponent().getName().equals(p1.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
					}
					
				}
				if(c1 instanceof HeroTargetSpell){

					if(user.equals("client")){
						if(model.getCurrentHero().getName().equals(p2.getName())){
							Temp = b;
							Temp.setActionCommand("HeroTargetSpell");
							Spell = (Spell) c1;
					try {
						SendAction("Chose HT Spell");
						dosclient.writeObject(c1);
			        dosclient.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
						}
						else if(model.getOpponent().getName().equals(p2.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
					}
					else if(user.equals("server")){
						if(model.getCurrentHero().getName().equals(p1.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Cards`");
						}
						else if(model.getOpponent().getName().equals(p1.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
					}
					
				}
				if(c1 instanceof LeechingSpell){
					
					if(user.equals("client")){
						if(model.getCurrentHero().getName().equals(p2.getName())){
							Temp=b;
							 Temp.setActionCommand("LeechingSpell");
							 Spell = (Spell) c1;
					try {
						SendAction("Chose LS Spell");
						dosclient.writeObject(c1);
			        dosclient.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
						}
						else if(model.getOpponent().getName().equals(p2.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
					}
					else if(user.equals("server")){
						if(model.getCurrentHero().getName().equals(p1.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Cards`");
						}
						else if(model.getOpponent().getName().equals(p1.getName())){
							JOptionPane.showMessageDialog(Board, "Not Your Turn");
						}
					}
					
				}
			}
		Who();
			updateMinionStats();
			Board.revalidate();
			Board.repaint();
				}
				else if(model.getOpponent().getName().equals(p2.getName())){
					JOptionPane.showMessageDialog(Board, "Not Your Turn");
				}
				
			}
			else if(user.equals("server")){
				if(model.getCurrentHero().getName().equals(p1.getName())){
					JOptionPane.showMessageDialog(Board, "Not Your Cards`");
				}
				else if(model.getOpponent().getName().equals(p1.getName())){
					JOptionPane.showMessageDialog(Board, "Not Your Turn");
				}
				
			}
			
		}
		if(b.getActionCommand().equals("Current Hero Power")){
			 try {
				 if(user.equals("server")){
					 if(model.getCurrentHero().getName().equals(p1.getName())){
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
							SendAction("Hunter Hero Power");
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
								SendAction("Warlock Hero Power");
			    			}
							if(model.getCurrentHero() instanceof Paladin){
								Paladin p = (Paladin) model.getCurrentHero();
								p.useHeroPower();
								Board.getHeroUpInfo().setText(model.getOpponent().toString());
							    Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
							    updateMinionStats();
								model.getCurrentHero().listenToMinions();
								model.getOpponent().listenToMinions();
								SendAction("Paladin Hero Power");
							}
						 }
						 else if(model.getOpponent().getName().equals(p1.getName())){
							  model.getOpponent().useHeroPower();
						 }
				 }
				 
				 else if(user.equals("client")){
					 JOptionPane.showMessageDialog(Board, "Not Your Power");
				 }
			 
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 updateMinionStats();
			 Board.revalidate();
			 Board.repaint(); 
		}
        if(b.getActionCommand().equals("Opponent Hero Power")){
        	try {
        		if(user.equals("client")){
        			if(model.getCurrentHero().getName().equals(p2.getName())){
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
            				SendAction("Hunter Hero Power");
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
        					SendAction("Warlock Hero Power");
        				    
            			}
            			if(model.getCurrentHero() instanceof Paladin){
        					Paladin p = (Paladin) model.getCurrentHero();
        					p.useHeroPower();
        					Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
        				    Board.getHeroDownInfo().setText(model.getOpponent().toString());
        				    updateMinionStats();
        					model.getCurrentHero().listenToMinions();
        					model.getOpponent().listenToMinions();
        					SendAction("Paladin Hero Power");
        				}
            			
        			}
        			 else if(model.getOpponent().getName().equals(p2.getName())){
						  model.getOpponent().useHeroPower();
					 }
        			
        		}
        		
        		
        		else if(user.equals("server"))
        			 JOptionPane.showMessageDialog(Board, "Not Your Power");
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(Board, e1.getMessage());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
        		

        		try {
        			if(user.equals("server")){
        				s.close();
        				pause.dispose();
        		Board.dispose();
        		winner.dispose();
        				
        			}
        			else if(user.equals("client")){
        				clientsocket.close();
        				pause.dispose();
                		Board.dispose();
                		winner.dispose();
        			}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		
        		new GameView();
        	}
        	
        }
        if(compC.contains(b)){
        	if(user.equals("server")){
        		if(model.getCurrentHero().getName().equals(p1.getName())){
        			if(Temp.getActionCommand().equals("Current Hero Power") && model.getCurrentHero() instanceof Priest){
        			int index=compC.indexOf(b);
        			Minion c1 = model.getCurrentHero().getField().get(index);
        			Priest m = (Priest) model.getCurrentHero();
					try {
						m.useHeroPower(c1);
						updateMinionStats();
						SendAction("Minion Priest Power");
						dos.writeObject(index);
						dos.flush();
             		    Board.revalidate();
         			    Board.repaint();
					} catch (NotEnoughManaException
							| HeroPowerAlreadyUsedException
							| NotYourTurnException | FullHandException
							| FullFieldException | CloneNotSupportedException e1) {
						    JOptionPane.showMessageDialog(Board, e1.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        		}
        		if(Temp.getActionCommand().equals("MinionTargetSpell")){
            		int index=compO.indexOf(b);
        			Minion c1 = model.getOpponent().getField().get(index);
        			try {
						model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
						Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
				    Board.getHeroUpInfo().setText(model.getOpponent().toString());
				    SendAction("Play MT Spell");
					dos.writeObject(index);
					dos.flush();
				    
        			updateMinionStats();
        			Board.revalidate();
        			Board.repaint();
					} catch (NotYourTurnException | NotEnoughManaException
							| InvalidTargetException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
            	}
        		else{
            	Temp = b;
            	System.out.println("Chose Minion Reached");
        		}
            }
        		else if(model.getOpponent().getName().equals(p1.getName())){
        			JOptionPane.showMessageDialog(Board, "Not Your Turn");
        		}
        		
        		}
        		
            else if(user.equals("client")){
            	if(model.getCurrentHero().getName().equals(p2.getName())){
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
           					   SendAction("Minion Mage Power");
           					   dosclient.writeObject(index);
           					   dosclient.flush();
           					   
                    		    Board.revalidate();
                			    Board.repaint(); 
    					} catch (NotEnoughManaException
    							| HeroPowerAlreadyUsedException
    							| NotYourTurnException | FullHandException
    							| FullFieldException
    							| CloneNotSupportedException e1) {
    						JOptionPane.showMessageDialog(Board, e1.getMessage());
    					} catch (IOException e1) {
							e1.printStackTrace();
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
    					SendAction("Play MT Spell");
    					dosclient.writeObject(index);
    					dosclient.flush();
    				    
            			updateMinionStats();
            			Board.revalidate();
            			Board.repaint();
    					} catch (NotYourTurnException | NotEnoughManaException
    							| InvalidTargetException e1) {
    						JOptionPane.showMessageDialog(Board, e1.getMessage());
    					} catch (IOException e1) {
							e1.printStackTrace();
						}
                	}
                	
                	
                	if(Temp.getActionCommand().equals("LeechingSpell")){
                		int index=compC.indexOf(b);
            			Minion c1 = model.getOpponent().getField().get(index);
            			try {
    						model.getCurrentHero().castSpell((LeechingSpell)Spell, c1);
    						SendAction("Play LS Spell");
    						dosclient.writeObject(index);
        					dosclient.flush();
    						updateMinionStats();
            			    Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
    					} catch (NotYourTurnException | NotEnoughManaException e1) {
    						JOptionPane.showMessageDialog(Board, e1.getMessage());
    					} catch (IOException e1) {
							e1.printStackTrace();
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
                					if(c1.getAttack()>=c2.getCurrentHP()){
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
                					if(c2.getAttack()>=c1.getCurrentHP()){
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
                			if(c1.getAttack()>=c2.getCurrentHP()){
                				if(c2.getAttack()>=c1.getCurrentHP()){
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
                				if(c2.getAttack()>=c1.getCurrentHP()){
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
                			SendAction("Attack With Minion");
                			dosclient.writeObject(index);
                			dosclient.flush();
                			dosclient.writeObject(index2);
                			dosclient.flush();
                			
        				} catch (CannotAttackException | NotYourTurnException
        						| TauntBypassException | InvalidTargetException
        						| NotSummonedException e1) {
        					JOptionPane.showMessageDialog(Board, e1.getMessage());
        				} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                      }
            	}
            	else if(model.getOpponent().getName().equals(p2.getName())){
            		JOptionPane.showMessageDialog(Board, "Not Your Turn");
            	}
            	
            		 updateMinionStats();
            		 Board.revalidate();
        			 Board.repaint(); 
            	}
   		 Board.revalidate();
	    Board.repaint();
        }
        if(compO.contains(b)){
        	if(user.equals("client")){
            	if(model.getCurrentHero().getName().equals(p2.getName())){
            		if(Temp.getActionCommand().equals("Current Hero Power") && model.getCurrentHero() instanceof Priest){
        			int index=compO.indexOf(b);
        			Minion c1 = model.getCurrentHero().getField().get(index);
        			Priest pr = (Priest) model.getCurrentHero();
					try {
						pr.useHeroPower(c1);
						updateMinionStats();
						SendAction("Minion Priest Power");
						dosclient.writeObject(index);
						dosclient.flush();
						
             		    Board.revalidate();
         			    Board.repaint();
					} catch (NotEnoughManaException
							| HeroPowerAlreadyUsedException
							| NotYourTurnException | FullHandException
							| FullFieldException | CloneNotSupportedException e1) {
						    JOptionPane.showMessageDialog(Board, e1.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        		}
        		if(Temp.getActionCommand().equals("MinionTargetSpell")){
            		int index=compO.indexOf(b);
        			Minion c1 = model.getOpponent().getField().get(index);
        			try {
						model.getCurrentHero().castSpell((MinionTargetSpell)Spell, c1);
						Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
				    Board.getHeroDownInfo().setText(model.getOpponent().toString());
				    SendAction("Play MT Spell");
				    dosclient.writeObject(index);
					dosclient.flush();
				    
        			updateMinionStats();
        			Board.revalidate();
        			Board.repaint();
					} catch (NotYourTurnException | NotEnoughManaException
							| InvalidTargetException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
            	}
        		else{
            	Temp = b;
            	System.out.println("Chose Minion Reached");
        		}
            	}
            	else if(model.getOpponent().getName().equals(p2.getName())){
            		JOptionPane.showMessageDialog(Board, "Not Your Turn");
            	}
        		
            	}
            else if(user.equals("server")){
            	if(model.getCurrentHero().getName().equals(p1.getName())){
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
        					   SendAction("Minion Mage Power");
        					   dos.writeObject(index2);
           					   dos.flush();
           					   
                    		    Board.revalidate();
                			    Board.repaint(); 
						} catch (NotEnoughManaException
								| HeroPowerAlreadyUsedException
								| NotYourTurnException | FullHandException
								| FullFieldException
								| CloneNotSupportedException e1) {
							JOptionPane.showMessageDialog(Board, e1.getMessage());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
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
     					    SendAction("Play MT Spell");
     					    dos.writeObject(index);
       					    dos.flush();
       				    
                			updateMinionStats();
                			Board.revalidate();
                			Board.repaint();
        					} catch (NotYourTurnException | NotEnoughManaException
        							| InvalidTargetException e1) {
        						JOptionPane.showMessageDialog(Board, e1.getMessage());
        					} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
        					
                    	}
                	if(Temp.getActionCommand().equals("LeechingSpell")){
                		int index=compO.indexOf(b);
            			Minion c1 = model.getOpponent().getField().get(index);
            			try {
    						model.getCurrentHero().castSpell((LeechingSpell)Spell, c1);
    						SendAction("Play LS Spell");
    						dos.writeObject(index);
        					dos.flush();
        				    
            			    Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
            			    updateMinionStats();
    					} catch (NotYourTurnException | NotEnoughManaException e1) {
    						JOptionPane.showMessageDialog(Board, e1.getMessage());
    					} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
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
            					if(c1.getAttack()>=c2.getCurrentHP()){
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
            					if(c2.getAttack()>=c1.getCurrentHP()){
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
            			    if(c1.getAttack()>=c2.getCurrentHP()){
            			    	if(c2.getAttack()>=c1.getCurrentHP()){
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
                				if(c2.getAttack()>=c1.getCurrentHP()){
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
            			SendAction("Attack With Minion");
            			dos.writeObject(index);
            			dos.flush();
            			dos.writeObject(index2);
            			dos.flush();
            			
    				} catch (CannotAttackException | NotYourTurnException
    						| TauntBypassException | InvalidTargetException
    						| NotSummonedException e1) {
    					JOptionPane.showMessageDialog(Board, e1.getMessage());
    				} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            		
            		}
            		updateMinionStats();
           		    Board.revalidate();
       			    Board.repaint();
            	
            	}
            	else if(model.getOpponent().getName().equals(p1.getName())){
            		JOptionPane.showMessageDialog(Board, "Not Your Turn");
            	}
            		}
   		     Board.revalidate();
			 Board.repaint();
        }
        if(b.getActionCommand().equals("Attack Down")){
        	if(user.equals("client")){
        		if(model.getCurrentHero().getName().equals(p2.getName())){
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
        				try {
						SendAction("Mage Hero Power");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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
        				SendAction("Play HT Spell");
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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
					System.out.println("Before Attacking Hero");
					SendAction("Attack Hero With Minion");
					dosclient.writeObject(c1);
					dosclient.flush();
        			System.out.println("After Attacking Hero");
        		    Board.revalidate();
    			    Board.repaint();
        		}
        			
        			
				} catch (CannotAttackException | NotYourTurnException
						| TauntBypassException | InvalidTargetException
						| NotSummonedException e1) {
					JOptionPane.showMessageDialog(Board, e1.getMessage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        		}
        		else if(model.getOpponent().getName().equals(p2.getName())){
        			JOptionPane.showMessageDialog(Board, "Not Your Turn");
        		}
        			
        	}
        	
        	
        	else if(user.equals("server")){
        		if(model.getCurrentHero().getName().equals(p1.getName())){
        			if(Temp.getActionCommand().equals("Current Hero Power") && model.getCurrentHero() instanceof Priest){
				try {
					Priest pr = (Priest) model.getCurrentHero();
					pr.useHeroPower(model.getCurrentHero());
					Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
					Board.getHeroUpInfo().setText(model.getOpponent().toString());
         		    Board.revalidate();
     			    Board.repaint();
     			    SendAction("Priest Hero Power");
				} catch (NotEnoughManaException
						| HeroPowerAlreadyUsedException
						| NotYourTurnException | FullHandException
						| FullFieldException | CloneNotSupportedException e1) {
					    JOptionPane.showMessageDialog(Board, e1.getMessage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		}
        		}
        		else if(model.getOpponent().getName().equals(p1.getName())){
        			JOptionPane.showMessageDialog(Board, "Not Your Turn");
        		}
        	
        	}
       		    Board.revalidate();
   			    Board.repaint();
        }
        if(b.getActionCommand().equals("Attack Upper")){
        	if(user.equals("server")){
        		if(model.getCurrentHero().getName().equals(p1.getName())){
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
    			try {
					SendAction("Mage Hero Power");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
        				SendAction("Play HT Spell");
					} catch (NotYourTurnException | NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(Board, e1.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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
					System.out.println("Before Attacking Hero");
        			SendAction("Attack Hero With Minion");
        			dos.writeObject(c1);
					dos.flush();
        			System.out.println("After Attacking Hero");
        		}
        		    Board.revalidate();
    			    Board.repaint(); 
    			    
        			
				} catch (CannotAttackException | NotYourTurnException
						| TauntBypassException | InvalidTargetException
						| NotSummonedException e1) {
					JOptionPane.showMessageDialog(Board, e1.getMessage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        //		updateMinionStats();
       		    Board.revalidate();
   			    Board.repaint();
        	}
        	
        		}
        		else if(model.getOpponent().getName().equals(p1.getName())){
        			JOptionPane.showMessageDialog(Board, "Not Your Turn");
        		}
        		}
        	else if(user.equals("client")){
        		if(model.getCurrentHero().getName().equals(p2.getName())){
        			if(Temp.getActionCommand().equals("Current Hero Power") && model.getCurrentHero() instanceof Priest){
					try {
						Priest pr = (Priest) model.getCurrentHero();
						pr.useHeroPower(model.getCurrentHero());
						Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
						Board.getHeroDownInfo().setText(model.getOpponent().toString());
             		    Board.revalidate();
         			    Board.repaint();
         			    SendAction("Priest Hero Power");
					} catch (NotEnoughManaException
							| HeroPowerAlreadyUsedException
							| NotYourTurnException | FullHandException
							| FullFieldException | CloneNotSupportedException e1) {
						    JOptionPane.showMessageDialog(Board, e1.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        		}
        		}
        		else if(model.getOpponent().getName().equals(p2.getName())){
        			JOptionPane.showMessageDialog(Board, "Not Your Turn");
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
	
	public void Who(){//change color
		if(user.equals("server")){
			if(model.getOpponent().getName().equals(p1.getName())){
				Board.getUp().setForeground(Color.GREEN);
			    Board.getDown().setForeground(Color.WHITE);
				Board.getUp().setText("Opp(Cur)");
				Board.getDown().setText("You");
				Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
				Board.getHeroDownInfo().setText(model.getOpponent().toString());
			}
			else if(model.getCurrentHero().getName().equals(p1.getName())){
				Board.getUp().setForeground(Color.WHITE);
			    Board.getDown().setForeground(Color.GREEN);
			    Board.getUp().setText("Opp");
			    Board.getDown().setText("You(Cur)");
			    Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
				Board.getHeroUpInfo().setText(model.getOpponent().toString());
			}
			
		}
		else if(user.equals("client")){
			if(model.getOpponent().getName().equals(p2.getName())){
				Board.getUp().setForeground(Color.WHITE);
			    Board.getDown().setForeground(Color.GREEN);
			    Board.getUp().setText("You");
			    Board.getDown().setText("Opp(Cur)");
			    Board.getHeroDownInfo().setText(model.getCurrentHero().toString());
				Board.getHeroUpInfo().setText(model.getOpponent().toString());
			}
			else if(model.getCurrentHero().getName().equals(p2.getName())){
				Board.getUp().setForeground(Color.GREEN);
			    Board.getDown().setForeground(Color.WHITE);
			    Board.getUp().setText("You(Curr)");
			    Board.getDown().setText("Opp");
			    Board.getHeroUpInfo().setText(model.getCurrentHero().toString());
				Board.getHeroDownInfo().setText(model.getOpponent().toString());
			}
			
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
			Minion t=null;
			if(user.equals("server")){
				if(model.getOpponent().getName().equals(p1.getName())){
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
						Board.getOpponentHandPanel().add(j);
						Board.getOpponentHandCards().add(j);
							
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
						Board.getOpponentFieldPanel().add(j);
						compO.add(j);
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
						Board.getCurrentFieldPanel().add(j);
						compC.add(j);
						
				    }
				}
				else if(model.getCurrentHero().getName().equals(p1.getName())){
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
		    
	}
			else if(user.equals("client")){
				if(model.getOpponent().getName().equals(p2.getName())){
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
				else if(model.getCurrentHero().getName().equals(p2.getName())){
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
				
			}
		
		Board.revalidate();
	    Board.repaint(); 
	}
	public static void main(String[] args) throws FullHandException, CloneNotSupportedException, UnknownHostException, IOException {
		new SocketController();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
   @Override
   public void mouseEntered(MouseEvent e) {
			JButton button = (JButton)e.getSource();
			String name=button.getText();
			if(user.equals("server")){
				if(Board.getCurrentHandCards().contains(button)){
					if(model.getCurrentHero().getName().equals(p1.getName()))
					{
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
					else if(model.getOpponent().getName().equals(p1.getName())){{
			int index=Board.getCurrentHandCards().indexOf(button);
			Card c1 = model.getOpponent().getHand().get(index);
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
				}
		}
			
		    if(compC.contains(button)){
				int index=compC.indexOf(button);
				if(user.equals("server")){
					if(model.getCurrentHero().getName().equals(p1.getName())){
						Card c1 = model.getCurrentHero().getField().get(index);
					if(c1 instanceof Minion){
					name=((Minion)c1).getName();
					String x = "MinionImages/"+name+".png";
					Board.getShowCard().setIcon(new ImageIcon(x));
				    Board.getCardInfo().setText(((Minion)c1).toString());
					}
					}
					else if(model.getOpponent().getName().equals(p1.getName())){
				Card c1 = model.getOpponent().getField().get(index);
				if(c1 instanceof Minion){
				name=((Minion)c1).getName();
				String x = "MinionImages/"+name+".png";
				Board.getShowCard().setIcon(new ImageIcon(x));
			    Board.getCardInfo().setText(((Minion)c1).toString());
				}
				}
				}
				else if(user.equals("client")){

					if(model.getCurrentHero().getName().equals(p2.getName())){
						Card c1 = model.getOpponent().getField().get(index);
					if(c1 instanceof Minion){
					name=((Minion)c1).getName();
					String x = "MinionImages/"+name+".png";
					Board.getShowCard().setIcon(new ImageIcon(x));
				    Board.getCardInfo().setText(((Minion)c1).toString());
					}
					}
					else if(model.getOpponent().getName().equals(p2.getName())){
				Card c1 = model.getCurrentHero().getField().get(index);
				if(c1 instanceof Minion){
				name=((Minion)c1).getName();
				String x = "MinionImages/"+name+".png";
				Board.getShowCard().setIcon(new ImageIcon(x));
			    Board.getCardInfo().setText(((Minion)c1).toString());
				}
				}
				
				}
				
			}
		    
		    if(user.equals("client")){
				if(Board.getOpponentHandCards().contains(button)){
					if(model.getCurrentHero().getName().equals(p2.getName())){
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

					if(model.getOpponent().getName().equals(p2.getName())){
						int index=Board.getOpponentHandCards().indexOf(button);
						Card c1 = model.getOpponent().getHand().get(index);
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
				
			
			}
			
			if(compO.contains(button)){
					int index=compO.indexOf(button);
					if(user.equals("client")){
						if(model.getCurrentHero().getName().equals(p2.getName())){
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
					else if(user.equals("server")){

						if(model.getCurrentHero().getName().equals(p1.getName())){
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
 
