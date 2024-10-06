package com.axialeaa.doormat.tinker_kit;

public interface TinkerKitInitializer {

    /**
     * Registers new instances of {@link TinkerType} before registering the blocks that may or may not use them. You can leave this method empty if you haven't created any new types, otherwise calling a simple "noop" method in a static class containing TinkerType fields should suffice.
     */
    void registerTypes();

    void registerBlocks();

    void registerModificationPredicates();

}
