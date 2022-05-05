package myCompiler;

import java.util.*;
import java.io.*;
import java.lang.*;

public class MainCompiler {
	
	// the lexical analyzer and syntax analyzer are implemented
	// as static methods which are called from inside the main method and save their output in 
	// different text files. The output file from lexical analyzer is the input to syntax analyzer.
	public static int NumOfInputs = 0, NumOfOutputs = 0; //to keep track of READ and WRITE statements for user IO
	public static String[] Inputs = new String[10];
	public static String[] Outputs = new String[10];
	
	
	static void lexicalAnalyzer(String s) {
		// output is the list of tokens with their respective classes to be saved in lexoutput.txt and 
		// the symbol table to be saved in symboltable.txt
		try {
			
			FileWriter writer = new FileWriter(new File("lexoutput.txt"));
			writer.write("Token \t\t Classification \n");					
			String[] keywords = {"CLASS","CONST","VAR","IF","THEN","ELSE","PROCEDURE","WHILE","CALL","DO","ODD","READ","WRITE"};
			
			int[][] TokenStateTable = 
				{
						
				/*	L	D	*	/	=	<	>	b	+	-	!	,	;	{	}	(	)	other	*/
		/* 0 */ 	{	5,	3,	2,	7,	11,	14,	17,	0,	20,	21,	22,	24,	25,	26,	27,	28,	29,	1},
		/* 1 */ 	{	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1},		//Error
		/* 2 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//mul_op *
		/* 3 */ 	{	4,	3,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4},
		/* 4 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//Integer
		/* 5 */ 	{	5,	5,	6,	6,	6,	6,	6,	6,	6,	6,	6,	6,	6,	6,	6,	6,	6,	6},
		/* 6 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//Variable
		/* 7 */ 	{	10,	10,	8,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10},
		/* 8 */ 	{	8,	8,	9,	8,	8,	8,	8,	8,	8,	8,	8,	8,	8,	8,	8,	8,	8,	8},
		/* 9 */ 	{	8,	8,	8,	0,	8,	8,	8,	8,	8,	8,	8,	8,	8,	8,	8,	8,	8,	8},
		/* 10 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//mul_op /
		/* 11 */ 	{	12,	12,	12,	12,	13,	12,	12,	12,	12,	12,	12,	12,	12,	12,	12,	12,	12,	12},
		/* 12 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//assignment_op
		/* 13 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//equivalence_op
		/* 14 */ 	{	15,	15,	15,	15,	16,	15,	15,	15,	15,	15,	15,	15,	15,	15,	15,	15,	15,	15},
		/* 15 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//LessRel_op
		/* 16 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//LessEquelsRel_op
		/* 17 */ 	{	18,	18,	18,	18,	19,	18,	18,	18,	18,	18,	18,	18,	18,	18,	18,	18,	18,	18},
		/* 18 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//GreaterRel_op
		/* 19 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//GreaterEquelsRel_op
		/* 20 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//add_op +
		/* 21 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//add_op -	
		/* 22 */ 	{	1,	1,	1,	1,	23,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1,	1},
		/* 23 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//ineq_op !=
		/* 24 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//comma
		/* 25 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//semicolon
		/* 26 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//left bracket LB
		/* 27 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//right bracket RB
		/* 28 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//left parenthesis LP
		/* 29 */ 	{	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},		//right parenthesis RP
					
			};
						
			int column = -1;
			int state = 0;
			String temp = new String();
			for(int i=0; i<s.length(); i++) {
				char ch = s.charAt(i);
					
				if(Character.isAlphabetic(ch))
					column = 0;
				else if(Character.isDigit(ch))
					column = 1;
				else if(ch == '*')
					column = 2;
				else if(ch == '/')
					column = 3;
				else if(ch == '=')
					column = 4;
				else if(ch == '<')
					column = 5;
				else if(ch == '>')
					column = 6;
				else if(Character.isWhitespace(ch))
					column = 7;
				else if(ch == '+')
					column = 8;
				else if(ch == '-')
					column = 9;
				else if(ch == '!')
					column = 10;
				else if(ch == ',')
					column = 11;
				else if(ch == ';')
					column = 12;
				else if(ch == '{')
					column = 13;
				else if(ch == '}')
					column = 14;
				else
					column = 15;
				
				//moving to the next state
				state = TokenStateTable[state][column];
				boolean isKeyword = false;
				
				//writing all the tokens and their classes in lexoutput.txt file
				switch(state) {
				case 2: writer.write("* \t\t mop \n");
				state=0;
				break;
				case 3: writer.write(ch);
				break;
				case 4: writer.write(" \t\t integer \n");
				state=0;
				break;
				case 5: writer.write(ch);
				temp = temp+ch;
				break;
				case 6: 
					for(int n=0; n<13; n++) {
					if(keywords[n].equals(temp)) {
						writer.write(" \t\t $"+ keywords[n] +" \n");
						isKeyword = true;
						temp = "";
					}
					}
					if(isKeyword==false) {
						writer.write(" \t\t variable \n");
						temp = "";
					}
				state=0;
				break;
				case 10: writer.write("/ \t\t mop \n");
				state=0;
				break;
				case 12: writer.write("= \t\t assign \n");
				state=0;
				break;
				case 13: writer.write("== \t\t relop \n");
				state=0;
				break;
				case 15: writer.write("< \t\t relop \n");
				state=0;
				break;
				case 16: writer.write("<= \t\t relop \n");
				state=0;
				break;
				case 18: writer.write("> \t\t relop \n");
				state=0;
				break;
				case 19: writer.write(">= \t\t relop \n");
				state=0;
				break;
				case 20: writer.write("+ \t\t addop \n");
				state=0;
				break;
				case 21: writer.write("- \t\t addop \n");
				state=0;
				break;
				case 23: writer.write("!= \t\t relop \n");
				state=0;
				break;
				case 24: writer.write(", \t\t comma \n");
				state=0;
				break;
				case 25: writer.write("; \t\t semicolon \n");
				state=0;
				break;
				case 26: writer.write("{ \t\t LB \n");
				state=0;
				break;
				case 27: writer.write("} \t\t RB \n");
				state=0;
				break;
				case 28: writer.write("( \t\t LP \n");
				state=0;
				break;
				case 29: writer.write(") \t\t RP \n");
				state=0;
				break;
				}								
			}
			writer.write("EOF \t\t $EOF \n");
			writer.close();
			
			
			
			
			
			// Symbol table by doing FSA through lexoutput.txt
			
			BufferedReader readlex = new BufferedReader(new FileReader("lexoutput.txt"));
			
			FileWriter Swriter = new FileWriter(new File("symboltable.txt"));
			Swriter.write("Token\t\tClass\t\t\tValue\t\tAddress\t\tSegment \n");
			
			int[][] SymbolStateTable = {
					
				/*	CLASS		variable		{	CONST		VAR	=	Integer		,	;	EOF	other	*/
		/* 0 */		{	1,		10,			10,	4,		8,	13,	10,		10,	10,	10,	10},
		/* 1 */		{	13,		2,			13,	13,		13,	13,	13,		13,	13,	13,	13},
		/* 2 */		{	13,		13,			3,	13,		13,	13,	13,		13,	13,	13,	13},
		/* 3 */ 	{	13,		10,			13,	4,		8,	13,	13,		13,	13,	10,	13},
		/* 4 */		{	13,		5,			13,	13,		13,	13,	13,		13,	13,	13,	13},
		/* 5 */		{	13,		13,			13,	13,		13,	6,	13,		13,	13,	13,	13},
		/* 6 */		{	13,		13,			13,	13,		13,	13,	7,		13,	13,	13,	13},
		/* 7 */ 	{	13,		13,			13,	13,		13,	13,	13,		4,	3,	13,	13},	//add constant to symbol table
		/* 8 */		{	13,		9,			13,	13,		13,	13,	13,		13,	13,	13,	13},
		/* 9 */		{	13,		13,			13,	13,		13,	13,	13,		8,	3,	13,	13},	//add variable to symbol table
		/* 10 */	{	10,		10,			10,	10,		10,	10,	11,		10,	3,	12,	10},
		/* 11 */ 	{	10,		10,			10,	10,		10,	10,	10,		10,	10,	12,	10},	//add integer to symbol table
		/* 12 */ 	{	12,		12,			12,	12,		12,	12,	12,		12,	12,	12,	12},
		/* 13 */ 	{	13,		13,			13,	13,		13,	13,	13,		13,	13,	13,	13},
					
			};
				
		
		class Token{
			String Token_name;
			String Category;
			public Token(String token_name, String category) {
				super();
				Token_name = token_name;
				Category = category;
			}				
		}
		
			
		int columnST = -1;
		int stateST = 0;
		int DScounter = 0;
		
			String line = readlex.readLine();
			line = readlex.readLine();
			String tempToken = new String();
			
			while(line != null) {
				String[] x = line.split(" \\t\\t ");
				Token tk = new Token(x[0].trim(),x[1].trim());			
				line = readlex.readLine();
				
				//System.out.print(tk.Token_name+"\t"+tk.Category+"\n");
				
				if(tk.Category.equals("$CLASS"))
					columnST = 0;
				else if(tk.Category.equals("variable"))
					columnST = 1;
				else if(tk.Category.equals("LB"))
					columnST = 2;
				else if(tk.Category.equals("$CONST"))
					columnST = 3;
				else if(tk.Category.equals("$VAR"))
					columnST = 4;
				else if(tk.Category.equals("assign"))
					columnST = 5;
				else if(tk.Category.equals("integer"))
					columnST = 6;
				else if(tk.Category.equals("comma"))
					columnST = 7;
				else if(tk.Category.equals("semicolon"))
					columnST = 8;
				else if(tk.Category.equals("$EOF"))
					columnST = 9;
				else
					columnST = 10;
				
				// move to the next state
				stateST = SymbolStateTable[stateST][columnST];
				
				//System.out.println(tk.Category+"\t"+tk.Token_name+"\t"+stateST);
				
				switch(stateST) {
				case 2:
					Swriter.write(tk.Token_name+"\t\t"+"$ProgramName\t\t\t\t0\t\tCS \n");
					break;
				case 5:
					tempToken = tk.Token_name;
					break;
				case 7:
					Swriter.write(tempToken+"\t\tConstVar\t\t"+tk.Token_name+"\t\t"+DScounter+"\t\tDS\n");
					DScounter +=2;
					break;
				case 9:
					Swriter.write(tk.Token_name+"\t\tVar\t\t\t?\t\t"+DScounter+"\t\tDS\n");
					DScounter +=2;
					break;
				case 11:
					Swriter.write("Lit"+tk.Token_name+"\t\t$NumLit"+"\t\t\t"+tk.Token_name+"\t\t"+DScounter+"\t\tDS\n");
					DScounter +=2;
					break;
				}
			}
			
			// adding ten entries for temporaries to the symbol table 
			for(int count=1; count<=10; count++) {
				Swriter.write("T"+ count + "\t\t\t\t\t?\t\t" + DScounter +"\t\tDS \n");
				DScounter +=2;
			}
						

			Swriter.close();
			readlex.close();			

		}catch(IOException e) {
			System.out.println("Error occurred.");
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	static void Parser(String s) {
		//output from here are the quads which are saved in quads.txt
		try {
			
			BufferedReader readST = new BufferedReader(new FileReader("symboltable.txt"));
			BufferedReader readpgm = new BufferedReader(new FileReader(s));
			FileWriter quadwriter = new FileWriter(new File("quads.txt"));
			
			// Precedence Table for pushdown automata is stored in a two dimensional character array
			// < for yields, > for takes precedence, = for equal precedence and ! for no relation.
		    
		    char[][] PreTable = {
		    	/*	T	=	+	-	(	)	*	/	IF	THEN	==	!=	>	<	>=	<=	{	}	ELSE	*/
	/* T */		{	'!',	'<',	'!',	'!',	'!',	'!',	'!',	'!',	'<',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!'},
	/* = */		{	'>',	'>',	'<',	'<',	'<',	'!',	'<',	'<',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'>'},
	/* + */		{	'>',	'>',	'>',	'>',	'<',	'>',	'<',	'<',	'!',	'>',	'>',	'>',	'>',	'>',	'>',	'>',	'!',	'!',	'>'},
	/* - */		{	'>',	'!',	'>',	'>',	'<',	'>',	'<',	'<',	'!',	'>',	'>',	'>',	'>',	'>',	'>',	'>',	'!',	'!',	'>'},
	/* ( */		{	'!',	'!',	'<',	'<',	'<',	'=',	'<',	'<',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'>'},
	/* ) */		{	'>',	'!',	'>',	'>',	'!',	'>',	'>',	'>',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'>'},
	/* * */		{	'>',	'!',	'>',	'>',	'<',	'>',	'>',	'>',	'!',	'>',	'>',	'>',	'>',	'>',	'>',	'>',	'!',	'!',	'>'},
	/* / */		{	'>',	'!',	'>',	'>',	'<',	'>',	'>',	'>',	'!',	'>',	'>',	'>',	'>',	'>',	'>',	'>',	'!',	'!',	'>'},
	/* IF */	{	'>',	'!',	'<',	'<',	'<',	'!',	'<',	'<',	'!',	'=',	'<',	'<',	'<',	'<',	'<',	'<',	'!',	'!',	'='},
	/* THEN */	{	'>',	'<',	'!',	'!',	'!',	'!',	'!',	'!',	'<',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'<',	'!',	'='},
	/* == */	{	'!',	'!',	'<',	'<',	'<',	'!',	'<',	'<',	'!',	'>',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!'},
    	/* != */	{	'!',	'!',	'<',	'<',	'<',	'!',	'<',	'<',	'!',	'>',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!'},
	/* > */		{	'!',	'!',	'<',	'<',	'<',	'!',	'<',	'<',	'!',	'>',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!'},
	/* < */		{	'!',	'!',	'<',	'<',	'<',	'!',	'<',	'<',	'!',	'>',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!'},
	/* >= */	{	'!',	'!',	'<',	'<',	'<',	'!',	'<',	'<',	'!',	'>',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!'},
	/* <= */	{	'!',	'!',	'<',	'<',	'<',	'!',	'<',	'<',	'!',	'>',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!'},
	/* { */		{	'>',	'<',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'<'},
	/* } */		{	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!'},
	/* ELSE	*/	{	'!',	'<',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!',	'!'}
		    };
		    
		    String[] PreTokens = {"Terminator" , "=" , "+" , "-" , "(" , ")" , "*" , "/" , "IF" , "THEN" , "==" , "!=" , ">" , "<" , ">=" , "<=" , "{" , "}" , "ELSE"};

		    
			String stmt; // this string fetches and stores all executable statements for applying to the push down automata 
			String more; //	this string reads each new line from the source file
			String IOstmt;
			
				stmt = new String("Terminator");
				more = new String(" ");
			    while(more != null) {
			    	more = readpgm.readLine();
			    	if (more == null)
			    		break;
					more = more.trim();
					if(more.startsWith("CLASS")||more.startsWith("CONST")||more.startsWith("VAR")||more.startsWith("READ")||more.startsWith("WRITE")) {
						if (more.startsWith("READ")) {
							IOstmt = more.substring(5);
							IOstmt = IOstmt.replaceAll(",", " ");
							IOstmt = IOstmt.replaceAll(";", " ");
							Inputs[NumOfInputs++] = IOstmt;
						}
						else if (more.startsWith("WRITE")) {
							IOstmt = more.substring(6);
							IOstmt = IOstmt.replaceAll(",", " ");
							IOstmt = IOstmt.replaceAll(";", " ");
							Outputs[NumOfOutputs++] = IOstmt;
						}
					    continue;	// the lines which are not executable statements for PDA are skipped
					}
					stmt = stmt.concat(" "+more);
			    }

			    
			    
			    
			    
			    stmt = stmt.replaceAll("}", " ");
			    stmt = stmt.replaceAll(";", ";~");
			    stmt = stmt.trim();
			    stmt = stmt.substring(0, stmt.length()-1);
			    String[] multistmt = stmt.split("~");
		for(String sm: multistmt) // each iteration gets one executable statement
		{	//------------------------------------------------------------------------			    
				if(!sm.startsWith("Terminator"))
					sm = "Terminator "+sm;
			    stmt = sm;
			    
			    
			    stmt = stmt.replace(';',' ');
			    stmt = stmt.substring(0, stmt.length()-1);
			    stmt += " Terminator";

				readpgm.close();
			    
			String[] tokensForPDA = stmt.split("\\s+"); // this has all the tokens from all the executable statements
			String[] stack = new String[20]; // this holds the stack entries in the pushdown automata

			
			// implementing push down automata
			int Scount = 0, Pindex = 0;
			stack[Scount++] = tokensForPDA[Pindex++];
			
			// can use up to ten temporary variables when needed
			String[] tempArray = {"T1","T2","T3","T4","T5","T6","T7","T8","T9","T10"};
			int tempArrayIndex = 0;
			int m=0, n=0;	//variable for loop structures below
			
			while(true) // each iteration for a new token push or a pop
			{	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				if(Pindex < tokensForPDA.length) {	// there are still tokens left, that can be pushed
					String currentToken = tokensForPDA[Pindex]; 
					
					//System.out.println(currentToken);
					// we check and if the incoming token is a non terminal, we directly push it into the stack
					// if it is a terminal we check in the precedence table for precedence relation
					boolean nonterminal = true;
					for(n=0; n<PreTokens.length; n++) {
						if(currentToken.equals(PreTokens[n])) {
							nonterminal = false;
						}
					}
					
					if(nonterminal == true) {
						stack[Scount++] = tokensForPDA[Pindex++];
					}
					else{
						String stacktopToken = stack[Scount-1];	// topmost token on the stack
						// we find the topmost terminal token from the stack and save it in stacktopToken variable
						m=Scount-1;
						L1:while(m != -1) {
					    	for(n=0; n<PreTokens.length; n++) {
								if(stack[m].equals(PreTokens[n])) {
									stacktopToken = PreTokens[n];
									break L1;
								}
							}
							m--;
						}
	 
						
						int row = -1, column = -1;
						
						String sToken = stacktopToken;
						int indexOfToken = -1;
						for(n=0; n<PreTokens.length; n++) {
							if(sToken.equals(PreTokens[n]))
								indexOfToken = n;
						}
						row = indexOfToken;
						
						sToken = currentToken;
						indexOfToken = -1;
						for(n=0; n<PreTokens.length; n++) {
							if(sToken.equals(PreTokens[n]))
								indexOfToken = n;
						}
						column = indexOfToken;

						
						if(PreTable[row][column] =='<' || PreTable[row][column] =='=') {
							stack[Scount++] = tokensForPDA[Pindex++];	// next token from code is pushed into the stack
						}
						else if(PreTable[row][column] =='>')	// pop from the stack
							{		
							
							if(stack[Scount-2].equals("=")) {	// assignment is a unary operator, must be dealt separately
								quadwriter.write(stack[Scount-2] +"\t"+ stack[Scount-3] +"\t"+ stack[Scount-1] +"\t"+ "!" +"\n");
								stack[--Scount] = new String();
								stack[--Scount] = new String();
								stack[--Scount] = "PARSE-COMPLETE";
								Scount++;
							}
							
							else if((stack[Scount-3].equals("(") && stack[Scount-1].equals(")")) || (stack[Scount-3].equals("{") && stack[Scount-1].equals("}")) ) {	// in case of closing braces, removed from stack
								stack[Scount-3] = stack[Scount-2];
								stack[--Scount] = new String();
								stack[--Scount] = new String();								
							}
							
							else {
								quadwriter.write(stack[Scount-2] +"\t"+ stack[Scount-3] +"\t"+ stack[Scount-1] +"\t"+ tempArray[tempArrayIndex]+"\n");
								stack[--Scount] = new String();
								stack[--Scount] = new String();
								stack[--Scount] = tempArray[tempArrayIndex++];
								Scount++;
							}				
							
							}
						else if(PreTable[row][column] =='!') {
							System.out.print("Syntax Error as precedence "+row+" "+column+" ");
							System.out.println("between "+PreTokens[row]+" and "+PreTokens[column]+" precedence is"+ PreTable[row][column]);
						}
					}
					
				}
				
				//*/
				// to check the contents of the push down automata stack during each iteration of loop				
				for(String s1: stack) {
					if(s1 != null)
						System.out.print(s1+" ");
				}				
				System.out.println();
				//*/
				
				if(Pindex == tokensForPDA.length-1 && Scount == 2) {
					break;
				}					
			}	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			}	//------------------------------------------------------------------------
			
			quadwriter.close();
			readST.close();
			
		}catch(IOException e) {
			System.out.println("Error occurred.");
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	static void AssemblyCodeAndIO() {
		//output is saved in assemblycode.asm
		
		try {
		FileWriter asmwriter = new FileWriter(new File("assemblycode.asm"));
		BufferedReader readIOFile = new BufferedReader(new FileReader("IO.txt")); // this file has IO subroutines in assembly
		BufferedReader readST = new BufferedReader(new FileReader("symboltable.txt"));
		BufferedReader readST1 = new BufferedReader(new FileReader("symboltable.txt"));
		BufferedReader readQuads = new BufferedReader(new FileReader("quads.txt"));
		
		/* we read from IO.txt file to copy-paste the IO subroutines directly to the assembly code file */
		String ioline = readIOFile.readLine();
		int linecounter = 1;
		while(linecounter!=23){
			asmwriter.write(ioline+"\n");
			ioline =readIOFile.readLine();
			linecounter++;
		}
		
		// reading initialized data from Symbol table and writing in assembly code	
		asmwriter.write("\n");
		String stline = readST.readLine();
		while(stline!=null) {
			String[] STelements = stline.split("\\s+");
			if(STelements[1].equals("ConstVar") || STelements[1].equals("$NumLit")) {
				asmwriter.write("\t"+STelements[0]+"\tDW\t"+STelements[2]+"\n");
			}
			stline = readST.readLine();
		}		
		asmwriter.write("\n");
		
		
		// reading again from IO.txt file
		while(linecounter!=32){
			asmwriter.write(ioline+"\n");
			ioline =readIOFile.readLine();
			linecounter++;
		}

		
		// reading uninitialized data from Symbol table and writing in assembly code	
		asmwriter.write("\n");
		stline = readST1.readLine();
		while(stline!=null) {
			String[] STelements = stline.split("\\s+");
			if(STelements[1].equals("Var")||STelements[1].equals("?")) {
				asmwriter.write("\t"+STelements[0]+"\tRESB\t"+"1"+"\n");
			}
			stline = readST1.readLine();
		}		
				
		asmwriter.write("\n");
		
		// reading again from IO.txt file
		while(linecounter!=37){
			asmwriter.write(ioline+"\n");
			ioline =readIOFile.readLine();
			linecounter++;
			}
		
			String inputAssembly = new String();
		while(linecounter!=41) {
			inputAssembly = inputAssembly.concat(ioline+"\n");
			ioline =readIOFile.readLine();
			linecounter++;
		}
		
		int count = 0;
		while(count < NumOfInputs) {
			String inputAssemblyforthis = inputAssembly.replaceAll("INPUT",Inputs[count++].trim());
			if(count>1)
				inputAssemblyforthis = inputAssemblyforthis.replaceAll("Again:", "\t");
			asmwriter.write(inputAssemblyforthis+"\n");;
		}

		asmwriter.write("\n");
		//generating assembly code from quads
		String line = readQuads.readLine();
		while(line!=null) {
			String[] quadelements = line.split("\\s+"); 
			//asmwriter.write("current quad is"+line+"\n");//for testing
			
			switch(quadelements[0]) {
			case "+":
				asmwriter.write("\tmov ax, ["+ quadelements[1] +"]\n");
				asmwriter.write("\tadd ax, ["+ quadelements[2] +"]\n");
				asmwriter.write("\tmov ["+ quadelements[3] +"], ax\n");
				break;
			case "-":
				asmwriter.write("\tmov ax, ["+ quadelements[1] +"]\n");
				asmwriter.write("\tsub ax, ["+ quadelements[2] +"]\n");
				asmwriter.write("\tmov ["+ quadelements[3] +"], ax\n");
				break;
			case "*":
				asmwriter.write("\tmov ax, ["+ quadelements[1] +"]\n");
				asmwriter.write("\tmul word, ["+ quadelements[2] +"]\n");
				asmwriter.write("\tmov ["+ quadelements[3] +"], ax\n");
				break;
			case "/":
				asmwriter.write("\tmov dx, 0\n");
				asmwriter.write("\tmov ax, ["+ quadelements[1] +"]\n");
				asmwriter.write("\tmov bx, ["+ quadelements[2] +"]\n");
				asmwriter.write("\tdiv bx\n");
				asmwriter.write("\tmov ["+ quadelements[3] +"], ax\n");
				break;
			case "=":
				asmwriter.write("\tmov ax, ["+ quadelements[2] +"]\n");
				asmwriter.write("\tmov ["+ quadelements[1] +"], ax\n");
				break;
			}
			asmwriter.write("\n");
			line = readQuads.readLine();
		}
		
		String outputAssembly = new String();
		while(linecounter!=52) {
		outputAssembly = outputAssembly.concat(ioline+"\n");
		ioline =readIOFile.readLine();
		linecounter++;
		}
	
		count = 0;
		while(count < NumOfOutputs) {
			String outputAssemblyforthis = outputAssembly.replaceAll("OUTPUT",Outputs[count++].trim());
			asmwriter.write(outputAssemblyforthis+"\n");
		}
		
		
		// reading again from IO.txt file
			while(ioline!=null){
				asmwriter.write(ioline+"\n");
				ioline =readIOFile.readLine();
				linecounter++;
				}	
		
		readQuads.close();
		asmwriter.close();
		readST.close();
		readST1.close();
		readIOFile.close();
		}catch(IOException e) {
			System.out.println("an error occurred during code generation.");
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		System.out.print("Enter the name of the source file: ");
		Scanner input = new Scanner(System.in);
		String s = input.next();
		input.close();
		
		String program = new String();
		// Read the source code file into string named program.
		try(BufferedReader reader = new BufferedReader(new FileReader(s))) {
			{
				String line = reader.readLine();
				while(line != null) {
				    program = program.concat(line);
				    line = reader.readLine();
				    program = program.concat(" ");
				}
			}
		}
		
		// Call to lexical analyzer
		lexicalAnalyzer(program);
		
		// Call to Syntax analyzer and Semantic analyzer
		Parser(s);
		
		// Call to assembly Code creator 
		AssemblyCodeAndIO();
		
	}
}
