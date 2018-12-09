import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.function.DoubleToIntFunction;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ViewStats extends JFrame {
	private static JFrame window;

	private String studentName; 
	private Student student; 
	private int studentWrongQuestions;
	private int studentTotalQuestions;
	private double accuracy = 0; 
	
	private JButton back; 
	
	private Color indigo = new Color(56, 53, 74);
	private Color lightBlue = new Color(162, 236, 250);
	private Font font = new Font("Arial", Font.BOLD, 30);
	

	// Constructor
	ViewStats(Student student) {
		super("Summary Page");
		// Set the frame to full screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(false);

		// Set up the main panel
		JPanel panel = new JPanel();
		panel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		panel.setLayout(new BoxLayout(panel, getDefaultCloseOperation()));
		this.setUndecorated(true);

		// Focus the frame
		this.requestFocusInWindow();

		// Make the frame visible
		this.setVisible(true);

		this.add(panel);
		
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, getDefaultCloseOperation()));
		JPanel right = new JPanel();

		this.student = student;
		studentName = student.getName();
		studentWrongQuestions = student.getIncorrectQuestions();
		studentTotalQuestions = student.getTotalQuestions();
		accuracy = ((studentTotalQuestions-studentWrongQuestions) / (double)studentTotalQuestions) * 100.00;
		

		JLabel name = new JLabel(studentName);
		name.setFont(new Font("Serif", Font.BOLD, 100));
		name.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		name.setHorizontalAlignment(JLabel.CENTER);
		name.setForeground(lightBlue);
		
		JLabel stats = new JLabel(String.format("%.2f", accuracy) + "%");
		stats.setFont(new Font("Serif", Font.BOLD, 100));
		stats.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		stats.setHorizontalAlignment(JLabel.CENTER);
		stats.setForeground(lightBlue);
		
		
		JLabel wrong = new JLabel(Integer.toString(studentWrongQuestions));
		wrong.setFont(new Font("Serif", Font.BOLD, 100));
		wrong.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		wrong.setHorizontalAlignment(JLabel.CENTER);
		wrong.setForeground(lightBlue);
		
		JLabel total = new JLabel(Integer.toString(studentTotalQuestions));
		total.setFont(new Font("Serif", Font.BOLD, 100));
		total.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		total.setHorizontalAlignment(JLabel.CENTER);
		total.setForeground(lightBlue);
		
		
		
		
		
		left.add(wrong);
		left.add(Box.createRigidArea(new Dimension(0, 300)));
		
		
		panel.setBackground(indigo);
		

		

	} // End of constructor

}
