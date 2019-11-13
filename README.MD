# MineralContest

Ce mode de jeu a probablement été inventé par l'équipe de Squeezie, je ne m'avancerai pas sur ça.
Les règles du jeu sont les suivantes:

* Il y a trois équipes, l'équipe ROUGE, BLEU, et l'équipe JAUNE
* Ces trois équipes peuvent être composé de plusieurs joueurs (définissable, 2 par défaut)
* Chaque équipe doit récolté le plus de lingot, chaque lingot rapporte un certain nombre de points
* Il y a une arène, à un certain moment un coffre apparaitra, il sera possible de s'y téléporter, mais attention, une téléportation téléportera toute l'équipe ...
* Une partie dure 1 heure, 60 minutes.

## Commandes

```
mp_randomize_team <1 ou 0> # Permet d'activer ou non les équipes aléatoires (1 = oui, 0 = non aléatoire)
mp_iron_score <valeur> # Permet de modifier les points rapporté par un lingot de fer
mp_gold_score <valeur> # Permet de modifier les points rapporté par un lingot d'or
mp_diamond_score <valeur> # Permet de modifier les points rapporté par un lingot de diamant
mp_emerald_score <valeur> # Permet de modifier les points rapporté par un lingot d'émeraude
mp_team_max_player <valeur> # Permet de modifier le nombre de joueur requis par équipe pour lancer une partie
mp_enable_metrics <1 ou 0> # Permet d'activer ou non l'envoie de statistique

start # Permet de démarrer une partie
resume # Permet de reprendre une partie en pause
pause # permet de mettre une partie en pause
join <rouge | bleu | jaune> # permet de rejoindre une équipe
switch <player> <rouge | bleu | jaune> # Permet de mettre un joueur dans une équipe
```

## Installation

1. Télécharger le fichier [gamemode.zip](https://github.com/jaunefra/MineralContest/releases) 
2. Extraire l'archive
3. Glisser le dossier "world" dans le dossier du serveur
4. Glisser  le fichier .jar dans le dossier plugin
5. Lancer le jeu

## Librairies externes
[JSON.org](https://stleary.github.io/JSON-java/)

[EasyScoreboards](https://github.com/WinterAlexander/EasyScoreboards)

## Auteurs
[Synchroneyes](https://github.com/jaunefra)
[Turtle](https://github.com/IliasElabbassi)
