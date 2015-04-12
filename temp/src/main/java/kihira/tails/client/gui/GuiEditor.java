/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014
 *
 * See LICENSE for full License
 */

package kihira.tails.client.gui;

import kihira.tails.client.texture.TextureHelper;
import kihira.tails.common.PartInfo;
import kihira.tails.common.PartsData;
import kihira.tails.common.Tails;
import net.minecraft.client.Minecraft;

public class GuiEditor extends GuiBase {

    public int textureID;
    private PartsData.PartType partType;
    private PartsData partsData;
    private PartInfo editingPartInfo;
    public PartInfo originalPartInfo;

    public TintPanel tintPanel;
    public PartsPanel partsPanel;
    private PreviewPanel previewPanel;
    public TexturePanel texturePanel;
    public ControlsPanel controlsPanel;
    public LibraryPanel libraryPanel;
    public LibraryInfoPanel libraryInfoPanel;
    public LibraryImportPanel libraryImportPanel;

    public GuiEditor() {
        super(4);
        //Backup original PartInfo or create default one
        PartInfo partInfo;
        if (Tails.localPartsData == null) {
            Tails.setLocalPartsData(new PartsData());
        }

        //Default to Tail
        partType = PartsData.PartType.TAIL;
        for (PartsData.PartType partType : PartsData.PartType.values()) {
            if (!Tails.localPartsData.hasPartInfo(partType)) {
                Tails.localPartsData.setPartInfo(partType, new PartInfo(true, 1, 2, textureID, 0xFF4800, 0xFF0000, 0xDC143C, null, partType));
            }
        }
        partInfo = Tails.localPartsData.getPartInfo(partType);

        originalPartInfo = partInfo.deepCopy();
        setPartsData(Tails.localPartsData.deepCopy());
        this.editingPartInfo = originalPartInfo.deepCopy();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        int previewWindowEdgeOffset = 110;
        int previewWindowRight = width - previewWindowEdgeOffset;
        int previewWindowBottom = height - 30;

        //Not an ideal solution but keeps everything from resetting on resize
        if (tintPanel == null) {
            getLayer(0).add(previewPanel = new PreviewPanel(this, previewWindowEdgeOffset, 0, previewWindowRight - previewWindowEdgeOffset, previewWindowBottom));
            getLayer(1).add(partsPanel = new PartsPanel(this, 0, 0, previewWindowEdgeOffset, height - 43));
            getLayer(1).add(libraryPanel = new LibraryPanel(this, 0, 0, previewWindowEdgeOffset, height));
            getLayer(1).add(texturePanel = new TexturePanel(this, 0, height - 43, previewWindowEdgeOffset, 43));
            getLayer(1).add(tintPanel = new TintPanel(this, previewWindowRight, 0, width - previewWindowRight, height));
            getLayer(1).add(libraryImportPanel = new LibraryImportPanel(this, previewWindowRight, height - 60, width - previewWindowRight, 60));
            getLayer(1).add(libraryInfoPanel = new LibraryInfoPanel(this, previewWindowRight, 0, width - previewWindowRight, height - 60));
            getLayer(1).add(controlsPanel = new ControlsPanel(this, previewWindowEdgeOffset, previewWindowBottom, previewWindowRight - previewWindowEdgeOffset, height - previewWindowBottom));

            libraryInfoPanel.enabled = false;
            libraryImportPanel.enabled = false;
            libraryPanel.enabled = false;
        }
        else {
            tintPanel.resize(previewWindowRight, 0, width - previewWindowRight, height);
            libraryInfoPanel.resize(previewWindowRight, 0, width - previewWindowRight, height - 60);
            partsPanel.resize(0, 0, previewWindowEdgeOffset, height - 43);
            libraryPanel.resize(0, 0, previewWindowEdgeOffset, height);
            previewPanel.resize(previewWindowEdgeOffset, 0, previewWindowRight - previewWindowEdgeOffset, previewWindowBottom);
            texturePanel.resize(0, height - 43, previewWindowEdgeOffset, 43);
            libraryImportPanel.resize(previewWindowRight, height - 60, width - previewWindowRight, 60);
            controlsPanel.resize(previewWindowEdgeOffset, previewWindowBottom, previewWindowRight - previewWindowEdgeOffset, height - previewWindowBottom);
        }
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        Tails.proxy.addPartsData(mc.getSession().func_148256_e().getId(), Tails.localPartsData);
        super.onGuiClosed();
    }

    public void refreshTintPane() {
        tintPanel.refreshTintPane();
    }

    public void setPartsInfo(PartInfo newPartInfo) {
        //editingPartInfo.setTexture(null); //Clear texture data as we will no longer need it
        editingPartInfo = newPartInfo;
        if (editingPartInfo.hasPart) editingPartInfo.setTexture(TextureHelper.generateTexture(editingPartInfo));

        partsData.setPartInfo(partType, editingPartInfo);
        setPartsData(partsData);
    }

    public PartInfo getEditingPartInfo() {
        return editingPartInfo;
    }

    public void setPartsData(PartsData newPartsData) {
        partsData = newPartsData;
        Tails.proxy.addPartsData(Minecraft.getMinecraft().thePlayer.getGameProfile().getId(), partsData);
    }

    public PartsData getPartsData() {
        return partsData;
    }

    public void setPartType(PartsData.PartType partType) {
        this.partType = partType;

        PartInfo newPartInfo = partsData.getPartInfo(partType);
        if (newPartInfo == null) {
            newPartInfo = new PartInfo(false, 0, 0, 0, 0xFF4800, 0xFF0000, 0xDC143C, null, partType);
        }
        originalPartInfo = newPartInfo.deepCopy();
        PartInfo partInfo = originalPartInfo.deepCopy();

        clearCurrTintEdit();
        setPartsInfo(partInfo);
        partsPanel.initPartList();
        refreshTintPane();
        textureID = partInfo.textureID;
    }

    public PartsData.PartType getPartType() {
        return partType;
    }

    public void clearCurrTintEdit() {
        tintPanel.currTintEdit = 0;
    }
}
