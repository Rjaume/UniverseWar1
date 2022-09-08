package joc;

import java.awt.Color;
import java.awt.Rectangle;
public class Escales extends Objecte{
	Rectangle[] hitBox;
	int nombreEscalons, alturaEscalons, llargadaEscalons;
	float pendent;
	public Escales(Joc joc, int x, int y, int llargada, int altura, int nombreEscalons) {
		this.joc = joc;
		this.g = joc.g;
		this.xInicial = x;
		this.yInicial = y;
		this.x = x;
		this.y = y;
		this.llargada = llargada;
		this.altura = altura;
		this.nombreEscalons = nombreEscalons;
		llargadaEscalons = llargada/nombreEscalons;
		alturaEscalons = altura/nombreEscalons;
		hitBox = new Rectangle[nombreEscalons]; //com se que al referir-me a escales.hitBox no em prendra la de Objecte? Potser no hauria de posar extends Objecte
		for(int i = 0 ; i<nombreEscalons ; i++) {
			hitBox[i] = new Rectangle(x+i*llargadaEscalons,y-(i+1)*alturaEscalons,llargada-i*llargadaEscalons,alturaEscalons);
		}
	}
	void pinta() {
		for(int i=0;i<nombreEscalons;i++) {
			g.setColor(Color.GREEN);
			g.fillRect(x+llargadaEscalons*(i),y-alturaEscalons*(i+1),llargada-i*llargadaEscalons,alturaEscalons);
			g.setColor(Color.YELLOW);
			g.drawRect(x+llargadaEscalons*i-2*llargadaEscalons,y-alturaEscalons*(i+1),llargada-llargadaEscalons*i,alturaEscalons);
		}
//		g.setColor(Color.BLACK);
//		for(int i=0;i<nombreEscalons;i++) g.fillRect(x+llargadaEscalons*i,y-alturaEscalons*(i+1),llargada-llargadaEscalons*i,alturaEscalons);
	}
	void moure() {
		x = xInicial - joc.p.xFisiques;
		y = yInicial - joc.p.yFisiques;
		for(int i=0;i<nombreEscalons;i++) hitBox[i].setLocation(x+llargadaEscalons*i,y-alturaEscalons*(i+1));
	}
}
