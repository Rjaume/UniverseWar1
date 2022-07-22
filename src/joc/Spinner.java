package joc;
import java.awt.Rectangle;

public class Spinner extends Enemic {
	double angleNau; //per a fer l'atac
	double vx,vy;
	int n; //per l'animació 
	static float midaRelativa = (float) (50./1440); 
	public Spinner(Joc joc, int x, int y) {
		super(joc, x, y);
		v=8; //velocitat de l'atac
		hitBox = new Rectangle(x,y,llargada,altura);
		this.altura = joc.midaSpinner;
		this.llargada = joc.midaSpinner;
		llargadaMinimapa = joc.midaSpinnerM;
		alturaMinimapa = joc.midaSpinnerM;
		hitBox = new Rectangle(x,y,llargada,altura);
		mort = false;
		isVisible = true;
		n=0;
		vida=1;
		bodyDamage = 10;
		nombreParticules = 5;
		midaParticules = 8;
		color[0] = 0;
		color[1] = 100;
		color[2] = 200;
	}

	@Override
	void pinta() {
		g.drawImage(joc.imatgeSpinner[n],x,y,null);
		n+=1;
		n=n%18;
	}

	@Override
	void dispara() { //roda cap a la posició de la nau destruint el que té al davant
//		if(System.currentTimeMillis()-tempsUltimAtac > tempsEntreAtacs*1000) {
			if(Math.abs(xCentre-Nau.xCentre)>10 | Math.abs(yCentre-Nau.yCentre)>10){
				vx = v*Math.cos(angleNau);
				vy = v*Math.sin(angleNau);
			}
			else {
				vx=0;
				vy=0;
			}
		}
//	}

	@Override
	void moure() {
			x=xInicial-joc.c.xFisiques+varX;
			y=yInicial-joc.c.yFisiques+varY;
			xCentre = x+llargada/2;
			yCentre = y+altura/2;
			Double xd = (double) (Nau.xCentre-this.xCentre);
			Double yd = (double) (Nau.yCentre-this.yCentre);
			if(yd>0 && xd>0) { //recorda que l'eix de les y està invertit (escullo una determinació de l'angle que lligui amb la funció rota)
				angleNau = Math.atan(yd/xd);}
			if(yd>0 && xd<0) {
				angleNau = Math.PI-Math.atan(-yd/xd);
			}
			if(yd<0 && xd<0) {
				angleNau = Math.PI+Math.atan(yd/xd);
			}
			if(yd<0 && xd>0) {
				angleNau = 2*Math.PI - Math.atan(-yd/xd);
			}	
			hitBox.setLocation(x,y);
		if(isVisible) {
			varY+=vy;
			varX+=vx;
		}
	}
	

}
