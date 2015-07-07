/*
 * Author : Samarth Savanur
 */
package com.osu.cse.fpl.lisp;

public class MyInputValidation {

	String inputLine;
	
	/**
	 * @param inputLine
	 */
	public MyInputValidation(String inputLine) {
		this.inputLine = inputLine;
	}

   // checking invalid characters
	public boolean checkInputforvalidsplchar(String line)
	{
		int size = line.length();
		int i = 0;
		boolean status = true;

		if(size > 0)
		{
			while( i < size )
			{
				//checking ascii
				if ( (line.charAt(i) >= 48 && line.charAt(i) <=57)  
						|| (line.charAt(i) >=65 && line.charAt(i)<=90) 
						|| (line.charAt(i) == '(' )
						|| (line.charAt(i) == ')' )
						|| (line.charAt(i) == '.' )
						|| (line.charAt(i) == '-' )
						|| (line.charAt(i) == '+' ) 
						|| (line.charAt(i) == ' ' )
						|| (line.charAt(i) == '\n' )
						|| (line.charAt(i) == '\t' )
				)
				{
					// checking for signed numbers
					if(line.charAt(i) == '+' || line.charAt(i) == '-')
					{
						if(line.charAt(i+1) == ' ')
						{
							System.out.println("ERROR: In the input character" 
									+ line.charAt(i));
							status = false;
							int a = 0;
							breakFn(a);
						}
					}

				}
				else
				{
					status = true;
					return status;
				}
				i++;
			}
		}
		else
		{
			status = false;
		}
		return status;
	}
	
	// checking for literals validity
	public boolean checkLiteral(String token)
	{
		int i = 0;
		if(token.charAt(i) >=65 && token.charAt(i) <=90)
		{
			while(i<token.length())
			{
				if( (token.charAt(i) >= 48 && token.charAt(i) <=57)
						|| (token.charAt(i) >=65 && token.charAt(i)<=90))
				{
					//return true;
				}
				else
				{
					return false;
				}
				i++;
			}
		}
		else
		{
			return false;
		}
		return true;
	}

	// checking for numerical validity
	public boolean checkNumerical(String token)

	{
		int i = 0;
		if((token.charAt(i) >= 48 && token.charAt(i) <= 57) 
				|| token.charAt(0) == '+' 
					|| token.charAt(0) == '-' )
		{
			if(token.charAt(0) == '-' || token.charAt(0) == '+')
			{
				i++;  
			}

			while(i<token.length())
			{
				if( token.charAt(i) >= 48 && token.charAt(i) <= 57)
				{
					//return true;
				}
				else
				{
					return false;
				}
				i++;
			}
		}
		else
		{
			return false;
		}
		return true;
	}

	private void breakFn(int x) {
		// TODO Auto-generated method stub
		switch(x)
		{
		case 0:
			break;
		}
	}
	
}
