package GUI;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

//import test1.tester;
import GlowneKlasy.WarstwaLogiczna;
import GlowneKlasy.WarstwaPubliczna;

import javax.swing.JSeparator;

public class frame extends JFrame {

	private JPanel contentPane;
	private static JTable FATtable_1;
	private static JTable DATAtable;
	private JTextField txtFieldRozmiar;
	private JTextField txtFieldNazwa;
	public static JTextField txtFieldZajete;
	public static JLabel lblError;
	private static JButton btnZajmijBloki;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try 
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() 
		{
			
			public void run() 
			{
				
				try 
				{
					
					frame frame = new frame();
					frame.setVisible(true);
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public frame() 
	{
		setTitle("FAT Monitor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 365, 501);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(245, 245, 245));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		//Tworzenie nagłówków od 0 do 8
		Vector<String> columnNames = new Vector<String>();
		for (int i = 0; i < 8; i++) 
			columnNames.addElement(String.valueOf(i));
				
		//Inicjalizacja tablicy FAT z wartościami 254
		Vector<Vector> data = new Vector<Vector>();
		for (int i = 0; i < 4; i++) 
		{
			Vector<String> rowX = new Vector<String>();	
			for (int j = i*8 ; j < i*8 + 8; j++) 
			{
				rowX.addElement("254");
			}	
			data.addElement(rowX);	
		}
		
		
		//Pierwsze tworzenie tablicyFAT - (wszystkie bloki wolne)
		FATtable_1 = new JTable(data,columnNames);	
		FATtable_1.setRowSelectionAllowed(false);
		FATtable_1.setEnabled(false);
		FATtable_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
			
				if(e.getClickCount() == 2)
				{
					
					txtFieldZajete.setText("Działa dwuklik");
				}
				else
				{
					
					txtFieldZajete.setText("Działa jednoklik");
				}
			}
			
		});
		
		FATtable_1.setBackground(new Color(245, 245, 245));
		FATtable_1.setToolTipText("EOF -> Koniec pliku");
		FATtable_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		final JLabel lblTablicaFat = new JLabel("TABLICA FAT");
		lblTablicaFat.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		final JLabel lblTablicaDanych = new JLabel("TABLICA DANYCH");
		lblTablicaDanych.setBackground(new Color(220, 220, 220));
		lblTablicaDanych.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		Vector<String> columnNames2 = new Vector<String>();
		for (int i = 0; i < 8; i++) 
			columnNames2.addElement(String.valueOf(i));
		
		Vector<Vector> data2 = new Vector<Vector>();
		for (int i = 0; i < 4; i++) 
		{
			Vector<String> rowX2 = new Vector<String>();	
			for (int j = i*8 ; j < i*8 + 8; j++) 
			{
				rowX2.addElement("254");
			}	
			data2.addElement(rowX2);	
		}
		
		DATAtable = new JTable(data2,columnNames2);
		DATAtable.setEnabled(false);
		
		DATAtable.setBackground(new Color(245, 245, 245));
		DATAtable.setToolTipText("1 -> blok zajęty,     \r\n0 -> blok pusty.");
		DATAtable.setFont(new Font("Tahoma", Font.PLAIN, 14));
		DATAtable.setRowSelectionAllowed(false);
		
		//Event handler dla przycisku "Zapisz plik"
		
		JButton btnOdwie = new JButton("Zapisz plik");
		btnOdwie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				if(!txtFieldNazwa.getText().isEmpty() && !txtFieldRozmiar.getText().isEmpty())
				{
					byte[] dane = new byte[Integer.valueOf(txtFieldRozmiar.getText())];
					
					Random generator = new Random();
					
					//Uzupełniamy dane przykładowymi danymi
					generator.nextBytes(dane);
					
					//Wywołuje on funkcję Create w Warstwie Publicznej z parametrami podanymi w textBoxach (nazwa i rozmiar w bajtach)			
					//WarstwaPubliczna.Create(txtFieldNazwa.getText(),dane);
					btnZajmijBloki.setEnabled(false);
					
				}
			}
		});
		btnOdwie.setIcon(new ImageIcon(frame.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		btnOdwie.setBackground(new Color(255, 255, 255));
		btnOdwie.setToolTipText("Kliknij by zapisa\u0107 plik.");
		
		txtFieldRozmiar = new JTextField();
		txtFieldRozmiar.setToolTipText("Max: 256");
		txtFieldRozmiar.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Wielko\u015B\u0107 pliku:");
		
		JLabel lblB = new JLabel("B");
		
		JLabel lblNazwaPliku = new JLabel("Nazwa pliku:");
		
		txtFieldNazwa = new JTextField();
		txtFieldNazwa.setToolTipText("Nazwa w formacie 8.3");
		txtFieldNazwa.setColumns(10);
		
		txtFieldZajete = new JTextField();
		txtFieldZajete.setText("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31");
		txtFieldZajete.setToolTipText("Nr zajętych bloków podaj po przecinku. Max: 32.");
		txtFieldZajete.setBackground(new Color(245, 245, 245));
		txtFieldZajete.setForeground(new Color(255, 0, 0));
		txtFieldZajete.setColumns(10);
		
		btnZajmijBloki = new JButton("Zajmij");
		btnZajmijBloki.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				Vector<String> ktoreZajete = KtoreBlokiZajete(txtFieldZajete.getText());
				//Zaznaczamy, które bloki użytkownik początkowo zaznaczył jako zajęte (poprzez txtField)
				ZaznaczPoczatkowoZajete(ktoreZajete);		
				ZaznaczZajeteDane(ktoreZajete);

				//Znalezienie ktore bloki sa wolne
				Vector<String> wolneBloki = new Vector<String>();
				
				for (int i = 0; i < 32; i++) 
					if(!ktoreZajete.contains(String.valueOf(i)))
						wolneBloki.addElement(String.valueOf(i));
				
				//Akcja przekazująca wektor zajetych komórek do klasy tester
				//tester.InitWolneBloki(wolneBloki);
				
			}
		});
		btnZajmijBloki.setIcon(new ImageIcon(frame.class.getResource("/com/sun/java/swing/plaf/windows/icons/HardDrive.gif")));
		btnZajmijBloki.setToolTipText("Kliknij by zapisać plik.");
		btnZajmijBloki.setBackground(new Color(255, 255, 255));
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				//Czyscimy całkowicie FAT DET i DANE
				//tester.Reset();
				WarstwaLogiczna.WyczyscDysk();
				//Wyswietlamy zmiany - wypelniamy tabaele wartosciami 254
				UpdateWidokuPoResecie();	
				btnZajmijBloki.setEnabled(true);
			}
		});
		btnReset.setIcon(new ImageIcon(frame.class.getResource("/javax/swing/plaf/metal/icons/ocean/paletteClose.gif")));
		btnReset.setToolTipText("Kliknij by zwolnic bloki");
		btnReset.setBackground(Color.WHITE);
		
		JSeparator separator = new JSeparator();
		
		lblError = new JLabel("");
		lblError.setIcon(new ImageIcon(frame.class.getResource("/com/sun/java/swing/plaf/windows/icons/Error.gif")));
		lblError.setEnabled(false);
		lblError.setForeground(Color.RED);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(separator, GroupLayout.PREFERRED_SIZE, 313, GroupLayout.PREFERRED_SIZE)
						.addComponent(FATtable_1, GroupLayout.PREFERRED_SIZE, 304, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblNazwaPliku, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
								.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(41)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(txtFieldRozmiar, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblB, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
								.addComponent(txtFieldNazwa, GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)))
						.addComponent(lblTablicaFat)
						.addComponent(lblTablicaDanych, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(DATAtable, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 304, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addComponent(txtFieldZajete)
									.addComponent(lblError))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
									.addComponent(btnZajmijBloki, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnOdwie, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnReset, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)))))
					.addGap(21))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblTablicaFat, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(FATtable_1, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addGap(31)
					.addComponent(lblTablicaDanych)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(DATAtable, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
					.addGap(29)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 4, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNazwaPliku)
						.addComponent(txtFieldNazwa, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(5)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(txtFieldRozmiar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblB, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtFieldZajete, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnZajmijBloki))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(18)
							.addComponent(btnReset)
							.addGap(13)
							.addComponent(btnOdwie, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(33)
							.addComponent(lblError)))
					.addContainerGap(31, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
	}
	

