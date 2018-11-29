
// Imports
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

// Main JFrame
public class QuizEditor extends JFrame {

	// Variables
	private Subject s;
	private Unit u;
	private Question q, editQuestion;
	private boolean addQ, removeQ, editQ, newFormula, editingQ;
	private JList<String> list = new JList<String>(new DefaultListModel<String>());
	private static QuizEditor quiz;
	private JComboBox<String> subject = new JComboBox<String>();
	private JComboBox<String> unit = new JComboBox<String>();
	private JComboBox<String> formula = new JComboBox<String>();
	private Font font = new Font("Serif", Font.BOLD, 30);
	private Font small = new Font("Serif", Font.BOLD, 20);
	private JLabel currentFormulaDisplay;
	private int offset = 0;
	private int addingoffset = 20;

	private Database database;

	// QuizEditor(Database d) {
	QuizEditor(Database database, SimpleLinkedList<Subject> subjects, SimpleLinkedList<Symbol> symbols,
			SimpleLinkedList<SimpleLinkedList<Symbol>> formulas) {

		this.database = database;

		// Setting up frames
		this.setTitle("Quiz Editor");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(false);
		this.requestFocus(true);

		// Components on main frame
		JButton add = new JButton("Add");
		JButton remove = new JButton("Remove");
		JButton edit = new JButton("Edit");
		JButton addSubject = new JButton("Add new subject");
		JButton addUnit = new JButton("Add new unit");
		JButton removeSubject = new JButton("Remove current subject");
		JButton removeUnit = new JButton("Remove current unit");
		JButton confirm = new JButton("Confirm");
		JLabel problemStatement = new JLabel("Enter problem statement below");
		JLabel currentFormula = new JLabel("Current Formula");
		currentFormulaDisplay = new JLabel();
		JPanel contentPane = new JPanel();
		JPanel midPane = new JPanel();
		JPanel displayPane = new JPanel();
		JPanel displayFormula = new JPanel();
		JTextArea enter = new JTextArea();

		// Formatting
		add.setFont(font);
		add.setPreferredSize(new Dimension(150, 100));

		remove.setFont(font);
		remove.setPreferredSize(new Dimension(200, 100));

		edit.setFont(font);
		edit.setPreferredSize(new Dimension(150, 100));

		enter.setLineWrap(true);
		enter.setWrapStyleWord(true);

		subject.setFont(font);
		subject.setPreferredSize(new Dimension(100, 100));
		((JLabel) subject.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		addSubject.setFont(font);
		removeSubject.setFont(font);

		unit.setFont(font);
		((JLabel) unit.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		addUnit.setFont(font);
		removeUnit.setFont(font);

		confirm.setFont(font);

		formula.setFont(small);
		((JLabel) formula.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		problemStatement.setFont(small);
		problemStatement.setHorizontalAlignment(SwingConstants.CENTER);

		enter.setFont(small);
		currentFormula.setFont(small);

		currentFormulaDisplay.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		displayPane.setPreferredSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 200,300));

		// Initializing variables
		addQ = false;
		removeQ = false;
		editQ = false;
		editingQ = false;
		s = null;
		u = null;
		q = null;


		// SimpleLinkedList<Subject> subjects = d.getSubjects();

		// Adding all subjects
		subject.addItem("Choose a subject");
		unit.addItem("Choose a unit");
		for (int i = 0; i < subjects.size(); i++) {
			subject.addItem(subjects.get(i).getName());
		}

		// Subject combobox
		subject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String chosenSubject = (String) subject.getSelectedItem();
				// Update units in subject
				if (!chosenSubject.equals("Choose a subject")) {
					s = subjects.get(subject.getSelectedIndex() - 1);
					// Priority queue instead of linkedlist to keep the units in order
					SimpleLinkedList<Unit> units = s.getUnits();
					for (int i = 0; i < units.size(); i++) {
						unit.addItem(units.get(i).getName());
					}
				}
				list.setModel(new DefaultListModel<String>());
			}
		});

		// Unit combobox
		unit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String chosenUnit = (String) unit.getSelectedItem();
				list.setModel(new DefaultListModel<String>());
				// Updates questions in unit
				if (!chosenUnit.equals("Choose a unit")) {
					SimpleLinkedList<Unit> units = s.getUnits();
					DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
					u = units.get(unit.getSelectedIndex() - 1);
					SimpleLinkedList<Question> questions = u.getQuestions();
					for (int i = 0; i < questions.size(); i++) {
						model.addElement(questions.get(i).getProblemStatement());
					}
				}
			}
		});

		// Formula combobox
		formula.addItem("Choose a formula");
		formula.addItem("Create new formula");
		formula.addItem("Use previous formula");

		// Add Button
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addQ = true;
				removeQ = false;
				editQ = false;
				editingQ = false;
				displayPane.removeAll();

				// action listener for formula combobox
				formula.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String chosenFormula = (String) formula.getSelectedItem();
						q = new Question("", new SimpleLinkedList<Symbol>());
						// creating a new formula
						if (chosenFormula.equals("Create new formula")) {
							newFormula = true;
							createFormula(symbols, currentFormulaDisplay);
							// using a previous formula
						} else {
							newFormula = false;
							previousFormula(formulas, currentFormulaDisplay);
						}
					}
				});
				displayPane.add(formula);

				// Setting up add interface
				displayFormula.add(currentFormula);
				displayFormula.add(Box.createRigidArea(new Dimension(0,addingoffset)));
				currentFormula.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				displayFormula.add(currentFormulaDisplay);
				displayFormula.add(Box.createRigidArea(new Dimension(0,addingoffset)));
				displayFormula.setLayout(new BoxLayout(displayFormula, BoxLayout.Y_AXIS));
				JScrollPane scroll = new JScrollPane(displayFormula, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				displayPane.add(scroll);
				displayPane.add(Box.createRigidArea(new Dimension(0,addingoffset)));
				problemStatement.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				displayPane.add(problemStatement);
				enter.setEditable(true);
				displayPane.add(enter);
				revalidate();
			}
		});

		// Remove button
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeQ = true;
				addQ = false;
				editQ = false;
				editingQ = false;

				// Displays list with all questions in the unit selected supporting multi selection
				displayPane.removeAll();
				list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				displayPane.add(scroll);
				revalidate();
			}
		});

		// Edit button
		edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editQ = true;
				addQ = false;
				removeQ = false;
				editingQ = false;
				displayPane.removeAll();
				// Displays list with all questions in the unit but can only edit one at a time
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				subject.setVisible(true);
				unit.setVisible(true);
				JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				displayPane.add(scroll);
				revalidate();
			}
		});

		// Confirm Button
		// Button used to confirm choices for add, remove and edit
		confirm.addActionListener(new ActionListener() {
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
				if (q == null && !removeQ && !editQ && !editingQ) {
					JOptionPane.showMessageDialog(null, "A valid question has not been selected");
					return;
				}

				// Removes selected question
				if (removeQ) {
					SimpleLinkedList<Question> questions = u.getQuestions();
					int[] indicies = list.getSelectedIndices();
					DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
					for (int i = 0; i < indicies.length; i++) {
						// d.deleteQuestion(s,u,questions.get(i);
						model.removeElementAt(i);
						// Remove Later
						u.removeQuestion(questions.get(i));
					}
					list.clearSelection();


					// Adds a question
				} else if (addQ) {
					q.setProblemStatement(enter.getText());
					// d.addQuestion(s,u,q);
					DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
					model.addElement(q.getProblemStatement());
					if (newFormula)
						formulas.add(q.getFormula());
					// Remove Later
					u.addQuestion(q);
					q = null;
					currentFormulaDisplay.setIcon(new ImageIcon());
					enter.setText("");

					// Editing questions
				} else if (editQ) {
					// Same as add interface but formula and problem statement are already filled in
					editQ = false;
					editingQ = true;
					editQuestion = u.getQuestions().get((list.getSelectedIndex()));
					String editProblemStatement = editQuestion.getProblemStatement();
					SimpleLinkedList<Symbol> list = editQuestion.getFormula();

					SimpleLinkedList<Symbol> editFormula = new SimpleLinkedList<Symbol>();
					for (int i = 0; i < list.size(); i++) {
						editFormula.add(list.get(i));
					}
					q = new Question(editProblemStatement, editFormula);
					displayPane.removeAll();
					subject.setVisible(true);
					unit.setVisible(true);
					currentFormulaDisplay.setIcon(new ImageIcon(joinBufferedImages(editFormula)));
					enter.setText(editProblemStatement);
					formula.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String chosenFormula = (String) formula.getSelectedItem();
							if (chosenFormula.equals("Create new formula")) {
								newFormula = true;
								createFormula(symbols, currentFormulaDisplay);
							} else {
								newFormula = false;
								previousFormula(formulas, currentFormulaDisplay);
							}
						}
					});
					displayPane.add(formula);

					displayFormula.add(currentFormula);
					displayFormula.add(Box.createRigidArea(new Dimension(0,addingoffset)));
					currentFormula.setAlignmentX(JLabel.CENTER_ALIGNMENT);
					displayFormula.add(currentFormulaDisplay);
					displayFormula.add(Box.createRigidArea(new Dimension(0,addingoffset)));
					displayFormula.setLayout(new BoxLayout(displayFormula, BoxLayout.Y_AXIS));
					JScrollPane scroll = new JScrollPane(displayFormula, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					displayPane.add(scroll);
					displayPane.add(Box.createRigidArea(new Dimension(0,addingoffset)));
					problemStatement.setAlignmentX(JLabel.CENTER_ALIGNMENT);
					displayPane.add(problemStatement);
					enter.setEditable(true);
					displayPane.add(enter);
					revalidate();
					//
				} else {
					// Confirms selection for editing interface
					DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
					model.add(model.indexOf(editQuestion.getProblemStatement()), enter.getText());
					model.removeElement(editQuestion.getProblemStatement());
					editQuestion.setProblemStatement(enter.getText());
					editQuestion.setFormula(q.getFormula());
					if (newFormula) formulas.add(q.getFormula());
					q = null;
					currentFormulaDisplay.setIcon(new ImageIcon());
					enter.setText("");
				}
				
				database.update(); // call database to update
			}
		});

		// Add subject button
		addSubject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSubject(subjects);
			}
		});

		// Add unit button
		addUnit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (s == null) {
					JOptionPane.showMessageDialog(null, "A valid subject has not been selected");
					return;
				}
				SimpleLinkedList<Unit> unitlist = s.getUnits();
				addUnit(unitlist, null);
			}
		});

		// Remove subject button
		removeSubject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Confirm JDialog
				// d.removeSubject(s);
				if (s == null) {
					JOptionPane.showMessageDialog(null, "A valid subject has not been selected");
					return;
				}
				subject.removeItem(s.getName());
				subject.setSelectedIndex(0);
				s = null;
				database.update(); // call database to update
			}
		});

		// remove unit button
		removeUnit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (s == null) {
					JOptionPane.showMessageDialog(null, "A valid subject has not been selected");
					return;
				} else if (u == null) {
					JOptionPane.showMessageDialog(null, "A valid unit has not been selected");
					return;
				}
				unit.removeItem(u.getName());
				u = null;
				unit.setSelectedIndex(0);
				database.update(); // call database to update
			}
		});

		// Panel to store main buttons

		JPanel buttons = new JPanel();
		add.setAlignmentY(JButton.CENTER_ALIGNMENT);
		remove.setAlignmentY(JButton.CENTER_ALIGNMENT);
		edit.setAlignmentX(JButton.CENTER_ALIGNMENT);
		buttons.add(add);
		buttons.add(Box.createRigidArea(new Dimension(100,0)));
		buttons.add(remove);
		buttons.add(Box.createRigidArea(new Dimension(100,0)));
		buttons.add(edit);
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

		midPane.add(buttons);

		// Difference in height between components in the frame
		offset = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - (int)displayPane.getPreferredSize().getHeight() - (int)buttons.getPreferredSize().getHeight() - (int)subject.getMinimumSize().getHeight() - (int)unit.getMinimumSize().getHeight() - (int)confirm.getPreferredSize().getHeight();
		offset /= 5;

		midPane.add(Box.createRigidArea(new Dimension(0,offset)));

		// Panel to store subject related components
		JPanel subjectPane = new JPanel();
		subjectPane.add(subject);
		subjectPane.add(Box.createRigidArea(new Dimension(100,0)));
		subjectPane.add(addSubject);
		subjectPane.add(Box.createRigidArea(new Dimension(100,0)));
		subjectPane.add(removeSubject);
		subjectPane.setLayout(new BoxLayout(subjectPane, BoxLayout.X_AXIS));
		subjectPane.setPreferredSize(new Dimension((int) midPane.getPreferredSize().getWidth(), (int)subject.getMinimumSize().getHeight()));
		midPane.add(subjectPane);

		midPane.add(Box.createRigidArea(new Dimension(0,offset)));
		// Panel to store unit related components
		JPanel unitPane = new JPanel();
		unitPane.add(unit);
		unitPane.add(Box.createRigidArea(new Dimension(100,0)));
		unitPane.add(addUnit);
		unitPane.add(Box.createRigidArea(new Dimension(100,0)));
		unitPane.add(removeUnit);
		unitPane.setLayout(new BoxLayout(unitPane, BoxLayout.X_AXIS));
		unitPane.setPreferredSize(new Dimension((int) midPane.getPreferredSize().getWidth(), (int)unit.getMinimumSize().getHeight()));
		midPane.add(unitPane);

		displayPane.setLayout(new BoxLayout(displayPane, BoxLayout.Y_AXIS));
		midPane.add(Box.createRigidArea(new Dimension(0,offset)));
		midPane.add(displayPane);
		midPane.add(Box.createRigidArea(new Dimension(0,offset)));
		confirm.setAlignmentX(JButton.CENTER_ALIGNMENT);
		midPane.add(confirm);
		midPane.add(Box.createRigidArea(new Dimension(0,offset)));
		midPane.setLayout(new BoxLayout(midPane, BoxLayout.Y_AXIS));

		// main jpanel
		contentPane.add(Box.createRigidArea(new Dimension(100,0)));
		midPane.setPreferredSize(midPane.getMinimumSize());
		JScrollPane scrollpane = new JScrollPane(midPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scrollpane);
		contentPane.add(Box.createRigidArea(new Dimension(100,0)));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		this.setContentPane(contentPane);
		this.setVisible(true);
	}

	// method to add a subject
	// AddSubject(Database d) {
	private void addSubject(SimpleLinkedList<Subject> subjects) {
		JFrame addSubjectFrame = new JFrame("Add Subject");
		addSubjectFrame.setSize(500, 600);
		addSubjectFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);


		// Components
		JPanel contentPanel = new JPanel();
		JLabel enterName = new JLabel("Enter the  name of the subject");
		JLabel enterGrade = new JLabel("Enter the grade of the subject");
		JLabel enterLevel = new JLabel("Enter the level of the subject");
		JTextArea name = new JTextArea();
		JTextArea grade = new JTextArea();
		JTextArea level = new JTextArea();
		JButton addUnit = new JButton("Add a unit");
		JList<String> units = new JList<String>();
		JScrollPane scroll = new JScrollPane(units, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JButton confirm = new JButton("Confirm");


		// Formatting
		enterName.setFont(font);
		enterName.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		enterGrade.setFont(font);
		enterGrade.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		enterLevel.setFont(font);
		enterLevel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		name.setFont(small);
		name.setLineWrap(true);
		name.setWrapStyleWord(true);

		grade.setFont(small);
		grade.setLineWrap(true);
		grade.setWrapStyleWord(true);

		level.setFont(small);
		level.setLineWrap(true);
		level.setWrapStyleWord(true);

		addUnit.setFont(font);
		addUnit.setAlignmentX(CENTER_ALIGNMENT);
		confirm.setFont(font);
		confirm.setAlignmentX(JButton.CENTER_ALIGNMENT);

		// List items
		SimpleLinkedList<Unit> unitlist = new SimpleLinkedList<Unit>();
		DefaultListModel<String> model = (DefaultListModel<String>)list.getModel();

		// Add unit button
		addUnit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addUnit(unitlist, model);
			}
		});

		// Confirm button
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					subjects.add(new Subject(name.getText(), Integer.parseInt(grade.getText()), level.getText()));
					subject.addItem(name.getText());
					addSubjectFrame.dispose();
					database.update(); // call database to update
					return;
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "A valid grade has not been selected");
					return;
				}
			}
		});


		// Adding onto the content pane
		contentPanel.add(enterName);
		contentPanel.add(name);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(enterGrade);
		contentPanel.add(grade);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(enterLevel);
		contentPanel.add(level);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(scroll);
		contentPanel.add(addUnit);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(confirm);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		// this.setPreferredSize(preferredSize);


		addSubjectFrame.setContentPane(contentPanel);
		addSubjectFrame.setVisible(true);
	}



	// Method to add unit
	private void addUnit(SimpleLinkedList<Unit> unitlist, DefaultListModel<String> model) {
		JFrame addUnitFrame = new JFrame("Add Unit");
		addUnitFrame.setSize(500, 300);
		addUnitFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);


		// Components
		JPanel contentPanel = new JPanel();
		JLabel enterName = new JLabel("Enter unit name");
		JLabel enterNum = new JLabel("Enter unit number");
		JTextArea name = new JTextArea();
		JTextArea num = new JTextArea();
		JButton confirm = new JButton("Confirm");


		// Formatting
		enterName.setFont(font);
		enterName.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		enterNum.setFont(font);
		enterNum.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		name.setFont(small);
		name.setLineWrap(true);
		name.setWrapStyleWord(true);

		num.setFont(small);
		num.setLineWrap(true);
		num.setWrapStyleWord(true);

		confirm.setFont(font);
		confirm.setAlignmentX(JButton.CENTER_ALIGNMENT);



		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					unitlist.add(new Unit(name.getText(), Integer.parseInt(num.getText())));
					if (model != null) {
						model.addElement(name.getText());
					}
					if (s != null) {
						unit.addItem(name.getText());
					}
					addUnitFrame.dispose();
					database.update(); // call database to update
					return;
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "A valid number has not been selected");
				}
			}
		});


		// Adding components onto JFrame
		contentPanel.add(enterName);
		contentPanel.add(name);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(enterNum);
		contentPanel.add(num);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(confirm);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		addUnitFrame.setContentPane(contentPanel);
		addUnitFrame.setVisible(true);
	}



	// Method to join symbols together to form a formula
	private BufferedImage joinBufferedImages(SimpleLinkedList<Symbol> formula) {
		// get total width and max height
		int height = 0;
		int width = 0;
		for (int i = 0; i < formula.size(); i++) {
			width += formula.get(i).getImage().getWidth();
			height = Math.max(formula.get(i).getImage().getHeight(), height);
		}
		BufferedImage connectedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);


		// Draw each picture onto the new picture
		Graphics g = connectedImage.createGraphics();
		int sum = 0;
		for (int i = 0; i < formula.size(); i++) {
			g.drawImage(formula.get(i).getImage(), sum, 0, null);
			sum += formula.get(i).getImage().getWidth();
		}
		g.dispose();


		return connectedImage;
	}



	// Testing main class
	public static void main(String[] args) {
		try {
			//			SimpleLinkedList<Subject> subjects = new SimpleLinkedList<Subject>();
			//			Subject s = new Subject("HI", 11, "S");
			//			s.addUnit(new Unit("a", 1));
			//			s.addUnit(new Unit("b", 2));
			//			s.addUnit(new Unit("c", 3));
			//			s.addUnit(new Unit("d", 4));
			//			s.addUnit(new Unit("e", 5));
			//			subjects.add(s);
			Database database = new Database();
			SimpleLinkedList<Subject> subjects = database.getSubjects();

			Symbol add = new Operation("+");
			Symbol subtract = new Operation("-");
			Symbol multiply = new Operation("mul");
			Symbol divide = new Operation("div");
			Symbol pi = new Variable("pi");
			Symbol time = new Variable("time");
			Symbol velocity = new Variable("velocity");
			SimpleLinkedList<Symbol> symbols = new SimpleLinkedList<Symbol>();
			symbols.add(add);
			symbols.add(subtract);
			symbols.add(multiply);
			symbols.add(divide);
			symbols.add(pi);
			symbols.add(time);
			symbols.add(velocity);
			SimpleLinkedList<SimpleLinkedList<Symbol>> formulas = new SimpleLinkedList<SimpleLinkedList<Symbol>>();
			SimpleLinkedList<Symbol> tmp = new SimpleLinkedList<Symbol>();
			tmp.add(time);
			tmp.add(add);
			tmp.add(velocity);
			tmp.add(pi);
			formulas.add(tmp);
			quiz = new QuizEditor(database, subjects, symbols, formulas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	// Display all previous formulas
	private void previousFormula(SimpleLinkedList<SimpleLinkedList<Symbol>> formulas, JLabel formulaDisplay) {
		JFrame previousFormulaFrame = new JFrame("Choose Previous Formula");
		previousFormulaFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		previousFormulaFrame.requestFocus();
		previousFormulaFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());

		// setting up list
		DefaultListModel<BufferedImage> model = new DefaultListModel<BufferedImage>();
		JList<BufferedImage> list = new JList<BufferedImage>(model);
		JPanel contentPanel = new JPanel();
		for (int i = 0; i < formulas.size(); i++) {
			model.addElement(joinBufferedImages(formulas.get(i)));
		}
		list.setCellRenderer(new CustomListRenderer());
		list.setPreferredSize(list.getMinimumSize());
		JScrollPane scroll = new JScrollPane(list, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


		// Confirm button
		JButton confirm = new JButton("Confirm formula");
		confirm.setFont(font);
		confirm.setAlignmentX(JButton.CENTER_ALIGNMENT);

		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				q.setFormula(formulas.get(index));
				formulaDisplay.setIcon(new ImageIcon(joinBufferedImages(formulas.get(index))));
				quiz.revalidate();
				previousFormulaFrame.dispose();
				return;
			}
		});


		// Adding components onto frame
		contentPanel.add(scroll);
		contentPanel.add(confirm);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		previousFormulaFrame.setContentPane(contentPanel);
		previousFormulaFrame.setVisible(true);
	}




	// ChooseFormula (Database d) {

	// Method to create formulas
	private void createFormula(SimpleLinkedList<Symbol> symbols, JLabel formulaDisplay) {
		JFrame createFormulaFrame = new JFrame("Create new formula");
		createFormulaFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createFormulaFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		createFormulaFrame.requestFocus();

		// Panel for buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout((symbols.size() + 1) / (int) Math.sqrt((symbols.size() + 1)),(int) Math.sqrt(symbols.size())));

		// Main panel
		JPanel contentPanel = new JPanel();


		// List to store entered formula
		SimpleLinkedList<Symbol> formula = new SimpleLinkedList<Symbol>();
		// SimpleLinkedList<Symbol> symbols = d.getSymbols();


		// Display entered formula
		JLabel enteredFormula = new JLabel();
		enteredFormula.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		// list with all of the jbuttons
		SimpleLinkedList<JButton> buttonlist = new SimpleLinkedList<JButton>();

		for (int i = 0; i < symbols.size(); i++) {

			// JButton for all symbols
			JButton symbol = new JButton(new ImageIcon(symbols.get(i).getImage()));
			symbol.setBackground(Color.WHITE);
			symbol.addActionListener(new ActionListener() {
				// Add to formula
				public void actionPerformed(ActionEvent e) {
					formula.add(symbols.get(buttonlist.indexOf(symbol)));
					enteredFormula.setIcon(new ImageIcon(joinBufferedImages(formula)));
					createFormulaFrame.revalidate();
				}
			});
			buttonlist.add(symbol);
			buttons.add(symbol);
		}

		// Delete last symbol
		JButton backspace = new JButton("Del");
		backspace.setBackground(Color.WHITE);
		backspace.setFont(font);

		backspace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (formula.size() > 0) {
					formula.remove(formula.size() - 1);
				} else {
					return;
				}
				if (formula.size() > 0) {
					enteredFormula.setIcon(new ImageIcon(joinBufferedImages(formula)));
				} else {
					enteredFormula.setIcon(new ImageIcon());
				}
				createFormulaFrame.revalidate();
			}
		});

		buttons.add(backspace);


		// confirm button
		JButton confirm = new JButton("Confirm");
		confirm.setFont(font);
		confirm.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				q.setFormula(formula);
				formulaDisplay.setIcon(new ImageIcon(joinBufferedImages(formula)));
				quiz.revalidate();
				createFormulaFrame.dispose();
				return;
			}
		});

		// add to content pane
		contentPanel.add(buttons);
		JScrollPane scroll = new JScrollPane(enteredFormula, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		contentPanel.add(scroll);
		contentPanel.add(confirm);

		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		createFormulaFrame.setContentPane(contentPanel);
		createFormulaFrame.setVisible(true);
	}




	// Method to draw buffered images in a jlist
	private class CustomListRenderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setText("");
			label.setIcon(new ImageIcon((BufferedImage) value));
			label.setHorizontalAlignment(JLabel.CENTER);
			return label;
		}
	}
}
