package org.example.view.gui;

import org.example.controllers.GameController;
import org.example.model.Player;
import org.example.model.Worker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private GameController gameController;
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
        // Załaduj pixel art
        ImageIcon pixelArtIcon = new ImageIcon("src/main/resources/images/pixelart.png"); // Ścieżka do pixel art
        Image pixelArtImage = pixelArtIcon.getImage();

        BackgroundPanel backgroundPanel = new BackgroundPanel(pixelArtImage);
        backgroundPanel.setLayout(null); // Wyłącz domyślny layout, aby ustawiać komponenty ręcznie
        this.setLayout(new BorderLayout()); // Ustawienie odpowiedniego layoutu
        this.add(backgroundPanel, BorderLayout.CENTER); // Dodanie panelu z tłem
        this.setSize(1920, 1080);

        // Ustawiam fonta
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font tinyBig = null;
        Font tinyMedium = null;
        Font tinySmall = null;
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

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // Dodaj MouseListener do każdej listy
        addDoubleClickListener(kucharzeList, kucharzeModel, "Please select a cook to upgrade.");
        addDoubleClickListener(kelnerzyList, kelnerzyModel, "Please select a waiter to upgrade.");
        addDoubleClickListener(szefowieKuchniList, szefowieKuchniModel, "Please select a chef to upgrade.");
        addDoubleClickListener(marketingowcyList, marketingowcyModel, "Please select a marketer to upgrade.");
