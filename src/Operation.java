//Square root stored as sqrt
//Plus stored as +, minus as -, times as *, divided as /, squared is inputted as smth * itself

import java.awt.image.BufferedImage;

public class Operation extends Symbol{

    private String operation;
    private int precedence;

    // constructor
    Operation(String id, BufferedImage image) {
        super(id, image);
        if ((super.getId().equals("+")) || (super.getId().equals("-"))) {
            precedence = 1;
        } else if ((super.getId().equals("*")) || (super.getId().equals("/"))) {
            precedence = 2;
        } else {
            precedence = 3;
        }
    }

    public String getOperation() {
        return super.getId();
    }

    public int getPrecedence() {
        return precedence;
    }


}
