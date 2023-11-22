package stack_heap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Memory {

	static Logger log = LoggerFactory.getLogger(Memory.class);

	public static void main(String[] args) { // Line 1
		log.info("Tìm hiểu về Stack+Heap Memory!");

		int i=1; // Line 2
		Object obj = new Object(); // Line 3
		Memory mem = new Memory(); // Line 4
		mem.foo(obj); // Line 5

	} // Line 9

	private void foo(Object param) { // Line 6
		String str = param.toString(); //// Line 7
		log.info(str);
	} // Line 8
}
