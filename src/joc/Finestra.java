package joc;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class Finestra extends JFrame{
	Joc j;
	//static int AMPLADA=1200,ALTURA=800;
	int AMPLADA, ALTURA; //current size of the game screen 
	Image im; //tècnica doble buffer
	Graphics g;
	JLabel label1;
	static GraphicsDevice device = GraphicsEnvironment
	        .getLocalGraphicsEnvironment().getScreenDevices()[0]; // canviant el zero escullo la pantalla on jugo
	public static void main(String[] args) {
		new Finestra();
	}
	Finestra(){//constructor
		super("Universe War I");
		setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		device.setFullScreenWindow(this);
		try {
			Thread.sleep(1000); //esperem un segon a que s'obri la pantalla per a definir les mides a sota (solució 
			//bastant trista la veritat) (podriem ensenyar algun icona per a dir que està carregant)
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		AMPLADA=getContentPane().getSize().width;
		ALTURA=getContentPane().getSize().height;
		im=this.createImage(AMPLADA,ALTURA);
		g=im.getGraphics();
		j=new Joc(this);
		j.run();
		
	}
	public void update(Graphics g) {
		paint(g);
	}
	public void paint(Graphics g) {
		g.drawImage(im,0,0,null);
	}
}

