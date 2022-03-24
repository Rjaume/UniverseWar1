package joc;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Meteorit1 extends Enemic {
	BufferedImage Imatge;
	//número que es restarà a la vida de la nau cada cop que xoquem amb aquest tipus de meteorit
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
		llargada=34;
		altura=34;
		vida=1;
	}
	void pinta(Graphics g) {
		g.drawImage(Imatge,x,y,null);
	}
	void moure() {
		x=xInicial-joc.c.xFisiques+varX;
		y=yInicial-joc.c.yFisiques;
		varX-=v;
		if(Math.abs(Nau.x-this.x)>1500 || Math.abs(Nau.y+joc.c.yFisiques-this.y)>1500) {
			isVisible=false;
		}else {
			isVisible=true;
		}
	}
	void dispara() {
		
	}
}
