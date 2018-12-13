/**
 * [QuizTaker.java]
 * This class displays the main page for the application after logging in
 * Authors: Jason Wang, Yili Liu, Eric Long, Josh Cai, Brian Li
 * Date: December 9. 2018
 */

//java imports
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

//jar import
import org.imgscalr.Scalr;

//data structure import
import data_structures.SimpleLinkedList;

public class QuizTaker {

	// Init var
	private int numQuestions; // number of questions the user wants to do
	private String studentName;
	private static Random rand;

	// Lists for parts of each question
	private SimpleLinkedList<Unit> units;
	private SimpleLinkedList<Question> rootQuestions;
	private SimpleLinkedList<Question> questions;
	private SimpleLinkedList<String> answers;
	private SimpleLinkedList<String[]> choices;
	private SimpleLinkedList<String> problemStatements;
	private SimpleLinkedList<String[]> variableIDs;
	private SimpleLinkedList<double[]> variableValues;

	// Objects for use in generating questions
	public static Subject chosenSubject;
	private Unit chosenUnit;

	// Current user
	private static Student student;

	// Logo picture and colours
	private Color indigo, lightBlue;
	private BufferedImage logo;

	// Java gui components
	private static JFrame window;
	private JPanel title;
	private JPanel mainPanel;
	private JPanel optionsPanel;
	private JButton start;
	private JButton logout;
	private JButton stats;
	private JComboBox<String> unit;
	private JTextField numQuestionsField;

	/**
	 * QuizTaker 
	 * This method creates a new QuizTaker with the given student and
	 * subject
	 * 
	 * @param student, a Student that represents the current user
	 * @param chosenSubject, a Subject representing the subject the student belongs
	 *        to
	 */
	public QuizTaker(Student student, Subject chosenSubject) {
		this.student = student;
		this.chosenSubject = chosenSubject;

		studentName = student.getName();
		rand = new Random();

		// Init colours
		indigo = new Color(56, 53, 74);
		lightBlue = new Color(162, 236, 250);

		// Init main window
		window = new JFrame();
		window.setSize(Toolkit.getDefaultToolkit().getScreenSize());

		// Init logo image
		try {
			logo = ImageIO.read(new File("assets/logo.png"));
			logo = Scalr.resize(logo, (int) (window.getHeight() / 2));
		} catch (IOException e) {
			logo = null;
		}

		// Init JPanel for logo
		title = new LogoPanel();
		title.setBackground(indigo);
		title.setBorder(BorderFactory.createEmptyBorder(0, 0,
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 5), 0));

		// Init start, logout, and stats buttons
		start = new JButton("START");
		start.addActionListener(new StartButtonActionListener());
		start.setAlignmentX(Component.CENTER_ALIGNMENT);

		logout = new JButton("LOGOUT");
		logout.addActionListener(new LogoutButtonActionListener());
		logout.setAlignmentX(Component.CENTER_ALIGNMENT);

		stats = new JButton("Account Info");
		stats.addActionListener(new StatsListener());
		stats.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Init unit combo box and add all units into it
		unit = new JComboBox<String>();
		unit.addActionListener(new UnitActionListener());
		unit.addKeyListener(new StartKeyListener());
		addUnits();
		if (unit.getSelectedItem() != null) {
			String unitName = (String) unit.getSelectedItem();

			for (int i = 0; i < units.size(); i++) {
				if (unitName.equals(units.get(i).getName())) {
					chosenUnit = units.get(i);
				}
			}
		}

		// Init field used for entering numQuestions
		numQuestionsField = new JTextField("# of Questions");
		numQuestionsField.addFocusListener(new NumQuestionsFocusListener());
		numQuestionsField.addKeyListener(new StartKeyListener());

