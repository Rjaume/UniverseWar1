package joc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class ContadorBales {
	int balesRestants;
	Joc joc;
	Graphics g;
	static float llargadaRelativa = (float)2./1440, alturaRelativa = (float)10./900; //mides relatives a la pantalla de cada rectangle.
	static float midaTextRelativa = (float)15./10, separacioyRelativa = (float)20./10; //mides relatives a la altura de les barres.
	static float xBarraRelativa = (float)80./2; //mides relatives a la llargada de les barres.
	static int llargadaRectangle,alturaRectangle, yBarra, xBarra, midaText;
	String text1=new String("Ammo left: ");
	String text2=new String(" bullets");
	ContadorBales(Joc joc){
		this.joc=joc;
		this.g=joc.g;
		this.balesRestants=Nau.balesInicials; //inicialment queden totes les bales
		llargadaRectangle = joc.llargadaBarres;
		alturaRectangle = joc.alturaBarres;
		yBarra = Nau.yBarraVida + Math.round(separacioyRelativa*alturaRectangle);
		xBarra = Math.round(xBarraRelativa*llargadaRectangle);
		midaText = Math.round(midaTextRelativa * alturaRectangle);
	}
	void pinta() {
		g.setColor(Color.WHITE);
		g.setFont(new Font("MyriadPro",Font.PLAIN,midaText)); //posem la mida de la lletra relativa a la alçada de les barres.
		g.drawString("ammo: ",15,yBarra+alturaRectangle);
		g.setColor(Color.cyan);
		for(int i=0;i<balesRestants;i++) {
			g.fillRect(xBarra+i*2*llargadaRectangle,yBarra,llargadaRectangle,alturaRectangle); //posició relativa a la mida de les barres. No volem xocar amb la
			//barra de dalt ni menjar-nos el text de l'esquerra.
		}
		g.drawRect(xBarra,yBarra,2*Nau.balesInicials*llargadaRectangle,alturaRectangle); 
		
	}
}
