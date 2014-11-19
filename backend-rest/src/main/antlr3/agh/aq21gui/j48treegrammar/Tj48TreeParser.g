// This is parser for J48 grammar
//

parser grammar Tj48TreeParser;

options {

    language  = Java;
    output    = AST;
    superClass = AbstractTParser;
    tokenVocab = Tj48TreeLexer;
	rewrite = true;

	//In case of trouble -shut off:
	ASTLabelType = CommonTree;
}

// Some imaginary tokens for tree rewrites
//
tokens {
    NULL;
    TREE;
    NODE;
    BRANCH;
}

// What package should the generated source exist in?
//
@header {
    package agh.aq21gui.j48treegrammar;
    import agh.aq21gui.aq21grammar.AbstractTLexer;
    import agh.aq21gui.aq21grammar.AbstractTParser;
}

output : NL? DIGRAPH WS? ID WS? OPEN NL? element* CLOSE NL* EOF
 -> ^(TREE element*) ;

element : node NL
 -> node
 | branch NL
 -> branch;

node : name=ID WS OPEN_SQR node_desc CLOSE_SQR
 -> ^(NODE $name node_desc) ;

branch : from=ID PRODUCTION_ARROW to=ID branch_desc?
 -> ^(BRANCH $from $to branch_desc?) ;

node_desc : LABEL EQUAL QUOTE claz=ID garbage*
 -> $claz;

branch_desc : WS? OPEN_SQR LABEL EQUAL QUOTE condition=EQUAL WS? value=FLOAT QUOTE CLOSE_SQR
 -> $condition $value;

garbage : WS | FLOAT | OPEN_PAR | CLOSE_PAR
 | SLASH | QUOTE | ID | EQUAL | SHAPE | STYLE;