		// Init panel that contains the combobox and textfield and add them
		optionsPanel = new JPanel();
		optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));
		optionsPanel.setBackground(indigo);
		optionsPanel.add(unit);
		optionsPanel.add(numQuestionsField);
		optionsPanel.setVisible(true);

		// Init main panel and add everything
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(indigo);
		mainPanel.add(title);
		mainPanel.add(optionsPanel);
		mainPanel.add(start);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
		mainPanel.add(logout);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
		mainPanel.add(stats);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0,
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 5), 0));
		mainPanel.setVisible(true);

		// Main window settings and add mainPanel
		window.add(mainPanel, BorderLayout.CENTER);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setUndecorated(true);
		window.requestFocusInWindow();
		window.setVisible(true);
	}

	/**
	 * QuizTaker 
	 * This method takes in a list of root questions, recalculates a list
	 * of questions, and creates a new QuizTakerDisplay
	 * 
	 * @param rootQuestions, a SimpleLinkedList of Question objects representing the
	 *        list of root questions
	 */
	public QuizTaker(SimpleLinkedList<Question> rootQuestions) {

		// Init lists for parts of each question
		answers = new SimpleLinkedList<String>();
		choices = new SimpleLinkedList<String[]>();
		problemStatements = new SimpleLinkedList<String>();
		variableIDs = new SimpleLinkedList<String[]>();
		variableValues = new SimpleLinkedList<double[]>();
		questions = new SimpleLinkedList<Question>();

		// Init temporary question
		Question tempQ;

		for (int i = 0; i < rootQuestions.size(); i++) { // Loop through every root question
			tempQ = rootQuestions.get(i);

			if (tempQ.isPreset()) { // If the question is a preset question
				if (problemStatements.contain(tempQ.getProblemStatement())) { // If the question already exists in the
																				// list
					while (tempQ.isPreset()) {
						tempQ = rootQuestions.get(rand.nextInt(rootQuestions.size())); // Get a question that isn't
																						// preset
					}
					addNumQuestion(tempQ); // Call method to add a number question
				} else { // If the question doesn't already exist
					addWordQuestion(tempQ); // Call method to add a word question
				}
			} else {
				addNumQuestion(tempQ);
			}
		}
		// Create new display
		new QuizTakerDisplay(problemStatements, choices, answers, variableIDs, variableValues, questions, student);
		window.dispose();

	}

	/*
	 * addUnits Adds all units in the chosen subject to the unit combo box
	 */
	private void addUnits() {

		units = chosenSubject.getUnits();

		for (int i = 0; i < units.size(); i++) {
			unit.addItem(units.get(i).getName());
		}

	}

	/*
	 * startQuiz 
	 * Calculates a list of questions based on the root questions within
	 * the chosen subject and the number of questions the user wants to do, then
	 * creates a new display
	 */
	private void startQuiz() {

		// Init lists
		answers = new SimpleLinkedList<String>();
		choices = new SimpleLinkedList<String[]>();
		problemStatements = new SimpleLinkedList<String>();
		variableIDs = new SimpleLinkedList<String[]>();
		variableValues = new SimpleLinkedList<double[]>();
		questions = new SimpleLinkedList<Question>();
		rootQuestions = chosenUnit.getQuestions();

		// Init temporary question
		Question tempQ;

		for (int i = 0; i < numQuestions; i++) { // Loop based on numQuestions
			tempQ = rootQuestions.get(rand.nextInt(rootQuestions.size())); // Set tempQ to a question randomly chosen
																			// from
																			// the root questions

			if (tempQ.isPreset()) { // If question is preset
				if (problemStatements.contain(tempQ.getProblemStatement())) { // If already exists
					while (tempQ.isPreset()) {
						tempQ = rootQuestions.get(rand.nextInt(rootQuestions.size())); // Find number question
					}
					addNumQuestion(tempQ); // Add number question
				} else {
					addWordQuestion(tempQ); // Add word question
				}
			} else {
				addNumQuestion(tempQ); // Add number question
			}
		}

		// Create new display
		new QuizTakerDisplay(problemStatements, choices, answers, variableIDs, variableValues, questions, student);
		window.dispose();

	}

	/*
	 * addWordQuestion 
	 * Takes in a Question object, splits it up into its parts and
	 * adds those parts into their respective lists
	 */
	private void addWordQuestion(Question tempQ) {

		// Init vars and arrays
		String ans;
		String problemStatement;
		String[] wrongAns;
		String[] choicesArray;
		String[][] stringQuestions;
		boolean ansAdded; // Flag for is the answer has already been added to choices
		int ansIndex; // Index for where answer goes in choices

		stringQuestions = tempQ.getStringQuestions(1);

		// Get problem statement
		problemStatement = tempQ.getProblemStatement() + " " + stringQuestions[0][0];

		// Get answer
		ans = stringQuestions[0][1];

		// Get wrong answer array
		wrongAns = new String[3];
		wrongAns[0] = stringQuestions[0][2];
		wrongAns[1] = stringQuestions[0][3];
		wrongAns[2] = stringQuestions[0][4];

		// Set up choices array
		choicesArray = new String[4];
		ansIndex = rand.nextInt(choicesArray.length);
		ansAdded = false;

		// Add wrong answers and answer to choices
		for (int j = 0; j < choicesArray.length; j++) {
			if (ansAdded) {
				choicesArray[j] = wrongAns[j - 1];
			} else {
				if (j == ansIndex) {
					choicesArray[j] = ans;
					ansAdded = true;
				} else {
					choicesArray[j] = wrongAns[j];
				}
			}
		}

		// Add everything to their respective lists
		problemStatements.add(problemStatement);
		answers.add(ans);
		choices.add(choicesArray);
		variableIDs.add(null);
		variableValues.add(null);
		questions.add(new Question(tempQ.getProblemStatement(), tempQ.getSpecificQuestions(),
				tempQ.getSpecificAnswers(), tempQ.getPossibleAnswers(), tempQ.getImage()));

	}

	/*
	 * addNumQuestion
	 * Takes in a Question object, splits it up into its parts and
	 * adds those parts into their respective lists
	 */
	private void addNumQuestion(Question tempQ) {

		//Init lists
		SimpleLinkedList<Symbol> formula;
		SimpleLinkedList<Variable> tempVariables;

		//Init vars and arrays
		int ansIndex;
		String[] wrongAns;
		String[] choicesArray;
		double[] tempWrongAns;
		String ans;
		String[] IDs;
		double[] values;
		String problemStatement;
		boolean ansAdded;

		tempVariables = new SimpleLinkedList<Variable>();

		//Get and add answer
		ans = String.format("%.2f", tempQ.getAnswer());
		answers.add(ans);

		//Get wrong answers
		tempWrongAns = tempQ.getFalseAnswers();
		wrongAns = new String[tempWrongAns.length];
		for (int j = 0; j < tempWrongAns.length; j++) {
			wrongAns[j] = tempWrongAns[j] + "";
		}

		//Set up choices
		choicesArray = new String[wrongAns.length + 1];
		ansIndex = rand.nextInt(choicesArray.length);
		ansAdded = false;

		//Add wrong answers and answer to choices then add choices to list
		for (int j = 0; j < choicesArray.length; j++) {
			if (ansAdded) {
				choicesArray[j] = wrongAns[j - 1];
			} else {
				if (j == ansIndex) {
					choicesArray[j] = ans;
					ansAdded = true;
				} else {
					choicesArray[j] = wrongAns[j];
				}
			}
		}
		choices.add(choicesArray);

		//Get and add problemStatement
		problemStatement = tempQ.getProblemStatement();
		problemStatements.add(problemStatement);

		//Get formula
		//Break up formula into only the Variable objects
		formula = tempQ.getFormula();
		for (int j = 0; j < formula.size(); j++) {
			if (formula.get(j) instanceof Variable) {
				if (!((Variable) formula.get(j)).isConstant()) {
					boolean variableUsed = false;
					for (int k = 0; k < tempVariables.size(); k++) {
						if (tempVariables.get(k).getId().equals(formula.get(j).getId())) {
							variableUsed = true;
							break;
						}
					}
					if (variableUsed)
						continue;
					tempVariables.add((Variable) formula.get(j));
				}
			}
		}
		//Add ID and Value of each variable
		IDs = new String[tempVariables.size()];
		values = new double[tempVariables.size()];
		for (int k = 0; k < tempVariables.size(); k++) {
			IDs[k] = tempVariables.get(k).getId();
			values[k] = tempVariables.get(k).getValue();
		}
		//Add IDs and Values to lists
		variableIDs.add(IDs);
		variableValues.add(values);

		//Add question to list
		questions.add(new Question(problemStatement, formula));
	}
	
	/*
	 * UnitActionListener
	 * An action listener for the unit combo box
	 * Sets the chosen unit as the item selected in the combo box
	 */
	private class UnitActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (unit.getSelectedItem() != null) {
				String unitName = (String) unit.getSelectedItem();

				for (int i = 0; i < units.size(); i++) {
					if (unitName.equals(units.get(i).getName())) {
						chosenUnit = units.get(i);
					}
				}
			}
		}

	}

	/*
	 * NumQuestionsFocusListener
	 * A focus listener for the numQuestions field
	 * Displays "# of Questions" when field is empty and vice versa
	 */
	private class NumQuestionsFocusListener implements FocusListener {

		public void focusGained(FocusEvent e) {
			if (numQuestionsField.getText().trim().equals("# of Questions")) {
				numQuestionsField.setText("");
			}
		}

		public void focusLost(FocusEvent e) {
			if (numQuestionsField.getText().trim().equals("")) {
				numQuestionsField.setText("# of Questions");
			}
		}

	}

	/*
	 * StartButtonActionListener
	 * Action listener for the start button
	 * If the number of questions is valid, starts quiz
	 */
	private class StartButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			boolean validNum = true;

			try {
				numQuestions = Integer.parseInt(numQuestionsField.getText());
			} catch (Exception s) {
				validNum = false;
			}

			if (validNum && (numQuestions >= 1)) {
				startQuiz();
			} else {
				JOptionPane.showMessageDialog(null, "Invalid # of Questions");
			}
		}

	}

	/*
	 * LogoutButtonActionListener
	 * Action listener for the logout button
	 * Logs out and updates database
	 */
	private class LogoutButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			new Login();
			window.dispose();
			Login.getDatabase().update();
		}

	}
	
	/*
	 * StartKeyListener
	 * Key listener for the Enter key to start quiz
	 */
	private class StartKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				boolean validNum = true;

				try {
					numQuestions = Integer.parseInt(numQuestionsField.getText());
				} catch (Exception s) {
					validNum = false;
				}

				if (validNum && (numQuestions >= 1)) {
					startQuiz();
				} else {
					JOptionPane.showMessageDialog(null, "Invalid # of Questions");
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}

	/*
	 * LogoPanel
	 * Custom JPanel that draws the logo and current user
	 */
	private class LogoPanel extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setDoubleBuffered(true);

			g.drawImage(logo, (int) ((window.getWidth() / 2) - (logo.getWidth() / 2)), 0, null);

			g.setColor(lightBlue);
			g.drawString("Current User: " + studentName, window.getHeight() / 50, window.getHeight() / 50);

			repaint();
		}
	}

	/*
	 * StatsListener
	 * Action listener for the stats button
	 */
	private class StatsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			new ViewStats(student);

		}

	}

}
