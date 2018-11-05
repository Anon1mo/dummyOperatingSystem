package consoleWithMenu;
import GlowneKlasy.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Scanner;

import javaConsole.JavaConsole;
import menu.*;
import Pamiec.*;
import PomocniczeKlasy.BufIStat;
import PomocniczeKlasy.FormatHelpers;
import kernel.*;

public class ConsoleWithMenu {

	private static boolean fContinue = true;
	private static boolean fContinueItem2 = true;
	private static boolean fContinueItem3 = true;;
	private static JavaConsole console;
	private static Scanner scanner;
	
	public static void exitHandler()
	{
		fContinue = false;
	}


    public static void item1Handler() throws IOException{
                   
                            System.out.println("Wybrales stworzenie pliku");
                            System.out.println("Wpisz nazwe pliku");
                            
                            String nazwaPliku = "";
                    		
                    		while(true)
                    		{
                    			nazwaPliku = scanner.nextLine();
                    			
                    			if(WarstwaLogiczna.IstniejeTakiWpis(nazwaPliku))
                    			{
                    				System.out.println("Plik o podanej nazwie juz istnieje. Podaj poprawna nazwe lub zakoncz piszac \"koniec\".");
                    			}
                    			else if(nazwaPliku.equals("koniec"))
                    			{
                    				return;
                    			}
                    			else if(!FormatHelpers.AssertNazwaPliku(nazwaPliku))
                    			{
                    				System.out.println("Nazwa pliku nieprawidlowa. Poprawny fomrat: XXXXXXXX.XXX (8.3) \nPodaj poprawna nazwe lub zakoncz piszac \"koniec\".");
                    			}
                    			else
                    			{
                    				
                    				System.out.println("Nazwa pliku podana prawidlowo.");
                    				break;
                    			}
                    		}
   
                            System.out.println("Wpisz dane pliku. Konczysz wpisaniem \"koniec\". Format: XXX R(Rejestr) D(Nr Rejestru: 0, 1, 2) R(Rejestr) D(Liczba) 1(valid)");
                            
                           
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();//obiekt do konkatenacji tablic bajtowych
                            
                            String dane = "";
                            while(true)
                            {
                                  dane = scanner.nextLine();
                                 // System.out.println(dane); 
                                    if(dane.equals("koniec"))
                                                    break;
                                   
                                    String[] tab = dane.split("\\s");
                                    dane += " ";
                                    byte[] temp = dane.getBytes();
    
                                    if(tab[0].equals("ADD") || tab[0].equals("SUB") || tab[0].equals("MUL") || tab[0].equals("MOV") || tab[0].equals("JNZ") || tab[0].equals("JZ"))
                                    {
                                    	int isGreater = Integer.parseInt(tab[2]);
                                    	if(isGreater > 2){
                                    		System.out.println("Bledny rejestr. Do wyboru: 0 1 2");
                                    		continue;
                                    	}
                                    	else
                                    		System.out.println("Rozkaz prawidlowy");
                                    }
                                    else {
                                            System.out.println("Bledny rozkaz. Napisz go ponownie. Dostepne rozkazy: ADD SUB MUL JNZ JZ");
                                            continue;
                                    }
                                    outputStream.write(temp);//Dodanie tablicy
                                   // byte[] tym = outputStream.toByteArray();
                                   // String tymS = new String(tym);
                                   // System.out.println(tymS);
                            }
                            byte[] end = new byte[3];
                            end[0] = 'E';
                            end[1] = 'N';
                            end[2] = 'D';
                          
                            outputStream.write(end);//Dodanie znacznika konca pliku
                          
                            byte[] toFat = outputStream.toByteArray();
                            try 
                            {
                            	int status = WarstwaPubliczna.Create(nazwaPliku, toFat);
                            	if(status == -1)
                            	{
                            		System.out.println("Blad podczas zapisu: Brak miejsca na dysku.");
                            	}
                            	else if(status == -2)
                            	{
                            		System.out.println("Blad podczas zapisu: Zly format nazwy pliku.");	
                            	}
                            	else if(status == -4)
                            	{
                            		System.out.println("Blad podczas zapisu: Plik o podanej nazwie ju¿ istnieje.");
                            	}
                            	else
                            		System.out.println("Plik zostal poprawnie zapisany.");
								
							} catch (FileNotFoundException e) 
							{
								
								System.out.println("Wyjatek: " +  e.getMessage());
								
							} catch (ClassNotFoundException e) {
								
								System.out.println("Wyjatek: " +  e.getMessage());
							} catch (IOException e) {
								
								System.out.println("Wyjatek: " +  e.getMessage());
							}

                            scanner.nextLine();
            }


