
//Graphics & GUI imports
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

class QuizTakerDisplay extends JFrame {

	// Class variables
	private static JFrame window;
	Image nextPic;
	boolean questionAnswered;
	boolean correct = false, correct1 = false;

	JButton answer1, answer2, answer3, answer4;
	JButton nextButton;

	JLabel label;
	JLabel questionLabel;

	Font font1 = new Font("Serif", Font.BOLD, 100);
	Font font2 = new Font("Arial", Font.ITALIC, 50);
	Font font3 = new Font("Serif", Font.BOLD, 25);
	int questionNum = 0;
	SimpleLinkedList<String> questions;
	SimpleLinkedList<Double> answers;
	SimpleLinkedList<double[]> choices;

	double[][] wrongAnswer;

	// Constructor
	QuizTakerDisplay(SimpleLinkedList<String> question, SimpleLinkedList<double[]> choices,
			SimpleLinkedList<Double> answers) {
		super("Practice Like A Physicist");
		// Set the frame to full screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(false);

		// Set up the game panel
		JPanel panel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (correct) {
					correct = false;
					correct1 = true;
				} else if (correct1) {
					correct1 = false;
					try {Thread.sleep(500);}catch(Exception e) {}
					ActionEvent e = new ActionEvent(nextButton,0,"");
//					nextButton.actionPerformed(e);
					nextButton.getActionListeners()[0].actionPerformed(e);
				}
				repaint();
			}
		};
		panel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

		// Focus the frame
		this.requestFocusInWindow();

		// Make the frame visible
		this.setVisible(true);

		this.questions = question;
		this.choices = choices;
		this.answers = answers;

		if (questions.size() == 0) {
			dispose();
		}

		//creating buttons for each choice
		answer1 = new JButton(Double.toString(choices.get(questionNum)[0]));
		answer2 = new JButton(Double.toString(choices.get(questionNum)[1]));
		answer3 = new JButton(Double.toString(choices.get(questionNum)[2]));
		answer4 = new JButton(Double.toString(choices.get(questionNum)[3]));

		answer1.setFont(font3);
		answer2.setFont(font3);
		answer3.setFont(font3);
		answer4.setFont(font3);
		//		panel.setLayout(null);
		//		answer1.setBounds(200, 600 , 300 , 100);
		//		answer2.setBounds(600 , 600 , 300 , 100 );
		//		answer3.setBounds(1000 , 600 , 300 , 100);
		//		answer4.setBounds(1400 , 600 , 300 , 100);

		//add listners for each button
		answer1.addActionListener(new Answer1Listener());
		answer2.addActionListener(new Answer2Listener());
		answer3.addActionListener(new Answer3Listener());
		answer4.addActionListener(new Answer4Listener());
		JPanel panel1 = new JPanel();
		panel1.add(answer1);
		panel1.add(Box.createRigidArea(new Dimension(100, 0)));

		panel1.add(answer2);
		panel1.add(Box.createRigidArea(new Dimension(100, 0)));

		panel1.add(answer3);
		panel1.add(Box.createRigidArea(new Dimension(100, 0)));

		panel1.add(answer4);
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));

		nextButton = new JButton();
		try {
			nextButton = new JButton(new ImageIcon(ImageIO.read(new File("NextButton.png"))));
		} catch (Exception ex) {
		}
		questionLabel = new JLabel(questions.get(questionNum));

		questionLabel.setFont(font1);
		label = new JLabel("Question #" + Integer.toString(questionNum + 1));
		label.setFont(font2);
		nextButton.addActionListener(new NextButtonListener());

		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(Box.createRigidArea(new Dimension(0, 100)));
		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(0, 100)));
		questionLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(questionLabel);
		panel.add(Box.createRigidArea(new Dimension(0, 100)));
		panel1.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		panel.add(panel1);
		panel.add(Box.createRigidArea(new Dimension(0, 100)));
		JPanel flowPanel = new JPanel();
		flowPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		nextButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
		flowPanel.add(nextButton);
		panel.add(flowPanel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.setContentPane(panel);

	} // End of constructor
		


	// ********* INNER CLASSES *********
	//
	//	private class GameAreaPanel extends JPanel {
	//
	//		
	//
	//		GameAreaPanel() {

	//
	//		}
	//
	//		public void paintComponent(Graphics g) {
	//
	//			// Call the super class
	//			super.paintComponent(g);
	//			setDoubleBuffered(true);
	//
	//			questionAnswered = false;
	//
	//			g.setFont(font1);
	//			int displayNum = questionNum + 1;
	//			g.drawString("Question #" + displayNum, 700, 90);
	//
	//			g.drawImage(nextPic, 1750, 905, null, this);
	//
	//			g.setFont(font2);
	//			g.drawString(questions.get(questionNum), 900, 300);
	//
	//			answer1.setText(Double.toString(choices.get(questionNum)[0]));
	//			answer2.setText(Double.toString(choices.get(questionNum)[1]));
	//			answer3.setText(Double.toString(choices.get(questionNum)[2]));
	//			answer4.setText(Double.toString(choices.get(questionNum)[3]));
	//
	//			// Repaint
	//			repaint();
	//
	//		} // End of paintComponent
	//	}// End of GameAreaPanel
	//

	private class Answer1Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (choices.get(questionNum)[0] == answers.get(questionNum)) {
				answer1.setBackground(Color.GREEN);
				correct = true;
			} else {
				answer1.setBackground(Color.RED);
			}

		}
	}

	private class Answer2Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (choices.get(questionNum)[1] == answers.get(questionNum)) {
				correct = true;
				answer2.setBackground(Color.GREEN);
			} else {
				answer2.setBackground(Color.RED);
			}

		}
	}

	private class Answer3Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (choices.get(questionNum)[2] == answers.get(questionNum)) {
				answer3.setBackground(Color.GREEN);
				correct = true;
			} else {
				answer3.setBackground(Color.RED);
			}

		}
	}

	private class Answer4Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (choices.get(questionNum)[3] == answers.get(questionNum)) {
				answer4.setBackground(Color.GREEN);
				correct = true;
			} else {
				answer4.setBackground(Color.RED);
			}

		}
	}
	
	private class NextButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			questionNum++;
			answer2.setBackground(null);
			answer1.setBackground(null);
			answer3.setBackground(null);
			answer4.setBackground(null);
			if (questionNum == questions.size()) {
				dispose();
			}
			questionLabel.setText(questions.get(questionNum));
			label.setText("Question #" + Integer.toString(questionNum + 1));
			answer1.setText(Double.toString(choices.get(questionNum)[0]));
			answer2.setText(Double.toString(choices.get(questionNum)[1]));
			answer3.setText(Double.toString(choices.get(questionNum)[2]));
			answer4.setText(Double.toString(choices.get(questionNum)[3]));
			revalidate();
		}
	}

}
