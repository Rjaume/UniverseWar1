package joc;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;

public class Tile extends Objecte{
	Objecte objecte;
	public Tile(Joc joc, int x, int y, int llargada, int altura, Objecte objecte) {
		this.joc=joc;
		this.g = joc.g;
		this.xInicial = x;
		this.yInicial = y;
		this.x = xInicial;
		this.y = yInicial;
		this.llargada = llargada;
		this.altura = altura;
		this.objecte = objecte; //nau o personatge
		hitBox = new Rectangle(x,y,llargada,altura);
	}
	void moure() {
//		if(!joc.foraNau) {
//			x = xInicial - joc.c.xFisiques;
//			y = yInicial - joc.c.yFisiques;
//		}
//		else if(joc.p!=null) {
//			x = xInicial - joc.p.xFisiques;
//			y = yInicial - joc.p.yFisiques;
//		}
		if(objecte instanceof Nau) {
			x = xInicial - joc.c.xFisiques;
			y = yInicial - joc.c.yFisiques;
		}
		else if(objecte instanceof Personatge) {
			x = xInicial - joc.p.xFisiques;
			y = yInicial - joc.p.yFisiques;
		}
		hitBox.setLocation(x,y);
	}
	void pinta() {
		g.setColor(Color.RED);
//		int[] xpoints = new int[4];
//		int[] ypoints = new int[4];
//		xpoints[0]=x;
//		ypoints[0]=y;
//		xpoints[1]=x+llargada;
//		ypoints[1]=y;
//		xpoints[3]=x;
//		ypoints[3]=y+altura;
//		xpoints[2]=x+llargada;
//		ypoints[2]=y+altura; 
//		Polygon imatge = new Polygon(xpoints,ypoints,4); //podria també posar tiles inclinades (ho tinc al projecte Personatge)
//		g.fillPolygon(imatge); 
		g.fillRect(x,y,llargada,altura);
	}
}