	public static void item3Handler()
	{
		VM.pamiecOper.wyswietl_ram();
		
		System.out.println("\nAby wrocic do menu wcisnij dowolny klawisz");	
		String akcja = scanner.nextLine();
	}
	
	public static void item4Handler()
	{
		System.out.println("Ktory plik chcesz uruchomic?");
		WarstwaLogiczna.ToStringDET();
		String nazwaPliku = "";
		
		while(true)
		{
			nazwaPliku = scanner.nextLine();
			if(WarstwaLogiczna.IstniejeTakiWpis(nazwaPliku))
			{
				break;
			}
			else if(nazwaPliku.equals("koniec"))
			{
				return;
			}
			else
			{
				System.out.println("Podany plik nie istnieje. Podaj poprawna nazwe lub zakoncz piszac \"koniec\".");
			}
		}
		
		BufIStat bis = new BufIStat();
		bis = WarstwaPubliczna.Read(nazwaPliku);
		/*Kody bledow opisane w klasie CFG
		* 0	Operacja prawid³owo zakoñczona
		 *-1	Brak miejsca na dysku
		 *-2	Z³y format nazwy pliku
		 *-3	Plik tylko do odczytu
		 *-4	Plik o podanej nazwie ju¿ istnieje
		 *-5	Nie znaleziono wpisu o podanej nazwie*/
		
		
		if(bis.getStatus() == -2 )
		{
			System.out.println("Blad przy odczycie: z³y format nazwy pliku");
		}
		else if(bis.getStatus() == -5)
		{
			System.out.println("Blad przy odczycie: Nie znaleziono wpisu o podanej nazwie");
			return;
		}
		
		
		byte[] odczyt = bis.getBufor();
		//WASZE INSTRUKCJE CO ZROBIC Z TABLICA BAJTOW
		PCB p1 = new PCB(4, odczyt);
		p1.getTablica().alokujProces();
		p1.getTablica().zawartosc();
		Kernel.Int(1, p1);
		
		String k = scanner.nextLine();
	}
	
	
	public static void item2AHandler(){
			System.out.println("Wybrales 2A.");
			String s = scanner.nextLine();
			System.out.println(s);
			scanner.nextLine();
	}

	public static void item2BHandler(){
			System.out.println("Wybrales 2B.");
			scanner.nextLine();
	}

	public static void backHandler(){
			fContinueItem2 = false;
			fContinueItem3 = false;
	}

	
	public static void item2Handler(){
		fContinueItem2 = true;
		Menu menu = new Menu(console);

		menu.add("Item 2A", new MenuCallback() { public void Invoke() { item2AHandler(); } });
		menu.add("Item 2B", new MenuCallback() { public void Invoke() { item2BHandler(); } });
		menu.add("Back", new MenuCallback() { public void Invoke() { backHandler(); } });

		while(fContinueItem2)
		{
			console.clear();
			System.out.println("Pozycja nr 2: Wybierz opcje.");
			menu.show();
		}
	}
	
	public static void item5Handler(){
		WarstwaPubliczna.ShowFAT();
		
		System.out.println("Aby wrocic do menu wpisz \"koniec\"");
		
		String akcja = "";
		while(true)
		{
			akcja = scanner.nextLine();
			if(akcja.equals("koniec"))
			{
				return;
			}
			else
			{
				System.out.println("Aby wrocic do menu wpisz \"koniec\"");
			}
		}
	}
	
