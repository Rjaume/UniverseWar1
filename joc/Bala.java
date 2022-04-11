package joc;

import java.awt.Color;
import java.awt.Graphics;

public class Bala {
	int x,y,xInicial,yInicial,varX,vBalesNau,vBalesEnemigues=15;
	static float llargadaRelativa = (float)10./1440, alturaRelativa = (float)1./900; //mides bales, relatives a la mida de la pantalla
	static int llargada,altura;
	boolean xoc,isVisible, calculatXoc; 
	static int bulletDamage = 10;
	Joc joc;
	public Bala(Joc joc){
		vBalesNau = Math.abs((int) (joc.c.Vx/10+18)); //tenim una mica en compte la velocitat de la nau per a decidir si la bala anirà més rapid o més lenta
		xInicial = Nau.x+joc.c.xFisiques+Nau.llargada;
		yInicial = Nau.y+joc.c.yFisiques+Nau.altura/2;
		x=xInicial;
		y=yInicial;
		llargada = joc.llargadaBales;
		altura = joc.alturaBales;
		this.xoc=false; 
		isVisible=true;
		this.joc=joc;
		varX=0;
	}
	public Bala(Enemic e,Joc joc){
		calculatXoc = false;
		this.xInicial=e.x+joc.c.xFisiques;
		this.yInicial=e.y+25+joc.c.yFisiques;
		this.xoc=false;
		isVisible=true;
		this.joc=joc;
		varX=0;
	}
	void pinta(Graphics g) {
		g.setColor(Color.white);
		g.drawRect(x,y,llargada,altura);
	}
	void pintaBalaEnemic(Graphics g) {
		g.setColor(Color.white);
		g.drawRect(x,y,llargada,altura);
	}
	void moureDreta() { //la nau dispara cap a la dreta
		x=xInicial-joc.c.xFisiques+varX;
		y=yInicial-joc.c.yFisiques;
		varX+=vBalesNau;
	}
	void moureEsquerra() { //els enemics disparen cap a l'esquerra
		if(isVisible) {
			x=xInicial-joc.c.xFisiques+varX;
			y=yInicial-joc.c.yFisiques;
			varX-=vBalesEnemigues;
				if(x<-100) {
					isVisible=false;
				}
			}
	}
}
