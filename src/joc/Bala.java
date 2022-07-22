package joc;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class Bala {
	Rectangle hitBox;
	int xPinta,yPinta,dfx,dfy,x,y,xInicial,yInicial,velocitat;
	float vx0,vy0; //velocitats x i y inicials de la bala
	//(xPinta,yPinta) és la posició de la imatge de la bala i (x,y) és la posició de la bala. dfx i dfy son les diferencies de posició en ambdues (al rotar posem la bala dins una imatge més gran del que toca)
	float varX,varY;
	static float llargadaRelativa = (float)10./1440, alturaRelativa = (float)2./900; //mides bales, relatives a la mida de la pantalla
	static int llargada,altura,diagonal;
	boolean xoc,isVisible; 
	static int bulletDamage = 10;
	BufferedImage imatgeBala;
	double angleDispar; 
	Joc joc;
	Graphics g;
	boolean miratXoc;
	public Bala(Joc joc, Nau nau) {
		this.joc=joc;
		this.g = joc.g;
		vx0 = nau.Vx;
		vy0 = nau.Vy;
		velocitat = Nau.velocitatBales;//200
		llargada = joc.llargadaBala;
		altura = joc.alturaBala;
		diagonal = Math.round((float)(Math.sqrt(llargada*llargada+altura*altura)));
		angleDispar = nau.alpha;
		dfx = Math.round((float)(diagonal/2-diagonal/2*Math.cos(angleDispar))); //diferència entre les posicions de la imatge de la bala i la bala en si
		dfy = Math.round((float)(diagonal/2-diagonal/2*Math.sin(angleDispar)));
		xInicial = Math.round((float)(nau.xPinta+nau.xFisiques+Nau.diagonal/2+(nau.llargada/2)*Math.cos(angleDispar)))-dfx; //posició on hem de pintar la imatge de la bala
		yInicial = Math.round((float)(nau.yPinta+nau.yFisiques+Nau.diagonal/2+(nau.llargada/2)*Math.sin(angleDispar)))-dfy;
		x=xInicial;
		y=yInicial;
		varX = 0;
		varY = 0;
		xPinta=xInicial-joc.c.xFisiques;
		yPinta=yInicial-joc.c.yFisiques;
		imatgeBala = joc.bala;   
		imatgeBala = Nau.rota(imatgeBala,angleDispar);
		xoc = false;
		isVisible = true;
		hitBox = new Rectangle(x,y,llargada,llargada); //així m'estalvio de rotar
		miratXoc = false;
	}
	public Bala(Joc joc, Torreta torreta) {
		this.joc = joc;
		this.g = joc.g;
		vx0 = 0;//les torretes estan quietes, per tant les bales de les torretes tenen velocitat inicial 0
		vy0 = 0;
		llargada = joc.llargadaBala;
		altura = joc.alturaBala;
		velocitat = torreta.velocitatBales;
		diagonal = Math.round((float)(Math.sqrt(llargada*llargada+altura*altura)));
		angleDispar = torreta.angleNau;
		xInicial = torreta.xInicial+torreta.llargada/2-diagonal/2; //tinc en compte que la imatge de la bala és més gran que la bala en si
		yInicial = torreta.yInicial+torreta.altura/2-diagonal/2; //e.xInicial+e.llargada/2, e.xInicial+e.altura/2 és el centre de la torreta
		x=xInicial;
		y=yInicial;
		varX=0;
		varY=0;
		xPinta=xInicial-joc.c.xFisiques;
		yPinta=yInicial-joc.c.yFisiques;
		imatgeBala = joc.bala;
		imatgeBala = Nau.rota(imatgeBala, angleDispar);
		isVisible = true;
		xoc = false;
		hitBox = new Rectangle(x,y,llargada,llargada); //així m'estalvio de rotar 
	}
	public Bala(Joc joc, NauEnemiga1 nau) {
		this.joc = joc;
		this.g = joc.g;
		llargada = joc.llargadaBala;
		altura = joc.alturaBala;
		vx0 = 0;
		vy0 = 0;
		xInicial=nau.xInicial;
		yInicial=nau.yInicial+25;
		x=xInicial-joc.c.xFisiques;
		y=yInicial-joc.c.yFisiques;
		varX = 0;
		varY = 0;
		xPinta=x;
		yPinta=y;
		xoc=false;
		isVisible=true;
		velocitat = NauEnemiga1.velocitatBales;
		imatgeBala=joc.bala;
		angleDispar = 0;
		dfx = 0; //no rotem per tant no hi ha diferència entre on és la bala i on és la imatge de la bala
		dfy = 0;
		hitBox = new Rectangle(x,y,llargada,altura);
	}
	public Bala(Joc joc, Objecte objecte, int velocitat, double angleDispar) { //constructor per a situar una bala a una posició concreta amb certa velocitat i angle
		this.joc = joc;
		this.g = joc.g;
		this.velocitat = velocitat;
		this.angleDispar = angleDispar;
		llargada = joc.llargadaBala;
		altura = joc.alturaBala;
		diagonal = Math.round((float)(Math.sqrt(llargada*llargada+altura*altura)));
		xoc = false;
		isVisible = true;
		varX = 0;
		varY = 0;
		dfx = Math.round((float)(diagonal/2-diagonal/2*Math.cos(angleDispar))); //diferència entre les posicions de la imatge de la bala i la bala en si
		dfy = Math.round((float)(diagonal/2-diagonal/2*Math.sin(angleDispar)));
//		Double diagonalObjecte = Math.sqrt(objecte.llargada*objecte.llargada+objecte.altura*objecte.altura);
//		xInicial = Math.round((float)(objecte.xInicial+diagonalObjecte/2+(objecte.llargada/2)*Math.cos(angleDispar)))-dfx; //posició on hem de pintar la imatge de la bala
//		yInicial = Math.round((float)(objecte.yInicial+diagonalObjecte/2+(objecte.llargada/2)*Math.sin(angleDispar)))-dfy;
		xInicial = Math.round(objecte.xInicial+objecte.llargada/2-diagonal/2);
		yInicial = Math.round(objecte.yInicial+objecte.altura/2-diagonal/2);
		x=xInicial-joc.c.xFisiques;
		y=yInicial-joc.c.yFisiques;
		xPinta=x;
		yPinta=y;
		vx0 = 0;
		vy0 = 0;
		imatgeBala = joc.bala;
		imatgeBala = Nau.rota(imatgeBala, angleDispar);
		hitBox = new Rectangle(x,y,llargada,altura);
	}
	void pinta() {
//		g.setColor(Color.BLACK);
//		g.fillRect(x,y,llargada,llargada); //ESTAN FATAL LES HITBOXES KEKW!
		g.drawImage(imatgeBala,xPinta,yPinta,null);
	}
	void moure() { //té en compte la velocitat inicial de la nau
		if(isVisible) {
		xPinta=Math.round(xInicial-joc.c.xFisiques+varX);
		yPinta=Math.round(yInicial-joc.c.yFisiques+varY);
		x = xPinta;
		y = yPinta;
		hitBox.setLocation(x,y);
		varX+=vx0*Joc.dt+velocitat*Math.cos(angleDispar)*Joc.dt;
		varY+=vy0*Joc.dt+velocitat*Math.sin(angleDispar)*Joc.dt;
		}
	}
}
