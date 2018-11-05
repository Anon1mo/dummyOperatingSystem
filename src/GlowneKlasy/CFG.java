package GlowneKlasy;

/**
 * Przed uruchomieniem inicjalizacji nalezy skonfigurowac sciezki serializacji i utworzyc
 * odpowiednie foldery na dysku. Upewnij sie ze masz uprawnienia do zapisu w podanych lokalizacjach.
 * 
 @author Grupa I2.2 
 @version 3.0
 *
 *KODY BLEDOW APLIKACJI:
 *
 * 0	Operacja prawid³owo zakoñczona
 *-1	Brak miejsca na dysku
 *-2	Z³y format nazwy pliku
 *-3	Plik tylko do odczytu
 *-4	Plik o podanej nazwie ju¿ istnieje
 *-5	Nie znaleziono wpisu o podanej nazwie
 *

 *
 */
public abstract class CFG 
{
	 public static final String serDETPath = "c:\\projekty\\Serializacja\\fat.ser";
	 public static final String serFATPath = "c:\\projekty\\Serializacja\\det.ser";
	 public static final String serDATAPath = "c:\\projekty\\Serializacja\\data.ser";
	

	public static void main() 
	{
		WarstwaLogiczna.InitFAT(serDETPath, serFATPath, serDATAPath);
	}
	
}
