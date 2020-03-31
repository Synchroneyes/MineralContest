package fr.mapbuilder.Blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlocksColorChanger {

    public static void changeBlockColor(Block givenBlock, BlocksDataColor wantedColor) {
        String[] wantedType = {"concrete", "terracotta", "glass"};
        String blockType = givenBlock.getType().toString().replace("Material.", "");
        String[] exploded_block_type = blockType.split("_");


        for(int indexExplodedBlockType = 0; indexExplodedBlockType < exploded_block_type.length; ++indexExplodedBlockType)
            for(int indexWantedType = 0; indexWantedType < wantedType.length; ++indexWantedType)
                if(exploded_block_type[indexExplodedBlockType].equalsIgnoreCase(wantedType[indexWantedType])) {
                    if(exploded_block_type[indexExplodedBlockType].equalsIgnoreCase("glass")) {
                        // -1 is STAINED, index is glass
                        blockType = exploded_block_type[indexExplodedBlockType - 1] + "_" + exploded_block_type[indexExplodedBlockType];
                    } else {
                        blockType = exploded_block_type[indexExplodedBlockType];
                    }

                    blockType = (wantedColor.color + "_" + blockType).toUpperCase();
                    System.out.println(blockType);
                }

        Material newBlockType = Material.valueOf(blockType);
        givenBlock.setType(newBlockType);
        givenBlock.getState().getData().setData(Byte.parseByte(wantedColor.blockDataColor + ""));



    }
}
