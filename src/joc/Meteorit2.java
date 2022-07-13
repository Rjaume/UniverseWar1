package joc;

import java.awt.image.BufferedImage;

public class Meteorit2 extends Enemic{
	BufferedImage meteorit,meteoritEsquerdat;
	static float llargadaRelativa = (float)90./1440, alturaRelativa = (float)90./900; //mides relatives a la mida de la pantalla
	float angle; //angle de la trajectòria del meteorit respecte l'eix de les x en radians
	BufferedImage imatgeRotada1,imatgeRotada2;
	int vx,vy;
	public Meteorit2(Joc joc) {
		super(joc,true);
		this.angle = Math.round(Joc.r.nextInt()%360*(2*Math.PI/360)); //hauriem de restringir els angles, els meteorits que s'allunyen de la nau no cal que els fabriquem 
		this.bodyDamage = 50;
		this.v=Joc.r.nextInt(9)+9;  
		this.vx = Math.round((float)(v*Math.cos(angle)));
		this.vy = Math.round((float)(v*Math.sin(angle)));
		this.xoc=0; //nombre de xocs inicials és 0
		this.mort=false;
		meteorit=joc.imatgesMeteorits[3];
		meteoritEsquerdat=joc.imatgesMeteorits[4];
		this.imatgeRotada1 = Nau.rota(meteorit,angle);
		this.imatgeRotada2 = Nau.rota(meteoritEsquerdat,angle);
		llargada=joc.llargadaMeteorit2;
		altura=joc.alturaMeteorit2;
		vida=2;
		alturaMinimapa = joc.llargadaMeteorit2M;
		llargadaMinimapa = joc.llargadaMeteorit2M;
		balesInicials=0; //els meteorits no tenen bales
		color[0] = 160;//color mitjà particules
		color[1] = 80;
		color[2] = 20;
		midaParticules = 6; //mida mitjana particules 10
		nombreParticules =20;//12
	}
	
	void pinta() {
		if(this.xoc==0) {
			g.drawImage(imatgeRotada1,x,y,null);
		}
		if(this.xoc==1) {
			g.drawImage(imatgeRotada2,x,y,null);
		}
	}
	void moure() {
		x=xInicial-joc.c.xFisiques+varX;
		y=yInicial-joc.c.yFisiques+varY;
		varY-=vy;
		varX-=vx;
//		x=xInicial-joc.c.xFisiques+varX;
//		y=yInicial-joc.c.yFisiques;
//		varX-=v;
	}
	void dispara() {
	}
}
