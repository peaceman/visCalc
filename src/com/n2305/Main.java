package com.n2305;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by nico on 04/01/14.
 */
public class Main implements ActionListener {
    private final float CENTIMETERS_PER_INCH = 2.54f;

    public static void main(String[] args) {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "visCalc");

        JFrame frame = new JFrame("visCalc");
        frame.setContentPane(new Main(frame).main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel main;
    private JTextField textInputRatioY;
    private JTextField textOutput;
    private JTextField textInputRatioX;
    private JTextField textInputInch;

    public Main(JFrame mainFrame) {
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {
                textInputInch.requestFocus();
            }
        });

        new RecalculationListener().listenToTextComponents(textInputRatioX, textInputRatioY, textInputInch);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.printf("actionPerformed: %s\n", actionEvent.getActionCommand());
    }

    class RecalculationListener implements DocumentListener {
        private double inputRatioX;
        private double inputRatioY;
        private double inputInch;

        public void recalculate() {
            if (!hasValidInputNumbers()) {
                textOutput.setText("invalid input");
                return;
            }

            double diagonalCentimeters = inputInch * CENTIMETERS_PER_INCH;
            double inputRatio = inputRatioX / inputRatioY;

            double height = Math.sqrt(Math.pow(diagonalCentimeters, 2) / (Math.pow(inputRatio, 2) + 1));
            double width = inputRatio * height;

            textOutput.setText(String.format("W: %.2f H: %.2f", width, height));
        }

        public boolean hasValidInputNumbers() {
            try {
                inputRatioX = Double.parseDouble(textInputRatioX.getText());
                inputRatioY = Double.parseDouble(textInputRatioY.getText());
                inputInch = Double.parseDouble(textInputInch.getText());

                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        public void listenToTextComponents(JTextComponent... textComponents) {
            for (JTextComponent textComponent : textComponents) {
                textComponent.getDocument().addDocumentListener(this);
            }
        }

        @Override
        public void insertUpdate(DocumentEvent documentEvent) {
            recalculate();
        }

        @Override
        public void removeUpdate(DocumentEvent documentEvent) {
            recalculate();
        }

        @Override
        public void changedUpdate(DocumentEvent documentEvent) {
            recalculate();
        }
    }
}
