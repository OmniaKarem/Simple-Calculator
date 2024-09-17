package com.backendcalculator.backendcalculator;

import org.springframework.web.bind.annotation.*;

import java.util.Stack;

@RestController
public class BackendCalculatorController {

    @CrossOrigin()
    @PostMapping("/{result}")
    public String operate(@RequestBody String exp) {

        for (int i = 0; i < exp.length(); i++) {
            if (!detectErrors(exp)) {
                return "E";
            }
        }

        Stack<Double> value = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < exp.length(); i++) {
            if (Character.isDigit(exp.charAt(i))) {
                double number = 0;

                while ((i < exp.length()) && (Character.isDigit(exp.charAt(i)))) {
                    number = number * 10 + (exp.charAt(i) - '0');
                    i++;
                }
                value.push(number);
                i--;
            } else if (exp.charAt(i) == '.') {
                double number = value.pop();
                int z = 1;
                while (((i + 1) < exp.length()) && (Character.isDigit(exp.charAt(i + 1)))) {
                    number = number + ((double) (exp.charAt(i + 1) - '0') / Math.pow(10, z));
                    z++;
                    i++;
                }
                value.push(number);
            } else if (exp.charAt(i) == '+' || exp.charAt(i) == '-' || exp.charAt(i) == '*' || exp.charAt(i) == '/' || exp.charAt(i) == '^' || exp.charAt(i) == '%' || exp.charAt(i) == '√') {
                while ((!operators.isEmpty()) && (precedence(operators.peek()) >= precedence(exp.charAt(i)))) {
                    if (exp.charAt(i) == '+' || exp.charAt(i) == '-' || exp.charAt(i) == '*' || exp.charAt(i) == '/' || exp.charAt(i) == '^') {
                        char op = operators.pop();
                        double val1 = value.pop();
                        double val2 = value.pop();
                        value.push(applyOperator(op, val1, val2));
                    } else if (exp.charAt(i) == '%') {
                        char op = operators.pop();
                        double val = value.pop();
                        value.push(val / 100);
                    } else if (exp.charAt(i) == '√') {
                        char op = operators.pop();
                        double val = value.pop();
                        value.push(Math.sqrt(val));
                    }
                }
                operators.push(exp.charAt(i));
            }
        }

        while (!operators.isEmpty()) {
            char op = operators.pop();
            if (op == '+' || op == '-' || op == '*' || op == '^') {
                double val1 = value.pop();
                double val2 = value.pop();
                value.push(applyOperator(op, val1, val2));
            } else if (op == '/') {
                double val1 = value.pop();
                double val2 = value.pop();
                if (val1 == 0) {
                    return "E";
                } else {
                    value.push(applyOperator(op, val1, val2));
                }
            } else if (op == '%') {
                double val = value.pop();
                value.push(val / 100);
            } else if (op == '√') {
                double val = value.pop();
                if (val < 0) {
                    return "E";
                } else {
                    value.push(Math.sqrt(val));
                }
            }
        }
        return String.valueOf(value.pop());
    }


    public int precedence(char op1) {
        if (op1 == '+' || op1 == '-') {
            return 1;
        } else if (op1 == '*' || op1 == '/' || op1 == '%') {
            return 2;
        } else if (op1 == '^' || op1 == '√') {
            return 3;
        } else {
            return 0;
        }
    }

    public double applyOperator(char op, double val1, double val2) {
        double result = 0;
        if (op == '^') {
            result = Math.pow(val2, val1);
        } else if (op == '*') {
            result = val1 * val2;
        } else if (op == '/') {
            result = val2 / val1;
        } else if (op == '+') {
            result = val1 + val2;
        } else if (op == '-') {
            result = val2 - val1;
        }
        return result;
    }

    public boolean detectErrors(String exp) {
        exp = exp.substring(1, exp.length() - 1);
        boolean works = true;
        for (int i = 0; i < exp.length() - 1; i++) {

            if ((exp.charAt(i) == '+') || (exp.charAt(i) == '^') || (exp.charAt(i) == '-') || (exp.charAt(i) == '/') || (exp.charAt(i) == '*') || (exp.charAt(i) == '√')) {
                if ((Character.isDigit(exp.charAt(i + 1))) || (exp.charAt(i + 1) == '√')) {
                    works = true;
                } else {
                    works = false;
                    break;
                }
            } else if (exp.charAt(i) == '√' && exp.charAt(i + 1) == '-') {
                works = false;
                break;
            } else if (exp.charAt(i) == '/' && exp.charAt(i + 1) == '0') {
                works = false;
                break;
            }
        }
        if ((!(Character.isDigit(exp.charAt(0))) && !((exp.charAt(0)) != '+') && !((exp.charAt(0)) != '-') && !((exp.charAt(0)) != '√')) || (!(Character.isDigit(exp.charAt(exp.length() - 1))) && (exp.charAt(exp.length() - 1) != '%'))) {
            works = false;
        }
        return works;
    }


}
