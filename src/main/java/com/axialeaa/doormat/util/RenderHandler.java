package com.axialeaa.doormat.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.Iterator;

public class RenderHandler {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final HashSet<Line> LINES = new HashSet<>();
    private static final HashSet<Quad> QUADS = new HashSet<>();

    public synchronized static void renderWorld() {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        Camera camera = CLIENT.gameRenderer.getCamera();
        Vec3d cameraPos = camera.getPos();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        Iterator<Line> lineIterator = LINES.iterator();
        while (lineIterator.hasNext()) {
            Line line = lineIterator.next();
            line.render(buffer, cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
            if (System.currentTimeMillis() > line.removalTime)
                lineIterator.remove();
        }
        tessellator.draw();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Iterator<Quad> quadIterator = QUADS.iterator();
        while (quadIterator.hasNext()) {
            Quad quad = quadIterator.next();
            quad.render(buffer, cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
            if (System.currentTimeMillis() > quad.removalTime)
                quadIterator.remove();
        }
        tessellator.draw();
    }

    public synchronized static void addLine(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int strokeColor, long removalTime) {
        long l = System.currentTimeMillis() + removalTime;
        LINES.add(new Line(minX, minY, minZ, maxX, maxY, maxZ, strokeColor, l));
    }

    public synchronized static void addCuboidWireFrame(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int strokeColor, long removalTime) {
        long l = System.currentTimeMillis() + removalTime;
        LINES.add(new Line(minX, minY, minZ, maxX, minY, minZ, strokeColor, l));
        LINES.add(new Line(maxX, minY, minZ, maxX, maxY, minZ, strokeColor, l));
        LINES.add(new Line(maxX, maxY, minZ, minX, maxY, minZ, strokeColor, l));
        LINES.add(new Line(minX, maxY, minZ, minX, minY, minZ, strokeColor, l));

        LINES.add(new Line(minX, minY, minZ, minX, minY, maxZ, strokeColor, l));
        LINES.add(new Line(maxX, minY, minZ, maxX, minY, maxZ, strokeColor, l));
        LINES.add(new Line(minX, maxY, minZ, minX, maxY, maxZ, strokeColor, l));
        LINES.add(new Line(maxX, maxY, minZ, maxX, maxY, maxZ, strokeColor, l));

        LINES.add(new Line(minX, minY, maxZ, maxX, minY, maxZ, strokeColor, l));
        LINES.add(new Line(maxX, minY, maxZ, maxX, maxY, maxZ, strokeColor, l));
        LINES.add(new Line(maxX, maxY, maxZ, minX, maxY, maxZ, strokeColor, l));
        LINES.add(new Line(minX, maxY, maxZ, minX, minY, maxZ, strokeColor, l));
    }

    public static void addCuboidWireFrame(Vec3d pos, double size, int strokeColor, long removalTime) {
        addCuboidWireFrame(pos.getX() + size, pos.getY() + size, pos.getZ() + size, pos.getX() - size, pos.getY() - size, pos.getZ() - size, strokeColor, removalTime);
    }

    public synchronized static void addCuboidFaceFilled(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Direction direction, int fillColor, int strokeColor, long removalTime) {
        long l = System.currentTimeMillis() + removalTime;
        addCuboidWireFrame(minX, minY, minZ, maxX, maxY, maxZ, strokeColor, removalTime);
        switch (direction) {
            case NORTH -> QUADS.add(new Quad(
                minX, minY, minZ,
                minX, maxY, minZ,
                maxX, maxY, minZ,
                maxX, minY, minZ,
                fillColor, l
            )); // north side
            case SOUTH -> QUADS.add(new Quad(
                minX, minY, maxZ,
                maxX, minY, maxZ,
                maxX, maxY, maxZ,
                minX, maxY, maxZ,
                fillColor, l
            )); // south side
            case WEST -> QUADS.add(new Quad(
                minX, minY, minZ,
                minX, minY, maxZ,
                minX, maxY, maxZ,
                minX, maxY, minZ,
                fillColor, l
            )); // west side
            case EAST -> QUADS.add(new Quad(
                maxX, minY, minZ,
                maxX, maxY, minZ,
                maxX, maxY, maxZ,
                maxX, minY, maxZ,
                fillColor, l
            )); // east side
            case DOWN -> QUADS.add(new Quad(
                minX, minY, minZ,
                maxX, minY, minZ,
                maxX, minY, maxZ,
                minX, minY, maxZ,
                fillColor, l
            )); // down side
            case UP -> QUADS.add(new Quad(
                minX, maxY, minZ,
                minX, maxY, maxZ,
                maxX, maxY, maxZ,
                maxX, maxY, minZ,
                fillColor, l
            )); // up side
        }
    }

    public static void addCuboidFaceFilled(Vec3d pos, Direction direction, int fillColor, int strokeColor, long removalTime) {
        addCuboidFaceFilled(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, direction, fillColor, strokeColor, removalTime);
    }

    public synchronized static void addCuboidFilled(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int fillColor, int strokeColor, long removalTime) {
        long l = System.currentTimeMillis() + removalTime;
        addCuboidWireFrame(minX, minY, minZ, maxX, maxY, maxZ, strokeColor, removalTime);
        QUADS.add(new Quad(
            minX, minY, minZ,
            maxX, minY, minZ,
            maxX, maxY, minZ,
            minX, maxY, minZ,
            fillColor, l
        )); // north side
        QUADS.add(new Quad(
            minX, minY, maxZ,
            minX, maxY, maxZ,
            maxX, maxY, maxZ,
            maxX, minY, maxZ,
            fillColor, l
        )); // south side
        QUADS.add(new Quad(
            minX, minY, minZ,
            minX, maxY, minZ,
            minX, maxY, maxZ,
            minX, minY, maxZ,
            fillColor, l
        )); // west side
        QUADS.add(new Quad(
            maxX, minY, minZ,
            maxX, minY, maxZ,
            maxX, maxY, maxZ,
            maxX, maxY, minZ,
            fillColor, l
        )); // east side
        QUADS.add(new Quad(
            minX, minY, minZ,
            minX, minY, maxZ,
            maxX, minY, maxZ,
            maxX, minY, minZ,
            fillColor, l
        )); // down side
        QUADS.add(new Quad(
            minX, maxY, minZ,
            maxX, maxY, minZ,
            maxX, maxY, maxZ,
            minX, maxY, maxZ,
            fillColor, l
        )); // up side
    }

    public static void addCuboidFilled(Vec3d pos, double size, int fillColor, int strokeColor, long removalTime) {
        addCuboidFilled(pos.getX() + size, pos.getY() + size, pos.getZ() + size, pos.getX() - size, pos.getY() - size, pos.getZ() - size, fillColor, strokeColor, removalTime);
    }

    private record Line(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int strokeColor, long removalTime) {

        public void render(BufferBuilder buffer, double cameraX, double cameraY, double cameraZ) {
            float a = ((strokeColor & 0xFF000000) >>> 24) / 255F;
            float r = ((strokeColor & 0x00FF0000) >>> 16) / 255F;
            float g = ((strokeColor & 0x0000FF00) >>>  8) / 255F;
            float b = ((strokeColor & 0x000000FF)       ) / 255F;

            buffer.vertex(minX - cameraX, minY - cameraY, minZ - cameraZ).color(r, g, b, a).next();
            buffer.vertex(maxX - cameraX, maxY - cameraY, maxZ - cameraZ).color(r, g, b, a).next();
        }

    }
    
    private record Quad(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, int fillColor, long removalTime) {
        
        public void render(BufferBuilder buffer, double cameraX, double cameraY, double cameraZ) {
            float a = ((fillColor & 0xFF000000) >>> 24) / 255F;
            float r = ((fillColor & 0x00FF0000) >>> 16) / 255F;
            float g = ((fillColor & 0x0000FF00) >>>  8) / 255F;
            float b = ((fillColor & 0x000000FF)       ) / 255F;

            buffer.vertex(x1 - cameraX, y1 - cameraY, z1 - cameraZ).color(r, g, b, a).next();
            buffer.vertex(x2 - cameraX, y2 - cameraY, z2 - cameraZ).color(r, g, b, a).next();
            buffer.vertex(x3 - cameraX, y3 - cameraY, z3 - cameraZ).color(r, g, b, a).next();
            buffer.vertex(x4 - cameraX, y4 - cameraY, z4 - cameraZ).color(r, g, b, a).next();
        }
        
    }

}