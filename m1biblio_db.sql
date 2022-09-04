-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : Dim 04 sep. 2022 à 11:11
-- Version du serveur :  5.7.31
-- Version de PHP : 7.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `m1biblio_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `lecteur`
--

DROP TABLE IF EXISTS `lecteur`;
CREATE TABLE IF NOT EXISTS `lecteur` (
  `numlecteur` varchar(25) NOT NULL,
  `nomlecteur` varchar(100) NOT NULL,
  PRIMARY KEY (`numlecteur`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `lecteur`
--

INSERT INTO `lecteur` (`numlecteur`, `nomlecteur`) VALUES
('1', 'Rabe'),
('6', 'Bary'),
('8', 'Toky'),
('12', 'Aintso');

-- --------------------------------------------------------

--
-- Structure de la table `livre`
--

DROP TABLE IF EXISTS `livre`;
CREATE TABLE IF NOT EXISTS `livre` (
  `numlivre` varchar(25) NOT NULL,
  `designlivre` varchar(50) NOT NULL,
  `autlivre` varchar(30) NOT NULL,
  `dateeditlivre` varchar(25) NOT NULL,
  `dispolivre` varchar(5) NOT NULL,
  PRIMARY KEY (`numlivre`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `livre`
--

INSERT INTO `livre` (`numlivre`, `designlivre`, `autlivre`, `dateeditlivre`, `dispolivre`) VALUES
('L1', 'Physique', 'Matthieu Debron', '10/10/85', 'Non'),
('L2', 'Maths', 'Marc Lechar', '10/11/75', 'Non'),
('L5', 'SVT', 'Mac Lebron', '05/07/92', 'Non');

-- --------------------------------------------------------

--
-- Structure de la table `pret`
--

DROP TABLE IF EXISTS `pret`;
CREATE TABLE IF NOT EXISTS `pret` (
  `numlecteur` varchar(25) NOT NULL,
  `numlivre` varchar(25) NOT NULL,
  `datepret` varchar(25) NOT NULL,
  PRIMARY KEY (`numlecteur`,`numlivre`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `pret`
--

INSERT INTO `pret` (`numlecteur`, `numlivre`, `datepret`) VALUES
('1', 'L2', '20/05/20'),
('2', 'L2', '18/05/22'),
('2', 'L5', '07/08/20'),
('6', 'L1', '12/10/20'),
('8', 'L5', '24/10/19');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
