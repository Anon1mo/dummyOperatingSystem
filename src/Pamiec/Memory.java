package Pamiec;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

import GUI.frame;
import GlowneKlasy.*;

public class Memory {
	int ilosc_ramek;
	static Ramka[] pamiec;
	static byte[] tab_wolnych_ramek;
	static Queue<Integer> myQueue;
	public static List<TablicaStron> listaT = new ArrayList<TablicaStron>(); // TYMCZASOWO, pozniej w tym miejscu bedzie LISTA PROCESOW(!!! - wiele tablic stron.
	
public Memory(int ilosc_ramek)
{
	this.ilosc_ramek = ilosc_ramek;
	
	pamiec = new Ramka[ilosc_ramek];
	for(int i = 0; i < ilosc_ramek; i++)
		pamiec[i] = new Ramka();
	
	tab_wolnych_ramek = new byte[ilosc_ramek];
	myQueue = new LinkedList<Integer>();
/*	try{
	tst = new TablicaStron("abc.txt"); // wyjatek, bo otwiera plik z programem, pozniej to bedzie w liscie procesow
	}catch(Exception E)
	{Debug.println(E.getMessage());} */
	
	for(int i = 0; i < tab_wolnych_ramek.length; i++)
		tab_wolnych_ramek[i] = 1;
		
}
public int zapisz_do_ram(byte[] strona) // zwraca numer ramki 
{
	//PlikWymiany.miejsce.przestrzen_dyskowa[i].stronica = pamiec[i].frame.clone();
	int i = ZnajdzWolnaRamke();
	pamiec[i].frame = strona.clone();
	FIFO_in(i);
	
	return i;
	
} 
public byte[] dane_ramki(int nr_ramki)
{
	return pamiec[nr_ramki].frame;
}

public void wyswietl_ram(){
	System.out.println("Status pamieci: ");
	for(int i = 0; i < ilosc_ramek; i++)
	{
		System.out.println("Ramka nr " + i + " ");
	if(CzyWolna(i))
		System.out.println("wolna");
	else{
		System.out.println("zajeta przez proces " + pamiec[i].getPID());
		System.out.println("Dane w ramce: ");
		pamiec[i].CzytajRamke();	
		}
	
	}
	
}
/* 
 * GLOWNA FUNKCJA DLA MODULU 1 - czytaj_proces, zamiast TablicStron podanej w projekcie bedziesz musial podac po prostu 
 * PID i nr strony, ktora chcesz czytac, mozesz odczytywac w jakiejs petli for( int i = 0; i < ts.ilosc_wpisow; i++)
 * czytaj_proces(int nr_procesu, i) - NUMEROWANIE STRON W PROCESIE I PAMIECI od 0! 
 */
public static byte[] czytaj_proces(TablicaStron ts, int nr_strony) throws Exception{ // nr_strony = NR ROZKAZU!
	if(nr_strony > ts.ilosc_wpisow - 1){
		Debug.println("Error 0x00 proces " + ts.getPID() + " strona " + nr_strony);
		throw new ArrayIndexOutOfBoundsException(); // NUMERUJEMY OD 0, tak samo counter od zera = zerowy rozkaz.
	}
	if(ts.tab[nr_strony].getValid())
		return pamiec[ts.tab[nr_strony].getNrRamki()].frame;
	else
	{ 
		if(ts.tab[nr_strony].getNrRamki() >= 0){
		// w polu nr_ramki jest adres do miejsca w pliku stronicowania i strona sie kopiuje do pamieci
		Debug.println("Strona nr " + nr_strony + " nie byla w pamieci. Trwa sprowadzanie z pliku wymiany");
		int nowa_ramka = ZnajdzWolnaRamke();
		pamiec[nowa_ramka].frame = VM.plikWymiany.przestrzen_dyskowa[ts.tab[nr_strony].getNrRamki()].stronica.clone();
		ts.tab[nr_strony].UstawObiekt(nowa_ramka, true, (byte)0);
		FIFO_in(nowa_ramka);
		pamiec[nowa_ramka].PID = ts.PID;
		Debug.println("Strona sprowadzona z pliku wymiany i zapisana w ramce nr " + nowa_ramka);
		return pamiec[nowa_ramka].frame;
		}
		else throw new Exception("Stronicy nie ani w pamieci ani w pliku wymiany. Proces musi sie zakonczyc");//RZUC WYJATEK; PROGRAM MUSI SIE ZAKONCZYC BO NEI MA DANYCH	
	}
}
public int rozmiar_pamieci()
{
	int rozmiar = ilosc_ramek * 8;
	return rozmiar; // zwraca rozmiar pamieci w bajtach
}
public static void zwolnij_ramke(int nr_ramki)
{
	//sprawdz czy dirty bit
	int temp_pid = pamiec[nr_ramki].PID;
	for(TablicaStron x : listaT)
		if(x.getPID() == temp_pid)
		for(int i = 0; i < x.ilosc_wpisow; i++)
			if(x.tab[i].getValid()) // musimy sprawdzic czy valid bo w numerze ramki, moze byc ten adres do pliku wymiany
			if(x.tab[i].getNrRamki() == nr_ramki) // w tablicy stron tego procesu musze zmienic bit z valid na invalid
			{
				if(x.tab[i].getDirty() == 1) //jesli jest dirty bit to zapisujemy 
					{
						for(int y = 0; y < VM.plikWymiany.pojemnosc; y++) // sprawdzamy caly plik wymiany
						{
							if(VM.plikWymiany.przestrzen_dyskowa[y].PID != temp_pid) // sprawdzamy PID procesu
								continue; // jesli jest rozne PID to sprawdzamy dalej
							else if(VM.plikWymiany.przestrzen_dyskowa[y].PID == temp_pid)
									if(VM.plikWymiany.przestrzen_dyskowa[y].nr_strony == i) // sprawdzany strone w danym procesie
										VM.plikWymiany.przestrzen_dyskowa[y].stronica = pamiec[nr_ramki].frame.clone(); // update
							else continue;																// danych w pliku
						}
					}
				x.tab[i].setValid(false);
				
				boolean jest = false; // czy dana stronica jest juz w pliku wymiany
				for(int y = 0; y < VM.plikWymiany.pojemnosc; y++) // sprawdzamy caly plik wymiany
				{
					if(VM.plikWymiany.przestrzen_dyskowa[y].PID != temp_pid) // sprawdzamy PID procesu
						continue; // jesli jest rozne PID to sprawdzamy dalej
					else if(VM.plikWymiany.przestrzen_dyskowa[y].PID == temp_pid)
							if(VM.plikWymiany.przestrzen_dyskowa[y].nr_strony == i) // sprawdzany strone w danym procesie
								jest = true;
				else continue;
				}
				if(!jest){ // jesli nie ma tej strony na dysku to ja wprowadzamy.
				int wolne = VM.plikWymiany.wolne_miejsce();
				VM.plikWymiany.przestrzen_dyskowa[wolne].stronica = pamiec[nr_ramki].frame.clone();
				VM.plikWymiany.przestrzen_dyskowa[wolne].PID = pamiec[nr_ramki].getPID();
				VM.plikWymiany.przestrzen_dyskowa[wolne].nr_strony = i; 
				x.tab[i].setNrRamki(wolne);
				}
			}
			
	
	pamiec[nr_ramki].Wyczysc_Ramke(); // funkcja, ktora czysci ramke
	tab_wolnych_ramek[nr_ramki] = 1;
}
public static boolean CzyWolna(int nr_ramki){
	if(tab_wolnych_ramek[nr_ramki] == 1)
		return true;
	else return false;
}
public static int ZnajdzWolnaRamke(){
	int y = 0; // sprawdzenie ramek, numerowane od 0.
	
	 while(CzyWolna(y)!= true) // szukanie wolnej ramki
	 {
		 y++;
		 if(y > 7)
		 { 
			 y = FIFO_out(); // usuwanie ramki FIFO
			 zwolnij_ramke(y);
			 tab_wolnych_ramek[y] = 0;
			 Debug.println("Wszystkie ramki byly zajete. Wybrano ramke nr " + y);
			 return y;
		 }
	 }
	 tab_wolnych_ramek[y] = 0; // wybrana ramka nie jest juz wolna
	 Debug.println("Wybrano wolna ramke o nr " + y);
	 return y; // zwraca wolna ramke
	 
}
public static void FIFO_in(int ramka)
{
	Debug.println("Wstawiono do kolejki ramke o nr " + ramka);
	myQueue.add(ramka);
}
public static int FIFO_out()
{
	int nowa = myQueue.poll();
	Debug.println("Usunieto z kolejki ramke o nr " + nowa);
	return nowa;
}
public void FIFO_show()
{
	System.out.println("Aktualna kolejka ramek: ");
	for(Iterator<Integer> it = myQueue.iterator(); it.hasNext(); ){
		Integer b = it.next();
		System.out.println(b + " ");
		}
	
	System.out.println("");
	
}
public void wstaw_proces(TablicaStron ts)
{
	int y;
	for(int i = 0; i < ts.ilosc_wpisow; i++)
	{
		if(ts.tab[i].getValid())
		{
			y = ZnajdzWolnaRamke();
			tab_wolnych_ramek[y] = 0;
			FIFO_in(y); // dodanie nr ramki do kolejki FIFO
			pamiec[y].frame  = ts.tab[i].stronica.clone(); // kopiowanie stronicy do ramki
			pamiec[y].PID = ts.getPID();
			ts.tab[i].UstawObiekt(y, true, (byte) 0); // argumenty: numer ramki, valid invalid, dirty.
		}
		else 
		{
			int wolne = VM.plikWymiany.wolne_miejsce(); //szukanie wolnego miejsca w pliku stronicowania
			VM.plikWymiany.przestrzen_dyskowa[wolne].stronica = ts.tab[i].stronica.clone(); //skopiowanie wartosci 
			VM.plikWymiany.przestrzen_dyskowa[wolne].setPID(ts.getPID());
			VM.plikWymiany.przestrzen_dyskowa[wolne].nr_strony = i;
			ts.tab[i].nr_ramki = wolne; // zapisanie miejsca w pliku stronicowania w miejsce nieuzywanej na razie ramki.
		}
	}
	Debug.println("Alokowanie pamieci dla procesu o PID " + ts.getPID() + " zakonczylo sie sukcesem.");
	
}
//public static void main(String args[]) {

//}
}
