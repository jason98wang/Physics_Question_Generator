
//Graphics & GUI imports
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//Keyboard imports
import java.io.File;
import java.io.IOException;
import java.net.URL;

class QuizTakerDisplay extends JFrame {

	// Class variables
	private static JFrame window;
	Image nextPic;
	boolean questionAnswered;
	boolean correct = false, correct1 = false;

	JButton answer1, answer2, answer3, answer4;
	JButton nextButton;

	JLabel label;
	JTextArea questionLabel;
	
	JPanel panel2;
	Font font1 = new Font("Serif", Font.BOLD, 100);
	Font font2 = new Font("Arial", Font.ITALIC, 50);
	Font font3 = new Font("Serif", Font.BOLD, 25);
	int questionNum = 0;
	String[] ids;
	double[] values;
	static SimpleLinkedList<String> questions;
	SimpleLinkedList<Double> answers;
	SimpleLinkedList<double[]> choices;
	SimpleLinkedList<String[]> variableIDs;
	SimpleLinkedList<double[]> variableValues;
	boolean questionRight = true;
	static int questionWrong = 0;

	static SimpleLinkedList<Question> wrongQuestions = new SimpleLinkedList<Question>();

	SimpleLinkedList<Question> rootQuestions;
	URL url;

	//	ImageIcon acceleration, appliedForce, chemicalEnergy, delta, displacement, e, elasticForce, gravationalEnergy,
	//			gravationalForce, impulse, kineticEnergy, KineticFrictionalForce, lambda, magneticForce, momentum,
	//			normalForce, nuclearEnergy, soundEnergy, springForce, staticFrictionalForce, tensionalForce, thermalEnergy,
	//			theta, time, velocity, work, xForce, yForce;

	double[][] wrongAnswer;

	// Constructor
	QuizTakerDisplay(SimpleLinkedList<String> question, SimpleLinkedList<double[]> choices,
			SimpleLinkedList<Double> answers, SimpleLinkedList<String[]> variableIDs,
			SimpleLinkedList<double[]> variableValues, SimpleLinkedList<Question> rootQuestions) {

		super("Practice Like A Physicist");

		//		Icon icon = null;
		//		try {
		//			icon = new ImageIcon(ImageIO.read(new File("clapping.gif")));
		//		} catch (IOException e1) {}
		//		JLabel clapping = new JLabel(icon);

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
					//this.add(clapping);
					try {
						Thread.sleep(500);
					} catch (Exception e) {
					}
					ActionEvent e = new ActionEvent(nextButton, 0, "");
					nextButton.getActionListeners()[0].actionPerformed(e);
				}

