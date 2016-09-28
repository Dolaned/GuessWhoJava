package classes;

import def.Guess;

/**
 * Created by dylanaird on 26/09/2016.
 */
public class BST {

    BTNode rootNode;

    public BST(Guess question, Guess yesGuess, Guess noGuess) {
        rootNode = new BTNode(question);
        rootNode.setYesNode(new BTNode(yesGuess));
        rootNode.setNoNode(new BTNode(noGuess));
    }
    /*
    public void query() {
        rootNode.query();
    }*/

    class BTNode {

        BTNode yesNode;
        BTNode noNode;
        Guess guess;

        public BTNode(Guess nodeGuess) {
            guess = nodeGuess;
            noNode = null;
            yesNode = null;
        }


        public BTNode getYesNode() {
            return yesNode;
        }

        public void setYesNode(BTNode yesNode) {
            this.yesNode = yesNode;
        }

        public BTNode getNoNode() {
            return noNode;
        }

        public void setNoNode(BTNode noNode) {
            this.noNode = noNode;
        }

        public Guess getGuess() {
            return guess;
        }

        public void setGuess(Guess guess) {
            this.guess = guess;
        }

        public Boolean isGuess() {
            return !(noNode == null && yesNode == null);
        }
    }
}
