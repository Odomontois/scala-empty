package chat;

/**
 * User: Oleg
 * Date: 06-Jan-17
 * Time: 23:11
 */
public interface ForeachAction<K, V> {

    /**
     * Perform an action for each record of a stream.
     *
     * @param key    the key of the record
     * @param value  the value of the record
     */
    void apply(K key, V value);
}