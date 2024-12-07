package org.example.view.gui;

import org.example.service.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuView extends JPanel {
    public MainMenuView(Runnable startNewGameCallback, Runnable loadGameCallback) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.LIGHT_GRAY);

        ImageIcon pixelArtIcon = ResourceLoader.loadImage("src/main/resources/images/menu_pixelart.png");
        Image pixelArtImage = pixelArtIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(pixelArtImage);

        // Set layout to null for absolute positioning
        backgroundPanel.setLayout(null);

        ImageIcon button = ResourceLoader.loadScaledImage("src/main/resources/images/menu_button.png", 450, 90);
        ImageIcon button_hover = ResourceLoader.loadScaledImage("src/main/resources/images/menu_button_clicked.png", 450, 90);

        // Load and register fonts
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font tinyMedium = ResourceLoader.loadFont("src/main/resources/fonts/Tiny5-Regular.ttf", 25f);
        ge.registerFont(tinyMedium);

        // Configure buttons
        JButton newGameButton = new JButton("Nowa Gra", button);
        configureButton(newGameButton, tinyMedium, button_hover, 1395, 500, 450, 90, button, backgroundPanel);
        newGameButton.addActionListener(e -> startNewGameCallback.run());

        JButton loadGameButton = new JButton("Wczytaj Grę", button);
        configureButton(loadGameButton, tinyMedium, button_hover, 1395, 650, 450, 90, button, backgroundPanel);
        loadGameButton.addActionListener(e -> loadGameCallback.run());

        JButton exitButton = new JButton("Wyjdź", button);
        configureButton(exitButton, tinyMedium, button_hover, 1395, 800, 450, 90, button, backgroundPanel);
        exitButton.addActionListener(e -> System.exit(0));

        this.add(backgroundPanel, BorderLayout.CENTER);
    }

    private void configureButton(JButton button,
                                 Font tinySmall,
                                 ImageIcon buttonHoverIcon,
                                 int x, int y,
                                 int width, int height,
                                 ImageIcon buttonIcon,
                                 BackgroundPanel backgroundPanel) {
        button.setBounds(x, y, width, height);
        button.setFont(tinySmall);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setFocusable(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(buttonHoverIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(buttonIcon);
            }
        });
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        backgroundPanel.add(button);
    }
}
