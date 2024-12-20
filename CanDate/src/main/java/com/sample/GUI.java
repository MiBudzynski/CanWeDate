package com.sample;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GUI extends JFrame {

    private static final long serialVersionUID = 3378774311250822914L;

    private JButton continueButton;
    private String[] answers;

    public boolean isReady = false;
    public int selectedIndex = -1;

    public ArrayList<JRadioButton> radioButtons = new ArrayList<>();

    public GUI(String question) {
        super("Can We Date? - Result");
        initializeFrame();

        JPanel contentPanel = new JPanel(new BorderLayout());
        JLabel questionLabel = createLabel(question);
        contentPanel.add(questionLabel, BorderLayout.CENTER);

        if (isImagePath(question)) {
            addImageToPanel(contentPanel, extractImagePath(question));
        }

        this.add(contentPanel);
        finalizeFrame();
    }

    public GUI(String question, String[] answers) {
        super("Can We Date? - Question");
        initializeFrame();

        this.answers = answers;

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createLabel(question), BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup buttonGroup = new ButtonGroup();

        for (String answer : answers) {
            JRadioButton button;
            if (answer.endsWith(".png")) {
                button = createImageRadioButton(answer);
            } else {
                button = new JRadioButton(answer);
            }
            radioButtons.add(button);
            buttonGroup.add(button);
            optionsPanel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(optionsPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        continueButton = new JButton("Continue");
        continueButton.addActionListener(this::handleContinue);
        mainPanel.add(continueButton, BorderLayout.SOUTH);

        this.add(mainPanel);
        finalizeFrame();
    }

    public GUI(String imagePath, int unusedParameter) {
        super("Can We Date? - End State");
        initializeFrame();

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel imageLabel = createImageLabel(imagePath);
        mainPanel.add(imageLabel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> System.exit(0));
        mainPanel.add(closeButton, BorderLayout.SOUTH);

        this.add(mainPanel);
        finalizeFrame();
    }

    private void initializeFrame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
    }

    private void finalizeFrame() {
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Courier", Font.BOLD, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private boolean isImagePath(String text) {
        return text.startsWith("<") && text.endsWith(">");
    }

    private String extractImagePath(String text) {
        return text.substring(1, text.length() - 1);
    }

    private void addImageToPanel(JPanel panel, String imagePath) {
        JLabel imageLabel = createImageLabel("src/main/resources/imgs/" + imagePath + ".png");
        panel.add(imageLabel, BorderLayout.SOUTH);
    }

    private JLabel createImageLabel(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(getClass().getResource("/imgs/" + imagePath));
            ImageIcon scaledIcon = new ImageIcon(image.getScaledInstance(400, 400, Image.SCALE_SMOOTH));
            return new JLabel(scaledIcon);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return new JLabel("[Image not found]");
        }
    }

    private JRadioButton createImageRadioButton(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(getClass().getResource("/imgs/" + imagePath));
            ImageIcon scaledIcon = new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            JRadioButton radioButton = new JRadioButton();
            radioButton.setIcon(scaledIcon);
            radioButton.setPreferredSize(new Dimension(120, 120));
            return radioButton;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return new JRadioButton("[Image not found]");
        }
    }

    private void handleContinue(ActionEvent event) {
        for (int i = 0; i < radioButtons.size(); i++) {
            if (radioButtons.get(i).isSelected()) {
                selectedIndex = i;
                break;
            }
        }
        isReady = true;
        this.dispose();
    }

    public void waitForAnswer() {
        try {
            while (!isReady) {
                TimeUnit.MILLISECONDS.sleep(25);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getAnswer() {
        return selectedIndex >= 0 ? answers[selectedIndex] : answers[0];
    }
}
