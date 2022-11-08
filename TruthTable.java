import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;



public abstract class TruthTable {
    public static int and(int a, int b) {
        boolean aa = (a ==1);
        boolean bb = (b ==1);
        if (aa && bb) {
            return 1;
        }
        return 0;
    }

    public static int or(int a, int b) {
        boolean aa = (a ==1);
        boolean bb = (b ==1);
        if (aa || bb) {
            return 1;
        }
        return 0;
    }

    public static int[] not(int[] a) {
        int[] b = a.clone();
        for (int i = 0; i < a.length; i++) {
            if (a[i] == 1) {
                b[i] = 0;
            } else {
                b[i] = 1;
            }
        }
        return b;
    }

    public static int arrow(int a, int b) {
        boolean aa = (a ==1);
        boolean bb = (b ==1);
        if (!aa || bb) {
            return 1;
        }
        return 0;
    }

    public static int twoWayArrow(int a, int b) {
        boolean aa = (a ==1);
        boolean bb = (b ==1);
        if ((!aa || bb) && (aa || !bb)) {
            return 1;
        }
        return 0;
    }
    /**
     * the or operator = v
     * the and operator = &
     * the not operator = !
     * the if operator = ->
     * the if and only if = <->
     *     Examples of correct inputs
     *     p<->q
     *     avb
     *     !(c&d)
     *     you can use brackets to indicate the order. otherwise it wil go from left to right
     * @param p the string with the proposition. for example p&p
     */
    public static void truthTable(String p) {
        p = "(" + p + ")";
        Scanner scanner = new Scanner(p);
        scanner.useDelimiter("->|v|&|!|<->");
        List<String> stringList = new ArrayList<>();
        while (scanner.hasNext()) {
            String s1 = scanner.next();
            if (!(s1.equals("->") || s1.equals("v") || s1.equals("&")
                    || s1.equals("!") || s1.equals("<->"))) {
                String s2 = s1.replace("(", "").replace(")", "");
                if (!stringList.contains(s2) && !s2.equals("")) {
                    stringList.add(s2);
                }
            }
        }
        Collections.sort(stringList);
        System.out.println(stringList);

        int[][] truth = makeTruthTable(stringList);

        printTruth(truth);

        String pNew = p;
        System.out.println(pNew.substring(1,p.length()-1));
        printArray(idk(pNew, truth, stringList));
    }

    private static void printTruth(int[][] truth) {
        int[][] truthNew = new int[truth[0].length][truth.length];

        for (int i = 0; i< truth.length; i++) {
            for (int j = 0; j < truth[0].length; j++) {
                truthNew[j][i] = truth[truth.length - i - 1][j];
            }
        }
        for (int[] ints : truthNew) {
            for (int j = 0; j < truthNew[0].length; j++) {
                System.out.print(" " + ints[j] + "|");
            }
            System.out.println();
        }
    }

    private static int[][] makeTruthTable(List<String> stringList) {
        int[][] truth = new int[stringList.size()][(int) Math.pow(2, stringList.size())];
        int l = 0;
        for (int i = 0; i < stringList.size(); i++) {
            for (int j = 0; j < Math.pow(2, stringList.size()); j++) {
                if ((j - Math.pow(2,i)) % Math.pow(2,i + 1) == 0) {
                    l = (int) Math.pow(2,i);
                }
                if (l > 0) {
                    truth[i][j] = 1;
                    l--;
                } else {
                    truth[i][j] = 0;
                }
            }
        }
        return truth;
    }

    private static String semicolons(List<String> stringList, String pNew) {
        Character one = pNew.charAt(0);
        Character two = pNew.charAt(pNew.length()-1);
        if (one.equals('(') && two.equals(')')) {
            String[] sList = pNew.split("");
            int brackets = 1;
            pNew = countBrackets(pNew, sList, brackets);
        }
        for (String s : stringList) {
            pNew = pNew.replace(s, ";" + s + ";");
        }
        pNew = pNew
                .replace("(", ";(")
                .replace(")", ");")
                .replace("!", ";!");
        return pNew;
    }

    private static String countBrackets(String pNew, String[] sList, int brackets) {
        for (int i = 0; i < sList.length; i++) {
            String s = sList[i];
            if (s.equals("(")) {
                brackets++;
            } else if (s.equals(")")) {
                brackets--;
            }
            if (brackets == 0) {
                if (i == sList.length - 1) {
                    pNew = pNew.substring(1, pNew.length()-1);
                }
                break;
            }
        }
        return pNew;
    }

    private static String getMainConnective(String s, List<String> stringList) {
        Scanner scanner = new Scanner(s);
        scanner.useDelimiter(";");
        StringBuilder r = new StringBuilder();
        while (scanner.hasNext()) {
            String c = scanner.next();
            if (stringList.contains(c)) {
                r.append(c);
                r.append(";");
            } else if (c.equals("!")){
                r.append("!");
            } else if (c.equals("(")) {
                r.append(c);
                int numberOfBrackets = 1;
                countBrackets2(scanner, r, numberOfBrackets);
            } else if (c.equals("&") || c.equals("v") || c.equals("->") || c.equals("<->")) {
                r.append(c);
                r.append(";");
                appendString(scanner, r);
                r.append(";");
            }
        }
        return r.toString();
    }

    private static void appendString(Scanner scanner, StringBuilder r) {
        String c;
        while (scanner.hasNext()) {
            c = scanner.next();
            r.append(c);
        }
    }

    private static void countBrackets2(Scanner scanner, StringBuilder r, int numberOfBrackets) {
        String c;
        while (true) {

            c = scanner.next();
            if (c.equals("(")) {
                numberOfBrackets++;
            } else if (c.equals(")")) {
                numberOfBrackets--;
            }
            r.append(c);
            if (numberOfBrackets == 0) {
                r.append(";");
                break;
            }

        }
    }

