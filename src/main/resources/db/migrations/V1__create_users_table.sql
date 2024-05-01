CREATE TABLE user (
  id INT NOT NULL,
   discord_id BIGINT NOT NULL,
   guild_name VARCHAR(255) NULL,
   global_name VARCHAR(255) NULL,
   CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user ADD CONSTRAINT uc_user_discordid UNIQUE (discord_id);