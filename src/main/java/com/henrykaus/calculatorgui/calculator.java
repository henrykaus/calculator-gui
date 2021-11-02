package com.henrykaus.calculatorgui;

import java.util.Objects;
import java.util.Stack;

/**
 * PURPOSE: Calculates an answer to an expression based problem.
 *          Checks for bad input. Only allows characters: (, ), +, -, *, /, ^, and the number keys. Must be in a valid
 *          form, or throws back to user
 */
public class calculator
{
    private        String     expression;   // Current expression the calculator is working on
    static private operator[] operators;    // Allowed operators ((,),+,-,*,/,^)
    static private char[]     numbers;      // Allowed numbers (0,1,2,3,4,5,6,7,8,9)

    // Default constructor
    public calculator()
    {
        init();
    }

    /**
     * PURPOSE: set the calculator expression to a string
     * @param expression sets expression if valid, otherwise keeps original expression stored.
     *                   If first set, expression is set to default "0"
     * @throws NullPointerException for null expression given
     * @throws IllegalArgumentException for invalid expression
     */
    public void set_expression(String expression) throws NullPointerException, IllegalArgumentException
    {
        String old_expression = this.expression;    // Store old expression in case arg is illegal
        int    valid;                               // Store result of is_valid() check

        Objects.requireNonNull(expression, "expression must not be non-null");

        // Remove spaces from expression, replaces an all space string with a "0"
        this.expression = expression;
        this.remove_spaces();
        if (Objects.equals(this.expression, ""))
           this.expression = "0";

        // Checks validity of expression
        valid = is_valid(0, false, false);
        if (valid == -1) {
            this.expression = old_expression;
            throw new IllegalArgumentException("Invalid expression.");
        }
    }

    /**
     * PURPOSE: removes all space characters from the current stored expression string.
     *          Used by set_expression()
     */
    private void remove_spaces()
    {
        int           num_spaces = 0;   // Count of spaces in string
        StringBuilder new_expression;   // Stores the temp expression without spaces (" ")

        // Counts number of spaces in expression
        for (int i = 0; i < expression.length(); ++i)
        {
            if (expression.charAt(i) == ' ')
                ++num_spaces;
        }

        // Creates empty String if expression only contains spaces
        // Else, copies over non-space characters to new string and sets expression field
        if (num_spaces == expression.length())
            expression = "";
        else
        {
            new_expression = new StringBuilder(expression.length() - num_spaces);
            for (int i = 0; i < expression.length(); ++i)
            {
                if (expression.charAt(i) != ' ')
                    new_expression.append(expression.charAt(i));
            }
            expression = new_expression.toString();
        }
    }

    /**
     * PURPOSE: Recursively checks validity of expression for matching parens, correct characters, correct operator
     *          placement, correct decimal placement and if the expression is non-empty
     * @param index is the current index being examined
     * @param searching_for_paren flag is set if searching for matching paren
     * @param found_decimal flag is set if the current number already has a decimal
     * @return an int for the final index of the string (-1 if an error was encountered) indicating success, but
     *         should never be 0
     */
    private int is_valid(int index, boolean searching_for_paren, boolean found_decimal)
    {
        char curr_char;     // Current character being examined

        // Check if valid position or character
        if (index == expression.length() && !searching_for_paren && index != 0)   // Valid end of Expression
            return index;
        else if (index == expression.length())  // End of expression w/o finding matching ")" or zero char expression
            return -1;
        else if (!is_allowable(curr_char = expression.charAt(index)))    // Character is invalid
            return -1;

        // Check decimal validity
        if (is_operator(curr_char))                 // Found operator, unset decimal flag
            found_decimal = false;
        else if (curr_char == '.' && found_decimal) // Found a second decimal in same number
            return -1;
        else if (curr_char == '.')                  // Found first decimal
        {
            // A number either exists before . or after .
            if ((index != 0 && is_number(expression.charAt(index - 1))) ||
                    (index < expression.length() - 1 && is_number(expression.charAt(index + 1))))
                found_decimal = true;
            else
                return -1;
        }

        // Check operators
        if (index == 0)
        {
            // If starting with operator, must be either ( or -
            if (is_operator(curr_char) && curr_char != '(' && curr_char != '-')
                return -1;
        }
        else
        {
            char    prev_char       = expression.charAt(index - 1);
            boolean curr_char_is_op = is_operator(curr_char);
            boolean prev_char_is_op = is_operator(prev_char);
            // Check various not allowed conditions for operators
            if (curr_char == '(' && prev_char == ')' ||     // )(5...
                    curr_char_is_op && prev_char == '(' && curr_char != '(' && curr_char != '-' ||     // (*5...
                    prev_char_is_op && curr_char == ')' && prev_char != ')'  ||     // 5*)...
                    prev_char == ')' && curr_char == '.'  ||     // ).5...
                    prev_char == '.' && curr_char == '('  ||     // 5.(...
                    curr_char_is_op && curr_char != ')' && index == expression.length() - 1 ||   // 8+
                    curr_char_is_op && prev_char_is_op && (curr_char != '(' && curr_char != ')' && prev_char != ')' && curr_char != '-') || // */
                    curr_char == '(' && is_number(prev_char) ||  // 7(
                    prev_char == ')' && is_number(curr_char)     // )9
            )
                return -1;
        }

        // Check parens (own set of recursion)
        if (curr_char == '(')                   // Character is opening (
        {
            // Find closing paren and set current index to closing paren
            index = is_valid(index + 1, true, found_decimal);
            if (index == -1)
                return -1;
        }
        else if (curr_char == ')' && searching_for_paren)    // Found matching closing )
            return index; // Returns back to call above
        else if (curr_char == ')')                           // Found closing ) w/o opening (
            return -1;

        return is_valid(++index, searching_for_paren, found_decimal);
    }

