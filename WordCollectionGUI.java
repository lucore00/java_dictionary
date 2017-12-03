/******************************************
*                                         *
*        Author:     Brian Lucore         *
*        Date:		 12/8/06              *
*                                         *
*           Virtual Dictionary            *
*                                         *
*******************************************/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.sound.sampled.*;
import java.util.*;

/** Enormous WordCollectionGUI class which uses JFrame and ActionListener */

public class WordCollectionGUI extends JFrame implements ActionListener
{

/** Datafields for WordCollection, index, and then all buttons and labels */

    JLabel engLabel;
    JLabel tranLabel;
    JLabel picLabel;
    JTextField engField;
    JTextField transField;
    JButton picArea;
    JButton startButton;
    JButton nextButton;
    JButton prevButton;
    JButton helpButton;
    JButton quitButton;
    JButton volButton1;
    JButton volButton2;
    private WordCollection dictionary;
    private int index;

/** Boolean for checking if Start button was clicked first to activate the dictionary */

    private static boolean dictionaryIsStarted = false;

/** Audio datafields */

    AudioFormat audioFormat;
    AudioInputStream audioInputStream;
    SourceDataLine sourceDataLine;
    File engSound;
    File intSound;

/** Default constructor for WordCollectionGUI initializing most of the datafields and adding them to the WordCollectionGUI */

    public WordCollectionGUI()
    {
        WordCollectionGUILayout customLayout = new WordCollectionGUILayout();
        dictionary = new WordCollection("dictionary.txt");
        index = 0;

        getContentPane().setFont(new Font("Helvetica", Font.PLAIN, 12));
        getContentPane().setLayout(customLayout);

        engLabel = new JLabel("English Word:");
        engLabel.setFont(new Font("Arial", Font.BOLD, 14));
        getContentPane().add(engLabel);

        tranLabel = new JLabel("Transliterated Word:");
        tranLabel.setFont(new Font("Arial", Font.BOLD, 14));
        getContentPane().add(tranLabel);

        picLabel = new JLabel("Picture:");
        picLabel.setFont(new Font("Arial", Font.BOLD, 14));
        getContentPane().add(picLabel);

        engField = new JTextField("");
        getContentPane().add(engField);
        engField.setEditable(false);
        engField.setBackground(Color.white);

        transField = new JTextField("");
        getContentPane().add(transField);
        transField.setEditable(false);
        transField.setBackground(Color.white);

        picArea = new JButton();
        picArea.setBackground(Color.white);
        picArea.addActionListener(this);
        getContentPane().add(picArea);

        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 13));
        startButton.addActionListener(this);
        getContentPane().add(startButton);

        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 13));
        nextButton.addActionListener(this);
        getContentPane().add(nextButton);

        prevButton = new JButton("Previous");
        prevButton.setFont(new Font("Arial", Font.BOLD, 13));
        prevButton.addActionListener(this);
        getContentPane().add(prevButton);

        helpButton = new JButton("Help");
        helpButton.setFont(new Font("Arial", Font.BOLD, 13));
        helpButton.addActionListener(this);
        getContentPane().add(helpButton);

        quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 13));
        quitButton.addActionListener(this);
        getContentPane().add(quitButton);

        volButton1 = new JButton(volume());
        volButton1.addActionListener(this);
        getContentPane().add(volButton1);


        volButton2 = new JButton(volume());
        volButton2.addActionListener(this);
        getContentPane().add(volButton2);

        setSize(getPreferredSize());

/** Just a miscellaneous method incase user doesn't press Quit to exit the program.  This will also quit with the upper-right
X as usual in Windows, without confusing the compiler. */

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
    }

/** Primary method for populating the dictionary */

    public void popDict(int index)
    {
		Word w = dictionary.getWord(index);
		engField.setText(w.english);
		transField.setText(w.international);
		picArea.setIcon(bigPic(w.picture));

		engSound = new File(w.engSound);
		intSound = new File(w.intSound);

	}

/** Method for telling the Start button to call up popDict(), start at the first entry in the dictionary and optionally change
the background color to blue */

    public void start()
    {
		index = 0;
		popDict(index);
		getContentPane().setBackground(Color.magenta);
		dictionaryIsStarted = true;

	}

/** Method for telling the Next button to call up rotateRight() and popDict() and optionally change the background color to red
after checking if dictionary was activated first */

	public void next()
	{
		if(dictionaryIsStarted)
		{
			rotateRight();
			popDict(index);
			getContentPane().setBackground(Color.red);
		}
		else
		{
			String warning_string = "Please click Start first to activate the dictionary";
			JOptionPane.showMessageDialog(new Frame(), warning_string, "Warning", JOptionPane.WARNING_MESSAGE);
		   	return;
		}
	}

