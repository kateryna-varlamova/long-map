package de.comparus.opensource.longmap;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LongMapImplTest {
    private static final long KEY_1 = 1L;
    private static final long KEY_2 = 2L;
    private static final String VALUE_1 = "Value1";

    private static final String VALUE_2 = "Value2";

    private final LongMap<String> map = new LongMapImpl<>();

    @After
    public void clear() {
        //NB! Not implemented yet.
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
        assertEquals(1, map.size());
        assertEquals(VALUE_1, map.get(KEY_1));
        String prevVal = map.put(KEY_1, VALUE_2);
        assertEquals(1, map.size());
        assertEquals(VALUE_1, prevVal);
        assertEquals(VALUE_2, map.get(KEY_1));
    }

    @Test
    public void testPutValue_collision() {
        LongMap<String> map = new ConstHashLongMapImpl<>();
        for (long l = 0L; l < 10L; l++) {
            map.put(l, VALUE_1);
        }
        assertEquals(10, map.size());
        for (long l = 0L; l < 10L; l++) {
            assertEquals(VALUE_1, map.get(l));
        }
    }

    @Test
    @Ignore
    public void testRemoveValue() {
        //TODO: implement
    }

    @Test
    @Ignore
    public void testIsEmpty() {
        //TODO: implement
    }

    @Test
    public void testContainsKey() {
        map.put(KEY_1, VALUE_1);
        assertTrue(map.containsKey(KEY_1));
        assertFalse(map.containsKey(KEY_2));
    }

    @Test
    @Ignore
    public void testContainsValue() {
        //TODO: implement
    }

    @Test
    @Ignore
    public void testKeys() {
        //TODO: implement
    }

    @Test
    @Ignore
    public void testValues() {
        //TODO: implement
    }

    @Test
    @Ignore
    public void testResizing() {
        //TODO: implement
    }
}
