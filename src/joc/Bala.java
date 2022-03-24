package joc;

import java.awt.Color;
import java.awt.Graphics;

public class Bala {
	int x,y,xInicial,yInicial,varX,vBalesNau,vBalesEnemigues=15,llargada=10,amplada=1;
	boolean xoc,isVisible, calculatXoc; 
	static int bulletDamage = 10;
	Joc joc;
	public Bala(Joc joc){
		vBalesNau = Math.abs((int) (joc.c.Vx+18)); //MASSA! POTSER NO CAL TENIR 2 VARIABLES PER VELOCITAT NSE 
		yInicial=Nau.y+14+joc.c.yFisiques;
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
		g.drawRect(x,y,llargada,amplada);
	}
	void pintaBalaEnemic(Graphics g) {
		g.setColor(Color.white);
		g.drawRect(x,y,llargada,amplada);
	}
	void moureDreta() {
		x=xInicial-joc.c.xFisiques+varX;
		y=yInicial-joc.c.yFisiques;
		varX+=vBalesNau;
	}
	void moureEsquerra() {
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
