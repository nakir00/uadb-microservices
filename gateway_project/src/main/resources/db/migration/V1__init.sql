CREATE TABLE `utilisateurs` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `nom_utilisateur` varchar(255),
  `email` varchar(255),
  `password` varchar(255),
  `telephone` varchar(255),
  `cni` varchar(255),
  `role` varchar(255) COMMENT 'proprietaire | locataire',
  `cree_le` timestamp
);