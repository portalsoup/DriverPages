package com.jcleary.core;

/**
 *  A ternary data type that is capable of performing ternary based logical operations.
 *
 *  Below is a truth table for reference.
 *
 *  +---------------------------------------------+
 *  | A | B || and | xor | or | nor | xnor | nand |
 *  |=============================================|
 *  | F | F ||  F  |  F  |  F |  T  |  T   |  T   |
 *  | F | ? ||  F  |  ?  |  ? |  ?  |  ?   |  T   |
 *  | F | T ||  F  |  T  |  T |  F  |  F   |  T   |
 *  | ? | F ||  F  |  ?  |  ? |  ?  |  ?   |  T   |
 *  | ? | ? ||  ?  |  ?  |  ? |  ?  |  ?   |  ?   |
 *  | ? | T ||  ?  |  ?  |  T |  ?  |  ?   |  ?   |
 *  | T | F ||  F  |  T  |  T |  F  |  F   |  T   |
 *  | T | ? ||  ?  |  ?  |  T |  ?  |  ?   |  ?   |
 *  | T | T ||  T  |  F  |  T |  F  |  T   |  F   |
 *  +---------------------------------------------+
 *
 * Created by julian on 2/23/15.
 */
public enum Ternary {

    TRUE(true),
    UNKNOWN(null),
    FALSE(false);

    private Boolean val;

    Ternary(Boolean val) {
        this.val = val;
    }

    /**
     * Return this ternary as a boolean using null if unknown.
     *
     * @return
     */
    public boolean toBoolean() {
        return this.val;
    }

    /**
     * Squash this ternary value into a boolean defaulting all non-true values to false.
     * @return
     */
    public boolean squash() {
        if (this == TRUE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Perform a logical NOT operation on this ternary value.
     *
     * @return
     */
    public Ternary NOT() {

        switch (this) {
            case TRUE:
                return FALSE;
            case FALSE:
                return TRUE;
            default:
                return UNKNOWN;
        }
    }

    /**
     * Perform a logical AND operation with this ternary value and another.
     *
     * @param value
     * @return
     */
    public Ternary AND(Ternary value) {
        if (this == FALSE || value == FALSE) {
            return FALSE; // Any false value will yield false
        } else if (this == UNKNOWN || value == UNKNOWN) {
            return UNKNOWN; // If there are no false values, then any unknown yields unknown
        } else {
            return TRUE;
        }
    }

    /**
     * Perform a logical AND operation with this ternary value and another.
     *
     * @param value
     * @return
     */
    public Ternary AND(boolean value) {

        return AND(value ? TRUE : FALSE);
    }

    /**
     * Perform a logical XOR operation with this ternary value and another.
     *
     * @param value
     * @return
     */
    public Ternary XOR(Ternary value) {
        if (this == UNKNOWN || value == UNKNOWN) {
            return UNKNOWN; // If either value are unknown, then it's still unknown
        }

        // Neither terms are unknown
        if (this == TRUE) {
            if (value == TRUE) {
                return FALSE; // Eqality means false
            } else if (value == FALSE) {
                return TRUE; // Inequality means true
            }
        } else if (this == FALSE) {
            if (value == TRUE) {
                return TRUE; // Inequality again
            } else if (value == FALSE) {
                return FALSE; // Equality again
            }
        }
        throw new RuntimeException("Forgot how to math... shit");
    }

    /**
     * Perform a logical XOR operation with this ternary value and another.
     *
     * @param value
     * @return
     */
    public Ternary XOR(boolean value) {
        return XOR(value ? TRUE : FALSE);
    }

    /**
     * Perform a logical OR operation with this ternary value and another.
     *
     * @param value
     * @return
     */
    public Ternary OR(Ternary value) {

        if (value == TRUE || this == TRUE) {
            return TRUE; // Any true value yields true
        } else if (value == UNKNOWN || this == UNKNOWN) {
            return UNKNOWN; // If there are no true values, then any unknown yields uknown
        } else {
            return FALSE;
        }
    }

    /**
     * Perform a logical OR operation with this ternary value and another.
     *
     * @param value
     * @return
     */
    public Ternary OR(boolean value) {
        return OR(value ? TRUE : FALSE);
    }

    /**
     * Perform a logical NOR operation with this ternary value and another.
     * @param value
     * @return
     */
    public Ternary NOR(Ternary value) {
        if (this == UNKNOWN || value == UNKNOWN) {
            return UNKNOWN; // If either value is unknown, then the result is unkonwn
        }

        if (this == FALSE && value == FALSE) {
            return TRUE; // If both values are false, then return true.
        }
        return FALSE; // If there are no unkowns, but both values are not false.  Then we return false.
    }

    /**
     * Perform a logical NOR operation with this ternary value and another.
     * @param value
     * @return
     */
    public Ternary NOR(boolean value) {
        return NOR(value ? TRUE : FALSE);
    }

    /**
     * Perform a logical XNOR operation with this ternary value and another.
     *
     * @param value
     * @return
     */
    public Ternary XNOR(Ternary value) {
        if (this == UNKNOWN || value == UNKNOWN) {
            return UNKNOWN; // If either value is unknown, then the result is unknown
        }

        if (this == value) {
            return TRUE; // If neither value is unknown, then any equality will yield true
        }
        return FALSE;
    }

    /**
     * Perform a logical XNOR operation with this ternary value and another.
     *
     * @param value
     * @return
     */
    public Ternary XNOR(boolean value) {
        return XNOR(value ? TRUE : FALSE);
    }

    /**
     * Perform a logical NAND operation with this ternary value and another.
     * @param value
     * @return
     */
    public Ternary NAND(Ternary value) {
        if (this == FALSE || value == FALSE) {
            return TRUE; // If any values are false, then return true.
        }

        if (this == UNKNOWN || value == UNKNOWN) {
            return UNKNOWN; // If there are no false values, then any unknown will yield an unknown answer.
        }

        return FALSE; // In the one case where there are no false or unknown values, we return false
    }

    /**
     * Perform a logical NAND operation with this ternary value and another.
     * @param value
     * @return
     */
    public Ternary NAND(boolean value) {
        return NAND(value ? TRUE : FALSE);
    }


}
