package joc;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; //listener pel teclat
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener; //listener pels botons del ratolí
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class Personatge extends Objecte implements KeyListener, MouseListener, MouseMotionListener{
	int xRatoli, yRatoli;
	int xPinta,yPinta;//les imatges tenen espai buit al voltant per a poder rotar (ALGUNES)
	static double tempsSalt = 0.1; //temps que donem força cap amunt al saltar
	double tempsUltimSalt;
	boolean terra,esquerre,dreta,sostre; //mesura si xoquem amb cada un dels costats
	int nbales;
	Bala bales[];
	static int balesInicials =1000, velocitatBales = 200;
	static float multiplicadorMidaRelatiu = 2f/1920;
	float Mmida;
	int n; //l'usem per a l'animació 
	boolean fletxaDreta,fletxaEsquerre,fletxaAmunt,fletxaAvall, fletxaDreta2, fletxaEsquerre2;
	boolean escaloDreta, escaloEsquerre, sobreEscalesDreta, sobreEscalesEsquerre;
	double ultimCopFletxaDreta, ultimCopFletxaEsquerre;
	float llargadaImatge = 33 , alturaImatge = 46;
	double alpha; //angle recta personatge-ratolí amb l'horitzontal
	boolean gun; //true if our character has the gun in his hands
	int xFisiques,yFisiques; //on calculem les físiques (posició respecte la posició inicial), que usarem per a moure la resta d'objectes.
//	static float FJump=3000, FMove = 10000 , Ff=3000, M=60; //Força dels motors de la nau, Força de fregament, Massa de la nau i Velocitat maxima de la nau
	float Vx,Vy,Fx,Fy,Ffx,Nx,Ny,vMax;
	static float FG=2000;//força deguda a la gravetat
	BufferedImage idled[] = new BufferedImage[2], idleGd[] = new BufferedImage[2];
	BufferedImage idlee[] = new BufferedImage[2], idleGe[] = new BufferedImage[2];
	BufferedImage walkd[] = new BufferedImage[6], walkGd[] = new BufferedImage[6];
	BufferedImage walke[] = new BufferedImage[6], walkGe[] = new BufferedImage[6];
	BufferedImage rund[] = new BufferedImage[8], runGd[] = new BufferedImage[8];
	BufferedImage rune[] = new BufferedImage[8], runGe[] = new BufferedImage[8];
	BufferedImage deathd[] = new BufferedImage[2], deathGd[] = new BufferedImage[2];
	BufferedImage deathe[] = new BufferedImage[2], deathGe[] = new BufferedImage[2];
	BufferedImage jumpd[] = new BufferedImage[2], jumpGd[] = new BufferedImage[2];
	BufferedImage jumpe[] = new BufferedImage[2], jumpGe[] = new BufferedImage[2];
	BufferedImage crouchd, crouche, crouchGd, crouchGe;
	BufferedImage ladder[] = new BufferedImage[3];
	BufferedImage idlerotaciod[] = new BufferedImage[14];
	BufferedImage idlerotacioe[] = new BufferedImage[14];
	BufferedImage walkrotaciod[]  = new BufferedImage[42];
	BufferedImage walkrotacioe[]  = new BufferedImage[42];	
	int nidle, nwalkd, nwalke, nrund, nrune, ndeathd, ndeathe, njumpd, njumpe, nladder; //per les animacions
	String ultimaAccio;
	boolean pintat;
	boolean sobreEscalesVerticals; //comprova si som sobre unes escales verticals, o no
	Personatge(Joc joc){
		joc.f.addMouseMotionListener(this);
		joc.f.addMouseListener(this);
		this.joc=joc;
		this.g = joc.g;
		Mmida = joc.multiplicadorMidaPersonatge;
		llargada = Math.round(22*Mmida); //inicialment tenim la mida de idle
		altura = Math.round(45*Mmida);
		xCentre=joc.f.AMPLADA/2;
		yCentre=joc.f.ALTURA/2;
		x=joc.f.AMPLADA/2-llargada/2; 
		y=joc.f.ALTURA/2-altura/2+joc.f.ALTURA/6;
		llargadaImatge = llargadaImatge*Mmida;
		alturaImatge = alturaImatge*Mmida;
		xPinta = Math.round(joc.f.AMPLADA/2-llargadaImatge/2); //algunes imatges tenen espai buit al voltant
		yPinta = Math.round(joc.f.ALTURA/2-alturaImatge/2+joc.f.ALTURA/6);
		hitBox = new Rectangle(x,y,llargada,altura);
//		xFisiques=2000;
//		yFisiques=-1000;//-400
		xFisiques = 0;
		yFisiques = 3900;
//		yFisiques =2000;
		xRatoli = 0;
		yRatoli = 0;
		nbales = 0;
		bales=new Bala[balesInicials];
		n=0;
		idled[0] = joc.imatgesPersonatge[0];
		idled[1] = joc.imatgesPersonatge[38];
		idlee[0] = joc.imatgesPersonatge[1];
		idlee[1] = joc.imatgesPersonatge[39];
		jumpd[0] = joc.imatgesPersonatge[34];
		jumpd[1] = joc.imatgesPersonatge[35];
		jumpe[0] = joc.imatgesPersonatge[36];
		jumpe[1] = joc.imatgesPersonatge[37];
		deathe[0] = joc.imatgesPersonatge[30];
		deathe[1] = joc.imatgesPersonatge[31];
		deathd[0] = joc.imatgesPersonatge[32];
		deathd[1] = joc.imatgesPersonatge[33];
		idleGd[0] = joc.imatgesPersonatgePistola[38];
		idleGd[1] = joc.imatgesPersonatgePistola[0];
		idleGe[0] = joc.imatgesPersonatgePistola[1];
		idleGe[1] = joc.imatgesPersonatgePistola[39];
		jumpGd[0] = joc.imatgesPersonatgePistola[34];
		jumpGd[1] = joc.imatgesPersonatgePistola[35];
		jumpGe[0] = joc.imatgesPersonatgePistola[36];
		jumpGe[1] = joc.imatgesPersonatgePistola[37];
		deathGe[0] = joc.imatgesPersonatgePistola[30];
		deathGe[1] = joc.imatgesPersonatgePistola[31];
		deathGd[0] = joc.imatgesPersonatgePistola[32];
		deathGd[1] = joc.imatgesPersonatgePistola[33];
		crouchd = joc.imatgesPersonatge[43];
		crouche = joc.imatgesPersonatge[44];
		crouchGd = joc.imatgesPersonatgePistola[40];
		crouchGe = joc.imatgesPersonatgePistola[41];
		for(int i=0;i<6;i++) {
				walkd[i]=joc.imatgesPersonatge[i+2];
				walke[i]=joc.imatgesPersonatge[i+8];
				walkGd[i]=joc.imatgesPersonatgePistola[i+2];
				walkGe[i]=joc.imatgesPersonatgePistola[i+8];
		}

		for(int i=0;i<8;i++) {
				rund[i]=joc.imatgesPersonatge[i+14];
				rune[i]=joc.imatgesPersonatge[i+22];
				runGd[i]=joc.imatgesPersonatgePistola[i+14];
				runGe[i]=joc.imatgesPersonatgePistola[i+22];
		}
		for(int i=0;i<3;i++) ladder[i] = joc.imatgesPersonatge[40+i];
		Vx=0;
		Vy=0;
		Fx=0;
		Fy=0;
		Ffx=0;
//		Ffy=0;
		terra = false; 
		esquerre = false;
		dreta = false;
		sostre = false;
		nwalkd = 0;
		nwalke = 0;
		nrund = 0;
		nrune = 0;
		ndeathd = 0;
		ndeathe = 0;
		njumpd = 0;
		njumpe = 0;
		nidle = 0;
		nladder = 0;
		mort = false;
		escaloDreta = false;
		escaloEsquerre = false;
		ultimaAccio = "idleDreta";
		pintat = false;
		gun = false;
		sobreEscalesVerticals = false;
		for(int i=0;i<14;i++) {
			idlerotaciod[i] = joc.imatgesPersonatgeRotacio[i];
			idlerotacioe[i] = joc.imatgesPersonatgeRotacio[i+14];
		}
		for(int i=0;i<42;i++) {
			walkrotaciod[i] = joc.imatgesPersonatgeRotacio[i+28];
			walkrotacioe[i] = joc.imatgesPersonatgeRotacio[i+70];
		}
	}
	
	void disparar() { 
		if(gun && nbales<balesInicials) {
			bales[nbales]=new Bala(joc,this);
			nbales+=1;
		}
	}
	void pinta() {
		g.fillRect(hitBox.x,hitBox.y,hitBox.width,hitBox.height);
//		g.drawLine(x-100,Math.round(joc.f.ALTURA/2+joc.f.ALTURA/6-(18f/90)*altura),x+100,Math.round(joc.f.ALTURA/2+joc.f.ALTURA/6-(18f/90)*altura));
//		for(int i=0;i<4;i++) {
//			g.drawImage(imatge[n],x,y,null);
//		}
//		System.out.println("fletxaDreta: " + fletxaDreta);
//		System.out.println("fletxaDreta2:  " +fletxaDreta2);
//		System.out.println("fletxaEsquerre: " + fletxaEsquerre);
//		System.out.println("fletxaEsquerre2:  " +fletxaEsquerre2);
//		System.out.println("llargada: " + llargada);
//		System.out.println("altura: " + altura);
//		if(fletxaEsquerre) System.out.println("FLETXA E");
//		if(fletxaEsquerre2) System.out.println("FLETXA E 2");
//		System.out.println("mort: " + mort);
//		System.out.println("fletxaAmunt: " + fletxaAmunt);
//		g.setColor(Color.GREEN);
//		g.fillRect(joc.f.AMPLADA/2-llargada/2,joc.f.ALTURA/2-altura/2,llargada,altura);
//		System.out.println("ultimaAccio : " + ultimaAccio);
//		g.drawLine(x+llargada/2,y-200,x+llargada/2,y+200);
//		g.drawLine(x-200,y+altura/2,x+200,y+altura/2);
		g.drawLine(x-200,y,x+200,y);
		g.drawLine(x,y-200,x,y+200);
		alpha = calculaAngle();
//		System.out.println("pistola:  " + gun);
//		System.out.println("terra: " + terra);

		int v; //permet controlar la velocitat de cada animació
		if(mort & (ultimaAccio == "correrDreta" | ultimaAccio == "caminarDreta" | ultimaAccio == "saltarDreta" | ultimaAccio == "idleDreta" | ultimaAccio == "mortDreta" )) {
			v = 8; //permet controlar la velocitat de l'animació. S'ha de retocar (també per les altres animacions) per a trobar el punt adecuat
			if(ndeathd/v==0 || !terra) {
//				altura = 43;
//				llargada = 58;
				altura = Math.round(38*Mmida);
				llargada = Math.round(34*Mmida);
				if(altura!=hitBox.height) {
					hitBox.y-=altura-hitBox.height;
					y-=altura-hitBox.height;
					yPinta = Math.round(y-(alturaImatge-altura)*0.5f);
				}
				hitBox.width = llargada;
				hitBox.height = altura;
				if(ndeathd > 1) {
//					altura = 27;
					Math.round(18*Mmida);
				}
				if(gun) g.drawImage(deathGd[0],xPinta,yPinta-10,null);
				else g.drawImage(deathd[0],xPinta,yPinta-10,null);
				pintat = true;
				ndeathd+=1;
			}
			if(ndeathd/v>0 && terra) {
//				llargada = 58;
//				altura = 27;
				altura = Math.round(18*Mmida);
				llargada = Math.round(39*Mmida);
				if(altura!=hitBox.height) {
					hitBox.y-=altura-hitBox.height;
					y-=altura-hitBox.height;
					yPinta = Math.round(y-(alturaImatge-altura)*0.5f);
				}
				hitBox.width = llargada;
				hitBox.height = altura;
				if(gun) g.drawImage(deathGd[1],xPinta,yPinta,null);
				else g.drawImage(deathd[1],xPinta,yPinta,null);
				pintat = true;
			}
			ultimaAccio = "mortDreta";
		}
		if(mort & (ultimaAccio == "correrEsquerre" | ultimaAccio == "caminarEsquerre" | ultimaAccio == "saltarEsquerre" | ultimaAccio == "idleEsquerre" | ultimaAccio == "mortEsquerre" )) {
			v = 8; //permet controlar la velocitat de l'animació. S'ha de retocar (també per les altres animacions) per a trobar el punt adecuat
			if(ndeathe/v==0 || !terra) {
//				altura = 43;
//				llargada = 58;
				altura = Math.round(38*Mmida);
				llargada = Math.round(34*Mmida);
				if(altura!=hitBox.height) {
					hitBox.y-=altura-hitBox.height;
					y-=altura-hitBox.height;
					yPinta = Math.round(y-(alturaImatge-altura)*0.5f);
				}
				hitBox.width = llargada;
				hitBox.height = altura;
				if(ndeathe > 1) {
//					altura = 27;
					Math.round(18*Mmida);
				}
				if(gun) g.drawImage(deathGe[0],xPinta,yPinta-10,null);
				else g.drawImage(deathe[0],xPinta,yPinta-10,null);
				pintat = true;
				ndeathe+=1;
			}
			if(ndeathe/v>0 && terra) {
//			if(terra) { //no va bé perquè terra = true durant tota l'animació de mort. Ho puc demanar manualment
//				llargada = 58;
//				altura = 27;
				altura = Math.round(18*Mmida);
				llargada = Math.round(39*Mmida);
				if(altura!=hitBox.height) {
					hitBox.y-=altura-hitBox.height;
					y-=altura-hitBox.height;
					yPinta = Math.round(y-(alturaImatge-altura)*0.5f);
				}
				hitBox.width = llargada;
				hitBox.height = altura;
				if(gun) g.drawImage(deathe[1],xPinta,yPinta,null);
				else g.drawImage(deathe[1],xPinta,yPinta,null);
				pintat = true;
			}
			ultimaAccio = "mortEsquerre";
		}
		if(!mort && !sobreEscalesVerticals && (!sobreEscalesDreta && !sobreEscalesEsquerre) && !terra && (ultimaAccio == "idleDreta" || ultimaAccio == "caminarDreta" || ultimaAccio == "correrDreta" || ultimaAccio == "saltarDreta" || ultimaAccio == "ladderDreta")) {
//			llargada = 33;
//			altura = 67; //hauria de ser 69 però donava problemes al passar de saltar a idle
			v=20; //permet controlar la velocitat de l'animació
			llargada = Math.round(22*Mmida);
			altura = Math.round(45*Mmida); //hauria de ser 46
			if(altura!=hitBox.height) {
				hitBox.y-=altura-hitBox.height;
				y-=altura-hitBox.height;
				yPinta = Math.round(y-(alturaImatge-altura)*0.5f);
			}
			hitBox.width = llargada;
			hitBox.height = altura;
			if(njumpd/v==0) {
				if(gun) g.drawImage(jumpGd[0],xPinta,yPinta,null);
				else g.drawImage(jumpd[0],xPinta,yPinta,null);
				pintat = true;
				njumpd+=1;
			}
			if(njumpd/v==1) {
				if(gun) g.drawImage(jumpGd[1],xPinta,yPinta,null);
				else g.drawImage(jumpd[1],xPinta,yPinta,null);
				pintat = true;
			}
			ultimaAccio = "saltarDreta";
		}
		else njumpd = 0;
//		if(!mort & fletxaAmunt & (ultimaAccio == "idleEsquerre" | ultimaAccio == "caminarEsquerre" | ultimaAccio == "correrEsquerre" | ultimaAccio == "saltarEsquerre")) {
		if(!mort && !sobreEscalesVerticals && (!sobreEscalesDreta && !sobreEscalesEsquerre) && !terra && (ultimaAccio == "idleEsquerre" || ultimaAccio == "caminarEsquerre" || ultimaAccio == "correrEsquerre" || ultimaAccio == "saltarEsquerre" || ultimaAccio == "ladderEsquerre")) {
			llargada = 33;
//			altura = 67; //hauria de ser 69 però donava problemes al passar de saltar a idle
			v=20;
			llargada = Math.round(22*Mmida);
			altura = Math.round(45*Mmida); //hauria de ser 46
			if(altura!=hitBox.height) {
				hitBox.y-=altura-hitBox.height;
				y-=altura-hitBox.height;
				yPinta = Math.round(y-(alturaImatge-altura)*0.5f);

			}
			hitBox.width = llargada;
			hitBox.height = altura;
			if(njumpe/v==0) {
				if(gun) g.drawImage(jumpGe[0],xPinta,yPinta,null);
				else g.drawImage(jumpe[0],xPinta,yPinta,null);;
				pintat = true;
				njumpe+=1;
			}
			if(njumpe/v==1) {
				if(gun) g.drawImage(jumpGe[1],xPinta,yPinta,null);
				else g.drawImage(jumpe[1],xPinta,yPinta,null);
				pintat = true;
			}
			ultimaAccio = "saltarEsquerre";
		}
		else njumpe = 0;
		if(!mort && !fletxaAvall && !sobreEscalesVerticals && fletxaDreta && !fletxaEsquerre && !fletxaDreta2 && !fletxaAmunt && (terra || sobreEscalesDreta || sobreEscalesEsquerre)) {
//			llargada = 30;
//			altura = 69;
			v = 5; //permet regular velocitat animació
			llargada = Math.round(19*Mmida);
			altura = Math.round(47*Mmida);
			if(altura!=hitBox.height) {
				hitBox.y-=altura-hitBox.height;
				y-=altura-hitBox.height;
				yPinta = Math.round(y-(alturaImatge-altura)*0.5f);
			}
			hitBox.width = llargada;
			hitBox.height = altura;
			if(gun) { //rotacions
				yPinta+=1;
				if(alpha<=Math.PI/2 && alpha>= Math.PI/2-Math.PI/12 ) {
					g.drawImage(walkrotaciod[nwalkd/v],xPinta,yPinta,null);
				}
				if(alpha<=Math.PI/2-Math.PI/12 && alpha>=Math.PI/2-Math.PI/12 -Math.PI/6  ) {
					g.drawImage(walkrotaciod[6+nwalkd/v],xPinta,yPinta,null);
				}
				if(alpha<=Math.PI/2-Math.PI/12-Math.PI/6 && alpha>=Math.PI/2-Math.PI/12-Math.PI/3) {
					g.drawImage(walkrotaciod[12+nwalkd/v],xPinta,yPinta,null);
				}
				if(alpha<=Math.PI/12 || alpha>= 2*Math.PI-Math.PI/12){
					g.drawImage(walkrotaciod[18+nwalkd/v],xPinta,yPinta,null);
				}
				if(alpha<=2*Math.PI-Math.PI/12 && alpha>=2*Math.PI-Math.PI/12-Math.PI/6) {
					g.drawImage(walkrotaciod[24+nwalkd/v],xPinta,yPinta,null);
				}
				if(alpha<=2*Math.PI-Math.PI/12-Math.PI/6 && alpha>=3*Math.PI/2+Math.PI/12) {
					g.drawImage(walkrotaciod[30+nwalkd/v],xPinta,yPinta,null);
				}
				if(alpha>=3*Math.PI/2 && alpha<=3*Math.PI/2+Math.PI/12 ) {
					g.drawImage(walkrotaciod[36+nwalkd/v],xPinta,yPinta,null);
				}
				yPinta-=1;
			}
//			if(gun) g.drawImage(walkGd[nwalkd/v],xPinta,yPinta,null);
			
			else g.drawImage(walkd[nwalkd/v],xPinta,yPinta,null);
			pintat = true;
			nwalkd+=1;
			nwalkd = nwalkd % (v*6);
			ultimaAccio = "caminarDreta";
		}
		if(!mort && !fletxaAvall && !sobreEscalesVerticals && fletxaEsquerre && !fletxaDreta && !fletxaEsquerre2 && !fletxaAmunt && (terra || sobreEscalesDreta || sobreEscalesEsquerre)) {
//			llargada = 30;
//			altura = 69;
			v = 5; //permet regular velocitat animació
			llargada = Math.round(19*Mmida);
			altura = Math.round(47*Mmida);
			if(altura!=hitBox.height) {
				hitBox.y-=altura-hitBox.height;
				y-=altura-hitBox.height;
				yPinta = Math.round(y-(alturaImatge-altura)*0.5f);
			}
			hitBox.width = llargada;
			hitBox.height = altura;
			if(gun) g.drawImage(walkGe[nwalke/v],xPinta,yPinta,null);
			else g.drawImage(walke[nwalke/v],xPinta,yPinta,null);
			pintat = true;
			nwalke+=1;
			nwalke = nwalke % (6*v);
			ultimaAccio = "caminarEsquerre";
		}
		if(!mort && !fletxaAvall && !sobreEscalesVerticals && fletxaDreta && fletxaDreta2 && !fletxaEsquerre && !fletxaEsquerre2 && !fletxaAmunt && (terra || sobreEscalesDreta || sobreEscalesEsquerre) ) {
//			llargada = 36;
//			altura = 57;
			v = 3; //permet regular velocitat animació
			llargada = Math.round(34*Mmida);
			altura = Math.round(38*Mmida);
			if(altura!=hitBox.height) {
				hitBox.y-=altura-hitBox.height;
				y-=altura-hitBox.height;
			}
			hitBox.width = llargada;
			hitBox.height = altura;
			if(gun) g.drawImage(runGd[nrund/v],x,y,null);
			else g.drawImage(rund[nrund/v],x,y,null);
			pintat = true;
			nrund+=1;
			nrund = nrund % (v*8);
			ultimaAccio = "correrDreta";
		}
		if(!mort && !fletxaAvall && !sobreEscalesVerticals && fletxaEsquerre && fletxaEsquerre2 && !fletxaDreta && !fletxaDreta2 && !fletxaAmunt && (terra || sobreEscalesDreta || sobreEscalesEsquerre)) {
//			llargada = 36;
//			altura = 57;
			v = 3; //permet regular velocitat animació
			llargada = Math.round(34*Mmida);
			altura = Math.round(38*Mmida);
			if(altura!=hitBox.height) {
				hitBox.y-=altura-hitBox.height;
				y-=altura-hitBox.height;
			}
			hitBox.width = llargada;
			hitBox.height = altura;
			if(gun) g.drawImage(runGe[nrune/v],x,y,null);
			else g.drawImage(rune[nrune/v],x,y,null);
			pintat = true;
			nrune+=1;
			nrune = nrune % (v*8);
			ultimaAccio = "correrEsquerre";
		}
		if(!mort && !fletxaAvall && !sobreEscalesVerticals && (terra || sobreEscalesDreta || sobreEscalesEsquerre) && !fletxaAmunt && ((!fletxaEsquerre && !fletxaDreta) || (fletxaDreta & fletxaEsquerre) || (fletxaDreta && fletxaEsquerre2) || (fletxaEsquerre && fletxaDreta2))) {
//			llargada = 33;
//			altura = 67;
			v = 10;
			llargada = Math.round(22*Mmida);
			altura = Math.round(45*Mmida);
			if(altura!=hitBox.height) {
				hitBox.y-=altura-hitBox.height;
				y-=altura-hitBox.height;
				yPinta = Math.round(y-(alturaImatge-altura)*0.5f);
			}
			hitBox.width = llargada;
			hitBox.height = altura;
			if(ultimaAccio=="caminarEsquerre" || ultimaAccio=="correrEsquerre" || ultimaAccio == "saltarEsquerre" || (ultimaAccio=="idleEsquerre" && !gun) || (alpha >= Math.PI/2 && alpha<= 3*Math.PI/2 && gun)) {
				if(nidle/v==1) {
					if(gun) {//baixos
						if(alpha>=Math.PI/2 && alpha<= Math.PI/2+Math.PI/12 ) {
							g.drawImage(idlerotacioe[1],xPinta,yPinta,null);
						}
						if(alpha>=Math.PI/2+Math.PI/12 && alpha<=Math.PI/2+Math.PI/12+Math.PI/6 ) {
							g.drawImage(idlerotacioe[3],xPinta,yPinta,null);
						}
						if(alpha>=Math.PI/2+Math.PI/12+Math.PI/6 && alpha<=Math.PI/2+Math.PI/12+Math.PI/3) {
							g.drawImage(idlerotacioe[5],xPinta,yPinta,null);
						}
						if(alpha>=Math.PI-Math.PI/12 && alpha<= Math.PI+Math.PI/12){
							g.drawImage(idlerotacioe[7],xPinta,yPinta,null);
						}
						if(alpha>=Math.PI+Math.PI/12 && alpha<=Math.PI+Math.PI/12+Math.PI/6) {
							g.drawImage(idlerotacioe[9],xPinta,yPinta,null);
						}
						if(alpha>=Math.PI+Math.PI/12+Math.PI/6 && alpha<=Math.PI+Math.PI/12+2*Math.PI/6) {
							g.drawImage(idlerotacioe[11],xPinta,yPinta,null);
						}
						if(alpha>=Math.PI+Math.PI/12+2*Math.PI/6 && alpha<=3*Math.PI/2 ) {
							g.drawImage(idlerotacioe[13],xPinta,yPinta,null);
						}
					}
					else g.drawImage(idlee[nidle/v],xPinta,yPinta,null);
				}else{
					if(gun) {//alts
						if(alpha>=Math.PI/2 && alpha<= Math.PI/2+Math.PI/12 ) {
							g.drawImage(idlerotacioe[0],xPinta,yPinta,null);
						}
						if(alpha>=Math.PI/2+Math.PI/12 && alpha<=Math.PI/2+Math.PI/12+Math.PI/6 ) {
							g.drawImage(idlerotacioe[2],xPinta,yPinta,null);
						}
						if(alpha>=Math.PI/2+Math.PI/12+Math.PI/6 && alpha<=Math.PI/2+Math.PI/12+Math.PI/3) {
							g.drawImage(idlerotacioe[4],xPinta,yPinta,null);
						}
						if(alpha>=Math.PI-Math.PI/12 && alpha<= Math.PI+Math.PI/12){
							g.drawImage(idlerotacioe[6],xPinta,yPinta,null);
						}
						if(alpha>=Math.PI+Math.PI/12 && alpha<=Math.PI+Math.PI/12+Math.PI/6) {
							g.drawImage(idlerotacioe[8],xPinta,yPinta,null);
						}
						if(alpha>=Math.PI+Math.PI/12+Math.PI/6 && alpha<=Math.PI+Math.PI/12+2*Math.PI/6) {
							g.drawImage(idlerotacioe[10],xPinta,yPinta,null);
						}
						if(alpha>=Math.PI+Math.PI/12+2*Math.PI/6 && alpha<=3*Math.PI/2 ) {
							g.drawImage(idlerotacioe[12],xPinta,yPinta,null);
						}
					}
					else g.drawImage(idlee[nidle/v],xPinta,yPinta,null);
				}
				pintat = true;
				ultimaAccio = "idleEsquerre";
				nidle+=1;
				nidle = nidle % (v*2);
			}
			else {
				if(nidle/v==1) {
					if(gun) {//baixos
						if(alpha<=Math.PI/2 && alpha>= Math.PI/2-Math.PI/12 ) {
							g.drawImage(idlerotaciod[1],xPinta,yPinta,null);
						}
						if(alpha<=Math.PI/2-Math.PI/12 && alpha>=Math.PI/2-Math.PI/12 -Math.PI/6 ) {
							g.drawImage(idlerotaciod[3],xPinta,yPinta,null);
						}
						if(alpha<=Math.PI/2-Math.PI/12-Math.PI/6 && alpha>=Math.PI/2-Math.PI/12-Math.PI/3) {
							g.drawImage(idlerotaciod[5],xPinta,yPinta,null);
						}
						if(alpha<=Math.PI/12 || alpha>= 2*Math.PI-Math.PI/12){
							g.drawImage(idlerotaciod[7],xPinta,yPinta,null);
						}
						if(alpha<=2*Math.PI-Math.PI/12 && alpha>=2*Math.PI-Math.PI/12-Math.PI/6) {
							g.drawImage(idlerotaciod[9],xPinta,yPinta,null);
						}
						if(alpha<=2*Math.PI-Math.PI/12-Math.PI/6 && alpha>=3*Math.PI/2+Math.PI/12) {
							g.drawImage(idlerotaciod[11],xPinta,yPinta,null);
						}
						if(alpha>=3*Math.PI/2 && alpha<=3*Math.PI/2+Math.PI/12) {
							g.drawImage(idlerotaciod[13],xPinta,yPinta,null);
						}
					}
					else g.drawImage(idled[nidle/v],xPinta,yPinta,null);
				}else {
					if(gun) {//alts
						if(alpha<=Math.PI/2 && alpha>= Math.PI/2-Math.PI/12 ) {
							g.drawImage(idlerotaciod[0],xPinta,yPinta,null);
						}
						if(alpha<=Math.PI/2-Math.PI/12 && alpha>=Math.PI/2-Math.PI/12 -Math.PI/6 ) {
							g.drawImage(idlerotaciod[2],xPinta,yPinta,null);
						}
						if(alpha<=Math.PI/2-Math.PI/12-Math.PI/6 && alpha>=Math.PI/2-Math.PI/12-Math.PI/3) {
							g.drawImage(idlerotaciod[4],xPinta,yPinta,null);
						}
						if(alpha<=Math.PI/12 || alpha>= 2*Math.PI-Math.PI/12){
							g.drawImage(idlerotaciod[6],xPinta,yPinta,null);
						}
						if(alpha<=2*Math.PI-Math.PI/12 && alpha>=2*Math.PI-Math.PI/12-Math.PI/6) {
							g.drawImage(idlerotaciod[8],xPinta,yPinta,null);
						}
						if(alpha<=2*Math.PI-Math.PI/12-Math.PI/6 && alpha>=3*Math.PI/2+Math.PI/12) {
							g.drawImage(idlerotaciod[10],xPinta,yPinta,null);
						}
						if(alpha>=3*Math.PI/2 && alpha<=3*Math.PI/2+Math.PI/12) {
							g.drawImage(idlerotaciod[12],xPinta,yPinta,null);
						}
					}
					else g.drawImage(idled[nidle/v],xPinta,yPinta,null);
				}
				pintat = true;
				ultimaAccio = "idleDreta";
				nidle+=1;
				nidle = nidle % (v*2);
			}
		}
		if(!mort && sobreEscalesVerticals) {
			v=4;
			llargada = Math.round(24*Mmida);
			altura = Math.round(46*Mmida);
//			if(altura!=hitBox.height) {
//				hitBox.y-=altura-hitBox.height;
//				y-=altura-hitBox.height;
//			}
			if(fletxaAmunt || fletxaAvall) {
				nladder+=1;
				nladder = nladder%(v*3);
				g.drawImage(ladder[nladder/v],xPinta,yPinta,null);
				pintat = true;
			}
			else {
				g.drawImage(ladder[1],xPinta,yPinta,null);
				pintat = true;
			}
			if(fletxaDreta) ultimaAccio = "ladderDreta";
			else if(fletxaEsquerre) ultimaAccio = "ladderEsquerre";
			else ultimaAccio = "ladder";
		}
//		System.out.println("escalesDreta: " + sobreEscalesDreta);
//		System.out.println("escalesEsquerre: " + sobreEscalesEsquerre);
//		System.out.println("ultimaAccio: " + ultimaAccio);
		if(!mort && terra && fletxaAvall && !sobreEscalesVerticals) {
			if(ultimaAccio == "idleDreta" || ultimaAccio == "caminarDreta" || ultimaAccio=="correrDreta" || ultimaAccio == "saltarDreta" || ultimaAccio == "ajupirDreta") {
				llargada = Math.round(28*Mmida);
				altura = Math.round(31*Mmida);
				if(altura!=hitBox.height) {
					y-=altura-hitBox.height;
					hitBox.height = altura;
					hitBox.setLocation(x,y);
				}
				if(!gun) g.drawImage(crouchd,x,y,null);
				else g.drawImage(crouchGd,x,y,null);
				ultimaAccio = "ajupirDreta";
				pintat = true;
			}
			if(ultimaAccio == "idleEsquerre" || ultimaAccio == "caminarEsquerre" || ultimaAccio == "correrEsquerre" || ultimaAccio == "saltarEsquerre" || ultimaAccio == "ajupirEsquerre") {
				llargada = Math.round(28*Mmida);
				altura = Math.round(31*Mmida);
				if(altura!=hitBox.height) {
					y-=altura-hitBox.height;
					hitBox.height = altura;
					hitBox.setLocation(x,y);
				}
				if(!gun) g.drawImage(crouche,x,y,null);
				else g.drawImage(crouchGe,x,y,null);
				ultimaAccio = "ajupirEsquerre";
				pintat = true;
			}
		}
		if(!pintat && !sobreEscalesVerticals) { //això és la solució més cutre de l'univers
			if(ultimaAccio=="caminarEsquerre" | ultimaAccio=="correrEsquerre" | ultimaAccio == "idleEsquerre" | ultimaAccio == "saltarEsquerre") {
				yPinta = Math.round(y-(alturaImatge-altura)*0.5f);
				if(gun) g.drawImage(jumpGe[0],xPinta,yPinta,null);
				else g.drawImage(jumpe[0],xPinta,yPinta,null);
			}
			else {
				if(gun) g.drawImage(jumpGd[0],xPinta,y,null);
				else g.drawImage(jumpd[0],xPinta,yPinta,null);
			}
		}
//		g.setColor(Color.BLACK);
//		g.fillRect(x,y,30,100);
		pintat = false;
		//adaptem les hitBox a les noves mides obtingudes al fer certes accions
//		if(altura!=hitBox.height) {
//			hitBox.y-=altura-hitBox.height;
//			y-=altura-hitBox.height;
//		}
//		hitBox.width = llargada;
//		hitBox.height = altura;
//		hitBox.y+=altura-hitBox.height;
	}
	
	void restartValues() {
		xFisiques=0;
		yFisiques=0; //posem la posició calculada per les físiques a 0.
		Fx=0;
		Fy=0;
		Vx=0;
		Vy=0;
	}

	void Fisiques() {
		if(fletxaDreta & fletxaEsquerre || !fletxaDreta & !fletxaEsquerre || (fletxaAvall && !sobreEscalesVerticals)) Vx *=0.8;
		else if(fletxaEsquerre && !fletxaDreta && (!fletxaAmunt || sobreEscalesVerticals)) Vx--;
		else if(fletxaDreta && !fletxaEsquerre && (!fletxaAmunt || sobreEscalesVerticals)) Vx++;
		vMax = 8;
		//limito la velocitat al baixar escales: MANUALMENT
//		if(fletxaEsquerre && x-joc.escales.get(0).x>0 && x-joc.escales.get(0).x-joc.escales.get(0).llargada<0) vMax = 2;
		if(fletxaEsquerre2 | fletxaDreta2) vMax=16;
		if(Math.abs(Vx)<0.75) Vx=0;
		if(Math.abs(Vx)>vMax) Vx = Math.signum(Vx)*vMax;
//		if(escaloDreta) { // s'hauria de gestionar això com amb les tiles (*) segurament el millor és que l'escala tingui els escalons com a tiles
//			Vy = 0;
////			terra = true;
//		}
//		if(escaloEsquerre) {
//			Vy = 0;
////			terra = true;
//		}
		if(fletxaAmunt && !sobreEscalesVerticals) {
			hitBox.y+=2;
			if(escaloDreta || escaloEsquerre) {
				Vy=-5;
			}
			for(Tile tile : joc.tilesPersonatge) {
				if(tile.hitBox.intersects(hitBox)) Vy=-5;
			}
			hitBox.y-=2;
		}
		if(!sobreEscalesVerticals) { //gravetat
			Vy+=.3;
		}
		//xocs verticals
		for(Tile tile: joc.tilesPersonatge) { // (*)
			tile.hitBox.y -= Vy;
			if(tile.hitBox.intersects(hitBox)) {
				tile.hitBox.y += Vy;
				while(!tile.hitBox.intersects(hitBox)) tile.hitBox.y-=Math.signum(Vy);
				tile.hitBox.y+=Math.signum(Vy);
				Vy = 0;
				yFisiques += (tile.y-tile.hitBox.y); //ajustem yFisiques per tal que tile.y quedi a tile.hitBox.y
			}
		}
		//xocs horitzontals
		for(Tile tile: joc.tilesPersonatge) {
			tile.hitBox.x -= Vx;
			if(tile.hitBox.intersects(hitBox)) {
				tile.hitBox.x += Vx;
				while(!tile.hitBox.intersects(hitBox)) tile.hitBox.x-=Math.signum(Vx);
				tile.hitBox.x+=Math.signum(Vx);
				Vx = 0;
				xFisiques += (tile.x-tile.hitBox.x); //ajustem yFisiques per tal que tile.y quedi a tile.hitBox.y
					}
				}
		if(sobreEscalesDreta) {
			if(fletxaDreta) yFisiques-=Vx*0.5; // Vx* (pendent escala)
			if(fletxaEsquerre & !terra) yFisiques+=Vx*0.5;
		}
//		if(sobreEscalesEsquerre) {
//			if(fletxaEsquerre) yFisiques-=Vx*0.5;
//			if(fletxaDreta & !terra)  yFisiques+=Vx*0.5;
//		}
		if(escaloDreta && !fletxaAmunt) {
			Vy = 0;
//			terra = true;
		}
		if(escaloEsquerre  && !fletxaAmunt) {
			Vy = 0;
//			terra = true;
		}
		
		xFisiques += Vx;
		yFisiques += Vy;
		
	
	}
	double calculaAngle() { //angle respecte la horitzontal definida per la pistola 
		double x = xRatoli;
		double y = yRatoli;
		double xCentre = joc.f.AMPLADA/2;
		double yCentre = joc.f.ALTURA/2+joc.f.ALTURA/6-(18f/90)*altura; //alçada del centre de la pistola 
		double norma = Math.sqrt((x-xCentre)*(x-xCentre)+(y-yCentre)*(y-yCentre));
		double alpha=0;
		if(y-yCentre>=0) {
			alpha = Math.acos((x-xCentre)/norma);
		}
		if(y-yCentre<0) {
			alpha = 2*Math.PI-Math.acos((x-xCentre)/norma);
		}
		return (alpha);
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_D && !mort) { //podria demanar && (terra || sobreEscalesDreta || sobreEscalesEsquerre), no sé si m'agrada
			fletxaDreta=true;
			if(!fletxaDreta2) {
				if(Math.abs(ultimCopFletxaDreta-System.currentTimeMillis())<0.1*1000) {
					fletxaDreta2 = true;
				}
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_A && !mort) {
			fletxaEsquerre=true;
			if(!fletxaEsquerre2) {
				if(Math.abs(ultimCopFletxaEsquerre-System.currentTimeMillis())<0.1*1000) {
					fletxaEsquerre2 = true;
				}
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_W & !mort) {
			fletxaAmunt=true;
		}
		if(e.getKeyCode()==KeyEvent.VK_S & !mort) {
			fletxaAvall=true;
		}
		if(e.getKeyCode()==KeyEvent.VK_G && !mort) {
			boolean aux = false; //segurament es pot escriure d'una forma més elegant
			if(!gun) {
				aux = true;
				gun = true;
			}
			if(gun && !aux) gun = false;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_D) {
			ultimCopFletxaDreta = System.currentTimeMillis();
			fletxaDreta = false;
			fletxaDreta2 = false;
		}
		if(e.getKeyCode()==KeyEvent.VK_A) {
			ultimCopFletxaEsquerre = System.currentTimeMillis();
			fletxaEsquerre = false;
			fletxaEsquerre2 = false;
		}
		if(e.getKeyCode()==KeyEvent.VK_W) {
			fletxaAmunt=false;
		}
		if(e.getKeyCode()==KeyEvent.VK_S) {
			fletxaAvall=false;
		}
		if(e.getKeyCode()==KeyEvent.VK_R) {
			mort = true;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		xRatoli = e.getX();
		yRatoli = e.getY();
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton()==1 && !mort) {
			disparar(); //amb el botó dret disparem
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
