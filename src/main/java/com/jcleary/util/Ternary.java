package com.jcleary.util;

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

    TRUE,
    UNKNOWN,
    FALSE;

    /**
     * Squash this ternary value into a boolean defaulting all non-true values to false.
     *
     * @return      true if this value is TRUE
     *              false if this value is FALSE or UNKNOWN
     */
    public boolean squash() {
        return this == TRUE;
    }

    /**
     * Perform a logical NOT operation on this ternary value.
     *
     * @return      TRUE if this value is FALSE
     *              FALSE if this value is TRUE
     *              UNKNOWN if this value is UNKNOWN
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
     * @param value The other value
     *
     * @return      TRUE if this value and another valure are both TRUE,
     *              UNKNOWN if either value is UNKNOWN,
     *              and FALSE if one is FALSE
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
     * @param value Another value
     *
     * @return      TRUE if this value and another valure are both TRUE,
     *              UNKNOWN if either value is UNKNOWN,
     *              and FALSE if one is FALSE
     */
    public Ternary AND(boolean value) {

        return AND(value ? TRUE : FALSE);
    }

    /**
     * Perform a logical XOR operation with this ternary value and another.
     *
     * @param value Another value
     *
     * @return      UNKNOWN if either value is UNKNOWN,
     *              TRUE if one value is TRUE, and another value is FALSE
     */
    public Ternary XOR(Ternary value) {
        if (this == UNKNOWN || value == UNKNOWN) {
            return UNKNOWN; // If either value are unknown, then it's still unknown
        }
        return (this != value ? TRUE : FALSE);
    }

    /**
     * Perform a logical XOR operation with this ternary value and another.
     *
     * @param value Another value
     *
     * @return      UNKNOWN if either value is UNKNOWN,
     *              TRUE if one value is TRUE, and another value is FALSE
     */
    public Ternary XOR(boolean value) {
        return XOR(value ? TRUE : FALSE);
    }

    /**
     * Perform a logical OR operation with this ternary value and another.
     *
     * @param value Another value
     *
     * @return      TRUE if either value is TRUE,
     *              UNKNOWN if neither value are TRUE and one value is UNKNOWN
     *              else FALSE
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
     * @param value Another value
     *
     * @return      TRUE if either value is TRUE,
     *              UNKNOWN if neither value are TRUE and one value is UNKNOWN
     *              else FALSE
     */
    public Ternary OR(boolean value) {
        return OR(value ? TRUE : FALSE);
    }

    /**
     * Perform a logical NOR operation with this ternary value and another.
     *
     * @param value Another value
     *
     * @return      UNKNOWN if either value is UNKNOWN
     *              TRUE if both values are FALSE
     *              else FALSE
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
     *
     * @param value Another value
     *
     * @return      UNKNOWN if either value is UNKNOWN
     *              TRUE if both values are FALSE
     *              else FALSE
     */
    public Ternary NOR(boolean value) {
        return NOR(value ? TRUE : FALSE);
    }

    /**
     * Perform a logical XNOR operation with this ternary value and another.
     *
     * @param value Another value
     *
     * @return      UNKNOWN if either value is UNKNOWN
     *              TRUE if this value and another value are equal
     *              else FALSE
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
     * @param value Another value
     *
     * @return      UNKNOWN if either value is UNKNOWN
     *              TRUE if this value and another value are equal
     *              else FALSE
     */
    public Ternary XNOR(boolean value) {
        return XNOR(value ? TRUE : FALSE);
    }

    /**
     * Perform a logical NAND operation with this ternary value and another.
     *
     * @param value Another value
     *
     * @return      TRUE if either value is false
     *              UNKNOWN if no value is FALSE and either value is UNKNOWN
     *              else FALSE
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
     *
     * @param value Another value
     *
     * @return      TRUE if either value is false
     *              UNKNOWN if no value is FALSE and either value is UNKNOWN
     *              else FALSE
     */
    public Ternary NAND(boolean value) {
        return NAND(value ? TRUE : FALSE);
    }
}
