
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
import javax.swing.SpringLayout;

import data_structures.SimpleLinkedList;

import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
	JButton nextButton, exitButton;

	JLabel label;
	JLabel questionLabel;
	JLabel clapping = new JLabel(new ImageIcon("clapping.gif"));
	private long time;

	JPanel panel2;
	Font font1 = new Font("Serif", Font.BOLD, 100);
	Font font2 = new Font("Arial", Font.ITALIC, 50);
	Font font3 = new Font("Serif", Font.BOLD, 25);
	Color indigo = new Color(56, 53, 74);
	Color lightBlue = new Color(162, 236, 250);
	Color orange = new Color(255, 168, 104);
	Color defaultColor = new JButton().getBackground();
	int questionNum = 0;
	String[] ids;
	double[] values;
	double[][] wrongAnswer;
	static int questionWrong;
	static SimpleLinkedList<String> questions;
	static SimpleLinkedList<Question> wrongQuestions;
	SimpleLinkedList<Double> answers;
	SimpleLinkedList<double[]> choices;
	SimpleLinkedList<String[]> variableIDs;
	SimpleLinkedList<double[]> variableValues;
	SimpleLinkedList<Question> rootQuestions;
	boolean right = false;

	// Constructor
	QuizTakerDisplay(SimpleLinkedList<String> question, SimpleLinkedList<double[]> choices,
			SimpleLinkedList<Double> answers, SimpleLinkedList<String[]> variableIDs,
			SimpleLinkedList<double[]> variableValues, SimpleLinkedList<Question> rootQuestions) {

		super("Practice Like A Physicist");

	
//		Icon clapping = new ImageIcon("clapping.gif");


		// Set the frame to full screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(false);
		window = this;
		// Set up the game panel
		JPanel panel = new JPanel() {
//			JLabel clap = new JLabel(clapping);
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
	

				if (correct) {
					correct = false;
					correct1 = true;
					
				} else if (correct1) {
					if (time > 0) {
						if (System.currentTimeMillis() - time >= 2000) {
							time = 0;
							this.remove(clapping);
							correct1 = false;
							ActionEvent e = new ActionEvent(nextButton, 0, "");
							nextButton.getActionListeners()[0].actionPerformed(e);
						}
					} else {
						time = System.currentTimeMillis();
						this.add(clapping);
					}
//					correct1 = false;
//					this.add(clapp);
					
					//this.remove(clap);
				}

				repaint();
			}
		};
		panel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

		// Focus the frame
		this.requestFocusInWindow();
		this.setUndecorated(true);

		// Make the frame visible
		this.setVisible(true);

		this.questions = question;
		this.choices = choices;
		this.answers = answers;
		this.variableIDs = variableIDs;
		this.variableValues = variableValues;
		this.rootQuestions = rootQuestions;

		// creating buttons for each choice

		answer1 = new JButton(String.format("%.2f", choices.get(questionNum)[0]));
		answer2 = new JButton(String.format("%.2f", choices.get(questionNum)[1]));
		answer3 = new JButton(String.format("%.2f", choices.get(questionNum)[2]));
		answer4 = new JButton(String.format("%.2f", choices.get(questionNum)[3]));

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
		panel1.setBackground(indigo);

		panel2 = new JPanel();
		ids = variableIDs.get(0);
		values = variableValues.get(0);
		for (int j = 0; j < ids.length; j++) {
			try {
				try {
					Double.parseDouble(ids[j]);
				} catch (NumberFormatException e) {
					panel2.add(new JLabel(new ImageIcon(QuizEditor.stringToImage(ids[j]))));
					JLabel value = new JLabel(" = " + String.format("%.2f", values[j]) + "  ");
					value.setFont(font2);
					value.setForeground(lightBlue);
					panel2.add(value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
		panel2.setBackground(indigo);
		nextButton = new JButton();
		try {
			nextButton = new JButton(new ImageIcon(ImageIO.read(new File("nextButton.png"))));
		} catch (Exception ex) {
		}
		exitButton = new JButton("Exit");

		questionLabel = new JLabel(
				"<html><div style='text-align: center;'>" + questions.get(questionNum) + "</div></html");
		questionLabel.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		questionLabel.setHorizontalAlignment(JLabel.CENTER);

		double lengthOnRow = Math.ceil(questions.get(questionNum).length() / 3.00);

		int size;

		if (lengthOnRow < 47) {
			size = 100;
		} else {
			size = (int) (100 - (lengthOnRow - 46));
		}

		questionLabel.setFont(new Font("Serif", Font.BOLD, size));
		questionLabel.setForeground(lightBlue);

		label = new JLabel("Question #" + Integer.toString(questionNum + 1));
		label.setFont(new Font("Serif", Font.BOLD, 100));
		label.setForeground(orange);
		nextButton.addActionListener(new NextButtonListener());
		int x = 50;
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(Box.createRigidArea(new Dimension(0, x)));
		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(0, x)));
		questionLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		questionLabel.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				100 + (int) (Math.ceil(
						questionLabel.getPreferredSize().getHeight() * (questionLabel.getPreferredSize().getWidth()
								/ Toolkit.getDefaultToolkit().getScreenSize().getWidth())))));
		questionLabel.setMinimumSize(questionLabel.getPreferredSize());
		panel.add(questionLabel);
		panel.add(Box.createRigidArea(new Dimension(0, x)));
		panel2.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		panel.add(panel2);
		panel.add(Box.createRigidArea(new Dimension(0, x)));
		panel1.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		panel.add(panel1);
		panel.add(Box.createRigidArea(new Dimension(0, x)));
		panel.setBackground(indigo);
		JPanel panel3 = new JPanel();

		//		nextButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
		exitButton.setFont(font3);
		exitButton.addActionListener(new ExitButtonListener());
		panel3.setBackground(indigo);
		panel3.setOpaque(true);
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
		panel3.add(Box.createHorizontalStrut(50));
		panel3.add(exitButton);
		Component c = Box.createHorizontalStrut((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		panel3.add(c);
		panel3.add(nextButton);
		panel3.add(Box.createHorizontalStrut(50));
		panel.add(panel3);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JScrollPane scroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(16);

		this.setContentPane(scroll);
		java.awt.Rectangle r = panel3.getBounds();
		panel.setPreferredSize(
				new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), r.y + r.height + 50));
		c.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()
				- exitButton.getWidth() - nextButton.getWidth() - 100), 0));
