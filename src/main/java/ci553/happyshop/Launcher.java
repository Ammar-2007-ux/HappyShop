package ci553.happyshop;

import ci553.happyshop.client.UnifiedMain;
import javafx.application.Application;

/**
 * The Launcher class serves as the main entry point of the system.
 * It now starts the unified tab-based interface (UnifiedMain).
 */
public class Launcher  {

    public static void main(String[] args) {
        Application.launch(UnifiedMain.class, args);
    }
}
