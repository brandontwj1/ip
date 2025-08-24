import java.util.Scanner;

public class Omni {
    private static final String LINEBREAK = "    ________________________________________________________\n";
    private static final String INDENT = "    ";

    private static void greet() {
        System.out.println(
            LINEBREAK +
            INDENT + "Helloo! I'm Omni!\n" +
            INDENT + "What can I do for you?\n" +
            LINEBREAK
        );
    }

    private static void exit() {
        System.out.println(
            LINEBREAK +
            INDENT + "Byeee! See you in a bit!\n" +
            LINEBREAK
        );
    }

    private static void echo(String input) {
        System.out.println(
            LINEBREAK +
            INDENT + input + "\n" +
            LINEBREAK
        );
    }

    public static void main(String[] args) {
        Omni.greet();
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while (!input.equals("bye")) {
            Omni.echo(input);
            input = sc.nextLine();
        }
        Omni.exit();
    }
}
