package Pamiec;

import java.util.Random;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Scanner;
import GlowneKlasy.WarstwaPubliczna;
import PomocniczeKlasy.BufIStat;

public class TablicaStron {
	ObiektTablicy[] tab;
	int ilosc_wpisow;
	int PID;
	public TablicaStron(byte[] fTab, int PID)// throws Exception
	{
		// int PID ustawiane przez liste procesów
		// Proces.getPID();
		// DO TESTU 1
		
		/*byte podziel[] = new byte[24]; // ilosc bajtow wszystkich rozkazow do podzielenia
		int n = 0;
		Random rand = new Random(10);
		for(int i = 0; i < podziel.length; i++)
		{
			if(n == 7)
			{
				podziel[i] = 1;
				n = 0;
			}
			else 
			{
				podziel[i] = (byte)rand.nextInt(50);
				n++;
			}
			
		}*/
		
		// DO TESTU 2
	//	int n = 0;
	//	String s = "ADD R 0 X 1 1";
	//	byte[] bytes = s.getBytes();
		// WLASCIWY
		this.PID = PID; 
		
		
		int n = 0;
		String r = new String(fTab);
        String[] tabP = r.split("\\s+");
        for(String st : tabP)
        {
        	if(st.equals("ADD") || st.equals("SUB") || st.equals("MUL") || st.equals("MOV") || st.equals("JNZ") || st.equals("JZ"))
        		n++;
        }
        byte[] podziel = new byte[(n*8)+3];
        
        n = -1;
        
        for(int i = 0; i < tabP.length; i++)
        if(tabP[i].equals("ADD")|| tabP[i].equals("SUB") || tabP[i].equals("MUL") || tabP[i].equals("MOV"))
        {
        int TT;
        podziel[++n] = (byte)tabP[i].charAt(0);      
        podziel[++n] = (byte)tabP[i].charAt(1); 
        podziel[++n] = (byte)tabP[i].charAt(2); 
        podziel[++n] = (byte)tabP[++i].charAt(0); 
        TT = Integer.parseInt(tabP[++i]);
        podziel[++n] = (byte) TT;
        podziel[++n] = (byte)tabP[++i].charAt(0);
        TT = Integer.parseInt(tabP[++i]);
        podziel[++n] = (byte) TT;
        TT = Integer.parseInt(tabP[++i]);
        podziel[++n] = (byte)TT;
        }
        else if(tabP[i].equals("JNZ"))
        {
        int TT;
        podziel[++n] = (byte)tabP[i].charAt(0);      
        podziel[++n] = (byte)tabP[i].charAt(1); 
        podziel[++n] = (byte)tabP[i].charAt(2); 
        podziel[++n] = (byte)tabP[++i].charAt(0); 
        TT = Integer.parseInt(tabP[++i]);
        podziel[++n] = (byte) TT;
        TT = Integer.parseInt(tabP[++i]);
        podziel[++n] = (byte) TT;
        podziel[++n] = (byte) 0;
        TT = Integer.parseInt(tabP[++i]);
        podziel[++n] = (byte)TT;
        }
        else if(tabP[i].equals("JZ"))
        {
        int TT;
         podziel[++n] = (byte)tabP[i].charAt(0);      
         podziel[++n] = (byte)tabP[i].charAt(1); 
         podziel[++n] = (byte)0; 
         podziel[++n] = (byte)tabP[++i].charAt(0); 
         TT = Integer.parseInt(tabP[++i]);
         podziel[++n] = (byte) TT;
         TT = Integer.parseInt(tabP[++i]);
         podziel[++n] = (byte) TT;
         podziel[++n] = (byte) 0;
         TT = Integer.parseInt(tabP[++i]);
         podziel[++n] = (byte)TT;
        }
        else if(tabP[i].equals("END")){
        	
        	podziel[++n] = (byte) tabP[i].charAt(0);
        	podziel[++n] = (byte) tabP[i].charAt(1);
        	podziel[++n] = (byte) tabP[i].charAt(2);
        }
		
		if(podziel.length % 8 == 0)
			ilosc_wpisow = podziel.length/8 + 1; // przyklad: 64 bajty = 8 ramek + 1 na wynik
		else 
			ilosc_wpisow = podziel.length/8 + 2; // przyklad: 67 bajtow = 8 ramek na 64 bajty + 1 na pozostale 3 + 1 wynik
		
		tab = new ObiektTablicy[ilosc_wpisow];
		for(int i = 0; i < ilosc_wpisow; i++)
			tab[i] = new ObiektTablicy();
		
		int counter = 0; // do zliczania skopiowanych bajtow z pliku
		for(ObiektTablicy x : tab)
		{
			byte isV = 0; // czy strona ma byc od poczatku w pamieci, decyduje o tym ostatni bajt na stronie
			for(int i = 0; counter < podziel.length; i++)
			{
			x.stronica[i] = podziel[counter]; //bajt do stronicy = bajt z pliku
			counter++;
			
			if(i == 7) // siodmy bajt decyduje o tym czy strona ma byc od poczatku w pamieci.
				{	
					isV = x.stronica[i];
					break;
				}
			} 
			
			if(isV == 1)
				x.setValid(true);
			else 
				x.setValid(false);
			
			x.setDirty((byte)0);
		} 
	}
	
	public void zawartosc()
	 {
		System.out.println("Tablica procesu " + PID);
		for(int i = 0; i < ilosc_wpisow; i++)
		{
			System.out.printf("Numer stronicy: %d  Dane:", i);
			for(int j = 0; j < 8; j++)
				System.out.printf(" %d ", tab[i].stronica[j]);
			System.out.printf("Numer ramki: %d Bit valid: %b Bit dirty: %d", tab[i].nr_ramki, tab[i].valid_invalid, tab[i].dirty);
			System.out.println();
		}
		 //wypisuje zawartosc tablicy stronnic
	 }

