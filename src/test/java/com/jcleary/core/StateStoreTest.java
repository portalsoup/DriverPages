package com.jcleary.core;

import com.jcleary.core.store.StateStore;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by portalsoup on 2/16/17.
 */
public class StateStoreTest {

    @Test
    public void test() {
        StateStore store = new StateStore();

        List<String> originalList = new ArrayList<>();
        originalList.add("abc");
        store.update("twoots", originalList);

        List<String> list = store.getList("twoots");

        assert list.size() == 1;
        assert list.get(0).equals("abc");

        list.add("def");
        store.update("twoots", list);

        List<String> newList = store.getList("twoots");

        assert newList.size() == 2;
        assert list.get(1).equals("def");

        newList.add("ghi");
        store.update("twoots", newList);

        List<String> newNewList = store.getList("twoots");

        assert list.size() == 2;
        assert newNewList.size() == 3;

        newNewList.add("jkl");
        store.update("twoots", newNewList);

        List<String> newNewNewList = store.getList("twoots");

        assert list.size() == 2;
        assert newList.size() == 3;
        assert newNewList.size() == 4;

        store.update("twoots.id", 5);

        int twootId = store.getItem("twoots.id");

        assert twootId == 5;
        assert list.size() == 2;
        assert newList.size() == 3;
        assert newNewList.size() == 4;
    }

}