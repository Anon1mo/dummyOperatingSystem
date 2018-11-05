package Pamiec;

import java.awt.BorderLayout;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;


public class Debug {

	private static JFrame frame_debug = new JFrame();   
    
   
    private static JTextArea text_debug = new JTextArea();        
    
    private static TextAreaOutputStream output_debug = new TextAreaOutputStream( text_debug, 6000 );
             
    private static  PrintStream debug = new PrintStream( output_debug );
             
         
    public static void init()
    {
    	frame_debug.add( new JLabel(" Debug" ), BorderLayout.NORTH );
    	text_debug.setEditable(false);
    	frame_debug.add( new JScrollPane( text_debug )  );
    	frame_debug.setSize(400,400);
    	frame_debug.setVisible( true );
    	frame_debug.add(new JScrollPane());
    }
    public static void print(String s)
    {
    	debug.print(s);
    	text_debug.setCaretPosition(text_debug.getDocument().getLength());
    }
    public static void println(String s)
    {
    	debug.println(s);
    	text_debug.setCaretPosition(text_debug.getDocument().getLength());
    }
	
}
