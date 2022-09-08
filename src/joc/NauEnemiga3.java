package joc;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class NauEnemiga3 extends Enemic {
	static float llargadaRelativa = (float)70./1440, alturaRelativa = (float)56./900;
	int velocitatEsquivar, velocitatMoviment;//15 i 5 son valors raonables
	BufferedImage imatgesRotades[] = new BufferedImage[4];
	double angleNau;
	int varX, varY;
	double tempsEntreTrets;
	double tempsUltimTret;
	double tempsCarrega, tempsDispar, tempsTotalDispar;
	double vxNau, vyNau, vxMeteorits, vyMeteorits;
	double vx, vy;
	int mitjaDiagonal;
	int n; //per l'animació
	Laser laser;
	boolean disparant;
	double multiplicadorAltura;
	HashMap<Enemic, Integer> meteoritsXoc = new HashMap<Enemic, Integer>(); //assignem 0 o 1 a cada meteorit prou proper a la nau per a decidir si esquivem cap a un costat o cap a l'altre
	HashMap<Bala, Integer> balesXoc = new HashMap<Bala, Integer>(); //anàleg per les bales
	NauEnemiga3(Joc joc, int x, int y, double tempsEntreTrets, double tempsCarrega, double tempsDispar, double multiplicadorAltura, int velocitatEsquivar) {
		super(joc, x, y);
		this.tempsEntreTrets = tempsEntreTrets;
		this.tempsCarrega = tempsCarrega;
		this.tempsDispar = tempsDispar;
		this.multiplicadorAltura = multiplicadorAltura;
		this.velocitatEsquivar = velocitatEsquivar;
		velocitatMoviment = 5 + Joc.r.nextInt(3)-2;
		tempsTotalDispar = tempsCarrega + tempsDispar;
		llargada = joc.llargadaNauEnemiga2; //té les mateixes mides que nauEnemiga2
		altura = joc.alturaNauEnemiga2;
		llargadaMinimapa = joc.llargadaNauEnemiga2M;
		alturaMinimapa = joc.alturaNauEnemiga2M;
		angleNau = (double) 0;
		hitBox = new Rectangle(x,y,llargada,altura);
		varX = 0;
		varY = 0;
		xCentre = x + llargada/2;
		yCentre = y + altura/2;
		xoc = 0;
		vida = 5;
		nombreImatges = 4;
		imatges = new BufferedImage[nombreImatges];
		color[0] = 200;//color mitjà particules
		color[1] = 200;
		color[2] = 200;
		bodyDamage = 5;
		midaParticules = 7; //mida mitjana partícules
		nombreParticules = 10;
		mitjaDiagonal = (int)Math.round(Math.sqrt(llargada*llargada+altura*altura)/2);
		for(int i=0;i<3;i++) imatges[i] = joc.imatgesNauEnemiga2[i]; //joc.imatgesNauEnemiga3
		imatges[3] = joc.imatgesNauEnemiga2[0];
		isVisible = true;
		mort = false;
		n=0;
		tempsUltimTret = 0;
		for(int i=0;i<4;i++) imatgesRotades[i] = Nau.rota(imatges[i], angleNau);
	}

	void pinta() {
//		System.out.println(tempsTotalDispar);
//		System.out.println("x:  " + laser.xPinta);
//		System.out.println("y:  "+laser.yPinta);
//		g.drawLine(laser.xPinta+200,laser.yPinta,laser.xPinta-200,laser.yPinta);
//		g.drawLine(laser.xPinta,laser.yPinta+200,laser.xPinta,laser.yPinta-200);
//		g.drawLine(x+200,y,x-200,y);
//		g.drawLine(x,y+200,x,y-200);
		if(!disparant) for(int i=0;i<4;i++) {
			//girar fins a apuntar la nau, girar de laser.angledispar a angleNau en un temps: tempsEntreDispars-tempsTotalDispars
			imatgesRotades[i] = Nau.rota(imatges[i], angleNau);
		}
		n+=1;
		n=n%4;
		g.drawImage(imatgesRotades[n],x-(mitjaDiagonal-llargada/2),y-(mitjaDiagonal-altura/2),null);
	}

	void dispara() {
		if(Math.abs(tempsUltimTret-System.currentTimeMillis())>tempsEntreTrets*1000) {
			tempsUltimTret=System.currentTimeMillis();
			laser = new Laser(joc, this, tempsCarrega, tempsDispar, multiplicadorAltura);
		}
		if(Math.abs(tempsUltimTret-System.currentTimeMillis())<tempsTotalDispar*1000) disparant = true;
		else disparant = false;
	}

	void moure() {
		if(!joc.c.mort) {
			boolean xocaNau;
			xocaNau = false;
			Line2D.Double recta;
			Line2D.Double recta2; //per a millorar la presició en el cas dels meteorits
			Line2D.Double recta3; 
//			hitBox.width+=10; //per esquivar millor
//			hitBox.height+=10;
//			hitBox.x-=5;
//			hitBox.y-=5;
			for(int i=0 ; i<joc.c.nbales; i++) { //afegim a rectesNau les rectes definides per bales de la nau que tallen la nauEnemiga.
				//esquiva l'última bala de la nau disparada que està dins del cercle centrat a la nau i de radi la distància entre la nau i la nauenemiga + 100.
				if(Math.abs(Nau.xCentre-joc.c.bales[i].x)<Math.abs(Nau.xCentre-xCentre)+100 & Math.abs(Nau.yCentre-joc.c.bales[i].y)<Math.abs(Nau.yCentre-yCentre)+100) {
//					if(!balesXoc.containsKey(joc.c.bales[i])) balesXoc.put(joc.c.bales[i],Joc.r.nextInt(2));
					balesXoc.putIfAbsent(joc.c.bales[i], Joc.r.nextInt(2));
					int aux1 = Nau.xCentre+10*(joc.c.bales[i].x-Nau.xCentre);
					int aux2 = Nau.yCentre+10*(joc.c.bales[i].y-Nau.yCentre);
					recta = new Line2D.Double(Nau.xCentre, Nau.yCentre, aux1, aux2);
					if(recta.intersects(hitBox)) {
						xocaNau = true; 
					}
					if(xocaNau) { //
						double norma = Math.sqrt((recta.getY1()-recta.getY2())*(recta.getY1()-recta.getY2())+(recta.getX2()-recta.getX1())*(recta.getX2()-recta.getX1()));
						if(balesXoc.get(joc.c.bales[i])==1) {
							vxNau = velocitatEsquivar * Math.round((float)((recta.getY1()-recta.getY2())/norma));
							vyNau = velocitatEsquivar * Math.round((float)((recta.getX2()-recta.getX1())/norma));
						}
						else {
							vxNau = velocitatEsquivar * Math.round((float)((recta.getY2()-recta.getY1())/norma));
							vyNau = velocitatEsquivar * Math.round((float)((recta.getX1()-recta.getX2())/norma));
						}
					}
				}
//				else if(balesXoc.containsKey(joc.c.bales[i])) balesXoc.remove(joc.c.bales[i]);
				else balesXoc.remove(joc.c.bales[i]);
			}
			//Anàleg amb els meteorits:
			boolean xocaMeteorit=false;
			for(Enemic enemic : joc.enemics) {
				if(enemic instanceof Meteorit1 | enemic instanceof Meteorit2) {
					if(Math.abs(enemic.xCentre - xCentre)<200 & Math.abs(enemic.yCentre - yCentre)<200) { //circumferència on mirem si xoquem amb meteorits 
						meteoritsXoc.putIfAbsent(enemic, Joc.r.nextInt(2));
						//construeixo la recta definida per la trajectòria del meteorit
						int aux1 = enemic.xInicial+enemic.llargada/2-joc.c.xFisiques+10*(enemic.xCentre-enemic.xInicial-enemic.llargada/2+joc.c.xFisiques);
						int aux2 = enemic.yInicial+enemic.altura/2-joc.c.yFisiques+10*(enemic.yCentre-enemic.yInicial-enemic.altura/2+joc.c.yFisiques);
						int aux3 = enemic.xInicial + enemic.llargada/2-joc.c.xFisiques+10*(enemic.x+enemic.llargada/2-enemic.xInicial-enemic.llargada/2+joc.c.xFisiques);
						int aux4 = enemic.yInicial-joc.c.yFisiques + 10*(enemic.y-enemic.yInicial+joc.c.yFisiques);
						int aux5 = enemic.xInicial + enemic.llargada/2-joc.c.xFisiques+10*(enemic.x + enemic.llargada/2 - enemic.xInicial - enemic.llargada/2 + joc.c.xFisiques);
						int aux6 = enemic.yInicial + enemic.altura - joc.c.yFisiques+10*(enemic.y + enemic.altura - enemic.yInicial - enemic.altura + joc.c.yFisiques);
						recta = new Line2D.Double(enemic.xInicial+enemic.llargada/2-joc.c.xFisiques, enemic.yInicial + enemic.altura/2-joc.c.yFisiques, aux1, aux2);
						recta2 = new Line2D.Double(enemic.xInicial+enemic.llargada/2-joc.c.xFisiques, enemic.yInicial - joc.c.yFisiques,aux3, aux4);
						recta3 = new Line2D.Double(enemic.xInicial + enemic.llargada/2-joc.c.xFisiques, enemic.yInicial + enemic.altura - joc.c.yFisiques,aux5, aux6 );
						if(recta.intersects(hitBox) | recta2.intersects(hitBox) | recta3.intersects(hitBox)) {
							xocaMeteorit = true; 
						}
						if(xocaMeteorit) {
							double norma = Math.sqrt((recta.getY1()-recta.getY2())*(recta.getY1()-recta.getY2())+(recta.getX2()-recta.getX1())*(recta.getX2()-recta.getX1()));
							if(meteoritsXoc.get(enemic) == 1) {
								vxMeteorits = velocitatEsquivar * Math.round((float)((recta.getY1()-recta.getY2())/norma));
								vyMeteorits = velocitatEsquivar * Math.round((float)((recta.getX2()-recta.getX1())/norma));
							}
							else {
								vxMeteorits = velocitatEsquivar * Math.round((float)((recta.getY2()-recta.getY1())/norma));
								vyMeteorits = velocitatEsquivar * Math.round((float)((recta.getX1()-recta.getX2())/norma));
							}			
						}
					}
					else meteoritsXoc.remove(enemic);
				}
			}
			vxNau*=0.85;
			vyNau*=0.85;
			vxMeteorits*=0.85;
			vyMeteorits*=0.85;
			//afegim velocitat fixe cap a la nau 
			double vxfixe = 0;
			double vyfixe = 0;
			double xd = Nau.xCentre - xCentre;
			double yd = Nau.yCentre - yCentre;
			double norma = Math.sqrt(xd*xd+yd*yd);
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
			if(!xocaMeteorit) { //no sé si això fa que millori el moviment 
				vxfixe = velocitatMoviment*xd/norma;
				vyfixe = velocitatMoviment*yd/norma;
			}
			vx = vxNau + vxMeteorits - vxfixe;
			vy = vyNau + vyMeteorits - vyfixe;
			varY-=Math.round((float)vy);
			varX-=Math.round((float)vx);
			x=xInicial-joc.c.xFisiques+varX;
			y=yInicial-joc.c.yFisiques+varY;
			xCentre = x + llargada/2;
			yCentre = y + altura/2;
			hitBox.setLocation(x,y);
			}
	}

}
