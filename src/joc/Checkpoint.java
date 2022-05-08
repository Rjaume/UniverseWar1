package joc;
import java.awt.Graphics;
import java.awt.Color;

public class Checkpoint extends Objecte{
	Joc joc;
	Graphics g;
	boolean isVisible;
	boolean isInMinimap;
	int xInicial,yInicial;
	public Checkpoint(Joc joc, int x, int y) { //per a crear un checkpoint li donem la posició 
		this.joc=joc;
		this.g=joc.g;
		this.x = x;
		this.y = y;
		llargada = 1000;
		altura = 1000;
		llargadaMinimapa = joc.llargadaCheckpointM; //s'ha de calcular, he posat això per fer proves
		alturaMinimapa = joc.alturaCheckpointM; 
		isVisible = false;
		xInicial = this.x;
		yInicial = this.y;
	}
	void pinta() {
		g.setColor(Color.WHITE);
		g.fillOval(x,y,llargada,altura);
	}
	void moure() {
		x=xInicial-joc.c.xFisiques;
		y=yInicial-joc.c.yFisiques;
	}
}
