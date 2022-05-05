package edu.miracosta.cs113;
import java.util.*;

/**
 * HashTable implementation using chaining to tack a pair of key and value pairs.
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class HashTableChain<K, V> implements Map<K, V>  {

    private LinkedList<Entry<K, V>>[] table ;
    private  int numKeys ;
    private static final int CAPACITY = 101 ;
    private static final double LOAD_THRESHOLD = 1.5 ;

    ///////////// ENTRY CLASS ///////////////////////////////////////

    /**
     * Contains key-value pairs for HashTable
     * @param <K> the key
     * @param <V> the value
     */
    private static class Entry<K, V> implements Map.Entry<K, V>{
        private K key ;
        private V value ;

        /**
         * Creates a new key-value pair
         * @param key the key
         * @param value the value
         */
        public Entry(K key, V value) {
            this.key = key ;
            this.value = value ;
        }

        /**
         * Returns the key
         * @return the key
         */
        public K getKey() {
            return  key;
        }

        /**
         * Returns the value
         * @return the value
         */
        public V getValue() {
            return value ;
        }

        /**
         * Sets the value
         * @param val the new value
         * @return the old value
         */
        public V setValue(V val) {
            V oldVal = value;
            value = val ;
            return oldVal ;
        }
        @Override
        public String toString() {
            return  key + "=" + value  ;
        }



    }

    ////////////// end Entry Class /////////////////////////////////

    ////////////// EntrySet Class //////////////////////////////////

    /**
     * Inner class to implement set view
     */
    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {


        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new SetIterator();
        }

        @Override
        public int size() {
            return numKeys ;
        }
    }

    ////////////// end EntrySet Class //////////////////////////////

    //////////////   SetIterator Class ////////////////////////////

    /**
     * Class that iterates over the table. Index is table location
     * and lastItemReturned is entry
     */
    private class SetIterator implements Iterator<Map.Entry<K, V>> {

        private int index = 0 ;
        private Entry<K,V> lastItemReturned = null;
        private Iterator<Entry<K, V>> iter = null;

        @Override
        public boolean hasNext() {
        	if (iter != null && iter.hasNext()) {
        		return true;
        	}
        	
        	do {
        		index++;
        		if (index >= table.length) {
        			return false;
        		}
        	} while (table[index] == null);
        	iter = table[index].iterator();
        	return iter.hasNext();
        }

        @Override
        public Map.Entry<K, V> next() {
        	if (iter.hasNext()) {
        		lastItemReturned = iter.next();
        		return lastItemReturned;
        	} else {
        		throw new NoSuchElementException();
        	}
        }

        @Override
        public void remove() {
        	if (lastItemReturned == null) {
        		throw new IllegalStateException();
        	} else {
        		iter.remove();
        		lastItemReturned = null;
        	}
        }
    }

    ////////////// end SetIterator Class ////////////////////////////

    /**
     * Default constructor, sets the table to initial capacity size
     */
    public HashTableChain() {
        table = new LinkedList[CAPACITY] ;
    }

    /**
     * Method size returns number of keys.
     * @return The number of keys.
     */
    @Override
    public int size() {
        return numKeys;
    }

    /**
     * Method to check if table is empty.
     * @return Boolean if table has no keys.
     */
    @Override
    public boolean isEmpty() {
    	return numKeys < 1;
    }

    /**
     * Method containsKey checks to see if the table contains entered key.
     * @param key	The key being looked for.
     * @return Boolean true is table contains the key, else returns false.
     */
    @Override
    public boolean containsKey(Object key) {
    	SetIterator iterator = new SetIterator();
    	while (iterator.hasNext()) {
    		if (iterator.next().getKey().equals(key)) {
    			return true;
    		}
    	}
    	
    	return false;
    }

    /**
     * Method containsValue checks to see if the table contains entered value.
     * @param value	The value being looked for.
     * @return Boolean true is table contains the value, else returns false.
     */
    @Override
    public boolean containsValue(Object value) {
    	SetIterator iterator = new SetIterator();
    	while (iterator.hasNext()) {
    		if (iterator.next().getValue().equals(value)) {
    			return true;
    		}
    	}
    	
    	return false;
    }

    /**
     * Method get for class returns value if table has the searched for key.
     * @param key	The key being sought.
     * @return The value associated with this key if found; otherwise, null.
     */
    @Override
    public V get(Object key) {
    	int index = key.hashCode() % table.length;
    	if (index < 0) {
    		index += table.length;
    	}
    	if (table[index] == null) {
    		return null; // Key is not in the table.
    	}
    	
    	// Search the list at table[index] to find the key.
    	for (Entry<K, V> nextItem : table[index]) {
    		if (nextItem.getKey().equals(key)) {
    			return nextItem.getValue();
    		}
    	}
    	
    	// Assert: key is not in the table.
    	return null;
    }
    
    /**
     * Method put adds the key and value pair to the table using hashing. This
     * key-value pair is inserted in the table and numKeys is incremented. If the
     * key is already in the table, its value is changed to the argument value and 
     * numKeys is not changed. If the threshold is exceeded, the table is expanded.
     * @param key	The key of the item being inserted.
     * @param value	The value for this key.
     * @return	Old value associated with this key if found; otherwise, null.
     */
    @Override
    public V put(K key, V value) {
    	int index = key.hashCode() % table.length;
    	
    	if (index < 0) {
    		index += table.length;
    	}
    	if (table[index] == null) {
    		// Create a new linked list at table[index].
    		table[index] = new LinkedList<>();
    	}
    	
    	// Search the list at table[index] to find the key.
    	for (Entry<K, V> nextItem : table[index]) {
    		// If the search is successful, replace the old value.
    		if (nextItem.getKey().equals(key)) {
    			// Replace value for this key.
    			V oldVal = nextItem.getValue();
    			nextItem.setValue(value);
    			return oldVal;
    		}
    	}
    	
    	// Assert: key is not in the table, add new item.
    	table[index].addFirst(new Entry<>(key, value));
    	numKeys++;
    	if (numKeys > (LOAD_THRESHOLD * table.length)) {
    		rehash();
    	}
    	return null;
    }


    /**
     * Expands table size when loadFactor exceeds LOAD_THRESHOLD. Resizes the table 
     * to be 2X +1 bigger than previous. Each nondeleted entry from the original table
     * is reinserted into expanded table. The value of numKeys is reset to the number
     * of items actually inserted; numDeletes is reset to 0.
     */
    private void rehash() {
    	// Save a reference to the oldTable.
    	LinkedList<Entry<K, V>>[] oldTable = table;
    	
    	// Double capacity of this table.
    	table = new LinkedList[(table.length * 2) + 1];
    	
    	// Reinsert all items in oldTable into expanded table.
    	numKeys = 0;
    	
    	for (LinkedList<Entry<K, V>> tableEntry : oldTable) {
    		if (tableEntry != null) {
    			for (Entry e : tableEntry) {
    				if (e != null) {
    					put((K) e.getKey(), (V) e.getValue());
    				}
    			}
    		}
    	}
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder() ;
        for (int i = 0 ; i < table.length ; i++ ) {
            if (table[i] != null) {
                for (Entry<K, V> nextItem : table[i]) {
                    sb.append(nextItem.toString() + " ") ;
                }
                sb.append(" ");
            }
        }
        return sb.toString() ;

    }

    /**
     * Method removes an entry at the location of the entered key.
     * @param key	The key for the entry you want to remove.
     * @return The value of the entry being removed, else returns null.
     */
    @Override
    public V remove(Object key) {
    	SetIterator iterator = new SetIterator();
    	while (iterator.hasNext()) {
    		if (iterator.next().getKey().equals(key)) {
    			Entry<K, V> entry = new Entry<K, V>(iterator.lastItemReturned.getKey(), iterator.lastItemReturned.getValue());
    			iterator.remove();
    			return entry.getValue();
    		}
    	}
    	return null;
    }

    // throws UnsupportedOperationException
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException() ;
    }

    /**
     * Method empties the table by creating a new linked list and setting the number
     * of keys to 0.
     */
    @Override
    public void clear() {
    	table = new LinkedList[CAPACITY];
    	numKeys = 0;
    }

    /**
     * Method.
     * @param key	The key being looked for.
     * @return Boolean true is table contains the key, else returns false.
     */
    @Override
    public Set<K> keySet() {
    	Set<K> setOfKeys = new HashSet<K>(numKeys);
    	
    	for (LinkedList<Entry<K, V>> tableEntry : table) {
    		if (tableEntry != null) {
    			for (Entry e : tableEntry) {
    				setOfKeys.add((K) e.getKey());
    			}
    		}
    	}
    	
    	return setOfKeys;
    }

    // throws UnsupportedOperationException
    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException() ;
    }

    /**
     * Returns a set view of the hash table.
     * @return Set view of hash table.
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
    	return new EntrySet();
    }

    @Override
    public boolean equals(Object o) {
    	if (o instanceof Map) {
    		Map newMap = (Map) o;
    		if (newMap.size() != this.size()) {
    			return false;
    		}
    		
    		for (LinkedList <Entry<K, V>> linkedListInArray : table) {
    			if (linkedListInArray != null) {
    				for (Entry<K, V> entry : linkedListInArray) {
    					if (!(newMap.containsValue(entry.getValue()))) {
    						return false;
    					}
    				}
    			}
    		}
    	}
    	return true;
    }

    @Override
    public int hashCode() {
    	int hash = 1;
    	Set<K> setOfKeys = keySet();
    	for (K k : setOfKeys) {
    		hash += Objects.hashCode(k);
    	}
    	return hash;
    }
}