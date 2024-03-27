package ru.urfu.saveUtil;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Map;
import java.util.Set;

/**
 * Словарь с префиксом для хранения данных об определённом объекте
 * @param <K> - Тип ключа
 * @param <V> - Тип значения
 */
public class SubDictionary<K, V> extends AbstractMap<K, V> {

    private final Map<K, V> parentMap;
    private final String prefix;

    public SubDictionary(Map<K, V> parentMap, String prefix) {
        this.parentMap = parentMap;
        this.prefix = prefix;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<Entry<K, V>>() {
            @Override
            public int size() {
                return (int) parentMap.keySet().stream()
                        .filter(key -> key.toString().startsWith(prefix))
                        .count();
            }

            @Override
            public boolean contains(Object o) {
                if (o instanceof Map.Entry) {
                    Entry<K, V> entry = (Entry<K, V>) o;
                    return parentMap.containsKey(entry.getKey()) && entry.getValue().equals(parentMap.get(entry.getKey()));
                }
                return false;
            }

            @Override
            public java.util.Iterator<Entry<K, V>> iterator() {
                return parentMap.entrySet().stream()
                        .filter(entry -> entry.getKey().toString().startsWith(prefix))
                        .iterator();
            }
        };
    }

    @Override
    public V put(K key, V value) {
        return parentMap.put((K) (prefix + '.' + key.toString()), value);
    }

    @Override
    public V get(Object key) {
        return parentMap.get(prefix + '.' + key.toString());
    }
}
