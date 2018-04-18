import java.awt.*;
import  java.awt.event.*;
import java.io.*;
import java.util.*;
//import javax.swing.*;

class FunApp extends Frame implements ActionListener{

	Button calcButton = new Button("Calculator");
	Button quizButton = new Button("Quiz");
	Button tttButton = new Button("Tic Tac Toe");

	Label title = new Label("Fun App!");

	FunApp() {
		super();
		setTitle("FunApp");
		setSize(400, 400);
		
		calcButton.setBounds(120, 125, 150, 50);
		calcButton.addActionListener(this);
		quizButton.setBounds(120, 225, 150, 50);
		quizButton.addActionListener(this);
		tttButton.setBounds(120, 325, 150, 50);
		tttButton.addActionListener(this);
		title.setFont(new Font("ComicSans", 0, 30));
		title.setBounds(120, 40, 150, 50);

		this.add(calcButton);
		this.add(quizButton);
		this.add(tttButton);
		this.add(title);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println(((Button)e.getSource()).getLabel());

		if (((Button)e.getSource()).getLabel() == "Calculator") {
			new Calculator();
		}

		else if (((Button)e.getSource()).getLabel() == "Quiz"){
			new Quiz("Java Quiz");
		}

		else if (((Button)e.getSource()).getLabel() == "Tic Tac Toe"){
			
			boolean start = false; //if false computer starts
        	TicTacToe game = new TicTacToe(start);

        	game.playGame(start,game);
		}


	}

	public static void main(String[] args) {
		FunApp app = new FunApp();

		app.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent ev)
			{
				//System.exit(0);
				ev.getWindow().dispose();
				//reload();
			}
		});

		app.setLayout(null);
		app.setVisible(true);
	}
}






