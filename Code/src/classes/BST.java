package classes;
import def.Guess;

/**
 * Created by dylanaird on 26/09/2016.
 */
public class BST {



    class BTNode{

        BTNode yesNode;
        BTNode noNode;
        Guess guess;

        public BTNode(Guess nodeGuess)
        {
            guess = nodeGuess;
            noNode = null;
            yesNode = null;
        }


    }
}
