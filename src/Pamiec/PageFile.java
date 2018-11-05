package Pamiec;

public class PageFile {
int pojemnosc;
Strona[] przestrzen_dyskowa;
boolean[] wolne;
PageFile(int pojemnosc)
{
	this.pojemnosc = pojemnosc;
	
	przestrzen_dyskowa = new Strona[pojemnosc];
	for(int i = 0; i < pojemnosc; i++)
		przestrzen_dyskowa[i] = new Strona();
	
	wolne = new boolean[pojemnosc];
	for(int i = 0; i < wolne.length; i++)
		wolne[i] = true;
}
byte[] kopiujStrone(int miejsce)
{
	byte[] copy = przestrzen_dyskowa[miejsce].stronica.clone();
	
	return copy;
	
}
public int wolne_miejsce()
{
	int i;
	for(i = 0; i < wolne.length; i++)
		if(wolne[i])
		{
			wolne[i] = false; // juz nie bedzie wolne
			return i;
		}

	Debug.println("Dysk zapelniony");
	return -1; //informacja dla procesora, ze ma wstrzymac proces
}
public int zapiszStrone(byte[] dane) // zwraca adres, na ktorej zostala zapisana strona
{
	int gdzie = wolne_miejsce();
	przestrzen_dyskowa[gdzie].stronica = dane.clone();
	return gdzie;
	//zapisanie strony jesli bylby dirty bit
}
public void zwolnijMiejsce(int PID)
{
	for(int i = 0; i < pojemnosc; i++)
		if(przestrzen_dyskowa[i].PID == PID)
		{
			przestrzen_dyskowa[i].Wyczysc_stronice();
			wolne[i] = true;
		}
	
}
public void Wyswietl()
{
	for(int i = 0; i < pojemnosc; i++)
	{
		System.out.println("Adres " + i + " ");
		if(wolne[i])
			System.out.println("wolny");
		else 
		{
			System.out.println("zajety przez proces o PID " + przestrzen_dyskowa[i].getPID() + " nr strony " + 
					przestrzen_dyskowa[i].nr_strony + " Dane: ");
			for(int j = 0; j < 8; j++)
				System.out.println(przestrzen_dyskowa[i].stronica[j] + " ");
			
			System.out.println("");
				
		}
	}
	
}
}

