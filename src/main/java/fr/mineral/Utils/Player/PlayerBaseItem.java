package fr.mineral.Utils.Player;

import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class PlayerBaseItem {

    public static String everyRespawnName = "on_every_respawn";
    public static String onFirstSpawnName = "on_first_spawn";
    private static String[] whenChoices = {onFirstSpawnName, everyRespawnName};
    private static String[] availableTypeChoices = {"boots", "leggings", "chestplate", "helmet"};
    private static String[] autoEquipChoices = {"true", "false"};

    private static String[] itemAttributes = {"item_name", "quantity", "when"};
    private static String[] armorAttributes = {"item_name", "type", "auto_equip", "when"};


    private static mineralcontest plugin = mineralcontest.plugin;
    private static File baseItemFile;
    private static FileConfiguration fileConfiguration;

    private static LinkedList<ArmorItem> armorToGive;
    private static HashMap<ItemStack, String> itemsToGive;


    public static void copyDefaultFileToPluginDataFolder() {
        String file_name = "player_base_items.yml";
        baseItemFile = new File(plugin.getDataFolder(), file_name);
        if(!baseItemFile.exists())
            plugin.saveResource(file_name, false);

    }

    private static void setFileConfiguration() {
        if(baseItemFile == null) copyDefaultFileToPluginDataFolder();
        fileConfiguration = YamlConfiguration.loadConfiguration(baseItemFile);
    }

    public static void setPlayerBaseItems() throws Exception {
        if(fileConfiguration == null) setFileConfiguration();
        itemsToGive = new HashMap<>();
        armorToGive = new LinkedList<>();

        ConfigurationSection itemConfigSection = fileConfiguration.getConfigurationSection("items");
        ConfigurationSection armorConfigSection = fileConfiguration.getConfigurationSection("armors");

        if(itemConfigSection == null) {
            throw new Exception("Items section missing from player_base_item.yml file");
        }

        if(armorConfigSection == null)
            throw new Exception("Armor section missing from player_base_item.yml file");

        Set<String> itemsFromYML = itemConfigSection.getKeys(false);
        Set<String> armorsFromYML = armorConfigSection.getKeys(false);

        // For each items
        for(String itemName : itemsFromYML) {
            ConfigurationSection itemAttributesConfigSection = itemConfigSection.getConfigurationSection(itemName);
            ItemStack item = new ItemStack(Material.AIR, 1);
            String when = "";
            Boolean[] isAttributeSet = new Boolean[itemAttributes.length];
            Arrays.fill(isAttributeSet, false);
            for(String itemAttribute : itemAttributes) {
                Object itemAttributeValue = itemAttributesConfigSection.get(itemAttribute);
                if(itemAttributeValue != null) {
                    switch(itemAttribute) {
                        case "item_name":
                            Material itemMaterial = Material.valueOf((String) itemAttributeValue);
                            item.setType(itemMaterial);
                            isAttributeSet[getAttributeIndex(itemAttribute, "item")] = true;
                            break;
                        case "quantity":
                            item.setAmount((int) itemAttributeValue);
                            isAttributeSet[getAttributeIndex(itemAttribute, "item")] = true;
                            break;
                        case "when":
                            when = (String) itemAttributeValue;
                            for(String whenChoice : whenChoices)
                                if(when.equals(whenChoice)) {
                                    isAttributeSet[getAttributeIndex(itemAttribute, "item")] = true;
                                    break;
                                }
                            break;
                    }
                }

            }

            boolean error = false;
            for(int index = 0; index < isAttributeSet.length; index++)
                if(!isAttributeSet[index]) {
                    Bukkit.getLogger().severe("[MineralContest][player_base_item.yml] Missing item attribute \" " +  itemAttributes[index]+ "\" for item: " + itemName);
                    error = true;
                }

            if(!error) {
                itemsToGive.put(item, when);
                error = false;
            }
        }

        // For each Armor
        for(String itemName : armorsFromYML) {
            ConfigurationSection armorAttributesConfigSection = armorConfigSection.getConfigurationSection(itemName);
            ArmorItem armorToSave = new ArmorItem();
            String when = "";
            Boolean[] isAttributeSet = new Boolean[armorAttributes.length];
            Arrays.fill(isAttributeSet, false);
            for(String itemAttribute : armorAttributes) {
                Object attributeValue = armorAttributesConfigSection.get(itemAttribute);
                if(attributeValue instanceof Boolean) attributeValue = String.valueOf(attributeValue);
                if(attributeValue != null) {
                    switch((String) itemAttribute) {
                        case "item_name":
                            Material itemMaterial = Material.valueOf((String) attributeValue);
                            armorToSave.setItem_name(itemMaterial);
                            isAttributeSet[getAttributeIndex(itemAttribute, "armor")] = true;
                            break;

                        case "type":
                            try {
                                int indexType = getArmorTypeIndex((String) attributeValue);
                                armorToSave.setType(indexType);
                                isAttributeSet[getAttributeIndex(itemAttribute, "armor")] = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                                Error.Report(e);
                            }
                            break;

                        case "auto_equip":
                            try {
                                int autoEquip_index = getArmorAutoEquipe((String) attributeValue);
                                armorToSave.setAuto_equip((String) attributeValue);
                                isAttributeSet[getAttributeIndex(itemAttribute, "armor")] = true;
                            }catch (Exception e) {
                                e.printStackTrace();
                                Error.Report(e);
                            }
                            break;


                        case "when":
                            when = (String) attributeValue;
                            for(String whenChoice : whenChoices)
                                if(when.equals(whenChoice)) {
                                    armorToSave.setWhen(when);
                                    isAttributeSet[getAttributeIndex(itemAttribute, "armor")] = true;
                                    break;
                                }
                            break;
                    }
                }
            }

            boolean error = false;
            for(int index = 0; index < isAttributeSet.length; index++)
                if(!isAttributeSet[index]) {
                    Bukkit.getLogger().severe("[MineralContest][player_base_item.yml] Missing armor attribute \" " +  armorAttributes[index]+ "\" for item: " + itemName);
                    error = true;
                }

            if(!error) {
                armorToGive.add(armorToSave);
                error = false;
            }
        }
    }

    public static void givePlayerItems(Player player, String when) throws Exception {

        if(plugin.getGame().isGameStarted()) {
            if(itemsToGive == null) setPlayerBaseItems();

            for(Map.Entry<ItemStack, String> item : itemsToGive.entrySet()) {
                ItemStack itemToGive = item.getKey();
                String whenToGive = item.getValue();
                if(whenToGive.equals(everyRespawnName)) player.getInventory().addItem(itemToGive);
                else if(whenToGive.equalsIgnoreCase(when)) player.getInventory().addItem(itemToGive);
            }


            ItemStack[] armorToApply = new ItemStack[4];
            for(ArmorItem armor : armorToGive) {
                if(armor.getWhen().equals(everyRespawnName)) {
                    if(armor.getAuto_equip().equalsIgnoreCase("true")) armorToApply[armor.getType()] = new ItemStack(armor.getItem_name(), 1);
                    else {
                        // if auto equip = false
                        player.getInventory().addItem(new ItemStack(armor.getItem_name(), 1));
                        armorToApply[armor.getType()] = new ItemStack(Material.AIR, 1);
                    }
                } else if(armor.getWhen().equalsIgnoreCase(when)) {
                    if(armor.getAuto_equip().equalsIgnoreCase("true")) armorToApply[armor.getType()] = new ItemStack(armor.getItem_name(), 1);
                    else {
                        // if auto equip = false
                        player.getInventory().addItem(new ItemStack(armor.getItem_name(), 1));
                        armorToApply[armor.getType()] = new ItemStack(Material.AIR, 1);
                    }
                }
            }

            player.getInventory().setArmorContents(armorToApply);


        } else {
            Bukkit.getLogger().info("Tried to give player items when game is not started");
        }
    }




    private static int getAttributeIndex(String attribute, String type) throws Exception {
        int index = 0;
        if(type.equalsIgnoreCase("item")) {
            for(index = 0; index < itemAttributes.length; index++)
                if(itemAttributes[index].equalsIgnoreCase(attribute))
                    return index;

            throw new Exception("Invalide attribute for type: " + type);
        }

        if(type.equalsIgnoreCase("armor")) {
            for(index = 0; index < armorAttributes.length; index++)
                if(armorAttributes[index].equalsIgnoreCase(attribute))
                    return index;

            throw new Exception("Invalide attribute for type: " + type);
        }

        throw new Exception("Invalid type, required item or armor");
    }

    private static int getArmorTypeIndex(String type) throws Exception {
        for(int index = 0; index < availableTypeChoices.length; ++index)
            if(availableTypeChoices[index].equals(type)) return index;
        throw new Exception("Invalid armor type");
    }

    private static int getArmorAutoEquipe(String type) throws Exception {
        for(int index = 0; index < autoEquipChoices.length; ++index)
            if(autoEquipChoices[index].equals(type)) return index;
        throw new Exception("Invalid armor auto equipe");
    }
}
