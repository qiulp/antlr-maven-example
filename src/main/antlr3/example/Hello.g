grammar Hello;

@header {package example;}

@lexer::header {package example;}

greet
        :       'Hello' COMMA 'World';

COMMA
        :       ',';

