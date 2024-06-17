package com.axialeaa.doormat.util;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

@Deprecated(forRemoval = true)
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

    public static Color getTrubetskoyColor(String key) {
        if (!TRUBETSKOY_COLORS.containsKey(key))
            throw new NullPointerException("No Trubetskoy color of key " + key + "was found.");

        Color color = TRUBETSKOY_COLORS.get(key);

        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
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

        RenderSystem.disableDepthTest();

        Iterator<Line> lineIterator = LINES.iterator();

        while (lineIterator.hasNext()) {
            Line line = lineIterator.next();
            line.create(tessellator, cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
            if (WORLD == null || WORLD.getTime() >= line.removalTime)
                lineIterator.remove();
        }

        Iterator<Quad> quadIterator = QUADS.iterator();

        while (quadIterator.hasNext()) {
            Quad quad = quadIterator.next();
            quad.create(tessellator, cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
            if (WORLD == null || WORLD.getTime() >= quad.removalTime)
                quadIterator.remove();
        }

        RenderSystem.enableDepthTest();

        Iterator<Line> depthLineIterator = DEPTH_LINES.iterator();
        
        while (depthLineIterator.hasNext()) {
            Line line = depthLineIterator.next();
            line.create(tessellator, cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
            if (WORLD == null || WORLD.getTime() >= line.removalTime)
                depthLineIterator.remove();
        }

        Iterator<Quad> depthQuadIterator = DEPTH_QUADS.iterator();

        while (depthQuadIterator.hasNext()) {
            Quad quad = depthQuadIterator.next();
            quad.create(tessellator, cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
            if (WORLD == null || WORLD.getTime() >= quad.removalTime)
                depthQuadIterator.remove();
        }
    }

    public synchronized static void addLine(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color strokeColor, long removalTime, boolean shouldRenderBehindBlocks) {
        if (WORLD == null || strokeColor.getAlpha() == 0)
            return;

        long l = WORLD.getTime() + removalTime;
        HashSet<Line> lines = shouldRenderBehindBlocks ? DEPTH_LINES : LINES;

        lines.add(new Line(minX, minY, minZ, maxX, maxY, maxZ, strokeColor, l));
    }

    public static void addCuboidLines(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color strokeColor, long removalTime, boolean shouldRenderBehindBlocks) {
        addLine(minX, minY, minZ, maxX, minY, minZ, strokeColor, removalTime, shouldRenderBehindBlocks);
        addLine(maxX, minY, minZ, maxX, maxY, minZ, strokeColor, removalTime, shouldRenderBehindBlocks);
        addLine(maxX, maxY, minZ, minX, maxY, minZ, strokeColor, removalTime, shouldRenderBehindBlocks);
        addLine(minX, maxY, minZ, minX, minY, minZ, strokeColor, removalTime, shouldRenderBehindBlocks);

        addLine(minX, minY, minZ, minX, minY, maxZ, strokeColor, removalTime, shouldRenderBehindBlocks);
        addLine(maxX, minY, minZ, maxX, minY, maxZ, strokeColor, removalTime, shouldRenderBehindBlocks);
        addLine(minX, maxY, minZ, minX, maxY, maxZ, strokeColor, removalTime, shouldRenderBehindBlocks);
        addLine(maxX, maxY, minZ, maxX, maxY, maxZ, strokeColor, removalTime, shouldRenderBehindBlocks);

        addLine(minX, minY, maxZ, maxX, minY, maxZ, strokeColor, removalTime, shouldRenderBehindBlocks);
        addLine(maxX, minY, maxZ, maxX, maxY, maxZ, strokeColor, removalTime, shouldRenderBehindBlocks);
        addLine(maxX, maxY, maxZ, minX, maxY, maxZ, strokeColor, removalTime, shouldRenderBehindBlocks);
        addLine(minX, maxY, maxZ, minX, minY, maxZ, strokeColor, removalTime, shouldRenderBehindBlocks);
    }

    public static void addCuboidLines(Vec3d pos, double size, Color strokeColor, long removalTime, boolean shouldRenderBehindBlocks) {
        addCuboidLines(pos.getX() + size, pos.getY() + size, pos.getZ() + size, pos.getX() - size, pos.getY() - size, pos.getZ() - size, strokeColor, removalTime, shouldRenderBehindBlocks);
    }

    public synchronized static void addCuboidFaceQuad(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color fillColor, long removalTime, boolean shouldRenderBehindBlocks, Direction direction) {
        if (WORLD == null || fillColor.getAlpha() == 0)
            return;

        long l = WORLD.getTime() + removalTime;
        HashSet<Quad> quads = shouldRenderBehindBlocks ? DEPTH_QUADS : QUADS;

        quads.add(switch (direction) {
            case NORTH -> new Quad(
                minX, minY, minZ,
                maxX, minY, minZ,
                maxX, maxY, minZ,
                minX, maxY, minZ,
                fillColor, l
            );
            case SOUTH -> new Quad(
                minX, minY, maxZ,
                minX, maxY, maxZ,
                maxX, maxY, maxZ,
                maxX, minY, maxZ,
                fillColor, l
            );
            case WEST -> new Quad(
                minX, minY, minZ,
                minX, maxY, minZ,
                minX, maxY, maxZ,
                minX, minY, maxZ,
                fillColor, l
            );
            case EAST -> new Quad(
                maxX, minY, minZ,
                maxX, minY, maxZ,
                maxX, maxY, maxZ,
                maxX, maxY, minZ,
                fillColor, l
            );
            case DOWN -> new Quad(
                minX, minY, minZ,
                minX, minY, maxZ,
                maxX, minY, maxZ,
                maxX, minY, minZ,
                fillColor, l
            );
            case UP -> new Quad(
                minX, maxY, minZ,
                maxX, maxY, minZ,
                maxX, maxY, maxZ,
                minX, maxY, maxZ,
                fillColor, l
            );
        });
    }

    public static void addCuboidFaceQuad(Vec3d pos, double size, Color fillColor, long removalTime, boolean shouldRenderBehindBlocks, Direction direction) {
        addCuboidFaceQuad(pos.getX() + size, pos.getY() + size, pos.getZ() + size, pos.getX() - size, pos.getY() - size, pos.getZ() - size, fillColor, removalTime, shouldRenderBehindBlocks, direction);
    }

    public synchronized static void addCuboidQuads(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color fillColor, long removalTime, boolean shouldRenderBehindBlocks) {
        if (WORLD == null || fillColor.getAlpha() == 0)
            return;

        long l = WORLD.getTime() + removalTime;
        HashSet<Quad> quads = shouldRenderBehindBlocks ? DEPTH_QUADS : QUADS;

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
    }

    public static void addCuboidQuads(Vec3d pos, double size, Color fillColor, long removalTime, boolean shouldRenderBehindBlocks) {
        addCuboidQuads(pos.getX() + size, pos.getY() + size, pos.getZ() + size, pos.getX() - size, pos.getY() - size, pos.getZ() - size, fillColor, removalTime, shouldRenderBehindBlocks);
    }

    public static void addCuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color fillColor, Color strokeColor, long removalTime, boolean shouldRenderBehindBlocks) {
        addCuboidLines(minX, minY, minZ, maxX, maxY, maxZ, strokeColor, removalTime, shouldRenderBehindBlocks);
        addCuboidQuads(minX, minY, minZ, maxX, maxY, maxZ, fillColor, removalTime, shouldRenderBehindBlocks);
    }

    public static void addCuboid(Vec3d pos, double size, Color fillColor, Color strokeColor, long removalTime, boolean shouldRenderBehindBlocks) {
        addCuboidLines(pos, size, strokeColor, removalTime, shouldRenderBehindBlocks);
        addCuboidQuads(pos, size, fillColor, removalTime, shouldRenderBehindBlocks);
    }

    private record Line(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color strokeColor, long removalTime) {

        public void create(Tessellator tessellator, double cameraX, double cameraY, double cameraZ) {
            float r = strokeColor.getRed() / 255f;
            float g = strokeColor.getGreen() / 255f;
            float b = strokeColor.getBlue() / 255f;
            float a = strokeColor.getAlpha() / 255f;

            BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

            buffer.vertex((float) (minX - cameraX), (float) (minY - cameraY), (float) (minZ - cameraZ)).color(r, g, b, a);
            buffer.vertex((float) (maxX - cameraX), (float) (maxY - cameraY), (float) (maxZ - cameraZ)).color(r, g, b, a);

            BufferRenderer.drawWithGlobalProgram(buffer.end());
        }

    }
    
    private record Quad(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, Color fillColor, long removalTime) {
        
        public void create(Tessellator tessellator, double cameraX, double cameraY, double cameraZ) {
            float r = fillColor.getRed() / 255f;
            float g = fillColor.getGreen() / 255f;
            float b = fillColor.getBlue() / 255f;
            float a = fillColor.getAlpha() / 255f;

            BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            buffer.vertex((float) (x1 - cameraX), (float) (y1 - cameraY), (float) (z1 - cameraZ)).color(r, g, b, a);
            buffer.vertex((float) (x2 - cameraX), (float) (y2 - cameraY), (float) (z2 - cameraZ)).color(r, g, b, a);
            buffer.vertex((float) (x3 - cameraX), (float) (y3 - cameraY), (float) (z3 - cameraZ)).color(r, g, b, a);
            buffer.vertex((float) (x4 - cameraX), (float) (y4 - cameraY), (float) (z4 - cameraZ)).color(r, g, b, a);

            BufferRenderer.drawWithGlobalProgram(buffer.end());
        }
        
    }

}