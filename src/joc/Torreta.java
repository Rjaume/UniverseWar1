package joc;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.ArrayList;

public class Torreta extends Enemic{
	double tempsUltimTret;
	BufferedImage imatge;
	int xCentre, yCentre;
	int mitjaDiagonal;
	boolean mort;
	Double angleNau; //angle respecte l'horitzontal de la torreta per a estar apuntant la nau
	double tempsEntreTrets; //en segons 
	static int velocitatBales = 120;
	ArrayList<Bala> bales = new ArrayList<Bala>();
	static float llargadaRelativa = (float)40./1440, alturaRelativa = (float)40./900; // a canviar quan fem el disseny
	public Torreta(Joc joc, int x,  int y, double tempsEntreTrets ) {
		super(joc,x,y);
		this.llargada = joc.midaTorreta;
		this.altura = joc.midaTorreta;
		this.llargadaMinimapa = joc.midaTorretaM;
		this.alturaMinimapa = joc.midaTorretaM;
		this.tempsEntreTrets = tempsEntreTrets;
		bodyDamage = 10;
		imatge = joc.imatgeEnemic2;
		vida = 5;
		xoc = 0;
		tempsUltimTret = 0;
		calculatXoc =false;
		mort = false;
		angleNau = (double) 0;
		mitjaDiagonal = (int)Math.round(Math.sqrt(llargada*llargada*4+altura*altura*4)/2); //diagonal de la imatge de la torreta que originalment fa 80x80 (la torreta fa 40x40)
		color[0] = 255;//color mitjà particules
		color[1] = 255;
		color[2] = 255;
		midaParticules = 8; //mida mitjana particules
		nombreParticules = 12;
	}
	void pinta() {
		BufferedImage imatgeRotada = Nau.rota(imatge, angleNau);
		g.drawImage(imatgeRotada,x-(mitjaDiagonal-llargada/2),y-(mitjaDiagonal-altura/2),null);//la imatge és més gran que la torreta realment, per tant restem (tenim en compte rota()) 
	}
	void dispara() {
		if(Math.abs(tempsUltimTret-System.currentTimeMillis())>tempsEntreTrets*1000) {
				tempsUltimTret=System.currentTimeMillis();
				bales.add(new Bala(this,joc));
			}
	}
	void moure() {
		x=xInicial-joc.c.xFisiques;
		y=yInicial-joc.c.yFisiques;
		xCentre = x+llargada/2;
		yCentre = y+altura/2;
		Double xd = (double) (Nau.xCentre-this.xCentre); //vector dispar
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
	}
}
