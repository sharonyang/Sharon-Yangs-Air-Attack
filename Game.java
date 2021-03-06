// Sharon Yang
// April 23, 2012
// Game.java
// This game is an airplane game that allows user to fly in the sky and is allowed to shoot down or dodge obstructions.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.net.URL;

// Class variables (more detailed information see below):
/*
readingQuestions: this is calling the class that contains a Scanner to read the pop-up questions in the "Academic Questions.txt"
questionAsked: this JTextArea contains the question in the pop-up question window.
selectLevel: this is when there a few variables that defines user level and allows user to select level.
original: this tells the computer whether the game screen is shown.
selectChange: this tells the computer whether a level selection has been done.
timeDisplayString: this displays time of each level 
nextLevelCount: this helps the computer to keep track of time left in each level (>1 min)
randomQuestion: this variable determines the random selection of questions when user dies.
dying: this means the "dying question" window has not popped up yet.
rockCount and rockSpeed: these default the speed of the rocks and number of rocks produced to level 1
death: this indicates whether user is alive or dead
fontSize: this is default (or user-selected) font size.
lives: this defaults user's lives.
drawingArea: this calls the graphics class
gameStarted: this indicates whether one game has started or not.
instructionList: this string will contain the instruction of the program.
userLevel: this is user's default level.
frame: this creats the JFrame that contains the entire program.
overallPanel: this is the overall panel that contains all the sub-JPanels.
welcomeWindow: this contains the welcome page (that says Air Attack)
OPchangePage: this is the cardLayout that contains all pages of the game.
actualGame: this is the actual game page JPanel that will contain graphics page.
readingInstruction: this is a class reading the instruction text file.
chooseLevel: this creates the combo box that allows user to select level up to level 5.
cloud, star, and grass: radio buttons that set up the background page.
pause: a boolean that determines whether user has clicked on the screen to pause.
*/

// Classes and methods:
/*	
constructor: this area contains classLoaders and imageURLs that allow the program to use pictures contain in the same folder.
static method: this calls a few methods to set up the basic structure of the game pages.
setWelcomePage: this method sets up the welcome page (first page user sees)
setInstructionPage: this sets up the "Instruction" page of the game.
setSettingsPage: this sets up the "Settings" page of the game.
		
class gamePage: this contains the actual game page.	
	class gameGraphics: this is the graphics class of the game.
	class levelChanging: this class allows the computer to take account whether 60 sec has been reached
	class repaintBullet: this allows the computer to repaint bullet to show that they're moving forward.
	class repaintRocks: this allows the computer to move the rocks forward.
	class verifyShot: this verifies whether a rock has been hit by a bullet.
	paintComponent: this is the paintComponent method that calls most of the methods of this class.
	printBackground: this prints out the picture background of the game page.
	userMoves: this prints out user's airplane and his/her location.
		-printBullet: this prints out the bullets that are shot and still in screen
		-shooting: this allows the computer to advance the movement of the bullets (moving forward)
		-newShot: this allows the user to create a new shot of bullet.
		-rocks: this randomnizes the rocks' locations and movements.
		-rockAccelerate: this moves existing rocks forward.
		-printRocks: this prints out pictures of rocks according to locations.
		-hit: this verifies whether a bullet hits a rock.
		-die: if user contacts the rocks, some actions are initiated (either die completely, or death questions...)
		-startingOver: this method is called whenever user is starting over.
		-newGame: this sets a new condition (for any level)
		-changeLevel: this allows the changing level => more difficulty by changing rocks' speed and count		timeCount  this accounts for the 60 sec limit for each level of the game.
		-levelDisplay: this displays the level of the current situation.
		-selectingLevel: this allows user, when selecting a level through setting, experience a change in difficulty.		
		-class questionPanel: this is the death question Panel that pops up whenever user is about to lose life.
			--class answering: this allows the countdown for the answering (10 sec)
	keyPressed : this is where user uses up and down keys to move and space to shoot
	actionPerformed: this sets up the function of the buttons on the main page.
	stateChanged: this is the scroll bar's action that allows the user to select font size.
class ReadData: this class creates scanner that reads text file that contains the instruction.
class ReadQuestions: this class uses scanner to read a text file that contains the academic questions and answers.
*/

public class Game implements ActionListener, ChangeListener{
	private JFrame frame;
	private JPanel welcomeWindow, overallPanel, instruction, settingPage;
	private JTextArea instructionArea, questionAsked;
	private JComboBox chooseLevel;
	private JButton startGame, goBackSetting, goBackInstruction;
	private JSlider fontSizeChange;
	private JRadioButton cloud, star, grass;
	private CardLayout OPchangePage;
	private gamePage actualGame;
	private String instructionList, timeDisplayString;
	private String[][] questionArray;
	private ReadData readingInstruction;
	private ReadQuestions readingQuestions;
	private int userLevel, nextLevelCount, rockCount, rockSpeed, randomQuestion, yLocation, lives, fontSize;
	private boolean gameStarted, dying, original, death, selectChange, pause;
	private gameGraphics drawingArea;
	private Timer timerBullet, timerRocks, timerVerifyShot, levelChange;
	private Image backgroundPic, rocksPicture, rocksPic, rocksPic2, planePic, cloudPic, starPic, grassPic, bulletPic;
	private Color descriptColor;
	
