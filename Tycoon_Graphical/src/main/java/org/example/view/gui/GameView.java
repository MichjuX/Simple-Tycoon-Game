package org.example.view.gui;

import org.example.service.GameService;
import org.example.model.Player;
import org.example.model.Worker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameView extends JPanel {
    JButton _button = new JButton("Start");
    JLabel _balance = new JLabel();
    JLabel _profit = new JLabel();
    JLabel _dishes = new JLabel();
    JLabel _dayCustomers = new JLabel();
    JLabel _satisfaction = new JLabel();
    JLabel _speedLabel = new JLabel();
    private GameService gameService;
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
    private List<Worker> workers; // Lista pracowników

    private Runnable returnToMenuCallback;
    private Runnable saveGameCallback;

    // Konstruktor z opcjami powrotu do menu i zapisu gry
    public GameView(Runnable returnToMenuCallback, Runnable saveGameCallback) {
        this.returnToMenuCallback = returnToMenuCallback;
        this.saveGameCallback = saveGameCallback;

        setupView(returnToMenuCallback, saveGameCallback);
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


    private void setupView(Runnable returnToMenuCallback, Runnable saveGameCallback) {
        ///////////////////////////////////////////////////////////////
        // Ładowanie assetów i ustawianie okna
        ImageIcon pixelArtIcon = new ImageIcon("src/main/resources/images/pixelart.png"); // Ścieżka do pixel art
        Image pixelArtImage = pixelArtIcon.getImage();

        ImageIcon buttonIcon = new ImageIcon("src/main/resources/images/button.png");
        Image buttonImage = buttonIcon.getImage();
        buttonIcon = new ImageIcon(buttonImage.getScaledInstance(300, 50, Image.SCALE_SMOOTH));
        ImageIcon buttonSmallerIcon = new ImageIcon(buttonImage.getScaledInstance(150, 25, Image.SCALE_SMOOTH));

        ImageIcon buttonHoverIcon = new ImageIcon("src/main/resources/images/button_hover.png");
        Image buttonHoverImage = buttonHoverIcon.getImage();
        buttonHoverIcon = new ImageIcon(buttonHoverImage.getScaledInstance(300, 50, Image.SCALE_SMOOTH));
        ImageIcon buttonHoverSmallerIcon = new ImageIcon(buttonHoverImage.getScaledInstance(150, 25, Image.SCALE_SMOOTH));

        // Gify
        ImageIcon waiter = new ImageIcon("src/main/resources/images/waiter.gif");

        JLabel waiterLabel = new JLabel(waiter) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Rysowanie animowanego GIF-a w większym rozmiarze
                g.drawImage(waiter.getImage(), 0, 0, 250, 250, this);
            }
        };
        waiterLabel.setPreferredSize(new Dimension(250, 250));
        waiterLabel.setBounds(167, 434, 250, 250);


        ImageIcon cook = new ImageIcon("src/main/resources/images/cook.gif");
        JLabel cookLabel = new JLabel(cook) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Rysowanie animowanego GIF-a w większym rozmiarze
                g.drawImage(cook.getImage(), 0, 0, 400, 251, this);
            }
        };
        cookLabel.setPreferredSize(new Dimension(400, 250));
        cookLabel.setBounds(680, 434, 400, 250);




        BackgroundPanel backgroundPanel = new BackgroundPanel(pixelArtImage);
        backgroundPanel.setLayout(null); // Wyłącz domyślny layout, aby ustawiać komponenty ręcznie
        this.setLayout(new BorderLayout()); // Ustawienie odpowiedniego layoutu
        this.add(backgroundPanel, BorderLayout.CENTER); // Dodanie panelu z tłem
        this.setSize(1920, 1080);

        backgroundPanel.add(waiterLabel);
        backgroundPanel.add(cookLabel);
        ///////////////////////////////////////////////////////////////

        // Wczytywanie obrazu PNG
        ImageIcon imageIcon = new ImageIcon("src/main/resources/images/logo.png");

        // Tworzenie JLabel i ustawianie obrazu jako jego zawartości
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(200, 200, 200, 200);

        // Dodanie JLabel do JFrame