	public int nrRamki(int nr_strony) throws ArrayIndexOutOfBoundsException{
		
		return tab[nr_strony].nr_ramki;
	}
	public int getPID()
	{
		return PID;
	}
	public void alokujProces()
	{
		VM.pamiecOper.wstaw_proces(this);
		Memory.listaT.add(this);
		System.out.println("Proces zaalokowany w pamieci");
	}
	public void zapiszWynik(int wynik) //zapisanie wyniku do pamieci
	{
		byte[] data = new byte[8];
		data[3] = (byte) (wynik & 0xFF);
		data[2] = (byte) ((wynik >>> 8) & 0xFF);
		data[1] = (byte) ((wynik >>> 16) & 0xFF);
		data[0] = (byte) ((wynik >>> 24) & 0xFF); //BIG ENDIAN, do data[0] kopiujemy najstarsze bajty
		
		tab[(ilosc_wpisow)-1].stronica = data.clone();
		tab[(ilosc_wpisow)-1].valid_invalid = true;
		tab[(ilosc_wpisow)-1].dirty = 0;
		
		int ramka = VM.pamiecOper.zapisz_do_ram(data); // wyslanie i zapisanie strony w ramce, zwraca nam nr ramki
		tab[(ilosc_wpisow)-1].nr_ramki = ramka;
		
		VM.pamiecOper.pamiec[ramka].PID = PID; // wpisanie PID w numerze ramki
		
	}
	public int SprawdzWynik(){
		if(tab[(ilosc_wpisow)-1].getValid()){ // jesli wynik jest w pamieci
		byte[] temp = VM.pamiecOper.dane_ramki(tab[(ilosc_wpisow)-1].getNrRamki());
	
		ByteBuffer wrapped = ByteBuffer.wrap(temp); // big-endian by default
		int rozkaz = wrapped.getInt();
		System.out.println("\nWynik programu to: " + rozkaz);
		
		return rozkaz;
		}
		else
		{
			System.out.println("Wynik nie zostal jeszcze obliczony przez procesor");
			
			return 0;
			
		}
		
	}
	public void wypisz_z_pamieci(){
		for(ObiektTablicy x : tab)
		{
			if(x.getValid()){ // jesli jakies dane sa w pamieci operacyjnej to trzeba je usunac
			Memory.myQueue.remove(x.getNrRamki()); // musimy usunac z kolejki ramki, ktore byly uzywane przez ten proces
			Memory.zwolnij_ramke(x.getNrRamki()); 
			Memory.listaT.remove(this);
			}
			// wprowadzanie do stanu poczatkowego procesu.
			if(x.stronica[7] == 1) // ostatni bajt decyduje czy strona ma sie znajdowac w pamiec
			x.valid_invalid = true;
			else
				x.valid_invalid = false;
			
			x.setNrRamki(-1);
			VM.plikWymiany.zwolnijMiejsce(PID); // usuwanie danych z pliku wymiany
		}
		Debug.println("Proces o PID " + PID + " zostal prawidlowo usuniety z pamieci");
		System.out.println("Proces o PID " + PID + " zostal prawidlowo usuniety z pamieci");
	}
public static void main(String args[]) {
	// TESTOWANIE!
//	TablicaStron t1 = new TablicaStron("ta", 3);
	//TablicaStron t2 = new TablicaStron("ac", 4);
	//TablicaStron t3 = new TablicaStron("aac", 5);
	//t.alokujProces();
	//t.zawartosc();
	//VM.pamiecOper.wyswietl_ram();
	//t.zawartosc();
	//t.zawartosc();
	//VM.pamiecOper.wyswietl_ram();
	//VM.plikWymiany.Wyswietl();
	/*t.alokujProces();
	t1.alokujProces();
	t1.zawartosc();
	t2.alokujProces();
	t2.zawartosc();
	t.zawartosc();
	VM.pamiecOper.FIFO_show();
	VM.plikWymiany.Wyswietl();
	VM.pamiecOper.wyswietl_ram();
	t3.alokujProces();
	t3.zawartosc();
	t1.zawartosc();
	VM.pamiecOper.FIFO_show();
	VM.plikWymiany.Wyswietl();
	VM.pamiecOper.wyswietl_ram();
	t.zawartosc();
	t1.zawartosc();
	try{
	VM.pamiecOper.FIFO_show();
	byte[] tabbb = VM.pamiecOper.czytaj_proces(t, 2);
	}catch(Exception E){}
	t.zawartosc();
	VM.pamiecOper.wyswietl_ram();
	VM.plikWymiany.Wyswietl();
	t.wypisz_z_pamieci();
	t2.wypisz_z_pamieci();
	t1.wypisz_z_pamieci();
	VM.pamiecOper.wyswietl_ram();
	byte[] tabka = VM.pamiecOper.dane_ramki(3);
	for(int i = 0; i < tabka.length; i++)
	System.out.println(tabka[i]);
	//VM.pamiecOper.wyswietl_ram();
	//VM.plikWymiany.Wyswietl();
	//VM.pamiecOper.FIFO_show();
/*	VM.plikWymiany.Wyswietl();
	t.zawartosc();
	Memory.listaT.add(t);
	VM.pamiecOper.FIFO_show();
	try{
	//byte[] a = VM.pamiecOper.czytaj_proces(t, 3);
	}catch(Exception e){Debug.println(e.getMessage());}
	
	//t.zawartosc(); // poprawic
	//VM.pamiecOper.pamiec[3].CzytajRamke();
	//t.alokujProces();
	//t.zapiszWynik(mem, 144);
	//mem.wyswietl_ram();
	//t.zawartosc();
*/
}
}