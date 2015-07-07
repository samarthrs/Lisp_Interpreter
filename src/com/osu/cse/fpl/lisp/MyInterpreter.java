/*
 * Author : Samarth Savanur
 */
package com.osu.cse.fpl.lisp;

import java.io.*;
import java.util.*;

public class MyInterpreter {
	
      public static String input;
    //  public static String expr;
  //    static BufferedReader inputExpr = new BufferedReader(new InputStreamReader(System.in));	
	public static void main (String[] args)
	{
		try
		{
			String expr = "";
			input = "";
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
				 if(input.equals("$$") && expr.equals(""))
				  {
					  System.out.print("$$ Encountered: See ya Bubye!!!");
					  System.exit(0); 
				  }
			   // exiting in case of exit or $$
			  
			  
				 // System.out.print("1");
				  
				  // converting input to upper case
				  String upperCaseInput = input.toUpperCase();	
				  
				  // tokenizing input using string tokenizer
				  tokenizer = new StringTokenizer(upperCaseInput, "()\\$ ", true);
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
					if(input.length()>=2)
		              	  if(input.substring(input.length()-2).equals("$$"))
		  	              {
		  	            	 System.out.print("\n $$ Encountered: See ya Bubye !!!");
		  	            	 System.exit(0);
		  	            	 break;
		  	              }
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
	            	
	            	//add                   
                      //System.out.println(expression + " " + expression.charAt(expression.length()-1)+" - "+expression.substring(0,expression.length()-2));
                      boolean c=false;
                      int numCheck;
                      try {
                          //System.out.println(expression.substring(0,expression.length()-2).trim());
                          numCheck = Integer.parseInt(expr.substring(0,expr.length()-2).trim());
                          //System.out.println("Dot Notation Form >> "+numCheck);
                          c=true;
                      } catch (NumberFormatException e) {
                          //System.out.println("Wrong number");
                          numCheck = 0;
                      }
                      float floCheck;
                      boolean invInp=false;
                      if(!c)
                      try {
                          //System.out.println(expression.substring(0,expression.length()-2).trim());
                          floCheck = Float.parseFloat(expr.substring(0,expr.length()-2).trim());
                          System.out.println("Sorry !!! Invalid input");
                          if(input.length()>=2)
        	              	  if(input.substring(input.length()-2).equals("$$"))
        	  	              {
        	  	            	 System.out.print("\n $$ Encountered: See ya Bubye !!!");
        	  	            	 System.exit(0);
        	  	            	 break;
        	  	              }
                          expr="";
                          invInp = true;
                      } catch (NumberFormatException e) {
                          //System.out.println("Wrong number");
                          floCheck = 0;  
	            	 
                      }
	            	//  System.out.println("7");
	            	  
	            	  // checking validity of expression parenthesis -- valid condition 1
                      if(!invInp)
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
	                  //System.out.print("Dot Notation Form >> ");
	                  if(parser.equals(""))
	                  {  
	                	  System.out.println("");
	                	  parser.traverse(parser.rootExpression); 
	                	  
	                  }
	                  
	                	// My New Addition for Part 2
	                	
	                	MySExpr result = parser.evaluate(parser.rootExpression); 
	                	
	                	//Printing algorithm
						if(result.isAtom == true ) 
						{
							if(result.type == MyConstTokens.LITERAL)
							{
								System.out.print("Output >>");
								System.out.println(result.expr);
								System.out.println("");
							}
							else if(result.type == MyConstTokens.NUMERICALS)
							{
								System.out.print("Output >>");
								System.out.println(Integer.toString(result.integer));
								System.out.println("");
							}
						}
						else
						{
							System.out.print("Output >>");
							System.out.print(printalgo(result, true));
							System.out.println();
						}
					} 
	                
	                // My New Addition for Part 2	
	                  
	                  
	                                   
	                
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
	                	
	                  System.out.print("Dot Notation Form >> ");
	                  parser.traverse(parser.rootExpression);                
	                  System.out.println("");     
	                  
	                  
	                  
	                  
	                	//New Addition for Part 2
						MySExpr result = parser.evaluate(parser.rootExpression); 
						
						//Printing algorithm
						if(result.isAtom == true ) 
						{
							if(result.type == MyConstTokens.LITERAL)
							{
								System.out.print("Output >>");
								System.out.println(result.expr);
								System.out.println("");
							}
							else if(result.type == MyConstTokens.NUMERICALS)
							{
								System.out.print("Output >>");
								System.out.println(Integer.toString(result.integer));
								System.out.println("");
							}
						}
						else
						{
							System.out.print("Output >>");
							System.out.print(printalgo(result, true));
							System.out.println();
						}
					 
	                
	              //New Addition for Part 2
            
	                  
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
	                System.out.print("ERROR: More left parenthesis than right parenthesis!!! Invalid Sexpression");
	                if(input.length()>=2)
	              	  if(input.substring(input.length()-2).equals("$$"))
	  	              {
	  	            	 System.out.print("\n $$ Encountered: See ya Bubye !!!");
	  	            	 System.exit(0);
	  	            	 break;
	  	              }
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
	                System.out.print("ERROR: More right parenthesis than left parenthesis!!! Invalid Sexpression");
	                if(input.length()>=2)
		              	  if(input.substring(input.length()-2).equals("$$"))
		  	              {
		  	            	 System.out.print("\n $$ Encountered: See ya Bubye !!!");
		  	            	 System.exit(0);
		  	            	 break;
		  	              }
	                break; 
	              }  
	              
