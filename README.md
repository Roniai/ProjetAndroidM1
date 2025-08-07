# Gestion bibliothécaire
## Prérequis :
- Télécharger et installer Android Studio
- Créer un émulateur (ou AVD) : [Créer et gérer des appareils virtuels](https://developer.android.com/studio/run/managing-avds?hl=fr)
- Activer le mode débogage de votre téléphone si vous ne voulez pas utiliser un émulateur : [Activer le mode débogage Android](https://developer.android.com/codelabs/basic-android-kotlin-training-run-on-mobile-device?hl=fr#0)

## Backend : Symfony / API Platform
### Configurer la base de données :
- Créer une base de données vide nommée : **m1biblio_db**
- Importer le fichier **m1biblio_db.sql** dans la base de données créée

### Configurer Symfony :
- Télécharger **symfony-cli** pour qu'on puisse utiliser la ligne de commande : [Install Symfony CLI](https://symfony.com/download)
- Ajouter **C:\symfony-cli** dans Path si vous avez choisi de télécharger le fichier compressé au lieu de l'installer via ligne de commande

### Lancer le serveur :
Pour lancer le serveur, utiliser la ligne de commande suivant :
```cmd
cd .\backend-biblio\
symfony server:start
```
Pour faire le test de l'endpoint, veuillez renseigner l'url dans votre navigateur : http://localhost:8000/api

## Frontend : Android (JAVA)
### Lancer l'application :
- Ouvrir le projet complet dans **Android Studio**
- Dans la section en bas **build**, vous devez voir un téléchargement de **gradle**. Attend que ça se termine pour la synchronisation.
- Cliquer sur le bouton **Run App** pour lancer l'application

## Créer une configuration de lancement (Run Configuration)
En haut à droite d'Android Studio, clique sur la petite flèche à côté du bouton **"Run ▶️"**.
- Clique sur **"Edit Configurations…"**
- Clique sur le bouton + en haut à gauche
- Choisis **"Android App"**.
- Dans le champ Name, mets par exemple App.
- Dans Module, sélectionne app (ou le nom de ton module principal).
- Dans la section Launch Options :
- Launch: choisis **Default Activity**.
- Laisse les autres options par défaut.
- Clique sur Apply, puis sur **OK**.

## En cas d'erreur :
- Mettre à jour Android Studio
- Dans le fichier **gradle-wrapper.properties** : distributionUrl=https\://services.gradle.org/distributions/gradle-7.6.1-bin.zip
- **Installer Android 33 : **
- Ouvre Android Studio
- Va dans : **Tools > SDK Manager**
- Onglet **SDK Platforms**
- Coche **Android 13.0 (Tiramisu) – API Level 33**
- Clique sur **Apply > OK**
- Laisse-le installer les composants nécessaires.

