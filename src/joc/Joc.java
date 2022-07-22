package joc;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Joc implements KeyListener{
	BufferedImage menuInicial,menuFinal,fons,imatgeEnemic1,estrella,fonsRecords,menuControls, imatgesMeteorits[]=new BufferedImage[5], imatgeSpinner[] = new BufferedImage[18];
	BufferedImage imatgesNauXocOriginal[]= new BufferedImage[3],imatgesNau[]= new BufferedImage[3],imatgesNauXoc[] = new BufferedImage[3], imatgesTorreta[] = new BufferedImage[3], foratnegre, bala;
	File fitxerRecords;
	Graphics g;
	Finestra f;
	Nau c;
	Minimap map;
	int llargadaMeteorit1[]=new int[3];
	int alturaNau,llargadaNau,alturaBales,llargadaBales,alturaMeteorit1,alturaMeteorit2,llargadaMeteorit2,alturaNauEnemiga1,llargadaNauEnemiga1,llargadaForatNegre,alturaForatNegre,llargadaEstrella,
	alturaEstrella, llargadaBala, alturaBala, midaPaquetMunicio,midaTorreta, midaSpinner;//mida objectes
	int alturaMinimapa,llargadaMinimapa,alturaBarres,llargadaBarres,alturaRecords,llargadaRecords, xMenuRecords, yMenuRecords, xTextFinal, yTextFinal, midaLletraRecords, separacioRecords; //mida elements UI
	int alturaNauM,llargadaNauM,llargadaMeteorit1M, llargadaMeteorit2M,alturaNauEnemiga1M, llargadaNauEnemiga1M, llargadaCheckpointM, alturaCheckpointM,llargadaForatNegreM,midaPuntRadar, //mida elements minimapa
	midaPaquetMunicioM,midaTorretaM, midaSpinnerM;
	//potser agrupar valors de dalt en vectors
	boolean calculatRecords,apuntatTemps,inici,records,controls;
	ContadorTemps contadorTemps; //l'usarem per a contar quants segons durem vius. 
	ContadorBales contadorBales; //l'usarem per a escriure per pantalla les bales que ens queden 
	ArrayList<Estrella> estrelles =new ArrayList<Estrella>();
	ArrayList<Enemic> enemics = new ArrayList<Enemic>();
	ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
	ArrayList<PaquetMunicio> paquetsmunicio = new ArrayList<PaquetMunicio>();
	ArrayList<Torreta> torretes = new ArrayList<Torreta>();
	ArrayList<Spinner> spinners = new ArrayList<Spinner>();
	static Random r = new Random(); //element random que usarem al llarg del codi per a aconseguir valors aleatoris.
	static double dt=0.1; //l'usem per a les físiques de la nau
	Joc(Finestra f){
		this.f=f;
		this.g=f.g; 
	}
	void run(){ 
		Inicialitzacio();
			while(true) {
//				c.isTargetable = false;
				moviments();
				generacioMeteorits();
				xocs(); 
				repintar();
				if(!inici) {
					contadorTemps.apuntaTemps();
					contadorTemps.llegeixTemps();
				}
				f.repaint(); 
				try {
					Thread.sleep(20); //potser s'ha de variar en funció del processador
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	}
	void Inicialitzacio() {
		loadResources(); //obtenim les imatges i fitxers que farem servir 
		calculaMides(); //calculem mides dels objectes en funció de la mida de la pantalla
		try {
			resizeImages(); //canviem la mida de les nostres imatges a la mida adecuada en funció de la mida de la pantalla
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} 
		c=new Nau(this); //creem la nostra nau.
		map = new Minimap(this); //creem el minimapa
		f.addKeyListener(c);
		f.addKeyListener(this);
		inici=true;
		records=false;
	}
	void moviments() { //Moviments de la nau, enemics, bales i estrelles i dispars dels enemics
		
		c.Fisiques(); //Calculem com es mou la nau
		
		for(int i=0;i<c.nbales;i++) { //Les bales un cop passen a ser no visibles no ho tornen a ser mai
			if(c.bales[i].isVisible) {
				c.bales[i].moure();
				if(Math.abs(c.bales[i].x-c.x)>f.AMPLADA*2 || Math.abs(c.bales[i].y-c.y)>f.ALTURA*2) c.bales[i].isVisible=false;
			}
		}
		//Enemics
		for(Enemic enemic : enemics) {
			
			//isVisible
			if(!(enemic instanceof NauEnemiga2)) { //s'ha de treure
			if(Math.abs(c.x-enemic.x)>f.AMPLADA/2+enemic.llargada | Math.abs(c.y-enemic.y)>f.ALTURA/2+enemic.altura) enemic.isVisible = false;
			else enemic.isVisible = true;
			}
			
			//isInMinimap
			if(Math.abs(c.x-enemic.x)<f.AMPLADA && Math.abs(c.y-enemic.y)<f.ALTURA+0.5*f.ALTURA) enemic.isInMinimap = true;
			else enemic.isInMinimap = false;
			
			//Move
			if(!enemic.mort) enemic.moure();
			else enemic.particules.moure();
			
			//Shoot
			if((enemic instanceof NauEnemiga1 | enemic instanceof Torreta | enemic instanceof Spinner)) {
				for(Bala bala : enemic.bales) {
					if(bala.isVisible) bala.moure();
					if(Math.abs(bala.x-c.x)>f.AMPLADA*2 | Math.abs(bala.y-c.y)>f.ALTURA*2) bala.isVisible = false;
				}
			if(!enemic.mort & enemic.isVisible) enemic.dispara();
			}
		}
		//Estrelles de fons
		for(int i=0;i<estrelles.size();i++) {
			estrelles.get(i).moure();
		}
		//Checkpoints
		for(int i=0;i<checkpoints.size();i++) {
			//isVisible
			if(Math.abs(c.x-checkpoints.get(i).x)>f.AMPLADA*2 || Math.abs(c.y-checkpoints.get(i).y)>f.ALTURA*2) {
				checkpoints.get(i).isVisible=false;
			}else checkpoints.get(i).isVisible=true;
			
			//isInMinimap
			if(Math.abs(c.x-checkpoints.get(i).x)<f.AMPLADA && Math.abs(c.y-checkpoints.get(i).y)<f.ALTURA+0.5*f.ALTURA) {
				checkpoints.get(i).isInMinimap = true;
			}
			else checkpoints.get(i).isInMinimap = false;
			
			//Move
			checkpoints.get(i).moure();
		}
		for(int i=0;i<paquetsmunicio.size();i++) { //paquets munició
			//isVisible
			
			if(Math.abs(c.x-paquetsmunicio.get(i).x)>f.AMPLADA*2 || Math.abs(c.y-paquetsmunicio.get(i).y)>f.ALTURA*2) {
				paquetsmunicio.get(i).isVisible=false;
			}else {
				paquetsmunicio.get(i).isVisible=true;
			}
			//isInMinimap
			
			if(Math.abs(c.x-paquetsmunicio.get(i).x)<f.AMPLADA && Math.abs(c.y-paquetsmunicio.get(i).y)<f.ALTURA+0.5*f.ALTURA) {
				paquetsmunicio.get(i).isInMinimap = true;
			}
			else {
				paquetsmunicio.get(i).isInMinimap = false;
			}
			
			//movem
			paquetsmunicio.get(i).moure();
		}
	}
	void xocs() {		
		//Bales nau amb enemics
		for(int i=0; i<c.nbales; i++) { 
			for(Enemic enemic : enemics) if(!enemic.mort & enemic.isInMinimap & !(c.bales[i].xoc) & enemic.hitBox.intersects(c.bales[i].hitBox)) {
				enemic.xoc+=1;
				c.bales[i].xoc = true;
				if(enemic.xoc >= enemic.vida) {
					enemic.mort = true;
					enemic.particules = new ParticleSystem(this,enemic);
				}
			}
		}
		//Nau amb enemics
		for(Enemic enemic : enemics) if(c.isTargetable & enemic.isVisible & enemic.hitBox.intersects(c.hitBox)) {
				c.vida-=enemic.bodyDamage;
				c.tempsUltimXoc = System.currentTimeMillis(); //això ja fa que amb un xoc només perdem un cop la vida que hem de perdre.
		}
		
		//Bales enemics amb nau
		for(Enemic enemic : enemics) {
			for(Bala bala : enemic.bales) if(c.isTargetable & c.hitBox.intersects(bala.hitBox) & !bala.xoc & !c.mort & !inici) {
					c.vida -= Bala.bulletDamage; //s'haura de canviar si en algun moment posem diferents tipus de bales
					c.tempsUltimXoc = System.currentTimeMillis();
			}	
		}
				
		//Nau amb paquets de munició
		for(PaquetMunicio paquet : paquetsmunicio) {
			if(!paquet.agafat & c.hitBox.intersects(paquet.hitBox)) {
				paquet.agafat=true;
				contadorBales.balesRestants+=Math.min(paquet.bales,c.nbales);
				c.nbales-=Math.min(paquet.bales,c.nbales);
			}
		}
		
		//AQUI HI HAURIA D'HAVER XOCS AMB CHECKPOINTS QUE FARIEN SORTIR UN MENÚ ON FER MILLORES A LA NAU/ALTRES.
		//MIREM SI S'HA MORT LA NAU
		if(c.vida <= 0) c.mort = true;
	}
	void generacioMeteorits() {
		
		//meteorits
		int random  = r.nextInt(100); //potser prendre 1000 així puc afinar més
		if(random>70) { //posavem aixo per anar pujant dificultat amb el temps nivellDificultat("meteorit",27) //70
			enemics.add(new Meteorit1(this));
		}
		if(random>75) {//75
			enemics.add(new Meteorit2(this));
			if(r.nextInt(30)>16) { //donem alguna probabilitat diferent de zero a que ens apareixin meteorits grans esquerdats 
				enemics.get(enemics.size()-1).xoc=1;
			}
		} 
		if(random>98) {
			enemics.add(new NauEnemiga1(this));
		}
		
		//estrelles
		if(random>30) {
			estrelles.add(new Estrella(this));
		}
	}
	void restart() {
		apuntatTemps=false;
		c.restartValues(); //resetegem els valors de les variables associades a la nau 
		contadorTemps=new ContadorTemps(this,g); //creem un nou contador
		contadorBales=new ContadorBales(this);
		ArrayList<Enemic> m = new ArrayList<Enemic>(); //si que ens interessa resetejar aquest vector perquè sinó sen's farà molt gran
		this.enemics=m;								   
		ArrayList<Checkpoint> ck = new ArrayList<Checkpoint>();
		this.checkpoints=ck;
		ArrayList<Torreta> tr = new ArrayList<Torreta>();
		this.torretes = tr;
		calculatRecords=false;
		contadorBales.balesRestants=Nau.balesInicials;
		generacioMapa();
	}
	void repintar() {
		g.drawImage(fons,0,0,null);
		if(!inici) {
			//ESTRELLES, si ja porten cert temps dibuixades no les dibuixem 
			for(Estrella estrella : estrelles) if(estrella.isVisible) estrella.pinta();
			
			//DIBUIXEM BALES
			for(int i=0;i<c.nbales;i++){ //Si les bales no han xocat i la nau esta viva les pintem. 
				if(!c.bales[i].xoc & !c.mort & c.bales[i].isVisible) c.bales[i].pinta();
			}
			for(Enemic enemic : enemics) for(Bala bala : enemic.bales) bala.pinta();
			
			//DIBUIXEM ENEMICS
			for(Enemic enemic : enemics) {
				if(!enemic.mort & enemic.isVisible) enemic.pinta();
				if(enemic.mort) enemic.particules.pinta();
			}
			
			//DIBUIXEM CHECKPOINTS
			for(Checkpoint checkpoint : checkpoints) {
				if(checkpoint.isVisible) checkpoint.pinta();
			}
			
			//DIBUIXEM PAQUETS DE MUNICIÓ
			for(PaquetMunicio paquetmunicio : paquetsmunicio) {
				if(!paquetmunicio.agafat) paquetmunicio.pinta();
			}
			
			if(!c.mort) {
				//DIBUIXEM NAU BARRA VIDA I BARRA MUNICIÓ
				int dtemps = (int) Math.abs(c.tempsUltimXoc-System.currentTimeMillis()); //aqui regulem com ens avisa la nau que ha xocat amb un objecte
				if(dtemps>1000) {														//i quan de temps la nau és invencible després de xocar
					c.isTargetable = true;
					c.pinta(g, 0);
				}
				else {
				c.isTargetable = false;
				for(int i=0;i<10;i++) {
					if(dtemps>i*100 && dtemps<(i+1)*100) {
						c.pinta(g, i%2);
					}
				}
				}
				c.pintaBarraVida(g);
				contadorBales.pinta();
				//DIBUIXEM MINIMAPA
				map.pinta();
			}
			//DIBUIXEM MENU FINAL
			else {
				if(!records) {
					g.drawImage(menuFinal,0,0,null);
					contadorTemps.pinta();
				}
				if(records) { //SI ESTEM AL MENU DE RECORDS ENSENYEM ELS RECORDS
					g.drawImage(fonsRecords,0,0,null);
					contadorTemps.pintaRecords();
				}
				}
			}
		
		if(inici) { //al menu d'inici ensenyem meteorits de fons i estrelles
			for(int i=0;i<estrelles.size();i++) {
				if(estrelles.get(i).isVisible) {
					estrelles.get(i).pinta();
				}
			}
			for(Enemic enemic : enemics) if((enemic instanceof Meteorit1 | enemic instanceof Meteorit2) & enemic.isVisible) enemic.pinta();
			if(!records & !controls) g.drawImage(menuInicial,0,0,null); //ensenyem el menu inicial últim per a que els meteorits es vegin per sota. 
			if(records & !controls & !inici){
			g.drawImage(fonsRecords,0,0,null);
			contadorTemps.pintaRecords();
			}
			if(controls & !records) g.drawImage(menuControls,0,0,null);
		}
	}
	int nivellDificultat(String tipus,int i) { //ens permet regular la dificultat a mesura que portem més estona a una partida 
		long tempsEnPartida=(System.currentTimeMillis()-contadorTemps.tempsInicial)/1000;
		if(tipus=="meteorit") {
			for(int j=0; j<6; j++) {
				if(tempsEnPartida>j*10+10 && tempsEnPartida <= (j+1)*10+10) {
					return i-j-1;
				}
			}
				if(tempsEnPartida>70) {
					return i-6;
				}
			return i;
		}
		if(tipus=="meteoritGran") { //aqui pots posar-ho amb un for també, s'ha de modificar aquesta funció extensivament per tant ja ho faré (doncs la jugabilitat canvia, passa d'arcade a roguelike(aprox))
			if(tempsEnPartida>10 && tempsEnPartida<=20) {
				return i-1;
			}
			if(tempsEnPartida>20 && tempsEnPartida<=30) {
				return i-2;
			}
			if(tempsEnPartida>30 && tempsEnPartida<=40) {
				return i-3;
			}
			if(tempsEnPartida>40) {
				return i-4;
			}
		}
		if(tempsEnPartida>10 && tempsEnPartida<=20) {
			return i-1;
			}
		if(tempsEnPartida>20 && tempsEnPartida<=30) {
			return i-3;
			}
		if(tempsEnPartida>30 && tempsEnPartida<=40) {
			return i-4;
		}
		if(tempsEnPartida>40) {
			return i-6;
		}
		return i;
	}
	BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException { //funció que ens canvia la mida d'una imatge donada
	    Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH); //hi ha altres algorismes 
	    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB); //type_int_argb respecta la transperència
	    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
	    return outputImage;
	}
	void calculaMides() { //calcula mides dels objectes en funció de la mida de la pantalla
		llargadaNau = Math.round(Nau.llargadaRelativa * f.AMPLADA);
		alturaNau = Math.round(Nau.alturaRelativa * f.ALTURA);
		llargadaBales = Math.round(Bala.llargadaRelativa*f.AMPLADA);
		alturaBales = Math.round(Bala.alturaRelativa*f.ALTURA);
		llargadaMeteorit1[0] = Math.round(Meteorit1.llargadaRelativa[0]*f.AMPLADA); //meteorit petit amb molt foc
		llargadaMeteorit1[1] = Math.round(Meteorit1.llargadaRelativa[1]*f.AMPLADA); //meteorit petit amb una mica de foc
		llargadaMeteorit1[2] = Math.round(Meteorit1.llargadaRelativa[2]*f.AMPLADA); //meteorit petit sense foc
		alturaMeteorit1 = Math.round(Meteorit1.alturaRelativa*f.ALTURA);
		llargadaMeteorit2 = Math.round(Meteorit2.llargadaRelativa*f.AMPLADA);
		alturaMeteorit2 = Math.round(Meteorit2.alturaRelativa*f.ALTURA);
		llargadaNauEnemiga1 = Math.round(NauEnemiga1.llargadaRelativa*f.AMPLADA);
		alturaNauEnemiga1 = Math.round(NauEnemiga1.alturaRelativa*f.ALTURA);
		llargadaForatNegre = Math.round(ForatNegre.llargadaRelativa*f.AMPLADA);
		alturaForatNegre = Math.round(ForatNegre.alturaRelativa*f.ALTURA);
		llargadaMinimapa = Math.round(Minimap.llargadaRelativa*f.AMPLADA);
		alturaMinimapa = Math.round(Minimap.alturaRelativa*f.ALTURA);
		llargadaBarres = Math.round(ContadorBales.llargadaRelativa*f.AMPLADA);
		alturaBarres = Math.round(ContadorBales.alturaRelativa*f.ALTURA);
		llargadaEstrella = Math.round(Estrella.llargadaRelativa*f.AMPLADA);
		alturaEstrella = Math.round(Estrella.alturaRelativa*f.ALTURA);
		xMenuRecords = Math.round(ContadorTemps.xRelativa1*f.AMPLADA);
		yMenuRecords = Math.round(ContadorTemps.yRelativa1*f.ALTURA);
		xTextFinal = Math.round(ContadorTemps.xRelativa2*f.AMPLADA);
		yTextFinal = Math.round(ContadorTemps.yRelativa2*f.ALTURA);
		midaLletraRecords = Math.round(ContadorTemps.midaTextRelativa*f.AMPLADA);
		separacioRecords = Math.round(ContadorTemps.separacioRelativa*f.ALTURA);
		midaPuntRadar = Math.round(Minimap.midaPuntRadarRelativa*f.AMPLADA); //calculem mida objectes radar
		llargadaNauM = Math.round(Minimap.llargadaNauRelativa*f.AMPLADA);
		alturaNauM = Math.round(Minimap.alturaNauRelativa*f.ALTURA);
		llargadaMeteorit1M = Math.round(Minimap.llargadaMeteorit1Relativa*f.AMPLADA);
		llargadaMeteorit2M = Math.round(Minimap.llargadaMeteorit2Relativa*f.AMPLADA);
		llargadaCheckpointM = Math.round(Minimap.llargadaCheckpointRelativa*f.AMPLADA);
		alturaCheckpointM = Math.round(Minimap.alturaCheckpointRelativa*f.ALTURA);
		llargadaForatNegreM = Math.round(Minimap.llargadaForatNegreRelativa*f.AMPLADA);
		alturaBala = Math.round(Bala.alturaRelativa*f.ALTURA);
		llargadaBala = Math.round(Bala.llargadaRelativa*f.AMPLADA);
		midaPaquetMunicio = Math.round(PaquetMunicio.midaRelativa*f.AMPLADA);
		midaPaquetMunicioM = Math.round(Minimap.midaPaquetMunicioRelativa*f.AMPLADA);
		midaTorreta = Math.round(Torreta.llargadaRelativa*f.AMPLADA);
		midaTorretaM = Math.round(Minimap.midaTorretaRelativa*f.AMPLADA);
		midaSpinner = Math.round(Spinner.midaRelativa * f.AMPLADA);
		midaSpinnerM = Math.round(Minimap.midaSpinnerRelativa * f.AMPLADA);
	}
	void resizeImages()throws IOException{ // canviem la mida de les imatges usant les mides calculades anteriorment
		for(int i=0; i<3;i++) { 
			imatgesNau[i]=resizeImage(imatgesNau[i],llargadaNau,alturaNau);
			imatgesNauXoc[i]=resizeImage(imatgesNauXoc[i],llargadaNau,alturaNau);
		} 
		for(int i=0;i<3;i++) {
			imatgesMeteorits[i] = resizeImage(imatgesMeteorits[i],llargadaMeteorit1[i],alturaMeteorit1);
		}
		for(int i =0 ;i<18;i++) { 
			imatgeSpinner[i] = resizeImage(imatgeSpinner[i],midaSpinner,midaSpinner);
		}
		imatgesMeteorits[3] = resizeImage(imatgesMeteorits[3],llargadaMeteorit2,alturaMeteorit2);
		imatgesMeteorits[4] = resizeImage(imatgesMeteorits[4],llargadaMeteorit2,alturaMeteorit2);
		imatgeEnemic1 = resizeImage(imatgeEnemic1,llargadaNauEnemiga1,alturaNauEnemiga1);
		foratnegre = resizeImage(foratnegre,llargadaForatNegre,alturaForatNegre );
		bala = resizeImage(bala,llargadaBala,alturaBala);
		fons=resizeImage(fons,f.AMPLADA,f.ALTURA);
		menuInicial=resizeImage(menuInicial,f.AMPLADA,f.ALTURA);
		menuFinal=resizeImage(menuFinal,f.AMPLADA,f.ALTURA);
		menuControls=resizeImage(menuControls,f.AMPLADA,f.ALTURA);
		fonsRecords=resizeImage(fonsRecords,f.AMPLADA,f.ALTURA);
		for(int i=0;i<3;i++) imatgesTorreta[i] = resizeImage(imatgesTorreta[i], midaTorreta*2,midaTorreta*2); //multiplico per dos perquè la imatge de la torreta és el doble de llarga i el doble de ample que la torreta en si
	}
	public void generacioMapa() { //ELEMENTS FIXOS DEL MAPA: CHECKPOINTS, npcs, BASES ENEMIGUES ... 
		
		//checkpoints
//		Checkpoint checkpoint2 = new Checkpoint(this, 1500,4000);
//		Checkpoint checkpoint3 = new Checkpoint(this, 10000,-100);
//		checkpoints.add(checkpoint2);
//		checkpoints.add(checkpoint3);
		
		//paquets de munició
//		PaquetMunicio paquet1 = new PaquetMunicio(this, 2600,900,25);
//		PaquetMunicio paquet2 = new PaquetMunicio(this, 5000,800,25);
//		PaquetMunicio paquet3 = new PaquetMunicio(this, 1700,9000,25);
//		PaquetMunicio paquet4 = new PaquetMunicio(this, -700,565,25);
//		paquetsmunicio.add(paquet1);
//		paquetsmunicio.add(paquet2);
//		paquetsmunicio.add(paquet3);
//		paquetsmunicio.add(paquet4);
		
		//torretes
		Torreta torreta1 = new Torreta(this,800,1200,0.5,8,100);
		enemics.add(torreta1);
		Torreta torreta3 = new Torreta(this, 600, 300, 2, 1, 300);
		enemics.add(torreta3);
		
//		Torreta torreta2 = new Torreta(this,600,300,0.5,4,120);
//		enemics.add(torreta2);
		
		//spinners
//		Spinner spinner1 = new Spinner(this,500,300);
//		enemics.add(spinner1);
		
//		forats negre
		ForatNegre foratnegre1 = new ForatNegre(this,100,300);
		enemics.add(foratnegre1);
		
//		Naus enemigues 1
//		NauEnemiga1 nau1 = new NauEnemiga1(this,900,300);
//		enemics.add(nau1);
		
		//Naus enemigues 2
//		NauEnemiga2 nau2 = new NauEnemiga2(this,500,500);
//		enemics.add(nau2);
		
		
//		torreta1.bales.add(new Bala(this, torreta1, 100, 0));
	}
	void loadResources() {
		try {
			fons = ImageIO.read(getClass().getResource("/fons.png"));
		} catch (IOException e) {
		}
		try {
			menuInicial = ImageIO.read(getClass().getResource("/menuinicial.png"));
		} catch (IOException e) {
		}
		try {
		    menuFinal = ImageIO.read(getClass().getResource("/menufinal.png"));
		} catch (IOException e) { 
		}
		for(int i=1;i<6;i++) {
			try {
				imatgesMeteorits[i-1] = ImageIO.read(getClass().getResource("/meteorit"+i+".png")); 
			} catch (IOException e) {
			}	
		}
		try {
			imatgeEnemic1 = ImageIO.read(getClass().getResource("/enemic1.png"));
		} catch (IOException e) {
		}
		try {
			estrella = ImageIO.read(getClass().getResource("/estrella.png"));
		} catch (IOException e) {
		}
		try {
			fonsRecords = ImageIO.read(getClass().getResource("/records.png"));
		} catch (IOException e) {
		}
		try {
			menuControls = ImageIO.read(getClass().getResource("/controls.png"));
		} catch (IOException e) {
		}
		for(int i=1;i<4;i++) {
			try {
				imatgesNauXoc[i-1] = ImageIO.read(getClass().getResource("/nauespacial"+i+"grey.png"));
			} catch (IOException e) {
			}
			try {
				imatgesNau[i-1] = ImageIO.read(getClass().getResource("/nauespacial"+i+".png"));
			} catch (IOException e) {
			}
		}
		try {
			foratnegre = ImageIO.read(getClass().getResource("/foratnegre.png"));
		} catch (IOException e) {
		}
		try {
			bala = ImageIO.read(getClass().getResource("/bala.png"));
		} catch (IOException e) {
		}
		try {
			for(int i=1;i<4;i++) imatgesTorreta[i-1] = ImageIO.read(getClass().getResource("/torreta"+i+".png"));
		} catch (IOException e) {
		}
		for(int i =1 ;i<19;i++) { 
			try {
				imatgeSpinner[i-1] = ImageIO.read(getClass().getResource("/spinner"+i+".png"));
			} catch (IOException e) {
			}
		}
		fitxerRecords=new File("records.txt"); //no troba el fitxer quan exportem en runnable jar
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==80 && c.mort && !records) {
			restart();
		}
		if(e.getKeyCode()==80 && inici && !records) {
			inici=false;
			restart();
		}
		if(e.getKeyCode()==82 && inici && !controls) {
			records=true;
		}
		if(e.getKeyCode()==77 && c.mort && !records) {
			inici=true;
		}
		if(e.getKeyCode()==66 && records) {
			records=false;
		}
		if(e.getKeyCode()==67 && inici && !records) {
			controls=true;
		}
		if(e.getKeyCode()==66 && controls ) {
			controls=false;
		}
		if(e.getKeyCode()==27) {
			Finestra.device.setFullScreenWindow(null);
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
	}
}