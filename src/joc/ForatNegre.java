package joc;
import java.awt.image.BufferedImage;

public class ForatNegre extends Enemic{
	BufferedImage foratnegre;
	static float llargadaRelativa = (float)100./1440, alturaRelativa = (float)100./900; //mides relatives a la mida de la pantalla (té molt d'espai sobrant per posar llum per darrera)
	static int maxDistanceOfAtraction=1000; //realment respecte aquesta mida de pantalla la part real del forat negre es 33x34. (*)
	public ForatNegre(Joc joc) {
		super(joc);
		vida=10000;
		M=10000;
		campgravitatori = new CampGravitatori(joc,joc.c,this);
		foratnegre = joc.foratnegre;
		isNegligible = false;
		llargada = joc.llargadaForatNegre;
		altura = joc.alturaForatNegre;
		llargadaMinimapa = joc.llargadaForatNegreM;
		alturaMinimapa = joc.llargadaForatNegreM;
	}
	
	@Override
	void pinta() {
		g.drawImage(foratnegre,x-Math.round((float)(33./100)*llargada),y-Math.round((float)(34./100)*llargada),null); // de manera que x,y correspon a la part esquerre superior del meteorit 
		//(doncs el png del foratnegre té espai buit als voltants (per a tenir llum per darrera)) (*)
		
	}

	@Override
	void moure() { //Els forats negres no es mouen 	
		x=xInicial-joc.c.xFisiques;
		y=yInicial-joc.c.yFisiques;
		if(Math.abs(x-joc.c.x)>maxDistanceOfAtraction || Math.abs(y-joc.c.y)>maxDistanceOfAtraction) { 
			this.isNegligible = true; //si la nau està massa lluny del forat negre aquest deixa de xuclar
		}else {
			this.isNegligible = false;
		}
	}

	@Override
	void dispara() { //Els forats negres no disparen
	}

}