	public static void item6Handler(){
		
		System.out.println("Wpisz nazwe pliku ktora ma byc zmieniona lub zakoncz piszac \"koniec\"");
		
		String nazwaPliku = "";
		
		while(true)
		{
			nazwaPliku = scanner.nextLine();
			
			if(WarstwaLogiczna.IstniejeTakiWpis(nazwaPliku))
			{
				break;
			}
			else if(nazwaPliku.equals("koniec"))
			{
				return;
			}
			else
			{
				System.out.println("Podany plik nie istnieje. Podaj poprawna nazwe lub zakoncz piszac \"koniec\".");
			}
		}
		
		System.out.println("Podaj nowa nazwe pliku");
		String nowaNazwaPliku = scanner.nextLine();
		
		try {
			
			WarstwaPubliczna.Rename(nazwaPliku, nowaNazwaPliku);
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		System.out.println("Nazwa podanego pliku zostala pomyslnie zmieniona z "+ nazwaPliku + " na"+ nowaNazwaPliku +". Zakoncz piszac \"koniec\"");
	}
	
	public static void item7Handler()
	{
		System.out.println("Wpisz nazwe pliku ktory ma byc usuniety lub zakoncz piszac \"koniec\"");
		
		String nazwaPliku = "";
		
		while(true)
		{
			nazwaPliku = scanner.nextLine();
			
			if(WarstwaLogiczna.IstniejeTakiWpis(nazwaPliku))
			{
				break;
			}
			else if(nazwaPliku.equals("koniec"))
			{
				return;
			}
			else
			{
				System.out.println("Podany plik nie istnieje. Podaj poprawna nazwe lub zakoncz piszac \"koniec\".");
			}
		}
		
		try 
		{
			WarstwaPubliczna.Delete(nazwaPliku);
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		System.out.println("Podany plik zostal pomyslnie usuniety. Zakoncz piszac \"koniec\"");
	}

	public static void item8Handler(){
		
		WarstwaPubliczna.Dir();
		
		System.out.println("Aby wrocic do menu wpisz \"koniec\"");
		
		String akcja = "";
		while(true)
		{
			akcja = scanner.nextLine();
			if(akcja.equals("koniec"))
			{
				return;
			}
			else
			{
				System.out.println("Aby wrocic do menu wpisz \"koniec\"");
			}
		}
	}
	public static void item9Handler(){
		VM.plikWymiany.Wyswietl();
		
		System.out.println("\nAby wrocic do menu wcisnij dowolny klawisz");
		
		String akcja = scanner.nextLine();
	}
	public static void main(String[] args) {
    	Thread Console = new Thread() {
        public void run() {
		//Inicjalizacja komponentów
		CFG.main();
		console = new JavaConsole();
		scanner = new Scanner(System.in);
		Menu menu = new Menu(console);
		Debug.init();
		
		menu.add("Stworz plik", new MenuCallback() { public void Invoke() 
		{ try 
		{
			item1Handler();
		} catch (IOException e) 
		{
		
			
		} } });
		menu.add("Uruchom plik", new MenuCallback() { public void Invoke() { item4Handler(); } });
		//menu.add("Uruchom plik", new MenuCallback() { public void Invoke() { item2Handler(); } });
		menu.add("Wyswietl RAM", new MenuCallback() { public void Invoke() { item3Handler(); } });
		menu.add("Wyswietl Plik Wymiany", new MenuCallback() { public void Invoke() { item9Handler(); } });
		menu.add("Wyswietl FAT", new MenuCallback() { public void Invoke() { item5Handler(); } });
		menu.add("Lista plikow", new MenuCallback() { public void Invoke() { item8Handler(); } });

		menu.add("Zmien nazwe pliku", new MenuCallback() { public void Invoke() { item6Handler(); } });
		menu.add("Usun plik", new MenuCallback() { public void Invoke() { item7Handler(); } });
		
		menu.add("Zamknij system", new MenuCallback() { public void Invoke() { exitHandler(); } });

		
		while(fContinue)
		{
			console.clear();
			System.out.println("Witamy w systemie Windowsopodobnym. Wybierz opcje:");
			menu.show();
		}
		
		System.exit(0);
        }
		};
		Console.start();
		try{
		Kernel.cpumain();
		}catch(Exception e){e.getMessage();}
	}
}
