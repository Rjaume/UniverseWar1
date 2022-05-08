package joc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; //listener pel teclat
import java.awt.event.MouseEvent; 
import java.awt.event.MouseListener; //listener pels botons del ratolí
import java.awt.event.MouseMotionListener; //listener pel moviment del ratolí
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class Nau extends Objecte implements KeyListener, MouseListener, MouseMotionListener{
	static int balesInicials=50; //originalment 50
	Bala bales[];
	int xPinta,yPinta; //posició on pintem la imatge de la nau (té mida més grossa que la nau ja que rota ens canvia la mida de les imatges)
	int nbales; //conta el nombre de bales que queden a la nau. 
	int n; //l'usem per a l'animació 
	static float llargadaRelativa = (float)50./1440, alturaRelativa = (float)32./900;//mides de la nau, relatives a la mida de la pantalla. Son els quocients llargada/(amplada pantalla) i altura/(altura pantalla))
	static float diagonal;
	static int xBarraVida, yBarraVida=40, llargadaRectangleVida, alturaRectangleVida;  //fixem la posició de la barra de Vida que determina la de la barra de bales
	int tempsUltimXoc;
	static int vidaMaxima = 100; //vida maxima de la nau, cada xoc ens restarà un nombre determinat de vida 
	int vida; //vida actual de la nau 
	static int xVida = 10, yVida = 10; //posició de la barra de vida 
	boolean mort,fletxaDreta,fletxaEsquerra,fletxaAmunt,fletxaAvall,isTargetable;
	Joc joc;
	int xFisiques,yFisiques; //on calculem les físiques (posició respecte la posició inicial), que usarem per a moure la resta d'objectes.
	static float F=5000,Ff=700,M=60,vMax=120; //Força dels motors de la nau, Força de fregament, Massa de la nau i Velocitat maxima de la nau
	float Vx,Vy,Fx,Fy,Ffx,Ffy;
	float FxG,FyG;//forces Fx i Fy degudes al camp gravitatori
	BufferedImage imatgesNau[] = new BufferedImage[4];
	BufferedImage imatgesNauXoc[] = new BufferedImage[4];
	int xRatoli,yRatoli;
	double alpha; //angle que forma la semirecta definida pel ratolí i la nau i la direcció on apunta actualment la nau
	Nau(Joc joc){
		joc.f.addMouseMotionListener(this); //afegim el listener del moviment del ratoli respecte la nostra finestra
		joc.f.addMouseListener(this);
		this.joc=joc;
		llargada = joc.llargadaNau;
		altura = joc.alturaNau;
		diagonal = (float)Math.sqrt(llargada*llargada+altura*altura);
		int midarotacio = Math.round((float)Math.sqrt(llargada*llargada+altura*altura)); //el mateix que a rota però en el cas concret de la nau
		xPinta=joc.f.AMPLADA/2-midarotacio/2; //pintem la nau sempre al centre de la finestra
		yPinta=joc.f.ALTURA/2-midarotacio/2; //AQUI HEM DE TENIR EN COMPTE LA NOVA MIDA DONADA A ROTA 
		x=Math.round((float)joc.f.AMPLADA/2)-Math.round((float)llargada/2); //posició real de la nau
		y=Math.round((float)joc.f.ALTURA/2)-Math.round((float)altura/2);
		llargadaRectangleVida = joc.llargadaBarres;
		alturaRectangleVida = joc.alturaBarres;
		xBarraVida = Math.round(ContadorBales.xBarraRelativa*llargadaRectangleVida); 
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
		llargadaMinimapa = joc.llargadaNauM;
		alturaMinimapa = joc.alturaNauM;
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
		alpha = calculaAngle();
		BufferedImage imatgesNauRotades[]= new BufferedImage[4];
		BufferedImage imatgesNauXocRotades[] = new BufferedImage[4];
		for(int i=0;i<4;i++) {
			imatgesNauRotades[i] = rota(imatgesNau[i],alpha); //provem rota
			imatgesNauXocRotades[i] = rota(imatgesNauXoc[i],alpha);
		}
		if(a==1) {
			n+=1;
			n=n%4;
			g.drawImage(imatgesNauXocRotades[n],xPinta,yPinta,null);
		}
		else {
			n+=1;
			n=n%4;
			g.drawImage(imatgesNauRotades[n],xPinta,yPinta,null);
		}
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
	static BufferedImage rota(BufferedImage entrada, double angle) { //hem de donar angles entre zero i 2PI. (sentit horari), permet rotar qualsevol imatge. El problema és que al rotar la imatge d'un objecte aleshores perdo la posició d'aquest
		int amplada = entrada.getWidth();
	    int altura = entrada.getHeight();
	    double theta = Math.atan((float)altura/amplada); //angle per a fer els càlculs de la rotació 
	    double cos = Math.cos(angle+theta), sin = Math.sin(angle+theta);
		int diagonal = Math.round((float)Math.sqrt(amplada*amplada+altura*altura)); 
		double R = (double)diagonal/2;
		BufferedImage rotated = new BufferedImage(diagonal, diagonal, entrada.getType());  
		Graphics2D graphic = rotated.createGraphics();
		graphic.translate(R-R*cos,R-R*sin); //traslladem el graphics a la posició adecuadada del cercle centrat al centre de rotated de diàmetre diagonal
		graphic.rotate(angle); //rotem el graphics l'angle desitjat
		graphic.drawRenderedImage(entrada,null); //i ara pintem la nostra imatge al graphics que està rotat i a la posició adecuada
		graphic.dispose();
		return rotated;
	}
	double calculaAngle() { 
		double x = xRatoli;
		double y = yRatoli;
		double xCentre = joc.f.AMPLADA/2;
		double yCentre = joc.f.ALTURA/2;
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
	void Fisiques() { //segurament això ho hauriem de tenir en una classe a part
		
		//MOVIMENT UNIFORMEMENT ACCELERAT.
		if(!joc.inici && !mort) {
		if(fletxaDreta) { 
			if(Vx<vMax) {//no podem accelerar indefinidament
				Fx=F; 
			}
		}
		if(fletxaEsquerra) { 
			if(Vx>-vMax) {//velocitat màxima marxa enrere (fem que no puguem anar més rapid que els meteorits)
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
		
		float FX = Fx-Ffx+FxG; //forces resultants sobre la nau, degudes als motors de la nau i als forats negres
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
