package robot;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;


public class robot {

    static Robot bot; // class-level robot
    static int openInvKey = KeyEvent.VK_E;

    public static void main(String[] args) throws AWTException, IOException {
        bot = new Robot(); // initialize the class-level bot
        //attack("knockback mode", "wooden-stone-copper");
        //openInv();
        String settingsPath = "options/settings.txt";
        settingFile file = new settingFile(settingsPath); // settings reader

        // Get the value of "scaling" from the settings file
        String scalingResolution = file.named("scaling").orElse("default");
        System.out.println(scalingResolution);
    }

    public static class settingFile {
        private final Map<String, String> settings;

        public settingFile(String path) throws IOException {
            settings = readSettings(Path.of(path));
        }


        private Map<String, String> readSettings(Path path) throws IOException {
            Map<String, String> settings = new HashMap<>();
            List<String> lines = Files.readAllLines(path);
            for (var line : lines) {
                var parts = line.split("="); //splits at the = mark
                settings.put(parts[0].strip(), parts[1].strip());
            }
            return settings;
        }

        public Optional<String> named(String name) {
            return Optional.ofNullable(settings.get(name));
        }
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
            bot.mouseRelease(MouseEvent.BUTTON1_MASK); //BUTTON1_MASK is depricated, find a new version later on
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
