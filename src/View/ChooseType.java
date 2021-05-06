package View;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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

import Controller.AiController;
import Controller.Controller;
import Controller.SocketController;
import exceptions.FullHandException;

@SuppressWarnings("serial")
public class ChooseType extends JFrame implements ActionListener,MouseListener{
	private JLabel bj;
	private Clip clip;
	private JButton Multi;
	private JButton Single;
	
	public ChooseType(){
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
	    
		Multi = new JButton("Multi");
		Multi.addActionListener(this);
		Multi.setActionCommand("Multi");
		Multi.addMouseListener(this);
	    Multi.setBounds(800,710,200,200);
	    getContentPane().add(Multi);
		
	    
	    Single = new JButton("Single");
		Single.addActionListener(this);
		Single.setActionCommand("Single");
		Single.addMouseListener(this);
	    Single.setBounds(800,500,200,200);
	    getContentPane().add(Single);
	    
		
		this.getContentPane().add(bj);
		this.revalidate();
		this.repaint();
		this.setVisible(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}

	public static void main(String[] args){
		ChooseType t = new ChooseType();
		t.setVisible(true);
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
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton b = (JButton) e.getSource();
		if(b.getActionCommand().equals("Single")){
			try {
				new Controller();
				this.dispose();
			} catch (FullHandException | CloneNotSupportedException e1) {
				e1.printStackTrace();
			}
			
		}
		
		if(b.getActionCommand().equals("Exit")){
			clip.stop();
			this.dispose();
		}
		
		if(b.getActionCommand().equals("Multi")){
			
			HostorClient hc = new HostorClient();
			hc.setVisible(true);
			
			this.dispose();
		}
	}

	public JLabel getBj() {
		return bj;
	}

	public void setBj(JLabel bj) {
		this.bj = bj;
	}

	public Clip getClip() {
		return clip;
	}

	public void setClip(Clip clip) {
		this.clip = clip;
	}

	public JButton getMulti() {
		return Multi;
	}

	public void setMulti(JButton multi) {
		Multi = multi;
	}

	public JButton getSingle() {
		return Single;
	}

	public void setSingle(JButton single) {
		Single = single;
	}
}
