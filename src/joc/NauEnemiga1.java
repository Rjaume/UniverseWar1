package joc;

import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class NauEnemiga1 extends Enemic {
	static float llargadaRelativa = (float)50./1440, alturaRelativa = (float)50./900; //mides relatives a la mida de la pantalla
	static int tempsEntreTrets=2; //en segons
	double tempsUltimTret;
	static int velocitatBales = -100;
	public NauEnemiga1(Joc joc){
		super(joc);
		this.bodyDamage = 25;
		this.v=6;
		this.xoc=0;
		this.mort=false;
		llargada=joc.midaNauEnemiga1;
		altura=llargada;
		xCentre = x + llargada/2;
		yCentre = y + altura/2;
		alturaMinimapa = joc.midaNauEnemiga1M;
		llargadaMinimapa = alturaMinimapa;
		vida=1;
		nombreImatges = 1;
		imatges = new BufferedImage[nombreImatges];
		imatges[0] = joc.imatgeNauEnemiga1;
		isVisible=false;
		tempsUltimTret=0;
		color[0] = 255;//color mitjà particules
		color[1] = 255;
		color[2] = 255;
		midaParticules = 7; //mida mitjana partícules
		nombreParticules = 10;
		hitBox = new Rectangle(x,y,llargada,altura);
	}
	public NauEnemiga1(Joc joc, int x, int y) { //constructor per a poder situar l'enemic on volem
		super(joc,x,y);
		this.v=0;
		this.xoc =0;
		this.mort=false;
		llargada=joc.midaNauEnemiga1;
		altura=llargada;
		alturaMinimapa = joc.midaNauEnemiga1M;
		llargadaMinimapa = alturaMinimapa;
		vida =1;
		imatges[0] = joc.imatgeNauEnemiga1;
		isVisible = true;
		tempsUltimTret=100;
		color[0] = 230;
		color[1] = 230;
		color[2] = 230;
		midaParticules = 7;
		this.nombreParticules = 10;
		hitBox = new Rectangle(x,y,llargada,altura);
	}
	void pinta() {
		g.drawImage(imatges[0],x,y,null);
	}
	
	void moure() {
		x=xInicial-joc.c.xFisiques+varX;
		y=yInicial-joc.c.yFisiques;
		xCentre = x + llargada/2;
		yCentre = y + altura/2;
		varX-=v;
		((Rectangle) hitBox).setLocation(x,y);
	}
	void dispara() {
		if(Math.abs(tempsUltimTret-System.currentTimeMillis())>tempsEntreTrets*1000) {
		bales.add(new Bala(joc,this));
		tempsUltimTret=System.currentTimeMillis();
		}
	}

}
