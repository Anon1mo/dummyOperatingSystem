package GlowneKlasy;

import PomocniczeKlasy.BlokDanych;
import PomocniczeKlasy.BufIStat;
import PomocniczeKlasy.FormatHelpers;
import PomocniczeKlasy.WpisKatalogowy;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.lang.Math;
import java.math.BigInteger;
import java.util.*;

import javax.print.attribute.standard.DateTimeAtCompleted;



public abstract class WarstwaPubliczna 
{


	/**
	 * 
	 * @param nazwaPliku
	 * @param dane
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static int Create(String nazwaPliku, byte[] dane) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		if(!FormatHelpers.AssertNazwaPliku(nazwaPliku))
		{
			return -2;//Zly format nazwy pliku
		}
		
		if(WarstwaLogiczna.IstniejeTakiWpis(nazwaPliku))
		{
			
			return -4;//Plik o podanej nazwie juz istnieje
		}
		
		int liczPotrzBlok = (int) Math.ceil(dane.length/32.0); //liczba potrzebnych bloków do zaalokowania przekazanej liczby bajtów zak³adaj¹c, ¿e blok ma 32 bajty
		ArrayList<Integer> indeksyWolnychBlokow = new ArrayList<Integer>();
		
		//"11111110" = 254 -> oznaczenie w tablicy FAT, ¿e dany blok jest wolny
		//"11111111" = 255 -> oznaczenie koñca pliku w tablicy FAT
		//*"11111101"  = 253 -> ozaczenie komorki FAT ktora nie zostala zajeta przez zaden fizyczny plik
		//i jest sztucznie zajeta przez txtField z poziomu GUI
		//Komorka taka jest traktowana z puktu widzenia systemu plikow jako zajeta ale w rzeczywistosci nie znajduje
		//sie w niej zaden konkretny plik ( nie sa w nich zapisane adresy kolejnych blokow)
		
		for(int i = 0; i < WarstwaLogiczna.fileAllocationTable.length; i++)
		{
			if(WarstwaLogiczna.fileAllocationTable[i] == (byte)254)//Jezli w tablicy FAT napotkamy na 11111110
				indeksyWolnychBlokow.add(i);//Zapamietujemy wolny indeks z tablic FAT na liscie
			
			
			if(indeksyWolnychBlokow.size() == liczPotrzBlok)
			{
				//Jezli znalezlismy juz tyle bloków ile potrzeba do zaalokowania pamieci
				break;//przerywamy szukanie
			}
		}	
				
		if(indeksyWolnychBlokow.size() < liczPotrzBlok)
		{
			//Wyrzuca wyjatek: nie mozna zaalokowac pamieci - niewystarczajaca liczba wolnych blokow na dysk
			return -1;//Brak miejsca na dysku
		}
		else
		{
			Date data = new Date();
			
			//TWORZENIE WPISU KATALOGOWEGO I DOPISANIE GO DO TABLICY WPISÓW
			WpisKatalogowy wpis = new WpisKatalogowy();
			wpis.setNazwa(nazwaPliku);
			wpis.setTyp(FormatHelpers.GetRozszerzeniePliku(nazwaPliku));
			//wpis.setTyp(0);
			wpis.setLokalizacja(indeksyWolnychBlokow.get(0));
			wpis.setRozmiar(dane.length);
			wpis.setDataUtworzenia(data.toString());
					
			WarstwaLogiczna.DodajWpis(wpis);
			
			//OPERACJE NA TABLICY FAT
			for(int i = 0; i < indeksyWolnychBlokow.size() - 1; i ++)//Nie robimy tej operacji dla ostatniego indeksu poniewa¿ ten bedzie mia³ wartoœæ 11111111 ozn. koniec pliku
			{
				
				//Wczytujemy indeks tablicy FAT, ktory oznacza, ze dany blok jest pusty z indeksyWolnychBlokow
				//Przypisujemy indeksowi tablicy fat o danym numerze kolejny indeks tworz¹c ³añcuch az do przedostatniego elementu
				 WarstwaLogiczna.fileAllocationTable[indeksyWolnychBlokow.get(i)] = indeksyWolnychBlokow.get(i + 1).byteValue();
				 
			}
			
			//Dla ostatniego elementu na liœcie wolnych indeksów zapisujemy wartoœæ 11111111
			WarstwaLogiczna.fileAllocationTable[indeksyWolnychBlokow.get(indeksyWolnychBlokow.size() - 1)] = (byte) 255;
			
			//OPERACJE NA TABLICY DANYCH
			//Dane w bajtach sa porcjowane na 32 bajtowe bloki
			
			
			for(int i = 0; i < liczPotrzBlok; i ++)  
			{
				byte[] porcjaDanych = new byte[32];
				
				for (int j = 0; j < porcjaDanych.length; j++) 
				{	
					if((i * 32 + j ) >= dane.length)
						break;
					porcjaDanych[j] = dane[i * 32 + j];//Tworzenie porcji danych
				}
				
				BlokDanych blok = new BlokDanych();
				blok.setDaneBloku(porcjaDanych);

				WarstwaLogiczna.dataTable[indeksyWolnychBlokow.get(i)] = blok;
			}
		}
		
		WarstwaLogiczna.ZapiszStanSystemu();
		
	    return 0;
	    
	}

	/**
	 * 
	 * @param nazwaPliku
	 * @return
	 */
	public static BufIStat Read(String nazwaPliku)
	{
		BufIStat buforIStatus = new BufIStat();
		
		int i = WarstwaLogiczna.GetPoczatekPlikuWFATJesliIstnieje(nazwaPliku);
		
		if(!FormatHelpers.AssertNazwaPliku(nazwaPliku))
		{
			buforIStatus.setStatus(-2);
			buforIStatus.setBufor(null);
			return buforIStatus;//Zly format nazwy pliku
		}
		
		if(i == -5)
		{
			buforIStatus.setStatus(-5);
			buforIStatus.setBufor(null);
			return buforIStatus;//Zly format nazwy pliku
		}
		
		ArrayList<Integer> idDoOdczytania = WarstwaLogiczna.GetListaWpisowFAT(nazwaPliku,i);
		
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		for (Integer integer : idDoOdczytania) 
		{
			byte tmp[] = WarstwaLogiczna.dataTable[integer.intValue()].getDaneBloku();//Tworzenie tymczasowej tablicy bajtow
			try 
			{
				outputStream.write(tmp);//przygotowanie do konkatenacji
				
			} catch (IOException e) 
			{
				buforIStatus.setStatus(-10);
				buforIStatus.setBufor(null);
				return buforIStatus;//Wyjatek Javy: IOException
			}
			
		}
		
		byte buforTmp[] = outputStream.toByteArray();//Konkatenacja wszystkich danych pobranych z blokow
		int rozmiarPliku = WarstwaLogiczna.GetRozmiarPliku(nazwaPliku);
		
		byte[] bufor = Arrays.copyOfRange(buforTmp,0,rozmiarPliku);//Zwracana bedzie jedynie zawartosc pliku a nie ewentualne uzupelnienie bloku
		

		buforIStatus.setStatus(0);//Operacja pomyślnie zakonczona
		buforIStatus.setBufor(bufor);
		
		
		return buforIStatus;
	}
	
