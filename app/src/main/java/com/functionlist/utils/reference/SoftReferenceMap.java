package com.functionlist.utils.reference;

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;

public class SoftReferenceMap<K, V> extends HashMap<K, V> {
    
	private static final long serialVersionUID = -6107721000870689899L;
	
	private HashMap<K, SoftValue<K, V>> temp;
    private ReferenceQueue<V> queue;
 
    public SoftReferenceMap() {
        super();
        this.temp = new HashMap<K, SoftValue<K, V>>();
        queue = new ReferenceQueue<V>();
    }
 
    @Override
    public boolean containsKey(Object key) {
        clearMap();
        return temp.containsKey(key);
    }
 
    @Override
    public V get(Object key) {
        clearMap();
        SoftValue<K, V> softValue = temp.get(key);
        if (softValue != null) {
            return softValue.get();
        }
 
        return null;
    }
 
    @Override
    public V put(K key, V value) {
        SoftValue<K, V> softReference = new SoftValue<K, V>(key, value, queue);
        temp.put(key, softReference);
        clearMap();
        return null;
    }
    @Override
    public V remove(Object key)
    {
    	SoftValue<K, V> srf= temp.remove(key);
    	if(srf != null)
    	{
    		return srf.get();
    	}
    	else
    	{
    		return null;
    	}
    }
 
    @SuppressWarnings("unchecked")
    private void clearMap() {
        SoftValue<K, V> softReference = (SoftValue<K, V>) queue.poll();
        while (softReference != null) {
            temp.remove(softReference.getKey());
            softReference = (SoftValue<K, V>) queue.poll();
        }
    }
 
 
    public void dispose()
    {
    	clearMap();
    	temp.clear();
    	System.gc();
        System.runFinalization();
    }
}