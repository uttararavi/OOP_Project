import java.awt.*;
import java.awt.event.*;
//import javax.swing.*;
import java.io.*;
import java.util.*;
import java.lang.Math.*;


class Calculator extends Frame {

	//public boolean setClear=true;
	//public boolean doublePress = false;
	double number, memValue;
	char op;
	String expression ;

	String digitButtonText[] = {"7", "8", "9", "4", "5", "6", "1", "2", "3", ")", "0", "." };
	String operatorButtonText[] = {"/", "sqrt", "*", "(", "-", "log", "+", "=" , "exp", "sin", "cos" , "tan" };
	String specialButtonText[] = {"Backspc", "C", "C" };


	MyDigitButton digitButton[]=new MyDigitButton[digitButtonText.length];
	MyOperatorButton operatorButton[]=new MyOperatorButton[operatorButtonText.length];
	MySpecialButton specialButton[]=new MySpecialButton[specialButtonText.length];

	Label displayLabel=new Label(" ",Label.RIGHT);
	Label memLabel=new Label(" ",Label.RIGHT);

	final int FRAME_WIDTH = 325, FRAME_HEIGHT = 325;
	final int HEIGHT = 30, WIDTH = 30, H_SPACE = 10, V_SPACE = 10;
	final int TOPX = 30, TOPY = 50;

	Calculator() {

		super();

		int tempX=TOPX, y=TOPY;

		displayLabel.setBounds(tempX,y,240,HEIGHT);
		displayLabel.setBackground(Color.BLUE);
		displayLabel.setForeground(Color.WHITE);
		add(displayLabel);

		memLabel.setBounds(TOPX,  TOPY+HEIGHT+ V_SPACE,WIDTH, HEIGHT);
		add(memLabel);

		//set Co-ordinates for Special Buttons
		tempX=TOPX+1*(WIDTH+H_SPACE); y=TOPY+1*(HEIGHT+V_SPACE);

		for(int i=0;i<specialButton.length;i++)
		{
			specialButton[i]=new MySpecialButton(tempX,y,WIDTH*2,HEIGHT,specialButtonText[i], this);
			specialButton[i].setForeground(Color.RED);
			add(specialButton[i]);
			tempX=tempX+2*WIDTH+H_SPACE;
		}

		//set Co-ordinates for Digit Buttons
		int digitX=TOPX+WIDTH+H_SPACE;
		int digitY=TOPY+2*(HEIGHT+V_SPACE);
		tempX=digitX;  y=digitY;

		for(int i=0;i<digitButton.length;i++)
		{
			digitButton[i]=new MyDigitButton(tempX,y,WIDTH,HEIGHT,digitButtonText[i], this);
			add(digitButton[i]);
			digitButton[i].setForeground(Color.BLUE);
			
			tempX+=WIDTH+H_SPACE;
			if((i+1)%3==0){tempX=digitX; y+=HEIGHT+V_SPACE;}
		}

		//set Co-ordinates for Operator Buttons
		int opsX=digitX+2*(WIDTH+H_SPACE)+H_SPACE;
		int opsY=digitY;
		tempX=opsX;  y=opsY; 
		int i;
		for(i=0;i<operatorButton.length - 4;i++)
		{
			tempX+=WIDTH+H_SPACE;
			operatorButton[i]=new MyOperatorButton(tempX,y,WIDTH,HEIGHT,operatorButtonText[i], this);
			operatorButton[i].setForeground(Color.RED);
			add(operatorButton[i]);
			if((i+1)%2==0)
			{
				tempX=opsX; y+=HEIGHT+V_SPACE;
			}
		}

		// set Co-ordinates for new operator part
		tempX=TOPX;	
		y=TOPY+2*(HEIGHT+V_SPACE);

		for(; i<operatorButton.length; i++)
		{
			operatorButton[i]=new MyOperatorButton(tempX,y,WIDTH,HEIGHT,operatorButtonText[i], this);
			operatorButton[i].setForeground(Color.RED);
			add(operatorButton[i]);
			y+=HEIGHT+V_SPACE;
		}

		//Used to close the calculator window (won't close if this part isn't there)
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent ev)
			{
				//System.exit(0);
				ev.getWindow().dispose();
			}
		});

		setLayout(null);
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
		setVisible(true);

	}

	String evalExp(String s){
		
		//convert all exp->e , sin->s , cos->c etc

		Stack <Character> bracStack= new Stack<Character>();

		// start putting it in buffer
		String buffer = "";

			// if s doenst contain any special operators, return eval(s)
			if(!(s.contains("e") || s.contains("s") || s.contains("c") || s.contains("t") || s.contains("l") || s.contains("r"))){
				
				if (s.charAt(0) == '-')
					return s;

				float result = EvaluateString.evaluate(s);
				// System.out.print(s+", "+result + ";");
				return Float.toString(result);
				
			//eval(s) converts infix to postfix
			//evaluates the expression
			}
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);


			if(c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == ' ' || Character.isDigit(c)){

				buffer = buffer + Character.toString(c);

			}

			else if( c == 'e' || c == 's' || c == 'c' || c == 't' || c == 'r' || c == 'l' ){

				char spFunc = c;
				i++; 
				c = s.charAt(i);
				bracStack.push(c);
				i++;
				String innerBuffer = "";

				while(!bracStack.empty()){
					innerBuffer += s.charAt(i);
					if (s.charAt(i) == '(')
						bracStack.push('(');
					else if (s.charAt(i) == ')')
						bracStack.pop();
					i++;
				}
				innerBuffer = innerBuffer.substring(0, innerBuffer.length() - 1);
				String innerResult = evalExp(innerBuffer);
				switch (spFunc)
				{
					case 'e' : buffer += Math.exp(Float.parseFloat(innerResult)); break;
					case 's' : buffer += Math.sin(Float.parseFloat(innerResult)); break;
					case 'c' : buffer += Math.cos(Float.parseFloat(innerResult)); break;
					case 't' : buffer += Math.tan(Float.parseFloat(innerResult)); break;
					case 'r' : buffer += Math.sqrt(Float.parseFloat(innerResult)); break;
					case 'l' : buffer += Math.log(Float.parseFloat(innerResult)); break;
				};
			}		

		}
		//when you see e/s/c/t/l/r add '(' to the stack, keep adding the stuff after that to the a new buffer
		//once you see a a ')' pop '(' from the stack
		//and then call eval on what you've read in the buffer and return that to the pehla buffer

		return evalExp(buffer);
	}

	public static void main(String[] args) {
		Calculator c = new Calculator();
		//System.out.println("hello");
	}
}

