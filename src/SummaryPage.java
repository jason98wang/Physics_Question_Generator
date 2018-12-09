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

	private static JFrame window;

	JLabel title;
	JLabel accuracy;
	JButton exit, redo,homePage;
	Student student; 
	SimpleLinkedList<Question> wrongQuestions;
	Color indigo = new Color(56, 53, 74);
	Color lightBlue = new Color(162, 236, 250);
	Font font = new Font("Arial",Font.BOLD,30);
	
	// Constructor
	SummaryPage(SimpleLinkedList<Question> wrongQuestions, Student student) {
		super("Summary Page");
		// Set the frame to full screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(false);

		// Set up the game panel
		JPanel panel = new JPanel();
		panel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setUndecorated(true);

		// Focus the frame
		this.requestFocusInWindow();

		// Make the frame visible
		this.setVisible(true);

		title = new JLabel("Summary Page");
		title.setFont(new Font("Serif", Font.BOLD, 100));

		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(lightBlue);

		panel.add(title);
		panel.add(Box.createRigidArea(new Dimension(0, 200)));
		panel.setLayout(new BoxLayout(panel, getDefaultCloseOperation()));

		this.setContentPane(panel);

		double precentage = ((QuizTakerDisplay.questions.size() - QuizTakerDisplay.questionWrong) / (double)QuizTakerDisplay.questions.size()) * 100.00;
		accuracy = new JLabel(String.format("%.2f", precentage) + "%");
		
		System.out.println(QuizTakerDisplay.questionWrong);

		accuracy.setFont(new Font("Serif", Font.BOLD, 150));
		accuracy.setForeground(lightBlue);
			
		accuracy.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		accuracy.setHorizontalAlignment(JLabel.CENTER);
		
		panel.add(accuracy);
		panel.add(Box.createRigidArea(new Dimension(0, 200)));
		
		exit = new JButton("        Exit        ");
		exit.setFont(font);
		redo = new JButton("Redo Wrong Questions");
		redo.setFont(font);
		homePage= new JButton("Back to Main Page");
		homePage.setFont(font);
		
		redo.addActionListener( new RedoListener());
		exit.addActionListener( new ExitListener());
		homePage.addActionListener( new HomePageListener());
		
		
		JPanel panel1 = new JPanel();
		panel1.add(exit);
		panel1.add(Box.createRigidArea(new Dimension(100, 0)));
		panel1.add(redo);
		panel1.add(Box.createRigidArea(new Dimension(100, 0)));
		panel1.add(homePage);
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		panel1.setAlignmentX(CENTER_ALIGNMENT);
		panel1.setBackground(indigo);
		panel.add(panel1);
		
		panel.setBackground(indigo);
		
		this.wrongQuestions = QuizTakerDisplay.wrongQuestions;
		
		student.setIncorrectQuestions(student.getIncorrectQuestions() + wrongQuestions.size());
		student.setTotalQuestions(student.getTotalQuestions() + QuizTakerDisplay.questions.size());
		
		
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
			if(wrongQuestions.size() !=0) {
			QuizTaker.startQuiz(wrongQuestions);
			dispose();
			}else {
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