package fr.synchroneyes.data_storage;

/**
 * Classe listant le nom des tables en base de donnée
 */
public enum DatabaseTablesName {
    game("game", "CREATE TABLE IF NOT EXISTS `mineral_game` ( `id` int(11) NOT NULL, `gamestate` enum('started','ended') NOT NULL, `date_start` timestamp NOT NULL DEFAULT current_timestamp(), `date_end` timestamp NULL DEFAULT NULL, `map` varchar(255) NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ALTER TABLE `mineral_game` ADD PRIMARY KEY (`id`); ALTER TABLE `mineral_game` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;"),
    players("players", "CREATE TABLE IF NOT EXISTS `mineral_players` ( `id` int(11) NOT NULL, `uuid` text NOT NULL, `onlinemode` tinyint(1) NOT NULL, `name` text NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ALTER TABLE `mineral_players` ADD PRIMARY KEY (`id`); ALTER TABLE `mineral_players` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;"),
    game_chests("game_chests", "CREATE TABLE IF NOT EXISTS `mineral_game_chests` ( `id` int(11) NOT NULL, `gameid` int(11) NOT NULL, `playerid` int(11) NOT NULL, `chest_type` enum('airdrop','arena_chest','player_death_chest') NOT NULL, `first_opening` tinyint(1) NOT NULL DEFAULT 0, `date` timestamp NOT NULL DEFAULT current_timestamp() ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ALTER TABLE `mineral_game_chests` ADD PRIMARY KEY (`id`), ADD KEY `chest_gameid` (`gameid`), ADD KEY `chest_playerid` (`playerid`); ALTER TABLE `mineral_game_chests` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT; ALTER TABLE `mineral_game_chests` ADD CONSTRAINT `chest_gameid` FOREIGN KEY (`gameid`) REFERENCES `mineral_game` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE, ADD CONSTRAINT `chest_playerid` FOREIGN KEY (`playerid`) REFERENCES `mineral_players` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE;"),
    game_kills("game_kills", "CREATE TABLE IF NOT EXISTS `mineral_game_kills` ( `id` int(11) NOT NULL, `gameid` int(11) NOT NULL, `dead_playerid` int(11) NOT NULL, `killer_playerid` int(11) NOT NULL, `cause` text DEFAULT NULL, `date` timestamp NOT NULL DEFAULT current_timestamp() ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ALTER TABLE `mineral_game_kills` ADD PRIMARY KEY (`id`), ADD KEY `gameid` (`gameid`), ADD KEY `dead_playerid` (`dead_playerid`), ADD KEY `killer_playerid` (`killer_playerid`); ALTER TABLE `mineral_game_kills` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT; ALTER TABLE `mineral_game_kills` ADD CONSTRAINT `dead_playerid` FOREIGN KEY (`dead_playerid`) REFERENCES `mineral_players` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE, ADD CONSTRAINT `gameid` FOREIGN KEY (`gameid`) REFERENCES `mineral_game` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE, ADD CONSTRAINT `killer_playerid` FOREIGN KEY (`killer_playerid`) REFERENCES `mineral_players` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE;"),
    game_kits("game_kits", "CREATE TABLE IF NOT EXISTS `mineral_game_kits` ( `id` int(11) NOT NULL, `gameid` int(11) NOT NULL, `playerid` int(11) NOT NULL, `kit` text NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ALTER TABLE `mineral_game_kits` ADD PRIMARY KEY (`id`), ADD KEY `kit_playerid` (`playerid`), ADD KEY `kit_gameid` (`gameid`); ALTER TABLE `mineral_game_kits` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT; ALTER TABLE `mineral_game_kits` ADD CONSTRAINT `kit_gameid` FOREIGN KEY (`gameid`) REFERENCES `mineral_game` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE, ADD CONSTRAINT `kit_playerid` FOREIGN KEY (`playerid`) REFERENCES `mineral_players` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE;"),
    game_players("game_players", "CREATE TABLE IF NOT EXISTS `mineral_game_players` ( `id` int(11) NOT NULL, `gameid` int(11) NOT NULL, `playerid` int(11) NOT NULL, `join_event` enum('at_game_start','in_game','referee') NOT NULL, `team` varchar(255) DEFAULT NULL ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ALTER TABLE `mineral_game_players` ADD PRIMARY KEY (`id`), ADD KEY `game_player_id` (`playerid`), ADD KEY `game_id` (`gameid`); ALTER TABLE `mineral_game_players` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT; ALTER TABLE `mineral_game_players` ADD CONSTRAINT `game_id` FOREIGN KEY (`gameid`) REFERENCES `mineral_game` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE, ADD CONSTRAINT `game_player_id` FOREIGN KEY (`playerid`) REFERENCES `mineral_game_players` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE;"),
    game_shop_purchase("game_shop_purchase", "CREATE TABLE IF NOT EXISTS `mineral_game_shop_purchase` ( `id` int(11) NOT NULL, `gameid` int(11) NOT NULL, `playerid` int(11) NOT NULL, `shop_item` text DEFAULT NULL, `item_price` int(11) NOT NULL, `date` timestamp NOT NULL DEFAULT current_timestamp() ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ALTER TABLE `mineral_game_shop_purchase` ADD PRIMARY KEY (`id`), ADD KEY `shop_playerid` (`playerid`), ADD KEY `shop_gameid` (`gameid`); ALTER TABLE `mineral_game_shop_purchase` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT; ALTER TABLE `mineral_game_shop_purchase` ADD CONSTRAINT `shop_gameid` FOREIGN KEY (`gameid`) REFERENCES `mineral_game` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE, ADD CONSTRAINT `shop_playerid` FOREIGN KEY (`playerid`) REFERENCES `mineral_game_players` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE; COMMIT;"),
    game_end_players_info("game_end_players_info", "CREATE TABLE IF NOT EXISTS `mineral_game_end_players_info` ( `id` int(11) NOT NULL, `gameid` int(11) NOT NULL, `playerid` int(11) NOT NULL, `score_brought` int(11) NOT NULL, `score_lost` int(11) NOT NULL, `team_score` int(11) NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; "),
    game_end_teams_info("game_end_teams_info", "CREATE TABLE IF NOT EXISTS `mineral_game_end_teams_info` ( `id` int(11) NOT NULL, `gameid` int(11) NOT NULL, `team` varchar(255) NOT NULL, `score` int(11) NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ");

    private final String tableName;
    private final String db_content;

    DatabaseTablesName(String s, String db_content) {
        this.tableName = s;
        this.db_content = db_content;
    }

    public String toString() {
        String prefix = "mineral_";
        return prefix + tableName;
    }

    public String getCreationQuery() {
        return db_content;
    }
}
