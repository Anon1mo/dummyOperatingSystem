package kernel;
 
import Pamiec.Debug;
import Pamiec.Memory;
 
public class Kernel {
        // przerwania
        static int interruption = 0;
        static PCB IntPCB = null;
       
        public static void Int(int key, PCB p) {
                interruption = key;
                if (p != null) IntPCB = p;
        }
       
        // aktualny proces
        static PCB CPU;
       
        // kolejka procesów gotowych
        static Queue[] ReadyQueue = new Queue[16];
       
        // wektor kolejek gotowych
        static boolean[] VecReadyQueue = new boolean[16];
               
        // aktualizacja czasu oczekiwania w kolejce
        static void UpdateTimeAllProcesses() {
                for (int i=15; i>=0; i--) {
                        if (VecReadyQueue[i] == true){
                                for (PCB p : ReadyQueue[i].getQueue())
                                    p.set_time(p.get_czas() + 1);
                        }
                }
        }
       
        //Szukanie gotowego procesu do procesora
        static PCB FindReadyProcess() {
                for (int i=15; i>=0; i--) {
                        if (VecReadyQueue[i] == true){
                                PCB tmp = ReadyQueue[i].Download(); // pobranie z kolejki
                                tmp.setKwantCzasu(2); // kwant czasu
                                tmp.set_state(stan.WYKONYWANY); //zmiana stanu
                                if (ReadyQueue[i].isEmpty() == true) {
                                        VecReadyQueue[i] = false;
                                        Debug.print("Kolejka " + i + " pusta\n");
                                }
                                Debug.print("Znaleziono kolejny proces o PID: " + tmp.get_PID() + "\n");
                                return tmp;
                        }
                }
                return null; // jakis blad
        }
       
        static void ReadyProcess(PCB p) {
                if (CPU != null) {
                        if (p.getPriority() > CPU.getPriority()) {
                                CPU.set_state(stan.GOTOWY); // zmiana stanu na gotowy
                                CPU.set_time(0); // reset czasu oczekiwania
                                ReadyQueue[CPU.getPriority()].Add(CPU); // dodanie aktualnego procesu do kolejki
                                VecReadyQueue[CPU.getPriority()] = true; // wektor true dla watku wywalonego z CPU
                                Debug.print("Wywlaszczono proces PID " + CPU.get_PID() + " przez proces "+ p.get_PID() + "\n");
                                p.setKwantCzasu(2); // kwant czasu
                                p.set_state(stan.WYKONYWANY); //zmiana stanu
                                CPU = p; // uruchomienie nowego procesu
                        } else {
                                ReadyQueue[p.getPriority()].Add(p); // dodanie procesu do kolejki
                                VecReadyQueue[p.getPriority()] = true; // wektor true
                        }
                } else {
                        p.set_state(stan.WYKONYWANY);
                        CPU = p;
                }
        }
       
        // sprawdzanie max priorytetu gotowych procesów
        static int FindMaxPriority() {
                for (int i=15; i>0; i--) {
                        if (VecReadyQueue[i] == true) return i;
                }
                return 0;
        }
       
        // Sprawdzanie glodzonych procesow
        static boolean UpdateStarvedProcesses(int from, int m) {
                boolean ret = false;
                for (int i=1; i<16; i++){
                        check_next:
                        if (VecReadyQueue[i] == true){
                                for (PCB p : ReadyQueue[i].getQueue()) {
                                        if (p.get_czas() >= from) {
                                                p.setPriorytetDyn(p.getPriorytetDyn() + m); // dodanie modyfikatora
                                                if (p.getPriority() > 15) p.setPriorytetDyn(15 - p.getPriorytetBaz()); // zapobieganie priorytetowi powy¿ej 15
                                                p.set_time(0); // zerowanie czasu oczekiwania w nowym prorytecie
                                                ReadyQueue[i].getQueue().remove(p); // usunicie ze starej kolejki
                                                if (ReadyQueue[i].isEmpty() == true) VecReadyQueue[i] = false;
                                                ReadyQueue[p.getPriority()].Add(p); // dodanie do nowej kolejki
                                                VecReadyQueue[p.getPriority()] = true;
                                                Debug.println("Zwiekszono prorytet procesowi " + p.get_PID());
                                                ret = true;
                                                break check_next;
                                        }
                                }
                        }
                }
                return ret;
        }
       
