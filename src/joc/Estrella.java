package joc;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Estrella extends Objecte{
	BufferedImage imatge;
	int xInicial,yInicial,varX,v;
	static float llargadaRelativa = (float)1./1440, alturaRelativa = (float)1./900; //mides relatives a la mida de la pantalla
	Joc joc;
	static int maxGeneracio;
	double temps; // ens permetrà saber quant temps porta existint l'estrella per a 
	//no dibuixar-la al cap d'un temps 
	boolean isVisible; 
	public Estrella(Joc joc) {
		this.joc=joc;
		maxGeneracio=3*joc.f.ALTURA/2+1000;
		this.xInicial=Joc.r.nextInt(joc.f.AMPLADA)+joc.c.xFisiques;
		this.yInicial=Joc.r.nextInt(2*maxGeneracio)-maxGeneracio+joc.c.yFisiques;
		x=xInicial;
		y=yInicial;
		llargada = joc.llargadaEstrella;
		altura = joc.alturaEstrella;
		imatge=joc.estrella;
		if(Joc.r.nextInt(100)>80) { //posem algunes estrelles més grosses
			llargada = 2*llargada;
			altura = 2*altura;
			try {
				imatge = joc.resizeImage(imatge, llargada, altura);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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



