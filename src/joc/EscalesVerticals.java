package joc;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EscalesVerticals extends Objecte implements KeyListener{
	Rectangle hitBox; 
	float llargadaHitBox;
	BufferedImage imatge; //carrego una imatge per cada escala que vull
	boolean fletxaAmunt, fletxaAvall;
	boolean top, bottom, over; //control in which part of the stairs we are
	int numero; //quina de les escales és
	int velocitat = 10;
	EscalesVerticals(Joc joc, int x, int y, float llargadaRelativa, float alturaRelativa, int n){
		this.joc = joc;
		this.g = joc.g;
		this.x = x;
		this.y = y;
		xInicial = x;
		yInicial = y;
		this.llargada = Math.round(llargadaRelativa * joc.f.AMPLADA);
		this.altura = Math.round(alturaRelativa * joc.f.ALTURA);
		llargadaHitBox = ((float)llargada)/4;
		hitBox = new Rectangle(x+Math.round((llargada-llargadaHitBox)/2),y,Math.round(llargadaHitBox),altura);
		numero = n;
		try {
			imatge = joc.resizeImage(joc.imatgesLadder[numero],llargada,altura);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void pinta() {
		g.drawImage(imatge, x, y, null);
//		if(this.altura == 200) {
//			for(int i=1;i<altura/50+1;i++) {
//				System.out.println(i);
//				g.drawImage(imatge,x, y+altura-i*50,null);
//				g.drawRect(x,y+altura-i*50,50,50);
//			}
////			g.drawImage(imatge,x,y,null);
//		}
//		else {
//		g.setColor(Color.WHITE);
//		g.fillRect(x,y,llargada,altura);}
	}
	void moure() {
		x = xInicial-joc.p.xFisiques;
		y = yInicial-joc.p.yFisiques;
//		if(top && joc.p.fletxaAvall) { NO SÉ SI CAL 
//			
//		}
//		if(bottom && joc.p.fletxaAmunt) {
//			
//		}
		if(over) joc.p.Vy=0;
		
		if(over && joc.p.fletxaAmunt) {
			joc.p.yFisiques -= velocitat;
		}
		if(over && joc.p.fletxaAvall && joc.p.y+joc.p.altura+altura>=y && joc.p.y+joc.p.altura<=y+altura-10) { // la última condició sembla redundant perquè ja ho demano a joc però no ho és
			joc.p.yFisiques += velocitat;
		}
		hitBox.setLocation(x+Math.round((llargada-llargadaHitBox)/2),y);
//		topHitBox.setLocation(x,y);
//		bottomHitBox.setLocation(x,y);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
