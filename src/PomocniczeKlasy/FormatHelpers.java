package PomocniczeKlasy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.String;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public abstract class FormatHelpers 
{
	
/**
 * 
 * @param dataWejsciowa
 * @return String
 */
	public static String DateToString(Date dataWejsciowa)
	{
		String dataWyjsciowa = null;

		DateFormat df = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
		dataWyjsciowa = df.format(dataWejsciowa);
		return dataWyjsciowa;
	}

	/**
	 * 
 * @param dataWejsciowa Domyslny format datyWejsciowej: "yyyy MMM dd HH:mm:ss"
	 * @return Date
	 * @throws ParseException 
	 */
	public static Date StringToDate(String dataWejsciowa) throws ParseException
	{
		Date dataWyjsciowa = null;

		DateFormat df = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
		dataWyjsciowa = df.parse(dataWejsciowa);
		return dataWyjsciowa;
	}

	
	/**
	 * 
	 * @param byteTab
	 * @return int
	 */
	public static int ByteTabToInt(byte[] byteTab)
	{
		ByteBuffer buffer = ByteBuffer.wrap(byteTab);
		int result = buffer.getInt();
		return result;
	}
	
	/**
	 * 
	 * @param nazwaPliku
	 * @return boolean
	 */
	public static boolean AssertNazwaPliku(String nazwaPliku)
	{
		Pattern pattern = Pattern.compile("[a-zA-Z0-9]{1,8}\\.[a-zA-Z0-9]{1,3}");
		Matcher m = pattern.matcher(nazwaPliku);
		
		if(m.matches())
			return true;
		else
			return false;
	}
	
	public static String GetNazwaPliku(String nazwaPliku)
	{
		
	    String result = nazwaPliku.split("\\.")[0];
	    	    
		return result;
	}
	
	public static String GetRozszerzeniePliku(String nazwaPliku)
	{
	    String result = nazwaPliku.split("\\.")[1];
	    
		return result;
	}
	
	
	
}

