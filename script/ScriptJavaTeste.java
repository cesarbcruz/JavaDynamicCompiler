
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cesar
 */
public class ScriptJavaTeste {

    private int counter;

    public void printIt(Connection con) {
        try {
            System.out.println("printIt() connection param " + con.createStatement().executeQuery("select * from user limit 1").next());
        } catch (SQLException ex) {
            Logger.getLogger(ScriptJavaTeste.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printItString(String temp) {
        System.out.println("printIt() with param String : " + temp);
    }

    public void printItInt(int temp) {
        System.out.println("printIt() with param int : " + temp);
    }

    public void setCounter(int counter) {
        this.counter = counter;
        System.out.println("setCounter() set counter to : " + counter);
    }

    public void printCounter() {
        System.out.println("printCounter() : " + this.counter);
    }
}
