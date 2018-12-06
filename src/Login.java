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
 * Login UI
 * Main UI better
 * Preset questions
 */

public class Login {

	private Database database;
	private SimpleLinkedList<Student> students;
	private SimpleLinkedList<Subject> subjects;
	private Subject chosenSubject;

	private Color indigo, lightBlue;
	private static Random rand;
	private BufferedImage logo;
	private String studentNum;
	private String password;

	private static JFrame window;
	private JPanel title;
	private JPanel mainPanel;
	private JButton login;
	private JButton exit;
	private JComboBox<String> subject;
	private JTextField studentNumField;
	private JTextField passwordField;

	Login() {
		window = new JFrame();

		database = new Database();

		subjects = database.getSubjects();

		indigo = new Color(56, 53, 74);
		lightBlue = new Color(162, 236, 250);

		////////////////////////////////////////////////// GUI
		////////////////////////////////////////////////// STUFF/////////////////////////////////////

		window.setSize(Toolkit.getDefaultToolkit().getScreenSize());

		try {
			logo = ImageIO.read(new File("logo.png"));
			logo = Scalr.resize(logo, (int) (window.getHeight() / 2));
		} catch (IOException e) {
			logo = null;
		}

		title = new LogoPanel();
		title.setBackground(indigo);
		title.setBorder(BorderFactory.createEmptyBorder(0, 0, window.getHeight() / 2, 0));

		login = new JButton("LOGIN");
		login.addActionListener(new LoginButtonActionListener());
		login.setAlignmentX(Component.CENTER_ALIGNMENT);

		exit = new JButton("EXIT");
		exit.addActionListener(new ExitButtonActionListener());
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);

		subject = new JComboBox<String>();
		subject.addActionListener(new SubjectActionListener());
		addSubjects();

		studentNumField = new JTextField("Student #");
//		studentNumField.setPreferredSize(new Dimension(window.getWidth()/4, window.getHeight()/5));
		studentNumField.addFocusListener(new StudentNumFocusListener());

		passwordField = new JTextField("Password");
		passwordField.addFocusListener(new PasswordFocusListener());

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(indigo);
		mainPanel.add(title);
		mainPanel.add(subject);
		mainPanel.add(studentNumField);
		mainPanel.add(passwordField);
		mainPanel.add(login);
		mainPanel.add(exit);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, window.getHeight() / 5, 0));
		mainPanel.setVisible(true);

		window.add(mainPanel, BorderLayout.CENTER);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setUndecorated(true);
		window.requestFocusInWindow();
		window.setVisible(true);
	}

	private void addSubjects() {
		for (int i = 0; i < subjects.size(); i++) {
			subject.addItem(
					subjects.get(i).getName() + " " + subjects.get(i).getGrade() + " " + subjects.get(i).getLevel());
		}
	}

	private void startQuiz() {

		boolean valid = true;
		studentNum = studentNumField.getText();
		password = passwordField.getText();
		students = chosenSubject.getStudents();
		
		
		
		if (valid) {
			new QuizTaker();
			window.dispose();
		} else {
			JOptionPane.showMessageDialog(null, "Invalid student number or password");
		}

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
		}

	}

	private class StudentNumFocusListener implements FocusListener {

		public void focusGained(FocusEvent e) {
			if (studentNumField.getText().trim().equals("Student #")) {
				studentNumField.setText("");
			}
		}

		public void focusLost(FocusEvent e) {
			if (studentNumField.getText().trim().equals("")) {
				studentNumField.setText("Student #");
			}
		}

	}

	private class PasswordFocusListener implements FocusListener {

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

	}

	private class LoginButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			startQuiz();

		}

	}

	private class ExitButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			window.dispose();
		}

	}

	private class LogoPanel extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setDoubleBuffered(true);

			g.drawImage(logo, (int) ((window.getWidth() / 2) - (logo.getWidth() / 2)), 0, null);

			repaint();
		}
	}
}
