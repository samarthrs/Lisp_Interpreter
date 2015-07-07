/*
 * Author : Samarth Savanur
 */
package com.osu.cse.fpl.lisp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

public class MyParser {

	private static MyStringTokenizer mytokenizer;
	public MySExpr rootExpression;
	private static String inputLine;
	
	
	static MyInputValidation validator = new MyInputValidation(inputLine);
	
	//New Addition for Part 2
	
	private static MySExpr trueSexp = new MySExpr("T");
	private static MySExpr nilSexp = new MySExpr("NIL");

	public static Hashtable<String, Integer> numParamofUserFunc = new Hashtable<String, Integer>();

	//End of New Addition for Part 2
	
	//Default Constructor
	public MyParser(String line)
	{
		mytokenizer = new MyStringTokenizer(line); 
		rootExpression = null;
		inputLine = line; 
	}

	public void parseInput() throws IOException 
	{
		
		boolean status = validator.checkInputforvalidsplchar(inputLine);
		if(status == true)
		{
			rootExpression = new MySExpr();
			carParse(rootExpression);
		}
		else 
		{
			System.out.print("ERROR: Invalid input");
			MyInterpreter.check();
			int a = 0;
			MyInterpreter.main(null);
		}
	}

	private static void carParse(MySExpr node) throws IOException
	{
		String currentToken = mytokenizer.getCurToken();
		if(currentToken.equalsIgnoreCase("("))
		{
			mytokenizer.setCurTokenNull();
			literalParse(node);
		}
		else
		{
			if(validator.checkLiteral(currentToken))
			{
				node.expr = currentToken;
				node.isAtom = true;
				node.isNil = false;
				node.type = MyConstTokens.LITERAL;
			}
			else if(validator.checkNumerical(currentToken))
			{
				if(currentToken.charAt(0) == '+')
				{
					currentToken = currentToken.substring(1);
				}
				node.integer = Integer.parseInt(currentToken);
				node.isAtom = true;
				node.isNil = false;
				node.type = MyConstTokens.NUMERICALS;
			}
			else
			{
				node.type = MyConstTokens.SPL_CASE;
				System.out.println("ERROR: Invalid input");
			//	if (expr.contains("$$"))
			//	{
					
				//}
				MyInterpreter.check();
				int a = 0;
				MyInterpreter.main(null);
			}
			mytokenizer.setCurTokenNull();
		}
	}

	private static void literalParse(MySExpr node) throws IOException
	{
		String currentToken = mytokenizer.getCurToken();
		
		if (currentToken.equalsIgnoreCase(MyConstTokens.RIGHT_PARA))
		{
			mytokenizer.setCurTokenNull();
			node.expr = "NIL";
			node.isNil = true;
			node.isAtom = true;
			node.type = MyConstTokens.LITERAL;
		}
		else
		{
			mytokenizer.setCurToken(currentToken);
			MySExpr carSexp = new MySExpr();
			node.car = carSexp;
			carParse(carSexp);

			MySExpr cdrSexp = new MySExpr();
			node.cdr = cdrSexp;
			cdrParse(cdrSexp);
		}
	}

	private static void cdrParse(MySExpr node) throws IOException
	{
		String currentToken = mytokenizer.getCurToken();
		if(currentToken.equals(MyConstTokens.DOT))
		{
			mytokenizer.setCurTokenNull();
			carParse(node);
			String secondToken = mytokenizer.getCurToken();
			
			if(secondToken.equalsIgnoreCase(MyConstTokens.RIGHT_PARA))
			{
				mytokenizer.setCurTokenNull();
			}
			else
			{
				System.out.println("ERROR: Unexpected dot / dollar");
				MyInterpreter.check();
			}
		}
		else
		{
			mytokenizer.setCurToken(currentToken);
			parseRoot(node);
			String secondToken = mytokenizer.getCurToken();
			if(secondToken.equalsIgnoreCase(MyConstTokens.RIGHT_PARA))
			{
				mytokenizer.setCurTokenNull();
			}
			else
			{
				mytokenizer.setCurToken(secondToken);
			}
		}
	}

