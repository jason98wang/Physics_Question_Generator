/*
 * [QuizTakerDisplay.java]
 * The User Interface where the user takes the quiz
 * Author: Jason Wang
 * Nov. 20, 2018
 */
//Imports
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import data_structures.SimpleLinkedList;

class QuizTakerDisplay extends JFrame {

	// Declaring Variables
	private static JFrame window;

	private boolean correct = false;
	private boolean correct1 = false;

	private JButton nextButton, exitButton;

	private JLabel label;
	private JLabel questionLabel;
	private JPanel panel, choicesPanel;
	private JLabel clapping = new JLabel(new ImageIcon("clapping.gif"));
	private Dimension bottom, choice;
	private long time;

	private JPanel variablePanel, bottomPanel;
	private Component c;
	private Font font1 = new Font("Serif", Font.BOLD, 100);
	private Font font2 = new Font("Arial", Font.ITALIC, 50);
	private Font font3 = new Font("Serif", Font.BOLD, 25);
	private Color indigo = new Color(56, 53, 74);
	private Color lightBlue = new Color(162, 236, 250);
	private Color orange = new Color(255, 168, 104);
	private Color defaultColor = new JButton().getBackground();
	private int questionNum = 0;
	private String[] ids;
	private double[] values;
	private static int questionWrong;
	private static SimpleLinkedList<String> questionStatement;
	private static SimpleLinkedList<Question> wrongQuestions;
	private SimpleLinkedList<String> answers;
	private SimpleLinkedList<String[]> choices;
	private SimpleLinkedList<String[]> variableIDs;
	private SimpleLinkedList<double[]> variableValues;
	private SimpleLinkedList<Question> rootQuestions;

	private static Student student;
	private boolean right = true;
	private boolean clicked = false;
	private boolean finished = false;

	// Constructor
	QuizTakerDisplay(SimpleLinkedList<String> question, SimpleLinkedList<String[]> choices,
			SimpleLinkedList<String> answers, SimpleLinkedList<String[]> variableIDs,
			SimpleLinkedList<double[]> variableValues, SimpleLinkedList<Question> rootQuestions, Student student) {

		super("Practice Like A Physicist");
		// Set the frame to full screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(false);
		window = this;

		// Set up the main panel
		panel = new JPanel() {

			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				//if user gets right answer go to the next question
				if (correct) {
					correct = false;
					correct1 = true;

				} else if (correct1) {
					if (time > 0) {
						if (System.currentTimeMillis() - time >= 500) {
							time = 0;
							this.remove(clapping);
							correct1 = false;
							ActionEvent e = new ActionEvent(nextButton, 0, "");
							nextButton.getActionListeners()[0].actionPerformed(e);
						}
					} else {
						time = System.currentTimeMillis();
					}
				}

				g.setColor(lightBlue);
				g.drawString("Current User: " + student.getName(), window.getHeight() / 50, window.getHeight() / 50);
				repaint();
			}
		};

		//setting size of main panel based on screen size
		panel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

		// Focus the frame
		this.requestFocusInWindow();
		this.setUndecorated(true);

		// Make the frame visible
		this.setVisible(true);

		this.questionStatement = question;
		this.choices = choices;
		this.answers = answers;
		this.variableIDs = variableIDs;
		this.variableValues = variableValues;
		this.rootQuestions = rootQuestions;
		
		//traverse and make the longest question be at the top of the list
//				if (question.size() > 1) {
//					int max = -1;
//					int index = 0;
//					for (int i = 0; i < question.size(); i++) {
//						if (question.get(i).length() > max) {
//							max = question.get(i).length();
//							index = i;
//						}
//					}
//					
//					question.addToFront(question.get(index));
//					question.remove(index + 1);
//
//					choices.addToFront(choices.get(index));
//					choices.remove(index + 1);
//
//					answers.addToFront(answers.get(index));
//					answers.remove(index + 1);
//
//					variableIDs.addToFront(variableIDs.get(index));
//					variableIDs.remove(index + 1);
//
//					variableValues.addToFront(variableValues.get(index));
//					variableValues.remove(index + 1);
//
//					rootQuestions.addToFront(rootQuestions.get(index));
//					rootQuestions.remove(index + 1);
//
//				}

