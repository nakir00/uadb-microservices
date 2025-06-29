CREATE TABLE `maisons` (
  `id` integer PRIMARY KEY,
  `proprietaire_id` integer NOT NULL,
  `adresse` varchar(255),
  `latitude` decimal,
  `longitude` decimal,
  `description` text,
  `cree_le` timestamp
);

CREATE TABLE `chambres` (
  `id` integer PRIMARY KEY,
  `maison_id` integer NOT NULL,
  `titre` varchar(255),
  `description` text,
  `taille` varchar(255) COMMENT 'ex: 12m²',
  `type` varchar(255) COMMENT 'simple | appartement | maison',
  `meublee` boolean,
  `salle_de_bain` boolean,
  `prix` decimal,
  `disponible` boolean,
  `cree_le` timestamp
);