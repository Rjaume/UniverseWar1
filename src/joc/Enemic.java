package joc;

import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Enemic extends Objecte {
	int xInicial,yInicial,v,xoc,vida,balesInicials;
	int varX; //moviment de l'enemic cap a la esquerre
	int bodyDamage; //número que es restarà a la vida de la nau cada cop que xoquem amb un enemic (depèn de cada tipus d'enemic)
	boolean calculatXoc; //val true si ja hem tingut en compte el xoc d'aquest enemic amb la nau i false si no (així només restem un cop el damage a la vida de la nau)
	boolean isVisible; //no pintarem els enemics que ja no son visibles a la pantalla
	boolean disparatRecentment; // aixi l'enemic no dispararà massa seguit
	CampGravitatori campgravitatori; //camp gravitatori associat a l'enemic (només per forats negres ara mateix)
	int M; //mass of the enemy, for the enemies in which physics apply (right now only blackholes)
	boolean isNegligible; //si l'enemic es pot negligir per a les fisiques, for the enemies in which physics apply (right now only blackholes)
	boolean isInMinimap; //true if the enemy is in range of the minimap 
	int maxGeneracio;
	Joc joc;
	ArrayList<Bala> bales = new ArrayList<Bala>();
	boolean mort;
	public Enemic(Joc joc) {
		this.joc=joc;
		this.calculatXoc = false;
		maxGeneracio = 3*joc.f.ALTURA/2+1000;
		xInicial=2*joc.f.AMPLADA+joc.c.xFisiques;
		yInicial=Joc.r.nextInt(maxGeneracio*2)-maxGeneracio+joc.c.yFisiques; //genera un numero random de l'interval [-maxGeneracio,maxGeneracio].
		x=xInicial;
		y=yInicial;
		varX=0;
		isVisible=false;
		calculatXoc = false;
	}
	abstract void pinta(Graphics g);
	abstract void dispara();
	abstract void moure();
}
