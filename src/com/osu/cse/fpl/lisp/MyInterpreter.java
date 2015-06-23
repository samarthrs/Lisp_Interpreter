package com.osu.cse.fpl.lisp;

import java.io.*;
import java.util.*;

public class MyInterpreter {

	public static void main (String[] args)
	{
		try
		{
			String expr = "";
			String input = "";
			StringTokenizer tokenizer;
			
			int left_para_open = 0;
			int right_para_close = 0;
			
			boolean insideExp = false;
			String buffer = "";  
			
			// reading input from buffered reader
			BufferedReader inputExpr = new BufferedReader(new InputStreamReader(System.in));	
			// traversing and reading every next line
			while((input = inputExpr.readLine()) != null)
			{	
			   // exiting in case of exit or $$
			  
			  
				 // System.out.print("1");
				  
				  // converting input to upper case
				  String upperCaseInput = input.toUpperCase();	
				  
				  // tokenizing input using string tokenizer
				  tokenizer = new StringTokenizer(upperCaseInput, "() ", true);
				  while(tokenizer.hasMoreTokens())
	            {
					//  System.out.println("3");
	              buffer = tokenizer.nextToken(); 
	             expr += " ";
	             expr += buffer;
	             
	             // checking input token length
				  if(buffer.length() > 10)
				  {
					System.out.println("ERROR: Invalid length of token");
					break;
				  }
				  /* if (buffer.equalsIgnoreCase("$$"))
				  {
					  System.out.print("$$ encountered");
					  break;
				  } */
				  
				  // left parenthesis check
	              if(buffer.charAt(0) == '(')  
	              {
	            	//  System.out.println("4");
	                left_para_open++;
	              }
	             
	              // right parenthesis 
	              else if(buffer.charAt(0) == ')')
	              {
	            	//  System.out.println("5");
	                right_para_close++;
	              }
	              
	              // checking tab
	              else if(buffer.charAt(0) == ' ')
	              {
	            	//  System.out.println("6");
	                continue;
	              }
	              
	              // checking end of string through $
	              else if(buffer.charAt(0) == '$')
	              {
	            	//  expr += " ";
		           //   expr += buffer;
	            	
	            	  
	            	 
	            	//  System.out.println("7");
	            	  
	            	  // checking validity of expression parenthesis -- valid condition 1
	              if( left_para_open == 0)
	              {
	            	//  System.out.println("8");
	            	
	            	  // parsing input tokens
	                MyParser parser = new MyParser(expr);
	                parser.parseInput();
	                expr = "";
	                insideExp = false;
	                left_para_open = 0;
	                right_para_close = 0;
	                
	                if(parser.rootExpression != null)
	                {
	                	//System.out.println("9");
	                  System.out.print("SysOut >> ");
	                  if(parser.equals(""))
	                  {  
	                	  System.out.println("");
	                	  parser.traverse(parser.rootExpression); 
	                	  
	                  }
	                                   
	                }
	              }
	            
	           // checking validity of expression parenthesis -- valid condition 2
	              else if(left_para_open == right_para_close)
	              {   
	            	  // calling parser class to parse input
	                MyParser parser = new MyParser(expr);
	                parser.parseInput();
	                insideExp = false;
	                expr = "";
	                left_para_open = 0;
	                right_para_close = 0;
	                if(parser.rootExpression != null)
	                {
	                  System.out.print("SysOut >> ");
	                  parser.traverse(parser.rootExpression);                
	                  System.out.println("");                
	                }
	              }
	              
	           // checking validity of expression parenthesis -- invalid condition 1
	              else if((left_para_open - right_para_close) > 0 && insideExp == false)
	              {
	            	  // reseting counters and displaying error
	            	    expr = " "; 
	            	    insideExp = false;
		                left_para_open = 0;
		                right_para_close = 0;
	                System.out.print("ERROR: More left parenthesis than right parenthesis, Invalid Sexpression");
	                break;
	              }
	              
	           // checking validity of expression parenthesis -- invalid condition 2
	              else if ((right_para_close  - left_para_open) > 0 && insideExp == false)
	              {    
	            	// reseting counters and displaying error
	            	    expr = " "; 
	            	    insideExp = false;
		                left_para_open = 0;
		                right_para_close = 0;
	                System.out.print("ERROR: More right parenthesis than left parenthesis, Invalid Sexpression");
	                break; 
	              }  
	              
	              // checking for $$ at end of a valid / invalid expression
            	  if(buffer.equalsIgnoreCase("$$"))
	              {
	            	 System.out.print("$$ Encountered: End of expression");
	            	 System.exit(0);
	            	 break;
	              }
	                
	              }
	              }            			                          		     
	            
				  if(input.equalsIgnoreCase("$$"))
				  {
					  System.out.print("Bye!");
					  System.exit(0); 
				  }
			  }	            
			}			
	catch(IOException e)
		{
		System.out.println( "IO Exception: " + e);
		}				
	}
}
