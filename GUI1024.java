package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

public class GUI1024 extends JFrame implements ActionListener {
    static JMenuBar mBar;
    static JMenu file;
    static JMenuItem exitGame;
    static JMenuItem Reset;
    static JMenuItem Score;
    static JMenuItem resize;
    static JFrame f;
    static GUI1024Panel panel;

    public static void main(String arg[]) {
        GUI1024 gui = new GUI1024();
        f = new JFrame("Welcome to 1024!");
        mBar = new JMenuBar();
        file = new JMenu("File");
        exitGame = new JMenuItem("Exit");
        Reset = new JMenuItem("Reset the game");
        Score = new JMenuItem("Score");
        resize = new JMenuItem("Resize");

        exitGame.addActionListener(gui);
        Reset.addActionListener(gui);
        Score.addActionListener(gui);
        resize.addActionListener(gui);
        file.add(exitGame);
        file.add(Score);
        file.add(resize);
        file.add(file);
        mBar.add(file);
        f.setJMenuBar(mBar);


        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String h, w;
        boolean validNum = false;

        int i = 0, k = 0;
        do {
            h = JOptionPane.showInputDialog("Please Enter the desired Height of the Board.");
            try {
                i = Integer.parseInt(h);
            } catch (IllegalArgumentException e) {
                i = 0;
            }
            if (i > 0)
                validNum = true;
            else
                JOptionPane.showMessageDialog(null, "Not a valid number. Please try again.");
        } while (!validNum);
        validNum = false;
        do {
            w = JOptionPane.showInputDialog("Please Enter the desired Width of the Board.");
            try {
                k = Integer.parseInt(w);
            } catch (IllegalArgumentException e) {
                k = 0;
            }
            if (k > 0)
                validNum = true;
            else
                JOptionPane.showMessageDialog(null, "Not a valid number. Please try again.");
        } while (!validNum);

        panel = new GUI1024Panel(i, k);

        panel.setFocusable(true);
        f.getContentPane().add(panel);
        int dw = ((int) panel.getSize().getWidth() / 100) * 102;
        int dh = ((int) panel.getSize().getHeight() / 100) * 120;
        f.setSize(dw, dh);
        f.setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int dw, dh;
        switch (e.getActionCommand()) {
            case "Exit":
                System.exit(0);
                break;
            case "Reset Game":
                f.getContentPane().remove(panel);

                panel.setFocusable(true);
                f.getContentPane().add(panel);
                dw = ((int) panel.getSize().getWidth() / 100) * 105;
                dh = ((int) panel.getSize().getHeight() / 100) * 120;
                f.setSize(dw, dh);
                f.setVisible(true);
                break;
            case "Score":
                String Score;
                int i = 0;
                boolean validNum = false;
                do {
                    Score = JOptionPane.showInputDialog("Please enter a new feasible score. Ex: 8, 16, 32, 64, ...");
                    try {
                        i = Integer.parseInt(Score);
                    } catch (IllegalArgumentException l) {
                        i = 0;
                    }
                    if (i == 2 || i == 4 || i == 8 || i == 16 || i == 32 || i == 64 || i == 128 || i == 256 || i == 512 || i == 1024)
                        validNum = true;
                    else
                        JOptionPane.showMessageDialog(null, "Incorrect form of a score. Please try again.");
                } while (!validNum);
                panel.SetGoal(i);
                break;

            case "Resize":
                String h, w;
                boolean valid = false;

                int j = 0, s = 0;
                do {
                    h = JOptionPane.showInputDialog("Please Enter the desired Height of the Board.");
                    try {
                        j = Integer.parseInt(h);
                    } catch (IllegalArgumentException f) {
                        j = 0;
                    }
                    if (j > 0)
                        valid = true;
                    else
                        JOptionPane.showMessageDialog(null, "Not a valid number. Please try again.");
                } while (!valid);
                valid = false;
                do {
                    w = JOptionPane.showInputDialog("Please Enter the desired Width of the Board.");
                    try {
                        s = Integer.parseInt(w);
                    } catch (IllegalArgumentException x) {
                        s = 0;
                    }
                    if (s > 0)
                        valid = true;
                    else
                        JOptionPane.showMessageDialog(null, "Not a valid number. Please try again.");
                } while (!valid);

                f.getContentPane().remove(panel);
                panel = new GUI1024Panel(j, s);

                panel.setFocusable(true);
                f.getContentPane().add(panel);
                int wid = ((int) panel.getSize().getWidth() / 100) * 102;
                int hei = ((int) panel.getSize().getHeight() / 100) * 120;
                f.setSize(wid, hei);
                f.setVisible(true);
        }
    }

}


