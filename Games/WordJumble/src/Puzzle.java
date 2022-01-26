import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Puzzle {

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
	int skipNumber = 3;
	Timer t;
	int sec = 60;
	String original = "";
	private JTextField userInput;
	JButton skipBtn;
	JButton enterBtn;
	JLabel timer;
	JLabel score;
	JLabel currentLabel;
	private JButton startBtn;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Puzzle window = new Puzzle();
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
	public Puzzle() {
		frame = new JFrame("Word Jumble");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame.setBounds(100, 100, 450, 300);
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
			readText("Word Jumble/level" + (i+1) + ".txt", allLevels.get(i));
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
		currentLabel = new JLabel("New label");
		currentLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		GridBagConstraints gbc_currentLabel = new GridBagConstraints();
		gbc_currentLabel.anchor = GridBagConstraints.NORTH;
		gbc_currentLabel.insets = new Insets(0, 0, 5, 5);
		gbc_currentLabel.gridx = 1;
		gbc_currentLabel.gridy = 1;
		panel.add(currentLabel, gbc_currentLabel);
		
		JButton resetBtn = new JButton("Reset");
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
			for(int i = 0; i < allLevels.size(); i++) {
				allLevels.get(i).clear();
			}
			
			for(int i = 0; i < 8; i++) {
				readText("Word Jumble/level" + (i+1) + ".txt", allLevels.get(i));
			}
			count = 0;
			currentLevel = 0;
			currentScore = 0;
			original = "";
			skipNumber = 3;
			skipBtn.setText("Skip " + skipNumber);
			if(sec != 60) {
				t.stop();
			}
			sec = 60;
			timer.setText("Time: " + sec);
			userInput.setEnabled(true);
			skipBtn.setEnabled(true);
			startBtn.setEnabled(true);
			enterBtn.setEnabled(false);
			frame.getRootPane().setDefaultButton(startBtn);
			frame.repaint();
			frame.revalidate();
			userInput.requestFocusInWindow();
			playGame();
			
			
			}
		});
		
		userInput = new JTextField();
		GridBagConstraints gbc_userInput = new GridBagConstraints();
		gbc_userInput.insets = new Insets(0, 0, 5, 5);
		gbc_userInput.gridx = 1;
		gbc_userInput.gridy = 2;
		panel.add(userInput, gbc_userInput);
		userInput.setColumns(10);
		userInput.requestFocusInWindow();
		enterBtn = new JButton("Enter");
		enterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			// check win or show pop up
				if(userInput.getText().contentEquals(original)) {
					updateScore();
					playGame();
					userInput.setText("");
					levelCounter();
					userInput.requestFocusInWindow();
				}
			}
		});
		GridBagConstraints gbc_enterBtn = new GridBagConstraints();
		gbc_enterBtn.insets = new Insets(0, 0, 5, 5);
		gbc_enterBtn.gridx = 1;
		gbc_enterBtn.gridy = 3;
		panel.add(enterBtn, gbc_enterBtn);
		enterBtn.setEnabled(false);
		
		startBtn = new JButton("Start");
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			time();
			enterBtn.setEnabled(true);
			startBtn.setEnabled(false);
			frame.getRootPane().setDefaultButton(enterBtn);
			userInput.requestFocusInWindow();

			}
			
		});
		frame.getRootPane().setDefaultButton(startBtn);

		GridBagConstraints gbc_startBtn = new GridBagConstraints();
		gbc_startBtn.insets = new Insets(0, 0, 5, 5);
		gbc_startBtn.gridx = 1;
		gbc_startBtn.gridy = 5;
		panel.add(startBtn, gbc_startBtn);
		GridBagConstraints gbc_resetBtn = new GridBagConstraints();
		gbc_resetBtn.insets = new Insets(0, 0, 0, 5);
		gbc_resetBtn.gridx = 0;
		gbc_resetBtn.gridy = 7;
		panel.add(resetBtn, gbc_resetBtn);
		
		skipBtn = new JButton("Skip Word");
		skipBtn.setText("Skip " + skipNumber);
		skipBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			if(skipNumber != 0) {
				playGame();
				skipNumber--;
				skipBtn.setText("Skip " + skipNumber);
				levelCounter();
				userInput.requestFocusInWindow();
				frame.getRootPane().setDefaultButton(enterBtn);

			}}
		});
		GridBagConstraints gbc_skipBtn = new GridBagConstraints();
		gbc_skipBtn.gridx = 2;
		gbc_skipBtn.gridy = 7;
		panel.add(skipBtn, gbc_skipBtn);
		

		playGame();
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
		return output;
		
	}
	
	public String jumble(String original) {
		String jumbledWord = original;
		int wordSize = original.length();
		int jumbleCount = 10;
		while(jumbleCount>0) {
			jumbleCount--;
			int index1 = ThreadLocalRandom.current().nextInt(0,wordSize);
			int index2 = ThreadLocalRandom.current().nextInt(0,wordSize);
			jumbledWord = swapIndex(jumbledWord, index1, index2);
		}
		return jumbledWord;
	}
	public String swapIndex(String word, int index1, int index2) {
		char[] charArray = word.toCharArray();
		char temp = charArray[index1];
		charArray[index1] = charArray[index2];
		charArray[index2] = temp;
		
		return new String(charArray);
	} 
	
	public void playGame() {
		original = selectRandomWord();
		String jumbledWord = jumble(original);
		currentLabel.setText(jumbledWord);	
		
		}
	public void levelCounter() {
		count++;
		if(count%9 == 0) {
			count = 0;
			currentLevel++;	}
	}
	public void updateScore() {
		currentScore++;
		score.setText("Score: " + "\n" + currentScore);
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
					skipBtn.setEnabled(false);
					sec = 60;
				}
				sec--;
				timer.setText("Time: " + sec);
				
			}
			
		});
		t.start();
	}
	
}