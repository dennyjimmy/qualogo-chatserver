CREATE DATABASE chatdb;

USE chatdb;

-- Create table for chat messages
CREATE TABLE chat_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    timestamp BIGINT NOT NULL
);

-- Insert chat message
INSERT INTO chat_message (username, message, timestamp) VALUES ('john_doe', 'Hello, everyone!', 1622548800000);
INSERT INTO chat_message (username, message, timestamp) VALUES ('jane_doe', 'Hi, John!', 1622548801000);

-- Create table for user
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `password_hash` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;

-- Insert users
INSERT INTO `user` (username, password_hash) VALUES ('user1', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `user` (username, password_hash) VALUES ('user2', 'e10adc3949ba59abbe56e057f20f883e');