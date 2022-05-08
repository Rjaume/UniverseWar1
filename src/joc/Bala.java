package joc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bala {
	int xPinta,yPinta,dfx,dfy,x,y,xInicial,yInicial,vBalesNau,vBalesEnemigues=15; 
	float vxBalaInicial,vyBalaInicial; //velocitats x i y de la nau en el moment de disparar
	//(xPinta,yPinta) és la posició de la imatge de la bala i (x,y) és la posició de la bala. dfx i dfy son les diferencies de posició en ambdues (al rotar posem la bala dins una imatge més gran del que toca)
	float varX,varY;
	static float llargadaRelativa = (float)10./1440, alturaRelativa = (float)1./900; //mides bales, relatives a la mida de la pantalla
	static int llargada,altura,diagonal;
	boolean xoc,isVisible, calculatXoc; 
	static int bulletDamage = 10;
	static BufferedImage imatgeBala;
	double angleDispar; //valor angle de la nau quan hem disparat
	Joc joc;
	public Bala(Joc joc){ //constructor per la nau
		this.joc=joc;
		vxBalaInicial = joc.c.Vx;
		vyBalaInicial = joc.c.Vy;
		vBalesNau = 300;//200
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
		g.drawImage(imatgeBala,xPinta,yPinta,null);
	}
	void pintaBalaEnemic(Graphics g) {
		g.setColor(Color.white);
		g.drawRect(xPinta,yPinta,llargada,altura);
	}
	void moureBalaNau() { //
		xPinta=Math.round(xInicial-joc.c.xFisiques+varX);
		yPinta=Math.round(yInicial-joc.c.yFisiques+varY);
		x = xPinta+dfx;
		y = yPinta+dfy;
		varX+=vxBalaInicial*Joc.dt+vBalesNau*Math.cos(angleDispar)*Joc.dt;
		varY+=vyBalaInicial*Joc.dt+vBalesNau*Math.sin(angleDispar)*Joc.dt;
	}
	void moureEsquerra() { //els enemics disparen cap a l'esquerra
		if(isVisible) {
			xPinta=Math.round(xInicial-joc.c.xFisiques+varX);
			yPinta=yInicial-joc.c.yFisiques;
			varX-=vBalesEnemigues;
				if(Math.abs(xPinta-joc.c.x)>1000) {
					isVisible=false;
				}
			}
	}
}
