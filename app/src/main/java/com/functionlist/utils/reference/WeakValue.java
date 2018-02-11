package com.functionlist.utils.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeakValue<K, V> extends WeakReference<V> {
    private K key;
    private int nSize = 0;

    public WeakValue(K k, V v, ReferenceQueue<? super V> q) {
        super(v, q);
        this.key = k;
    }

	public K getKey() {
		return key;
	}

	public int getSize() {
		return nSize;
	}

	public void setSize(int nSize) {
		this.nSize = nSize;
	}
	
    
}