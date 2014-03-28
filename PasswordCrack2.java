import edu.rit.pj2.IntVbl;
import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;

/**
 * Class PasswordCrack2 is the password cracking main program.
 */
public class PasswordCrack2
    extends Task
    {
    /**
     * Global reduction variable for number of passwords found.
     */
    IntVbl found;

    /**
     * Main program.
     *
     * @param  args  Command line argument strings.
     */
    public void main
        (String[] args)
        throws Exception
        {
        // TBD

        parallelFor (0, 1727603) .exec (new Loop()
            {
            // Thread-local reduction variable for number of passwords found.
            IntVbl thrFound;

            // TBD

            public void run (int i) throws Exception
                {
                String password = getPassword (i);
                // TBD
                }
            });

        // TBD
        }

    /**
     * Get the password for the given index.
     *
     * @param  index  Index in the range 0 to 1727603.
     *
     * @return  Password corresponding to index.
     */
    private static String getPassword
        (int index)
        {
        // TBD

        if (index < 36)
            {
            // Convert index to one of 36 possible one-character passwords
            // TBD
            }
        else if (index < 1332)
            {
            // Convert index to one of 36^2 = 1296 possible two-character
            // passwords
            // TBD
            }
        else if (index < 47988)
            {
            // Convert index to one of 36^3 = 46656 possible three-character
            // passwords
            // TBD
            }
        else
            {
            // Convert index to one of 36^4 = 1679616 possible four-character
            // passwords
            // TBD
            }

        // TBD
        }
    }
