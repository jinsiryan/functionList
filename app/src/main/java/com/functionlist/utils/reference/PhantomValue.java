package com.functionlist.utils.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

class PhantomValue<K, V> extends PhantomReference<V> {
    private K key;
    private int nSize = 0;

    public PhantomValue(K k, V v, ReferenceQueue<? super V> q) {
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