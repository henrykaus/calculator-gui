package com.henrykaus.calculatorgui;

/**
 * PURPOSE: Houses a symbol and next reference for a linked list of operators of same precedence
 * HOW TO USE: Next houses an operator of same precedence (ex. + -> -, or * -> /)
 */
public class operator
{
    private char     symbol;    // Operator symbol (ex. *, /, +, -)
    private operator next;      // Next operator in linked list

    // Default Constructor
    public operator()
    {
        symbol = '\0';
        next   = null;
    }

    // Parameterized Constructor
    public operator(char symbol)
    {
        this.symbol = symbol;
        next        = null;
    }

    /**
     * PURPOSE: Returns if a character matches the operator symbol
     * @param symbol to compare with
     * @return if the operator symbols are equal
     */
    public boolean is_equals(char symbol)
    {
        return this.symbol == symbol;
    }

    /**
     * PURPOSE: Getter for the operator character
     * @return operator character
     */
    public char get_symbol()
    {
        return symbol;
    }

    /**
     * PURPOSE: Getter for next reference
     * @return next operator reference
     */
    public operator get_next()
    {
        return next;
    }

    /**
     * PURPOSE: Sets next reference
     * @param next operator to set next reference to
     */
    public void set_next(operator next)
    {
        this.next = next;
    }
}