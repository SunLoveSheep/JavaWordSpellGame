import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

import javax.swing.JTextField;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook; 
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import java.io.*;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JRadioButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

// use implements MouseListener and ActionListener to include events listeners
public class WS_main_window implements MouseListener, ActionListener {	
	private JFrame frame;
	private JTextField textField;
	private static Vector<String> wordlist = new Vector<String>();
	private JButton btnA, btnB, btnC, btnD, btnE, btnF, btnG, btnH, btnI, btnJ, btnK, btnL,
	btnM, btnN, btnO, btnP, btnQ, btnR, btnS, btnT, btnU, btnV, btnW, btnX, btnY, btnZ;
	private JRadioButton radiobtn_playerfirst, radiobtn_AIfirst;
	private int Player_or_AI_First = 0; //0 for player first, 1 for AI first
	private static int shortestlength = 4;
	
	public static void TxtRead(){
		File f = new File("Allwords.txt");
		try 
		{
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while((line = br.readLine())!=null)
			{
				if (line.length()>=shortestlength)
				{
					wordlist.add(line);
				}
			}
			br.close();
			fr.close();
		}catch (FileNotFoundException e1) {
				e1.printStackTrace();
				} catch (IOException e) {
				e.printStackTrace();
				}		
}
	
	//Read words data from files to String
	public static void ExcelRead(){
		//public Ex1() { }
	    try {
	        FileInputStream file = new FileInputStream(new File("Allwords.xlsx"));

	        //Get the workbook instance for XLS file 
	        HSSFWorkbook workbook = new HSSFWorkbook(file);

	        //Get first sheet from the workbook
	        HSSFSheet sheet = workbook.getSheetAt(0);

	        //Iterate through each rows from first sheet
	        Iterator<Row> rowIterator = sheet.iterator();
	        while(rowIterator.hasNext()) {
	            Row row = rowIterator.next();

	            //For each row, iterate through each columns
	            Iterator<Cell> cellIterator = row.cellIterator();
	            //while(cellIterator.hasNext()) {                  
	                Cell cell = cellIterator.next();

	                switch(cell.getCellType()) {
	                    case Cell.CELL_TYPE_BOOLEAN:
	                        System.out.print(cell.getBooleanCellValue() + "\t");
	                        break;
	                    case Cell.CELL_TYPE_NUMERIC:
	                        System.out.print(cell.getNumericCellValue() + "\t");
	                        break;
	                    case Cell.CELL_TYPE_STRING:
	                        wordlist.add(cell.getStringCellValue());
	                        //System.out.print(wordlist.get(0) + "\t");
	                        break;
	                }
	            //}
	        }
	        file.close();
	        workbook.close();

	    } catch (FileNotFoundException e) {

	    } catch (IOException e) {

	        e.printStackTrace();
	    }
	}
	
	//This module enables AI guess process based on the current textField
	public String AIguess(String input){
		Vector<String> tmplist = new Vector<String>();
		String output = "";
		int count=0;
		for (int i=0;i<wordlist.size();i++)
		{
			if(input.matches(wordlist.get(i))==true)// full match
			{
				//show a pop-up dialog
				JOptionPane.showMessageDialog(frame, "Guess Done! Player win \n Word guessed: " + input);
				if (Player_or_AI_First == 0)
				{
					return null;
				}
				else
				{
					//generate a random alphabeta character
					Random rand = new Random();
					StringBuffer alphabeta = new StringBuffer("abcdefghijklmnopqrstuvwxyz");
					int randomnumber = rand.nextInt(alphabeta.length());
					StringBuffer tmp = new StringBuffer();
					tmp.append(alphabeta.charAt(randomnumber));
					return tmp.toString();
				}
			}
			if(input.regionMatches(true, 0, wordlist.get(i), 0, input.length())==true) //partial match
			{
				//record the matched words
				tmplist.add(wordlist.get(i));
				count++;
			}
		}
		
		if (count==0)
		{
			//show a pop-up dialog
			JOptionPane.showMessageDialog(frame, "No match! Player loss \n Word guessed: " + input);
			if (Player_or_AI_First == 0)
			{
				return null;
			}
			else
			{
				//generate a random alphabeta character
				Random rand = new Random();
				StringBuffer alphabeta = new StringBuffer("abcdefghijklmnopqrstuvwxyz");
				int randomnumber = rand.nextInt(alphabeta.length());
				StringBuffer tmp = new StringBuffer();
				tmp.append(alphabeta.charAt(randomnumber));
				return tmp.toString();
			}
		}
		
		//randomly pick a matched word
		Random rand = new Random();
		int randomnumber = rand.nextInt(tmplist.size());
		if (tmplist.size()>1){
			output = tmplist.get(randomnumber);
		}
		else{
			output = tmplist.get(0);
		}
		output = output.substring(0, input.length()+1);
		
		for (int i=0;i<wordlist.size();i++)
		{
			if(output.matches(wordlist.get(i))==true)
			{
				//show a pop-up dialog
				JOptionPane.showMessageDialog(frame, "Guess Done! AI win \n Word guessed: "+ output);
				if (Player_or_AI_First == 0)
				{
					return null;
				}
				else
				{
					//generate a random alphabeta character
					Random rand1 = new Random();
					StringBuffer alphabeta = new StringBuffer("abcdefghijklmnopqrstuvwxyz");
					int randomnumber1 = rand1.nextInt(alphabeta.length());
					StringBuffer tmp = new StringBuffer();
					tmp.append(alphabeta.charAt(randomnumber1));
					return tmp.toString();
				}
			}
		}
		
		return output;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//ExcelRead();//read words into a string vector "wordlist"		
		TxtRead();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WS_main_window window = new WS_main_window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WS_main_window() {
		frame = new JFrame();
		frame.setBounds(100, 100, 876, 489);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);		
		
		textField = new JTextField();
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setBounds(217, 160, 427, 119);
		frame.getContentPane().add(textField);
		textField.setColumns(15);
		
		createbtns();
	}
	
