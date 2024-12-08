package org.example.service;

import org.example.Main;
import org.example.view.gui.MainMenuView;

public class MainMenuService {
    int mode;
    MainMenuView mainMenuView;
    public MainMenuService(){
        mode = 0;
    }
    public int changeMode(){
        System.out.println("Service change mode - " + mode);
        if(mode<2){
            mode++;
        }
        else {
            mode = 0;
        }
        switch (mode){
            case 0:
                System.out.println("Normal mode");
                break;
            case 1:
                System.out.println("Hard mode");
                break;
            case 2:
                System.out.println("Easy mode");
                break;
        }
        return mode;
    }
    public int getMode(){
        return mode;
    }
    public void setView(MainMenuView mainMenuView){
        this.mainMenuView = mainMenuView;
    }
}
