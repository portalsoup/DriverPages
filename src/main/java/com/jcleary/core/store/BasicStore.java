package com.jcleary.core.store;

import com.google.common.collect.ImmutableMap;
import com.jcleary.core.store.exceptions.StoreException;
import org.apache.commons.exec.util.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by portalsoup on 2/16/17.
 */
public class BasicStore {


    private ImmutableMap<String, Object> store;

    private Object get(String path) {
        if (path == null) {
            throw new StoreException("Path can't be null");
        }

        Object item = store.get(path);

        if (item == null) {
            throw new StoreException("Invalid path <" + path + ">");
        }
        return item;
    }

    @SuppressWarnings("unchecked")
    public <T> T getItem(String path) {
        Object item = get(path);
        return (T) item;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String path) {
        Object list = get(path);

        return new ArrayList<>((List<T>) list);
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getMap(String path) {
        Object map = get(path);

        return MapUtils.copy((Map<K, V>) map);
    }

    public void update(String path, Object item) {
        Map<String, Object> map;
        if (store == null) {
            map = new HashMap<>();
        } else {
            map = new HashMap<>(store);
        }
        map.put(path, item);
        store = ImmutableMap.copyOf(map);
    }
}
