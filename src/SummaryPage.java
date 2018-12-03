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
import javax.swing.JPanel;

import data_structures.SimpleLinkedList;

class SummaryPage extends JFrame {

	private static JFrame window;

	JLabel title;
	JLabel accuracy;
	JButton exit, redo;
	SimpleLinkedList<Question> wrongQuestions;

	// Constructor
	SummaryPage(SimpleLinkedList<Question> wrongQuestions) {
		super("Summary Page");
		// Set the frame to full screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(false);

		// Set up the game panel
		JPanel panel = new JPanel();
		panel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

		// Focus the frame
		this.requestFocusInWindow();

		// Make the frame visible
		this.setVisible(true);

		title = new JLabel("Summary Page");
		title.setFont(new Font("Serif", Font.BOLD, 100));

		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		title.setHorizontalAlignment(JLabel.CENTER);

		panel.add(title);
		panel.add(Box.createRigidArea(new Dimension(0, 200)));
		panel.setLayout(new BoxLayout(panel, getDefaultCloseOperation()));

		this.setContentPane(panel);

		double precentage = ((QuizTakerDisplay.questions.size() - QuizTakerDisplay.questionWrong) / (double)QuizTakerDisplay.questions.size()) * 100.00;
		accuracy = new JLabel(String.format("%.2f", precentage) + "%");

		accuracy.setFont(new Font("Serif", Font.BOLD, 150));
		
		accuracy.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		accuracy.setHorizontalAlignment(JLabel.CENTER);
		
		panel.add(accuracy);
		panel.add(Box.createRigidArea(new Dimension(0, 200)));
		
		exit = new JButton("        Exit        ");
		redo = new JButton("Redo Wrong Questions");
		
		redo.addActionListener( new RedoListener());
		
		
		JPanel panel1 = new JPanel();
		panel1.add(exit);
		panel1.add(Box.createRigidArea(new Dimension(100, 0)));
		panel1.add(redo);
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		panel1.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(panel1);
		
		this.wrongQuestions = QuizTakerDisplay.wrongQuestions;


	} // End of constructor

	private class exitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			window.dispose();
		}
	}

	private class RedoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			QuizTaker.startQuiz(wrongQuestions);
			dispose();
		}
	}

}