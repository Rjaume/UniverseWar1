package joc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class ContadorBales {
	int balesRestants;
	Joc joc;
	Graphics g;
	String text1=new String("Ammo left: ");
	String text2=new String(" bullets");
	ContadorBales(Joc joc){
		this.joc=joc;
		this.g=joc.g;
		this.balesRestants=Nau.balesInicials; //inicialment queden totes les bales
	}
	void pinta() {
		g.setColor(Color.WHITE);
		g.setFont(new Font("MyriadPro",Font.PLAIN,15));
		g.drawString("ammo: ",15,80);
		g.setColor(Color.cyan);
		for(int i=0;i<balesRestants;i++) {
			g.fillRect(90+i*4,70,2,10);
		}
		g.drawRect(90,70,4*Nau.balesInicials,10);
		//g.drawString(text1+balesRestants+" / "+ Nau.balesInicials+text2, 480, 750); 
		
	}
}
