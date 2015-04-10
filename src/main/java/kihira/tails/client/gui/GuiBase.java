package kihira.tails.client.gui;

import kihira.foxlib.client.gui.GuiBaseScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public abstract class GuiBase extends GuiBaseScreen {

    //0 is bottom layer
    private final ArrayList<ArrayList<Panel>> layers = new ArrayList<ArrayList<Panel>>();

    public GuiBase(int layerCount) {
        for (int i = 0; i < layerCount; i++) {
            layers.add(new ArrayList<Panel>());
        }
    }

    public ArrayList<Panel> getLayer(int layer) {
        return layers.get(layer);
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
        for (ArrayList<Panel> layer : layers) {
            for (Panel panel : layer) {
                ScaledResolution scaledRes = new ScaledResolution(mc, panel.right - panel.left, panel.bottom - panel.top);
                panel.setWorldAndResolution(mc, scaledRes.getScaledWidth(), scaledRes.getScaledHeight());
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float p_73863_3_) {
        for (ArrayList<Panel> layer : layers) {
            for (Panel panel : layer) {
                if (panel.enabled) {
                    GL11.glPushMatrix();
                    GL11.glTranslatef(panel.left, panel.top, 0);
                    GL11.glColor4f(1, 1, 1, 1);
                    panel.drawScreen(mouseX - panel.left, mouseY - panel.top, p_73863_3_);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glPopMatrix();
                }
            }
        }
        GL11.glColor4f(1, 1, 1, 1);
        super.drawScreen(mouseX, mouseY, p_73863_3_);
    }

    @Override
    protected void keyTyped(char key, int keycode) {
        for (ArrayList<Panel> layer : layers) {
            for (Panel panel : layer) {
                if (panel.enabled) panel.keyTyped(key, keycode);
            }
        }
        super.keyTyped(key, keycode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (ArrayList<Panel> layer : layers) {
            for (Panel panel : layer) {
                if (panel.enabled && mouseX > panel.left && mouseX < panel.right && mouseY > panel.top && mouseY < panel.bottom || panel.alwaysReceiveMouse) {
                    panel.mouseClicked(mouseX - panel.left, mouseY - panel.top, mouseButton);
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int mouseButton) {
        for (ArrayList<Panel> layer : layers) {
            for (Panel panel : layer) {
                if (panel.enabled && mouseX > panel.left && mouseX < panel.right && mouseY > panel.top && mouseY < panel.bottom || panel.alwaysReceiveMouse) {
                    panel.mouseMovedOrUp(mouseX - panel.left, mouseY - panel.top, mouseButton);
                }
            }
        }
        super.mouseMovedOrUp(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long pressTime) {
        for (ArrayList<Panel> layer : layers) {
            for (Panel panel : layer) {
                if (panel.enabled && mouseX > panel.left && mouseX < panel.right && mouseY > panel.top && mouseY < panel.bottom || panel.alwaysReceiveMouse) {
                    panel.mouseClickMove(mouseX - panel.left, mouseY - panel.top, mouseButton, pressTime);
                }
            }
        }
        super.mouseClickMove(mouseX, mouseY, mouseButton, pressTime);
    }

    @Override
    public void onGuiClosed() {
        for (ArrayList<Panel> layer : layers) {
            for (Panel panel : layer) {
                panel.onGuiClosed();
            }
        }
        super.onGuiClosed();
    }
}
