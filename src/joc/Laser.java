package joc;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.geom.AffineTransform;

public class Laser {
	static float llargadaRelativa = 1, alturaRelativa = (float) (20./900);
	int llargada, altura, diagonal, diagonalImatge;
	static int damage = 20;
	double angleDispar;
	Polygon hitBox;
	int x, y, xInicial, yInicial, xPinta, yPinta, dfx, dfy;
	BufferedImage imatgeLaser, imatgeCarregant;
	double tempsCarrega,tempsUltimDispar, tempsDisparant; // temps que el laser està carregant, temps últim dispar i temps que el laser s'està disparant
	Joc joc;
	Graphics g;
	boolean on; //si val true el laser fa mal
	NauEnemiga3 nau;
	public Laser(Joc joc, NauEnemiga3 nau, Double tempsCarrega, double tempsDisparant, double multiplicadorAltura) {
		this.joc = joc;
		this.g = joc.g;
		this.nau = nau;
		llargada = joc.llargadaLaser;
		altura = Math.round((float)(joc.alturaLaser * multiplicadorAltura));
		angleDispar = nau.angleNau;
		diagonalImatge = (int)Math.round(Math.sqrt(llargada*llargada+altura*altura*3.24));
		dfx = Math.round((float)(diagonalImatge/2-diagonalImatge/2*Math.cos(angleDispar))); //diferència entre les posicions de la imatge del làser i el laser en si
		dfy = Math.round((float)(diagonalImatge/2-diagonalImatge/2*Math.sin(angleDispar)));
//		dfx = 0;
//		dfy = 0;
//		xInicial=nau.x+joc.c.xFisiques; //s'ha d'ajustar
//		yInicial=nau.y+joc.c.yFisiques;
		xInicial = nau.xCentre;
		yInicial = nau.yCentre;
		x = xInicial;
		y = yInicial;
		xPinta = x-dfx;
		yPinta = y-dfy;
//		xPinta = xInicial - joc.c.xFisiques - dfx;
//		yPinta = yInicial - joc.c.yFisiques - dfy;
//		imatgeLaser = joc.laser;
//		imatgeCarregant = joc.laserCarregant;
		try {
			imatgeLaser = joc.resizeImage(joc.laser, llargada, altura);
			imatgeCarregant = joc.resizeImage(joc.laserCarregant, llargada, altura);
		} catch (IOException e) {
			e.printStackTrace();
		}
		imatgeLaser = Nau.rota(imatgeLaser, angleDispar);
		imatgeCarregant = Nau.rota(imatgeCarregant, angleDispar);
		this.tempsCarrega = tempsCarrega*1000;
		this.tempsDisparant = tempsDisparant*1000;
		tempsUltimDispar = System.currentTimeMillis();
		on = false;
		diagonal = (int)Math.round(Math.sqrt(llargada*llargada+altura*altura));
		int[] xpoints = {x, (int)Math.round(x+llargada*Math.cos(angleDispar)), (int) Math.round(x+diagonal*Math.cos(Math.atan(((float)altura)/llargada)+angleDispar)), (int)Math.round(x+altura*Math.cos(Math.PI/2+angleDispar))};
		int[] ypoints = {y, (int)Math.round(y+llargada*Math.sin(angleDispar)), (int) Math.round(y+diagonal*Math.sin(Math.atan(((float)altura)/llargada)+angleDispar)), (int)Math.round(y+altura*Math.sin(Math.PI/2+angleDispar))};
		hitBox = new Polygon(xpoints, ypoints, 4);
		//hitbox = 
	}
	void pinta() {
		if(!nau.mort) {
//		g.fillRect(xPinta,yPinta,100,100);
		if(System.currentTimeMillis()-tempsUltimDispar < tempsCarrega) {
//			BufferedImage imatgeCarregantRotada = Nau.rota(imatgeCarregant, nau.angleNau);
//			imatgeCarregant = Nau.rota(imatgeCarregant, nau.angleNau);
//			int dfx = Math.round((float)(diagonal/2-diagonal/2*Math.cos(nau.angleNau))); //diferència entre les posicions de la imatge de la bala i la bala en si
//			int dfy = Math.round((float)(diagonal/2-diagonal/2*Math.sin(nau.angleNau)));
//			xPinta = x - dfx;
//			yPinta = y - dfy;
			g.drawImage(imatgeCarregant,xPinta,yPinta,null);
			on=false;
		}
		else if(System.currentTimeMillis()-tempsUltimDispar < tempsCarrega + tempsDisparant) {
			g.drawImage(imatgeLaser,xPinta,yPinta,null);
			if(!on) on = true;
		}
		if(System.currentTimeMillis()-tempsUltimDispar > tempsCarrega + tempsDisparant) on = false;
		}
//		int[] xpoints = {x, (int)Math.round(x+llargada*Math.cos(angleDispar)), (int) Math.round(x+diagonal*Math.cos(Math.atan(((float)altura)/llargada)+angleDispar)), (int)Math.round(x+altura*Math.cos(Math.PI/2+angleDispar))};
//		int[] ypoints = {y, (int)Math.round(y+llargada*Math.sin(angleDispar)), (int) Math.round(y+diagonal*Math.sin(Math.atan(((float)altura)/llargada)+angleDispar)), (int)Math.round(y+altura*Math.sin(Math.PI/2+angleDispar))};
//		int[] xpoints = {x-dfx, (int)Math.round(x+llargada*Math.cos(angleDispar))-dfx, (int) Math.round(x+diagonalImatge*Math.cos(Math.atan(((float)1.8*altura)/llargada)+angleDispar))-dfx, (int)Math.round(x+altura*1.8*Math.cos(Math.PI/2+angleDispar))-dfx};
//		int[] ypoints = {y-dfy, (int)Math.round(y+llargada*Math.sin(angleDispar))-dfy, (int) Math.round(y+diagonalImatge*Math.sin(Math.atan(((float)1.8*altura)/llargada)+angleDispar))-dfy, (int)Math.round(y+altura*1.8*Math.sin(Math.PI/2+angleDispar))-dfy};
//		int[] xpoints = {(int)Math.round(x+0.5*altura*Math.sin(angleDispar)), (int)Math.round(x+0.5*altura*Math.sin(angleDispar)+llargada*Math.cos(angleDispar)), (int) Math.round(x+0.5*altura*Math.sin(angleDispar)+diagonal*Math.cos(Math.atan(((float)altura)/llargada)+angleDispar)), (int)Math.round(x+0.5*altura*Math.sin(angleDispar)+altura*Math.cos(Math.PI/2+angleDispar))};
//		int[] ypoints = {(int)Math.round(y-0.5*altura*Math.cos(angleDispar)), (int)Math.round(y-0.5*altura*Math.cos(angleDispar)+llargada*Math.sin(angleDispar)), (int) Math.round(y-0.5*altura*Math.cos(angleDispar)+diagonal*Math.sin(Math.atan(((float)altura)/llargada)+angleDispar)), (int)Math.round(y-0.5*altura*Math.cos(angleDispar)+altura*Math.sin(Math.PI/2+angleDispar))};
//		g.drawPolygon(xpoints,ypoints,4);
	}
	void moure() {
//		x = xInicial - joc.c.xFisiques;
//		y = yInicial - joc.c.yFisiques;
//		x = nau.x;
//		y = nau.y;
		x = nau.xCentre;
		y = nau.yCentre;
		xPinta = x - dfx;
		yPinta = y - dfy;
		int[] xpoints = {(int)Math.round(x+0.5*altura*Math.sin(angleDispar)), (int)Math.round(x+0.5*altura*Math.sin(angleDispar)+llargada*Math.cos(angleDispar)), (int) Math.round(x+0.5*altura*Math.sin(angleDispar)+diagonal*Math.cos(Math.atan(((float)altura)/llargada)+angleDispar)), (int)Math.round(x+0.5*altura*Math.sin(angleDispar)+altura*Math.cos(Math.PI/2+angleDispar))};
		int[] ypoints = {(int)Math.round(y-0.5*altura*Math.cos(angleDispar)), (int)Math.round(y-0.5*altura*Math.cos(angleDispar)+llargada*Math.sin(angleDispar)), (int) Math.round(y-0.5*altura*Math.cos(angleDispar)+diagonal*Math.sin(Math.atan(((float)altura)/llargada)+angleDispar)), (int)Math.round(y-0.5*altura*Math.cos(angleDispar)+altura*Math.sin(Math.PI/2+angleDispar))};
		hitBox = new Polygon(xpoints, ypoints, 4);
		//moure hitbox
	}
}
