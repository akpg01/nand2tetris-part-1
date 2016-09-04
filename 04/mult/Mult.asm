// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)


// initialize n = R0
@R0
D=M
@n
M=D

// set p = R1
@R1
D=M
@p
M=D

// initialize i
@i
M=0

// initialize R2
@0
D=A
@R2
M=D

(LOOP)
  // if (i==n) go to end
  @i
  D=M
  @n
  D=D-M  // D=D-R0
  @END
  D; JEQ
  @p
  D=M  // D=R1
  @R2
  M = D+M // R2 = R2 + R1

  // increment i
  @i
  M = M+1
  @LOOP
  0; JMP  // go to Loop
(END)
  @END
  0; JMP //Infinite Loop
