import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.function.DoubleToIntFunction;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
//		panel.setMinimumSize(panel.getPreferredSize());
		panel.setSize(panel.getPreferredSize());
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.setUndecorated(true);
		
		// Focus the frame
		this.requestFocusInWindow();

		// Make the frame visible
		this.setVisible(true);

		this.setContentPane(panel);

		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		JPanel right = new JPanel();

		this.student = student;
		studentName = student.getName();
		studentWrongQuestions = student.getIncorrectQuestions();
		studentTotalQuestions = student.getTotalQuestions();
		accuracy = ((studentTotalQuestions - studentWrongQuestions) / (double) studentTotalQuestions) * 100.00;


		back = new JButton(new ImageIcon("BackButton.png"));
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		back.setFont(font);

		JLabel name = new JLabel(studentName);
		name.setFont(new Font("Serif", Font.BOLD, 50));
		name.setForeground(lightBlue);
		name.setSize(name.getPreferredSize());
		name.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		panel.setBackground(indigo);
		panel.add(name);

		JLabel stats = new JLabel(String.format("%.2f", accuracy) + "%");
		stats.setFont(new Font("Serif", Font.BOLD, 50));
		stats.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		stats.setHorizontalAlignment(JLabel.CENTER);
		stats.setForeground(lightBlue);

		JLabel wrong = new JLabel("Total Incorrect: " + Integer.toString(studentWrongQuestions));
		wrong.setFont(new Font("Serif", Font.BOLD, 50));
		wrong.setHorizontalAlignment(JLabel.CENTER);
		wrong.setForeground(lightBlue);

		JLabel total = new JLabel("Total Questions: " + Integer.toString(studentTotalQuestions));
		total.setFont(new Font("Serif", Font.BOLD, 50));
		total.setHorizontalAlignment(JLabel.CENTER);
		total.setForeground(lightBlue);

		left.add(wrong);
		left.add(Box.createRigidArea(new Dimension(0, 100)));
		left.add(total);
		left.add(Box.createRigidArea(new Dimension(0, 10)));
		left.add(back);
		left.setBackground(indigo);

		panel.add(Box.createRigidArea(new Dimension(0, 200)));
		JPanel all = new JPanel();
		all.setLayout(new BoxLayout(all, BoxLayout.X_AXIS));
		all.add(Box.createHorizontalStrut(50));
		all.add(left);
		all.setBackground(indigo);
		right.add(stats);
		right.setBackground(indigo);
		all.add(right);
		panel.add(all);
		System.out.println(name.getSize());
	} // End of constructor

}
