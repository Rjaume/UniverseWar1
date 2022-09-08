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
	static float llargadaRelativa = (float)14./1440, alturaRelativa = (float)5./900; //mides bales, relatives a la mida de la pantalla
	static int llargada,altura,diagonal;
	boolean xoc,isVisible; 
	static int bulletDamage = 10;
	BufferedImage imatgeBala;
	double angleDispar; 
	Joc joc;
	Graphics g;
	Objecte objecte;
	public Bala(Joc joc, Nau nau) {
		this.joc=joc;
		this.g = joc.g;
//		vx0 = nau.Vx;
//		vy0 = nau.Vy;
		this.objecte = nau;
		vx0 = 0;
		vy0 = 0;
		velocitat = Nau.velocitatBales;//200
		llargada = joc.llargadaBala;
		altura = joc.alturaBala;
		diagonal = Math.round((float)(Math.sqrt(llargada*llargada+altura*altura)));
		angleDispar = nau.alpha;
		dfx = Math.round((float)(diagonal/2-diagonal/2*Math.cos(angleDispar))); //diferència entre les posicions de la imatge de la bala i la bala en si
		dfy = Math.round((float)(diagonal/2-diagonal/2*Math.sin(angleDispar)));
		xInicial = Math.round((float)(nau.xPinta+Nau.diagonal/2+(nau.llargada/2)*Math.cos(angleDispar))); //posició on hem de pintar la imatge de la bala
		yInicial = Math.round((float)(nau.yPinta+Nau.diagonal/2+(nau.llargada/2)*Math.sin(angleDispar))); //he de sumar les físiques perquè les bales de la nau surten de la nau, la posició de la qual no es veu afectada per les fisiques 
		x=xInicial;
		y=yInicial;
		varX = 0;
		varY = 0;
		xPinta=xInicial-dfx;
		yPinta=yInicial-dfy;
		imatgeBala = joc.bala1;   
		imatgeBala = Nau.rota(imatgeBala,angleDispar);
		xoc = false;
		isVisible = true;
		hitBox = new Rectangle(x-diagonal/2,y-diagonal/2,llargada,llargada); //així m'estalvio de rotar
	}
	public Bala(Joc joc, Personatge personatge) {
		this.joc = joc;
		this.g = joc.g;
		this.objecte = personatge;
		angleDispar = personatge.alpha;
		//la posició inicial de la bala depèn de l'angle (depenent de l'angle tenim la pistola a un lloc o a un altre)
		if(angleDispar>=Math.PI/2 && angleDispar<= Math.PI/2+Math.PI/12 ) { // 16, 39
//			xInicial = personatge.x + Math.round((16f*personatge.Mmida)/1920*joc.f.AMPLADA);
			xInicial = personatge.x + Math.round((16f*personatge.Mmida));
			yInicial = personatge.y + Math.round((39f*personatge.Mmida));
		}
		if(angleDispar>=Math.PI/2+Math.PI/12 && angleDispar<=Math.PI/2+Math.PI/12+Math.PI/6 ) { // 8, 33
			xInicial = personatge.x + Math.round((8f*personatge.Mmida));
			yInicial = personatge.y + Math.round((33f*personatge.Mmida));
		}
		if(angleDispar>=Math.PI/2+Math.PI/12+Math.PI/6 && angleDispar<=Math.PI/2+Math.PI/12+Math.PI/3) { //1,23
			xInicial = personatge.x + Math.round((1f*personatge.Mmida));
			yInicial = personatge.y + Math.round((23f*personatge.Mmida));
		}
		if(angleDispar>=Math.PI-Math.PI/12 && angleDispar<= Math.PI+Math.PI/12){//2, 16
			xInicial = personatge.x + Math.round((2f*personatge.Mmida));
			yInicial = personatge.y + Math.round((16f*personatge.Mmida));
		}
		if(angleDispar>=Math.PI+Math.PI/12 && angleDispar<=Math.PI+Math.PI/12+Math.PI/6) { // 4, 9
			xInicial = personatge.x + Math.round((4f*personatge.Mmida));
			yInicial = personatge.y + Math.round((9f*personatge.Mmida));
		}
		if(angleDispar>=Math.PI+Math.PI/12+Math.PI/6 && angleDispar<=Math.PI+Math.PI/12+2*Math.PI/6) { // 7, 3
			xInicial = personatge.x + Math.round((7f*personatge.Mmida));
			yInicial = personatge.y + Math.round((3f*personatge.Mmida));
		}
		if(angleDispar>=Math.PI+Math.PI/12+2*Math.PI/6 && angleDispar<=3*Math.PI/2 ) { // 13, 2
			xInicial = personatge.x + Math.round((13f*personatge.Mmida));
			yInicial = personatge.y + Math.round((2f*personatge.Mmida));
		}
		if(angleDispar<=Math.PI/2 && angleDispar>= Math.PI/2-Math.PI/12 ) { // 18, 39
			xInicial = personatge.x + Math.round((18f*personatge.Mmida));
			yInicial = personatge.y + Math.round((39f*personatge.Mmida));
		}
		if(angleDispar<=Math.PI/2-Math.PI/12 && angleDispar>=Math.PI/2-Math.PI/12 -Math.PI/6 ) { // 26,36
			xInicial = personatge.x + Math.round((26f*personatge.Mmida));
			yInicial = personatge.y + Math.round((33f*personatge.Mmida));
		}
		if(angleDispar<=Math.PI/2-Math.PI/12-Math.PI/6 && angleDispar>=Math.PI/2-Math.PI/12-Math.PI/3) { //33, 23
			xInicial = personatge.x + Math.round((33f*personatge.Mmida));
			yInicial = personatge.y + Math.round((23f*personatge.Mmida));
		}
		if(angleDispar<=Math.PI/12 || angleDispar>= 2*Math.PI-Math.PI/12){// 32, 16
			xInicial = personatge.x + Math.round((32f*personatge.Mmida));
			yInicial = personatge.y + Math.round((16f*personatge.Mmida));
		}
		if(angleDispar<=2*Math.PI-Math.PI/12 && angleDispar>=2*Math.PI-Math.PI/12-Math.PI/6) { //30,8
			xInicial = personatge.x + Math.round((30f*personatge.Mmida));
			yInicial = personatge.y + Math.round((8f*personatge.Mmida));
		}
		if(angleDispar<=2*Math.PI-Math.PI/12-Math.PI/6 && angleDispar>=3*Math.PI/2+Math.PI/12) { //28, 3
			xInicial = personatge.x + Math.round((28f*personatge.Mmida));
			yInicial = personatge.y + Math.round((3f*personatge.Mmida));
		}
		if(angleDispar>=3*Math.PI/2 && angleDispar<=3*Math.PI/2+Math.PI/12) { //22, 1
			xInicial = personatge.x + Math.round((22f*personatge.Mmida));
			yInicial = personatge.y + Math.round((1f*personatge.Mmida));
		}
		x=xInicial;
		y=yInicial;
		llargada = joc.llargadaBala;
		altura = joc.alturaBala;
		diagonal = Math.round((float)(Math.sqrt(llargada*llargada+altura*altura)));
		dfx = Math.round((float)(diagonal/2-diagonal/2*Math.cos(angleDispar))); //diferència entre les posicions de la imatge de la bala i la bala en si
		dfy = Math.round((float)(diagonal/2-diagonal/2*Math.sin(angleDispar)));
		xPinta=xInicial-dfx;
		yPinta=yInicial-dfy;
		vx0 = 0;
		vy0 = 0;
		imatgeBala = joc.bala1; 
		imatgeBala = Nau.rota(imatgeBala,angleDispar);
		velocitat = Personatge.velocitatBales;
		angleDispar = personatge.alpha;
		xoc = false;
		isVisible = true;
		varX = 0;
		varY = 0;
		hitBox = new Rectangle(x-diagonal/2,y-diagonal/2,llargada,llargada); //així m'estalvio de rotar 

	}
	public Bala(Joc joc, Torreta torreta) {
		this.joc = joc;
		this.g = joc.g;
		this.objecte = torreta;
		vx0 = 0;//les torretes estan quietes, per tant les bales de les torretes tenen velocitat inicial 0
		vy0 = 0;
		llargada = joc.llargadaBala;
		altura = joc.alturaBala;
		velocitat = torreta.velocitatBales;
		diagonal = Math.round((float)(Math.sqrt(llargada*llargada+altura*altura)));
		angleDispar = torreta.angleNau;
		dfx = Math.round((float)(diagonal/2-diagonal/2*Math.cos(angleDispar))); //diferència entre les posicions de la imatge de la bala i la bala en si
		dfy = Math.round((float)(diagonal/2-diagonal/2*Math.sin(angleDispar)));
		xInicial = torreta.x+torreta.llargada/2; //tinc en compte que la imatge de la bala és més gran que la bala en si
		yInicial = torreta.y+torreta.altura/2; //e.xInicial+e.llargada/2, e.xInicial+e.altura/2 és el centre de la torreta
		x=xInicial;
		y=yInicial;
		varX=0;
		varY=0;
		xPinta=xInicial-dfx;
		yPinta=yInicial-dfy;
		imatgeBala = joc.bala2;
		imatgeBala = Nau.rota(imatgeBala, angleDispar);
		isVisible = true;
		xoc = false;
		hitBox = new Rectangle(x-diagonal/2,y-diagonal/2,llargada,llargada); //així m'estalvio de rotar 
	}
	public Bala(Joc joc, NauEnemiga2 nau) {
		this.joc = joc;
		this.g = joc.g;
		this.objecte = nau;
		vx0 = (float) nau.vx;
		vy0 = (float) nau.vy;
		llargada = joc.llargadaBala;
		altura = joc.alturaBala;
		velocitat = nau.velocitatBales;
		diagonal = Math.round((float)(Math.sqrt(llargada*llargada+altura*altura)));
		dfx = Math.round((float)(diagonal/2-diagonal/2*Math.cos(angleDispar))); //diferència entre les posicions de la imatge de la bala i la bala en si
		dfy = Math.round((float)(diagonal/2-diagonal/2*Math.sin(angleDispar)));
		angleDispar = nau.angleNau;
		xInicial = nau.xCentre + joc.c.xFisiques; 
		yInicial = nau.yCentre + joc.c.yFisiques; 
		x=xInicial;
		y=yInicial;
		xPinta = xInicial-dfx-joc.c.xFisiques;
		yPinta = yInicial-dfy-joc.c.yFisiques;
		varX=0;
		varY=0;
		imatgeBala = joc.bala2;
		imatgeBala = Nau.rota(imatgeBala, angleDispar);
		isVisible = true;
		xoc = false;
		hitBox = new Rectangle(x-diagonal/2,y-diagonal/2,llargada,llargada); //així m'estalvio de rotar 
	}
	public Bala(Joc joc, NauEnemiga1 nau) {
		this.joc = joc;
		this.g = joc.g;
		this.objecte = nau;
		llargada = joc.llargadaBala;
		altura = joc.alturaBala;
		vx0 = 0;
		vy0 = 0;
		xInicial=nau.x + joc.c.xFisiques;
		yInicial=nau.y+25 + joc.c.yFisiques;
		x=xInicial;
		y=yInicial;
		varX = 0;
		varY = 0;
		xPinta=x-joc.c.xFisiques;
		yPinta=y-joc.c.yFisiques;
		xoc=false;
		isVisible=true;
		velocitat = NauEnemiga1.velocitatBales;
		imatgeBala=joc.bala2;
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
		xInicial = Math.round(objecte.xInicial+objecte.llargada/2-diagonal/2);
		yInicial = Math.round(objecte.yInicial+objecte.altura/2-diagonal/2);
		x=xInicial-joc.c.xFisiques;
		y=yInicial-joc.c.yFisiques;
		xPinta=x - dfx;
		yPinta=y - dfy;
		vx0 = 0;
		vy0 = 0;
		imatgeBala = joc.bala2;
		imatgeBala = Nau.rota(imatgeBala, angleDispar);
		hitBox = new Rectangle(x-diagonal/2,y-diagonal/2,llargada,llargada);
	}
	void pinta() {
		g.drawImage(imatgeBala,xPinta,yPinta,null);
	}
	void moure() { //té en compte la velocitat inicial de la nau
		if(isVisible) {
		if(objecte instanceof Nau || objecte instanceof Personatge) { // no sé si ho hauria de fer diferent 
			x = Math.round(xInicial+varX);
			y = Math.round(yInicial+varY);
		}
		else {
			x = Math.round(xInicial+varX)-joc.c.xFisiques;
			y = Math.round(yInicial+varY)-joc.c.yFisiques;
		}
		xPinta = x-dfx;
		yPinta = y-dfy;
		if(objecte instanceof NauEnemiga1) hitBox.setLocation(x,y);
		else hitBox.setLocation(x-diagonal/2,y-diagonal/2);
		varX+=vx0*Joc.dt+velocitat*Math.cos(angleDispar)*Joc.dt;
		varY+=vy0*Joc.dt+velocitat*Math.sin(angleDispar)*Joc.dt;
		}
	}
}