	public Game(){
		// this area contains classLoaders and imageURLs that allow the program to use pictures contain in the same folder.
		ClassLoader c1 = Game.class.getClassLoader();
		URL imageURL1 = c1.getResource("Rocks.png"); // picture of the rocks.
		rocksPic = Toolkit.getDefaultToolkit().createImage(imageURL1);
		
		ClassLoader c2 = Game.class.getClassLoader();
		URL imageURL2 = c2.getResource("Plane.png"); // picture of user's plane.
		planePic = Toolkit.getDefaultToolkit().createImage(imageURL2);
		
		ClassLoader c3 = Game.class.getClassLoader();
		URL imageURL3 = c3.getResource("cloudBackground.png"); // picture of the "Clouds" background.
		cloudPic = Toolkit.getDefaultToolkit().createImage(imageURL3);
		
		ClassLoader c4 = Game.class.getClassLoader();
		URL imageURL4 = c4.getResource("starBackground.png"); // picture of the "Starry night" background.
		starPic = Toolkit.getDefaultToolkit().createImage(imageURL4);

		ClassLoader c5 = Game.class.getClassLoader();
		URL imageURL5 = c5.getResource("grassBackground.png"); // picture of the "Greens" or grass background.
		grassPic = Toolkit.getDefaultToolkit().createImage(imageURL5);
		
		ClassLoader c6 = Game.class.getClassLoader();
		URL imageURL6 = c6.getResource("bulletPic.png"); // picture of the bullet.
		bulletPic = Toolkit.getDefaultToolkit().createImage(imageURL6);
		
		ClassLoader c7 = Game.class.getClassLoader();
		URL imageURL7 = c7.getResource("rockPic2.png"); // picture of the rocks (different color when in stars mode).
		rocksPic2 = Toolkit.getDefaultToolkit().createImage(imageURL7);
		

		backgroundPic = cloudPic; // this default the background picture to "Clouds"
		rocksPicture = rocksPic; // default the color of the rocks to dirt color.
		// end of section for image downloading.
		
		// here is an array that contains all the questions in the pop-up window.
		questionArray = new String[3][40];
		for (int j = 0; j<3; j ++)
			for (int i = 0; i<40; i ++)
				questionArray[j][i] = "";
				
		// this is calling the class that contains a Scanner to read the pop-up questions in the "Academic Questions.txt"
		readingQuestions = new ReadQuestions();
		
		// this JTextArea contains the question in the pop-up question window.
		questionAsked = new JTextArea(1,10);
		
		// this is when there a few variables that defines user level and allows user to select level.
		original = false; // this tells the computer whether the game screen is shown.
		selectChange = false; // this tells the computer whether a level selection has been done.
		timeDisplayString = "Time left = 01:00"; // this displays time of each level 
		nextLevelCount = -1; // this helps the computer to keep track of time left in each level (>1 min)
		randomQuestion=0; // this variable determines the random selection of questions when user dies.
		
		dying = false; // this means the "dying question" window has not popped up yet.
		// these default the speed of the rocks and number of rocks produced to level 1
		rockCount =700; 
		rockSpeed = 5;
		pause = false;
		
		death = false; 	// this indicates whether user is alive or dead
		fontSize = 18; // this is default (or user-selected) font size.
		lives = 3; // this defaults user's lives.
		drawingArea = new gameGraphics(); // this calls the graphics class
		gameStarted = false; // this indicates whether one game has started or not.
		instructionList = ""; // this string will contain the instruction of the program.
		userLevel = 1; // this is user's default level.
		
		frame = new JFrame("Air Attack by Sharon Yang"); // this creats the JFrame that contains the entire program.
		overallPanel = new JPanel(); // this is the overall panel that contains all the sub-JPanels.
		welcomeWindow = new JPanel(); // this contains the welcome page (that says Air Attack)
		OPchangePage = new CardLayout(); // this is the cardLayout that contains all pages of the game.
		actualGame = new gamePage(); // this is the actual game page JPanel that will contain graphics page.
		readingInstruction = new ReadData(); // this is a class reading the instruction text file.
		
		chooseLevel = new JComboBox(); // this creates the combo box that allows user to select level up to level 5.
		frame.getContentPane().setLayout(new BorderLayout()); // this is setting the frame's layout.
		frame.getContentPane().add(BorderLayout.CENTER, overallPanel); // this is putting the overall panel on the JFrame.
		
		// this is creating the radio buttons that set up the background page.
		cloud = new JRadioButton("Clouds"); 
		star = new JRadioButton("Star");
		grass = new JRadioButton("Greens");
		
		// MANDATORY: required for JFrame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // make program quit as soon as close window
		frame.setSize(600,600); // give the frame a size in pixels
		frame.setVisible(true);
		frame.setResizable(false); // this makes user unable to readjust the size of the JFrame.
		
		overallPanel.setLayout(OPchangePage); // this sets the overall Panel's layout to cardLayout.
		welcomeWindow.setLayout(new GridLayout(6,1)); // this sets up the welcome window's layout.
		Color welcomeColor = new Color (109,207,238); // this creates the background color for the welcome window.
		overallPanel.add(welcomeWindow, "panel 1"); // this is adding the welcome window as the first page of the overall JPanel
		overallPanel.add(actualGame, "panel 2"); // the actual game is located at the second layer of the overall JPanel
		welcomeWindow.setBackground(welcomeColor);
	}
	
	public static void main(String [] args){ // this calls a few methods to set up the basic structure of the game pages.
		Game start = new Game();
		start.setWelcomePage();
		start.setInstructionPage();
		start.setSettingsPage();
	}
	
	public void setWelcomePage(){ // this method sets up the welcome page (first page user sees)
		Font titleFont = new Font ( "Helvetica", Font.BOLD, 30 );
		
		// this labels the title of the welcome page.
		JLabel welcomeTitle = new JLabel ("Welcome to Air Attack", JLabel.CENTER);
		welcomeTitle.setFont(titleFont);
		welcomeTitle.setForeground(Color.white);
		
		// this creates buttons that link to all the other pages of the game.
		instruction = new JPanel();
		instruction.setLayout(new BorderLayout());
		instruction.setBackground(new Color(114, 236, 180));
		
		settingPage = new JPanel();
		settingPage.setLayout(new BorderLayout());
		settingPage.setBackground(new Color(255,187,225));
		
		overallPanel.add(instruction, "panel 3");
		overallPanel.add(settingPage, "panel 4");
		
		welcomeWindow.add(welcomeTitle);
		
		// this initiates the function of those buttons to 'flip' to certain JPanel pages when clicked.
		startGame = new JButton("Start Game");
		startGame.addActionListener(this);
		welcomeWindow.add(startGame);
		
		JButton viewInstruction = new JButton("View Instruction");
		viewInstruction.addActionListener(this);
		welcomeWindow.add(viewInstruction);
		
		JButton settings = new JButton("Settings");
		settings.addActionListener(this);
		welcomeWindow.add(settings);
		
		JButton endGame = new JButton("End Air Attack");
		endGame.addActionListener(this);
		welcomeWindow.add(endGame);
		
		Font authorFont = new Font ( "Helvetica", Font.BOLD, fontSize );
		JLabel authorTitle = new JLabel ("Created by Sharon Yang", JLabel.CENTER);
		authorTitle.setForeground(Color.white);
		authorTitle.setFont(authorFont);
		welcomeWindow.add(authorTitle);
	}
	