//        backgroundPanel.add(imageLabel);

        ///////////////////////////////////////////////////////////////
        // Ustawiam fonta
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font tinyBig = null;
        Font tinyMedium = null;
        Font tinySmall = null;
        Font tinySmallest = null;
        try {
            File fontFile = new File("src/main/resources/fonts/Tiny5-Regular.ttf");

            // Duża
            tinyBig = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(40f);
            ge.registerFont(tinyBig);

            // Średnia
            tinyMedium = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(25f);
            ge.registerFont(tinyMedium);

            // Mała
            tinySmall = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(20f);
            ge.registerFont(tinySmall);

            tinySmallest = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(15f);
            ge.registerFont(tinySmallest);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Dodaj MouseListener do każdej listy
        addDoubleClickListener(kucharzeList, kucharzeModel, "Please select a cook to upgrade.");
        addDoubleClickListener(kelnerzyList, kelnerzyModel, "Please select a waiter to upgrade.");
        addDoubleClickListener(szefowieKuchniList, szefowieKuchniModel, "Please select a chef to upgrade.");
        addDoubleClickListener(marketingowcyList, marketingowcyModel, "Please select a marketer to upgrade.");
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
        _dayCustomers.setBounds(45, 25, 500, 60); // Zwiększ wysokość, aby pomieścić oba wiersze
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
        Font finalTinySmallest = tinySmallest;
        DefaultListCellRenderer sharedRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setFont(finalTinySmallest);
                renderer.setForeground(Color.WHITE);
                renderer.setOpaque(false);
                return renderer;
            }
        };
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Tworzenie list
        configureList(kelnerzyList, new JScrollPane(), new JLabel(), "Kelnerzy:"
                , 120, 775, backgroundPanel, sharedRenderer, tinySmallest);

        configureList(kucharzeList, new JScrollPane(), new JLabel(), "Kucharze:"
                , 655, 775, backgroundPanel, sharedRenderer, tinySmallest);

        configureList(szefowieKuchniList, new JScrollPane(), new JLabel(), "Szefowie Kuchni:"
                , 1005, 775, backgroundPanel, sharedRenderer, tinySmallest);

        configureList(marketingowcyList, new JScrollPane(), new JLabel(), "Marketingowcy:"
                , 1500, 795, backgroundPanel, sharedRenderer, tinySmallest);
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Przyciski "Kup" dla każdego typu pracownika
        JButton buyWaiterButton = new JButton("Zatrudnij Kelnera", buttonIcon);
        JButton buyCookButton = new JButton("Zatrudnij Kucharza", buttonIcon);
        JButton buyChefButton = new JButton("Zatrudnij Szefa Kuchni", buttonIcon);
        JButton buyMarketerButton = new JButton("Zatrudnij Marketingowca", buttonIcon);

        configureButton(buyWaiterButton, tinySmallest, buttonHoverIcon,
                120, 910, 300, 30,
                buttonIcon, backgroundPanel);
        buyWaiterButton.addActionListener(e -> buy(1));

        configureButton(buyCookButton, tinySmallest, buttonHoverIcon,
                650, 910, 300, 30,
                buttonIcon, backgroundPanel);
        buyCookButton.addActionListener(e -> buy(0));

        configureButton(buyChefButton, tinySmallest, buttonHoverIcon,
                1000, 910, 300, 30,
                buttonIcon, backgroundPanel);
        buyChefButton.addActionListener(e -> buy(2));

        configureButton(buyMarketerButton, tinySmallest, buttonHoverIcon,
                1500, 940, 300, 30,
                buttonIcon, backgroundPanel);
        buyMarketerButton.addActionListener(e -> buy(3));
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Przyciski Menu i zapisu
        JButton returnButton = new JButton("Menu Główne", buttonSmallerIcon);
        configureButton(returnButton, tinySmallest, buttonHoverSmallerIcon,
                1740, 10, 150, 30,
                buttonSmallerIcon, backgroundPanel);
        returnButton.addActionListener(e -> {
            // Niestandardowe przyciski
            Object[] options = {"Tak", "Nie"};
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Czy na pewno chcesz wyjść bez zapisu?",
                    "Potwierdzenie",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, // Ikona (null dla domyślnej)
                    options, // Niestandardowe przyciski
                    options[1] // Domyślnie zaznaczony przycisk ("Nie")
            );

            // Sprawdzenie wyboru użytkownika
            if (choice == JOptionPane.YES_OPTION) {
                if (returnToMenuCallback != null) {
                    returnToMenuCallback.run();
                }
            }
        });
        backgroundPanel.add(returnButton);


        JButton saveButton = new JButton("Zapisz Grę", buttonSmallerIcon);
        configureButton(saveButton, tinySmallest, buttonHoverSmallerIcon,
                1740, 50, 150, 30,
                buttonSmallerIcon, backgroundPanel);
        saveButton.addActionListener(e -> {
            if (saveGameCallback != null) {
                saveGameCallback.run();
            }
        });
        backgroundPanel.add(saveButton);
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Przyciski czasu gry
        JButton speedIncrease = new JButton("Przyspiesz czas", buttonSmallerIcon);
        JButton speedDecrease = new JButton("Zwolnij czas", buttonSmallerIcon);
        configureButton(speedIncrease, tinySmallest, buttonHoverSmallerIcon,
                1570, 10, 150, 30,
                buttonSmallerIcon, backgroundPanel);
        speedIncrease.addActionListener(e -> gameService.changeTimeSpeed("faster"));

        configureButton(speedDecrease, tinySmallest, buttonHoverSmallerIcon,
                1570, 50, 150, 30,
                buttonSmallerIcon, backgroundPanel);

        speedDecrease.addActionListener(e -> gameService.changeTimeSpeed("slower"));

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
    private void addDoubleClickListener(JList<Worker> list, DefaultListModel<Worker> model, String noSelectionMessage) {
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Podwójne kliknięcie
                    int selectedIndex = list.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Worker selectedWorker = model.getElementAt(selectedIndex);
                        int globalIndex = workers.indexOf(selectedWorker); // Znajdź indeks w globalnej liście
                        if (globalIndex != -1 && upgradeCallback != null) {
                            upgradeCallback.upgradeWorker(globalIndex);
                        }
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
    public void setUpgradeCallback(UpgradeCallback upgradeCallback) {
        this.upgradeCallback = upgradeCallback;
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
        this.workers = workers; // Aktualizacja listy pracowników

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
                    System.out.println("znany typ pracownika: " + worker.getName());
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
    public void buy(int id) {
        if (upgradeCallback != null) {
            upgradeCallback.buyWorker(id); // Wywołaj logikę backendu
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

    public void setController(GameService gameService){
        this.gameService = gameService;
    }


}
