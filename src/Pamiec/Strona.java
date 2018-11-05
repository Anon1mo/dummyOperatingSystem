package Pamiec;

public class Strona { //klasa uzywana do PageFile - pliku wymiany
	byte[] stronica;
	int PID;
	int nr_strony; // nr strony w TablicyStron danego procesu
	public Strona(){
		stronica = new byte[8];
	}
	public void setPID(int PID){
		this.PID = PID;
	}
	public int getPID(){
		return PID;
	}
	public void setNRstr(int nr_strony){
		this.nr_strony = nr_strony;
	}
	public int getNRstr(){
		return nr_strony;
	}
	public void Wypelnij_stronice(byte[] tab)
	{
		stronica = tab.clone();
		
	}
	public void Wyczysc_stronice()
	{
		for(int i = 0; i < 8; i++)
			stronica[i] = 0;
		
		PID = 0;
		nr_strony = 0;
	}
	public void Wypelnij_stronice(byte p0, byte p1, byte p2, byte p3, byte p4, byte p5, byte p6, byte p7)
	{ // niestety nie ma argumentow domyslnych w javie.
		stronica[0] = p0;
		stronica[1] = p1;
		stronica[2] = p2;
		stronica[3] = p3;
		stronica[4] = p4;
		stronica[5] = p5;
		stronica[6] = p6;
		stronica[7] = p7;
	}
	public byte SprawdzBajt(int nr_bajtu)
	{
		return stronica[nr_bajtu];
	}
}
