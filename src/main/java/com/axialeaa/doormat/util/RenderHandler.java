package com.axialeaa.doormat.util;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.List;
import java.util.*;

public class RenderHandler {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final ClientWorld WORLD = CLIENT.world;
    private static final HashSet<Line> LINES = new HashSet<>();
    private static final HashSet<Quad> QUADS = new HashSet<>();
    private static final HashSet<Line> DEPTH_LINES = new HashSet<>();
    private static final HashSet<Quad> DEPTH_QUADS = new HashSet<>();

    /**
     * A list of visually-distinct colours based on <a href="https://sashamaps.net/docs/resources/20-colors/">this list</a> by Sasha Trubetskoy, annotated with their "accessibility ratings".
     */
    public static final Map<String, Color> TRUBETSKOY_COLORS = Util.make(Maps.newHashMap(), map -> {
        map.put("maroon",   new Color(128,   0,   0)); // 99.99
        map.put("red",      new Color(230,  25,  75)); // 99
        map.put("pink",     new Color(250, 190, 212)); // 99
        map.put("brown",    new Color(170, 110,  40)); // 99
        map.put("orange",   new Color(245, 130,  48)); // 99.99
        map.put("apricot",  new Color(255, 215, 180)); // 95
        map.put("olive",    new Color(128, 128,   0)); // 95
        map.put("yellow",   new Color(255, 225,  25)); // 100
        map.put("beige",    new Color(255, 250, 200)); // 99
        map.put("lime",     new Color(210, 245,  60)); // 95
        map.put("green",    new Color( 60, 180,  75)); // 99
        map.put("mint",     new Color(170, 255, 195)); // 99
        map.put("teal",     new Color(  0, 128, 128)); // 99
        map.put("cyan",     new Color( 70, 240, 240)); // 99
        map.put("navy",     new Color(  0,   0, 128)); // 99.99
        map.put("blue",     new Color(  0, 130, 200)); // 100
        map.put("purple",   new Color(145,  30, 180)); // 95
        map.put("lavender", new Color(220, 190, 255)); // 99.99
        map.put("magenta",  new Color(240,  50, 230)); // 99
        map.put("black",    Color.BLACK);                       // 100
        map.put("grey",     Color.GRAY);                        // 100
        map.put("white",    Color.WHITE);                       // 100
    });

    public static Color getTrubetskoyColor(String key, int alpha) {
        if (!TRUBETSKOY_COLORS.containsKey(key))
            throw new NullPointerException("No Trubetskoy color of key " + key + "was found.");

        Color color = TRUBETSKOY_COLORS.get(key);

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        return new Color(r, g, b, alpha);
    }

    public static Color getTrubetskoyColor(String key) {
        return getTrubetskoyColor(key, 255);
    }

