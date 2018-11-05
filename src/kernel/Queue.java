package kernel;

import java.util.ArrayList;

import Pamiec.Debug;

public class Queue {
	private ArrayList<PCB> queue = new ArrayList<PCB>();
	
	public PCB Download() {
		return queue.remove(0);
	}
	
	public void Add(PCB process) {
		queue.add(process);
		Debug.print("Dodano proces " + process.get_PID() + " do kolejki " + queue.get(0).getPriority() + "\n");
	}
	
	boolean isEmpty() {
		if (queue.isEmpty() == true) return true;
		else return false;
	}

	public ArrayList<PCB> getQueue() {
		return queue;
	}
}
