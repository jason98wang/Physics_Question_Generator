
//Graphics & GUI imports
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

class QuizTakerDisplay extends JFrame {

	// Class variables
	private static JFrame window;
	Image nextPic;
	boolean questionAnswered;
	Button nextButton;

	JButton answer1, answer2, answer3, answer4;

	Font font1 = new Font("Serif", Font.BOLD, 100);
	Font font2 = new Font("Arial", Font.ITALIC, 50);
	Font font3 = new Font("Serif", Font.BOLD, 25);
	int questionNum = 0;
	SimpleLinkedList<String> questions;
	SimpleLinkedList<Double> answers;
	SimpleLinkedList<double[]> choices;

	double[][] wrongAnswer;

	// Main
	public static void main(String[] args) {

		// run the window

		//window = new QuizTakerDisplay(stuff);
	}

	// Constructor
	 QuizTakerDisplay(SimpleLinkedList<String> question, SimpleLinkedList<double[]> choices, SimpleLinkedList<Double> answers) {
		super("Practice Like A Physicist");
		// Set the frame to full screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(false);
		
		// Set up the game panel
		JPanel panel = new JPanel();
		panel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		
//		MouseActionListener mouseListener = new MouseActionListener();
//		this.addMouseListener(mouseListener);

		// Focus the frame
		this.requestFocusInWindow();

		// Make the frame visible
		this.setVisible(true);

		this.questions = question; 
		this.choices = choices;
		this.answers = answers;
		
		if (questions.size() == 0) dispose();
		answer1 = new JButton(Double.toString(choices.get(questionNum)[0]));
		answer2 = new JButton(Double.toString(choices.get(questionNum)[1]));
		answer3 = new JButton(Double.toString(choices.get(questionNum)[2]));
		answer4 = new JButton(Double.toString(choices.get(questionNum)[3]));
		
		answer1.setFont(font3);
		answer2.setFont(font3);
		answer3.setFont(font3);
		answer4.setFont(font3);
//		panel.setLayout(null);
//		answer1.setBounds(200, 600 , 300 , 100);
//		answer2.setBounds(600 , 600 , 300 , 100 );
//		answer3.setBounds(1000 , 600 , 300 , 100);
//		answer4.setBounds(1400 , 600 , 300 , 100);

		answer1.addActionListener(new Answer1Listener());
		answer2.addActionListener(new Answer2Listener());
		answer3.addActionListener(new Answer3Listener());
		answer4.addActionListener(new Answer4Listener());
		
		
		JPanel panel1 = new JPanel();
		panel1.add(answer1);
		panel1.add(Box.createRigidArea(new Dimension(100,0)));
		panel1.add(answer2);
		panel1.add(Box.createRigidArea(new Dimension(100,0)));
		panel1.add(answer3);
		panel1.add(Box.createRigidArea(new Dimension(100,0)));
		panel1.add(answer4);
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		JButton next = new JButton();
		try {
			next = new JButton(new ImageIcon(ImageIO.read(new File("NextButton.png"))));
		} catch (Exception ex) {}
		JLabel questionLabel = new JLabel(questions.get(questionNum));
		
		questionLabel.setFont(font1);
		JLabel label = new JLabel("Question #" + Integer.toString(questionNum + 1));
		label.setFont(font2);
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				questionNum++;
				if (questionNum == questions.size()) dispose();
				questionLabel.setText(questions.get(questionNum));
				label.setText("Question #" + Integer.toString(questionNum + 1));
				answer1.setText(Double.toString(choices.get(questionNum)[0]));
				answer2.setText(Double.toString(choices.get(questionNum)[1]));
				answer3.setText(Double.toString(choices.get(questionNum)[2]));
				answer4.setText(Double.toString(choices.get(questionNum)[3]));
				revalidate();
				
			}
		});
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(Box.createRigidArea(new Dimension(0,100)));
		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(0,100)));
		questionLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(questionLabel);
		panel.add(Box.createRigidArea(new Dimension(0,100)));
		panel1.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		panel.add(panel1);
		panel.add(Box.createRigidArea(new Dimension(0,100)));
		JPanel flowPanel = new JPanel();
		flowPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
	
		
		next.setAlignmentX(JButton.LEFT_ALIGNMENT);
		flowPanel.add(next);
		panel.add(flowPanel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.setContentPane(panel);
		
	
	} // End of constructor

	// ********* INNER CLASSES *********