        public static void cpumain() throws Exception {
                // debuger
                Debug.init();
               
                // zerowanie wektorów na starcie...
                for (int i=0; i<16; i++) {
                        VecReadyQueue[i] = false;
                }
               
                // zerowanie kolejek na starcie...
                for(int i=0; i<16; i++) {
                     ReadyQueue[i] = new Queue();
                }
               
                // proces bezczynnoci
                PCB zero = new PCB(0);
                ReadyProcess(zero);
               
                while(true) {
                    while (interruption == 0) { // jeli nie ma przerwania
                                if (CPU != null) {
                                        // proces bezczynnsci------------------------------
                                        if (CPU.getPriority() == 0) {
                                                Debug.print("Proces bezczynnosci\n");
                                                CPU.setKwantCzasu(CPU.getKwantCzasu()-1);
                                               
                                                if (CPU.getKwantCzasu() == 0) { // jesli skonczyl sie kwant czasu dla procesu
                                                        if (FindMaxPriority() > 0) {
                                                                CPU.set_state(stan.GOTOWY); // zmiana statusu na gotowy
                                                                CPU.set_time(0); // reset czasu oczekiwania
                                                                ReadyQueue[CPU.getPriority()].Add(CPU); // dodanie aktualnego procesu do kolejki
                                                                VecReadyQueue[CPU.getPriority()] = true; // wektor true dla watku wywalonego z CPU
                                                                CPU = FindReadyProcess(); // wyszukanie kolejnego procesu do cpu
                                                        } else {
                                                                CPU.setKwantCzasu(2);
                                                        }
                                                }
                                                Thread.sleep(1000);
                                               
                                        } else {
                                        //----------------------------koniec procesu bezczynnosci-------
                                                Debug.print("Takt CPU - proces PID " + CPU.get_PID() + "\n");
                                                CPU.setKwantCzasu(CPU.getKwantCzasu()-1); // zmniejszenie kwantu czasu
                                                Rozkaz.dekoduj(CPU, Memory.czytaj_proces(CPU.getTablica(), CPU.get_licznik())); // wykonywanie rozkazu
                                                UpdateTimeAllProcesses(); // aktualizacja czasu pozostalych procesow w kolejce
                                                Thread.sleep(1000);
                                                if (CPU.get_state() == stan.ZAKONCZONY) { // jesli proces wszystko wykonal,zakonczyl prace
                                                        Debug.print("Zakonczono proces " + CPU.get_PID() + "\n");
                                                        CPU.getTablica().zapiszWynik(CPU.get_R0());
                                                        CPU.getTablica().SprawdzWynik();
                                                        CPU.getTablica().wypisz_z_pamieci(); // usuwanie z pamieci
                                                        PCB.usun(CPU); // usuwanie z listy wszytskich PCB
                                                        CPU = FindReadyProcess();// szukaj nowego
                                                } else if (CPU.getKwantCzasu() == 0) { // jesli skonczyl sie kwant czasu dla procesu
                                                        if (VecReadyQueue[CPU.getPriority()] == true) { // jesli istnieja inne procesy o tym samym prorytecie
                                                                CPU.set_state(stan.GOTOWY); // zmiana statusu na gotowy
                                                                CPU.set_time(0); // reset czasu oczekiwania
                                                                CPU.setPriorytetDyn(0); // przywrócenie priorytetu bazowego
                                                                ReadyQueue[CPU.getPriority()].Add(CPU); // dodanie aktualnego procesu do kolejki
                                                                VecReadyQueue[CPU.getPriority()] = true; // wektor true dla watku wywalonego z CPU
                                                                CPU = FindReadyProcess(); // wyszukanie kolejnego procesu do cpu
                                                        } else { // jesli to jedyny proces o takim priorytecie
                                                                CPU.setKwantCzasu(2); // przedluz czas wykonywania o kolejny kwant czasu
                                                                Debug.print("Zwiekszono kwant czasu aktualnemu watkowi o priorytecie " + CPU.getPriority() + "\n");
                                                        }
                                                }
                                                // doadowanie glodzonych procesow
                                                if(UpdateStarvedProcesses(4, 15)) {
                                                        // wywlaszczenie
                                                        if (FindMaxPriority() > CPU.getPriority()) {
                                                                CPU.set_state(stan.GOTOWY); // zmiana statusu na gotowy
                                                                CPU.set_time(0); // reset czasu oczekiwania
                                                                ReadyQueue[CPU.getPriority()].Add(CPU); // dodanie aktualnego procesu do kolejki
                                                                VecReadyQueue[CPU.getPriority()] = true; // wektor true dla watku wywalonego z CPU
                                                                Debug.println("Wywlaszczono proces o PID: " + CPU.get_PID() + "przez glodzony proces");
                                                                CPU = FindReadyProcess(); // wyszukanie kolejnego procesu do cpu
                                                                CPU.setKwantCzasu(CPU.getKwantCzasu() * 2); // podwojenie kwantu czasu
                                                        }
                                                }
                                        }
                                }
                    }
                    // jesli mamy przerwanie:
                    if (interruption == 1) { // przerwanie - dodanie nowego programu
                            Debug.print("Przerwanie 0x01 - add process\n");
                           
                                ReadyProcess(IntPCB);
                            //IntPCB.getTablica().alokujProces();
                                //Memory.listaT.add(IntPCB.getTablica());
                                IntPCB = null;
                    }
                    interruption = 0; // zerowanie prerwania
                }
        }
}