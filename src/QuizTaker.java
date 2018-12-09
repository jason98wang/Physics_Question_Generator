import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.imgscalr.Scalr;

import data_structures.SimpleLinkedList;

import javax.swing.JComboBox;

/*
 * To Do
 * Main UI better
 * Preset questions
 */

public class QuizTaker {

	private int numQuestions;

	private SimpleLinkedList<Unit> units;
	private SimpleLinkedList<Question> rootQuestions;
	private SimpleLinkedList<Question> questions;
	private SimpleLinkedList<String> answers;
	private SimpleLinkedList<String[]> choices;
	private SimpleLinkedList<String> problemStatements;
	private SimpleLinkedList<String[]> variableIDs;
	private SimpleLinkedList<double[]> variableValues;
	public static Subject chosenSubject;
	private Unit chosenUnit;
	private static Student student;
	private String studentName;

	private Color indigo, lightBlue;
	private static Random rand;
	private BufferedImage logo;

	private static JFrame window;
	private JPanel title;
	private JPanel mainPanel;
	private JPanel optionsPanel;
	private JButton start;
	private JButton exit;
	private JButton stats;
	private JComboBox<String> unit;
	private JTextField numQuestionsField;

	///////////////////////////////////////////////////////////////////// CONSTRUCTORS///////////////////////

	public QuizTaker(Student student, Subject chosenSubject) {
		this.student = student;
		this.chosenSubject = chosenSubject;
		studentName = student.getName();
		rand = new Random();

		window = new JFrame();

		indigo = new Color(56, 53, 74);
		lightBlue = new Color(162, 236, 250);

		window.setSize(Toolkit.getDefaultToolkit().getScreenSize());

		try {
			logo = ImageIO.read(new File("logo.png"));
			logo = Scalr.resize(logo, (int) (window.getHeight() / 2));
		} catch (IOException e) {
			logo = null;
		}

		title = new LogoPanel();
		title.setBackground(indigo);
		title.setBorder(BorderFactory.createEmptyBorder(0, 0,
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 5), 0));

		start = new JButton("START");
		start.addActionListener(new StartButtonActionListener());
		start.setAlignmentX(Component.CENTER_ALIGNMENT);

