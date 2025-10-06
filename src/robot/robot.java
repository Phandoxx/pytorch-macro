package robot;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public class robot {

    static Robot bot; // class-level robot
    static int openInvKey = KeyEvent.VK_E;

    public static void main(String[] args) throws AWTException {
        bot = new Robot(); // initialize the class-level bot
        attack("knockback mode", "stone");
        openInv();
    }

    public static void attack(String weapon, String teir) {
        if ("knockback mode".equalsIgnoreCase(weapon)) {
            knockbackMode();
        }
        else {
            int cooldown = switch (weapon) {
                case "sword" -> 650;
                case "axe" -> 1000;
                default -> 0;
            };

            //add if axe here:

            bot.delay(cooldown);

            bot.mousePress(MouseEvent.BUTTON1_MASK);
            bot.mouseRelease(MouseEvent.BUTTON1_MASK);
        }

    }

    public static void openInv() {
        bot.keyPress(openInvKey);
        bot.keyRelease(openInvKey);
    }

    public static void knockbackMode() {
        System.out.println("Knockback mode active! Tracking player...");
    }
}
