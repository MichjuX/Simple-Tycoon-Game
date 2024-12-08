package org.example.service;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResourceLoader {
    private ResourceLoader() {
    } // Prevent instantiation

    public static ImageIcon[] loadImageIcons(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon smallerIcon = new ImageIcon(image.getScaledInstance(150, 25, Image.SCALE_SMOOTH));
        return new ImageIcon[]{new ImageIcon(image), smallerIcon};
    }

    public static Font loadFont(String path, float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ImageIcon loadScaledImage(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    public static ImageIcon loadImage(String path) {
        return new ImageIcon(path);
    }

    public static JLabel createAnimatedLabel(String gifPath, int width, int height) {
        ImageIcon icon = new ImageIcon(gifPath);

        JLabel label = new JLabel(icon) {
            @Override
            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
                // Rysowanie animowanego GIF-a w większym rozmiarze
                g.drawImage(icon.getImage(), 0, 0, width, height, this);
            }
        };
        label.setPreferredSize(new Dimension(width, height));
        return label;
    }

}

