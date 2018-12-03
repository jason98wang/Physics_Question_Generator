import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import data_structures.SimpleLinkedList;

/*
 * [ManagementSystem.java]
 * The UI and management system for the tournament generator
 * Author: Josh Cai
 * October 3, 2018
 */

public class StudentInfo {

	private Database database;
	private SimpleLinkedList<Subject> subjects;
	private Subject curSubject;

	private Object[] objs;
	private Object[] col = {"Name", "Student Number", "Password", "%"};
	private DefaultTableModel tableModel;
	private JTable table;

	private Font font = new Font("Serif", Font.BOLD, 30);
	private Font small = new Font("Serif", Font.BOLD, 20);
	private Color indigo = new Color(56, 53, 74);
	private Color lightBlue = new Color(162, 236, 250);
	private Color orange = new Color(232, 167, 55);

	private JComboBox<String> subjectMenu;
	private HashMap<Integer, Student> intToStudent;
	JButton save;

	// JComponents for adding a student
	private JFrame addFrame;
	private JPanel addPanel;
	private JTextField addNameField;
	private JTextField addStudentNumberField;
	private JTextField addPasswordField;
	private JButton addEnterButton;

	public static void main(String[] args) {
		new StudentInfo();
	}

	//Constructor 
	public StudentInfo() {
		database = new Database();
		subjects = database.getSubjects();
		objs = new Object[4];
		tableModel = new DefaultTableModel(new Object[0][0], col){
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		table = new JTable(tableModel);
		subjectMenu = new JComboBox<String>();
		intToStudent = new HashMap<Integer, Student>();

		JFrame frame = new JFrame("PLAP Student Centre");
		JPanel panel = new JPanel();
		save = new JButton("Save");
		JButton addStudent = new JButton("Add Student");
		JButton deleteStudent = new JButton("Delete Student");
		Border padding = BorderFactory.createEmptyBorder(50, 200, 50, 200);

		// ButtonGroup group = new ButtonGroup();

		initDropDownMenu();

		//Add JButton to table and format table
		table.setRowHeight(30);
		table.setFillsViewportHeight(true);
		table.setOpaque(true);
		table.setBackground(Color.DARK_GRAY);
		table.setForeground(Color.WHITE);

		//Format generate button
		save.setPreferredSize(new Dimension(100, 50));
		save.setVisible(false);
		save.setAlignmentX(Component.CENTER_ALIGNMENT);

		addStudent.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteStudent.setAlignmentX(Component.CENTER_ALIGNMENT);

		//Format panel
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(subjectMenu);
		panel.add(new JScrollPane(table));
		panel.add(addStudent);
		panel.add(deleteStudent);
		panel.add(save);
		panel.setBorder(padding);
		panel.setBackground(Color.BLACK);

		//Format main window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.setSize(1200, 850);
		frame.setVisible(true);

		//ActionListener for addStudent button
		addStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				add(); //open add student window
			}
		});

		//ActionListener for deleteStudent button
		deleteStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});

		//ActionListener for generate button
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				database.update();
				save.setVisible(false);
			}
		});
	}

	private void initDropDownMenu() {
		// drop down menu
		subjectMenu.setFont(font);
		subjectMenu.setBackground(lightBlue);
		subjectMenu.setPreferredSize(new Dimension(100, 100));
		((JLabel) subjectMenu.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		// Adding all subjects
		subjectMenu.addItem("Choose a subject");
		for (int i = 0; i < subjects.size(); i++) {
			subjectMenu.addItem(subjects.get(i).getName() + " " + subjects.get(i).getGrade() + " " + subjects.get(i).getLevel());
		}

		// Subject combobox
		subjectMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String chosenSubject = (String) subjectMenu.getSelectedItem();

				String name = chosenSubject.substring(0, chosenSubject.indexOf(" "));
				int grade = Integer.parseInt(chosenSubject.substring(chosenSubject.indexOf(" ") + 1, chosenSubject.lastIndexOf(" ")));
				String level = chosenSubject.substring(chosenSubject.lastIndexOf(" ") + 1);

				int i = 0;
				boolean found = false;
				while (i < subjects.size() && !found) {
					Subject s = subjects.get(i);
					if (s.getName().equals(name) && s.getGrade() == grade && s.getLevel().equals(level)) {
						curSubject = s;
						clearTable();

						SimpleLinkedList<Student> students = s.getStudents();
						for (int j = 0; j < students.size(); j++) {
							Student st = students.get(j);
							objs[0] = st.getName();
							objs[1] = st.getStudentNumber();
							objs[2] = st.getPassword();
							objs[3] = st.getPercentage();
							tableModel.addRow(objs);
							intToStudent.put(j, st);
						}

						found = true;
					}
					i++;
				}
			}
		});

		subjectMenu.setVisible(true);
	}

	private void delete() {
		int[] rows = table.getSelectedRows();

		for (int i = rows.length - 1; i >= 0; i--) {
			Student st = intToStudent.get(rows[i]);
			curSubject.getStudents().remove(st);
			tableModel.removeRow(rows[i]);
			
			for (int j = rows[i]; j < table.getRowCount(); j++) {
				intToStudent.put(j, intToStudent.get(j + 1));
			}
		}

		save.setVisible(true);
	}

	private void clearTable() {
		while (tableModel.getRowCount() > 0) {
			tableModel.removeRow(0);
		}
	}

	private void enterStudent() {
		objs[0] = addNameField.getText();
		objs[1] = addStudentNumberField.getText();
		objs[2] = addPasswordField.getText();
		objs[3] = -1;
		Student st = new Student((String) objs[0], (String) objs[1], (String) objs[2]);
		intToStudent.put(tableModel.getRowCount(), st);
		tableModel.addRow(objs);
		curSubject.getStudents().add(st);
		save.setVisible(true);
	}

	//Window for adding teams into system
	private void add() {
		addFrame = new JFrame("Add Student");
		addPanel = new JPanel();
		addNameField = new JTextField("    Name    ");
		addStudentNumberField = new JTextField("Student Number");
		addPasswordField = new JTextField("  Password  ");
		addEnterButton = new JButton("ENTER");

		//Format panel
		addPanel.setLayout(new FlowLayout());
		addPanel.setBackground(Color.BLACK);
		addPanel.add(addNameField);
		addPanel.add(addStudentNumberField);
		addPanel.add(addPasswordField);
		addPanel.add(addEnterButton);

		//Format frame
		addFrame.add(addPanel);
		addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addFrame.setSize(500, 100);
		addFrame.setVisible(true);


		//Actionlistener for enter button
		addEnterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enterStudent();
			}
		});

		addNameField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (addNameField.getText().trim().equals("Name")) {
					addNameField.setText("");
				}
			}

			public void focusLost(FocusEvent e) {
				if (addNameField.getText().trim().equals("")) {
					addNameField.setText("Name");
				}
			}
		});
		addStudentNumberField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (addStudentNumberField.getText().trim().equals("Student Number")) {
					addStudentNumberField.setText("");
				}
			}

			public void focusLost(FocusEvent e) {
				if (addStudentNumberField.getText().trim().equals("")) {
					addStudentNumberField.setText("Student Number");
				}
			}
		});
		addPasswordField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (addPasswordField.getText().trim().equals("Password")) {
					addPasswordField.setText("");
				}
			}

			public void focusLost(FocusEvent e) {
				if (addPasswordField.getText().trim().equals("")) {
					addPasswordField.setText("Password");
				}
			}
		});

		//Keylisteners for text field that have same function as the enter button when the enter key is pressed
		addNameField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					enterStudent();
				}
			}
			public void keyReleased(KeyEvent e) {}
		});

		addStudentNumberField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					enterStudent();
				}
			}
			public void keyReleased(KeyEvent e) {}
		});

		addPasswordField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					enterStudent();
				}
			}
			public void keyReleased(KeyEvent e) {}
		});
	}

	//Method for checking for duplicate student names
	private boolean checkDuplicate(String name, String studentNumber, String password) {
		for (int i = 0; i < curSubject.getStudents().size(); i++) {
			Student st = curSubject.getStudents().get(i);
			if (st.getName().equals(name) && st.getStudentNumber().equals(studentNumber) && st.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}
}