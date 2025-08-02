CREATE TABLE `maisons` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `proprietaire_id` integer NOT NULL,
  `adresse` varchar(255),
  `latitude` decimal(10,8),
  `longitude` decimal(11,8),
  `description` text,
  `cree_le` timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `chambres` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `maison_id` integer NOT NULL,
  `titre` varchar(255),
  `description` text,
  `taille` varchar(255) COMMENT 'ex: 12m²',
  `type` varchar(255) COMMENT 'simple | appartement | maison',
  `meublee` boolean,
  `salle_de_bain` boolean,
  `prix` decimal(10,2),
  `disponible` boolean,
  `cree_le` timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `rendez_vous` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `locataire_id` integer NOT NULL,
  `chambre_id` integer NOT NULL,
  `date_heure` timestamp,
  `statut` varchar(255) COMMENT 'en_attente | confirmé | annulé',
  `cree_le` timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `medias` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `chambre_id` integer NOT NULL,
  `url` varchar(255),
  `type` varchar(255) COMMENT 'photo | video',
  `description` text,
  `cree_le` timestamp DEFAULT CURRENT_TIMESTAMP
);

-- Ajout des clés étrangères
ALTER TABLE `chambres` ADD FOREIGN KEY (`maison_id`) REFERENCES `maisons` (`id`);
ALTER TABLE `rendez_vous` ADD FOREIGN KEY (`chambre_id`) REFERENCES `chambres` (`id`);
ALTER TABLE `medias` ADD FOREIGN KEY (`chambre_id`) REFERENCES `chambres` (`id`);

-- Si vous avez des tables pour les propriétaires et locataires, ajoutez ces contraintes :
-- ALTER TABLE `maisons` ADD FOREIGN KEY (`proprietaire_id`) REFERENCES `proprietaires` (`id`);
-- ALTER TABLE `rendez_vous` ADD FOREIGN KEY (`locataire_id`) REFERENCES `locataires` (`id`);