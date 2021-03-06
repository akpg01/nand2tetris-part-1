package assembler;
/**
 * Code
 * @author akpan
 * Objective: 
 * 	- provides the binary codes of all the assembly language mnemonics.
 */
public class Code {
	/**
	 * dest
	 * @param str
	 * @return string
	 * Objective: returns the binary code of the dest mnemonic (3 bits)
	 */
	public String dest(String str){
		String result = "";
		switch(str){
		case "null":
			result = "000";
			break;
		case "M":
			result = "001";
			break;
		case "D":
			result = "010";
			break;
		case "MD": 
			result = "011";
			break;
		case "A":
			result = "100";
			break;
		case "AM": 
			result = "101";
			break;
		case "AD":
			result = "110";
			break;
		case "AMD":
			result = "111";
			break;
		}
		return result;
	}
	/**
	 * comp
	 * @param str
	 * @return string
	 * Objective: returns the binary code of the comp mnemonic (7 bits)
	 */
	public String comp(String str){
		String result = ""; 
		switch(str){
		case "0":
			result="0101010"; 
			break;
		case "1":
			result="0111111";
			break;
		case "-1":
			result="0111010";
			break;
		case "D":
			result ="0001100";
			break;
		case "A":
			result ="0110000";
			break;
		case "!D":
			result ="0001101";
			break;
		case "!A":
			result ="0110001";
			break;
		case "-D": 
			result ="0001111";
			break;
		case "-A":
			result ="0110011";
			break;
		case "D+1":
			result ="0011111";
			break;
		case "A+1":
			result ="0110111";
			break;
		case "D-1":
			result ="0001110";
			break;
		case "A-1":
			result ="0110010";
			break;
		case "D+A":
			result ="0000010";
			break;
		case "D-A":
			result ="0010011";
			break;
		case "A-D":
			result ="0000111";
			break;
		case "D&A":
			result ="0000000";
			break;
		case "D|A":
			result ="0010101";
			break;
		case "M":
			result ="1110000";
			break;
		case "!M":
			result ="1110001";
			break;
		case "-M":
			result ="1110011";
			break;
		case "M+1":
			result ="1110111";
			break;
		case "M-1":
			result ="1110010";
			break;
		case "D+M":
			result ="1000010";
			break;
		case "D-M":
			result ="1010011";
			break;
		case "M-D":
			result ="1000111";
			break;
		case "D&M":
			result ="1000000";
			break;
		case "D|M":
			result ="1010101";
			break;
		}
		return result;
	}
	/**
	 * jump
	 * @param str
	 * @return string
	 * Objective: returns the binary code of the jump mnemonic (3 bits)
	 */
	public String jump(String str){
		String result = "";
		switch(str){
		case "null": 
			result = "000";
			break;
		case "JGT":
			result = "001";
			break;
		case "JEQ":
			result = "010";
			break;
		case "JGE":
			result = "011";
			break;
		case "JLT":
			result = "100";
			break;
		case "JNE":
			result = "101";
			break;
		case "JLE":
			result = "110";
			break;
		case "JMP":
			result = "111";
			break;
		}
		return result;
	}
	
}
