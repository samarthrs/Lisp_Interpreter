package com.osu.cse.fpl.lisp;

public class MyParser {

	private static MyStringTokenizer mytokenizer;
	public MySExpr rootExpression;
	private static String inputLine;
	
	static MyInputValidation validator = new MyInputValidation(inputLine);

	//Default Constructor
	public MyParser(String line)
	{
		mytokenizer = new MyStringTokenizer(line); 
		rootExpression = null;
		inputLine = line; 
	}

	public void parseInput() 
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
			int a = 0;
			breakFn(a);
		}
	}

	private static void carParse(MySExpr node)
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
				System.out.println("ERROR: invalid input");
				int a = 0;
				breakFn(a);
			}
			mytokenizer.setCurTokenNull();
		}
	}

	private static void literalParse(MySExpr node)
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

	private static void cdrParse(MySExpr node)
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
				System.out.println("error: unexpected dot / dollar");
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

	private static void parseRoot(MySExpr node)
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
}


