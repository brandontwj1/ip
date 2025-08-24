public class Omni {
    private static final String LINEBREAK = "____________________________________________________________\n";

    private static void greet() {
        System.out.println(
            LINEBREAK +
            "Helloo! I'm Omni!\n" +
            "What can I do for you?"
        );
    }

    private static void exit() {
        System.out.println(
            LINEBREAK +
            "Byeee! See you in a bit!\n" +
            LINEBREAK
        );
    }

    public static void main(String[] args) {
        Omni.greet();
        Omni.exit();
    }
}