	public void setInstructionPage(){
		// this sets up the "Instruction" page of the game.
		goBackInstruction = new JButton("Return to Menu");
		goBackInstruction.addActionListener(this);
		instruction.add(BorderLayout.SOUTH, goBackInstruction);
		Font instructionFont = new Font ( "Helvetica", Font.BOLD, fontSize );
		instructionArea = new JTextArea(20,20);
		
		// this creates a JTextArea that contains the instruction that is saved in a .txt file
		instructionArea.setText(instructionList);
		instructionArea.setFont(instructionFont);
		instructionArea.setEditable(false);
		// this allows the JTextArea to have scroller on the side.
		instructionArea.setLineWrap(true);
		JScrollPane scroller = new JScrollPane(instructionArea);
		instructionArea.setMargin( new Insets(7,7,7,7) );
		instruction.add(BorderLayout.CENTER, scroller);
		
		// this labels the JPanel that contains the instruction.
		JLabel instructionTitle = new JLabel("INSTRUCTION OF AIR ATTACK", JLabel.CENTER);
		Font instructionTitleFont = new Font ( "Helvetica", Font.BOLD, 30 );
		instructionTitle.setFont(instructionTitleFont);
		instruction.add(BorderLayout.NORTH, instructionTitle);
	}
	
	public void setSettingsPage(){
		// this sets up the "Settings" page of the game.
		// there is a return to menu button on the settings page.
		goBackSetting = new JButton("Return to Menu");
		goBackSetting.addActionListener(this);
		settingPage.add(BorderLayout.SOUTH, goBackSetting);
		JPanel settingContent = new JPanel();
		settingContent.setBackground( new Color(255,255,180));
		settingPage.add(BorderLayout.CENTER, settingContent);
		settingContent.setLayout(new GridLayout(5,1));
		
		// this creates the title of the settings page.
		Font settingTitleFont = new Font ( "Helvetica", Font.BOLD, 30 );
		JLabel settingTitle = new JLabel ("SETTINGS FOR AIR ATTACK", JLabel.CENTER);
		settingTitle.setFont(settingTitleFont);
		settingContent.add(settingTitle);
		
		// this creates a combo box for the settings page.
		chooseLevel.addItem("-------");
		chooseLevel.addItem("Level 1");
		chooseLevel.addItem("Level 2");
		chooseLevel.addItem("Level 3");
		chooseLevel.addItem("Level 4");
		chooseLevel.addItem("Level 5");
		
		// when a certain level is clicked, action is initialized to transport the user to that level.
		chooseLevel.addActionListener(this);
		JPanel chooseLevelPane = new JPanel();
		chooseLevelPane.setLayout(new FlowLayout());
		chooseLevelPane.setBackground(new Color(255,255,180));
		JLabel chooseLevelLabel = new JLabel("Choose a Level (Default is Level 1):  ");
		Font CLTitleFont = new Font ( "Helvetica", Font.BOLD, 20 );
		chooseLevelLabel.setFont(CLTitleFont);
		chooseLevelPane.add(chooseLevelLabel);
		chooseLevelPane.add(chooseLevel);
		settingContent.add(chooseLevelPane);
		
		
		JPanel backgroundChangePane = new JPanel();
		
		// this sets up Radio Buttons that allow the user to select his/her favorite theme/background.
		backgroundChangePane.setLayout(new FlowLayout());
		backgroundChangePane.setBackground(new Color(255,255,180));

		
		// this describes the function of the radio buttons.
		JLabel backgroundChangeLabel = new JLabel ("Select the Background of the Game (Default is Clouds):          ");
		Font BCLTitleFont = new Font ( "Helvetica", Font.BOLD, fontSize );
		backgroundChangeLabel.setFont(BCLTitleFont);
		backgroundChangePane.add(backgroundChangeLabel);
		
		// the buttons are created corresponding with the themes.
		ButtonGroup buttons123 = new ButtonGroup();
		buttons123.add(cloud);
		buttons123.add(star);
		buttons123.add(grass);
		backgroundChangePane.add(cloud);
		backgroundChangePane.add(star);
		backgroundChangePane.add(grass);
		cloud.setSelected(true);
		cloud.setBackground(new Color(255,255,180));
		star.setBackground(new Color(255,255,180));
		grass.setBackground(new Color(255,255,180));
		cloud.addActionListener(this);
		star.addActionListener(this);
		grass.addActionListener(this);
		settingContent.add(backgroundChangePane);
		
		
		JPanel setFontSizePane = new JPanel();
		setFontSizePane.setBackground(new Color(255,255,180));
		setFontSizePane.setLayout(new FlowLayout());
		
		// user can also select the font size of the description of the game.
		JLabel fontChangeLabel = new JLabel ("Select the Size of the Font (Default is 18): ");
		Font FCTitleFont = new Font ( "Helvetica", Font.BOLD, 16 );
		fontChangeLabel.setFont(FCTitleFont);
		fontSizeChange = new JSlider (10, 20, 18);
		
		// a slider is used to allow the user to drag to certain number for font size.
		fontSizeChange.setBackground(new Color(255,255,180));
		fontSizeChange.setMajorTickSpacing(1);
		fontSizeChange.setPaintTicks(true);
		fontSizeChange.setLabelTable(fontSizeChange.createStandardLabels(1));
		fontSizeChange.setPaintLabels(true);
		fontSizeChange.addChangeListener(this);
		
		setFontSizePane.add(fontChangeLabel);
		setFontSizePane.add(fontSizeChange);
		settingContent.add(setFontSizePane);
	}
	
