/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014
 *
 * See LICENSE for full License
 */

package kihira.tails.client;

import com.google.common.collect.ArrayListMultimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.tails.client.model.tail.*;
import kihira.tails.client.render.RenderPart;
import kihira.tails.common.PartsData;

import java.util.List;

//Yeah using side only isn't nice but as this is static, it means it only gets constructed on the client
@SideOnly(Side.CLIENT)
public class PartRegistry {

    private static final ArrayListMultimap<PartsData.PartType, RenderPart> partRegistry = ArrayListMultimap.create();

    static {
        //Tails
        registerPart(PartsData.PartType.TAIL, new RenderPart("tail.fluffy", 2, null, new ModelFluffyTail(), "foxTail"));


    }

    public static void registerPart(PartsData.PartType partType, RenderPart renderPart) {
        partRegistry.put(partType, renderPart);
    }

    public static List<RenderPart> getParts(PartsData.PartType partType) {
        return partRegistry.get(partType);
    }

    /**
     * Safely gets a render part. By safely, this means it checks if the type id is within bounds of the list for that
     * part type and if not, returns the RenderPart associated with type id 0.
     * @param partType The part type
     * @param index The index/type id
     * @return The render part
     */
    public static RenderPart getRenderPart(PartsData.PartType partType, int index) {
        List<RenderPart> parts = PartRegistry.getParts(partType);
        index = index >= parts.size() ? 0 : index;
        return parts.get(index);
    }
}