    public static Color getColorWithAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public synchronized static void render() {
        Camera camera = CLIENT.gameRenderer.getCamera();
        Vec3d cameraPos = camera.getPos();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        RenderSystem.disableDepthTest();

        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        Iterator<Line> lineIterator = LINES.iterator();
        while (lineIterator.hasNext()) {
            Line line = lineIterator.next();
            line.create(buffer, cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
            if (WORLD == null || WORLD.getTime() >= line.removalTime)
                lineIterator.remove();
        }
        tessellator.draw();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Iterator<Quad> quadIterator = QUADS.iterator();
        while (quadIterator.hasNext()) {
            Quad quad = quadIterator.next();
            quad.create(buffer, cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
            if (WORLD == null || WORLD.getTime() >= quad.removalTime)
                quadIterator.remove();
        }
        tessellator.draw();

        RenderSystem.enableDepthTest();

        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        Iterator<Line> depthLineIterator = DEPTH_LINES.iterator();
        while (depthLineIterator.hasNext()) {
            Line line = depthLineIterator.next();
            line.create(buffer, cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
            if (WORLD == null || WORLD.getTime() >= line.removalTime)
                depthLineIterator.remove();
        }
        tessellator.draw();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Iterator<Quad> depthQuadIterator = DEPTH_QUADS.iterator();
        while (depthQuadIterator.hasNext()) {
            Quad quad = depthQuadIterator.next();
            quad.create(buffer, cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
            if (WORLD == null || WORLD.getTime() >= quad.removalTime)
                depthQuadIterator.remove();
        }
        tessellator.draw();
    }

    public synchronized static void addLine(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color strokeColor, long removalTime, boolean shouldObeyDepthTest) {
        if (WORLD == null || strokeColor.getAlpha() == 0)
            return;

        long l = WORLD.getTime() + removalTime;
        List<Line> lines = new ArrayList<>();

        lines.add(new Line(minX, minY, minZ, maxX, maxY, maxZ, strokeColor, l));

        if (shouldObeyDepthTest)
            DEPTH_LINES.addAll(lines);
        else LINES.addAll(lines);
    }

    public synchronized static void addCuboidLines(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color strokeColor, long removalTime, boolean shouldObeyDepthTest) {
        addLine(minX, minY, minZ, maxX, minY, minZ, strokeColor, removalTime, shouldObeyDepthTest);
        addLine(maxX, minY, minZ, maxX, maxY, minZ, strokeColor, removalTime, shouldObeyDepthTest);
        addLine(maxX, maxY, minZ, minX, maxY, minZ, strokeColor, removalTime, shouldObeyDepthTest);
        addLine(minX, maxY, minZ, minX, minY, minZ, strokeColor, removalTime, shouldObeyDepthTest);

        addLine(minX, minY, minZ, minX, minY, maxZ, strokeColor, removalTime, shouldObeyDepthTest);
        addLine(maxX, minY, minZ, maxX, minY, maxZ, strokeColor, removalTime, shouldObeyDepthTest);
        addLine(minX, maxY, minZ, minX, maxY, maxZ, strokeColor, removalTime, shouldObeyDepthTest);
        addLine(maxX, maxY, minZ, maxX, maxY, maxZ, strokeColor, removalTime, shouldObeyDepthTest);

        addLine(minX, minY, maxZ, maxX, minY, maxZ, strokeColor, removalTime, shouldObeyDepthTest);
        addLine(maxX, minY, maxZ, maxX, maxY, maxZ, strokeColor, removalTime, shouldObeyDepthTest);
        addLine(maxX, maxY, maxZ, minX, maxY, maxZ, strokeColor, removalTime, shouldObeyDepthTest);
        addLine(minX, maxY, maxZ, minX, minY, maxZ, strokeColor, removalTime, shouldObeyDepthTest);
    }

    public static void addCuboidLines(Vec3d pos, double size, Color strokeColor, long removalTime, boolean shouldObeyDepthTest) {
        addCuboidLines(pos.getX() + size, pos.getY() + size, pos.getZ() + size, pos.getX() - size, pos.getY() - size, pos.getZ() - size, strokeColor, removalTime, shouldObeyDepthTest);
    }

    public synchronized static void addCuboidQuads(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color fillColor, long removalTime, boolean shouldObeyDepthTest) {
        if (WORLD == null || fillColor.getAlpha() == 0)
            return;

        long l = WORLD.getTime() + removalTime;
        List<Quad> quads = new ArrayList<>();

        quads.add(new Quad(
            minX, minY, minZ,
            maxX, minY, minZ,
            maxX, maxY, minZ,
            minX, maxY, minZ,
            fillColor, l
        )); // north side
        quads.add(new Quad(
            minX, minY, maxZ,
            minX, maxY, maxZ,
            maxX, maxY, maxZ,
            maxX, minY, maxZ,
            fillColor, l
        )); // south side
        quads.add(new Quad(
            minX, minY, minZ,
            minX, maxY, minZ,
            minX, maxY, maxZ,
            minX, minY, maxZ,
            fillColor, l
        )); // west side
        quads.add(new Quad(
            maxX, minY, minZ,
            maxX, minY, maxZ,
            maxX, maxY, maxZ,
            maxX, maxY, minZ,
            fillColor, l
        )); // east side
        quads.add(new Quad(
            minX, minY, minZ,
            minX, minY, maxZ,
            maxX, minY, maxZ,
            maxX, minY, minZ,
            fillColor, l
        )); // down side
        quads.add(new Quad(
            minX, maxY, minZ,
            maxX, maxY, minZ,
            maxX, maxY, maxZ,
            minX, maxY, maxZ,
            fillColor, l
        )); // up side

        if (shouldObeyDepthTest)
            DEPTH_QUADS.addAll(quads);
        else QUADS.addAll(quads);
    }

    public static void addCuboidQuads(Vec3d pos, double size, Color fillColor, long removalTime, boolean shouldObeyDepthTest) {
        addCuboidQuads(pos.getX() + size, pos.getY() + size, pos.getZ() + size, pos.getX() - size, pos.getY() - size, pos.getZ() - size, fillColor, removalTime, shouldObeyDepthTest);
    }

    public static void addCuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color fillColor, Color strokeColor, long removalTime, boolean shouldObeyDepthTest) {
        addCuboidLines(minX, minY, minZ, maxX, maxY, maxZ, strokeColor, removalTime, shouldObeyDepthTest);
        addCuboidQuads(minX, minY, minZ, maxX, maxY, maxZ, fillColor, removalTime, shouldObeyDepthTest);
    }

    public static void addCuboid(Vec3d pos, double size, Color fillColor, Color strokeColor, long removalTime, boolean shouldObeyDepthTest) {
        addCuboidLines(pos, size, strokeColor, removalTime, shouldObeyDepthTest);
        addCuboidQuads(pos, size, fillColor, removalTime, shouldObeyDepthTest);
    }

    private record Line(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color strokeColor, long removalTime) {

        public void create(BufferBuilder buffer, double cameraX, double cameraY, double cameraZ) {
            float r = strokeColor.getRed() / 255f;
            float g = strokeColor.getGreen() / 255f;
            float b = strokeColor.getBlue() / 255f;
            float a = strokeColor.getAlpha() / 255f;

            buffer.vertex(minX - cameraX, minY - cameraY, minZ - cameraZ).color(r, g, b, a).next();
            buffer.vertex(maxX - cameraX, maxY - cameraY, maxZ - cameraZ).color(r, g, b, a).next();
        }

    }
    
    private record Quad(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, Color fillColor, long removalTime) {
        
        public void create(BufferBuilder buffer, double cameraX, double cameraY, double cameraZ) {
            float r = fillColor.getRed() / 255f;
            float g = fillColor.getGreen() / 255f;
            float b = fillColor.getBlue() / 255f;
            float a = fillColor.getAlpha() / 255f;

            buffer.vertex(x1 - cameraX, y1 - cameraY, z1 - cameraZ).color(r, g, b, a).next();
            buffer.vertex(x2 - cameraX, y2 - cameraY, z2 - cameraZ).color(r, g, b, a).next();
            buffer.vertex(x3 - cameraX, y3 - cameraY, z3 - cameraZ).color(r, g, b, a).next();
            buffer.vertex(x4 - cameraX, y4 - cameraY, z4 - cameraZ).color(r, g, b, a).next();
        }
        
    }

}