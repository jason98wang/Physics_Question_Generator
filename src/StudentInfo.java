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
	private Object[] col = {"Name", "Student Number", "Password", ""};
	private DefaultTableModel tableModel;
	private JTable table;

	private Font font = new Font("Serif", Font.BOLD, 30);
	private Font small = new Font("Serif", Font.BOLD, 20);
	private Color indigo = new Color(56, 53, 74);
	private Color lightBlue = new Color(162, 236, 250);
	private Color orange = new Color(232, 167, 55);

	private JComboBox<String> subjectMenu = new JComboBox<String>();

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

		JFrame frame = new JFrame("PLAP Student Centre");
		JButton save = new JButton("Save");
		JButton addStudent = new JButton("Add Student");
		JPanel panel = new JPanel();
		Border padding = BorderFactory.createEmptyBorder(50, 200, 50, 200);
		
		// ButtonGroup group = new ButtonGroup();

		initDropDownMenu();

		//Add JButton to table and format table
		table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
		table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JTextField()));
		table.setRowHeight(30);
		table.setFillsViewportHeight(true);
		table.setOpaque(true);
		table.setBackground(Color.DARK_GRAY);
		table.setForeground(Color.WHITE);

		//Format generate button
		save.setPreferredSize(new Dimension(100, 50));
		save.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		addStudent.setAlignmentX(Component.CENTER_ALIGNMENT);

		//Format panel
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(subjectMenu);
		panel.add(new JScrollPane(table));
		panel.add(addStudent);
		panel.add(save);
		panel.setBorder(padding);
		panel.setBackground(Color.BLACK);

		//Format main window
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.setSize(1200, 850);
		frame.setVisible(true);

		//ActionListener for addTeam button
		addStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				add(); //open add team window
			}
		});

		//ActionListener for generate button
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				database.update();
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
							objs[3] = "Remove";
							tableModel.addRow(objs);
						}
						
						found = true;
					}
					i++;
				}
			}
		});

		subjectMenu.setVisible(true);
	}
	
	private void clearTable() {
		for (int j = 0; j < tableModel.getRowCount(); j++) {
			tableModel.removeRow(0);
		}
	}

	//Window for adding teams into system
	private void add() {
		JFrame frame = new JFrame("Add Student");
		JPanel panel = new JPanel();
		JTextField nameField = new JTextField("    Name    ");
		JTextField studentNumberField = new JTextField("Student Number");
		JTextField passwordField = new JTextField("  Password  ");
		JButton enterButton = new JButton("ENTER");
		
		//Format panel
		panel.setLayout(new FlowLayout());
		panel.setBackground(Color.BLACK);
		panel.add(nameField);
		panel.add(studentNumberField);
		panel.add(passwordField);
		panel.add(enterButton);

		//Format frame
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(500, 100);
		frame.setVisible(true);


		//Actionlistener for enter button
		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				objs[0] = nameField.getText();
				objs[1] = studentNumberField.getText();
				objs[2] = passwordField.getText();
				objs[3] = "Remove";
				tableModel.addRow(objs);
				curSubject.getStudents().add(new Student((String) objs[0], (String) objs[1], (String) objs[2]));
			}
		});

		nameField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (nameField.getText().trim().equals("Name")) {
					nameField.setText("");
				}
			}

			public void focusLost(FocusEvent e) {
				if (nameField.getText().trim().equals("")) {
					nameField.setText("Name");
				}
			}
		});
		studentNumberField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (studentNumberField.getText().trim().equals("Student Number")) {
					studentNumberField.setText("");
				}
			}

			public void focusLost(FocusEvent e) {
				if (studentNumberField.getText().trim().equals("")) {
					studentNumberField.setText("Student Number");
				}
			}
		});
		passwordField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (passwordField.getText().trim().equals("Password")) {
					passwordField.setText("");
				}
			}

			public void focusLost(FocusEvent e) {
				if (passwordField.getText().trim().equals("")) {
					passwordField.setText("Password");
				}
			}
		});

		//Keylisteners for text field that have same function as the enter button when the enter key is pressed
		nameField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					objs[0] = nameField.getText();
					objs[1] = studentNumberField.getText();
					objs[2] = passwordField.getText();
					objs[3] = "Remove";
					tableModel.addRow(objs);
					curSubject.getStudents().add(new Student((String) objs[0], (String) objs[1], (String) objs[2]));
				}
			}
			public void keyReleased(KeyEvent e) {}
		});

		studentNumberField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					objs[0] = nameField.getText();
					objs[1] = studentNumberField.getText();
					objs[2] = passwordField.getText();
					objs[3] = "Remove";
					tableModel.addRow(objs);
					curSubject.getStudents().add(new Student((String) objs[0], (String) objs[1], (String) objs[2]));
				}
			}
			public void keyReleased(KeyEvent e) {}
		});

		passwordField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					objs[0] = nameField.getText();
					objs[1] = studentNumberField.getText();
					objs[2] = passwordField.getText();
					objs[3] = "Remove";
					tableModel.addRow(objs);
					curSubject.getStudents().add(new Student((String) objs[0], (String) objs[1], (String) objs[2]));
				}
			}
			public void keyReleased(KeyEvent e) {}
		});
	}



	//Method for removing a specific team from system
	private void removeStudent(int index) {
	}

	//Method for checking for duplicate team names
	private boolean checkDuplicate(String teamName) {
		return true;
	}

	//Inner classes for adding a JButton to the JTable
	private class ButtonRenderer extends JButton implements TableCellRenderer {

		public ButtonRenderer() {
			setOpaque(true);
			setBackground(Color.LIGHT_GRAY); //Format button colours
			setForeground(Color.BLACK);
		}

		public Component getTableCellRendererComponent(JTable table, Object obj, boolean selected, boolean focused,
				int row, int col) {

			if(obj == null){
				setText("");
			} else {
				setText(obj.toString());
			}

			return this;
		}

	}
	private class ButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean clicked;

		public ButtonEditor(JTextField txt) {
			super(txt);

			button = new JButton();
			button.setOpaque(false);

			//Actionlistener for the remove buttons
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
					removeStudent(table.getSelectedRow()); //Remove team from arraylists and table
					tableModel.removeRow(table.getSelectedRow());
				}
			});
		}

		public Component getTableCellEditorComponent(JTable table, Object obj, boolean selected, int row, int col) {

			if(obj == null){
				label = "";
			} else {
				label = obj.toString();
			}
			button.setText(label); //Set text on button to "Remove"
			clicked = true;

			return button;
		}

		public Object getCellEditorValue() {
			clicked = false;
			return new String(label);
		}

		public boolean stopCellEditing() {
			clicked = false;
			return super.stopCellEditing();
		}

		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
	}
}