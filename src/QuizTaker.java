import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class QuizTaker {

	private int numQuestions;

	private Database database;
	private SimpleLinkedList<Subject> subjects;
	private SimpleLinkedList<Unit> units;
	private SimpleLinkedList<Question> rootQuestions;
	private SimpleLinkedList<Double> answers;
	private SimpleLinkedList<double[]> choices;
	private SimpleLinkedList<String> problemStatements;
	private SimpleLinkedList<String[]> variableIDs;
	private SimpleLinkedList<double[]> variableValues;
	private Subject chosenSubject;
	private Unit chosenUnit;

	private Random rand;

	private JFrame window;
	private JLabel title;
	private JPanel mainPanel;
	private JPanel optionsPanel;
	private JButton start;
	private JButton exit;
	private JComboBox<String> subject, unit;
	private JTextField numQuestionsField;

	QuizTaker() {
		window = new JFrame("Practice Like A Physicist");

		database = new Database();

		subjects = database.getSubjects();
//		subjects = new SimpleLinkedList<Subject>();
//		subjects.add(new Subject("Physics", 11, "U"));
//		subjects.add(new Subject("Physics", 12, "U"));
//		subjects.add(new Subject("Physics", 11, "AP"));
//
//		subjects.get(0).addUnit(new Unit("TLAP", 1));
//		subjects.get(0).addUnit(new Unit("Kinematics", 1));
//		subjects.get(0).addUnit(new Unit("Dynamics", 1));
//
//		subjects.get(1).addUnit(new Unit("No TLAP", 1));
//		subjects.get(1).addUnit(new Unit("CMPM", 1));
//		subjects.get(1).addUnit(new Unit("Momentum", 1));
//
//		subjects.get(2).addUnit(new Unit("TLAP but harder", 1));
//		subjects.get(2).addUnit(new Unit("Kinematics but harder", 1));
//		subjects.get(2).addUnit(new Unit("Dynamics but harder", 1));

		////////////////////////////////////////////////// GUI
		////////////////////////////////////////////////// STUFF/////////////////////////////////////

		title = new JLabel("PRACTICE LIKE A PHYSICIST");
		title.setFont(new Font("Serif", Font.PLAIN, 30));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);

		start = new JButton("START");
		start.addActionListener(new StartButtonActionListener());
		start.setAlignmentX(Component.CENTER_ALIGNMENT);

		exit = new JButton("EXIT");
		exit.addActionListener(new ExitButtonActionListener());
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);

		unit = new JComboBox<String>();
		unit.addActionListener(new UnitActionListener());
//		unit.setAlignmentY(Component.CENTER_ALIGNMENT);

		subject = new JComboBox<String>();
		subject.addActionListener(new SubjectActionListener());
		addSubjects();
//		subject.setAlignmentY(Component.CENTER_ALIGNMENT);

		numQuestionsField = new JTextField("# of Questions");
		numQuestionsField.addFocusListener(new NumQuestionsFocusListener());
//		numQuestionsField.setAlignmentY(Component.CENTER_ALIGNMENT);

		optionsPanel = new JPanel();
		optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));
		optionsPanel.add(subject);
		optionsPanel.add(unit);
		optionsPanel.add(numQuestionsField);
		optionsPanel.setVisible(true);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(title);
		mainPanel.add(optionsPanel);
		mainPanel.add(start);
		mainPanel.add(exit);
		mainPanel.setBorder(
				BorderFactory.createEmptyBorder((int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 10), 0,
						(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 10), 0));
		mainPanel.setVisible(true);

		window.add(mainPanel, BorderLayout.CENTER);

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		window.setResizable(false);
		window.requestFocusInWindow();
		window.setVisible(true);
	}

	private void addSubjects() {
		for (int i = 0; i < subjects.size(); i++) {
			subject.addItem(
					subjects.get(i).getName() + " " + subjects.get(i).getGrade() + " " + subjects.get(i).getLevel());
		}
	}

	private void addUnits() {

		units = chosenSubject.getUnits();

		if (unit != null) {
			unit.removeAllItems();
		}

		for (int i = 0; i < units.size(); i++) {
			unit.addItem(units.get(i).getName());
		}

	}

	private void startQuiz() {
		answers = new SimpleLinkedList<Double>();
		choices = new SimpleLinkedList<double[]>();
		problemStatements = new SimpleLinkedList<String>();
		variableIDs = new SimpleLinkedList<String[]>();
		variableValues = new SimpleLinkedList<double[]>();
		
		SimpleLinkedList<Symbol> formula;
		SimpleLinkedList<Variable> tempVariables;
		Question tempQ;
		int tempRand;

		double[] wrongAns;
		double ans;
		String[] IDs;
		double[] values;
		String problemStatement;

		rand = new Random();
		rootQuestions = chosenUnit.getQuestions();

		for (int i = 0; i < numQuestions; i++) {
			tempQ = rootQuestions.get(rand.nextInt(rootQuestions.size()));
			tempVariables = new SimpleLinkedList<Variable>();

			ans = tempQ.getAnswer();
			answers.add(ans);

			wrongAns = new double[3];
			wrongAns[0] = tempQ.getFalseAnswers()[0];
			wrongAns[1] = tempQ.getFalseAnswers()[1];
			wrongAns[2] = tempQ.getFalseAnswers()[2];
			tempRand = rand.nextInt(4);
			if (tempRand == 3) {
				choices.add(new double[] { ans, wrongAns[0], wrongAns[1], wrongAns[2] });
			} else if (tempRand == 2) {
				choices.add(new double[] { wrongAns[0], ans, wrongAns[1], wrongAns[2] });
			} else if (tempRand == 1) {
				choices.add(new double[] { wrongAns[0], wrongAns[1], ans, wrongAns[2] });
			} else {
				choices.add(new double[] { wrongAns[0], wrongAns[1], wrongAns[2], ans });
			}

			problemStatement = tempQ.getProblemStatement();
			problemStatements.add(problemStatement);

			formula = tempQ.getFormula();
			for(int j = 0; j < formula.size(); j++) {
				if(formula.get(j) instanceof Variable) {
					tempVariables.add((Variable)formula.get(j));
				}
			}
			IDs = new String[tempVariables.size()];
			values = new double[tempVariables.size()];
			for(int k = 0; k < tempVariables.size(); k++) {
				IDs[k] = tempVariables.get(k).getId();
				values[k] = tempVariables.get(k).getValue();
			}
			variableIDs.add(IDs);
			variableValues.add(values);
			
		}
		new QuizTakerDisplay(problemStatements,choices,answers,variableIDs,variableValues); 
		window.dispose();
		
	}

	////////////////////////////////////////////////////// PRIVATE
	////////////////////////////////////////////////////// CLASSES////////////////////////////////

	private class SubjectActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String subjectName = (String) subject.getSelectedItem();

			for (int i = 0; i < subjects.size(); i++) {
				if (subjectName.equals(subjects.get(i).getName() + " " + subjects.get(i).getGrade() + " "
						+ subjects.get(i).getLevel())) {
					chosenSubject = subjects.get(i);
				}
			}

			addUnits();
		}

	}

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
				Integer.parseInt(numQuestionsField.getText());
			} catch (Exception s) {
				validNum = false;
			}

			if (validNum) {
				numQuestions = Integer.parseInt(numQuestionsField.getText());

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
}