/** Method for telling the Previous button to call up rotateLeft() and popDict() and optionally change the background color to
white after checking if dictionary was activated first */

	public void previous()
	{
		if(dictionaryIsStarted)
		{
			rotateLeft();
			popDict(index);
			getContentPane().setBackground(Color.white);
		}
		else
		{
			String warning_string = "Please click Start first to activate the dictionary";
			JOptionPane.showMessageDialog(new Frame(), warning_string, "Dictionary not activated yet", JOptionPane.INFORMATION_MESSAGE);
		   	return;
		}
	}

/** Method for loading the previous element in the dictionary */

	private void rotateLeft()
	{
		if(index == 0)
		{
			index = dictionary.size() - 1;
		}

		else
		{
			index = index - 1;
		}
	}

/** Method for loading next element in the dictionary */

	private void rotateRight()
	{
		if(index == dictionary.size() - 1)
		{
			index = 0;
		}

		else
		{
			index = index + 1;
		}
	}

/** Method for playing back data from an audio file.  Associates with PlayThread class and run() method on bottom. */

	public void playSound(File soundFile)
	{
		try
		{
		      audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		      audioFormat = audioInputStream.getFormat();
		      System.out.println(audioFormat);
		      DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

		      sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);

		      new PlayThread(audioFormat, audioInputStream, sourceDataLine).start();
		}
		catch (Exception e)
		{
		      e.printStackTrace();
		      System.exit(0);
		}
  	}

/** Method for displaying the Help dialogue box */

    public void help()
    {
		String help = "Virtual Dictionary is a little program that contains a word in English in the top \n" +
						"field, the transliterated word from another language (as in, how the word would look \n" +
						"in our Latin script) in the next field, the word pronunciations, and finally a \n" +
						"pictorial representation of the word.  Click Start to begin with the first entry \n" +
						"and then Next to advance to the next entry, Previous for the previous entry or \n" +
						"Quit to exit the program.  Click the volume buttons next to the word to hear what \n" +
						"it sounds like. \n\n" +
						"I hope you enjoy it.  Please send all questions or comments to luco00@comcast.net.";

           JOptionPane.showMessageDialog(new Frame(),
  	         help, "Virtual Dictionary Help", JOptionPane.INFORMATION_MESSAGE);
	}

/** Method for displaying the picture area dialogue box */

	public void bigButton()
	{
		String str = "Guess what?  You have encountered the only 'bug' in my program.  This button does \n" +
				 	 "nothing.  It should be just a text area for the picture, but I am unable to get \n" +
				 	 "that to work. \n\nContinue ...";

			JOptionPane.showMessageDialog(new Frame(),
  	         str, "Don't click the big picture area", JOptionPane.INFORMATION_MESSAGE);
	}

/** Method for loading the vol.gif file */

    public Icon volume()
	{
			Icon img = new ImageIcon(getClass().getResource("vol.gif"));
			return img;
	}

/** Method for playing the English word */

	public void hearEnglish()
	{
		if (dictionaryIsStarted)
		{
			playSound(engSound);
		}
		else
		{
			String warning_string = "You do not have an English word activated.  Click Start first.";
			JOptionPane.showMessageDialog(new Frame(), warning_string, "Dictionary not activated yet", JOptionPane.INFORMATION_MESSAGE);
		   	return;
		}
	}

/** Method for playing the foreign word */

	public void hearForeign()
	{
		if (dictionaryIsStarted)
		{
			playSound(intSound);
		}
		else
		{
			String warning_string = "You do not have a foreign word activated.  Click Start first.";
			JOptionPane.showMessageDialog(new Frame(), warning_string, "Dictionary not activated yet", JOptionPane.INFORMATION_MESSAGE);
		   	return;
		}
	}


/** Method for loading the pictorial representation of the current word */

	public Icon bigPic(String imageName)
	{
		Icon img = new ImageIcon(getClass().getResource(imageName));
		return img;
	}

/** Method for telling the buttons to perform their tasks */

    public void actionPerformed (ActionEvent e)
    {
		if(e.getSource() == startButton)
		{
			start();
		}
		if(e.getSource() == nextButton)
		{
			next();
		}
		if(e.getSource() == prevButton)
		{
			previous();
		}
		if(e.getSource() == helpButton)
		{
			help();
		}

		if(e.getSource() == volButton1)
		{
			hearEnglish();
		}

		if(e.getSource() == volButton2)
		{
			hearForeign();
		}
		if(e.getSource() == picArea)
		{
			bigButton();
		}

		else if(e.getSource() == quitButton)
		{
			System.exit(0);
		}
	}

