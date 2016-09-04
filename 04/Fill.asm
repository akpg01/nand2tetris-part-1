// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

(LOOP)
// set current screen pointer to @SCREEN
@SCREEN
D=A
@0
M=D

// check keyboard input
(CHECK_KBD)
// check whether a key has been pressed on the keyboard
@KBD
D=M
@WHITE // key is not pressed, jump to white
D;JEQ
@BLACK
D;JGT // key is pressed, jump to black

@CHECK_KBD
0;JMP

(WHITE)
@1
M=0   // fill screen with zeros
@FILL
0;JMP

(BLACK)
@1
M=-1 // change screen color (-1 = 11111111111111)
@FILL
0;JMP

(FILL)
@1    // determine what to fill the SCREEN with
D=M   // D contains black or white

@0
A=M  // access address of pixel to be filled
M=D  // fill it with black or white

@0
D=M+1  // increment to next pixel
@KBD
D=A-D  // KBD-SCREEN = A

@0
M=M+1  // increment to next pixel

@FILL
D;JGT  // the whole screen is black

@LOOP
0; JMP // infinite loop
