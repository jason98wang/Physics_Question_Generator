import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class StudentInfo {

	private Database database;
	private SimpleLinkedList<Subject> subjects;
	private SimpleLinkedList<Student> students;
	private Subject chosenSubject;
	private Student chosenStudent;

	private int style = 0;
	private JFrame window;
	private JLabel title;
	private JPanel mainPanel;
	private JPanel optionsPanel;
	private JButton start;
	private JButton exit;
	private JComboBox<String> subject, student;
	private JTable table;
	private String[] columnNames;
	private Object[][] data;

	public static void main(String[] args) {
		new StudentInfo();
	}

	StudentInfo() {
		window = new JFrame("PLAP Student Info");

		database = new Database();

		subjects = database.getSubjects();

		title = new JLabel("PRACTICE LIKE A PHYSICIST");
		title.setFont(new Font("Serif", Font.PLAIN, 30));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);

		start = new JButton("START");
		start.addActionListener(new ToggleButtonActionListener());
		start.setAlignmentX(Component.CENTER_ALIGNMENT);

		exit = new JButton("EXIT");
		exit.addActionListener(new ExitButtonActionListener());
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);

		//		numQuestionsField = new JTextField("# of Questions");
		//		numQuestionsField.addFocusListener(new NumQuestionsFocusListener());
		//		numQuestionsField.setAlignmentY(Component.CENTER_ALIGNMENT);

		student = new JComboBox<String>();
		student.addActionListener(new StudentActionListener());

		subject = new JComboBox<String>();
		subject.addActionListener(new SubjectActionListener());
		addSubjects();

		optionsPanel = new JPanel();
		optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));
		optionsPanel.add(subject);
		optionsPanel.add(student);
		optionsPanel.setVisible(true);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(title);
		mainPanel.add(optionsPanel);
		mainPanel.add(start);
		mainPanel.add(exit);
		mainPanel.setBorder(BorderFactory.createEmptyBorder((int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 10), 0,
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 10), 0));
		mainPanel.setVisible(true);

		table = new JTable();
		String[] columns = {"Name", "Student Number", "Password", "Incorrect", "Total"};
		data = new Object[10][5];
		mainPanel.add(table);

		window.add(mainPanel, BorderLayout.CENTER);

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		window.setResizable(false);
		window.requestFocusInWindow();
		window.setVisible(true);
	}

	public class SimpleTable implements TableModelListener {
		public SimpleTable() {
			table.getModel().addTableModelListener(this);
		}

		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
			int column = e.getColumn();
			TableModel model = (TableModel)e.getSource();
			String columnName = model.getColumnName(column);
			Object data = model.getValueAt(row, column);
			
		}
	}

	////////////////////////////////////////////////////// PRIVATE
	////////////////////////////////////////////////////// CLASSES////////////////////////////////

	private void addSubjects() {
		for (int i = 0; i < subjects.size(); i++) {
			subject.addItem(subjects.get(i).getName() + " " + subjects.get(i).getGrade() + " " + subjects.get(i).getLevel());
		}
	}

	private void addStudents() {
		students = chosenSubject.getStudents();
		if (student != null) {
			student.removeAllItems();
		}

		for (int i = 0; i < students.size(); i++) {
			student.addItem(students.get(i).getName());
		}

	}

	private class SubjectActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String subjectName = (String) subject.getSelectedItem();

			for (int i = 0; i < subjects.size(); i++) {
				if (subjectName.equals(subjects.get(i).getName() + " " + subjects.get(i).getGrade() + " " + subjects.get(i).getLevel())) {
					chosenSubject = subjects.get(i);
				}
			}

			addStudents();
		}

	}

	private class StudentActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (student.getSelectedItem() != null) {
				String unitName = (String) student.getSelectedItem();

				for (int i = 0; i < students.size(); i++) {
					if (unitName.equals(students.get(i).getName())) {
						chosenStudent = students.get(i);
					}
				}
			}
		}

	}

	private class ToggleButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			style++;
			if (style > 2) {
				style -= 3;
			}

		}

	}

	private class ExitButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			window.dispose();
		}
	}
}