	private static void parseRoot(MySExpr node) throws IOException
	{
		String currentToken = mytokenizer.getCurToken();
		if( (currentToken != null ) &&(false == currentToken.equalsIgnoreCase(")")))
		{
			MySExpr carSexp = new MySExpr();
			node.car = carSexp;
			carParse(carSexp);

			MySExpr cdrSexp = new MySExpr();
			node.cdr = cdrSexp;
			parseRoot(cdrSexp);
		}
		else
		{
			node.expr = "NIL";
			node.isNil = true;
			node.isAtom = true;
			node.type = MyConstTokens.LITERAL;
		}
	}

	public void traverse(MySExpr inSexp)
    {
	MySExpr temp = inSexp;
	
	if(temp == null)
	    return;
	if(temp.type == MyConstTokens.LITERAL)
	    {
		System.out.print(temp.expr);
                return;
	    }
	if(temp.type == MyConstTokens.NUMERICALS)
	    {
		System.out.print(temp.integer);
                return;
	    }

        System.out.print("(");
	    traverse(temp.car);
        System.out.print(" . ");
	    traverse(temp.cdr);
        System.out.print(")");
        return;
    }
	
	public static void breakFn(int x)
	{
		switch(x)
		{
		case 0:
			break;
		}
	}
	
	
	//New Addition for Part 2
	
	public MySExpr evaluate(MySExpr root) throws IOException
	{
		//DList.deflist should not be explicitly set to null because function
		//definitions need to persist
		return eval(root, nilSexp, MyDefinitionList.deflist);
	}

	private static MySExpr eval(MySExpr root, MySExpr alist,
			MySExpr deflist) throws IOException
	{
		if(root.isAtom)
		{
			//Process the atom
			if(root.type == MyConstTokens.NUMERICALS)
			{
//				System.out.println("I'm Inside isAtom_isNumericals");
				return root;
			}
			else if(root.type == MyConstTokens.LITERAL)
			{
//				System.out.println("I'm Inside isAtom_isLiteral_if");
				if(root.expr.equalsIgnoreCase("T"))
				{
//					System.out.println("I'm Inside isAtom_isLiteral_True");
					return new MySExpr("T");
				}
				else if(root.expr.equalsIgnoreCase("NIL"))
				{
//					System.out.println("I'm Inside isAtom_isLiteral_NIL");
					return new MySExpr("NIL");
				}
				else 
				{
//					System.out.println("I'm Inside isAtom_isLiteral_else");
					if(bound(root, alist))
					{
//						System.out.println("I'm Inside isAtom_isLiteral_else_ifbound");
						return getval(root, alist);
					}
					else
					{
//						System.out.println("I'm Inside isAtom_isLiteral_else_elsebound");
						//Unbounded variable
						System.out.println("ERROR: Unbounded variable " + root.expr);
						MyInterpreter.check();
						MyInterpreter.main(null);
						return nilSexp;
					}
				}
			}
			else
			{
//				System.out.println("I'm Inside isAtom_else");
				//Should not come over here
				return nilSexp;
			}
		}
		else
		{
//			System.out.println("I'm Inside not_Atom");
			//CAR child should be the function name
			String functionName = root.car.expr;

			//Check for type of function
			if(functionName.equalsIgnoreCase(""))
			{
//				System.out.println("I'm Inside not_Atom_space_inside");
				//Means call has come for an intermediate node
				System.out.println("ERROR: Unknown User-Defined function");
				MyInterpreter.check();
				MyInterpreter.main(null);
				return nilSexp;
			}
			else if(functionName.equalsIgnoreCase(MyConstTokens.QUOTE))
			{
				//Check for number of params, should take only a list/Sexpression
				//cdr child of root should be aparam list
				int numOfParams = findNumParams(root.cdr);
//				System.out.println("1");
				if(numOfParams == 1)
				{
//					System.out.println("2");
					return car(cdr(root));
					
				}
				else
				{
					System.out.println("ERROR: Illegal paramters for QUOTE()");
					MyInterpreter.check();
//					System.out.println("3");
					MyInterpreter.main(null);
//					System.out.println("4");
					return nilSexp;
				}
			}
			else if(functionName.equalsIgnoreCase(MyConstTokens.COND))
			{
//				System.out.println("5");
				return evcon(cdr(root), alist, deflist);
			}
			else if(functionName.equalsIgnoreCase(MyConstTokens.DEFUN))
			{
//				System.out.println("6");
				if(checkFuncNameParameters(root))
				{
//					System.out.println("7");
					//Add stuff to deflist
					addtodeflist(cdr(root));
//					System.out.println("8");
					//Need to print the function name if it is a DEFUN
					return new MySExpr(car(cdr(root)).expr);
				}
				else
				{
					System.out.println("ERROR: Illegal paramters for the " +
					"User-Defined function");
					MyInterpreter.check();
					MyInterpreter.main(null);
//					System.out.println("9");
					return nilSexp;
				}
			}
			else
			{
//				System.out.println("10");
				return apply(root.car, evlist(root.cdr, alist, deflist), alist, deflist); 
				
			}
		}
	}

