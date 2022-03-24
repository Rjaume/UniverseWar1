package joc;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class NauEnemiga1 extends Enemic {
	BufferedImage nauEnemiga1;
	static int tempsEntreTrets=2; //en segons
	double tempsUltimTret;
	public NauEnemiga1(Joc joc){
		super(joc);
		this.bodyDamage = 25;
		this.v=6;
		this.xoc=0;
		this.mort=false;
		llargada=50;
		altura=50;
		vida=1;
		this.nauEnemiga1=joc.enemic1;
		isVisible=true;
		tempsUltimTret=0;
	}
	void pinta(Graphics g) {
		g.drawImage(nauEnemiga1,x,y,null);
	}
	
	void moure() {
		x=xInicial-joc.c.xFisiques+varX;
		y=yInicial-joc.c.yFisiques;
		varX-=v;
			if(Math.abs(Nau.x-this.x)>1500 || Math.abs(Nau.y-this.y)>1500) {
				isVisible=false;
			}else {
				isVisible=true;
			}
	}
	void dispara() {
		if(Math.abs(tempsUltimTret-System.currentTimeMillis())>tempsEntreTrets*1000) {
		bales.add(new Bala(this,joc));
		tempsUltimTret=System.currentTimeMillis();
		}
	}

}
