package joc;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Meteorit1 extends Enemic {
	BufferedImage Imatge;//
	static float llargadaRelativa[] = new float[] {(float)78./1440, (float)78./1440,(float)34./1440 };
	static float alturaRelativa = (float)34./900; //mides relatives a la mida de la pantalla
	public Meteorit1(Joc joc){
		super(joc);
		this.bodyDamage = 25;
		this.v=Joc.r.nextInt(8)+12; 
		this.xoc=0;
		this.mort=false;
		if(this.v<=14) {//en funcio de la velocitat el meteorit tindrà més foc o menys
		this.Imatge=joc.imatgesMeteorits[2];
		}
		if(this.v>14&&this.v<=16) { 
			this.Imatge=joc.imatgesMeteorits[1];
		}
		if(this.v>16) {
			this.Imatge=joc.imatgesMeteorits[0];
		}
		altura=joc.alturaMeteorit1;
		llargada=altura; //a efectes dels xocs l'altura és la mateixa que la llargada.
		alturaMinimapa = joc.llargadaMeteorit1M;
		llargadaMinimapa = joc.llargadaMeteorit1M;
		vida=1;
	}
	void pinta(Graphics g) {
		g.drawImage(Imatge,x,y,null);
	}
	void moure() {
		x=xInicial-joc.c.xFisiques+varX;
		y=yInicial-joc.c.yFisiques;
		varX-=v;
		}
	void dispara() {
		
	}
}