		exit = new JButton("EXIT");
		exit.addActionListener(new ExitButtonActionListener());
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);

		stats = new JButton("Account Info");
		stats.addActionListener(new StatsListener());
		stats.setAlignmentX(Component.CENTER_ALIGNMENT);

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

		numQuestionsField = new JTextField("# of Questions");
		numQuestionsField.addFocusListener(new NumQuestionsFocusListener());
		numQuestionsField.addKeyListener(new StartKeyListener());

		optionsPanel = new JPanel();
		optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));
		optionsPanel.setBackground(indigo);
		optionsPanel.add(unit);
		optionsPanel.add(numQuestionsField);
		optionsPanel.setVisible(true);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(indigo);
		mainPanel.add(title);
		mainPanel.add(optionsPanel);
		mainPanel.add(start);
		mainPanel.add(exit);
		mainPanel.add(stats);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0,
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 5), 0));
		mainPanel.setVisible(true);

		window.add(mainPanel, BorderLayout.CENTER);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setUndecorated(true);
		window.requestFocusInWindow();
		window.setVisible(true);
	}

	public QuizTaker(SimpleLinkedList<Question> rootQuestions) {
		answers = new SimpleLinkedList<String>();
		choices = new SimpleLinkedList<String[]>();
		problemStatements = new SimpleLinkedList<String>();
		variableIDs = new SimpleLinkedList<String[]>();
		variableValues = new SimpleLinkedList<double[]>();
		questions = new SimpleLinkedList<Question>();

		Question tempQ;

		for (int i = 0; i < rootQuestions.size(); i++) {
			tempQ = rootQuestions.get(i);

			if (tempQ.isPreset()) {
				addWordQuestion(tempQ);
			} else {
				addNumQuestion(tempQ);
			}
		}
		new QuizTakerDisplay(problemStatements, choices, answers, variableIDs, variableValues, questions, student);
		window.dispose();

	}

	///////////////////////////////////////////////////////// METHODS//////////////////////////

	private void addUnits() {

		units = chosenSubject.getUnits();

		for (int i = 0; i < units.size(); i++) {
			unit.addItem(units.get(i).getName());
		}

	}

	private void startQuiz() {
		answers = new SimpleLinkedList<String>();
		choices = new SimpleLinkedList<String[]>();
		problemStatements = new SimpleLinkedList<String>();
		variableIDs = new SimpleLinkedList<String[]>();
		variableValues = new SimpleLinkedList<double[]>();
		questions = new SimpleLinkedList<Question>();

		Question tempQ;

		rootQuestions = chosenUnit.getQuestions();
		
		for (int i = 0; i < numQuestions; i++) {
			tempQ = rootQuestions.get(rand.nextInt(rootQuestions.size()));
			
			if (tempQ.isPreset()) {
				addWordQuestion(tempQ);
			} else {
				addNumQuestion(tempQ);
			}
		}
		new QuizTakerDisplay(problemStatements, choices, answers, variableIDs, variableValues, questions, student);
		window.dispose();

	}

	private void addWordQuestion(Question tempQ) {

		String ans;
		String problemStatement;
		String[] wrongAns;
		String[] choicesArray;
		String[][] stringQuestions;

		boolean ansAdded;
		int ansIndex;

		stringQuestions = tempQ.getStringQuestions(1);

//		for (int row = 0; row < stringQuestions.length; row++) {
			problemStatement = stringQuestions[0][0];

			ans = stringQuestions[0][1];

			wrongAns = new String[3];
			wrongAns[0] = stringQuestions[0][2];
			wrongAns[1] = stringQuestions[0][3];
			wrongAns[2] = stringQuestions[0][4];

			choicesArray = new String[4];
			ansIndex = rand.nextInt(choicesArray.length);
			ansAdded = false;

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

			problemStatements.add(problemStatement);
			answers.add(ans);
			choices.add(choicesArray);
			variableIDs.add(null);
			variableValues.add(null);
			
			questions.add(new Question(tempQ.getProblemStatement(), tempQ.getSpecificQuestions(),
					tempQ.getSpecificAnswers(), tempQ.getPossibleAnswers(), tempQ.getImage()));
//		}

	}

	private void addNumQuestion(Question tempQ) {

		SimpleLinkedList<Symbol> formula;
		SimpleLinkedList<Variable> tempVariables;

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

		ans = String.format("%.2f",tempQ.getAnswer());
		answers.add(ans);

		tempWrongAns = tempQ.getFalseAnswers();
		wrongAns = new String[tempWrongAns.length];
		for (int j = 0; j < tempWrongAns.length; j++) {
			wrongAns[j] = tempWrongAns[j] + "";
		}

		choicesArray = new String[wrongAns.length + 1];
		ansIndex = rand.nextInt(choicesArray.length);
		ansAdded = false;

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

		problemStatement = tempQ.getProblemStatement();
		problemStatements.add(problemStatement);

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
		IDs = new String[tempVariables.size()];
		values = new double[tempVariables.size()];
		for (int k = 0; k < tempVariables.size(); k++) {
			IDs[k] = tempVariables.get(k).getId();
			values[k] = tempVariables.get(k).getValue();
		}
		variableIDs.add(IDs);
		variableValues.add(values);

		questions.add(new Question(problemStatement, formula));
	}

	////////////////////////////////////////////////////// PRIVATE
	////////////////////////////////////////////////////// CLASSES////////////////////////////////

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

	private class ExitButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			window.dispose();
		}

	}

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

	private class StatsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			new ViewStats(student);

		}

	}

}
