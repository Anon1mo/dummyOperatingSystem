package menu;

import java.util.ArrayList;
import java.util.Scanner;
import javaConsole.JavaConsole;


public class Menu {

	private JavaConsole console;
	
	public Menu(JavaConsole c) {
		console = c;
	}

	private class MenuItem {

		private MenuCallback _mc;
		private String _text;

		/**
		 * @param text - tekst do wyswietlenia
		 * @param mc obiekt MenuCallback 
		 */
		public MenuItem(String text, MenuCallback mc) {
			_mc = mc;
			_text = text;
		}

		/**
		 * @return the MenuCallback object
		 */
		public MenuCallback get_mc() {
			return _mc;
		}

		/**
		 * @return Wyswietlony tekst
		 */
		public String get_text() {
			return _text;
		}

	}

	private ArrayList<MenuItem> Items =
		new ArrayList<MenuItem>();
	
	/**
	 * @param text Co wyswietlic
	 * @param mc MenuCallback
	 * @return boolean prawda = powodzenie
	 */
	public boolean add(String text, MenuCallback mc) {
		return Items.add(new MenuItem(text, mc));
	}

	/**
	 * @brief Wyswietlanie menu
	 */
	public void show() {
		int choosen = 0;
		Scanner in = new Scanner(System.in);

		for (int i = 0; i < Items.size(); ++i) {
			MenuItem mi = Items.get(i);
			System.out.printf(" [%d] %s \n", i + 1, mi.get_text());
		}

		System.out.println(); // add a line

		try {
			choosen = in.nextInt();
		} catch (Exception e1) { /* Ignore non numeric and mixed */ }

		console.clear();

		if (choosen > Items.size() || choosen < 1) {
			System.out.println("Niewlasciwa opcja.\nWcisnij enter by kontynuowac...");
			in.nextLine();
			in.nextLine();
		} else {
			MenuItem mi = Items.get(choosen - 1);
			MenuCallback mc = mi.get_mc();
			mc.Invoke();
		}
	}
}
