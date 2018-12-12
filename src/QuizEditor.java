
/**
 * [QuizEditor.java]
 * This class is used to add/edit/remove questions into or out of the database
 * Authors: Eric Long
 * Date: Nov 20, 2018
 */
// Formatting imports
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

// Used for drop image
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

// ImageIO
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;

// Swing
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

// Data structure
import data_structures.SimpleLinkedList;

//Other
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * 
 * class for running the quiz editor user interface
 */
public class QuizEditor extends JFrame {

	// Objects
	private static QuizEditor quiz;
	private Subject s;
	private Unit u;
	private Question q, editQuestion;
	private SimpleLinkedList<Unit> unitList;

	// Booleans
	private boolean addQ, removeQ, editQ, newFormula, editingQ, noFormula;

	// JComponents
	private JLabel currentFormulaDisplay;
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> list = new JList<String>(listModel);
	private DefaultComboBoxModel<String> subjectModel = new DefaultComboBoxModel<String>();
	private JComboBox<String> subject = new JComboBox<String>(subjectModel);
	private DefaultComboBoxModel<String> unitModel = new DefaultComboBoxModel<String>();
	private JComboBox<String> unit = new JComboBox<String>(unitModel);
	private DefaultComboBoxModel<String> formulaModel = new DefaultComboBoxModel<String>();
	private JComboBox<String> formula = new JComboBox<String>(formulaModel);

	// Font and Color
	private Font font = new Font("Serif", Font.BOLD, 25);
	private Font small = new Font("Serif", Font.BOLD, 15);
	private Color indigo = new Color(56, 53, 74);
	private Color lightBlue = new Color(162, 236, 250);
	private Color orange = new Color(255, 168, 104);

	// Image chosen for question
	private BufferedImage image = null;

	// Offset for spacing out the components
	private int offset = 0;
	private int addingoffset = 20;

