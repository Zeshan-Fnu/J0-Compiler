# J0-Compiler
This program takes as input, J0 source code in a text file and produces *86 assembly code.
The assembly code can be assembled with NASM.
The Lexical Analyzer uses Finite State Automata, first to create token list and then to create symbol table.
The Parser uses Push Down Automata for each executable statement to creates quads and solve expressions.
The IO is implemented by copying and pasting assembly code from IO.txt file, which also has other boilerplate assembly code. 
Read the documentation file for further information.