class MyDigitButton extends Button implements ActionListener{

	Calculator cl ;

	MyDigitButton(int x,int y, int width,int height,String cap, Calculator clc) {
		
		super(cap);
		setBounds(x,y,width,height);
		this.cl=clc;
		addActionListener(this);
	}

 
	public void actionPerformed(ActionEvent ev) {
		
		String tempText=((MyDigitButton)ev.getSource()).getLabel();

		// if (cl.displayLabel.getText().equals("0"))
		// 		cl.displayLabel.setText("");
		if(tempText == ")")
		{
			char last = cl.displayLabel.getText().charAt(cl.displayLabel.getText().length() - 1);
			if(!(last == '(')){
				if (Character.isDigit(last) || cl.displayLabel.getText().charAt(cl.displayLabel.getText().length() - 2) == ')'){
					cl.displayLabel.setText(cl.displayLabel.getText() + " " + tempText + " ");
					
				}
			}

		}		
		else {
			cl.displayLabel.setText(cl.displayLabel.getText() + tempText);
		}	
		
		
	}
}

class MyOperatorButton extends Button implements ActionListener {

	Calculator cl;

	MyOperatorButton(int x,int y, int width,int height,String cap, Calculator clc)
	{
		super(cap);
		setBounds(x,y,width,height);
		this.cl=clc;
		addActionListener(this);
	}


	public void actionPerformed(ActionEvent ev) {

		//avoiding double press
		// char binaryOperators[] = {'/', '*', '%', '-', '+'};
	
		String tempText=((MyOperatorButton)ev.getSource()).getLabel();
		
		if(tempText.equals("=")) { 

			cl.expression = cl.displayLabel.getText();

			cl.expression = cl.expression.replace("exp","e");
			cl.expression = cl.expression.replace("sin","s");
			cl.expression = cl.expression.replace("cos","c");
			cl.expression = cl.expression.replace("tan","t");
			cl.expression = cl.expression.replace("sqrt","r");
			cl.expression = cl.expression.replace("log","l");

			cl.displayLabel.setText(cl.evalExp(cl.expression));
		}
		else if (tempText.equals("("))
			cl.displayLabel.setText(cl.displayLabel.getText() + " " + tempText + " ");	

		else {
			if (tempText.length() == 1) {
				if(Character.isDigit(cl.displayLabel.getText().charAt(cl.displayLabel.getText().length() - 1)) || cl.displayLabel.getText().charAt(cl.displayLabel.getText().length() - 1) == ')'){
					cl.displayLabel.setText(cl.displayLabel.getText() + " " + tempText + " ");	
				}
			}
		
			else if (tempText.length() > 1) {
				if (cl.displayLabel.getText().length() > 0 && !Character.isDigit(cl.displayLabel.getText().charAt(cl.displayLabel.getText().length() - 1)))
					cl.displayLabel.setText(cl.displayLabel.getText() + tempText + "( ");	
				else if (cl.displayLabel.getText().length() == 0)
					cl.displayLabel.setText(cl.displayLabel.getText() + tempText + "( ");
			}
		
		}

	}
}


class MySpecialButton extends Button implements ActionListener {

	Calculator cl;

	MySpecialButton(int x,int y, int width,int height,String cap, Calculator clc) {
		super(cap);
		setBounds(x,y,width,height);
		this.cl=clc;
		addActionListener(this);
	}
	public void actionPerformed(ActionEvent ev) {

		String tempText = ((MySpecialButton)ev.getSource()).getLabel();

		if(tempText == "Backspc"){
			cl.displayLabel.setText(cl.displayLabel.getText().substring(0, Math.max(0, cl.displayLabel.getText().length()-1)));
		}

		else if (tempText == "C"){

			cl.displayLabel.setText(cl.displayLabel.getText().substring(0, Math.min(0, cl.displayLabel.getText().length()-1)));

		}


		
	}
}

