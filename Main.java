import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {
    public static void main(String[] args) throws Exception {
        String outputResult;
        System.out.println("Enter an expression with two operands:");
        Scanner in = new Scanner(System.in);
        String s = in.nextLine().strip();
	try{
        	Expression expression = Main.parseExpression(s);
        	boolean isRoman = expression.getIsRoman();
        	int result = Main.calculate(expression);
        	if (isRoman)
            		outputResult = Main.arabicToRoman(result);
        	else outputResult = String.valueOf(result);
	}
	catch (Exception e){
		System.out.println("Exception! " + e.getMessage());
		return;
	}
        System.out.println("Result: " + outputResult);
    }

    public static int calculate(Expression expression){
        int result = 0;
        int leftOperand = expression.getLeftOperand();
        int rightOperand = expression.getRightOperand();
        String operator = expression.getOperator();
        switch (operator) {
            case "+":
                result = leftOperand + rightOperand;
                break;
            case "-":
                result = leftOperand - rightOperand;
                break;
            case "*":
                result = leftOperand * rightOperand;
                break;
            case "/":
                result = leftOperand / rightOperand;
        }
        return result;
    }
    public static Expression parseExpression(String s) throws Exception {
        String operator;
        int leftOperand;
        int rightOperand;
        String arabicRegex = "^([0-9]{1,2})\\s*([*+/-])\\s*([0-9]{1,2})$";
        String romanRegex = "^(XI?|(?:IX|IV|V?I{0,3}))\\s*([*+/-])\\s*(XI?|(?:IX|IV|V?I{0,3}))$";
        Pattern arabicPattern = Pattern.compile(arabicRegex);
        Pattern romanPattern = Pattern.compile(romanRegex);
        Matcher arabicMatcher = arabicPattern.matcher(s);
        Matcher romanMatcher = romanPattern.matcher(s);
        boolean isArabic = arabicMatcher.matches();
        boolean isRoman = romanMatcher.matches();
        if (!(isArabic || isRoman))
            throw new Exception("Incorrect number(s) in the operands");
        if (isRoman) {
            leftOperand = Main.romanToArabic(romanMatcher.group(1));
            rightOperand = Main.romanToArabic(romanMatcher.group(3));
            operator = romanMatcher.group(2);
        }else {
            leftOperand = Integer.parseInt(arabicMatcher.group(1));
            rightOperand = Integer.parseInt(arabicMatcher.group(3));
            operator = arabicMatcher.group(2);
        }
        if (leftOperand > 10 || rightOperand > 10)
            throw new Exception("Incorrect number(s) in the operands");
	Expression expression = new Expression(leftOperand, rightOperand, operator, isRoman);
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
    private static String arabicToRoman(int arabicNumber) throws Exception {
        Map<Integer, String> numbers = new HashMap<>();
        numbers.put(1, "I");
        numbers.put(5, "V");
        numbers.put(10, "X");
        numbers.put(50, "L");
	numbers.put(100, "C");
        String result = "";
        if (arabicNumber > 100)
            throw new Exception("Too big roman number in the result");
        if (arabicNumber <= 0)
            throw new Exception("Too small roman number in the result");
        int remainder = arabicNumber;
        while(remainder > 0) {
	    if (remainder == 100){
		result += numbers.get(100);
		remainder -= 100;
		continue;
	    }
	    if (remainder - 100 >= -10){
		result += "X" + numbers.get(100);
		remainder -= 90;
		continue;
	    }	
	    if (remainder >= 50){
		result +=numbers.get(50);
		remainder -= 50;
		continue;
	    }
	    if (remainder - 50 >= -10){
		result += "X" + numbers.get(50);
		remainder -= 40;
		continue;
	    }
            if (remainder >= 10){
		result += numbers.get(10);
		remainder -= 10;
		continue;
	    }
	    if (remainder - 10 == -1){
		result += "I" + numbers.get(10);
		break;
	    }	
            if (remainder >= 5) {
                result += numbers.get(5);
                remainder -= 5;
		continue;
	    }
            if (remainder - 5 == -1){
		result += "I" + numbers.get(5);	
		break;
            }
            result += numbers.get(1);
            remainder -= 1;
        }
        return result;
    }
}

class Expression{
        private int leftOperand;
        private int rightOperand;
        private String operator;       
        private boolean isRoman;

        public Expression(int leftOperand, int rightOperand, String operator, boolean isRoman){
        	this.leftOperand = leftOperand;
        	this.rightOperand = rightOperand;
        	this.operator = operator;
        	this.isRoman = isRoman;
        }
        
        public int getLeftOperand(){
        	return leftOperand;
        }

        public void setLeftOperand(int leftOperand){
        	this.leftOperand = leftOperand;
        }

        public int getRightOperand(){
        	return rightOperand;
        }

        public void setRightOperand(int rightOperand){
        	this.rightOperand = rightOperand;
        }

        public String getOperator(){
        	return operator;
        }

        public void setOperator(String operator){
        	this.operator = operator;
        }

        public boolean getIsRoman(){
        	return isRoman;
        }

        public void setIsRoman(boolean isRoman){
        	this.isRoman = isRoman;
        }
}
