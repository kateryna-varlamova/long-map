package de.comparus.opensource.longmap;

import org.junit.After;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LongMapImplTest {
    private static final long KEY_1 = 1L;
    private static final long KEY_2 = 2L;
    private static final String VALUE_1 = "Value1";

    private static final String VALUE_2 = "Value2";

    private final LongMap<String> map = new LongMapImpl<>();

    @After
    public void clear() {
        map.clear();
    }

    @Test
    public void testPutGetValue() {
        map.put(KEY_1, VALUE_1);
        assertEquals(1, map.size());
        assertEquals(VALUE_1, map.get(KEY_1));
    }

    @Test
    public void testPutValue_overridePreviousValue() {
        map.put(KEY_1, VALUE_1);
        String prevVal = map.put(KEY_1, VALUE_2);
        assertEquals(1, map.size());
        assertEquals(VALUE_1, prevVal);
        assertEquals(VALUE_2, map.get(KEY_1));
    }

    @Test
    public void testPutValue_collision() {
        long count = 5L;
        LongMap<String> map = prepareMapCollision(count);
        for (long l = 0L; l < count; l++) {
            assertEquals(VALUE_1, map.get(l));
        }
    }

    @Test
    public void testGet_nonExist() {
        map.put(KEY_1, VALUE_1);
        assertNull(map.get(KEY_2));
    }

    @Test
    public void testRemoveValue() {
        map.put(KEY_1, VALUE_1);
        String removed = map.remove(KEY_1);
        assertEquals(VALUE_1, removed);
        assertEquals(0, map.size());
    }

    @Test
    public void testRemoveValue_nonExist() {
        map.put(KEY_1, VALUE_1);
        String removed = map.remove(KEY_2);
        assertEquals(1, map.size());
        assertNull(removed);
    }

    @Test
    public void testRemoveValue_collision_first() {
        testRemoveValue_collision(5L, 0L);
    }

    @Test
    public void testRemoveValue_collision_last() {
        long count = 5L;
        testRemoveValue_collision(count, count - 1L);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(map.isEmpty());
        map.put(KEY_1, VALUE_1);
        assertFalse(map.isEmpty());
    }

    @Test
    public void testContainsKey() {
        map.put(KEY_1, VALUE_1);
        assertTrue(map.containsKey(KEY_1));
        assertFalse(map.containsKey(KEY_2));
    }

    @Test
    public void testContainsValue() {
        map.put(KEY_1, VALUE_1);
        assertTrue(map.containsValue(VALUE_1));
        assertFalse(map.containsValue(VALUE_2));
    }

    @Test
    public void testKeysValues() {
        testKeysValues(map);
    }

    @Test
    public void testKeysValues_collision() {
        testKeysValues(new ConstHashLongMapImpl<>());
    }

    @Test
    public void testEmptyMap() {
        LongMap<String> map = new LongMapImpl<>();
        map.put(KEY_1, VALUE_1);
        map.clear();
        assertNull(map.get(KEY_1));
        assertNull(map.remove(KEY_1));
        assertFalse(map.containsKey(KEY_1));
        assertFalse(map.containsValue(VALUE_2));
        assertNull(map.keys());
        assertNull(map.values());
    }

    @Test
    public void testNewMap() {
        LongMap<String> map = new LongMapImpl<>();
        assertNull(map.get(KEY_1));
        assertNull(map.remove(KEY_1));
        assertFalse(map.containsKey(KEY_1));
        assertFalse(map.containsValue(VALUE_2));
        assertNull(map.keys());
        assertNull(map.values());
    }

    @Test
    public void testResizing() {
        LongMapImpl<String> map = new LongMapImpl<>();
        // The default threshold is 12. To force resizing at least 13 items should be added.
        int count = 13;

        long[] expectedKeys = new long[count];
        // put item to init map
        map.put(0, VALUE_1);
        expectedKeys[0] = 0;
        assertEquals(16, map.table.length);
        for (int i = 1; i < count; i++) {
            expectedKeys[i] = i;
            map.put(i, VALUE_1);
        }
        assertEquals(32, map.table.length);
        long[] actualKeys = map.keys();
        Arrays.sort(actualKeys);
        assertArrayEquals(expectedKeys, actualKeys);
    }

    private LongMap<String> prepareMapCollision(long count) {
        LongMap<String> map = new ConstHashLongMapImpl<>();
        for (long l = 0L; l < count; l++) {
            map.put(l, VALUE_1);
        }
        assertEquals(count, map.size());
        return map;
    }

    private void testRemoveValue_collision(long count, long position) {
        LongMap<String> map = prepareMapCollision(count);
        String removed = map.remove(position);
        assertEquals(VALUE_1, removed);
        assertEquals((count - 1L), map.size());
    }

    private void testKeysValues(LongMap<String> map) {
        String[] expectedValues = {"Val1", "Val2", "Val3", "Val4"};
        long[] expectedKeys = {1L, 23L, 590L, 6734L};
        for (int i = 0; i < expectedValues.length; i++) {
            map.put(expectedKeys[i], expectedValues[i]);
        }
        String[] actualValues = map.values();
        Arrays.sort(actualValues);
        long[] actualKeys = map.keys();
        Arrays.sort(actualKeys);
        assertArrayEquals(expectedValues, actualValues);
        assertArrayEquals(expectedKeys, actualKeys);
    }
}