	//Checks if the function name and parameters for an user-defined function are legal or not
	//(1) Function name should not match with any built-in function name
	//(2) Parameters should be literal atoms
	//(3) T and NIL are not allowed as parameters
	//(4) Duplicate parameter names are not allowed
	//Returns true if yes, otherwise returns false
	//Format is (DEFUN XXXX (X Y) (...BODY...))
	private static boolean checkFuncNameParameters(MySExpr exp) throws IOException
	{
		//exp points to the list starting root of DEFUN Sexpression

		String functionName = car(cdr(exp)).expr;
		MySExpr functionRoot = car(cdr(exp));

		//functionRoot points to the function node

		//Compare function name
		if(functionRoot.type == MyConstTokens.LITERAL && checkFunctionName(functionName) ) 
		{
			MySExpr parameterListRoot = car(cdr(cdr(exp)));

			MySExpr temp = parameterListRoot;

			//Default size of ArrayList is ten
			//Hopefully should work for our test cases
			ArrayList<String> al = new ArrayList<String>();

			//Iterate through the param list and check for error conditions
			while(temp != null)
			{
				if(temp.car != null)
				{
					//Car child will be the parameter
					//Can be both literal and numeral atom or list

					if(temp.car.type == MyConstTokens.LITERAL)
					{
						String parameter = temp.car.expr;
						if(parameter.equalsIgnoreCase(MyConstTokens.TRUE) ||
								parameter.equalsIgnoreCase(MyConstTokens.NIL))
						{
							System.out.println("ERROR: Invalid parameter for " +
									"User-Defined function " + functionName + "()");
							MyInterpreter.check();
							MyInterpreter.main(null);
						}
						else
						{
							al.add(parameter);
						}
					}
					else
					{
						System.out.println("ERROR: Non-literal parameter in " + 
								"function " + functionName + "()");
						MyInterpreter.check();
						MyInterpreter.main(null);
					}
				}
				else
				{
					//System.out.println("Param is null for " + temp.expression);
				}
				temp = temp.cdr;
			}

			//All the parameters have been added to the ArrayList
			int originalNumberofParams = al.size();

			//Add the information to the hashmap for future look-up
			numParamofUserFunc.put(functionName, originalNumberofParams);

			HashSet<String> parameterSet = new HashSet<String>(al);
			int newNumberofParams = parameterSet.size();

			if(originalNumberofParams != newNumberofParams)
			{
				//Means there were duplicates
				System.out.println("ERROR: Duplicate parameters in function " + 
						functionName + "()");

				//Clear the hashmap
				numParamofUserFunc.remove(functionName);
				MyInterpreter.check();
				MyInterpreter.main(null);
			}
		}
		else
		{
			System.out.println("ERROR: Illegal function name " + functionName + "()");
			MyInterpreter.check();
			MyInterpreter.main(null);
		}
		return true;
	}

