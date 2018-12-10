/**
 * [StudentInfo].java
 * StudentInfo allows teachers to add and delete students from any specific subject
 * and gets such information from database
 * @author      Yili Liu
 * @since       Nov.20.2018
 */

// IMPORTS
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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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

import data_structures.SimpleLinkedList;

public class StudentInfo {

	// INIT VARIABLES
	// initialize database, list of subjects, and the current subject shown on the UI
	private Database database;
	private SimpleLinkedList<Subject> subjects;
	private Subject curSubject;

	// variables used for the table, drop down menu of subjects and the save button
	private Object[] objs;
	private Object[] col = {"Name", "Student Number", "Password", "%"};
	private DefaultTableModel tableModel;
	private JTable table;
	private JComboBox<String> subjectMenu;
	private JButton save;

	// font and colours for aesthetics
	private Font font = new Font("Serif", Font.BOLD, 30);
	private Color indigo = new Color(56, 53, 74);
	private Color lightBlue = new Color(162, 236, 250);

	// JComponents for adding a student
	private JFrame addFrame;
	private JPanel addPanel;
	private JTextField addNameField;
	private JTextField addStudentNumberField;
	private JTextField addPasswordField;
	private JButton addEnterButton;

	// MAIN METHOD
	public static void main(String[] args) {
		// create new StudentInfo object
		new StudentInfo();
	}

	// CONSTRUCTOR
	/**
	 * Constructor
	 * 
	 */ 
	private StudentInfo() {
		// create new database and get its subjects
		database = new Database();
		subjects = database.getSubjects();

		// make new table of students and drop down menu of subjects
		objs = new Object[4];
		tableModel = new DefaultTableModel(new Object[0][0], col){
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		table = new JTable(tableModel);
		subjectMenu = new JComboBox<String>();

		// make new JComponents for the main page
		JFrame frame = new JFrame("PLAP Student Centre");
		JPanel panel = new JPanel();
		save = new JButton("Save");
		JButton addStudent = new JButton("Add Student");
		JButton deleteStudent = new JButton("Delete Student");
		Border padding = BorderFactory.createEmptyBorder(50, 200, 50, 200);

		// initalize the drop down menu
		initDropDownMenu();

		//Add JButton to table and format table
		table.setRowHeight(30);
		table.setFillsViewportHeight(true);
		table.setOpaque(true);
		table.setBackground(indigo);
		table.setForeground(lightBlue);

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
		panel.setBackground(indigo);

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

	/**
	 * initDropDownMenu
	 * creates drop down menu of subjects for the user to choose from
	 */
	private void initDropDownMenu() {
		// refine aesthetics of menu
		subjectMenu.setFont(font);
		subjectMenu.setPreferredSize(new Dimension(100, 100));
		((JLabel) subjectMenu.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		// add all subjects
		subjectMenu.addItem("Choose a subject");
		for (int i = 0; i < subjects.size(); i++) {
			subjectMenu.addItem(subjects.get(i).getName() + " " + subjects.get(i).getGrade() + " " + subjects.get(i).getLevel());
		}

		// Subject combobox
		subjectMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// take the string of the chosen subject
				String chosenSubject = (String) subjectMenu.getSelectedItem();

				// loop through list of subjects
				int i = 0;
				boolean found = false;
				while (i < subjects.size() && !found) {
					Subject s = subjects.get(i);
					String str = s.getName() + " " + s.getGrade() + " " + s.getLevel();

					// if the chosen subject is found
					if (chosenSubject.equals(str)) {
						// set current subject to s
						curSubject = s;

						// clear the table
						clearTable();

						// fill in the table with student information from chosen subject
						SimpleLinkedList<Student> students = curSubject.getStudents();
						for (int j = 0; j < students.size(); j++) {
							Student st = students.get(j);
							objs[0] = st.getName();
							objs[1] = st.getStudentNumber();
							objs[2] = st.getPassword();
							objs[3] = st.getPercentage();
							tableModel.addRow(objs);
						}
						found = true;
					}
					i++; // increment counter
				}
			}
		});
	}

	/**
	 * delete
	 * find the selected rows to be deleted
	 * and delete said students
	 */
	private void delete() {
		// get selected rows
		int[] rows = table.getSelectedRows();

		// for every row
		for (int i = rows.length - 1; i >= 0; i--) {
			// remove student from the subject and the table
			Student st = curSubject.getStudents().get(rows[i]);
			curSubject.getStudents().remove(st);
			tableModel.removeRow(rows[i]);
		}

		// set save to be visible since a change has been made
		save.setVisible(true);
	}

	/**
	 * clearTable
	 * clear the table
	 */
	private void clearTable() {
		// loop through and clear every row of the table
		while (tableModel.getRowCount() > 0) {
			tableModel.removeRow(0);
		}
	}

	/**
	 * enterStudent
	 * add a student to the current subject
	 */
	private void enterStudent() {
		// take in student information
		objs[0] = addNameField.getText();
		objs[1] = addStudentNumberField.getText();
		objs[2] = addPasswordField.getText();
		objs[3] = -1;
		
		// create new student
		Student st = new Student((String) objs[0], (String) objs[1], (String) objs[2]);
		
		// add student to list of students in current subject and update the table
		tableModel.addRow(objs);
		curSubject.getStudents().add(st);
		
		// set save to be visible since a change has been made
		save.setVisible(true);
	}

	/**
	 * add
	 * creates new window for adding a student to a subject
	 */
	private void add() {
		// instantiate JComponents
		addFrame = new JFrame("Add Student");
		addPanel = new JPanel();
		addNameField = new JTextField("    Name    ");
		addStudentNumberField = new JTextField("Student Number");
		addPasswordField = new JTextField("  Password  ");
		addEnterButton = new JButton("ENTER");

		// format panel
		addPanel.setLayout(new FlowLayout());
		addPanel.setBackground(lightBlue);
		
		// add JComponents to the panel
		addPanel.add(addNameField);
		addPanel.add(addStudentNumberField);
		addPanel.add(addPasswordField);
		addPanel.add(addEnterButton);

		// format frame and add panel
		addFrame.add(addPanel);
		addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addFrame.setSize(500, 100);
		addFrame.setVisible(true);

		// Actionlistener for enter button
		addEnterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// enter student
				enterStudent();
			}
		});

		// focus listeners for all fiels for user friendliness
		// when a user clicks into a blank textfield it becomees blank
		// and when they click away from it text filler shows up
		addNameField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (addNameField.getText().trim().equals("Name")) {
					addNameField.setText("");
				}
			}

			public void focusLost(FocusEvent e) {
				if (addNameField.getText().trim().equals("")) {
					addNameField.setText("    Name    ");
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
					addPasswordField.setText("  Password  ");
				}
			}
		});

		// Keylisteners for text field that have same function as the enter button when the enter key is pressed
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
}