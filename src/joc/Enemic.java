package joc;

import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Enemic {
	int xInicial,yInicial,x,y,v,xoc,llargada,altura,vida,balesInicials;
	int varX; //moviment de l'enemic cap a la esquerre
	int bodyDamage; //número que es restarà a la vida de la nau cada cop que xoquem amb un enemic (depèn de cada tipus d'enemic)
	boolean calculatXoc; //val true si ja hem tingut en compte el xoc d'aquest enemic amb la nau i false si no (així només restem un cop el damage a la vida de la nau)
	boolean isVisible; //no pintarem els enemics que ja no son visibles a la pantalla
	boolean disparatRecentment; // aixi l'enemic no dispararà massa seguit
	CampGravitatori campgravitatori; //if needed
	int M; //mass of the enemy, for the enemies in which physics apply (rn only blackholes)
	boolean isNegligible; //si l'enemic es pot negligir per a les fisiques, for the enemies in which physics apply (rn only blackholes)
	static int maxGeneracio=3*Finestra.ALTURA/2+1000;
	Joc joc;
	ArrayList<Bala> bales = new ArrayList<Bala>();
	boolean mort;
	public Enemic(Joc joc) {
		this.joc=joc;
		this.calculatXoc = false;
		xInicial=Finestra.AMPLADA+100+joc.c.xFisiques;
		yInicial=Joc.r.nextInt(maxGeneracio*2)-maxGeneracio+joc.c.yFisiques; //genera un numero random de l'interval [-maxGeneracio,maxGeneracio].
		x=xInicial;
		y=yInicial;
		varX=0;
		isVisible=true;
		calculatXoc = false;
	}
	abstract void pinta(Graphics g);
	abstract void dispara();
	abstract void moure();
}
