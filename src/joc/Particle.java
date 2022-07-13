package joc;
import java.awt.Color;

public class Particle extends Objecte {
	double v0x;
	double v0y;
	float varX, varY;
	int[] color = new int[3];
	public Particle(Joc joc,int xInicial, int yInicial, double v0x, double v0y, int mida, int[] color) {
		this.joc = joc;
		this.g = joc.g;
		this.xInicial = xInicial;
		this.yInicial = yInicial;
		int rvx = Joc.r.nextInt()%ParticleSystem.velocitat/4; //afegim part aleatòria a la velocitat de cada partícula del sistema de particules
		int rvy = Joc.r.nextInt()%ParticleSystem.velocitat/4;
		this.v0x = v0x+rvx;
		this.v0y = v0y+rvy;
		int rl = Joc.r.nextInt()%mida/2; //afegim part aleatòria a la mida de cada partícula del sistema de particules
		int ra = Joc.r.nextInt()%mida/2;
		this.llargada = mida+rl;
		this.altura = mida+ra;
		int rR = Joc.r.nextInt()%20; //afegim part aleatòria al color de cada partícula del sistema de particules
		int rG = Joc.r.nextInt()%20;
		int rB = Joc.r.nextInt()%20;
		this.color[0] = Math.max(color[0]+rR,0);
		this.color[1] = Math.max(color[1]+rG,0);
		this.color[2] = Math.max(color[2]+rB,0);
		this.color[0] = Math.min(this.color[0],255);
		this.color[1] = Math.min(this.color[1],255);
		this.color[2] = Math.min(this.color[2],255);
		varX = 0;
		varY = 0;
		isVisible = true;
	}
	void moure() {
		if(isVisible) {
			x=Math.round(xInicial-joc.c.xFisiques+varX);
			y=Math.round(yInicial-joc.c.yFisiques+varY);
			varX+=v0x*Joc.dt;
			varY+=v0y*Joc.dt;
			}
	}
	void pinta() {
		if(isVisible) {
			g.setColor(new Color(color[0],color[1],color[2]));
			g.fillRect(x,y,llargada,altura);
		}
	}
}
