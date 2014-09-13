package test.eidos.kingchanllenge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.concurrent.ThreadSafe;
@ThreadSafe
public class ListHelper <E>{
	
	public List<E> list=
			Collections.synchronizedList(new ArrayList<E>() );
	public boolean putIfMissing(E element) {
		synchronized (list) {
			boolean missing= !list.contains(element);
			if (missing) list.add(element);
			return missing;
		}
	}
}
