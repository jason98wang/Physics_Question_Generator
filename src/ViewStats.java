import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.function.DoubleToIntFunction;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
/*
 * [ViewStats.java]
 * The page where students can check out their life time account stats
 * Author: Jason Wang
 * Nov. 20, 2018
 */
public class ViewStats extends JFrame {
	private static JFrame window;

	private String studentName;
	private Student student;
	private int studentWrongQuestions;
	private int studentTotalQuestions;
	private double accuracy = 0;
	private Color orange = new Color(255, 168, 104);

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
		panel.setMinimumSize(panel.getPreferredSize());
		panel.setSize(panel.getPreferredSize());
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.setUndecorated(true);
		
		// Focus the frame
		this.requestFocusInWindow();

		// Make the frame visible
		this.setVisible(true);
		this.setContentPane(panel);
	
		this.student = student;
		studentName = student.getName();
		studentWrongQuestions = student.getIncorrectQuestions();
		studentTotalQuestions = student.getTotalQuestions();
		accuracy = ((studentTotalQuestions - studentWrongQuestions) / (double) studentTotalQuestions) * 100.00;


		//creating the back button the leave the page
		back = new JButton(new ImageIcon("BackButton.png"));
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		back.setFont(font);
		back.setContentAreaFilled(false); 
		back.setBorder(BorderFactory.createEmptyBorder());

		//creating the label with the student's name
		JLabel name = new JLabel(studentName);
		name.setFont(new Font("Serif", Font.BOLD, 100));
		name.setForeground(orange);
		name.setSize(name.getPreferredSize());
		name.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		//adding the label to the main panel and changing the background color
		panel.setBackground(indigo);
		panel.add(name);

		//creating the life time accuracy label
		JLabel stats = new JLabel(String.format("Accuracy " + "%.2f", accuracy) + "%");
		stats.setFont(new Font("Serif", Font.BOLD, 150));
		stats.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		stats.setHorizontalAlignment(JLabel.CENTER);
		stats.setForeground(lightBlue);

		//creating the life time total incorrect questions panel
		JLabel wrong = new JLabel("Total Incorrect: " + Integer.toString(studentWrongQuestions));
		wrong.setFont(new Font("Serif", Font.BOLD, 50));
		wrong.setHorizontalAlignment(JLabel.CENTER);
		wrong.setForeground(lightBlue);

		//creating the life time total questions panel
		JLabel total = new JLabel("Total Questions: " + Integer.toString(studentTotalQuestions));
		total.setFont(new Font("Serif", Font.BOLD, 50));
		total.setHorizontalAlignment(JLabel.CENTER);
		total.setForeground(lightBlue);

		//adding extra spacce between the panels
		panel.add(Box.createRigidArea(new Dimension(0, 100)));
		
		//adding the accuracy to the main panel
		panel.add(stats);
		
		panel.add(Box.createRigidArea(new Dimension(0, 150)));
		
		//adding labels to the left side of the screen
		JPanel leftPanel = new JPanel();
		leftPanel.add(wrong);
		leftPanel.add(Box.createRigidArea(new Dimension(100, 0)));
		leftPanel.add(total);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
		leftPanel.setBackground(indigo);
		panel.add(leftPanel);
		panel.add(Box.createRigidArea(new Dimension(0,50)));
			
		//creating a panel to add the backButton and align it to the left side
		JPanel backButtonPanel = new JPanel();
		backButtonPanel.add(Box.createRigidArea(new Dimension(100,0)));
		backButtonPanel.add(back);
		backButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		backButtonPanel.setBackground(indigo);
		panel.add(backButtonPanel);
		
		
	

	} // End of constructor

}
