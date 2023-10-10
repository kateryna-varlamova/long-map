package de.comparus.opensource.longmap;

/**
 * Implementation of a hash table with keys of type long
 * @param <V> the type of the value
 */
public class LongMapImpl<V> implements LongMap<V> {

    /**
     * The default initial capacity
     */
    static final int DEFAULT_INITIAL_CAPACITY = 16;

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

    private long size = 0;

    /**
     * Ctor.
     */
    public LongMapImpl() {
        // TODO: implement lazy initialization.
        // TODO: add load factor
        this.table = (Node<V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
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
        //TODO: create only if needed
        return put(key, value, hash(key));
    }
    private V put(long key, V value, int hash) {
        int pos = position(hash);
        if (table[pos] == null) {
            table[pos] = new Node<>(key, value, hash(key));
            size++;
            return null;
        } else {
            Node<V> parent = null;
            Node<V> current = table[pos];
            while (current != null) {
                if (current.hash == hash && current.key == key) {
                    V curValue = current.value;
                    current.value = value;
                    return curValue;
                }
                parent = current;
                current = current.next;
            }
            parent.next = new Node<>(key, value, hash(key));
            size++;
        }
        return null;
    }

    /**
     * Gets the value by the specified key
     * @param key the key
     * @return the value by the specified key
     */
    @Override
    public V get(long key) {
        int hash = hash(key);
        int pos = position(hash);
        Node<V> current = table[pos];
        while (current != null) {
            if (current.hash == hash && current.key == key) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Removes the value from the map by specified key if it exists.
     * @param key the key
     * @return the value that was removed or NULL in case there was no value for the specified key
     */
    @Override
    public V remove(long key) {
        int hash = hash(key);
        int pos = position(hash);
        Node<V> current = table[pos];
        if (current == null) return null;
        if (current.hash == hash && current.key == key) {
            table[pos] = current.next;
            size--;
            return current.value;
        }
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
        return false;
    }

    /**
     * Returns an array of keys from the map
     * @return an array of keys from the map
     */
    @Override
    public long[] keys() {
        return null;
    }

    /**
     * Returns an array of values from the map
     * @return an array of values from the map
     */
    @Override
    public V[] values() {
        return null;
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
        size = 0;
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
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
}
