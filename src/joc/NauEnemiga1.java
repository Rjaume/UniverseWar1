package joc;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class NauEnemiga1 extends Enemic {
	BufferedImage nauEnemiga1;
	static float llargadaRelativa = (float)50./1440, alturaRelativa = (float)50./900; //mides relatives a la mida de la pantalla
	static int tempsEntreTrets=2; //en segons
	double tempsUltimTret;
	public NauEnemiga1(Joc joc){
		super(joc);
		this.bodyDamage = 25;
		this.v=6;
		this.xoc=0;
		this.mort=false;
		llargada=joc.llargadaNauEnemiga1;
		altura=joc.alturaNauEnemiga1;
		alturaMinimapa = 4;
		llargadaMinimapa = 4;
		vida=1;
		this.nauEnemiga1=joc.enemic1;
		isVisible=false;
		tempsUltimTret=0;
	}
	void pinta(Graphics g) {
		g.drawImage(nauEnemiga1,x,y,null);
	}
	
	void moure() {
		x=xInicial-joc.c.xFisiques+varX;
		y=yInicial-joc.c.yFisiques;
		varX-=v;
	}
	void dispara() {
		if(Math.abs(tempsUltimTret-System.currentTimeMillis())>tempsEntreTrets*1000) {
		bales.add(new Bala(this,joc));
		tempsUltimTret=System.currentTimeMillis();
		}
	}

}
