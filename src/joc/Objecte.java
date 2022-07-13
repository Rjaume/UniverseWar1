package joc;
import java.awt.Graphics;

public class Objecte {
int x,y;
Joc joc;
Graphics g;
int xInicial, yInicial;
int xCentre, yCentre;
int llargada,altura; 
int llargadaMinimapa,alturaMinimapa;
int midaParticules;
boolean isVisible; //no pintarem els objectes que no son visibles a la pantalla
boolean isInMinimap; //true if the object is in range of the minimap 
int color[] = new int[3]; //colors, RGB
int nombreParticules; //nombre de particules en què explota l'objecte al morir-se
boolean hasParticles; 
}
