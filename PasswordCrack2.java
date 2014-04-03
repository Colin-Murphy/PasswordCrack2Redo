/**
	File: PasswordCrack.java	
	Designed for RIT Concepts of Paralel and Distributed Systems Project 1
	
	@author Colin L Murphy <clm3888@rit.edu>
	@version 3/28/14
*/

//Read files
import java.io.File;
//Arraylist for storing users and hashed passwords
import java.util.ArrayList;
//Buffered reader
import java.io.BufferedReader;
//File reader
import java.io.FileReader;
//Io exception
import java.io.IOException;
//File not found
import java.io.FileNotFoundException;


//Hex Class
import edu.rit.util.Hex;



import edu.rit.pj2.IntVbl;
import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;

//Hash passwords
import java.security.MessageDigest;

/**
 * Class PasswordCrack2 is the password cracking main program.
 */
public class PasswordCrack2 extends Task {
    /**
     * Global reduction variable for number of passwords found.
     */
    IntVbl found;
    
    //Hashmap to contain the users and their hashed passwords
	private ArrayList<User> users = new ArrayList<User>();

    /**
     * Main program.
     *
     * @param  args  Command line argument strings.
     */
    public void main(String[] args) throws Exception {
        //Insure proper amount of arguments
		if (args.length != 1) {
			System.err.println("Usage: java pj2 PasswordCrack2" 
				+ "<databaseFile>");
			//Were done here
			return;
		}
		
		
		File databaseFile = new File(args[0]);
		
		//Reader for reading the input file
		BufferedReader reader;

		//Read the database
		try {
			reader = new BufferedReader
				(new FileReader(databaseFile));
		}
		
		//File not found, tell the user and give up
		catch(FileNotFoundException e) {
			System.err.println("File " + databaseFile.getName() + 
				" does not exist");
			System.exit (1);
			//End the program
			return;
		}

		
		String line = "";
		//Parse input and start thread group 2 (users)
		try {
			while((line = reader.readLine()) !=null) {
				//Split into tokens at any whitespace
				String[] tokens = line.split("\\s+");
				
				//Add this password and use to the hashmap
				users.add(new User(tokens[0], tokens[1]));

				
			}
		}
		
		
		//Something went wrong, give up
		catch(IOException e) {
			System.err.println("Error reading " + 
				databaseFile.getName());
			System.exit (1);
		}

		found = new IntVbl.Sum(0);
        parallelFor (0, 1727603) .exec (new Loop() {
            // Thread-local reduction variable for number of passwords found.
            IntVbl thrFound;
            
            public void start() {
            	thrFound = threadLocal(found);
            }

            public void run (int i) throws Exception {
            	            	
                String password = getPassword (i);
                //Get the password hash of the index in base 36 as a string
                
                byte[] data;
                try {
					MessageDigest md = MessageDigest.getInstance ("SHA-256");
					data = password.getBytes ("UTF-8");
					md.update (data);
					data = md.digest();
				}
				catch (Throwable exc) {
					throw new IllegalStateException ("Shouldn't happen", exc);
				}

				String hex = Hex.toString(data);
				
				for (int j = 0; j<users.size();j++) {
					User user = users.get(j);
					//If this hash exists then print it
					if (user.password.equals(hex) && !user.printed) {
						System.out.println(user.username + " " + password);
						user.printed = true;
						thrFound.item++;
					}
				}
			}
		});
		
		//Print the statistics
		System.out.println(users.size() + " users");
		System.out.println(found.item + " passwords found");

	}

    /**
     * Get the password for the given index.
     *
     * @param  index  Index in the range 0 to 1727603.
     *
     * @return  Password corresponding to index.
     */
    private static String getPassword(int index) {
		//All the characters in the string combinations        
        char[] chars = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d',
        	'e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u',
        	'v','w','x','y','z'};
        	
		//less than 36 is really easy
        if (index < 36) {
            return ("" + chars[index]);
		}
        else if (index < 1332) {
            // Convert index to one of 36^2 = 1296 possible two-character
            // passwords
            char ones = chars[index%36];
            int value = index/36;
            char tens = chars[value%36];
            return "" + tens + ones;
		}
        else if (index < 47988) {
            // Convert index to one of 36^3 = 46656 possible three-character
            // passwords
            char ones = chars[index%36];
            int value = index / 36;
            char tens = chars[value%36];
            value = value/36;
            char hundreds = chars[value%36];
            
            return "" + hundreds + tens + ones;
		}
        else {
            // Convert index to one of 36^4 = 1679616 possible four-character
            // passwords
            char ones = chars[index%36];
            int value = index / 36;
            char tens = chars[(value%36)];
            value = value/36;
            char hundreds = chars[value%36];
            value = value/36;
            char thousands = chars[value%36];
            
            return "" + thousands + hundreds + tens + ones;
        }
	}
	/**
		Private nested class for representing a user
	*/
	private class User {
		/**
			Username string
		*/
		String username;
		/**
			Password string
		*/
		String password;
		
		//Dont want to reprint in the case of multiple users with the same passwords
		boolean printed = false;
		
		
		/**
			Construct a new user object
			@param username: The username
			@param password: The password
		*/
		public User (String username, String password) {
			//Assign variables
			this.username = username;
			this.password = password;
		}
	}
}


	
