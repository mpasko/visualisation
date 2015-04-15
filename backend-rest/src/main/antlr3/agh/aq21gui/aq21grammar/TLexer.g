// This is lexer for AQ21 grammar
//

lexer grammar TLexer;

options {

   language=Java;
   superClass = AbstractTLexer;
}

@header {

    package agh.aq21gui.aq21grammar;
}

DESCRIPTION    :   'description'    ;
ATTRIBUTES    :   'attributes'    ;
DOMAINS    :   'domains'    ;
RUNS    :   'runs'    ;
TESTS	:	'tests'		;
OUTPUT_HYPOTHESES    :   'output_hypotheses'    ;
INPUT_HYPOTHESES    :   'input_hypotheses'    ;
EVENTS	:	'events'	;
TESTING_EVENTS	:	'testing_events'	;

EPSILON : 'epsilon' ;
COST : 'cost';

RESULT_NAME : 'lef_star' | 'lef_partial_star' | 'lef_sort' ;

OPEN    :   '\{' ;
CLOSE   :   '\}' ;
EQUAL   :   '=' | '<=' | '>=' | '<>' | '<' | '>' ;
RANGE_OP : '\@' ;
DOT     :   '\.' ;
COLON   :   '\:' ;
COMA    :   '\,' ;
SLASH   :   '\/' ;
RULE_ARROW  :   '<--' ;
OPEN_SQR    :   '\[' ;
CLOSE_SQR   :   '\]' ;
//ASTERISK    :   '*' ;

// VALUE   :   ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')* ;

ID  : '-'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')*
	|   '*' 
    |   '\?'
    |   'N\/A';

//FLOAT  :  '-'? '0'..'9'+ '\.' '0'..'9'+ ;
FLOAT  :  '-'? '0'..'9'+ ('\.' '0'..'9'+ )?;

PERCENT : '0'..'9'+ '\%' ;

//INT :	'-'? '0'..'9'+ ;

WS : WHITESPACE {skip();};

NL : NEWLINE (NEWLINE | WHITESPACE | COM)* ;

COMMENT : COM {skip();};

fragment
COM
    :   ('#' (~NEWLINE)*
    |   '(#' ( options {greedy=false;} : . )* '#)' )
    ;

fragment
NEWLINE : ('\r' | '\n')
		;

fragment
WHITESPACE  :   ( 
	' ' | '\t'
        )
    ;