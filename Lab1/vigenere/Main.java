package vigenere;


import java.io.IOException;

public class Main {
    public static void main (String [] args) {
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view , model);
        try {
            controller.chooseAndProcessFileFromDesktop();
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
