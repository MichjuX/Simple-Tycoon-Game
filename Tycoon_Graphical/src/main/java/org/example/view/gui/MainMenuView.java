package org.example.view.gui;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JPanel {
    public MainMenuView(Runnable startNewGameCallback, Runnable loadGameCallback) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.LIGHT_GRAY);

        JLabel titleLabel = new JLabel("Restaurant Tycoon");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton newGameButton = new JButton("Nowa Gra");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.addActionListener(e -> startNewGameCallback.run());

        JButton loadGameButton = new JButton("Wczytaj Grę");
        loadGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadGameButton.addActionListener(e -> loadGameCallback.run());

        JButton exitButton = new JButton("Wyjdź");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> System.exit(0));

        this.add(Box.createVerticalGlue());
        this.add(titleLabel);
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        this.add(newGameButton);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(loadGameButton);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(exitButton);
        this.add(Box.createVerticalGlue());
    }
}
