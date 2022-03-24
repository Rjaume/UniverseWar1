package joc;

public class CampGravitatori {
	Joc joc;
	static double C=1; //constant that depends on gravity (in theory it should be 6.67*10^(-11) but setting it lower allows us to have a lower mass for the black hole
	double K; //will be the top part of our force equation
	Nau c;
	Enemic e;
	double distq; //distance between c and enemic squared
	CampGravitatori(Joc joc, Nau nau, Enemic enemic){ //sets a new gravitational field between nau and enemic
		this.joc = joc;
		this.c = nau;
		this.e = enemic;
		this.K = C*Nau.M*enemic.M;
	}
	//we could also have a constructor to set gravitational fields between other types of objects (p.e: enemics and enemics)
	//fem els calculs prenent els punts centrals de cada objecte 
	double Fx() {
		double xCEnemy = e.x+e.llargada/2; //punts centrals de cada objecte
		double yCEnemy = e.y+e.altura/2;
		double xCNau = Nau.x+Nau.llargada/2;
		double yCNau = Nau.y+Nau.altura/2;
		distq=Math.pow(xCEnemy-xCNau, 2)+Math.pow(yCEnemy-yCNau,2);
		return(K*(xCEnemy-xCNau)/Math.pow(distq,3/2));
	}
	double Fy() {
		double xCEnemy = e.x+e.llargada/2; //punts centrals de cada objecte
		double yCEnemy = e.y+e.altura/2;
		double xCNau = Nau.x+Nau.llargada/2;
		double yCNau = Nau.y+Nau.altura/2;
		distq=Math.pow(xCEnemy-xCNau, 2)+Math.pow(yCEnemy-yCNau,2);
		return(K*(yCEnemy-yCNau)/Math.pow(distq,3/2));
	}
}