	class gamePage extends JPanel implements ActionListener{ // this contains the actual game page.
		public gamePage (){
			setBackground(Color.black); // the background (which is never really shown) is set to black.
			setLayout(new BorderLayout());
			
			// these are buttons on the bottom of the screen that allow the user to go to other pages during the game.
			JButton goMenu = new JButton ("Main Screen");
			JButton goInstruction = new JButton ("Instruction");
			JButton goSetting = new JButton ("Settings");
			JButton goExit = new JButton ("Quit");
			
			JPanel gamePanel = new JPanel();
			
			// this is the background of the game page (which is never really shown, since it's covered with pictures.)
			gamePanel.setBackground(Color.gray);
			gamePanel.setLayout(new FlowLayout());
			gamePanel.add(goMenu);
			gamePanel.add(goInstruction);
			gamePanel.add(goSetting);
			gamePanel.add(goExit);
			goMenu.addActionListener(this);
			goInstruction.addActionListener(this);
			goSetting.addActionListener(this);
			goExit.addActionListener(this);
			this.add(BorderLayout.SOUTH, gamePanel);
			this.add(BorderLayout.CENTER, drawingArea);
		}
		
		public void actionPerformed (ActionEvent evt){ // if the buttons on the bottom are clicked...
			String command = evt.getActionCommand();
			
			if (command.equals("Main Screen")){ // this brings the user back to main menu during the game.
				original = false;
				OPchangePage.show(overallPanel, "panel 1");
				if (timerBullet.isRunning()){ // meanwhile pausing the game for the user so he/she won't die.
					timerBullet.stop();
					timerRocks.stop();
					timerVerifyShot.stop();
					levelChange.stop();
				}
			}
			
			else if (command.equals("Quit")) // if the 'quit' button is clicked, the game ends.
				System.exit(0);
			
			else if (command.equals("Instruction")){ // if the instruction button is clicked, user is brought to that page
									// with his/her game paused so he/she won't die.
				original = false;
				OPchangePage.show(overallPanel, "panel 3");
				if (timerBullet.isRunning()){	
					timerBullet.stop();
					timerRocks.stop();
					timerVerifyShot.stop();
					levelChange.stop();
				}	
			}
			
			else if (command.equals("Settings")){ // user is brought to that page with his/her game paused so he/she won't die.
				original = false;
				OPchangePage.show(overallPanel, "panel 4");
				if (timerBullet.isRunning()){	
					timerBullet.stop();
					timerRocks.stop();
					timerVerifyShot.stop();
					levelChange.stop();
				}
			}
		}
	
	} // end class gamePage
	
	class gameGraphics extends JPanel implements KeyListener, MouseListener{ // this is the graphics class of the game.
		private JRadioButton choiceA, choiceB, choiceC, choiceD;
		private int[][] bullet, rocks;
		private int bulletCount, seconds11;
		private questionPanel dieQuestion;
		private Timer answerTime;
		private JLabel timeCount, rightOrWrong;
		private char answer;
		public gameGraphics (){
			descriptColor= Color.black; // this is the color description on the sides of the screen.
			rightOrWrong = new JLabel("", JLabel.CENTER); // this tells the user whether they get the "death" questions right.
			answer='a'; // this first default the correct answer to a--it will later be filled with right answers.
			answering answerEnds = new answering(); // this calls the answering class that ends the 'death' question when 10 sec is reached.
			answerTime = new Timer (1000, answerEnds); // this timer allows the computer to terminate the death question when time is reached.
			seconds11 = 20; // this sets the time to answer a question to 20 seconds.
			timeCount = new JLabel("Time left: "+seconds11+" seconds", JLabel.CENTER); // this tells the user how much time is left.
			dieQuestion = new questionPanel(); // this is the JPanel that contains the death question.
			setLayout(new BorderLayout()); // this sets up the layout of the game page.
			this.add(BorderLayout.CENTER,dieQuestion);
			dieQuestion.setVisible(false); // the death question JPanel is first set to invisible, only visible when user dies.
			bulletCount = 50; // this defaults the number of bullets user can have.
			addKeyListener(this);
			addMouseListener(this);
			yLocation = 240;
			
			// these are timers that allow user to shoot bullets, print rocks, verify effective shots, and change levels when 60 sec reached.
			// during different time intervals.
			repaintBullet bulletAction = new repaintBullet();
			timerBullet = new Timer(100, bulletAction);
			
			repaintRocks rockAction = new repaintRocks();
			timerRocks = new Timer (rockCount, rockAction);
			
			verifyShot shootAction = new verifyShot();
			timerVerifyShot = new Timer (rockSpeed, shootAction);
			
			levelChanging levelAction = new levelChanging();
			levelChange = new Timer (1000, levelAction);
			
			// this defaults the bullets and rocks location.
			bullet = new int [2][50];
			for (int i = 0; i < 50; i++)
				for (int j = 0; j  < 2; j++)
					bullet[j][i] = 0;
			
			rocks = new int [2][1000];
			for (int i = 0; i < 1000; i++)
				for (int j = 0; j  < 2; j++)
					rocks[j][i] = 0;
		}
		
		class levelChanging implements ActionListener{ // this class allows the computer to take account whether 60 sec has been reached
			public void actionPerformed(ActionEvent e){
				nextLevelCount = nextLevelCount+1;
				drawingArea.timeCount(); 
				drawingArea.repaint();
			}
		}
		
		class repaintBullet implements ActionListener{ // this allows the computer to repaint bullet to show that they're moving forward.
			public void actionPerformed(ActionEvent e){
				drawingArea.shooting(); 
				drawingArea.repaint();
			}
		}
		
		class repaintRocks implements ActionListener{ // this allows the computer to move the rocks forward.
			public void actionPerformed(ActionEvent e){
				drawingArea.rocks(); 
				drawingArea.repaint();
			}
		}
		
		class verifyShot implements ActionListener{ // this verifies whether a rock has been hit by a bullet.
			public void actionPerformed(ActionEvent e){
				drawingArea.rockAccelerate();
				drawingArea.hit(); 
				drawingArea.die(); 
				drawingArea.repaint();
			}
		}
		
		public void paintComponent(Graphics g) { // this is the paintComponent method that calls most of the methods of this class.
			super.paintComponent(g);
			printBackground(g); // this prints out the background according to default or user's liking
			if (selectChange){ // selectingLevel is only called when user changes level by selecting levels in "Settings"
				selectingLevel();
				selectChange = false;
			}
			levelDisplay(g); // this displays the user's level and situation (lives).
			userMoves(g); // this prints out user's location and movement.
			printRocks(g); // this prints out rocks' locations and movements.
			printBullet(g); // this prints out bullets that are shot.
			hit(); // this allows the bullets to eliminate the rocks when hit.
			ifDiedThen(g); // if user dies when contacting with a rock, death questions are initiated.
		}
		
