# Restaurant Tycoon
## Application Description
Restaurant Tycoon is a typical tycoon game where the player runs their own business and builds their own brand to earn as much money as possible.  
The restaurant serves one delicious dish, which is prepared even when no one has ordered it—so that the customer can receive it immediately after ordering. This way, the restaurant maintains a good reputation and customer satisfaction—who wouldn’t want to get a delicious dish right away?

## Features
- Multiple views.
- Customers arrive in real-time.
- Each customer served by a waiter generates money.
- Independent customers, waiters, and chefs based on threads.
- Dish creation and serving mechanics based on threads.
- Employee hiring view, start menu, and exit game menu (ESC).
- Saving game data to a file.
- Dynamic screen refreshing.
- Dynamic prefix change for the account balance and income to prevent exceeding the maximum value for variables.
- Dynamic management of the account balance and calculation of current earnings ($/dish).

## Gameplay Description
- The player chooses whether to start a new game or load an existing one.
- The player starts the game with a certain account balance and one hired chef and waiter.
- To increase revenue, the player can upgrade employees (increases $/dish) or hire new employees to speed up production:
  - Hiring a chef: Increases the number of dishes produced;
  - Hiring a waiter: Increases the number of dishes served;
  - Hiring a head chef: Increases the speed of dish production;
  - Hiring a marketer: Increases customer interest in the restaurant (customers come more often).
- ### NOTE! The value of a ready-made dish does not increase after upgrading an employee! Dishes produced have the same value as at the time of their production. The game requires proper management of the number of employees. In this way, hiring too many chefs or too few waiters is penalized (dishes get cold).
