package joc;

import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.Color;


public class Meteorit1 extends Enemic {
	static float llargadaRelativa[] = new float[] {(float)78./1440, (float)78./1440,(float)34./1440 }; //78 34
	static float alturaRelativa = (float)34./900; //mides relatives a la mida de la pantalla
	float angle; //angle de la trajectòria del meteorit respecte l'eix de les x en radians
	BufferedImage imatgeRotada;
	int vx,vy;
	AffineTransform moure;
	int diagonal;
	int n;
	boolean curt;//quina mida és el meteorit
	public Meteorit1(Joc joc){
		super(joc,true);
		this.angle = Math.round(Joc.r.nextInt()%360*(2*Math.PI/360)); //hauriem de restringir els angles, els meteorits que s'allunyen de la nau no cal que els fabriquem 
		this.bodyDamage = 25;
		this.v=Joc.r.nextInt(8)+10; 
		this.vx = Math.round((float)(v*Math.cos(angle)));
		this.vy = Math.round((float)(v*Math.sin(angle)));
		this.xoc=0;
		this.mort=false;
		curt = false;
		nombreImatges = 34;
		imatges = new BufferedImage[34];
		if(this.v<=12) {//en funcio de la velocitat el meteorit tindrà més foc o menys
		nombreImatges=1;
		imatges = new BufferedImage[1];
		imatges[0]=Nau.rota(joc.imatgesMeteorits[2],angle);
//		this.imatgeRotada = Nau.rota(Imatge,angle);
		curt = true;
		}
		if(this.v>12&&this.v<=14) { 
//			this.Imatge=joc.imatgesMeteorits[1];
			for(int i=0;i<34;i++) {
				imatges[i] = Nau.rota(joc.imatgesMeteorit11[i],angle);
//				imatgesRotades[i] = Nau.rota(imatges[i], angle);
			}
			n=0;
		}
		if(this.v>14) {
			for(int i=0;i<34;i++) {
				imatges[i] = Nau.rota(joc.imatgesMeteorit12[i],angle);
//				imatgesRotades[i] = Nau.rota(imatges[i], angle);
			}
			n=0;
//			this.Imatge=joc.imatgesMeteorits[0];
//			this.imatgeRotada = Nau.rota(Imatge,angle);
		}
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
		if(curt) diagonal= Math.round((float)Math.sqrt(altura*altura+altura*altura)); //diagonals imatges
		else {diagonal= Math.round((float)Math.sqrt(78./34*altura*78./34*altura+altura*altura));}
		hitBox = new Rectangle(x,y,altura,altura);
		r = Joc.r.nextInt(2);
	}
	void pinta() {
		if(this.v<=12) g.drawImage(imatges[0],xPinta,yPinta,null);
		else{
			n+=1;
			n=n%34;
			g.drawImage(imatges[n],xPinta,yPinta,null);
			}
	}
	void moure() {
		x=xInicial-joc.c.xFisiques+varX;
		y=yInicial-joc.c.yFisiques+varY;
		if(curt) {
		xPinta = Math.round((float)(x - diagonal/2 + altura/2));
		yPinta = Math.round((float)(y - diagonal/2 + altura/2));
		}
		else {
		xPinta = Math.round((float)(x-diagonal/2+(22./34*altura)*Math.cos(angle))+altura/2); 
		yPinta = Math.round((float)(y-diagonal/2+(22./34*altura)*Math.sin(angle))+altura/2);
		}
		xCentre = x+llargada/2;
		yCentre = y+altura/2;
		varY-=vy;
		varX-=vx;
		hitBox.setLocation(x,y);
		}
	void dispara() {
		
	}
}