//        _workerList.setToolTipText("Double-click a worker to upgrade.");

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
        ///////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////
        // Monitor w kuchni
        _dishes.setText("Gotowe dania: 0");
        _dishes.setBounds(945, 90, 500, 30);
        _dishes.setFont(tinyBig);
        _dishes.setForeground(Color.WHITE);
        ///////////////////////////////////////////////////////////////


        //Renderer do zmiany wyglądu list
        DefaultListCellRenderer sharedRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setFont(new Font("Arial", Font.BOLD, 14)); // Ustaw czcionkę
                renderer.setOpaque(false); // Ustaw przezroczystość
                return renderer;
            }
        };
        
        // Tworzenie list
        configureList(kucharzeList, new JScrollPane(), new JLabel(), "Kucharze:", 650, 760, backgroundPanel, sharedRenderer);
        configureList(kelnerzyList, new JScrollPane(), new JLabel(), "Kelnerzy:", 120, 760, backgroundPanel, sharedRenderer);
        configureList(szefowieKuchniList, new JScrollPane(), new JLabel(), "Szefowie Kuchni:", 1000, 760, backgroundPanel, sharedRenderer);
        configureList(marketingowcyList, new JScrollPane(), new JLabel(), "Marketingowcy:", 1500, 780, backgroundPanel, sharedRenderer);

        // Przyciski "Kup" dla każdego typu pracownika
        JButton buyCookButton = new JButton("Zatrudnij Kucharza");
        buyCookButton.setBounds(650, 910, 300, 30);
        buyCookButton.addActionListener(e -> buy(0));
        backgroundPanel.add(buyCookButton);

        JButton buyWaiterButton = new JButton("Zatrudnij Kelnera");
        buyWaiterButton.setBounds(120, 910, 300, 30);
        buyWaiterButton.addActionListener(e -> buy(1));
        backgroundPanel.add(buyWaiterButton);

        JButton buyChefButton = new JButton("Zatrudnij Szefa Kuchni");
        buyChefButton.setBounds(1000, 910, 300, 30);
        buyChefButton.addActionListener(e -> buy(2));
        backgroundPanel.add(buyChefButton);

        JButton buyMarketerButton = new JButton("Zatrudnij Marketingowca");
        buyMarketerButton.setBounds(1500, 940, 300, 30);
        buyMarketerButton.addActionListener(e -> buy(3));
        backgroundPanel.add(buyMarketerButton);


        backgroundPanel.add(_balance);
        backgroundPanel.add(_button);
        backgroundPanel.add(_profit);
        backgroundPanel.add(_dishes);
        backgroundPanel.add(_dayCustomers);
        backgroundPanel.add(_satisfaction);

        // Dodanie przycisku powrotu do menu głównego
        JButton returnButton = new JButton("Menu Główne");
        returnButton.setBounds(1740, 10, 150, 30);
        returnButton.addActionListener(e -> {
            if (returnToMenuCallback != null) {
                returnToMenuCallback.run();
            }
        });
        backgroundPanel.add(returnButton);

        // Dodanie przycisku zapisu gry
        JButton saveButton = new JButton("Zapisz Grę");
        saveButton.setBounds(1740, 50, 150, 30);
        saveButton.addActionListener(e -> {
            if (saveGameCallback != null) {
                saveGameCallback.run();
            }
        });
        backgroundPanel.add(saveButton);

        this.setVisible(true);

        System.out.println("Komponenty dodane: " + this.getComponents().length);
    }
    // Ustawienie ClickListenera dla list
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
    // Ustawienie wyglądu list
    private void configureList(JList<?> list,
                               JScrollPane scrollPane,
                               JLabel label,
                               String labelText,
                               int x, int y,
                               BackgroundPanel backgroundPanel,
                               DefaultListCellRenderer sharedRenderer
                               ) {
        // Konfiguracja JLabel
        label.setText(labelText);
        label.setBounds(x, y - 30, 200, 20);
        backgroundPanel.add(label);

        // Konfiguracja JList i JScrollPane
        list.setOpaque(false);
        scrollPane.setViewportView(list);
        scrollPane.setBounds(x, y, 300, 150);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        list.setCellRenderer(sharedRenderer);

        backgroundPanel.add(scrollPane);
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
    private void handleUpgradeAction() {
        int selectedIndex = _workerList.getSelectedIndex();
        if (selectedIndex != -1) {
            if (upgradeCallback != null) {
                upgradeCallback.upgradeWorker(selectedIndex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a worker to upgrade.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    public void setUpgradeCallback(UpgradeCallback upgradeCallback) {
        this.upgradeCallback = upgradeCallback;
    }
    public void updateProfit(double profit) {
        _profit.setText(String.format("Profit: %.2f$/s", profit));
    }
    public void updateProfit(int satisfaction) {
        double profit = gameController.getPlayer().getCurrentProfit();

        switch(satisfaction){
            case 0:
                _profit.setText(String.format("Profit: %.2f$/s", profit));
                break;
            case 1:
                _profit.setText(String.format(
                        "<html><span style='color: white;'>Profit: %.2f$/s </span><span style='color: yellow;'>(-50%%)</span></html>",
                        profit/2
                ));
                break;
            case 2:
                _profit.setText(String.format(
                        "<html><span style='color: white;'>Profit: %.2f$/s </span><span style='color: red;'>(-75%%)</span></html>",
                        profit/4
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
    public void updateDayAndCustomers(int day, int customerCount){
        System.out.println("dzien: " + day);
        day=day%7;
        switch(day){
            case 0:
                _dayCustomersSetText("Poniedziałek", customerCount);
                break;
            case 1:
                _dayCustomersSetText("Wtorek", customerCount);
                break;
            case 2:
                _dayCustomersSetText("Środa", customerCount);
                break;
            case 3:
                _dayCustomersSetText("Czwartek", customerCount);
                break;
            case 4:
                _dayCustomersSetText("Piątek", customerCount);
                break;
            case 5:
                _dayCustomersSetText("Sobota", customerCount);
                break;
            case 6:
                _dayCustomersSetText("Niedziela", customerCount);
                break;
        }
    }
    private void _dayCustomersSetText(String day, int customers) {
        _dayCustomers.setText(
                String.format(
                        "<html>" +
                                "<span style='color: #34e5eb; font-size: 20px;'>%s - </span>" +
                                "<span style='color: #34e5eb; font-size: 20px;'>Klienci w kolejce: %s</span>" +
                                "</html>",
                        day, customers
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

    public void setController(GameController gameController){
        this.gameController = gameController;
    }


}
