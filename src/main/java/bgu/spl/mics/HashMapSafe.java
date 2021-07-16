package bgu.spl.mics;

import java.util.*;
import java.util.Map.Entry;

public class HashMapSafe<K, V>{
	private HashMap<K,V> hashmap;
	
	public HashMapSafe () {
		hashmap = new HashMap<K, V>();
	}

	public synchronized int  size() {
		return hashmap.size();
	}

	public synchronized boolean isEmpty() {
		return hashmap.isEmpty();
	}

	public  synchronized boolean containsKey(Object key) {
		return hashmap.containsKey(key);
	}

	public synchronized boolean containsValue(Object value) {
		return hashmap.containsValue(value);
	}

	public synchronized V get(Object key) {
		return hashmap.get(key);
	}

	public synchronized V put(K key, V value) {
		return hashmap.put(key, value);
	}

	public synchronized V remove(Object key) {
		return hashmap.remove(key);
	}

	public synchronized void putAll(Map<? extends K, ? extends V> m) {
		hashmap.putAll(m);
	}

	public synchronized void clear() {
		hashmap.clear();
	}

	public synchronized Set<K> keySet() {
		return hashmap.keySet();
	}

	public synchronized Collection<V> values() {
		return hashmap.values();
	}

	public synchronized Set<Entry<K, V>> entrySet() {
		return hashmap.entrySet();
	}

}