	//An user-defined function should have a different name than the built-in
	//functions
	//Returns true if the name is unique, otherwise returns false
	private static boolean checkFunctionName(String functionName)
	{
		if(validator.checkNumerical(functionName))
		{
			return false;
		}
		if( (functionName.equalsIgnoreCase(MyConstTokens.CAR)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.CDR)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.CONS)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.EQ)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.ATOM)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.NULL)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.INT)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.COND)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.QUOTE)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.DEFUN)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.FUNC_PLUS)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.SUB)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.TIMES)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.QUOTIENT)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.REMAINDER)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.LESS)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.GREATER)) ||
				(functionName.equalsIgnoreCase(MyConstTokens.TRUE))||
				(functionName.equalsIgnoreCase(MyConstTokens.NIL)))
		{
			return false;
		}
		return true;
	}

	private static void addtodeflist(MySExpr exp) throws IOException
	{
//		System.out.println("11");
		MySExpr temp = cons(car(exp), cons(car(cdr(exp)), car(cdr(cdr(exp)))));
//		System.out.println("12");
		MyDefinitionList.deflist = cons(temp, MyDefinitionList.deflist);
//		System.out.println("13");
		System.out.println("Added successfully !!!");
	}

	private static boolean searchdeflist(MySExpr exp, MySExpr list) throws IOException
	{
//		System.out.println("50");
		if(list.expr.equalsIgnoreCase("NIL") || exp.expr.equalsIgnoreCase("NIL"))
		{
//			System.out.println("51");
			return false;
		}
		if(eq(exp, car(car(list))).expr.equalsIgnoreCase("T"))
		{
//			System.out.println("52");
			return true;
		}
		else
		{
//			System.out.println("53");
			return searchdeflist(exp, cdr(list));
		}
	}

	private static MySExpr apply(MySExpr function, MySExpr aparam, 
			MySExpr alist, MySExpr deflist) throws IOException
	{
		//Compute number of actual params
//		System.out.println("14");
		int numOfParams = findNumParams(aparam);

		if(function.isAtom)
		{
//			System.out.println("15");
			if(function.expr.equalsIgnoreCase(MyConstTokens.CAR))
			{
//				System.out.println("16");
				//Check for num of params
				if(numOfParams ==1 )
				{
//					System.out.println("17");
					return car(car(aparam));
				}
			}
			else if(function.expr.equalsIgnoreCase(MyConstTokens.CDR))
			{
//				System.out.println("18");
				//Check for num of params
				if(numOfParams == 1)
				{
//					System.out.println("19");
					return cdr(car(aparam));
				}
			}
			else if(function.expr.equalsIgnoreCase(MyConstTokens.CONS))
			{
//				System.out.println("20");
				//Check for num of params
				if(numOfParams == 2)
				{
//					System.out.println("21");
					return cons(car(aparam), car(cdr(aparam)));
				}
			}
			else if(function.expr.equalsIgnoreCase(MyConstTokens.ATOM))
			{
//				System.out.println("22");
				//Check for num of params
				if(numOfParams == 1)
				{
//					System.out.println("23");
					return atom(car(aparam));
				}
			}
			else if(function.expr.equalsIgnoreCase(MyConstTokens.EQ))
			{
//				System.out.println("24");
				//Need to check for number of arguments
				if(numOfParams == 2)
				{
//					System.out.println("25");
					return eq(car(aparam), car(cdr(aparam)));
				}
			}
			else if(function.expr.equalsIgnoreCase(MyConstTokens.INT))
			{
//				System.out.println("26");
				//Need to check for number of arguments
				if(numOfParams == 1)
				{
//					System.out.println("27");
					return integer(car(aparam));
				}
			}
			else if(function.expr.equalsIgnoreCase(MyConstTokens.GREATER))
			{
//				System.out.println("28");
				//Need to first check for num of params
				if(numOfParams == 2)
				{
//					System.out.println("29");
					return greater(car(aparam), car(cdr(aparam)));
				}
			}
			else if(function.expr.equalsIgnoreCase(MyConstTokens.LESS))
			{
//				System.out.println("30");
				//Need to first check for num of params
				if(numOfParams == 2)
				{
//					System.out.println("31");
					return less(car(aparam), car(cdr(aparam)));
				}
			}
			else if(function.expr.equalsIgnoreCase(MyConstTokens.NULL))
			{
//				System.out.println("32");
				//Need to first check for num of params
				if(numOfParams == 1)
				{
//					System.out.println("33");
					return nullsexp(car(aparam));
				}
			}
			else if(function.expr.equalsIgnoreCase(MyConstTokens.FUNC_PLUS))
			{
//				System.out.println("34");
				//Need to first check for num of params
				if(numOfParams == 2)
				{
//					System.out.println("35");
					return plus(car(aparam), car(cdr(aparam)));
				}
			}
			else if(function.expr.equalsIgnoreCase(MyConstTokens.SUB))
			{
//				System.out.println("36");
				//Need to first check for num of params
				if(numOfParams == 2)
				{
//					System.out.println("37");
					return minus(car(aparam), car(cdr(aparam)));
				}
			}
			else if(function.expr.equalsIgnoreCase(MyConstTokens.TIMES))
			{
//				System.out.println("38");
				//Need to first check for num of params
				if(numOfParams == 2)
				{
//					System.out.println("39");
					return times(car(aparam), car(cdr(aparam)));
				}
			}
			else if(function.expr.equalsIgnoreCase(MyConstTokens.QUOTIENT))
			{
//				System.out.println("40");
				//Need to first check for num of params
				if(numOfParams == 2)
				{
//					System.out.println("41");
					return quotient(car(aparam), car(cdr(aparam)));
				}
			}
			else if(function.expr.equalsIgnoreCase(MyConstTokens.REMAINDER))
			{
//				System.out.println("42");
				//Need to first check for num of params
				if(numOfParams == 2)
				{
//					System.out.println("43");
					return remainder(car(aparam), car(cdr(aparam)));
				}
			}
			else
			{
				//Check if it is a user-defined function
//				System.out.println("44");
				//Check dlist for definition else error
				if(searchdeflist(function, deflist))
				{
//					System.out.println("45");
					//Check for number of parameters
					if(numParamofUserFunc.get(function.expr) == numOfParams)
					{
//						System.out.println("46");
//						System.out.println("Trying now !!!");
						return eval(cdr(getval(function, deflist)), 
								addpairs(car(getval(function, deflist)), aparam, alist),
								deflist);
						
					}
					else if(numParamofUserFunc.get(function.expr) < numOfParams)
					{
						//ERROR
//						System.out.println("47");
						System.out.println("ERROR: More number of parameters");
//						System.out.println("Trying now again !!!");
						MyInterpreter.check();
						MyInterpreter.main(null);
					}
					else
					{
						//ERROR
//						System.out.println("47");
						System.out.println("ERROR: Less number of parameters");
//						System.out.println("Trying now again !!!");
						MyInterpreter.check();
						MyInterpreter.main(null);
					}
				}
				else
				{
					//ERROR 
//					System.out.println("48");
					System.out.println("ERROR: Unknown User-Defined function");
					MyInterpreter.check();
					MyInterpreter.main(null);
				}
			}

			//If the flow comes over here, it means there is an error
			System.out.println("ERROR: Wrong number of parameters");
			MyInterpreter.check();
			MyInterpreter.main(null);
			return nilSexp;
		}
		else
		{
			return nilSexp;
		}
	}

	//Computes and returns the number of parameters whose tree is rooted at exp
	private static int findNumParams(MySExpr exp)
	{
		MySExpr temp = exp;
		int num = 0;
		
		//Iterate through the param list
		while(temp != null ) 
		{
			if(temp.car != null)
			{
				num++;
			}
			temp = temp.cdr;
		}
		return num;
	}

	private static MySExpr evlist(MySExpr aparam, MySExpr alist, MySExpr dlist) throws IOException
	{
		if(aparam.expr.equalsIgnoreCase("NIL"))
		{
			return nilSexp;
		}
		else
		{
			return cons(eval(car(aparam), alist, dlist), evlist(cdr(aparam), alist, dlist));
		}
	}

	private static MySExpr evcon(MySExpr exp, MySExpr alist, MySExpr dlist) throws IOException
	{
		if(exp.expr.equalsIgnoreCase("NIL"))
		{
			//Error condition, all conditions are nil
			System.out.println("ERROR: All conditionals are false");
			MyInterpreter.check();
			MyInterpreter.main(null);
			return nilSexp;
		}
		else
		{
			if(eval(car(car(exp)), alist, dlist).expr.equalsIgnoreCase("T"))
			{
				//Condition is true
				return eval(car(cdr(car(exp))), alist, dlist);
			}
			else
			{
				return evcon(cdr(exp), alist, dlist);
			}
		}
	}


	//alist is a list of (x.y) pairs, i.e., ((x.y)(a.b)(c.d))
	//Check if exp is in alist, return true if found, else false
	private static boolean bound(MySExpr exp, MySExpr alist) throws IOException
	{
		if(alist == null || exp == null || alist.car == null)
		{
			return false;
		}
		else 
		{
			if(car(alist) != null && car(car(alist)) != null)
			{
				if(exp.expr != null && exp.expr.equalsIgnoreCase((car(car(alist))).expr))
				{
					return true;
				}
				else
				{
					return bound(exp, cdr(alist));
				}
			}
			else
			{
				return false;
			}
		}
	}

	//Find the first (x.y) in alist and returns y
	//Pre-condition: bound(exp, alist) returns true
	private static MySExpr getval(MySExpr exp, MySExpr alist) throws IOException
	{
		//Do I need to check for bound() here?
		if(alist == null)
		{
			//Should not happen because bound() evaluates to true
			return new MySExpr("NIL");
		}
		else
		{
			if(eq(exp, car(car(alist))).expr.equalsIgnoreCase("T"))
			{
				return (cdr(car(alist)));
			}
			else
			{
				return getval(exp, cdr(alist));
			}
		}
	}

	//Returns a new list with pairs (xi.yi) followed by the contents of z
	private static MySExpr addpairs(MySExpr xlist, MySExpr ylist, 
			MySExpr alist) throws IOException
	{
		if(xlist.expr.equalsIgnoreCase("NIL"))
		{
//			System.out.println("Trying inside AddPairs-if !!!");
			return alist;
		}
		else
		{
//			System.out.println("Trying inside AddPairs-else !!!");
			return (cons(cons(car(xlist), car(ylist)), addpairs(cdr(xlist), cdr(ylist), alist)));
		}
	}

	//Return the car of an Sexpression
	private static MySExpr car(MySExpr exp) throws IOException
	{
		if(exp.expr.equalsIgnoreCase("NIL") || exp.expr.equalsIgnoreCase("T"))
		{
			return nilSexp;
		}
		else
		{
			if(exp.isAtom == true)
			{
				System.out.println("ERROR: CAR undefined for an atom" );
				MyInterpreter.check();
				MyInterpreter.main(null);
			}
			return exp.car;
		}
	}

	//Return the cdr of an Sexpression
	private static MySExpr cdr(MySExpr exp) throws IOException
	{
		if(exp.expr.equalsIgnoreCase("NIL") || exp.expr.equalsIgnoreCase("T"))
		{
			return nilSexp;
		}
		else
		{
			if(exp.isAtom == true)
			{
				System.out.println("ERROR: CDR undefined for an atom");
				MyInterpreter.check();
				MyInterpreter.main(null);
			}
			return exp.cdr;
		}
	}

	//Returns true if the input Sexpression is an atom
	private static MySExpr atom(MySExpr exp)
	{
		if(exp.isAtom == true && exp.cdr == null)
		{
			return trueSexp;
		}
		return nilSexp;
	}

	//Returns true/false depending on whether two Sexpressions are equal or not
	//eq should work only on atoms, otherwise error
	private static MySExpr eq(MySExpr exp1, MySExpr exp2) throws IOException
	{
		if(exp1.isAtom == true && exp2.isAtom == true)
		{
			if(exp1.type == MyConstTokens.LITERAL && 
					exp2.type == MyConstTokens.LITERAL)
			{
				if(exp1.expr.equalsIgnoreCase(exp2.expr))
				{
					return trueSexp;
				}			
				else
				{
					return nilSexp;
				}
			}
			else if(exp1.type == MyConstTokens.NUMERICALS && 
					exp2.type == MyConstTokens.NUMERICALS)
			{
				if(exp1.integer == exp2.integer)
				{
					return trueSexp;
				}
				else
				{
					return nilSexp;
				}
			}
			System.out.println("ERROR: eq() is defined only for atoms");
			MyInterpreter.check();
			MyInterpreter.main(null);
			return nilSexp;
		}
		else
		{
			System.out.println("ERROR: eq() is defined only for atoms");
			MyInterpreter.check();
			MyInterpreter.main(null);
			return nilSexp;
		}
	}

	//Returns an Sexpression which is the the cons of two Sexpressions
	private static MySExpr cons(MySExpr exp1, MySExpr exp2)
	{
		MySExpr newExp = new MySExpr(exp1, exp2);
		return newExp;
	}

	//Returns true if the atom is an integer
	private static MySExpr integer(MySExpr exp1) throws IOException
	{
		if(exp1.isAtom == true && exp1.type == MyConstTokens.NUMERICALS)
		{
			return trueSexp;
		}
		else
		{
			System.out.println("ERROR: Not a numeral atom");
			MyInterpreter.check();
			MyInterpreter.main(null);
			return nilSexp;
		}
	}

	//Returns true if exp1 is greater than exp2
	//Works only with numeral atoms
	private static MySExpr greater(MySExpr exp1, MySExpr exp2) throws IOException
	{
		if(exp1.type == MyConstTokens.NUMERICALS && exp2.type == MyConstTokens.NUMERICALS)
		{
			if(exp1.integer > exp2.integer)
			{
				return trueSexp;
			}
			else
			{
				return nilSexp;
			}
		}
		else
		{
			System.out.println("ERROR:greater() function should ve Numericals");
			MyInterpreter.check();
			MyInterpreter.main(null);
			return nilSexp;
		}
	}

	//Returns true if exp1 is smaller than exp2
	//Works only with numeral atoms
	private static MySExpr less(MySExpr exp1, MySExpr exp2) throws IOException
	{
		if(exp1.type == MyConstTokens.NUMERICALS && exp1.type == MyConstTokens.NUMERICALS)
		{
			if(exp1.integer < exp2.integer)
			{
				return trueSexp;
			}
			else
			{
				return nilSexp;
			}
		}
		else
		{
			System.out.println("ERROR: less() function should ve Numericals");
			MyInterpreter.check();
			MyInterpreter.main(null);
			return nilSexp;
		}
	}

	//Returns true if the atom is nil
	private static MySExpr nullsexp(MySExpr exp1)
	{
		if(exp1 != null)//Is this check required?
		{
			if(exp1.isAtom)
			{
				if(exp1.type == MyConstTokens.LITERAL && exp1.expr.equalsIgnoreCase("NIL"))
				{
					return trueSexp;
				}
				else
				{
					return nilSexp;
				}
			}
			else
			{
				return nilSexp;
			}
		}
		else
		{
			return nilSexp;
		}
	}

	//Returns the plus
	private static MySExpr plus(MySExpr exp1, MySExpr exp2) throws IOException
	{
		int sum;

		if(exp1.type == MyConstTokens.NUMERICALS && exp2.type == MyConstTokens.NUMERICALS)
		{
			sum = exp1.integer + exp2.integer;
			return new MySExpr(sum);
		}
		else
		{
			//Need to give error message
			System.out.println("ERROR: plus() function should ve Numericals as Atom type");
			MyInterpreter.check();
			MyInterpreter.main(null);
		}
		return nilSexp;
	}

	//Returns the minus
	private static MySExpr minus(MySExpr exp1, MySExpr exp2) throws IOException
	{
		int minus;

		if(exp1.type == MyConstTokens.NUMERICALS && exp2.type == MyConstTokens.NUMERICALS)
		{
			minus = exp1.integer - exp2.integer;
			return new MySExpr(minus);
		}
		else
		{
			//Need to give error message
			System.out.println("ERROR: minus() function should ve Numericals as Atom type");
			MyInterpreter.check();
			MyInterpreter.main(null);
		}
		return nilSexp;
	}

	//Return the product
	private static MySExpr times(MySExpr exp1, MySExpr exp2) throws IOException
	{
		int product;

		if(exp1.type == MyConstTokens.NUMERICALS && exp2.type == MyConstTokens.NUMERICALS)
		{
			product = exp1.integer * exp2.integer;
			return new MySExpr(product);
		}
		else
		{
			//Need to give error message
			System.out.println("ERROR: times() function should ve Numericals as Atom type");
			MyInterpreter.check();
			MyInterpreter.main(null);
		}
		return nilSexp;
	}

	//Return the quotient
	private static MySExpr quotient(MySExpr exp1, MySExpr exp2) throws IOException
	{
		int quotient;

		if(exp1.type == MyConstTokens.NUMERICALS && exp2.type == MyConstTokens.NUMERICALS)
		{
			quotient = exp1.integer / exp2.integer;
			return new MySExpr(quotient);
		}
		else
		{
			//Need to give error message
			System.out.println("ERROR: quotient() function should ve Numericals as Atom type");
			MyInterpreter.check();
			MyInterpreter.main(null);
		}
		return nilSexp;
	}

	//Return the remainder
	private static MySExpr remainder(MySExpr exp1, MySExpr exp2) throws IOException
	{
		int remainder;

		if(exp1.type == MyConstTokens.NUMERICALS && exp2.type == MyConstTokens.NUMERICALS)
		{
			remainder = exp1.integer % exp2.integer;
			return new MySExpr(remainder);
		}
		else
		{
			//Need to give error message
			System.out.println("ERROR: remainder() function should ve Numericals as Atom type");
			MyInterpreter.check();
			MyInterpreter.main(null);
		}
		return nilSexp;
	}
	
	
	
	
	
}


