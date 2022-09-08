package joc;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Torreta extends Enemic{
	double tempsUltimTret;
	int nombreDeCanons;
	int xCentre, yCentre;
	int mitjaDiagonal;
	boolean mort;
	Double angleNau; //angle respecte l'horitzontal de la torreta per a estar apuntant la nau
	double tempsEntreTrets; //en segons 
	int velocitatBales;
	static double angle4 = Math.PI*1/2, angle8 = Math.PI*1/4;
	static float llargadaRelativa = (float)40./1440, alturaRelativa = (float)40./900; // a canviar quan fem el disseny
	public Torreta(Joc joc, int x,  int y, double tempsEntreTrets, int nombreDeCanons,int velocitatBales) {
		super(joc,x,y);
		this.velocitatBales = velocitatBales;
		this.llargada = joc.midaTorreta;
		this.altura = joc.midaTorreta;
		this.llargadaMinimapa = joc.midaTorretaM;
		this.alturaMinimapa = joc.midaTorretaM;
		this.tempsEntreTrets = tempsEntreTrets;
		this.nombreDeCanons = nombreDeCanons; // 1, 4 o 8
		bodyDamage = 10;
		nombreImatges = 1;
		imatges = new BufferedImage[nombreImatges];
		switch(nombreDeCanons) {
		case 1:
			imatges[0] = joc.imatgesTorreta[0];
			break;
		case 4:
			imatges[0] = joc.imatgesTorreta[1];
			break;
		case 8:
			imatges[0] = joc.imatgesTorreta[2];
			break;
		}
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
		hitBox = new Rectangle(x,y,llargada,altura);
	}
	void pinta() {
		BufferedImage imatgeRotada = Nau.rota(imatges[0], angleNau);
		g.drawImage(imatgeRotada,x-(mitjaDiagonal-llargada/2),y-(mitjaDiagonal-altura/2),null);//la imatge és més gran que la torreta realment, per tant restem (tenim en compte rota()) 
	}	
	void dispara() {
		if(Math.abs(tempsUltimTret-System.currentTimeMillis())>tempsEntreTrets*1000) {
			tempsUltimTret=System.currentTimeMillis();
		switch(nombreDeCanons) {
		case 1:
				bales.add(new Bala(joc,this));
			break;
		case 4:
			for(int i=0;i<4;i++) bales.add(new Bala(joc, this, velocitatBales, angleNau + angle4*i));
			break;
		case 8:
			for(int i=0;i<8;i++) bales.add(new Bala(joc,this,velocitatBales, angleNau + angle8*i));
			break;
		}
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
		hitBox.setLocation(x,y);
	}
}
