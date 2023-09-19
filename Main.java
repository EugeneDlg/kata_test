import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {
    public static void main(String[] args) throws Exception {
        String output_result;
        System.out.println("Enter an expression with two operands:");
        Scanner in = new Scanner(System.in);
        String s = in.nextLine().strip();
	try{
        	Map<String, Object> expression = Main.parseExpression(s);
        	boolean isRoman = (boolean) expression.get("isRoman");
        	int result = Main.calculate(expression);
        	if (isRoman)
            		output_result = Main.arabicToRoman(result);
        	else output_result = String.valueOf(result);
	}
	catch (Exception e){
		System.out.println(e.getMessage());
		return;
	}
        System.out.println("Result: " + output_result);
    }

    public static int calculate(Map expression){
        int result = 0;
        int left_operand = (int) expression.get("left_operand");
        int right_operand = (int) expression.get("right_operand");
        String operator = (String) expression.get("operator");
        switch (operator) {
            case "+":
                result = left_operand + right_operand;
                break;
            case "-":
                result = left_operand - right_operand;
                break;
            case "*":
                result = left_operand * right_operand;
                break;
            case "/":
                result = left_operand / right_operand;
        }
        return result;
    }
    public static Map<String, Object> parseExpression(String s) throws Exception {
        String operator;
        int left_operand;
        int right_operand;
        String arabic_regex = "^([0-9]{1,2})\\s*([*+/-])\\s*([0-9]{1,2})$";
        String roman_regex = "^(XI?|(?:IX|IV|V?I{0,3}))\\s*([*+/-])\\s*(XI?|(?:IX|IV|V?I{0,3}))$";
        Pattern arabic_pattern = Pattern.compile(arabic_regex);
        Pattern roman_pattern = Pattern.compile(roman_regex);
        Matcher arabic_matcher = arabic_pattern.matcher(s);
        Matcher roman_matcher = roman_pattern.matcher(s);
        boolean isArabic = arabic_matcher.matches();
        boolean isRoman = roman_matcher.matches();
        if (!(isArabic || isRoman))
            throw new Exception("Incorrect expression format");
        if (isRoman) {
            left_operand = Main.romanToArabic(roman_matcher.group(1));
            right_operand = Main.romanToArabic(roman_matcher.group(3));
            operator = roman_matcher.group(2);
        }else {
            left_operand = Integer.parseInt(arabic_matcher.group(1));
            right_operand = Integer.parseInt(arabic_matcher.group(3));
            operator = arabic_matcher.group(2);
        }
        if (left_operand > 10 || right_operand > 10)
            throw new Exception("Too big number in operands");
        Map<String, Object> expression = new HashMap<String, Object>();
        expression.put("left_operand", left_operand);
        expression.put("right_operand", right_operand);
        expression.put("operator", operator);
        expression.put("isRoman", isRoman);
        return expression;
    }
    private static int romanToArabic(String roman){
        Map<String, Integer> numbers = new HashMap<>();
        numbers.put("I", 1);
        numbers.put("V", 5);
        numbers.put("X", 10);
        int result = 0;
        String[] symbols = roman.split("");

        for (int i=0; i < symbols.length - 1; i++) {
            if (numbers.get(symbols[i]) < numbers.get(symbols[i+1]))
                result -= numbers.get(symbols[i]);
            else
                result += numbers.get(symbols[i]);
        }
        result += numbers.get(symbols[symbols.length - 1]);
        return result;
    }
    private static String arabicToRoman(int arabic_number) throws Exception {
        Map<Integer, String> numbers = new HashMap<>();
        numbers.put(1, "I");
        numbers.put(4, "IV");
        numbers.put(5, "V");
        numbers.put(9, "IX");
        numbers.put(10, "X");
        String result = "";
        if (arabic_number > 10)
            throw new Exception("Too big roman number in the result");
        if (arabic_number <= 0)
            throw new Exception("Too small roman number in the result");
        int temp_number = arabic_number;
        while(temp_number > 0) {
            if (temp_number == 10)
                return numbers.get(10);
            if (temp_number == 9)
                return numbers.get(9);
            if (temp_number == 4)
                return numbers.get(4);
            if (temp_number >= 5) {
                result += numbers.get(5);
                temp_number = temp_number - 5;
            }else {
                result += numbers.get(1);
                temp_number = temp_number - 1;
            }
        }
        return result;
    }
}
