INSERT INTO lang (langId, code, description)
VALUES (1, 'de', 'Deutsch');
INSERT INTO lang (langId, code, description)
VALUES (2, 'en', 'English');
INSERT INTO lang (langId, code, description)
VALUES (3, 'fr', 'FranÃ§ais');
INSERT INTO lang (langId, code, description)
VALUES (4, 'pt', 'PortuguÃªs');

SELECT @product1_product_id := nextval(product_seq);
SELECT @product2_product_id := nextval(product_seq);

INSERT INTO product (id, clientId, price)
VALUES (@product1_product_id, 1, 10.20);
INSERT INTO product (id, clientId, price)
VALUES (@product2_product_id, 1, 99.99);

INSERT INTO product_lang (id, product_id, langId, name, description)
VALUES (nextval(product_lang_seq), @product1_product_id, 1, 'Isotherm-Tasche fÃ¼r Lebensmittel',
        'Halten Sie Ihr Picknick schÃ¶n kÃ¼hl oder warm! Schicke, isolierte Tasche fÃ¼r den BÃ¼ro-Lunch oder AusflÃ¼ge. Innen mit Aluminium-Folie. Oberes Abteil 25 x 16 x H 15 cm. Unteres Abteil 25 x 16 x H 7 cm. H total 24 cm. OberflÃ¤che wasserabperlend. Leicht glÃ¤nzendes Perl ...');
INSERT INTO product_lang (id, product_id, langId, name, description)
VALUES (nextval(product_lang_seq), @product1_product_id, 3, 'Sac Ã  repas isotherme',
        'Gardez votre lunch bien chaud ou bien frais avec un sac isotherme chic pour le bureau ou les excursions ! DoublÃ© avec de la feuille d''aluminium. Compartiment du dessus 25 x 16 x H 15 cm. Compartiment du dessous 25 x 16 x H 7 cm. H totale 24 cm. RevÃªtement dÃ©perlant. Gris perle, lÃ©gÃ¨rement brillant.');
INSERT INTO product_lang (id, product_id, langId, name, description)
VALUES (nextval(product_lang_seq), @product2_product_id, 1, 'Mira Eck Glas USB A',
        'Eck-Steckdosenelement, 2-fach und Doppel USB Charger (Ladestation). Frontseite in bedrucktem und kratzfestem Glas, fÃ¼r 90Â° Ecke.');
INSERT INTO product_lang (id, product_id, langId, name, description)
VALUES (nextval(product_lang_seq), @product2_product_id, 2, 'Mira corner111 glass USB A',
        'Corner socket element, 2-fold and double USB charger (charging station). Front printed and scratch-resistant glass. for 90Â° corner.');