CREATE TABLE tunnel (
  id BIGINT AUTO_INCREMENT NOT NULL,
   db_schema VARCHAR(255) NULL,
   CONSTRAINT pk_tunnel PRIMARY KEY (id)
);

CREATE TABLE user (
  id BIGINT AUTO_INCREMENT NOT NULL,
   username VARCHAR(255) NULL,
   password VARCHAR(255) NULL,
   tunnel_id BIGINT NULL,
   CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user ADD CONSTRAINT uc_user_username UNIQUE (username);

ALTER TABLE user ADD CONSTRAINT FK_USER_ON_TUNNEL FOREIGN KEY (tunnel_id) REFERENCES tunnel (id);


INSERT INTO tunnel(id, db_schema) VALUES(1,'spring_a');
INSERT INTO tunnel(id, db_schema) VALUES(2,'spring_b');
INSERT INTO tunnel(id, db_schema) VALUES(3,'spring_c');
INSERT INTO tunnel(id, db_schema) VALUES(4,'spring_d');

#password is always admin
INSERT INTO user (id,username,password,tunnel_id) VALUES (1,'admin','1000:e4dfa15e9d5f5e4d424cfd7c56b40067150896cd8b54a364:884705dde4d421d8ed2c5b763f0f5c2083a48176d85a10eb',1);

INSERT INTO user (id,username,password,tunnel_id) VALUES (2,'admin_b','1000:e4dfa15e9d5f5e4d424cfd7c56b40067150896cd8b54a364:884705dde4d421d8ed2c5b763f0f5c2083a48176d85a10eb',2);

