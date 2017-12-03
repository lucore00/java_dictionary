/******************************************
*                                         *
*        Author:     Brian Lucore         *
*        Date:		 12/8/06              *
*                                         *
*           Virtual Dictionary            *
*                                         *
*******************************************/

/** Class Word */

public class Word
{

/** Datafields to be called from GUI */

	public String english = "";
	public String international = "";
	public String engSound = "";
	public String intSound = "";
	public String picture = "";

/** Overloaded constructor for Word */

	public Word(String english, String international, String engSound, String intSound, String picture)
	{
		this.english =  english;
		this.international = international;
		this.engSound = engSound;
		this.intSound = intSound;
		this.picture = picture;
	}

}