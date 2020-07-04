package fr.synchroneyes.mineral.Core.Coffre;

import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import java.util.LinkedList;
import java.util.List;

public class AutomatedChestManager {

    private List<AutomatedChestAnimation> coffresAvecAnimation;

    public AutomatedChestManager() {
        this.coffresAvecAnimation = new LinkedList<>();

        registerCoffres();
    }


    private void registerCoffres() {
        this.coffresAvecAnimation.add(new TestAnimation());
    }

    public AutomatedChestAnimation getFromClass(Class c) {
        for (AutomatedChestAnimation automatedChestAnimation : coffresAvecAnimation)
            if (automatedChestAnimation.getClass().equals(c)) return automatedChestAnimation;
        return null;
    }

    public AutomatedChestAnimation getFromInventory(Inventory v) {
        for (AutomatedChestAnimation automatedChestAnimation : coffresAvecAnimation)
            if (automatedChestAnimation.getInventory().equals(v)) return automatedChestAnimation;
        return null;
    }

    public void replace(Class c, AutomatedChestAnimation automatedChestAnimation) {
        for (AutomatedChestAnimation automatedChestAnimation1 : coffresAvecAnimation) {
            if (automatedChestAnimation1.getClass().equals(c)) {
                coffresAvecAnimation.remove(automatedChestAnimation1);
                break;
            }
        }

        coffresAvecAnimation.add(automatedChestAnimation);
    }

    public boolean isThisAnAnimatedInventory(Inventory i) {
        for (AutomatedChestAnimation chestAnimation : coffresAvecAnimation)
            if (chestAnimation.getInventory().equals(i)) return true;
        return false;
    }

    public AutomatedChestAnimation getChestAnomation(Block b) {
        for (AutomatedChestAnimation automatedChest : coffresAvecAnimation)
            if (automatedChest.getLocation().equals(b.getLocation())) return automatedChest;
        return null;
    }

    public boolean isThisBlockAChestAnimation(Block b) {
        for (AutomatedChestAnimation automatedChest : coffresAvecAnimation) {
            if (automatedChest.getLocation() != null && automatedChest.getLocation().equals(b.getLocation()))
                return true;

        }
        return false;
    }


}
