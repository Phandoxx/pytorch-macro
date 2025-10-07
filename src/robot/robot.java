package robot;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;


public class robot {

    static Robot bot; // class-level robot
    static int openInvKey = KeyEvent.VK_E;

    public static void main(String[] args) throws AWTException {
        bot = new Robot(); // initialize the class-level bot
        //attack("knockback mode", "wooden-stone-copper");
        //openInv();
        setScalingSetting();
    }

    public static void setScalingSetting() {
        Path path = Path.of("options/settings.txt");

        try {
            List<String> allLines = Files.readAllLines(path);

        int lineNumber = 1;
        if (lineNumber < allLines.size()) {
            String scalingSetting = allLines.get(lineNumber);
            System.out.println("Line " + (lineNumber + 1) + ": " + scalingSetting);
        }
        else {
            System.out.println("Line " + lineNumber + " not found");
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //read txt file here
    }

    public static void attack(String weapon, String tier) {
        if ("knockback mode".equalsIgnoreCase(weapon)) {
            knockbackMode();
        }
        else {
            int cooldown = switch (weapon) {
                case "spear" -> 1;
                case "sword" -> 650;
                default -> 0;
            };

            //tier handling logic goes here, check if weapon = axe then add to cooldown based on tier
            if (weapon.equals("axe")) {
                cooldown = switch (tier) {
                    case "wooden-stone-copper" -> 1250;
                    case "iron" -> 1110;
                    case "gold-diamond-netherite" -> 1000;
                    default -> 0;
                };
            }



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
