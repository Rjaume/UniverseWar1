package joc;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ForatNegre extends Enemic{
	BufferedImage foratnegre;
	static int maxDistanceOfAtraction=1000;
	public ForatNegre(Joc joc) {
		super(joc);
		vida=10000;
		M=10000;
		campgravitatori = new CampGravitatori(joc,joc.c,this);
		foratnegre = joc.foratnegre;
		isNegligible = false;
		llargada = 33;
		altura = 33;
		llargadaMinimapa = 2;
		alturaMinimapa = 2;
	}
	
	@Override
	void pinta(Graphics g) {
		g.drawImage(foratnegre,x-35,y-34,null); // de manera que x,y correspon a la part esquerre superior del meteorit 
		//(doncs el png del meteorit té espai buit als voltants (per a tenir llum per darrera))
		
	}

	@Override
	void moure() { //Els forats negres no es mouen 	
		x=xInicial-joc.c.xFisiques;
		y=yInicial-joc.c.yFisiques;
		if(Math.abs(x-Nau.x)>maxDistanceOfAtraction || Math.abs(y-Nau.y)>maxDistanceOfAtraction) { 
			this.isNegligible = true; //si la nau està massa lluny del forat negre aquest deixa de xuclar
		}else {
			this.isNegligible = false;
		}
	}

	@Override
	void dispara() { //Els forats negres no disparen
	}

}