    /**
     * PURPOSE: check is character is a number, operator, or .
     * @param to_check is char to compare against
     * @return boolean for if the character is allowable in the expression
     */
    private boolean is_allowable(char to_check)
    {
        return is_number(to_check) || is_operator(to_check) || to_check == '.';
    }

    /**
     * PURPOSE: check if the param is a number in ascii form
     * @param to_check is a character to compare with
     * @return boolean for if the character is a number
     */
    private boolean is_number(char to_check)
    {
        for (char character : numbers)
        {
            if (to_check == character)
                return true;
        }
        return false;
    }

    /**
     * PURPOSE: check if the param is an operator used in the calculator
     * @param to_check is a character to compare with
     * @return boolean for if the character is an operator
     */
    private boolean is_operator(char to_check)
    {
        // Check character in the array of linked lists
        for (operator character : operators)
        {
            while (character != null)
            {
                if (character.is_equals(to_check))
                    return true;
                character = character.get_next();
            }
        }
        return false;
    }

    /**
     * PURPOSE: turn infix expression into postfix and calculate based on postfix
     * @return the evaluation in double form
     */
    public double calculate()
    {
        String post_fix = make_post_fix();  // Post-fix version of expression
        return calculate_answer(post_fix);
    }

    /**
     * PURPOSE: convert the field expression into a postfix form
     * @return postfix form of the infix field expression
     */
    private String make_post_fix()
    {
        Stack<Character> operators = new Stack<>();                 // Operators in expression
        StringBuilder postfix_expression = new StringBuilder(expression.length()*2);     // Postfix form
        char curr_symbol;                                           // Current character
        char prev_symbol = '\0';                                    // Previous character

        for (int i = 0; i < expression.length(); ++i)
        {
            // Assign curr and prev
            curr_symbol = expression.charAt(i);
            if (i > 0) prev_symbol = expression.charAt(i - 1);

            // If a number or decimal, append to postfix expression
            if (is_number(curr_symbol) || curr_symbol == '.')
                postfix_expression.append(curr_symbol);
            else
            {
                // Append variable amount of spaces to separate numbers
                postfix_expression.append(' ');
                if (operators.isEmpty() || curr_symbol == '(')
                {
                    if (curr_symbol == '-' && (prev_symbol == '\0' || (is_operator(prev_symbol) && prev_symbol != ')')))
                        postfix_expression.append("0 ");
                    operators.push(curr_symbol);
                }
                else if (curr_symbol == ')')
                {
                    char top_op;
                    while ((top_op = operators.pop()) != '(')
                        postfix_expression.append(top_op);
                }
                else if (curr_symbol == '-' && (is_operator(prev_symbol) && prev_symbol != ')'))
                {
                    postfix_expression.append("0 ");
                    operators.push(curr_symbol);
                }
                else if (curr_symbol == '^')
                    operators.push(curr_symbol);
                else // 9*-6
                {
                    char top_op = operators.pop();
                    int  precedence = compare_precedence(top_op, curr_symbol);

                    if (precedence > 0)
                        operators.push(top_op);
                    else
                    {
                        postfix_expression.append(top_op);
                        while (!operators.isEmpty() && compare_precedence((top_op = operators.pop()), curr_symbol) >= 0)
                        {
                            if (top_op == '(') {
                                operators.push(top_op);
                                break;
                            }
                            else
                                postfix_expression.append(top_op);
                        }
                    }
                    operators.push(curr_symbol);
                }
            }
        }

        while (!operators.isEmpty())
            postfix_expression.append(operators.pop());

        return postfix_expression.toString();
    }

