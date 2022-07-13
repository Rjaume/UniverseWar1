package joc;

import java.util.ArrayList;

public abstract class Enemic extends Objecte {
	int v,xoc,vida,balesInicials;
	int varX,varY; //moviment de l'enemic cap a la esquerre
	int bodyDamage; //número que es restarà a la vida de la nau cada cop que xoquem amb un enemic (depèn de cada tipus d'enemic)
	boolean calculatXoc; //val true si ja hem tingut en compte el xoc d'aquest enemic amb la nau i false si no (així només restem un cop el damage a la vida de la nau)
	CampGravitatori campgravitatori; //camp gravitatori associat a l'enemic (només per forats negres ara mateix)
	int M; //mass of the enemy, for the enemies in which physics apply (right now only blackholes)
	boolean isNegligible; //si l'enemic es pot negligir per a les fisiques, for the enemies in which physics apply (right now only blackholes)
	int maxGeneracio;
	Joc joc;
	ArrayList<Bala> bales = new ArrayList<Bala>();
	boolean mort;
	ParticleSystem particules;
	public Enemic(Joc joc) {
		this.joc=joc;
		this.g=joc.g;
		this.calculatXoc = false;
		maxGeneracio = 3*joc.f.ALTURA/2+1000;
		xInicial=2*joc.f.AMPLADA+joc.c.xFisiques;
		yInicial=Joc.r.nextInt(maxGeneracio*2)-maxGeneracio+joc.c.yFisiques; //genera un numero random de l'interval [-maxGeneracio,maxGeneracio].
		x=xInicial;
		y=yInicial;
		varX=0;
		varX=0;
		isVisible=false;
		calculatXoc = false;
		hasParticles = false;
	}
	public Enemic(Joc joc,boolean a) { //constructor per objectes rotats
		this.joc=joc;
		this.g=joc.g;
		this.calculatXoc = false;
		maxGeneracio = 3*joc.f.AMPLADA/2+1000;
		float angleGeneracio; //NO M'AGRADA
		if(Joc.r.nextInt()%100>95) { //5 per cent de possibilitats que el meteorit vingui de qualsevol direcció i senttit
		angleGeneracio= (float) (Joc.r.nextInt()%360*(2*Math.PI/360)); //angle que ens determina on es genera el nostre enemic
		}else { //la majoria de meteorits venen de davant 
			angleGeneracio = (float) (Joc.r.nextInt()%16*(2*Math.PI/360));
			}
		xInicial = joc.c.xFisiques + Math.round((float)(maxGeneracio*Math.cos(angleGeneracio)));
		yInicial = joc.c.yFisiques -  Math.round((float)(maxGeneracio*Math.sin(angleGeneracio)));
		x=xInicial;
		y=yInicial;
		varX=0;
		varX=0;
		isVisible=false;
		calculatXoc = false;
		hasParticles = false;
	}
	public Enemic(Joc joc, int x, int y) { //constructor per a situar enemics a una posició concreta
		this.joc=joc;
		this.g=joc.g;
		this.x=x;
		this.y=y;
		xInicial = x;
		yInicial = y;
		hasParticles = false;
	}
	abstract void pinta();
	abstract void dispara();
	abstract void moure();
}
