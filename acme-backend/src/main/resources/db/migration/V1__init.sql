CREATE TABLE lang (
  langId          INT           NOT NULL AUTO_INCREMENT,
  code            CHAR(2)       NOT NULL,
  description     VARCHAR(50),
  PRIMARY KEY (langId)
);

CREATE TABLE client (
  clientId        INT           NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (clientId)
);

CREATE TABLE product (
  productId     BIGINT(20)    NOT NULL AUTO_INCREMENT,
  clientId      INT           NOT NULL,
  price         DECIMAL(10,2) NOT NULL,
  createdAt     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updatedAt     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted       tinyint(1)    NOT NULL DEFAULT 0,
  PRIMARY KEY (productId),
  CONSTRAINT fk_product_clientId FOREIGN KEY (clientId) REFERENCES client (clientId) ON DELETE CASCADE
);

CREATE TABLE product_lang (
  productId BIGINT(20) NOT NULL,
  langId INT NOT NULL,
  name VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  PRIMARY KEY (productId, langId),
  CONSTRAINT fk_product_lang_productId FOREIGN KEY (productId) REFERENCES product (productId) ON DELETE CASCADE,
  CONSTRAINT fk_product_lang_langId    FOREIGN KEY (langId)    REFERENCES lang (langId)       ON DELETE CASCADE
);

INSERT INTO lang (langId, code, description) VALUES (1, 'de', 'Deutsch');
INSERT INTO lang (langId, code, description) VALUES (2, 'en', 'English');
INSERT INTO lang (langId, code, description) VALUES (3, 'fr', 'Français');
INSERT INTO lang (langId, code, description) VALUES (4, 'pt', 'Português');

INSERT INTO client (clientId) VALUES (1);
INSERT INTO client (clientId) VALUES (2);
INSERT INTO client (clientId) VALUES (3);

INSERT INTO product (productId, clientId, price) VALUES (1, 1, 10.20);
INSERT INTO product (productId, clientId, price) VALUES (2, 1, 99.99);

INSERT INTO product_lang (productId, langId, name, description) VALUES (1, 1, 'Isotherm-Tasche für Lebensmittel', 'Halten Sie Ihr Picknick schön kühl oder warm! Schicke, isolierte Tasche für den Büro-Lunch oder Ausflüge. Innen mit Aluminium-Folie. Oberes Abteil 25 x 16 x H 15 cm. Unteres Abteil 25 x 16 x H 7 cm. H total 24 cm. Oberfläche wasserabperlend. Leicht glänzendes Perl ...');
INSERT INTO product_lang (productId, langId, name, description) VALUES (1, 3, 'Sac à repas isotherme', 'Gardez votre lunch bien chaud ou bien frais avec un sac isotherme chic pour le bureau ou les excursions ! Doublé avec de la feuille d''aluminium. Compartiment du dessus 25 x 16 x H 15 cm. Compartiment du dessous 25 x 16 x H 7 cm. H totale 24 cm. Revêtement déperlant. Gris perle, légèrement brillant.');
INSERT INTO product_lang (productId, langId, name, description) VALUES (2, 1, 'Mira Eck Glas USB A', 'Eck-Steckdosenelement, 2-fach und Doppel USB Charger (Ladestation). Frontseite in bedrucktem und kratzfestem Glas, für 90° Ecke.');
INSERT INTO product_lang (productId, langId, name, description) VALUES (2, 2, 'Mira corner glass USB A', 'Corner socket element, 2-fold and double USB charger (charging station). Front printed and scratch-resistant glass. for 90° corner.');