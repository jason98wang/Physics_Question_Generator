import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class QuizTaker extends JFrame{
	
	QuizTaker(){
		super("Practice Like A Physicist");
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(false);
		this.requestFocusInWindow();
		this.setVisible(true);
	}
	
	private class mainPanel extends JPanel{
		
	}
}
