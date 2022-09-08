package joc;

import java.awt.Color;

public class NauMarePersonatge extends Objecte{ //nau mare vista des de la perspectiva del personatge
	//vector amb moltes imageSlices, vaig dibuixant la part de la nau que veig amb el personatge
	ImageSlice[] imatges = new ImageSlice[26];
	static float llargadaRelativa = 15000f/1920, alturaRelativa = 7500f/1080, llargadaSliceRelativa = 750f/1920, alturaSliceRelativa = 750f/1080;
	int llargadaSlice, alturaSlice;
 	NauMarePersonatge(Joc joc){
 		this.joc = joc;
 		this.g = joc.g;
 		this.xInicial = joc.naumare1.x;
 		this.yInicial = joc.naumare1.y;
 		x = xInicial;
 		y = yInicial;
 		this.llargada = joc.llargadaNauMarePersonatge;
 		this.altura = joc.alturaNauMarePersonatge;
 		this.llargadaSlice = joc.llargadaSliceNauMarePersonatge;
 		this.alturaSlice = joc.alturaSliceNauMarePersonatge;
		//get images
		//tiles

 		joc.tilesPersonatge.add(new Tile(joc,x,Math.round(joc.naumare1.y+(159f/242)*altura),llargada,5, joc.p));
		joc.tilesPersonatge.add(new Tile(joc,Math.round(joc.c.xPinta+joc.diagonalMitjosZoom-15),Math.round(joc.c.yPinta+joc.diagonalMitjosZoom),30,5, joc.p));
		joc.tilesPersonatge.add(new Tile(joc,x,y+Math.round((2480f/7500)*altura),Math.round((3960f/15000)*llargada),5,joc.p)); //terra motor de dalt 
		joc.tilesPersonatge.add(new Tile(joc,x,y+ Math.round((5358f/7500)*altura),Math.round((3960f/15000)*llargada),5,joc.p)); //terra motor de baix 
		for(int i=0;i<26;i++) {
			imatges[i] = new ImageSlice();
			imatges[i].imatge = joc.imatgesNauMarePersonatge[i];
		}
		for(int i=1;i<7;i++) { //tercera fila
			imatges[i-1].x = x+llargadaSlice*(i-1);
			imatges[i-1].y=y+alturaSlice*2;
		}
		for(int i=1;i<7;i++) {
			imatges[i+5].x = x+llargadaSlice*(i-1);
			imatges[i+5].y=y+alturaSlice*3;
		}
		imatges[12].x = x+llargadaSlice*5; //cinquena fila
		imatges[12].y = y+alturaSlice*4;
		imatges[13].x = x+llargadaSlice*5; //sisena fila
		imatges[13].y = y+alturaSlice*5;
		for(int i=1;i<7;i++) { //setena fila
			imatges[i+13].x = x+llargadaSlice*(i-1);
			imatges[i+13].y=y+alturaSlice*6;
		}
		for(int i=1;i<7;i++) { //vuitena fila
			imatges[i+19].x = x+llargadaSlice*(i-1);
			imatges[i+19].y=y+alturaSlice*7;
		}
 	}
	void moure() {
		x = xInicial-joc.p.xFisiques;
		y = yInicial-joc.p.yFisiques;
		for(int i=1;i<7;i++) { //tercera fila
			imatges[i-1].x = x+llargadaSlice*(i-1);
			imatges[i-1].y=y+alturaSlice*2;
		}

		for(int i=1;i<7;i++) {
			imatges[i+5].x = x+llargadaSlice*(i-1);
			imatges[i+5].y=y+alturaSlice*3;
		}
		imatges[12].x = x+llargadaSlice*5; //cinquena fila
		imatges[12].y = y+alturaSlice*4;
		imatges[13].x = x+llargadaSlice*5; //sisena fila
		imatges[13].y = y+alturaSlice*5;
		for(int i=1;i<7;i++) { //setena fila
			imatges[i+13].x = x+llargadaSlice*(i-1);
			imatges[i+13].y=y+alturaSlice*6;
		}
		for(int i=1;i<7;i++) { //vuitena fila
			imatges[i+19].x = x+llargadaSlice*(i-1);
			imatges[i+19].y=y+alturaSlice*7;
		}
	}
	void pinta() {
//		for(int i=0;i<26;i++) {
//			g.drawImage(imatges[i].imatge,imatges[i].x,imatges[i].y,null);
//		}
		for(int i=0;i<26;i++) {
//			if(Math.abs(imatges[i].x+llargadaSlice*0.5-joc.p.xCentre)<joc.f.AMPLADA*0.5+0.5*llargadaSlice && Math.abs(imatges[i].y+alturaSlice*0.5-joc.p.yCentre)<joc.f.ALTURA*0.5+0.5*alturaSlice) {
//				imatges[i].isVisible = true;
				g.drawImage(imatges[i].imatge,imatges[i].x,imatges[i].y,null);
//				g.fillRect(imatges[i].x,imatges[i].y,llargadaSlice,alturaSlice);
				g.setColor(Color.GREEN);
				g.drawRect(imatges[i].x,imatges[i].y,llargadaSlice,alturaSlice);
				
//			}
//			else imatges[i].isVisible = false;
		}
	}
}
