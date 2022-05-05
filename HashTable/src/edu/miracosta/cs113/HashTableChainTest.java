package edu.miracosta.cs113;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * HashTableChainTest : Tester class for a hash table implementation which utilizes chaining. As a class which extends
 * Java's Map, HashTableChain will be assessed for its functionality through its implementation of this interface.
 *
 * @author King
 * @version 1.0
 */
public class HashTableChainTest {

    /** Map implementation using integer values with String keys. */
    private Map<String, Integer> hashTable;

    /** Helper method using Map's put() to place a given number of unique key-value pairs into this Map. */
    private void populateMapWithPut(int numElements) {
        for (int i = 0; i < numElements; i++) {
            hashTable.put(Integer.toString(i), i);
        }
    }

    /** Helper method to place elements with unique key values and duplicate values based on the above method. */
    private void addDuplicateValues(int numElements) {
        for (int i = 0; i < numElements; i++) {
            hashTable.put(Integer.toString(i) + "k", i);
        }
    }

    @Before
    public void setup() {
        hashTable = new HashTableChain<String, Integer>();
    }

    // region Map tests
    // The following tests assess the functionality of your Map implementation,
    // with exception to the putAll() and values() methods.

    @Test
    public void testIsEmptyTrue() {
        // Returns true if this map contains no key-value mappings.
        assertTrue(hashTable.isEmpty());
    }

    @Test
    public void testIsEmptyFalse() {
        populateMapWithPut(20);
        assertFalse(hashTable.isEmpty());
    }

    @Test
    public void testClear() {
        // Removes all of the mapping from this map
        populateMapWithPut(12);
        hashTable.clear();

        // Validate with isEmpty
        assertTrue(hashTable.isEmpty());
    }

    @Test
    public void testContainsKeyTrue() {
        // Returns true if this map contains a mapping for the specified key.
        populateMapWithPut(5);
        assertTrue(hashTable.containsKey("3"));
    }

    @Test
    public void testContainsKeyFalse() {
        populateMapWithPut(301);
        assertFalse(hashTable.containsKey("303"));
    }

    @Test
    public void testContainsValueTrue() {
        // Returns true if this map maps one or more keys to the specified value.
        populateMapWithPut(99);
        assertTrue(hashTable.containsValue(33));
    }

    @Test
    public void testContainsValueFalse() {
        populateMapWithPut(12);
        assertFalse(hashTable.containsValue(13));
    }

    @Test
    public void testEntrySet() {
        // Returns a Set view of the mappings contained in this map.
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        hashTable.put("three", 3);

        // Because order is arbitrary, expect one of the following outputs from this Set's toString
        String[] expectedSets = { "[one=1, two=2, three=3]", "[one=1, three=3, two=2]", "[two=2, one=1, three=3]",
                                "[two=2, three=3, one=1]", "[three=3, one=1, two=2]", "[three=3, two=2, one=1]" };

        String setString = hashTable.entrySet().toString();

        boolean validSet = setString.equals(expectedSets[0]) || setString.equals(expectedSets[1]) ||
                setString.equals(expectedSets[2]) || setString.equals(expectedSets[3]) ||
                setString.equals(expectedSets[4]) || setString.equals(expectedSets[5]);

        assertTrue("Test entrySet failed - invalid value returned by Set's toString: " + setString, validSet);
    }

    @Test
    public void testKeySet() {
        // Returns a Set view of the keys contained in this map.
        hashTable.put("one", 44);
        hashTable.put("two", 203);
        hashTable.put("three", 1020);

        // Because order is arbitrary, expect one of the following outputs from this key set's toString
        String[] expectedSets = { "[one, two, three]", "[one, three, two]", "[two, one, three]",
                "[two, three, one]", "[three, one, two]", "[three, two, one]" };

        String setString = hashTable.keySet().toString();

        boolean validSet = setString.equals(expectedSets[0]) || setString.equals(expectedSets[1]) ||
                setString.equals(expectedSets[2]) || setString.equals(expectedSets[3]) ||
                setString.equals(expectedSets[4]) || setString.equals(expectedSets[5]);

        assertTrue("Test keySet failed - invalid value returned by Set's toString: " + setString, validSet);
    }

    @Test
    public void testEqualsTrue() {
        // Compares the specified object with this map for equality.
        populateMapWithPut(13);

        Map<String, Integer> other = new Hashtable<String, Integer>();
        for (int i = 0; i < 13; i ++) {
            other.put(Integer.toString(i), i);
        }

        assertTrue(hashTable.equals(other));
    }

    @Test
    public void testEqualsFalse() {
        populateMapWithPut(30);

        Map<String, Integer> other = new Hashtable<String, Integer>();
        for (int i = 0; i < 29; i ++) {
            other.put(Integer.toString(i), i);
        }
        other.put("thirty", 30);

        assertFalse(hashTable.equals(other));
    }

    @Test
    public void testGet() {
        // Returns the value to which the specified key is mapped, or null...
        populateMapWithPut(331);
        assertEquals(new Integer(120), hashTable.get("120"));
    }

    @Test
    public void testGetNull() {
        populateMapWithPut(6);
        assertEquals(null, hashTable.get("no such key"));
    }

    @Test
    public void testHashCode() {
        // Returns the hash code value for this map.
        hashTable.put("one", 1);

        Map<String, Integer> other = new Hashtable<String, Integer>();
        other.put("one", 1);

        assertEquals(other.hashCode(), hashTable.hashCode());
    }

    @Test
    public void testRemove() {
        // Removes the mapping for a key from this map if it is present.
        populateMapWithPut(310);

        hashTable.remove("74");
        assertFalse(hashTable.containsKey("74"));
    }

    @Test
    public void testRemoveValue() {
        populateMapWithPut(13);

        Integer removedValue = hashTable.remove("10");
        assertFalse(hashTable.containsValue(removedValue));
    }

    @Test
    public void testRemoveEmpty() {
        assertEquals(null, hashTable.remove("5"));
    }

    @Test
    public void testSize() {
        populateMapWithPut(33);
        assertEquals("Test size 1 failed - invalid number of entries.", 33, hashTable.size());
    }

    @Test
    public void testSizeEmpty() {
        // Returns the number of key-value mappings in this map.
        // Empty map
        assertEquals("Test size 2 failed - empty map.", 0, hashTable.size());
    }
    @Test
    public void testSizeLarge() {
        populateMapWithPut(320);
        assertEquals("Test size 3 failed - invalid number of entries.", 320, hashTable.size());
    }

    @Test
    public void testSizeDuplicateValues() {
        // Add 40 additional elements
        populateMapWithPut(263);
        addDuplicateValues(40);
        assertEquals("Test size 4 failed - invalid number of entries.", 303, hashTable.size());
    }

    @Test
    public void testSizeCollisions() {
        // Attempt to add 10 values whose keys already exist within this map
        populateMapWithPut(300);
        addDuplicateValues(5);
        populateMapWithPut(10);
        assertEquals("Test size 5 failed - collisive key entries have been added.", 305, hashTable.size());
    }

    // endregion Map tests
    // region SetIterator tests

    // TODO: "

    // endregion SetIterator tests

} // End of class HashTableChainTest
