/*
 * Author : Samarth Savanur
 */
package com.osu.cse.fpl.lisp;

import java.util.*;

public class MyStringTokenizer {
	
	// initiating variable of existing java string tokenizer type
	StringTokenizer tokenizer;
	
	String curToken;
	int noOfTokens;
	int tokenCount;
	String input;

	public void setCurToken(String curToken) {
		this.curToken = curToken;
	}
	
	public void setCurTokenNull() {
		this.curToken = null;
	}
	
    // generate default constructor
	/**
	 * @param tokenizer
	 * @param curToken
	 * @param noOfTokens
	 * @param tokenCount
	 * @param input
	 */
	public MyStringTokenizer() {
		this.tokenizer = null;
		this.curToken = null;
		this.noOfTokens = 0;
		this.tokenCount = 0;
		this.input = null;
	}

	// constructor to tokenize input
	/**
	 * @param tokenizer
	 * @param curToken
	 * @param noOfTokens
	 * @param tokenCount
	 * @param input
	 */
	public MyStringTokenizer(String inputLine) {
		this.curToken = null;
		
		this.tokenCount = 0;
		this.input = inputLine;
		// adding delimiters to be tokens
		this.tokenizer = new StringTokenizer(input, "() . \t \n $" +"$$", true);
		noOfTokens = tokenizer.countTokens();
	}
	
	
    public String getCurToken()
    {
	if(curToken != null)
	    {
		return curToken;
	    }
	else
	    {
		if(tokenizer.hasMoreTokens())
		    {
			curToken = tokenizer.nextToken();
			while(Character.isWhitespace(curToken.charAt(0)))
			    {
				curToken = tokenizer.nextToken();
			    }
			tokenCount++;
		    }
		else
		    {
			curToken = null;
			if(tokenCount != noOfTokens)
			    {
				System.out.print("Error : No of tokens are different from token count");
				
			    }
		    }
	    }
	return curToken;
    }    
}


    
    

