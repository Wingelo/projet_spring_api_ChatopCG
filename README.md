# Pour installer le projet : 

## 1 - Installation de MySQL

### 1.1 Télécharger MySQL
- Rendez-vous sur [MySQL Community Server](https://dev.mysql.com/downloads/mysql/).
- Téléchargez la version compatible avec votre système d'exploitation.

### 1.2 Installer MySQL
- Lancez l'installateur et suivez les instructions.
- Pendant l’installation :
  - Configurez le mot de passe pour l’utilisateur `root`.
  - Conservez le port par défaut (`3306`).

### 1.3 Vérifier l'installation
- Assurez-vous que MySQL est démarré :
  - Via le service `mysql`.
  - Ou avec MySQL Workbench (interface graphique).
- Testez la connexion avec la commande suivante :
    ```bash
  mysql -u root -p
    ```
### 1.4 Créer votre base avec cette ligne de commande
```bash
CREATE DATABASE nom_de_la_base;
```
## 2 Configuration du Projet Java avec Maven et MySQL
Ce guide explique comment configurer et exécuter le projet Java.
### 2.1 Commandes Maven pour exécuter le projet

1. **Compiler et construire le projet :**
```bash
   mvn clean install
```
### 2.2 Modifier le fichier application.properties en conséquence pour que l'application démarre (Vous trouverez des numéros de 1 à 2 que vous devez modifier)
- 1# Configuration de la base de données
- 2# Configuration de l'application
    -  Pour la clè secret vous devez lancer la commande dans bash(par exemple : Le GIT BASH)
        ```bash
        openssl rand -base64 32
        ```
        
L'application a bien été installé ! :) 
