package joc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class ContadorTemps {
	long tempsInicial; //en milisegons
	long tempsMort; //en milisegons
	String tempsContat; //en segons 
	Joc joc;
	Graphics g;
	ArrayList<Integer> llistaRecords =new ArrayList<Integer>();
	String text1=new String("You managed to stay alive for ");
	String text2=new String(" seconds ");
	ContadorTemps(Joc joc, Graphics g){
		tempsInicial=System.currentTimeMillis();
		this.joc=joc;
		this.g=g;
	}
	void pinta() { //pinta el temps que hem durat per pantalla després de cada mort
			g.setFont(new Font("MyriadPro", Font.PLAIN, 20)); 
			g.setColor(Color.BLACK);
			g.drawString(text1+tempsContat+text2, 405, 555);
	}
	void pintaRecords() { //pinta els records al menú de records
		for(int i=Math.min(llistaRecords.size(),5)-1;i>-1;i--) {
			g.setFont(new Font("MyriadPro",Font.PLAIN,20));
			g.setColor(Color.BLACK);
			g.drawString(Integer.toString(llistaRecords.get(i))+text2,550,202+i*100);
		}
	}
	void apuntaTemps() { //apunta al fitxer ordenadament si el temps que hem obtingut és millor que el pitjor registrat al fitxer
		if(joc.c.mort && !joc.apuntatTemps) {
			tempsMort=System.currentTimeMillis();
			tempsContat=Integer.toString((int)(tempsMort-tempsInicial)/1000);
			joc.apuntatTemps=true;
			
			try { 
			Scanner RecordsScanner = new Scanner(joc.fitxerRecords);
			ArrayList<Integer> recordsApuntats = new ArrayList<Integer>();
			while(RecordsScanner.hasNextInt()) {
				recordsApuntats.add(Integer.parseInt(RecordsScanner.nextLine()));
				}
			RecordsScanner.close();
			int apuntats=recordsApuntats.size();
			String aux="";
			if(apuntats<5) {
				recordsApuntats.add(Integer.parseInt(tempsContat));
				recordsApuntats.sort(Comparator.reverseOrder());
				FileWriter RecordsWriter = new FileWriter(joc.fitxerRecords);
				for(int i=0;i<recordsApuntats.size();i++) {
					aux+=recordsApuntats.get(i)+"\n";
				}
				RecordsWriter.write(aux);
				RecordsWriter.close();
			}
			if(apuntats==5) {  
				if(recordsApuntats.get(4)<Integer.parseInt(tempsContat)) { //si el més petit que tenim apuntat és més gran
					// o igual que el temps contat no fem res
					recordsApuntats.set(4,Integer.parseInt(tempsContat));
					recordsApuntats.sort(Comparator.reverseOrder());
					FileWriter RecordsWriter = new FileWriter(joc.fitxerRecords);
					for(int i=0;i<5;i++) {
						aux+=recordsApuntats.get(i)+"\n";
					}
					RecordsWriter.write(aux);
					RecordsWriter.close();
				}
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	void llegeixTemps() { //llegeix els temps del fitxer.
		if((!joc.calculatRecords && joc.c.mort) || (joc.records && !joc.calculatRecords) ) {
			try {
				Scanner RecordsScanner = new Scanner(joc.fitxerRecords);
				llistaRecords.clear();
				while(RecordsScanner.hasNextLine()) {
				llistaRecords.add(Integer.parseInt(RecordsScanner.nextLine()));
				}
				RecordsScanner.close();
				joc.calculatRecords=true;
			} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		}
	}
	
}
