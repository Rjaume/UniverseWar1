package joc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; //listener pel teclat
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener; //listener pels botons del ratolí
import java.awt.event.MouseMotionListener; //listener pel moviment del ratolí
import java.awt.image.BufferedImage;

public class Nau extends Objecte implements KeyListener, MouseListener, MouseMotionListener{
	static int balesInicials=1000; //originalment 50
	Bala bales[];
	int xPinta,yPinta; //posició on pintem la imatge de la nau (té mida més grossa que la nau ja que rota ens canvia la mida de les imatges)
	static int xCentre,yCentre;//posició central de la nau
	int nbales; //conta el nombre de bales que queden a la nau. 
	static int velocitatBales = 300; //300
	int n; //l'usem per a l'animació  // 50,32
	static float llargadaRelativa = (float)60./1440, alturaRelativa = (float)40./900;//mides de la nau, relatives a la mida de la pantalla. Son els quocients llargada/(amplada pantalla) i altura/(altura pantalla))
	static float diagonal;
	static int xBarraVida, yBarraVida=40, llargadaRectangleVida, alturaRectangleVida;  //fixem la posició de la barra de Vida que determina la de la barra de bales
	double tempsUltimXoc;
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
	double alpha; //angle que forma la semirecta definida pel ratolí i la nau i la horitzontal 
	int r ; //per a randomitzar cap on esquiva la nauEnemiga2
	Rectangle hitBox;
	boolean aparcada, colision;
	Nau(Joc joc){
		joc.f.addMouseMotionListener(this); //afegim el listener del moviment del ratoli respecte la nostra finestraf
		joc.f.addMouseListener(this);
		this.joc=joc;
		llargada = joc.llargadaNau;
		altura = joc.alturaNau;
		diagonal = (float)Math.sqrt(llargada*llargada+altura*altura);
//		int midarotacio = Math.round((float)Math.sqrt(llargada*llargada+altura*altura)); //el mateix que a rota però en el cas concret de la nau
		xCentre=joc.f.AMPLADA/2;
		yCentre=joc.f.ALTURA/2;
		xPinta=Math.round(joc.f.AMPLADA/2-diagonal/2); //pintem la nau sempre al centre de la finestra
		yPinta=Math.round(joc.f.ALTURA/2-diagonal/2); //AQUI HEM DE TENIR EN COMPTE LA NOVA MIDA DONADA A ROTA 
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
		hitBox = new Rectangle(x,y,llargada,altura);
		r = Joc.r.nextInt(2);
		aparcada = false;
		colision = false;
	}
	void disparar() { 
		if(nbales<balesInicials && !joc.inici && !aparcada) {
			bales[nbales]=new Bala(joc,this);
			nbales+=1;
			joc.contadorBales.balesRestants-=1;
		}
		r = Joc.r.nextInt(2);
	}
	void pinta(Graphics g, int a) { //si enviem a=1 pintem la nau grisa(ho usem per a fer pampallugues quan xoquem)
		//dibuixem nau
		if(!aparcada) {
		alpha = calculaAngle();
		BufferedImage imatgesNauRotades[]= new BufferedImage[4];
		BufferedImage imatgesNauXocRotades[] = new BufferedImage[4];
		for(int i=0;i<4;i++) {
			imatgesNauRotades[i] = rota(imatgesNau[i],alpha); 
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
		else {
			g.drawImage(rota(imatgesNau[0],3*Math.PI/2),xPinta, yPinta,null);
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
//		xFisiques=0;
//		yFisiques=0; //posem la posició calculada per les físiques a 0.
		if(!(joc.ultimCheckpoint==null)) {
			xFisiques = joc.ultimCheckpoint.xInicial-x;
			yFisiques = joc.ultimCheckpoint.yInicial-y;
		}
		else {
			xFisiques = 0;
			yFisiques = 0;
		}
		nbales=0;
		Fx=0;
		Fy=0;
		Vx=0;
		Vy=0;
		bales=new Bala[balesInicials];
	}
	static BufferedImage rota(BufferedImage entrada, double angle) { //hem de donar angles entre zero i 2PI. (sentit horari)!!!!, permet rotar qualsevol imatge. El problema és que al rotar la imatge d'un objecte aleshores perdo la posició d'aquest
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
		if(!aparcada) {
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
		return(this.alpha);
	}
	void moure() { //per moure la nau quan som fora
			x = joc.f.AMPLADA/2-joc.p.xFisiques;
			y = joc.f.ALTURA/2-joc.p.yFisiques;
			xPinta = Math.round(x - Math.round(Math.sqrt(llargada*llargada+altura*altura))*0.5f);
			yPinta = Math.round(y - Math.round(Math.sqrt(llargada*llargada+altura*altura))*0.5f-0.33f*Math.abs(llargada-joc.llargadaNau*(1+91f/22))); //ajust cutre (com el que faig a zooming = true)
	}
	void Fisiques() { //segurament això ho hauriem de tenir en una classe a part
		if(!aparcada) {
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
			Vx = 0;
		}else {
			Ffx=Ff;
		}
		if(Math.abs(Vy)<1) {
			Ffy=0;
			Vy = 0;
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
		Vy+=((FY)/Nau.M)*Joc.dt;
		
		//COL·LISIONS NAU AMB TILES
		//xocs verticals
		for(Tile tile: joc.tilesNau) { 
			tile.hitBox.y -= Vy/10;  //sembla que Vy i Vx son massa grans
			if(tile.hitBox.intersects(hitBox)) {
				tile.hitBox.y += Vy/10;
				while(!tile.hitBox.intersects(hitBox)) tile.hitBox.y-=Math.signum(Vy);
				tile.hitBox.y+=Math.signum(Vy);
				if(Math.signum(FY)==Math.signum(Vy)) FY = 0; 
				Vy = 0;
				yFisiques += (tile.y-tile.hitBox.y); //ajustem yFisiques per tal que tile.y quedi a tile.hitBox.y
			}
		}
		//xocs horitzontals
		for(Tile tile: joc.tilesNau) {
			tile.hitBox.x -= Vx/10;
			if(tile.hitBox.intersects(hitBox)) {
				tile.hitBox.x += Vx/10;
				while(!tile.hitBox.intersects(hitBox)) tile.hitBox.x-=Math.signum(Vx);
				tile.hitBox.x+=Math.signum(Vx);
				if(Math.signum(FX)==Math.signum(Vx)) FX = 0; 
				Vx = 0;
				xFisiques += (tile.x-tile.hitBox.x); //ajustem yFisiques per tal que tile.y quedi a tile.hitBox.y
			}
		}
		xFisiques+=Vx*Joc.dt+0.5*((FX)/Nau.M)*(Joc.dt)*(Joc.dt);
		xFisiques=(int)xFisiques;
		yFisiques+=Vy*Joc.dt+0.5*((FY)/Nau.M)*(Joc.dt)*(Joc.dt);
		yFisiques=(int)yFisiques;
		Fx=0;
		Fy=0;
		FxG=0;
		FyG=0;
		if(hitBox.width == llargada && hitBox.height == altura) {
		hitBox.width=altura; //mirem si al canviar la mida de la hitBox xocarem amb alguna tile, en cas de fer-ho no volem canviar la mida de la hitBox perquè ens podria donar problemes
		hitBox.height=llargada;
		hitBox.x = xCentre - altura/2;
		hitBox.y = yCentre - llargada/2;
		colision = false;
		for(Tile tile : joc.tilesNau) {
			if(tile.hitBox.intersects(hitBox)) colision = true;
		}
		hitBox.width=llargada;
		hitBox.height=altura;
		hitBox.x = xCentre - llargada/2;
		hitBox.y = yCentre - altura/2;
		}
		if(hitBox.width == altura && hitBox.height == llargada) {
			hitBox.width=llargada; //mirem si al canviar la mida de la hitBox xocarem amb alguna tile, en cas de fer-ho no volem canviar la mida de la hitBox perquè ens podria donar problemes
			hitBox.height=altura;
			hitBox.x = xCentre - llargada/2;
			hitBox.y = yCentre - altura/2;
			colision = false;
			for(Tile tile : joc.tilesNau) {
				if(tile.hitBox.intersects(hitBox)) colision = true;
			}
			hitBox.width=altura;
			hitBox.height=llargada;
			hitBox.x = xCentre - altura/2;
			hitBox.y = yCentre - llargada/2;
		}
		if(!colision) { //si no estem en una col·lisió canviem hitBox al rotar (la posem vertical o horitzontal)
		hitBox.width = llargada;
		hitBox.height = altura;
		hitBox.x = x;
		hitBox.y = y;
		if((alpha>Math.PI/4 && alpha<3*Math.PI/4) || (alpha>5*Math.PI/4 && alpha<7*Math.PI/4)) {
			hitBox.x = Math.round(xPinta + 0.5f*(diagonal - altura));
			hitBox.y = Math.round(yPinta + 0.5f*(diagonal - llargada));
			hitBox.width = altura;
			hitBox.height = llargada;
		}
		}
		}		
		}
	}
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_D && mort==false && !joc.foraNau) {
			fletxaDreta=true;
		}
		if(e.getKeyCode()==KeyEvent.VK_A && mort==false && !joc.foraNau) {
			fletxaEsquerra=true;
		}
		if(e.getKeyCode()==KeyEvent.VK_W && mort==false && !joc.foraNau) {
			fletxaAmunt=true;
		}
		if(e.getKeyCode()==KeyEvent.VK_S && mort==false && !joc.foraNau) {
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
