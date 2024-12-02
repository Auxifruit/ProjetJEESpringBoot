# Projet de Gestion de Scolarité - **CyEase**

![alt_text](https://github.com/Auxifruit/ProjetGestionJEE/blob/main/logo.png)

## Table des matières
1. [DESCRIPTION](#description)
2. [FONCTIONNALITÉS](#fonctionnalités)
3. [TECHNOLOGIES](#technologies)
4. [UTILISATION](#utilisation)
5. [CONTRIBUTEURS](#contributeurs)

## DESCRIPTION
Ce projet a été réalisé du 25/10/24 au 02/12/24 dans le cadre du module **JEE** enseigné par Monsieur HADDACHE. Il s'agit d'une **application web** de gestion de scolarité, permettant aux étudiants, enseignants et administrateurs de gérer les informations académiques de manière efficace. L'objectif principal de ce projet est de fournir un outil complet pour la gestion des cours, des inscriptions, des notes des étudiants, et la génération de rapports académiques.

Le projet a été développé avec **Spring Boot** pour faciliter le développement, la gestion des dépendances et la mise en production rapide. Grâce à Spring Boot, l'application bénéficie d'une structure plus moderne et flexible.

### Fonctionnalités principales :
Le projet comprend les fonctionnalités suivantes :

- Gestion des utilisateurs : administrateurs, enseignants, étudiants.
- Ajout, modification et suppression des notes des étudiants.
- Génération de rapports détaillés en PDF sur les notes des étudiants.
- Envoi de notifications par email lors de la mise à jour des notes, la modification de l'emploi du temps, validation de l'inscription, etc.

## FONCTIONNALITÉS

1. **Gestion des utilisateurs**  
   _Administrateurs :_ Ajout, suppression et gestion des utilisateurs, gestion des classes.  
   _Enseignants :_ Saisie et modification des notes des étudiants. Générer des rapports PDF détaillant les notes des étudiants.  
   _Étudiants :_ Consultation de leurs notes et génération des rapports PDF par email.  

2. **Gestion des notes**  
   Permet aux enseignants d'ajouter, modifier ou supprimer les notes des étudiants.  
   Les notes sont associées à des matières et des cours, avec des coefficients pour chaque note.  

3. **Génération de rapports PDF**  
   Les administrateurs, enseignants et étudiants peuvent générer un rapport détaillé des notes des étudiants, incluant la moyenne par matière et la moyenne générale.  
   Le rapport est généré en format PDF et peut être téléchargé.  

4. **Notifications par email**  
   Lorsqu'une note est ajoutée ou modifiée, lorsqu'une séance est modifiée, ou lors d'une inscription en attente de validation, un email de bienvenue est envoyé lorsque l'inscription est validée par un administrateur. Les notifications sont envoyées automatiquement à l'étudiant concerné. Cette fonctionnalité utilise l'API Gmail pour l'envoi des emails en utilisant OAuth2.

## TECHNOLOGIES

Le projet utilise plusieurs technologies pour assurer son bon fonctionnement :

- **Java 17** : Le langage de programmation principal utilisé pour le développement de l'application.
- **Spring Boot** : Framework principal utilisé pour le développement de l'application web, facilitant la gestion des dépendances, la configuration et le déploiement.
- **Hibernate** : Framework ORM utilisé pour gérer l'accès à la base de données et la persistance des données.
- **MySQL** : Système de gestion de base de données relationnelle utilisé pour stocker les informations (utilisateurs, notes, matières, etc.).
- **iText PDF** : Bibliothèque utilisée pour générer des rapports PDF détaillant les performances académiques des étudiants.
- **API Gmail (OAuth2)** : Utilisé pour l'envoi de notifications par email, en utilisant un processus d'authentification sécurisé via OAuth2.

## UTILISATION
Authentification des administrateurs  
L'authentification des utilisateurs se fait via OAuth2 avec Google. Les administrateurs doivent se connecter à l'application en utilisant leur compte Google, ce qui permet de gérer de manière sécurisée l'envoi d'emails et l'accès aux fonctionnalités.

Gestion des utilisateurs  
Administrateurs : Les administrateurs peuvent ajouter ou supprimer des utilisateurs et attribuer des rôles (étudiant, enseignant).
Enseignants : Les enseignants peuvent ajouter ou modifier les notes des étudiants, consulter leurs performances et générer des rapports PDF.
Étudiants : Les étudiants peuvent consulter leurs notes et recevoir des notifications par email concernant les mises à jour de leurs notes, les modifications des cours.

Gestion des notes  
Les enseignants peuvent saisir, modifier et supprimer les notes des étudiants pour chaque cours. Les notes sont associées à des matières et des coefficients, permettant de calculer les moyennes pondérées.

Notifications par email    
Lorsqu'une note est ajoutée ou modifiée, lorsqu'une séance est modifiée ou ajoutée, pour confirmer les inscriptions et les affectations à une classe : une notification par email est envoyée automatiquement à l'étudiant concerné. Cela permet à l'étudiant d'être informé en temps réel de toute mise à jour concernant ses informations académiques.

## CONTRIBUTEURS
Ce projet a été réalisé par :

- **Amine AIT MOUSSA** (4M1NESan)
- **Guillaume BARRÉ** (Auxifruit)
- **Siham DAANOUNI** (SihamDaanouni)
- **Clément DURÉCU** (shxclem)
- **Amaury PROVENT** (Amaury75)
- **Hicham BETTAHAR** (Poupouski)

Étudiants en **ING2 GSI**.
