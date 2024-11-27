package org.example.service;

public class NavigationHandler {
    private int selectedOption;
    private int maxOptions;

    public NavigationHandler(int maxOptions) {
        this.maxOptions = maxOptions;
        this.selectedOption = 0;
    }

    public void moveUp() {
        if (selectedOption > 0) {
            selectedOption--;
        } else {
            selectedOption = maxOptions - 1; // Wraca na ostatnią opcję
        }
    }

    public void moveDown() {
        if (selectedOption < maxOptions - 1) {
            selectedOption++;
        } else {
            selectedOption = 0; // Wraca na pierwszą opcję
        }
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setMaxOptions(int maxOptions) {
        this.maxOptions = maxOptions;
        if (selectedOption >= maxOptions) {
            selectedOption = maxOptions - 1; // Dopasowanie opcji do zmienionej liczby maksymalnych opcji
        }
    }

    public void resetSelection() {
        selectedOption = 0;
    }
}
