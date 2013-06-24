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

any_character : ID | FLOAT | DOT | COLON | SLASH | EQUAL | NL
 -> ^(NULL) ;

attributes : ATTRIBUTES NL? OPEN NL? attribute* CLOSE NL?
 -> attribute* ;

attribute 
    : name=ID type=ID OPEN simple_value (COMA simple_value)* CLOSE NL
 -> ^(ATTRIBUTE $name $type ^(DOMAIN_ARGS_BRACE simple_value+))
	| name=ID type=ID simple_value (COMA simple_value)* NL
 -> ^(ATTRIBUTE $name $type ^(DOMAIN_ARGS simple_value+))
	| name=ID type=ID (EPSILON EQUAL FLOAT COST EQUAL FLOAT)? NL 
 -> ^(ATTRIBUTE $name $type ^(DOMAIN_ARGS_EMPTY)) 
    ;

domains : DOMAINS NL? OPEN NL? domain* CLOSE NL? -> domain* ;

domain
   : name=ID type=ID OPEN simple_value (COMA simple_value)* CLOSE NL
 -> ^(DOMAIN $name $type ^(DOMAIN_ARGS_BRACE simple_value+))
   | name=ID type=ID simple_value (COMA simple_value)* NL
 -> ^(DOMAIN $name $type ^(DOMAIN_ARGS simple_value+)) 
//   | name=ID type=ID NL
// -> ^(DOMAIN $name $type ^(DOMAIN_ARGS_EMPTY))
   ;

runs : RUNS NL? OPEN NL? parameter* run* CLOSE NL?
 -> ^(RUNS_PARAMS parameter*) ^(RUNS_LIST run* );

run : name=ID NL? OPEN NL? parameter* run_result* CLOSE NL?
 -> ^(RUN $name ^(RUNS_PARAMS parameter*) ^(RESULT run_result*) ) ;

run_result : name=RESULT_NAME NL? OPEN NL? result_parameter* CLOSE NL?
 -> ^(RESULTS $name result_parameter*) ;

tests : TESTS NL? OPEN NL? parameter* test* CLOSE NL?
 -> ^(TESTS_PARAMS parameter*) ^(TESTS_LIST test*);

test : name=ID NL? OPEN NL? parameter* CLOSE NL?
 -> ^(TEST $name ^(TESTS_PARAMS parameter*) );

output_hypotheses : OUTPUT_HYPOTHESES name=ID NL? OPEN NL? parameter* hypothesis CLOSE NL?
 -> $name hypothesis parameter* ;

input_hypotheses : INPUT_HYPOTHESES name=ID NL? OPEN NL? parameter* hypothesis CLOSE NL?
 -> $name hypothesis parameter* ;

hypothesis : class_descriptions NL? rule*
 -> ^(HYPOTHESIS_BODY class_descriptions rule*) ;

rule : 
	RULE_ARROW selectors
	-> ^(RULE selectors ) 
     | RULE_ARROW selectors rule_additional_params+ NL?
	-> ^(RULE selectors rule_additional_params+) ;

selectors : selector+
 -> ^(SELECTORS selector+) ;

selector : OPEN_SQR name=ID kind=EQUAL atom selector_additional_params? CLOSE_SQR NL? 
	-> ^(SELECTOR $name $kind atom);

selector_additional_params : COLON simple_value (COMA simple_value)*  
	-> ^(SELECTOR_PARAMS simple_value+) ;

rule_additional_params : COLON parameter (COMA parameter)* 
	-> ^(RULE_PARAMS parameter+) ;

result_parameter : name=ID COMA fl_value=FLOAT NL?
 -> ^(PARAMETER $name $fl_value) ;

parameter : name=ID EQUAL value? NL?
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
	: simple_value -> simple_value
	| first=simple_value DOT DOT last=simple_value -> ^(RANGE $first $last)
	| simple_value ( COMA simple_value )+ -> ^(VALUE_SET simple_value+)
	;

simple_value
	:  id_value=ID ->  $id_value
	| fl_value=FLOAT ->  $fl_value 
	| pe_value=PERCENT ->  $pe_value
	;