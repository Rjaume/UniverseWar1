package joc;
import java.awt.Graphics; 
import java.awt.Color;
public class Minimap {
	Joc joc;
	Graphics g;
	Nau c;
	static float llargadaRelativa = (float)174./1440, alturaRelativa = (float)174./900; //mides relatives a la mida de la pantalla
	static int x,y,llargada,altura; //posició del minimapa a la pantalla i mides
	static int xR,yR,llargadaRectangle, alturaRectangle; //posició i mida del rectangle que simula el que veiem a la pantalla dins el minimapa
	static int x0,y0; //posició on pintem la nau al minimapa
	static float midaPuntRadarRelativa=(float) (10./1440);
	static float llargadaNauRelativa = (float) (4./1440), alturaNauRelativa = (float) (3./900);
	static float llargadaMeteorit1Relativa = (float) (2./1440);
	static float llargadaMeteorit2Relativa = (float) (4./1440);
	static float midaNauEnemiga1Relativa = (float) (4./1440);
	static float llargadaForatNegreRelativa = (float) (2./1440);
	static float llargadaCheckpointRelativa = (float) (3./1440), alturaCheckpointRelativa = (float) (3./900);
	static float midaPaquetMunicioRelativa = (float) (2./1440);
	static float midaTorretaRelativa = (float) (3./1440);
	static float midaSpinnerRelativa = (float) (3./1440);
	static float llargadaNauEnemiga2Relativa = (float) (4./1440);
	static float alturaNauEnemiga2Relativa = (float) (3./900);
	static int midaPuntRadar; //mida de l'indicador del radar
	static int p1,p2; //punt on hem de pintar el quadrat del radar 
	static Color seethrough_gray = new Color((float)0.5,(float)0.5,(float)0.5,(float)0.5);
	public Minimap(Joc joc) {
		this.joc=joc;
		c=joc.c;
		g=joc.g;
		llargada = joc.llargadaMinimapa;
		altura = joc.alturaMinimapa;
		x=joc.f.AMPLADA-llargada;
		y=joc.f.ALTURA-altura;
		llargadaRectangle = Math.round(llargada/2);
		alturaRectangle = Math.round(altura/3);
		xR=x+Math.round(llargadaRectangle/2);
		yR=y+alturaRectangle;
		x0=x+llargada/2-c.llargadaMinimapa/2;
		y0=y+altura/2-c.alturaMinimapa/2;
		midaPuntRadar=joc.midaPuntRadar;
	}
	void pinta() {
		//Layout
		g.setColor(Color.BLACK);
		g.fillRect(x,y,llargada-1,altura-1);
		g.setColor(seethrough_gray);
		g.drawRect(xR,yR,llargadaRectangle,alturaRectangle);
		//Nau
		g.setColor(Color.GREEN);
		g.fillRect(x0,y0,c.llargadaMinimapa, c.alturaMinimapa);
		//Enemics
		for (Enemic enemic: joc.enemics) {
			if(enemic instanceof Meteorit1 | enemic instanceof Meteorit2 | enemic instanceof ForatNegre) g.setColor(Color.RED);
			else g.setColor(Color.BLUE);
			if(enemic.isInMinimap && !enemic.mort) {
				int xM = xR+llargadaRectangle*enemic.x/joc.f.AMPLADA;
				int yM = yR+alturaRectangle*enemic.y/joc.f.ALTURA;
				g.fillRect(xM,yM,enemic.llargadaMinimapa,enemic.alturaMinimapa);
			}
		}
		//Checkpoints
		for(Checkpoint checkpoint: joc.checkpoints) {
			if(checkpoint.isInMinimap) { //minimapa
				int xM = xR+llargadaRectangle*checkpoint.x/joc.f.AMPLADA;
				int yM = yR+alturaRectangle*checkpoint.y/joc.f.ALTURA;
				g.setColor(Color.WHITE);
				g.fillOval(xM,yM,checkpoint.llargadaMinimapa,checkpoint.alturaMinimapa);
			}
			else { //radar fora minimapa
				calculaPunt(checkpoint);
				g.setColor(Color.BLUE);
				g.fillRect(p1,p2,midaPuntRadar,midaPuntRadar);
			}
		}
		//paquets de munició
		for(PaquetMunicio paquet: joc.paquetsmunicio) {
			if(!paquet.agafat) {
				if(paquet.isInMinimap) {
					int xM = xR+llargadaRectangle*paquet.x/joc.f.AMPLADA;
					int yM = yR+alturaRectangle*paquet.y/joc.f.ALTURA;
					g.setColor(Color.GREEN);
					g.fillRect(xM,yM,paquet.llargadaMinimapa,paquet.alturaMinimapa);
				}
				else { //radar fora minimapa
					calculaPunt(paquet);
					g.setColor(Color.GREEN);
					g.fillRect(p1,p2,midaPuntRadar,midaPuntRadar);
				}
			}
		}
		
		//marc 
		g.setColor(Color.WHITE);
		g.drawRect(x-1,y-1,llargada,altura);
	}
	void calculaPunt(Objecte objecte) { //mirem amb quin segment talla la semirecta definida pel centre de la nau i la posició de l'objecte. (Pels objectes tracked pel radar)
		double x1 = Minimap.xR+Minimap.llargadaRectangle*objecte.x/joc.f.AMPLADA; //posició objecte al minimapa
		double y1 = Minimap.yR+Minimap.alturaRectangle*objecte.y/joc.f.ALTURA;
		int A = Math.round(altura/2);
		int L = Math.round(llargada/2);
		double x=((A)/(y1-y0))*(x1-x0)+x0;
		double lambda = (x-x0)/(x1-x0);
		double y=((y1-y0)/(x1-x0))*(x-x0)+y0;
		if(Math.abs(y-y0)<=A+0.1 & Math.abs(x-x0)<=L+0.1 & lambda>0) { //recta d'abaix
			p1=Math.round(Math.round(x));
			p2=(int) (y0+A)-midaPuntRadar-1;
		}
		x=((-A)/(y1-y0))*(x1-x0)+x0;
		y=((y1-y0)/(x1-x0))*(x-x0)+y0;
		lambda = (x-x0)/(x1-x0);
		if(Math.abs(y-y0)<=A+0.1 & Math.abs(x-x0)<=L+0.1 & lambda>0) { //recta de dalt
			p1=Math.round(Math.round(x));
			p2=(int) (y0-A)+1;
		}
		y=((-L)/(x1-x0))*(y1-y0)+y0;
		x=((x1-x0)/(y1-y0))*(y-y0)+x0;
		lambda = (y-y0)/(y1-y0);
		if(Math.abs(x-x0)<=L+0.1 & Math.abs(y-y0)<=A+0.1 & lambda>0) { //recta de la esquerre
			p1=(int) (x0-L)+1;
			p2=Math.round(Math.round(y));
		}
		y=((L)/(x1-x0))*(y1-y0)+y0;
		x=((x1-x0)/(y1-y0))*(y-y0)+x0;
		lambda = (y-y0)/(y1-y0);
		if(Math.abs(x-x0)<=L+0.1 & Math.abs(y-y0)<=A+0.1 & lambda>0) { //recta de la dreta
			p1=(int) (x0+L)-midaPuntRadar-1;
			p2=Math.round(Math.round(y));
		}
	}
	
}
