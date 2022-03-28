package joc;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Meteorit2 extends Enemic{
	BufferedImage meteorit,meteoritEsquerdat;
	public Meteorit2(Joc joc) {
		super(joc);
		this.bodyDamage = 50;
		this.v=Joc.r.nextInt(9)+9;  
		this.xoc=0; //nombre de xocs inicials és 0
		this.mort=false;
		meteorit=joc.imatgesMeteorits[3];
		meteoritEsquerdat=joc.imatgesMeteorits[4];
		llargada=90;
		altura=90;
		vida=2;
		alturaMinimapa = 6;
		llargadaMinimapa = 6;
		balesInicials=0; //els meteorits no tenen bales
	}
	
	void pinta(Graphics g) {
		if(this.xoc==0) {
			g.drawImage(meteorit,x,y,null);
		}
		if(this.xoc==1) {
			g.drawImage(meteoritEsquerdat,x,y,null);
		}
	}
	void moure() {
		x=xInicial-joc.c.xFisiques+varX;
		y=yInicial-joc.c.yFisiques;
		varX-=v;
	}
	void dispara() {
	}
}
