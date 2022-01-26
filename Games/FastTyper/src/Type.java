import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Type {

	private JFrame frame;
	
	public ArrayList<ArrayList<String>> allLevels = new ArrayList<ArrayList<String>>();
	
	public ArrayList<String> level1 = new ArrayList<String>();
	public ArrayList<String> level2 = new ArrayList<String>();
	public ArrayList<String> level3 = new ArrayList<String>();
	public ArrayList<String> level4 = new ArrayList<String>();
	public ArrayList<String> level5 = new ArrayList<String>();
	public ArrayList<String> level6 = new ArrayList<String>();
	public ArrayList<String> level7 = new ArrayList<String>();
	public ArrayList<String> level8 = new ArrayList<String>();
	
	int currentLevel = 0;
	int count = 0;
	int currentScore = 0;
	int streak = 0;
	Timer t;
	int sec = 90;
	String original = "";
	String original2 = "";
	JTextField userInput;
	JButton enterBtn;
	JLabel timer;
	JLabel score;
	JLabel currentWord;
	JButton startBtn;
	ImageIcon play,again;
	JLabel nextWord;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Type window = new Type();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Type() {
		frame = new JFrame("Fast Typer");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame.setBounds(100, 100, 700, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.getRootPane().setDefaultButton(startBtn);

		allLevels.add(level1);
		allLevels.add(level2);
		allLevels.add(level3);
		allLevels.add(level4);
		allLevels.add(level5);
		allLevels.add(level6);
		allLevels.add(level7);
		allLevels.add(level8);
		
		
		for(int i = 0; i < 8; i++) {
			readText("Words/level" + (i+1) + ".txt", allLevels.get(i));
		}
		
		
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{131, 167, 122, 0};
		gbl_panel.rowHeights = new int[]{25, 20, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		timer = new JLabel("Time: " + sec);
		GridBagConstraints gbc_timer = new GridBagConstraints();
		gbc_timer.gridheight = 2;
		gbc_timer.insets = new Insets(0, 0, 5, 5);
		gbc_timer.gridx = 0;
		gbc_timer.gridy = 0;
		panel.add(timer, gbc_timer);
		
		score = new JLabel("Score: " + "\r" + currentScore);
		GridBagConstraints gbc_score = new GridBagConstraints();
		gbc_score.gridheight = 2;
		gbc_score.insets = new Insets(0, 0, 5, 0);
		gbc_score.gridx = 2;
		gbc_score.gridy = 0;
		panel.add(score, gbc_score);
		frame.revalidate();
		frame.repaint();
		currentWord = new JLabel("New label");
		currentWord.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		GridBagConstraints gbc_currentLabel = new GridBagConstraints();
		gbc_currentLabel.anchor = GridBagConstraints.NORTH;
		gbc_currentLabel.insets = new Insets(0, 0, 5, 5);
		gbc_currentLabel.gridx = 1;
		gbc_currentLabel.gridy = 1;
		panel.add(currentWord, gbc_currentLabel);
		
		startBtn = new JButton("Start");
		play = new ImageIcon("Icons/play.png");
		Image img1 = play.getImage();
		Image newPlay = img1.getScaledInstance( 20, 20,  java.awt.Image.SCALE_SMOOTH );  
		play = new ImageIcon(newPlay);
		startBtn.setIcon(play);
		startBtn.setBackground(Color.GREEN);
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			time();
			enterBtn.setEnabled(true);
			startBtn.setEnabled(false);
			userInput.setEnabled(true);
			startBtn.setBackground(null);
			frame.getRootPane().setDefaultButton(enterBtn);
			userInput.requestFocusInWindow();
			}
			
		});
		enterBtn = new JButton("Enter");
		enterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			// check win or show pop up
				String userWord = userInput.getText();
				if(!userWord.equals(original)) {
					sec -= 5;
					timer.setText("Time: " + sec);
					userInput.setText("");
					game();
				}
				else
					if(userWord.contentEquals(original)) {
						updateScore();
						game();
						userInput.setText("");
						userInput.requestFocusInWindow();
						streak++;
						if(streak == 5) {
							streak = 0;
							sec += 5;
							timer.setText("Time: " + sec);
						}
					}
			}
		});
		
		nextWord = new JLabel("New label");
		nextWord.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_nextWord = new GridBagConstraints();
		gbc_nextWord.insets = new Insets(0, 0, 5, 5);
		gbc_nextWord.gridx = 1;
		gbc_nextWord.gridy = 2;
		panel.add(nextWord, gbc_nextWord);
		
		userInput = new JTextField();
		GridBagConstraints gbc_userInput = new GridBagConstraints();
		gbc_userInput.insets = new Insets(0, 0, 5, 5);
		gbc_userInput.gridx = 1;
		gbc_userInput.gridy = 3;
		panel.add(userInput, gbc_userInput);
		userInput.setColumns(10);
		userInput.setEnabled(false);
		GridBagConstraints gbc_enterBtn = new GridBagConstraints();
		gbc_enterBtn.insets = new Insets(0, 0, 5, 5);
		gbc_enterBtn.gridx = 1;
		gbc_enterBtn.gridy = 4;
		panel.add(enterBtn, gbc_enterBtn);
		enterBtn.setEnabled(false);
		frame.getRootPane().setDefaultButton(startBtn);

		GridBagConstraints gbc_startBtn = new GridBagConstraints();
		gbc_startBtn.insets = new Insets(0, 0, 5, 5);
		gbc_startBtn.gridx = 1;
		gbc_startBtn.gridy = 5;
		panel.add(startBtn, gbc_startBtn);
		
		JButton restartBtn = new JButton("Restart");
		again = new ImageIcon("Icons/again.png");
		Image img = again.getImage();
		Image newAgain = img.getScaledInstance( 20, 20,  java.awt.Image.SCALE_SMOOTH );  
		again = new ImageIcon(newAgain);
		restartBtn.setIcon(again);
		restartBtn.setBackground(Color.lightGray);
		restartBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JOptionPane.showMessageDialog(null, "GAME OVER!" + "\n" + "Your Final Score: " + currentScore);
				
				resetGame();
			}
		});
		GridBagConstraints gbc_restartBtn = new GridBagConstraints();
		gbc_restartBtn.insets = new Insets(0, 0, 5, 0);
		gbc_restartBtn.gridx = 2;
		gbc_restartBtn.gridy = 6;
		panel.add(restartBtn, gbc_restartBtn);
		
		firstGame();
	}
	
	public void readText(String path, ArrayList<String> currentArray) {
		try {
			BufferedReader buf = new BufferedReader(new FileReader(path));
		
			String word = buf.readLine();
			
			while(word != null) {
				currentArray.add(word);
				word= buf.readLine();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String selectRandomWord() {
		ArrayList<String> input = allLevels.get(currentLevel);
		
		Random r = new Random();
		int randomNumber = r.nextInt(input.size());
		
		String output = input.get(randomNumber);
		input.remove(randomNumber);
		levelCounter();
		return output;
		
	}
	
	public void firstGame() {
		original = selectRandomWord();
		currentWord.setText(original);	
		original2 = selectRandomWord();
		nextWord.setText(original2);
	}
	public void game() {
		original2 = selectRandomWord();
		original = nextWord.getText();
		currentWord.setText(original);
		nextWord.setText(original2);	
		
		}
	public void levelCounter() {
		count++;
		if(count%5 == 0) {
			count = 0;
			currentLevel++;	}
	}
	public void updateScore() {
		currentScore++;
		score.setText("Score: " + currentScore);
	}
	
	public void resetGame() {
		
		for(int i = 0; i < allLevels.size(); i++) {
			allLevels.get(i).clear();
		}
		
		for(int i = 0; i < 8; i++) {
			readText("Words/level" + (i+1) + ".txt", allLevels.get(i));
		}
		count = 0;
		currentLevel = 0;
		currentScore = 0;
		score.setText("Score: " + currentScore);
		original = "";
		if(sec != 90) {
			t.stop();
		}
		sec = 90;
		timer.setText("Time: " + sec);
		userInput.setEnabled(false);
		startBtn.setEnabled(true);
		startBtn.setBackground(Color.green);
		enterBtn.setEnabled(false);
		frame.getRootPane().setDefaultButton(startBtn);
		frame.repaint();
		frame.revalidate();
		userInput.setText("");
		userInput.requestFocusInWindow();
		firstGame();
		
	}
	public void time() {
		t = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (sec == 0) {
					t.stop();
					JOptionPane.showMessageDialog(null, "GAME OVER!" + "\n" + "Your Final Score: " + currentScore);
					userInput.setEnabled(false);
					enterBtn.setEnabled(false);
					sec = 90;
					resetGame();
				}
				sec--;
				timer.setText("Time: " + sec);
				
			}
			
		});
		t.start();
	}
	
}