		public void printBackground(Graphics g){ // this prints out the picture background of the game page.
			g.drawImage (backgroundPic,0,0, this);
		}

		public void userMoves(Graphics g) { // this prints out user's airplane and his/her location.
			g.drawImage(planePic, 20, yLocation-20,this);
			this.requestFocus();
		}
		
		public void printBullet(Graphics g){ // this prints out the bullets that are shot and still in screen
			Font bulletFont = new Font ( "Helvetica", Font.BOLD, fontSize ); // This makes the font for the description.
			g.setFont ( bulletFont );
			g.setColor(descriptColor);
			g.drawString("You have "+bulletCount+" bullets left.", 320,530); // this tells the user how many bullets he/she has left
			if (lives>1)
				g.drawString("You have "+lives+" lives left.", 320,50); 
			else if (lives ==1)
				g.drawString("You have "+lives+" life left.", 320,50); 
			else if (lives ==0)	
				g.drawString("You just died.", 350,50); 
				
			for (int col = 0; col < 50; col++){ // this draws the bullets by calling a picture.
				if (bullet[0][col]!=0)
					g.drawImage(bulletPic,bullet[0][col],bullet[1][col],this);	
			}
		}
		
		public void shooting(){ // this allows the computer to advance the movement of the bullets (moving forward)
			for (int col = 0; col < 50; col++){
				if (bullet[0][col]!=0&&bullet[0][col]<700)
					bullet[0][col] = bullet[0][col]+20;
			}
			
			bulletCount = 0;
			for (int col = 49; col >= 0; col--){ // this counts the number of bullets left.
				if (bullet[0][col]==0)
					bulletCount++;
				else if (bullet[0][col]!=0)
					break;
			}
		}
		
		public void newShot(){ // this allows the user to create a new shot of bullet.
			for (int col = 0; col < 50; col++){
				if (bullet[0][col]==0){
					bullet[0][col] = 100; 
					bullet[1][col] = yLocation +10;
					break;
				}
				else{}
			}
		}
		
		public void rocks(){ // this randomnizes the rocks' locations and movements.
			for (int col = 0; col < 1000; col++){
				if (rocks[0][col]==0){
					rocks[0][col] =600;
					rocks[1][col] = (int)(Math.random()*480);
					break;
				}
				else if (rocks[0][col]!=0){}
			}
		}
		
		public void rockAccelerate(){ // this moves existing rocks forward.
			for (int col = 0; col < 1000; col++){
				if (rocks[0][col]!=0){
					if (rocks[0][col]!=-20)
						rocks[0][col] = rocks[0][col]-1;
					}
			}
		}
		
		public void printRocks(Graphics g){ // this prints out pictures of rocks according to locations.
			for (int col = 0; col < 1000; col++){
				if (rocks[0][col]!=0)
					g.drawImage(rocksPicture,rocks[0][col],rocks[1][col],this);
			}
		}
		
		public void hit(){ // this verifies whether a bullet hits a rock.
			for (int col = 0; col < 1000; col++){
				if (rocks[0][col]!=0){
					for (int i = 0; i < 50; i++){
						if (rocks[0][col]-bullet[0][i]+20<5&& (bullet[1][i]>rocks[1][col]-20 && bullet[1][i]<rocks[1][col]+40)){
							rocks[0][col] = 0;
							bullet[1][i] = 700;
							break;	
						}
						else{}
					}
				}
			}
		}
		
		public void die(){ // if user contacts the rocks, some actions are initiated (either die completely, or death questions...)
			for (int col = 0; col < 1000; col++){ // this figures the collision
				if (rocks[0][col]==100&& rocks[1][col]<= yLocation+40&& rocks[1][col]+20>=yLocation&& lives >0){
					lives = lives -1; // user first loses one life no matter what.
					if (lives>=0&&dieQuestion.isVisible()==false){
						if (timerBullet.isRunning()){ // then all the timers are stop
							timerBullet.stop();
							timerRocks.stop();
							timerVerifyShot.stop();
							levelChange.stop();
						}
						
						// the following generates death questions by rotation...
						// all the questions have to be asked once in order for them to
						// be asked again. (the purpose of the boolean variable)
						
						boolean allUsed = true;
						
						for (int i = 0; i <40; i++){
							if (questionArray[2][i].equals("false"))
								allUsed = false;
						}
						
						if (allUsed == true){
							for (int i = 0; i <40; i++)
								questionArray[2][i]= "false";
						}
						
						// this randomizes the questions (from the available/unused ones)
						do {
							randomQuestion = (int)(Math.random()*40);										
						} while(questionArray[2][randomQuestion].equals("true"));
						
						questionArray[2][randomQuestion] = "true";

						questionAsked.setText(questionArray[0][randomQuestion]);
						
						// this saves the correct answer in the char.
						answer = questionArray[1][randomQuestion].charAt(0);
						
						// this allows the radio buttons for user's selection to be visible
						choiceA.setVisible(true);
						choiceB.setVisible(true);
						choiceC.setVisible(true);
						choiceD.setVisible(true);
						timeCount.setVisible(true);
						
						// this opens up the death question's window.
						dieQuestion.setVisible(true);
						dying = true;
						
						// this starts the timing of user's answering of the question (10 sec)
						if (answerTime.isRunning()==false)
							answerTime.start();
					}
					break;
				}
			}
		}
		
		public void ifDiedThen(Graphics g){ // if user loses all three lives...	
			if (lives ==0){
				death = true;
				if (timerBullet.isRunning()){ // all the timers are stopped.
					timerBullet.stop();
					timerRocks.stop();
					timerVerifyShot.stop();
					levelChange.stop();
				}
				g.setColor(Color.gray); // a window (by drawing graphics) will popped up
							// informing the user his/her death and to start over.
				g.fillRect(40,20,500,480);
				g.setColor(Color.black);
				g.drawRect(40,20,500,480);
				Font dieFont = new Font ( "Helvetica", Font.BOLD, fontSize );
				g.setFont(dieFont);
				g.drawString("Dear User, you just died.",180,200);
				g.drawString("If you wish to start over,",180,230);
				g.drawString("you will be transported back to Level 1.",110,260);
				
				g.setColor(Color.white);
				g.fillRect(250,350,100,40);
				g.setColor(Color.black);
				g.drawRect(250,350,100,40);
				g.drawString("Start Over", 253,375);
			}
		}
		
