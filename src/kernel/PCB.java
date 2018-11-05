package kernel;
 
import java.util.ArrayList;
import java.util.List;
 
import Pamiec.TablicaStron;
 
public class PCB {
        public static int ilosc_procesow=0;
        public int PID;
        public stan state;
        public int R0,R1,R2,R3;
        public int  licznik;
        public int priorytet_dyn;
        public final int priorytet_baz;
        String nazwa_pliku;
        public static List<PCB> lista_procesow=new ArrayList<PCB>();
        TablicaStron tablica;
        public int czas;
        int kwant_czasu;
       
        public PCB(int priorytet_baz, byte[] fTab) {
                state=stan.NOWY;
                PCB.lista_procesow.add(this);
                ilosc_procesow++;
                PID=ilosc_procesow;
                licznik=0;
                this.priorytet_baz=priorytet_baz;
                //this.nazwa_pliku=nazwa_pliku;
                tablica=new TablicaStron(fTab, PID);
                priorytet_dyn=0;
                state=stan.GOTOWY;
        }
       
        public PCB(int priorytet_baz) {
                state=stan.NOWY;
                PCB.lista_procesow.add(this);
                ilosc_procesow++;
                PID=ilosc_procesow;
                licznik=0;
                this.priorytet_baz=priorytet_baz;
                this.priorytet_dyn=0;
                state=stan.GOTOWY;
        }
       
        public static void usun(PCB e)  {
            lista_procesow.remove(e);
        }
       
        public void set_time(int czas) {
                this.czas=czas;
        }
       
        public int get_czas() {
                return czas;   
        }
       
        public int get_licznik() {
                return licznik;
        }
       
        public void set_licznik(int wartosc) {
                licznik=wartosc;
        }
       
        public void zwieksz_licznik() {
                licznik++;
        }
       
        public int get_R0() {
                return R0;     
        }
       
        public void set_R0(int wartosc) {
                R0=wartosc;
        }
       
        public int get_R1() {
                return R1;     
        }
       
        public void set_R1(int wartosc) {
                R1=wartosc;
        }
       
        public int get_R2() {
                return R2;     
        }
       
        public void set_R2(int wartosc) {
                R2=wartosc;
        }
       
        public void Set_Priority_Class(int priorytet) {
                if(priorytet<priorytet_baz)
                        priorytet_dyn=priorytet_baz;
                else
                        priorytet_dyn=priorytet;
        }
       
        public void set_state(stan nowy_stan) {
                state=nowy_stan;
        }
       
        public int get_PID() {
                return PID;
        }
       
        public int getPriorytetBaz() {
                return priorytet_baz;
        }
       
        public int getPriorytetDyn() {
                return priorytet_dyn;
        }
       
        public void setPriorytetDyn(int p) {
                priorytet_dyn = p;
        }
       
        public int getPriority() {
                return priorytet_baz + priorytet_dyn;
        }
       
        public void setKwantCzasu(int k) {
                kwant_czasu = k;
        }
       
        public int getKwantCzasu() {
                return kwant_czasu;
        }
       
        public TablicaStron getTablica() {
                return tablica;
        }
       
        public stan get_state() {
                return state;
        }
}