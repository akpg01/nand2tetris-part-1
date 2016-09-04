package assembler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Main {
/**
 * Main
 * @param args
 *  The class reads a filename entered at the command prompt.
 * Objectives:
 * 	- verifies that a filename has been entered
 *  - confirms that the file extension is .asm
 *  - attempts to read the file.
 * @throws IOException 
 */
	private static int ROM = 0;
	private static int RAM = 16;
	
	public static void main(String[] args) throws IOException {
		// verify that a .asm file has been supplied when program is executed
		if(args.length < 0)
			System.err.println("Please enter a valid filename");
		// read file name
		File inFile = new File(args[0]);
		BufferedWriter bw = null;

		// check that file extension is .asm
		String filename= inFile.toString();
		String file_extension = filename.substring(filename.lastIndexOf("."));
		if(!file_extension.equals(".asm")){
			System.err.println("File extension must be \".asm.\"");
			System.exit(1);
		}
		
		// output file
		String outFilename = filename.substring(0,filename.indexOf('.'));
		String out = outFilename + ".hack";
		
		try {
			FileWriter fw = new FileWriter(out);
			bw = new BufferedWriter(fw);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		// send to parser class. 
		Parser parser = new Parser(inFile);
		
		// initialize the code class
		Code code = new Code();
		
		// initialize the symbol table
		SymbolTable symbols = new SymbolTable();
		
		// First Pass
		
		// check whether file has more commands
		while(parser.hasMoreCommands()){
			
			// get the next line in the file
			parser.advance();
			// get the command type of the line
			String command = parser.getCurrentCommand().trim();
			if(command.equals("") || command.charAt(0) == '/' )
				continue;
			String commandType = parser.commandType().toString().trim();
			String lsymbol = "";
			if(command.charAt(0) == '(')
				lsymbol = parser.symbol().trim();
			firstPass(commandType,symbols,lsymbol);
		}
		
		// Second Pass
		
		// create new parser
		parser = new Parser(inFile);
		while(parser.hasMoreCommands()){
			// get the next line in the file
			parser.advance();
			// get the command type of the line
			String command = parser.getCurrentCommand().trim();
			if(command.equals("") || command.charAt(0) == '/' )
				continue;
			
			String commandType = parser.commandType().toString().trim();
			// get the symbol
			String symbol = parser.symbol().trim();
			secondPass(commandType,symbols,symbol,parser,bw,code);
		}
		bw.close();
		System.out.println("Done");
	}
	
	/**
	 * firstPass
	 * @param str
	 * @param tbl
	 * @param symbol
	 * Objective: 
	 * 	- keep a running number recording the ROM address into which the current command will 
	 *    be eventually loaded (starts at 0 and increments by 1 whenever a C-instruction or an
	 *    A-instruction is encountered, but does not change when a label pseudocommand or a 
	 *    comment is encountered). 
	 *  - Each time a pseudocommand (Xxx) is encounterd, add a new entry to the symbol table, 
	 *    associating Xxx with the ROM address that will eventually store the next command in the 
	 *    program. 
	 *  - Results in entering all the program's labels along with their ROM address into the
	 *    symbol table. 
	 */
	private static void firstPass(String command, SymbolTable tbl, String symbol){
		if(command.equals("A_COMMAND") || command.equals("C_COMMAND"))
			ROM++;
		else if(command.equals("L_COMMAND")){
			tbl.addEntry(symbol, ROM);
		}
	}
	/**
	 * secondPass
	 * @param command
	 * @param tbl
	 * @param symbol
	 * Objective: 
	 * 	- Each time a symbolic A-instruction is encountered, namely, @Xxx where Xxx is a symbol and 
	 * 	  not a number, look up Xxx in the symbol table.
	 *  - If the symbol is found in the table, replace it with its numeric meaning and complete
	 *    the command's translation.
	 *  - If the symbol is not found in the table, then it must represent a new variable. To handle
	 *    it, add the pair (Xxx, n) to the symbol table, where n is the next available RAM address, 
	 *    and complete the command's translation.
	 *  - The allocated RAM addresses are consecutive numbers, starting at address 16 (just after the
	 *    addresses allocated to the predefined symbols).
	 */
	private static void secondPass(String command, SymbolTable tbl, String symbol, Parser parse, BufferedWriter bw, Code code){
		if(command.equals("A_COMMAND")){
			// initial bit
			String str = "0";
			int commandNumber = 0;
			
			// check if Xxx is a number
			if(isNumber(symbol)){
				// convert string to integer
				commandNumber = Integer.parseInt(symbol);
				
			}else if(tbl.contains(symbol)){
				// if symbol exists in the symbol table, get the address
				commandNumber = tbl.getAddress(symbol);
			}else if(!tbl.contains(symbol)){
				// if symbol does not exist in the symbol table, add it to the symbol table
				tbl.addEntry(symbol, RAM);
				RAM++;
				commandNumber = tbl.getAddress(symbol);
			}
			// get binary representation
			String binaryRep = Integer.toBinaryString(commandNumber);
			// create 16-bit string
			str += appendZeros(binaryRep.length(), binaryRep);
			// write to file
			writeToFile(str, bw);
		}else if(command.equals("C_COMMAND")){
			// initial bits
			String str = "111";
			String compBinary = code.comp(parse.comp());
			String destBinary = code.dest(parse.dest());
			String jumpBinary = code.jump(parse.jump());
			str = str+ compBinary+destBinary+jumpBinary;
			writeToFile(str,bw);
		}
	}
	
	/**
	 * appendZeros
	 * @param n
	 * @param sr
	 * @return
	 * Objective: For A_COMMANDs, if the value after @ is a decimal constant, generates
	 * the equivalent 15-bit binary constant. Additional zeros are added if necessary.
	 */
	private static String appendZeros(int n, String str){
		int num = 15-n; 
		StringBuilder strs = new StringBuilder();
		
		for(int i = 0; i < num ; i++){
			strs.append("0");
		}
		return strs.append(str).toString();
	}
	/**
	 * writeToFile
	 * @param stringData
	 * @param filename
	 * Objective: writes parsed binary results to a .hack file.
	 */
	
	private static void writeToFile(String stringData, BufferedWriter bw){
		try{
			bw.write(stringData);
			bw.newLine();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * isNumber
	 * @param str
	 * @return boolean
	 * Objective: 
	 * 	- Returns true if the string entered is a number; otherwise, false.
	 */
	private static boolean isNumber(String str){
		try{
			Integer.parseInt(str);
			
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}

}