	/**
	 * Method that takes in everything from the database
	 * 
	 * @param database
	 *            database object where information is stored and retrieved from
	 */
	QuizEditor(Database database) {

		// Lists from database;
		SimpleLinkedList<Symbol> symbols = database.getSymbols();
		SimpleLinkedList<Subject> subjects = database.getSubjects();
		SimpleLinkedList<SimpleLinkedList<Symbol>> formulas = database.getFormulas();

		// Setting up frame
		setTitle("Quiz Editor");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setUndecorated(true);
		setResizable(false);
		setFocusable(true);
		requestFocusInWindow();
		quiz = this;

		// Initializing variables
		addQ = false;
		removeQ = false;
		editQ = false;
		editingQ = false;
		noFormula = false;
		s = null;
		u = null;
		q = null;

		// Action event for when the window is closed, updates database
		addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			/**
			 * Updates database
			 */
			@Override
			public void windowClosed(WindowEvent arg0) {
				database.update();
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}

		});
		
		JLabel logo = new JLabel(new ImageIcon("logo.png"));
		// Buttons on main frame
		JButton add = new JButton("Add");
		JButton remove = new JButton("Remove");
		JButton edit = new JButton("Edit");
		JButton addSubject = new JButton("Add new subject");
		JButton addUnit = new JButton("Add new unit");
		JButton removeSubject = new JButton("Remove current subject");
		JButton removeUnit = new JButton("Remove current unit");
		JButton editSubject = new JButton("Edit current subject");
		JButton editUnit = new JButton("Edit current unit");
		JButton confirm = new JButton("Confirm");
		JButton clear = new JButton("Clear");
		JButton exit = new JButton("Exit");

		
		// JPanels on the min frame
		JPanel contentPane = new JPanel();
		JPanel midPane = new JPanel();
		JPanel displayPane = new JPanel();
		JPanel displayFormula = new JPanel();

		// Panels for the add interface
		JPanel hasFormula = new JPanel();
		JPanel notFormula = new JPanel();

		// Formatting for jlist
		list.setBackground(lightBlue);

		// Formatting for combo box
		subject.setFont(font);
		subject.setBackground(lightBlue);
		((JLabel) subject.getRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
		subject.setPreferredSize(subject.getMinimumSize());

		unit.setFont(font);
		unit.setBackground(lightBlue);
		((JLabel) unit.getRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
		unit.setPreferredSize(unit.getMinimumSize());
		
		formula.setFont(small);
		((JLabel) formula.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		// Formatting for buttons
		add.setFont(font);
		add.setBackground(lightBlue);
		add.setPreferredSize(new Dimension(150, 100));

		remove.setFont(font);
		remove.setBackground(lightBlue);
		remove.setPreferredSize(new Dimension(200, 100));

		edit.setFont(font);
		edit.setBackground(lightBlue);
		edit.setPreferredSize(new Dimension(150, 100));

		addSubject.setFont(font);
		addSubject.setBackground(lightBlue);

		removeSubject.setFont(font);
		removeSubject.setBackground(lightBlue);
		
		editSubject.setFont(font);
		editSubject.setBackground(lightBlue);
		
		addUnit.setFont(font);
		addUnit.setBackground(lightBlue);

		removeUnit.setFont(font);
		removeUnit.setBackground(lightBlue);

		editUnit.setFont(font);
		editUnit.setBackground(lightBlue);
		
		confirm.setFont(font);
		confirm.setAlignmentX(JButton.CENTER_ALIGNMENT);
		confirm.setBackground(lightBlue);

		clear.setFont(font);
		clear.setAlignmentX(JButton.CENTER_ALIGNMENT);
		clear.setBackground(lightBlue);

		exit.setFont(font);
		exit.setAlignmentX(JButton.CENTER_ALIGNMENT);
		exit.setBackground(lightBlue);

		// Format display pane and the logo
		displayPane.setPreferredSize(
				new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 200, 300));
		displayPane.setBackground(lightBlue);

		logo.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		// Components on the add and edit interface when a formula is chosen
		JLabel problemStatement = new JLabel("Enter problem statement below");
		JLabel currentFormula = new JLabel("Current Formula");
		JTextArea enter = new JTextArea();
		currentFormulaDisplay = new JLabel();

		// Formatting
		currentFormulaDisplay.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		problemStatement.setFont(small);
		problemStatement.setHorizontalAlignment(SwingConstants.CENTER);

		enter.setFont(small);
		enter.setLineWrap(true);
		enter.setWrapStyleWord(true);
		enter.setEditable(true);

		currentFormula.setFont(small);

		// Adding everything onto the has formula pane which is the panel that is shown
		// when a formula is selected

		// Displays the formula that has been chosen and is scrollable in case the
		// formula s long
		displayFormula.add(currentFormula);
		displayFormula.add(Box.createVerticalStrut(addingoffset));
		currentFormula.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		displayFormula.add(currentFormulaDisplay);
		displayFormula.add(Box.createVerticalStrut(addingoffset));
		displayFormula.setLayout(new BoxLayout(displayFormula, BoxLayout.Y_AXIS));

		// making the formula scrollable
		JScrollPane scroll = new JScrollPane(displayFormula, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		scroll.setMinimumSize(new Dimension((int) displayPane.getPreferredSize().getWidth(), 200));
		hasFormula.add(scroll);
		hasFormula.add(Box.createVerticalStrut(addingoffset));

		// Displays the problem statement that the user types
		problemStatement.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		hasFormula.add(problemStatement);
		enter.setPreferredSize(new Dimension((int) enter.getPreferredSize().getWidth(),
				(int) enter.getPreferredSize().getHeight() * 20));

		// Problem statement is also scrollable
		JScrollPane enterScroll = new JScrollPane(enter, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		enterScroll.getVerticalScrollBar().setUnitIncrement(16);
		enterScroll.setPreferredSize(enter.getMinimumSize());
		enterScroll.setMinimumSize(enterScroll.getPreferredSize());
		hasFormula.add(enterScroll);
		hasFormula.setLayout(new BoxLayout(hasFormula, BoxLayout.Y_AXIS));

		// No formula to add
		JPanel leftPanel = new JPanel();
		DropPane dragImage = new DropPane();

		// Root Question Label
		JLabel rootQuestion = new JLabel("Root Question");
		rootQuestion.setFont(font);
		rootQuestion.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		// Text Area to enter root question with line wrap
		JTextArea enterQ = new JTextArea();
		enterQ.setLineWrap(true);
		enterQ.setWrapStyleWord(true);
		enterQ.setFont(small);
		
		// Makes the text area scrollable only if the root question is too long
		JScrollPane scrollEnter = new JScrollPane(enterQ, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollEnter.getVerticalScrollBar().setUnitIncrement(16);
		scrollEnter.setPreferredSize(new Dimension((int) enterQ.getPreferredSize().getWidth(),
				(int) enterQ.getPreferredSize().getHeight() * 3));

		
		// Panel with image selector and root question
		leftPanel.add(dragImage);
		leftPanel.add(rootQuestion);
		leftPanel.add(scrollEnter);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		
		// Panel with place to enter sub questions and answers and choices
		JPanel rightPanel = new JPanel();

		// Labels for 3 columns
		JLabel questionsLabel = new JLabel("Questions");
		questionsLabel.setFont(small);
		questionsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		JLabel rightAnswerLabel = new JLabel("Right Answer");
		rightAnswerLabel.setFont(small);
		rightAnswerLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		JLabel allAnswersLabel = new JLabel("Choices");
		allAnswersLabel.setFont(small);
		allAnswersLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		
		// Question list
		DefaultListModel<String> questionsModel = new DefaultListModel<String>();
		JList<String> questions = new JList<String>(questionsModel);
		JScrollPane questionsScroll = new JScrollPane(questions, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		questionsScroll.getVerticalScrollBar().setUnitIncrement(16);
		questionsScroll.getHorizontalScrollBar().setUnitIncrement(16);
		questionsScroll.setPreferredSize(new Dimension((int) questionsScroll.getPreferredSize().getWidth(),
				(int) questionsScroll.getPreferredSize().getHeight() * 2));

		
		// Answer list
		DefaultListModel<String> rightAnswerModel = new DefaultListModel<String>();
		JList<String> rightAnswer = new JList<String>(rightAnswerModel);
		JScrollPane rightAnswerScroll = new JScrollPane(rightAnswer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		rightAnswerScroll.getVerticalScrollBar().setUnitIncrement(16);
		rightAnswerScroll.getHorizontalScrollBar().setUnitIncrement(16);
		rightAnswerScroll.setPreferredSize(new Dimension((int) rightAnswerScroll.getPreferredSize().getWidth(),
				(int) rightAnswerScroll.getPreferredSize().getHeight() * 2));

		
		// Choicies list
		DefaultListModel<String> allAnswersModel = new DefaultListModel<String>();
		JList<String> allAnswers = new JList<String>(allAnswersModel);
		JScrollPane allAnswersScroll = new JScrollPane(allAnswers, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		allAnswersScroll.getVerticalScrollBar().setUnitIncrement(16);
		allAnswersScroll.getHorizontalScrollBar().setUnitIncrement(16);
		allAnswersScroll.setPreferredSize(new Dimension((int) allAnswersScroll.getPreferredSize().getWidth(),
				(int) allAnswersScroll.getPreferredSize().getHeight() * 2));

		
		// enter question text field which adds the question when the enter key is typed
		JTextField enterQuestion = new JTextField();
		enterQuestion.setFont(font);
		JScrollBar scrollQuestion = new JScrollBar(JScrollBar.HORIZONTAL);
		scrollQuestion.setModel(enterQuestion.getHorizontalVisibility());
		enterQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String S = enterQuestion.getText();
				enterQuestion.setText("");
				// something is in the text field
				if (S.equals("")) {
					return;
				}
				questionsModel.addElement(S);
			}
		});

		
		// Enter answer for the specific question that has been enter, this answers is also
		// automatically added to the list of all the possible choices if it is not already
		// there
		JTextField enterRightAnswer = new JTextField();
		enterRightAnswer.setFont(font);
		JScrollBar scrollRightAnswer = new JScrollBar(JScrollBar.HORIZONTAL);
		scrollRightAnswer.setModel(enterRightAnswer.getHorizontalVisibility());
		enterRightAnswer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String S = enterRightAnswer.getText();
				enterRightAnswer.setText("");
				// something is in the text field
				if (S.equals("")) {
					return;
				}
				// adds to all answers if it is not already there
				if (!allAnswersModel.contains(S)) {
					allAnswersModel.addElement(S);
				
				}
				rightAnswerModel.addElement(S);
			}
		});

		
		// Enter choices : this textfield adds choices to the question
		JTextField enterAllAnswers = new JTextField();
		enterAllAnswers.setFont(font);
		JScrollBar scrollAllAnswers = new JScrollBar(JScrollBar.HORIZONTAL);
		scrollAllAnswers.setModel(enterAllAnswers.getHorizontalVisibility());
		enterAllAnswers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String S = enterAllAnswers.getText();
				enterAllAnswers.setText("");
				if (S.equals("")) {
					return;
				}
				allAnswersModel.addElement(S);
			}
		});

		// Remove last question int the list
		JButton removeLastQuestion = new JButton("Remove");
		removeLastQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!questionsModel.isEmpty()) {
					questionsModel.removeElementAt(questionsModel.size() - 1);
				}
			}
		});
		removeLastQuestion.setAlignmentX(JButton.CENTER_ALIGNMENT);

		
		// Remove last answer in the list
		JButton removeLastAnswer = new JButton("Remove");
		removeLastAnswer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!rightAnswerModel.isEmpty()) {
					allAnswersModel.removeElement(rightAnswerModel.getElementAt(rightAnswerModel.size() - 1));
					rightAnswerModel.removeElementAt(rightAnswerModel.size() - 1);
				}
			}
		});
		removeLastAnswer.setAlignmentX(JButton.CENTER_ALIGNMENT);

		// Remove last option in the list
		JButton removeLastChoice = new JButton("Remove");
		removeLastChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!allAnswersModel.isEmpty()) {
					allAnswersModel.removeElementAt(allAnswersModel.size() - 1);
				}
			}
		});
		removeLastChoice.setAlignmentX(JButton.CENTER_ALIGNMENT);

		// Question column
		JPanel questionsPanel = new JPanel();
		questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
		questionsPanel.add(questionsLabel);
		questionsPanel.add(questionsScroll);
		questionsPanel.add(Box.createVerticalStrut(20));
		questionsPanel.add(enterQuestion);
		questionsPanel.add(scrollQuestion);
		questionsPanel.add(removeLastQuestion);

		// Answer column
		JPanel rightAnswerPanel = new JPanel();
		rightAnswerPanel.setLayout(new BoxLayout(rightAnswerPanel, BoxLayout.Y_AXIS));
		rightAnswerPanel.add(rightAnswerLabel);
		rightAnswerPanel.add(rightAnswerScroll);
		rightAnswerPanel.add(Box.createVerticalStrut(20));
		rightAnswerPanel.add(enterRightAnswer);
		rightAnswerPanel.add(scrollRightAnswer);
		rightAnswerPanel.add(removeLastAnswer);

		// Choices column
		JPanel allAnswersPanel = new JPanel();
		allAnswersPanel.setLayout(new BoxLayout(allAnswersPanel, BoxLayout.Y_AXIS));
		allAnswersPanel.add(allAnswersLabel);
		allAnswersPanel.add(allAnswersScroll);
		allAnswersPanel.add(Box.createVerticalStrut(20));
		allAnswersPanel.add(enterAllAnswers);
		allAnswersPanel.add(scrollAllAnswers);
		allAnswersPanel.add(removeLastChoice);

		// Add 3 columns
		rightPanel.add(Box.createHorizontalStrut(50));
		rightPanel.add(questionsPanel);
		rightPanel.add(Box.createHorizontalStrut(50));
		rightPanel.add(rightAnswerPanel);
		rightPanel.add(Box.createHorizontalStrut(50));
		rightPanel.add(allAnswersPanel);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));

		// Add left and right column together
		notFormula.add(leftPanel);
		notFormula.add(rightPanel);
		notFormula.setLayout(new BoxLayout(notFormula, BoxLayout.X_AXIS));

		// Adding all subjects
		subjectModel.addElement("Choose a subject");
		unitModel.addElement("Choose a unit");
		for (int i = 0; i < subjects.size(); i++) {
			subjectModel.addElement(
					subjects.get(i).getName() + " " + subjects.get(i).getGrade() + " " + subjects.get(i).getLevel());
		}

		// Subject combobox
		subject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Gets subject name
				String chosenSubject = (String) subject.getSelectedItem();

				// Removes all subjects
				int size = unit.getItemCount();
				for (int i = 1; i < size; i++) {
					unit.removeItemAt(1);
				}

				// Update units in subject
				if (!chosenSubject.equals("Choose a subject")) {

					s = subjects.get(subject.getSelectedIndex() - 1);
					// TODO Priority queue instead of linkedlist to keep the units in order
					SimpleLinkedList<Unit> units = s.getUnits();

					// Adds units
					for (int i = 0; i < units.size(); i++) {
						unitModel.addElement(units.get(i).getNum() + ". " + units.get(i).getName());
					}

					// Subject has not been chosen
				} else {
					s = null;
				}
			}
		});

		// Unit combobox
		unit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Name of unit
				String chosenUnit = (String) unit.getSelectedItem();

				// Updates questions in unit
				if (!chosenUnit.equals("Choose a unit")) {
					
					// Units in subject
					SimpleLinkedList<Unit> units = s.getUnits();
					u = units.get(unit.getSelectedIndex() - 1);
					SimpleLinkedList<Question> questions = u.getQuestions();
					listModel.removeAllElements();
					// Add questions
					for (int i = 0; i < questions.size(); i++) {
						listModel.addElement(questions.get(i).getProblemStatement());
					}
				}
			}
		});

		// Formula combobox choices
		formula.addItem("Choose a formula");
		formula.addItem("Create new formula");
		formula.addItem("Use previous formula");
		formula.addItem("No formula (Enter answers myself)");
		formula.setBackground(lightBlue);

		// Formula combo box action listener
		formula.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// only allowed when adding or editing(should not be shown in any other function)
				if (!addQ && !editQ) {
					return;
				}

				// choice name
				String chosenFormula = (String) formula.getSelectedItem();

				// enter answers
				if (chosenFormula.equals("No formula (Enter answers myself)")) {
					// q.setFormula(null);

					// currentFormulaDisplay.setIcon(new ImageIcon());
					// Update frame
					if (!noFormula) {
						noFormula = true;
						displayPane.remove(hasFormula);
						displayPane.add(notFormula);
					}

					// enter formula to autogenerate answers
				} else {
					// Update frame
					if (noFormula) {
						noFormula = false;
						displayPane.remove(notFormula);
						displayPane.add(hasFormula);
					}

					// Nothing has been chosen
					if (chosenFormula.equals("Choose a formula")) {
						return;
					}

					q = new Question("", new SimpleLinkedList<Symbol>());

					// creating a new formula
					if (chosenFormula.equals("Create new formula")) {
						newFormula = true;
						setVisible(false);
						// Create formula ui
						createFormula(symbols, currentFormulaDisplay);

						// using a previous formula
					} else if (chosenFormula.equals("Use previous formula")) {
						setVisible(false);
						newFormula = false;
						// use previous formula ui
						previousFormula(formulas, currentFormulaDisplay);
					}
				}
				// Update frame
				quiz.revalidate();
			}
		});

		// Add Button action listener
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Already adding
				if (addQ) {
					return;
				}
				
				addQ = true;
				removeQ = false;
				editQ = false;
				editingQ = false;

				// Update pane
				displayPane.removeAll();
				displayPane.add(formula);
				 if (noFormula) {
					 displayPane.add(notFormula);
				 } else {
					 displayPane.add(hasFormula);
				 }

				revalidate();

				// Update sizes
				midPane.setPreferredSize(new Dimension((int) midPane.getMinimumSize().getWidth(),
						(int) exit.getLocation().getY() + 50 + offset));
				contentPane.setPreferredSize(new Dimension((int) contentPane.getPreferredSize().getWidth(),
						(int) midPane.getPreferredSize().getHeight()));
			}
		});

		// Remove button action listener
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeQ = true;
				addQ = false;
				editQ = false;
				editingQ = false;

				// Displays list with all questions in the unit selected supporting multi
				// selection
				displayPane.removeAll();
				displayPane.setMinimumSize(
						new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 200, 300));
				
				// allows multiple questions to be deleted at the same time
				list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scroll.getVerticalScrollBar().setUnitIncrement(16);
				scroll.getHorizontalScrollBar().setUnitIncrement(16);
				scroll.setPreferredSize(displayPane.getPreferredSize());
				displayPane.add(scroll);
				revalidate();

				// Update frame size
				midPane.setPreferredSize(new Dimension((int) midPane.getMinimumSize().getWidth(),
						(int) exit.getLocation().getY() + 50 + offset));
				contentPane.setPreferredSize(new Dimension((int) contentPane.getPreferredSize().getWidth(),
						(int) midPane.getPreferredSize().getHeight()));

			}
		});

		// Edit button action listener
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editQ = true;
				addQ = false;
				removeQ = false;
				editingQ = false;
				
				// clear frame
				displayPane.removeAll();
				displayPane.setMinimumSize(
						new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 200, 300));

				// Displays list with all questions in the unit but can only edit one at a time
				// so that you can choose which question you would like to edit, cant edit multiple
				// questions at the same time
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				subject.setVisible(true);
				unit.setVisible(true);
				JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scroll.getVerticalScrollBar().setUnitIncrement(16);
				scroll.getHorizontalScrollBar().setUnitIncrement(16);
				displayPane.add(scroll);
				revalidate();

				// update frame size
				midPane.setPreferredSize(new Dimension((int) midPane.getMinimumSize().getWidth(),
						(int) exit.getLocation().getY() + 50 + offset));
				contentPane.setPreferredSize(new Dimension((int) contentPane.getPreferredSize().getWidth(),
						(int) midPane.getPreferredSize().getHeight()));
			}
		});

		// Confirm Button
		// Button used to confirm choices for add, remove and edit
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Add remove or edit has not been selected
				if (!addQ && !removeQ && !editQ && !editingQ) {
					JOptionPane.showMessageDialog(null, "Add, remove or edit has not been selected");
					return;
				}

				// No subject chosen
				if (s == null) {
					JOptionPane.showMessageDialog(null, "A valid subject has not been selected");
					return;
				}

				// No unit chosen
				if (u == null) {
					JOptionPane.showMessageDialog(null, "A valid unit has not been selected");
					return;
				}

				// No question chosen
				if (q == null && !noFormula && !removeQ && !editQ && !editingQ) {
					JOptionPane.showMessageDialog(null, "A valid question has not been selected");
					return;
				}

				// Removes selected question
				if (removeQ) {
					SimpleLinkedList<Question> questions = u.getQuestions();
					int[] indicies = list.getSelectedIndices();
					// Need the array because the model updates indexes after every removal
					Object[] A = listModel.toArray();
					SimpleLinkedList<Question> remove = new SimpleLinkedList<Question>();
					// deletes questions that have been selected
					for (int i = 0; i < indicies.length; i++) {
						// d.deleteQuestion(s,u,questions.get(i);
						listModel.removeElement(A[indicies[i]]);
						remove.add(questions.get(indicies[i]));
					}
					for (int i = 0; i < remove.size(); i++) {
						u.removeQuestion(remove.get(i));
					}
					list.clearSelection();

					// Adds a question
				} else if (addQ) {
					if (noFormula) {
						// there must be an answer for every question
						if (questionsModel.size() != rightAnswerModel.size()) {
							JOptionPane.showMessageDialog(null, "Questions must match answers");
							return;
						}
						// get problem statement
						String problemStatement = enterQ.getText();
						if (problemStatement.equals("")) {
							JOptionPane.showMessageDialog(null, "Please enter a problem statement");
							return;
						}
						// puts everything in the 3 lists into 3 simple linked lists 
						SimpleLinkedList<String> specificQuestions = new SimpleLinkedList<String>();
						SimpleLinkedList<String> specificAnswers = new SimpleLinkedList<String>();
						SimpleLinkedList<String> possibleAnswers = new SimpleLinkedList<String>();
						for (int i = 0; i < questionsModel.size(); i++) {
							specificQuestions.add(questionsModel.getElementAt(i));
						}
						for (int i = 0; i < rightAnswerModel.size(); i++) {
							specificAnswers.add(rightAnswerModel.getElementAt(i));
						}
						for (int i = 0; i < allAnswersModel.size(); i++) {
							possibleAnswers.add(allAnswersModel.getElementAt(i));
						}
						// clears the list, image, and problem
						enterQ.setText("");
						enterQuestion.setText("");
						questionsModel.removeAllElements();
						enterRightAnswer.setText("");
						rightAnswerModel.removeAllElements();
						enterAllAnswers.setText("");
						allAnswersModel.removeAllElements();
						if (image == null) {
							q = new Question(problemStatement, specificQuestions, specificAnswers, possibleAnswers, null);
						} else {
							q = new Question(problemStatement, specificQuestions, specificAnswers, possibleAnswers,
								Scalr.resize(image,400));
						}
						image = null;
						listModel.addElement(problemStatement);
					} else {
						if (enter.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Enter a problem statement");
							return;
						}
						q.setProblemStatement(enter.getText());

						// no formula 
						if (q.getFormula() == null || q.getFormula().size() == 0) {
							JOptionPane.showMessageDialog(null, "Please enter a formula or select no formula");
							return;
						}
						// if it is a new formula, the formula list is updated
						if (newFormula) {
				
							formulas.add(q.getFormula());
							newFormula = false;
						}
						
						// clears everything
						currentFormulaDisplay.setIcon(new ImageIcon());
						enter.setText("");
						listModel.addElement(q.getProblemStatement());
					}
					// Adds to list of questions and jlist which contains the questions in the unit
					
					u.addQuestion(q);
					q = null;
					
					
					// Editing questions
				} else if (editQ) {
					// Same as add interface but formula and problem statement are already filled in
					editQ = false;
					editingQ = true;
					// question that is being edited
					editQuestion = u.getQuestions().get((list.getSelectedIndex()));
					displayPane.removeAll();
					displayPane.add(formula);
					// no formula
					if (editQuestion == null) {
						return;
					}
					if (editQuestion.isPreset()) {
						displayPane.add(notFormula);
						image = editQuestion.getImage();
						SimpleLinkedList<String> questionsList = editQuestion.getSpecificQuestions();
						SimpleLinkedList<String> answersList = editQuestion.getSpecificAnswers();
						SimpleLinkedList<String> choicesList = editQuestion.getPossibleAnswers();
						enterQ.setText(editQuestion.getProblemStatement());
						for (int i = 0; i < questionsList.size(); i++) {
							questionsModel.addElement(questionsList.get(i));
						}
						for (int i = 0; i < answersList.size(); i++) {
							rightAnswerModel.addElement(answersList.get(i));
						}
						for (int i = 0; i < choicesList.size(); i++) {
							allAnswersModel.addElement(choicesList.get(i));
						}
						// No formula
						formulaModel.setSelectedItem("No formula (Enter answers myself)");
					// has a formula
					} else {
						displayPane.add(hasFormula);
						// update formula and problem statement
						String editProblemStatement = editQuestion.getProblemStatement();
						SimpleLinkedList<Symbol> list = editQuestion.getFormula();

						SimpleLinkedList<Symbol> editFormula = new SimpleLinkedList<Symbol>();
						for (int i = 0; i < list.size(); i++) {
							editFormula.add(list.get(i));
						}
						q = new Question(editProblemStatement, editFormula);
						currentFormulaDisplay.setIcon(new ImageIcon(joinBufferedImages(editFormula)));
						enter.setText(editProblemStatement);
					}
					// Formatting
					revalidate();
					
					// update frame size
					midPane.setPreferredSize(new Dimension((int) midPane.getMinimumSize().getWidth(),
							(int) exit.getLocation().getY() + 50 + offset));
					contentPane.setPreferredSize(new Dimension((int) contentPane.getPreferredSize().getWidth(),
							(int) midPane.getPreferredSize().getHeight()));

			
				} else {
					
					// there is no formula
					if (noFormula) {
						
						// answer for every question
						if (questionsModel.size() != rightAnswerModel.size()) {
							JOptionPane.showMessageDialog(null, "Questions must match answers");
							return;
						}
						
						// get problem statement
						String problemStatement = enterQ.getText();
						if (problemStatement.equals("")) {
							JOptionPane.showMessageDialog(null, "Enter a valid problem statement");
						}
						// put elements in list into simple linked lists
						SimpleLinkedList<String> specificQuestions = new SimpleLinkedList<String>();
						SimpleLinkedList<String> specificAnswers = new SimpleLinkedList<String>();
						SimpleLinkedList<String> possibleAnswers = new SimpleLinkedList<String>();
						for (int i = 0; i < questionsModel.size(); i++) {
							specificQuestions.add(questionsModel.getElementAt(i));
						}
						for (int i = 0; i < rightAnswerModel.size(); i++) {
							specificAnswers.add(rightAnswerModel.getElementAt(i));
						}
						for (int i = 0; i < allAnswersModel.size(); i++) {
							possibleAnswers.add(allAnswersModel.getElementAt(i));
						}

						// Clear everything
						enterQ.setText("");
						enterQuestion.setText("");
						questionsModel.removeAllElements();
						enterRightAnswer.setText("");
						rightAnswerModel.removeAllElements();
						enterAllAnswers.setText("");
						allAnswersModel.removeAllElements();
						if (image == null) {
							q = new Question(problemStatement, specificQuestions, specificAnswers, possibleAnswers, null);
						} else {
							q = new Question(problemStatement, specificQuestions, specificAnswers, possibleAnswers,
								Scalr.resize(image,400));
						}
						image = null;
						
						// remove original question and adds the new one
						u.removeQuestion(editQuestion);
						u.addQuestion(q);
						listModel.removeElement(editQuestion.getProblemStatement());
						listModel.addElement(problemStatement);
					} else {
						// there must be a formula 
						if (q.getFormula() == null || q.getFormula().size() == 0) {
							JOptionPane.showMessageDialog(null, "Please enter a formula or select no formula.");
							return;
						}
						if (enter.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Please enter a problem statement");
							return;
						}
						// removes the edited question and adds a new one to the unit list and the jlist of questions
						listModel.add(listModel.indexOf(editQuestion.getProblemStatement()), enter.getText());
						listModel.removeElement(editQuestion.getProblemStatement());
						editQuestion.setProblemStatement(enter.getText());
						editQuestion.setFormula(q.getFormula());
						if (newFormula) {
							formulas.add(q.getFormula());
							newFormula = false;
						}
						currentFormulaDisplay.setIcon(new ImageIcon());
						enter.setText("");
					}
					// reset q
					q = null;
				}

			}
		});

		
		// button to clear everything that was entered
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enter.setText("");
				enterQ.setText("");
				currentFormulaDisplay.setIcon(new ImageIcon());
				enterQuestion.setText("");
				questionsModel.removeAllElements();
				enterRightAnswer.setText("");
				rightAnswerModel.removeAllElements();
				enterAllAnswers.setText("");
				allAnswersModel.removeAllElements();
			}
		});
		
		
		// Add subject button
		addSubject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				s = null;
				subject.setSelectedIndex(0);
				// makes the main frame invisible and runs the add subject method
				setVisible(false);
				addSubject(null, subjects);
			}
		});

		// Add unit button
		addUnit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Subject must be selected
				if (s == null) {
					JOptionPane.showMessageDialog(null, "A valid subject has not been selected");
					return;
				}
				unit.setSelectedIndex(0);
				u = null;
				// makes main frame invisible and runs the add unit method
				setVisible(false);
				SimpleLinkedList<Unit> unitList = s.getUnits();
				addUnit(null, unitList, null, null);
			}
		});

		// Remove subject button
		removeSubject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// subject has to be selected or nothing will be removed
				if (s == null) {
					JOptionPane.showMessageDialog(null, "A valid subject has not been selected");
					return;
				}
				// remove s from simplelinkedlist from the database
				subjects.remove(s);

				subjectModel.removeElement(subjectModel.getSelectedItem());

				subjectModel.setSelectedItem("Choose a subject");

			
				
				// s  is null since it has been removed
				s = null;
			}
		});

		// remove unit button
		removeUnit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Subject has not been selected
				if (s == null) {
					JOptionPane.showMessageDialog(null, "A valid subject has not been selected");
					return;
					// unit has not been selected
				} else if (u == null) {
					JOptionPane.showMessageDialog(null, "A valid unit has not been selected");
					return;
				}
				// gets units in subject
				SimpleLinkedList<Unit> units = s.getUnits();
				
				// removes u from subject and the unit list
				units.remove(u);

				unitModel.removeElement(unitModel.getSelectedItem());
				u = null;

				unitModel.setSelectedItem("Choose a unit");
			}
		});
		
		editSubject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Subject must be selected
				if (s == null) {
					JOptionPane.showMessageDialog(null, "A valid subject has not been selected");
					return;
				}
				
				// makes main frame invisible and runs the add unit method
				setVisible(false);
				addSubject(s, subjects);
			}
		});
		
		editUnit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Subject must be selected
				if (s == null) {
					JOptionPane.showMessageDialog(null, "A valid subject has not been selected");
					return;
				}
				if (u == null) {
					JOptionPane.showMessageDialog(null, "A valid unit has not been selected");
				}
				setVisible(false);
				SimpleLinkedList<Unit> unitList = s.getUnits();
				addUnit(u, unitList, null, null);
			}
		});
		// disposes jframe
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				quiz.dispose();
			}
		});

		// logo
		midPane.add(logo);

		// Panel to store main buttons
		JPanel buttons = new JPanel();
		add.setAlignmentY(JButton.CENTER_ALIGNMENT);
		remove.setAlignmentY(JButton.CENTER_ALIGNMENT);
		edit.setAlignmentX(JButton.CENTER_ALIGNMENT);
		buttons.add(add);
		buttons.add(Box.createHorizontalStrut(100));
		buttons.add(remove);
		buttons.add(Box.createHorizontalStrut(100));
		buttons.add(edit);
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.setBackground(indigo);
		midPane.add(buttons);

		// Difference in height between components in the frame (can be set as a constant)
		offset = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()
				- (int) displayPane.getPreferredSize().getHeight() - (int) buttons.getPreferredSize().getHeight()
				- (int) subject.getMinimumSize().getHeight() - (int) unit.getMinimumSize().getHeight()
				- (int) confirm.getPreferredSize().getHeight() - (int) exit.getPreferredSize().getHeight()
				- (int) clear.getPreferredSize().getHeight();
		offset /= 7;
		offset += 50;
		midPane.add(Box.createVerticalStrut(offset));

		// Panel to store subject related components
		JPanel subjectPane = new JPanel();
		subjectPane.add(subject);
		subjectPane.add(Box.createHorizontalStrut(50));
		subjectPane.add(addSubject);
		subjectPane.add(Box.createHorizontalStrut(50));
		subjectPane.add(removeSubject);
		subjectPane.add(Box.createHorizontalStrut(50));
		subjectPane.add(editSubject);
		subjectPane.setLayout(new BoxLayout(subjectPane, BoxLayout.X_AXIS));
		subjectPane.setPreferredSize(
				new Dimension((int) midPane.getMaximumSize().getWidth(), (int) subject.getMinimumSize().getHeight()));
		subjectPane.setBackground(indigo);
		subjectPane.setMaximumSize(subjectPane.getPreferredSize());
		midPane.add(subjectPane);

		midPane.add(Box.createVerticalStrut(offset));

		// Panel to store unit related components
		JPanel unitPane = new JPanel();
		unitPane.add(unit);
		unitPane.add(Box.createHorizontalStrut(100));
		unitPane.add(addUnit);
		unitPane.add(Box.createHorizontalStrut(100));
		unitPane.add(removeUnit);
		unitPane.add(Box.createHorizontalStrut(100));
		unitPane.add(editUnit);
		unitPane.setLayout(new BoxLayout(unitPane, BoxLayout.X_AXIS));
		unitPane.setPreferredSize(
				new Dimension((int) midPane.getMinimumSize().getWidth(), (int) unit.getMinimumSize().getHeight()));
		unitPane.setBackground(indigo);
		unitPane.setMaximumSize(unitPane.getPreferredSize());
		midPane.add(unitPane);

		// Adding everything to the main panel
		displayPane.setLayout(new BoxLayout(displayPane, BoxLayout.Y_AXIS));
		midPane.add(Box.createVerticalStrut(offset));
		midPane.add(displayPane);
		midPane.add(Box.createVerticalStrut(offset));
		midPane.add(confirm);
		midPane.add(Box.createVerticalStrut(offset));
		midPane.add(clear);
		midPane.add(Box.createVerticalStrut(offset));
		midPane.add(exit);
		midPane.add(Box.createVerticalStrut(offset));
		midPane.setBackground(indigo);
		midPane.setLayout(new BoxLayout(midPane, BoxLayout.Y_AXIS));

		// main jpanel
		contentPane.add(Box.createHorizontalStrut(100));

		contentPane.add(midPane);
		contentPane.add(Box.createHorizontalStrut(100));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		contentPane.setBackground(indigo);
		
		// make content pane scrollable
		JScrollPane scrollPane = new JScrollPane(contentPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getVerticalScrollBar().setBackground(lightBlue);
		scrollPane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		this.setContentPane(scrollPane);
		this.setVisible(true);
		
		// adjusting the sizes
		midPane.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 200,
				(int) exit.getLocation().getY() + 50 + offset));
		midPane.setMaximumSize(midPane.getPreferredSize());
		midPane.setMinimumSize(midPane.getPreferredSize());
		midPane.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		contentPane.setPreferredSize(midPane.getPreferredSize());
		contentPane.setSize(contentPane.getPreferredSize());
		revalidate();

	}

	/**
	 * method to run to ui for adding a subject
	 * @param subject the subject to be edited
	 * @param subjects the subject simple linked list received from the database
	 */
	private void addSubject(Subject sub, SimpleLinkedList<Subject> subjects) {
		
		// frame setup
		JFrame addSubjectFrame = new JFrame("Add Subject");
		addSubjectFrame.setSize(550, 700);
		addSubjectFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addSubjectFrame.setUndecorated(true);
		
		// window listener to set the main frame visible when closed
		addSubjectFrame.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
				quiz.setVisible(true);
			}

			@Override
			public void windowClosing(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}

		});

		// Components
		JPanel contentPanel = new JPanel();
		JLabel enterName = new JLabel("Enter the  name of the subject");
		JLabel enterGrade = new JLabel("Enter the grade of the subject");
		JLabel enterLevel = new JLabel("Enter the level of the subject");
		JLabel unitLabel = new JLabel("List of Units");
		JTextArea name = new JTextArea();
		JTextArea grade = new JTextArea();
		JTextArea level = new JTextArea();
		JButton addUnit = new JButton("Add a unit");
		JButton removeUnit = new JButton("Remove selected units");
		JLabel logo = new JLabel(new ImageIcon("logo.png"));
		// Scrollable JList
		DefaultListModel<String> unitModel = new DefaultListModel<String>();
		JList<String> units = new JList<String>(unitModel);
		units.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scroll = new JScrollPane(units, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		/// Buttons
		JButton confirm = new JButton("Confirm");
		JButton cancel = new JButton("Cancel");
		
		
		// Scrollable panel
		JScrollPane scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		// Formatting
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);

		logo.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		enterName.setForeground(orange);
		enterName.setFont(font);
		enterName.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		enterGrade.setForeground(orange);
		enterGrade.setFont(font);
		enterGrade.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		enterLevel.setForeground(orange);
		enterLevel.setFont(font);
		enterLevel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		units.setForeground(orange);
		units.setFont(font);
		units.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		name.setBackground(lightBlue);
		name.setFont(small);
		name.setLineWrap(true);
		name.setWrapStyleWord(true);

		grade.setBackground(lightBlue);
		grade.setFont(small);
		grade.setLineWrap(true);
		grade.setWrapStyleWord(true);

		level.setBackground(lightBlue);
		level.setFont(small);
		level.setLineWrap(true);
		level.setWrapStyleWord(true);

		addUnit.setBackground(lightBlue);
		addUnit.setFont(font);
		addUnit.setAlignmentX(JButton.CENTER_ALIGNMENT);

		removeUnit.setBackground(lightBlue);
		removeUnit.setFont(font);
		removeUnit.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		confirm.setBackground(lightBlue);
		confirm.setFont(font);
		confirm.setAlignmentX(JButton.CENTER_ALIGNMENT);

		cancel.setBackground(lightBlue);
		cancel.setFont(font);
		cancel.setAlignmentX(JButton.CENTER_ALIGNMENT);

		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		// List of units
		unitList = new SimpleLinkedList<Unit>();
		units.setBackground(lightBlue);

		
		
		
		// Action Listeners
		// Add unit button that sets this frame invisible when pressed
		addUnit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addSubjectFrame.setVisible(false);
				addUnit(null, unitList, unitModel, addSubjectFrame);
			}
		});

		// remove unit
		removeUnit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] delete = units.getSelectedIndices();
				Object[] A = unitModel.toArray();
				SimpleLinkedList<Unit> remove = new SimpleLinkedList<Unit>();
				for (int i = 0; i < delete.length; i++) {
					unitModel.removeElement(A[delete[i]]);
					remove.add(unitList.get(delete[i]));
				}
				for (int i = 0; i < remove.size(); i++) {
					unitList.remove(remove.get(i));
				}
			}
		});
		// Confirm button
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure everything is filled in 
				if (name.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "A valid name has not been selected");
					return;
				} else if (grade.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "A valid grade has not been selected");
					return;
				} else if (level.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "A valid level has not been selected");
					return;
				}
				try {
					// Add subject
					Subject tempSubject = new Subject(name.getText(), Integer.parseInt(grade.getText()), level.getText());
					if (sub != null) {
						if (subjectModel.getIndexOf(name.getText() + " " + grade.getText() + " " + level.getText()) >= 0) {
							subjectModel.removeElement(name.getText() + " " + grade.getText() + " " + level.getText());
						}
						subjects.remove(sub);
					} else {
						if (subjectModel.getIndexOf(name.getText() + " " + grade.getText() + " " + level.getText()) >= 0) {
							JOptionPane.showMessageDialog(null, "This subject already exists!");
							return;
						}
					}
					for (int i = 0; i < unitList.size(); i++) {
						tempSubject.getUnits().add(unitList.get(i));
					}
					subjects.add(tempSubject);
					subjectModel.addElement(name.getText() + " " + grade.getText() + " " + level.getText());
					subjectModel.setSelectedItem(name.getText() + " " + grade.getText() + " " + level.getText());
					unit.setSelectedIndex(0);
					addSubjectFrame.dispose();
					// the grade must be an integer
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "A valid grade has not been selected");
				}
			}
		});

		// cancel button that exits the frame without doing anything
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addSubjectFrame.dispose();
			}
		});
		
		// add subject if it is editing
		if (sub != null) {
			name.setText(sub.getName());
			level.setText(sub.getLevel());
			grade.setText(Integer.toString(sub.getGrade()));
			SimpleLinkedList<Unit> tmp = sub.getUnits();
			for (int i = 0; i < tmp.size(); i++) {
				unitList.add(tmp.get(i));
				unitModel.addElement(unitList.get(i).getNum() + ". " + unitList.get(i).getName());
			}
		}
		
		// Adding onto the content pane
		contentPanel.add(logo);
		contentPanel.add(enterName);
		contentPanel.add(name);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.add(enterGrade);
		contentPanel.add(grade);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.add(enterLevel);
		contentPanel.add(level);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.add(unitLabel);
		contentPanel.add(scroll);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.add(addUnit);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.add(removeUnit);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.add(confirm);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.add(cancel);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.setBackground(indigo);
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		// this.setPreferredSize(preferredSize);

		
		addSubjectFrame.setContentPane(scrollPane);
		addSubjectFrame.setVisible(true);

	}

	/**
	 * method to run the ui for adding a unit
	 * @param unit the unit that is being edited
	 * @param unitList the simple linked list of units to add to
	 * @param model model of the jlist to add to if there is one
	 * @param addSubjectFrame frame to update when a unit is added
	 */
	private void addUnit(Unit un, SimpleLinkedList<Unit> unitList, DefaultListModel<String> model, JFrame addSubjectFrame) {

		// Main frame
		JFrame addUnitFrame = new JFrame("Add Unit");
		addUnitFrame.setSize(550, 600);
		addUnitFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addUnitFrame.setUndecorated(true);
		// window listener to set the subject creating frame or the main frame to visible depending on which frame the method was called from
		addUnitFrame.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
				if (addSubjectFrame == null) {
					quiz.setVisible(true);
				} else {
					addSubjectFrame.setVisible(true);
				}
			}

			@Override
			public void windowClosing(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}

		});
		// Components
		JPanel contentPanel = new JPanel();
		JLabel enterName = new JLabel("Enter unit name");
		JLabel enterNum = new JLabel("Enter unit number");
		JTextArea name = new JTextArea();
		JTextArea num = new JTextArea();
		JButton confirm = new JButton("Confirm");
		JButton cancel = new JButton("Cancel");
		JLabel logo = new JLabel(new ImageIcon("logo.png"));
		
		// makes whole content panel scrollable
		JScrollPane scroll = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// Formatting
		logo.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		enterName.setForeground(orange);
		enterName.setFont(font);
		enterName.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		enterNum.setForeground(orange);
		enterNum.setFont(font);
		enterNum.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		name.setBackground(lightBlue);
		name.setFont(small);
		name.setLineWrap(true);
		name.setWrapStyleWord(true);

		num.setBackground(lightBlue);
		num.setFont(small);
		num.setLineWrap(true);
		num.setWrapStyleWord(true);

		confirm.setBackground(lightBlue);
		confirm.setFont(font);
		confirm.setAlignmentX(JButton.CENTER_ALIGNMENT);

		cancel.setBackground(lightBlue);
		cancel.setFont(font);
		cancel.setAlignmentX(JButton.CENTER_ALIGNMENT);

		
		// action listener to confirm the choice
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (name.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "A valid name has not been selected");
					return;
				} else if (num.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "A valid number has not been selected");
					return;
				}
				try {
					// add unit to list
					Unit tempUnit = new Unit(name.getText(), Integer.parseInt(num.getText()));
					String nameString = name.getText();
					unitList.add(tempUnit);
					if (un != null) {
						unitList.remove(un);
						if (model != null) {
							model.removeElement(un.getNum() + ". " + un.getName());
						}
						if (s != null) {
							unitModel.removeElement(un.getNum() + ". " + un.getName());
						}
					}
					// adds to unit to model if there is one
					if (model != null) {
						if (model.contains(num.getText() + ". " + nameString)) {
							JOptionPane.showMessageDialog(null, "List already contains this unit");
							model.removeElement(un.getNum() + ". " + unit.getName());
							return;
						}
						model.addElement(num.getText() + ". " + nameString);
					
					
					// adds unit to subject if the subject is not a new one
					} else {
						if (unitModel.getIndexOf(num.getText() + ". " + nameString) > -1) {
							JOptionPane.showMessageDialog(null, "Subject contains this unit.");
							unitList.remove(tempUnit);
							return;
						}
						unitModel.addElement(num.getText() + ". " + nameString);
						unitModel.setSelectedItem(num.getText() + ". " + nameString);
					}
					
					addUnitFrame.dispose();
					// unit must be an integer
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "A valid number has not been selected");
				}
			}
		});

		// Close window
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addUnitFrame.dispose();
			}
		});

		if (un != null) {
			name.setText(un.getName());
			num.setText(Integer.toString(un.getNum()));
		}
		
		// Adding components onto JFrame
		contentPanel.add(logo);
		contentPanel.add(enterName);
		contentPanel.add(name);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.add(enterNum);
		contentPanel.add(num);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.add(confirm);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.add(cancel);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(indigo);
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		addUnitFrame.setContentPane(scroll);
		addUnitFrame.setVisible(true);
	}

	/**
	 * method to choose a previous formula
	 * @param formulas simple linked list of formulas that have previously been used in questions
	 * @param formulaDisplay display label for the chosen formula
	 */
	private void previousFormula(SimpleLinkedList<SimpleLinkedList<Symbol>> formulas, JLabel formulaDisplay) {
		// set up frame
		JFrame previousFormulaFrame = new JFrame("Choose Previous Formula");
		previousFormulaFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		previousFormulaFrame.requestFocus();
		previousFormulaFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		previousFormulaFrame.setUndecorated(true);
		
		// window listener to set the main frame as visible when this frame is closed
		previousFormulaFrame.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
				quiz.setVisible(true);
			}

			@Override
			public void windowClosing(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}

		});

		// Components
		DefaultListModel<BufferedImage> model = new DefaultListModel<BufferedImage>();
		JList<BufferedImage> list = new JList<BufferedImage>(model);
		JButton confirm = new JButton("Confirm formula");
		JButton cancel = new JButton("Cancel");
		JPanel buttons = new JPanel();
		JPanel contentPanel = new JPanel();
		JPanel contentPane = new JPanel();
		JLabel logo = new JLabel(new ImageIcon("logo.png"));
		JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// plap logo
		logo.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		// setting up list
		for (int i = 0; i < formulas.size(); i++) {
			model.addElement(joinBufferedImages(formulas.get(i)));
		}
		list.setCellRenderer(new CustomListRenderer());
		list.setPreferredSize(list.getMinimumSize());

		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);

		
		// Confirm button

		confirm.setFont(font);
		confirm.setAlignmentX(JButton.CENTER_ALIGNMENT);
		confirm.setBackground(lightBlue);
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// gets index
				int index = list.getSelectedIndex();
				
				// makes sure selected index is valid
				if (formulas.get(index) == null) {
					return;
				}
				
				// udates formula
				q.setFormula(formulas.get(index));
				formulaDisplay.setIcon(new ImageIcon(joinBufferedImages(formulas.get(index))));
				quiz.revalidate();
				previousFormulaFrame.dispose();
			}
		});

		// cancel button which just exits the frame
		cancel.setFont(font);
		cancel.setAlignmentX(JButton.CENTER_ALIGNMENT);
		cancel.setBackground(lightBlue);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				previousFormulaFrame.dispose();
			}
		});
		
		
		// finalizes jpanel that has all the buttons
		buttons.add(confirm);
		buttons.add(Box.createHorizontalStrut(offset));
		buttons.add(cancel);
		buttons.setBackground(indigo);
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

		// Adding components onto frame
		contentPanel.add(logo);
		contentPanel.add(scroll);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.add(buttons);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(indigo);

		// Scrollable jlist
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		scrollPane.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 100,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()));

		contentPane.add(Box.createHorizontalStrut(50));
		contentPane.add(scrollPane);
		contentPane.add(Box.createHorizontalStrut(50));
		contentPane.setBackground(indigo);
		previousFormulaFrame.setContentPane(contentPane);
		previousFormulaFrame.setVisible(true);
		revalidate();
	}

	/**
	 * method to create new formulas
	 * @param symbols the list of symbols
	 * @param formulaDisplay the display to update when something is chosen
	 */
	private void createFormula(SimpleLinkedList<Symbol> symbols, JLabel formulaDisplay) {
		
		// set up jframe
		JFrame createFormulaFrame = new JFrame("Create new formula");
		createFormulaFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createFormulaFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		createFormulaFrame.requestFocus();

		
		// window listener to set main frame visible when this one is closed
		createFormulaFrame.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
				quiz.setVisible(true);
			}

			@Override
			public void windowClosing(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}

		});

		// Components
		JButton backspace = new JButton("Del");
		JButton cancel = new JButton("Cancel");
		JButton confirm = new JButton("Confirm");
		JButton ok = new JButton("OK");
		JLabel enteredFormula = new JLabel();
		JPanel button = new JPanel();
		JPanel buttons = new JPanel();
		JPanel constantPane = new JPanel();
		JPanel contentPane = new JPanel();
		JPanel contentPanel = new JPanel();
		JTextArea constant = new JTextArea();
		
		
		// JScrollPanes to make components scrollable 
		JScrollPane scroll = new JScrollPane(enteredFormula, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		

		// Lists
		SimpleLinkedList<Symbol> formula = new SimpleLinkedList<Symbol>();
		SimpleLinkedList<JButton> buttonlist = new SimpleLinkedList<JButton>();

		// Formatting the layout
		int cols = (int) Math.ceil(Math.sqrt(((double)symbols.size() + 2.0)));
		int rows = (int) Math.ceil((double) (symbols.size() + 2) / cols);
		buttons.setLayout(new GridLayout(rows, cols));

		enteredFormula.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		// Adding all symbols
		for (int i = 0; i < symbols.size(); i++) {

			// JButton for all symbols
			BufferedImage image = symbols.get(i).getImage();
			
			// makes image smaller
			Image tmp = image.getScaledInstance(image.getWidth() / 2, image.getHeight() / 2, Image.SCALE_SMOOTH);
			JButton symbol = new JButton(new ImageIcon(tmp));
			symbol.setBackground(lightBlue);
			symbol.addActionListener(new ActionListener() {
				@Override
				// Add to formula
				public void actionPerformed(ActionEvent e) {

					if (formula.size() > 0) {
						String id = symbols.get(buttonlist.indexOf(symbol)).getId();
						try {
							// checks if id is a number
							Integer.parseInt(id);
							
							// if it is it gets the previous symbol and removes it
							Symbol previous = formula.get(formula.size() - 1);
							formula.remove(formula.size() - 1);
							
							// it checks if it is a variable
							if (previous instanceof Variable) {
								// gets previous id
								String prevId = previous.getId();
								
								// used to seperate 2 variables
								String between = "-";
								
								// '-' char is removed if the previous is also a number
								if (prevId.charAt(prevId.length() - 1) >= '0'
										&& prevId.charAt(prevId.length() - 1) <= '9') {
									between = "";
								}
								
								// add new symbol
								formula.add(new Variable(previous.getId() + between + id));
							} else {
								// if it is not then the previous and current number symbol are both
								// added to the formula
								formula.add(previous);
								formula.add(symbols.get(buttonlist.indexOf(symbol)));
							}
						} catch (NumberFormatException ex) {
							
							// adds symbol if the current symbol is not an integer
							formula.add(symbols.get(buttonlist.indexOf(symbol)));
						}
						// this is the first symbol in the formula so it is just added
					} else {
						formula.add(symbols.get(buttonlist.indexOf(symbol)));
					}
					// updates frame and display
					enteredFormula.setIcon(new ImageIcon(joinBufferedImages(formula)));
					createFormulaFrame.revalidate();
				}
			});
			
			// add button
			// button list is the simplelinked list used so that you can determine the symbol
			// that is pressed with the index of the button and the index of the symbol in 
			// the symbol list
			buttonlist.add(symbol);
			// button is the grid to add the button to
			buttons.add(symbol);
		}

		// Delete last symbol
		backspace.setBackground(Color.WHITE);
		backspace.setFont(font);
		backspace.setBackground(lightBlue);
		backspace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// if it has at least one symbol
				if (formula.size() > 0) {
					formula.remove(formula.size() - 1);
				} else {
					return;
				}
				// if it still has at least one symbol after removing
				if (formula.size() > 0) {
					enteredFormula.setIcon(new ImageIcon(joinBufferedImages(formula)));
				} else {
					enteredFormula.setIcon(new ImageIcon());
				}
				createFormulaFrame.revalidate();
			}
		});

		// add backspace button to grid
		buttons.add(backspace);

		
		// place where the user is able to type in their own numbers
		constant.setLineWrap(true);
		constant.setWrapStyleWord(true);
		ok.setFont(font);
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// if it is a double
					double d = Double.parseDouble(constant.getText());
					
					// if it is an integer and is not the first element
					if (formula.size() > 0 && d % 1 == 0) {
						
						// gets prev and removes it from the formula
						Symbol previous = formula.get(formula.size() - 1);
						formula.remove(formula.size() - 1);
						
						// if previous is a variable
						if (previous instanceof Variable) {
							
							String id = previous.getId();
							// used to seperate variables
							String between = "-";
							
							// if prev is a number and this number is connected
							if (id.charAt(id.length() - 1) >= '0' && id.charAt(id.length() - 1) <= '9') {
								between = "";
							}
							formula.add(new Variable(previous.getId() + between + constant.getText()));
						} else {
							// if previous is an operation, it is re-added
							formula.add(previous);
							formula.add(new Symbol(constant.getText()));
						}
					} else {
						// is a doouble or is the first element
						formula.add(new Symbol(constant.getText()));
					}
					
					// update and clear
					enteredFormula.setIcon(new ImageIcon(joinBufferedImages(formula)));
					createFormulaFrame.revalidate();
					constant.setText("");
					
					// number must be entererd
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "A valid number was not entered");
				}
			}
		});
		ok.setBackground(lightBlue);
		
		// puts button and textfield onto jpanel
		constantPane.add(constant);
		constantPane.add(ok);
		constantPane.setBackground(indigo);
		constantPane.setLayout(new BoxLayout(constantPane, BoxLayout.X_AXIS));

		// add constant jpanel
		buttons.add(constantPane);

		
		// confirm button
		confirm.setFont(font);
		confirm.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		confirm.setBackground(lightBlue);
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Nothing has been entered yet
				if (formula.size() == 0) {
					return;
				}
				
				// set formula and update
				q.setFormula(formula);
				formulaDisplay.setIcon(new ImageIcon(joinBufferedImages(formula)));
				quiz.revalidate();
				createFormulaFrame.dispose();
			}
		});
		
		// cancel button that exits the frame
		cancel.setFont(font);
		cancel.setAlignmentX(JButton.CENTER_ALIGNMENT);
		cancel.setBackground(lightBlue);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createFormulaFrame.dispose();
			}
		});
		// add to content pane

		// scrollable
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		scroll.setPreferredSize(new Dimension((int) contentPanel.getPreferredSize().getWidth(),
				(int) enteredFormula.getPreferredSize().getHeight() + 150));
		scroll.setBorder(BorderFactory.createEmptyBorder());
		
		// add to content pane
		// contentPane.add(logo);
		contentPanel.add(buttons);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.add(scroll);
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		
		// add to botton of grid
		button.add(confirm);
		button.add(Box.createHorizontalStrut(50));
		button.add(cancel);
		button.setBackground(indigo);
		contentPanel.add(button);

		
		// update content pane
		contentPanel.add(Box.createVerticalStrut(addingoffset));
		contentPanel.setBackground(indigo);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPane.add(Box.createHorizontalStrut(50));

		// scrollable content pane
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setPreferredSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 100,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
		contentPane.add(scrollPane);

		contentPane.add(Box.createHorizontalStrut(50));
		contentPane.setBackground(indigo);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

		createFormulaFrame.setContentPane(contentPane);
		createFormulaFrame.setVisible(true);
	}

	/**
	 * method to convert a formula into one bufferedimage
	 * @param formula the simplelinkedlist of symbols that are contained in the formula
	 * @return formula as a bufferedimage
	 */
	private BufferedImage joinBufferedImages(SimpleLinkedList<Symbol> formula) {

		// get total width and max height
		int height = 0;
		int width = 0;
		for (int i = 0; i < formula.size(); i++) {
			BufferedImage image = stringToImage(formula.get(i).getId());
			height = Math.max(image.getHeight() / 2, height);
			width += image.getWidth() / 2;
		}
		BufferedImage connectedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		// Draw each picture onto the new picture
		Graphics g = connectedImage.createGraphics();
		int sum = 0;
		for (int i = 0; i < formula.size(); i++) {
			BufferedImage image = stringToImage(formula.get(i).getId());
			g.drawImage(image.getScaledInstance(image.getWidth() / 2, image.getHeight() / 2, Image.SCALE_SMOOTH), sum,
					0, null);
			sum += image.getWidth() / 2;
		}
		g.dispose();
		return connectedImage;
	}

	/**
	 * method to convert symbol id to a buffered image
	 * @param S id of the symbol
	 * @return the buffered image
	 */
	public static BufferedImage stringToImage(String S) {
		// System.out.println(S);
		// is default operation
		try {
			BufferedImage image = ImageIO.read(new File("Symbols/Operations/" + S + ".png"));
			return image;
		} catch (IOException e) {
		}

		// is default variable
		try {
			BufferedImage image = ImageIO.read(new File("Symbols/Variables/" + S + ".png"));
			return image;
		} catch (IOException e) {
		}

		// is double
		try {
			Double.parseDouble(S);
			if (S.endsWith(".0")) {
				S = S.substring(0, S.length() - 2);
			}
			int height = 0;
			int width = 0;
			SimpleLinkedList<BufferedImage> imageList = new SimpleLinkedList<BufferedImage>();
			// gets width and height
			for (int i = 0; i < S.length(); i++) {
				BufferedImage image = null;
				
				// negative
				if (S.charAt(i) == '-') {
					if (i > 0)
						continue;
					try {
						image = ImageIO.read(new File("Symbols/Operations/-.png"));
					} catch (IOException e) {
						System.out.println(S.charAt(i));
					}
					
				// decimal
				} else if (S.charAt(i) == '.') {
					try {
						image = ImageIO.read(new File("Symbols/Operations/decimal.png"));
					} catch (IOException e) {
						System.out.println(S.charAt(i));
					}
					
				// number
				} else {
					try {
						image = ImageIO.read(new File("Symbols/Variables/" + (S.charAt(i) - '0') + ".png"));
					} catch (IOException e) {
						System.out.println(i);
					}
				}
				imageList.add(image);
				height = Math.max(height, image.getHeight());
				width += image.getWidth();
			}

			// draws it all together
			BufferedImage connectedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			// Draw each picture onto the new picture
			Graphics g = connectedImage.createGraphics();
			int sum = 0;
			for (int i = 0; i < S.length(); i++) {
				g.drawImage(imageList.get(i), sum, 0, null);
				sum += imageList.get(i).getWidth();
			}
			g.dispose();
			return connectedImage;
		} catch (NumberFormatException e) {
		}

		// is a variable with subscript
		try {
			
			// subscript smaller than variable
			// seperates with '-'
			BufferedImage variable = stringToImage(S.substring(0, S.indexOf("-")));
			BufferedImage subscript = stringToImage(S.substring(S.indexOf("-") + 1));
			Image tmp = subscript.getScaledInstance(subscript.getWidth() / 2, subscript.getHeight() / 2,
					Image.SCALE_SMOOTH);
			int height = variable.getHeight();
			int width = variable.getWidth() + subscript.getWidth() / 2;
			BufferedImage connectedImage = new BufferedImage(width, height + 30, BufferedImage.TYPE_INT_ARGB);

			Graphics g = connectedImage.getGraphics();
			g.drawImage(variable, 0, 0, null);
			g.drawImage(tmp, variable.getWidth(), variable.getHeight() - subscript.getHeight() / 2 + 30, null);
			g.dispose();
			return connectedImage;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// should not be reached
		return null;
	}

	// Testing main class
	public static void main(String[] args) {
		try {
			Database database = new Database();
			quiz = new QuizEditor(database);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * class to render the jlist
	 * displays the jlist instead of the object name
	 */
	private class CustomListRenderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setText("");
			label.setBackground(lightBlue);
			label.setIcon(new ImageIcon((BufferedImage) value));
			label.setHorizontalAlignment(JLabel.CENTER);
			return label;
		}
	}
	
	
	/**
	 * pane to drop images on
	 * 
	 */
	private class DropPane extends JPanel {

		
		// Variables
		private DropTarget dropTarget;
		private DropTargetHandler dropTargetHandler;
		private Point dragPoint;
		private boolean dragOver = false;
		private JLabel message;
		private BufferedImage target;

		/**
		 * constructor for the pane
		 */
		public DropPane() {
			// drop pricture here message
			message = new JLabel("Drop Picture Here:");
			message.setFont(message.getFont().deriveFont(Font.BOLD, 24));
			add(message);
			
			// user can also select a file path instead of dropping the image
			JButton selectFile = new JButton("Select File");
			selectFile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fileChooser = new JFileChooser();
					
					// can only choose images
					FileFilter imageFilter = new FileNameExtensionFilter("Image files",
							ImageIO.getReaderFileSuffixes());
					fileChooser.setFileFilter(imageFilter);
					int returnValue = fileChooser.showOpenDialog(null);
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						// make sure image can be read
						try {
							image = ImageIO.read(selectedFile);
						} catch (IOException io) {
							io.printStackTrace();
						}
					}
				}
			});
			add(selectFile);
			setPreferredSize(new Dimension(400, 400));
		}
		
		/**
		 * gets drop target
		 * @return drop target
		 */
		private DropTarget getMyDropTarget() {
			if (dropTarget == null) {
				dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, null);
			}
			return dropTarget;
		}

		/**
		 * gets drop target handler
		 * @return drop target hander
		 */
		private DropTargetHandler getDropTargetHandler() {
			if (dropTargetHandler == null) {
				dropTargetHandler = new DropTargetHandler();
			}
			return dropTargetHandler;
		}

		/**
		 * method to add drop target listener
		 */
		@Override
		public void addNotify() {
			super.addNotify();
			try {
				getMyDropTarget().addDropTargetListener(getDropTargetHandler());
			} catch (TooManyListenersException ex) {
				// ex.printStackTrace();
			}
		}

		
		/**
		 * draws green box when file is over the panel and also draws the image
		 * 
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (dragOver) {
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(new Color(0, 255, 0, 64));
				g2d.fill(new Rectangle(getWidth(), getHeight()));
				// if drag is valid
				if (dragPoint != null && target != null) {
					int x = dragPoint.x - 12;
					int y = dragPoint.y - 12;
					g2d.drawImage(target, x, y, this);
				}
				g2d.dispose();
			}
			// image is selected
			if (image != null) {
				g.drawImage(image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
			}
			repaint();
		}

		/**
		 * 
		 * subclass for drop target listener
		 *
		 */
		protected class DropTargetHandler implements DropTargetListener {

			/**
			 * update listeners
			 * @param dtde drop target drag event
			 */
			protected void processDrag(DropTargetDragEvent dtde) {
				if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					dtde.acceptDrag(DnDConstants.ACTION_COPY);
				} else {
					dtde.rejectDrag();
				}
			}

			
			// dragged into the panel
			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				processDrag(dtde);
				SwingUtilities.invokeLater(new DragUpdate(true, dtde.getLocation()));
				repaint();
			}

			// dragged the file over the panel
			@Override
			public void dragOver(DropTargetDragEvent dtde) {
				processDrag(dtde);
				SwingUtilities.invokeLater(new DragUpdate(true, dtde.getLocation()));
				repaint();
			}

			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) {
			}

			// dragged in and then left the panel
			@Override
			public void dragExit(DropTargetEvent dte) {
				SwingUtilities.invokeLater(new DragUpdate(false, null));
				repaint();
			}

			// file dropped on the panel
			@Override
			public void drop(DropTargetDropEvent dtde) {

				SwingUtilities.invokeLater(new DragUpdate(false, null));

				// gets file
				Transferable transferable = dtde.getTransferable();
				if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					dtde.acceptDrop(dtde.getDropAction());
					try {
						
						// gets file path
						List<?> transferData = (List<?>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
						if (transferData != null && transferData.size() > 0) {

							dtde.dropComplete(true);
							// gets last image that has been dropped and checks if it is an image
							try {
								image = ImageIO.read((File) transferData.get(transferData.size() - 1));
							} catch (IIOException exx) {
								System.out.println("Not an image");
							}
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					// rejects if not supported
					dtde.rejectDrop();
				}
			}
		}

		/**
		 * class to run drag update
		 */
		public class DragUpdate implements Runnable {

			private boolean dragOver;
			private Point dragPoint;

			/**
			 * constructor to declare variables
			 * @param dragOver
			 * @param dragPoint
			 */
			public DragUpdate(boolean dragOver, Point dragPoint) {
				this.dragOver = dragOver;
				this.dragPoint = dragPoint;
			}

			// run thread
			@Override
			public void run() {
				DropPane.this.dragOver = dragOver;
				DropPane.this.dragPoint = dragPoint;
				DropPane.this.repaint();
			}
		}

	}

}