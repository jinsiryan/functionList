package com.functionlist.utils.reference;

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;

public class WeakReferenceMap<K, V> extends HashMap<K, V> {

	private static final long serialVersionUID = -6107721000870689899L;

	protected HashMap<K, WeakValue<K, V>> temp;
    protected ReferenceQueue<V> queue;

    public WeakReferenceMap() {
        super();
        this.temp = new HashMap<K, WeakValue<K, V>>();
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
        WeakValue<K, V> value = temp.get(key);
        if (value != null) {
            return value.get();
        }
 
        return null;
    }
 
    @Override
    public V put(K key, V value) {
        WeakValue<K, V> reference = new WeakValue<K, V>(key, value, queue);
        temp.put(key, reference);
        clearMap();
        return null;
    }
    @Override
    public V remove(Object key)
    {
        WeakValue<K, V> srf= temp.remove(key);
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
        WeakValue<K, V> reference = null;
        while ((reference = (WeakValue<K, V>) queue.poll()) != null) {
            temp.remove(reference.getKey());
        }
    }

 
 
    public void dispose()
    {
    	clearMap();
    	temp.clear();
        System.runFinalization();
    }
}