		// creating buttons for each choice based on number of options
		choicesPanel = new JPanel();
		SimpleLinkedList<JButton> buttonlist = new SimpleLinkedList<JButton>();

		variablePanel = new JPanel();
		variablePanel.setLayout(new BoxLayout(variablePanel, BoxLayout.X_AXIS));
		variablePanel.setBackground(indigo);

		//determine if the current question contains a number or word answer
		if (variableIDs.get(questionNum) == null) {
			displayWordAnswerQuestions();
			
			//adding the custom inputed picture
			Image pic = (rootQuestions.get(questionNum).getImage()).getScaledInstance(500, 300, Image.SCALE_DEFAULT);
			variablePanel.add(new JLabel (new ImageIcon(pic)));
		} else {
			displayNumberAnswerQuestions();
			//creating panel for the variables and displaying them		
			ids = variableIDs.get(0);
			values = variableValues.get(0);
			for (int j = 0; j < ids.length; j++) {
				try {
					try {
						Double.parseDouble(ids[j]);
					} catch (NumberFormatException e) {
						variablePanel.add(new JLabel(new ImageIcon(QuizEditor.stringToImage(ids[j]))));
						JLabel value = new JLabel(" = " + String.format("%.2f", values[j]) + "  ");
						value.setFont(font2);
						value.setForeground(lightBlue);
						variablePanel.add(value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		//creating the next button
		nextButton = new JButton();
		try {
			nextButton = new JButton(new ImageIcon(ImageIO.read(new File("nextButton.png"))));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		nextButton.setContentAreaFilled(false);
		nextButton.setBorder(BorderFactory.createEmptyBorder());
		nextButton.addActionListener(new NextButtonListener());

		//creating the exit page button
		exitButton = new JButton("Exit");
		exitButton.setFont(font3);
		exitButton.addActionListener(new ExitButtonListener());

		//creating a label for the question number
		label = new JLabel("Question #" + Integer.toString(questionNum + 1));
		label.setFont(new Font("Serif", Font.BOLD, 100));
		label.setForeground(orange);

		//Creating a label for the question and centering it on the page
		questionLabel = new JLabel(
				"<html><div style='text-align: center;'>" + getQuestionStatment().get(questionNum) + "</div></html");
		questionLabel.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		questionLabel.setHorizontalAlignment(JLabel.CENTER);
		questionLabel.setFont(new Font("Serif", Font.BOLD, 100));
		questionLabel.setForeground(lightBlue);

		//setting the size of question label based on the size of the question
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

		//adding panels and labels to the main panel 
		panel.add(questionLabel);
		panel.add(Box.createRigidArea(new Dimension(0, x)));
		variablePanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		panel.add(variablePanel);
		panel.add(Box.createRigidArea(new Dimension(0, x)));
		choicesPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		panel.add(choicesPanel);
		panel.add(Box.createRigidArea(new Dimension(0, x)));
		panel.setBackground(indigo);

		//create panel to add the back button and the exit butotn 
		bottomPanel = new JPanel();
		bottomPanel.setBackground(indigo);
		bottomPanel.setOpaque(true);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.add(Box.createHorizontalStrut(50));
		bottomPanel.add(exitButton);
		c = Box.createHorizontalStrut((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		bottomPanel.add(c);
		bottomPanel.add(nextButton);
		bottomPanel.add(Box.createHorizontalStrut(50));
		bottomPanel.setBackground(indigo);
		panel.add(bottomPanel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		//creating a scroll wheel in case the question is too large to fit on screen 
		JScrollPane scroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(16);

		this.setContentPane(scroll);
		java.awt.Rectangle r = bottomPanel.getBounds();

		//setting the size of the panel
		panel.setPreferredSize(
				new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), r.y + r.height + 50));
		c.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()
				- exitButton.getWidth() - nextButton.getWidth() - 100), 0));

		//reseting variables
		questionWrong = 0;
		wrongQuestions = new SimpleLinkedList<Question>();
		choice = choicesPanel.getMinimumSize();
		bottom = bottomPanel.getMinimumSize();
		this.student = student;
	} // End of constructor

	/**
	 * getQuestionStatment
	 * This method returns the question statement
	 * @return questionStatement, the question statement 
	 */
	public static SimpleLinkedList<String> getQuestionStatment() {
		return questionStatement;
	}

	/**
	 * getWrongQuestions
	 * This method returns the list of wrong questions 
	 * @return wrongQuestions, SimpleLinkedList of wrong questions in the question type
	 */
	public static SimpleLinkedList<Question> getWrongQuestions() {
		return wrongQuestions;
	}

	/**
	 * getQuestionWrong
	 * This method returns the number of wrong questions
	 * @return questionWrong, the number of questions the user got wrong 
	 */
	public static int getQuestionWrong() {
		return questionWrong;
	}

	/**
	 * playMusic
	 * This method plays a sound signifying the user got the question correct
	 */
	public void playMusic() {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File("CorrectSound.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	/**
	 * displayNumberAnswerQuestions
	 * This method displays a question of the number type(Number as an answer)
	 */
	public void displayNumberAnswerQuestions() {
		//creating buttons and adding them to a list of buttons 
		SimpleLinkedList<JButton> buttonlist = new SimpleLinkedList<JButton>();
		//create buttons based on the number of choices needed
		for (int i = 0; i < choices.get(questionNum).length; i++) {
			//round the number displayed on the button
			JButton button = new JButton(String.format("%.2f", Double.parseDouble(choices.get(questionNum)[i])));
			button.setFont(font3);
			button.setOpaque(true);
			button.setBorderPainted(false);
			buttonlist.add(button);
			//create actionListner for the button created
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (button.getText().equals(answers.get(questionNum))) {
						//if the user gets the question right, change color to green, play music and move on to the next question
						correct = true;
						finished = true;
						for (int i = 0; i < buttonlist.size(); i++) {
							//set the question to incorrect if the user made an incorrect choice prior to the correct one
							if ((buttonlist.get(i).getBackground()) != defaultColor) {
								right = false;
							}
						}
						button.setBackground(Color.GREEN);
						playMusic();
					} else {
						//if the user gets the question wrong, change the color of button to red
						button.setBackground(Color.RED);
					}
					//set true if the user attempts the question
					clicked = true;
				}
			});

			//add the created button choicesPanel
			choicesPanel.add(button);
			choicesPanel.add(Box.createRigidArea(new Dimension(100, 0)));
			choicesPanel.setBackground(null);
		}
	}

	/**
	 * displayWordAnswerQuestions
	 * This method displays a question of the word type(word as an answer, customized added by teacher)
	 */
	public void displayWordAnswerQuestions() {
		//creating buttons and adding them to a list of buttons 
		SimpleLinkedList<JButton> buttonlist = new SimpleLinkedList<JButton>();
		//creating buttons based on number of choices 
		for (int i = 0; i < choices.get(questionNum).length; i++) {
			JButton button = new JButton(choices.get(questionNum)[i]);
			button.setFont(font3);
			button.setOpaque(true);
			button.setBorderPainted(false);
			
			buttonlist.add(button);
			//create action listener for the button
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (button.getText().equals(answers.get(questionNum))) {
						//if the user gets the question right, change color to green, play music and move on to the next question
						correct = true;
						finished = true;
						for (int i = 0; i < buttonlist.size(); i++) {
							///set the question to incorrect if the user made an incorrect choice prior to the correct one
							if ((buttonlist.get(i).getBackground()) != defaultColor) {
								right = false;
							}
						}
						button.setBackground(Color.GREEN);
						playMusic();
					} else {
						//change button color to red if the user gets the question wrong
						button.setBackground(Color.RED);
					}
					clicked = true;
				}
			});

			//adding button to the choicesPanel
			choicesPanel.add(button);
			choicesPanel.add(Box.createRigidArea(new Dimension(100, 0)));
			choicesPanel.setBackground(null);
			
		}
	}

	private class NextButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//remove labels/images currently on the panel
			variablePanel.removeAll();
			choicesPanel.removeAll();

			//Add question to wrongQuesitons and add on to number of questions wrong if the user didnt get the question correct
			if (!right || !clicked || !finished) {
				if (!wrongQuestions.contain(rootQuestions.get(questionNum))) {
					wrongQuestions.add(rootQuestions.get(questionNum));
				}
				questionWrong++;
			}
			
			//increase the question number index 
			questionNum++;
			
			//if we reached the end, close this page and create summary page
			if (questionNum == getQuestionStatment().size()) {
				System.out.println("run");
				new SummaryPage(wrongQuestions, student);
				dispose();
				return;
			}

			//determine if the next question is a word answer question of number answer
			if (variableIDs.get(questionNum) == null) {
				
				//display the word answer question
				displayWordAnswerQuestions();

				//add the picture added by the teacher
				Image pic = (rootQuestions.get(questionNum).getImage()).getScaledInstance(500, 300, Image.SCALE_DEFAULT);
				variablePanel.add(new JLabel (new ImageIcon(pic)));
			} else {
				//display the number answer question
				displayNumberAnswerQuestions();
				
				//add the picture of the vaiables and its values
				ids = variableIDs.get(questionNum);
				values = variableValues.get(questionNum);
				if (ids != null) {
					for (int j = 0; j < ids.length; j++) {
						try {
							variablePanel.add(new JLabel(new ImageIcon(QuizEditor.stringToImage(ids[j]))));
							JLabel value = new JLabel(" = " + String.format("%.2f", values[j]) + "  ");
							value.setFont(font2);
							value.setForeground(lightBlue);
							variablePanel.add(value);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}

			//resetting theses attributes for the next question
			right = true;
			clicked = false;
			finished = false;

			//change the question on question label and reformat based on the question length
			questionLabel.setText("<html><div style='text-align: center;'>" + getQuestionStatment().get(questionNum)
					+ "</div></html");
			Graphics g = questionLabel.getGraphics();
			int width = g.getFontMetrics(questionLabel.getFont()).stringWidth(getQuestionStatment().get(questionNum));
			int height = g.getFontMetrics(questionLabel.getFont()).getHeight();
//			System.out.println(x);
			questionLabel.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
					100 + height * (int) (Math.ceil(
							(double)width / Toolkit.getDefaultToolkit().getScreenSize().getWidth()))));
			questionLabel.setMaximumSize(questionLabel.getPreferredSize());
			questionLabel.setMinimumSize(questionLabel.getPreferredSize());
			
			//change the question number of the label
			label.setText("Question #" + Integer.toString(questionNum + 1));
			bottomPanel.setPreferredSize(bottom);
			bottomPanel.setMaximumSize(bottomPanel.getPreferredSize());
			choicesPanel.setPreferredSize(choice);
			choicesPanel.setMaximumSize(choicesPanel.getPreferredSize());
			revalidate();
			java.awt.Rectangle r = bottomPanel.getBounds();
			
			panel.setPreferredSize(
					new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), r.y + r.height));
			panel.setSize(panel.getPreferredSize());
			panel.setMinimumSize(panel.getPreferredSize());
			panel.setMaximumSize(panel.getPreferredSize());
			revalidate();
//			System.out.println(choicesPanel.getSize());
//			System.out.println(panel.getSize());
			
		}
	}

	/*
	 * [ExitButtonListener.java]
	 * Private class ActionListener for the exit button
	 */
	private class ExitButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//dispose the window if the exit button is clicked
			window.dispose();
			//update the database after file closes
			Login.getDatabase().update();
		}
	}

}
