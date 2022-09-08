package joc;
import java.awt.Color;
import java.awt.Rectangle;

public class Checkpoint extends Objecte{
	static float llargadaRelativa = (float)60./1440, alturaRelativa = (float)60./900;
	boolean reached; //true si hem arribat al checkpoint i false sinó
	public Checkpoint(Joc joc, int x, int y) { //per a crear un checkpoint li donem la posició 
		this.joc=joc;
		this.g=joc.g;
		this.x = x;
		this.y = y;
		llargada = joc.midaCheckpoint;
		altura = llargada;
		llargadaMinimapa = joc.llargadaCheckpointM; //s'ha de calcular, he posat això per fer proves
		alturaMinimapa = joc.alturaCheckpointM; 
		isVisible = false;
		xInicial = this.x;
		yInicial = this.y;
		hitBox = new Rectangle(x,y,llargada,altura);
		reached = false;
	}
	void pinta() {
		g.setColor(Color.WHITE);
		g.fillOval(x,y,llargada,altura);
	}
	void moure() {
		x=xInicial-joc.c.xFisiques;
		y=yInicial-joc.c.yFisiques;
		hitBox.setLocation(x,y);
	}
}
