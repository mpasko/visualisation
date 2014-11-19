// This is lexer for J48 grammar
//

lexer grammar Tj48TreeLexer;

options {

   language = Java;
   superClass = AbstractTLexer;
}

@header {
    package agh.aq21gui.j48treegrammar;
    import agh.aq21gui.aq21grammar.AbstractTLexer;
}

DIGRAPH    :   'digraph' ;
LABEL    :   'label' ;
SHAPE    :   'shape' ;
STYLE    :   'style';

OPEN    :   '\{' ;
CLOSE   :   '\}' ;
EQUAL   :   '=' | '<=' | '>=' | '<>' | '>' | '<';
RANGE_OP : '\@' ;
DOT     :   '\.' ;
COLON   :   '\:' ;
COMA    :   '\,' ;
SLASH   :   '\/' ;
PRODUCTION_ARROW  :   '\&' ;
OPEN_SQR    :   '\[' ;
CLOSE_SQR   :   '\]' ;
OPEN_PAR    :   '\(' ;
CLOSE_PAR   :   '\)' ;
//ASTERISK    :   '*' ;
QUOTE : '\"';

// VALUE   :   ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')* ;

ID  :	('a'..'z'|'A'..'Z'|'_'|'-') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')*
	|   '*' ;

//FLOAT  :  '-'? '0'..'9'+ '\.' '0'..'9'+ ;
FLOAT  :  '-'? '0'..'9'+ ('\.' '0'..'9'+ )?;

PERCENT : '0'..'9'+ '\%' ;

//INT :	'-'? '0'..'9'+ ;

WS : WHITESPACE {};

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