package de.comparus.opensource.longmap;

/**
 * ConstHashLongMapImpl extends LongMapImp. It overrides hash method to return some constant value.
 * @param <V> the type of the value
 */
public class ConstHashLongMapImpl<V> extends LongMapImpl<V>{

    @Override
    int hash(long val) {
        return 12;
    }
}
