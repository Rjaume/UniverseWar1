package joc;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Joc implements KeyListener{
	BufferedImage menuInicial,menuFinal,fons,enemic1,estrella,fonsRecords,menuControls, imatgesMeteorits[]=new BufferedImage[5], imatgesNau[]=new BufferedImage[3];
	BufferedImage imatgesNauXoc[] = new BufferedImage[3], foratnegre;
	File fitxerRecords;
	Graphics g;
	Finestra f;
	Nau c;
	boolean calculatRecords,apuntatTemps,inici,records,controls;
	ContadorTemps contadorTemps; //l'usarem per a contar quants segons durem vius. 
	ContadorBales contadorBales; //l'usarem per a escriure per pantalla les bales que ens queden 
	ArrayList<Estrella> estrelles =new ArrayList<Estrella>();
	ArrayList<Enemic> enemics = new ArrayList<Enemic>();
	static Random r = new Random(); //element random que usarem al llarg del codi per a aconseguir valors aleatoris.
	static double dt=0.1; //l'usem per a les físiques de la nau
	int lastimmobilex; //x de l'últim enemic immobil generat
	Joc(Finestra f){
		this.f=f;
		this.g=f.g; 
	}
	void run() { 
		Inicialitzacio();
			while(true) {
				System.out.println(c.Vx); 
				moviments();
				generacioEnemics();
				xocs(); 
				repintar();
				contadorTemps.apuntaTemps();
				contadorTemps.llegeixTemps();
				f.repaint(); //crida update
				try {
					Thread.sleep(20); //potser s'ha de variar en funció del processador
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	}
	void Inicialitzacio() {
		loadResources(); //obtenim les imatges i fitxers que farem servir 
		c=new Nau(this); //creem la nostra nau.
		contadorTemps=new ContadorTemps(this,g); //creem un nou contador
		contadorBales=new ContadorBales(this);
		f.addKeyListener(c);
		f.addKeyListener(this);
		inici=true;
		apuntatTemps=false;
		records=false;
		calculatRecords=false;
		lastimmobilex = -10000;
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
		try {
			imatgesNau[0] = ImageIO.read(getClass().getResource("/nauespacial1.png"));
		} catch (IOException e) {
		}
		try {
			imatgesNau[1] = ImageIO.read(getClass().getResource("/nauespacial2.png"));
		} catch (IOException e) {
		}
		try {
			imatgesNau[2] = ImageIO.read(getClass().getResource("/nauespacial3.png"));
		} catch (IOException e) {
		}
		try {
			imatgesMeteorits[0] = ImageIO.read(getClass().getResource("/meteorit1.png")); 
		} catch (IOException e) {
		}
		try {
			imatgesMeteorits[1] = ImageIO.read(getClass().getResource("/meteorit2.png"));
		} catch (IOException e) {
		}
		try {
			imatgesMeteorits[2] = ImageIO.read(getClass().getResource("/meteorit3.png"));
		} catch (IOException e) {
		}
		try {
			imatgesMeteorits[3] = ImageIO.read(getClass().getResource("/meteorit21.png"));
		} catch (IOException e) {
		}
		try {
			imatgesMeteorits[4] = ImageIO.read(getClass().getResource("/meteorit22.png"));
		} catch (IOException e) {
		}
		try {
			enemic1 = ImageIO.read(getClass().getResource("/enemic1.png"));
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
		try {
			imatgesNauXoc[0] = ImageIO.read(getClass().getResource("/nauespacial1grey.png"));
		} catch (IOException e) {
		}
		try {
			imatgesNauXoc[1] = ImageIO.read(getClass().getResource("/nauespacial2grey.png"));
		} catch (IOException e) {
		}
		try {
			imatgesNauXoc[2] = ImageIO.read(getClass().getResource("/nauespacial3grey.png"));
		} catch (IOException e) {
		}
		try {
			foratnegre = ImageIO.read(getClass().getResource("/foratnegre.png"));
		} catch (IOException e) {
		}
		fitxerRecords=new File("records.txt"); //no sé si així funciona quan exportem en un runnable jar
		}
	void moviments() { //moviments de la nau, enemics, bales i estrelles i dispars dels enemics
		
		c.Fisiques(); //calculem com es mou la nau
		
		for(int i=0;i<c.nbales;i++) //bales nau
			c.bales[i].moureDreta();
		
		for(int i=0;i<enemics.size();i++) { //enemics
			enemics.get(i).moure();
			if(enemics.get(i) instanceof NauEnemiga1) {
				for(int j=0;j<(enemics.get(i)).bales.size();j++) {
					((enemics.get(i)).bales.get(j)).moureEsquerra();
				}
				if(enemics.get(i).mort==false && enemics.get(i).disparatRecentment==false) {
					enemics.get(i).dispara();
				}
			}
		}
		
		for(int i=0;i<estrelles.size();i++) {//estrelles de fons
			estrelles.get(i).moure();
		}
	}
	void xocs() {		
		//XOCS BALES AMB ENEMICS	
		for(int i=0;i<c.nbales;i++) {
			for(int j=0;j<enemics.size();j++) {
				if(Math.abs(c.bales[i].x-enemics.get(j).x)<=15 && c.bales[i].y<=enemics.get(j).y+enemics.get(j).altura && c.bales[i].y+1>=enemics.get(j).y && (enemics.get(j).xoc<enemics.get(j).vida) && !inici && !c.bales[i].xoc) {
					enemics.get(j).xoc+=1;
					c.bales[i].xoc=true;
				}
				if(enemics.get(j).xoc>=enemics.get(j).vida) {
					enemics.get(j).mort=true;
				}
			}
		}
		//XOC NAU AMB ENEMICS
		for(int i=0;i<enemics.size();i++) {
			if (c.isTargetable && !enemics.get(i).calculatXoc) { //estem assumint que no podem xocar més d'un cop amb el mateix enemics, això podria canviar més endavant
				if((Nau.y+30>enemics.get(i).y)&&(Nau.y<enemics.get(i).y+enemics.get(i).altura)&&(Math.abs(enemics.get(i).x-Nau.x)<=30)&&(enemics.get(i).xoc<enemics.get(i).vida)&&inici==false) { //hem de demanar que el meteorit no hagi estat matat i que no estiguem al menu inicial 
					c.vida -= enemics.get(i).bodyDamage;
					c.tempsUltimXoc = (int)System.currentTimeMillis();
					enemics.get(i).calculatXoc = true;
				}
			}
			else{ if(!((Nau.y+30>enemics.get(i).y)&&(Nau.y<enemics.get(i).y+enemics.get(i).altura)&&(Math.abs(enemics.get(i).x-Nau.x)<=30)&&(enemics.get(i).xoc<enemics.get(i).vida)&&inici==false)){
				enemics.get(i).calculatXoc = false; //així podem xocar més d'un cop amb el mateix enemic
			}
			}
		}
		
		//XOC BALES ENEMICS AMB NAU
		for(int i=0;i<enemics.size();i++) {
			for(int j=0;j<enemics.get(i).bales.size();j++) {
				if(c.isTargetable && !(enemics.get(i).bales).get(j).calculatXoc){
					if((Nau.x-enemics.get(i).bales.get(j).x<=10)&&(enemics.get(i).bales.get(j).x-Nau.x<=50)&&(enemics.get(i).bales.get(j).y-Nau.y>=-1)&&(enemics.get(i).bales.get(j).y-Nau.y<=32)&&(enemics.get(i).bales.get(j).xoc==false)&&(c.mort==false)&&(inici==false)) {
						c.vida -= Bala.bulletDamage; //s'haura de canviar si en algun moment posem diferents tipus de bales
						c.tempsUltimXoc = (int)System.currentTimeMillis();
						(enemics.get(i).bales).get(j).calculatXoc = true;
					}	
				}
			}
		}
		
		//MIREM SI S'HA MORT LA NAU
		if(c.vida <= 0) {
			c.mort = true;
		}
	}
	void generacioEnemics() {
		//ENEMICS MOBILS, ANEM GENERANT
		if(r.nextInt(30)>nivellDificultat("meteorit",25)) { 
			enemics.add(new Meteorit1(this));
		}
		if(r.nextInt(30)>nivellDificultat("meteoritGran",22)) {
			//enemics.add(new Meteorit2(this));
			if(r.nextInt(30)>16) { //donem alguna probabilitat diferent de zero a que ens apareixin meteorits grans esquerdats 
				//enemics.get(enemics.size()-1).xoc=1;
			}
		} 
		if(r.nextInt(100)>nivellDificultat("enemic",98)) {
			//enemics.add(new NauEnemiga1(this));
		}
	
		//GENERACIÓ ESTRELLES, SUPOSO QUE MÉS ENDAVANT FAREM UNA FUNCIÓ QUE ES DIGUI GENERAMAPA O POTSER GENERAFONS
		//(ja funciona s'ha de retocar el 200 obviament)
		if(r.nextInt(100)>30) {
			estrelles.add(new Estrella(this));
		}
		//ENEMICS IMMOBILS, ANEM GENERANT CADA CERTA DISTÀNCIA RECORREGUDA PER LA NAU

		if((c.xFisiques-lastimmobilex)>1000){ //cada 1000 pixels forat negre
			enemics.add(new ForatNegre(this)); 
			lastimmobilex = c.xFisiques;
		}
	}
	void restart() {
		apuntatTemps=false;
		contadorTemps.tempsInicial=System.currentTimeMillis();//resetegem el temps inicial del contador. 
		c.restartValues(); //resetegem els valors de les variables associades a la nau 
		contadorTemps=new ContadorTemps(this,g); //creem un nou contador
		contadorBales=new ContadorBales(this);
		ArrayList<Enemic> m = new ArrayList<Enemic>();
		this.enemics=m;
		calculatRecords=false;
		contadorBales.balesRestants=Nau.balesInicials;
		lastimmobilex = -10000;
	}
	void repintar() {
		g.drawImage(fons,0,0,null);
		if(inici==false) {
			//DIBUIXEM ESTRELLES, si ja porten cert temps dibuixades no les dibuixem 
			for(int i=0;i<estrelles.size();i++) {
				if(estrelles.get(i).isVisible){
				estrelles.get(i).pinta(g);
				}
			}
			//DIBUIXEM BALES
			for(int i=0;i<c.nbales;i++){
				if(c.bales[i].xoc==false && c.mort==false && c.bales[i].isVisible) { //si les bales no han xocat i la nau esta viva les pintem. 
				c.bales[i].pinta(g);
				}
			}
			for(int i=0;i<enemics.size();i++) {
				for(int j=0;j<enemics.get(i).bales.size();j++) {
					if(enemics.get(i).bales.get(j).xoc==false && enemics.get(i).bales.get(j).isVisible) { 
						enemics.get(i).bales.get(j).pintaBalaEnemic(g);
					}
				}
			}
			//DIBUIXEM ENEMICS
			for(int i=0;i<enemics.size();i++) {
				if(enemics.get(i).mort==false && enemics.get(i).isVisible) { 
					enemics.get(i).pinta(g);
				}
			}
			//DIBUIXEM NAU BARRA VIDA I BARRA MUNICIÓ
			if(!c.mort) {
				int dtemps = Math.abs(c.tempsUltimXoc-(int)System.currentTimeMillis()); //aqui regulem com ens avisa la nau que ha xocat amb un objecte
				if(dtemps>750) {
					c.isTargetable = true;
					c.pinta(g, 0);
				}
				else {
				c.isTargetable = false;
				for(int i=0;i<10;i++) {
					if(dtemps>i*75 && dtemps<(i+1)*75) {
						c.pinta(g, i%2);
					}
				}
				}
				c.pintaBarraVida(g);
				contadorBales.pinta();
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
		
		if(inici) { //al menu d'inici ensenyem enemics de fons i estrelles
			for(int i=0;i<estrelles.size();i++) {
				if(estrelles.get(i).isVisible) {
					estrelles.get(i).pinta(g);
				}
			}
			for(int i=0;i<enemics.size();i++) {
				if(!(enemics.get(i) instanceof NauEnemiga1) && enemics.get(i).isVisible) {
				enemics.get(i).pinta(g);
				}
			}
			if(!records && !controls) {
				g.drawImage(menuInicial,0,0,null); //ensenyem el menu inicial últim per a que els meteorits es vegin per sota. 
			} 
			if(records && !controls){
			g.drawImage(fonsRecords,0,0,null);
			contadorTemps.pintaRecords();
			}
			if(controls && !records) {
				g.drawImage(menuControls,0,0,null);
			}
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
	}
	@Override
	public void keyReleased(KeyEvent e) {
	}
}