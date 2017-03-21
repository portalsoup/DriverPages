package com.jcleary.util;

import org.testng.annotations.Test;

import static com.jcleary.util.Ternary.FALSE;
import static com.jcleary.util.Ternary.TRUE;
import static com.jcleary.util.Ternary.UNKNOWN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by portalsoup on 2/24/17.
 */
public class TernaryTest {

    @Test
    public void squashTrueTest() {
        assertThat(TRUE.squash(), equalTo(true));
    }

    @Test
    public void squashFalseTest() {
        assertThat(FALSE.squash(), equalTo(false));
    }

    @Test
    public void squashUnknownTest() {
        assertThat(UNKNOWN.squash(), equalTo(false));
    }

    @Test
    public void NOTTrueTest() {
        assertThat(TRUE.NOT(), equalTo(FALSE));
    }

    @Test
    public void NOTFalseTest() {
        assertThat(FALSE.NOT(), equalTo(TRUE));
    }

    @Test
    public void NOTUnknownTest() {
        assertThat(UNKNOWN.NOT(), equalTo(UNKNOWN));
    }

    @Test
    public void ANDTrueTest() {
        assertThat(TRUE.AND(TRUE), equalTo(TRUE));
        assertThat(TRUE.AND(FALSE), equalTo(FALSE));
        assertThat(TRUE.AND(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void ANDFalseTest() {
        assertThat(FALSE.AND(TRUE), equalTo(FALSE));
        assertThat(FALSE.AND(FALSE), equalTo(FALSE));
        assertThat(FALSE.AND(UNKNOWN), equalTo(FALSE));
    }

    @Test
    public void ANDUnknownTest() {
        assertThat(UNKNOWN.AND(TRUE), equalTo(UNKNOWN));
        assertThat(UNKNOWN.AND(FALSE), equalTo(FALSE));
        assertThat(UNKNOWN.AND(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void ANDboolTest() {
        assertThat(TRUE.AND(true), equalTo(TRUE));
        assertThat(TRUE.AND(false), equalTo(FALSE));
    }

    @Test
    public void XORTrueTest() {
        assertThat(TRUE.XOR(TRUE), equalTo(FALSE));
        assertThat(TRUE.XOR(FALSE), equalTo(TRUE));
        assertThat(TRUE.XOR(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void XORFalseTest() {
        assertThat(FALSE.XOR(TRUE), equalTo(TRUE));
        assertThat(FALSE.XOR(FALSE), equalTo(FALSE));
        assertThat(FALSE.XOR(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void XORUnknownTest() {
        assertThat(UNKNOWN.XOR(TRUE), equalTo(UNKNOWN));
        assertThat(UNKNOWN.XOR(FALSE), equalTo(UNKNOWN));
        assertThat(UNKNOWN.XOR(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void XORboolTest() {
        assertThat(TRUE.XOR(true), equalTo(FALSE));
        assertThat(TRUE.XOR(false), equalTo(TRUE));
    }

    @Test
    public void ORTrueTest() {
        assertThat(TRUE.OR(TRUE), equalTo(TRUE));
        assertThat(TRUE.OR(FALSE), equalTo(TRUE));
        assertThat(TRUE.OR(UNKNOWN), equalTo(TRUE));
    }

    @Test
    public void ORFalseTest() {
        assertThat(FALSE.OR(TRUE), equalTo(TRUE));
        assertThat(FALSE.OR(FALSE), equalTo(FALSE));
        assertThat(FALSE.OR(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void ORUnknownTest() {
        assertThat(UNKNOWN.OR(TRUE), equalTo(TRUE));
        assertThat(UNKNOWN.OR(FALSE), equalTo(UNKNOWN));
        assertThat(UNKNOWN.OR(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void ORboolTest() {
        assertThat(UNKNOWN.OR(true), equalTo(TRUE));
        assertThat(UNKNOWN.OR(false), equalTo(UNKNOWN));
    }

    @Test
    public void NORTrueTest() {
        assertThat(TRUE.NOR(TRUE), equalTo(FALSE));
        assertThat(TRUE.NOR(FALSE), equalTo(FALSE));
        assertThat(TRUE.NOR(UNKNOWN), equalTo(FALSE));
    }

    @Test
    public void NORFalseTest() {
        assertThat(FALSE.NOR(TRUE), equalTo(FALSE));
        assertThat(FALSE.NOR(FALSE), equalTo(TRUE));
        assertThat(FALSE.NOR(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void NORUnknownTest() {
        assertThat(UNKNOWN.NOR(TRUE), equalTo(FALSE));
        assertThat(UNKNOWN.NOR(FALSE), equalTo(UNKNOWN));
        assertThat(UNKNOWN.NOR(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void NORboolTest() {
        assertThat(FALSE.NOR(true), equalTo(FALSE));
        assertThat(FALSE.NOR(false), equalTo(TRUE));
    }

    @Test
    public void XNORTrueTest() {
        assertThat(TRUE.XNOR(TRUE), equalTo(TRUE));
        assertThat(TRUE.XNOR(FALSE), equalTo(FALSE));
        assertThat(TRUE.XNOR(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void XNORFalseTest() {
        assertThat(FALSE.XNOR(TRUE), equalTo(FALSE));
        assertThat(FALSE.XNOR(FALSE), equalTo(TRUE));
        assertThat(FALSE.XNOR(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void XNORUnknownTest() {
        assertThat(UNKNOWN.XNOR(TRUE), equalTo(UNKNOWN));
        assertThat(UNKNOWN.XNOR(FALSE), equalTo(UNKNOWN));
        assertThat(UNKNOWN.XNOR(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void XNORboolTest() {
        assertThat(TRUE.XNOR(true), equalTo(TRUE));
        assertThat(TRUE.XNOR(false), equalTo(FALSE));
    }

    @Test
    public void NANDTrueTest() {
        assertThat(TRUE.NAND(TRUE), equalTo(FALSE));
        assertThat(TRUE.NAND(FALSE), equalTo(TRUE));
        assertThat(TRUE.NAND(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void NANDFalseTest() {
        assertThat(FALSE.NAND(TRUE), equalTo(TRUE));
        assertThat(FALSE.NAND(FALSE), equalTo(TRUE));
        assertThat(FALSE.NAND(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void NANDUnknownTest() {
        assertThat(UNKNOWN.NAND(TRUE), equalTo(UNKNOWN));
        assertThat(UNKNOWN.NAND(FALSE), equalTo(UNKNOWN));
        assertThat(UNKNOWN.NAND(UNKNOWN), equalTo(UNKNOWN));
    }

    @Test
    public void NANDboolTest() {
        assertThat(TRUE.NAND(true), equalTo(FALSE));
        assertThat(TRUE.NAND(false), equalTo(TRUE));
    }
}