/*
 * Author : Samarth Savanur
 */
package com.osu.cse.fpl.lisp;

// class for including all the constants for the interpreter
public final class MyConstTokens {
	
	// including all acceptable atoms
	public static int LITERAL = 0;
	public static int NUMERICALS = 1;
	public static int SPL_CASE = 3;
	
	//defining braces and operators
	public static final String TRUE = "T";
    public static final String NIL = "NIL";
    public static final String LEFT_PARA = "(";
    public static final String RIGHT_PARA = ")";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String DOT = ".";
    public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";

    // defining fundamental tokens & mathematical operators
    public static final String CAR = "CAR";
    public static final String CDR = "CDR";
    public static final String CONS = "CONS";
    public static final String ATOM = "ATOM";
    public static final String NULL = "NULL";
    public static final String INT = "INT";
    public static final String COND = "COND";
    public static final String DEFUN = "DEFUN";
    public static final String FUNC_PLUS = "PLUS";
    public static final String SUB = "MINUS";
    public static final String EQ = "EQ";
    public static final String QUOTE = "QUOTE";
    public static final String TIMES = "TIMES";
    public static final String QUOTIENT = "QUOTIENT";
    public static final String REMAINDER = "REMAINDER";
    public static final String LESS = "LESS"; 
    public static final String GREATER = "GREATER";
    
}

