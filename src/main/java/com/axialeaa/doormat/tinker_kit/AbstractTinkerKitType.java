package com.axialeaa.doormat.tinker_kit;

public abstract class AbstractTinkerKitType<T> {

//    private static final Map<Block, Entry> DEFAULT_VALUES = new HashMap<>();
//
//    private static final Map<Block, Entry> TRANSIENT_VALUES = new HashMap<>();
//
//    public abstract String getName();
//
//    public abstract String getCommandRule();
//
//    public abstract @Nullable T getDefaultValue(Entry entry);
//
//    public abstract @Nullable T getTransientValue(Entry entry);
//
//    private static Entry getDefaultMap(Block block) {
//        return DEFAULT_VALUES.get(block);
//    }
//
//    private static Entry getTransientValues(Block block) {
//        return TRANSIENT_VALUES.get(block);
//    }
//
//    /**
//     * @return true if this rule entries has support for the component.
//     */
//    public boolean canModify(Block block) {
//        if (isBarrelUnmodifiable(block) || (this instanceof TickPriorityType && !hasDelay(block)))
//            return false;
//
//        return DEFAULT_VALUES.containsKey(block) && getDefaultValue(getDefaultMap(block)) != null;
//    }
//
//    private static boolean isBarrelUnmodifiable(Block block) {
//        return !DoormatSettings.redstoneOpensBarrels && block instanceof BarrelBlock;
//    }
//
//    private static boolean hasDelay(Block block) {
//        return TinkerKit.Type.DELAY.getValues(block).containsKey(block) && (int) TinkerKit.Type.DELAY.getValue(block) > 0;
//    }
//
//    /**
//     * @return true if the map for this rule entries has been modified.
//     */
//    public boolean hasBeenModified() {
//        for (Block block : this.getBlocks()) {
//            if (!this.isDefaultValue(block))
//                return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * Converts a list of all modifiable blocks (by <code>entries</code>) into a list of their keys, sorts them alphabetically, and then re-interprets the blocks from the keys.
//     * @return a sorted list of blocks ordered alphabetically by their keys.
//     */
//    public List<Block> getBlocks() {
//        List<String> strings = new ArrayList<>(Registries.BLOCK.stream().filter(this::canModify).map(TinkerKit::getKey).toList());
//        Collections.sort(strings);
//
//        Stream<String> stream = Arrays.stream(strings.toArray(String[]::new));
//        Stream<Block> mapped = stream.map(key -> Registries.BLOCK.get(Identifier.tryParse(key)));
//
//        return mapped.toList();
//    }
//
//    /**
//     * Converts a list of all modifiable blocks (by <code>entries</code>) into a list of their keys and sorts them alphabetically.
//     * @return a sorted array of all modifiable blocks' keys (by <code>entries</code>), used for command autocompletion.
//     */
//    public String[] getBlockKeys() {
//        List<String> strings = new ArrayList<>(Registries.BLOCK.stream().filter(this::canModify).map(TinkerKit::getKey).toList());
//        Collections.sort(strings);
//
//        return strings.toArray(String[]::new);
//    }
//
//    private Entry getValues(Block block) {
//        Map<Block, Object> map = new HashMap<>();
//        Entry defaults = getDefaultMap(block);
//
//        if (!DEFAULT_VALUES.containsKey(block) || !defaults.containsKey(block))
//            return Map.copyOf(map);
//
//        map.putAll(defaults);
//        Map<Block, Object> transients = this.getTransientValues();
//
//        if (!TinkerKit.TRANSIENT_VALUES.containsKey(this) || !transients.containsKey(block))
//            return Map.copyOf(map);
//
//        map.putAll(transients);
//
//        return Map.copyOf(map);
//    }
//
//    /**
//     * @param block the block to get the modified value of.
//     * @return the default value assigned to the <code>block</code>.
//     * @apiNote This requires you cast the return value depending on the {@link TinkerKit.Type}.
//     */
//    public Object getValue(Block block) {
//        return this.getValues(block).(block);
//    }
//
//    /**
//     * @param block the block to get the default value of.
//     * @return the default value assigned to the <code>block</code>.
//     * @throws NullPointerException if no value can be found for the <code>block</code>.
//     * @apiNote This requires you cast the return value depending on the {@link TinkerKit.Type}.
//     */
//    public @Nullable Object getDefaultValue(Block block) {
//        return TinkerKit.DEFAULT_VALUES.containsKey(this) ? this.getDefaultMap().get(block) : null;
//    }
//
//    /**
//     * @param block the block to get the modified value of.
//     * @return the default value assigned to the <code>block</code>.
//     * @apiNote This requires you cast the return value depending on the {@link TinkerKit.Type}.
//     */
//    @Nullable Object getTransientValue(Block block) {
//        return TinkerKit.TRANSIENT_VALUES.containsKey(this) ? this.getTransientValues().get(block) : null;
//    }
//
//    /**
//     * @return true if the map value assigned to this component is the default.
//     */
//    public boolean isDefaultValue(Block block) {
//        return Objects.equals(this.getValue(block), this.getDefaultValue(block));
//    }
//
//    public void set(Block block, Object value) {
//        if (!this.canModify(block))
//            throw new IllegalArgumentException(String.format("Failed to set %s to a new %s value: %s!", TinkerKit.getTranslatedName(block), this.getName(), value));
//
//        if (!TinkerKit.TRANSIENT_VALUES.containsKey(this)) {
//            TinkerKit.TRANSIENT_VALUES.put(this, new HashMap<>(Map.of(block, value)));
//            return;
//        }
//
//        Map<Block, Object> transientValues = this.getTransientValues();
//        Map<Block, Object> map = new HashMap<>(transientValues);
//        map.put(block, value);
//
//        transientValues.putAll(map);
//    }
//
//    public void reset(Block block) {
//        if (!TinkerKit.TRANSIENT_VALUES.containsKey(this)) {
//            DoormatServer.LOGGER.error("transient values does not contain block {}", block);
//            return;
//        }
//
//        Map<Block, Object> transients = this.getTransientValues();
//        DoormatServer.LOGGER.error("found transient values for block {}", block);
//
//        if (!transients.containsKey(block)) {
//            DoormatServer.LOGGER.error("{} transient value does not contain value of entries {}", block, this);
//            return;
//        }
//
//        try {
//            transients.remove(block);
//            DoormatServer.LOGGER.info("removed {} value from block {}", this, block);
//        }
//        catch (Exception e) {
//            DoormatServer.LOGGER.error("removed {} value from block {}", this, block, e);
//        }
//
//        if (transients.isEmpty()) {
//            DoormatServer.LOGGER.warn("transient values for block {} is empty", block);
//            TinkerKit.TRANSIENT_VALUES.remove(this);
//        }
//    }

}
