CREATE DATABASE IF NOT EXISTS shop;
USE shop;

-- 1. آدرس‌ها
CREATE TABLE Addresses (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  country VARCHAR(50) NOT NULL,
  city VARCHAR(50) NOT NULL
);

-- 2. کاربران


-- 3. کیف پول
CREATE TABLE Wallet (
  user_id BIGINT PRIMARY KEY,
  balance DECIMAL(10,2) NOT NULL DEFAULT 0,
  FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- 4. نویسنده‌ها
CREATE TABLE Authors (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  name VARCHAR(100) NOT NULL,
  pen_name VARCHAR(100),
  born DATE NOT NULL,
  died DATE,
  nationality VARCHAR(50) NOT NULL,
  image_link TEXT NULL,
  FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- 5. کتاب‌ها
CREATE TABLE Books (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  admin_id BIGINT NOT NULL,
  author_id BIGINT NOT NULL,
  title VARCHAR(200) NOT NULL,
  publisher VARCHAR(100),
  year YEAR,
  price DECIMAL(10,2),
  synopsis TEXT,
  content TEXT,
  image_link TEXT NULL,
  FOREIGN KEY (admin_id) REFERENCES Users(id),
  FOREIGN KEY (author_id) REFERENCES Authors(id)
);

-- 6. ژانرها
CREATE TABLE Genres (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
);

-- 7. ارتباط کتاب‌ها و ژانرها (Many-to-Many)
CREATE TABLE Book_Genres (
  book_id BIGINT NOT NULL,
  genre_id BIGINT NOT NULL,
  PRIMARY KEY (book_id, genre_id),
  FOREIGN KEY (book_id) REFERENCES Books(id),
  FOREIGN KEY (genre_id) REFERENCES Genres(id)
);

-- 8. نظرات (Review)
CREATE TABLE Review (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  book_id BIGINT NOT NULL,
  rate INT NOT NULL CHECK (rate BETWEEN 1 AND 5),
  comment TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES Users(id),
  FOREIGN KEY (book_id) REFERENCES Books(id)
);

-- 9. سفارش‌ها
CREATE TABLE Orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- 10. اقلام هر سفارش
CREATE TABLE Order_Items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  book_id BIGINT NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  price DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (order_id) REFERENCES Orders(id),
  FOREIGN KEY (book_id) REFERENCES Books(id)
);
