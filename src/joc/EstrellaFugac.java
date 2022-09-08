package joc;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.AlphaComposite;

public class EstrellaFugac extends Objecte {
	long tempsCreacio;
	double angle;
	int n, v, vx, vy, varX, varY;
	BufferedImage[] imatges = new BufferedImage[21];
	float opacity;
	public EstrellaFugac(Joc joc) {
		this.joc=joc;
		this.g = joc.g;
		this.xInicial=Joc.r.nextInt(3*joc.f.AMPLADA)-joc.f.AMPLADA+joc.c.xFisiques;
		this.yInicial=Joc.r.nextInt(3*joc.f.ALTURA)-joc.f.ALTURA+joc.c.yFisiques;
		x=xInicial;
		y=yInicial;
		angle = 2*Math.PI*Joc.r.nextInt(360)/360;
		llargada = Joc.r.nextInt(100)+50;
		for(int i=0;i<11;i++)
			try {
				joc.imatgesEstrellaf[i]=joc.resizeImage(joc.imatgesEstrellaf[i], llargada, 20);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		for(int i=0;i<11;i++) imatges[i] = Nau.rota(joc.imatgesEstrellaf[i], angle);
		for(int i=1;i<11;i++) imatges[10+i] = imatges[10-i];
		isVisible=true;
		n=0;
		v = llargada/10+Joc.r.nextInt(40)+15;
		vx = Math.round((float)Math.cos(angle) * v);
		vy = Math.round((float)Math.sin(angle) * v);
		opacity = Math.max(0.3f,Math.min(Joc.r.nextFloat(),0.6f));
	}
	void pinta() {
//		Graphics2D g2=(Graphics2D)g;
		joc.g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g.drawImage(imatges[n],x,y,null);
		joc.g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		n+=1;
		n=n%21;
		if(n==0) isVisible = false;
	}
	void moure() {
		if(isVisible) {
			x=xInicial-joc.c.xFisiques+varX;
			y=yInicial-joc.c.yFisiques+varY;
			varX+=vx;
			varY+=vy;
			}
	}
}
