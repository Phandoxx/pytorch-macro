package robot;

public class teams {

    // First method
    public static void mainCallThread() {
        robot.findTemplateOnScreen("images/spam/teams/teams_chat_button_disabled.png", "click", "true");
    }

    // Second method
    public static void watchdog() {
        for (int i = 0; i < 5; i++) {
            System.out.println("watchdog running: " + i);
            try { Thread.sleep(700); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public static void teamsCallLoop() {
        // Thread for mainCallThread
        Thread t1 = new Thread() {
            public void run() {
                mainCallThread();
            }
        };

        // Thread for watchdog
        Thread t2 = new Thread() {
            public void run() {
                watchdog();
            }
        };

        System.out.println("teamsCallLoop Activated");
        t1.start();
        t2.start();
    }

}
//Start audio call: Alt+Shift+A

//End audio call: Ctrl+Shift+H
