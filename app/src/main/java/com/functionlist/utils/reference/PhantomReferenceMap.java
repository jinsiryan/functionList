package com.functionlist.utils.reference;

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;

public class PhantomReferenceMap<K, V> extends HashMap<K, V> {

	private static final long serialVersionUID = -6107721000870689899L;

	private HashMap<K, PhantomValue<K, V>> temp;
    private ReferenceQueue<V> queue;

    public PhantomReferenceMap() {
        super();
        this.temp = new HashMap<K, PhantomValue<K, V>>();
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
        PhantomValue<K, V> value = temp.get(key);
        if (value != null) {
            return value.get();
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        PhantomValue<K, V> reference = new PhantomValue<K, V>(key, value, queue);
        temp.put(key, reference);
        clearMap();
        return null;
    }
    @Override
    public V remove(Object key)
    {
        PhantomValue<K, V> srf= temp.remove(key);
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
        PhantomValue<K, V> reference = null;
        while ((reference = (PhantomValue<K, V>) queue.poll()) != null) {
            temp.remove(reference.getKey());
        }
    }

 
 
    public void dispose()
    {
//    	clearMap();
    	temp.clear();
//    	System.gc();
//        System.runFinalization();
    }
}