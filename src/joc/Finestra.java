package joc;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Finestra extends JFrame {
	Joc j;
	static int AMPLADA=1200,ALTURA=800;
	Image im; //tècnica doble buffer
	Graphics g;
	JLabel label1;
	public static void main(String[] args) {
		new Finestra();
	}
	Finestra(){//constructor
		super("Universe War I");
		setSize(AMPLADA,ALTURA);
		setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
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

