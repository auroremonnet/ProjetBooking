-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Ven 25 Avril 2025 à 19:44
-- Version du serveur :  5.6.17
-- Version de PHP :  5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `booking_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `administrateur`
--

CREATE TABLE IF NOT EXISTS `administrateur` (
  `idAdministrateur` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `motDePasse` varchar(255) NOT NULL,
  `photo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idAdministrateur`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Contenu de la table `administrateur`
--

INSERT INTO `administrateur` (`idAdministrateur`, `nom`, `prenom`, `email`, `motDePasse`, `photo`) VALUES
(1, 'monnet', 'aurore', 'AURORE', 'monnet', 'AURORE.jpg'),
(2, 'nussbaumer', 'hippolyte', 'HIPPO', 'nussbaumer', 'HIPPO.jpg'),
(4, 'lucas', 'basile', 'BASILE', 'lucas', 'BASILE.jpg'),
(5, 'gonzalez', 'pablo', 'PABLO', 'gonzalez', 'BOSS.jpg');

-- --------------------------------------------------------

--
-- Structure de la table `avis`
--

CREATE TABLE IF NOT EXISTS `avis` (
  `idAvis` int(11) NOT NULL AUTO_INCREMENT,
  `idClient` int(11) NOT NULL,
  `idHebergement` int(11) NOT NULL,
  `note` int(11) NOT NULL,
  `commentaire` text,
  `dateAvis` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idAvis`),
  KEY `idClient` (`idClient`),
  KEY `idHebergement` (`idHebergement`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Contenu de la table `avis`
--

INSERT INTO `avis` (`idAvis`, `idClient`, `idHebergement`, `note`, `commentaire`, `dateAvis`) VALUES
(1, 1, 1, 5, 'Excellent séjour, services parfaits.', '2025-04-15 13:50:30');

-- --------------------------------------------------------

--
-- Structure de la table `categoriehebergement`
--

CREATE TABLE IF NOT EXISTS `categoriehebergement` (
  `idCategorie` int(11) NOT NULL AUTO_INCREMENT,
  `nomCategorie` varchar(100) NOT NULL,
  PRIMARY KEY (`idCategorie`),
  UNIQUE KEY `nomCategorie` (`nomCategorie`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=6 ;

--
-- Contenu de la table `categoriehebergement`
--

INSERT INTO `categoriehebergement` (`idCategorie`, `nomCategorie`) VALUES
(4, 'Appartement'),
(2, 'Chalet'),
(1, 'Hôtel'),
(5, 'Maison'),
(3, 'Villa');

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE IF NOT EXISTS `client` (
  `idClient` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `motDePasse` varchar(255) NOT NULL,
  `typeClient` enum('nouveau','ancien') NOT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`idClient`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

--
-- Contenu de la table `client`
--

INSERT INTO `client` (`idClient`, `nom`, `prenom`, `email`, `motDePasse`, `typeClient`, `adresse`, `telephone`) VALUES
(1, 'Dupont', 'Jean', 'jean', 'jean', 'ancien', '1 Rue de Paris, 75000 Paris', '0123456789'),
(3, 'monnet', 'Aurpre', 'cacacom', 'petitebite', 'ancien', '210 rue de vaugigi', '0624060306'),
(4, 'Martin', 'Alice', 'alice.martin@example.com', 'mdpAlice1', 'nouveau', '12 rue des Fleurs, 75001 Paris', '0600000001'),
(6, 'Dubois', 'Marie', 'marie.dubois@example.com', 'mdpMarie3', 'nouveau', '56 boulevard Saint-Germain, 75005 Paris', '0600000003'),
(7, 'Petit', 'Thomas', 'thomas.petit@example.com', 'mdpThomas4', 'ancien', '78 place de la Concorde, 75008 Paris', '0600000004'),
(8, 'Moreau', 'Julien', 'julien.moreau@example.com', 'mdpJulien5', 'nouveau', '90 rue de Rivoli, 75004 Paris', '0600000005'),
(9, 'Laurent', 'Sophie', 'sophie.laurent@example.com', 'mdpSophie6', 'ancien', '102 avenue des Champs-Élysées, 75008 Paris', '0600000006');

-- --------------------------------------------------------

--
-- Structure de la table `hebergement`
--

CREATE TABLE IF NOT EXISTS `hebergement` (
  `idHebergement` int(11) NOT NULL AUTO_INCREMENT,
  `idCategorie` int(11) DEFAULT NULL,
  `nom` varchar(100) NOT NULL,
  `adresse` varchar(255) NOT NULL,
  `localisation` varchar(100) DEFAULT NULL,
  `description` text,
  `prix` decimal(10,2) NOT NULL,
  `categorie` varchar(50) NOT NULL,
  `photos` text,
  `options` text,
  `capacite_max` int(11) DEFAULT '2',
  `nombre_lits` int(11) DEFAULT '1',
  `complementDescription` text COMMENT 'Description détaillée ou complémentaire du bien (3-4 lignes)',
  PRIMARY KEY (`idHebergement`),
  KEY `fk_hebergement_categorie` (`idCategorie`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=14 ;

--
-- Contenu de la table `hebergement`
--

INSERT INTO `hebergement` (`idHebergement`, `idCategorie`, `nom`, `adresse`, `localisation`, `description`, `prix`, `categorie`, `photos`, `options`, `capacite_max`, `nombre_lits`, `complementDescription`) VALUES
(1, 1, 'Hôtel Parisien', '1 Avenue de la République, Paris', 'Paris', 'Hôtel de luxe au cœur de Paris', '150.00', 'hôtel', 'HOTEL10.jpg', '{"wifi": true, "climatisation": true}', 4, 2, 'Ce luxueux hôtel est situé au cœur vibrant de Paris. Idéal pour les séjours touristiques ou professionnels. Prestations haut de gamme et services personnalisés.'),
(3, 1, 'Hôtel Parisien', '1 Avenue de la République, Paris', 'Paris', 'Hôtel de luxe au cœur de Paris', '150.00', 'hôtel', 'HOTEL11.jpg', '{"wifi": true, "climatisation": true}', 2, 1, 'Petit hôtel élégant dans la capitale, parfait pour une escapade romantique ou un voyage d’affaires. Confort moderne et excellente localisation.'),
(4, 3, 'Villa Provence', '123 Chemin de Lavande, Provence', 'Provence', 'Villa de charme en Provence', '200.00', 'villa', 'villa1.jpg', '{"piscine": true, "jardin": true}', 10, 6, 'Villa de charme nichée au milieu des lavandes. Offrant tranquillité et espace, elle est idéale pour les grandes familles ou les groupes.'),
(5, 4, 'Appartement Marseille', '45 Rue Saint-Ferréol, Marseille', 'Marseille', 'Bel appartement en centre-ville', '100.00', 'appartement', 'APPART10.jpg', '{"wifi": true, "fumeur": false}', 3, 12, 'Appartement lumineux en plein centre de Marseille. Proche des commerces et des transports, parfait pour un séjour urbain.'),
(6, 2, 'Chalet Montagne Lyon', 'Le Mont, Lyon', 'Lyon', 'Chalet cosy avec vue montagne', '180.00', 'chalet', 'CHALET10.jpg', '{"cheminée": true, "animaux": true}', 8, 4, 'Chalet chaleureux en montagne avec vue panoramique. Ambiance cocooning, idéal pour les séjours nature en famille.'),
(7, 5, 'Maison Azur', '72 Promenade des Anglais, Nice', 'Nice', 'Maison lumineuse avec vue mer', '220.00', 'maison', 'MAISON10.jpg', '{"vue": "mer", "climatisation": true}', 7, 4, 'Grande maison en bord de mer à Nice. Terrasse ensoleillée, vue imprenable et accès rapide à la plage.'),
(8, 4, 'Loft Bordeaux', '8 Rue Sainte-Catherine, Bordeaux', 'Bordeaux', 'Loft moderne en plein centre', '130.00', 'appartement', 'APPART11.jpg', '{"wifi": true, "cuisine": true}', 4, 2, 'Loft moderne situé dans la plus grande rue commerçante de Bordeaux. Parfait pour découvrir la ville à pied.'),
(9, 2, 'Chalet Lac d''Annecy', 'Route du lac, Annecy', 'Annecy', 'Chalet proche du lac et de la montagne', '190.00', 'chalet', 'CHALET11.jpg', '{"nature": true, "parking": true}', 2, 1, 'Ce chalet en bois vous offre une expérience authentique à deux pas du lac. Un havre de paix pour amoureux de la nature.'),
(10, 3, 'Villa Corse Sud', 'Plage de Palombaggia, Porto-Vecchio', 'Corse', 'Villa pieds dans l’eau', '300.00', 'villa', 'VILLA11.jpg', '{"plage": true, "vue": "mer"}', 16, 10, 'Villa de luxe en bord de mer à Porto-Vecchio. Accès direct à la plage, prestations haut de gamme et intimité assurée.'),
(11, 1, 'Hôtel Alsacien', '10 Place Kléber, Strasbourg', 'Strasbourg', 'Hôtel traditionnel alsacien', '110.00', 'hôtel', 'HOTEL12.jpg', '{"petit-déjeuner": true, "wifi": true}', 6, 4, 'Hôtel typique alsacien au centre de Strasbourg. Charme local, proximité de la cathédrale et accueil chaleureux.'),
(12, 4, 'Appartement Lille Flandres', '5 Rue de Paris, Lille', 'Lille', 'Studio proche des gares', '90.00', 'appartement', 'APPART12.jpg', '{"wifi": true, "fumeur": false}', 2, 1, 'Studio bien équipé à Lille, à deux pas des gares. Idéal pour les voyageurs en transit ou les séjours de courte durée.'),
(13, 5, 'Maison des Mont D''or', '1 chemin de vide pot', 'Lyon', 'Description automatique', '450.00', 'Villa', 'VILLA12.jpg', 'wifi, clim, piscine, jardin, fumeur', 18, 8, 'Grande maison moderne nichée dans les monts du Lyonnais. Piscine, jardin et équipements complets pour familles nombreuses.');

-- --------------------------------------------------------

--
-- Structure de la table `mail`
--

CREATE TABLE IF NOT EXISTS `mail` (
  `idMail` int(11) NOT NULL AUTO_INCREMENT,
  `idClient` int(11) NOT NULL,
  `idAdministrateur` int(11) NOT NULL DEFAULT '0',
  `objet` varchar(255) DEFAULT NULL,
  `contenu` text,
  `dateEnvoi` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idMail`),
  KEY `idClient` (`idClient`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=19 ;

--
-- Contenu de la table `mail`
--

INSERT INTO `mail` (`idMail`, `idClient`, `idAdministrateur`, `objet`, `contenu`, `dateEnvoi`) VALUES
(18, 1, 1, 'Message Admin', 'va te faire enculllllllllllllll.ééééééééééééééééé', '2025-04-24 15:18:46'),
(17, 1, 1, 'Message Client', 'ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh', '2025-04-23 10:28:31'),
(16, 1, 1, 'Message Client', 'est ce que dans ce sens ça marche au moins', '2025-04-23 10:28:01'),
(15, 1, 1, 'Message Admin', 'va te faire enculé jean et tes copains', '2025-04-23 10:26:55'),
(14, 1, 1, 'Message Admin', 'va te faire enculé jean', '2025-04-23 10:24:54'),
(13, 1, 1, 'Message Admin', 'caca', '2025-04-23 10:24:34');

-- --------------------------------------------------------

--
-- Structure de la table `moyenpaiement`
--

CREATE TABLE IF NOT EXISTS `moyenpaiement` (
  `idMoyenPaiement` int(11) NOT NULL AUTO_INCREMENT,
  `idClient` int(11) NOT NULL,
  `typeCarte` varchar(20) NOT NULL,
  `numeroLast4` char(4) NOT NULL,
  `cvv` char(3) NOT NULL,
  `expMois` tinyint(4) NOT NULL,
  `expAnnee` smallint(6) NOT NULL,
  `solde` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`idMoyenPaiement`),
  KEY `fk_moyen_client` (`idClient`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=14 ;

--
-- Contenu de la table `moyenpaiement`
--

INSERT INTO `moyenpaiement` (`idMoyenPaiement`, `idClient`, `typeCarte`, `numeroLast4`, `cvv`, `expMois`, `expAnnee`, `solde`) VALUES
(1, 1, 'Carte bancaire', '1234', '123', 12, 2025, '350.00');

-- --------------------------------------------------------

--
-- Structure de la table `paiement`
--

CREATE TABLE IF NOT EXISTS `paiement` (
  `idPaiement` int(11) NOT NULL AUTO_INCREMENT,
  `idReservation` int(11) NOT NULL,
  `montant` decimal(10,2) NOT NULL,
  `modePaiement` varchar(30) NOT NULL,
  `datePaiement` datetime NOT NULL,
  `idMoyenPaiement` int(11) DEFAULT NULL,
  `cvv` char(3) NOT NULL,
  `taux_reduction` decimal(5,2) DEFAULT '0.00',
  PRIMARY KEY (`idPaiement`),
  KEY `fk_paiement_reservation` (`idReservation`),
  KEY `fk_paiement_moyen` (`idMoyenPaiement`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=7 ;

--
-- Contenu de la table `paiement`
--

INSERT INTO `paiement` (`idPaiement`, `idReservation`, `montant`, `modePaiement`, `datePaiement`, `idMoyenPaiement`, `cvv`, `taux_reduction`) VALUES
(5, 29, '3150.00', 'Carte bancaire', '2025-04-23 11:34:13', 1, '123', '0.00'),
(6, 31, '500.00', 'Carte bancaire', '2025-04-24 19:19:01', 1, '123', '50.00');

-- --------------------------------------------------------

--
-- Structure de la table `reduction_client`
--

CREATE TABLE IF NOT EXISTS `reduction_client` (
  `type_client` enum('nouveau','ancien') NOT NULL COMMENT 'Type de client',
  `taux_reduction` decimal(5,2) NOT NULL COMMENT 'Taux de réduction en %',
  PRIMARY KEY (`type_client`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Réduction uniforme selon le type de client';

--
-- Contenu de la table `reduction_client`
--

INSERT INTO `reduction_client` (`type_client`, `taux_reduction`) VALUES
('nouveau', '5.00'),
('ancien', '15.00');

-- --------------------------------------------------------

--
-- Structure de la table `reservation`
--

CREATE TABLE IF NOT EXISTS `reservation` (
  `idReservation` int(11) NOT NULL AUTO_INCREMENT,
  `dateArrivee` date NOT NULL,
  `dateDepart` date NOT NULL,
  `nombreAdultes` int(11) NOT NULL,
  `nombreEnfants` int(11) NOT NULL,
  `nombreChambres` int(11) NOT NULL,
  `idClient` int(11) NOT NULL,
  `idHebergement` int(11) NOT NULL,
  `statut` enum('En attente','Confirmée','Payée','Annulée') NOT NULL,
  `dateReservation` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idReservation`),
  KEY `idClient` (`idClient`),
  KEY `idHebergement` (`idHebergement`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=35 ;

--
-- Contenu de la table `reservation`
--

INSERT INTO `reservation` (`idReservation`, `dateArrivee`, `dateDepart`, `nombreAdultes`, `nombreEnfants`, `nombreChambres`, `idClient`, `idHebergement`, `statut`, `dateReservation`) VALUES
(3, '2025-04-22', '2025-04-29', 6, 2, 4, 1, 6, 'Confirmée', '2025-04-20 18:21:36'),
(5, '2025-04-22', '2025-04-29', 6, 0, 1, 1, 4, 'Confirmée', '2025-04-21 12:28:56'),
(6, '2025-04-22', '2025-04-28', 2, 0, 1, 1, 1, 'Confirmée', '2025-04-21 13:00:18'),
(21, '2025-04-23', '2025-04-29', 2, 0, 1, 1, 1, 'Confirmée', '2025-04-22 11:31:16'),
(22, '2025-04-23', '2025-04-28', 2, 0, 1, 1, 1, 'Confirmée', '2025-04-22 12:03:41'),
(23, '2025-04-30', '2025-05-06', 2, 0, 1, 1, 1, 'Confirmée', '2025-04-22 12:06:00'),
(24, '2025-04-30', '2025-05-06', 2, 0, 1, 1, 1, 'Confirmée', '2025-04-22 12:06:15'),
(25, '2025-04-23', '2025-04-25', 2, 0, 1, 1, 1, 'Confirmée', '2025-04-22 12:12:17'),
(26, '2025-04-23', '2025-04-24', 6, 0, 4, 1, 7, 'Confirmée', '2025-04-22 12:20:04'),
(27, '2025-04-23', '2025-04-24', 6, 0, 4, 1, 7, 'Confirmée', '2025-04-22 12:20:11'),
(29, '2025-08-13', '2025-08-20', 8, 4, 7, 1, 13, 'Confirmée', '2025-04-23 10:34:04'),
(30, '2025-04-24', '2025-04-25', 2, 0, 1, 1, 4, 'Confirmée', '2025-04-23 13:30:21'),
(31, '2025-04-25', '2025-04-30', 2, 0, 1, 1, 4, 'Confirmée', '2025-04-24 17:18:31'),
(32, '2025-04-25', '2025-04-30', 2, 0, 1, 1, 4, 'Confirmée', '2025-04-24 17:20:02'),
(33, '2025-04-25', '2025-04-30', 2, 0, 1, 1, 4, 'Confirmée', '2025-04-24 17:20:39'),
(34, '2025-04-28', '2025-04-30', 2, 0, 1, 1, 4, 'Confirmée', '2025-04-25 15:46:53');

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `hebergement`
--
ALTER TABLE `hebergement`
  ADD CONSTRAINT `fk_hebergement_categorie` FOREIGN KEY (`idCategorie`) REFERENCES `categoriehebergement` (`idCategorie`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Contraintes pour la table `moyenpaiement`
--
ALTER TABLE `moyenpaiement`
  ADD CONSTRAINT `fk_moyen_client` FOREIGN KEY (`idClient`) REFERENCES `client` (`idClient`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `paiement`
--
ALTER TABLE `paiement`
  ADD CONSTRAINT `fk_paiement_moyen` FOREIGN KEY (`idMoyenPaiement`) REFERENCES `moyenpaiement` (`idMoyenPaiement`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_paiement_reservation` FOREIGN KEY (`idReservation`) REFERENCES `reservation` (`idReservation`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`idClient`) REFERENCES `client` (`idClient`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `reservation_ibfk_2` FOREIGN KEY (`idHebergement`) REFERENCES `hebergement` (`idHebergement`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
