import java.awt.*;
import  java.awt.event.*;
import java.io.*;
import java.util.*;
//import javax.swing.*;

class Quiz extends Frame implements ActionListener{
	private Label lblq;
	private Checkbox choice1;
	private Checkbox choice2;
	private Checkbox choice3;
	private Checkbox choice4;
	private CheckboxGroup cg;
	private Button nextbt;
	private Panel p;
	private HashMap<Integer, String>  qchoices;
	private HashMap<Integer, String> qselectedans;
	private HashMap<Integer, String> qcorrectans;
	private int qindex;
	
	//constructor of Quiz
	Quiz(String title){
		//System.out.println("hello");
		setTitle(title);
		setLayout(new BorderLayout());
		setSize(new Dimension(500,300));
		setBackground(Color.LIGHT_GRAY);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		//adding menubar and menu options
		MenuBar mb=new MenuBar();
		Menu mf=new Menu("File");
		MenuItem iadd=new MenuItem("Add...");
		iadd.addActionListener(this);
		MenuItem iexit=new MenuItem("Exit");
		iexit.addActionListener(this);
		mf.add(iadd);
		mf.add(iexit);
		mb.add(mf);
		setMenuBar(mb);

		cg=new CheckboxGroup();
		choice1=new Checkbox("C++",false,cg);
		choice2=new Checkbox("VB.NET",false,cg);
		choice3=new Checkbox("Java",true,cg);
		choice4=new Checkbox("Software Engineering",false,cg);
		p=new Panel();
		p.setLayout(new GridLayout(6,1));
		lblq=new Label("What is...");
		lblq.setForeground(Color.BLUE);
		lblq.setFont(new Font("Arial", 0, 20));
		p.add(lblq);
		p.add(new Label("Select the correct answer:"));
		p.add(choice1);
		p.add(choice2);
		p.add(choice3);
		p.add(choice4);
		add(p,BorderLayout.CENTER);
		nextbt=new Button("Next");
		nextbt.setBackground(Color.LIGHT_GRAY);
		nextbt.addActionListener(this);
		add(nextbt, BorderLayout.SOUTH);

		//used to close the window
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent ev)
			{
				//System.exit(0);
				ev.getWindow().dispose();
			}
		});

		setVisible(true);
		reload();
	}
	
	//loads information
	public void reload(){
		init();		
		loadQchoices("qchoices.txt");
		loadAnswers("qcorrectans.txt");
		nextbt.setLabel("Next");
		moveFirst();
	}
	
	public void showCom(boolean hide){
		lblq.setVisible(hide);
		p.setVisible(hide);
		nextbt.setVisible(hide);	
		
	}
	

	public void moveTo(int qin){
		String str=qchoices.get(qin);
		String[] parts=str.split("_");		
		lblq.setText(parts[0]);
		choice1.setLabel(parts[1]);
		choice2.setLabel(parts[2]);
		choice3.setLabel(parts[3]);
		choice4.setLabel(parts[4]);
		
	}

	//initializes and loads set of questions and options
	public void init(){
		qchoices=new HashMap<Integer, String>();
		qselectedans=new HashMap<Integer, String>();
		qcorrectans=new HashMap<Integer, String>();
		
	}

	public void loadQchoices(String filename){
		
		try{
			File f=new File(filename);			
			if(f.exists() && f.length()>0){
				FileReader fr=new FileReader(filename);
				BufferedReader br=new BufferedReader(fr);
				String str="";
				int index=0;
				while((str=br.readLine())!=null){
					qchoices.put(index,str);
					index++;
				}
		
				br.close();
				showCom(true);
			}
			else{
				showCom(false);
			}
		}catch(IOException ie){}
	}
	
	public void loadAnswers(String filename){
		
		try{
		File f=new File(filename);
		if(f.exists() && f.length()>0){
		FileReader fr=new FileReader(filename);
		BufferedReader br=new BufferedReader(fr);
		String str="";
		int index=0;
		while((str=br.readLine())!=null){
			qcorrectans.put(index,str);
			index++;
		}
		
			br.close();
			showCom(true);
		}
		else{
			showCom(false);
		}
		}catch(IOException ie){}
	}
	public void moveFirst(){
		qindex=0;
		moveTo(qindex);
	}
	public void moveNext(){
		addSelectedAnswer();
		if(qindex<qchoices.size()-1){
			qindex++;
			moveTo(qindex);
		}
		
	}
	
	//generates report as an html page
	public void showReport(){
		try{
		FileWriter fw=new FileWriter("qreport.html");
		BufferedWriter bw=new BufferedWriter(fw);
		bw.write("<html><head><title>Quiz report</title></head>");
		bw.newLine();
		bw.write("<body>");
		bw.write("<h1>Quiz Report</h1>");
		bw.newLine();
		bw.write("<ol>");
		for(int i=0;i<qchoices.size();i++){
			String str=qchoices.get(i);
			bw.write("<li style='color:#996633;font-size:15pt'>");
			bw.write(str.substring(0,str.indexOf('_')));
			bw.write("<ul style='color:#000000;font-size:12pt'>");
			bw.write("<li>Your answer: "+qselectedans.get(i)+"</li>");
			bw.write("<li>Correct answer: "+qcorrectans.get(i)+"</li>");
			bw.write("</ul>");
			bw.write("</li>");
			
		}
		bw.write("</ol>");
		bw.write("</body>");
		bw.newLine();
		bw.write("</html>");		
		bw.close();
		//show the qreport.html file
		Desktop dt=Desktop.getDesktop();
		if(Desktop.isDesktopSupported())
			dt.open(new File("qreport.html"));
		}catch(IOException e){}
	}
		class AddQ extends Frame implements ActionListener{
			TextField txtq;
			TextField txtc;
			Button btadd;
			Button btsave;
			Button btclose;
			Panel p2;
			CheckboxGroup cg;
			int count=0;
			
			AddQ(String title){
				setTitle(title);
				setSize(500,200);
				addWindowListener(new WindowAdapter(){
					
					public void windowClosing(WindowEvent e){						
						e.getWindow().dispose();
						reload();
					}
				});
				setResizable(false);
				setBackground(Color.LIGHT_GRAY);
				setLayout(new BorderLayout());
				Panel p=new Panel(new FlowLayout(FlowLayout.LEADING));
				txtq=new TextField(30);
				txtq.addFocusListener(new ListFocus());
				p.add(new Label("Enter a question:"));
				p.add(txtq);
				Panel p1=new Panel(new FlowLayout(FlowLayout.LEFT));
				p1.add(new Label("Enter an answer choice:"));
				txtc=new TextField(20);
				p1.add(txtc);
				Panel gp=new Panel(new GridLayout(2,1));
				gp.add(p);
				gp.add(p1);
				
				btadd=new Button("Add");
				btadd.addActionListener(this);
				p1.add(btadd);
				cg=new CheckboxGroup();				
				p2=new Panel(new GridLayout(4,1));
				btsave=new Button("Save");
				btsave.addActionListener(this);
				btclose=new Button("Close");
				btclose.addActionListener(this);
				add(gp,BorderLayout.NORTH);
				add(p2,BorderLayout.CENTER);
				setVisible(true);
			}
			
			class ListFocus implements FocusListener{
				public void focusGained(FocusEvent e){
					
				}
				public void focusLost(FocusEvent e){
					if(txtq.getText().trim().length()<=0){
						txtq.requestFocus();
						
					}
				}
			}
			public void actionPerformed(ActionEvent e){
				if(e.getActionCommand()=="Add"){
					if(count<4){
						String textch=txtc.getText().trim();
						if(textch.length()>0){
							Checkbox ch=new Checkbox(textch,true,cg);
							p2.add(ch);
							count++;
							}
						if(count==4){	
							p2.add(btsave);
							p2.add(btclose);
						}
						validate();					
				
					}
					
				}
				else if(e.getActionCommand()=="Save"){
					
					if(txtq.getText().trim().length()>0){
						saveQChoices("qchoices.txt");
						saveCorrectAns("qcorrectans.txt");
						reset();
					}
					
				}
				else if(e.getActionCommand()=="Close"){
					dispose();

					reload();
				}
				
			}
			public void reset(){
				count=0;
				p2.removeAll();
				validate();
				txtq.setText("");
				txtc.setText("");
			}
			public void saveQChoices(String filename){
				try{
				FileWriter fw=new FileWriter(filename,true);
				BufferedWriter bw=new BufferedWriter(fw);
				String strq=txtq.getText()+"_";
				for(int i=0;i<p2.getComponentCount();i++){
					Component c=p2.getComponent(i);
					if(c instanceof Checkbox){
						strq+=((Checkbox) c).getLabel()+"_";
					}
				}
				bw.write(strq);
				bw.newLine();				
				bw.close();
				}catch(IOException e){}
			}
			public void saveCorrectAns(String filename){
				Checkbox ch=(Checkbox)cg.getSelectedCheckbox();
				String strc=ch.getLabel();
				try{
					FileWriter fw=new FileWriter(filename,true);
					BufferedWriter bw=new BufferedWriter(fw);					
					bw.write(strc);
					bw.newLine();					
					bw.close();
					}catch(IOException e){}
			}
		}
	
	
	public void movePrev(){
		if(qindex>0){
			qindex--;
			moveTo(qindex);
		}
		
	}
	public void addSelectedAnswer(){
		Checkbox selc=cg.getSelectedCheckbox();
		qselectedans.put(qindex, selc.getLabel());
		
	}
	public Insets getInsets(){
		return new Insets(50,30,15,15);
	}
	
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Next")){
			moveNext();
			if(qindex==qchoices.size()-1)
				nextbt.setLabel("Generate report");
		}
		else if(e.getActionCommand().equals("Generate report")){
			addSelectedAnswer();
			showReport();
		}
		else if(e.getActionCommand().equals("Add...")){
			new AddQ("Add questions");
		}
		else if(e.getActionCommand().equals("Exit")){
			System.exit(0);
		}
	}
	
	
}
// public class SQuiz {
// 	public static void main(String[] args){
// 		new Show("Java Quiz");
		
// 	}
// }

