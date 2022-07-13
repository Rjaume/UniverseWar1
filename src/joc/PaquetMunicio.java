package joc;
import java.awt.Color;
import java.awt.Graphics;

public class PaquetMunicio extends Objecte{
	int bales; //quantitat de bales que conté el paquet
	boolean agafat; //controla si hem agafat o no el paquet
	static float midaRelativa = (float) (30./1440);
	
	public PaquetMunicio(Joc joc, int x, int y, int bales) {
		this.joc = joc;
		this.g=joc.g;
		this.x = x;
		this.y = y;
		this.bales = bales;
		agafat = false;
		isVisible = false;
		xInicial = this.x;
		yInicial = this.y;
		llargadaMinimapa = joc.midaPaquetMunicioM;
		alturaMinimapa = joc.midaPaquetMunicioM;
		llargada = joc.midaPaquetMunicio;
		altura = joc.midaPaquetMunicio;
	}
	void pinta() {
		g.setColor(Color.GREEN);
		g.fillRect(x,y,llargada,altura);
	}
	void moure() {
		x=xInicial-joc.c.xFisiques;
		y=yInicial-joc.c.yFisiques;
	}
}
