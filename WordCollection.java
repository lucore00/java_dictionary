/******************************************
*                                         *
*        Author:     Brian Lucore         *
*        Date:		 12/8/06              *
*                                         *
*           Virtual Dictionary            *
*                                         *
*******************************************/

import java.util.*;
import java.io.*;

/** Class WordCollection */

public class WordCollection
{

/** Blank txtfile String for reading in the text file and dictionary List for beginning creation of the collection */

	public String txtfile = "";
	public List dictionary = null;

/** Overloaded constructor for WordCollection, new ArrayList collection called dictionary, and calling up load() later
for the GUI */

	public WordCollection(String txtfile)
	{
		this.txtfile = txtfile;
		dictionary = new ArrayList();
		load();
	}

/** Method for reading in contents of the dictionary.txt file. Also, "for" loop for reading in the file and "if" decision
containing StringTokenizer for parsing one line of dictionary.txt at a time and saving it in memory for access in GUI */

	public void load()
	{
		try
		{
			FileInputStream fis = new FileInputStream(txtfile);
			BufferedReader fileInput = new BufferedReader(new InputStreamReader(fis));
			String str = "";
			str = fileInput.readLine();

			for( ;str != null; )
			{
				Word w = null;

				if(str != null)
				{
					StringTokenizer tokenizer = new StringTokenizer(str, ",");
					w = new Word(tokenizer.nextToken().trim(),
								tokenizer.nextToken().trim(),
								"sounds\\" + tokenizer.nextToken().trim(),
								"sounds\\" + tokenizer.nextToken().trim(),
								"images\\" + tokenizer.nextToken().trim());

				}

				dictionary.add(w);
				str = fileInput.readLine();
			}
		}

		catch(Exception e)
		{

		}
	}

/** Method for loading word elements from dictionary List */

	public Word getWord(int index)
	{
		if(index >= 0 && index < dictionary.size())
		{
			Word w = (Word) ((ArrayList) dictionary).get(index);
			return w;
		}
		return null;
	}

/** Method for helping WordCollectionGUI prevent index from going out of bounds */

	public int size()
	{
		return dictionary.size();
	}

}
