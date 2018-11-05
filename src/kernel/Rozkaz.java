package kernel;
 
import Pamiec.Debug;
 
public class Rozkaz {
        public static void dekoduj(PCB cpu, byte[] b) {

          String r;            
                r = "" + (char)b[0] + (char)b[1] + (char)b[2];
                switch(r) {
                        case "ADD":
                                switch(b[3]) {
                                        case (byte)'R':
                                                switch(b[4]) {
                                                        case 0:
                                                                switch(b[5]) {
                                                                        case (byte)'X':
                                                                                        cpu.set_R0(cpu.get_R0() + b[6]);
                                                                                        cpu.zwieksz_licznik();
                                                                        break;
                                                                        case (byte)'R':
                                                                                switch(b[6]) {
                                                                                        case 0:
                                                                                                cpu.set_R0(cpu.get_R0() + cpu.get_R0());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 1:
                                                                                                cpu.set_R0(cpu.get_R0() + cpu.get_R1());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 2:
                                                                                                cpu.set_R0(cpu.get_R0() + cpu.get_R2());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                }
                                                                        break;
                                                                }
                                                        break;
                                                        case 1:
                                                                switch(b[5]) {
                                                                        case (byte)'X':
                                                                                cpu.set_R1(cpu.get_R1() + b[6]);
                                                                                cpu.zwieksz_licznik();
                                                                        break;
                                                                        case (byte)'R':
                                                                                switch(b[6]) {
                                                                                        case 0:
                                                                                                cpu.set_R1(cpu.get_R1() + cpu.get_R0());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 1:
                                                                                                cpu.set_R1(cpu.get_R1() + cpu.get_R1());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 2:
                                                                                                cpu.set_R1(cpu.get_R1() + cpu.get_R2());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                }
                                                                        break;
                                                                }
                                                        break;
                                                        case 2:
                                                                switch(b[5]) {
                                                                        case (byte)'X':
                                                                                        cpu.set_R2(cpu.get_R2() + b[6]);
                                                                                        cpu.zwieksz_licznik();
                                                                        break;
                                                                        case (byte)'R':
                                                                                switch(b[6]) {
                                                                                        case 0:
                                                                                                cpu.set_R2(cpu.get_R2() + cpu.get_R0());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 1:
                                                                                                cpu.set_R2(cpu.get_R2() + cpu.get_R1());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 2:
                                                                                                cpu.set_R2(cpu.get_R2() + cpu.get_R2());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                }
                                                                        break;
                                                                }
                                                        break;
                                                }
                                        break;
                                }
                        break;
                       
                        case "SUB":
                                switch(b[3]) {
                                        case (byte)'R':
                                                switch(b[4]) {
                                                        case 0:
                                                                switch(b[5]) {
                                                                        case (byte)'X':
                                                                                        cpu.set_R0(cpu.get_R0() - b[6]);
                                                                                        cpu.zwieksz_licznik();
                                                                        break;
                                                                        case (byte)'R':
                                                                                switch(b[6]) {
                                                                                        case 0:
                                                                                                cpu.set_R0(cpu.get_R0() - cpu.get_R0());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 1:
                                                                                                cpu.set_R0(cpu.get_R0() - cpu.get_R1());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 2:
                                                                                                cpu.set_R0(cpu.get_R0() - cpu.get_R2());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                }
                                                                        break;
                                                                }
                                                        break;
                                                        case 1:
                                                                switch(b[5]) {
                                                                        case (byte)'X':
                                                                                cpu.set_R1(cpu.get_R1() - b[6]);
                                                                                cpu.zwieksz_licznik();
                                                                        break;
                                                                        case (byte)'R':
                                                                                switch(b[6]) {
                                                                                        case 0:
                                                                                                cpu.set_R1(cpu.get_R1() - cpu.get_R0());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 1:
                                                                                                cpu.set_R1(cpu.get_R1() - cpu.get_R1());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 2:
                                                                                                cpu.set_R1(cpu.get_R1() - cpu.get_R2());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                }
                                                                        break;
                                                                }
                                                        break;
                                                        case 2:
                                                                switch(b[5]) {
                                                                        case (byte)'X':
                                                                                        cpu.set_R2(cpu.get_R2() - b[6]);
                                                                                        cpu.zwieksz_licznik();
                                                                        break;
                                                                        case (byte)'R':
                                                                                switch(b[6]) {
                                                                                        case 0:
                                                                                                cpu.set_R2(cpu.get_R2() - cpu.get_R0());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 1:
                                                                                                cpu.set_R2(cpu.get_R2() - cpu.get_R1());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 2:
                                                                                                cpu.set_R2(cpu.get_R2() - cpu.get_R2());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                }
                                                                        break;
                                                                }
                                                        break;
                                                }
                                        break;
                                }
                        break;
                       
                        case "MUL":
                                switch(b[3]) {
                                        case (byte)'R':
                                                switch(b[4]) {
                                                        case 0:
                                                                switch(b[5]) {
                                                                        case (byte)'X':
                                                                                        cpu.set_R0(cpu.get_R0() * b[6]);
                                                                                        cpu.zwieksz_licznik();
                                                                        break;
                                                                        case (byte)'R':
                                                                                switch(b[6]) {
                                                                                        case 0:
                                                                                                cpu.set_R0(cpu.get_R0() * cpu.get_R0());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 1:
                                                                                                cpu.set_R0(cpu.get_R0() * cpu.get_R1());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 2:
                                                                                                cpu.set_R0(cpu.get_R0() * cpu.get_R2());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                }
                                                                        break;
                                                                }
                                                        break;
                                                        case 1:
                                                                switch(b[5]) {
                                                                        case (byte)'X':
                                                                                cpu.set_R1(cpu.get_R1() * b[6]);
                                                                                cpu.zwieksz_licznik();
                                                                        break;
                                                                        case (byte)'R':
                                                                                switch(b[6]) {
                                                                                        case 0:
                                                                                                cpu.set_R1(cpu.get_R1() * cpu.get_R0());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 1:
                                                                                                cpu.set_R1(cpu.get_R1() * cpu.get_R1());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 2:
                                                                                                cpu.set_R1(cpu.get_R1() * cpu.get_R2());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                }
                                                                        break;
                                                                }
                                                        break;
                                                        case 2:
                                                                switch(b[5]) {
                                                                        case (byte)'X':
                                                                                        cpu.set_R2(cpu.get_R2() * b[6]);
                                                                                        cpu.zwieksz_licznik();
                                                                        break;
                                                                        case (byte)'R':
                                                                                switch(b[6]) {
                                                                                        case 0:
                                                                                                cpu.set_R2(cpu.get_R2() * cpu.get_R0());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 1:
                                                                                                cpu.set_R2(cpu.get_R2() * cpu.get_R1());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                        case 2:
                                                                                                cpu.set_R2(cpu.get_R2() * cpu.get_R2());
                                                                                                cpu.zwieksz_licznik();
                                                                                        break;
                                                                                }
                                                                        break;
                                                                }
                                                        break;
                                                }
                                        break;
                                        case (byte)'X':
                                                               
                                        break;
                                }
                        break;
                       
                        case "MOV":
                                switch(b[4]) {
                                        case 0: // rejestr 0
                                                switch(b[5]) {
                                                        case (byte)'X': // wartosc
                                                                cpu.set_R0(b[6]);
                                                                cpu.zwieksz_licznik();
                                                        break;
                                                        case (byte)'R': // wartosc rejestru
                                                                switch(b[6]) {
                                                                        case 1:
                                                                                cpu.set_R0(cpu.get_R1());
                                                                                cpu.zwieksz_licznik();
                                                                        break;
                                                                        case 2:
                                                                                cpu.set_R0(cpu.get_R2());
                                                                                cpu.zwieksz_licznik();
                                                                        break;
                                                                }
                                                        break; 
                                                }
                                        break;
                                        case 1: // rejestr 1
                                                switch(b[5]) {
                                                        case (byte)'X': // wartosc
                                                                cpu.set_R1(b[6]);
                                                                cpu.zwieksz_licznik();
                                                        break;
                                                        case (byte)'R': // wartosc rejestru
                                                                switch(b[6]) {
                                                                        case 0:
                                                                                cpu.set_R1(cpu.get_R0());
                                                                                cpu.zwieksz_licznik();
                                                                        break;
                                                                        case 2:
                                                                                cpu.set_R1(cpu.get_R2());
                                                                                cpu.zwieksz_licznik();
                                                                        break;
                                                                }
                                                        break;
                                                }
                                        break;
                                        case 2: // rejestr 1
                                                switch(b[5]) {
                                                        case (byte)'X': // wartosc
                                                                cpu.set_R2(b[6]);
                                                                cpu.zwieksz_licznik();
                                                        break;
                                                        case (byte)'R': // wartosc rejestru
                                                                switch(b[6]) {
                                                                        case 0:
                                                                                cpu.set_R2(cpu.get_R0());
                                                                                cpu.zwieksz_licznik();
                                                                        break;
                                                                        case 1:
                                                                                cpu.set_R2(cpu.get_R1());
                                                                                cpu.zwieksz_licznik();
                                                                        break;
                                                                }
                                                        break;
                                                }
                                        break;
                                }
                        break;
                       
                        case "JZ ":
                                switch(b[4]) {
                                        case 0: // rejestr 0
                                                if (cpu.get_R0() == 0) cpu.set_licznik(b[5]);
                                                else cpu.zwieksz_licznik();
                                        break;
                                        case 1: // rejestr 1
                                                if (cpu.get_R1() == 0) cpu.set_licznik(b[5]);
                                                else cpu.zwieksz_licznik();
                                        break;
                                        case 2: // rejestr 2
                                                if (cpu.get_R2() == 0) cpu.set_licznik(b[5]);
                                                else cpu.zwieksz_licznik();
                                        break;
                                }
                        break;
                       
                        case "JNZ":
                                switch(b[4]) {
                                        case 0: // rejestr 0
                                                if (cpu.get_R0() != 0) cpu.set_licznik(b[5]);
                                                else cpu.zwieksz_licznik();
                                        break;
                                        case 1: // rejestr 1
                                                if (cpu.get_R1() != 0) cpu.set_licznik(b[5]);
                                                else cpu.zwieksz_licznik();
                                        break;
                                        case 2: // rejestr 2
                                                if (cpu.get_R2() != 0) cpu.set_licznik(b[5]);
                                                else cpu.zwieksz_licznik();
                                        break;
                                }
                        break;
                       
                        case "END":
                                Debug.println("Koniec procesu o PID: " + cpu.get_PID());
                                cpu.set_state(stan.ZAKONCZONY);
                        break;
                       
                        default:
                                Debug.println("Niepoprawny rozkaz procesu PID: " + cpu.get_PID());
                                cpu.set_state(stan.ZAKONCZONY);
                        break;
                }
                Debug.println("R0 = " + cpu.get_R0() + " R1 = " + cpu.get_R1() + " " + "R2 = "+ cpu.get_R2() + " licznik = " + cpu.get_licznik());
        }
}