public static Vector<String> KtoreBlokiZajete(String txtFieldZajete)
{
		
		String[] zaj = txtFieldZajete.split(",");
		Vector<String> zajete = new Vector<String>();
		
		for (String i : zaj) 
		{
			zajete.addElement(i);
		}
		
		
		return zajete;		
}

	/**
	 * Na wejściu przyjmowany jest vector z indeksami bloków zajetych w tablicy FAT
	 * Funkcja pokazuje łańcuch pliku w tablicy FAT dodając EOF ozn. koniec pliku
	 */
	public static void PokazLancuchPlikuFAT(Vector<String> lancuchPliku)
	{	
		String eof = "EOF";
		lancuchPliku.add(eof);
		
		TableModel tm = FATtable_1.getModel();
		int column = 0;
		int row = 0;
		
		for (int i = 0; i < lancuchPliku.size() - 1; i++) 
		{
			column = (Integer.valueOf(lancuchPliku.get(i)))%8;
			row = (int) Math.floor((Integer.valueOf(lancuchPliku.get(i)))/8);
			System.out.println("RZAD NR " + row + "KOLUMNA NR " + column);
			tm.setValueAt(lancuchPliku.get(i + 1), row, column);
		}
		
		lancuchPliku.remove(eof);
	}
	
	public static void ZaznaczPoczatkowoZajete(Vector<String> poczZajeteBloki)
	{
		TableModel tm = FATtable_1.getModel();
		int column = 0;
		int row = 0;
		System.out.println("KOM. FAT POCZ. ZAJETE");
		for (String i : poczZajeteBloki) 
		{
				column = (Integer.valueOf(i))%8;
				row = (int) Math.floor((Integer.valueOf(i))/8);
				System.out.println("RZAD NR " + row + " KOLUMNA NR " + column);
				tm.setValueAt("X", row, column);
		}
		
	}
	
	public static void ZaznaczZajeteDane(Vector<String> poczZajeteBloki)
	{
		TableModel tm2 = DATAtable.getModel();
		int column = 0;
		int row = 0;
		
		System.out.println("BLOKI POCZ. ZAJETE");
		for (String i : poczZajeteBloki) 
		{
				column = (Integer.valueOf(i))%8;
				row = (int) Math.floor((Integer.valueOf(i))/8);
				System.out.println("RZAD NR " + row + " KOLUMNA NR " + column);
				tm2.setValueAt("X", row, column);
		}
		
	}
	
	public static void UpdateWidokuPoResecie()
	{
		TableModel tm = FATtable_1.getModel();
		TableModel tm2 = DATAtable.getModel();
		
		int column = 0;
		int row = 0;
		
		for (int i = 0; i < 32; i++) 
		{
			column = (Integer.valueOf(i))%8;
			row = (int) Math.floor((Integer.valueOf(i))/8);
			tm.setValueAt("254", row, column);
			tm2.setValueAt("254", row, column);
		}
		
		txtFieldZajete.setText("");
		lblError.setText("");
		lblError.setEnabled(false);
	}
}
