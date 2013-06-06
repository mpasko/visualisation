// This is a sample lexer generated by the ANTLR3 Maven Archetype
// generator. It shows how to use a superclass to implement
// support methods and variables you may use in the lexer actions
// rather than create a messy lexer that has the Java code embedded
// in it.
//

lexer grammar TLexer;

options {

   language=Java;  // Default

   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and 
   // variables will be placed.
   //
   superClass = AbstractTLexer;
}

// What package should the generated source exist in?
//
@header {

    package agh.aq21gui.aq21grammar;
}

// This is just a simple lexer that matches the usual suspects
//

// KEYSER : 'Keyser' ;
// SOZE   : 'Soze' ;

// ADD : '+' ;
// SEMI: ';' ;

DESCRIPTION    :   'Description'    ;
ATTRIBUTES    :   'attributes'    ;
DOMAINS    :   'domains'    ;
RUNS    :   'runs'    ;
OUTPUT_HYPOTHESES    :   'Output_Hypotheses'    ;

EPSILON : 'epsilon' ;
COST : 'cost';

RESULT_NAME : 'LEF_star' | 'LEF_partial_star' | 'LEF_sort' ;

OPEN    :   '\{' ;
CLOSE   :   '\}' ;
EQUAL   :   '=' | '<=' | '>=' | '<>';
DOT     :   '\.' ;
COLON   :   '\:' ;
COMA    :   '\,' ;
SLASH   :   '\/' ;
RULE_ARROW  :   '<--' ;
OPEN_SQR    :   '\[' ;
CLOSE_SQR   :   '\]' ;
//ASTERISK    :   '*' ;

// VALUE   :   ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')* ;

ID  :	('a'..'z'|'A'..'Z'|'_'|'-') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')*
	|   '*' ;

FLOAT  :  '0'..'9'+ ( '\.' '0'..'9'+ )? ;

PERCENT : '0'..'9'+ '\%' ;

// INT :	'0'..'9'+ ;

WS  :   ( ' '
        | '\t'
		| NEWLINE
        ) {skip();}
    ;

COMMENT
    :   ('#' (~NEWLINE)*
    |   '(#' ( options {greedy=false;} : . )* '#)' ){skip();}
    ;

fragment
NEWLINE : ('\r' | '\n')
		;

// STRING
//    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
//    ;

// fragment
// HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

// fragment
// ESC_SEQ
//     :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
//     |   UNICODE_ESC
//     |   OCTAL_ESC
//     ;

// fragment
// OCTAL_ESC
//     :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
//     |   '\\' ('0'..'7') ('0'..'7')
//     |   '\\' ('0'..'7')
//     ;

// fragment
// UNICODE_ESC
//     :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
//     ;
// 