package com.jcleary.core.store;

import com.google.common.collect.ImmutableMap;
import com.jcleary.core.store.exceptions.StoreException;
import com.jcleary.util.RegexUtil;
import org.apache.commons.exec.util.MapUtils;
import org.testng.log4testng.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by portalsoup on 2/16/17.
 */
public class StateStore {

    private final Logger log = Logger.getLogger(StateStore.class);

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

    public String interpolate(String str) {
        String result = str;
        Pattern p = Pattern.compile(RegexUtil.INTERPOLATE_SINGLE.getPattern());
        Matcher m = p.matcher(str);
        for (int x = 0; x < m.groupCount(); x++) {
            String temp = m.group(x);
            try {
                result = result.replace("${" + m.group(x) + "}", getItem(temp));
            } catch (StoreException se) {
                log.warn("Attempted to interpolate '" + temp + "' but could not find it in the store!");
            }
        }
        return result;
    }
}
