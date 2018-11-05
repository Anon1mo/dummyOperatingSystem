package PomocniczeKlasy;

import java.io.Serializable;

public class BlokDanych implements Serializable
{

	private byte[] daneBloku = new byte[32];
	
	
	public BlokDanych()
	{
		
	}
	
	public BlokDanych(byte[] dane)
	{
		this.daneBloku = dane;
	}
	
	public byte[] getDaneBloku() 
	{
		return daneBloku;
	}

	public void setDaneBloku(byte[] daneBloku) 
	{
		this.daneBloku = daneBloku;
	}

	public void TEST()
	{
		System.out.println("TEST");
	}
}