	@Override //define the actions to take when some alphabeta button is clicked
	public void mouseClicked(MouseEvent e) {
		String previous_t = textField.getText();
		String new_t = "";
		String t = "";
		JButton btnTmp = (JButton)e.getComponent();
		t = String.valueOf(btnTmp.getText());
		t = previous_t+t;
		textField.setText(t);
		textField.setFont(new Font("Georgia", Font.ITALIC, 80));
		
		new_t=AIguess(t);
		textField.setText(new_t);
		textField.setFont(new Font("Georgia", Font.ITALIC, 80));
	}
	
	//define the actions to take when a radiobuttion is selected
	public void actionPerformed(ActionEvent e) {
	    if (e.getActionCommand() == "Player")
	    {
	    	Player_or_AI_First = 0;
	    	textField.setText("");
	    }
	    else
	    {
	    	Player_or_AI_First = 1;
	    	Random rand = new Random();
			StringBuffer alphabeta = new StringBuffer("abcdefghijklmnopqrstuvwxyz");
			int randomnumber = rand.nextInt(alphabeta.length());
			StringBuffer tmp = new StringBuffer();
			tmp.append(alphabeta.charAt(randomnumber));
			textField.setText(tmp.toString());
			textField.setFont(new Font("Georgia", Font.ITALIC, 80));
	    }
	}
	
	//create all buttons, include all alphabet buttons, restart button, and radio buttons