//
//	private class GameAreaPanel extends JPanel {
//
//		// Declare variables
//
//		GameAreaPanel() {
//			nextPic = Toolkit.getDefaultToolkit().getImage("NextButton.png");
//
//			nextButton = new Button(1750, 905, 115, 115);
//			
////			questions.add("1 + 1");
////			questions.add("2 + 2");
////			questions.add("2 + 3");
////			questions.add("4 + 5");
////
////			wrongAnswer = new double[4][questions.size()];
////
////			answers.add(2.0);
////			answers.add(4.0);
////			answers.add(5.0);
////			answers.add(9.0);
////
////			wrongAnswer[0][0] = 2;
////			wrongAnswer[0][1] = 3;
////			wrongAnswer[0][2] = 1;
////			wrongAnswer[0][3] = 5;
//
//		}
//
//		public void paintComponent(Graphics g) {
//
//			// Call the super class
//			super.paintComponent(g);
//			setDoubleBuffered(true);
//
//			questionAnswered = false;
//
//			g.setFont(font1);
//			int displayNum = questionNum + 1;
//			g.drawString("Question #" + displayNum, 700, 90);
//
//			g.drawImage(nextPic, 1750, 905, null, this);
//
//			g.setFont(font2);
//			g.drawString(questions.get(questionNum), 900, 300);
//
//			answer1.setText(Double.toString(choices.get(questionNum)[0]));
//			answer2.setText(Double.toString(choices.get(questionNum)[1]));
//			answer3.setText(Double.toString(choices.get(questionNum)[2]));
//			answer4.setText(Double.toString(choices.get(questionNum)[3]));
//
//			// Repaint
//			repaint();
//
//		} // End of paintComponent
//	}// End of GameAreaPanel
//
//	public class MouseActionListener implements MouseListener {
//
//		@Override
//		public void mouseClicked(MouseEvent e) {
//
////			System.out.println("Mouse Clicked");
////			System.out.println("X:" + e.getX() + " y:" + e.getY());
//			Rectangle boundingBox;
//			boundingBox = new Rectangle(e.getX() - 50, e.getY() - 50, 100, 100);
//
//			if (boundingBox.intersects(nextButton.boundingBox)) {
//				System.out.println("CLiked");
//				questionNum++;
//				answer1.setBackground(null);
//				answer2.setBackground(null);
//				answer3.setBackground(null);
//				answer4.setBackground(null);
//			}
//
//		}
//
//		@Override
//		public void mouseEntered(MouseEvent arg0) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void mouseExited(MouseEvent arg0) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void mousePressed(MouseEvent arg0) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void mouseReleased(MouseEvent arg0) {
//			// TODO Auto-generated method stub
//
//		}
//
//	}

	private class Answer1Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (choices.get(questionNum)[0] == answers.get(questionNum)) {
				System.out.println("yay");
				answer1.setBackground(Color.GREEN);
			} else {
				answer1.setBackground(Color.RED);
			}

		}
	}

	private class Answer2Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (choices.get(questionNum)[1] == answers.get(questionNum)) {
				System.out.println("yay");
				answer2.setBackground(Color.GREEN);
			} else {
				answer2.setBackground(Color.RED);
			}

		}
	}

	private class Answer3Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (choices.get(questionNum)[2] == answers.get(questionNum)) {
				System.out.println("yay");
				answer3.setBackground(Color.GREEN);
			} else {
				answer3.setBackground(Color.RED);
			}

		}
	}

	private class Answer4Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (choices.get(questionNum)[3] == answers.get(questionNum)) {
				System.out.println("yay");
				answer4.setBackground(Color.GREEN);
			} else {
				answer4.setBackground(Color.RED);
			}

		}
	}

}
