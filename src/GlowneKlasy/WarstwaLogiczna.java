package GlowneKlasy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;

import PomocniczeKlasy.BlokDanych;
import PomocniczeKlasy.WpisKatalogowy;

public abstract class WarstwaLogiczna {

	 public static boolean zainicjalizowano = false;//Statyczna flaga wykorzystwana w metodzie Create w WarstwiePublicznej
	 public static boolean saPlikiSerializacji;//Statyczna flaga okreslajaca czy znaleziono pliki serializacji na dysku
	 
	 public static WpisKatalogowy[] directoryEntryTable = new WpisKatalogowy[32];//Tablica zawieraj¹ca wpisy katalogowe
	 public static byte[] fileAllocationTable = new byte[32];//FAT
	 public static BlokDanych[] dataTable = new BlokDanych[32];//tablica danych zawieraj¹ca bloki danych


	
	 
	 /**
	 * Funkcja inicjalizujaca system plikow i wznawiajaca jego dzialanie jezeli podane sciezki do plikow serializacji
	 * podane sa prawidlowo
	 * 
	 * @param serDETPath 
	 * @param serFATPath 
	 * @param serDATAPath 
	  * 
	  */
	 public static void InitFAT(String serDETPath, String serFATPath, String serDATAPath)
	 {
		 	//Alternatywa dla knstruktora statycznego klasy WarstwaLogiczna
		 	//Działanie na statycznej fladze pozwalajacej sprawdzic czy tablica FAT została zainicjalizowana
		 	//Wykorzystywane przy pierwszym uruchminiu srodowiska graficznego
		 
		 	File plik1 = new File(serDETPath);
		 	File plik2 = new File(serFATPath);
		 	File plik3 = new File(serDATAPath);
		 	
		 	if(plik1.exists() && plik2.exists() && plik3.exists())
		 	{
		 		try
		 		{
					OdczytajStanSystemu();//Wczytujemy stan systemu
	
				} catch (FileNotFoundException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 	}
		 	else
		 	{
		 		if(!WarstwaLogiczna.zainicjalizowano)
				{
					for (int i = 0; i < WarstwaLogiczna.fileAllocationTable.length; i++) 
					{
						WarstwaLogiczna.fileAllocationTable[i] = (byte) 254;
					}
					WarstwaLogiczna.zainicjalizowano = true;
				}
		 	}
		 	
			
	 }
	 
	/**
	 * 
	 * @param wpis
	 */
	public static void DodajWpis(WpisKatalogowy wpis)
	{
		for(int i=0; i < directoryEntryTable.length; i++ )
		{
			if(directoryEntryTable[i] == null)
			{
				directoryEntryTable[i] = wpis;
				break;
			}
		}
	}
	
	/**
	 * 
	 * @param nazwaWpisu
	 * @param typWpisu
	 * @param lokalizacjaWpisu
	 * @param rozmiarWpisu
	 * @param dataUtworzeniaWpisu
	 * @param flagaWpisu
	 */
	public static void StwórzIDodajWpis(String nazwaWpisu,String typWpisu,int lokalizacjaWpisu,int rozmiarWpisu,String dataUtworzeniaWpisu,byte flagaWpisu)
	{
		WpisKatalogowy wpis = new WpisKatalogowy(nazwaWpisu,typWpisu,lokalizacjaWpisu,rozmiarWpisu,dataUtworzeniaWpisu,flagaWpisu);
		
		for(int i=0; i < directoryEntryTable.length; i++ )
			if(directoryEntryTable[i] == null)
				directoryEntryTable[i] = wpis;
	}
	
	/**
	 * 
	 * @param indeks
	 */
	public static void UsunWpisOIndeksie(int indeks)
	{
		if(directoryEntryTable[indeks] != null)
			directoryEntryTable[indeks] = null;
	}
	
	/**
	 * 
	 * @param nazwaPliku
	 */
	public static int UsunWpisONazwie(String nazwaPliku)
	{	
		if(!IstniejeTakiWpis(nazwaPliku))
		{
			return -5;//Nie znaleziono wpisu o podanej nazwie
		}
		
		for(int i = 0;i < directoryEntryTable.length;i++)
		{
			if(directoryEntryTable[i] != null)
				if (directoryEntryTable[i].getNazwa().equals(nazwaPliku)) 
				{
					directoryEntryTable[i] = null;
					System.out.println("Usunieto wpis pliku o nazwie: " + nazwaPliku);
					break;
				}
		}
		
		return 0;//Operacja prawidłowo zakonczona

	}
	
	/**
	 * 
	 * @param wpis
	 * @return
	 */
	public static boolean IstniejeTakiWpis(String wpis)
	{
		for(int i=0; i < directoryEntryTable.length ; i++)
			if(directoryEntryTable[i] != null)
				if(directoryEntryTable[i].getNazwa().equals(wpis))
					return true;
		return false;
	}
	
	
	/**
	 * 
	 * @param wpis
	 * @return
	 */
	public static int GetPoczatekPlikuWFATJesliIstnieje(String nazwaPliku)
	{
		for (int i = 0; i < directoryEntryTable.length; i++)
			if(directoryEntryTable[i] != null)
				if(directoryEntryTable[i].getNazwa().equals(nazwaPliku))
					return directoryEntryTable[i].getLokalizacja();	
			
		return -5;//Nie znaleziono wpisu o podanej nazwie 
	}
	
	/**
	 * Przed przekazaniem parametru poczPlikuWFAT nalezy upewnic sie ze plik podanej nazwie istnieje
	 * poprzez funkcje GetPoczatekPlikuWFATJesliIstnieje(String nazwaPliku).
	 * Jej rezult przekazac jako poczPlikuWFAT
	 * @param nazwaPliku
	 * @param poczPlikuWFAT
	 * @return
	 */
	public static ArrayList<Integer> GetListaWpisowFAT(String nazwaPliku, int poczPlikuWFAT)
	{
		ArrayList<Integer> lista = new ArrayList<>();

		lista.add(poczPlikuWFAT);
		int tmp = poczPlikuWFAT;
		int tmpNext;

		while(fileAllocationTable[tmp] != (byte) 255)
		{
			tmpNext = fileAllocationTable[tmp];
			lista.add(tmpNext);
			tmp = tmpNext;
			System.out.println("=> " + tmpNext );
		} 
		
		
		return lista;
	}
	
	
	/**
	 * 
	 * @param nazwaPliku
	 * @return
	 */
	public static int GetRozmiarPliku(String nazwaPliku)
	{
		for(int i=0; i < directoryEntryTable.length ; i++)
			if(directoryEntryTable[i] != null)
				if(directoryEntryTable[i].getNazwa().equals(nazwaPliku))
					return directoryEntryTable[i].getRozmiar();
		return -5;
	}
	
	/**
	 * Wypisanie tablicy FAT
	 */
	public static void ToStringFAT()
	{
		System.out.println("TABLICA FAT:\n");
		int count = 0;
		for (Byte i: fileAllocationTable) 
		{
			
			System.out.println( "FAT[" + count + "] = "+ (i == (byte)255 ? i.toString():"wolny"));
			count++;
		}
	}
	
	/**
	 * Wypisanie tablicy wpisow katalogowych
	 */
	public static void ToStringDET()
	{
		System.out.println("TABLICA DET:");
		
		for (WpisKatalogowy wk : directoryEntryTable) 
		{
			if(wk != null)
				System.out.println(wk.getNazwa() + " Rozmiar: " + wk.getRozmiar() + " B, Pocz od: FAT[" + wk.getLokalizacja() + "], " + " " + wk.getDataUtworzenia());
		}
	}
	/**
	 * Funkcja czyszczaca wszystkie wpisy katalgowe, tablice FAT oraz tablicę danych
	 */
	public static void WyczyscDysk()
	{
		for(int i = 0; i < 32; i++ )
		{
			fileAllocationTable[i] = (byte) 254;
			if(dataTable[i] != null)
			{
				dataTable[i].setDaneBloku(null);
				dataTable[i] = null;
			}
			UsunWpisOIndeksie(i);
		}

	}
	
	
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void ZapiszStanSystemu() throws FileNotFoundException, IOException
	{
		//Serializacja obiektow
		
		ObjectOutputStream out1 = new ObjectOutputStream(
									new BufferedOutputStream(
										new FileOutputStream(CFG.serFATPath)));
		
		for (int i = 0; i < fileAllocationTable.length; i++) 
		{
			out1.writeByte(fileAllocationTable[i]);
		}
		out1.close();
		
		ObjectOutputStream out2 = new ObjectOutputStream(
									new BufferedOutputStream(
										new FileOutputStream(CFG.serDETPath)));

		out2.writeObject(directoryEntryTable);
		out2.close();
		
		ObjectOutputStream out3 = new ObjectOutputStream(
									new BufferedOutputStream(
										new FileOutputStream(CFG.serDATAPath)));
		
		out3.writeObject(dataTable);
		out3.close();
	}
	
	
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void OdczytajStanSystemu() throws FileNotFoundException, IOException, ClassNotFoundException
	{
		//Deserializacja obiektow
		
		ObjectInputStream in1 = new ObjectInputStream(
									new BufferedInputStream(
										new FileInputStream(CFG.serFATPath)));
		
		for (int i = 0; i < fileAllocationTable.length; i++) 
		{
			fileAllocationTable[i] = in1.readByte();
		}
		in1.close();
		
		ObjectInputStream in2 = new ObjectInputStream(
									new BufferedInputStream(
										new FileInputStream(CFG.serDETPath)));
		
		directoryEntryTable = (WpisKatalogowy[]) in2.readObject();
		in2.close();
		
		ObjectInputStream in3 = new ObjectInputStream(
									new BufferedInputStream(
										new FileInputStream(CFG.serDATAPath)));
		
		dataTable = (BlokDanych[]) in3.readObject();
		in3.close();
	}
		
}