	              // checking for $$ at end of a valid / invalid expression
	              if(input.length()>=2)
            	  if(input.substring(input.length()-2).equals("$$"))
	              {
	            	 System.out.print("\n $$ Encountered: See ya Bubye !!!");
	            	 System.exit(0);
	            	 break;
	              }
	                
	              }
	              }            			                          		     
	            
				 
			  }	            
			}			
	catch(IOException e)
		{
		System.out.println( "IO Exception: " + e);
		}				
	}
	
	// New addition for Project part 2
	//Traverses an Sexpression and prints the output in either list notation or using dot notation
	//isLeft indicates whether we are at the car or cdr branch
	private static String printalgo(MySExpr result, boolean isLeft)
	{
		MySExpr temp = result;

		MySExpr leftChild = temp.car;
		String leftString = "";

		MySExpr rightChild = temp.cdr;
		String rightString = "";

		//Do not print NIL
		if(temp.type == MyConstTokens.LITERAL && temp.expr.equalsIgnoreCase("NIL"))
		{
			return "";
		}
		
		if(leftChild.expr.equals("T"))
            return "(T)";
        if(leftChild.expr.equals("NIL"))
            return "(NIL)";

		//Traverse the car child
		if(leftChild.isAtom == true)
		{
			if(leftChild.type == MyConstTokens.LITERAL)
			{
				leftString = leftChild.expr;
			}
			else if(leftChild.type == MyConstTokens.NUMERICALS)
			{
				leftString = Integer.toString(leftChild.integer);
			}
		}
		else
		{
			//Recursive call
			leftString = printalgo(leftChild, true);
		}

		//Traverse the cdr child
		if(rightChild.isAtom == true)
		{
			if(rightChild.type == MyConstTokens.LITERAL)
			{
				rightString = rightChild.expr;
			}
			else if(rightChild.type == MyConstTokens.NUMERICALS)
			{
				rightString = Integer.toString(rightChild.integer);
			}
		}
		else
		{
			//Recursive call
			rightString = printalgo(rightChild, false);
		}

				return "(" + leftString + " . " + rightString + ")";
		
	}

	public static void check() throws IOException		
	{
		//System.out.println(input);
		if(input.contains("$$"))
		{
			System.out.print("$$ Encountered: See ya Bubye !!!");
//			 break;
			 System.exit(0);
       	
		}
	/*	while((input = inputExpr.readLine()) != null)
		{	
			 if(input.equals("$$") && expr.equals(""))
			  {
				  System.out.print("$$ Encountered: See ya Bubye!!!");
				  System.exit(0); 
			  }*/
//		else
//			continue;
	
	}
}

	