		public void startingOver(){ // this method is called whenever user is starting over.
			chooseLevel.setSelectedIndex(0);
			userLevel = 1;
			bulletCount = 50;
			yLocation = 240;
			rockCount = 700; // this defaults the level to level 1's conditions.
			rockSpeed = 5;
			lives = 3;
			nextLevelCount=-1;
			bullet = new int [2][50];
			for (int i = 0; i < 50; i++) // this fills up the bullets again.
				for (int j = 0; j  < 2; j++)
					bullet[j][i] = 0;
		}
		
		public void newGame(){ // this sets a new condition (for any level)
			death = false;
			repaintBullet bulletAction = new repaintBullet();
			timerBullet = new Timer(100, bulletAction);
			
			repaintRocks rockAction = new repaintRocks();
			timerRocks = new Timer (rockCount, rockAction);
			
			verifyShot shootAction = new verifyShot();
			timerVerifyShot = new Timer (rockSpeed, shootAction);
			
			levelChanging levelAction = new levelChanging();
			levelChange = new Timer (1000, levelAction);
			
			if (timerBullet.isRunning() == false){ // this initiates the timers if they have not been initiated yet.
				timerBullet.start();
				timerRocks.start();
				timerVerifyShot.start();
				levelChange.start();
			}
			
			// at the beginning of every level, (not just the first) rocks are cleared to restart
			rocks = new int [2][1000];
			for (int i = 0; i < 1000; i++)
				for (int j = 0; j  < 2; j++)
					rocks[j][i] = 0;
		}
		
		public void changeLevel(){ // this allows the changing level => more difficulty by changing rocks' speed and count
			if (userLevel ==1){
				rockCount = 700;
				rockSpeed = 5;
			}
			
			else if (userLevel ==2){
				rockCount = 400;
				rockSpeed = 4;
			}
			
			else if (userLevel ==3){
				rockCount = 350;
				rockSpeed = 3;
			}
			
			else if (userLevel ==4){
				rockCount = 300;
				rockSpeed = 3;
			}
			
			else if (userLevel ==5){
				rockCount = 200;
				rockSpeed = 3;
			}
			
			else if (userLevel ==6){
				rockCount = 150;
				rockSpeed = 3;
			}
			
			else if (userLevel ==7){
				rockCount = 100;
				rockSpeed = 2;
			}
			
			else if (userLevel ==8){
				rockCount = 100;
				rockSpeed = 2;
			}
			
			else if (userLevel ==9){
				rockCount = 100;
				rockSpeed = 1;
			}
			
			else if (userLevel ==10){
				rockCount = 50;
				rockSpeed = 1;
			}
			
			else if (userLevel > 10&& rockCount >=10){ // this allows the user to continue playing the game after level 10
				rockCount = rockCount - 5;
				rockSpeed = 1;
			}
			
			else{ // this allows the user to go on infinite level (except no more changes to speed/count)
				rockCount = 1;
				rockSpeed = 1;
			}
		}
	
		public void timeCount(){ // this accounts for the 60 sec limit for each level of the game.
			if (nextLevelCount==0)
				timeDisplayString = "Time left = 01:00";
			else if (nextLevelCount >0 && nextLevelCount <51)
				timeDisplayString = "Time left = 00:"+(60-nextLevelCount)+"";
			else if (nextLevelCount==51 || (nextLevelCount > 51&&nextLevelCount <60))
				timeDisplayString = "Time left = 00:0"+(60-nextLevelCount)+"";
			else if (nextLevelCount == 60){ // when time is reached (60 sec) next level is called.
				timeDisplayString = "Time left = 00:00";
				if (timerBullet.isRunning()){	
					timerBullet.stop();
					timerRocks.stop();
					timerVerifyShot.stop();
					levelChange.stop();
				}
				nextLevelCount =-1;
				userLevel ++;
				chooseLevel.setSelectedIndex(0);
				changeLevel();
				newGame();	 
			}
		}
		
		public void levelDisplay(Graphics g){ // this displays the level of the current situation.
			Font levelFont = new Font ( "Helvetica", Font.BOLD, fontSize ); // This makes the font for the description.
			g.setColor(descriptColor);
			g.setFont (levelFont);
			g.drawString("You are now in Level "+userLevel+".", 320,80); 
			g.drawString(timeDisplayString, 320,110); 
		}
		
		public void selectingLevel(){ // this allows user, when selecting a level through setting, experience a change in difficulty.
			changeLevel();		// even when time has not been reached.
			if (timerBullet.isRunning()){
				timerBullet.stop();
				timerRocks.stop();
				timerVerifyShot.stop();
				levelChange.stop();
			}
			newGame();
		}
		
