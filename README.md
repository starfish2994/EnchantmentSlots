# 🪄 Welcome to EnchantmentSlots

> **EnchantmentSlots** help you add enchantment slot in your server.

---

## 📘 Packet-Based Enchantment Slot System

**EnchantmentSlots** is a **packet-based plugin** that adds customizable **enchantment slot limits** to items.  
Unlike similar plugins, **EnchantmentSlots never actually edits your item lore**.  
That means:
- If you uninstall the plugin, **nothing will be permanently changed**.
- All items remain **exactly as they were**.
- It's as if the plugin was never installed at all.

If desired, you can even configure the plugin to **not add any NBT data** to items.  
NBT is only used for caching slot data to improve performance and avoid recalculating each time.

### ✨ Real-Time Detection and Synchronization
Thanks to its packet-based design, all enchantment changes are updated **instantly in real time**.  
No delay, no missed enchantment detection like traditional plugins.

In addition, EnchantmentSlots works **perfectly with non-packet-based item plugins**.  
Other plugins will not detect EnchantmentSlots’ hidden data, so **compatibility is guaranteed**.

---

## ⚙️ Custom Default Enchantment Slots

You can:
- Assign **different default slot amounts** for different items.
- Give **different players** unique default slot settings (based on permissions or conditions).
- Fully support **third-party item systems** such as **MMOItems tiers** or custom plugin items.

### 🔗 Third-Party Enchantment Support
Fully compatible with:
- **EcoEnchants**
- **ExcellentEnchants**
- **MythicEnchants**
- **Aiyatsbus**

> ⚠️ Note:  
> AdvancedEnchantments and EpicEnchants are not true enchantment systems; therefore, EnchantmentSlots cannot support them.

---

## 🪶 Custom Enchantment Slot Lore Display

EnchantmentSlots provides **powerful item placeholders** for displaying enchantment data.  
You can show:
- Enchantment names and levels
- Total slot amount and used slots
- Remaining slot amount

These placeholders also work in **other item plugins** (e.g., item display, GUI, or custom lore systems).

When displaying enchantments from other plugins, EnchantmentSlots will automatically **inherit their original color and rarity styles**.

You can configure:
- Fuzzy match rules (e.g., hide lore display for specific names or lore patterns)
- Specific item types (including third-party items) to hide slot display
- Full customization of **what to show**, **where to show**, and **how to show it**

> "What it shows, where it shows, and how it shows — it’s all up to you."

---

## 🧭 Enchantment Slot Check System

EnchantmentSlots automatically monitors and controls enchantment limits under various conditions:
- Vanilla enchanting table
- Anvil merging
- EnchantGUI plugin
- Player-item interactions

If an item exceeds its allowed enchantment slots:
- Enchanting will be **canceled automatically** (for enchanting table, anvil, or EnchantGUI).
- During interactions, **extra enchantments will be automatically removed**.

Every time an item’s enchantment state changes, the plugin **rechecks it instantly**, ensuring the system stays consistent.

> Trying to glitch extra enchantments? Impossible — they’ll be automatically cleaned up.

---

## 🔮 Slot Expansion System

Players can increase their item’s enchantment slot capacity using **Slot Expanders** (special items).

You can configure:
- Added slot amount
- Success chance
- Applicable item types
- Actions for success and failure
- Maximum slot expansion per item and per player

Custom textures with **CustomModelData** are supported as well.

---

## 🧩 Feature Overview

- 📦 **Packet-based system** — lore only exists on the client, server data remains clean.
- ⚙️ **Real-time synchronization** — enchantment changes apply instantly.
- 🎨 **Creative mode compatible** — works in all game modes.
- 🔄 **Automatic lore update** — supports old items (even before plugin installation).
- 🧠 **Condition system** — rank-based or permission-based default slot logic.
- 🧱 **MMOItems and third-party item plugin support.**
- 🧿 **EnchantGUI plugin compatibility.**
- 💥 **Auto-removal of illegal enchantments.**
- 🎯 **PlaceholderAPI support** — display current and max slot amounts easily.
- 📑 **Automatic enchantment display sorting** (EcoEnchants v11+ / ExcellentEnchants v4+).
- ⚒️ **Smithing table sync** — updates enchantment slots when item material changes.
- 🎲 **Random slot generation** — items can spawn with random slot amounts.
- 🧷 **Custom enchantment slot cost** — strong enchantments can use more slots, curses can use none.


---

### ❤️ EnchantmentSlots - A lightweight enchantment slot system.  
It works seamlessly with any item plugin and is perfect for RPG, survival, or custom gameplay servers.

> *Efficient. Clean. Compatible.*  
> *The enchantment slot system your high-version server deserves.*

Consider respect my work and buy the plugin here, you can get free support, subbmit suggestion service. [Click to buy](https://www.spigotmc.org/resources/enchantmentslots-add-enchantment-slot-feature-to-your-server-1-17-1-20.113048/)