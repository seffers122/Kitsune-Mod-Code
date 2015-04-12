/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014
 *
 * See LICENSE for full License
 */

package kihira.tails.client.render;

import kihira.tails.api.IRenderHelper;
import kihira.tails.client.model.tail.ModelFluffyTail;
import kihira.tails.common.PartInfo;
import kihira.tails.common.PartsData;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

public class PlayerRenderHelper implements IRenderHelper {

    @Override
    public void onPreRenderTail(EntityLivingBase entity, RenderPart tail, PartInfo info, double x, double y, double z) {
        if (tail.modelPart instanceof ModelFluffyTail) {
            GL11.glTranslatef(0F, 0.65F, 0.1F);
            GL11.glScalef(0.8F, 0.8F, 0.8F);

        }
    }
}