		class questionPanel extends JPanel implements ActionListener{ // this is the death question Panel that pops up whenever user is about to lose life.
			public questionPanel(){
				setBackground(Color.white);
				setLayout(new GridLayout(3,1));
				JPanel infoOverall = new JPanel();
				infoOverall.setLayout(new GridLayout(3,1));
				infoOverall.setBackground(Color.white);
				
				// this gives out the instruction of the death question policy on top of the window.
				JLabel infoDie = new JLabel("Dear user, you are about to lose one life.",JLabel.CENTER);
				JLabel infoDie1 = new JLabel("If you can answer the following question in 20 seconds,",JLabel.CENTER);
				JLabel infoDie2 = new JLabel("then you can get your life back and bullets refilled!",JLabel.CENTER);
				Font dieFont = new Font ( "Helvetica", Font.BOLD, fontSize );
				infoDie.setFont(dieFont);
				infoDie1.setFont(dieFont);
				infoDie2.setFont(dieFont);
				infoOverall.add(infoDie);
				infoOverall.add(infoDie1);
				infoOverall.add(infoDie2);
				
				this.add(infoOverall);
				
				// this creates a JTextArea that allows the user to view the question.
				questionAsked.setEditable(false);
				questionAsked.setLineWrap(true);
				JScrollPane scroller = new JScrollPane(questionAsked);
				// this initiates scroller when the questions are extremely long.
				questionAsked.setMargin( new Insets(7,7,7,7) );

				questionAsked.setFont(dieFont);
				this.add(scroller);
				
				JPanel lastSection = new JPanel();
				JPanel answerChoice = new JPanel();
				lastSection.setLayout(new GridLayout(3,1));
				answerChoice.setLayout(new FlowLayout());
				lastSection.setBackground(Color.white);
				answerChoice.setBackground(Color.white);
				lastSection.add(answerChoice);
				
				// this sets up the radiobuttons for user's answer selection.
				choiceA = new JRadioButton("(a)");
				choiceB = new JRadioButton("(b)");
				choiceC = new JRadioButton("(c)");
				choiceD = new JRadioButton("(d)");

				answerChoice.add(choiceA);
				answerChoice.add(choiceB);
				answerChoice.add(choiceC);
				answerChoice.add(choiceD);
				choiceA.addActionListener(this);
				choiceB.addActionListener(this);
				choiceC.addActionListener(this);
				choiceD.addActionListener(this);
				Font correctFont = new Font ( "Helvetica", Font.BOLD, fontSize );
				timeCount.setFont(correctFont);
				lastSection.add(timeCount);
				
				// this tells the user whether the answer he/she selected is right.
				rightOrWrong.setFont(correctFont);
				lastSection.add(rightOrWrong);
				this.add(lastSection);
			}
			
			public void actionPerformed (ActionEvent evt){ // so if the answer matches, then the window disappears
									// as user gains one life + bullets refilled.
				if (choiceA.isSelected()&&answer=='a'){
					rightOrWrong.setText("You've gotten the question right!");
					choiceA.setVisible(false);
					choiceA.setSelected(false);
					choiceB.setVisible(false);
					choiceC.setVisible(false);
					choiceD.setVisible(false);
					timeCount.setVisible(false);
					choiceA.setSelected(false);
					seconds11 = 1;
					if (lives <3)
						lives++;
					bullet = new int [2][50];
					for (int i = 0; i < 50; i++)
						for (int j = 0; j  < 2; j++)
							bullet[j][i] = 0;
				}
				else if (choiceB.isSelected()&&answer=='b'){
					rightOrWrong.setText("You've gotten the question right!");
					choiceA.setVisible(false);
					choiceB.setVisible(false);
					choiceC.setVisible(false);
					choiceD.setVisible(false);
					timeCount.setVisible(false);
					choiceB.setSelected(false);
					seconds11 = 1;
					if (lives <3)
						lives++;
					bullet = new int [2][50];
					for (int i = 0; i < 50; i++)
						for (int j = 0; j  < 2; j++)
							bullet[j][i] = 0;
				}
				else if (choiceC.isSelected()&&answer=='c'){
					rightOrWrong.setText("You've gotten the question right!");
					choiceA.setVisible(false);
					choiceB.setVisible(false);
					choiceC.setVisible(false);
					choiceD.setVisible(false);
					timeCount.setVisible(false);
					choiceC.setSelected(false);
					seconds11 = 1;
					if (lives <3)
						lives++;
					bullet = new int [2][50];
					for (int i = 0; i < 50; i++)
						for (int j = 0; j  < 2; j++)
							bullet[j][i] = 0;
				}
				else if (choiceD.isSelected()&&answer=='d'){
					rightOrWrong.setText("You've gotten the question right!");
					choiceA.setVisible(false);
					choiceB.setVisible(false);
					choiceC.setVisible(false);
					choiceD.setVisible(false);
					timeCount.setVisible(false);
					choiceD.setSelected(false);
					seconds11 = 1;
					if (lives <3)
						lives++;
					bullet = new int [2][50];
					for (int i = 0; i < 50; i++)
						for (int j = 0; j  < 2; j++)
							bullet[j][i] = 0;
				}
				else { // if user doesn't get the question right, user doesn't get the life back,
					// and the correct answer will be displayed.
					rightOrWrong.setText("The correct answer is "+answer+" . Sorry!");
					choiceA.setVisible(false);
					choiceB.setVisible(false);
					choiceC.setVisible(false);
					choiceD.setVisible(false);
					timeCount.setVisible(false);
					choiceA.setSelected(false);
					choiceB.setSelected(false);
					choiceC.setSelected(false);
					choiceD.setSelected(false);
					seconds11 = 2;
				}
			}
		}
		
		class answering implements ActionListener{ // this allows the countdown for the answering (10 sec)
			public void actionPerformed(ActionEvent e){
				if (seconds11 >0&& seconds11!=2){ // this displays the time left for answering.
					seconds11--;
					timeCount.setText("Time left: "+seconds11+" seconds");
				}
				else if (seconds11 == 2){
					seconds11--;
					timeCount.setText("Time left: "+seconds11+" second");
				}
					
				
				else if (seconds11 ==0){ // when time is reached and no correct answer => seems as a wrong answer
							// user doesn't get the life back.
					if (answerTime.isRunning())
						answerTime.stop();
					seconds11 = 20;
					dying = false;
					dieQuestion.setVisible(false);
							
					rightOrWrong.setText("");
					
					timeCount.setText("Time left: "+seconds11+" seconds");
					if (timerBullet.isRunning()==false&&original){ // the timer is restarted after the closing of death question window.
						timerBullet.start();
						timerRocks.start();
						timerVerifyShot.start();
						levelChange.start();
					}
				}
			}
		}
		
		public void keyPressed (KeyEvent evt){ // this is where user uses up and down keys to move and space to shoot
			int value = evt.getKeyCode();
			if (value == KeyEvent.VK_DOWN && yLocation < 480 && timerBullet.isRunning()){
				yLocation= yLocation +40;
			}
			else if (value == KeyEvent.VK_UP && yLocation > 0 && timerBullet.isRunning()){
				yLocation= yLocation -40;
			}
			if (value == KeyEvent.VK_SPACE && timerBullet.isRunning())
				newShot();
			repaint();
		}
		
