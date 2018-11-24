
// Imports
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
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



// Main JFrame
public class QuizEditor extends JFrame {
	
	
	
	private Subject s;
	private Unit u;
	private Question q, editQuestion;
	private boolean addQ, removeQ, editQ, newFormula, editingQ;
	private JList<String> list = new JList<String>(new DefaultListModel<String>());
	private static QuizEditor quiz;

	
	
	// QuizEditor(Database d) {
	QuizEditor(SimpleLinkedList<Subject> subjects, SimpleLinkedList<Symbol> symbols, SimpleLinkedList<SimpleLinkedList<Symbol>> formulas) {
		
		this.setTitle("Quiz Editor");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(true);
		this.requestFocus(true);
		JButton add = new JButton("Add");
		JButton remove = new JButton("Remove");
		JButton edit = new JButton("Edit");
		JButton addSubject = new JButton("Add new subject");
		JButton addUnit = new JButton("Add new unit");
		JButton removeSubject = new JButton("Remove current subject");
		JButton removeUnit = new JButton("Remove current unit");
		JButton confirm = new JButton("Confirm");
		JPanel contentPane = new JPanel();
		JPanel leftPane = new JPanel();
		JPanel midPane = new JPanel();
		JPanel rightPane = new JPanel();
		JPanel displayPane = new JPanel();
		JComboBox<String> subject = new JComboBox<String>();
		JComboBox<String> unit = new JComboBox<String>();
		JComboBox<String> formula = new JComboBox<String>();
		JTextArea enter = new JTextArea();
		
		
		enter.setLineWrap(true);
		enter.setWrapStyleWord(true);
		JLabel problemStatement = new JLabel("Enter problem statement below");
		addQ = false;
		removeQ = false;
		editQ = false;
		editingQ = false;
		s = null;
		u = null;
		q = null;
		// SimpleLinkedList<Subject> subjects = d.getSubjects();

		
		subject.addItem("Choose a subject");
		unit.addItem("Choose a unit");
		for (int i = 0; i < subjects.size(); i++) {
			subject.addItem(subjects.get(i).getName());
		}

		
		
		subject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String chosenSubject = (String) subject.getSelectedItem();
				if (!chosenSubject.equals("Choose a subject")) {
					s = subjects.get(subject.getSelectedIndex() - 1);
					// Priority queue
					SimpleLinkedList<Unit> units = s.getUnits();
					for (int i = 0; i < units.size(); i++) {
						unit.addItem(units.get(i).getName());
					}
				}
				list.setModel(new DefaultListModel<String>());
			}
		});
		
		
		
		unit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String chosenUnit = (String) unit.getSelectedItem();
				list.setModel(new DefaultListModel<String>());

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
		
		
		
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!addQ) {
					addQ = true;
					removeQ = false;
					editQ = false;
					editingQ = false;
					displayPane.removeAll();
					subject.setVisible(true);
					unit.setVisible(true);
					if (formula.getItemCount() == 0) {
						formula.addItem("Choose a formula");
						formula.addItem("Create new formula");
						formula.addItem("Use previous formula");
					}
					JPanel displayFormula = new JPanel();
					JLabel currentFormula = new JLabel("Current Formula");
					JLabel currentFormulaDisplay = new JLabel();

					formula.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String chosenFormula = (String) formula.getSelectedItem();
							q = new Question("", new SimpleLinkedList<Symbol>());
							if (chosenFormula.equals("Create new formula")) {
								newFormula = true;
								createFormula(symbols, currentFormulaDisplay);

							} else {
								newFormula = false;
								previousFormula(formulas, q, currentFormulaDisplay);
							}
						}
					});
					displayPane.add(formula);

					displayFormula.add(currentFormula);
					displayFormula.add(currentFormulaDisplay);
					displayPane.add(displayFormula);
					displayPane.add(problemStatement);
					enter.setEditable(true);
					displayPane.add(enter);
				}
				revalidate();
			}
		});
		
		
		
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!removeQ) {
					removeQ = true;
					addQ = false;
					editQ = false;
					editingQ = false;
					displayPane.removeAll();
					list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					subject.setVisible(true);
					unit.setVisible(true);
					JScrollPane scroll = new JScrollPane(list);
					displayPane.add(scroll);
				}
				revalidate();
			}
		});
		
		
		
		edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!editQ) {
					editQ = true;
					addQ = false;
					removeQ = false;
					editingQ = false;
					list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					subject.setVisible(true);
					unit.setVisible(true);
					JScrollPane scroll = new JScrollPane(list);
					displayPane.add(scroll);
				}
			}
		});
		
		
		
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!addQ && !removeQ && !editQ && !editingQ) {
					JOptionPane.showMessageDialog(null, "Add, remove or edit has not been selected");
					return;
				}
				if (s == null) {
					JOptionPane.showMessageDialog(null, "A valid subject has not been selected");
					return;
				}
				if (u == null) {
					JOptionPane.showMessageDialog(null, "A valid unit has not been selected");
					return;
				}
				if (q == null && !removeQ) {
					JOptionPane.showMessageDialog(null, "A valid question has not been selected");
					return;
				}
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

				} else if (addQ) {
					q.setProblemStatement(enter.getText());
					// d.addQuestion(s,u,q);
					DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
					model.addElement(q.getProblemStatement());
					if (newFormula) formulas.add(q.getFormula());
					// Remove Later 
					u.addQuestion(q);
					q = null;
					// Editing questions
				} else if (editQ) {
					editQ = false;
					editingQ = true;
					editQuestion = u.getQuestions().get((list.getSelectedIndex()));
					displayPane.removeAll();
					subject.setVisible(true);
					unit.setVisible(true);
					if (formula.getItemCount() == 0) {
						formula.addItem("Choose a formula");
						formula.addItem("Create new formula");
						formula.addItem("Use previous formula");
					}
					JPanel displayFormula = new JPanel();
					JLabel currentFormula = new JLabel("Current Formula");
					JLabel currentFormulaDisplay = new JLabel();
					currentFormulaDisplay.setIcon(new ImageIcon(joinBufferedImages(editQuestion.getFormula())));
					enter.setText(editQuestion.getProblemStatement());
					formula.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String chosenFormula = (String) formula.getSelectedItem();
							q = new Question(null, null);
							if (chosenFormula.equals("Create new formula")) {
								newFormula = true;
								createFormula(symbols, currentFormulaDisplay);

							} else {
								newFormula = false;
								previousFormula(formulas, q, currentFormulaDisplay);
							}
						}
					});
					displayPane.add(formula);
					displayFormula.add(currentFormula);
					displayFormula.add(currentFormulaDisplay);
					displayPane.add(displayFormula);
					displayPane.add(problemStatement);
					enter.setEditable(true);
					displayPane.add(enter);
				} else {
					editQuestion.setProblemStatement(enter.getText());
					editQuestion.setFormula(q.getFormula());
				}
			}
		});
		
		
		
		addSubject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSubject();
				// AddSubject a = new Addsubject(d)
				subjects.add(sbjct);
				// d.addSubject(sbjct);
			}
		});
		
		
		
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
			}
		});
		
		
		
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
			}
		});
		
		
		
		JPanel buttons = new JPanel();
		buttons.add(add);
		buttons.add(remove);
		midPane.add(buttons);
		midPane.add(subject);
		midPane.add(unit);
		displayPane.setLayout(new BoxLayout(displayPane, BoxLayout.Y_AXIS));
		midPane.add(displayPane);
		midPane.add(confirm);
		midPane.setLayout(new BoxLayout(midPane, BoxLayout.Y_AXIS));
		contentPane.add(leftPane);
		contentPane.add(midPane);
		contentPane.add(rightPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		this.setContentPane(contentPane);
		this.setVisible(true);
	}

	
	
	// AddSubject(Database d) {
	private Subject sbjct;
	private void addSubject() {
		JFrame addSubjectFrame = new JFrame("Add Subject");
		addSubjectFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel contentPanel = new JPanel();
		JLabel enterName = new JLabel("Enter the  name of the subject");
		JLabel enterGrade = new JLabel("Enter the grade of the subject");
		JLabel enterLevel = new JLabel("Enter the level of the subject");
		JTextArea name = new JTextArea();
		JTextArea grade = new JTextArea();
		JTextArea level = new JTextArea();
		JButton addUnit = new JButton("Add a unit");
		SimpleLinkedList<Unit> unitlist = sbjct.getUnits();
		DefaultListModel<String> model = new DefaultListModel<String>();
		JList<String> units = new JList<String>(model);;
		addUnit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addUnit(unitlist, model);
			}
		});
		JButton confirm = new JButton("Confirm");
		
		
		
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sbjct = new Subject(name.getText(),Integer.parseInt(grade.getText()),level.getText());
					addSubjectFrame.dispose();
					return;
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "A valid grade has not been selected");
					return;
				}
			}
		});
		
		
		
		contentPanel.add(enterName);
		contentPanel.add(name);
		contentPanel.add(enterGrade);
		contentPanel.add(grade);
		contentPanel.add(enterLevel);
		contentPanel.add(level);
		contentPanel.add(addUnit);
		contentPanel.add(confirm);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		// this.setPreferredSize(preferredSize);
		addSubjectFrame.setContentPane(contentPanel);
		addSubjectFrame.setVisible(true);
	}

	
	
	private void addUnit(SimpleLinkedList<Unit> unitlist, DefaultListModel<String> model) {
		JFrame addUnitFrame = new JFrame("Add Unit");
		addUnitFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(200, 500));
		JPanel contentPanel = new JPanel();
		JLabel enterName = new JLabel("Enter unit name");
		JLabel enterNum = new JLabel("Enter unit number");
		JTextArea name = new JTextArea();
		JTextArea num = new JTextArea();
		JButton confirm = new JButton("Confirm");
		
		
		
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					unitlist.add(new Unit(name.getText(), Integer.parseInt(num.getText())));
					if (model != null) {
						model.addElement(name.getText());
					}
					addUnitFrame.dispose();
					return;
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "A valid number has not been selected");
				}
			}
		});
		
		
		
		contentPanel.add(enterName);
		contentPanel.add(name);
		contentPanel.add(enterNum);
		contentPanel.add(num);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		addUnitFrame.setVisible(true);
	}

	private BufferedImage joinBufferedImages(SimpleLinkedList<Symbol> formula) {
		int height = 0;
		int width = 0;
		for (int i = 0; i < formula.size(); i++) {
			width += formula.get(i).getImage().getWidth();
			height = Math.max(formula.get(i).getImage().getHeight(), height);
		}
		if (height == 0 && width == 0)
			return null;
		BufferedImage connectedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = connectedImage.createGraphics();
		int sum = 0;
		for (int i = 0; i < formula.size(); i++) {
			g.drawImage(formula.get(i).getImage(), sum, 0, null);
			sum += formula.get(i).getImage().getWidth();
		}
		g.dispose();
		return connectedImage;
	}

	
	
	public static void main(String[] args) {
		try {
			SimpleLinkedList<Subject> subjects = new SimpleLinkedList<Subject>();
			Subject s = new Subject("HI",11,"S");
			s.addUnit(new Unit("a", 1));
			s.addUnit(new Unit("b", 2));
			s.addUnit(new Unit("c", 3));
			s.addUnit(new Unit("d", 4));
			s.addUnit(new Unit("e", 5));
			subjects.add(s);
			Symbol add = new Symbol("add", ImageIO.read(new File("Symbols/Operations/Add.png")));
			Symbol equal = new Symbol("equal", ImageIO.read(new File("Symbols/Operations/equal.png")));
			Symbol pi = new Symbol("pi", ImageIO.read(new File("Symbols/Variables/Pi.png")));
			Symbol time = new Symbol("time", ImageIO.read(new File("Symbols/Variables/time.png")));
			Symbol velocity = new Symbol("velocity", ImageIO.read(new File("Symbols/Variables/velocity.png")));
			SimpleLinkedList<Symbol> symbols = new SimpleLinkedList<Symbol>();
			symbols.add(add);
			symbols.add(equal);
			symbols.add(pi);
			symbols.add(time);
			symbols.add(velocity);
			SimpleLinkedList<SimpleLinkedList<Symbol>> formulas = new SimpleLinkedList<SimpleLinkedList<Symbol>>();
			SimpleLinkedList<Symbol> tmp = new SimpleLinkedList<Symbol>();
			tmp.add(time);
			tmp.add(add);
			tmp.add(velocity);
			tmp.add(equal);
			tmp.add(pi);
			formulas.add(tmp);
			quiz = new QuizEditor(subjects, symbols, formulas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	private void previousFormula(SimpleLinkedList<SimpleLinkedList<Symbol>> formulas, Question q, JLabel formulaDisplay) {
		JFrame previousFormulaFrame = new JFrame("Choose Previous Formula");
		previousFormulaFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		previousFormulaFrame.requestFocus();
		previousFormulaFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		DefaultListModel<BufferedImage> model = new DefaultListModel<BufferedImage>();
		JList<BufferedImage> list = new JList<BufferedImage>(model);
		JPanel contentPanel = new JPanel();
		for (int i = 0; i < formulas.size(); i++) {
			model.addElement(joinBufferedImages(formulas.get(i)));
		}
		list.setCellRenderer(new CustomListRenderer());
		JScrollPane scroll = new JScrollPane(list);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentPanel.add(scroll);
		JButton confirm = new JButton("Confirm formula");
		
		
		
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
		
		
		
		contentPanel.add(confirm);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		previousFormulaFrame.setContentPane(contentPanel);
		previousFormulaFrame.setVisible(true);
	}

	
	
	
	// ChooseFormula (Database d) {
	private JLabel enteredFormula;

	private void createFormula(SimpleLinkedList<Symbol> symbols, JLabel formulaDisplay) {
		JFrame createFormulaFrame = new JFrame("Create new formula");
		createFormulaFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createFormulaFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		createFormulaFrame.requestFocus();
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout((symbols.size() + 1) / (int) Math.sqrt((symbols.size() + 1)),(int) Math.sqrt(symbols.size())));
		JPanel contentPanel = new JPanel();
		SimpleLinkedList<Symbol> formula = new SimpleLinkedList<Symbol>();
		// SimpleLinkedList<Symbol> symbols = d.getSymbols();
		enteredFormula = new JLabel();
		enteredFormula.setHorizontalAlignment(JLabel.CENTER);
		SimpleLinkedList<JButton> buttonlist = new SimpleLinkedList<JButton>();
	
		for (int i = 0; i < symbols.size(); i++) {
			JButton symbol = new JButton(new ImageIcon(symbols.get(i).getImage()));
			symbol.setBackground(Color.WHITE);
			symbol.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					formula.add(symbols.get(buttonlist.indexOf(symbol)));
					enteredFormula.setIcon(new ImageIcon(joinBufferedImages(formula)));
					revalidate();
				}
			});
			buttonlist.add(symbol);
			buttons.add(symbol);
		}
		JButton backspace = new JButton("Del");
		backspace.setBackground(Color.WHITE);
		
		
		
		backspace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formula.remove(formula.size() - 1);
				if (formula.size() > 0) {
					enteredFormula.setIcon(new ImageIcon(joinBufferedImages(formula)));
				} else {
					enteredFormula = new JLabel();
				}
				revalidate();
				revalidate();
			}
		});
		
		
		
		buttons.add(backspace);
		contentPanel.add(buttons);
		contentPanel.add(enteredFormula);
		JButton confirm = new JButton("Confirm");
		
		
		
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				q.setFormula(formula);
				formulaDisplay.setIcon(new ImageIcon(joinBufferedImages(formula)));
				quiz.revalidate();
				createFormulaFrame.dispose();
				return;
			}
		});
		
		
		
		contentPanel.add(confirm);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		createFormulaFrame.setContentPane(contentPanel);
		createFormulaFrame.setVisible(true);
	}

	
	
	
	private class CustomListRenderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setText("");
			label.setIcon(new ImageIcon((BufferedImage) value));
			label.setHorizontalAlignment(JLabel.CENTER);
			return label;
		}
	}
}
