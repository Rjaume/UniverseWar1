package joc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Nau implements KeyListener {
	static int balesInicials=50;
	Bala bales[];
	int nbales; //conta el nombre de bales que queden a la nau. 
	int n; //l'usem per a l'animació 
	static float llargadaRelativa = (float)50./1440, alturaRelativa = (float)32./900;//mides de la nau, relatives a la mida de la pantalla. Son els quocients llargada/(amplada pantalla) i altura/(altura pantalla))
	static int llargada,altura;
	static int xBarraVida, yBarraVida=40, llargadaRectangleVida, alturaRectangleVida;  //fixem la posició de la barra de Vida que determina la de la barra de bales
	static int llargadaMinimapa = 2, alturaMinimapa = 2;
	int tempsUltimXoc;
	static int x,y; //posició on pintem la nau (el centre de la finestra).
	static int vidaMaxima = 100; //vida maxima de la nau, cada xoc ens traurà un nombre determinat de vida 
	int vida;
	static int xVida = 10, yVida = 10; //posició de la barra de vida 
	boolean mort,fletxaDreta,fletxaEsquerra,fletxaAmunt,fletxaAvall,isTargetable;
	Joc joc;
	int xFisiques,yFisiques; //on calculem les físiques (posició respecte la posició inicial), que usarem per a moure la resta d'objectes.
	static float F=5000,Ff=700,M=60,vMax=120; //Força dels motors de la nau, Força de fregament, Massa de la nau i Velocitat maxima de la nau
	float Vx,Vy,Fx,Fy,Ffx,Ffy;
	float FxG,FyG;//forces Fx i Fy degudes al camp gravitatori
	BufferedImage imatgesNau[] = new BufferedImage[4];
	BufferedImage imatgesNauXoc[] = new BufferedImage[4];
	
	Nau(Joc joc){
		this.joc=joc;
		x=joc.f.AMPLADA/2-llargada/2;
		y=joc.f.ALTURA/2-altura/2;
		llargada = joc.llargadaNau;
		altura = joc.alturaNau;
		llargadaRectangleVida = joc.llargadaBarres;
		alturaRectangleVida = joc.alturaBarres;
		xBarraVida = Math.round(ContadorBales.xBarraRelativa*llargadaRectangleVida); //no pot ser que la barra ens xoqui amb el text
		xFisiques=0;
		yFisiques=0;
		n=0;
		isTargetable = true;
		bales=new Bala[balesInicials];
		nbales=0;//nombre de bales gastades inicial es 0;
		vida = vidaMaxima; //inicialment la nau té la mida maxima 
		mort=false;//inicialment la nau no està morta
		for(int i=0;i<3;i++) {
			imatgesNau[i]=joc.imatgesNau[i];
			imatgesNauXoc[i]=joc.imatgesNauXoc[i];
		}
		imatgesNau[3]=joc.imatgesNau[2];
		imatgesNauXoc[3]=joc.imatgesNauXoc[2];
		tempsUltimXoc = 0;
		Vx=0;
		Vy=0;
		Fx=0;
		Fy=0;
		Ffx=0;
		Ffy=0;
	}
	void disparar() { 
		if(nbales<balesInicials) {
			bales[nbales]=new Bala(joc);
			nbales+=1;
			joc.contadorBales.balesRestants-=1;
		}
	}
	void pinta(Graphics g, int a) { //si enviem a=1 pintem la nau grisa(ho usem per a fer pampallugues quan xoquem)
		//dibuixem nau
		if(a==1) {
			n+=1;
			n=n%4;
			g.drawImage(imatgesNauXoc[n],x,y,null);
		}
		else {
			n+=1;
			n=n%4;
			g.drawImage(imatgesNau[n],x,y,null);
		}
	}
	void pintaGris(Graphics g) {
		n+=1;
		n=n%4;
		g.drawImage(imatgesNauXoc[n],x,y,null);
	}
	void pintaBarraVida(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("MyriadPro",Font.PLAIN,Math.round(ContadorBales.midaTextRelativa * alturaRectangleVida)));
		g.drawString("health: ",15,yBarraVida + alturaRectangleVida);
		g.setColor(Color.RED);
		for(int i=0; i<=this.vida; i++) {
			g.fillRect(xBarraVida+i*llargadaRectangleVida,yBarraVida,llargadaRectangleVida,alturaRectangleVida); 
		}
		g.drawRect(xBarraVida,yBarraVida,Nau.vidaMaxima*llargadaRectangleVida,alturaRectangleVida);
	}
	void restartValues() {
		mort=false; //posem que la nau no s'ha mort 
		isTargetable = true;
		tempsUltimXoc = 0;
		vida = Nau.vidaMaxima;
		xFisiques=0;
		yFisiques=0; //posem la posició calculada per les físiques a 0.
		nbales=0;
		Fx=0;
		Fy=0;
		Vx=0;
		Vy=0;
		bales=new Bala[balesInicials];
	}
	void Fisiques() { //segurament això ho hauries de tenir en una classe 
		
		//MOVIMENT UNIFORMEMENT ACCELERAT.
		if(!joc.inici && !mort) {
		if(fletxaDreta) { 
			if(Vx<vMax) {//no podem accelerar indefinidament
				Fx=Nau.F; 
			}
		}
		if(fletxaEsquerra) { 
			if(Vx>-50) {//velocitat màxima marxa enrere (fem que no puguem anar més rapid que els meteorits)
				Fx=-F;
			}
		}
		if(fletxaAmunt) {
			if(Math.abs(Vy)<150) {
				Fy=-F;
			}
		}
		if(fletxaAvall) {
			if(Math.abs(Vy)<150) {
				Fy=F;
			}
		}
		
		if(Math.abs(Vx)<1) { //Si la velocitat és molt propera a zero no donem força de fregament. 
			Ffx=0;
		}else {
			Ffx=Ff;
		}
		if(Math.abs(Vy)<1) {
			Ffy=0;
		}else {
			Ffy=Ff;
		}
		if(Vx<0) {//La força de fregament ha d'anar en sentit contrari al moviment. 
			Ffx=-Ffx;
		}
		if(Vy<0) {
			Ffy=-Ffy;
		}
		//CAMP GRAVITATORI DEGUT ALS FORATS NEGRES:
		float res1 = 0;
		for(int i=0; i<joc.enemics.size();i++) {
			if(joc.enemics.get(i) instanceof ForatNegre && !joc.enemics.get(i).isNegligible) {
				res1+=joc.enemics.get(i).campgravitatori.Fx();
			}
		}
		FxG = res1;
		float res2 = 0;
		for(int i=0; i<joc.enemics.size();i++) {
			if(joc.enemics.get(i) instanceof ForatNegre && !joc.enemics.get(i).isNegligible) {
				res2+=joc.enemics.get(i).campgravitatori.Fy();
			}
		}
		FyG = res2;
		
		//Equacions del moviment uniformement accelerat.
		
		float FX = Fx-Ffx+FxG; //forces resultants sobre la nau 
		float FY = Fy-Ffy+FyG;
		Vx+=((FX)/Nau.M)*Joc.dt; 
		xFisiques+=Vx*Joc.dt+0.5*((FX)/Nau.M)*(Joc.dt)*(Joc.dt);
		xFisiques=(int)xFisiques;
		Vy+=((FY)/Nau.M)*Joc.dt;
		yFisiques+=Vy*Joc.dt+0.5*((Fy)/Nau.M)*(Joc.dt)*(Joc.dt);
		yFisiques=(int)yFisiques;
		Fx=0;
		Fy=0;
		FxG=0;
		FyG=0;
		}
		
	}
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_D && mort==false) {
			fletxaDreta=true;
		}
		if(e.getKeyCode()==KeyEvent.VK_A && mort==false) {
			fletxaEsquerra=true;
		}
		if(e.getKeyCode()==KeyEvent.VK_W && mort==false) {
			fletxaAmunt=true;
		}
		if(e.getKeyCode()==KeyEvent.VK_S && mort==false) {
			fletxaAvall=true;
		}
		if(e.getKeyCode()==32 && mort==false) {
			disparar();
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_D) {
			fletxaDreta=false;
		}
		if(e.getKeyCode()==KeyEvent.VK_A) {
			fletxaEsquerra=false;
		}
		if(e.getKeyCode()==KeyEvent.VK_W) {
			fletxaAmunt=false;
		}
		if(e.getKeyCode()==KeyEvent.VK_S) {
			fletxaAvall=false;
		}
	}
}