/** Main WordCollectionGUI method for running the program */

    public static void main(String args[])

    {
        WordCollectionGUI window = new WordCollectionGUI();

        window.setTitle("Virtual Dictionary");
        window.pack();
        window.show();
    }
}

/** Class WordCollectionGUILayout */

class WordCollectionGUILayout implements LayoutManager
{

/** Default constructor for WordCollectionGUILayout */

    public WordCollectionGUILayout()
    {
    }

/** Add method for helping implement LayoutManager components */

    public void addLayoutComponent(String name, Component comp)
    {
    }

/** Remove method for removing components */

    public void removeLayoutComponent(Component comp)
    {
    }

/** Method for defining GUI component dimensions with Insets LayoutManager*/

    public Dimension preferredLayoutSize(Container parent)
    {
        Dimension dim = new Dimension(0, 0);

        Insets insets = parent.getInsets();
        dim.width = 792 + insets.left + insets.right;
        dim.height = 652 + insets.top + insets.bottom;

        return dim;
    }

/** Method for defining minimum GUI component dimentions */

    public Dimension minimumLayoutSize(Container parent)
    {
        Dimension dim = new Dimension(0, 0);
        return dim;
    }

/** Method for placing components on the grid with Insets */

    public void layoutContainer(Container parent)
    {
        Insets insets = parent.getInsets();

        Component c;
        c = parent.getComponent(0);
        if (c.isVisible()) {c.setBounds(insets.left+80,insets.top+16,104,16);}
        c = parent.getComponent(1);
        if (c.isVisible()) {c.setBounds(insets.left+40,insets.top+104,144,16);}
        c = parent.getComponent(2);
        if (c.isVisible()) {c.setBounds(insets.left+104,insets.top+192,72,16);}
        c = parent.getComponent(3);
        if (c.isVisible()) {c.setBounds(insets.left+200,insets.top+16,424,24);}
        c = parent.getComponent(4);
        if (c.isVisible()) {c.setBounds(insets.left+200,insets.top+104,424,24);}
        c = parent.getComponent(5);
        if (c.isVisible()) {c.setBounds(insets.left+192,insets.top+192,520,376);}
        c = parent.getComponent(6);
        if (c.isVisible()) {c.setBounds(insets.left+32,insets.top+592,64,24);}
        c = parent.getComponent(7);
        if (c.isVisible()) {c.setBounds(insets.left+176,insets.top+592,64,24);}
        c = parent.getComponent(8);
        if (c.isVisible()) {c.setBounds(insets.left+328,insets.top+592,112,24);}
        c = parent.getComponent(9);
        if (c.isVisible()) {c.setBounds(insets.left+520,insets.top+592,72,24);}
        c = parent.getComponent(10);
        if (c.isVisible()) {c.setBounds(insets.left+656,insets.top+592,64,24);}
        c = parent.getComponent(11);
        if (c.isVisible()) {c.setBounds(insets.left+640,insets.top+16,32,32);}
        c = parent.getComponent(12);
        if (c.isVisible()) {c.setBounds(insets.left+640,insets.top+104,32,32);}
    }
}

/** Internal class PlayThread for playing back audio files.  Associates with playSound(soundFile) method above */

	class PlayThread extends Thread
	{

		    AudioFormat audioFormat;
		    AudioInputStream audioInputStream;
    		SourceDataLine sourceDataLine;

	  byte tempBuffer[] = new byte[10000];

/** Overloaded constructor for PlayThread */

	public PlayThread(AudioFormat audioFormat, AudioInputStream audioInputStream, SourceDataLine sourceDataLine)
	{
		this.audioFormat = audioFormat;
		this.audioInputStream = audioInputStream;
        this.sourceDataLine = sourceDataLine;
	}

/** Rather complex method for inner class PlayThread for playing back audio files.  Also associates with
playSound(soundFile) method above */

	  public void run()
	  {
		try
		{
		  sourceDataLine.open(audioFormat);
		  sourceDataLine.start();

		  int cnt;

		  while((cnt = audioInputStream.read(tempBuffer,0,tempBuffer.length)) != -1)
		  {
			if(cnt > 0)
			{
			  sourceDataLine.write(tempBuffer, 0, cnt);
			}
		  }
		  sourceDataLine.drain();
		  sourceDataLine.close();
		}
		catch (Exception e)
		{
		  e.printStackTrace();
		  System.exit(0);
		}
	  }
	}
