package Pamiec;

public class Ramka {
	int PID;
	byte[] frame;
	public Ramka(){
		frame = new byte[8];
		PID = -1;
	}
	
public int czytajBajt(int nr_bajtu)
{
	return frame[nr_bajtu];
}
public byte[] CzytajRamke(){
	for(int i = 0; i < frame.length; i++)
		System.out.println("Zawartosc bajtu " + i + " : " + Integer.toBinaryString((int)frame[i]));
	
	return frame;
}
public void Wyczysc_Ramke()
{
	for(int x = 0; x < 8; x++)
		frame[x] = 0;
}
public int getPID(){
	return PID;
}
public void setPID(int PID){
	this.PID = PID;
}

}