	/**
	 * 
	 * @param nazwaPliku
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static int Delete(String nazwaPliku) throws FileNotFoundException, IOException
	{
		//WarstwaLogiczna.ToStringFAT();
		int i = WarstwaLogiczna.GetPoczatekPlikuWFATJesliIstnieje(nazwaPliku);
		if(i == -5)
		{
			return -5;
		}
		
		ArrayList<Integer> idDoUsuniecia = WarstwaLogiczna.GetListaWpisowFAT(nazwaPliku,i);
		
		if(WarstwaLogiczna.UsunWpisONazwie(nazwaPliku) == -5)
		{
			return -5;
		}
		
		for (Integer integer : idDoUsuniecia) 
		{
			WarstwaLogiczna.fileAllocationTable[integer.intValue()] = (byte) 254;
			WarstwaLogiczna.dataTable[integer.intValue()].setDaneBloku(null);
			WarstwaLogiczna.dataTable[integer.intValue()] = null;
		}
		
		try 
		{
			WarstwaLogiczna.ZapiszStanSystemu();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
					
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		WarstwaLogiczna.ZapiszStanSystemu();
		
		return 0;
	}
	
	
	/**
	 * 
	 * @param nazwaPliku
	 * @param nowaNazwaPliku
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static int Rename(String nazwaPliku , String nowaNazwaPliku) throws FileNotFoundException, IOException
	{
		
		int i = WarstwaLogiczna.GetPoczatekPlikuWFATJesliIstnieje(nazwaPliku);
		//Jeśli nie udało się znalezc poczatku pliku FAT -> Blad
		if(i == -5)
		{
			return -5;//Blad dla shella
		}
		
		WarstwaLogiczna.directoryEntryTable[i].setNazwa(nowaNazwaPliku);
		
		WarstwaLogiczna.ZapiszStanSystemu();
		
		return 0;
	}
	
	/**
	 * Wypisanie tablicy wpisow katalogowych
	 */
	public static int Dir()
	{
		WarstwaLogiczna.ToStringDET();
		
		return 0;
	}
	
	/**
	 * Wypisanie tablicy FAT
	 */
	public static int ShowFAT()
	{
		WarstwaLogiczna.ToStringFAT();
		return 0;
	}
}
