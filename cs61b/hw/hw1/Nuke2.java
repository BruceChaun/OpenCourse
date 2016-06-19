/* Nuke2.java */

import java.io.*;

/*  A class that reads a string from the keyboard and 
 *  prints the same string, with its second character removed.
 */

class Nuke2 {

    /* @param arg not used.
     * @exception Exception thrown if there are any problems parsing 
     * the user's input or opening the connection.
     */
    public static void main(String[] arg) throws Exception {
	BufferedReader keyboard = 
	    new BufferedReader(new InputStreamReader(System.in));
	String inputString = keyboard.readLine();
	String outputString = 
	    inputString.replaceFirst(inputString.substring(1, 2), "");
	System.out.println(outputString);
    }
}
