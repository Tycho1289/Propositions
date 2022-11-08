import java.util.*;
import java.lang.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a proposition: ");
        TruthTable.truthTable(scanner.next());
    }
}
