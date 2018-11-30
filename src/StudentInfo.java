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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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

	private String teamName;
	private String teamSeed;
	private ArrayList<String> teamNames = new ArrayList();
	private boolean seed = true;
	private boolean singleBracket = true;
	private boolean doubleBracket = false;
	private boolean validSeed;
	//	private Bracket bracket;
	//	private SingleGenerator singleGenerator;
	//	private DoubleGenerator doubleGenerator;
	//	private Display display;
	private Object[] objs = new Object[3];
	private Object col[] = { "Team", "Seed", "" };
	private DefaultTableModel tableModel = new DefaultTableModel(new Object[0][0], col);
	private JTable table = new JTable(tableModel);

	private int match;
	private int round;
	private int numRounds;
	private int numMatches;
	private String[][] teamsInMatch = new String[2][1];
	private String team1Name;
	private String team2Name;
	
	public static void main(String[] args) {
		new StudentInfo();
	}

	//Constructor 
	public StudentInfo() {
		JFrame firstFrame = new JFrame("Tournament Generator");
		JButton generate = new JButton("Generate");
		JButton addTeam = new JButton("Add Team");
		JButton removeAllTeams = new JButton("Remove All Teams");
		JRadioButton singleButton = new JRadioButton("Single Elimination");
		JRadioButton doubleButton = new JRadioButton("Double Elimination");
		JCheckBox seedButton = new JCheckBox("Seed");
		JLabel tournamentOptionsLabel = new JLabel("Tournament Options");
		JLabel teamOptionsLabel = new JLabel("Team Options");
		JPanel tournamentOptions = new JPanel();
		JPanel teamOptions = new JPanel();
		JPanel mainPanel = new JPanel();
		Border padding = BorderFactory.createEmptyBorder(50, 200, 50, 200);
		ButtonGroup group = new ButtonGroup();

		//Add JButton to table and format table
		table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
		table.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JTextField()));
		table.setRowHeight(30);
		table.setFillsViewportHeight(true);
		table.setOpaque(true);
		table.setBackground(Color.DARK_GRAY);
		table.setForeground(Color.WHITE);

		//Start with seed selected and set colours
		seedButton.setSelected(true);
		seedButton.setBackground(Color.BLACK);
		seedButton.setForeground(Color.WHITE);

		//Start with single elimination selected and set colours
		singleButton.setSelected(true);
		singleButton.setForeground(Color.WHITE);
		singleButton.setBackground(Color.BLACK);

		//Set doubleButton colours
		doubleButton.setBackground(Color.BLACK);
		doubleButton.setForeground(Color.WHITE);

		//Add single and double radio buttons to button group
		group.add(singleButton);
		group.add(doubleButton);

		//Format generate button
		generate.setPreferredSize(new Dimension(100, 50));
		generate.setAlignmentX(Component.CENTER_ALIGNMENT);

		//Format tournamentOptions panel
		tournamentOptions.setLayout(new FlowLayout());
		tournamentOptions.add(singleButton);
		tournamentOptions.add(doubleButton);
		tournamentOptions.add(seedButton);
		tournamentOptions.setBackground(Color.BLACK);
		tournamentOptions.setVisible(true);
		tournamentOptions.setPreferredSize(new Dimension(750, 75));

		//Format teamOptions panel
		teamOptions.setLayout(new FlowLayout());
		teamOptions.add(addTeam);
		teamOptions.add(removeAllTeams);
		teamOptions.setBackground(Color.BLACK);
		teamOptions.setVisible(true);
		teamOptions.setPreferredSize(new Dimension(750, 75));

		//Format tournamentOptionsLabel
		tournamentOptionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		tournamentOptionsLabel.setFont(new Font("Serif", Font.PLAIN, 30));
		tournamentOptionsLabel.setForeground(Color.GREEN);

		//Format teamOptionsLabel
		teamOptionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		teamOptionsLabel.setFont(new Font("Serif", Font.PLAIN, 30));
		teamOptionsLabel.setForeground(Color.GREEN);

		//Format mainPanel
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(tournamentOptionsLabel);
		mainPanel.add(tournamentOptions);
		mainPanel.add(new JScrollPane(table));
		mainPanel.add(teamOptionsLabel);
		mainPanel.add(teamOptions);
		mainPanel.add(generate);
		mainPanel.setBorder(padding);
		mainPanel.setBackground(Color.BLACK);

		//Format main window
		firstFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		firstFrame.setLayout(new BorderLayout());
		firstFrame.add(mainPanel, BorderLayout.CENTER);
		firstFrame.setResizable(false);
		firstFrame.setSize(1200, 850);
		firstFrame.setVisible(true);

		//ActionListener for seed checkbox
		seedButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getItemSelectable() == seedButton) {
					if (e.getStateChange() == ItemEvent.DESELECTED) { //If checkbox is selected, set seed to true; false otherwise
						seed = false;
					} else if (e.getStateChange() == ItemEvent.SELECTED) {
						seed = true;
					}
				}
			}
		});

		//ActionListener for double radio button
		doubleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				singleBracket = false; //set doubleBracket to true, single to false, and disable seed checkbox
				doubleBracket = true;
				seedButton.setSelected(false);
				seedButton.setEnabled(false);
			}
		});

		//ActionListener for single radio button
		singleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				singleBracket = true;
				doubleBracket = false; //Set single to true, double to false, and enable seed checkbox
				seedButton.setEnabled(true);
			}
		});

		//ActionListener for addTeam button
		addTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				add(); //open add team window
			}
		});

		//ActionListener for removeAll button
		removeAllTeams.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeAll(); //call removeAll method
			}
		});

		//ActionListener for generate button
		generate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edit(); //open edit window
			}
		});
	}

	//Window for choosing winners of each match and updating the display
	private void edit() {
		JFrame frame = new JFrame("Update Teams");
		JButton team1 = new JButton(team1Name);
		JButton team2 = new JButton(team2Name);
		JLabel roundMatch = new JLabel("Round 1 Match 1");
		JLabel instructions = new JLabel("Click on winner");
		JPanel buttons = new JPanel();
		JPanel panel = new JPanel();

		//Set sizes of team buttons
		team1.setPreferredSize(new Dimension(100, 50));
		team2.setPreferredSize(new Dimension(100, 50));

		//Format main panel
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(roundMatch);
		panel.add(instructions);
		panel.add(buttons);
		panel.setBackground(Color.BLACK);

		//Format buttons panel
		buttons.setLayout(new FlowLayout());
		buttons.add(team1);
		buttons.add(team2);
		buttons.setBackground(Color.BLACK);
		buttons.setVisible(true);
		buttons.setPreferredSize(new Dimension(750, 75));
		buttons.setAlignmentX(Component.CENTER_ALIGNMENT);

		//Format roundMatch label
		roundMatch.setPreferredSize(new Dimension(750, 25));
		roundMatch.setAlignmentX(Component.CENTER_ALIGNMENT);
		roundMatch.setForeground(Color.GREEN);
		roundMatch.setFont(new Font("Serif", Font.PLAIN, 20));
		roundMatch.setVisible(true);

		//Format instructions label
		instructions.setPreferredSize(new Dimension(750, 25));
		instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
		instructions.setForeground(Color.WHITE);
		instructions.setVisible(true);

		//Format main window
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(500, 150);
		frame.setVisible(true);

		//ActionListener for team1 button
		//		team1.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		//				bracket.setMatchWinner(team1Name, round, match); //Set team1 as the winner of the match
		//
		//				match++; //Next match
		//				if (match > numMatches) { //If match is higher than numMatches in that round, go to next round
		//					round++;
		//					if (bracket.getTournamentWinner() == null) { //If tournament is not done yet
		//						match = 1;
		//						numMatches = bracket.getNumberOfMatchesInRound(round); //Set match back to 1 and get numMatches in that round
		//					} else {
		//						frame.dispose(); //Otherwise, tournament is finished
		//						display.update(bracket);
		//						JOptionPane.showMessageDialog(null, team1Name + " wins the tournament!");
		//					}
		//				}
		//
		//				if (bracket.getTournamentWinner() == null) {
		//					teamsInMatch = bracket.getTeamsInMatch(round, match); //Get teamNames of the teams in this round and match
		//					team1Name = teamsInMatch[0][0];
		//					team2Name = teamsInMatch[1][0];
		//
		//					roundMatch.setText("Round " + round + " Match " + match); //Update roundMatch label and buttons
		//					team1.setText(team1Name);
		//					team2.setText(team2Name);
		//
		//					display.update(bracket); //Update display
		//				}
		//			}
		//		});
		//
		//		//ActionListener for team2 button
		//		//Same logic as actionListener for team1
		//		team2.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent e) {
		//				bracket.setMatchWinner(team2Name, round, match);
		//
		//				match++;
		//				if (match > numMatches) {
		//					round++;
		//					if (bracket.getTournamentWinner() == null) {
		//						match = 1;
		//						numMatches = bracket.getNumberOfMatchesInRound(round);
		//					} else {
		//						frame.dispose();
		//						display.update(bracket);
		//						JOptionPane.showMessageDialog(null, team2Name + " wins the tournament!");
		//					}
		//				}
		//
		//				if (bracket.getTournamentWinner() == null) {
		//					teamsInMatch = bracket.getTeamsInMatch(round, match);
		//					team1Name = teamsInMatch[0][0];
		//					team2Name = teamsInMatch[1][0];
		//
		//					roundMatch.setText("Round " + round + " Match " + match);
		//					team1.setText(team1Name);
		//					team2.setText(team2Name);
		//
		//					display.update(bracket);
		//				}
		//			}
		//		});
	}

	//Window for adding teams into system
	private void add() {

		JFrame frame = new JFrame("Add Teams");
		JPanel panel = new JPanel();
		JTextField nameField = new JTextField("Team name");
		JTextField seedField = new JTextField("Team seed");
		JButton enterButton = new JButton("ENTER");

		//Format panel
		panel.setLayout(new FlowLayout());
		panel.setBackground(Color.BLACK);
		panel.add(nameField);
		panel.add(seedField);
		panel.add(enterButton);

		//Format frame
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(500, 100);
		frame.setVisible(true);

		//Enable or disable seed text field based on if seed checkbox is checked or not
		if (seed) {
			seedField.setEditable(true);
		} else {
			seedField.setEditable(false);
		}

		//Focus listeners for textfields for visual effect
		//Will have "Team/seed name" in field by default
		//When the field is selected, the text will disappear
		//When the field is deselected and the field is empty, text reappears
		nameField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (nameField.getText().trim().equals("Team name")) {
					nameField.setText("");
				}
			}

			public void focusLost(FocusEvent e) {
				if (nameField.getText().trim().equals("")) {
					nameField.setText("Team name");
				}
			}
		});
		seedField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (seedField.getText().trim().equals("Team seed")) {
					seedField.setText("");
				}
			}

			public void focusLost(FocusEvent e) {
				if (seedField.getText().trim().equals("")) {
					seedField.setText("Team seed");
				}
			}
		});

		//Actionlistener for enter button
		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				teamName = nameField.getText(); //Get team name from text field

				nameField.requestFocus(); //Set focus back to nameField for user friendliness
				if (!checkDuplicate(teamName)) { //Check for duplicate team
					if (!teamName.equals("")) { //Check if team name is empty
						if (seed) { //If seed checkbox is selected
							teamSeed = seedField.getText();

							validSeed = true;

							try {
								Integer.parseInt(teamSeed); //Check for valid seed
							} catch (Exception s) {
								validSeed = false;
							}

							if (validSeed) { //If name and seed are valid
								seedField.setText("");
								nameField.setText(""); //Empty the fields

								teamNames.add(teamName); //Add teams to the arraylists

								objs[0] = teamName;
								objs[1] = teamSeed; //Add teams and seeds to table
								objs[2] = "Remove";
								tableModel.addRow(objs);
							} else { //If invalid seed
								JOptionPane.showMessageDialog(null, "Invalid seed");
								seedField.requestFocus();
							}
						} else { //If seed checkbox not selected
							nameField.setText("");

							teamNames.add(teamName);

							objs[0] = teamName;
							objs[1] = "N/A";
							objs[2] = "Remove";
							tableModel.addRow(objs);
						}
					} else { //If team name is empty
						JOptionPane.showMessageDialog(null, "Team name is empty");
					}
				} else { //If duplicate team
					JOptionPane.showMessageDialog(null, "Duplicate Team");
				}
			}
		});

		//Keylisteners for text field that have same function as the enter button when the enter key is pressed
		nameField.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {

			}

			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					teamName = nameField.getText();

					nameField.requestFocus();
					if (!checkDuplicate(teamName)) {
						if (!teamName.equals("")) {
							if (seed) {
								teamSeed = seedField.getText();

								validSeed = true;

								try {
									Integer.parseInt(teamSeed);
								} catch (Exception s) {
									validSeed = false;
								}

								if (validSeed) {
									seedField.setText("");
									nameField.setText("");

									teamNames.add(teamName);

									objs[0] = teamName;
									objs[1] = teamSeed;
									objs[2] = "Remove";
									tableModel.addRow(objs);
								} else {
									JOptionPane.showMessageDialog(null, "Invalid seed");
									seedField.requestFocus();
								}
							} else {
								nameField.setText("");

								teamNames.add(teamName);

								objs[0] = teamName;
								objs[1] = "N/A";
								objs[2] = "Remove";
								tableModel.addRow(objs);
							}
						} else {
							JOptionPane.showMessageDialog(null, "Team name is empty");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Duplicate Team");
					}
				}
			}

			public void keyReleased(KeyEvent e) {

			}
		});
		seedField.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {

			}

			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					teamName = nameField.getText();

					nameField.requestFocus();
					if (!checkDuplicate(teamName)) {
						if (!teamName.equals("")) {
							if (seed) {
								teamSeed = seedField.getText();

								validSeed = true;

								try {
									Integer.parseInt(teamSeed);
								} catch (Exception s) {
									validSeed = false;
								}

								if (validSeed) {
									seedField.setText("");
									nameField.setText("");

									teamNames.add(teamName);

									objs[0] = teamName;
									objs[1] = teamSeed;
									objs[2] = "Remove";
									tableModel.addRow(objs);
								} else {
									JOptionPane.showMessageDialog(null, "Invalid seed");
									seedField.requestFocus();
								}
							} else {
								nameField.setText("");

								teamNames.add(teamName);

								objs[0] = teamName;
								objs[1] = "N/A";
								objs[2] = "Remove";
								tableModel.addRow(objs);
							}
						} else {
							JOptionPane.showMessageDialog(null, "Team name is empty");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Duplicate Team");
					}
				}
			}

			public void keyReleased(KeyEvent e) {

			}
		});
	}

	//Method for removing all teams in system
	private void removeAll() {
		teamNames.clear();

		for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
			tableModel.removeRow(i);
		}
	}

	//Method for removing a specific team from system
	private void removeTeam(int index) {
		teamNames.remove(index);
	}

	//Method for checking for duplicate team names
	private boolean checkDuplicate(String teamName) {

		if (teamNames.contains(teamName)) {
			return true;
		} else {
			return false;
		}

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
					removeTeam(table.getSelectedRow()); //Remove team from arraylists and table
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
			if (clicked) {

			}

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