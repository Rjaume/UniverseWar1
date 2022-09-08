package joc;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Estrella extends Objecte{
	BufferedImage imatge;
	int xInicial,yInicial,varX,varY,vx,vy;
	static float llargadaRelativa = (float)1./1440, alturaRelativa = (float)1./900; //mides relatives a la mida de la pantalla
	double temps; // ens permetrà saber quant temps porta existint l'estrella per a 
	//no dibuixar-la al cap d'un temps 
	public Estrella(Joc joc) {
		this.joc=joc;
		this.g = joc.g;
		this.xInicial=Joc.r.nextInt(3*joc.f.AMPLADA)-joc.f.AMPLADA+joc.c.xFisiques;
		this.yInicial=Joc.r.nextInt(3*joc.f.ALTURA)-joc.f.ALTURA+joc.c.yFisiques;
		x=xInicial;
		y=yInicial;
		llargada = joc.llargadaEstrella;
		altura = joc.alturaEstrella;
		imatge=joc.estrella;
		if(Joc.r.nextInt(100)>70) { //posem algunes estrelles més grosses
			if(Joc.r.nextInt(100)>80) {
				llargada= 3*llargada;
				altura = 3*altura;
			}
			else {
				llargada = 2*llargada;
				altura = 2*altura;
			}
			try {
				imatge = joc.resizeImage(imatge, llargada, altura);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		temps=System.currentTimeMillis();
		this.vx=Joc.r.nextInt(5)+1;
		this.vy=Joc.r.nextInt(3)-1;
		isVisible=true;
	}

	void pinta() {
		g.drawImage(imatge,x,y,null);
	}
	void moure() {
		if(isVisible) {
			x=xInicial-joc.c.xFisiques+varX;
			y=yInicial-joc.c.yFisiques+varY;
			varX-=vx;
			varY-=vy;
				if(x<-100 || (System.currentTimeMillis()-temps>20*1000) ) {
					isVisible=false;
				}
			}
	}
}



