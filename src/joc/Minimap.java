package joc;
import java.awt.Graphics; 
import java.awt.Color;
/*Representació dels voltant de la nau, el minimapa es un quadrat a baix a la dreta de la pantalla que ens mostra la nau (representada
 * com un quadrat-rectangle (de color verd per exemple) i ens representa els enemics com rectangles (en vermell per exemple). I també
 * més endevant quant tinguem objectes no neccessariament enemics, també els podrem representar d'un altre color*/
public class Minimap {
	Joc joc;
	Graphics g;
	Nau c;
	static int x=1026,y=626,llargada=174,altura=174; //posició del minimapa a la pantalla i mides
	static int xR=x+43, yR=y+58, llargadaR=88, alturaR=58; //posició i mida del rectangle que simula el que veiem a la pantalla dins el minimapa
	static int llargadaNau=4, alturaNau=3; //mida de la nau representada al minimapa (podríem respectar proporcions)
	Color seethrough_gray = new Color((float)0.5,(float)0.5,(float)0.5,(float)0.5);
	public Minimap(Joc joc) {
		this.joc=joc;
		this.c=joc.c;
		this.g=joc.g;
	}
	void pinta() {
		//Layout
		g.setColor(Color.WHITE);
		g.drawRect(x-1,y-1,llargada,altura);
		g.setColor(Color.BLACK);
		g.fillRect(x,y,llargada-1,altura-1);
		g.setColor(seethrough_gray);
		g.drawRect(xR,yR,llargadaR,alturaR);
		//Nau
		g.setColor(Color.GREEN);
		g.fillRect(x+llargada/2-llargadaNau/2,y+altura/2-alturaNau/2,llargadaNau, alturaNau);
		//Enemics
		g.setColor(Color.RED); 
		for (Enemic enemic: joc.enemics) {
			if(enemic.isInMinimap && !enemic.mort) {
				System.out.println("hola");
				int xM = xR+88*enemic.x/1200;
				int yM = yR+58*enemic.y/800;
				g.fillRect(xM,yM,enemic.llargadaMinimapa,enemic.alturaMinimapa);
			}
		}
		/*Com ho fem? Puc posar true a un boolean als enemics que ens indiqui si un element està prou a prop de la nau com per ser 
		 * vist al minimapa i aqui loopejar per tots els enemics dibuixant (amb posició escalada) als enemics amb aquest boolean true*/
		
	}
}
