import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.imgscalr.Scalr;

import data_structures.SimpleLinkedList;

/*
 * To Do
 * Resize
 * Quality of life
 * Look better
 */

public class Login {

	private static Database database;
	private SimpleLinkedList<Student> students;
	private SimpleLinkedList<Subject> subjects;
	private Subject chosenSubject;
	private Student student;

	private Color indigo;
	private BufferedImage logo;
	private String studentNum;
	private String password;

	private static JFrame window;
	private JPanel title;
	private JPanel mainPanel;
	private JButton login;
	private JButton exit;
	private JTextField studentNumField;
	private JPasswordField passwordField;

	Login() {
		window = new JFrame();

		database = new Database();

		subjects = getDatabase().getSubjects();

		indigo = new Color(56, 53, 74);

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

		studentNumField = new JTextField("Student #");
//		studentNumField.setPreferredSize(new Dimension(window.getWidth()/4, window.getHeight()/5));
		studentNumField.addFocusListener(new StudentNumFocusListener());
		studentNumField.addKeyListener(new LoginKeyListener());

		passwordField = new JPasswordField();
		passwordField.addKeyListener(new LoginKeyListener());

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(indigo);
		mainPanel.add(title);
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

	private void startQuiz() {

		boolean valid = true;
		password = "";
		char[] tempPassword = passwordField.getPassword();
		studentNum = studentNumField.getText();

		for (int i = 0; i < tempPassword.length; i++) {
			password += tempPassword[i];
		}

		for (int i = 0; i < subjects.size(); i++) {
			student = subjects.get(i).getStudent(studentNum, password);
			if (student != null) {
				chosenSubject = subjects.get(i);
				new QuizTaker(student, chosenSubject);
				window.dispose();
				return;
			}
		}

		JOptionPane.showMessageDialog(null, "Invalid student number or password");
		passwordField.requestFocus();

	}

	
	////////////////////////////////////////////////////// PRIVATE
	////////////////////////////////////////////////////// CLASSES////////////////////////////////



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

	private class LoginKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				startQuiz();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

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


	public static Database getDatabase() {
		return database;
	}

}
