# Restaurant Tycoon
## Opis aplikacji
Restaurant Tycoon to typowa gra tycoon gdzie gracz prowadzi swój własny biznes i rozwija własną markę aby zarabiać jak najwięcej pieniędzy.

## Funkcjonalności
- Wiele widoków.
- Klienci przychodzą w czasie rzeczywistym.
- Każdy klient obsłużony przez kelnera daje pieniądze.
- Niezależni klienci, kelnerzy i kucharze oparci na wątkach.
- Mechanika tworzenia i wydawania dań oparta na wątkach.
- Widok zatrudniania pracowników, menu startowe oraz menu wyjścia z gry (ESC).
- Zapis dnych gry do pliku.
- Dynamiczne odświeżanie ekranu.
- Dynamiczna zmiana przedrostka stanu konta oraz dochodu w celu zapobiegnięcia przekroczeniu maksymalnej wartości dla zmiennych.
- Dynamiczne zarządzanie stanem konta oraz wyliczanie aktualnego zarobku ($/danie).

## Opis działania gry
- Gracz wybiera czy chce zacząć nową grę, czy chce wczytać istniejącą grę.
- Gracz startuje grę z pewnym stanem konta oraz jednym zatrudnionym kucharzem i kelnerem.
- W celu zwiększenia przychodu gracz może ulepszać pracowników (zwiększa ilość $/danie) lub zatrudniać nowych pracowników w celu przyspieszenia produkcji:
  - Zatrudnienie kucharza: Zwiększa liczbę produkowanych dań;
  - Zatrudnienie kelnera: Zwiększa liczbę wydawanych dań;
  - Zatrudnienie szefa kuchni: Zwiększa prędkość produkcji dań;
  - Zatrudnienie marketingowca: Zwiększa zainteresowanie klientów lokalem (klienci częściej przychodzą).
- ### UWAGA! Wartość gotowego dania po ulepszeniu pracownika nie zwiększa się! Wyprodukowane dania mają wartość taką jak w czasie ich produkcji. Gra wymaga poprawnego zarządzania ilością pracowników. W ten sposób zatrudnianie zbyt wielu kucharzy jest karane (dania stygną).
