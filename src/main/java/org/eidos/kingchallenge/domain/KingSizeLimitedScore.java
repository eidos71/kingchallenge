package org.eidos.kingchallenge.domain;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Sets a Limited ConcurrentSkipListSet with a max Size
 * @author eidos71
 *
 * @param <E> Element incoming
 */
@ThreadSafe
public class KingSizeLimitedScore<E> extends ConcurrentSkipListSet<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3740024596713101252L;
	static final Logger LOG = Logger
			.getLogger(ConcurrentSkipListSet.class.getName());
	 private final int maxSize;
	 private   AtomicInteger currentElements;
	 private final Object lock= new Object();
	 private final int levelScope;
	 /**
	  * 
	  * @param pLevelScope
	  * @param maxSize
	  * @param comparator
	  */
	 public KingSizeLimitedScore(int pLevelScope, int maxSize,Comparator<? super E> comparator) {
		   super(comparator);
		   this.levelScope=pLevelScope;
	       this.maxSize=maxSize;
	       this.currentElements =new AtomicInteger(0);
	       
	    }  
	 
	 	/**
	 	 * 
	 	 * @param pLevelScope
	 	 * @param maxSize
	 	 */
	    public KingSizeLimitedScore(int pLevelScope, int maxSize) {
	        this.maxSize = maxSize;
	        this.levelScope=pLevelScope;
		       this.currentElements =new AtomicInteger(0);
	  
	    }
		    public final  boolean remove(Object e) {
	    	boolean isDeleted=  super.remove(e);
	    	 if ( isDeleted) {
	    			this.currentElements.decrementAndGet();
	    	 }
	  
	    	return isDeleted;
	    }
 
	/**
	 * Will check the max status of the element
	 * and return  
	 */
	public final boolean add(E e) {
		if (this.currentElements.get() == maxSize)
			throw new IllegalStateException(
					"List is already at maximum size of " + maxSize);

		boolean isAdded = super.add(e);
		if (isAdded) {
			//We only take care of the existing elements
			this.currentElements.incrementAndGet();
			if (LOG.isLoggable(Level.FINEST))
				LOG.finest(String.format("currentElem:%1$s, LevelScope:%2$s",
						this.currentElements.get(), this.levelScope));
		}
		return isAdded;
	}
	public void clear(){
		this.currentElements.getAndSet(0);
		super.clear();
	
	}
	

	public int getMaxSize() {
		return maxSize;
	}

	public AtomicInteger getCurrentElements() {
		return currentElements;
	}

	public int getLevelScope() {
		return levelScope;
	}
	
	
	
}
