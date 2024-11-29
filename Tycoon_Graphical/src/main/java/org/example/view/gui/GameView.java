package org.example.view.gui;

import org.example.model.Player;
import org.example.model.Worker;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameView extends JPanel {
    JButton _button = new JButton("Start");
    JLabel _balance = new JLabel();
    JLabel _profit = new JLabel();
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

    // Konstruktor bezargumentowy (domyślny)
    public GameView() {
        setupView(null);
    }

    // Konstruktor z parametrem Runnable (powrót do menu głównego)
    public GameView(Runnable returnToMenuCallback) {
        setupView(returnToMenuCallback);
    }


    private void setupView(Runnable returnToMenuCallback) {
        ImageIcon backgroundIcon = new ImageIcon("src/main/resources/images/background.png"); // Ścieżka do obrazka
        Image backgroundImage = backgroundIcon.getImage();

        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.setLayout(null); // Wyłącz domyślny layout, aby ustawiać komponenty ręcznie
        this.setLayout(new BorderLayout()); // Ustawienie odpowiedniego layoutu
        this.add(backgroundPanel, BorderLayout.CENTER);
        this.setSize(1920, 1080);

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
        _balance.setBounds(200, 20, 200, 30); // Położenie i rozmiar etykiety
        _balance.setFont(new Font("Arial", Font.BOLD, 16)); // Styl czcionki
        _balance.setForeground(Color.BLACK);

        _profit.setText("Profit: 4.00$/s");
        _profit.setBounds(200, 70, 200, 30); // Położenie i rozmiar etykiety
        _profit.setFont(new Font("Arial", Font.BOLD, 16)); // Styl czcionki
        _profit.setForeground(Color.BLACK);

        JScrollPane kucharzeScroll = new JScrollPane(kucharzeList);
        kucharzeScroll.setBounds(700, 150, 200, 150);
        backgroundPanel.add(kucharzeScroll);
        JLabel kucharzeLabel = new JLabel("Kucharze:");
        kucharzeLabel.setBounds(700, 120, 200, 20);
        backgroundPanel.add(kucharzeLabel);

        JScrollPane kelnerzyScroll = new JScrollPane(kelnerzyList);
        kelnerzyScroll.setBounds(10, 150, 200, 150);
        backgroundPanel.add(kelnerzyScroll);
        JLabel kelnerzyLabel = new JLabel("Kelnerzy:");
        kelnerzyLabel.setBounds(10, 120, 200, 20);
        backgroundPanel.add(kelnerzyLabel);

        JScrollPane szefowieScroll = new JScrollPane(szefowieKuchniList);
        szefowieScroll.setBounds(1000, 100, 200, 150);
        backgroundPanel.add(szefowieScroll);
        JLabel szefowieLabel = new JLabel("Szefowie Kuchni:");
        szefowieLabel.setBounds(1000, 70, 200, 20);
        backgroundPanel.add(szefowieLabel);

        JScrollPane marketingowcyScroll = new JScrollPane(marketingowcyList);
        marketingowcyScroll.setBounds(1500, 130, 200, 150);
        backgroundPanel.add(marketingowcyScroll);
        JLabel marketingowcyLabel = new JLabel("Marketingowcy:");
        marketingowcyLabel.setBounds(1500, 100, 200, 20);
        backgroundPanel.add(marketingowcyLabel);

        // Przyciski "Kup" dla każdego typu pracownika
        JButton buyCookButton = new JButton("Kup Kucharza");
        buyCookButton.setBounds(700, 310, 150, 30);
        buyCookButton.addActionListener(e -> buy(0));
        backgroundPanel.add(buyCookButton);

        JButton buyWaiterButton = new JButton("Kup Kelnera");
        buyWaiterButton.setBounds(10, 310, 150, 30);
        buyWaiterButton.addActionListener(e -> buy(1));
        backgroundPanel.add(buyWaiterButton);

        JButton buyChefButton = new JButton("Kup Szefa Kuchni");
        buyChefButton.setBounds(1000, 260, 150, 30);
        buyChefButton.addActionListener(e -> buy(2));
        backgroundPanel.add(buyChefButton);

        JButton buyMarketerButton = new JButton("Kup Marketingowca");
        buyMarketerButton.setBounds(1500, 290, 150, 30);
        buyMarketerButton.addActionListener(e -> buy(3));
        backgroundPanel.add(buyMarketerButton);


        backgroundPanel.add(_balance);
        backgroundPanel.add(_button);
        backgroundPanel.add(_profit);

        this.setVisible(true);

        System.out.println("Komponenty dodane: " + this.getComponents().length);
    }


    public void updateBalance(double balance, Player player) {
        _balance.setText(String.format(String.format("Stan konta: %.2f%s$",
                balance,
                _prefixList[player.getBalancePrefix()])));
        _balance.setBounds(200, 50, 200, 30); // Położenie i rozmiar
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



}
