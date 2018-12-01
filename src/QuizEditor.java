
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
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
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
	private Color indigo = new Color(56, 53, 74);
	private Color lightBlue = new Color(162, 236, 250);
	private Color orange = new Color(232, 167, 55);
	private JLabel currentFormulaDisplay;
	
	private int offset = 0;
	private int addingoffset = 20;

	private Database database;

	// QuizEditor(Database d) {
	QuizEditor(Database database, SimpleLinkedList<Subject> subjects, SimpleLinkedList<Symbol> symbols,
			SimpleLinkedList<SimpleLinkedList<Symbol>> formulas) {

		this.database = database;
		// Setting up frames
		setTitle("Quiz Editor");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setFocusable(true);
		setResizable(false);
		setUndecorated(true);
		requestFocusInWindow();
		// Components on main frame
		JButton add = new JButton("Add");
		JButton remove = new JButton("Remove");
		JButton edit = new JButton("Edit");
		JButton addSubject = new JButton("Add new subject");
		JButton addUnit = new JButton("Add new unit");
		JButton removeSubject = new JButton("Remove current subject");
		JButton removeUnit = new JButton("Remove current unit");
		JButton confirm = new JButton("Confirm");
		JButton exit = new JButton("Exit");
		JLabel problemStatement = new JLabel("Enter problem statement below");
		JLabel currentFormula = new JLabel("Current Formula");
		JLabel logo = new JLabel(new ImageIcon("logo.png"));
		
		currentFormulaDisplay = new JLabel();
		JPanel contentPane = new JPanel();
		JPanel midPane = new JPanel();
		JPanel displayPane = new JPanel();
		JPanel displayFormula = new JPanel();
		JTextArea enter = new JTextArea();

		// Formatting
		list.setBackground(indigo);
		
		
		add.setFont(font);
		add.setBackground(lightBlue);
		add.setPreferredSize(new Dimension(150, 100));

		remove.setFont(font);
		remove.setBackground(lightBlue);
		remove.setPreferredSize(new Dimension(200, 100));

		edit.setFont(font);
		edit.setBackground(lightBlue);
		edit.setPreferredSize(new Dimension(150, 100));

		enter.setLineWrap(true);
		enter.setWrapStyleWord(true);

		subject.setFont(font);
		subject.setBackground(lightBlue);
		subject.setPreferredSize(new Dimension(100, 100));
		((JLabel) subject.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		addSubject.setFont(font);
		addSubject.setBackground(lightBlue);
		
		removeSubject.setFont(font);
		removeSubject.setBackground(lightBlue);

		unit.setFont(font);
		unit.setBackground(lightBlue);
		((JLabel) unit.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		addUnit.setFont(font);
		addUnit.setBackground(lightBlue);
		
		removeUnit.setFont(font);
		removeUnit.setBackground(lightBlue);
		
		confirm.setFont(font);
		confirm.setAlignmentX(JButton.CENTER_ALIGNMENT);
		confirm.setBackground(lightBlue);
		
		exit.setFont(font);
		exit.setAlignmentX(JButton.CENTER_ALIGNMENT);
		exit.setBackground(lightBlue);
		
		formula.setFont(small);
		((JLabel) formula.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		problemStatement.setFont(small);
		problemStatement.setHorizontalAlignment(SwingConstants.CENTER);

		enter.setFont(small);
		currentFormula.setFont(small);

		currentFormulaDisplay.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		displayPane.setPreferredSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 200,300));
		displayPane.setBackground(lightBlue);
		
		logo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
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
			subject.addItem(subjects.get(i).getName() + " " + subjects.get(i).getGrade() + " " + subjects.get(i).getLevel());
		}

		// Subject combobox
		subject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String chosenSubject = (String) subject.getSelectedItem();
				// Update units in subject
				if (!chosenSubject.equals("Choose a subject")) {
					for (int i = 1; i < unit.getItemCount(); i++) {
						unit.removeItemAt(i);
					}
					s = subjects.get(subject.getSelectedIndex() - 1);
					// Priority queue instead of linkedlist to keep the units in order
					SimpleLinkedList<Unit> units = s.getUnits();
					
					for (int i = 0; i < units.size(); i++) {
						unit.addItem(units.get(i).getNum() + ". " + units.get(i).getName());
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
		formula.setBackground(lightBlue);

		// Add Button
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addQ) return;
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
				JScrollPane scroll = new JScrollPane(displayFormula, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scroll.getVerticalScrollBar().setUnitIncrement(16);
				scroll.getHorizontalScrollBar().setUnitIncrement(16);
				scroll.setMinimumSize(new Dimension((int)displayPane.getPreferredSize().getWidth(),200));
				displayPane.add(scroll);
				displayPane.add(Box.createRigidArea(new Dimension(0,addingoffset)));
				problemStatement.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				displayPane.add(problemStatement);
				enter.setEditable(true);
				displayPane.add(enter);
				revalidate();
				midPane.setPreferredSize(new Dimension((int)midPane.getMinimumSize().getWidth(),(int)exit.getLocation().getY() + 50 + offset));
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
				scroll.getVerticalScrollBar().setUnitIncrement(16);
				scroll.getHorizontalScrollBar().setUnitIncrement(16);
				displayPane.add(scroll);
				revalidate();
				midPane.setPreferredSize(new Dimension((int)midPane.getMinimumSize().getWidth(),(int)exit.getLocation().getY() + 50 + offset));

				
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
				scroll.getVerticalScrollBar().setUnitIncrement(16);
				scroll.getHorizontalScrollBar().setUnitIncrement(16);
				displayPane.add(scroll);
				revalidate();
				midPane.setPreferredSize(new Dimension((int)midPane.getMinimumSize().getWidth(),(int)exit.getLocation().getY() + 50 + offset));

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
					Object[] A = model.toArray();
					for (int i = 0; i < indicies.length; i++) {
						// d.deleteQuestion(s,u,questions.get(i);
						model.removeElement(A[indicies[i]]);
						// Remove Later
						u.removeQuestion(questions.get(indicies[i]));
					}
					list.clearSelection();


					// Adds a question
				} else if (addQ) {
					q.setProblemStatement(enter.getText());
					// d.addQuestion(s,u,q);
					DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
					model.addElement(q.getProblemStatement());
					if (newFormula) {
						formulas.add(q.getFormula());
						newFormula = false;
					}
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
					JScrollPane scroll = new JScrollPane(displayFormula, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					scroll.getVerticalScrollBar().setUnitIncrement(16);
					scroll.getHorizontalScrollBar().setUnitIncrement(16);
					scroll.setPreferredSize(new Dimension((int)displayPane.getPreferredSize().getWidth(),200));
					displayPane.add(scroll);
					displayPane.add(Box.createRigidArea(new Dimension(0,addingoffset)));
					problemStatement.setAlignmentX(JLabel.CENTER_ALIGNMENT);
					displayPane.add(problemStatement);
					enter.setEditable(true);
					displayPane.add(enter);
					revalidate();
					midPane.setPreferredSize(new Dimension((int)midPane.getMinimumSize().getWidth(),(int)exit.getLocation().getY() + 50 + offset));
					
					
					//
				} else {
					// Confirms selection for editing interface
					DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
					model.add(model.indexOf(editQuestion.getProblemStatement()), enter.getText());
					model.removeElement(editQuestion.getProblemStatement());
					editQuestion.setProblemStatement(enter.getText());
					editQuestion.setFormula(q.getFormula());
					if (newFormula) {
						formulas.add(q.getFormula());
						newFormula = false;
					}
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
		
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quiz.dispose();
			}
		});
		
		
		midPane.add(logo);
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
		buttons.setBackground(indigo);
		midPane.add(buttons);

		// Difference in height between components in the frame
		offset = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - (int)displayPane.getPreferredSize().getHeight() - (int)buttons.getPreferredSize().getHeight() - (int)subject.getMinimumSize().getHeight() - (int)unit.getMinimumSize().getHeight() - (int)confirm.getPreferredSize().getHeight() - (int)exit.getPreferredSize().getHeight();
		offset /= 6;
		offset += 50;
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
		subjectPane.setBackground(indigo);
		subjectPane.setMaximumSize(subjectPane.getPreferredSize());
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
		unitPane.setBackground(indigo);
		unitPane.setMaximumSize(unitPane.getPreferredSize());
		midPane.add(unitPane);

		displayPane.setLayout(new BoxLayout(displayPane, BoxLayout.Y_AXIS));
		midPane.add(Box.createRigidArea(new Dimension(0,offset)));
		midPane.add(displayPane);
		midPane.add(Box.createRigidArea(new Dimension(0,offset)));
		midPane.add(confirm);
		midPane.add(Box.createRigidArea(new Dimension(0,offset)));
		midPane.add(exit);
		midPane.add(Box.createRigidArea(new Dimension(0,offset)));
		midPane.setBackground(indigo);
		midPane.setLayout(new BoxLayout(midPane, BoxLayout.Y_AXIS));
		
		// main jpanel
		this.setVisible(true);
		contentPane.add(Box.createRigidArea(new Dimension(100,0)));
		
		midPane.setPreferredSize(new Dimension((int)midPane.getMinimumSize().getWidth(),(int)exit.getLocation().getY() + 50));
		JScrollPane scrollpane = new JScrollPane(midPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane.getVerticalScrollBar().setUnitIncrement(16);
		scrollpane.getVerticalScrollBar().setBackground(lightBlue);
		scrollpane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		contentPane.add(scrollpane);
		contentPane.add(Box.createRigidArea(new Dimension(100,0)));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		contentPane.setBackground(indigo);
		this.setContentPane(contentPane);
		this.setVisible(true);
		midPane.setPreferredSize(new Dimension((int)midPane.getMinimumSize().getWidth(),(int)exit.getLocation().getY() + 50 + offset));
		revalidate();		
	}

	// method to add a subject
	// AddSubject(Database d) {
	private void addSubject(SimpleLinkedList<Subject> subjects) {
		JFrame addSubjectFrame = new JFrame("Add Subject");
		addSubjectFrame.setSize(500, 700);
		addSubjectFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addSubjectFrame.setUndecorated(true);

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
		DefaultListModel<String> unitmodel = new DefaultListModel<String>();
		JList<String> units = new JList<String>(unitmodel);
		JScrollPane scroll = new JScrollPane(units, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		JButton confirm = new JButton("Confirm");
		JButton cancel = new JButton("Cancel");
		JLabel logo = new JLabel(new ImageIcon("logo.png"));
		// Formatting
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
		addUnit.setAlignmentX(CENTER_ALIGNMENT);
		confirm.setBackground(lightBlue);
		confirm.setFont(font);
		confirm.setAlignmentX(JButton.CENTER_ALIGNMENT);
		cancel.setBackground(lightBlue);
		cancel.setFont(font);
		cancel.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		// List items
		SimpleLinkedList<Unit> unitlist = new SimpleLinkedList<Unit>();
		units.setBackground(lightBlue);
		
		// Add unit button
		addUnit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addUnit(unitlist, unitmodel);
			}
		});

		// Confirm button
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					subjects.add(new Subject(name.getText(), Integer.parseInt(grade.getText()), level.getText()));
					subject.addItem(name.getText() + " " + grade.getText() + " " + level.getText());
					addSubjectFrame.dispose();
					database.update(); // call database to update
					return;
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "A valid grade has not been selected");
					return;
				}
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSubjectFrame.dispose();
			}
		});


		// Adding onto the content pane
		contentPanel.add(logo);
		contentPanel.add(enterName);
		contentPanel.add(name);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(enterGrade);
		contentPanel.add(grade);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(enterLevel);
		contentPanel.add(level);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(unitLabel);
		contentPanel.add(scroll);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(addUnit);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(confirm);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(cancel);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.setBackground(indigo);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		// this.setPreferredSize(preferredSize);

		JScrollPane scrollpane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane.getVerticalScrollBar().setUnitIncrement(16);
		addSubjectFrame.setContentPane(scrollpane);
		addSubjectFrame.setVisible(true);
	}



	// Method to add unit
	private void addUnit(SimpleLinkedList<Unit> unitlist, DefaultListModel<String> model) {
		JFrame addUnitFrame = new JFrame("Add Unit");
		addUnitFrame.setSize(500, 300);
		addUnitFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addUnitFrame.setUndecorated(true);

		// Components
		JPanel contentPanel = new JPanel();
		JLabel logo = new JLabel(new ImageIcon("logo.png"));
		JLabel enterName = new JLabel("Enter unit name");
		JLabel enterNum = new JLabel("Enter unit number");
		JTextArea name = new JTextArea();
		JTextArea num = new JTextArea();
		JButton confirm = new JButton("Confirm");
		JButton cancel = new JButton("Cancel");


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

		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					unitlist.add(new Unit(name.getText(), Integer.parseInt(num.getText())));
					if (model != null) {
						model.addElement(name.getText());
					}
					if (s != null) {
						unit.addItem(num.getText() + ". " + name.getText());
					}
					addUnitFrame.dispose();
					database.update(); // call database to update
					return;
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "A valid number has not been selected");
				}
			}
		});

		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addUnitFrame.dispose();
			}
		});
		
		// Adding components onto JFrame
		contentPanel.add(logo);
		contentPanel.add(enterName);
		contentPanel.add(name);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(enterNum);
		contentPanel.add(num);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(confirm);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.add(cancel);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(indigo);
		JScrollPane scroll = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		addUnitFrame.setContentPane(scroll);
		addUnitFrame.setVisible(true);
	}



	// Method to join symbols together to form a formula
	private BufferedImage joinBufferedImages(SimpleLinkedList<Symbol> formula) {
		// get total width and max height
		int height = 0;
		int width = 0;
		for (int i = 0; i < formula.size(); i++) {
			if (formula.get(i).getImage() == null) {
				BufferedImage image = stringToImage(formula.get(i).getId());
				width += image.getWidth();
				height = Math.max(image.getHeight(), height);
			} else {
				width += formula.get(i).getImage().getWidth();
				height = Math.max(formula.get(i).getImage().getHeight(), height);
			}
		}
		BufferedImage connectedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);


		// Draw each picture onto the new picture
		Graphics g = connectedImage.createGraphics();
		int sum = 0;
		for (int i = 0; i < formula.size(); i++) {
			if (formula.get(i).getImage() == null) {
				BufferedImage image = stringToImage(formula.get(i).getId());
				g.drawImage(stringToImage(formula.get(i).getId()), sum, 0, null);
				sum += image.getWidth();
			} else {
				g.drawImage(formula.get(i).getImage(), sum, 0, null);
				sum += formula.get(i).getImage().getWidth();
			}
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
		previousFormulaFrame.setUndecorated(true);
		
		JLabel logo = new JLabel(new ImageIcon("logo.png"));
		logo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		// setting up list
		DefaultListModel<BufferedImage> model = new DefaultListModel<BufferedImage>();
		JList<BufferedImage> list = new JList<BufferedImage>(model);
		JPanel contentPanel = new JPanel();
		JPanel contentPane = new JPanel();
		for (int i = 0; i < formulas.size(); i++) {
			model.addElement(joinBufferedImages(formulas.get(i)));
		}
		list.setCellRenderer(new CustomListRenderer());
		list.setPreferredSize(list.getMinimumSize());
		JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);

		// Confirm button
		JButton confirm = new JButton("Confirm formula");
		confirm.setFont(font);
		confirm.setAlignmentX(JButton.CENTER_ALIGNMENT);
		confirm.setBackground(lightBlue);
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
		
		JButton cancel = new JButton("Cancel");
		cancel.setFont(font);
		cancel.setAlignmentX(JButton.CENTER_ALIGNMENT);
		cancel.setBackground(lightBlue);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previousFormulaFrame.dispose();
			}
		});
		JPanel buttons = new JPanel();
		buttons.add(confirm);
		buttons.add(Box.createRigidArea(new Dimension(offset,0)));
		buttons.add(cancel);
		buttons.setBackground(indigo);
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		// Adding components onto frame
		contentPanel.add(logo);
		contentPanel.add(scroll);
		contentPanel.add(Box.createRigidArea(new Dimension(0, addingoffset)));
		contentPanel.add(buttons);
		contentPanel.add(Box.createRigidArea(new Dimension(0, addingoffset)));
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(indigo);
		Dimension fullscreen = Toolkit.getDefaultToolkit().getScreenSize();
		JScrollPane scrollpane = new JScrollPane(contentPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane.getVerticalScrollBar().setUnitIncrement(16);
		scrollpane.getHorizontalScrollBar().setUnitIncrement(16);
		scrollpane.setPreferredSize(new Dimension((int)fullscreen.getWidth() - 100, (int)fullscreen.getHeight()));
		contentPane.add(Box.createRigidArea(new Dimension(50,0)));
		contentPane.add(scrollpane);
		contentPane.add(Box.createRigidArea(new Dimension(50,0)));
		contentPane.setBackground(indigo);
		previousFormulaFrame.setContentPane(contentPane);
		previousFormulaFrame.setVisible(true);
		revalidate();
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
		buttons.setLayout(new GridLayout((symbols.size() + 2) / (int) Math.sqrt((symbols.size() + 2)),(int) Math.sqrt(symbols.size())));

		// Main panel
		JPanel contentPanel = new JPanel();
		JPanel contentPane = new JPanel();

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
			symbol.setBackground(lightBlue);
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
		backspace.setBackground(lightBlue);
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
		
		JPanel constantPane = new JPanel();
		JButton ok = new JButton("OK");
		JTextArea constant = new JTextArea();
		constant.setLineWrap(true);
		constant.setWrapStyleWord(true);
		ok.setFont(font);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					double d = Double.parseDouble(constant.getText());
					formula.add(new Symbol(Double.toString(d)));
					enteredFormula.setIcon(new ImageIcon(joinBufferedImages(formula)));
					createFormulaFrame.revalidate();
					constant.setText("");
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "A valid number was not entered");
				}
			}
		});
		ok.setBackground(lightBlue);
		
		constantPane.add(constant);
		constantPane.add(ok);
		constantPane.setLayout(new BoxLayout(constantPane,BoxLayout.X_AXIS));
		buttons.add(constantPane);
		// confirm button
		JButton confirm = new JButton("Confirm");
		confirm.setFont(font);
		confirm.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		confirm.setBackground(lightBlue);
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Nothing has been entered yet
				if (formula.size() == 0) return;
				q.setFormula(formula);
				quiz.revalidate();
				createFormulaFrame.dispose();
			}
		});
		
		JButton cancel = new JButton("Cancel");
		cancel.setFont(font);
		cancel.setAlignmentX(JButton.CENTER_ALIGNMENT);
		cancel.setBackground(lightBlue);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFormulaFrame.dispose();
			}
		});
		// add to content pane
		JLabel logo = new JLabel(new ImageIcon("logo.png"));
		contentPane.add(logo);
		contentPanel.add(buttons);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		JScrollPane scroll = new JScrollPane(enteredFormula, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		contentPanel.add(scroll);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		JPanel button = new JPanel();
		button.add(confirm);
		button.add(Box.createRigidArea(new Dimension(50,0)));
		button.add(cancel);
		button.setBackground(indigo);
		contentPanel.add(button);
		contentPanel.add(Box.createRigidArea(new Dimension(0,addingoffset)));
		contentPanel.setBackground(indigo);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPane.add(Box.createRigidArea(new Dimension(50,0)));
		JScrollPane scrollpane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane.getVerticalScrollBar().setUnitIncrement(16);
		Dimension fullscreen = Toolkit.getDefaultToolkit().getScreenSize();
		scrollpane.setPreferredSize(new Dimension((int)fullscreen.getWidth() - 100, (int)fullscreen.getHeight()));
		contentPane.add(scrollpane);
		contentPane.add(Box.createRigidArea(new Dimension(50,0)));
		contentPane.setBackground(indigo);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		createFormulaFrame.setContentPane(contentPane);
		createFormulaFrame.setVisible(true);
	}

	private BufferedImage stringToImage(String S) {
		if (S.endsWith(".0")) { 
			S = S.substring(0, S.length() - 2);
		}
		int height = 0;
		int width = 0;
		SimpleLinkedList<BufferedImage> imageList = new SimpleLinkedList<BufferedImage>();
		for (int i = 0; i < S.length(); i++) {
			BufferedImage image = null;
			if (S.charAt(i) == '-') {
				try {
					image = ImageIO.read(new File("Symbols/Operations/-.png"));
				} catch (IOException e) {
					System.out.println(S.charAt(i));
				}
			} else if (S.charAt(i) == '.') {
				try {
					image = ImageIO.read(new File("Symbols/Operations/decimal.png"));
				} catch (IOException e) {
					System.out.println(S.charAt(i));
				}
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
	}


	// Method to draw buffered images in a jlist
	private class CustomListRenderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setText("");
			label.setBackground(lightBlue);
			label.setIcon(new ImageIcon((BufferedImage) value));
			label.setHorizontalAlignment(JLabel.CENTER);
			return label;
		}
	}
	
}