		public void keyTyped (KeyEvent evt){}
		public void keyReleased (KeyEvent evt){}
		
		public void mousePressed(MouseEvent evt){
			int x = evt.getX();
			int y = evt.getY();
			if (x>0 && timerBullet.isRunning()&&!death){ // this allows user to pause the program when clicked
				pause = true;				// anywhere on the screen during the game.
				timerBullet.stop();
				timerRocks.stop();
				timerVerifyShot.stop();
				levelChange.stop();
			}
			
			else if (x>0 && timerBullet.isRunning()== false&&!death){ // this allows the user to resume the game
				pause=false;						// when clicked anywhere on the screen once again.
				timerBullet.start();
				timerRocks.start();
				timerVerifyShot.start();
				levelChange.start();
			}
			
			if (death && x>250 && x < 350 && y > 350 && y < 390){ // this allows user to start over (from scratch) when died completely.
				startingOver();
				newGame();
			}
		}
		
		public void mouseClicked(MouseEvent evt){}
		public void mouseReleased(MouseEvent evt){}
		public void mouseEntered(MouseEvent evt){}
		public void mouseExited(MouseEvent evt){}
	} // class gameGraphics

	public void actionPerformed (ActionEvent evt){ // this sets up the function of the buttons on the main page.
		String command = evt.getActionCommand();
		
		if (command.equals("Start Game")){ // start game button initiates the game from scratch (level 1)
			original = true;
			OPchangePage.show(overallPanel, "panel 2");
			if (gameStarted == false){
				if (timerBullet.isRunning()==false){
					timerBullet.start();
					timerRocks.start();
					timerVerifyShot.start();
					levelChange.start();
				}
				gameStarted = true;
				startGame.setText("Continue Game");
				goBackInstruction.setText("Continue Game");
				goBackSetting.setText("Continue Game");
			}
		}
		
		else if (command.equals("Continue Game")){ // continue game replaces start game once a game is initiated.
			original = true;
			OPchangePage.show(overallPanel, "panel 2");
			chooseLevel.setSelectedIndex(0);
			
			if (timerBullet.isRunning() == false&&dying==false&&pause == false){
				timerBullet.start();
				timerRocks.start();
				timerVerifyShot.start();
				levelChange.start();
				original = true;
			}
		}
				
		// these buttons call to specific pages of the cardLayout.
		else if (command.equals("View Instruction")){ 
			original = false;
			OPchangePage.show(overallPanel, "panel 3");
		}	
		
		else if (command.equals("Return to Menu")){
			original = false;
			OPchangePage.show(overallPanel, "panel 1");
		}
		
		else if (command.equals("Settings")){
			original = false;
			OPchangePage.show(overallPanel, "panel 4");
		}
		
		else if (command.equals("End Air Attack"))
			System.exit(0);
	
		// these are conditions called by the combo box that allows user to select the desired level.
		if (chooseLevel.getSelectedIndex()==1){
			userLevel = 1;
			selectChange = true;
			nextLevelCount=-1;
		}
		
		else if (chooseLevel.getSelectedIndex()==2){	
			userLevel = 2;
			selectChange = true;
			nextLevelCount=-1;
		}
		
		else if (chooseLevel.getSelectedIndex()==3){
			userLevel = 3;
			selectChange = true;
			nextLevelCount=-1;
		}
		
		else if (chooseLevel.getSelectedIndex()==4){	
			userLevel = 4;
			selectChange = true;
			nextLevelCount=-1;
		}
		
		else if (chooseLevel.getSelectedIndex()==5){	
			userLevel = 5;
			selectChange = true;
			nextLevelCount=-1;
		}
		
		// this is the radio buttons that allow the user to select the desired theme.
		if (cloud.isSelected()){
			backgroundPic = cloudPic;
			descriptColor = Color.black;
			rocksPicture = rocksPic;
		}
		
		else if (star.isSelected()){
			backgroundPic = starPic;
			descriptColor = Color.white;
			rocksPicture = rocksPic2;
		}
		
		else if (grass.isSelected()){
			backgroundPic = grassPic;
			descriptColor = Color.black;
			rocksPicture = rocksPic;
		}
	}
	
	public void stateChanged (ChangeEvent evt){ // this is the scroll bar's action that allows the user to
							// select the preferred font size.
		fontSize = fontSizeChange.getValue();
		Font instructionFont = new Font ( "Helvetica", Font.BOLD, fontSize );
		instructionArea.setFont(instructionFont);
	}
	
	public class ReadData { // this class creates scanner that reads text file that contains the instruction.
		private Scanner inFile; 
		public ReadData() {
			String fileName = "Instruction.txt";
			try {
				inFile = new Scanner(new File(fileName));
			} catch (FileNotFoundException e) {
				System.err.printf("ERROR: Cannot open %s\n", fileName);
				System.exit(1);
			}
			while (inFile.hasNextLine()) {
				instructionList = instructionList + "\n" + inFile.nextLine();
			}
			inFile.close();
		}
	}
	
	public class ReadQuestions { // this class uses scanner to read a text file that contains the academic questions and answers.
		private Scanner inFile;
		private int i, j, count;
		public ReadQuestions() {
			i = 0;
			j = 0;
			count =0;
			String fileName = "Academic Questions.txt";
			try {
				inFile = new Scanner(new File(fileName));
			} catch (FileNotFoundException e) {
				System.err.printf("ERROR: Cannot open %s\n", fileName);
				System.exit(1);
			}
			
			while (inFile.hasNextLine()) { // the spacing in the text file is adjusted so that answers and questions
					// are located in a certain manner. taking advantage of that we can store the questions accordingly.
				if (j==0)
					questionArray[j][i] = questionArray[j][i]+"\n"+inFile.nextLine();
				else{ 
					questionArray[j][i] = questionArray[j][i]+inFile.nextLine();
					questionArray[2][i] = "false";
				}
				count++;
				if (count ==5&&j ==0){
					j=1;
					count =0;
				}
				else if (count ==3 && j ==1){
					j =0;
					if (i<40)
						i++;
					else if (i==40) // since there are only 40 questions in the text file, the scanner stops reading
						// once it counts to 40.
						inFile.close();
					count =0;
				}
			}
		}
	}
} // end Game.java