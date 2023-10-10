package de.comparus.opensource.longmap;

import java.lang.reflect.Array;

/**
 * Implementation of a hash table with keys of type long.
 * Not thread-safe.
 * @param <V> the type of the value
 */
public class LongMapImpl<V> implements LongMap<V> {
    /**
     * The default initial capacity
     */
    static final int DEFAULT_INITIAL_CAPACITY = 16;

    static final float DEFAULT_INITIAL_LOAD_FACTOR = .75F;

    /**
     * A map entry (key-value pair).
     * @param <V> the type of the value
     */
    static class Node<V> {
        long key;
        V value;
        int hash;
        Node<V> next;

        Node(long key, V value, int hash, Node<V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        Node(long key, V value, int hash) {
            this(key, value, hash, null);
        }
    }

    /**
     * The table.
     */
    Node<V>[] table;

    float loadFactor;

    int threshold;

    int capacity;

    private int size = 0;

    /**
     * Ctor.
     */
    public LongMapImpl() {
        // TODO: implement lazy initialization.
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_INITIAL_LOAD_FACTOR);
    }

    public LongMapImpl(int capacity) {
        this(capacity, DEFAULT_INITIAL_LOAD_FACTOR);
    }

    public LongMapImpl(int capacity, float loadFactor) {
        this.loadFactor = loadFactor;
        this.capacity = capacity;
        this.threshold = threshold(capacity);
    }

    /**
     * Puts the specified value with the specified key into this map. If the map contains a value for this key,
     * it will be replaced.
     * @param key the key
     * @param value the value
     * @return the previous value specified by the provided key
     */
    @Override
    public V put(long key, V value) {
        initIfNecessary();
        if ((size + 1) > threshold) resize();
        return put(key, value, hash(key), true);
    }
    private V put(long key, V value, int hash, boolean increaseSize) {
        // Obtain the right bundle by hash code of the key
        int pos = position(hash);
        // If the bundle has no key-value pairs, add current key-value pair here
        if (table[pos] == null) {
            table[pos] = new Node<>(key, value, hash(key));
            if (increaseSize) size++;
            return null;
        } else {
            Node<V> parent = null;
            Node<V> current = table[pos];
            // If the bundle has key-value pair, check all children.
            // If mapping for the current key exist, replace its value with the current value.
            while (current != null) {
                if (current.hash == hash && current.key == key) {
                    V curValue = current.value;
                    current.value = value;
                    return curValue;
                }
                parent = current;
                current = current.next;
            }
            // If mapping for the current key was not found, add a new one in the tail.
            parent.next = new Node<>(key, value, hash(key));
            if (increaseSize) size++;
        }
        return null;
    }

    /**
     * Gets the value by the specified key
     * @param key the key
     * @return the value by the specified key or NULL if there is no value for the key
     */
    @Override
    public V get(long key) {
        if (table == null) return null;
        int hash = hash(key);
        // Obtain the right bundle by hash code of the key
        int pos = position(hash);
        Node<V> current = table[pos];
        // Check all key-value pairs in this bundle.
        while (current != null) {
            if (current.hash == hash && current.key == key) {
                return current.value;
            }
            current = current.next;
        }
        // Return null if nothing was found
        return null;
    }

    /**
     * Removes the value from the map by specified key if it exists.
     * @param key the key
     * @return the value that was removed or NULL in case there was no value for the specified key
     */
    @Override
    public V remove(long key) {
        if (table == null) return null;
        int hash = hash(key);
        // Obtain the right bundle by hash code of the key
        int pos = position(hash);
        Node<V> current = table[pos];
        // If this bundle has no key-value pairs, return null
        if (current == null) return null;
        // Check if the key in this bundle is that one we are looking for. If yes, remove it.
        if (current.hash == hash && current.key == key) {
            table[pos] = current.next;
            size--;
            return current.value;
        }
        // If no, check if this node has any children. If yes, check all of them.
        Node<V> parent = current;
        current = current.next;
        while (current != null) {
            if (current.hash == hash && current.key == key) {
                parent.next = current.next;
                size--;
                return current.value;
            }
            parent = current;
            current = current.next;
        }
        return null;
    }

    /**
     * Returns true if the map contains no key-value pairs
     * @return true if the map contains no key-value pairs
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns true if the map contains mapping for this key
     * @param key the key
     * @return true if the map contains mapping for this key
     */
    @Override
    public boolean containsKey(long key) {
        return get(key) != null;
    }

    /**
     * Returns true if the map contains the specified value
     * @param value the value
     * @return true if the map contains the specified value
     */
    @Override
    public boolean containsValue(V value) {
        if (table == null) return false;
        // Check every bundle if it has a key-value pair
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                // If bundle has a value, then check all possible nodes in this list in case of collision
                Node<V> current = table[i];
                while (current != null) {
                    if (current.value.equals(value)) return true;
                    current = current.next;
                }
            }
        }
        return false;
    }

    /**
     * Returns an array of keys from the map
     * @return an array of keys from the map or NULL if map is empty
     */
    @Override
    public long[] keys() {
        if (table == null || isEmpty()) return null;
        long[] result = new long[size];
        int counter = 0;
        // Check every bundle if it has a key-value pair
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                // If bundle has a value, then check all possible nodes in this list in case of collision
                Node<V> current = table[i];
                while (current != null) {
                    result[counter] = current.key;
                    current = current.next;
                    counter++;
                }
            }
        }
        return result;
    }

    /**
     * Returns an array of values from the map
     * @return an array of values from the map or NULL in case if map is empty
     */
    @Override
    public V[] values() {
        if (table == null) return null;
        if (isEmpty()) return null;
        // To find any value to be used to create the result array.
        // An additional was introduced with purpose to improve performance
        V anyVal = findAny();
        V[] result = (V[]) Array.newInstance(anyVal.getClass(), size);
        int counter = 0;
        // Check every bundle if it has a key-value pair
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                // If bundle has a value, then check all possible nodes in this list in case of collision
                Node<V> current = table[i];
                while (current != null) {
                    result[counter] = current.value;
                    current = current.next;
                    counter++;
                }
            }
        }
        return result;
    }

    /**
     * Returns the number of key-value pairs
     * @return the number of key-value pairs
     */
    @Override
    public long size() {
        return size;
    }

    /**
     * Removes all key-value pairs from the map
     */
    @Override
    public void clear() {
        if (table == null) return;
        size = 0;
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    private void resize() {
        Node<V>[] oldTable = table;
        capacity *= 2;
        threshold *= 2;
        // Create new array with bigger length.
        // Transfer all values from the previous one.
        table = (Node<V>[]) new Node[capacity];
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                Node<V> current = oldTable[i];
                while (current != null) {
                    put(current.key, current.value, current.hash, false);
                    current = current.next;
                }
            }
        }
    }

    /**
     * Returns a hash code for the specified long value.
     * @param val the value to hash
     * @return the hash code for the specified long value.
     */
    int hash(long val) {
        return Long.hashCode(val);
    }

    /**
     * Returns the position in the table for the key by its hash code
     * @param hash hash code of the key
     * @return the position in the table for the specified hash code
     */
    private int position(int hash) {
        return hash%(table.length - 1);
    }

    private V findAny() {
        for (int i = 0; i < table.length; i++) {
            Node<V> current = table[i];
            if (current != null) {
                return current.value;
            }
        }
        return null;
    }

    private int threshold(int capacity) {
        return Math.round(capacity * loadFactor);
    }

    private void initIfNecessary() {
        if (table == null) this.table = (Node<V>[]) new Node[capacity];
    }
}
