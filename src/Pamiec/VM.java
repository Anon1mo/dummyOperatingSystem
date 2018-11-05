package Pamiec;

public abstract class VM { // Virtual Memory! Pamiec Operacyjna + Plik Wymiany, dostep z kazdego miejsca VM.pamiecOper lub VM.plikWymiany
	
public static PageFile plikWymiany = new PageFile(64);
public static Memory pamiecOper = new Memory(8);

}
