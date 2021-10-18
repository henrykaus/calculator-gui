package com.henrykaus.calculatorgui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * PURPOSE: Controller for the calculator-view root GridPane
 */
public class calculator_controller
{
    @FXML private TextField  answer;                // Double answer for expression
    @FXML private TextField  expression_field;      // Where expression is typed in
    private final calculator program_calculator;    // Calculator to perform operators and store expression String

    // Default Constructor
    public calculator_controller()
    {
        program_calculator = new calculator();
    }

    /*
     * PURPOSE: Perform expression operations on equals click or return press
     */
    @FXML protected void equals_button_click()
    {
        String expression = expression_field.getCharacters().toString();

        if (expression.equals(""))
            expression = null;
        program_calculator.set_expression(expression);

        try {
            if (program_calculator.is_valid())
                answer.setText(Double.toString(program_calculator.calculate()));
            else
                answer.setText("INVALID");
        }
        catch(NullPointerException exception) {
            answer.setText("0");
        }
        catch(ArithmeticException exception) {
            answer.setText("DIV BY 0");
        }
        catch(IndexOutOfBoundsException exception) {
            answer.setText("INTERNAL ERR");
        }
    }

    /**
     * PURPOSE: Empty the expression_field to ""
     */
    @FXML protected void clear_button_click()
    {
        expression_field.setText("");
    }

    /**
     * PURPOSE: Delete the last symbol from the expression in the expression_field
     */
    @FXML protected void delete_button_click()
    {
        String new_string = expression_field.getCharacters().toString();

        if (new_string.length() == 0)
            return;
        else if (new_string.length() == 1)
            expression_field.setText("");

        expression_field.setText(new_string.substring(0, new_string.length() - 1));
    }

    /**
     * PURPOSE: Negate the expression by surrounding current expression with "-()"
     *    also: Will remove -() if it detects one already there and won't negate an empty expression_field
     */
    @FXML protected void negate_button_click()
    {
        String expression_string = expression_field.getCharacters().toString();
        if (expression_string.equals(""))
            return;
        else if (expression_string.length() >= 3 && expression_string.charAt(0) == '-' && expression_string.charAt(1) == '(' && expression_string.charAt(expression_string.length() - 1) == ')')
            expression_field.setText(expression_string.substring(2, expression_string.length() - 1));
        else
            expression_field.setText("-(" + expression_field.getCharacters().toString() + ")");
    }

    /**
     * PURPOSES: Append symbol to end of expression_field
     */
    @FXML protected void left_paren_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "(");
    }

    @FXML protected void right_paren_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + ")");
    }

    @FXML protected void power_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "^");
    }

    @FXML protected void divides_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "/");
    }

    @FXML protected void seven_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "7");
    }

    @FXML protected void eight_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "8");
    }

    @FXML protected void nine_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "9");
    }

    @FXML protected void times_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "*");
    }

    @FXML protected void four_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "4");
    }

    @FXML protected void five_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "5");
    }

    @FXML protected void six_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "6");
    }

    @FXML protected void minus_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "-");
    }

    @FXML protected void one_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "1");
    }

    @FXML protected void two_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "2");
    }

    @FXML protected void three_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "3");
    }

    @FXML protected void plus_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "+");
    }

    @FXML protected void zero_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + "0");
    }

    @FXML protected void decimal_button_click()
    {
        expression_field.setText(expression_field.getCharacters().toString() + ".");
    }
}