    /**
     * PURPOSE: Calculates result based off postfix expression, takes an expression with at least one space between
     *          each double. Ex. 6 4 *2 5 /+3-
     * @param postfix_expression a postfix expression
     * @return a result as a double
     */
    private double calculate_answer(String postfix_expression)
    {

        Stack<Double> operands       = new Stack<>();       // Operands from expression
        StringBuilder curr_full_number = new StringBuilder(); // Current full number
        int           running_index  = 0;                   // Index in postfix expression

        while (running_index < postfix_expression.length())
        {
            // Skip initial whitespace
            for (; running_index < postfix_expression.length() && postfix_expression.charAt(running_index) == ' '; ++running_index);
            if (running_index == postfix_expression.length()) break;

            // Grab full double and push on operands stack
            if (postfix_expression.charAt(running_index) == '.' || is_number(postfix_expression.charAt(running_index))) {
                // Get first full number
                for (; running_index < postfix_expression.length(); ++running_index) {
                    if (postfix_expression.charAt(running_index) == ' ' || is_operator(postfix_expression.charAt(running_index)))
                        break;
                    else
                        curr_full_number.append(postfix_expression.charAt(running_index));
                }
                operands.push(Double.parseDouble(curr_full_number.toString()));
                curr_full_number = new StringBuilder();
            }
            // Perform operation with current operator and top two numbers and push on stack
            else
            {
                double second = operands.pop();
                double first  = operands.pop();
                char   op     = postfix_expression.charAt(running_index);
                operands.push(perform_operation(first, op, second));
                ++running_index;
            }
        }

        // Return final item (result) in stack
        return operands.pop();
    }

    /**
     * PURPOSE: perform operation between two doubles and an operator. Selects choice if available
     * @param first is the first double
     * @param op is the operator
     * @param second is the second double
     * @return the result between the two doubles with the operator (0 if op doesn't exist)
     * @throws ArithmeticException for a divide by 0 error
     * @throws IndexOutOfBoundsException for a bad operator
     */
    private double perform_operation(double first, char op, double second) throws ArithmeticException, IndexOutOfBoundsException
    {
        if (op == '-')
            return first - second;
        if (op == '+')
            return first + second;
        if (op == '*')
            return first * second;
        if (op == '^')
            return Math.pow(first, second);
        if (op == '/')
        {
            if (second == 0.0)
                throw new ArithmeticException("Divide by 0");
            return first / second;
        }
        throw new IndexOutOfBoundsException(op + " not an operator.");
    }

    /**
     * PURPOSE: initialize data fields of calculator and put operators in PEMDAS order
     */
    private void init()
    {
        expression = "0";
        numbers    = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        // Assign operators in array of LL according to order of operations (^,*,/,+,-,(,))
        operators    = new operator[4];
        operators[0] = new operator('^');
        operators[1] = new operator('*');
        operators[1].set_next(new operator('/'));
        operators[2] = new operator('+');
        operators[2].set_next(new operator('-'));
        operators[3] = new operator('(');
        operators[3].set_next(new operator(')'));
    }

    /**
     * PURPOSE: Compares precedence of two operators passed in
     * @param left is operator 1
     * @param right is operator 2
     * @return 0< if left is greater, <0 if right is greater, 0 if same
     */
    private int compare_precedence(char left, char right)
    {
        int left_rank  = find_rank(left);
        int right_rank = find_rank(right);

        return Integer.compare(left_rank, right_rank);
    }

    /**
     * PURPOSE: Finds rank of a given operator according to order of operations
     * @param to_find is operator to find rank of
     * @return returns index in array ( 0=^ | 1=*,/ | 2=*,/ | 3=(,) )
     * @throws IndexOutOfBoundsException if operator doesn't exist
     */
    private int find_rank(char to_find) throws IndexOutOfBoundsException
    {
        operator character; // Current character in array of LLs

        // If finds operator, return index
        for (int i = 0; i < operators.length; ++i)
        {
            character = operators[i];
            while (character != null)
            {
                if (character.is_equals(to_find))
                    return i;
                character = character.get_next();
            }
        }
        throw new IndexOutOfBoundsException(to_find + " not an operator.");
    }
}