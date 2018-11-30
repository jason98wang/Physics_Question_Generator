import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class SummaryPage extends JFrame {

	private static JFrame window;

	JLabel title;
	double accuracy;
	JButton exit, redo;
	SimpleLinkedList<Question> wrongQuestions;
	SimpleLinkedList<Double> newAnswers = new SimpleLinkedList<Double>();
	SimpleLinkedList<double[]> choices;

	// Constructor
	SummaryPage() {
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

		panel.add(title);

		this.setContentPane(panel);

		accuracy = QuizTakerDisplay.questionWrong / QuizTakerDisplay.questions.size();

		// accuracy.setFont(new Font("Serif", Font.BOLD, 100));
		// panel.add(accuracy);

		exit = new JButton("Exit");
		redo = new JButton("Redo Wrong Questions");

		System.out.println(QuizTakerDisplay.questionWrong);
		this.wrongQuestions = QuizTakerDisplay.wrongQuestions;

	} // End of constructor

	private class exitListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			window.dispose();
		}
	}
	
	private class redoListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			//get answers
			for(int i = 0; i<wrongQuestions.size(); i++) {
				newAnswers.add(wrongQuestions.get(i).getAnswer());
			}
			
			
			
			
			
			
			
			
			//new QuizTakerDisplay();
		}

}