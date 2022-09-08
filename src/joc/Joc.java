package joc;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

import javax.imageio.ImageIO;

public class Joc implements KeyListener{
	BufferedImage menuInicial,menuFinal,fons,imatgeNauEnemiga1,estrella,fonsRecords,menuControls, imatgesMeteorits[]=new BufferedImage[5], imatgeSpinner[] = new BufferedImage[18], imatgesNauEnemiga2[] = new BufferedImage[3];
	BufferedImage imatgesNauXocOriginal[]= new BufferedImage[3],imatgesNau[]= new BufferedImage[3],imatgesNauXoc[] = new BufferedImage[3], imatgesTorreta[] = new BufferedImage[3], foratnegre, bala1, bala2, laser, laserCarregant;
	BufferedImage imatgesMeteorit11[] = new BufferedImage[34], imatgesMeteorit12[] = new BufferedImage[34], imatgesEstrellaf[] = new BufferedImage[11], nauSenseFoc, imatgesPersonatge[] = new BufferedImage[45];
	BufferedImage[] imatgesPersonatgePistola = new BufferedImage[42], imatgesNauMare = new BufferedImage[36], imatgesVidreNauMare = new BufferedImage[9], imatgesNauMarePersonatge = new BufferedImage[26];
	BufferedImage[] imatgesLadder = new BufferedImage[1];
	BufferedImage[]  imatgesPersonatgeRotacio = new BufferedImage[112]; //1-28: idled/idlee,  29-112: walkd/walke, 113-196: rund/rune
	File fitxerRecords;
	Graphics g;
	Graphics2D g2;
	Finestra f;
	Nau c;
	Personatge p;
	Minimap map;
	static double epsilon = 0; //Permet ajustar les mides de les caixes de xoc associades a cada tile pel personatge. Fent epsilon gran les fem més petites.
	int llargadaMeteorit1[]=new int[3];
	int alturaNau,llargadaNau,alturaBales,llargadaBales,alturaMeteorit1,alturaMeteorit2,llargadaMeteorit2,midaNauEnemiga1,llargadaForatNegre,alturaForatNegre,llargadaEstrella,
	alturaEstrella, llargadaBala, alturaBala, midaPaquetMunicio,midaTorreta, midaSpinner, llargadaNauEnemiga2, alturaNauEnemiga2, llargadaLaser, alturaLaser, midaCheckpoint, llargadaNauMare,
	alturaNauMare, llargadaAparcament,alturaAparcament, llargadaSliceNauMare, alturaSliceNauMare, llargadaAscensor, alturaAscensor, llargadaNauMarePersonatge,
	alturaNauMarePersonatge, llargadaSliceNauMarePersonatge, alturaSliceNauMarePersonatge;//mida objectes
	float multiplicadorMidaPersonatge;
	int alturaMinimapa,llargadaMinimapa,alturaBarres,llargadaBarres,alturaRecords,llargadaRecords, xMenuRecords, yMenuRecords, xTextFinal, yTextFinal, midaLletraRecords, separacioRecords; //mida elements UI
	int alturaNauM,llargadaNauM,llargadaMeteorit1M, llargadaMeteorit2M, midaNauEnemiga1M, llargadaCheckpointM, alturaCheckpointM,llargadaForatNegreM,midaPuntRadar, //mida elements minimapa
	midaPaquetMunicioM,midaTorretaM, midaSpinnerM, llargadaNauEnemiga2M, alturaNauEnemiga2M;
	//potser agrupar valors de dalt en vectors
	boolean calculatRecords,apuntatTemps,inici,records,controls;
	ContadorTemps contadorTemps; //l'usarem per a contar quants segons durem vius. 
	ContadorBales contadorBales; //l'usarem per a escriure per pantalla les bales que ens queden 
	ArrayList<Estrella> estrelles =new ArrayList<Estrella>();
	ArrayList<EstrellaFugac> estrellesf = new ArrayList<EstrellaFugac>();
	ArrayList<Enemic> enemics = new ArrayList<Enemic>();
	ArrayList<PaquetMunicio> paquetsmunicio = new ArrayList<PaquetMunicio>();
	ArrayList<Torreta> torretes = new ArrayList<Torreta>();
	ArrayList<Spinner> spinners = new ArrayList<Spinner>();
	ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
	ArrayList<Tile> tilesNau = new ArrayList<Tile>();
	ArrayList<Tile> tilesPersonatge = new ArrayList<Tile>();
	ArrayList<Escales> escales = new ArrayList<Escales>(); 
	ArrayList<EscalesVerticals> escalesVerticals = new ArrayList<EscalesVerticals>();
	ArrayList<Ascensor> ascensors = new ArrayList<Ascensor>();
	NauMare naumare1; //si vull més naus mare, potser posar un vector
	NauMarePersonatge naumare1P; //naumare1 vista des de la perspectiva del personatge
	Checkpoint ultimCheckpoint; //guardem l'últim checkpoint pel que hem passat
//	HashMap<Checkpoint, Boolean> checkpoints = new HashMap<Checkpoint, Boolean>(); //controla quin és l'últim checkpoint emmagetzemat
	static Random r = new Random(); //element random que usarem al llarg del codi per a aconseguir valors aleatoris.
	static double dt=0.1; //l'usem per a les físiques de la nau
	float nZoom; //per l'animació al aparcar la nau
	float diagonalMitjosZoom;
	boolean zooming;
	boolean foraNau; //val true si controlem la nau, val false si controlem el personatge
	Joc(Finestra f){
		this.f=f;
		this.g=f.g; 
		this.g2 = f.g2;
	}
	void run(){ 
		Inicialitzacio();
			while(true && !foraNau) {
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
			if(foraNau) {
				InicialitzacioPersonatge();
				while(true) {
					moviments();
					xocs();
					repintar();
					contadorTemps.apuntaTemps();
					contadorTemps.llegeixTemps();
					f.repaint();
					try {
						Thread.sleep(20); //potser s'ha de variar en funció del processador
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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
		nZoom = 0;
		zooming = false;
	}
	void InicialitzacioPersonatge() {
		System.gc(); //no sé si serveix d'alguna cosa
		p = new Personatge(this);
		f.addKeyListener(p);
		ArrayList<Enemic> m = new ArrayList<Enemic>(); 
		this.enemics = m;
		generacioMapaPersonatge();
		naumare1 = null; //naumare1 ocupa massa espai. Potser crear naumarePersonate i que sigui la naumare1 per dins
	}
	void moviments() { //Moviments de la nau, enemics, bales i estrelles i dispars dels enemics (pensa que c.x == p.x i c.y == p.y)
		if(!foraNau) c.Fisiques(); //Calculem com es mou la nau
		
		if(p!=null && foraNau) p.Fisiques(); //En cas d'estar fora la nau, calculem com es mou el personatge
		
		if(!c.aparcada) { //objectes nau
			for(int i=0;i<c.nbales;i++) { //Les bales un cop passen a ser no visibles no ho tornen a ser mai
				if(c.bales[i].isVisible) {
					c.bales[i].moure();
					if(Math.abs(c.bales[i].x-c.x)>f.AMPLADA*2 || Math.abs(c.bales[i].y-c.y)>f.ALTURA*2) c.bales[i].isVisible=false;
				}
			}
			
			//Tiles Nau
			for(Tile tile : tilesNau) { 
				//isVisible
				if(Math.abs(c.x-tile.x)>f.AMPLADA*2 || Math.abs(c.y-tile.y)>f.ALTURA*2) {
					tile.isVisible=false;
				}else {
					tile.isVisible=true;
				}
				//isMinimap 
				if(Math.abs(c.x-tile.x)<f.AMPLADA && Math.abs(c.y-tile.y)<f.ALTURA+0.5*f.ALTURA) {
					tile.isInMinimap = true;
				}
				else {
					tile.isInMinimap = false;
				}
				//movem
				tile.moure();
			}
		//Enemics
		for(Enemic enemic : enemics) {
			//isVisible
			if(Math.abs(c.x-enemic.x)>f.AMPLADA/2+enemic.llargada | Math.abs(c.y-enemic.y)>f.ALTURA/2+enemic.altura) enemic.isVisible = false;
			else enemic.isVisible = true;
			
			//isInMinimap
			if(Math.abs(c.x-enemic.x)<f.AMPLADA && Math.abs(c.y-enemic.y)<f.ALTURA+0.5*f.ALTURA) enemic.isInMinimap = true;
			else enemic.isInMinimap = false;
			
			//Move
			if(!enemic.mort) enemic.moure();
			else enemic.particules.moure();
			
			//Shoot
			if((enemic instanceof NauEnemiga1 | enemic instanceof Torreta | enemic instanceof Spinner | enemic instanceof NauEnemiga2)) {
				for(Bala bala : enemic.bales) {
					if(bala.isVisible) bala.moure();
					if(Math.abs(bala.x-c.x)>f.AMPLADA*2 | Math.abs(bala.y-c.y)>f.ALTURA*2) bala.isVisible = false;
				}
			}
			if(enemic instanceof NauEnemiga3) {
				if(((NauEnemiga3) enemic).tempsUltimTret!=0) ((NauEnemiga3) enemic).laser.moure();
//				if(((NauEnemiga3) enemic).laser.isVisible) 
			}
			if(!enemic.mort & enemic.isVisible & !c.mort) enemic.dispara();
			}
			//Estrelles de fons
			for(Estrella estrella : estrelles) estrella.moure();
			for(EstrellaFugac estrella : estrellesf) estrella.moure();
		
			//CheckpointsNau
			for(int i=0;i<checkpoints.size();i++) {
				//isVisible
				if(Math.abs(c.x-checkpoints.get(i).x)>f.AMPLADA*2 || Math.abs(c.y-checkpoints.get(i).y)>f.ALTURA*2) checkpoints.get(i).isVisible=false;
				else checkpoints.get(i).isVisible=true;
					
				//isInMinimap
				if(Math.abs(c.x-checkpoints.get(i).x)<f.AMPLADA && Math.abs(c.y-checkpoints.get(i).y)<f.ALTURA+0.5*f.ALTURA) checkpoints.get(i).isInMinimap = true;
				else checkpoints.get(i).isInMinimap = false;
					
				//Move
				checkpoints.get(i).moure();
			}
			//Paquets munició Nau
			for(int i=0;i<paquetsmunicio.size();i++) { 
			
				//isVisible		
				if(Math.abs(c.x-paquetsmunicio.get(i).x)>f.AMPLADA*2 || Math.abs(c.y-paquetsmunicio.get(i).y)>f.ALTURA*2) {
					paquetsmunicio.get(i).isVisible=false;
				}else paquetsmunicio.get(i).isVisible=true;
				
				//isInMinimap
				if(Math.abs(c.x-paquetsmunicio.get(i).x)<f.AMPLADA && Math.abs(c.y-paquetsmunicio.get(i).y)<f.ALTURA+0.5*f.ALTURA) paquetsmunicio.get(i).isInMinimap = true;
				else paquetsmunicio.get(i).isInMinimap = false;
		
				//Move
				paquetsmunicio.get(i).moure();
			}
		}
		
		if(p!=null && foraNau) { //objectes personatge
			
			if(naumare1P!=null) naumare1P.moure();
			
			for(int i=0;i<p.nbales;i++) { //Les bales un cop passen a ser no visibles no ho tornen a ser mai
				if(p.bales[i].isVisible) {
					p.bales[i].moure();
					if(Math.abs(p.bales[i].x-p.x)>f.AMPLADA*2 || Math.abs(p.bales[i].y-p.y)>f.ALTURA*2) p.bales[i].isVisible=false;
				}
			}
			//tiles Personatge
			for(Tile tile : tilesPersonatge) { 
				//isVisible
				if(Math.abs(p.x-tile.x)>f.AMPLADA*2 || Math.abs(p.y-tile.y)>f.ALTURA*2) {
					tile.isVisible=false;
				}else {
					tile.isVisible=true;
				}
				//isMinimap 
				if(Math.abs(p.x-tile.x)<f.AMPLADA && Math.abs(p.y-tile.y)<f.ALTURA+0.5*f.ALTURA) {
					tile.isInMinimap = true;
				}
				else {
					tile.isInMinimap = false;
				}
				//movem
				tile.moure();
			}
			for(Escales escales : escales) { 
				if(Math.abs(p.x-escales.x)>f.AMPLADA*2 || Math.abs(p.y-escales.y)>f.ALTURA*2) {
					escales.isVisible=false;
				}else {
					escales.isVisible=true;
				}
				//movem
				escales.moure();
			}
			for(EscalesVerticals escalaVertical : escalesVerticals) {
				if(Math.abs(p.x-escalaVertical.x)>f.AMPLADA*2 || Math.abs(p.y-escalaVertical.y)>f.ALTURA*2) escalaVertical.isVisible = true;
				else escalaVertical.isVisible = false;
				escalaVertical.moure();
			}
			for(Ascensor ascensor : ascensors) {
				if(Math.abs(p.x-ascensor.x)>f.AMPLADA*2 || Math.abs(p.y-ascensor.y)>f.ALTURA*2) {
					ascensor.isVisible=false;
				}else {
					ascensor.isVisible=true;
				}
				ascensor.moure();
			}
		}
		//Nau per quan som fora
		if(p!=null) c.moure(); //falta isVisible, etc..
		//Nau mare
		if(naumare1!=null) naumare1.moure();
		//si la nau està aparcada fem zoom i fem apareixer el personatges
		if(c.aparcada) {
			//si tenia el zooming=true a xocs aleshores no movia la nauMare al lloc que toca per tant ho he posat just després
			//dels moviments així la nauEnemiga es posa on toca i llavors li bloquegem el moviment.
			if(nZoom<92) {
			int vNauMare = 24; //velocitat animació zoom
			int vNauEspacial = 10;
			zooming = true; 
			c.llargada = Math.round(llargadaNau*(1+nZoom/vNauEspacial));
			c.altura = Math.round(alturaNau*(1+nZoom/vNauEspacial));
			diagonalMitjosZoom = Math.round(Math.sqrt(c.llargada*c.llargada+c.altura*c.altura)*0.5);
			float diagonalMitjosZoomNM = Math.round(Math.sqrt(llargadaNau*(1+nZoom/vNauMare)*llargadaNau*(1+nZoom/vNauMare)+alturaNau*(1+nZoom/vNauMare)*alturaNau*(1+nZoom/vNauMare))*0.5);
			c.xPinta = Math.round(Nau.xCentre - diagonalMitjosZoom);
			c.yPinta = Math.round(Nau.yCentre - diagonalMitjosZoom-0.30f*Math.abs(c.llargada-llargadaNau*(1+nZoom/vNauMare)));//-dfdiagonals);
			for(int i=0;i<4;i++) {
				try {
					c.imatgesNau[i] = resizeImage(nauSenseFoc,c.llargada,c.altura);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//resize nauMare
			naumare1.llargada = Math.round(llargadaNauMare*(1+nZoom/vNauMare));
			naumare1.altura = Math.round(alturaNauMare*(1+nZoom/vNauMare));
			NauMare.llargadaSlice = Math.round(llargadaSliceNauMare*(1+nZoom/vNauMare));
			NauMare.alturaSlice = Math.round(alturaSliceNauMare*(1+nZoom/vNauMare));
			//canviar la mida de la nau mare fa que vagi massa lent!
			for(int i = 0 ; i<36;i++) {
				if(i!=13 && i!=14) {
				if(naumare1.imatges[i].isVisible) {
					try {
						naumare1.imatges[i].imatge = resizeImage(imatgesNauMare[i], NauMare.llargadaSlice, NauMare.alturaSlice);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}
			}
			for(int i=0;i<9;i++) {
				if(naumare1.imatgesVidre[i].isVisible) {
					try {
						naumare1.imatgesVidre[i].imatge = resizeImage(imatgesVidreNauMare[i], NauMare.llargadaSlice, NauMare.alturaSlice);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			g.setColor(Color.PINK);
			//hem de canviar posicions de slices de la nau i canviar la mida de les slices que son visible (no cal modificar isVIsible aqui, ja ho fem a pinta)
			NauMare.llargadaAparcament = Math.round(llargadaAparcament*(1+nZoom/vNauMare));
			NauMare.alturaAparcament = Math.round(alturaAparcament*(1+nZoom/vNauMare));
			naumare1.xAparcament = Math.round(Nau.xCentre-0.5f*NauMare.llargadaAparcament);
			naumare1.yAparcament = Math.round(Nau.yCentre-0.5f*NauMare.alturaAparcament);
//			naumare1.x = Math.round(Nau.xCentre - ((NauMare.llargadaRelativa*f.AMPLADA*0.2f+NauMare.llargadaAparcamentRelativa*f.AMPLADA*0.5f)/(NauMare.llargadaRelativa*f.AMPLADA))*naumare1.llargada);
//			naumare1.y = Math.round(Nau.yCentre-((2*NauMare.alturaRelativa*f.ALTURA/3-NauMare.alturaAparcamentRelativa*f.ALTURA*0.5f)/(NauMare.alturaRelativa*f.ALTURA))*naumare1.altura+5f/70*c.llargada); //no sé perquè he de canviar el 11f/70 al canviar com faig zoom
			naumare1.x = Math.round(Nau.xCentre-0.2f*naumare1.llargada);
			naumare1.y = Math.round(Nau.yCentre - (2f/3-40f/alturaNauMare)*naumare1.altura);
			nZoom+=2;
		}
		else {
			zooming = false;
			foraNau = true;
		}
		}
	}
	void xocs() {	
		if(!c.aparcada) {
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
			if(enemic instanceof NauEnemiga3) {
				if(!(((NauEnemiga3) enemic).laser==null)) {
					if(((NauEnemiga3) enemic).laser.on &((NauEnemiga3) enemic).laser.hitBox.intersects(c.hitBox) & c.isTargetable) {
						c.vida -= Laser.damage;
						c.tempsUltimXoc = System.currentTimeMillis();
					}
				}
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
		
		//Checkpoints amb nau 
		for(Checkpoint checkpoint : checkpoints) {
			if(!c.mort & checkpoint.hitBox.intersects(c.hitBox) & !checkpoint.reached) {
				ultimCheckpoint = checkpoint;
				checkpoint.reached = true; 
				for(int i = 0; i< checkpoints.indexOf(checkpoint) ; i++) { //posem que ja hem arribat als checkpoints anteriors al que acabem d'arribar
					checkpoints.get(i).reached = true;
				}
			}
		}
		
		//aparcament
		if(naumare1!=null) {
			if(!c.aparcada && naumare1.hitBoxAparcament.contains(Nau.xCentre, Nau.yCentre) && naumare1.hitBoxAparcament.contains(Nau.xCentre, Nau.yCentre - c.llargada*0.5) && c.alpha > 5*Math.PI/4+0.3 && c.alpha < 7*Math.PI/4-0.3) { //es podria ajustar més 
				c.aparcada = true;
				for(int i = 0; i < 4 ; i++) c.imatgesNau[i] = nauSenseFoc;
				c.Vx = 0;
				c.Vy = 0;
				c.alpha = 3*Math.PI/2;
			}
		}
				
		//Mirem si s'ha mort la nau
		if(c.vida <= 0) c.mort = true;
		
		}
		
		//Nau mare amb enemics
		if(naumare1!=null) {
		for(Enemic enemic : enemics) {
			if(!enemic.mort && enemic.hitBox.intersects(naumare1.hitBox)) {
				enemic.mort = true;
				enemic.particules = new ParticleSystem(this,enemic);
			}
			//Nau mare amb partícules(les partícules que xoquen amb la nau mare desapareixen)
			if(enemic.particules!=null) {
			for(int i = 0 ; i<enemic.particules.nombreParticules ; i++) {
				if(enemic.particules.particles[i].hitBox.intersects(naumare1.hitBox)) {
					enemic.particules.particles[i].isVisible = false;
				}
			}
			}
		}
		}
		
		//xocs relacionats amb el personatge
		if(foraNau && p!=null) {
			
		p.terra = false;
		
		for(Tile tile : tilesPersonatge) {
			if (p.escaloDreta || p.escaloEsquerre || (p.y+p.altura+4>=tile.y && p.y+p.altura<=tile.y+tile.altura && Math.abs(tile.x+tile.llargada/2-p.xCentre)<tile.llargada/2+p.llargada/2)){
				p.terra = true;
			}
		}
		
		p.escaloDreta = false;
		p.escaloEsquerre = false;
		p.sobreEscalesDreta = false;
		p.sobreEscalesEsquerre =false;
		
		for(Escales escales : escales) {
			for(int i=0;i<escales.nombreEscalons;i++) { //sobre escalons
				if(escales.hitBox[i].intersects(p.hitBox)) {
					if(!p.escaloEsquerre && (p.ultimaAccio == "correrDreta" || p.ultimaAccio == "caminarDreta" || p.ultimaAccio == "saltarDreta" || p.ultimaAccio == "idleDreta" || p.ultimaAccio == "idleEsquerre" || p.ultimaAccio=="mortDreta" || p.ultimaAccio == "ajupirDreta")) p.escaloDreta = true;
					if(!p.escaloDreta && (p.ultimaAccio == "correrEsquerre" || p.ultimaAccio == "caminarEsquerre" || p.ultimaAccio == "saltarEsquerre" || p.ultimaAccio == "idleDreta" || p.ultimaAccio == "idleEsquerre" || p.ultimaAccio=="mortEsquerre" || p.ultimaAccio == "ajupirEsquerre")) p.escaloEsquerre =true;
				}
			}
			for(int i=0;i<escales.nombreEscalons;i++) { //sobre escales
				escales.hitBox[i].x-=2*escales.llargadaEscalons;
				escales.hitBox[i].width+=2*escales.llargadaEscalons;
				if(escales.hitBox[i].intersects(p.hitBox)) {
					if(!p.sobreEscalesEsquerre & (p.ultimaAccio == "correrDreta" || p.ultimaAccio == "caminarDreta" || p.ultimaAccio == "saltarDreta" || p.ultimaAccio == "idleDreta" || p.ultimaAccio == "idleEsquerre" || p.ultimaAccio == "ajupirDreta")) p.sobreEscalesDreta = true;
					if(!p.sobreEscalesDreta & (p.ultimaAccio == "correrEsquerre" | p.ultimaAccio == "caminarEsquerre" | p.ultimaAccio == "saltarEsquerre" || p.ultimaAccio == "idleDreta" || p.ultimaAccio == "idleEsquerre" || p.ultimaAccio == "ajupirEsquerre")) p.sobreEscalesEsquerre =true;
				}
				escales.hitBox[i].x+=2*escales.llargadaEscalons;
				escales.hitBox[i].width-=2*escales.llargadaEscalons;
			}
		}
//		p.sostre = false;
//		for(Tile tile : tilesPersonatge) {
//			if(p.y<tile.y+Tile.mida && Math.abs(tile.x+Tile.mida/2-Character.xCentre)<Tile.mida/2) {
//				p.sostre = true;
//			}
//		}
		
		p.esquerre = false;
		for(Tile tile : tilesPersonatge) {
			if(Math.abs(tile.y+tile.altura/2-p.yCentre)<tile.altura/2+p.altura/2-epsilon & tile.x+tile.llargada-p.x>0 & tile.x+tile.llargada-p.x<10) {
				p.esquerre = true;
			}
		}
		p.dreta = false;
		for(Tile tile : tilesPersonatge) {
			if(Math.abs(tile.y+tile.altura/2-p.yCentre)<tile.altura/2+p.altura/2-epsilon & tile.x-p.x<p.llargada & tile.x-p.x>p.llargada-10 ) {
				p.dreta = true;
			}
		}
		
		//fem que no s'enganxi a les parets
		boolean aux; //mesura si la part del centre de sota del personatage toca amb alguna tile
		aux = false;
		for(Tile tile : tilesPersonatge) {
			if(p.y+p.altura-tile.y>0 && p.y+p.altura-tile.y<2 && Math.abs(p.xCentre-tile.x-tile.llargada/2)<tile.llargada/2) {
				aux = true;
			}
		}
		if(((p.dreta | p.esquerre) & p.terra) & !aux && !p.sobreEscalesDreta && !p.sobreEscalesEsquerre) {
			p.terra = false;
		}
		for(Ascensor ascensor : ascensors) {
			if(ascensor.hitBox.intersects(p.hitBox)) ascensor.controls = true;
			else ascensor.controls = false;
		}
		p.sobreEscalesVerticals = false;
		for(EscalesVerticals escalesVerticals : escalesVerticals) {
//			if(escalesVerticals.topHitBox.intersects(p.hitBox)) escalesVerticals.top = true;
//			else escalesVerticals.top=false;
//			if(escalesVerticals.bottomHitBox.intersects(p.hitBox)) escalesVerticals.bottom = true;
//			else escalesVerticals.bottom = false;
			if(escalesVerticals.hitBox.intersects(p.hitBox) && p.y+p.altura+escalesVerticals.altura>=escalesVerticals.y && p.y+p.altura<=escalesVerticals.y+escalesVerticals.altura-10) {
				p.sobreEscalesVerticals = true;
				escalesVerticals.over = true;
			}
			else escalesVerticals.over = false;
		}
		}		
	}
	void generacioMeteorits() {
		if(!c.aparcada) {
			//meteorits
			int random  = r.nextInt(100); //potser prendre 1000 així puc afinar més
			if(random>70) { //posavem aixo per anar pujant dificultat amb el temps nivellDificultat("meteorit",27) //70
				enemics.add(new Meteorit1(this));
			}
			if(random>75) {//75
				enemics.add(new Meteorit2(this));
				if(r.nextInt(30)>16) { //donem alguna probabilitat diferent de zero a que ens apareixin meteorits grans esquerdats 
					if(enemics.size()!=0) enemics.get(enemics.size()-1).xoc=1;
				}
			} 
			if(random>98) {
				enemics.add(new NauEnemiga1(this));
			}
		
			//estrelles
			if(random>20) { // 30
				estrelles.add(new Estrella(this));
			}
			if(random>98) { //98
				estrellesf.add(new EstrellaFugac(this));
			}
		}
	}
	void restartNau() {
		apuntatTemps=false;
		c.restartValues(); //resetegem els valors de les variables associades a la nau 
		contadorTemps=new ContadorTemps(this,g); //creem un nou contador
		contadorBales=new ContadorBales(this);
		ArrayList<Enemic> m = new ArrayList<Enemic>(); //si que ens interessa resetejar aquest vector perquè sinó sen's farà molt gran
		this.enemics=m;								   
//		ArrayList<Checkpoint> ck = new ArrayList<Checkpoint>();
//		this.checkpoints=ck;
//		ArrayList<Torreta> tr = new ArrayList<Torreta>();
//		this.torretes = tr;
		calculatRecords=false;
		contadorBales.balesRestants=Nau.balesInicials;
		generacioMapa();
		//per treballar amb la segona part del joc així no he de fer zoom cada cop
		c.xFisiques = 800; //aixi surto del mateix lloc que després de fer zoom //800
		c.yFisiques = 1200;//1510; //1200
		moviments();
		foraNau = true;
	}
	void repintar() {
		g.drawImage(fons,0,0,null);
		
		if(!inici) {
			if(!foraNau) {
				//ESTRELLES, si ja porten cert temps dibuixades no les dibuixem 
				for(Estrella estrella : estrelles) if(estrella.isVisible) estrella.pinta();
				for(EstrellaFugac estrella : estrellesf) if(estrella.isVisible) estrella.pinta();
	
				//DIBUIXEM BALES NAU
				for(int i=0;i<c.nbales;i++){ //Si les bales no han xocat i la nau esta viva les pintem. 
					if(!c.bales[i].xoc & !c.mort & c.bales[i].isVisible) c.bales[i].pinta();
				}
				//DIBUIXEM TILES NAU
//				for(Tile tile : tilesNau) {
//					if(tile.isVisible) tile.pinta(); 
//				}
			}
			if(!foraNau && !c.mort) {
				//DIBUIXEM NAU BARRA VIDA I BARRA MUNICIÓ
				int dtemps = (int) Math.abs(c.tempsUltimXoc-System.currentTimeMillis()); //aqui regulem com ens avisa la nau que ha xocat amb un objecte
				if(dtemps>1000 && !c.aparcada) {														//i quan de temps la nau és invencible després de xocar
					c.isTargetable = true;
					c.pinta(g, 0);
				}
				if(dtemps <1000 && !c.aparcada) {
				c.isTargetable = false;
				for(int i=0;i<10;i++) {
					if(dtemps>i*100 && dtemps<(i+1)*100) {
						c.pinta(g, i%2);
					}
				}
				}
			}
			if(c.aparcada) {
				c.pinta(g, 0);
			}
			if(foraNau && p!=null) {
				//xocar--> pampallugues
				//barra vida
				//barra munició
				//barra stamina
				//DIBUIXEM TILES PERSONATGE
				if(naumare1P!=null) naumare1P.pinta();
				for(EscalesVerticals escalaVertical : escalesVerticals) escalaVertical.pinta();
				for(Ascensor ascensor : ascensors) if(ascensor.isVisible) ascensor.pinta();
				for(int i=0 ; i<p.nbales;i++) { //bales personatge
					if(p.bales[i].isVisible) {
						p.bales[i].pinta();
					}
				}
				p.pinta(); //aqui hauriem de posar pampallugues quan ens fan mal al personatge(com amb la nau) i també pintar les barres de vida, munició i stamina del personatge
				for(Escales escales : escales) if(escales.isVisible) escales.pinta();
				for(Tile tile : tilesPersonatge) {
					tile.pinta(); 
				}
			}
			if(!c.aparcada) {
			for(Enemic enemic : enemics) {
				if(!(enemic instanceof NauEnemiga3)) for(Bala bala : enemic.bales) bala.pinta();
				else if(((NauEnemiga3) enemic).tempsUltimTret!=0)((NauEnemiga3) enemic).laser.pinta();
			}
			
			//DIBUIXEM ENEMICS
			for(Enemic enemic : enemics) {
				if(!enemic.mort & enemic.isVisible) enemic.pinta();
				if(enemic.mort) enemic.particules.pinta();
			}
			}
//			//NAU MARE
			if(naumare1!=null) naumare1.pinta();
			
			//DIBUIXEM CHECKPOINTS
			for(Checkpoint checkpoint : checkpoints) {
				if(checkpoint.isVisible) checkpoint.pinta();
			}
			
			//DIBUIXEM PAQUETS DE MUNICIÓ
			for(PaquetMunicio paquetmunicio : paquetsmunicio) {
				if(!paquetmunicio.agafat) paquetmunicio.pinta();
			}
			
			//DIBUIXEM MENU FINAL, minimapa, barra vida, barra munició 
			if(!foraNau) {
				if(!c.mort) {
				c.pintaBarraVida(g);
//				contadorBales.pinta();
				map.pinta();
				}
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
			}

		
		if(inici) { //al menu d'inici ensenyem meteorits de fons i estrelles
			for(int i=0;i<estrelles.size();i++) {
				if(estrelles.get(i).isVisible) {
					estrelles.get(i).pinta();
				}
			}
			for(Enemic enemic : enemics) if((enemic instanceof Meteorit1 | enemic instanceof Meteorit2) & enemic.isVisible & !c.aparcada) enemic.pinta();
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
	    Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST); //hi ha altres algorismes 
	    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB); //type_int_argb respecta la transperència
	    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
	    return outputImage;
	}
	BufferedImage resizeImage(BufferedImage originalImage, double mult) throws IOException { //anàleg però explícitem quants cops més grossa volem que sigui la nova imatge
		int targetWidth = Math.round((float)(originalImage.getWidth()*mult));
		int targetHeight = Math.round((float)(originalImage.getHeight()*mult));
		Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST); //hi ha altres algorismes 
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
		midaNauEnemiga1 = Math.round(NauEnemiga1.llargadaRelativa*f.AMPLADA);
		midaNauEnemiga1M = Math.round(Minimap.midaNauEnemiga1Relativa*f.AMPLADA);
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
		llargadaForatNegreM = Math.round(Minimap.llargadaForatNegreRelativa*f.AMPLADA);
		alturaBala = Math.round(Bala.alturaRelativa*f.ALTURA);
		llargadaBala = Math.round(Bala.llargadaRelativa*f.AMPLADA);
		midaPaquetMunicio = Math.round(PaquetMunicio.midaRelativa*f.AMPLADA);
		midaPaquetMunicioM = Math.round(Minimap.midaPaquetMunicioRelativa*f.AMPLADA);
		midaTorreta = Math.round(Torreta.llargadaRelativa*f.AMPLADA);
		midaTorretaM = Math.round(Minimap.midaTorretaRelativa*f.AMPLADA);
		midaSpinner = Math.round(Spinner.midaRelativa * f.AMPLADA);
		midaSpinnerM = Math.round(Minimap.midaSpinnerRelativa * f.AMPLADA);
		llargadaNauEnemiga2 = Math.round(NauEnemiga2.llargadaRelativa * f.AMPLADA);
		alturaNauEnemiga2 = Math.round(NauEnemiga2.alturaRelativa * f.ALTURA);
		llargadaNauEnemiga2M = Math.round(Minimap.llargadaNauEnemiga2Relativa * f.AMPLADA);
		alturaNauEnemiga2M = Math.round(Minimap.alturaNauEnemiga2Relativa * f.ALTURA);
		llargadaLaser = Math.round(Laser.llargadaRelativa * f.AMPLADA);
		alturaLaser = Math.round(Laser.alturaRelativa * f.ALTURA);
		midaCheckpoint = Math.round(Checkpoint.llargadaRelativa * f.AMPLADA);
		llargadaCheckpointM = Math.round(Minimap.llargadaCheckpointRelativa*f.AMPLADA);
		alturaCheckpointM = Math.round(Minimap.alturaCheckpointRelativa*f.ALTURA);
		llargadaNauMare = Math.round(NauMare.llargadaRelativa * f.AMPLADA);
		alturaNauMare = Math.round(NauMare.alturaRelativa * f.AMPLADA);
		llargadaAparcament = Math.round(NauMare.llargadaAparcamentRelativa * f.AMPLADA);
		alturaAparcament = Math.round(NauMare.alturaAparcamentRelativa * f.ALTURA);
		llargadaSliceNauMare = Math.round(NauMare.llargadaSliceRelativa * f.AMPLADA);
		alturaSliceNauMare = Math.round(NauMare.alturaSliceRelativa * f.AMPLADA);
		llargadaNauMarePersonatge = Math.round(NauMarePersonatge.llargadaRelativa * f.AMPLADA);
		alturaNauMarePersonatge = Math.round(NauMarePersonatge.alturaRelativa * f.ALTURA);
		llargadaSliceNauMarePersonatge = Math.round(NauMarePersonatge.llargadaSliceRelativa * f.AMPLADA);
		alturaSliceNauMarePersonatge = Math.round(NauMarePersonatge.alturaSliceRelativa * f.ALTURA);
		multiplicadorMidaPersonatge = Personatge.multiplicadorMidaRelatiu * f.AMPLADA;
		llargadaAscensor = Math.round(Ascensor.llargadaRelativa * f.AMPLADA);
		alturaAscensor = Math.round(Ascensor.alturaRelativa * f.ALTURA);
	}
	void resizeImages()throws IOException{ // canviem la mida de les imatges usant les mides calculades anteriorment
		for(int i=0; i<3;i++) { 
			imatgesNau[i]=resizeImage(imatgesNau[i],llargadaNau,alturaNau);
			imatgesNauXoc[i]=resizeImage(imatgesNauXoc[i],llargadaNau,alturaNau);
		} 
		for(int i=0;i<3;i++) {
			imatgesMeteorits[i] = resizeImage(imatgesMeteorits[i],llargadaMeteorit1[i],alturaMeteorit1);
		}
		for(int i =0 ;i<18;i++) imatgeSpinner[i] = resizeImage(imatgeSpinner[i],midaSpinner,midaSpinner);
		
		imatgesMeteorits[3] = resizeImage(imatgesMeteorits[3],llargadaMeteorit2,alturaMeteorit2);
		imatgesMeteorits[4] = resizeImage(imatgesMeteorits[4],llargadaMeteorit2,alturaMeteorit2);
		imatgeNauEnemiga1 = resizeImage(imatgeNauEnemiga1,midaNauEnemiga1,midaNauEnemiga1);
		foratnegre = resizeImage(foratnegre,llargadaForatNegre,alturaForatNegre );
		bala1 = resizeImage(bala1,llargadaBala,alturaBala);
		bala2 = resizeImage(bala2,llargadaBala,alturaBala);
		fons=resizeImage(fons,f.AMPLADA,f.ALTURA);
		menuInicial=resizeImage(menuInicial,f.AMPLADA,f.ALTURA);
		menuFinal=resizeImage(menuFinal,f.AMPLADA,f.ALTURA);
		menuControls=resizeImage(menuControls,f.AMPLADA,f.ALTURA);
		fonsRecords=resizeImage(fonsRecords,f.AMPLADA,f.ALTURA);
		for(int i=0;i<3;i++) imatgesTorreta[i] = resizeImage(imatgesTorreta[i], midaTorreta*2,midaTorreta*2); //multiplico per dos perquè la imatge de la torreta és el doble de llarga i el doble de ample que la torreta en si
		for(int i=0;i<3;i++) imatgesNauEnemiga2[i] = resizeImage(imatgesNauEnemiga2[i], llargadaNauEnemiga2, alturaNauEnemiga2);
		for(int i=0;i<34;i++) imatgesMeteorit11[i] = resizeImage(imatgesMeteorit11[i], llargadaMeteorit1[1], alturaMeteorit1);
		for(int i=0;i<34;i++) imatgesMeteorit12[i] = resizeImage(imatgesMeteorit12[i], llargadaMeteorit1[0], alturaMeteorit1);
		laser = resizeImage(laser, llargadaLaser, alturaLaser*90/50);
		laserCarregant = resizeImage(laserCarregant, llargadaLaser, alturaLaser*90/50);
		nauSenseFoc = resizeImage(nauSenseFoc, llargadaNau, alturaNau);
		for(int i = 0 ; i<36;i++) {
			if(i!=13 && i!=14) {
			imatgesNauMare[i] = resizeImage(imatgesNauMare[i],llargadaSliceNauMare, alturaSliceNauMare);
			}
		}
		for(int i = 0 ;i<9;i++) {
			imatgesVidreNauMare[i] = resizeImage(imatgesVidreNauMare[i],llargadaSliceNauMare, alturaSliceNauMare);
		}
		for(int i=0;i<42;i++) imatgesPersonatgePistola[i] = resizeImage(imatgesPersonatgePistola[i],multiplicadorMidaPersonatge);
		for(int i=0;i<45;i++) imatgesPersonatge[i] = resizeImage(imatgesPersonatge[i],multiplicadorMidaPersonatge);
		for(int i = 0;i<26;i++) {
			imatgesNauMarePersonatge[i] = resizeImage(imatgesNauMarePersonatge[i],llargadaSliceNauMarePersonatge,alturaSliceNauMarePersonatge);
		}
		for(int i=0;i<112;i++) {
			imatgesPersonatgeRotacio[i] = resizeImage(imatgesPersonatgeRotacio[i],multiplicadorMidaPersonatge);
		}
//		checkpoint = resizeImage(checkpoint, midaCheckpoint, midaCheckpoint);
	}
	public void generacioMapa() { //ELEMENTS FIXOS DEL MAPA: CHECKPOINTS, npcs, BASES ENEMIGUES ...  (per la nau)
		//checkpoints
//		Checkpoint checkpoint2 = new Checkpoint(this, 1500,1000);
//		Checkpoint checkpoint3 = new Checkpoint(this, 1800,1000);
//		Checkpoint checkpoint4 = new Checkpoint(this, 2100,1000);
//		Checkpoint checkpoint5 = new Checkpoint(this, 2400,1000);
//		Checkpoint checkpoint6 = new Checkpoint(this, 2700,1000);
//		Checkpoint checkpoint7 = new Checkpoint(this, 3000,1000);
//		checkpoints.add(checkpoint2);
//		checkpoints.add(checkpoint3);
//		checkpoints.add(checkpoint4);
//		checkpoints.add(checkpoint5);
//		checkpoints.add(checkpoint6);
//		checkpoints.add(checkpoint7);
		
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
//		Torreta torreta1 = new Torreta(this,800,1200,0.5,4,200);
//		enemics.add(torreta1);
//		Torreta torreta3 = new Torreta(this, 600, 300, 2, 1, 300);
//		enemics.add(torreta3);
		
//		Torreta torreta2 = new Torreta(this,600,300,0.5,8,120);
//		enemics.add(torreta2);
		
		//spinners
//		Spinner spinner1 = new Spinner(this,500,300);
//		enemics.add(spinner1);
		
//		forats negre
//		ForatNegre foratnegre1 = new ForatNegre(this,100,300);
//		enemics.add(foratnegre1);
		
//		Naus enemigues 1
//		NauEnemiga1 nau1 = new NauEnemiga1(this,900,300);
//		enemics.add(nau1);
		
//		Naus enemigues 2
//		NauEnemiga2 nau2 = new NauEnemiga2(this,100,100,0.5,400,20);

//		enemics.add(nau2);
//		NauEnemiga2 nau3 = new NauEnemiga2(this,100,100,0.1,400);
//		enemics.add(nau3);
//		NauEnemiga3 nau3 = new NauEnemiga3(this,500,500,2,0.5,0.5,1,15);
//		NauEnemiga3 nau4 = new NauEnemiga3(this,1700,100,2,0.5,0.5,1,15);
//		NauEnemiga3 nau5 = new NauEnemiga3(this,1700,700,2,0.5,0.5,1,15);
//		NauEnemiga3 nau6 = new NauEnemiga3(this,1700,800,2,0.5,0.5,1,15);
//		NauEnemiga3 nau7 = new NauEnemiga3(this,1700,900,2,0.5,0.5,1,15);
//		enemics.add(nau4);
//		enemics.add(nau5);
//		enemics.add(nau6);
//		enemics.add(nau7);
		
//		Nau Mare
		naumare1 = new NauMare(this, 1000,1000);
		
		
//		torreta1.bales.add(new Bala(this, torreta1, 100, 0));
	}
	void generacioMapaPersonatge() { //anàleg pel personatge
//		naumare1.moure(); //passo per aqui per a ajustar les físiques a les del personatge
		//tiles personatge, escales, enemics personatge
		naumare1P = new NauMarePersonatge(this);
		escales.add(new Escales(this, naumare1P.x+Math.round((1950f/15000)*naumare1P.llargada),naumare1P.y+Math.round((4933f/7500)*naumare1P.altura),2000,1000,100 )); 
		escalesVerticals.add(new EscalesVerticals(this, naumare1P.x+Math.round((1950f/15000)*naumare1P.llargada) - 750 + 2200, naumare1P.y+Math.round((4930f/7500)*naumare1P.altura-(1500f/1080)*f.ALTURA),50f/1920,1500f/1080,0));
		int[] distanciesAscensor = new int[]{Math.round(((1606f-alturaAscensor*0.5f)/7500)*naumare1P.altura),Math.round(((1606f-alturaAscensor*0.5f)/7500)*naumare1P.altura),Math.round(((1606f-alturaAscensor*0.5f)/7500)*naumare1P.altura)};
		ascensors.add(new Ascensor(this,naumare1P.x+Math.round((3968f/15000)*naumare1P.llargada), naumare1P.y+Math.round(naumare1P.altura/2-Ascensor.alturaRelativa*f.ALTURA/2),3,distanciesAscensor,1)); 
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
			imatgeNauEnemiga1 = ImageIO.read(getClass().getResource("/enemic1.png"));
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
				imatgesNauXoc[i-1] = ImageIO.read(getClass().getResource("/nauespacial2"+i+"grey.png"));
			} catch (IOException e) {
			}
			try {
				imatgesNau[i-1] = ImageIO.read(getClass().getResource("/nauespacial2"+i+".png"));
			} catch (IOException e) {
			}
		}
		try {
			foratnegre = ImageIO.read(getClass().getResource("/foratnegre.png"));
		} catch (IOException e) {
		}
		try {
			bala1 = ImageIO.read(getClass().getResource("/bala1.png"));
		} catch (IOException e) {
		}
		try {
			bala2 = ImageIO.read(getClass().getResource("/bala2.png"));
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
		for(int i=1 ;i<4;i++) { 
			try {
				imatgesNauEnemiga2[i-1] = ImageIO.read(getClass().getResource("/nauenemiga2"+i+".png"));
			} catch (IOException e) {
			}
		}
		for(int i=1;i<35;i++) {
			try {
				imatgesMeteorit11[i-1] = ImageIO.read(getClass().getResource("/meteorit1"+i+".png"));
				imatgesMeteorit12[i-1] = ImageIO.read(getClass().getResource("/meteorit2"+i+".png"));
			} catch(IOException e) {
			}
		}
		try {
			laser = ImageIO.read(getClass().getResource("/laser.png"));
			laserCarregant = ImageIO.read(getClass().getResource("/lasercarregant.png"));
		} catch (IOException e) {
		}
		for(int i=1 ;i<12;i++) { 
			try {
				imatgesEstrellaf[i-1] = ImageIO.read(getClass().getResource("/estrellaf"+i+".png"));
			} catch (IOException e) {
			}
		}
		try {
			nauSenseFoc = ImageIO.read(getClass().getResource("/nausensefoc.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			try {
				imatgesLadder[0] = ImageIO.read(getClass().getResource("/escalavertical1.png"));
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		try {
			imatgesPersonatge[0] = ImageIO.read(getClass().getResource("/idle1.png"));
			imatgesPersonatge[1] = ImageIO.read(getClass().getResource("/idle1e.png"));
			imatgesPersonatge[34] = ImageIO.read(getClass().getResource("/jump1.png"));
			imatgesPersonatge[35] = ImageIO.read(getClass().getResource("/jump2.png"));
			imatgesPersonatge[36] = ImageIO.read(getClass().getResource("/jump1e.png"));
			imatgesPersonatge[37] = ImageIO.read(getClass().getResource("/jump2e.png"));
			imatgesPersonatge[38] = ImageIO.read(getClass().getResource("/idle2.png"));
			imatgesPersonatge[39] = ImageIO.read(getClass().getResource("/idle2e.png"));
			imatgesPersonatge[39] = ImageIO.read(getClass().getResource("/idle2e.png"));
			imatgesPersonatge[43] = ImageIO.read(getClass().getResource("/crouch1.png"));
			imatgesPersonatge[44] = ImageIO.read(getClass().getResource("/crouch1e.png"));
			imatgesPersonatgePistola[0] = ImageIO.read(getClass().getResource("/idleG1.png"));
			imatgesPersonatgePistola[1] = ImageIO.read(getClass().getResource("/idleG1e.png"));
			imatgesPersonatgePistola[34] = ImageIO.read(getClass().getResource("/jumpG1.png"));
			imatgesPersonatgePistola[35] = ImageIO.read(getClass().getResource("/jumpG2.png"));
			imatgesPersonatgePistola[36] = ImageIO.read(getClass().getResource("/jumpG1e.png"));
			imatgesPersonatgePistola[37] = ImageIO.read(getClass().getResource("/jumpG2e.png"));
			imatgesPersonatgePistola[38] = ImageIO.read(getClass().getResource("/idleG2.png"));
			imatgesPersonatgePistola[39] = ImageIO.read(getClass().getResource("/idleG2e.png"));
			imatgesPersonatgePistola[40] = ImageIO.read(getClass().getResource("/crouchG1.png"));
			imatgesPersonatgePistola[41] = ImageIO.read(getClass().getResource("/crouchG1e.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int i=1;i<7;i++) {
			try {
				imatgesPersonatge[i+1] = ImageIO.read(getClass().getResource("/walk"+i+".png"));
				imatgesPersonatge[i+7] = ImageIO.read(getClass().getResource("/walk"+i+"e.png"));
				imatgesPersonatgePistola[i+1] = ImageIO.read(getClass().getResource("/walkG"+i+".png"));
				imatgesPersonatgePistola[i+7] = ImageIO.read(getClass().getResource("/walkG"+i+"e.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i=1;i<9;i++) {
			try {
				imatgesPersonatge[13+i] = ImageIO.read(getClass().getResource("/run"+i+".png"));
				imatgesPersonatge[21+i] = ImageIO.read(getClass().getResource("/run"+i+"e.png"));
				imatgesPersonatgePistola[13+i] = ImageIO.read(getClass().getResource("/runG"+i+".png"));
				imatgesPersonatgePistola[21+i] = ImageIO.read(getClass().getResource("/runG"+i+"e.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i=1;i<3;i++) {
			try {
				imatgesPersonatge[29+i] = ImageIO.read(getClass().getResource("/death"+i+".png"));
				imatgesPersonatge[31+i] = ImageIO.read(getClass().getResource("/death"+i+"e.png"));
				imatgesPersonatgePistola[29+i] = ImageIO.read(getClass().getResource("/deathG"+i+".png"));
				imatgesPersonatgePistola[31+i] = ImageIO.read(getClass().getResource("/deathG"+i+"e.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i=1;i<4;i++) {
			try {
				imatgesPersonatge[39+i] = ImageIO.read(getClass().getResource("/ladder"+i+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i=1;i<37;i++) {
		if(i!=14 && i!=15) {
		try {
				imatgesNauMare[i-1] = ImageIO.read(getClass().getResource("/naumare"+i+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		for(int i=1;i<10;i++) {
			try {
				imatgesVidreNauMare[i-1] = ImageIO.read(getClass().getResource("/naumare"+i+"vidre.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i = 1;i<27;i++) {
			try {
				imatgesNauMarePersonatge[i-1] = ImageIO.read(getClass().getResource("/" + i + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int a=0;
		for(int i=1;i<14;i=i+2) {
			try {
				a+=1;
				imatgesPersonatgeRotacio[i-1] = ImageIO.read(getClass().getResource("/IdlePistola" + a + ".png"));
				imatgesPersonatgeRotacio[i] = ImageIO.read(getClass().getResource("/IdlePistola"+ a + "_.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		a=0;
		for(int i=1;i<14;i=i+2) {
			try {
				a+=1;
				imatgesPersonatgeRotacio[13+i] = ImageIO.read(getClass().getResource("/IdlePistola" + a + "e.png"));
				imatgesPersonatgeRotacio[14+i] = ImageIO.read(getClass().getResource("/IdlePistola"+ a + "e_.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i=29; i<113 ;i++) {
			try {
				imatgesPersonatgeRotacio[i-1] = ImageIO.read(getClass().getResource("/pP" + i + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fitxerRecords=new File("records.txt"); //no troba el fitxer quan exportem en runnable jar
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==80 && c.mort && !records && !inici) {
			restartNau();
		}
		if(e.getKeyCode()==80 && inici && !records) {
			inici=false;
			restartNau();
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