package joc;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bala {
	int xPinta,yPinta,dfx,dfy,x,y,xInicial,yInicial,velocitat;
	float vxBalaInicial,vyBalaInicial; //velocitats x i y inicials de la bala
	//(xPinta,yPinta) és la posició de la imatge de la bala i (x,y) és la posició de la bala. dfx i dfy son les diferencies de posició en ambdues (al rotar posem la bala dins una imatge més gran del que toca)
	float varX,varY;
	static float llargadaRelativa = (float)10./1440, alturaRelativa = (float)1./900; //mides bales, relatives a la mida de la pantalla
	static int llargada,altura,diagonal;
	boolean xoc,isVisible, calculatXoc; 
	static int bulletDamage = 10;
	BufferedImage imatgeBala;
	double angleDispar; 
	Joc joc;
	public Bala(Objecte e,Joc joc){
		if(e instanceof Nau) {
			this.joc=joc;
			vxBalaInicial = joc.c.Vx;
			vyBalaInicial = joc.c.Vy;
			velocitat = Nau.velocitatBales;//200
			imatgeBala = joc.bala;
			llargada = joc.llargadaBala;
			altura = joc.alturaBala;
			diagonal = Math.round((float)(Math.sqrt(llargada*llargada+altura*altura)));
			angleDispar = joc.c.alpha;
			imatgeBala = Nau.rota(imatgeBala,angleDispar);
			dfx = Math.round((float)(diagonal/2-diagonal/2*Math.cos(angleDispar))); //diferència entre les posicions de la imatge de la bala i la bala en si
			dfy = Math.round((float)(diagonal/2-diagonal/2*Math.sin(angleDispar)));
			xInicial = Math.round((float)(joc.c.xPinta+joc.c.xFisiques+Nau.diagonal/2+(joc.c.llargada/2)*Math.cos(angleDispar)))-dfx; //posició on hem de pintar la imatge de la bala
			yInicial = Math.round((float)(joc.c.yPinta+joc.c.yFisiques+Nau.diagonal/2+(joc.c.llargada/2)*Math.sin(angleDispar)))-dfy;
			x = xInicial+dfx; //posició on realment és la bala, tinguent en compte que la imatge té mida més gran
			y = yInicial+dfy;
			xPinta=xInicial;
			yPinta=yInicial;
			llargada = joc.llargadaBales;
			altura = joc.alturaBales;
			this.xoc=false; 
			isVisible=true;
			varX=0;
			varY=0;
		}
		if(e instanceof NauEnemiga1) {
			this.joc=joc;
			calculatXoc = false;
			this.xInicial=e.x+joc.c.xFisiques;
			this.yInicial=e.y+25+joc.c.yFisiques;
			this.xoc=false;
			isVisible=true;
			varX=0;
			velocitat = NauEnemiga1.velocitatBales;
			imatgeBala=joc.bala;
			angleDispar = 0;
			dfx = 0; //no rotem per tant no hi ha diferència entre on és la bala i on és la imatge de la bala
			dfy = 0;}
		if(e instanceof Torreta) {
			this.joc=joc;
			xPinta = xInicial;
			yPinta = yInicial;
			llargada = joc.llargadaBala;
			altura = joc.alturaBala;
			velocitat = Torreta.velocitatBales;
			diagonal = Math.round((float)(Math.sqrt(llargada*llargada+altura*altura)));
//			dfx = Math.round((float)(diagonal/2-diagonal/2*Math.abs(Math.cos(2*Math.PI-angleDispar)))); //diferència entre les posicions de la imatge de la bala i la bala en si
//			dfy = Math.round((float)(diagonal/2-diagonal/2*Math.abs(Math.sin(2*Math.PI-angleDispar))));
			angleDispar = ((Torreta) e).angleNau;
			this.xInicial = e.xInicial+e.llargada/2-diagonal/2; //tinc en compte que la imatge de la bala és més gran que la bala en si
			this.yInicial = e.yInicial+e.altura/2-diagonal/2; //e.xInicial+e.llargada/2, e.xInicial+e.altura/2 és el centre de la torreta
			imatgeBala = joc.bala;
			imatgeBala = Nau.rota(imatgeBala, angleDispar);
			varX=0;
			varY=0;
			vxBalaInicial = 0;//les torretes estan quietes, per tant les bales de les torretes tenen velocitat inicial 0
			vyBalaInicial = 0;
			isVisible = true;
			xoc = false;
		}
	}
	void pinta(Graphics g) {
		g.drawImage(imatgeBala,xPinta,yPinta,null);
	}
	void moure() { //té en compte la velocitat inicial de la nau
		if(isVisible) {
		xPinta=Math.round(xInicial-joc.c.xFisiques+varX);
		yPinta=Math.round(yInicial-joc.c.yFisiques+varY);
		x = xPinta+dfx;
		y = yPinta+dfy;
		varX+=vxBalaInicial*Joc.dt+velocitat*Math.cos(angleDispar)*Joc.dt;
		varY+=vyBalaInicial*Joc.dt+velocitat*Math.sin(angleDispar)*Joc.dt;
		}
	}
}
