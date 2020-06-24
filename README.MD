# MineralContest - [Site web](http://mineral.synchroneyes.fr)


Ce mode de jeu a probablement été inventé par l'équipe de Squeezie, je ne m'avancerai pas sur ça.
Les règles du jeu sont les suivantes:

* Il y a trois équipes, l'équipe ROUGE, BLEU, et l'équipe JAUNE
* Ces trois équipes peuvent être composé de p lusieurs joueurs (définissable, 2 par défaut)
* Chaque équipe doit récolté le plus de lingot, chaque lingot rapporte un certain nombre de points
* Il y a une arène, à un certain moment un coffre apparaitra, il sera possible de s'y téléporter, mais attention, une téléportation téléportera toute l'équipe ...
* Une partie dure 1 heure, 60 minutes.

## Commandes

```
[ADMIN] mp_randomize_team <1 ou 0> # Permet d'activer ou non les équipes aléatoires (1 = oui, 0 = non aléatoire)

[ADMIN] mp_iron_score  # Permet de modifier les points rapporté par un lingot de fer

[ADMIN] mp_gold_score  # Permet de modifier les points rapporté par un lingot d'or

[ADMIN] mp_diamond_score  # Permet de modifier les points rapporté par un lingot de diamant

[ADMIN] mp_emerald_score  # Permet de modifier les points rapporté par un lingot d'émeraude

[ADMIN] mp_team_max_player  # Permet de modifier le nombre de joueur requis par équipe pour lancer une partie

[ADMIN] mp_enable_metrics  <1 ou 0> # Permet d'activer ou non l'envoie de statistique

[ADMIN] mp_add_team_penality   # Ajoute une pénalité à une équipe

[ADMIN] mp_reset_team_penality  # Supprime une pénalité d'équipe

[ADMIN] mp_set_language  # Choisit le langage à 

[ADMIN] mp_start_vote  # Démarre un vote

[ADMIN] mp_enable_item_drop <0 ou 1 ou 2> # 0 = aucun drop, 1 = drop de minerais, 2 = drop tout 
l'inventaire

[ADMIN] mp_enable_friendly_fire <0 ou 1> # Active ou non le tir allié

[ADMIN] mp_set_playzone_radius <valeur> # Défini la taille en block de la zone de jeu

[ADMIN] start  # Permet de démarrer une partie, si la valeur "force" est ajoutée, alors la partie démarrera sans tenir compte du nombre de joueur en ligne

[ADMIN] resume # Permet de reprendre une partie en pause

[ADMIN] pause # permet de mettre une partie en pause

[ADMIN] switch   # Permet de mettre un joueur dans une équipe

[ADMIN] allow <pseudo> # Permet la connexion d'un joueur

[ADMIN] referee # Permet de devenir arbitre

[ADMIN] stopgame # Arrête une partie

ready # Permet de se mettre prêt

join  # permet de rejoindre une équipe

t <message> # Envoie un message à son équipe

team <message> # Envoie un message à son équipe


```

## Installation

1. Télécharger la map [http://mineral.synchroneyes.fr/downloads](http://synchroneyes.fr/downloads/)
2. Glisser le dossier la map dans le dossier du serveur et le renommer si necessaire en "world"
3. Télécharger le fichier jar [MineralContest-1.5.4.1.jar](https://github.com/jaunefra/MineralContest/releases/download/1.5.4.1/MineralContest-1.5.4.1.jar)
4. Glisser  le fichier .jar dans le dossier plugin
(OPTIONEL) Supprimer le fichier MineralContest-1.0.jar si il existe
5. Lancer le jeu
6. Have fun !

## Librairies externes
[JSON.org](https://stleary.github.io/JSON-java/)

[EasyScoreboards](https://github.com/WinterAlexander/EasyScoreboards)

## Auteurs
[Synchroneyes](https://github.com/jaunefra)
[Turtle](https://github.com/IliasElabbassi)


___

## A faire
- Remplacer la lecture de fichier json en fichier yml
- Ajouter la possibilité de créer ses propres map (déjà implémenter mais besoin de modification (plus de json, mais du yml))
