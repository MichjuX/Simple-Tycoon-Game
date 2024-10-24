package org.example;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.input.KeyType;

public class Main {
    public static void main(String[] args) {
        try {
            // Tworzenie terminala
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            TerminalScreen screen = new TerminalScreen(terminal);
            screen.startScreen();

            // Ustawienia menu
            String[] options = {"Opcja 1", "Opcja 2", "Wyjście"};
            int selectedOption = 0;

            while (true) {
                // Wyświetlanie menu
                screen.clear();
                TextGraphics textGraphics = screen.newTextGraphics(); // Użycie TextGraphics do rysowania

                for (int i = 0; i < options.length; i++) {
                    String displayText = (i == selectedOption) ? "> " + options[i] : "  " + options[i];

                    // Ustawiamy kolor dla wybranej opcji
                    if (i == selectedOption) {
                        textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
                    } else {
                        textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
                    }
                    textGraphics.putString(1, i, displayText); // Rysowanie tekstu na ekranie
                }
                screen.refresh();

                // Oczekiwanie na wejście od użytkownika
                KeyStroke keyStroke = screen.readInput();
                if (keyStroke.getKeyType() == KeyType.Escape) {
                    break; // Wyjście po naciśnięciu ESC
                } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                    selectedOption = (selectedOption - 1 + options.length) % options.length;
                } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                    selectedOption = (selectedOption + 1) % options.length;
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    switch (selectedOption) {
                        case 0:
                            System.out.println("Wybrano Opcję 1");
                            break;
                        case 1:
                            System.out.println("Wybrano Opcję 2");
                            break;
                        case 2:
                            System.out.println("Zamykam program...");
                            screen.close();
                            return; // Kończymy program
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


