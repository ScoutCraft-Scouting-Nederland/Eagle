package nl.scoutcraft.eagle.server.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.server.data.EagleKeys;
import nl.scoutcraft.eagle.server.locale.IMessage;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ItemBuilder {

    private final ItemStack is;
    private ItemMeta im;

    @Nullable private IMessage name;
    @Nullable private IMessage lore;

    public ItemBuilder(Material mat) {
        this(new ItemStack(mat, 1));
    }

    public ItemBuilder(Material mat, int amount) {
        this(new ItemStack(mat, amount));
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
        this.im = is.getItemMeta();
        this.name = null;
        this.lore = null;
    }

    public ItemBuilder type(Material type) {
        is.setItemMeta(im);
        is.setType(type);
        im = is.getItemMeta();
        return this;
    }

    public Material type() {
        return is.getType();
    }

    public ItemBuilder amount(int amount) {
        is.setAmount(amount);
        return this;
    }

    public int amount() {
        return is.getAmount();
    }

    public ItemBuilder durability(int damage) {
        if (im instanceof Damageable dm)
            dm.setDamage(damage);
        return this;
    }

    @Deprecated
    public ItemBuilder name(String name) {
        im.displayName(TextUtils.colorize(name));
        return this;
    }

    public ItemBuilder name(Component name) {
        im.displayName(name);
        return this;
    }

    public ItemBuilder name(@Nullable IMessage name) {
        this.name = name;
        return this;
    }

    public ItemBuilder lore(List<Component> lore) {
        if (lore.size() == 1 && TextUtils.toString(lore.get(0)).isEmpty())
            lore.clear();

        im.lore(lore);
        return this;
    }

    public ItemBuilder lore(String... loreStrings) {
        List<Component> lore = new ArrayList<>();

        for (String str : loreStrings)
            for (String split : str.split("\n"))
                lore.add(TextUtils.colorize(split));

        return this.lore(lore);
    }

    public ItemBuilder lore(Component... lore) {
        return this.lore(Lists.newArrayList(lore));
    }

    public ItemBuilder lore(@Nullable IMessage lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder customModel(Integer value) {
        this.im.setCustomModelData(value);
        return this;
    }

    public ItemBuilder unbreakable(boolean value) {
        im.setUnbreakable(value);
        return this;
    }

    public ItemBuilder enchant(Enchantment ench, int level) {
        im.addEnchant(ench, level, true);
        return this;
    }

    public ItemBuilder armorColor(Color color) {
        if (im instanceof LeatherArmorMeta lam)
            lam.setColor(color);
        return this;
    }

    public ItemBuilder skull(UUID uuid) {
        if (im instanceof SkullMeta sm)
            sm.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        return this;
    }

    public ItemBuilder skull(PlayerProfile profile) {
        if (im instanceof SkullMeta sm)
            sm.setPlayerProfile(profile);
        return this;
    }

    public ItemBuilder skull(String texture) {
        return this.skull(UUID.randomUUID(), texture);
    }

    public ItemBuilder skull(UUID uuid, String texture) {
        if (im instanceof SkullMeta sm) {
            PlayerProfile profile = Bukkit.createProfile(uuid);
            profile.getProperties().add(new ProfileProperty("textures", texture));
            sm.setPlayerProfile(profile);
        }
        return this;
    }

    @Deprecated
    public ItemBuilder bookTitle(String title) {
        return this.bookTitle(TextUtils.colorize(title));
    }

    public ItemBuilder bookTitle(Component title) {
        if (im instanceof BookMeta bm)
            bm.title(title);
        return this;
    }

    @Deprecated
    public ItemBuilder bookAuthor(String author) {
        return this.bookAuthor(TextUtils.colorize(author));
    }

    public ItemBuilder bookAuthor(Component author) {
        if (im instanceof BookMeta bm)
            bm.author(author);
        return this;
    }

    public ItemBuilder bookGeneration(BookMeta.Generation gen) {
        if (im instanceof BookMeta bm)
            bm.setGeneration(gen);
        return this;
    }

    @Deprecated
    public ItemBuilder bookPages(String... pages) {
        return this.bookPages(Arrays.stream(pages).map(TextUtils::colorize).collect(Collectors.toList()));
    }

    public ItemBuilder bookPages(Component... pages) {
        if (im instanceof BookMeta bm)
            bm.pages(pages);
        return this;
    }

    public ItemBuilder bookPages(List<Component> pages) {
        if (im instanceof BookMeta bm)
            bm.pages(pages);
        return this;
    }

    public ItemBuilder compassLodestoneTracked(boolean value) {
        if (im instanceof CompassMeta cm)
            cm.setLodestoneTracked(value);
        return this;
    }

    public ItemBuilder compassLoadstoneTarget(@Nullable Location loc) {
        if (im instanceof CompassMeta cm)
            cm.setLodestone(loc);
        return this;
    }

    public ItemBuilder potionBase(PotionData data) {
        if (im instanceof PotionMeta pm)
            pm.setBasePotionData(data);
        return this;
    }

    public ItemBuilder potionColor(Color color) {
        if (im instanceof PotionMeta pm)
            pm.setColor(color);
        return this;
    }

    public ItemBuilder potionEffect(PotionEffect effect, boolean overwrite) {
        if (im instanceof PotionMeta pm)
            pm.addCustomEffect(effect, overwrite);
        return this;
    }

    public ItemBuilder potionEffects(List<PotionEffect> effects) {
        if (im instanceof PotionMeta pm) {
            pm.clearCustomEffects();
            effects.forEach(effect -> pm.addCustomEffect(effect, false));
        }
        return this;
    }

    public ItemBuilder crossbowProjectile(ItemStack item) {
        if (im instanceof CrossbowMeta cm)
            cm.addChargedProjectile(item);
        return this;
    }

    public ItemBuilder crossbowProjectiles(List<ItemStack> items) {
        if (im instanceof CrossbowMeta cm)
            cm.setChargedProjectiles(items);
        return this;
    }

    public <T> ItemBuilder data(NamespacedKey key, PersistentDataType<T, T> type, T value) {
        im.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    public ItemBuilder hideUnbreakable(boolean value) {
        return this.setFlag(ItemFlag.HIDE_UNBREAKABLE, value);
    }

    public ItemBuilder hideEnchants(boolean value) {
        return this.setFlag(ItemFlag.HIDE_ENCHANTS, value);
    }

    public ItemBuilder hideDye(boolean value) {
        return this.setFlag(ItemFlag.HIDE_DYE, value);
    }

    public ItemBuilder hideAttributes(boolean value) {
        return this.setFlag(ItemFlag.HIDE_ATTRIBUTES, value);
    }

    public ItemBuilder hidePotionEffects(boolean value) {
        return this.setFlag(ItemFlag.HIDE_POTION_EFFECTS, value);
    }

    public ItemBuilder hidePlacedOn(boolean value) {
        return this.setFlag(ItemFlag.HIDE_PLACED_ON, value);
    }

    public ItemBuilder hideDestroys(boolean value) {
        return this.setFlag(ItemFlag.HIDE_DESTROYS, value);
    }

    private ItemBuilder setFlag(ItemFlag flag, boolean value) {
        if (value) {
            im.addItemFlags(flag);
        } else {
            im.removeItemFlags(flag);
        }
        return this;
    }

    public ItemStack build() {
        return build((Locale) null);
    }

    public ItemStack build(@Nullable Player player) {
        return build(player == null ? null : player.getPersistentDataContainer().get(EagleKeys.LOCALE, EagleKeys.LOCALE_TAG_TYPE));
    }

    public ItemStack build(@Nullable Locale locale) {
        if (this.name != null)
            this.name(this.name.get(locale));

        if (this.lore != null)
            this.lore(this.lore.get(locale));

        this.is.setItemMeta(this.im);
        return this.is;
    }

    public ItemBuilder copy() {
        return new ItemBuilder(this.build().clone());
    }
}
