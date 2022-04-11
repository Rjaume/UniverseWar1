package joc;
import java.awt.Graphics; 
import java.awt.Color;
public class Minimap {
	Joc joc;
	Graphics g;
	Nau c;
	static float llargadaRelativa = (float)174./1440, alturaRelativa = (float)174./900; //mides relatives a la mida de la pantalla
	static int x,y,llargada,altura; //posició del minimapa a la pantalla i mides
	static int xR,yR,llargadaRectangle, alturaRectangle; //posició i mida del rectangle que simula el que veiem a la pantalla dins el minimapa
	static int llargadaNau=4, alturaNau=3; //mida de la nau representada al minimapa
	static Color seethrough_gray = new Color((float)0.5,(float)0.5,(float)0.5,(float)0.5);
	public Minimap(Joc joc) {
		this.joc=joc;
		c=joc.c;
		g=joc.g;
		llargada = joc.llargadaMinimapa;
		altura = joc.alturaMinimapa;
		x=joc.f.AMPLADA-llargada;
		y=joc.f.ALTURA-altura;
		llargadaRectangle = Math.round(llargada/2);
		alturaRectangle = Math.round(altura/3);
		xR=x+Math.round(llargadaRectangle/2);
		yR=y+alturaRectangle;
	}
	void pinta() {
		//Layout
		g.setColor(Color.BLACK);
		g.fillRect(x,y,llargada-1,altura-1);
		g.setColor(seethrough_gray);
		g.drawRect(xR,yR,llargadaRectangle,alturaRectangle);
		//Nau
		g.setColor(Color.GREEN);
		g.fillRect(x+llargada/2-llargadaNau/2,y+altura/2-alturaNau/2,llargadaNau, alturaNau);
		//Enemics
		g.setColor(Color.RED); 
		for (Enemic enemic: joc.enemics) {
			if(enemic.isInMinimap && !enemic.mort) {
				int xM = xR+llargadaRectangle*enemic.x/joc.f.AMPLADA;
				int yM = yR+alturaRectangle*enemic.y/joc.f.ALTURA;
				g.fillRect(xM,yM,enemic.llargadaMinimapa,enemic.alturaMinimapa);
			}
		}
		//marc 
		g.setColor(Color.WHITE);
		g.drawRect(x-1,y-1,llargada,altura);
	}
}
