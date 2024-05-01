CREATE TABLE command_history (
  id INT NOT NULL,
   user_id INT NULL,
   user_name VARCHAR(255) NULL,
   guild_id BIGINT NULL,
   guild_name VARCHAR(255) NULL,
   command_name VARCHAR(255) NULL,
   command_option VARCHAR(255) NULL,
   date_added datetime NULL,
   CONSTRAINT pk_commandhistory PRIMARY KEY (id)
);

ALTER TABLE command_history ADD CONSTRAINT FK_COMMANDHISTORY_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);