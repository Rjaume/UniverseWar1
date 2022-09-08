package joc;
import java.awt.Rectangle;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class NauMare extends Objecte{
	Rectangle hitBoxAparcament;
//	int xAparcament, yAparcament;
	static float llargadaAparcamentRelativa = (float)50./1400, alturaAparcamentRelativa = (float)50./900;
	static float llargadaRelativa = (float)2430./1440, alturaRelativa = (float)1210./1440; //mides relatives a la mida de la pantalla
	static int llargadaAparcament, alturaAparcament;
	int xAparcament, yAparcament, xAparcamentInicial, yAparcamentInicial;
	int lastx, lasty; //ultima posició abans de que el personatge baixi de la nau 
	ImageSlice[] imatges = new ImageSlice[36]; //imatge de la nau mare a trossos per aconseguir millor rendiment
	ImageSlice[] imatgesVidre = new ImageSlice[9];
	static float llargadaSliceRelativa = 243f/1440, alturaSliceRelativa = 242f/1440;
	static int llargadaSlice;
	static int alturaSlice; //mides de les particions de la imatge de la nau mare
	NauMare(Joc joc, int x, int y){
		this.joc = joc;
		this.g = joc.g;
		this.x = x;
		this.y = y;
		xInicial = x;
		yInicial = y;
		llargadaAparcament = joc.llargadaAparcament;
		alturaAparcament = joc.alturaAparcament;
		llargada = joc.llargadaNauMare;
		altura = joc.alturaNauMare;
		hitBox = new Rectangle(x,y,llargada,altura);
		isVisible = true;
		xCentre = x+Math.round(llargada*0.5f);
		yCentre = y+Math.round(altura*0.5f);
		xAparcament = Math.round(x+llargada*0.2f);
		yAparcament = y+2*altura/3-alturaAparcament-20;
		hitBoxAparcament = new Rectangle(xAparcament,yAparcament,llargadaAparcament,alturaAparcament);
		joc.tilesNau.add(new Tile(joc,x,y,llargada, altura/3,joc.c));
		joc.tilesNau.add(new Tile(joc,x,y+Math.round(2f/3*altura),llargada,altura/3,joc.c));
		llargadaSlice = joc.llargadaSliceNauMare;
		alturaSlice = joc.alturaSliceNauMare;
		for(int i = 0; i<36; i++) imatges[i] = new ImageSlice();
		for(int i=0; i<9;i++) imatgesVidre[i] = new ImageSlice();
		for(int i=0;i<36;i++) {
			imatges[i].imatge = joc.imatgesNauMare[i];
			imatges[i].isVisible = true;
		}
		for(int i=0;i<9;i++) {
			imatgesVidre[i].imatge = joc.imatgesVidreNauMare[i];
			imatgesVidre[i].isVisible = true;
		}
		for(int i=0;i<3;i++) { //primera fila
			imatges[i].x = x+llargadaSlice*i;
			imatges[i].y = y;}
		for(int i=0;i<10;i++) { //segona fila
			imatges[i+3].x = x+llargadaSlice*i;
			imatges[i+3].y = y+alturaSlice;}
		for(int i=0;i<10;i++) { //tercera fila
			imatges[i+13].x = x+llargadaSlice*i;
			imatges[i+13].y = y+alturaSlice*2;}
		for(int i=0;i<10;i++) { //quarta fila
			imatges[i+23].x = x+llargadaSlice*i;
			imatges[i+23].y = y+alturaSlice*3;}
		for(int i=0;i<3;i++) { //cinquena fila
			imatges[i+33].x = x+llargadaSlice*i;
			imatges[i+33].y = y+alturaSlice*4;}
		for(int i=0;i<3;i++) { //primera fila vidre
			imatgesVidre[i].x = x+llargadaSlice*i;
			imatgesVidre[i].y = y+alturaSlice;
		}
		for(int i=0;i<3;i++) { //segona fila vidre
			imatgesVidre[i+3].x = x+llargadaSlice*i;
			imatgesVidre[i+3].y = y+alturaSlice*2;
		}
		for(int i=0;i<3;i++) { //tercera fila vidre
			imatgesVidre[i+6].x = x+llargadaSlice*i;
			imatgesVidre[i+6].y = y+alturaSlice*3;
		}
		
	}
	void pinta() {
		if(!joc.foraNau) {
		for(int i=0;i<36;i++) {
			if(Math.abs(imatges[i].x+llargadaSlice*0.5-Nau.xCentre)<joc.f.AMPLADA*0.5+0.5*llargadaSlice && Math.abs(imatges[i].y+alturaSlice*0.5-Nau.yCentre)<joc.f.ALTURA*0.5+0.5*alturaSlice) {
				imatges[i].isVisible = true;
				g.drawImage(imatges[i].imatge,imatges[i].x,imatges[i].y,null);
				g.setColor(Color.GREEN);
				g.drawRect(imatges[i].x,imatges[i].y, llargadaSlice,alturaSlice);
				
			}
			else imatges[i].isVisible = false;
		}
			for(int i = 0;i<9;i++) {
				if(Math.abs(imatgesVidre[i].x+llargadaSlice*0.5-Nau.xCentre)<joc.f.AMPLADA*0.5+0.5*llargadaSlice && Math.abs(imatgesVidre[i].y+alturaSlice*0.5-Nau.yCentre)<joc.f.ALTURA*0.5+0.5*alturaSlice) {
					imatgesVidre[i].isVisible = true;
					if(i<3) g.drawImage(imatgesVidre[i].imatge,x+llargadaSlice*i, y+alturaSlice,null);
					if(i>2 && i<6) g.drawImage(imatgesVidre[i].imatge,x+llargadaSlice*(i-3),y+alturaSlice*2,null);
					if(i>5) g.drawImage(imatgesVidre[i].imatge,x+llargadaSlice*(i-6),y+alturaSlice*3,null);
				}
				else imatgesVidre[i].isVisible = false;
			}
		}
		if(!joc.c.aparcada) {
			g.setColor(Color.YELLOW);	
			joc.g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
			g.fillRect(xAparcament, yAparcament, llargadaAparcament, alturaAparcament);
			joc.g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			}
//		g.setColor(Color.BLACK);
//		g.fillRect(x,y,llargada,altura/3);
//		g.fillRect(x,y+2*altura/3, llargada, altura/3);
//		g.fillRect(x+llargada,y,10,altura);
//		g.fillRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
	}
//	void pintaVidre() {
//		
//	}
	void moure() {
		if(!joc.zooming && !joc.foraNau) {
		x = xInicial - joc.c.xFisiques;
		y = yInicial - joc.c.yFisiques;
		xCentre = x+llargada/2;
		yCentre = y+altura/2;
		xAparcament =  Math.round(x+llargada*0.2f);
		yAparcament = y+2*altura/3-alturaAparcament-20;
		hitBox.setLocation(x,y);
		hitBoxAparcament.setLocation(xAparcament, yAparcament);
		}
		if(joc.zooming) {
			lastx = x;
			lasty = y;
		}
		if(joc.p!=null && joc.foraNau) {
			x = lastx - joc.p.xFisiques;
			y = lasty - joc.p.yFisiques;
			xCentre = x+llargada/2;
			yCentre = y+altura/2;
			hitBox.setLocation(x,y);
		}
		for(int i=0;i<3;i++) { //primera fila
			imatges[i].x = x+llargadaSlice*i;
			imatges[i].y = y;}
		for(int i=0;i<10;i++) { //segona fila
			imatges[i+3].x = x+llargadaSlice*i;
			imatges[i+3].y = y+alturaSlice;}
		for(int i=0;i<10;i++) { //tercera fila
			imatges[i+13].x = x+llargadaSlice*i;
			imatges[i+13].y = y+alturaSlice*2;}
		for(int i=0;i<10;i++) { //quarta fila
			imatges[i+23].x = x+llargadaSlice*i;
			imatges[i+23].y = y+alturaSlice*3;}
		for(int i=0;i<3;i++) { //cinquena fila
			imatges[i+33].x = x+llargadaSlice*i;
			imatges[i+33].y = y+alturaSlice*4;}
		for(int i=0;i<3;i++) { //primera fila vidre
			imatgesVidre[i].x = x+llargadaSlice*i;
			imatgesVidre[i].y = y+alturaSlice;
		}
		for(int i=0;i<3;i++) { //segona fila vidre
			imatgesVidre[i+3].x = x+llargadaSlice*i;
			imatgesVidre[i+3].y = y+alturaSlice*2;
		}
		for(int i=0;i<3;i++) { //tercera fila vidre
			imatgesVidre[i+6].x = x+llargadaSlice*i;
			imatgesVidre[i+6].y = y+alturaSlice*3;
		}
	}
}
