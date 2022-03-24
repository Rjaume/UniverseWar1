package joc;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Estrella {
	BufferedImage imatge;
	int xInicial,yInicial,x,y,varX,v;
	Joc joc;
	static int maxGeneracio=3*Finestra.ALTURA/2+1000;
	double temps; // ens permetra saber quant temps porta existint l'estrella per a 
	//no dibuixar-la al cap d'un temps 
	boolean isVisible; 
	public Estrella(Joc joc) {
		this.joc=joc;
		this.xInicial=Joc.r.nextInt(Finestra.AMPLADA)+joc.c.xFisiques;
		this.yInicial=Joc.r.nextInt(2*maxGeneracio)-maxGeneracio+joc.c.yFisiques;
		x=xInicial;
		y=yInicial;
		imatge=joc.estrella;
		temps=System.currentTimeMillis();
		this.v=Joc.r.nextInt(5)+1;
		isVisible=true;
	}

	void pinta(Graphics g) {
		g.drawImage(imatge,x,y,null);
	}
	void moure() {
		if(isVisible) {
			x=xInicial-joc.c.xFisiques+varX;
			y=yInicial-joc.c.yFisiques;
			varX-=v;
				if(x<-100 || (System.currentTimeMillis()-temps>20*1000) ) {
					isVisible=false;
				}
			}
	}
}



