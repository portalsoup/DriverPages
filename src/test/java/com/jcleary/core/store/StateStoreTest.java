package com.jcleary.core.store;

import com.jcleary.core.store.exceptions.StoreException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;


/**
 * Created by portalsoup on 2/16/17.
 */
public class StateStoreTest {

    @Test
    public void getNullPathTest() {
        StateStore store = new StateStore();

        try {
            store.getItem(null);
            assert false;
        } catch (StoreException se) {

        }
    }

    @Test
    public void getInvalidPathTest() {
        StateStore store = new StateStore();

        try {
            store.getItem("i.am.invalid");
            assert false;
        } catch (StoreException se) {

        }
    }

    @Test
    public void mapTest() {
        StateStore store = new StateStore();
        Map<String, String> map = new HashMap<>();

        map.put("one"   , "1");

        store.update("my.map", map);

        Map<String, String> gotten = store.getMap("my.map");

        assertThat(map, equalTo(gotten));
    }

    @Test
    public void interpolationOneVariableTest() {
        StateStore store = new StateStore();
        store.update("one", "ONE");
        String original = "I am a test string ${one}";
        String expected = "I am a test string ONE";

        String interpolated = store.interpolate(original);

        assertThat(interpolated, equalTo(expected));
    }

    @Test
    public void interpolateTwoVariableTest_1() {
        StateStore store = new StateStore();
        store.update("one", "un");
        store.update("two", "deux");

        String original = "${one} ${two}";
        String expected = "un deux";

        String interpolated = store.interpolate(original);

        assertThat(interpolated, equalTo(expected));
    }

    @Test
    public void interpolateTwoVariableTest_2() {
        StateStore store = new StateStore();
        store.update("one", "un");
        store.update("two", "deux");

        String original = "${one} I am a string in-between ${two}";
        String expected = "un I am a string in-between deux";

        String interpolated = store.interpolate(original);

        assertThat(interpolated, equalTo(expected));
    }

    @Test
    public void interpolateTwoVariableTest_3() {
        StateStore store = new StateStore();
        store.update("one", "un");
        store.update("two", "deux");

        String original = "We are strings ${one} I am a string in-between ${two} on the edges";
        String expected = "We are strings un I am a string in-between deux on the edges";

        String interpolated = store.interpolate(original);

        assertThat(interpolated, equalTo(expected));
    }

    @Test
    public void interpolateTwoVariableTest_4() {
        StateStore store = new StateStore();
        store.update("one", "un");
        store.update("two", "deux");

        String original = "no-padding${one} ${two}no-padding";
        String expected = "no-paddingun deuxno-padding";

        String interpolated = store.interpolate(original);

        assertThat(interpolated, equalTo(expected));
    }

    @Test
    public void interpolateduplicateVariablesTest() {
        StateStore store = new StateStore();
        store.update("one", "uno");

        String original = "1 is ${one} and one is also ${one}";
        String expected = "1 is uno and one is also uno";

        String interpolated = store.interpolate(original);

        assertThat(interpolated, equalTo(expected));
    }

    @Test
    public void interpolateNotFoundTest() {
        StateStore store = spy(new StateStore());

        String original = "${wome}";

        try {
            store.interpolate(original);
            assert false;
        } catch (StoreException se) {

        }
        verify(store, times(1)).getItem("wome");
    }


    @Test
    public void immutabilityTest() {
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