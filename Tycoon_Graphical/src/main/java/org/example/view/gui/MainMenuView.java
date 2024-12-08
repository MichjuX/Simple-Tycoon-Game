package org.example.view.gui;

import org.example.controllers.MainMenuController;
import org.example.service.MainMenuService;
import org.example.service.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuView extends JPanel {
    MainMenuController controller;
    JButton modeButton;
    public MainMenuView(Runnable startSwingGameCallback,
                        Runnable loadSwingGameCallback,
                        MainMenuController controller
    ){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.LIGHT_GRAY);

        ImageIcon pixelArtIcon = ResourceLoader.loadImage("src/main/resources/images/backgrounds/menu_pixelart.png");
        Image pixelArtImage = pixelArtIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(pixelArtImage);

        // Set layout to null for absolute positioning
        backgroundPanel.setLayout(null);

        ImageIcon button = ResourceLoader.loadScaledImage("src/main/resources/images/buttons/menu_button.png", 450, 90);
        ImageIcon button_hover = ResourceLoader.loadScaledImage("src/main/resources/images/buttons/menu_button_clicked.png", 450, 90);

        // Load and register fonts
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font tinyMedium = ResourceLoader.loadFont("src/main/resources/fonts/Tiny5-Regular.ttf", 25f);
        ge.registerFont(tinyMedium);

        // Configure buttons
        JButton newGameButton = new JButton("Nowa Gra", button);
        configureButton(newGameButton, tinyMedium, button_hover, 1395, 500, 450, 90, button, backgroundPanel);
        newGameButton.addActionListener(e -> startSwingGameCallback.run());

        JButton loadGameButton = new JButton("Wczytaj Grę", button);
        configureButton(loadGameButton, tinyMedium, button_hover, 1395, 610, 450, 90, button, backgroundPanel);
        loadGameButton.addActionListener(e -> loadSwingGameCallback.run());

        modeButton= new JButton("Tyb gry: Graficzny", button);
        configureButton(modeButton, tinyMedium, button_hover, 1395, 720, 450, 90, button, backgroundPanel);
        modeButton.addActionListener(e -> controller.handleChangeMode());

        JButton exitButton = new JButton("Wyjdź", button);
        configureButton(exitButton, tinyMedium, button_hover, 1395, 830, 450, 90, button, backgroundPanel);
        exitButton.addActionListener(e -> System.exit(0));

        this.add(backgroundPanel, BorderLayout.CENTER);
    }
    public void changeMode(int mode){
        System.out.println("View change mode - " + mode);
        switch (mode){
            case 0:
                modeButton.setText("Tryb gry: Graficzny");
                break;
            case 1:
                modeButton.setText("Tryb gry: Tekstowy");
                break;
            case 2:
                modeButton.setText("Tryb gry: Graficzny + Tekstowy");
                break;
        }
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
