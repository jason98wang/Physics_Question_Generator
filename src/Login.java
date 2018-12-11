/**
 * [Login.java]
 * This class displays the login UI of the application
 * @author Josh Cai
 * @since Nov.20.2018
 */

//Java imports
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//jar import
import org.imgscalr.Scalr;

//Data structure import
import data_structures.SimpleLinkedList;

public class Login {
	
	//Init database
	private static Database database;
	
	//Init list of subjects, chosen subject, and current user
	private SimpleLinkedList<Subject> subjects;
	private Subject chosenSubject;
	private Student student;

	//Init logo, font, and colour
	private Font buttonFont;
	private Color indigo;
	private BufferedImage logo;
	
	//Init vars
	private String studentNum;
	private String password;

	//Init java gui components
	private static JFrame window;
	private JPanel title;
	private JPanel mainPanel;
	private JPanel loginPanel;
	private JButton login, exit;
	private JTextField studentNumField;
	private JPasswordField passwordField;
	
	//Main class
	public static void main(String[] args) {
		new Login();
	}

	/**
	 * Login
	 * This method creates a new Login object
	 */
	Login() {
		//Main window
		window = new JFrame();
		window.setSize(Toolkit.getDefaultToolkit().getScreenSize());

		//Create database and get subjects
		database = new Database();
		subjects = getDatabase().getSubjects();

		//Colour and font
		indigo = new Color(56, 53, 74);
		buttonFont = new Font("Arial", Font.BOLD, 30);

		//Logo image
		try {
			logo = ImageIO.read(new File("assets/logo.png"));
			logo = Scalr.resize(logo, (int) (window.getHeight() / 2));
		} catch (IOException e) {
			logo = null;
		}

		//JPanel for logo
		title = new LogoPanel();
		title.setBackground(indigo);
		title.setBorder(BorderFactory.createEmptyBorder(0, 0, window.getHeight() / 2, 0));

		//Login, exit buttons
		login = new JButton("LOGIN");
		login.setFont(buttonFont);
		login.addActionListener(new LoginButtonActionListener());
		login.setAlignmentX(Component.CENTER_ALIGNMENT);
		login.requestFocus();

		exit = new JButton("EXIT");
		exit.setFont(buttonFont);
		exit.addActionListener(new ExitButtonActionListener());
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);

		//Text fields for student # and password
		studentNumField = new JTextField("Student #");
		studentNumField.setFont(buttonFont);
		studentNumField.addFocusListener(new StudentNumFocusListener());
		studentNumField.addKeyListener(new LoginKeyListener());

		passwordField = new JPasswordField("Password");
		passwordField.setFont(buttonFont);
		passwordField.addFocusListener(new PasswordFocusListener());
		passwordField.addKeyListener(new LoginKeyListener());
		passwordField.setEchoChar((char) 0);

		//Panel that contains the text fields
		loginPanel = new JPanel();
		loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));
		loginPanel.setBackground(indigo);
		loginPanel.add(studentNumField);
		loginPanel.add(passwordField);
		loginPanel.setVisible(true);

		//Main panel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(indigo);
		mainPanel.add(title);
		mainPanel.add(loginPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		mainPanel.add(login);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		mainPanel.add(exit);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, window.getHeight() / 5, 0));
		mainPanel.setVisible(true);

		//Window settings and add everything
		window.add(mainPanel, BorderLayout.CENTER);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setUndecorated(true);
		window.requestFocusInWindow();
		window.setVisible(true);
	}
	
	/*
	 * startQuiz
	 * Creates new QuizTaker object if student number and password are valid
	 */
	private void startQuiz() {

		//Get password
		password = "";
		char[] tempPassword = passwordField.getPassword();
		studentNum = studentNumField.getText();
		for (int i = 0; i < tempPassword.length; i++) {
			password += tempPassword[i];
		}

		//Check if student number and password are valid
		for (int i = 0; i < subjects.size(); i++) {
			student = subjects.get(i).getStudent(studentNum, password);
			if (student != null) {
				chosenSubject = subjects.get(i);   //If valid, set chosen subject and create new QuizTaker
				new QuizTaker(student, chosenSubject);
				window.dispose();
				return;
			}
		}
		//If not, show message
		JOptionPane.showMessageDialog(null, "Invalid student number or password");
		passwordField.requestFocus();

	}

	/*
	 * StudentNumFocusListener
	 * A focus listener for the studentNum field
	 * Displays "Student #" when field is empty and vice versa
	 */
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

	/*
	 * PasswordFocusListener
	 * A focus listener for the password field
	 * Displays "Password" when field is empty and vice versa
	 */
	private class PasswordFocusListener implements FocusListener {

		public void focusGained(FocusEvent e) {
			char[] tempPassword = passwordField.getPassword();
			String password = "";

			for (int i = 0; i < tempPassword.length; i++) {
				password += tempPassword[i];
			}

			if (password.equals("Password")) {
				passwordField.setText("");
				passwordField.setEchoChar('*');
			}
		}

		public void focusLost(FocusEvent e) {
			char[] tempPassword = passwordField.getPassword();
			String password = "";

			for (int i = 0; i < tempPassword.length; i++) {
				password += tempPassword[i];
			}

			if (password.equals("")) {
				passwordField.setText("Password");
				passwordField.setEchoChar((char) 0);
			}
		}

	}

	/*
	 * LoginButtonActionListener
	 * Action listener for the login button
	 */
	private class LoginButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			startQuiz();

		}

	}

	/*
	 * ExitButtonActionListener
	 * Action listener for the exit button
	 */
	private class ExitButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			window.dispose();
		}

	}

	/*
	 * LoginKeyListener
	 * Key listener for the login button
	 */
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

	/*
	 * LogoPanel
	 * Custom JPanel that draws the logo and current user
	 */
	private class LogoPanel extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setDoubleBuffered(true);

			g.drawImage(logo, (int) ((window.getWidth() / 2) - (logo.getWidth() / 2)), 0, null);

			repaint();
		}
	}

	/**
	 * getDatabase
	 * returns the database object
	 * @return the database object
	 */
	public static Database getDatabase() {
		return database;
	}

}
