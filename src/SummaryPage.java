/**
 * [SummaryPage.java]
 * The UI at the end of the quiz
 * Author: Jason Wang
 * Nov. 20, 2018
 */
//imports
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import data_structures.SimpleLinkedList;

class SummaryPage extends JFrame {

	//declaring variables
	private JLabel title;
	private JLabel accuracy;
	private JButton exit, redo, homePage ;
	private Student student;
	private SimpleLinkedList<Question> wrongQuestions;
	private Color indigo = new Color(56, 53, 74);
	private Color lightBlue = new Color(162, 236, 250);
	private Font font = new Font("Arial", Font.BOLD, 30);

	// Constructor
	SummaryPage(SimpleLinkedList<Question> wrongQuestions, Student student) {
		super("Summary Page");
		// Set the frame to full screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(false);

		// Set up the main panel
		JPanel panel = new JPanel();
		panel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setUndecorated(true);

		// Focus the frame
		this.requestFocusInWindow();

		// Make the frame visible
		this.setVisible(true);

		//Creating summary page title label 
		title = new JLabel("Summary Page");
		title.setFont(new Font("Serif", Font.BOLD, 100));

		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(lightBlue);

		//Adding the title to the main panel 
		panel.add(title);
		panel.add(Box.createRigidArea(new Dimension(0, 200)));
		panel.setLayout(new BoxLayout(panel, getDefaultCloseOperation()));

		this.setContentPane(panel);

		//calculating the accuracy of the the quiz 
		double precentage = ((QuizTakerDisplay.getQuestionStatment().size() - QuizTakerDisplay.getQuestionWrong())
				/ (double) QuizTakerDisplay.getQuestionStatment().size()) * 100.00;

		//creating new label for the accuracy 
		accuracy = new JLabel(String.format("%.2f", precentage) + "%");

		//set font and color
		accuracy.setFont(new Font("Serif", Font.BOLD, 150));
		accuracy.setForeground(lightBlue);
		accuracy.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		accuracy.setHorizontalAlignment(JLabel.CENTER);

		//adding accuracy to the main panel 
		panel.add(accuracy);
		panel.add(Box.createRigidArea(new Dimension(0, 200)));

		//Initializing the text in the exit button, re-do wrong questions button and home page
		exit = new JButton("Exit");
		exit.setFont(font);
		redo = new JButton("Redo Wrong Questions");
		redo.setFont(font);
		homePage = new JButton("Back to Main Page");
		homePage.setFont(font);

		//adding listeners for each button 
		redo.addActionListener(new RedoListener());
		exit.addActionListener(new ExitListener());
		homePage.addActionListener(new HomePageListener());

		//adding the three buttons on to the Panel with proper spacing
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(exit);
		buttonPanel.add(Box.createRigidArea(new Dimension(100, 0)));
		buttonPanel.add(redo);
		buttonPanel.add(Box.createRigidArea(new Dimension(100, 0)));
		buttonPanel.add(homePage);
		buttonPanel.add(Box.createRigidArea(new Dimension(100, 0)));
		buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
		buttonPanel.setBackground(indigo);
		panel.add(buttonPanel);

		//setting the background color to indigo
		panel.setBackground(indigo);

		this.wrongQuestions = QuizTakerDisplay.getWrongQuestions();

		this.student = student;

		//Adding data to the student account
		student.setIncorrectQuestions(student.getIncorrectQuestions() + QuizTakerDisplay.getQuestionWrong());
		student.setTotalQuestions(student.getTotalQuestions() + QuizTakerDisplay.getQuestionStatment().size());

		System.out.println(QuizTakerDisplay.getQuestionWrong());
		
		
		

	} // End of constructor

	/*
	 * [ExitButtonListener.java]
	 * Private class ActionListener for the exit button
	 */
	private class ExitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			//if the window closes update database
			Login.getDatabase().update();
		}
	}

	/*
	 * [RedoListener.java]
	 * Private class ActionListener for the redo button
	 */
	private class RedoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (wrongQuestions.size() != 0) {
				//generate new questions based on the ones messed up
				new QuizTaker(wrongQuestions);
				dispose();
			} else {
				//if no wrong questions to do
				JOptionPane.showMessageDialog(null, "No wrong question to do! Choose a new unit and practice again.");
			}

		}
	}

	/*
	 * [HomePageListener.java]
	 * Private class ActionListener for the home button
	 */
	private class HomePageListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//go back to the quiz taker main menu
			new QuizTaker(student, QuizTaker.chosenSubject);
			dispose();
		}

	}
	


}