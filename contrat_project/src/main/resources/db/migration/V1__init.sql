CREATE TABLE `contrats` (
  `id` integer  PRIMARY KEY AUTO_INCREMENT,
  `locataire_id` integer NOT NULL,
  `chambre_id` integer NOT NULL,
  `date_debut` date,
  `date_fin` date,
  `montant_caution` decimal,
  `mois_caution` integer COMMENT '<= 3',
  `description` text,
  `mode_paiement` varchar(255) COMMENT 'virement | cash | mobile money',
  `periodicite` varchar(255) COMMENT 'journalier | hebdomadaire | mensuel',
  `statut` varchar(255) COMMENT 'actif | resilié',
  `cree_le` timestamp
);

CREATE TABLE `paiements` (
  `id` integer  PRIMARY KEY AUTO_INCREMENT,
  `contrat_id` integer NOT NULL,
  `montant` decimal,
  `statut` varchar(255) COMMENT 'payé | impayé',
  `date_echeance` date,
  `date_paiement` timestamp,
  `cree_le` timestamp
);


CREATE TABLE `problemes` (
  `id` integer  PRIMARY KEY AUTO_INCREMENT,
  `contrat_id` integer NOT NULL,
  `signale_par` integer COMMENT 'utilisateur_id',
  `description` text,
  `type` varchar(255) COMMENT 'plomberie | electricite | autre',
  `responsable` varchar(255) COMMENT 'locataire | proprietaire',
  `resolu` boolean,
  `cree_le` timestamp
);




ALTER TABLE `paiements` ADD FOREIGN KEY (`contrat_id`) REFERENCES `contrats` (`id`);




ALTER TABLE `problemes` ADD FOREIGN KEY (`contrat_id`) REFERENCES `contrats` (`id`);

