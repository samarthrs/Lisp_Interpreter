package com.osu.cse.fpl.lisp;

public class MySExpr {
	
	//defining car,cdr of type Sexpr
	public MySExpr car;
    public MySExpr cdr;
    
    // initiating other variables
    public String expr;
    public int integer;
	public boolean isAtom;
    public boolean isList;
    public int type;
    public boolean isNil;

    //generated getters and setters for car and cdr
	public MySExpr getCar() {
		return car;
	}

	public MySExpr getCdr() {
		return cdr;
	}

	public void setCar(MySExpr car) {
		this.car = car;
	}

	public void setCdr(MySExpr cdr) {
		this.cdr = cdr;
	}

	//default constructor with no arguments for the special characters apart from numerical and literals
	/**
	 * @param car
	 * @param cdr
	 * @param expr
	 * @param integer
	 * @param isAtom
	 * @param isList
	 * @param type
	 * @param isNil
	 */
    public MySExpr() {
    	this.car = null;
    	this.cdr = null;
    	this.expr = "";
    	this.integer = -9999;
    	this.isAtom = false;
    	this.isList = false;
    	this.isNil = false;
    	this.type = MyConstTokens.SPL_CASE;	
	}

    // constructor to handle numerical
    /**
	 * @param car
	 * @param cdr
	 * @param expr
	 * @param integer
	 * @param isAtom
	 * @param isList
	 * @param type
	 * @param isNil
	 */
    public MySExpr(int numerical) {
		this.car = null;
		this.cdr = null;
		this.expr = "";
		this.integer = numerical;
		this.isAtom = false;
		this.isList = false;
		this.type = MyConstTokens.NUMERICALS;
		this.isNil = false;
	}

    // constructor to handle literals
    /**
	 * @param car
	 * @param cdr
	 * @param expr
	 * @param integer
	 * @param isAtom
	 * @param isList
	 * @param type
	 * @param isNil
	 */
	public MySExpr(String expr) {
		
		this.expr = expr;
		this.integer = -9999;
		this.isAtom = true;
		this.type = MyConstTokens.LITERAL;
		this.car = null;
		this.cdr = null;
		this.isList = false;
		
		//checking for null value of the expression
		if(expr.equalsIgnoreCase("NIL"))
		{
			this.isNil = true;
		}
		else
		{
			this.isNil = false;
		}
		
	}

    //constructor to handle car & cdr
    /**
	 * @param car
	 * @param cdr
	 * @param expr
	 * @param integer
	 * @param isAtom
	 * @param isList
	 * @param type
	 * @param isNil
	 */
	public MySExpr(MySExpr car, MySExpr cdr) {
		this.car = car;
		this.cdr = cdr;
		this.expr = "";
		this.integer = -9999;
		this.isAtom = false;
		this.isList = false;
		this.type = MyConstTokens.SPL_CASE;
		this.isNil = false;
	}
}


