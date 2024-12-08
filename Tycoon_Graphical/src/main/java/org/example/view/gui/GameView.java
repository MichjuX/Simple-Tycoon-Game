package org.example.view.gui;

import org.example.controllers.GameController;
import org.example.service.GameService;
import org.example.model.Player;
import org.example.model.Worker;
import org.example.service.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GameView extends JPanel {
    JButton _button = new JButton("Start");
    JLabel _balance = new JLabel();
    JLabel _profit = new JLabel();
    JLabel _dishes = new JLabel();
    JLabel _dayCustomers = new JLabel();
    JLabel _satisfaction = new JLabel();
    JLabel _speedLabel = new JLabel();
    JButton buyTipJarButton;
    JButton buyPaintingButton;
    private JButton openShopButton;
    private BackgroundPanel shopPanel;
    private JButton closeShopButton;
    private GameService gameService;
    private GameController controller;
    private BackgroundPanel backgroundPanel;
    private JButton _upgradeButton = new JButton("Upgrade");
    private final String[] _prefixList = {"", "k", "M", "B", "T", "Qa", "Qi", "Sx", "Sp", "O", "N", "D"};
    private JList<String> _workerList = new JList<>();
    private DefaultListModel<String> _workerListModel = new DefaultListModel<>();
    private UpgradeCallback upgradeCallback;

    private DefaultListModel<Worker> kucharzeModel = new DefaultListModel<>();
    private DefaultListModel<Worker> kelnerzyModel = new DefaultListModel<>();
    private DefaultListModel<Worker> szefowieKuchniModel = new DefaultListModel<>();
    private DefaultListModel<Worker> marketingowcyModel = new DefaultListModel<>();

    private JList<Worker> kucharzeList = new JList<>(kucharzeModel);
    private JList<Worker> kelnerzyList = new JList<>(kelnerzyModel);
    private JList<Worker> szefowieKuchniList = new JList<>(szefowieKuchniModel);
    private JList<Worker> marketingowcyList = new JList<>(marketingowcyModel);
     // Lista pracowników

    private Runnable returnToMenuCallback;
    private Runnable saveGameCallback;
    private org.example.view.gui.Callback Callback;

    // Konstruktor z opcjami powrotu do menu i zapisu gry
    public GameView() {
        setupView();
        setupEscapeKeyListener();
    }

    private void setupEscapeKeyListener() {
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    showPauseMenu();
                }
            }
        });
    }

    private void showPauseMenu() {
        String[] options = {"Wyjdź bez zapisu", "Zapisz i wyjdź", "Anuluj"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Wybierz opcję:",
                "Menu pauzy",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[2] // Domyślna opcja "Anuluj"
        );

        switch (choice) {
            case 0 -> {
                // Wyjdź bez zapisu
                if (returnToMenuCallback != null) {
                    returnToMenuCallback.run();
                }
            }
            case 1 -> {
                // Zapisz i wyjdź
                if (saveGameCallback != null) {
                    saveGameCallback.run();
                }
                if (returnToMenuCallback != null) {
                    returnToMenuCallback.run();
                }
            }
            case 2 -> {
                // Anuluj
                // Nic nie robimy, wracamy do gry
            }
        }
    }


    private void setupView() {
        ///////////////////////////////////////////////////////////////
        // Ładowanie assetów i ustawianie okna
        ImageIcon pixelArtIcon = ResourceLoader.loadImage("src/main/resources/images/backgrounds/pixelart.png");
        Image pixelArtImage = pixelArtIcon.getImage();

        ImageIcon[] buttonIcons = ResourceLoader.loadImageIcons("src/main/resources/images/buttons/button.png", 290, 30);
        ImageIcon buttonIcon = buttonIcons[0];
        ImageIcon buttonSmallerIcon = buttonIcons[1];

        ImageIcon buttonHoverIcon = ResourceLoader.loadScaledImage("src/main/resources/images/buttons/button_hover.png", 290, 30);
        ImageIcon buttonHoverSmallerIcon = ResourceLoader.loadScaledImage("src/main/resources/images/button_hover.png", 150, 25);

        // Gify
        JLabel waiterLabel = ResourceLoader.createAnimatedLabel("src/main/resources/images/waiter.gif", 250, 250);
        waiterLabel.setBounds(167, 434, 250, 250);

        JLabel cookLabel = ResourceLoader.createAnimatedLabel("src/main/resources/images/cook.gif", 400, 251);
        cookLabel.setBounds(674, 434, 400, 251);

        JLabel ventLabel = ResourceLoader.createAnimatedLabel("src/main/resources/images/vent.gif", 150, 145);
        ventLabel.setBounds(635, 125, 150, 145);


        BackgroundPanel backgroundPanel = new BackgroundPanel(pixelArtImage);
        this.backgroundPanel = backgroundPanel;
        backgroundPanel.setLayout(null); // Wyłącz domyślny layout, aby ustawiać komponenty ręcznie
        this.setLayout(new BorderLayout()); // Ustawienie odpowiedniego layoutu
        this.add(backgroundPanel, BorderLayout.CENTER); // Dodanie panelu z tłem
        this.setSize(1920, 1080);

        backgroundPanel.add(waiterLabel);
        backgroundPanel.add(cookLabel);
        backgroundPanel.add(ventLabel);
        ///////////////////////////////////////////////////////////////

        // Wczytywanie obrazu PNG
        ImageIcon imageIcon = new ImageIcon("src/main/resources/images/logo.png");

        ///////////////////////////////////////////////////////////////
        // Ustawiam fonta
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // Duża
        Font tinyBig = ResourceLoader.loadFont("src/main/resources/fonts/Tiny5-Regular.ttf", 40f);
        ge.registerFont(tinyBig);

        // Średnia
        Font tinyMedium = ResourceLoader.loadFont("src/main/resources/fonts/Tiny5-Regular.ttf", 25f);
        ge.registerFont(tinyMedium);

        // Mała
        Font tinySmall = ResourceLoader.loadFont("src/main/resources/fonts/Tiny5-Regular.ttf", 20f);
        ge.registerFont(tinySmall);

        Font tinySmallest = ResourceLoader.loadFont("src/main/resources/fonts/Tiny5-Regular.ttf", 15f);
        ge.registerFont(tinySmallest);

        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Dodaj MouseListener do każdej listy
        addDoubleClickListener(kucharzeList, kucharzeModel, "Please select a cook to upgrade.");
        addDoubleClickListener(kelnerzyList, kelnerzyModel, "Please select a waiter to upgrade.");
        addDoubleClickListener(szefowieKuchniList, szefowieKuchniModel, "Please select a chef to upgrade.");
        addDoubleClickListener(marketingowcyList, marketingowcyModel, "Please select a marketer to upgrade.");

        kucharzeList.setToolTipText("Kliknij dwukrotnie, aby ulepszyć.");
        kelnerzyList.setToolTipText("Kliknij dwukrotnie, aby ulepszyć.");
        szefowieKuchniList.setToolTipText("Kliknij dwukrotnie, aby ulepszyć.");
        marketingowcyList.setToolTipText("Kliknij dwukrotnie, aby ulepszyć.");
//        _workerList.setToolTipText("Double-click a worker to upgrade.");
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Monitor kelnera
        _dayCustomers.setText(
                "<html>" +
                        "<span style='color: #34e5eb; font-size: 20px;'>Poniedziałek - </span>" +
                        "<span style='color: #34e5eb; font-size: 20px;'>Klienci w kolejce: 0</span>" +
                        "</html>"
        );
        _dayCustomers.setBounds(45, 25, 700, 60); // Zwiększ wysokość, aby pomieścić oba wiersze
        _dayCustomers.setFont(tinyMedium);
        _dayCustomers.setForeground(Color.cyan);

        _balance.setText("Balance: 0.00");
        _balance.setBounds(45, 60, 500, 50);
        _balance.setFont(tinyBig);
        _balance.setForeground(Color.WHITE);

        _profit.setText("Profit: 4.00$/s");
        _profit.setBounds(45, 110, 500, 30);
        _profit.setFont(tinyBig);
        _profit.setForeground(Color.WHITE);

        _satisfaction.setText("Klienci są zadowoleni");
        _satisfaction.setBounds(45, 145, 600, 30);
        _satisfaction.setFont(tinyMedium);
        _satisfaction.setForeground(Color.GREEN);

        backgroundPanel.add(_balance);
        backgroundPanel.add(_button);
        backgroundPanel.add(_profit);
        backgroundPanel.add(_dayCustomers);
        backgroundPanel.add(_satisfaction);
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Monitor w kuchni
        _dishes.setText("Gotowe dania: 0");
        _dishes.setBounds(850, 90, 500, 30);
        _dishes.setFont(tinyBig);
        _dishes.setForeground(Color.WHITE);

        backgroundPanel.add(_dishes);
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Wyświetlacz czasu
        _speedLabel.setText("Czas: 1.00x");
        _speedLabel.setBounds(1740, 110, 150, 30);
        _speedLabel.setFont(tinySmall);
        _speedLabel.setForeground(Color.WHITE);
        backgroundPanel.add(_speedLabel);
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        //Renderer do zmiany wyglądu list
        DefaultListCellRenderer sharedRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setFont(tinySmallest);
                renderer.setForeground(Color.WHITE);
                renderer.setOpaque(false);
                return renderer;
            }
        };
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Tworzenie list
        configureList(kelnerzyList, new JScrollPane(), new JLabel(), "Kelnerzy:"
                , 129, 785, backgroundPanel, sharedRenderer, tinySmallest);

        configureList(kucharzeList, new JScrollPane(), new JLabel(), "Kucharze:"
                , 663, 775, backgroundPanel, sharedRenderer, tinySmallest);

        configureList(szefowieKuchniList, new JScrollPane(), new JLabel(), "Szefowie Kuchni:"
                , 1001, 775, backgroundPanel, sharedRenderer, tinySmallest);

        configureList(marketingowcyList, new JScrollPane(), new JLabel(), "Marketingowcy:"
                , 1507, 815, backgroundPanel, sharedRenderer, tinySmallest);
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Przyciski "Kup" dla każdego typu pracownika
        JButton buyWaiterButton = new JButton("Zatrudnij Kelnera", buttonIcon);
        JButton buyCookButton = new JButton("Zatrudnij Kucharza", buttonIcon);
        JButton buyChefButton = new JButton("Zatrudnij Szefa Kuchni", buttonIcon);
        JButton buyMarketerButton = new JButton("Zatrudnij Marketingowca", buttonIcon);

        configureButton(buyWaiterButton, tinySmallest, buttonHoverIcon,
                124, 915, 300, 30,
                buttonIcon, backgroundPanel);
        buyWaiterButton.addActionListener(e -> controller.handleBuyAction(1));

        configureButton(buyCookButton, tinySmallest, buttonHoverIcon,
                659, 905, 300, 30,
                buttonIcon, backgroundPanel);
        buyCookButton.addActionListener(e -> controller.handleBuyAction(0));

        configureButton(buyChefButton, tinySmallest, buttonHoverIcon,
                997, 905, 300, 30,
                buttonIcon, backgroundPanel);
        buyChefButton.addActionListener(e -> controller.handleBuyAction(2));

        configureButton(buyMarketerButton, tinySmallest, buttonHoverIcon,
                1502, 944, 300, 30,
                buttonIcon, backgroundPanel);
        buyMarketerButton.addActionListener(e -> controller.handleBuyAction(3));
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Przyciski Menu i zapisu
        ImageIcon navigationButtonIcon = ResourceLoader.loadScaledImage("src/main/resources/images/buttons/button_navigation.png", 150, 30);
        ImageIcon navigationButtonHoverIcon = ResourceLoader.loadScaledImage("src/main/resources/images/buttons/button_navigation_hover.png", 150, 30);
        JButton returnButton = new JButton("Menu Główne", navigationButtonIcon);
        configureButton(returnButton, tinySmallest, navigationButtonHoverIcon,
                1744, 10, 150, 30,
                navigationButtonIcon, backgroundPanel);
        returnButton.addActionListener(e -> controller.handleReturnToMenu());
        backgroundPanel.add(returnButton);


        JButton saveButton = new JButton("Zapisz Grę", navigationButtonIcon);
        configureButton(saveButton, tinySmallest, navigationButtonHoverIcon,
                1744, 57, 150, 30,
                navigationButtonIcon, backgroundPanel);
        saveButton.addActionListener(e -> controller.handleSaveGame());
        backgroundPanel.add(saveButton);
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Przyciski czasu gry
        JButton speedIncrease = new JButton("Przyspiesz czas", navigationButtonIcon);
        JButton speedDecrease = new JButton("Zwolnij czas", navigationButtonIcon);
        configureButton(speedIncrease, tinySmallest, navigationButtonHoverIcon,
                1568, 10, 150, 30,
                navigationButtonIcon, backgroundPanel);
        speedIncrease.addActionListener(e -> controller.increaseGameSpeed());

        configureButton(speedDecrease, tinySmallest, navigationButtonHoverIcon,
                1568, 57, 150, 30,
                navigationButtonIcon, backgroundPanel);

        speedDecrease.addActionListener(e -> controller.decreaseGameSpeed());

        createShopPanel(tinySmallest);
        createOpenShopButton();

        this.setVisible(true);

        System.out.println("Komponenty dodane: " + this.getComponents().length);
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
        // Dodanie MouseListener do obsługi zdarzeń myszy
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(buttonHoverIcon); // Zmień ikonę
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(buttonIcon); // Przywróć ikonę
            }
        });
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        backgroundPanel.add(button);
    }


    ///////////////////////////////////////////////////////////////
    // Ustawienie Click Listener dla list
    public void addDoubleClickListener(JList<Worker> list, DefaultListModel<Worker> model, String noSelectionMessage) {
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Podwójne kliknięcie
                    int selectedIndex = list.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Worker selectedWorker = model.getElementAt(selectedIndex);
                        System.out.println("wejde");
                        controller.handleWorkerUpgrade(selectedWorker);
                    } else {
                        JOptionPane.showMessageDialog(GameView.this,
                                noSelectionMessage,
                                "No Selection",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
    }


    ///////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    // Ustawienie wyglądu list
    private void configureList(JList<?> list,
                               JScrollPane scrollPane,
                               JLabel label,
                               String labelText,
                               int x, int y,
                               BackgroundPanel backgroundPanel,
                               DefaultListCellRenderer sharedRenderer,
                               Font font
                               ) {
        // Konfiguracja JLabel
        label.setText(labelText);
        label.setBounds(x, y - 30, 250, 20);
        label.setForeground(Color.WHITE);
        label.setFont(font);
        backgroundPanel.add(label);

        // Konfiguracja JList i JScrollPane
        list.setOpaque(false);
        scrollPane.setViewportView(list);
        scrollPane.setBounds(x, y, 300, 120);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        list.setCellRenderer(sharedRenderer);
        configureVerticalScrollBar(scrollPane);
        configureHorizontalScrollBar(scrollPane);
        backgroundPanel.add(scrollPane);

        backgroundPanel.add(scrollPane);
    }
    ///////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    // Scrollbary
    private void configureVerticalScrollBar(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.WHITE; // Kolor uchwytu
                this.thumbHighlightColor = Color.WHITE; // Jasny obrys uchwytu
                this.thumbDarkShadowColor = Color.WHITE; // Ciemny obrys uchwytu
                this.trackColor = Color.LIGHT_GRAY; // Kolor tła
                this.trackHighlightColor = Color.WHITE; // Obszar pod uchwytem
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createInvisibleButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createInvisibleButton();
            }

            private JButton createInvisibleButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });
    }
    private void configureHorizontalScrollBar(JScrollPane scrollPane) {
        scrollPane.getHorizontalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.WHITE; // Kolor uchwytu
                this.thumbHighlightColor = Color.WHITE; // Jasny obrys uchwytu
                this.thumbDarkShadowColor = Color.WHITE; // Ciemny obrys uchwytu
                this.trackColor = Color.LIGHT_GRAY; // Kolor tła
                this.trackHighlightColor = Color.WHITE; // Obszar pod uchwytem
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createInvisibleButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createInvisibleButton();
            }

            private JButton createInvisibleButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });
    }
    ///////////////////////////////////////////////////////////////
    JButton buyFlowerButton;
    JButton buyCactusButton;
    JButton buyRackButton;
    ///////////////////////////////////////////////////////////////
    // Sklep
    private void createShopPanel(Font tinySmallest) {
        ImageIcon backgroundImage = ResourceLoader.loadScaledImage("src/main/resources/images/backgrounds/buy_menu.png", 250, 270);
        shopPanel = new BackgroundPanel(backgroundImage.getImage());

        shopPanel.setLayout(new BoxLayout(shopPanel, BoxLayout.Y_AXIS));
        shopPanel.setBackground(Color.WHITE);
        shopPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        ImageIcon navigationButtonIcon = ResourceLoader.loadScaledImage("src/main/resources/images/buttons/button_buy_menu_buy.png",
                250, 30);
        ImageIcon navigationButtonHoverIcon = ResourceLoader.loadScaledImage("src/main/resources/images/buttons/button_buy_menu_buy_hover.png",
                250, 30);


        // Dodaj przyciski do kupowania przedmiotów
        buyTipJarButton = new JButton("Słoik na tipy - 10000$", navigationButtonIcon);
        buyPaintingButton = new JButton("Kup obaz - 50000$", navigationButtonIcon);
        buyFlowerButton = new JButton("Kup kwiat - 100000$", navigationButtonIcon);
        buyCactusButton = new JButton("Kup kaktusa - 150000$", navigationButtonIcon);
        buyRackButton = new JButton("Kup szafke - 200000$", navigationButtonIcon);


        configureButton(buyTipJarButton, tinySmallest, navigationButtonHoverIcon,
                0, 0, 250, 30, navigationButtonIcon, shopPanel);

        configureButton(buyPaintingButton, tinySmallest, navigationButtonHoverIcon,
                0, 0, 250, 30, navigationButtonIcon, shopPanel);

        configureButton(buyFlowerButton, tinySmallest, navigationButtonHoverIcon,
                0, 0, 250, 30, navigationButtonIcon, shopPanel);

        configureButton(buyCactusButton, tinySmallest, navigationButtonHoverIcon,
                0, 0, 250, 30, navigationButtonIcon, shopPanel);

        configureButton(buyRackButton, tinySmallest, navigationButtonHoverIcon,
                0, 0, 250, 30, navigationButtonIcon, shopPanel);



        buyTipJarButton.addActionListener(e -> controller.handleBuyDecoration(10000, 0));
        buyPaintingButton.addActionListener(e -> controller.handleBuyDecoration(50000, 1));
        buyFlowerButton.addActionListener(e -> controller.handleBuyDecoration(100000, 2));
        buyCactusButton.addActionListener(e -> controller.handleBuyDecoration(150000, 3));
        buyRackButton.addActionListener(e -> controller.handleBuyDecoration(200000, 4));

        shopPanel.add(Box.createRigidArea(new Dimension(45, 20)));
        shopPanel.add(buyTipJarButton);
        shopPanel.add(Box.createRigidArea(new Dimension(45, 10)));
        shopPanel.add(buyPaintingButton);
        shopPanel.add(Box.createRigidArea(new Dimension(45, 10)));
        shopPanel.add(buyFlowerButton);
        shopPanel.add(Box.createRigidArea(new Dimension(45, 10)));
        shopPanel.add(buyCactusButton);
        shopPanel.add(Box.createRigidArea(new Dimension(45, 10)));
        shopPanel.add(buyRackButton);


        // Dodaj przycisk zamknięcia sklepu
        closeShopButton = new JButton("Zamknij sklep", navigationButtonIcon);
        configureButton(closeShopButton, tinySmallest, navigationButtonHoverIcon,
                0, 0, 250, 30, navigationButtonIcon, shopPanel);
        closeShopButton.addActionListener(e -> hideShopPanel());
        shopPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        shopPanel.add(closeShopButton);

        // Ukryj panel sklepu na początku
        shopPanel.setVisible(false);
        backgroundPanel.add(shopPanel);
    }
    private void createOpenShopButton() {
        ImageIcon button = ResourceLoader.loadScaledImage("src/main/resources/images/buttons/button_buy_menu.png", 90, 90);
        openShopButton = new JButton(" ", button);
        configureButton(openShopButton,
                new Font("Arial", Font.PLAIN, 20),
                ResourceLoader.loadScaledImage("src/main/resources/images/buttons/button_buy_menu_hover.png", 90, 90),
                0, 0, 90, 90, button, backgroundPanel);
        openShopButton.setBounds(1420, 20, 90, 90); // Dostosuj pozycję według potrzeb
        openShopButton.addActionListener(e -> showShopPanel());
        backgroundPanel.add(openShopButton);
    }
    private void showShopPanel() {
        shopPanel.setVisible(true);
        shopPanel.setBounds(1300, 100, 300, 270); // Dostosuj pozycję i rozmiar według potrzeb
    }

    private void hideShopPanel() {
        shopPanel.setVisible(false);
    }

    public void displayDecoration(int id){
        System.out.println("wyświetlam");
        switch(id){
            case 0:
                JLabel tipJar = ResourceLoader.createAnimatedLabel("src/main/resources/images/decorations/tip-jar.png", 50, 70);
                tipJar.setBounds(129, 615, 50, 70);
                backgroundPanel.add(tipJar);
                buyTipJarButton.setVisible(false);

                break;
            case 1:
                JLabel painting = ResourceLoader.createAnimatedLabel("src/main/resources/images/decorations/painting.png", 210, 140);
                painting.setBounds(1550, 250, 210, 140);
                backgroundPanel.add(painting);
                buyPaintingButton.setVisible(false);
                break;
            case 2:
                JLabel flower = ResourceLoader.createAnimatedLabel("src/main/resources/images/decorations/flower.png", 72, 154);
                flower.setBounds(465, 530, 70, 160);
                backgroundPanel.add(flower);
                buyFlowerButton.setVisible(false);
                break;
            case 3:
                JLabel cactus  = ResourceLoader.createAnimatedLabel("src/main/resources/images/decorations/cactus.png", 60, 140);
                cactus.setBounds(1805, 575, 60, 140);
                backgroundPanel.add(cactus);
                buyCactusButton.setVisible(false);
                break;
            case 4:
                JLabel rack = ResourceLoader.createAnimatedLabel("src/main/resources/images/decorations/rack.png", 386, 164);
                rack.setBounds(655, 376, 390, 170);
                backgroundPanel.add(rack);
                buyRackButton.setVisible(false);
            break;

        }
        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }
    ///////////////////////////////////////////////////////////////

    public void displayObject(int objectIndex) {
        switch(objectIndex){
            case 0:
                System.out.println("wyświetlam bossa");
                JLabel bossLabel = ResourceLoader.createAnimatedLabel("src/main/resources/images/boss.gif", 250, 250);
                bossLabel.setBounds(1100, 434, 250, 250);
                backgroundPanel.add(bossLabel);
                break;
            case 1:
                System.out.println("wyświetlam kurwa");
                JLabel marketer = ResourceLoader.createAnimatedLabel("src/main/resources/images/marketer.gif", 400, 251);
                marketer.setBounds(1450, 444, 400, 251);
                backgroundPanel.add(marketer);
                break;
        }
        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    public void updateBalance(double balance, Player player) {
        _balance.setText(String.format(String.format("Stan konta: %.2f%s$",
                balance,
                _prefixList[player.getBalancePrefix()])));
    }


    public void updateWorkerList(List<Worker> workers) {
        _workerListModel.clear(); // Czyści starą listę
        for (Worker worker : workers) {
            _workerListModel.addElement(worker.toString()); // Dodaje pracowników
        }
    }
    public void setCallback(Callback Callback) {
        this.Callback = Callback;
    }
    public void updateProfit(double profit) {
        int prefix = gameService.getPlayer().getDisplayedProfitPrefix();
        _profit.setText(String.format("Profit: %.2f%s$/klient", profit, _prefixList[prefix]));
    }
    public void updateProfit(int satisfaction) {
        double profit = gameService.getPlayer().getDisplayedProfit();
        int prefix = gameService.getPlayer().getDisplayedProfitPrefix();

        switch(satisfaction){
            case 0:
                _profit.setText(String.format("Profit: %.2f%s$/klient", profit, _prefixList[prefix]));
                break;
            case 1:
                _profit.setText(String.format(
                        "<html><span style='color: white;'>Profit: %.2f%s$/klient</span><span style='color: yellow;'>(-50%%)</span></html>",
                        profit/2, _prefixList[prefix]
                ));
                break;
            case 2:
                _profit.setText(String.format(
                        "<html><span style='color: white;'>Profit: %.2f%s$/klient</span><span style='color: red;'>(-75%%)</span></html>",
                        profit/4, _prefixList[prefix]
                ));
                break;
        }
    }

    public void updateDishes(int dishCount) {
        _dishes.setText(String.format("Gotowe dania: %d", dishCount));
    }
    public void updateWorkerLists(List<Worker> workers) {
        gameService.setWorkers(workers);

        kucharzeModel.clear();
        kelnerzyModel.clear();
        szefowieKuchniModel.clear();
        marketingowcyModel.clear();

        for (Worker worker : workers) {
            switch (worker.getName()) {
                case "Kucharz":
                    kucharzeModel.addElement(worker);
                    break;
                case "Kelner":
                    kelnerzyModel.addElement(worker);
                    break;
                case "Szef kuchni":
                    szefowieKuchniModel.addElement(worker);
                    break;
                case "Marketingowiec":
                    marketingowcyModel.addElement(worker);
                    break;
                default:
                    System.out.println("Nieznany typ pracownika: " + worker.getName());
            }
        }
    }
    public void updateDayAndCustomers(int day, int[] hour, int customerCount){
        day=day%7;
        switch(day){
            case 0:
                _dayCustomersSetText("Poniedziałek", hour, customerCount);
                break;
            case 1:
                _dayCustomersSetText("Wtorek", hour, customerCount);
                break;
            case 2:
                _dayCustomersSetText("Środa", hour, customerCount);
                break;
            case 3:
                _dayCustomersSetText("Czwartek", hour, customerCount);
                break;
            case 4:
                _dayCustomersSetText("Piątek", hour, customerCount);
                break;
            case 5:
                _dayCustomersSetText("Sobota", hour, customerCount);
                break;
            case 6:
                _dayCustomersSetText("Niedziela", hour, customerCount);
                break;
        }
    }
    private void _dayCustomersSetText(String day, int[] hour, int customers) {
        _dayCustomers.setText(
                String.format(
                        "<html>" +
                                "<span style='color: #34e5eb; font-size: 20px;'>%s </span>" +
                                "<span style='color: #34e5eb; font-size: 20px;'>%02d:%02d - </span>" +
                                "<span style='color: #34e5eb; font-size: 20px;'>Klienci w kolejce: %s</span>" +
                                "</html>",
                        day, hour[0], hour[1], customers
                )
        );
    }


    public void updateSatisfaction(int satisfaction){
        updateProfit(satisfaction);
        switch(satisfaction){
            case 0:
                _satisfaction.setText("Klienci są zadowoleni");
                _satisfaction.setForeground(Color.GREEN);
                break;
            case 1:
                _satisfaction.setText("Klienci się niecierpliwią");
                _satisfaction.setForeground(Color.YELLOW);
                break;
            case 2:
                _satisfaction.setText("Klienci są niezadowoleni");
                _satisfaction.setForeground(Color.RED);
                break;
        }
    }
    public void updateSpeedLabel(double speed) {
        String speedText = String.format("Czas: %.2fx", speed);
        _speedLabel.setText(speedText);
        System.out.println("Speed: " + speed);
    }

    public void setService(GameService gameService){
        this.gameService = gameService;
    }
    public void setController(GameController controller){
        this.controller = controller;
    }
}
