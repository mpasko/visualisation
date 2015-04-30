// This is parser for AQ21 grammar
//

parser grammar TParser;

options {

    language  = Java;
    output    = AST;
    superClass = AbstractTParser;
    tokenVocab = TLexer;
	rewrite = true;

	//In case of trouble -shut off:
	ASTLabelType = CommonTree;
}

// Some imaginary tokens for tree rewrites
//
tokens {
    NULL;
	START;
	OUTPUT;
	HYPOTHESIS;
	HYPOTHESIS_BODY;
	HYPOTHESES;
	DESCRIPTIONS;
	DOMAINS;
	ATTRIBUTES;
	RUNS;
	CLASSES;
	CLASS_DESCRIPTION;
	RULE;
	RULE_PARAMS;
	SELECTOR;
	SELECTORS;
	SELECTOR_PARAMS;
	VALUE;
	PARAMETER;
	DOMAIN;
	DOMAIN_ARGS;
	ATTRIBUTE;
	ATTRIBUTE_ARGS;
	RUNS_PARAMS;
	RUNS_LIST;
	RUN;
	RUN_SPEC_PARAMS;
	RESULTS;
	RESULT;
	EVENTS;
	ROW;
	CELL;
	DOMAIN_ARGS_BRACE;
	ATTRIBUTE_ARGS_BRACE;
	DOMAIN_ARGS_EMPTY;
	ATTRIBUTE_ARGS_EMPTY;
	TESTS;
	TESTS_PARAMS;
	TESTS_LIST;
	TEST;
	TESTING_EVENTS;
	INPUT_HYPOTHESES;
	RANGE;
	VALUE_SET;
}

// What package should the generated source exist in?
//
@header {

    package agh.aq21gui.aq21grammar;
}

//output : output_items EOF -> ^(START output_items) ;

output : NL? output_item* EOF
 -> ^(OUTPUT output_item*) ;

output_item
 : description -> ^(DESCRIPTIONS)
 | domains -> ^(DOMAINS domains)
 | attributes -> ^(ATTRIBUTES attributes)
 | runs -> ^(RUNS runs)
 | tests -> ^(TESTS tests)
 | output_hypotheses -> ^(HYPOTHESES output_hypotheses)
 | input_hypotheses -> ^(INPUT_HYPOTHESES input_hypotheses)
 | events -> ^(EVENTS events)
 | testing_events -> ^(TESTING_EVENTS testing_events)
 ;

description : DESCRIPTION NL? OPEN any_character* CLOSE NL?
 -> ^(NULL) ;

any_character : INT | ID | PERCENT | DOT | COLON | SLASH | EQUAL | NL
 -> ^(NULL) ;

attributes : ATTRIBUTES NL? OPEN NL? attribute* CLOSE NL?
 -> attribute* ;

attribute 
    : name=ID type=ID OPEN simple_value (COMA simple_value)* CLOSE NL
 -> ^(ATTRIBUTE $name $type ^(DOMAIN_ARGS_BRACE simple_value+))
	| name=ID type=ID simple_value (COMA simple_value)* NL
 -> ^(ATTRIBUTE $name $type ^(DOMAIN_ARGS simple_value+))
	| name=ID type=ID (EPSILON EQUAL simple_value COST EQUAL simple_value)? NL 
 -> ^(ATTRIBUTE $name $type ^(DOMAIN_ARGS_EMPTY)) 
    ;

domains : DOMAINS NL? OPEN NL? domain* CLOSE NL? -> domain* ;

domain
   : name=ID type=ID OPEN simple_value (COMA simple_value)* CLOSE NL
 -> ^(DOMAIN $name $type ^(DOMAIN_ARGS_BRACE simple_value+))
   | name=ID type=ID simple_value (COMA simple_value)* NL
 -> ^(DOMAIN $name $type ^(DOMAIN_ARGS simple_value+)) 
   | name=ID type=ID NL
 -> ^(DOMAIN $name $type ^(DOMAIN_ARGS_EMPTY))
   ;

runs : RUNS NL? OPEN NL? (simple_parameter NL?)* run* CLOSE NL?
 -> ^(RUNS_PARAMS simple_parameter*) ^(RUNS_LIST run* );

