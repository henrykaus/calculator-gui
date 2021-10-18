package com.henrykaus.calculatorgui;

import java.util.Stack;

public class calculator
{
    private        String     expression;
    static private operator[] operators;
    static private char[]     numbers;

    public calculator()
    {
        init();
    }

    public void set_expression(String expression)
    {
        this.expression = expression;
    }

    public boolean is_valid()
    {
        remove_spaces();
        if (expression == null)
            return false;

        int    valid = is_valid(0, false, false);
        return valid != -1;
    }

    private void remove_spaces() throws NullPointerException
    {
        if (this.expression == null)
            throw new NullPointerException("Null expression");

        int num_spaces = 0;
        StringBuilder new_expression;
        for (int i = 0; i < expression.length(); ++i)
        {
            if (expression.charAt(i) == ' ')
                ++num_spaces;
        }
        if (num_spaces == expression.length())
            expression = null;
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

    private int is_valid(int index, boolean searching_for_paren, boolean found_decimal)
    {
        char curr_char;
        // Check characters
        if (index == expression.length() && !searching_for_paren)   // End of Expression
            return index;
        else if (index == expression.length())                      // End of expression w/o finding matching ")"
            return -1;
        else if (!is_allowable(curr_char = expression.charAt(index)))    // Character isn't valid
            return -1;

        // Check decimal
        if (is_operator(curr_char))   // Found next operator
            found_decimal = false;
        else if (curr_char == '.' && found_decimal) // Found a second decimal in same number
            return -1;
        else if (curr_char == '.')      // Found first decimal (and is surrounded by a number
        {
            if ((index != 0 && is_number(expression.charAt(index - 1))) ||
                    (index < expression.length() - 1 && is_number(expression.charAt(index + 1))))
                found_decimal = true;
            else
                return -1;
        }

        // Check operators
        if (index == 0)
        {
            if (is_operator(curr_char) && curr_char != '(' && curr_char != '-')
                return -1;
        }
        else
        {
            char    prev_char       = expression.charAt(index - 1);
            boolean curr_char_is_op = is_operator(curr_char);
            boolean prev_char_is_op = is_operator(prev_char);
            if (curr_char == '(' && prev_char == ')' ||     // )(5...
                    curr_char_is_op && prev_char == '(' && curr_char != '(' && curr_char != '-' ||     // (*5...
                    prev_char_is_op && curr_char == ')' && prev_char != ')'  ||     // 5*)...
                    prev_char == ')' && curr_char == '.'  ||     // ).5...
                    prev_char == '.' && curr_char == '('  ||     // 5.(...
                    curr_char_is_op && curr_char != ')' && index == expression.length() - 1 ||   // 9+8+
                    curr_char_is_op && prev_char_is_op && (curr_char != '(' && curr_char != ')' && prev_char != ')' && curr_char != '-') || // */
                    curr_char == '(' && is_number(prev_char) ||        // 7(
                    prev_char == ')' && is_number(curr_char)           // )9
            )
                return -1;
        }

        // Check parens
        if (curr_char == '(')                   // Character is opening (
        {
            // Set index to closing paren
            index = is_valid(index + 1, true, found_decimal);
            if (index == -1)
                return -1;
        }
        else if (curr_char == ')' && searching_for_paren)    // Found matching closing )
            return index;
        else if (curr_char == ')')                           // Found closing ) w/o opening (
            return -1;

        return is_valid(++index, searching_for_paren, found_decimal);
    }

    private boolean is_allowable(char to_check)
    {
        return is_number(to_check) || is_operator(to_check) || to_check == '.';
    }

    private boolean is_number(char to_check)
    {
        for (char character : numbers)
        {
            if (to_check == character)
                return true;
        }
        return false;
    }

    private boolean is_operator(char to_check)
    {
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

    public double calculate() throws NullPointerException
    {
        if (this.expression == null)
            throw new NullPointerException("Null expression.");

        String post_fix = make_post_fix();
        return calculate_answer(post_fix);
    }

    private String make_post_fix() throws NullPointerException
    {
        if (this.expression == null)
            throw new NullPointerException("Null expression.");

        Stack<Character> operators = new Stack<>();
        StringBuilder post_fix_expression = new StringBuilder();
        char curr_symbol;
        char prev_symbol = '\0';

        for (int i = 0; i < expression.length(); ++i)
        {
            curr_symbol = expression.charAt(i);
            if (i > 0) prev_symbol = expression.charAt(i - 1);
            if (is_number(curr_symbol) || curr_symbol == '.')
                post_fix_expression.append(curr_symbol);
            else
            {
                post_fix_expression.append(' ');
                if (operators.isEmpty() || curr_symbol == '(')
                {
                    if (curr_symbol == '-' && (prev_symbol == '\0' || is_operator(prev_symbol)))
                        post_fix_expression.append("0 ");
                    operators.push(curr_symbol);
                }
                else if (curr_symbol == ')')
                {
                    char top_op;
                    while ((top_op = operators.pop()) != '(')
                        post_fix_expression.append(top_op);
                }
                else if (curr_symbol == '-' && is_operator(prev_symbol))
                {
                    post_fix_expression.append("0 ");
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
                        post_fix_expression.append(top_op);
                        while (!operators.isEmpty() && compare_precedence((top_op = operators.pop()), curr_symbol) >= 0)
                        {
                            if (top_op == '(') {
                                operators.push(top_op);
                                break;
                            }
                            else
                                post_fix_expression.append(top_op);
                        }
                    }
                    operators.push(curr_symbol);
                }
            }
        }

        while (!operators.isEmpty())
            post_fix_expression.append(operators.pop());

        return post_fix_expression.toString();
    }

    private double calculate_answer(String post_fix_expression)
    {
        // We are getting a post-fix expression with at least one space between each double
        // 6 4 *2 5 /+3-

        Stack<Double> operands = new Stack<>();
        StringBuilder current_symbol = new StringBuilder();
        int running_index = 0;

        while (running_index < post_fix_expression.length())
        {
            // Skip initial whitespace
            for (; running_index < post_fix_expression.length() && post_fix_expression.charAt(running_index) == ' '; ++running_index);
            if (running_index == post_fix_expression.length()) break;

            if (post_fix_expression.charAt(running_index) == '.' || is_number(post_fix_expression.charAt(running_index))) {
                // Get first number
                for (; running_index < post_fix_expression.length(); ++running_index) {
                    if (post_fix_expression.charAt(running_index) == ' ' || is_operator(post_fix_expression.charAt(running_index)))
                        break;
                    else
                        current_symbol.append(post_fix_expression.charAt(running_index));
                }
                operands.push(Double.parseDouble(current_symbol.toString()));
                current_symbol = new StringBuilder();
            }
            else
            {
                double second = operands.pop();
                double first = operands.pop();
                char op = post_fix_expression.charAt(running_index);
                operands.push(choose_operator(first, op, second));
                ++running_index;
            }
        }

        return operands.pop();
    }

    private double choose_operator(double first, char op, double second) throws ArithmeticException
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
        return 0.0;
    }

    private void init()
    {
        expression = null;
        numbers    = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        operators    = new operator[4];
        operators[0] = new operator('^');
        operators[1] = new operator('*');
        operators[1].set_next(new operator('/'));
        operators[2] = new operator('+');
        operators[2].set_next(new operator('-'));
        operators[3] = new operator('(');
        operators[3].set_next(new operator(')'));
    }

    private int compare_precedence(char left, char right)
    {
        int left_rank  = find_rank(left);
        int right_rank = find_rank(right);

        return Integer.compare(left_rank, right_rank);
    }

    private int find_rank(char to_find) throws IndexOutOfBoundsException
    {
        operator character;
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