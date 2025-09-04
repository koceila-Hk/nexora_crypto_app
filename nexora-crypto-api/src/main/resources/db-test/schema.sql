CREATE TABLE USERS (
   user_id INT AUTO_INCREMENT PRIMARY KEY,
   username VARCHAR(50) NOT NULL unique,
   email VARCHAR(150) NOT NULL unique,
   balance DECIMAL(15,2),
   date_creation DATETIME NOT NULL
);

CREATE TABLE COIN_WALLET(
   wallet_id INT AUTO_INCREMENT PRIMARY KEY,
   quantity_crypto DECIMAL(15,8),
   crypto_name VARCHAR(20) NOT NULL,
   user_id INT NOT NULL,
   FOREIGN KEY(user_id) REFERENCES USERS(user_id)
);

CREATE TABLE TRANSACTIONS(
   transaction_id INT AUTO_INCREMENT PRIMARY KEY,
   type VARCHAR(4) NOT NULL,
   crypto_name VARCHAR(20) NOT NULL,
   quantity DECIMAL(15,8),
   unit_price DECIMAL(15,8) NOT NULL,
   total_amount DECIMAL(15,8) NOT NULL,
   date_transaction DATETIME,
   user_id INT NOT NULL,
   FOREIGN KEY(user_id) REFERENCES USERS(user_id)
);

CREATE TABLE TOKEN(
   id INT AUTO_INCREMENT PRIMARY KEY,
   token VARCHAR(200) UNIQUE,
   revoked BOOLEAN,
   expired BOOLEAN,
   user_id INT NOT NULL,
   FOREIGN KEY(user_id) REFERENCES USERS(user_id)
);
