package org.example.view.gui;

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
    JLabel _customers = new JLabel();
    JLabel _curretDay = new JLabel();
    JLabel _satisfaction = new JLabel();
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
        try {
            File fontFile = new File("src/main/resources/fonts/Tiny5-Regular.ttf");

            // Duża
            tinyBig = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(40f);
            ge.registerFont(tinyBig);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // Dodaj MouseListener do każdej listy
        kucharzeList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = kucharzeList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Worker selectedWorker = kucharzeModel.getElementAt(selectedIndex);
                        int globalIndex = workers.indexOf(selectedWorker); // Znajdź indeks w globalnej liście
                        if (globalIndex != -1 && upgradeCallback != null) {
                            upgradeCallback.upgradeWorker(globalIndex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(GameView.this,
                                "Please select a cook to upgrade.",
                                "No Selection",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

// Powtórz dla innych list (kelnerzyList, szefowieKuchniList, marketingowcyList)
        kelnerzyList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = kelnerzyList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Worker selectedWorker = kelnerzyModel.getElementAt(selectedIndex);
                        int globalIndex = workers.indexOf(selectedWorker);
                        if (globalIndex != -1 && upgradeCallback != null) {
                            upgradeCallback.upgradeWorker(globalIndex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(GameView.this,
                                "Please select a waiter to upgrade.",
                                "No Selection",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        szefowieKuchniList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = szefowieKuchniList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Worker selectedWorker = szefowieKuchniModel.getElementAt(selectedIndex);
                        int globalIndex = workers.indexOf(selectedWorker);
                        if (globalIndex != -1 && upgradeCallback != null) {
                            upgradeCallback.upgradeWorker(globalIndex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(GameView.this,
                                "Please select a waiter to upgrade.",
                                "No Selection",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        marketingowcyList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = marketingowcyList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Worker selectedWorker = marketingowcyModel.getElementAt(selectedIndex);
                        int globalIndex = workers.indexOf(selectedWorker);
                        if (globalIndex != -1 && upgradeCallback != null) {
                            upgradeCallback.upgradeWorker(globalIndex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(GameView.this,
                                "Please select a waiter to upgrade.",
                                "No Selection",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
//        _workerList.setToolTipText("Double-click a worker to upgrade.");

        // Dodanie etykiety do wyświetlania balansu
        _balance.setText("Balance: 0.00");
        _balance.setBounds(90, 70, 500, 50); // Położenie i rozmiar etykiety
        _balance.setFont(tinyBig);
        _balance.setForeground(Color.WHITE);

        _profit.setText("Profit: 4.00$/s");
        _profit.setBounds(90, 120, 500, 30); // Położenie i rozmiar etykiety
        _profit.setFont(tinyBig);
        _profit.setForeground(Color.WHITE);

        _dishes.setText("Gotowe dania: 0");
        _dishes.setBounds(500, 100, 500, 30);
        _dishes.setFont(new Font("Arial", Font.BOLD, 16)); // Styl czcionki
        _dishes.setForeground(Color.BLACK);

        _customers.setText("Klienci w kolejce: 0");
        _customers.setBounds(500, 150, 200, 30);
        _customers.setFont(new Font("Arial", Font.BOLD, 16)); // Styl czcionki
        _customers.setForeground(Color.BLACK);

        _curretDay.setText("Poniedziałek");
        _curretDay.setBounds(500, 200, 200, 30);
        _customers.setFont(new Font("Arial", Font.BOLD, 16)); // Styl czcionki
        _customers.setForeground(Color.BLACK);

        _satisfaction.setText("Klienci są zadowoleni");
        _satisfaction.setBounds(400, 250, 400, 30);
        _satisfaction.setFont(new Font("Arial", Font.BOLD, 16)); // Styl czcionki
        _satisfaction.setForeground(Color.BLACK);

        JScrollPane kucharzeScroll = new JScrollPane(kucharzeList);
        kucharzeScroll.setBounds(650, 760, 300, 150);
        backgroundPanel.add(kucharzeScroll);
        JLabel kucharzeLabel = new JLabel("Kucharze:");
        kucharzeLabel.setBounds(650, 730, 200, 20);
        backgroundPanel.add(kucharzeLabel);

        JScrollPane kelnerzyScroll = new JScrollPane(kelnerzyList);
        kelnerzyScroll.setBounds(120, 760, 300, 150);
        backgroundPanel.add(kelnerzyScroll);
        JLabel kelnerzyLabel = new JLabel("Kelnerzy:");
        kelnerzyLabel.setBounds(180, 730, 200, 20);
        backgroundPanel.add(kelnerzyLabel);

        JScrollPane szefowieScroll = new JScrollPane(szefowieKuchniList);
        szefowieScroll.setBounds(1000, 760, 300, 150);
        backgroundPanel.add(szefowieScroll);
        JLabel szefowieLabel = new JLabel("Szefowie Kuchni:");
        szefowieLabel.setBounds(1000, 730, 200, 20);
        backgroundPanel.add(szefowieLabel);

        JScrollPane marketingowcyScroll = new JScrollPane(marketingowcyList);
        marketingowcyScroll.setBounds(1500, 780, 300, 150);
        backgroundPanel.add(marketingowcyScroll);
        JLabel marketingowcyLabel = new JLabel("Marketingowcy:");
        marketingowcyLabel.setBounds(1500, 750, 200, 20);
        backgroundPanel.add(marketingowcyLabel);

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
        backgroundPanel.add(_customers);
        backgroundPanel.add(_curretDay);
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
    public void updateDishes(int dishCount) {
        _dishes.setText(String.format("Gotowe dania: %d", dishCount));
    }
    public void updateCustomers(int customerCount) {
        _customers.setText(String.format("Klienci w kolejce: %d", customerCount));
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

    public void updateDay(int day){
        System.out.println("dzien: " + day);
        day=day%7;
        switch(day){
            case 0:
                _curretDay.setText("Poniedziałek");
                break;
            case 1:
                _curretDay.setText("Wtorek");
                break;
            case 2:
                _curretDay.setText("Środa");
                break;
            case 3:
                _curretDay.setText("Czwartek");
                break;
            case 4:
                _curretDay.setText("Piątek");
                break;
            case 5:
                _curretDay.setText("Sobota");
                break;
            case 6:
                _curretDay.setText("Niedziela");
                break;
        }
    }
    public void updateSatisfaction(int satisfaction){
        switch(satisfaction){
            case 0:
                _satisfaction.setText("Klienci są zadowoleni");
                break;
            case 1:
                _satisfaction.setText("Klienci się niecierpliwią (-50% zysku)");
                break;
            case 2:
                _satisfaction.setText("Klienci są niezadowoleni (-75% zysku)");
                break;
        }
    }



}
