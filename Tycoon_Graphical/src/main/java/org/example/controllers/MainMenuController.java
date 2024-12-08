package org.example.controllers;

import org.example.service.MainMenuService;
import org.example.view.gui.MainMenuView;

public class MainMenuController {
    MainMenuService mainMenuService;
    MainMenuView mainMenuView;
    public void handleChangeMode() {
        mainMenuView.changeMode(mainMenuService.changeMode());
    }

    public void setService(MainMenuService mainMenuService) {
        this.mainMenuService = mainMenuService;
    }

    public void setView(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
    }
}
