package joc;

public class ParticleSystem extends Objecte {
	int nombreParticules;
	static int velocitat = 150;
	double alpha;
	Particle particles[];
	public ParticleSystem(Joc joc, Objecte objecte) {
		this.joc = joc;
		this.g = joc.g;
		this.xInicial = objecte.x+joc.c.xFisiques; //prendre objecte.xInicial i yInicial no funciona pels metoerits
		this.yInicial = objecte.y+joc.c.yFisiques;
		llargada = objecte.midaParticules;
		nombreParticules = objecte.nombreParticules;
		alpha = 2*Math.PI/nombreParticules;
		particles = new Particle[nombreParticules];
		for(int i=0;i<nombreParticules;i++) {
			particles[i] = new Particle(joc, xInicial, yInicial, velocitat * Math.cos(alpha*i), velocitat * Math.sin(alpha*i), llargada, objecte.color);
		}
	}
	void moure() {
		for(int i=0;i<nombreParticules; i++) {
			particles[i].moure();
			if(Math.abs(particles[i].x-joc.c.x)>joc.f.AMPLADA*2 || Math.abs(particles[i].y-joc.c.y)>joc.f.ALTURA*2) {
				particles[i].isVisible = false;
			}
		}
	
	}
	void pinta() {
		for(int i=0;i<nombreParticules; i++) {
			if(particles[i].isVisible) particles[i].pinta();
		}
	}
}