    private static int[] idk(String s, int[][] truth, List<String> stringList) {
        s = semicolons(stringList, s);
        s = getMainConnective(s, stringList);
        Scanner scanner = new Scanner(s);
        scanner.useDelimiter(";");
        String element1 = scanner.next();
        if (stringList.contains(s.substring(0,s.length()-1))) {
            return truth[truth.length - stringList.indexOf(element1) - 1];
        }
        if (!scanner.hasNext() && element1.charAt(0) == '!') {
            if (element1.charAt(0) == '(' && element1.charAt(element1.length()-1) == ')') {
                element1 = element1.substring(1, element1.length()-1);
            }
            return not(idk(element1.substring(1), truth, stringList));
        } else if (!scanner.hasNext()) {
            element1 = element1.substring(1, element1.length()-1);
            return idk(element1, truth, stringList);
        }
        String prop = scanner.next();
        String element2 = scanner.next();

        return recursion(truth, stringList, element1, prop, element2);
    }

    private static int[] recursion(int[][] truth, List<String> stringList,
                                   String element1, String prop, String element2) {
        if (stringList.contains(element1) && stringList.contains(element2)) {
            return one(truth, stringList, element1, prop, element2);
        } else if (stringList.contains(element1)) {
            return two(truth, stringList, element1, prop, element2);
        } else if (stringList.contains(element2)) {
            return three(truth, stringList, element1, prop, element2);
        } else {
            return four(truth, stringList, element1, prop, element2);
        }
    }

    private static int[] four(int[][] truth, List<String> stringList,
                              String element1, String prop, String element2) {
        if (element1.charAt(0) == '!' && element2.charAt(0) == '!') {
            return (value(not(idk(element1.substring(1), truth, stringList)),
                    prop, not(idk(element2.substring(1), truth, stringList))));
        } else if (element1.charAt(0) == '!') {
            return (value(not(idk(element1.substring(1), truth, stringList)),
                    prop, idk(element2, truth, stringList)));
        } else if (element2.charAt(0) == '!') {
            return (value(idk(element1, truth, stringList),
                    prop, not(idk(element2.substring(1), truth, stringList))));
        } else {
            return (value(idk(element1, truth, stringList),
                    prop, idk(element2, truth, stringList)));
        }
    }

    private static int[] three(int[][] truth, List<String> stringList,
                               String element1, String prop, String element2) {
        if (element1.charAt(0) == '!' && element2.charAt(0) == '!') {
            return (value(not(idk(element1.substring(1), truth, stringList)),
                    prop, not(truth[truth.length - stringList.indexOf(element2) - 1])));
        } else if (element1.charAt(0) == '!') {
            return (value(not(idk(element1.substring(1), truth, stringList)),
                    prop, truth[truth.length - stringList.indexOf(element2) - 1]));
        } else if (element2.charAt(0) == '!') {
            return (value(idk(element1, truth, stringList),
                    prop, not(truth[truth.length - stringList.indexOf(element2) - 1])));
        } else {
            return (value(idk(element1, truth, stringList),
                    prop, truth[truth.length - stringList.indexOf(element2) - 1]));
        }
    }

    private static int[] two(int[][] truth, List<String> stringList,
                             String element1, String prop, String element2) {
        if (element1.charAt(0) == '!' && element2.charAt(0) == '!') {
            return (value(not(truth[truth.length - stringList.indexOf(element1) - 1]),
                    prop, not(idk(element2.substring(1), truth, stringList))));
        } else if (element1.charAt(0) == '!') {
            return (value(not(truth[truth.length - stringList.indexOf(element1) - 1]),
                    prop, idk(element2, truth, stringList)));
        } else if (element2.charAt(0) == '!') {
            return (value(truth[truth.length - stringList.indexOf(element1) - 1],
                    prop, not(idk(element2.substring(1), truth, stringList))));
        } else {
            return (value(truth[truth.length - stringList.indexOf(element1) - 1],
                    prop, idk(element2, truth, stringList)));
        }
    }

    private static int[] one(int[][] truth, List<String> stringList,
                             String element1, String prop, String element2) {
        if (element1.charAt(0) == '!' && element2.charAt(0) == '!') {
            return (value(not(truth[truth.length - stringList.indexOf(element1) - 1]),
                    prop, not(truth[truth.length - stringList.indexOf(element2) - 1])));
        } else if (element1.charAt(0) == '!') {
            return (value(not(truth[truth.length - stringList.indexOf(element1) - 1]),
                    prop, truth[truth.length - stringList.indexOf(element2) - 1]));
        } else if (element2.charAt(0) == '!') {
            return (value(truth[truth.length - stringList.indexOf(element1) - 1],
                    prop, not(truth[truth.length - stringList.indexOf(element2) - 1])));
        } else {
            return (value(truth[truth.length - stringList.indexOf(element1) - 1],
                    prop, truth[truth.length - stringList.indexOf(element2) - 1]));
        }
    }

    private static void printArray(int[] arr) {
        for (int ints : arr) {
            System.out.println(ints);
        }
    }


    private static int[] value(int[] t1, String p, int[] t2) {
        int[] finalTruth = new int[t1.length];
        for (int i = 0; i < t1.length; i++) {
            switch (p) {
                case "&" -> finalTruth[i] = and(t1[i], t2[i]);
                case "v" -> finalTruth[i] = or(t1[i], t2[i]);
                case "->" -> finalTruth[i] = arrow(t1[i], t2[i]);
                case "<->" -> finalTruth[i] = twoWayArrow(t1[i], t2[i]);
            }
        }
        return finalTruth;
    }
}