				repaint();
			}
		};
		panel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		//		panel.setBackground(new Color(56, 53, 74));

		// Focus the frame
		this.requestFocusInWindow();

		// Make the frame visible
		this.setVisible(true);

		this.questions = question;
		this.choices = choices;
		this.answers = answers;
		this.variableIDs = variableIDs;
		this.variableValues = variableValues;
		this.rootQuestions = rootQuestions;

		// if (questions.size() == 0) {
		// dispose();
		// }

		// creating buttons for each choice

		answer1 = new JButton(Double.toString(round(choices.get(questionNum)[0], 2)));
		answer2 = new JButton(Double.toString(round(choices.get(questionNum)[1], 2)));
		answer3 = new JButton(Double.toString(round(choices.get(questionNum)[2], 2)));
		answer4 = new JButton(Double.toString(round(choices.get(questionNum)[3], 2)));

		answer1.setFont(font3);
		answer2.setFont(font3);
		answer3.setFont(font3);
		answer4.setFont(font3);

		// add listeners for each button
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
		panel2 = new JPanel();
		ids = variableIDs.get(0);
		values = variableValues.get(0);
		for (int j = 0; j < ids.length; j++) {
			try {
				panel2.add(new JLabel(new ImageIcon(ImageIO.read(new File("Symbols/Variables/" + ids[j] + ".png")))));
				JLabel value = new JLabel(" = " + String.format("%.2f", values[j]) + "  ");
				value.setFont(font2);
				panel2.add(value);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
		nextButton = new JButton();
		try {
			nextButton = new JButton(new ImageIcon(ImageIO.read(new File("nextButton.png"))));
		} catch (Exception ex) {
		}
		questionLabel = new JTextArea(questions.get(questionNum));


		double lengthOnRow = Math.ceil(questions.get(questionNum).length() / 3.00);

		int size;
		
		if(lengthOnRow < 47) {
			size = 100;
		}else {
			size = (int)(100 - (lengthOnRow - 46));
		}

		questionLabel.setRows(2);
		
		
		
		questionLabel.setFont(new Font("Serif", Font.BOLD, size));
		
		label = new JLabel("Question #" + Integer.toString(questionNum + 1));
		label.setFont(font2);
		nextButton.addActionListener(new NextButtonListener());
		int x = 50;
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(Box.createRigidArea(new Dimension(0, x)));
		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(0, x)));
		questionLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(questionLabel);
		panel.add(Box.createRigidArea(new Dimension(0, x)));
		panel2.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		panel.add(panel2);
		panel.add(Box.createRigidArea(new Dimension(0, x)));
		panel1.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		panel.add(panel1);
		panel.add(Box.createRigidArea(new Dimension(0, x)));
		JPanel flowPanel = new JPanel();
		flowPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		nextButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
		flowPanel.add(nextButton);
		panel.add(flowPanel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.setContentPane(panel);

		//		panel1.setBackground(new Color(56, 53, 74));
		//		panel2.setBackground(new Color(56, 53, 74));
		//		flowPanel.setBackground(new Color(56, 53, 74));
		
		questionLabel.setLineWrap(true);
		//questionLabel.setEditable(false);
		//JScrollPane scrollPane = new JScrollPane(questionLabel);
		


	} // End of constructor

	private class Answer1Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (choices.get(questionNum)[0] == answers.get(questionNum)) {
				answer1.setBackground(Color.GREEN);
				correct = true;
				if (questionRight != true) {
					questionWrong++;
				}
			} else {
				answer1.setBackground(Color.RED);
				questionRight = false;
				if (!wrongQuestions.contain(rootQuestions.get(questionNum))) {
					wrongQuestions.add(rootQuestions.get(questionNum));

					System.out.println("added");
				}
			}

		}
	}

	private class Answer2Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (choices.get(questionNum)[1] == answers.get(questionNum)) {
				correct = true;
				answer2.setBackground(Color.GREEN);
				if (questionRight != true) {
					questionWrong++;
				}
			} else {
				answer2.setBackground(Color.RED);
				questionRight = false;
				if (!wrongQuestions.contain(rootQuestions.get(questionNum))) {
					wrongQuestions.add(rootQuestions.get(questionNum));
					System.out.println("added");
				}
			}

		}
	}

	private class Answer3Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (choices.get(questionNum)[2] == answers.get(questionNum)) {
				answer3.setBackground(Color.GREEN);
				correct = true;
				if (questionRight != true) {
					questionWrong++;
				}
			} else {
				answer3.setBackground(Color.RED);
				questionRight = false;
				if (!wrongQuestions.contain(rootQuestions.get(questionNum))) {
					wrongQuestions.add(rootQuestions.get(questionNum));
					System.out.println("added");
				}

			}

		}
	}

	private class Answer4Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (choices.get(questionNum)[3] == answers.get(questionNum)) {
				answer4.setBackground(Color.GREEN);
				correct = true;
				if (questionRight != true) {
					questionWrong++;
				}
			} else {
				answer4.setBackground(Color.RED);
				questionRight = false;
				if (!wrongQuestions.contain(rootQuestions.get(questionNum))) {
					wrongQuestions.add(rootQuestions.get(questionNum));
					System.out.println("added");
				}
			}

		}
	}

	private class NextButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			panel2.removeAll();
			questionNum++;
			questionRight = true;
			if (questionNum == questions.size()) {

				new SummaryPage(wrongQuestions);
				dispose();
				return;
			}
			ids = variableIDs.get(questionNum);
			values = variableValues.get(questionNum);

			for (int j = 0; j < ids.length; j++) {
				try {
					panel2.add(
							new JLabel(new ImageIcon(ImageIO.read(new File("Symbols/Variables/" + ids[j] + ".png")))));
					JLabel value = new JLabel(" = " + String.format("%.2f", values[j]) + "  ");
					value.setFont(font2);
					panel2.add(value);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			answer2.setBackground(null);
			answer1.setBackground(null);
			answer3.setBackground(null);
			answer4.setBackground(null);

			questionLabel.setText(questions.get(questionNum));
			label.setText("Question #" + Integer.toString(questionNum + 1));
			answer1.setText(Double.toString(round(choices.get(questionNum)[0], 2)));
			answer2.setText(Double.toString(round(choices.get(questionNum)[1], 2)));
			answer3.setText(Double.toString(round(choices.get(questionNum)[2], 2)));
			answer4.setText(Double.toString(round(choices.get(questionNum)[3], 2)));
			revalidate();
		}
	}

	public double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

}