//		System.out.println(exitButton.getBounds());
//		System.out.println(nextButton.getBounds());

		questionWrong = 0;
		wrongQuestions = new SimpleLinkedList<Question>();
		

		//panel.remove(clap);
	} // End of constructor

	private class Answer1Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (choices.get(questionNum)[0] == answers.get(questionNum)) {
				answer1.setBackground(Color.GREEN);
				correct = true;
				if (answer3.getBackground() == defaultColor && answer2.getBackground() == defaultColor
						&& answer4.getBackground() == defaultColor) {
					right = true;
				}
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
				if (answer1.getBackground() == defaultColor && answer3.getBackground() == defaultColor
						&& answer4.getBackground() == defaultColor) {
					right = true;
				}
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
				if (answer1.getBackground() == defaultColor && answer2.getBackground() == defaultColor
						&& answer4.getBackground() == defaultColor) {
					right = true;
				}
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
				if (answer1.getBackground() == defaultColor && answer2.getBackground() == defaultColor
						&& answer3.getBackground() == defaultColor) {
					right = true;
				}
			} else {
				answer4.setBackground(Color.RED);
			}

		}
	}

	private class NextButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			panel2.removeAll();

			if (!right) {
				if (!wrongQuestions.contain(rootQuestions.get(questionNum))) {
					wrongQuestions.add(rootQuestions.get(questionNum));
				}
				questionWrong++;
			}
			right = false;
			questionNum++;

			if (questionNum == questions.size()) {
				new SummaryPage(wrongQuestions);
				dispose();
				return;
			}

			ids = variableIDs.get(questionNum);
			values = variableValues.get(questionNum);

			for (int j = 0; j < ids.length; j++) {
				try {
					panel2.add(new JLabel(new ImageIcon(QuizEditor.stringToImage(ids[j]))));
					JLabel value = new JLabel(" = " + String.format("%.2f", values[j]) + "  ");
					value.setFont(font2);
					value.setForeground(lightBlue);
					panel2.add(value);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			answer2.setBackground(defaultColor);
			answer1.setBackground(defaultColor);
			answer3.setBackground(defaultColor);
			answer4.setBackground(defaultColor);

			questionLabel
					.setText("<html><div style='text-align: center;'>" + questions.get(questionNum) + "</div></html");
			questionLabel.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
					100 + (int) (Math.ceil(
							questionLabel.getPreferredSize().getHeight() * (questionLabel.getPreferredSize().getWidth()
									/ Toolkit.getDefaultToolkit().getScreenSize().getWidth())))));
			questionLabel.setMinimumSize(questionLabel.getPreferredSize());
			
			label.setText("Question #" + Integer.toString(questionNum + 1));
			answer1.setText(String.format("%.2f", choices.get(questionNum)[0]));
			answer2.setText(String.format("%.2f", choices.get(questionNum)[1]));
			answer3.setText(String.format("%.2f", choices.get(questionNum)[2]));
			answer4.setText(String.format("%.2f", choices.get(questionNum)[3]));
			revalidate();
		}
	}

	private class ExitButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			window.dispose();
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
