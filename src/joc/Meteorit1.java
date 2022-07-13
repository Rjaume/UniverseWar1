package joc;

import java.awt.image.BufferedImage;

public class Meteorit1 extends Enemic {
	BufferedImage Imatge;//
	static float llargadaRelativa[] = new float[] {(float)78./1440, (float)78./1440,(float)34./1440 };
	static float alturaRelativa = (float)34./900; //mides relatives a la mida de la pantalla
	float angle; //angle de la trajectòria del meteorit respecte l'eix de les x en radians
	BufferedImage imatgeRotada;
	int vx,vy;
	public Meteorit1(Joc joc){
		super(joc,true);
		this.angle = Math.round(Joc.r.nextInt()%360*(2*Math.PI/360)); //hauriem de restringir els angles, els meteorits que s'allunyen de la nau no cal que els fabriquem 
		this.bodyDamage = 25;
		this.v=Joc.r.nextInt(8)+12; 
		this.vx = Math.round((float)(v*Math.cos(angle)));
		this.vy = Math.round((float)(v*Math.sin(angle)));
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
		this.imatgeRotada = Nau.rota(Imatge,angle);
		altura=joc.alturaMeteorit1;
		llargada=altura; //a efectes dels xocs l'altura és la mateixa que la llargada.
		alturaMinimapa = joc.llargadaMeteorit1M;
		llargadaMinimapa = joc.llargadaMeteorit1M;
		vida=1;
		color[0] = 160; //color mitjà particules
		color[1] = 80;
		color[2] = 20;
		midaParticules = 6; //mida mitjana partícules
		nombreParticules = 7;
	}
	void pinta() {
		g.drawImage(imatgeRotada,x,y,null);
	}
	void moure() {
		x=xInicial-joc.c.xFisiques+varX;
		y=yInicial-joc.c.yFisiques+varY;
		varY-=vy;
		varX-=vx;
		//perquè ho moc diferent que a bala ? 
		}
	void dispara() {
		
	}
}