	public void createbtns(){
		JButton btnRestart = new JButton("Restart");
		btnRestart.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				textField.setText("");
			}
		});
		btnRestart.setFont(new Font("Georgia", Font.BOLD, 20));
		btnRestart.setBounds(217, 300, 163, 38);
		frame.getContentPane().add(btnRestart);
		
		JButton btnHelp = new JButton("Help");
		String helptext = "1. Each time player and AI will guess an alphabet in turn \n"
				+"2. To make a guess, press any alphabet surrounding the window \n"
				+"3. Once a guess finishes a word, that guesser will win \n"
				+"4. If player spells a wrong word, player will loss \n"
				+"5. In default, player will go first to pick one alphabet to start the guess \n"
				+"6. Only words with more than "+shortestlength+" characters will be counted"; 
		btnHelp.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				JOptionPane.showMessageDialog(null,
						helptext, "RULES", JOptionPane.INFORMATION_MESSAGE);
				btnRestart.setFont(new Font("Georgia", Font.BOLD, 20));
			}
		});
		btnHelp.setFont(new Font("Georgia", Font.BOLD, 20));
	    btnHelp.setBounds(481, 300, 163, 38);
	    frame.getContentPane().add(btnHelp);
		
		btnA = new JButton("a");
		btnA.addMouseListener(this);
		btnA.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnA.setBounds(0, 0, 77, 81);
		frame.getContentPane().add(btnA);
		
		btnB = new JButton("b");
		btnB.addMouseListener(this);
		btnB.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnB.setBounds(88, 0, 77, 81);
		frame.getContentPane().add(btnB);
		
		btnC = new JButton("c");
		btnC.addMouseListener(this);
		btnC.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnC.setBounds(175, 0, 77, 81);
		frame.getContentPane().add(btnC);
		
		btnD = new JButton("d");
		btnD.addMouseListener(this);
		btnD.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnD.setBounds(262, 0, 77, 81);
		frame.getContentPane().add(btnD);
		
		btnE = new JButton("e");
		btnE.addMouseListener(this);
		btnE.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnE.setBounds(350, 0, 77, 81);
		frame.getContentPane().add(btnE);
		
		btnF = new JButton("f");
		btnF.addMouseListener(this);
		btnF.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnF.setBounds(437, 0, 77, 81);
		frame.getContentPane().add(btnF);
		
		btnG = new JButton("g");
		btnG.addMouseListener(this);
		btnG.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnG.setBounds(524, 0, 77, 81);
		frame.getContentPane().add(btnG);
		
		btnH = new JButton("h");
		btnH.addMouseListener(this);
		btnH.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnH.setBounds(611, 0, 77, 81);
		frame.getContentPane().add(btnH);
		
		btnV = new JButton("v");
		btnV.addMouseListener(this);
		btnV.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnV.setBounds(88, 363, 77, 81);
		frame.getContentPane().add(btnV);
		
		btnU = new JButton("u");
		btnU.addMouseListener(this);
		btnU.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnU.setBounds(175, 363, 77, 81);
		frame.getContentPane().add(btnU);
		
		btnT = new JButton("t");
		btnT.addMouseListener(this);
		btnT.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnT.setBounds(262, 363, 77, 81);
		frame.getContentPane().add(btnT);
		
		btnS = new JButton("s");
		btnS.addMouseListener(this);
		btnS.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnS.setBounds(350, 363, 77, 81);
		frame.getContentPane().add(btnS);
		
		btnR = new JButton("r");
		btnR.addMouseListener(this);
		btnR.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnR.setBounds(437, 363, 77, 81);
		frame.getContentPane().add(btnR);
		
		btnQ = new JButton("q");
		btnQ.addMouseListener(this);
		btnQ.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnQ.setBounds(524, 363, 77, 81);
		frame.getContentPane().add(btnQ);
		
		btnP = new JButton("p");
		btnP.addMouseListener(this);
		btnP.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnP.setBounds(611, 363, 77, 81);
		frame.getContentPane().add(btnP);
		
		btnO = new JButton("o");
		btnO.addMouseListener(this);
		btnO.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnO.setBounds(698, 363, 77, 81);
		frame.getContentPane().add(btnO);
		
		btnN = new JButton("n");
		btnN.addMouseListener(this);
		btnN.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnN.setBounds(785, 363, 77, 81);
		frame.getContentPane().add(btnN);
		
		btnM = new JButton("m");
		btnM.addMouseListener(this);
		btnM.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnM.setBounds(785, 272, 77, 81);
		frame.getContentPane().add(btnM);
		
		btnL = new JButton("l");
		btnL.addMouseListener(this);
		btnL.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnL.setBounds(785, 183, 77, 81);
		frame.getContentPane().add(btnL);
		
		btnI = new JButton("i");
		btnI.addMouseListener(this);
		btnI.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnI.setBounds(698, 0, 77, 81);
		frame.getContentPane().add(btnI);
		
		btnJ = new JButton("j");
		btnJ.addMouseListener(this);
		btnJ.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnJ.setBounds(785, 0, 77, 81);
		frame.getContentPane().add(btnJ);
		
		btnK = new JButton("k");
		btnK.addMouseListener(this);
		btnK.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnK.setBounds(785, 91, 77, 81);
		frame.getContentPane().add(btnK);
		
		btnW = new JButton("w");
		btnW.addMouseListener(this);
		btnW.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnW.setBounds(0, 363, 77, 81);
		frame.getContentPane().add(btnW);
		
		btnZ = new JButton("z");
		btnZ.addMouseListener(this);
		btnZ.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnZ.setBounds(0, 91, 77, 81);
		frame.getContentPane().add(btnZ);
		
		btnY = new JButton("y");
		btnY.addMouseListener(this);
		btnY.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnY.setBounds(0, 183, 77, 81);
		frame.getContentPane().add(btnY);
		
		btnX = new JButton("x");
		btnX.addMouseListener(this);
		btnX.setFont(new Font("Georgia", Font.ITALIC, 40));
		btnX.setBounds(0, 272, 77, 81);
		frame.getContentPane().add(btnX);
		
		radiobtn_playerfirst = new JRadioButton("Player First");
		radiobtn_playerfirst.setFont(new Font("Georgia", Font.BOLD, 15));
		radiobtn_playerfirst.addActionListener(this);
		radiobtn_playerfirst.setActionCommand("Player");
		radiobtn_playerfirst.setSelected(true);
		radiobtn_playerfirst.setBounds(218, 106, 121, 23);
		frame.getContentPane().add(radiobtn_playerfirst);
		
		radiobtn_AIfirst = new JRadioButton("AI First");
		radiobtn_AIfirst.setFont(new Font("Georgia", Font.BOLD, 15));
		radiobtn_AIfirst.addActionListener(this);
		radiobtn_AIfirst.setActionCommand("AI");
		radiobtn_AIfirst.setSelected(true);
		radiobtn_AIfirst.setBounds(218, 131, 121, 23);
		frame.getContentPane().add(radiobtn_AIfirst);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(radiobtn_playerfirst);
	    group.add(radiobtn_AIfirst);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
