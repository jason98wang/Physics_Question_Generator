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

/*
 * [SummaryPage.java]
 * The UI at the end of the quiz
 * Author: Jason Wang
 * Nov. 20, 2018
 */
class SummaryPage extends JFrame {

	private static JFrame window;

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
		double precentage = ((QuizTakerDisplay.getQuestions().size() - QuizTakerDisplay.getQuestionWrong())
				/ (double) QuizTakerDisplay.getQuestions().size()) * 100.00;

		//creating new label for the accuracy 
		accuracy = new JLabel(String.format("%.2f", precentage) + "%");

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

		//adding the three buttons on to the Jpanel with proper spacing
		JPanel panel1 = new JPanel();
		panel1.add(exit);
		panel1.add(Box.createRigidArea(new Dimension(100, 0)));
		panel1.add(redo);
		panel1.add(Box.createRigidArea(new Dimension(100, 0)));
		panel1.add(homePage);
		panel1.add(Box.createRigidArea(new Dimension(100, 0)));
		panel1.setAlignmentX(CENTER_ALIGNMENT);
		panel1.setBackground(indigo);
		panel.add(panel1);

		//setting the background color to indigo
		panel.setBackground(indigo);

		this.wrongQuestions = QuizTakerDisplay.getWrongQuestions();

		this.student = student;

		//Adding data to the student account
		student.setIncorrectQuestions(student.getIncorrectQuestions() + QuizTakerDisplay.getQuestionWrong());
		student.setTotalQuestions(student.getTotalQuestions() + QuizTakerDisplay.getQuestions().size());

		Login.getDatabase().update();

	} // End of constructor

	private class ExitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	private class RedoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (wrongQuestions.size() != 0) {
				QuizTaker.startQuiz(wrongQuestions);
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "No wrong question to do! Choose a new unit and practice again.");
			}

		}
	}

	private class HomePageListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			new QuizTaker(student, QuizTaker.chosenSubject);
			dispose();
		}

	}
	


}