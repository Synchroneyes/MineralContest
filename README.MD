# MineralContest - [Site web](http://mineral.synchroneyes.fr)


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
mp_add_team_penality <valeur> <equipe> # Ajouter des points de pénalité à une équipe
mp_reset_team_penality <equipe> # Reset la pénalité d'une équipe

start # Permet de démarrer une partie
resume # Permet de reprendre une partie en pause
pause # permet de mettre une partie en pause
join <rouge | bleu | jaune> # permet de rejoindre une équipe
switch <player> <rouge | bleu | jaune> # Permet de mettre un joueur dans une équipe
arene # Permet de téléporter l'équipe dans l'arène
arena # Permet de téléporter l'équipe dans l'arène
```

## Installation

1. Télécharger la map [http://mineral.synchroneyes.fr/](http://mineral.synchroneyes.fr/download/map/world.zip)
2. Glisser le dossier la map dans le dossier du serveur et le renommer si necessaire en "world"
3. Télécharger le fichier jar [MineralContest-1.1.jar](https://github.com/jaunefra/MineralContest/releases/download/1.1/MineralContest-1.1.jar)
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
