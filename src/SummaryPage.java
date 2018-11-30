import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class SummaryPage extends JFrame {

	private static JFrame window;

	JLabel title, accuracy;

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
		
		accuracy = new JLabel((QuizTakerDisplay.wrongQuestions.size() / QuizTakerDisplay.questions.size()) + "");
		accuracy.setFont(new Font("Serif", Font.BOLD, 100));
		panel.add(accuracy);
		
		System.out.println(QuizTakerDisplay.wrongQuestions.size());

	} // End of constructor


}
