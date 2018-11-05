package Pamiec;

public class ObiektTablicy {
	
	byte[] stronica;
	boolean valid_invalid;
	int nr_ramki;
	byte dirty;
	public ObiektTablicy(){
		valid_invalid = false;
		nr_ramki = -1;
		dirty = 0;
		stronica = new byte[8];
	}
	public void UstawObiekt(int nr_ramki, boolean valid_invalid, byte dirty){
		this.nr_ramki = nr_ramki;
		this.valid_invalid = valid_invalid;
		this.dirty = dirty;
	}
	public void setValid(boolean valid_invalid){
		this.valid_invalid = valid_invalid; // to do bledu strony jednak
	}
	public void setDirty(byte dirty){
		this.dirty = dirty;
	}
	public boolean getValid()
	{
		return valid_invalid;// getValid, getDirty
	}
	public byte getDirty()
	{
		return dirty;
	}
	public int getNrRamki(){
		
		return nr_ramki;
	}
	public void setNrRamki(int nr_ramki){
		this.nr_ramki = nr_ramki;
	}
	public void Wypelnij_stronice(byte[] tab)
	{
		stronica = tab.clone();	
	}
}
