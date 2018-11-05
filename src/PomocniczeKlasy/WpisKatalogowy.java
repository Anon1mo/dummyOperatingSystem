package PomocniczeKlasy;

import java.io.Serializable;
import java.lang.String;

public class WpisKatalogowy implements Serializable
{

private String nazwa; //nazwa pliku w standardzie 8.3
private String typ; //informacja s³u¿¹ca do rozpoznana rodzaju zawartoœci pliku
private int lokalizacja; //pierwszy blok tablicy FAT
private int rozmiar; //bie¿¹cy rozmiar pliku w bajtach
private String dataUtworzenia; //data utworzenia pliku
private byte flaga; //ustawienia atrybutów,1 bit – atrybut Readonly



//-----------------------------Konstruktorzy-----------------


public WpisKatalogowy() 
{
	
}

public WpisKatalogowy(String nazwa, String typ, int lokalizacja, int rozmiar,String dataUtworzenia, byte flaga) 
{
	this.nazwa = nazwa;
	this.typ = typ;
	this.lokalizacja = lokalizacja;
	this.rozmiar = rozmiar;
	this.dataUtworzenia = dataUtworzenia;
	this.flaga = flaga;
}


//-----------------------------Get/Set-----------------------
public String getNazwa() 
{
	return nazwa;
}
public void setNazwa(String nazwa) 
{
	this.nazwa = nazwa;
}

public String getTyp() 
{
	return typ;
}
public void setTyp(String typ) 
{
	this.typ = typ;
}

public int getLokalizacja() 
{
	return lokalizacja;
}
public void setLokalizacja(int lokalizacja) 
{
	this.lokalizacja = lokalizacja;
}

public int getRozmiar() 
{
	return rozmiar;
}
public void setRozmiar(int rozmiar) 
{
	this.rozmiar = rozmiar;
}

public String getDataUtworzenia() 
{
	return dataUtworzenia;
}
public void setDataUtworzenia(String dataUtworzenia) 
{
	this.dataUtworzenia = dataUtworzenia;
}

public byte getFlaga() 
{
	return flaga;
}
public void setFlaga(byte flaga) 
{
	this.flaga = flaga;
}

}