run : name=ID NL? OPEN NL? (enhanced_parameter NL?)* run_result* CLOSE NL?
 -> ^(RUN $name ^(RUNS_PARAMS enhanced_parameter*) ^(RESULT run_result*) ) ;

run_result : name=RESULT_NAME NL? OPEN NL? (coma_parameter NL?)* CLOSE NL?
 -> ^(RESULTS $name coma_parameter*) ;

tests : TESTS NL? OPEN NL? (simple_parameter NL?)* test* CLOSE NL?
 -> ^(TESTS_PARAMS simple_parameter*) ^(TESTS_LIST test*);

test : name=ID NL? OPEN NL? (simple_parameter NL?)* CLOSE NL?
 -> ^(TEST $name ^(TESTS_PARAMS simple_parameter*) );

output_hypotheses : OUTPUT_HYPOTHESES name=ID NL? OPEN NL? (simple_parameter NL?)* hypothesis CLOSE NL?
 -> $name hypothesis simple_parameter* ;

input_hypotheses : INPUT_HYPOTHESES name=ID NL? OPEN NL? (simple_parameter NL?)* hypothesis CLOSE NL?
 -> $name hypothesis simple_parameter* ;

hypothesis : class_descriptions NL? rule*
 -> ^(HYPOTHESIS_BODY class_descriptions rule*) ;

rule : 
	RULE_ARROW selectors NL?
	-> ^(RULE selectors ) 
     | RULE_ARROW selectors NL? rule_additional_params+ NL?
	-> ^(RULE selectors rule_additional_params+) ;

selectors : (selector (NL? selector)*)?
 -> ^(SELECTORS selector*) ;

selector : OPEN_SQR name=ID kind=EQUAL atom selector_additional_params? CLOSE_SQR
	-> ^(SELECTOR $name $kind atom);

selector_additional_params : COLON simple_value (COMA simple_value)*  
	-> ^(SELECTOR_PARAMS simple_value+) ;

rule_additional_params : COLON simple_parameter (NL? COMA simple_parameter)* 
	-> ^(RULE_PARAMS simple_parameter+) ;

coma_parameter : name=ID COMA fl_value=simple_value
 -> ^(PARAMETER $name $fl_value) ;

simple_parameter : name=ID EQUAL fl_value=simple_value
 -> ^(PARAMETER $name $fl_value) ;

enhanced_parameter : name=ID EQUAL value?
 -> ^(PARAMETER $name value?) ;

value : simple_value
 -> ^(VALUE simple_value)
 | class_descriptions
 -> class_descriptions ;

class_descriptions : class_description+
 -> ^(CLASSES class_description+) ;

class_description : OPEN_SQR name=ID kind=EQUAL simple_value CLOSE_SQR
 -> ^(CLASS_DESCRIPTION $name $kind simple_value) ;

events : EVENTS NL? OPEN NL? row* CLOSE NL?
  -> row*;

testing_events : TESTING_EVENTS NL? OPEN NL? row* CLOSE NL?
  -> row*;

row : simple_value (COMA simple_value)* NL
  -> ^(ROW simple_value+); 

atom
//	: id_value=ID ->  $id_value
//	| in_value=INT ->  $in_value 
//	| first=INT DOT rest=INT -> {new CommonTree(new CommonToken(ID, $first.text+"."+$rest.text))}
//	| first=ID  RANGE_OP last=ID  -> ^(RANGE $first $last)
//	| first=INT RANGE_OP last=INT -> ^(RANGE $first $last)
//	| ID ( COMA ID )+ -> ^(VALUE_SET ID+)

//before uncomment fix the code!
//	| INT ( COMA INT )+ -> ^(VALUE_SET INT+)
	: simple_value -> simple_value
	| simple_value RANGE_OP simple_value -> ^(RANGE simple_value+) 
	| simple_value (COMA simple_value)+ -> ^(VALUE_SET simple_value+)
	;

simple_value
	: id_value=ID ->  $id_value
//	| in_value=INT ->  $in_value 
	| fl_value=FLOAT ->  $fl_value 
//	| first=INT DOT rest=INT -> {new CommonTree(new CommonToken(ID, $first.text+"."+$rest.text))}
	| pe_value=PERCENT ->  $pe_value
	;