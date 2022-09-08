package joc;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener; //listener pels botons del ratolí
import java.awt.event.MouseMotionListener; //listener pel moviment del ratolí

import java.awt.Point;

public class Ascensor extends Objecte implements MouseListener{
	static float llargadaRelativa=404f/1920,alturaRelativa=310f/1080;
	int velocitat =10, varY;
	Tile terra; //el terra de l'ascensor és una tile
	boolean controls; //controla si ensenyem els controls de l'ascensor
	boolean moving;
	int nombrePisos;
	int pisActual;
	int distanciesEntrePisos[];
	Rectangle[] botons = new Rectangle[3];
	boolean[] botoPremut = new boolean[3];
	Ascensor(Joc joc, int x, int y, int nombrePisos, int[] distanciesEntrePisos, int pisInicial){//hauria de demanar el nombre de botons i la posició on t'ha de portar cadascun 
		this.joc = joc;
		joc.f.addMouseListener(this);
		g = joc.g;
		xInicial = x;
		yInicial = y;
		this.x = xInicial;
		this.y = yInicial;
		llargada = joc.llargadaAscensor;
		altura = joc.alturaAscensor;
		terra = new Tile(joc, x, y+altura, llargada, 4, joc.p);
		joc.tilesPersonatge.add(terra); //afegeixo el terra de l'ascensor al vector de tiles de joc
		hitBox = new Rectangle(x+llargada/2,y+altura/2, joc.p.llargada, altura/2); //si xoquem amb aquesta hitBox, preguntem on volem anar amb l'ascensor
		this.nombrePisos = nombrePisos;
		this.distanciesEntrePisos = distanciesEntrePisos;
		botons = new Rectangle[nombrePisos];
		botoPremut = new boolean[nombrePisos];
		for(int i=0;i<nombrePisos;i++) {
			botoPremut[i] = false;
			botons[i] = new Rectangle(x-llargada/4,y+i*altura/4,llargada/4,altura/4);
		}
		varY = 0;
		pisActual = pisInicial;
		botoPremut[pisActual] = true; 
		moving = false;
	}
	void pinta() {
		g.fillRect(x,y,llargada,altura);
		if(controls) {
			for(int i=0;i<nombrePisos;i++) {
				String numero = String.valueOf(i);
				g.setColor(Color.WHITE);
				g.fillRect(x,y+i*altura/4,llargada/4,altura/4);
				g.setColor(Color.BLACK);
				g.drawRect(x,y+i*altura/4,llargada/4,altura/4);
				g.drawString(numero,x,y+i*altura/4);
			}
		}
		/*
		 if(controls) g.drawImage(imatgeControls); imatge amb botons per escollir la planta
		 */
	}
	void moure() {
		for(int i = 0; i<nombrePisos;i++) {
			if(botoPremut[i]) {
				if(pisActual!=i) {
					int distanciaEntrePisos=0;
					for(int j=Math.min(pisActual, i) ; j<Math.max(pisActual, i);j++) { //distància entre el pis que estem i el pis on volem anar
						distanciaEntrePisos+=distanciesEntrePisos[j];
					}
					if(Math.abs(varY)<distanciaEntrePisos) {
						moving = true;
						if(i<pisActual) velocitat=-Math.abs(velocitat);
						varY+=velocitat;
						yInicial-=velocitat; // potser pot ser problematic modificar yInicial
						terra.yInicial-=velocitat;
						joc.p.yFisiques-=velocitat;
						x=xInicial-joc.p.xFisiques;
						y=yInicial-joc.p.yFisiques;
					}
					else {
						moving = false;
						pisActual=i;
					}
				}
				else {
					x = xInicial - joc.p.xFisiques;
					y = yInicial - joc.p.yFisiques;
					varY = 0;
					velocitat = Math.abs(velocitat);
				}
			}
		}
//		terra.moure();//aixi movem el terra i l'ascensor a la vegada
//		terra.pinta();
		hitBox.setLocation(x+llargada/2,y+altura/2);
		for(int i=0;i<nombrePisos;i++) {
			botons[i].setLocation(x,y+i*altura/4);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(!moving) { //si no ens estem movent podem apretar botons 
		for(int i = 0; i<nombrePisos ; i++)if(botons[i].intersects(e.getX(),e.getY(),1,1)) {
			botoPremut[i] = true;
			for(int j=0 ; j<nombrePisos;j++) {
				if(j!=i) botoPremut[j] = false; //al apretar un botó els altres deixen d'estar apretats
			}
		}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
