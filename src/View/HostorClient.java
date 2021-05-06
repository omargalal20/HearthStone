package View;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.util.Timer;
import java.util.TimerTask;

import Controller.Controller;
import Controller.SocketController;
import exceptions.FullHandException;

@SuppressWarnings("serial")
public class HostorClient extends JFrame implements ActionListener,MouseListener{

	private JLabel bj;
	private Clip clip;
	private JButton Host;
	private JButton Join;
	
	private int port = 5000;
	private InetAddress ip;
	
	private ServerSocket serversocket;
	private Scanner scanner = new Scanner(System.in);
	
	JDialog dialog;
	
	private Socket clientsocket;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	
	
	public HostorClient(){
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
	    
		Host = new JButton("Host");
		Host.addActionListener(this);
		Host.setActionCommand("Host");
		Host.addMouseListener(this);
		Host.setBounds(800,500,200,200);
	    getContentPane().add(Host);
		
	    
	    Join = new JButton("Join");
		Join.addActionListener(this);
		Join.setActionCommand("Client");
		Join.addMouseListener(this);
	    Join.setBounds(800,710,200,200);
	    getContentPane().add(Join);
	    
		
		this.getContentPane().add(bj);
		this.revalidate();
		this.repaint();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}

	public static void main(String[] args){
		HostorClient hc = new HostorClient();
		hc.setVisible(true);
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

	public JButton getHost() {
		return Host;
	}

	public JButton getSingle() {
		return Join;
	}

	public JButton getJoin() {
		return Join;
	}

	public void setJoin(JButton join) {
		Join = join;
	}

	public void setHost(JButton host) {
		Host = host;
	}

}
