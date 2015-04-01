import java.util.*;

public class additionClass {

	 LinkedList<String> workQ;
	private boolean done;  // no more directories to be added
	private int size;  // number of directories in the queue

	public additionClass() {
		workQ = new LinkedList<String>();
		done = false;
		size = 0;
	}

	public synchronized void add(String s) {
		workQ.add(s);
		size++;
		notifyAll();
	}

	public synchronized String remove() {
		String s;
		while (!done && size == 0) {
			try {
				wait();
			} catch (Exception e) {};
		}
		if (size > 0) {
			s = workQ.remove();
			size--;
			notifyAll();
		} else
			s = null;
		return s;
	}

	public synchronized void finish() {
		done = true;
		notifyAll();
	}
}