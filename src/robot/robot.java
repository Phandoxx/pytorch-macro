package robot;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static org.opencv.imgproc.Imgproc.matchTemplate;


public class robot {

    static Robot bot; // class-level robot
    static int openInvKey = KeyEvent.VK_E;
    static String debugMode = "disabled";

    public static void main(String[] args) throws AWTException, IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("OpenCV version:" + Core.VERSION);
        bot = new Robot(); // initialize the class-level bot
        String settingsPath = "options/settings.txt";
        settingFile file = new settingFile(settingsPath); // settings reader
        takeScreenshot();

        debugMode = file.named("debugMode").orElse("disabled");
        System.out.println("debugMode:" + debugMode);


        findTemplateOnScreen("images/spam/teams/teams_chat_enabled.png");

        //attack("knockback mode", "wooden-stone-copper");
        //openInv();

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

    public static void takeScreenshot() throws IOException {
        File outputFile = new File("images/screenshot/screenshot.png");

        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenImage = bot.createScreenCapture(screenRect);
        ImageIO.write(screenImage, "png", outputFile);
        System.out.println("Screenshot saved to " + outputFile.getAbsolutePath());
    }

    public static org.opencv.core.Point findTemplateOnScreen(String templatePath) {
        String screenshotPath = "images/screenshot/screenshot.png";

        // Load the screenshot and template
        Mat image = Imgcodecs.imread(screenshotPath);
        Mat template = Imgcodecs.imread(templatePath);

        // Safety checks
        if (image.empty()) {
            System.out.println("Screenshot not found or could not be loaded: " + screenshotPath);
            return null;
        }
        if (template.empty()) {
            System.out.println("Template not found or could not be loaded: " + templatePath);
            return null;
        }

        // Create result matrix
        int resultCols = image.cols() - template.cols() + 1;
        int resultRows = image.rows() - template.rows() + 1;
        Mat result = new Mat(resultRows, resultCols, CvType.CV_32FC1);

        // Perform template matching
        Imgproc.matchTemplate(image, template, result, Imgproc.TM_CCOEFF_NORMED);

        // Get best match
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        org.opencv.core.Point matchLoc = mmr.maxLoc;

        System.out.println("Best match value: " + mmr.maxVal);
        System.out.println("Match location: " + matchLoc);

        // Draw rectangle for debugging
        Imgproc.rectangle(
                image,
                matchLoc,
                new org.opencv.core.Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),
                new org.opencv.core.Scalar(0, 255, 0),
                2
        );

        if (debugMode.equals("enabled")) {
            Imgcodecs.imwrite("images/screenshot/debug_result.png", image);
        }

        if (mmr.maxVal < 0.8) { // optional threshold
            System.out.println("⚠️ Weak or no match found (maxVal < 0.8)");
            return null;
        }

        image.release();
        template.release();
        result.release();

        return matchLoc;
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
            bot.mouseRelease(MouseEvent.BUTTON1_MASK); //BUTTON1_MASK is deprecated, find a new version later on
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