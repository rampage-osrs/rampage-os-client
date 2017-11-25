package com.rampage.client.cache.graphics;

import com.brutality.client.cache.graphics.RSInterface;
import com.brutality.client.cache.graphics.TextDrawingArea;
import com.rampage.client.Client;
import com.rampage.client.Interfaces;
import com.rampage.client.RSMenuItem;
import com.rampage.client.Sprite;
import com.rampage.client.Stream;
import com.rampage.client.TextClass;
import com.rampage.client.cache.StreamLoader;
import com.rampage.client.cache.anim.Class36;
import com.rampage.client.cache.def.EntityDef;
import com.rampage.client.cache.def.ItemDef;
import com.rampage.client.entity.model.Model;
import com.rampage.client.link.MRUNodes;

public class RSInterface {
	
	public static boolean getString = false;

	public static final int OPTION_OK = 1;
	public static final int OPTION_USABLE = 2;
	public static final int OPTION_CLOSE = 3;
	public static final int OPTION_TOGGLE_SETTING = 4;
	public static final int OPTION_RESET_SETTING = 5;
	public static final int OPTION_CONTINUE = 6;
	public static final int OPTION_SLIDER = 7;

	public static final int TYPE_CONTAINER = 0;
	public static final int TYPE_MODEL_LIST = 1;
	public static final int TYPE_INVENTORY = 2;
	public static final int TYPE_RECTANGLE = 3;
	public static final int TYPE_TEXT = 4;
	public static final int TYPE_SPRITE = 5;
	public static final int TYPE_MODEL = 6;
	public static final int TYPE_ITEM_LIST = 7;

	public void swapInventoryItems(int i, int j) {
		int k = inventoryItemId[i];
		inventoryItemId[i] = inventoryItemId[j];
		inventoryItemId[j] = k;
		k = inventoryAmounts[i];
		inventoryAmounts[i] = inventoryAmounts[j];
		inventoryAmounts[j] = k;
	}

	public static void unpack(StreamLoader streamLoader,
			TextDrawingArea textDrawingAreas[], StreamLoader streamLoader_1) {
		aMRUNodes_238 = new MRUNodes(50000);
		Stream stream = new Stream(streamLoader.getDataForName("data"));
		int i = -1;
		int j = stream.readUnsignedWord();
		interfaceCache = new RSInterface[j + 60000];
		while (stream.currentOffset < stream.buffer.length) {
			int k = stream.readUnsignedWord();
			if (k == 65535) {
				i = stream.readUnsignedWord();
				k = stream.readUnsignedWord();
			}
			RSInterface rsInterface = interfaceCache[k] = new RSInterface();
			rsInterface.id = k;
			rsInterface.parentID = i;
			rsInterface.type = stream.readUnsignedByte();
			rsInterface.atActionType = stream.readUnsignedByte();
			rsInterface.contentType = stream.readUnsignedWord();
			rsInterface.width = stream.readUnsignedWord();
			rsInterface.height = stream.readUnsignedWord();
			rsInterface.aByte254 = (byte) stream.readUnsignedByte();
			rsInterface.mOverInterToTrigger = stream.readUnsignedByte();
			if (rsInterface.mOverInterToTrigger != 0)
				rsInterface.mOverInterToTrigger = (rsInterface.mOverInterToTrigger - 1 << 8)
						+ stream.readUnsignedByte();
			else
				rsInterface.mOverInterToTrigger = -1;
			int i1 = stream.readUnsignedByte();
			if (i1 > 0) {
				rsInterface.anIntArray245 = new int[i1];
				rsInterface.anIntArray212 = new int[i1];
				for (int j1 = 0; j1 < i1; j1++) {
					rsInterface.anIntArray245[j1] = stream.readUnsignedByte();
					rsInterface.anIntArray212[j1] = stream.readUnsignedWord();
				}

			}
			int k1 = stream.readUnsignedByte();
			if (k1 > 0) {
				rsInterface.valueIndexArray = new int[k1][];
				for (int l1 = 0; l1 < k1; l1++) {
					int i3 = stream.readUnsignedWord();
					rsInterface.valueIndexArray[l1] = new int[i3];
					for (int l4 = 0; l4 < i3; l4++)
						rsInterface.valueIndexArray[l1][l4] = stream
								.readUnsignedWord();

				}

			}
			if (rsInterface.type == TYPE_CONTAINER) {
				rsInterface.drawsTransparent = false;
				rsInterface.scrollMax = stream.readUnsignedWord();
				rsInterface.isMouseoverTriggered = stream.readUnsignedByte() == 1;
				int i2 = stream.readUnsignedWord();
				rsInterface.children = new int[i2];
				rsInterface.childX = new int[i2];
				rsInterface.childY = new int[i2];
				for (int j3 = 0; j3 < i2; j3++) {
					rsInterface.children[j3] = stream.readUnsignedWord();
					rsInterface.childX[j3] = stream.readSignedWord();
					rsInterface.childY[j3] = stream.readSignedWord();
				}
			}
			if (rsInterface.type == TYPE_MODEL_LIST) {
				stream.readUnsignedWord();
				stream.readUnsignedByte();
			}
			if (rsInterface.type == TYPE_INVENTORY) {
				rsInterface.inventoryItemId = new int[rsInterface.width * rsInterface.height];
				rsInterface.inventoryAmounts = new int[rsInterface.width * rsInterface.height];
				rsInterface.allowSwapItems = stream.readUnsignedByte() == 1;
				rsInterface.hasActions = stream.readUnsignedByte() == 1;
				rsInterface.usableItems = stream.readUnsignedByte() == 1;
				rsInterface.replaceItems = stream.readUnsignedByte() == 1;
				rsInterface.spritePaddingX = stream.readUnsignedByte();
				rsInterface.spritePaddingY = stream.readUnsignedByte();
				rsInterface.spritesX = new int[20];
				rsInterface.spritesY = new int[20];
				rsInterface.sprites = new Sprite[20];
				for (int j2 = 0; j2 < 20; j2++) {
					int k3 = stream.readUnsignedByte();
					if (k3 == 1) {
						rsInterface.spritesX[j2] = stream.readSignedWord();
						rsInterface.spritesY[j2] = stream.readSignedWord();
						String s1 = stream.readString();
						if (streamLoader_1 != null && s1.length() > 0) {
							int i5 = s1.lastIndexOf(",");
							rsInterface.sprites[j2] = method207(
									Integer.parseInt(s1.substring(i5 + 1)),
									streamLoader_1, s1.substring(0, i5));
						}
					}
				}
				rsInterface.actions = new String[5];
				for (int l3 = 0; l3 < 5; l3++) {
					rsInterface.actions[l3] = stream.readString();
					if (rsInterface.actions[l3].length() == 0)
						rsInterface.actions[l3] = null;
					if (rsInterface.parentID == 3824)
						rsInterface.actions[4] = "Buy 100";
					if (rsInterface.parentID == 1644)
						rsInterface.actions[2] = "Operate";
				}
			}
			if (rsInterface.type == TYPE_RECTANGLE)
				rsInterface.aBoolean227 = stream.readUnsignedByte() == 1;
			if (rsInterface.type == 4 || rsInterface.type == 1) {
				rsInterface.centerText = stream.readUnsignedByte() == 1;
				int k2 = stream.readUnsignedByte();
				if (textDrawingAreas != null)
					rsInterface.textDrawingAreas = textDrawingAreas[k2];
				rsInterface.textShadow = stream.readUnsignedByte() == 1;
			}
		if(rsInterface.type == TYPE_TEXT) {
				rsInterface.message = stream.readString().replaceAll("RuneScape", "ServerName");
				rsInterface.aString228 = stream.readString();
				if (getString)
					rsInterface.message = Integer.toString(rsInterface.id);
			}
			if (rsInterface.type == TYPE_MODEL_LIST || rsInterface.type == TYPE_RECTANGLE || rsInterface.type == TYPE_TEXT)
				rsInterface.textColor = stream.readDWord();
			if (rsInterface.type == TYPE_RECTANGLE || rsInterface.type == TYPE_TEXT) {
				rsInterface.anInt219 = stream.readDWord();
				rsInterface.anInt216 = stream.readDWord();
				rsInterface.anInt239 = stream.readDWord();
			}
			if (rsInterface.type == TYPE_SPRITE) {
				rsInterface.drawsTransparent = false;
				String s = stream.readString();
				if (streamLoader_1 != null && s.length() > 0) {
					int i4 = s.lastIndexOf(",");
					rsInterface.sprite1 = method207(
							Integer.parseInt(s.substring(i4 + 1)),
							streamLoader_1, s.substring(0, i4));
				}
				s = stream.readString();
				if (streamLoader_1 != null && s.length() > 0) {
					int j4 = s.lastIndexOf(",");
					rsInterface.sprite2 = method207(
							Integer.parseInt(s.substring(j4 + 1)),
							streamLoader_1, s.substring(0, j4));
				}
			}
			if (rsInterface.type == TYPE_MODEL) {
				int l = stream.readUnsignedByte();
				if (l != 0) {
					rsInterface.anInt233 = 1;
					rsInterface.mediaID = (l - 1 << 8)
							+ stream.readUnsignedByte();
				}
				l = stream.readUnsignedByte();
				if (l != 0) {
					rsInterface.anInt255 = 1;
					rsInterface.anInt256 = (l - 1 << 8)
							+ stream.readUnsignedByte();
				}
				l = stream.readUnsignedByte();
				if (l != 0)
					rsInterface.anInt257 = (l - 1 << 8)
							+ stream.readUnsignedByte();
				else
					rsInterface.anInt257 = -1;
				l = stream.readUnsignedByte();
				if (l != 0)
					rsInterface.anInt258 = (l - 1 << 8)
							+ stream.readUnsignedByte();
				else
					rsInterface.anInt258 = -1;
				rsInterface.modelZoom = stream.readUnsignedWord();
				rsInterface.modelRotation1 = stream.readUnsignedWord();
				rsInterface.modelRotation2 = stream.readUnsignedWord();
			}
			if (rsInterface.type == TYPE_ITEM_LIST) {
				rsInterface.inventoryItemId = new int[rsInterface.width
						* rsInterface.height];
				rsInterface.inventoryAmounts = new int[rsInterface.width
						* rsInterface.height];
				rsInterface.centerText = stream.readUnsignedByte() == 1;
				int l2 = stream.readUnsignedByte();
				if (textDrawingAreas != null)
					rsInterface.textDrawingAreas = textDrawingAreas[l2];
				rsInterface.textShadow = stream.readUnsignedByte() == 1;
				rsInterface.textColor = stream.readDWord();
				rsInterface.spritePaddingX = stream.readSignedWord();
				rsInterface.spritePaddingY = stream.readSignedWord();
				rsInterface.hasActions = stream.readUnsignedByte() == 1;
				rsInterface.actions = new String[5];
				for (int k4 = 0; k4 < 5; k4++) {
					rsInterface.actions[k4] = stream.readString();
					if (rsInterface.actions[k4].length() == 0)
						rsInterface.actions[k4] = null;
				}

			}
			if (rsInterface.atActionType == OPTION_USABLE || rsInterface.type == TYPE_INVENTORY) {
				rsInterface.selectedActionName = stream.readString();
				rsInterface.spellName = stream.readString();
				rsInterface.spellUsableOn = stream.readUnsignedWord();
			}

			if (rsInterface.type == 8)
				rsInterface.message = stream.readString();

			if (rsInterface.atActionType == OPTION_OK || rsInterface.atActionType == OPTION_TOGGLE_SETTING
					|| rsInterface.atActionType == OPTION_RESET_SETTING || rsInterface.atActionType == OPTION_CONTINUE) {
				rsInterface.tooltip = stream.readString();
				if (rsInterface.tooltip.length() == 0) {
					if (rsInterface.atActionType == 1)
						rsInterface.tooltip = "Ok";
					if (rsInterface.atActionType == 4)
						rsInterface.tooltip = "Select";
					if (rsInterface.atActionType == 5)
						rsInterface.tooltip = "Select";
					if (rsInterface.atActionType == 6)
						rsInterface.tooltip = "Continue";
				}
			}
			if (rsInterface.atActionType == OPTION_SLIDER) {
				rsInterface.tooltip = "Ok";
			}
			if (rsInterface.id == 8278) {
				rsInterface.message = "Players will be required to just use an Abyssal whip and Dragon dagger during combat.";
			}
			if (rsInterface.parentID == 6412) {
				if (rsInterface.scrollMax > 0) {
					rsInterface.scrollMax = 300;
				}
			}
		}
		aClass44 = streamLoader;
		constructLunar();
		priceChecker(textDrawingAreas);
		equipmentScreen(textDrawingAreas);
		equipmentTab(textDrawingAreas);
		clanChatSetup(textDrawingAreas);
		clanChatTab(textDrawingAreas);
		configureLunar(textDrawingAreas);
		quickPrayers(textDrawingAreas);
		overlayListener(textDrawingAreas);
		edgevilleHomeTeleport(textDrawingAreas);
		nightmareZone(textDrawingAreas);
		slayerInterface(textDrawingAreas);
		slayerInterfaceSub1(textDrawingAreas);
		slayerInterfaceSub2(textDrawingAreas);
		itemsKeptOnDeath(textDrawingAreas);
		itemsOnDeathDATA(textDrawingAreas);
		itemsOnDeath(textDrawingAreas);
		hiscores(textDrawingAreas);
		updateShopWidget(textDrawingAreas);
		bank(textDrawingAreas);
		tradeUIAddon(textDrawingAreas);
		questTab(textDrawingAreas);
		slayerRewardsBuy(textDrawingAreas);
		slayerRewardsLearn(textDrawingAreas);
		slayerRewardsAssign(textDrawingAreas);
		preloadEquipmentWidget(textDrawingAreas);
		JewelryCrafting(textDrawingAreas);
		initializeTitleWidget(textDrawingAreas);
		bountyHunterWidget(textDrawingAreas);
		extraOptions(textDrawingAreas);
		//optionsTab(textDrawingAreas);
		addSettingsWidget(textDrawingAreas);
		godWars(textDrawingAreas);
		achievements(textDrawingAreas);
		deadManBank(textDrawingAreas);
		safeDeadMan(textDrawingAreas);
		starterWidget(textDrawingAreas);
		spawnInterface(textDrawingAreas);
		lootingBag(textDrawingAreas);
		starterInterface(textDrawingAreas);
		defaultTextDrawingAreas = textDrawingAreas;
		Interfaces.loadInterfaces();
		aMRUNodes_238 = null;
	}
	public static TextDrawingArea[] defaultTextDrawingAreas;
	

	
	public static void addPestControlRewardWidget(TextDrawingArea[] tda) {
		RSInterface main = addInterface(37000);
		addSprite(37001, 0, "Interfaces/Pest Control/Reward/IMAGE");
		addButton(37002, 1, "Interfaces/Pest Control/Reward/IMAGE", "Confirm");
		addText(37003, "10 points", tda, 1, 0xFF981F, true, true);
		addHoverButton(37004, "Interfaces/Pest Control/Reward/IMAGE", 3, 21, 21, "Close", -1, 37005, 3);
		addHoveredButton(37005, "Interfaces/Pest Control/Reward/IMAGE", 4, 21, 21, 37006);
		addText(37007, "1,000 pts", tda, 2, 0xFF981F, false, true);
		
		setChildren(7, main);
		setBounds(37001, 0, 0, 0, main);
		setBounds(37002, 181, 273, 1, main);
		setBounds(37003, 253, 300, 2, main);
		setBounds(37004, 463, 14, 3, main);
		setBounds(37005, 463, 14, 4, main);
		setBounds(37007, 35, 19, 5, main);
		setBounds(37010, 27, 43, 6, main);
		
		RSInterface scroll = addInterface(37010);
		scroll.width = 442;
		scroll.height = 221;
		scroll.scrollMax = 450;
		setChildren(68, scroll);
		int x = 5;
		int y = 5;
		int imageId = 5;
		String[] names = new String[] {
				"Attack - 10,000 xp", "Defence - 10,000 xp", "Magic - 10,000 xp",
				"Prayer - 1,000 xp", "Strength - 10,000 xp", "Range - 10,000 xp",
				"Hitpoints - 3,300 xp"};
		for (int index = 0; index < 35; index += 5) {
			addSprite(37012 + index, imageId, "Interfaces/Pest Control/Reward/IMAGE");
			addText(37013 + index, names[index / 5], tda, 1, 0x339900, false, true);
			addClickableText(37014 + index, "(1 Pt)", "(1 Pt)", tda, 0, 0xFF981F, false, true, 40);
			addClickableText(37015 + index, "(10 Pts)", "(10 Pts)", tda, 0, 0xFF981F, false, true, 40);
			addClickableText(37016 + index, "(100 Pts)", "(100 Pts)", tda, 0, 0xFF981F, false, true, 40);
			setBounds(37012 + index, x, y, index, scroll);
			setBounds(37013 + index, x + 32, y, index + 1, scroll);
			setBounds(37014 + index, x + 32, y + 16, index + 2, scroll);
			setBounds(37015 + index, x + 70, y + 16, index + 3, scroll);
			setBounds(37016 + index, x + 120, y + 16, index + 4, scroll);
			y += 40;
			if (imageId == 8) {
				x += 210;
				y = 5;
			}
			imageId++;
		}
		addSprite(37050, 2, "Interfaces/Pest Control/Reward/IMAGE");
		setBounds(37050, 53, 165, 35, scroll);
		addSprite(37051, 2, "Interfaces/Pest Control/Reward/IMAGE");
		setBounds(37051, 53, 265, 36, scroll);
		x = 5;
		y = 180;
		names = new String[] {
				"Herb Pack", "Seed Pack", "Mineral Pack", "Void Knight Mace", "Void Knight Robe", "Void Mage Helm",
				"Void Melee Helm", "Void Knight Top", "Void Knight Gloves", "Void Range Helm"
		};
		int[] items = new int[] {
				257, 5295, 449, 8841, 8840, 11663, 11665, 8839, 8842, 11664
		};
		String[] costs = new String[] {
				"(30 Pts)", "(15 Pts)", "(15 Pts)", "(250 Pts)", "(250 Pts)",
				"(200 Pts)", "(200 Pts)", "(250 Pts)", "(150 Pts)", "(200 Pts)"
		};
		for (int index = 0; index < 30; index += 3) {
			addText(37052 + index, names[index / 3], tda, 1, 0x339900, false, true);
			addClickableText(37053 + index, costs[index / 3], costs[index / 3], tda, 0, 0xFF981F, false, true, 40);
			addToItemGroup(37054 + index, 1, 1, 0, 0, false, "", "", "");
			interfaceCache[37054 + index].inventoryItemId = new int[] { items[index / 3] + 1 };
			interfaceCache[37054 + index].inventoryAmounts = new int[] { 1 };
			setBounds(37052 + index, x + 32, y, 37 + index, scroll);
			setBounds(37053 + index, x + 32, y + 16, 37 + index + 1, scroll);
			setBounds(37054 + index, x, y, 37 + index + 2, scroll);
			y += 40;
			if (y == 220 && x == 215) {
				x = 5;
				y = 280;
			}
			if (x == 5 && y == 440) {
				x += 210;
				y = 280;
			}
			if (y == 260) {
				x += 210;
				y = 180;
			}
		}
		darken(37084, 200, 40, 0x000000, (byte) 100);
		setBounds(37084, 0, 0, 67, scroll);
	}
	
	public static void addTransparentSprite1(int id, int spriteId, String spriteName, int opacity) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
		tab.opacity = (byte) opacity;
		tab.drawsTransparent = true;
	}
	
	public static void lootingBag(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(26700);
		addSprite(26701, 0, "looting/BAG");
		addHoverButton(26702, "looting/BAG", 1, 16, 16, "Close", 0, 26703, 1);
		addHoveredButton(26703, "looting/BAG", 2, 16, 16, 26704);
		addText(26705, "Looting bag", tda, 2, 0xFF9900, true, true);
		int index = 0;
		//itemGroup(26706, 4, 7, 13, 0);
		addToItemGroup(26706 + index, 4, 7, 13, 0, false, "", "", "");
		addText(26707, "Value: 0 coins", tda, 0, 0xFF9900, true, true);
		tab.totalChildren(6);
		tab.child(0, 26701, 9, 21);
		tab.child(1, 26702, 168, 4);
		tab.child(2, 26703, 168, 4);
		tab.child(3, 26705, 95, 4);
		tab.child(4, 26706, 12, 23);
		tab.child(5, 26707, 95, 250);
	}
	
	/*public static void lootingBag(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(26700);
		addSprite(26701, 0, "looting/BAG");
		addHoverButton(26702, "looting/BAG", 1, 16, 16, "Close", 0, 26703, 1);
		addHoveredButton(26703, "looting/BAG", 2, 16, 16, 26704);
		addText(26705, "Looting bag", tda, 2, 0xFF9900, true, true);
		int index = 0;
		//itemGroup(26706, 4, 7, 13, 0);
		addToItemGroup(26706 + index, 4, 7, 13, 0, false, "", "", "");
		addText(26707, "Value: 0 coins", tda, 0, 0xFF9900, true, true);
		tab.totalChildren(6);
		tab.child(0, 26701, 9, 21);
		tab.child(1, 26702, 168, 4);
		tab.child(2, 26703, 168, 4);
		tab.child(3, 26705, 95, 4);
		tab.child(4, 26706, 12, 23);
		tab.child(5, 26707, 95, 250);
	}*/
	
	public static void spawnInterface(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(39699);
		addSprite(39700, 0, "looting/BAG");
		RSInterface scroll = addInterface(39760);
		scroll.width = 114;
		scroll.height = 230;
		scroll.scrollMax = 550;
		setChildren(50, scroll);
		for (int i = 0; i < 50; i += 2) {
			addClickableSprites(39750 + i, "View", "Interfaces/Titles/IMAGE", 3, 4, 5, 14);
			addText(39750 + i + 1, "Entry "+ (1 + i / 2), tda, 1, 0xFF981F, false, true);
			setBounds(39750 + i, 0, i / 2 * 22, i, scroll);
			setBounds(39750 + i + 1, 4, 3 + (i / 2 * 22), i + 1, scroll);
		}
		setChildren(2, widget);
		setBounds(39700, 9, 21, 0, widget);
		setBounds(39760, 61, 51, 1, widget);
	}
	
	public static void initializeTitleWidget(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(33000);
		addSprite(33001, 0, "Interfaces/Titles/IMAGE");
		addSprite(33002, 6, "Interfaces/Titles/IMAGE");
		//addTransparentSprite1(33001, 0, "Interfaces/Titles/IMAGE", 150);
		//addTransparentSprite1(33002, 6, "Interfaces/Titles/IMAGE", 150);
		drawRoundedRectangle(33004, 200, 130, 0x000000, (byte) 30, true, true);
		addButton(33005, 1, "Interfaces/Titles/IMAGE", "Close", 3, 52);
		addButton(33008, 7, "Interfaces/Titles/IMAGE", "Ok");
		addText(33011, "Lorem ipsum dolor sit amet,\\n"
						+ "consectetur adipiscing elit,\\n"
						+ "sed do eiusmod tempor incididunt\\n"
						+ "ut labore et dolore magna aliqua.\\n"
						+ "Ut enim ad minim veniam, quis\\n"
						+ "nostrud exercitation ullamco \\n"
						+ "laboris nisi ut aliquip ex ea\\n"
						+ "commodo consequat.", tda, 1, 0xFF981F, false, true);
		addText(33012, "Purchase", tda, 1, 0xFF981F, true, true);
		drawRoundedRectangle(33013, 140, 22, 0x000000, (byte) 30, true, true);
		addText(33014, "$", tda, 2, 0xFF981F, false, true);
		addText(33015, "45,000GP", tda, 1, 0xFF981F, false, true);
		addButton(33016, 9, "Interfaces/Titles/IMAGE", "Information");
		addSprites(33017, "Interfaces/Titles/IMAGE", 11, 12, 13);
		setChildren(15, widget);
		setBounds(33001, 56, 45, 0, widget);
		setBounds(33002, 250, 80, 1, widget);
		setBounds(33004, 223, 90, 2, widget);
		setBounds(33005, 435, 51, 3, widget);
		setBounds(33008, 277, 250, 4, widget);
		setBounds(33011, 230, 100, 5, widget);
		setBounds(33012, 320, 255, 6, widget);
		setBounds(33013, 250, 224, 7, widget);
		setBounds(33014, 255, 228, 8, widget);
		setBounds(33015, 268, 228, 9, widget);
		setBounds(33016, 418, 51, 10, widget);
		setBounds(33017, 370, 221, 11, widget);
		setBounds(33049, 61, 51, 12, widget);
		setBounds(33030, 0, 0, 13, widget);
		setBounds(33035, 0, 0, 14, widget);
		
		RSInterface scroll = addInterface(33049);
		scroll.width = 114;
		scroll.height = 230;
		scroll.scrollMax = 550;
		setChildren(50, scroll);
		
		for (int i = 0; i < 50; i += 2) {
			addClickableSprites(33050 + i, "View", "Interfaces/Titles/IMAGE", 3, 4, 5, 14);
			addText(33050 + i + 1, "Entry "+ (1 + i / 2), tda, 1, 0xFF981F, false, true);
			setBounds(33050 + i, 0, i / 2 * 22, i, scroll);
			setBounds(33050 + i + 1, 4, 3 + (i / 2 * 22), i + 1, scroll);
		}
		
		widget = addInterface(33030);
		setChildren(1, widget);
		addText(33031, "Selected Title", tda, 2, 0xFF981F, true, true);
		setBounds(33031, 320, 66, 0, widget);
		
		widget = addInterface(33035);
		setChildren(1, widget);
		addInputField(33036, 16, 0xFF981F, "Custom title", 120, 22, false, true, "[A-Za-z0-9 ]");
		setBounds(33036, 262, 58, 0, widget);
	}

	
	public static void itemGroup(int id, int w, int h, int x, int y) {
		RSInterface rsi = addInterface(id);
		rsi.width = w;
		rsi.height = h;
		rsi.inventoryItemId = new int[w * h];
		rsi.inventoryAmounts = new int[w * h];
		rsi.usableItems = false;
		rsi.hasActions = false;
		rsi.spritePaddingX = x;
		rsi.spritePaddingY = y;
		rsi.spritesX = new int[20];
		rsi.spritesY = new int[20];
		rsi.sprites = new Sprite[20];
		rsi.type = 2;
	}
	
	public static void Teleports(TextDrawingArea[] tda) {
	     RSInterface tab = addInterface(34000);
	     addSprite(34001, 0, "Interfaces/Teleports/BACKGROUND");
	     	/**
			 * Teleport 1
			 **/
	     	addText(34002, "Rock Crabs", tda, 1, 0xFFA500, true, true);
	     	addHoverButton(34003, "Interfaces/Teleports/BUTTON", 0, 97, 38, "Rock Crabs", -1, 34004, 1);
			addHoveredButton(34004, "Interfaces/Teleports/BUTTONH", 0, 97, 38, 36006);
			/**
			 * Teleport 2
			 **/
			addText(34005, "Yaks", tda, 1, 0xFFA500, true, true);
	     	addHoverButton(34006, "Interfaces/Teleports/BUTTON", 0, 97, 38, "Yaks", -1, 34007, 1);
			addHoveredButton(34007, "Interfaces/Teleports/BUTTONH", 0, 97, 38, 36006);
			/**
			 * Teleport 3
			 **/
			addText(34008, "Brimhaven", tda, 1, 0xFFA500, true, true);
	     	addHoverButton(34009, "Interfaces/Teleports/BUTTON", 0, 97, 38, "Brimhaven Dungeon", -1, 34010, 1);
			addHoveredButton(34010, "Interfaces/Teleports/BUTTONH", 0, 97, 38, 36006);
			/**
			 * Teleport 4 (Second Row)
			 **/
			addText(34011, "Experiments", tda, 1, 0xFFA500, true, true);
	     	addHoverButton(34012, "Interfaces/Teleports/BUTTON", 0, 97, 38, "Experiments", -1, 34013, 1);
			addHoveredButton(34013, "Interfaces/Teleports/BUTTONH", 0, 97, 38, 36006);
	     tab.totalChildren(13);
	     tab.child(0, 34001, 7, 30);
	     tab.child(1, 34003, 152, 77); /*HoverButton1 "Rock Crabs"*/
	     tab.child(2, 34004, 152, 77); /*HoverButton2 "Rock Crabs"*/
	     tab.child(3, 34002, 200, 87); /*Text1 "Rock Crabs"*/
	     
	     tab.child(4, 34006, 269, 77); /*HoverButton1 "Yaks"*/
	     tab.child(5, 34007, 269, 77); /*HoverButton2 "Yaks"*/
		 tab.child(6, 34005, 315, 87); /*Text1 "Yaks"*/
		 
		 tab.child(7, 34009, 386, 77); /*HoverButton1 "Brim Dung"*/
	     tab.child(8, 34010, 386, 77); /*HoverButton2 "Brim Dung"*/
		 tab.child(9, 34008, 434, 87); /*Text1 "Brim Dung"*/
		 
		 tab.child(10, 34012, 152, 157); /*HoverButton1 "Experiments"*/
	     tab.child(11, 34013, 152, 157); /*HoverButton2 "Experiments"*/
	     tab.child(12, 34011, 200, 167); /*Text1 "Experiments"*/
	    
	}
	
	public static void hiscores(TextDrawingArea[] wid) {
		RSInterface rsinterface = addInterface(42000);
		addSprite(42001, 292, "Interfaces/Hiscores/BACKGROUND");
		addText(42002, "ServerName Hiscores", wid, 2, 0xff981f, true);
		//addButton(42004, 42000, 298, 294, "Interfaces/Hiscores/KILLS", 69, 20, "Kills", 1, 5, 265);
		//addConfigButton(42005, 42000, 298, 295, "Interfaces/Hiscores/DEATHS", 74, 20, "Kills", 2, 5, 265);
		//addConfigButton(42006, 42000, 298, 296, "Interfaces/Hiscores/KDR", 60, 20, "Kills", 3, 5, 265);
		//addConfigButton(42007, 42000, 298, 297, "Interfaces/Hiscores/BOUNTY", 100, 20, "Kills", 4, 5, 265);
		addText(42008, "Player Name:", wid, 2, 0xff981f, true);
		addText(42009, "Kills:", wid, 2, 0xff981f, true);
		addText(42010, "Deaths:", wid, 2, 0xff981f, true);
		addText(42011, "KDR:", wid, 2, 0xff981f, true);
		addText(42012, "Killstreak:", wid, 2, 0xff981f, true);
		/*First Row*/
		addText(42013, "1) N/A", wid, 1, 0xff981f, false);
		addText(42014, "2) N/A", wid, 1, 0xff981f, false);
		addText(42015, "3) N/A", wid, 1, 0xff981f, false);
		addText(42016, "4) N/A", wid, 1, 0xff981f, false);
		addText(42017, "5) N/A", wid, 1, 0xff981f, false);
		addText(42018, "6) N/A", wid, 1, 0xff981f, false);
		addText(42019, "7) N/A", wid, 1, 0xff981f, false);
		addText(42020, "8) N/A", wid, 1, 0xff981f, false);
		addText(42021, "9) N/A", wid, 1, 0xff981f, false);
		addText(42022, "10) N/A", wid, 1, 0xff981f, false);
		/*Second Row*/
		addText(42023, "11", wid, 0, 0xff981f, true);
		addText(42024, "12", wid, 0, 0xff981f, true);
		addText(42025, "13", wid, 0, 0xff981f, true);
		addText(42026, "14", wid, 0, 0xff981f, true);
		addText(42027, "15", wid, 0, 0xff981f, true);
		addText(42028, "16", wid, 0, 0xff981f, true);
		addText(42029, "17", wid, 0, 0xff981f, true);
		addText(42030, "18", wid, 0, 0xff981f, true);
		addText(42031, "19", wid, 0, 0xff981f, true);
		addText(42032, "20", wid, 0, 0xff981f, true);
		/*Third Row*/
		addText(42033, "21", wid, 0, 0xff981f, true);
		addText(42034, "22", wid, 0, 0xff981f, true);
		addText(42035, "23", wid, 0, 0xff981f, true);
		addText(42036, "24", wid, 0, 0xff981f, true);
		addText(42037, "25", wid, 0, 0xff981f, true);
		addText(42038, "26", wid, 0, 0xff981f, true);
		addText(42039, "27", wid, 0, 0xff981f, true);
		addText(42040, "28", wid, 0, 0xff981f, true);
		addText(42041, "29", wid, 0, 0xff981f, true);
		addText(42042, "30", wid, 0, 0xff981f, true);
		/*Fourth Row*/
		addText(42043, "31", wid, 0, 0xff981f, true);
		addText(42044, "32", wid, 0, 0xff981f, true);
		addText(42045, "33", wid, 0, 0xff981f, true);
		addText(42046, "34", wid, 0, 0xff981f, true);
		addText(42047, "35", wid, 0, 0xff981f, true);
		addText(42048, "36", wid, 0, 0xff981f, true);
		addText(42049, "37", wid, 0, 0xff981f, true);
		addText(42050, "38", wid, 0, 0xff981f, true);
		addText(42051, "39", wid, 0, 0xff981f, true);
		addText(42052, "40", wid, 0, 0xff981f, true);
		/*Fith Row*/
		addText(42053, "41", wid, 0, 0xff981f, true);
		addText(42054, "42", wid, 0, 0xff981f, true);
		addText(42055, "43", wid, 0, 0xff981f, true);
		addText(42056, "44", wid, 0, 0xff981f, true);
		addText(42057, "45", wid, 0, 0xff981f, true);
		addText(42058, "46", wid, 0, 0xff981f, true);
		addText(42059, "47", wid, 0, 0xff981f, true);
		addText(42060, "48", wid, 0, 0xff981f, true);
		addText(42061, "49", wid, 0, 0xff981f, true);
		addText(42062, "", wid, 0, 0xff981f, true);
		addHoverButton(42063, "BankTab/BANK", 1, 15, 15, "Close Window", -1, 42064, 3);
		addHoveredButton(42064, "BankTab/BANK", 2, 15, 15, 42065);
		rsinterface.totalChildren(59);
		rsinterface.child(0, 42001, 12, 23);
		rsinterface.child(1, 42002, 255, 33);
		rsinterface.child(2, 42008, 89, 66);
		rsinterface.child(3, 42009, 202, 66);
		rsinterface.child(4, 42010, 281, 66);
		rsinterface.child(5, 42011, 355, 66);
		rsinterface.child(6, 42012, 440, 66);
		rsinterface.child(7, 42013, 25, 89);
		rsinterface.child(8, 42014, 25, 109);
		rsinterface.child(9, 42015, 25, 129);
		rsinterface.child(10, 42016, 25, 149);
		rsinterface.child(11, 42017, 25, 169);
		rsinterface.child(12, 42018, 25, 189);
		rsinterface.child(13, 42019, 25, 209);
		rsinterface.child(14, 42020, 25, 229);
		rsinterface.child(15, 42021, 25, 249);
		rsinterface.child(16, 42022, 25, 269);
		rsinterface.child(17, 42023, 200, 91);
		rsinterface.child(18, 42024, 200, 111);
		rsinterface.child(19, 42025, 200, 131);
		rsinterface.child(20, 42026, 200, 151);
		rsinterface.child(21, 42027, 200, 171);
		rsinterface.child(22, 42028, 200, 191);
		rsinterface.child(23, 42029, 200, 211);
		rsinterface.child(24, 42030, 200, 231);
		rsinterface.child(25, 42031, 200, 251);
		rsinterface.child(26, 42032, 200, 271);
		rsinterface.child(27, 42033, 280, 91);
		rsinterface.child(28, 42034, 280, 111);
		rsinterface.child(29, 42035, 280, 131);
		rsinterface.child(30, 42036, 280, 151);
		rsinterface.child(31, 42037, 280, 171);
		rsinterface.child(32, 42038, 280, 191);
		rsinterface.child(33, 42039, 280, 211);
		rsinterface.child(34, 42040, 280, 231);
		rsinterface.child(35, 42041, 280, 251);
		rsinterface.child(36, 42042, 280, 271);
		rsinterface.child(37, 42043, 355, 91);
		rsinterface.child(38, 42044, 355, 111);
		rsinterface.child(39, 42045, 355, 131);
		rsinterface.child(40, 42046, 355, 151);
		rsinterface.child(41, 42047, 355, 171);
		rsinterface.child(42, 42048, 355, 191);
		rsinterface.child(43, 42049, 355, 211);
		rsinterface.child(44, 42050, 355, 231);
		rsinterface.child(45, 42051, 355, 251);
		rsinterface.child(46, 42052, 355, 271);
		rsinterface.child(47, 42053, 438, 91);
		rsinterface.child(48, 42054, 438, 111);
		rsinterface.child(49, 42055, 438, 131);
		rsinterface.child(50, 42056, 438, 151);
		rsinterface.child(51, 42057, 438, 171);
		rsinterface.child(52, 42058, 438, 191);
		rsinterface.child(53, 42059, 438, 211);
		rsinterface.child(54, 42060, 438, 231);
		rsinterface.child(55, 42061, 438, 251);
		rsinterface.child(56, 42062, 438, 271);
		rsinterface.child(57, 42063, 470, 32);
		rsinterface.child(58, 42064, 470, 32);
	}
	
	 public static void AddInterfaceButton(int i, int j, int hoverId, String name, int W, int H, String S, int AT) {
        RSInterface RSInterface = addInterface(i);
        RSInterface.id = i;
        RSInterface.parentID = i;
        RSInterface.interfaceType = 5;
        RSInterface.atActionType = AT;
        RSInterface.opacity = 0;
        RSInterface.hoverType = hoverId;
        RSInterface.disabledSprite = imageLoader(j, name);
        RSInterface.enabledSprite = imageLoader(j, name);
        RSInterface.width = W;
        RSInterface.height = H;
        RSInterface.tooltip = S;
    }

    public static void AddInterfaceButton(int id, int sid, String spriteName, String tooltip, int mOver, int atAction, int width, int height) {
        RSInterface tab = interfaceCache[id] = new RSInterface();
        tab.id = id;
        tab.parentID = id;
        tab.interfaceType = 5;
        tab.atActionType = atAction;
        tab.contentType = 0;
        tab.opacity = 0;
        tab.hoverType = mOver;
        tab.disabledSprite = imageLoader(sid, spriteName);
        tab.enabledSprite = imageLoader(sid, spriteName);
        tab.width = width;
        tab.height = height;
        tab.tooltip = tooltip;
        tab.inventoryhover = true;
    }

    private static void AddInterfaceButton(int ID, int type, int hoverID, int dS, int eS, String NAME, int W, int H, String text, int configFrame, int configId) {
        RSInterface rsinterface = addInterface(ID);
        rsinterface.id = ID;
        rsinterface.parentID = ID;
        rsinterface.interfaceType = 5;
        rsinterface.atActionType = type;
        rsinterface.opacity = 0;
        rsinterface.hoverType = hoverID;
        rsinterface.disabledSprite = imageLoader(dS, NAME);
        rsinterface.enabledSprite = imageLoader(eS, NAME);
        rsinterface.width = W;
        rsinterface.height = H;
        rsinterface.valueCompareType = new int[1];
        rsinterface.requiredValues = new int[1];
        rsinterface.valueCompareType[0] = 1;
        rsinterface.requiredValues[0] = configId;
        rsinterface.valueIndexArray = new int[1][3];
        rsinterface.valueIndexArray[0][0] = 5;
        rsinterface.valueIndexArray[0][1] = configFrame;
        rsinterface.valueIndexArray[0][2] = 0;
        rsinterface.tooltip = text;
    }

    public static void AddInterfaceButtons(int id, int sid, String spriteName, String tooltip, int mOver, int atAction) {
        RSInterface rsinterface = interfaceCache[id] = new RSInterface();
        rsinterface.id = id;
        rsinterface.parentID = id;
        rsinterface.interfaceType = 5;
        rsinterface.atActionType = atAction;
        rsinterface.contentType = 0;
        rsinterface.opacity = (byte) 0;
        rsinterface.hoverType = mOver;
        rsinterface.disabledSprite = imageLoader(sid, spriteName);
        rsinterface.enabledSprite = imageLoader(sid, spriteName);
        rsinterface.width = rsinterface.disabledSprite.myWidth;
        rsinterface.height = rsinterface.enabledSprite.myHeight;
        rsinterface.tooltip = tooltip;
        rsinterface.inventoryhover = true;
    }
    public void setChild(int id, int interID, int x, int y) {
		children[id] = interID;
		childX[id] = x;
		childY[id] = y;
	}
	
    public static void optionsTab(TextDrawingArea[] TDA) {
		   RSInterface tab = addTabInterface(904);
		   RSInterface energy = interfaceCache[149];
		   energy.textColor = 0xff9933;
		   addSprite(905, 9, "Options/SPRITE");
		   addSprite(951, 32, "Options/SPRITE");
		   addSprite(953, 33, "Options/SPRITE");
		   addSprite(955, 34, "Options/SPRITE");
		   addSprite(947, 36, "Options/SPRITE");
		   addSprite(949, 35, "Options/SPRITE");
		   addSprite(36001, 4, "Options/SPRITE");
		   addSprite(36002, 4, "Options/SPRITE");
		   addSprite(36016, 4, "Options/SPRITE");
		   addButton(36100, 30, "Options/SPRITE", "Settings");
		   addSprite(36101, 43, "Options/SPRITE");
		   addHoverButton(36004, "Options/SPRITE", 37, 50, 39, "Fixed", -1, 36005, 1);
		   addHoveredButton(36005, "Options/SPRITE", 38, 50, 39, 36006);
		   addHoverButton(36007, "Options/SPRITE", 39, 50, 39, "Resizable", -1, 36008, 1);
		   addHoveredButton(36008, "Options/SPRITE", 40, 50, 39, 36009);
		   addHoverButton(36010, "Options/SPRITE", 41, 50, 39, "Fullscreen", -1, 36011, 1);
		   addHoveredButton(36011, "Options/SPRITE", 42, 50, 39, 36012);
		   
		   addText(36003, "Screen Modes", 0xFF981F, true, true, -1, TDA, 1);
	       addText(36017, "Client Options", 0xFF981F, true, true, -1, TDA, 1);
	       addText(36013, "Fixed", 0xFF981F, true, true, -1, TDA, 1);
	       addText(36014, "Resizable", 0xFF981F, true, true, -1, TDA, 1);
	       addText(36015, "Fullscreen", 0xFF981F, true, true, -1, TDA, 1);
	       
		   
		   addConfigButton(906, 904, 10, 14, "Options/SPRITE", 32, 16, "Dark", 1, 5, 166);
		   addConfigButton(908, 904, 11, 15, "Options/SPRITE", 32, 16, "Normal", 2, 5, 166);
		   addConfigButton(910, 904, 12, 16, "Options/SPRITE", 32, 16, "Bright", 3, 5, 166);
		   addConfigButton(912, 904, 13, 17, "Options/SPRITE", 32, 16, "Very Bright", 4, 5, 166);
		   addConfigButton(152, 904, 30, 31, "Options/SPRITE", 40, 40, "Toggle-run", 1, 5, 173);
		   addConfigButton(913, 904, 30, 31, "Options/SPRITE", 40, 40, "Toggle-Mouse Buttons", 0, 5, 170);
		   addConfigButton(915, 904, 30, 31, "Options/SPRITE", 40, 40, "Toggle-Chat Effects", 0, 5, 171);
		   addConfigButton(957, 904, 30, 31, "Options/SPRITE", 40, 40, "Toggle-Split Private Chat", 1, 5, 287);
		   addConfigButton(12464, 904, 30, 31, "Options/SPRITE", 40, 40, "Toggle-Accept Aid", 0, 5, 427);
		   tab.totalChildren(32);
		   int x = 0;
		   int y = 2;
		   tab.setChild(0, 36001, 0, 22);
		   tab.setChild(1, 36002, 0, 85);
		   tab.setChild(2, 36003, 93, 4);
		   tab.setChild(3, 36004, 9, 30);
		   tab.setChild(4, 36005, 9, 30);
		   tab.setChild(5, 36007, 70, 30);
		   tab.setChild(6, 36008, 70, 30);
		   tab.setChild(7, 36010, 131, 30);
		   tab.setChild(8, 36011, 131, 30);
		   tab.setChild(9, 36013, 34,70);    
		   tab.setChild(10, 36014, 93,70);
		   tab.setChild(11, 36015, 158,70);
		   tab.setChild(14, 905, 13 + x, 110 + y);
		   tab.setChild(15, 906, 48 + x, 118 + y);
		   tab.setChild(16, 908, 80 + x, 118 + y);
		   tab.setChild(17, 910, 112 + x, 118 + y);
		   tab.setChild(18, 912, 144 + x, 118 + y);
		   tab.setChild(19, 152, 75, 208);
		   tab.setChild(20, 913, 15, 153);
		   tab.setChild(21, 915, 75, 153);
		   tab.setChild(22, 957, 135, 153); 
		   tab.setChild(23, 951, 139, 159);
		   tab.setChild(24, 953, 79, 160);
		   tab.setChild(25, 955, 19, 159);
		   tab.setChild(26, 947, 87, 212);
		   tab.setChild(27, 12464, 15, 208);
		   tab.setChild(28, 949, 20, 213);
		   tab.setChild(29, 149, 80, 231);
		   tab.setChild(12, 36016, 0, 105);
		   tab.setChild(13, 36017, 93, 87);
		   tab.child(30, 36100, 135, 208);
		   tab.setChild(31, 36101, 135, 208);
	}

	public static void extraOptions(TextDrawingArea[] TDA) {
	        RSInterface Interface = addTabInterface(24500);
	        int index = 0;
	        addSprite(27520, 0, "Interfaces/Options/LINE");
	        addText(27501, "Toggle Ingame", 0xFF981F, false, true, -1, TDA, 3);
	        addText(27527, "Settings:", 0xFF981F, false, true, -1, TDA, 3);
	        addText(27502, "HD Textures:", 0xFF981F, false, true, -1, TDA, 1);
	        addText(27503, "Region Fog:", 0xFF981F, false, true, -1, TDA, 1);
	        addText(27504, "Enemy Info:", 0xFF981F, false, true, -1, TDA, 1);
	        addText(27535, "Shading:", 0xFF981F, false, true, -1, TDA, 1);
	        addText(27600, "Timers:", 0xFF981F, false, true, -1, TDA, 1);
	        //addText(27801, "Left Click Attack:", 0xFF981F, true, true, -1, TDA, 1);
	        addText(27519, "Unused:", 0xFF981F, false, true, -1, TDA, 1);
	        addText(27505, "Back", 0xFF981F, false, true, -1, TDA, 1);
	        addText(27524, "", 0x66ff00, true, true, -1, TDA, 1);
	        addText(27525, "", 0x66ff00, true, true, -1, TDA, 1);
	        addText(27526, "", 0x66ff00, true, true, -1, TDA, 1);
	        addText(27528, "@red@OFF", 0xff0000, true, true, -1, TDA, 1);
	        addText(27517, "", 0x66ff00, true, true, -1, TDA, 1);
	        addText(27518, "@red@OFF", 0xff0000, true, true, -1, TDA, 1);
	        //addText(27540, "@red@OFF", 0xff0000, true, true, -1, TDA, 1);
	        addText(27529, "@red@OFF", 0xff0000, true, true, -1, TDA, 1);
	        addText(27530, "@red@OFF", 0xff0000, true, true, -1, TDA, 1);
	        addText(27531, "@red@OFF", 0xff0000, true, true, -1, TDA, 1);
	        addText(27536, "", 0x66ff00, true, true, -1, TDA, 1);
	        addText(27537, "", 0xffffff, true, true, -1, TDA, 1);
	        addText(27538, "", 0xffffff, true, true, -1, TDA, 1);
	        addText(27602, "", 0x66ff00, true, true, -1, TDA, 1);
	        addText(27603, "@red@OFF", 0xff0000, true, true, -1, TDA, 1);
	        addText(27604, "", 0x66ff00, true, true, -1, TDA, 1);
	        //addText(27605, "@red@OFF", 0xff0000, true, true, -1, TDA, 1);
	        addButton(27508, 0, "Interfaces/Options/BUTTON", "Toggle");
	        addButton(27509, 0, "Interfaces/Options/BUTTON", "Toggle");
	        addButton(27510, 0, "Interfaces/Options/BUTTON", "Toggle");
	        addButton(27521, 0, "Interfaces/Options/BUTTON", "Toggle");
	        addButton(27532, 0, "Interfaces/Options/BUTTON", "Toggle");
	        addButton(27533, 0, "Interfaces/Options/BUTTON", "Toggle");
	       // addButton(27606, 0, "Interfaces/Options/BUTTON", "Toggle");
	        addButton(27607, 1, "Interfaces/Options/BUTTON", "Back"); //ROOFS
	        setChildren(32, Interface);
	        setBounds(27520, 0, 50, index, Interface);
	        index++;
	        setBounds(27501, 45, 5, index, Interface);
	        index++;
	        setBounds(27527, 70, 25, index, Interface);
	        index++;
	        setBounds(27508, 45, 60, index, Interface);
	        index++;
	        setBounds(27509, 45, 90, index, Interface);
	        index++;
	        setBounds(27510, 45, 120, index, Interface);
	        index++;
	        setBounds(27532, 45, 150, index, Interface);
	        index++;
	        setBounds(27521, 45, 180, index, Interface);
	        index++;
	        setBounds(27533, 45, 210, index, Interface);
	        index++;
	        //setBounds(27606, 45, 210, index, Interface);
	       // index++;
	        setBounds(27607, 60, 240, index, Interface);
	        index++;
	        setBounds(27502, 51, 63, index, Interface);
	        index++;
	        setBounds(27503, 51, 93, index, Interface);
	        index++;
	        setBounds(27519, 51, 213, index, Interface);
	        index++;
	   
	        setBounds(27504, 51, 123, index, Interface);
	        index++;
	        setBounds(27535, 51, 153, index, Interface);
	        index++;
	        setBounds(27600, 51, 183, index, Interface);
	        index++;
	       // setBounds(27601, 84, 213, index, Interface);
	       // index++;
	        setBounds(27505, 86, 243, index, Interface);
	        index++;
	        setBounds(27524, 135, 62, index, Interface);
	        index++;
	        setBounds(27518, 135, 213, index, Interface);
	        index++;
	        setBounds(27517, 135, 213, index, Interface);
	        index++;
	        setBounds(27525, 135, 92, index, Interface);
	        index++;
	        setBounds(27526, 135, 123, index, Interface);
	        index++;
	        setBounds(27528, 135, 62, index, Interface);
	        index++;
	        setBounds(27529, 135, 92, index, Interface);
	        index++;
	        setBounds(27530, 135, 123, index, Interface);
	        index++;
	        setBounds(27531, 135, 153, index, Interface);
	        index++;
	        setBounds(27536, 135, 153, index, Interface);
	        index++;
	        setBounds(27537, 135, 153, index, Interface);
	        index++;
	        setBounds(27538, 135, 153, index, Interface);
	        index++;
	        setBounds(27602, 135, 183, index, Interface);
	        index++;
	        setBounds(27603, 135, 183, index, Interface);
	        index++;
	        setBounds(27604, 135, 213, index, Interface);
	        index++;
	        //setBounds(27605, 135, 213, index, Interface);
	        //index++;
	    }
	public static void JewelryCrafting(TextDrawingArea[] tda) {
			RSInterface tab = addInterface(42500);
			tab.message = "";
			//setChildren(9, tab);
			addSprite(42501, 1, "Interfaces/Achievements/IMAGE");
			addHoverButton(42502, "BankTab/BANK", 1, 24, 24, "Close Window", 250, 30003, 3);
			addHoveredButton(42503, "BankTab/BANK", 2, 24, 24, 5379);
			addText(42504, "What would you like to make?", tda, 2, 0xE68A00, true, true);
			addText(42505, "Rings", tda, 2, 0xE68A00, true, true);
			addText(42513, "Necklaces", tda, 2, 0xE68A00, true, true);
			addText(42521, "Amulets", tda, 2, 0xE68A00, true, true);
			addText(42529, "Bracelets", tda, 2, 0xE68A00, true, true);
			setChildren(8, tab);
			int i = 0;
			setBounds(42501, 0, 0, 0, tab);
			i++;
			setBounds(42502, + 490, + 5, i, tab);
			i++;
			setBounds(42503, + 390, + 32, i, tab);
			i++;
			setBounds(42504, + 270, + 15, i, tab);
			i++;
			setBounds(42505, + 70, + 60, i, tab);
			i++;
			setBounds(42513, + 70, + 115, i, tab);
			i++;
			setBounds(42521, + 70, + 190, i, tab);
			i++;
			setBounds(42529, + 70, + 265, i, tab);
			i++;
			RSInterface inventory = addInterface(42600);
			setChildren(1, inventory);
			addToItemGroup(42601, 7, 1, 24, 24, false, "", "", "");
			//setBounds(42510, 120, 110, 9, tab);
		}
	public static void preloadEquipmentWidget(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(32000);
		addSprite(32001, 0, "Interfaces/PreloadingGear/IMAGE");
		addText(32003, "ServerName Preloaded Gear Setup", tda, 2, 0xFF981F);
		addHoverButton(32004, "Interfaces/PreloadingGear/IMAGE", 3, 16, 16, "Dismiss", -1, 32005, 3);
		addHoveredButton(32005, "Interfaces/PreloadingGear/IMAGE", 4, 16, 16, 32006);
		addSprite(32006, 12, "Interfaces/PreloadingGear/IMAGE");
		//addHoverButton(35040, "Interfaces/PreloadingGear/button", 0, 81, 26, "Spawn", 201, 35041, 5);
		//addHoveredButton(35041, "Interfaces/PreloadingGear/button", 1, 81, 26, 2001);
		addItemChooser(32007, tda);
		addInputField(32002, 14, 0xFF981F, "Name", 120, 22, false, false, "[A-Za-z0-9 ]");
		setChildren(10, widget);
		setBounds(32001, 12, 1, 0, widget);
		setBounds(32030, 17, 29 + 7, 1, widget);
		setBounds(32003, 116, 13, 2, widget);
		setBounds(32004, 472, 11, 3, widget);
		setBounds(32005, 472, 11, 4, widget);
		setBounds(32006, 313, 55, 5, widget);
		setBounds(32100, 165, 55, 6, widget);
		setBounds(32200, 0, -10, 7, widget);
		setBounds(32002, 340, 283, 8, widget);
		setBounds(32007, 125, 65, 9, widget);
		
		RSInterface buttons = addInterface(32030);
		setChildren(10, buttons);
		int height = 0;
		int childId = 0;
		for (int id = 0; id < 30; id += 3) {
			RSInterface button = addInterface(32031 + id);
			addButton(32031 + id + 1, 1, "Interfaces/PreloadingGear/IMAGE", "View");
			addButton(32031 + id + 1, 1, "Interfaces/PreloadingGear/IMAGE", 130, 29,
					new String[] {"Load inventory", "Load equipment", "View"});
			addText(32031 + id + 2, "New slot", tda, 1, 0xFF981F);
			System.out.println("ID: "+3203 + id + 2+"");
			setChildren(2, button);
			setBounds(32031 + id + 1, 0, 0, 0, button);
			setBounds(32031 + id + 2, 5, 7, 1, button);
			setBounds(32031 + id, 0, height, childId, buttons);
			childId++;
			height += 29;
		}
		
		RSInterface inventory = addInterface(32100);
		setChildren(56, inventory);
		int index = 0;
		for (int row = 0; row < 7; row++) {
			for (int collumn = 0; collumn < 4; collumn++) {
				addButton(32101 + index, 13,"Interfaces/PreloadingGear/IMAGE", 36, 36, new String[] {"Delete", "Edit"});
				addToItemGroup(32129 + index, 1, 1, 0, 0, false, "", "", "");
				setBounds(32101 + index, collumn * 36, row * 36, index, inventory);
				setBounds(32129 + index, collumn * 36 + 2, row * 36 + 2, 28 + index, inventory);
				index++;
			}
		}
		
		RSInterface equipment = addInterface(32200);
		setChildren(22, equipment);
		int xLocation[] = {
				389, 348, 389, 333, 389, 445, 389, 333, 389, 445, 430
		};
		int yLocation[] = {
				78, 117, 117, 156, 156, 156, 196, 236, 236, 236, 117
		};
		for (int i = 0; i < 11; i++) {
			addButton(32201 + i, 14,"Interfaces/PreloadingGear/IMAGE", 36, 36, new String[] {"Delete", "Edit"});
			addToItemGroup(32212 + i, 1, 1, 0, 0, false, "", "", "");
			interfaceCache[32212 + i].sprite2 = new Sprite("Interfaces/PreloadingGear/IMAGE 17");
			setBounds(32201 + i, xLocation[i] -3, yLocation[i] - 3, i, equipment);
			setBounds(32212 + i, xLocation[i] - 5, yLocation[i] - 8, 11 + i, equipment);
		}
	}
	
	public static void addItemChooser(int interfaceId, TextDrawingArea[] tda) {
		RSInterface widget = addInterface(interfaceId);
		addSprite(interfaceId + 1, 19, "Interfaces/PreloadingGear/IMAGE");
		addInputField(interfaceId + 2, 50, 0xFF981F, "Name", 242, 21, false, true);
		addInputField(interfaceId + 5, 10, 0xFF981F, "Amount", 110, 21, false, true, "[\\d]");
		addButton(interfaceId + 6, 18, "Interfaces/PreloadingGear/IMAGE", "Submit");
		addText(interfaceId + 7, "Submit", tda, 0, 0xFF981F);
		addHoverButton(interfaceId + 8, "Interfaces/PreloadingGear/IMAGE", 3, 16, 16, "Cancel", -1, interfaceId + 9, 1);
		addHoveredButton(interfaceId + 9, "Interfaces/PreloadingGear/IMAGE", 4, 16, 16, interfaceId + 10);
		addConfigButton(interfaceId + 11, interfaceId, 20, 21, "Interfaces/PreloadingGear/IMAGE", 14, 15, "Toggle", 1, 4, 810);
		addTooltip(interfaceId + 12, "When toggled on, item searching is limited to a player's bank.\n"
				+ "When off, any item name that contains your input will be visible.");
		interfaceCache[interfaceId + 11].mOverInterToTrigger = interfaceId + 12;
		darken(interfaceId + 14, 512, 334, 0x000000, (byte) 110);
		setChildren(11, widget);
		setBounds(interfaceId + 14, - 125, - 65, 0, widget);
		setBounds(interfaceId + 1, 0, 0, 1, widget);
		setBounds(interfaceId + 2, 5, 160, 2, widget);
		setBounds(interfaceId + 3, 5, 6, 3, widget);
		setBounds(interfaceId + 5, 5, 182, 4, widget);
		setBounds(interfaceId + 6, 117, 182, 5, widget);
		setBounds(interfaceId + 7, 150, 187, 6, widget);
		setBounds(interfaceId + 8, 216, 6, 7, widget);
		setBounds(interfaceId + 9, 216, 6, 8, widget);
		setBounds(interfaceId + 11, 230, 184, 9, widget);
		setBounds(interfaceId + 12, 0, 210, 10, widget);
		RSInterface scroll = addInterface(interfaceId + 3);
		scroll.scrollMax = 32 * 10 + 30;
		scroll.height = 154;
		scroll.width = 226;
		setChildren(1, scroll);
		int x = 0, y = 0;
		addToItemGroup(interfaceId + 4, 6, 10, 3, 3, false, "", "", "");
		setBounds(interfaceId + 4, x + 5, y + 5, 0, scroll);
		for (int i = 0; i < widget.children.length - 1; i++) {
			interfaceCache[interfaceId + i].isItemSearchComponent = true;	
		}
	}
	
	public static void starterInterface(TextDrawingArea[] TDA) {
		RSInterface tab = addTabInterface(58500);
		addSprite(58501, 0, "Starter/SPRITE");
		addText(58502, "Account Selection", TDA, 2, 0xff9933, true, true);
		addText(58503, "Pker", TDA, 3, 0xff9933, false, true);
		addText(58504, "Extreme", TDA, 3, 0xff9933, false, true);
		addText(58505, "Ironman", TDA, 3, 0xff9933, false, true);
		addText(58514, "Welcome to ServerName, we would recommend reading the tutorial\\nas it will help a lot. Enjoy your journey!", TDA, 0, 0xff9933, true, true);
		addText(58515, "In the normal game mode, you would get normal experience\\nfor skilling and high experience for combat, recommended\\nfor people who want to play and enjoy ServerName casually.", TDA, 1, 0xff9933, true, true);
		addHoverButton(58516, "Starter/SPRITE", 1, 110, 37, "Confirm", -1, 58517, 1);
		addHoveredButton(58517, "Starter/SPRITE", 2, 110, 37, 58518);
		addText(58519, "Confirm", TDA, 3, 0xff9933, true, true);
		addConfigButton(58520, 58500, 3, 4, "Starter/SPRITE", 15, 15, "Select Pker", 0, 5, 1085);
		addConfigButton(58521, 58500, 3, 4, "Starter/SPRITE", 15, 15, "Select Extreme", 1, 5, 1085);
		addConfigButton(58522, 58500, 3, 4, "Starter/SPRITE", 15, 15, "Select Ironman", 2, 5, 1085);

		tab.totalChildren(12);
		tab.child(0, 58501, 12, 40);
		tab.child(1, 58502, 255, 49);
		tab.child(2, 58503, 63, 93);
		tab.child(3, 58504, 63, 133);
		tab.child(4, 58505, 63, 173);
		tab.child(5, 58515, 310, 130);
		tab.child(6, 58516, 134, 207);
		tab.child(7, 58517, 134, 207);
		tab.child(8, 58520, 40, 93);
		tab.child(9, 58521, 40, 134);
		tab.child(10, 58522, 40, 175);
		tab.child(11, 58519, 187, 215);
//		tab.child(10, 58523, 139, 209);
	}
	public static RSInterface addContainer(int id, int contentType, int width, int height, String... actions) {
		RSInterface container = addInterface(id);
		container.parentID = id;
		container.type = 2;
		container.contentType = contentType;
		container.width = width;
		container.height = height;
		container.sprites = new Sprite[20];
		container.spritesX = new int[20];
		container.spritesY = new int[20];
		container.spritePaddingX = 7;
		container.spritePaddingY = 1;
		container.inventoryItemId = new int[width * height];
		container.inventoryAmounts = new int[width * height];
		container.allowSwapItems = true;
		container.actions = actions;
		return container;
	}
	public static void spawnItems(int interfaceId, TextDrawingArea[] tda) {
		RSInterface widget = addInterface(interfaceId);
		addSprite(interfaceId + 1, 19, "Interfaces/PreloadingGear/IMAGE");
		addInputField(interfaceId + 2, 50, 0xFF981F, "Name", 242, 21, false, true);
		addInputField(interfaceId + 5, 10, 0xFF981F, "Amount", 110, 21, false, true, "[\\d]");
		addButton(interfaceId + 6, 18, "Interfaces/PreloadingGear/IMAGE", "Submit");
		addText(interfaceId + 7, "Submit", tda, 0, 0xFF981F);
		addHoverButton(interfaceId + 8, "Interfaces/PreloadingGear/IMAGE", 3, 16, 16, "Cancel", -1, interfaceId + 9, 1);
		addHoveredButton(interfaceId + 9, "Interfaces/PreloadingGear/IMAGE", 4, 16, 16, interfaceId + 10);
		addConfigButton(interfaceId + 11, interfaceId, 20, 21, "Interfaces/PreloadingGear/IMAGE", 14, 15, "Toggle", 1, 4, 810);
		addTooltip(interfaceId + 12, "When toggled on, item searching is limited to a player's bank.\n"
				+ "When off, any item name that contains your input will be visible.");
		interfaceCache[interfaceId + 11].mOverInterToTrigger = interfaceId + 12;
		darken(interfaceId + 14, 512, 334, 0x000000, (byte) 110);
		setChildren(11, widget);
		setBounds(interfaceId + 14, - 125, - 65, 0, widget);
		setBounds(interfaceId + 1, 0, 0, 1, widget);
		setBounds(interfaceId + 2, 5, 160, 2, widget);
		setBounds(interfaceId + 3, 5, 6, 3, widget);
		setBounds(interfaceId + 5, 5, 182, 4, widget);
		setBounds(interfaceId + 6, 117, 182, 5, widget);
		setBounds(interfaceId + 7, 150, 187, 6, widget);
		setBounds(interfaceId + 8, 216, 6, 7, widget);
		setBounds(interfaceId + 9, 216, 6, 8, widget);
		setBounds(interfaceId + 11, 230, 184, 9, widget);
		setBounds(interfaceId + 12, 0, 210, 10, widget);
		RSInterface scroll = addInterface(interfaceId + 3);
		scroll.scrollMax = 32 * 10 + 30;
		scroll.height = 154;
		scroll.width = 226;
		setChildren(1, scroll);
		int x = 0, y = 0;
		addToItemGroup(interfaceId + 4, 6, 10, 3, 3, false, "", "", "");
		setBounds(interfaceId + 4, x + 5, y + 5, 0, scroll);
		for (int i = 0; i < widget.children.length - 1; i++) {
			interfaceCache[interfaceId + i].isItemSearchComponent = true;	
		}
	}
	
	 public static void slayerInterface(TextDrawingArea[] tda) {
	        RSInterface rsInterface = addInterface(41000);
	        addSprite(41001, 1, "Interfaces/SlayerInterface/IMAGE");
	        addHoverButton(41002, "Interfaces/SlayerInterface/IMAGE", 4, 16, 16, "Close window", 0, 41003, 1);
	        addHoveredButton(41003, "Interfaces/SlayerInterface/IMAGE", 5, 16, 16, 41004);
	        addHoverButton(41005, "", 0, 85, 20, "Buy", 0, 41006, 1);
	        addHoverButton(41007, "", 0, 85, 20, "Learn", 0, 41008, 1);
	        addHoverButton(41009, "", 0, 85, 20, "Assignment", 0, 41010, 1);
	        addText(41011, "Slayer Points: ", tda, 3, 0xFF981F);
	        addTextButton(41012, "Slayer Experience                           50", "Buy Slayer Experience", 0xFF981F, false, true, tda, 1, 400);
	        addTextButton(41013, "Slayer's Respite                             25", "Buy Slayer's Respite", 0xFF981F, false, true, tda, 1, 401);
	        addTextButton(41014, "Slayer Darts                                     35", "Buy Slayer Darts", 0xFF981F, false, true, tda, 1, 402);
	        addTextButton(41015, "Broad Arrows                                    25", "Buy Broad Arrows", 0xFF981F, false, true, tda, 1, 403);
	        setChildren(11, rsInterface);
	        rsInterface.child(0, 41001, 12, 10);
	        rsInterface.child(1, 41002, 473, 20);
	        rsInterface.child(2, 41003, 473, 20);
	        rsInterface.child(3, 41005, 21, 23);
	        rsInterface.child(4, 41007, 107, 23);
	        rsInterface.child(5, 41009, 193, 23);
	        rsInterface.child(6, 41011, 98, 74);
	        rsInterface.child(7, 41012, 124, 128);
	        rsInterface.child(8, 41013, 125, 160);
	        rsInterface.child(9, 41014, 125, 190);
	        rsInterface.child(10, 41015, 124, 220);
	        
	    }
	    

	public static void slayerInterfaceSub1(TextDrawingArea[] tda) {
		RSInterface rsInterface = addInterface(41500);
		addSprite(41501, 2, "Interfaces/SlayerInterface/IMAGE");
		addHoverButton(41502, "Interfaces/SlayerInterface/IMAGE", 4, 16, 16,
				"Close window", 0, 41503, 1);
		addHoveredButton(41503, "Interfaces/SlayerInterface/IMAGE", 5, 16, 16,
				41504);
		addHoverButton(41505, "", 0, 85, 20, "Buy", 0, 41506, 1);
		addHoverButton(41507, "", 0, 85, 20, "Learn", 0, 41508, 1);
		addHoverButton(41509, "", 0, 85, 20, "Assignment", 0, 41510, 1);
		addText(41511, "Slayer Points: ", tda, 3, 0xFF981F);
		setChildren(7, rsInterface);
		rsInterface.child(0, 41501, 12, 10);
		rsInterface.child(1, 41502, 473, 20);
		rsInterface.child(2, 41503, 473, 20);
		rsInterface.child(3, 41505, 21, 23);
		rsInterface.child(4, 41507, 107, 23);
		rsInterface.child(5, 41509, 193, 23);
		rsInterface.child(6, 41511, 98, 74);
	}
	
	 public static void overlayListener(TextDrawingArea[] tda){
	     addText(35000, "", tda, 0, 0);
	    }

	public static void slayerInterfaceSub2(TextDrawingArea[] tda) {
		RSInterface rsInterface = addInterface(42000);
		addSprite(42001, 3, "Interfaces/SlayerInterface/IMAGE");
		addHoverButton(42002, "Interfaces/SlayerInterface/IMAGE", 4, 16, 16,
				"Close window", 0, 42003, 1);
		addHoveredButton(42003, "Interfaces/SlayerInterface/IMAGE", 5, 16, 16,
				42004);
		addHoverButton(42005, "", 0, 85, 20, "Buy", 0, 42006, 1);
		addHoverButton(42007, "", 0, 85, 20, "Learn", 0, 42008, 1);
		addHoverButton(42009, "", 0, 85, 20, "Assignment", 0, 42010, 1);
		addText(42011, "Slayer Points: ", tda, 3, 0xFF981F);
		addTextButton(42012, "Cancel Task",
				"Temporarily cancel your current slayer task", 0xFF981F, false,
				true, tda, 1, 300);
		addTextButton(42013, "Remove Task permanently",
				"Permanently remove this monster as a task", 0xFF981F, false,
				true, tda, 1, 305);
		addText(42014, "line 1", tda, 1, 0xFF981F);
		addText(42015, "line 2", tda, 1, 0xFF981F);
		addText(42016, "line 3", tda, 1, 0xFF981F);
		addText(42017, "line 4", tda, 1, 0xFF981F);
		addButton(42018, 6, "Interfaces/SlayerInterface/IMAGE",
				"Delete removed slayer task");
		addButton(42019, 6, "Interfaces/SlayerInterface/IMAGE",
				"Delete removed slayer task");
		addButton(42020, 6, "Interfaces/SlayerInterface/IMAGE",
				"Delete removed slayer task");
		addButton(42021, 6, "Interfaces/SlayerInterface/IMAGE",
				"Delete removed slayer task");
		setChildren(17, rsInterface);
		rsInterface.child(0, 42001, 12, 10);
		rsInterface.child(1, 42002, 473, 20);
		rsInterface.child(2, 42003, 473, 20);
		rsInterface.child(3, 42005, 21, 23);
		rsInterface.child(4, 42007, 107, 23);
		rsInterface.child(5, 42009, 193, 23);
		rsInterface.child(6, 42011, 98, 74);
		rsInterface.child(7, 42012, 71, 127);
		rsInterface.child(8, 42013, 71, 146);
		rsInterface.child(9, 42014, 71, 216);
		rsInterface.child(10, 42015, 71, 234);
		rsInterface.child(11, 42016, 71, 252);
		rsInterface.child(12, 42017, 71, 270);
		rsInterface.child(13, 42018, 303, 215);
		rsInterface.child(14, 42019, 303, 233);
		rsInterface.child(15, 42020, 303, 251);
		rsInterface.child(16, 42021, 303, 269);
	}
	
	public static void achievements(TextDrawingArea[] tda) {
		RSInterface rsi = addInterface(49000);
		addSprite(49001, 1, "Interfaces/Achievements/IMAGE");
		addHoverButton(49002, "Interfaces/Achievements/IMAGE", 15, 16, 16, "Close Window", -1, 49003, 3);
		addHoveredButton(49003, "Interfaces/Achievements/IMAGE", 16, 16, 16, 49004);
		addConfigButton(49005, 49000, 12, 20, "Interfaces/Achievements/IMAGE", 71, 29,
				"Tier Tier I", 1, 1, 800);
		addConfigButton(49006, 49000, 13, 20, "Interfaces/Achievements/IMAGE", 71, 29,
				"Tier Tier II", 1, 1, 801);
		addConfigButton(49007, 49000, 14, 20, "Interfaces/Achievements/IMAGE", 71, 29,
				"View Tier III", 1, 1, 802);
		addSprite(49014, 11, "Interfaces/Achievements/IMAGE");
		addText(49016, "1000", tda, 0, 0xff981f, true, true);
		addText(49017, "Tier I", tda, 0, 0xff981f, false, true);
		addText(49018, "Tier II", tda, 0, 0xff981f, false, true);
		addText(49019, "Tier III", tda, 0, 0xff981f, false, true);
		addText(49020, "100", tda, 0, 0xff981f, false, true);
		
		setChildren(14, rsi);
		setBounds(49001, 0, 0, 0, rsi);
		setBounds(49002, 490, 6, 1, rsi);
		setBounds(49003, 490, 6, 2, rsi);
		
		setBounds(49005, 15, 10, 3, rsi);
		setBounds(49006, 90, 10, 4, rsi);
		setBounds(49007, 165, 10, 5, rsi);
		
		setBounds(49014, 415, 14, 6, rsi);
		setBounds(49016, 443, 19, 7, rsi);
		setBounds(49017, 37, 19, 8, rsi);
		setBounds(49018, 111, 19, 9, rsi);
		setBounds(49019, 184, 19, 10, rsi);
		
		setBounds(49100, 3, 48, 11, rsi);
		setBounds(51100, 3, 48, 12, rsi);
		setBounds(53100, 3, 48, 13, rsi);

		RSInterface scroll = addInterface(49100);
		setChildren(800, scroll);
		scroll.scrollMax = 6502;
		scroll.height = 281;
		scroll.width = 486;
		int y = 0;
		for(int i = 0; i < 100; i++) {
			addSprite(49101 + i, 10, "Interfaces/Achievements/IMAGE");
			addSprite(49201 + i, 2, "Interfaces/Achievements/IMAGE");
			addSprite(49301 + i, 5, "Interfaces/Achievements/IMAGE");
			addText(49401 + i, "", tda, 2, 0xFFFFFF, true, true);
			addText(49501 + i, "", tda, 2, 0xFFFFFF, false, true);
			addText(49601 + i, "", tda, 2, 0x425619, false, true);
			addSprite(49701 + i, 6, "Interfaces/Achievements/IMAGE");
			addText(49801 + i, "0/1", tda, 1, 0xFFFFFF, true, true);
			setBounds(49101 + i, 1, y, i, scroll);
			setBounds(49201 + i, 8, y + 5, 100 + i, scroll);
			setBounds(49301 + i, 430, y + 12, 200 + i, scroll);
			setBounds(49401 + i, 448, y + 24, 300 + i, scroll);
			setBounds(49501 + i, 65, y + 9, 400 + i, scroll);
			setBounds(49601 + i, 65, y + 24, 500 + i, scroll);
			setBounds(49701 + i, 65, y + 41, 600 + i, scroll);
			setBounds(49801 + i, 160, y + 43, 700 + i, scroll);
			y += 65;
		}
		RSInterface tier2 = addInterface(51100);
		setChildren(800, tier2);
		tier2.scrollMax = 6502;
		tier2.height = 281;
		tier2.width = 486;
		y = 0;
		for(int i = 0; i < 100; i++) {
			addSprite(51101 + i, 10, "Interfaces/Achievements/IMAGE");
			addSprite(51201 + i, 3, "Interfaces/Achievements/IMAGE");
			addSprite(51301 + i, 5, "Interfaces/Achievements/IMAGE");
			addText(51401 + i, "", tda, 2, 0xFFFFFF, true, true);
			addText(51501 + i, "", tda, 2, 0xFFFFFF, false, true);
			addText(51601 + i, "", tda, 2, 0x425619, false, true);
			addSprite(51701 + i, 6, "Interfaces/Achievements/IMAGE");
			addText(51801 + i, "0/1", tda, 1, 0xFFFFFF, true, true);
			setBounds(51101 + i, 1, y, i, tier2);
			setBounds(51201 + i, 8, y + 5, 100 + i, tier2);
			setBounds(51301 + i, 430, y + 12, 200 + i, tier2);
			setBounds(51401 + i, 448, y + 24, 300 + i, tier2);
			setBounds(51501 + i, 65, y + 9, 400 + i, tier2);
			setBounds(51601 + i, 65, y + 24, 500 + i, tier2);
			setBounds(51701 + i, 65, y + 41, 600 + i, tier2);
			setBounds(51801 + i, 160, y + 43, 700 + i, tier2);
			y += 65;
		}
		RSInterface tier3 = addInterface(53100);
		setChildren(800, tier3);
		tier3.scrollMax = 6502;
		tier3.height = 281;
		tier3.width = 486;
		y = 0;
		for(int i = 0; i < 100; i++) {
			addSprite(53101 + i, 10, "Interfaces/Achievements/IMAGE");
			addSprite(53201 + i, 4, "Interfaces/Achievements/IMAGE");
			addSprite(53301 + i, 5, "Interfaces/Achievements/IMAGE");
			addText(53401 + i, "", tda, 2, 0xFFFFFF, true, true);
			addText(53501 + i, "", tda, 2, 0xFFFFFF, false, true);
			addText(53601 + i, "", tda, 2, 0x425619, false, true);
			addSprite(53701 + i, 6, "Interfaces/Achievements/IMAGE");
			addText(53801 + i, "0/1", tda, 1, 0xFFFFFF, true, true);
			setBounds(53101 + i, 1, y, i, tier3);
			setBounds(53201 + i, 8, y + 5, 100 + i, tier3);
			setBounds(53301 + i, 430, y + 12, 200 + i, tier3);
			setBounds(53401 + i, 448, y + 24, 300 + i, tier3);
			setBounds(53501 + i, 65, y + 9, 400 + i, tier3);
			setBounds(53601 + i, 65, y + 24, 500 + i, tier3);
			setBounds(53701 + i, 65, y + 41, 600 + i, tier3);
			setBounds(53801 + i, 160, y + 43, 700 + i, tier3);
			y += 65;
		}
	}
	
	public static void godWars(TextDrawingArea[] tda) {
		RSInterface rsinterface = addTabInterface(16210);
		addText(16211, "NPC killcount", tda, 0, 0xff9040, true, true);
		addText(16212, "Armadyl kills", tda, 0, 0xff9040, true, true);
		addText(16213, "Bandos kills", tda, 0, 0xff9040, true, true);
		addText(16214, "Saradomin kills", tda, 0, 0xff9040, true, true);
		addText(16215, "Zamorak kills", tda, 0, 0xff9040, true, true);
		addText(16216, "0", tda, 0, 0x66FFFF, true, true);// armadyl
		addText(16217, "0", tda, 0, 0x66FFFF, true, true);// bandos
		addText(16218, "0", tda, 0, 0x66FFFF, true, true);// saradomin
		addText(16219, "0", tda, 0, 0x66FFFF, true, true);// zamorak
		rsinterface.scrollMax = 0;
		rsinterface.children = new int[9];
		rsinterface.childX = new int[9];
		rsinterface.childY = new int[9];
		rsinterface.children[0] = 16211;
		rsinterface.childX[0] = -52 + 375 + 30;
		rsinterface.childY[0] = 7;
		rsinterface.children[1] = 16212;
		rsinterface.childX[1] = -52 + 375 + 30;
		rsinterface.childY[1] = 30;
		rsinterface.children[2] = 16213;
		rsinterface.childX[2] = -52 + 375 + 30;
		rsinterface.childY[2] = 44;
		rsinterface.children[3] = 16214;
		rsinterface.childX[3] = -52 + 375 + 30;
		rsinterface.childY[3] = 58;
		rsinterface.children[4] = 16215;
		rsinterface.childX[4] = -52 + 375 + 30;
		rsinterface.childY[4] = 73;
		rsinterface.children[5] = 16216;
		rsinterface.childX[5] = -52 + 460 + 60;
		rsinterface.childY[5] = 31;
		rsinterface.children[6] = 16217;
		rsinterface.childX[6] = -52 + 460 + 60;
		rsinterface.childY[6] = 45;
		rsinterface.children[7] = 16218;
		rsinterface.childX[7] = -52 + 460 + 60;
		rsinterface.childY[7] = 59;
		rsinterface.children[8] = 16219;
		rsinterface.childX[8] = -52 + 460 + 60;
		rsinterface.childY[8] = 74;
	}

	public static void addSettingsWidgetGlobals(TextDrawingArea[] tda) {
	 	//Background
		addSprite(31001, 0, "Options/main tab/sprite");

		addBankHover(31014, 4, 31015, 1, 2,		//Aid
				"Options/Interface/aid", 40, 40, 304, 1,
				"Toggle Accept Aid", 31016, 1, 2,
				"Options/Interface/aid", 31017, "", "", 12, 20);

		addBankHover(31018, 4, 31019, 1, 2,		//Run
				"Options/Interface/run", 40, 40, 173, 1,
				"Toggle run", 31020, 1, 2, "Options/Interface/run",
				31021, "", "", 12, 20);

		addButton(31022, 1, "Options/Interface/bond", 40, 40, "Open Store", 1);	//Bond

		addButton(31023, 1, "Options/Interface/house", 40, 40, "More Options", 1);	//Empty

	 	//Display Pressed
		addHoverButton(31002, "Options/main tab/display", 2, 40, 40, "Display", -1, 31003, 1);
		addHoveredButton(31003, "Options/main tab/display", 4, 40, 40, 31004);

		//Display Not Pressed
		addHoverButton(31041, "Options/main tab/display", 1, 40, 40, "Display", -1, 31042, 1);
		addHoveredButton(31042, "Options/main tab/display", 3, 40, 40, 31043);

		//Audio Pressed
		addHoverButton(31061, "Options/main tab/audio", 2, 40, 40, "Audio", -1, 31062, 1);
		addHoveredButton(31062, "Options/main tab/audio", 4, 40, 40, 31063);

		//Audio Not Pressed
		addHoverButton(31005, "Options/main tab/audio", 1, 40, 40, "Audio", -1, 31006, 1);
		addHoveredButton(31006, "Options/main tab/audio", 3, 40, 40, 31007);

		//Chat Pressed
		addHoverButton(31051, "Options/main tab/chat", 2, 40, 40, "Chat", -1, 31052, 1);
		addHoveredButton(31052, "Options/main tab/chat", 4, 40, 40, 31053);

		//Chat Not Pressed
		addHoverButton(31008, "Options/main tab/chat", 1, 40, 40, "Chat", -1, 31009, 1);
		addHoveredButton(31009, "Options/main tab/chat", 3, 40, 40, 31010);

		//Controls Pressed
		addHoverButton(31081, "Options/main tab/controls", 2, 40, 40, "Controls", -1, 31082, 1);
		addHoveredButton(31082, "Options/main tab/controls", 4, 40, 40, 31083);

		//Controls Not Pressed
		addHoverButton(31011, "Options/main tab/controls", 1, 40, 40, "Controls", -1, 31012, 1);
		addHoveredButton(31012, "Options/main tab/controls", 3, 40, 40, 31013);

	}
	public static void addSettingsAdvancedOptions(TextDrawingArea[] tda) {
		RSInterface advanced = addInterface(31300);
		//Background
		addSprite(31301, 0, "Options/Interface/5/advanced");

		//Close Hover Button
		addHoverButton(31302, "Options/Interface/5/advanced", 1, 21, 21, "Close", -1, 31303, 1);
		addHoveredButton(31303, "Options/Interface/5/advanced", 2, 21, 21, 31304);

		//Title Text
		addText(31305, "Advanced Options", 0xFF981F, true, true, -1, tda, 2);

		addBankHover(31306, 4, 31307, 1, 2,
				"Options/Interface/5/panel", 40, 40, 775, 1,
				"Toggle Transparent side-panel", 31308, 1, 2,
				"Options/Interface/5/panel", 31309, "", "", 12, 20);

		addBankHover(31310, 4, 31311, 2, 1,
				"Options/Interface/5/xp", 40, 40, 308, 0,
				"Toggle 'Remaining XP' tooltips", 31312, 2, 1,
				"Options/Interface/5/xp", 31313, "", "", 12, 20);

		addBankHover(31314, 4, 31315, 1, 2,
				"Options/Interface/5/roof", 40, 40, 307, 1,
				"Toggle Roof-removal", 31316, 1, 2,
				"Options/Interface/5/roof", 31317, "", "", 12, 20);

		addBankHover(31318, 4, 31319, 1, 2,
				"Options/Interface/5/orbs", 40, 40, 306, 0,
				"Toggle Data orbs", 31320, 1, 2,
				"Options/Interface/5/orbs", 31321, "", "", 12, 20);

		addBankHover(31322, 4, 31323, 1, 2,
				"Options/Interface/5/chat", 40, 40, 776, 1,
				"Toggle Transparent chatbox", 31324, 1, 2,
				"Options/Interface/5/chat", 31325, "", "", 12, 20);

		addBankHover(31326, 4, 31327, 1, 2,
				"Options/Interface/5/tab", 40, 40, 777, 1,
				"Toggle Side-stones arrangement", 31328, 1, 2,
				"Options/Interface/5/tab", 31329, "", "", 12, 20);

		setChildren(16, advanced);
		setBounds(31301, 134, 64, 0, advanced);
		setBounds(31302, 350, 71, 1, advanced);
		setBounds(31303, 350, 71, 2, advanced);
		setBounds(31305, 255, 74, 3, advanced);
		//Transparent side panel
		setBounds(31306, 146, 104, 4, advanced);
		setBounds(31308, 146, 104, 5, advanced);
		//Remaining XP
		setBounds(31310, 206, 104, 6, advanced);
		setBounds(31312, 206, 104, 7, advanced);
		//Roofs
		setBounds(31314, 266, 104, 8, advanced);
		setBounds(31316, 266, 104, 9, advanced);
		//Orbs
		setBounds(31318, 326, 104, 10, advanced);
		setBounds(31320, 326, 104, 11, advanced);
		//Transparent Chat
		setBounds(31322, 146, 158, 12, advanced);
		setBounds(31324, 146, 158, 13, advanced);
		//Side stones arrangement
		setBounds(31326, 146, 212, 14, advanced);
		setBounds(31328, 146, 212, 15, advanced);

	}
	public static void addSettingsWidgetControlsTab(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(31080); // Controls Tab
		addBankHover(31084, 4, 31085, 1, 2,
				"Options/Interface/4/mouse", 40, 40, 313, 0,
				"Toggle number of mouse buttons", 31086, 1, 2,
				"Options/Interface/4/mouse", 31087, "",
				"", 12, 20);

		addButton(31040, 1, "Options/Interface/4/fkeys", 40, 40,
				"Set F-Keys", 1);

		addBankHover(31088, 4, 31089, 1, 2,
				"Options/Interface/4/camera", 40, 40, 314, 0,
				"Toggle Mouse Camera", 31090, 1, 2,
				"Options/Interface/4/camera", 31091, "",
				"", 12, 20);

		addBankHover(31070, 4, 31071, 1, 2,
				"Options/Interface/4/shift", 40, 40, 778, 1,
				"Toggle Shift-Click Drop", 31072, 1, 2,
				"Options/Interface/4/shift", 31073, "",
				"", 12, 20);

		addText(31092, "Attack option priority:", 0xFF981F, false, true, 52,
				tda, 1);

		addBankHover(31093, 4, 31094, 2, 1,
				"Options/Interface/4/check", 15, 15, 315, 1, "Left-click",
				31095, 2, 1, "Options/Interface/4/check", 31096, "",
				"", 12, 20);
		addBankHover(31097, 4, 31098, 1, 2,
				"Options/Interface/4/check", 15, 15, 315, 1, "Right-click",
				31099, 1, 2, "Options/Interface/4/check", 31100, "",
				"", 12, 20);
		addHoverText(31106, "Left-click where available.",
				"Left-click where available.", tda, 0, 0xFF981F, false, true,
				150);

		addHoverText(31107, "Always right-click.", "Always right-click.", tda,
				0, 0xFF981F, false, true, 150);

		setChildren(30, widget);
		setBounds(31001, 3, 45, 0, widget);

		setBounds(31041, 6, 3, 1, widget); // Display
		setBounds(31042, 6, 3, 2, widget);

		setBounds(31005, 52, 3, 24, widget); // Audio
		setBounds(31006, 52, 3, 25, widget);

		setBounds(31008, 98, 3, 26, widget); // Chat
		setBounds(31009, 98, 3, 27, widget);

		setBounds(31081, 144, 3, 3, widget); // Controls
		setBounds(31082, 144, 3, 4, widget);

		setBounds(31014, 6, 221, 5, widget); // Aid
		setBounds(31016, 6, 221, 6, widget); // Aid
		setBounds(31018, 52, 221, 7, widget); // Run
		setBounds(31020, 52, 221, 8, widget); // Run
		setBounds(31022, 144, 221, 9, widget); // House
		setBounds(31023, 98, 221, 10, widget); // Membership Bonds
		setBounds(149, 57, 241, 11, widget); // Run Text
		setBounds(31084, 12, 71, 12, widget); // Mouse
		setBounds(31086, 12, 71, 13, widget); // Mouse
		setBounds(31088, 54, 71, 15, widget); // Camera
		setBounds(31090, 54, 71, 16, widget); // Camera
		setBounds(31040, 96, 71, 14, widget); // fkeys
		setBounds(31070, 138, 71, 28, widget); // Shift Drop
		setBounds(31072, 138, 71, 29, widget); // Shift Drop

		setBounds(31092, 14, 126, 17, widget); // Text
		setBounds(31093, 15, 145, 18, widget); // Check
		setBounds(31095, 15, 145, 19, widget); // Check
		setBounds(31097, 15, 162, 20, widget); // Check
		setBounds(31099, 15, 162, 21, widget); // Check

		setBounds(31106, 33, 146, 22, widget); // Text
		setBounds(31107, 33, 163, 23, widget); // Text
	}
	public static void addSettingsWidgetDisplayTab(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(904);

		//Advanced Options Button
		addButton(31150, 2, "Options/Interface/1/options", 140, 30, "Advanced Options", 1);
		addText(31151, "Advanced Options", 16750623, false, true, 52, tda, 1);

		//Fixed Screen and Resizable Screen Buttons
		addBankHover(31152, 5, 31153, 0, 1,
				"Options/Interface/1/fixed", 62, 52, 771, 0,
				"Fixed Screen", 31154, 0, 1, "Options/Interface/1/fixed",
				31155, "", "", 12, 20);
		addBankHover(31156, 5, 31157, 0, 1,
				"Options/Interface/1/resizable", 62, 52, 771, 1,
				"Resizable Screen", 31158, 0, 1, "Options/Interface/1/resizable",
				31159, "", "", 12, 20);

		/*
		addBankHover(31024, 4, 31025, 1, 2,
				"Options/main tab/orbs", 40, 40, 306, 0,
				"Toggle Data Orbs", 31026, 1, 2,
				"Options/main tab/orbs", 31027, "", "", 12, 20);

		addBankHover(31028, 4, 31029, 1, 2,
				"Options/main tab/roof", 40, 40, 307, 1,
				"Toggle roof-removal", 31030, 1, 2,
				"Options/main tab/roof", 31031, "", "", 12, 20);

		addBankHover(31122, 4, 31123, 1, 2,
				"Options/main tab/pchat", 40, 40, 287, 1,
				"Toggle Split Chat", 31124, 1, 2,
				"Options/main tab/pchat", 31125, "", "", 12, 20);

		addBankHover(31032, 4, 31033, 2, 1,
				"Options/main tab/xp", 40, 40, 308, 0,
				"Toggle 'XP Drop' Most Recent", 31034, 2, 1,
				"Options/main tab/xp", 31035, "", "", 12, 20);
		*/

		addText(149, "", 16750623, false, true, 52, tda, 1);

		addSprite(31035, 3, "Options/Interface/1/light");
		addSprite(31036, 0, "Options/Interface/1/light");

		addBankHover(31037, 5, 31038, 2, 1, "Options/Interface/1/light", 16,
				16, 166, 1, "Adjust Screen Brightness", 31039, 2, 1,
				"Options/Interface/1/light", 31108, "", "", 12, 20);

		addBankHover(31109, 5, 31110, 2, 1, "Options/Interface/1/light", 16,
				16, 166, 2, "Adjust Screen Brightness", 31111, 2, 1,
				"Options/Interface/1/light", 31112, "", "", 12, 20);

		addBankHover(31113, 5, 31114, 2, 1, "Options/Interface/1/light", 16,
				16, 166, 3, "Adjust Screen Brightness", 31115, 2, 1,
				"Options/Interface/1/light", 31116, "", "", 12, 20);

		addBankHover(31117, 5, 31118, 2, 1, "Options/Interface/1/light", 16,
				16, 166, 4, "Adjust Screen Brightness", 31119, 2, 1,
				"Options/Interface/1/light", 31120, "", "", 12, 20);

		addToggleButton(31200, 904, 3, 4, "Options/Interface/1/zoom", 32, 32, "Toggle Scroll Zooming", 0, 774);
		addSprite(31201, 0, "Options/Interface/1/zoom");

		addSlider(37771, 101, 37772, 0, "Options/Interface/1/zoom", 37773, 1, "Options/Interface/1/zoom");

		setChildren(34, widget);
		setBounds(31001, 3, 45, 0, widget);

		setBounds(31002, 6, 3, 1, widget); // Display
		setBounds(31003, 6, 3, 2, widget);

		setBounds(31005, 52, 3, 11, widget); // Audio
		setBounds(31006, 52, 3, 12, widget);

		setBounds(31008, 98, 3, 13, widget); // Chat
		setBounds(31009, 98, 3, 14, widget);

		setBounds(31011, 144, 3, 3, widget); // Controls
		setBounds(31012, 144, 3, 4, widget);

		setBounds(31014, 6, 221, 5, widget); // Aid
		setBounds(31016, 6, 221, 6, widget); // Aid

		setBounds(31018, 52, 221, 7, widget); // Run
		setBounds(31020, 52, 221, 8, widget); // Run

		setBounds(31022, 144, 221, 9, widget); // House
		setBounds(31023, 98, 221, 10, widget); // Membership Bonds

		/*
		setBounds(31024, 23, 119, 11, widget); // Orbs
		setBounds(31026, 23, 119, 12, widget); // Orbs

		setBounds(31028, 75, 119, 13, widget); // Roof
		setBounds(31030, 75, 119, 14, widget); // Roof

		setBounds(31122, 75, 110, 15, widget); // Split-private
		setBounds(31124, 75, 110, 16, widget); // Split-private

		setBounds(31032, 127, 119, 17, widget); // Remaining-XP
		setBounds(31034, 127, 119, 18, widget); // Remaining-XP
		*/

		setBounds(31150, 26, 180, 30, widget); // Advanced Options
		setBounds(31151, 45, 188, 31, widget);

		setBounds(31152, 26, 123, 15, widget); // Fixed
		setBounds(31154, 26, 123, 16, widget); // Fixed

		setBounds(31156, 104, 123, 17, widget); // Resizable
		setBounds(31158, 104, 123, 18, widget); // Resizable

		setBounds(149, 57, 241, 19, widget); // Run Text

		// Zooming bar
		setBounds(31200, 12, 53, 32, widget); // Mouse
		setBounds(37771, 66, 62, 33, widget); // Zoom bar

		//Brightness bar
		setBounds(31035, 12, 89, 20, widget); // Sun
		setBounds(31036, 50, 97, 21, widget); // Bar

		setBounds(31037, 58, 98, 22, widget); // Button
		setBounds(31039, 58, 98, 23, widget); // Button

		setBounds(31109, 90, 98, 24, widget); // Button
		setBounds(31111, 90, 98, 25, widget); // Button

		setBounds(31113, 121, 98, 26, widget); // Button
		setBounds(31115, 121, 98, 27, widget); // Button

		setBounds(31117, 153, 98, 28, widget); // Button
		setBounds(31119, 153, 98, 29, widget); // Button

	}
	public static void addSettingsWidgetSoundTab(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(31500); // Chat Tab


		setChildren(16, widget);
		setBounds(31001, 3, 45, 0, widget);
		setBounds(31041, 6, 3, 1, widget); // Display
		setBounds(31042, 6, 3, 2, widget);
		setBounds(31061, 52, 3, 3, widget); // Audio
		setBounds(31062, 52, 3, 4, widget);
		setBounds(31008, 98, 3, 5, widget); // Chat
		setBounds(31009, 98, 3, 6, widget);
		setBounds(31011, 144, 3, 7, widget); // Controls
		setBounds(31012, 144, 3, 8, widget);

		setBounds(31014, 6, 221, 9, widget); // Aid
		setBounds(31016, 6, 221, 10, widget); // Aid
		setBounds(31018, 52, 221, 11, widget); // Run
		setBounds(31020, 52, 221, 12, widget); // Run
		setBounds(31022, 144, 221, 13, widget); // House
		setBounds(31023, 98, 221, 14, widget); // Membership Bonds
		setBounds(149, 57, 241, 15, widget); // Run Text

	}
	public static void addSettingsWidgetChatTab(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(31400); // Chat Tab

		addBankHover(31401, 4, 31402, 1, 2,
				"Options/Interface/3/pchat", 40, 40, 287, 1,
				"Toggle Split Chat", 31403, 1, 2,
				"Options/Interface/3/pchat", 31404, "",
				"", 12, 20);

		setChildren(18, widget);
		setBounds(31001, 3, 45, 0, widget);
		setBounds(31041, 6, 3, 1, widget); // Display
		setBounds(31042, 6, 3, 2, widget);
		setBounds(31005, 52, 3, 3, widget); // Audio
		setBounds(31006, 52, 3, 4, widget);
		setBounds(31051, 98, 3, 5, widget); // Chat
		setBounds(31052, 98, 3, 6, widget);
		setBounds(31011, 144, 3, 7, widget); // Controls
		setBounds(31012, 144, 3, 8, widget);

		setBounds(31401, 75, 71, 16, widget); //Private Chat Split
		setBounds(31403, 75, 71, 17, widget);

		setBounds(31014, 6, 221, 9, widget); // Aid
		setBounds(31016, 6, 221, 10, widget); // Aid
		setBounds(31018, 52, 221, 11, widget); // Run
		setBounds(31020, 52, 221, 12, widget); // Run
		setBounds(31022, 144, 221, 13, widget); // House
		setBounds(31023, 98, 221, 14, widget); // Membership Bonds
		setBounds(149, 57, 241, 15, widget); // Run Text

	 }

	public static void addSettingsWidget(TextDrawingArea[] tda) {
	 	addSettingsWidgetGlobals(tda);
		addSettingsAdvancedOptions(tda);
		addSettingsWidgetDisplayTab(tda);
		addSettingsWidgetSoundTab(tda);
		addSettingsWidgetChatTab(tda);
		addSettingsWidgetControlsTab(tda);
	}

	public static void bountyHunterWidget(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(28000);
		addHDSprite(28001, 1, "Interfaces/BountyHunter/IMAGE");
		addText(28003, "Target:", tda, 0, 0xFFFF00, false, true);
		addText(28004, "Abnant", tda, 1, 0xFFFFFF, true, true);
		addText(28005, "Lvl 1-4, Cmb 70", tda, 0, 0xCC0000, true, true);
		addText(28006, "Wealth: V. Low", tda, 0, 0xFFFF00, true, true);
		setChildren(14, widget);
		setBounds(28001, 320, 10, 0, widget);
		setBounds(28003, 440, 13, 1, widget);
		setBounds(28004, 458, 26, 2, widget);
		setBounds(28005, 458, 42, 3, widget);
		setBounds(28006, 359, 42, 4, widget);
		setBounds(196, 420, 281, 5, widget);
		setBounds(28030, 345, 20, 6, widget);
		setBounds(28032, 345, 20, 7, widget);
		setBounds(28034, 345, 20, 8, widget);
		setBounds(28036, 345, 20, 9, widget);
		setBounds(28038, 345, 20, 10, widget);
		setBounds(28040, 345, 20, 11, widget);
		setBounds(28020, 0, 0, 12, widget);
		setBounds(28070, 0, 0, 13, widget);
		
		RSInterface sprite;
		int imageId = 2;
		for (int i = 0; i < 12; i += 2) {
    		sprite = addInterface(28030 + i);
    		addSprite(28031 + i, imageId++, "Interfaces/BountyHunter/IMAGE");
    		setChildren(1, sprite);
    		setBounds(28031 + i, 0, 0, 0, sprite);
		}
		
		RSInterface statistics = addInterface(28020);
		setChildren(9, statistics);
		addHDSprite(28021, 0, "Interfaces/BountyHunter/IMAGE");
		addText(28022, "Current  Record", tda, 0, 0xFFFF00, false, true);
		addText(28023, "Rogue:", tda, 0, 0xFFFF00, false, true);
		addText(28024, "Hunter:", tda, 0, 0xFFFF00, false, true);
		addText(28025, "1", tda, 0, 0xFFFF00, true, true);
		addText(28026, "2", tda, 0, 0xFFFF00, true, true);
		addText(28027, "3", tda, 0, 0xFFFF00, true, true);
		addText(28028, "4", tda, 0, 0xFFFF00, true, true);
		addSprite(28029, 8, "Interfaces/BountyHunter/IMAGE");
		
		setBounds(28021, 340, 58, 0, statistics);
		setBounds(28022, 420, 60, 1, statistics);
		setBounds(28023, 375, 73, 2, statistics);
		setBounds(28024, 375, 87, 3, statistics);
		setBounds(28025, 440, 73, 4, statistics);
		setBounds(28026, 440, 87, 5, statistics);
		setBounds(28027, 481, 73, 6, statistics);
		setBounds(28028, 481, 87, 7, statistics);
		setBounds(28029, 347, 74, 8, statistics);
		
		RSInterface timerWidget = addInterface(28070);
		addHDSprite(28071, 10, "Interfaces/BountyHunter/IMAGE");
		addText(28072, "0:59", tda, 0, 0xff9040, true, true);
		setChildren(2, timerWidget);
		setBounds(28071, 293, 10, 0, timerWidget);
		setBounds(28072, 307, 27, 1, timerWidget);
	}
	
	/*public static void initializeTitleWidget(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(33000);
		addSprite(33001, 0, "Interfaces/Titles/IMAGE");
		addSprite(33002, 6, "Interfaces/Titles/IMAGE");
		drawRoundedRectangle(33004, 200, 130, 0x000000, (byte) 30, true, true);
		addButton(33005, 1, "Interfaces/Titles/IMAGE", "Close", 3, 52);
		addButton(33008, 7, "Interfaces/Titles/IMAGE", "Ok");
		addText(33011, "Lorem ipsum dolor sit amet,\\n"
						+ "consectetur adipiscing elit,\\n"
						+ "sed do eiusmod tempor incididunt\\n"
						+ "ut labore et dolore magna aliqua.\\n"
						+ "Ut enim ad minim veniam, quis\\n"
						+ "nostrud exercitation ullamco \\n"
						+ "laboris nisi ut aliquip ex ea\\n"
						+ "commodo consequat.", tda, 1, 0xFF981F, false, true);
		addText(33012, "Purchase", tda, 1, 0xFF981F, true, true);
		drawRoundedRectangle(33013, 140, 22, 0x000000, (byte) 30, true, true);
		addText(33014, "$", tda, 2, 0xFF981F, false, true);
		addText(33015, "45,000GP", tda, 1, 0xFF981F, false, true);
		addButton(33016, 9, "Interfaces/Titles/IMAGE", "Information");
		addSprites(33017, "Interfaces/Titles/IMAGE", 11, 12, 13);
		setChildren(15, widget);
		setBounds(33001, 56, 45, 0, widget);
		setBounds(33002, 250, 80, 1, widget);
		setBounds(33004, 223, 90, 2, widget);
		setBounds(33005, 435, 51, 3, widget);
		setBounds(33008, 277, 250, 4, widget);
		setBounds(33011, 230, 100, 5, widget);
		setBounds(33012, 320, 255, 6, widget);
		setBounds(33013, 250, 224, 7, widget);
		setBounds(33014, 255, 228, 8, widget);
		setBounds(33015, 268, 228, 9, widget);
		setBounds(33016, 418, 51, 10, widget);
		setBounds(33017, 370, 221, 11, widget);
		setBounds(33049, 61, 51, 12, widget);
		setBounds(33030, 0, 0, 13, widget);
		setBounds(33035, 0, 0, 14, widget);
		
		RSInterface scroll = addInterface(33049);
		scroll.width = 114;
		scroll.height = 230;
		scroll.scrollMax = 550;
		setChildren(50, scroll);
		
		for (int i = 0; i < 50; i += 2) {
			addClickableSprites(33050 + i, "View", "Interfaces/Titles/IMAGE", 3, 4, 5, 14);
			addText(33050 + i + 1, "Entry "+ (1 + i / 2), tda, 1, 0xFF981F, false, true);
			setBounds(33050 + i, 0, i / 2 * 22, i, scroll);
			setBounds(33050 + i + 1, 4, 3 + (i / 2 * 22), i + 1, scroll);
		}
		
		widget = addInterface(33030);
		setChildren(1, widget);
		addText(33031, "Selected Title", tda, 2, 0xFF981F, true, true);
		setBounds(33031, 320, 66, 0, widget);
		
		widget = addInterface(33035);
		setChildren(1, widget);
		addInputField(33036, 16, 0xFF981F, "Custom title", 120, 22, false, true, "[A-Za-z0-9 ]");
		setBounds(33036, 262, 58, 0, widget);
	}*/

	
	public static void slayerRewardsBuy(TextDrawingArea[] tda) {
		RSInterface rsi = addTab(25400);
		int j = 0;
		addSprite(25401, 1, "Slayer/PANEL");
		addButton(25402, 0, "Slayer/TAB", "View", 27640, 1, 75, 23);
		addButton(25403, 1, "Slayer/TAB", "View", 27640, 1, 75, 23);
		addButton(25404, 1, "Slayer/TAB", "View", 27640, 1, 75, 23);
		addText(25405, "Buy", tda, 0, 0xff9040);
		addText(25406, "Learn", tda, 0, 0xff9040);
		addText(25407, "Assignment", tda, 0, 0xff9040);
		addText(25408, "Slayer Points : ", tda, 2, 0xff9040);
		addText(25409, "XXXX", tda, 2, 0xff9040);
		addHoverText(25510, "Close window", "Close window", tda, 1, 0xff9040,
				false, true, 73);
		addHoverText(25411, "Purchase slayer experience", "Slayer experience",
				tda, 1, 0xff9040, false, true, 205);
		addText(25412, "(50 Points)", tda, 0, 0xff9040);
		addHoverText(25413, "Purchase Slayer's Respite(4)",
				"Slayer's Respite(4)", tda, 1, 0xff9040, false, true, 205);
		addText(25414, "(25 Points)", tda, 0, 0xff9040);
		addHoverText(25415, "Purchase 250 Slayer dart casts",
				"Slayer dart casts", tda, 1, 0xff9040, false, true, 205);
		addText(25416, "(35 Points)", tda, 0, 0xff9040);
		addHoverText(25417, "Nothing yet!", "Nothing here!", tda, 1, 0xff9040,
				false, true, 205);
		addText(25418, "(35 Points)", tda, 0, 0xff9040);
		rsi.totalChildren(18);
		rsi.child(j, 25401, 10, 10);
		j++;
		rsi.child(j, 25402, 20, 20);
		j++;
		rsi.child(j, 25403, 98, 20);
		j++;
		rsi.child(j, 25404, 176, 20);
		j++;
		rsi.child(j, 25405, 40, 25);
		j++;
		rsi.child(j, 25406, 118, 25);
		j++;
		rsi.child(j, 25407, 186, 25);
		j++;
		rsi.child(j, 25408, 85, 75);
		j++;
		rsi.child(j, 25409, 200, 75);
		j++;
		rsi.child(j, 25510, 409, 20);
		j++;
		rsi.child(j, 25411, 161, 153);
		j++;
		rsi.child(j, 25412, 409, 157);
		j++;
		rsi.child(j, 25413, 161, 190);
		j++;
		rsi.child(j, 25414, 409, 194);
		j++;
		rsi.child(j, 25415, 161, 233);
		j++;
		rsi.child(j, 25416, 409, 237);
		j++;
		rsi.child(j, 25417, 161, 270);
		j++;
		rsi.child(j, 25418, 409, 274);
		j++;
	}

	public static void slayerRewardsLearn(TextDrawingArea[] tda) {
		RSInterface rsi = addTab(25200);
		int j = 0;
		addSprite(25201, 2, "Slayer/PANEL");
		addButton(25202, 1, "Slayer/TAB", "View", 27640, 1, 75, 23);
		addButton(25203, 0, "Slayer/TAB", "View", 27640, 1, 75, 23);
		addButton(25204, 1, "Slayer/TAB", "View", 27640, 1, 75, 23);
		addText(25205, "Buy", tda, 0, 0xff9040);
		addText(25206, "Learn", tda, 0, 0xff9040);
		addText(25207, "Assignment", tda, 0, 0xff9040);
		addText(25208, "Slayer Points : ", tda, 2, 0xff9040);
		addText(25209, "XXXX", tda, 2, 0xff9040);
		addHoverText(25210, "Close window", "Close window", tda, 1, 0xff9040,
				false, true, 73);
		addHoverText(25211, "Unlock the ability to create a slayer helmet",
				"Unlock", tda, 1, 0xff9040, false, true, 205);
		addText(25212, "(300 Points)", tda, 0, 0xff9040);
		addHoverText(25213, "Unlock the ability to create rings of slaying",
				"Unlock", tda, 1, 0xff9040, false, true, 205);
		addText(25214, "(100 Points)", tda, 0, 0xff9040);
		rsi.totalChildren(14);
		rsi.child(j, 25201, 10, 10);
		j++;
		rsi.child(j, 25202, 20, 20);
		j++;
		rsi.child(j, 25203, 98, 20);
		j++;
		rsi.child(j, 25204, 176, 20);
		j++;
		rsi.child(j, 25205, 40, 25);
		j++;
		rsi.child(j, 25206, 118, 25);
		j++;
		rsi.child(j, 25207, 186, 25);
		j++;
		rsi.child(j, 25208, 85, 75);
		j++;
		rsi.child(j, 25209, 200, 75);
		j++;
		rsi.child(j, 25210, 409, 20);
		j++;
		rsi.child(j, 25211, 150, 165);
		j++;
		rsi.child(j, 25212, 409, 170);
		j++;
		rsi.child(j, 25213, 150, 225);
		j++;
		rsi.child(j, 25214, 409, 230);
		j++;
	}

	public static void slayerRewardsAssign(TextDrawingArea[] tda) {
		RSInterface rsi = addTab(25300);
		int j = 0;
		addSprite(25301, 3, "Slayer/PANEL");
		addButton(25602, 1, "Slayer/TAB", "View", 27640, 1, 75, 23);
		addButton(25603, 1, "Slayer/TAB", "View", 27640, 1, 75, 23);
		addButton(25604, 0, "Slayer/TAB", "View", 27640, 1, 75, 23);
		addText(25305, "Buy", tda, 0, 0xff9040);
		addText(25306, "Learn", tda, 0, 0xff9040);
		addText(25307, "Assignment", tda, 0, 0xff9040);
		addText(25308, "Slayer Points : ", tda, 2, 0xff9040);
		addText(25309, "XXXX", tda, 2, 0xff9040);
		addHoverText(25310, "Close window", "Close window", tda, 1, 0xff9040,
				false, true, 73);
		addHoverText(25311, "Cancel your current task",
				"Cancel your current task", tda, 1, 0xff9040, false, true, 140);
		addText(25312, "(30 Points)", tda, 0, 0xff9040);
		addHoverText(25313, "Cancel and stop assigning current task",
				"Cancel task and stop it from being assigned", tda, 1,
				0xff9040, false, true, 225);
		addText(25314, "(100 Points)", tda, 0, 0xff9040);
		addText(25315, "line 1", tda, 1, 0xFF981F);
		addText(25316, "line 2", tda, 1, 0xFF981F);
		addText(25317, "line 3", tda, 1, 0xFF981F);
		addText(25318, "line 4", tda, 1, 0xFF981F);
		addButton(25319, 6, "Slayer/IMAGE", "Delete removed slayer task");
		addButton(25320, 6, "Slayer/IMAGE", "Delete removed slayer task");
		addButton(25321, 6, "Slayer/IMAGE", "Delete removed slayer task");
		addButton(25322, 6, "Slayer/IMAGE", "Delete removed slayer task");
		rsi.totalChildren(22);
		rsi.child(j, 25301, 10, 10);
		j++;
		rsi.child(j, 25602, 20, 20);
		j++;
		rsi.child(j, 25603, 98, 20);
		j++;
		rsi.child(j, 25604, 176, 20);
		j++;
		rsi.child(j, 25305, 40, 25);
		j++;
		rsi.child(j, 25306, 118, 25);
		j++;
		rsi.child(j, 25307, 186, 25);
		j++;
		rsi.child(j, 25308, 85, 75);
		j++;
		rsi.child(j, 25309, 200, 75);
		j++;
		rsi.child(j, 25310, 409, 20);
		j++;
		rsi.child(j, 25311, 87, 130);
		j++;
		rsi.child(j, 25312, 409, 134);
		j++;
		rsi.child(j, 25313, 87, 150);
		j++;
		rsi.child(j, 25314, 409, 154);
		j++;
		rsi.child(j, 25315, 87, 205);
		j++;
		rsi.child(j, 25316, 87, 225);
		j++;
		rsi.child(j, 25317, 87, 245);
		j++;
		rsi.child(j, 25318, 87, 265);
		j++;
		rsi.child(j, 25319, 400, 205);
		j++;
		rsi.child(j, 25320, 400, 225);
		j++;
		rsi.child(j, 25321, 400, 245);
		j++;
		rsi.child(j, 25322, 400, 265);
		j++;
	}
	
	public static void questTab(TextDrawingArea[] TDA) {
		RSInterface Interface = addInterface(638);
		setChildren(6, Interface);
		addText(29155, "ServerName", 0xFF981F, false, true, 52, TDA, 2);
		//addText(29156, "", 0xFF981F, false, true, 52, TDA, 2);
		addSprite(29157, 0, "Interfaces/QuestTab/QUEST");
//		addSprite(29157, 0, "looting/BAG");
		addButton(29156, 2, "Interfaces/QuestTab/QUEST", 18, 18, "View Quests", 1);
		addButton(29158, 1, "Interfaces/QuestTab/QUEST", 18, 18, "View Achievements", 1);
		addButton(29275, 3, "Interfaces/QuestTab/QUEST", 18, 18, "View Titles", 1);
		setBounds(29155, 10, 5, 0, Interface);
		setBounds(29157, 3, 24, 2, Interface);
		setBounds(29160, 5, 29, 4, Interface);
		setBounds(29158, 165, 5, 3, Interface);	// Achievements
		setBounds(29275, 125, 5, 5, Interface); 	// Titles
		setBounds(29156, 145, 5, 1, Interface);	// Quests
		Interface = addInterface(29160);
		Interface.height = 214;
		Interface.width = 165;
		Interface.scrollMax = 1700;
		Interface.newScroller = false;
		setChildren(105, Interface);
		addText(29161, "Server Information", 16750623, false, true, 52, TDA, 2);
		addHoverText(29162, "", "View", TDA, 0, 16711680, false, true, 150);
		addHoverText(29163, "", "View", TDA, 0, 16711680, false, true, 150);
		addHoverText(29164, "", "View", TDA, 0, 16711680, false, true, 150);
		addHoverText(29165, "", "View", TDA, 0, 16711680, false, true, 150);
		addHoverText(29166, "", "View", TDA, 0, 16711680, false, true, 150);
		addHoverText(29167, "", "View", TDA, 0, 16711680, false, true, 150);
		addText(29168, "Player Information", 16750623, false, true, 52, TDA, 2);
		removeSomething(663);
		//addHoverText(663, "", "View", TDA, 0, 16711680, false, true, 150);
 		setBounds(29161, 4, 4, 0, Interface);
 		setBounds(29162, 8, 22, 1, Interface);
 		setBounds(29163, 8, 35, 2, Interface);
 		setBounds(29164, 8, 48, 3, Interface);
 		setBounds(29165, 8, 61, 4, Interface);
		setBounds(29166, 8, 74, 5, Interface);
		setBounds(29167, 8, 87, 6, Interface);
		setBounds(29168, 4, 100, 7, Interface);
 		setBounds(663, 4, 87, 8, Interface);
		int Ypos = 118;
		int frameID = 9;
		for (int iD = 29169; iD <= 29264; iD++) {
			addHoverText(iD, "", "View"/* "View Quest Journal, "+iD */, TDA,
					0, 0xff0000, false, true, 150);
			setBounds(iD, 8, Ypos, frameID, Interface);
			frameID++;
			Ypos += 13;
			Ypos++;
		}

		Interface = addInterface(29265);
		try {
			setChildren(4, Interface);
			addSprite(39700, 0, "looting/BAG");
			addText(29266, "        Achievements", 0xFF981F, false, true, -1,
					TDA, 2);
			/*addButton(29267, 2, "Interfaces/QuestTab/QUEST", 18, 18,
					"Swap to Quests", 1);*/
			addSprite(29269, 0, "Interfaces/QuestTab/QUEST");
			//tab.child(0, 26701, 9, 21);
			setBounds(39700, 9, 21, 0, Interface);
			//setBounds(29267, 165, 5, 1, Interface);
			setBounds(29266, 10, 5, 1, Interface);
			setBounds(29269, 3, 24, 2, Interface);
			setBounds(29268, 5, 29, 3, Interface);
			
			Interface = addInterface(29268);
			Interface.height = 214;
			Interface.width = 165;
			Interface.scrollMax = 215;
			Interface.newScroller = false;
			setChildren(9, Interface);
			setBounds(29295, 8, 6, 0, Interface);
			setBounds(29287, 8, 21, 1, Interface);
			setBounds(29305, 8, 36, 2, Interface);
			setBounds(29306, 8, 51, 3, Interface);
			setBounds(29307, 8, 66, 4, Interface);
			setBounds(29308, 8, 81, 5, Interface);
			setBounds(29309, 8, 96, 6, Interface);
			setBounds(29310, 8, 110, 7, Interface);
			setBounds(29311, 8, 125, 8, Interface);
			addHoverText(29295, "Tasks Completed 0/6", "View Achievements",
					TDA, 0, 65280, false, true, 150);
			addHoverText(29287, "Easy Tasks", "View Achievements", TDA, 0,
					0xFF981F, false, true, 150);
			addHoverText(29305, "Light 100 Fires", "View Achievements", TDA, 0,
					0xff0000, false, true, 150);
			addHoverText(29306, "Catch 100 Fish", "View Achievements", TDA, 0,
					0xff0000, false, true, 150);
			addHoverText(29307, "Kill 30 Rockcrabs", "View Achievements", TDA,
					0, 0xff0000, false, true, 150);
			addHoverText(29308, "Chop 100 Trees", "View Achievements", TDA, 0,
					0xff0000, false, true, 150);
			addHoverText(29309, "Earn 35 Pk Points", "View Achievements", TDA,
					0, 0xff0000, false, true, 150);
			addHoverText(29310, "Medium Tasks", "View Achievements", TDA, 0,
					0xFF981F, false, true, 150);
			addHoverText(29311, "Coming Soon...", "View Achievements", TDA, 0,
					0xff0000, false, true, 150);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
private static void tradeUIAddon(TextDrawingArea[] tda) {
		
		RSInterface main = addInterface(55000);
		interfaceCache[3557].message = "";
		interfaceCache[3558].message = "";
		
		setChildren(3, main);
		
		setBounds(3443, 0, 0, 0, main);
		setBounds(55010, 25, 85, 1, main);
		setBounds(55050, 265, 85, 2, main);
		
		RSInterface widget = addInterface(55010);
		setChildren(28, widget);
		widget.width = 213;
		widget.height = 205;
		widget.scrollMax = 28 * 15;
		for (int child = 0; child < widget.children.length; child++) {
			addText(55011 + child, "", tda, 2, 0xFFFFFF, true, true);
			setBounds(55011 + child, 100, child * 15, child, widget);
		}
		
		widget = addInterface(55050);
		setChildren(28, widget);
		widget.width = 213;
		widget.height = 205;
		widget.scrollMax = 28 * 15;
		for (int child = 0; child < widget.children.length; child++) {
			addText(55051 + child, "", tda, 2, 0xFFFFFF, true, true);
			setBounds(55051 + child, 100, child * 15, child, widget);
		}
		
	}
	
	public static void updateShopWidget(TextDrawingArea[] tda) {
		RSInterface widget = interfaceCache[3824];
		int[] childrenId = new int[widget.children.length + 3];
		int[] childrenX = new int[widget.children.length + 3];
		int[] childrenY = new int[widget.children.length + 3];
		for (int i = 0; i < widget.children.length; i++) {
			childrenId[i] = widget.children[i];
			childrenX[i] = widget.childX[i];
			childrenY[i] = widget.childY[i];
		}
		addHover(28056, 3, 0, 28057, 8, "Interfaces/Equipment/CUSTOM", 21, 21, "Close Window");
		addHovered(28057, 9, "Interfaces/Equipment/CUSTOM", 21, 21, 28058);
		addText(3902, "", 16750623, false, true, 52, tda, 1);
		setChildren(95, widget);
		for (int i = 0; i < widget.children.length; i++) {
			setBounds(childrenId[i], childrenX[i], childrenY[i], i, widget);
		}
		setBounds(28050, 0, 0, 92, widget);
		setBounds(28056, 472, 27, 93, widget);
		setBounds(28057, 472, 27, 94, widget);
		
		RSInterface subWidget = addInterface(28050);
		setChildren(2, subWidget);
		// Shop currencies sprites
		addSprites(28051, "Interfaces/ShopCurrencies/IMAGE", 0, 1, 2, 3, 4, 5, 6, 7, 8);
		addText(28052, "Currency:", tda, 1, 0xFFFF00, false, true);
		setBounds(28051, 20, 26, 0, subWidget);
		setBounds(28052, 48, 30, 1, subWidget);
	}


	
	public static void removeConfig(int id) {
		@SuppressWarnings("unused")
		RSInterface rsi = interfaceCache[id] = new RSInterface();
	}
	
	public static void itemsOnDeathDATA(TextDrawingArea[] tda) {
		RSInterface RSinterface = addInterface(17115);
		addText(17109, "", 0xff981f, false, false, 0, tda, 0);
		addText(17110, "The normal amount of", 0xff981f, false, false, 0, tda,
				0);
		addText(17111, "items kept is three.", 0xff981f, false, false, 0, tda,
				0);
		addText(17112, "", 0xff981f, false, false, 0, tda, 0);
		addText(17113, "If you are skulled,", 0xff981f, false, false, 0, tda, 0);
		addText(17114, "you will lose all your", 0xff981f, false, false, 0,
				tda, 0);
		addText(17117, "items, unless an item", 0xff981f, false, false, 0, tda,
				0);
		addText(17118, "protecting prayer is", 0xff981f, false, false, 0, tda,
				0);
		addText(17119, "used.", 0xff981f, false, false, 0, tda, 0);
		addText(17120, "", 0xff981f, false, false, 0, tda, 0);
		addText(17121, "Item protecting prayers", 0xff981f, false, false, 0,
				tda, 0);
		addText(17122, "will allow you to keep", 0xff981f, false, false, 0,
				tda, 0);
		addText(17123, "one extra item.", 0xff981f, false, false, 0, tda, 0);
		addText(17124, "", 0xff981f, false, false, 0, tda, 0);
		addText(17125, "The items kept are", 0xff981f, false, false, 0, tda, 0);
		addText(17126, "selected by the server", 0xff981f, false, false, 0,
				tda, 0);
		addText(17127, "and include the most", 0xff981f, false, false, 0, tda,
				0);
		addText(17128, "expensive items you're", 0xff981f, false, false, 0,
				tda, 0);
		addText(17129, "carrying.", 0xff981f, false, false, 0, tda, 0);
		addText(17130, "", 0xff981f, false, false, 0, tda, 0);
		RSinterface.parentID = 17115;
		RSinterface.id = 17115;
		RSinterface.type = 0;
		RSinterface.atActionType = 0;
		RSinterface.contentType = 0;
		RSinterface.width = 130;
		RSinterface.height = 197;
		RSinterface.aByte254 = 0;
		RSinterface.mOverInterToTrigger = -1;
		RSinterface.scrollMax = 280;
		RSinterface.children = new int[20];
		RSinterface.childX = new int[20];
		RSinterface.childY = new int[20];
		RSinterface.children[0] = 17109;
		RSinterface.childX[0] = 0;
		RSinterface.childY[0] = 0;
		RSinterface.children[1] = 17110;
		RSinterface.childX[1] = 0;
		RSinterface.childY[1] = 12;
		RSinterface.children[2] = 17111;
		RSinterface.childX[2] = 0;
		RSinterface.childY[2] = 24;
		RSinterface.children[3] = 17112;
		RSinterface.childX[3] = 0;
		RSinterface.childY[3] = 36;
		RSinterface.children[4] = 17113;
		RSinterface.childX[4] = 0;
		RSinterface.childY[4] = 48;
		RSinterface.children[5] = 17114;
		RSinterface.childX[5] = 0;
		RSinterface.childY[5] = 60;
		RSinterface.children[6] = 17117;
		RSinterface.childX[6] = 0;
		RSinterface.childY[6] = 72;
		RSinterface.children[7] = 17118;
		RSinterface.childX[7] = 0;
		RSinterface.childY[7] = 84;
		RSinterface.children[8] = 17119;
		RSinterface.childX[8] = 0;
		RSinterface.childY[8] = 96;
		RSinterface.children[9] = 17120;
		RSinterface.childX[9] = 0;
		RSinterface.childY[9] = 108;
		RSinterface.children[10] = 17121;
		RSinterface.childX[10] = 0;
		RSinterface.childY[10] = 120;
		RSinterface.children[11] = 17122;
		RSinterface.childX[11] = 0;
		RSinterface.childY[11] = 132;
		RSinterface.children[12] = 17123;
		RSinterface.childX[12] = 0;
		RSinterface.childY[12] = 144;
		RSinterface.children[13] = 17124;
		RSinterface.childX[13] = 0;
		RSinterface.childY[13] = 156;
		RSinterface.children[14] = 17125;
		RSinterface.childX[14] = 0;
		RSinterface.childY[14] = 168;
		RSinterface.children[15] = 17126;
		RSinterface.childX[15] = 0;
		RSinterface.childY[15] = 180;
		RSinterface.children[16] = 17127;
		RSinterface.childX[16] = 0;
		RSinterface.childY[16] = 192;
		RSinterface.children[17] = 17128;
		RSinterface.childX[17] = 0;
		RSinterface.childY[17] = 204;
		RSinterface.children[18] = 17129;
		RSinterface.childX[18] = 0;
		RSinterface.childY[18] = 216;
		RSinterface.children[19] = 17130;
		RSinterface.childX[19] = 0;
		RSinterface.childY[19] = 228;
	}
	
	public static void itemsKeptOnDeath(TextDrawingArea[] tda) {
		RSInterface Interface = addInterface(22030);
		addSprite(22031, 1, "Interfaces/Death/SPRITE");
		addHoverButton(22032, "Interfaces/Death/SPRITE", 2, 17, 17, "Close",
				250, 22033, 3);
		addHoveredButton(22033, "Interfaces/Death/SPRITE", 3, 17, 17, 22034);
		addText(22035, "", tda, 0, 0xff981f, false, true);
		addText(22036, "", tda, 0, 0xff981f, false, true);
		addText(22037, "", tda, 0, 0xff981f, false, true);
		addText(22038, "", tda, 0, 0xff981f, false, true);
		addText(22039, "", tda, 0, 0xff981f, false, true);
		addText(22040, "", tda, 1, 0xffcc33, false, true);
		setChildren(9, Interface);
		setBounds(22031, 7, 8, 0, Interface);
		setBounds(22032, 480, 18, 1, Interface);
		setBounds(22033, 480, 18, 2, Interface);
		setBounds(22035, 348, 98, 3, Interface);
		setBounds(22036, 348, 110, 4, Interface);
		setBounds(22037, 348, 122, 5, Interface);
		setBounds(22038, 348, 134, 6, Interface);
		setBounds(22039, 348, 146, 7, Interface);
		setBounds(22040, 398, 297, 8, Interface);
	}	
	
	

	
	public static void itemsOnDeath(TextDrawingArea[] wid) {
		RSInterface rsinterface = addInterface(17100);
		addSprite(17101, 2, 2);
		// addHover(17102,"Items Kept On Death/SPRITE", 1, 17, 17, "Close", 0,
		// 10602, 1);
		// addHovered(10602,"Items Kept On Death/SPRITE", 3, 17, 17, 10603);
		addText(17103, "Items kept on death", wid, 2, 0xff981f);
		addText(17104, "Items I will keep...", wid, 1, 0xff981f);
		addText(17105, "Items I will lose...", wid, 1, 0xff981f);
		addText(17106, "Info", wid, 1, 0xff981f);
		addText(17107, "", wid, 1, 0xffcc33);
		addText(17108, "", wid, 1, 0xffcc33);
		// rsinterface.scrollMax = 50;
		//Don't allow players to move items inside the interface
		interfaceCache[10494].replaceItems = false;
		rsinterface.replaceItems = false;
		rsinterface.isMouseoverTriggered = false;
		rsinterface.children = new int[12];
		rsinterface.childX = new int[12];
		rsinterface.childY = new int[12];
		rsinterface.children[0] = 17101;
		rsinterface.childX[0] = 7;
		rsinterface.childY[0] = 8;
		rsinterface.children[1] = 32004; //15210 is old close button
		rsinterface.childX[1] = 478;
		rsinterface.childY[1] = 17;
		rsinterface.children[2] = 17103;
		rsinterface.childX[2] = 185;
		rsinterface.childY[2] = 18;
		rsinterface.children[3] = 17104;
		rsinterface.childX[3] = 22;
		rsinterface.childY[3] = 49;
		rsinterface.children[4] = 17105;
		rsinterface.childX[4] = 22;
		rsinterface.childY[4] = 109;
		rsinterface.children[5] = 17106;
		rsinterface.childX[5] = 347;
		rsinterface.childY[5] = 49;
		rsinterface.children[6] = 17107;
		rsinterface.childX[6] = 348;
		rsinterface.childY[6] = 270;
		rsinterface.children[7] = 17108;
		rsinterface.childX[7] = 401;
		rsinterface.childY[7] = 293;
		rsinterface.children[8] = 17115;
		rsinterface.childX[8] = 348;
		rsinterface.childY[8] = 64;
		rsinterface.children[9] = 10494;
		rsinterface.childX[9] = 26;
		rsinterface.childY[9] = 71;
		rsinterface.children[10] = 10600;
		rsinterface.childX[10] = 26;
		rsinterface.childY[10] = 129;
		rsinterface.children[11] = 15211;
		rsinterface.childX[11] = 478;
		rsinterface.childY[11] = 17;
		rsinterface = interfaceCache[10494];
		rsinterface.spritePaddingX = 6;
		rsinterface.spritePaddingY = 5;
		rsinterface = interfaceCache[10600];
		rsinterface.spritePaddingX = 6;
		rsinterface.spritePaddingY = 5;
	}
	
	public static void quickPrayers(final TextDrawingArea[] TDA) {
		int i = 0;
		final RSInterface localRSInterface = addInterface(17200);
		addSprite(17201, 3, "QuickPrayer/Sprite");
		addText(17240, "Select your quick prayers:", TDA, 0, 16750623, false,
				true);
		addTransparentSprite(17249, 0, "QuickPrayer/SPRITE");
		int j = 17202;
		for (int k = 630; j <= 17229 || k <= 659; ++k) {
			addConfigButton(j, 17200, 2, 1, "QuickPrayer/Sprite", 14, 15,
					"Select", 0, 1, k);
			j++;
		}
		addHoverButton(17241, "QuickPrayer/SPRITE", 4, 190, 24,
				"Confirm Selection", -1, 17242, 1);
		addHoveredButton(17242, "QuickPrayer/SPRITE", 5, 190, 24, 17243);
		setChildren(62, localRSInterface);
		// --- Prayer sprites ---
		//1st row
		setBounds(25001, 5, 28, i++, localRSInterface);
		setBounds(25003, 44, 28, i++, localRSInterface);
		setBounds(25005, 79, 31, i++, localRSInterface);
		setBounds(25007, 116, 30, i++, localRSInterface);
		setBounds(25009, 153, 29, i++, localRSInterface);
		//2nd row
		setBounds(25011, 5, 64, i++, localRSInterface);
		setBounds(25013, 44, 63, i++, localRSInterface);
		setBounds(25015, 79, 65, i++, localRSInterface);
		setBounds(25017, 116, 66, i++, localRSInterface);
		setBounds(25019, 154, 66, i++, localRSInterface);
		//3rd row
		setBounds(25021, 4, 99, i++, localRSInterface);
		setBounds(25023, 44, 102, i++, localRSInterface);
		setBounds(25025, 81, 100, i++, localRSInterface);
		setBounds(25027, 117, 100, i++, localRSInterface);
		setBounds(25029, 156, 102, i++, localRSInterface);
		//4th row
		setBounds(25031, 5, 137, i++, localRSInterface);
		setBounds(25033, 43, 136, i++, localRSInterface);
		setBounds(25035, 83, 136, i++, localRSInterface);
		setBounds(25037, 115, 133, i++, localRSInterface);
		setBounds(25039, 154, 136, i++, localRSInterface);
		//5th row
		setBounds(25041, 5, 172, i++, localRSInterface);
		setBounds(25043, 41, 170, i++, localRSInterface);
		setBounds(25045, 79, 175, i++, localRSInterface);
		setBounds(25047, 116, 170, i++, localRSInterface);
		setBounds(25049, 161, 172, i++, localRSInterface);
		//6th row
		setBounds(25051, 4, 215, i++, localRSInterface);
		setBounds(25053, 43, 207, i++, localRSInterface);
		setBounds(25055, 80, 207, i++, localRSInterface);

		// setBounds(18061, 78, 212, i++, localRSInterface);
		// setBounds(18121, 116, 208, i++, localRSInterface);
		setBounds(17249, 0, 25, i++, localRSInterface);
		setBounds(17201, 0, 22, i++, localRSInterface);
		setBounds(17201, 0, 237, i++, localRSInterface);

		// --- Select buttons ---
		//1st row
		setBounds(17202, 2, 25, i++, localRSInterface);
		setBounds(17203, 39, 25, i++, localRSInterface);
		setBounds(17204, 76, 25, i++, localRSInterface);
		setBounds(17205, 113, 25, i++, localRSInterface);
		setBounds(17206, 150, 25, i++, localRSInterface);
		//2nd row
		setBounds(17207, 2, 61, i++, localRSInterface);
		setBounds(17208, 39, 61, i++, localRSInterface);
		setBounds(17209, 76, 61, i++, localRSInterface);
		setBounds(17210, 113, 61, i++, localRSInterface);
		setBounds(17211, 150, 61, i++, localRSInterface);
		//3rd row
		setBounds(17212, 2, 97, i++, localRSInterface);
		setBounds(17213, 39, 97, i++, localRSInterface);
		setBounds(17214, 76, 97, i++, localRSInterface);
		setBounds(17215, 113, 97, i++, localRSInterface);
		setBounds(17216, 150, 97, i++, localRSInterface);
		//4th row
		setBounds(17217, 2, 133, i++, localRSInterface);
		setBounds(17218, 39, 133, i++, localRSInterface);
		setBounds(17219, 76, 133, i++, localRSInterface);
		setBounds(17220, 113, 133, i++, localRSInterface);
		setBounds(17221, 150, 133, i++, localRSInterface);
		//5th row
		setBounds(17222, 2, 169, i++, localRSInterface);
		setBounds(17223, 39, 169, i++, localRSInterface);
		setBounds(17224, 76, 169, i++, localRSInterface);
		setBounds(17225, 113, 169, i++, localRSInterface);
		setBounds(17226, 150, 169, i++, localRSInterface);
		//6th row
		setBounds(17227, 2, 205, i++, localRSInterface);
		setBounds(17228, 39, 205, i++, localRSInterface);
		setBounds(17229, 76, 205, i++, localRSInterface);

		setBounds(17240, 5, 5, i++, localRSInterface);
		setBounds(17241, 0, 237, i++, localRSInterface);
		setBounds(17242, 0, 237, i++, localRSInterface);
		j = 0;
		addPrayerWithTooltip(25000, 0, 83, 0, j, 25052,
				"Activate @lre@Thick Skin");
		j++;
		addPrayerWithTooltip(25002, 0, 84, 3, 0, 25054,
				"Activate @lre@Burst of Strength");

		addSprite(25003, 1, "QuickPrayer/normal/PRAYERON");
		j++;
		addPrayerWithTooltip(25004, 0, 85, 6, j, 25056,
				"Activate @lre@Clarity of Thought");
		j++;
		addPrayerWithTooltip(25006, 0, 601, 7, j, 25058,
				"Activate @lre@Sharp Eye");
		j++;
		addPrayerWithTooltip(25008, 0, 602, 8, j, 25060,
				"Activate @lre@Mystic Will");
		j++;
		addPrayerWithTooltip(25010, 0, 86, 9, j, 25062,
				"Activate @lre@Rock Skin");
		j++;
		addPrayerWithTooltip(25012, 0, 87, 12, j, 25064,
				"Activate @lre@Superhuman Strength");
		j++;
		addPrayerWithTooltip(25014, 0, 88, 15, j, 25066,
				"Activate @lre@Improved Reflexes");
		j++;
		addPrayerWithTooltip(25016, 0, 89, 18, j, 25068,
				"Activate @lre@Rapid Restore");
		j++;
		addPrayerWithTooltip(25018, 0, 90, 21, j, 25070,
				"Activate @lre@Rapid Heal");
		j++;
		addPrayerWithTooltip(25020, 0, 91, 24, j, 25072,
				"Activate @lre@Protect Item");
		j++;
		addPrayerWithTooltip(25022, 0, 603, 25, j, 25074,
				"Activate @lre@Hawk Eye");
		j++;
		addPrayerWithTooltip(25024, 0, 604, 26, j, 25076,
				"Activate @lre@Mystic Lore");
		j++;
		addPrayerWithTooltip(25026, 0, 92, 27, j, 25078,
				"Activate @lre@Steel Skin");
		j++;
		addPrayerWithTooltip(25028, 0, 93, 30, j, 25080,
				"Activate @lre@Ultimate Strength");
		j++;
		addPrayerWithTooltip(25030, 0, 94, 33, j, 25082,
				"Activate @lre@Incredible Reflexes");
		j++;
		addPrayerWithTooltip(25032, 0, 95, 36, j, 25084,
				"Activate @lre@Protect from Magic");
		j++;
		addPrayerWithTooltip(25034, 0, 96, 39, j, 25086,
				"Activate @lre@Protect from Missles");
		j++;
		addPrayerWithTooltip(25036, 0, 97, 42, j, 25088,
				"Activate @lre@Protect from Melee");
		j++;
		addPrayerWithTooltip(25038, 0, 605, 43, j, 25090,
				"Activate @lre@Eagle Eye");
		j++;
		addPrayerWithTooltip(25040, 0, 606, 44, j, 25092,
				"Activate @lre@Mystic Might");
		j++;
		addPrayerWithTooltip(25042, 0, 98, 45, j, 25094,
				"Activate @lre@Retribution");
		j++;
		addPrayerWithTooltip(25044, 0, 99, 48, j, 25096,
				"Activate @lre@Redemption");
		j++;
		addPrayerWithTooltip(25046, 0, 100, 51, j, 25098,
				"Activate @lre@Smite");
		j++;
		addPrayerWithTooltip(25048, 0, 607, 59, j, 25100,
				"Activate @lre@Chivalry");
		j++;
		addPrayerWithTooltip(25050, 0, 608, 69, j, 25102,
				"Activate @lre@Piety");
		j++;
		addPrayerWithTooltip(25052, 0, 609, 73, j, 25104,
				"Activate @lre@Rigour");
		j++;
		addPrayerWithTooltip(25054, 0, 610, 76, j, 25106,
				"Activate @lre@Augury");
	}
	private static void addPrayerWithTooltip(final int i, final int j,
			final int k, final int l, final int j2, final int m,
			final String string) {
		addPrayer(i, j, k, l, j2, string, m, "normal/PRAYERON");
	}
	
	/*public static void prayerTab(TextDrawingArea[] tda) {
		  RSInterface tab = addTabInterface(5608);
		  RSInterface currentPray = interfaceCache[687];
		  addSprite(5651, 0, "Prayer/PRAYER");
		  currentPray.textColor = 0xFF981F;
		  currentPray.textShadow = true;
		  currentPray.message = "%1/%2";

		  int[] ID1 = { 18016, 18017, 18018, 18019, 18020, 18021, 18022, 18023,
		    18024, 18025, 18026, 18027, 18028, 18029, 18030, 18031, 18032,
		    18033, 18034, 18035, 18036, 18037, 18038, 18039, 18040, 18041 };
		  int[] X = { 8, 44, 80, 114, 150, 8, 44, 80, 116, 152, 8, 42, 78, 116,
		    152, 8, 44, 80, 116, 150, 6, 44, 80, 116, 150, 6 };
		  int[] Y = { 6, 6, 6, 4, 4, 42, 42, 42, 42, 42, 79, 76, 76, 78, 78, 114,
		    114, 114, 114, 112, 148, 150, 150, 150, 148, 184 };

		  int[] hoverIDs = { 18050, 18052, 18054, 18056, 18058, 18060, 18062,
		    18064, 18066, 18068, 18070, 18072, 18074, 18076, 18078, 18080,
		    18082, 18084, 18086, 18088, 18090, 18092, 18094, 18096, 18098,
		    18100 };
		  int[] hoverX = { 12, 8, 20, 12, 24, 2, 2, 6, 6, 50, 6, 6, 10, 6, 6, 5,
		    5, 5, 5, 5, 18, 28, 28, 50, 1, 1 };
		  int[] hoverY = { 42, 42, 42, 42, 42, 80, 80, 80, 80, 80, 118, 118, 118,
		    118, 118, 150, 150, 150, 150, 150, 105, 80, 65, 65, 65, 110 };
		  String[] hoverStrings = {
		    "Level 01\nThick Skin\nIncreases your Defence by 5%",
		    "Level 04\nBurst of Strength\nIncreases your Strength by 5%",
		    "Level 07\nCharity of Thought\nIncreases your Attack by 5%",
		    "Level 08\nSharp Eye\nIncreases your Ranged by 5%",
		    "Level 09\nMystic Will\nIncreases your Magic by 5%",
		    "Level 10\nRock Skin\nIncreases your Defence by 10%",
		    "Level 13\nSuperhuman Strength\nIncreases your Strength by 10%",
		    "Level 16\nImproved Reflexes\nIncreases your Attack by 10%",
		    "Level 19\nRapid Restore\n2x restore rate for all stats\nexcept Hitpoints and Prayer",
		    "Level 22\nRapid Heal\n2x restore rate for the\nHitpoints stat",
		    "Level 25\nProtect Item\nKeep one extra item if you die",
		    "Level 26\nHawk Eye\nIncreases your Ranged by 10%",
		    "Level 27\nMystic Lore\nIncreases your Magic by 10%",
		    "Level 28\nSteel Skin\nIncreases your Defence by 15%",
		    "Level 31\nUltimate Strength\nIncreases your Strength by 15%",
		    "Level 34\nIncredible Reflexes\nIncreases your Attack by 15%",
		    "Level 37\nProtect from Magic\nProtection from magical attacks",
		    "Level 40\nProtect from Missiles\nProtection from ranged attacks",
		    "Level 43\nProtect from Melee\nProtection from close attacks",
		    "Level 44\nEagle Eye\nIncreases your Ranged by 15%",
		    "Level 45\nMystic Might\nIncreases your Magic by 15%",
		    "Level 46\nRetribution\nInflicts damage to nearby\ntargets if you die",
		    "Level 49\nRedemption\nHeals you when damaged\nand Hitpoints falls\nbelow 10%",
		    "Level 52\nSmite\n1/4 of damage dealt is\nalso removed from\nopponents Prayer",
		    "Level 60\nChivalry\nIncreases your Defence by 20%,\nStrength by 18% and Attack by\n15%",
		    "Level 70\nPiety\nIncreases your Defence by 25%,\nStrength by 23% and Attack by\n20%" };

		  int ID2[] = { 5609, 5610, 5611, 5612, 5613, 5614, 5615, 5616, 5617,
		    5618, 5619, 5620, 5621, 5622, 5623, 683, 684, 685, 5632, 5633,
		    5634, 5635, 5636, 5637, 5638, 5639, 5640, 5641, 5642, 5643,
		    5644, 686, 5645, 5649, 5647, 5648, 18000, 18001, 18002, 18003,
		    18004, 18005, 18006, 18007, 18008, 18009, 18010, 18011, 18012,
		    18013, 18014, 18015, 5651, 687 };
		  int X2[] = { 6, 42, 78, 6, 42, 78, 114, 150, 6, 114, 150, 6, 42, 78,
		    114, 42, 78, 114, 8, 44, 80, 8, 44, 80, 116, 152, 8, 116, 152,
		    8, 44, 80, 116, 44, 80, 116, 114, 117, 150, 153, 42, 45, 78,
		    81, 150, 153, 6, 9, 150, 157, 6, 8, 65, 14 };
		  int Y2[] = { 4, 4, 4, 40, 40, 40, 40, 40, 76, 76, 76, 112, 112, 112,
		    112, 148, 148, 148, 6, 6, 6, 42, 42, 42, 42, 42, 79, 78, 78,
		    114, 114, 114, 114, 150, 150, 150, 4, 8, 4, 7, 76, 80, 76, 79,
		    112, 116, 148, 151, 148, 151, 184, 194, 242, 244 };

		  String[] oldPrayerNames = { "Thick Skin", "Burst of Strength",
		    "Charity of Thought", "Rock Skin", "Superhuman Strength",
		    "Improved Reflexes", "Rapid Restore", "Rapid Heal",
		    "Protect Item", "Steel Skin", "Ultimate Strength",
		    "Incredible Reflexes", "Protect from Magic",
		    "Protect from Missiles", "Protect from Melee", "Retribution",
		    "Redemption", "Smite" };
		  addPrayer(18000, 0, 601, 7, 0, "Sharp Eye");
		  addPrayer(18002, 0, 602, 8, 1, "Mystic Will");
		  addPrayer(18004, 0, 603, 25, 2, "Hawk Eye");
		  addPrayer(18006, 0, 604, 26, 3, "Mystic Lore");
		  addPrayer(18008, 0, 605, 43, 4, "Eagle Eye");
		  addPrayer(18010, 0, 606, 44, 5, "Mystic Might");
		  addPrayer(18012, 0, 607, 59, 6, "Chivalry");
		  addPrayer(18014, 0, 608, 69, 7, "Piety");

		  for (int i = 0; i < 18; i++) {
		   addOldPrayer(ID2[i], oldPrayerNames[i]);
		  }

		  for (int i = 0; i < 26; i++) {
		   addPrayerHover(ID1[i], hoverIDs[i], i, hoverStrings[i]);
		  }

		  tab.totalChildren(106); // 54
		  tab.child(52, 5651, 70, 242);
		  for (int ii = 0; ii < 54; ii++) {
		   tab.child(ii, ID2[ii], X2[ii], Y2[ii]);
		  }

		  int frame = 54;
		  int frame2 = 0;
		  for (int i : ID1) {
		   tab.child(frame, i, X[frame2], Y[frame2]);
		   frame++;
		   frame2++;
		  }

		  int frame3 = 0;
		  for (int i : hoverIDs) {
		   tab.child(frame, i, hoverX[frame3], hoverY[frame3]);
		   frame++;
		   frame3++;
		  }
		 }*/
	
	public static void addPrayer(final int i, final int configId,
			final int configFrame, final int requiredValues,
			final int prayerSpriteID, final String PrayerName, final int Hover,
			final String loca) {
		RSInterface Interface = addTabInterface(i);
		Interface.id = i;
		Interface.parentID = 22500;
		Interface.type = 5;
		Interface.atActionType = 4;
		Interface.contentType = 0;
		Interface.aByte254 = 0;
		Interface.hoverType = Hover;
		Interface.sprite1 = imageLoader(prayerSpriteID, "QuickPrayer/" + loca
				+ "");
		Interface.sprite2 = imageLoader(prayerSpriteID, "QuickPrayer/" + loca
				+ "");
		Interface.width = 34;
		Interface.height = 34;
		Interface.anIntArray212 = new int[1];
		Interface.anIntArray245 = new int[1];
		Interface.anIntArray212[0] = 1;
		Interface.anIntArray245[0] = configId;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 5;
		Interface.valueIndexArray[0][1] = configFrame;
		Interface.valueIndexArray[0][2] = 0;
		Interface.tooltip = "Activate@lre@ " + PrayerName;
		Interface = addTabInterface(i + 1);
		Interface.id = i + 1;
		Interface.parentID = 22500;
		Interface.type = 5;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.aByte254 = 0;
		Interface.sprite1 = imageLoader(prayerSpriteID, "QuickPrayer/" + loca
				+ "");
		Interface.sprite2 = imageLoader(prayerSpriteID, "QuickPrayer/" + loca
				+ "");
		Interface.width = 34;
		Interface.height = 34;
		Interface.anIntArray212 = new int[1];
		Interface.anIntArray245 = new int[1];
		Interface.anIntArray212[0] = 2;
		Interface.anIntArray245[0] = requiredValues + 1;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 2;
		Interface.valueIndexArray[0][1] = 5;
		Interface.valueIndexArray[0][2] = 0;
	}
	
	public static void bankPin(TextDrawingArea[] wid) {
		RSInterface tab = addInterface(59500);
		addSprite(59501, 0, "Interfaces/Bankpin/IMAGE");
		addText(59502, "Account Pin", wid, 2, 0xFF981F, true, true);
		addText(59503, "Enter your 4 digit code", wid, 1, 0xFF981F, true, true);
		addText(59504, "Enter your 4 digit code", wid, 1, 0xFF981F, true, true);
		addText(59505, "Enter your 4 digit code", wid, 1, 0xFF981F, true, true);
		addText(59506, "Press enter to submit", wid, 2, 0xFF981F, true, true);
		addInputField(59507, 8, 0xFF981F, "", 100, 24, true);
		addHoverButton(59508, "Interfaces/Bankpin/IMAGE", 1, 16, 16, "Close", 375, 59509, 3);
		addHoveredButton(59509, "Interfaces/Bankpin/IMAGE", 2, 16, 16, 59510);
		setChildren(9, tab);
		setBounds(59501, 256 - 140, 120, 0, tab);
		setBounds(59502, 256, 132, 1, tab);
		setBounds(59503, 256, 150, 2, tab);
		setBounds(59504, 256, 165, 3, tab);
		setBounds(59505, 256, 180, 4, tab);
		setBounds(59506, 256, 200, 5, tab);
		setBounds(59507, 256 - 50, 220, 6, tab);
		setBounds(59508, 374, 127, 7, tab);
		setBounds(59509, 374, 127, 8, tab);
	}
	
	public static void starterWidget(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(40000);
		tab.message = "";
		//setChildren(9, tab);
		addSprite(40001, 0, "Interfaces/Starter/STARTER");
		addHoverButton(40002, "BankTab/BANK", 1, 24, 24, "Close Window", 250, 30003, 3);
		addHoveredButton(40003, "BankTab/BANK", 2, 24, 24, 5379);
		addText(40004, "Game Modes", tda, 2, 0xE68A00, true, true);
		addText(40005, "@whi@Which mode would you like to play RuneServer in?", tda, 3, 0xE68A00, true, true);
		addText(40006, "Play normal game mode", tda, 0, 0xE68A00, true, true);
		addBankHover(40007, 4, 40008, 0, 1,"Interfaces/Starter/TOGGLE", 75, 20, 556, 1,
				"Select Normal", 40009, 1, 0, "Interfaces/Starter/TOGGLE", 40010, "", "", 12, 20);
		addText(40008, "@red@(Information on game mode 'Normal')", tda, 0, 0xE68A00, true, true);
		addText(40009, "In normal game mode you can do everything as normal @whi@(No restrictions)", tda, 0, 0xE68A00, true, true);
		//addText(40009, "unprotected skills. You can select a maximum of 2", tda, 0, 0xE68A00, true, true);
		//addText(40010, "combat and 3 non-combat skills to protect.", tda, 0, 0xE68A00, true, true);
		setChildren(9, tab);
		int i = 0;
		setBounds(40001, + 10, + 0, i, tab);
		i++;
		setBounds(40002, + 475, + 5, i, tab);
		i++;
		setBounds(40003, + 390, + 32, i, tab);
		i++;
		setBounds(40004, + 270, + 10, i, tab);
		i++;
		setBounds(40005, + 250, + 50, i, tab);
		i++;
		setBounds(40006, + 100, + 90, i, tab);
		i++;
		setBounds(40007, + 25, + 90, i, tab);
		i++;
		setBounds(40008, + 130, + 100, i, tab);
		i++;
		setBounds(40009, + 202, + 110, i, tab);
		i++;
		//setBounds(40010, 120, 110, 9, tab);
	}
	
	/*public static void itemsOnDeathDeadMan(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(30000);
		tab.message = "";
		setChildren(22, tab);
		addSprite(30001, 0, "DeadMan/DEATH");
		addHoverButton(30002, "BankTab/BANK", 1, 24, 24, "Close Window", 250, 30003, 3);
		addHoveredButton(30003, "BankTab/BANK", 2, 24, 24, 5379);
		addText(30004, "When you die in Deadman mode, while @whi@skulled@or2@ OR", tda, 0, 0xE68A00, true, true);
		addText(30005, "from @whi@ PvP combat@or2@, you lose the @whi@28@or2@ most valuable", tda, 0, 0xE68A00, true, true);
		addText(30006, "stacks of items in your bank, as well as @whi@all of the", tda, 0, 0xE68A00, true, true);
		addText(30007, "items you are carrying at the time.", tda, 0, 0xE68A00, true, true);
		addText(30008, "You also lose @whi@half of your total experience@or2@ in any", tda, 0, 0xE68A00, true, true);
		addText(30009, "unprotected skills. You can select a maximum of 2", tda, 0, 0xE68A00, true, true);
		addText(30010, "combat and 3 non-combat skills to protect.", tda, 0, 0xE68A00, true, true);
		addText(30011, "Lost on Death", tda, 2, 0xE68A00, true, true);
		addText(30012, "Experience lost:", tda, 1, 0xE68A00, true, true);
		addText(30013, "Items lost from bank:", tda, 1, 0xE68A00, true, true);
		
		addText(30014, "Protected:", tda, 0, 0xE68A00, true, true);
		addText(30015, "Combat", tda, 0, 0xE68A00, true, true);
		addText(30016, "0", tda, 0, 0xE68A00, true, true);
		addText(30017, "/", tda, 0, 0xE68A00, true, true);
		addText(30018, "0", tda, 0, 0xE68A00, true, true);
		addText(30019, "Other", tda, 0, 0xE68A00, true, true);
		addText(30020, "0", tda, 0, 0xE68A00, true, true);
		addText(30021, "/", tda, 0, 0xE68A00, true, true);
		addText(30022, "0", tda, 0, 0xE68A00, true, true);
		
		setBounds(30001, 10, 0, 0, tab);
		setBounds(30002, 475, 5, 1, tab);
		setBounds(30003, 390, 32, 2, tab);
		setBounds(30004, 138, 40, 3, tab);
		setBounds(30005, 138, 50, 4, tab);
		setBounds(30006, 138, 60, 5, tab);
		setBounds(30007, 102, 70, 6, tab);
		setBounds(30008, 138, 90, 7, tab);
		setBounds(30009, 138, 100, 8, tab);
		setBounds(30010, 120, 110, 9, tab);
		setBounds(30011, 250, 10, 10, tab);
		setBounds(30012, 330, 35, 11, tab);
		setBounds(30013, 80, 150, 12, tab);
		
		setBounds(30014, 455, 295, 13, tab);
		setBounds(30015, 446, 305, 14, tab);
		setBounds(30016, 470, 305, 15, tab);
		setBounds(30017, 475, 305, 16, tab);
		setBounds(30018, 480, 305, 17, tab);
		setBounds(30019, 442, 315, 18, tab);
		setBounds(30020, 470, 315, 19, tab);
		setBounds(30021, 475, 315, 20, tab);
		setBounds(30022, 480, 315, 21, tab);
	}*/
	
	public static void safeDeadMan(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(27000);
		tab.message = "";
		setChildren(15, tab);
		addSprite(27001, 0, "DeadMan/SAFE");
		addHoverButton(27002, "BankTab/BANK", 1, 24, 24, "Close Window", 250, 27003, 3);
		addHoveredButton(27003, "BankTab/BANK", 2, 24, 24, 5379);
		addText(27004, "Safe Deposit Box", tda, 2, 0xE68A00, true, true);
		addText(27005, "0", tda, 2, 0xE68A00, true, true);
		addText(27006, "/", tda, 2, 0xE68A00, true, true);
		addText(27007, "10", tda, 2, 0xE68A00, true, true);
		addBankHover1(27008, 5, 27009, 1, "DeadMan/SAFE", 35, 25,
				"Deposit inventory items", 27010, 1, "DeadMan/SAFE", 27011,
				"Empty the items you have in your inventory into your bank", 0, 20);
		
		addBankHover1(27012, 5, 27013, 2, "DeadMan/SAFE", 35, 25,
				"Deposit inventory items", 27014, 2, "DeadMan/SAFE", 27015,
				"Empty the items you have in your inventory into your bank", 0, 20);
		setBounds(27001, 110, 25, 0, tab);
		setBounds(27002, 390, 32, 1, tab);
		setBounds(27003, 390, 32, 2, tab);
		setBounds(27004, 270, 36, 3, tab);
		setBounds(27005, 160, 270, 4, tab);
		setBounds(27006, 190, 270, 5, tab);
		setBounds(27007, 220, 270, 6, tab);
		
		setBounds(27008, 280, 258, 7, tab);
		setBounds(27009, 280, 258, 8, tab);
		setBounds(27010, 280, 258, 9, tab);
		setBounds(27011, 280, 258, 10, tab);
		
		setBounds(27012, 320, 258, 11, tab);
		setBounds(27013, 320, 258, 12, tab);
		setBounds(27014, 320, 258, 13, tab);
		setBounds(27015, 320, 258, 14, tab);
	}
	
	public static void runepouch(TextDrawingArea[] tda) {
	     RSInterface tab = addInterface(41700);
	     addSprite(41701, 0, "runepouch/sprite");
	     addSprite(41705, 0, "runepouch/rune");
	     addSprite(41706, 1, "runepouch/rune");
	     addText(41702, "Rune pouch", tda, 2, 0xFFA500, true, true);
	     addText(41703, "Pouch", tda, 2, 0xFFA500, true, true);
	     addText(41704, "Inventory", tda, 2, 0xFFA500, true, true);
	     addHoverButton(41707, "runepouch/close", 0, 21, 21, "Close window", 0, 41708, 1);
	     addHoveredButton(41708, "runepouch/close", 1, 21, 21, 41709);
	  	 RSInterface add = addInterface(41710);
	  	 addToItemGroup(add, 3, 1, 26, 1, false, null, null, null);
	  	 add = addInterface(41711);
	  	 addToItemGroup(add, 7, 4, 16, 4, false, null, null, null);
	     tab.totalChildren(10);
	     tab.child(0, 41701, 0, 0);
	     tab.child(1, 41702, 253, 29);
	     tab.child(2, 41703, 253, 62);
	     tab.child(3, 41704, 253, 137);
	     tab.child(4, 41705, 105, 57);
	     tab.child(5, 41706, 342, 57);
	     tab.child(6, 41707, 406, 26);
	     tab.child(7, 41708, 406, 26);
	     tab.child(8, 41710, 186, 86);
	     tab.child(9, 41711, 98, 154);
	    }
	
	public static void addToItemGroup(RSInterface rsi, int w, int h, int x,
			   int y, boolean actions, String action1, String action2,
			   String action3) {
			  rsi.width = w;
			  rsi.height = h;
			  rsi.inventoryItemId = new int[w * h];
			  rsi.inventoryAmounts = new int[w * h];
			  rsi.usableItems = false;
			  rsi.hasActions = false;
			  rsi.spritePaddingX = x;
			  rsi.spritePaddingY = y;
			  rsi.spritesX = new int[20];
			  rsi.spritesY = new int[20];
			  rsi.sprites = new Sprite[20];
			  rsi.actions = new String[5];
			  if (actions) {
			   rsi.actions[0] = action1;
			   rsi.actions[1] = action2;
			   rsi.actions[2] = action3;
			  }
			  rsi.type = 2;
			 }
	
	public static void deadManBank(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(44000);
		tab.message = "";
		setChildren(31, tab);
		addSprite(44001, 0, "DeadMan/DEPOSIT");
		addHoverButton(44002, "BankTab/BANK", 1, 24, 24, "Close Window", 250, 44003, 3);
		addHoveredButton(44003, "BankTab/BANK", 2, 24, 24, 5379);
		addText(44004, "Withdraw as:", tda, 1, 0xE68A00, true, true);
		addBankHover(44005, 4, 44006, 1, 2, "DeadMan/BUTTON", 75, 20, 555, 0,
				"Toggle Items", 44007, 1, 2, "DeadMan/BUTTON", 44008, "", "", 12, 20);
		addText(44009, "Item", tda, 1, 0xE68A00, true, true);
		addBankHover(44010, 4, 44011, 1, 2,"DeadMan/BUTTON", 75, 20, 556, 1,
				"Toggle Notes", 44012, 1, 2, "DeadMan/BUTTON", 44013, "", "", 12, 20);
		addText(44014, "Note", tda, 1, 0xE68A00, true, true);
		addText(44015, "Bank Raid", tda, 2, 0xE68A00, true, true);
		
		addBankHover1(44016, 5, 44017, 3, "DeadMan/BUTTON", 35, 25,
				"Deposit inventory items", 44018, 3, "DeadMan/BUTTON", 44019,
				"Empty the items you have in your inventory into your bank", 0, 20);
		
		addText(44024, "0", tda, 0, 0xE68A00, true, true);
		addText(44025, "_", tda, 0, 0xE68A00, true, true);
		addText(44026, "800", tda, 0, 0xE68A00, true, true);
		
		addBankHover1(44020, 5, 44021, 4, "DeadMan/BUTTON", 35, 25,
				"Deposit inventory items", 44022, 4, "DeadMan/BUTTON", 44023,
				"Empty the items you have in your inventory into your bank", 0, 20);
		
		for(int i = 0; i < 5; i++) {
			addInterface(44027 + i);
			if(i == 0)
				addConfigButton(44031, 44000, 1, 0, "BankTab/TAB", 48, 38, new String[] {"Open"}, 1, 700);
			else
				addConfigButton(44031 + i, 44000, 4, 2, "BankTab/TAB", 48, 38, new String[] {"Open"}, 1, 700 + i);
				addToItemGroup(44040 + i, 1, 1, 0, 0, false, "", "", "");
		}
		
		setBounds(44001, 75, 17, 0, tab);
		setBounds(44002, 425, 22, 1, tab);
		setBounds(44003, 475, 10, 2, tab);
		setBounds(44004, 165, 270, 3, tab);
        setBounds(44005, 80, 288, 4, tab);
        setBounds(44006, 105, 290, 5, tab);
        setBounds(44007, 80, 288, 6, tab);
        setBounds(44008, 105, 290, 7, tab);
        setBounds(44009, 120, 292, 8, tab);
        setBounds(44010, 165, 288, 9, tab);
        setBounds(44011, 170, 288, 10, tab);
        setBounds(44012, 165, 288, 11, tab);
        setBounds(44013, 170, 292, 12, tab);
        setBounds(44014, 205, 292, 13, tab);
        setBounds(44015, 260, 25, 14, tab);
        
        setBounds(44016, 285, 272, 15, tab);
        setBounds(44017, 285, 272, 16, tab);
        setBounds(44018, 285, 272, 17, tab);
        setBounds(44019, 285, 25, 18, tab);
        
        setBounds(44020, 395, 272, 19, tab);
        setBounds(44021, 395, 272, 20, tab);
        setBounds(44022, 395, 272, 21, tab);
        setBounds(44023, 395, 25, 22, tab);
        
        setBounds(44024, 360, 280, 23, tab);
        setBounds(44025, 360, 285, 24, tab);
        setBounds(44026, 360, 295, 25, tab);
        
        int x = 80;
		for(int i = 0; i < 5; i++) {
			setBounds(44027 + i, 0, 0, 26 + i, tab);
			RSInterface rsi = interfaceCache[44027 + i];
			setChildren(2, rsi);
			setBounds(44031 + i, x, 54, 0, rsi);
			setBounds(44040 + i, x + 5, 39, 1, rsi);
			x += 41;
		}
	}
	
	public static void bank(TextDrawingArea[] tda) {
		RSInterface rs = addInterface(5292);
		rs.message = "";
		setChildren(28, rs);
		addSprite(58001, 0, "BankTab/BANK");
		addHoverButton(5384, "BankTab/BANK", 1, 24, 24, "Close Window", -1, 5380, 1);
		addHoveredButton(5380, "BankTab/BANK", 2, 24, 24, 5379);
		addHoverButton(5294, "BankTab/BANK", 3, 100, 33, "Set A Bank PIN", 250, 5295, 4);
		addHoveredButton(5295, "BankTab/BANK", 4, 100, 33, 5296);
		addBankHover(58002, 4, 58003, 5, 8, "BankTab/BANK", 37, 29, 304, 1,
				"Swap Withdraw Mode", 58004, 7, 6, "BankTab/BANK", 58005,
				"Switch to insert items \nmode",
				"Switch to swap items \nmode.", 12, 20);
		addBankHover(58010, 4, 58011, 9, 11, "BankTab/BANK", 37, 29, 115, 1,
				"Swap Withdrawal Mode", 58012, 10, 12, "BankTab/BANK", 58013,
				"Switch to note withdrawal \nmode",
				"Switch to item withdrawal \nmode", 12, 20);
		addBankHover1(58018, 5, 58019, 17, "BankTab/BANK", 37, 29,
				"Deposit carried items", 58020, 18, "BankTab/BANK", 58021,
				"Empty your backpack into\nyour bank", 0, 20);
		addBankHover1(58026, 5, 58027, 19, "BankTab/BANK", 35, 25,
				"Deposit worn items", 58028, 20, "BankTab/BANK", 58029,
				"Empty the items your are\nwearing into your bank", 0, 20);
		for(int i = 0; i < 9; i++) {
			addInterface(58050 + i);
			if(i == 0)
				addConfigButton(58031, 5292, 1, 0, "BankTab/TAB", 48, 38, new String[] {"Price Check", "View"}, 1, 700);
			else
				addConfigButton(58031 + i, 5292, 4, 2, "BankTab/TAB", 48, 38, new String[] {"Price Check", "Collapse", "View"}, 1, 700 + i);
			addToItemGroup(58040 + i, 1, 1, 0, 0, false, "", "", "");
		}
		addSprite(58060, 21, "BankTab/BANK");
		addText(58061, "0", tda, 0, 0xE68A00, true, true);
		addText(58062, "350", tda, 0, 0xE68A00, true, true);
		addInputField(58063, 50, 0xE68A00, "Search", 132, 23, false, true);
		addText(58064, "Jason's Bank", tda, 1, 0xE68A00, true, true);
		RSInterface Interface = interfaceCache[5385];
		Interface.height = 208;
		Interface.width = 481;
		Interface = interfaceCache[5382];
		Interface.width = 10;
		Interface.spritePaddingX = 12;
		Interface.height = 35;
		Interface.actions = new String[] {"Withdraw 1", "Withdraw 5", "Withdraw 10", "Withdraw All", "Withdraw X", "Withdraw All but one"};
		setBounds(58001, 13, 1, 0, rs);
		setBounds(5384, 475, 10, 1, rs);
		setBounds(5380, 475, 10, 2, rs);
		setBounds(5294, 193, 297, 3, rs);
		setBounds(5295, 193, 297, 4, rs);
		setBounds(58002, 25, 297, 5, rs);
		setBounds(58003, 10, 237, 6, rs);
		setBounds(58010, 67, 297, 7, rs);
		setBounds(58011, 52, 237, 8, rs);
		
		setBounds(58018, 109, 297, 9, rs);
		setBounds(58019, 94, 237, 10, rs);
		setBounds(58026, 151, 297, 11, rs);
		setBounds(58027, 136, 237, 12, rs);
		setBounds(5385, -3, 76, 13, rs);
		RSInterface.interfaceCache[5385].height = 216;
		int x = 68;
		for(int i = 0; i < 9; i++) {
			setBounds(58050 + i, 0, 0, 14 + i, rs);
			RSInterface rsi = interfaceCache[58050 + i];
			setChildren(2, rsi);
			setBounds(58031 + i, x, 36, 0, rsi);
			setBounds(58040 + i, x + 5, 39, 1, rsi);
			x += 41;
		}
		setBounds(58060, 452, 295, 23, rs);
		setBounds(58061, 473, 299, 24, rs);
		setBounds(58062, 473, 310, 25, rs);
		setBounds(58063, 315, 298, 26, rs);
		setBounds(58064, 250, 11, 27, rs);
	}
	
	public static void addInputField(int identity, int characterLimit, int color, String text, int width, int height, boolean asterisks, boolean updatesEveryInput, String regex) {
		RSInterface field = addFullScreenInterface(identity);
		field.id = identity;
		field.type = 16;
		field.atActionType = 8;
		field.message = text;
		field.width = width;
		field.height = height;
		field.characterLimit = characterLimit;
		field.textColor = color;
		field.displayAsterisks = asterisks;
		field.tooltips = new String[] {"Clear", "Edit"};
		field.defaultInputFieldText = text;
		field.updatesEveryInput = updatesEveryInput;
		field.inputRegex = regex;
	}
	
	public static void addInputField(int identity, int characterLimit, int color, String text, int width, int height, boolean asterisks, boolean updatesEveryInput) {
		RSInterface field = addFullScreenInterface(identity);
		field.id = identity;
		field.type = 16;
		field.atActionType = 8;
		field.message = text;
		field.width = width;
		field.height = height;
		field.characterLimit = characterLimit;
		field.textColor = color;
		field.displayAsterisks = asterisks;
		field.defaultInputFieldText = text;
		field.tooltips = new String[] {"Clear", "Edit"};
		field.updatesEveryInput = updatesEveryInput;
	}
	
	public static void addInputField(int identity, int characterLimit, int color, String text, int width, int height, boolean asterisks) {
		RSInterface field = addFullScreenInterface(identity);
		field.id = identity;
		field.type = 16;
		field.atActionType = 8;
		field.message = text;
		field.width = width;
		field.height = height;
		field.characterLimit = characterLimit;
		field.textColor = color;
		field.displayAsterisks = asterisks;
		field.defaultInputFieldText = text;
		field.tooltips = new String[] {"Clear", "Edit"};
	}
	
	public static RSInterface addFullScreenInterface(int id) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.id = id;
		rsi.parentID = id;
		rsi.width = 765;
		rsi.height = 503;
		return rsi;
	}

	public boolean updatesEveryInput;
	public String defaultInputFieldText = "";
	int[] inputFieldTriggers;
	public boolean displayAsterisks;
	public int characterLimit;
	public static int currentInputFieldId;
	public String inputRegex = "";
	public boolean isInFocus;
	public String[] tooltips;
	
	public static void addHoverText(int id, String text, String tooltip,
			TextDrawingArea tda[], int idx, int color, boolean centerText,
			boolean textShadowed, int width) {
		RSInterface rsinterface = addInterface(id);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 4;
		rsinterface.atActionType = 1;
		rsinterface.width = width;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.aByte254 = 0;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.centerText = centerText;
		rsinterface.textShadow = textShadowed;
		rsinterface.textDrawingAreas = tda[idx];
		rsinterface.message = text;
		rsinterface.aString228 = "";
		rsinterface.textColor = color;
		rsinterface.anInt219 = 0;
		rsinterface.anInt216 = 0xffffff;
		rsinterface.anInt239 = 0;
		rsinterface.tooltip = tooltip;
	}
	
//	public static void priceChecker(TextDrawingArea[] wid) {
//		RSInterface rsi = addInterface(77000);
//
//		addSprite(77001, 1, "Interfaces/PriceChecker/checker");
//		
//		// Close
//		addHoverButton(77002, "Interfaces/PriceChecker/close", 1, 16, 21, "Close", -1, 77003, 1);
//		addHoveredButton(77003, "Interfaces/PriceChecker/close", 2, 21, 21, 77004);
//		
//		// Deposit all
//		addHoverButton(77005, "Interfaces/PriceChecker/deposit", 1, 36, 36, "Add all", -1, 77006, 1);
//		addHoveredButton(77006, "Interfaces/PriceChecker/deposit", 2, 36, 36, 77007);
//		
//		// Search
//		addHoverButton(77008, "Interfaces/PriceChecker/search", 1, 36, 36, "Search for item", -1, 77009, 1);
//		addHoveredButton(77009, "Interfaces/PriceChecker/search", 2, 36, 36, 77010);
//
//		// Text
//		addText(77012, "Total guide price:", wid, 1, 0xFF981F, false, true);
//		addText(77013, "0", wid, 1, 0xffffff, false, true);
//
//		rsi.totalChildren(11);
//		rsi.child(0, 77001, 15, 15);
//		rsi.child(1, 77002, 467, 22);
//		rsi.child(2, 77003, 467, 22);
//		rsi.child(3, 77005, 451, 285);
//		rsi.child(4, 77006, 451, 285);
//		rsi.child(5, 77008, 25, 285);
//		rsi.child(6, 77009, 25, 285);
//		rsi.child(7, 77011, 160, 22);
//		rsi.child(8, 77012, 211, 287);
//		rsi.child(9, 77013, 255, 302);
//		rsi.child(10, 77014, 19, 55);
//		
//		RSInterface scroll = addInterface(77014);
//
//		// Item Slots
//		addText(77015, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77016, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77018, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77019, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77021, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77022, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77024, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77025, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77027, "", wid, 0, 0xFFFFFF, true, true); // Soul rune showing someone above
//		addText(77028, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77030, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77031, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77033, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77034, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77036, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77037, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77039, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77040, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77042, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77043, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77045, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77046, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77048, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77049, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77051, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77052, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77054, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77055, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77057, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77058, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77060, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77061, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77063, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77064, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77066, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77067, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77069, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77070, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77072, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77073, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77075, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77076, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77078, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77079, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77081, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77082, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77084, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77085, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77087, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77088, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77090, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77091, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77093, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77094, "", wid, 0, 0xFFFFFF, true, true);
//
//		addText(77096, "", wid, 0, 0xFFFFFF, true, true);
//		addText(77097, "", wid, 0, 0xFFFFFF, true, true);
//
//		addPriceChecker(77099); // Price Checker
//		
//		scroll.totalChildren(57);
//		scroll.child(0, 77015, 49, 32);
//		scroll.child(1, 77016, 49, 42);
//		// scroll.child(2, 77017, 49, 52);
//		scroll.child(2, 77018, 138, 32);
//		scroll.child(3, 77019, 138, 42);
//		// scroll.child(5, 77020, 138, 52);
//		scroll.child(4, 77021, 227, 32);
//		scroll.child(5, 77022, 227, 42);
//		// scroll.child(8, 77023, 227, 52);
//		scroll.child(6, 77024, 316, 32);
//		scroll.child(7, 77025, 316, 42);
//		// scroll.child(11, 77026, 316, 52);
//		scroll.child(8, 77027, 405, 32);
//		scroll.child(9, 77028, 405, 42);
//		// scroll.child(14, 77029, 405, 52); //First Row
//		scroll.child(10, 77030, 49, 92);
//		scroll.child(11, 77031, 49, 102);
//		// scroll.child(17, 77032, 49, 112);
//		scroll.child(12, 77033, 138, 92);
//		scroll.child(13, 77034, 138, 102);
//		// scroll.child(20, 77035, 138, 112);
//		scroll.child(14, 77036, 227, 92);
//		scroll.child(15, 77037, 227, 102);
//		// scroll.child(23, 77038, 227, 112);
//		scroll.child(16, 77039, 316, 92);
//		scroll.child(17, 77040, 316, 102);
//		// scroll.child(26, 77041, 316, 112);
//		scroll.child(18, 77042, 405, 92);
//		scroll.child(19, 77043, 405, 102);
//		// scroll.child(29, 77044, 405, 112); //Second Row
//		scroll.child(20, 77045, 49, 152);
//		scroll.child(21, 77046, 49, 162);
//		// scroll.child(32, 77047, 49, 172);
//		scroll.child(22, 77048, 138, 152);
//		scroll.child(23, 77049, 138, 162);
//		// scroll.child(35, 77050, 138, 172);
//		scroll.child(24, 77051, 227, 152);
//		scroll.child(25, 77052, 227, 162);
//		// scroll.child(38, 77053, 227, 172);
//		scroll.child(26, 77054, 316, 152);
//		scroll.child(27, 77055, 316, 162);
//		// scroll.child(41, 77056, 316, 172);
//		scroll.child(28, 77057, 405, 152);
//		scroll.child(29, 77058, 405, 162);
//		// scroll.child(44, 77059, 405, 172); //Third Row
//		scroll.child(30, 77060, 49, 212);
//		scroll.child(31, 77061, 49, 222);
//		// scroll.child(47, 77062, 49, 232);
//		scroll.child(32, 77063, 138, 212);
//		scroll.child(33, 77064, 138, 222);
//		// scroll.child(50, 77065, 138, 232);
//		scroll.child(34, 77066, 227, 212);
//		scroll.child(35, 77067, 227, 222);
//		// scroll.child(53, 77068, 227, 232);
//		scroll.child(36, 77069, 316, 212);
//		scroll.child(37, 77070, 316, 222);
//		// scroll.child(56, 77071, 316, 232);
//		scroll.child(38, 77072, 405, 212); // Forth Row
//		scroll.child(39, 77073, 405, 222);
//		// scroll.child(59, 77074, 405, 232);
//		scroll.child(40, 77075, 49, 272);
//		scroll.child(41, 77076, 49, 282);
//		// roll.child(62, 77077, 49, 292);
//		scroll.child(42, 77078, 138, 272);
//		scroll.child(43, 77079, 138, 282);
//		// scroll.child(65, 77080, 138, 292);
//		scroll.child(44, 77081, 227, 272);
//		scroll.child(45, 77082, 227, 282);
//		// scroll.child(68, 77083, 227, 292);
//		scroll.child(46, 77084, 316, 272);
//		scroll.child(47, 77085, 316, 282);
//		// scroll.child(71, 77086, 316, 292);
//		scroll.child(48, 77087, 405, 272); // Fifth Row
//		scroll.child(49, 77088, 405, 282);
//		// scroll.child(74, 77089, 405, 292);
//		scroll.child(50, 77090, 49, 332);
//		scroll.child(51, 77091, 49, 342);
//		// scroll.child(77, 77092, 49, 352);
//		scroll.child(52, 77093, 138, 332);
//		scroll.child(53, 77094, 138, 342);
//		// scroll.child(80, 77095, 138, 352);
//		scroll.child(54, 77096, 227, 332);
//		scroll.child(55, 77097, 227, 342);
//		// scroll.child(83, 77098, 227, 352);
//		
//		scroll.child(56, 77099, 32, 0); // PriceChecker method
//		scroll.width = 480 - 30;
//		scroll.height = 217;
//		scroll.scrollMax = 500;
//		
//	}


public static void priceChecker(TextDrawingArea[] tda) {
	RSInterface rsi = addTabInterface(43933);
	addSprite(7245, 1, "Interfaces/PriceChecker/checker");
	addPriceChecker(7246);
	addHoverButton(7247, "Interfaces/PriceChecker/close", 1, 21, 21, "Close", -1, 7248, 1);
	addHoveredButton(7248, "Interfaces/PriceChecker/close", 2, 21, 21, 7244);
	rsi.totalChildren(67);
	rsi.child(0, 7245, 10, 20);//was 10 so + 10
	rsi.child(1, 7246, 105, 50);
	rsi.child(2, 7247, 472, 23);
	rsi.child(3, 7248, 472, 23);

	addText(7350, "Total value:", tda, 0, 0xFFFFFF, false, true);
	rsi.child(4, 7350, 225, 295);  // Open Text
	addText(7351, "0", tda, 0, 0xFFFFFF, true, true);
	rsi.child(5, 7351, 251, 306);
	addText(7352, "", tda, 0, 0xFFFFFF, false, true);
	rsi.child(6, 7352, 120, 150);

	addText(7353, "", tda, 0, 0xFFFFFF, true, true);rsi.child(7, 7353, 120, 85);
	addText(7354, "", tda, 0, 0xFFFFFF, true, true);rsi.child(8, 7354, 120, 95);
	addText(7355, "", tda, 0, 0xFFFFFF, true, true);rsi.child(9, 7355, 120, 105);
	addText(7356, "", tda, 0, 0xFFFFFF, true, true);rsi.child(10, 7356, 190, 85);
	addText(7357, "", tda, 0, 0xFFFFFF, true, true);rsi.child(11, 7357, 190, 95);
	addText(7358, "", tda, 0, 0xFFFFFF, true, true);rsi.child(12, 7358, 190, 105);
	addText(7359, "", tda, 0, 0xFFFFFF, true, true);rsi.child(13, 7359, 260, 85);
	addText(7360, "", tda, 0, 0xFFFFFF, true, true);rsi.child(14, 7360, 260, 95);
	addText(7361, "", tda, 0, 0xFFFFFF, true, true);rsi.child(15, 7361, 260, 105);
	addText(7362, "", tda, 0, 0xFFFFFF, true, true);rsi.child(16, 7362, 330, 85);
	addText(7363, "", tda, 0, 0xFFFFFF, true, true);rsi.child(17, 7363, 330, 95);
	addText(7364, "", tda, 0, 0xFFFFFF, true, true);rsi.child(18, 7364, 330, 105);
	addText(7365, "", tda, 0, 0xFFFFFF, true, true);rsi.child(19, 7365, 400, 85);
	addText(7366, "", tda, 0, 0xFFFFFF, true, true);rsi.child(20, 7366, 400, 95);
	addText(7367, "", tda, 0, 0xFFFFFF, true, true);rsi.child(21, 7367, 400, 105);
	addText(7368, "", tda, 0, 0xFFFFFF, true, true);rsi.child(22, 7368, 120, 145);
	addText(7369, "", tda, 0, 0xFFFFFF, true, true);rsi.child(23, 7369, 120, 155);
	addText(7370, "", tda, 0, 0xFFFFFF, true, true);rsi.child(24, 7370, 120, 165);
	addText(7371, "", tda, 0, 0xFFFFFF, true, true);rsi.child(25, 7371, 190, 145);
	addText(7372, "", tda, 0, 0xFFFFFF, true, true);rsi.child(26, 7372, 190, 155);
	addText(7373, "", tda, 0, 0xFFFFFF, true, true);rsi.child(27, 7373, 190, 165);
	addText(7374, "", tda, 0, 0xFFFFFF, true, true);rsi.child(28, 7374, 260, 145);
	addText(7375, "", tda, 0, 0xFFFFFF, true, true);rsi.child(29, 7375, 260, 155);
	addText(7376, "", tda, 0, 0xFFFFFF, true, true);rsi.child(30, 7376, 260, 165);
	addText(7377, "", tda, 0, 0xFFFFFF, true, true);rsi.child(31, 7377, 330, 145);
	addText(7378, "", tda, 0, 0xFFFFFF, true, true);rsi.child(32, 7378, 330, 155);
	addText(7379, "", tda, 0, 0xFFFFFF, true, true);rsi.child(33, 7379, 330, 165);
	addText(7380, "", tda, 0, 0xFFFFFF, true, true);rsi.child(34, 7380, 400, 145);
	addText(7381, "", tda, 0, 0xFFFFFF, true, true);rsi.child(35, 7381, 400, 155);
	addText(7382, "", tda, 0, 0xFFFFFF, true, true);rsi.child(36, 7382, 400, 165);
	addText(7383, "", tda, 0, 0xFFFFFF, true, true);rsi.child(37, 7383, 120, 205);
	addText(7384, "", tda, 0, 0xFFFFFF, true, true);rsi.child(38, 7384, 120, 215);
	addText(7385, "", tda, 0, 0xFFFFFF, true, true);rsi.child(39, 7385, 120, 225);
	addText(7386, "", tda, 0, 0xFFFFFF, true, true);rsi.child(40, 7386, 190, 205);
	addText(7387, "", tda, 0, 0xFFFFFF, true, true);rsi.child(41, 7387, 190, 215);
	addText(7388, "", tda, 0, 0xFFFFFF, true, true);rsi.child(42, 7388, 190, 225);
	addText(7389, "", tda, 0, 0xFFFFFF, true, true);rsi.child(43, 7389, 260, 205);
	addText(7390, "", tda, 0, 0xFFFFFF, true, true);rsi.child(44, 7390, 260, 215);
	addText(7391, "", tda, 0, 0xFFFFFF, true, true);rsi.child(45, 7391, 260, 225);
	addText(7392, "", tda, 0, 0xFFFFFF, true, true);rsi.child(46, 7392, 330, 205);
	addText(7393, "", tda, 0, 0xFFFFFF, true, true);rsi.child(47, 7393, 330, 215);
	addText(7394, "", tda, 0, 0xFFFFFF, true, true);rsi.child(48, 7394, 330, 225);
	addText(7395, "", tda, 0, 0xFFFFFF, true, true);rsi.child(49, 7395, 400, 205);
	addText(7396, "", tda, 0, 0xFFFFFF, true, true);rsi.child(50, 7396, 400, 215);
	addText(7397, "", tda, 0, 0xFFFFFF, true, true);rsi.child(51, 7397, 400, 225);	
	addText(7398, "", tda, 0, 0xFFFFFF, true, true);rsi.child(52, 7398, 120, 260);
	addText(7399, "", tda, 0, 0xFFFFFF, true, true);rsi.child(53, 7399, 120, 270);
	addText(7400, "", tda, 0, 0xFFFFFF, true, true);rsi.child(54, 7400, 120, 280);
	addText(7401, "", tda, 0, 0xFFFFFF, true, true);rsi.child(55, 7401, 190, 260);
	addText(7402, "", tda, 0, 0xFFFFFF, true, true);rsi.child(56, 7402, 190, 270);
	addText(7403, "", tda, 0, 0xFFFFFF, true, true);rsi.child(57, 7403, 190, 280);
	addText(7404, "", tda, 0, 0xFFFFFF, true, true);rsi.child(58, 7404, 260, 260);
	addText(7405, "", tda, 0, 0xFFFFFF, true, true);rsi.child(59, 7405, 260, 270);
	addText(7406, "", tda, 0, 0xFFFFFF, true, true);rsi.child(60, 7406, 260, 280);
	addText(7407, "", tda, 0, 0xFFFFFF, true, true);rsi.child(61, 7407, 330, 260);
	addText(7408, "", tda, 0, 0xFFFFFF, true, true);rsi.child(62, 7408, 330, 270);
	addText(7409, "", tda, 0, 0xFFFFFF, true, true);rsi.child(63, 7409, 330, 280);
	addText(7410, "", tda, 0, 0xFFFFFF, true, true);rsi.child(64, 7410, 400, 260);
	addText(7411, "", tda, 0, 0xFFFFFF, true, true);rsi.child(65, 7411, 400, 270);
	addText(7412, "", tda, 0, 0xFFFFFF, true, true);rsi.child(66, 7412, 400, 280);
}



	public static void addPriceChecker(int index) {
		RSInterface rsi = interfaceCache[index] = new RSInterface();
		rsi.actions = new String[10];
		rsi.spritesX = new int[20];
		rsi.inventoryAmounts = new int[25];
		rsi.inventoryItemId = new int[30];
		rsi.spritesY = new int[20];
		rsi.children = new int[0];
	 	rsi.childX = new int[0];
	 	rsi.childY = new int[0];
		rsi.actions[0] = "Take 1";
		rsi.actions[1] = "Take 5";
		rsi.actions[2] = "Take 10";
		rsi.actions[3] = "Take All";
		rsi.actions[4] = "Take X";
		rsi.centerText = true;
		rsi.aBoolean227 = false;
		rsi.replaceItems = false;
		rsi.usableItems = false;
		rsi.hasActions = false;
		rsi.allowSwapItems = true;
		rsi.textShadow = false;
		rsi.spritePaddingX = 38;
		rsi.spritePaddingY = 28;
		rsi.height = 5;
		rsi.width = 5;
		rsi.parentID = 43933;
		rsi.id = 43933;
		rsi.type = 2;
	}

	/*
	private static void priceChecker(TextDrawingArea[] tda) {
		RSInterface rsi = addTabInterface(43933);
		final String[] options = {"Remove 1", "Remove 5", "Remove 10", "Remove All", "Remove X"};
		addSprite(45000, 0, "Interfaces/PriceChecker/pc");

		addHoverButton(45001, "Interfaces/PriceChecker/close", 0, 17, 17, "Close", -1, 45002, 1);
		addHoveredButton(45002, "Interfaces/PriceChecker/close", 1, 17, 17, 45003);

		addHoverButton(45004, "Interfaces/PriceChecker/deposit", 0, 35, 35, "Deposit All", -1, 45005, 1);
		addHoveredButton(45005, "Interfaces/PriceChecker/deposit", 1, 35, 35, 45006);

		addHoverButton(45007, "Interfaces/PriceChecker/withdraw", 0, 35, 35, "Withdraw All", -1, 45008, 1);
		addHoveredButton(45008, "Interfaces/PriceChecker/withdraw", 1, 35, 35, 45009);

		addText(45010, "0", tda, 0, 0xFFFFFF, true, true);
		addText(45011, "", tda, 0, 0xFFFFFF, true, true);

		//Actual items
		RSInterface container = addTabInterface(43934);
		container.actions = options;
		container.spritesX = new int[20];
		container.spritesY = new int[20];
		container.inventoryItemId = new int[24];
		container.inventoryAmounts = new int[24];
		container.centerText = true;
		container.filled = false;
		container.replaceItems = false;
		container.usableItems = false;
		container.allowSwapItems = false;
		container.spritePaddingX = 50;
		container.spritePaddingY = 30;
		container.height = 6;
		container.width = 6;
		container.parentID = 43933;
		container.type = TYPE_INVENTORY;

		rsi.totalChildren(58);
		int child = 0;

		rsi.child(child++, 45000, 10, 20);//was 10 so + 10
		rsi.child(child++, 45001, 471, 23);
		rsi.child(child++, 45010, 251, 306);
		rsi.child(child++, 45011, 260, 155);
		rsi.child(child++, 45002, 471, 23); //Close button hover
		rsi.child(child++, 43934, 28, 50); //Container

		//Deposit hovers
		rsi.child(child++, 45004, 455, 285);
		rsi.child(child++, 45005, 455, 285);

		rsi.child(child++, 45007, 420, 285);
		rsi.child(child++, 45008, 420, 285);

		//Add text next to items, ROW 1
		int interface_ = 45100;
		int xDraw = 47;
		int yDraw = 81;
		int counter = 0;
		for(int i = 0; i < container.inventoryItemId.length; i++) {

			addText(interface_, "", tda, 0, 0xFFFFFF, true, true);
			rsi.child(child++, interface_, xDraw, yDraw);

			interface_++;
			counter++;
			xDraw += 80;

			if(counter == container.width) {
				xDraw = 47;
				yDraw += 62;
				counter = 0;
			}
		}

		//Add text next to items, ROW 2
		interface_ = 45200;
		xDraw = 47;
		yDraw = 93;
		counter = 0;
		for(int i = 0; i < container.inventoryItemId.length; i++) {

			addText(interface_, "", tda, 0, 0xFFFFFF, true, true);
			rsi.child(child++, interface_, xDraw, yDraw);

			interface_++;
			counter++;
			xDraw += 80;

			if(counter == container.width) {
				xDraw = 47;
				yDraw += 62;
				counter = 0;
			}
		}
	}
	*/
	
	public static void equipmentTab(TextDrawingArea[] wid) {
		RSInterface Interface = interfaceCache[1644];
		addSprite(15101, 0, "Interfaces/Equipment/bl");// cheap hax
		addSprite(15102, 1, "Interfaces/Equipment/bl");// cheap hax
		addSprite(15109, 2, "Interfaces/Equipment/bl");// cheap hax
		removeConfig(21338);
		removeConfig(21344);
		removeConfig(21342);
		removeConfig(21341);
		removeConfig(21340);
		removeConfig(15103);
		removeConfig(15104);
		// Interface.children[23] = 15101;
		// Interface.childX[23] = 40;
		// Interface.childY[23] = 205;
		Interface.children[24] = 15102;
		Interface.childX[24] = 110;
		Interface.childY[24] = 205;
		Interface.children[25] = 15109;
		Interface.childX[25] = 39;
		Interface.childY[25] = 240;
		Interface.children[26] = 27650;
		Interface.childX[26] = 0;
		Interface.childY[26] = 0;
		Interface = addInterface(27650);
		
		addHoverButton(27653, "Interfaces/Equipment/BOX", 1, 40, 40, "Show Equipment Stats", -1, 27655, 1); // Stats
		addHoveredButton(27655, "Interfaces/Equipment/HOVER", 1, 40, 40, 27665); // Stats Hover
		
		addHoverButton(27651, "Interfaces/Equipment/BOX", 2, 40, 40, "Show Presets", -1, 27652, 1); // Preset
		addHoveredButton(27652, "Interfaces/Equipment/HOVER", 2, 40, 40, 27664); // Preset Hover

		addHoverButton(27654, "Interfaces/Equipment/BOX", 3, 40, 40, "Show Items Kept on Death", -1, 27657, 1); // Items kept on death 
		addHoveredButton(27657, "Interfaces/Equipment/HOVER", 3, 40, 40, 27666); // Items kept on death hover
		
		addHoverButton(27658, "Interfaces/Equipment/BOX", 4, 40, 40, "Show Price Checker", -1, 27659, 1); // Price checker
		addHoveredButton(27659, "Interfaces/Equipment/HOVER", 4, 40, 40, 27667); // Price checker hover

		setChildren(8, Interface);
		
		setBounds(27653, 6, 210, 2, Interface); // Stats
		setBounds(27655, 6, 210, 4, Interface); // Stats hover
		
		setBounds(27651, 52, 210, 0, Interface); // Preset
		setBounds(27652, 52, 210, 1, Interface); // Preset hover
		
		setBounds(27654, 98, 210, 3, Interface); // Items kept on death
		setBounds(27657, 98, 210, 5, Interface); // Items kept on death hover
		
		setBounds(27658, 144, 210, 6, Interface); // Price checker
		setBounds(27659, 144, 210, 7, Interface); // Price checker hover
	}	
	
	public static void equipmentScreen(TextDrawingArea[] wid) {
		RSInterface Interface = RSInterface.interfaceCache[1644];
		//Equipment Tab
		addButton(19144, 6, "Interfaces/Equipment/CUSTOM", "Show Equipment Stats");
		removeSomething(19145);
		removeSomething(19146);
		removeSomething(19147);
		// setBounds(19144, 21, 210, 23, Interface);
		setBounds(19145, 40, 210, 24, Interface);
		setBounds(19146, 40, 210, 25, Interface);
		setBounds(19147, 40, 210, 26, Interface);

		//Equipment Stats
		RSInterface tab = addTabInterface(15106);
		addSprite(15107, 7, "Interfaces/Equipment/CUSTOM");	//Background
		addHoverButton(15210, "Interfaces/Equipment/CUSTOM", 8, 21, 21, "Close", 250, 15211, 3);	//Close Button
		addHoveredButton(15211, "Interfaces/Equipment/CUSTOM", 9, 21, 21, 15212);												//Close Button Hovered
		addText(15111, "Equip Your Character...", wid, 2, 0xe4a146, false, true);	//Equip your character
		addText(15112, "Attack bonus", wid, 2, 0xe4a146, false, true);				//Attack bonus
		addText(15113, "Defence bonus", wid, 2, 0xe4a146, false, true);				//Defence bonus
		addText(15114, "Other bonuses", wid, 2, 0xe4a146, false, true);				//Other bonuses
		addText(15116, "Ranged strength: +0", wid, 1, 0xe4a146, false, true);		//Ranged strength
		addText(15117, "Magic damage: +0%", wid, 1, 0xe4a146, false, true);			//Magic damage
		addText(15115, "Target-specific", wid, 2, 0xe4a146, false, true);			//Target-specific
		addText(15118, "Undead: 0%", wid, 1, 0xe4a146, false, true);					//Undead
		addText(15119, "Slayer: 0%", wid, 1, 0xe4a146, false, true);					//Slayer
		addText(15120, "Max Hits", wid, 2, 0xe4a146, false, true);					//Max hits
		addText(15121, "Melee:", wid, 1, 0xe4a146, false, true);							//Melee max
		addText(15122, "Magic:", wid, 1, 0xe4a146, false, true);							//Magic max
		addText(15123, "Range:", wid, 1, 0xe4a146, false, true);							//Range max
		interfaceCache[1686].message = "Melee strength: +0";
		for (int i = 1675; i <= 1684; i++) {
			textSize(i, wid, 1);
		}
		textSize(1686, wid, 1);
		textSize(1687, wid, 1);
		addChar(15125);
		tab.totalChildren(53);

		tab.child(49, 15120, 73, 264);
		tab.child(50, 15121, 38, 277);
		tab.child(51, 15122, 38, 290);
		tab.child(52, 15123, 38, 303);

		tab.child(0, 15107, 6, 5);
		tab.child(1, 15210, 477, 13);
		tab.child(2, 15211, 477, 13);
		tab.child(3, 15111, 16, 15);

		tab.child(16, 15112, 356, 35);		//Attack Bonus
		tab.child(4, 1675, 362, 48); 			//Off Stab
		tab.child(5, 1676, 362, 61); 			//Off Slash
		tab.child(6, 1677, 362, 74); 			//Off Crush
		tab.child(7, 1678, 362, 87); 			//Off Magic
		tab.child(8, 1679, 362, 100); 			//Off Range
		tab.child(18, 15113, 356, 120);	//Defence Bonus
		tab.child(9, 1680, 362, 133);			//Def Stab
		tab.child(10, 1681, 362, 146);			//Def Slash
		tab.child(11, 1682, 362, 159);			//Def Crush
		tab.child(12, 1683, 362, 172);			//Def Magic
		tab.child(13, 1684, 362, 185);			//Def Range
		tab.child(19, 15114, 356, 205);	//Other Bonuses
		tab.child(14, 1686, 362, 218);			//Melee Strength
		tab.child(45, 15116, 362, 231);		//Ranged strength
		tab.child(46, 15117, 362, 244);		//Magic damage
		tab.child(17, 1687, 362, 257);			//Prayer
		tab.child(44, 15115, 356, 277);	//Target-specific
		tab.child(47, 15118, 362, 290);		//Undead
		tab.child(48, 15119, 362, 303);		//Slayer

		//Player
		tab.child(15, 15125, 200, 200);

		//Equipment Slots and Interface
		tab.child(20, 1645, 84, 87);
		tab.child(21, 1646, 84, 120);
		tab.child(22, 1647, 84, 153);
		tab.child(23, 1648, 84, 194);
		tab.child(24, 1649, 28, 166);
		tab.child(25, 1650, 28, 202);
		tab.child(26, 1651, 140, 166);
		tab.child(27, 1652, 140, 202);
		tab.child(28, 1653, 54, 129);
		tab.child(29, 1654, 113, 129);
		tab.child(30, 1655, 64, 90);
		tab.child(31, 1656, 118, 89);
		tab.child(32, 1657, 84, 52);
		tab.child(33, 1658, 43, 91);
		tab.child(34, 1659, 84, 91);
		tab.child(35, 1660, 125, 91);
		tab.child(36, 1661, 28, 130);
		tab.child(37, 1662, 84, 130);
		tab.child(38, 1663, 140, 130);
		tab.child(39, 1664, 84, 170);
		tab.child(40, 1665, 84, 210);
		tab.child(41, 1666, 28, 210);
		tab.child(42, 1667, 140, 210);
		tab.child(43, 1688, 30, 92);	//All equipment

		for (int i = 1675; i <= 1684; i++) {
			RSInterface rsi = interfaceCache[i];
			rsi.textColor = 0xe4a146;
			rsi.centerText = false;
		}
		for (int i = 1686; i <= 1687; i++) {
			RSInterface rsi = interfaceCache[i];
			rsi.textColor = 0xe4a146;
			rsi.centerText = false;
		}

	}
	
	public static void addChar(int ID) {
		RSInterface t = interfaceCache[ID] = new RSInterface();
		t.id = ID;
		t.parentID = ID;
		t.type = 6;
		t.atActionType = 0;
		t.contentType = 328;
		t.width = 136;
		t.height = 168;
		t.aByte254 = 0;
		t.mOverInterToTrigger = 0;
		t.modelZoom = 560;
		t.modelRotation1 = 150;
		t.modelRotation2 = 0;
		t.anInt257 = -1;
		t.anInt258 = -1;
	}
	
	public int hoverTextColor;
	
	public static void clanChatSetup(TextDrawingArea[] tda) {
		RSInterface rsi = addInterface(18300);
		rsi.totalChildren(12 + 21);
		int count = 0;
		/* Background */
		addSprite(18301, 1, "/Interfaces/Clan Chat/sprite");
		rsi.child(count++, 18301, 14, 17);
		/* Close button */
		addButton(18302, 0, "/Interfaces/Clan Chat/close", "Close");
		interfaceCache[18302].atActionType = 3;
		rsi.child(count++, 18302, 475, 26);
		/* Clan Setup title */
		addText(18303, "Clan Setup", tda, 2, 0xFF981F, true, true);
		rsi.child(count++, 18303, 256, 26);
		String[] titles = { "Clan name:", "Who can enter chat?",
				"Who can talk on chat?", "Who can kick on chat?",
				"Who can ban on chat?" };
		String[] defaults = { "Chat Disabled", "Anyone", "Anyone", "Anyone",
				"Anyone"};
		String[] whoCan = { "Anyone", "Recruit", "Corporal", "Sergeant",
				"Lieutenant", "Captain", "General", "Only Me" };
		for (int index = 0, id = 18304, y = 50; index < titles.length; index++, id += 3, y += 40) {
			addButton(id, 2, "/Interfaces/Clan Chat/sprite", "");
			interfaceCache[id].atActionType = 0;
			if (index > 0) {
				interfaceCache[id].actions = whoCan;
			} else {
				interfaceCache[id].actions = new String[] { "Change title",
						"Delete clan" };
			}
			addText(id + 1, titles[index], tda, 0, 0xFF981F, true, true);
			addText(id + 2, defaults[index], tda, 1, 0xFFFFFF, true, true);
			rsi.child(count++, id, 25, y);
			rsi.child(count++, id + 1, 100, y + 4);
			rsi.child(count++, id + 2, 100, y + 17);
		}
		/* Table */
		addSprite(18319, 5, "/Interfaces/Clan Chat/sprite");
		rsi.child(count++, 18319, 197, 70);
		/* Labels */
		int id = 18320;
		int y = 74;
		addText(id, "Ranked Members", tda, 2, 0xFF981F, false, true);
		rsi.child(count++, id++, 202, y);
		addText(id, "Banned Members", tda, 2, 0xFF981F, false, true);
		rsi.child(count++, id++, 339, y);
		/* Ranked members list */
		RSInterface list = addInterface(id++);
		int lines = 100;
		list.totalChildren(lines);
		String[] ranks = { "Demote", "Recruit", "Corporal", "Sergeant",
				"Lieutenant", "Captain", "General", "Owner" };
		list.childY[0] = 2;
		// System.out.println(id);
		for (int index = id; index < id + lines; index++) {
			addText(index, "", tda, 1, 0xffffff, false, true);
			interfaceCache[index].actions = ranks;
			list.children[index - id] = index;
			list.childX[index - id] = 2;
			list.childY[index - id] = (index - id > 0 ? list.childY[index - id
					- 1] + 14 : 0);
		}
		id += lines;
		list.width = 119;
		list.height = 210;
		list.scrollMax = (lines * 14) + 2;
		rsi.child(count++, list.id, 199, 92);
		/* Banned members list */
		list = addInterface(id++);
		list.totalChildren(lines);
		list.childY[0] = 2;
		// System.out.println(id);
		for (int index = id; index < id + lines; index++) {
			addText(index, "", tda, 1, 0xffffff, false, true);
			interfaceCache[index].actions = new String[] { "Unban" };
			list.children[index - id] = index;
			list.childX[index - id] = 0;
			list.childY[index - id] = (index - id > 0 ? list.childY[index - id
					- 1] + 14 : 0);
		}
		id += lines;
		list.width = 119;
		list.height = 210;
		list.scrollMax = (lines * 14) + 2;
		rsi.child(count++, list.id, 339, 92);
		/* Table info text */
		y = 47;
		addText(id, "You can manage both ranked and banned members here.", tda,
				0, 0xFF981F, true, true);
		rsi.child(count++, id++, 337, y);
		addText(id, "Right click on a name to edit the member.", tda, 0,
				0xFF981F, true, true);
		rsi.child(count++, id++, 337, y + 11);
		/* Add ranked member button */
		y = 75;
		addButton(id, 0, "/Interfaces/Clan Chat/plus", "Add ranked member");
		interfaceCache[id].atActionType = 5;
		rsi.child(count++, id++, 319, y);
		/* Add banned member button */
		addButton(id, 0, "/Interfaces/Clan Chat/plus", "Add banned member");
		interfaceCache[id].atActionType = 5;
		rsi.child(count++, id++, 459, y);
		
		//addButton(id, 2, "/Interfaces/Clan Chat/sprite", "Allow Teleports");
		addHoverButton(id, "Interfaces/Clan Chat/sprite", 9, 150, 27, "Allow Teleports", 201, id, 5);
		rsi.child(count++, id++, 25, 248);
		addText(id, "Allow Teleports :", tda, 1,
				0xFFFFFF, true, true);
		rsi.child(count++, id++, 101, 254);
		addText(id, "@red@No", tda, 1,
				0xff0000, true, true);
		rsi.child(count++, id++, 157, 255);
		
		addHoverButton(id, "Interfaces/Clan Chat/sprite", 12, 150, 27, "Copy-kit", 201, id, 5);
		rsi.child(count++, id++, 25, 278);
		addText(id, "Allow Copy Kit :", tda, 1,
				0xFFFFFF, true, true);
		rsi.child(count++, id++, 101, 284);
		addText(id, "No", tda, 1,
				0xff0000, true, true);
		rsi.child(count++, id++, 157, 285);

		/* Hovers */
		int[] clanSetup = { 18302, 18304, 18307, 18310, 18313, 18316, 18526,
				18527 };
		String[] names = { "close", "sprite", "sprite", "sprite", "sprite",
				"sprite", "plus", "plus" };
		int[] ids = { 1, 3, 3, 3, 3, 3, 1, 1 };
		for (int index = 0; index < clanSetup.length; index++) {
			rsi = interfaceCache[clanSetup[index]];
			rsi.disabledHover = imageLoader(ids[index],
					"/Interfaces/Clan Chat/" + names[index]);
		}
	}
	
	public static void clanChatTab(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(18128);
		addHoverButton(18129, "Interfaces/Clan Chat/SPRITE", 6, 72, 32, "Join Clan", 550,
				18130, 5);
		addHoveredButton(18130, "Interfaces/Clan Chat/SPRITE", 7, 72, 32, 18131);
		addHoverButton(18132, "Interfaces/Clan Chat/SPRITE", 6, 72, 32, "Clan Setup", -1,
				18133, 5);
		addHoveredButton(18133, "Interfaces/Clan Chat/SPRITE", 7, 72, 32, 18134);
		addText(18135, "Join Clan", tda, 0, 0xff9b00, true, true);
		addText(18136, "Clan Setup", tda, 0, 0xff9b00, true, true);
		addSprite(18137, 37, "Interfaces/Clan Chat/SPRITE");
		addText(18138, "Clan Chat", tda, 2, 0xff9b00, true, true);
		addText(18139, "Talking in: Not in clan", tda, 0, 0xff9b00, false, true);
		addText(18140, "Owner: None", tda, 0, 0xff9b00, false, true);
		//addConfigButton(17145, 18128, 0, 1, "Interfaces/Clan Chat/Lootshare", 34, 29, "Toggle killfeed", 1, 5, 542);
		tab.totalChildren(11);
		tab.child(0, 18137, 5, 57);
		tab.child(1, 18143, 0, 62);
		tab.child(2, 18129, 15, 226);
		tab.child(3, 18130, 15, 226);
		tab.child(4, 18132, 103, 226);
		tab.child(5, 18133, 103, 226);
		tab.child(6, 18135, 51, 237);
		tab.child(7, 18136, 139, 237);
		tab.child(8, 18138, 95, 4);
		tab.child(9, 18139, 10, 27);
		tab.child(10, 18140, 25, 42);
		//tab.child(11, 17145, 145, 15);
		/* Text area */
		RSInterface list = addTabInterface(18143);
		list.totalChildren(100);
		for (int i = 18144; i <= 18244; i++) {
			addText(i, "", tda, 0, 0xffffff, false, true);
		}
		for (int id = 18144, i = 0; id <= 18243 && i <= 99; id++, i++) {
			interfaceCache[id].actions = new String[] { "Edit Rank", "Kick",
					"Ban", "Add", "Ignore", "Tele-To", "Copy-Kit" };
			list.children[i] = id;
			list.childX[i] = 20;
			for (int id2 = 18144, i2 = 1; id2 <= 18243 && i2 <= 99; id2++, i2++) {
				list.childY[0] = 7;
				list.childY[i2] = list.childY[i2 - 1] + 14;
			}
		}
		list.height = 158;
		list.width = 174;
		list.scrollMax = 1405;
	}

	
	public Sprite disabledHover;
	public Sprite enabledHover;
	
	public static void addHoverText2(int id, String text, String[] tooltips,
			TextDrawingArea tda[], int idx, int color, boolean center,
			boolean textShadowed, int width) {
		RSInterface rsinterface = addInterface(id);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 4;
		rsinterface.atActionType = 1;
		rsinterface.width = width;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.aByte254 = 0;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.centerText = center;
		rsinterface.textShadow = textShadowed;
		rsinterface.textDrawingAreas = tda[idx];
		rsinterface.message = text;
		rsinterface.aString228 = "";
		rsinterface.textColor = color;
		rsinterface.anInt219 = 0;
		rsinterface.anInt216 = 0xffffff;
		rsinterface.anInt239 = 0;
		rsinterface.tooltips = tooltips;
	}
	
	public static void addText2(int id, String text, TextDrawingArea tda[],
			int idx, int color, boolean center, boolean shadow) {
		RSInterface tab = addTabInterface(id);
		tab.parentID = id;
		tab.id = id;
		tab.type = 4;
		tab.atActionType = 0;
		tab.width = 0;
		tab.height = 11;
		tab.contentType = 0;
		tab.aByte254 = 0;
		tab.mOverInterToTrigger = -1;
		tab.centerText = center;
		tab.textShadow = shadow;
		tab.textDrawingAreas = tda[idx];
		tab.message = text;
		tab.aString228 = "";
		tab.textColor = color;
		tab.anInt219 = 0;
		tab.anInt216 = 0;
		tab.anInt239 = 0;
	}
	
	// addBankHover
	public static void addConfigHover(int interfaceID, int actionType,
			int hoverid, int spriteId, int spriteId2, String NAME, int Width,
			int Height, int configFrame, int configId, String Tooltip,
			int hoverId2, int hoverSpriteId, int hoverSpriteId2,
			String hoverSpriteName, int hoverId3, String hoverDisabledText,
			String hoverEnabledText, int X, int Y) {
		RSInterface hover = addTabInterface(interfaceID);
		hover.id = interfaceID;
		hover.parentID = interfaceID;
		hover.type = 5;
		hover.atActionType = actionType;
		hover.contentType = 0;
		hover.aByte254 = 0;
		hover.mOverInterToTrigger = hoverid;
		hover.sprite1 = imageLoader(spriteId, NAME);
		hover.sprite2 = imageLoader(spriteId2, NAME);
		hover.width = Width;
		hover.tooltip = Tooltip;
		hover.height = Height;
		hover.anIntArray245 = new int[1];
		hover.anIntArray212 = new int[1];
		hover.anIntArray245[0] = 1;
		hover.anIntArray212[0] = configId;
		hover.valueIndexArray = new int[1][3];
		hover.valueIndexArray[0][0] = 5;
		hover.valueIndexArray[0][1] = configFrame;
		hover.valueIndexArray[0][2] = 0;
		hover = addTabInterface(hoverid);
		hover.parentID = hoverid;
		hover.id = hoverid;
		hover.type = 0;
		hover.atActionType = 0;
		hover.width = 550;
		hover.height = 334;
		hover.isMouseoverTriggered = true;
		hover.mOverInterToTrigger = -1;
		addSprites(hoverId2, hoverSpriteId, hoverSpriteId2, hoverSpriteName,
				configId, configFrame);
		addHoverBox(hoverId3, interfaceID, hoverDisabledText, hoverEnabledText,
				configId, configFrame);
		setChildren(2, hover);
		setBounds(hoverId2, 15, 60, 0, hover);
		setBounds(hoverId3, X, Y, 1, hover);
	}

	  public static void drawTooltip(int id, String text) {
	        RSInterface rsinterface = addTabInterface(id);
	        rsinterface.parentID = id;
	        rsinterface.type = 0;
	        rsinterface.isMouseoverTriggered = true;
	        rsinterface.hoverType = -1;
	        addTooltipBox(id + 1, text);
	        rsinterface.totalChildren(1);
	        rsinterface.child(0, id + 1, 0, 0);
	    }

	 public static void addSprites(int ID, int i, int i2, String name,
             int configId, int configFrame)
     {
             RSInterface Tab = addTabInterface(ID);
             Tab.id = ID;
             Tab.parentID = ID;
             Tab.type = 5;
             Tab.atActionType = 0;
             Tab.contentType = 0;
             Tab.width = 512;
             Tab.height = 334;
             Tab.aByte254 = (byte) 0;
             Tab.mOverInterToTrigger = -1;
             Tab.anIntArray245 = new int[1];
             Tab.anIntArray212 = new int[1];
             Tab.anIntArray245[0] = 1;
             Tab.anIntArray212[0] = configId;
             Tab.valueIndexArray = new int[1][3];
             Tab.valueIndexArray[0][0] = 5;
             Tab.valueIndexArray[0][1] = configFrame;
             Tab.valueIndexArray[0][2] = 0;
             Tab.sprite1 = imageLoader(i, name);
             Tab.sprite2 = imageLoader(i2, name);
     }
	
	 public static void addBankHover(int interfaceID, int actionType,
             int hoverid, int spriteId, int spriteId2, String NAME, int Width,
             int Height, int configFrame, int configId, String Tooltip,
             int hoverId2, int hoverSpriteId, int hoverSpriteId2,
             String hoverSpriteName, int hoverId3, String hoverDisabledText,
             String hoverEnabledText, int X, int Y)
     {
             RSInterface hover = addTabInterface(interfaceID);
             hover.id = interfaceID;
             hover.parentID = interfaceID;
             hover.type = 5;
             hover.atActionType = actionType;
             hover.contentType = 0;
             hover.aByte254 = 0;
             hover.mOverInterToTrigger = hoverid;
             hover.sprite1 = imageLoader(spriteId, NAME);
             hover.sprite2 = imageLoader(spriteId2, NAME);
             hover.width = Width;
             hover.tooltip = Tooltip;
             hover.height = Height;
             hover.anIntArray245 = new int[1];
             hover.anIntArray212 = new int[1];
             hover.anIntArray245[0] = 1;
             hover.anIntArray212[0] = configId;
             hover.valueIndexArray = new int[1][3];
             hover.valueIndexArray[0][0] = 5;
             hover.valueIndexArray[0][1] = configFrame;
             hover.valueIndexArray[0][2] = 0;
             hover = addTabInterface(hoverid);
             hover.parentID = hoverid;
             hover.id = hoverid;
             hover.type = 0;
             hover.atActionType = 0;
             hover.width = 550;
             hover.height = 334;
             hover.isMouseoverTriggered = true;
             hover.mOverInterToTrigger = -1;
             addSprites(hoverId2, hoverSpriteId, hoverSpriteId2, hoverSpriteName,
                     configId, configFrame);
             addHoverBox(hoverId3, interfaceID, hoverDisabledText, hoverEnabledText,
                     configId, configFrame);
             setChildren(2, hover);
             setBounds(hoverId2, 15, 60, 0, hover);
             setBounds(hoverId3, X, Y, 1, hover);
     }

	public static void addSlider(int id, int width, int barInterface, int barId, String barName, int knobInterface, int knobId, String knobName) {
		RSInterface tab = addInterface(id);
		tab.type = 40;
		tab.width = width;
		tab.height = 14;
		tab.atActionType = 7;
		tab.tooltip = "Slide to change";
		addSprite(barInterface, barId, barName);
		addSprite(knobInterface, knobId, knobName);
		tab.sprites = new Sprite[] {RSInterface.interfaceCache[barInterface].sprite1, RSInterface.interfaceCache[knobInterface].sprite1};
	}

     public static void addBankHover1(int interfaceID, int actionType,
             int hoverid, int spriteId, String NAME, int Width, int Height,
             String Tooltip, int hoverId2, int hoverSpriteId,
             String hoverSpriteName, int hoverId3, String hoverDisabledText,
             int X, int Y)
     {
             RSInterface hover = addTabInterface(interfaceID);
             hover.id = interfaceID;
             hover.parentID = interfaceID;
             hover.type = 5;
             hover.atActionType = actionType;
             hover.contentType = 0;
             hover.aByte254 = 0;
             hover.mOverInterToTrigger = hoverid;
             hover.sprite1 = imageLoader(spriteId, NAME);
             hover.width = Width;
             hover.tooltip = Tooltip;
             hover.height = Height;

             hover = addTabInterface(hoverid);
             hover.parentID = hoverid;
             hover.id = hoverid;
             hover.type = 0;
             hover.atActionType = 0;
             hover.width = 550;
             hover.height = 334;
             hover.isMouseoverTriggered = true;
             hover.mOverInterToTrigger = -1;
             addSprites(hoverId2, hoverSpriteId, hoverSpriteId, hoverSpriteName, 0,
                     0);
             addHoverBox(hoverId3, interfaceID, hoverDisabledText,
                     hoverDisabledText, 0, 0);
             setChildren(2, hover);
             setBounds(hoverId2, 15, 60, 0, hover);
             setBounds(hoverId3, X, Y, 1, hover);
     }
     
     public static void addHover(int i, int aT, int cT, int hoverid, int sId,
             String NAME, int W, int H, String tip)
     {
             RSInterface rsinterfaceHover = addInterface(i);
             rsinterfaceHover.id = i;
             rsinterfaceHover.parentID = i;
             rsinterfaceHover.type = 5;
             rsinterfaceHover.atActionType = aT;
             rsinterfaceHover.contentType = cT;
             rsinterfaceHover.mOverInterToTrigger = hoverid;
             rsinterfaceHover.sprite1 = imageLoader(sId, NAME);
             rsinterfaceHover.sprite2 = imageLoader(sId, NAME);
             rsinterfaceHover.width = W;
             rsinterfaceHover.height = H;
             rsinterfaceHover.tooltip = tip;
     }

 	public static void addHovered(int i, int j, String imageName, int w, int h,
			int IMAGEID) {
		RSInterface rsinterfaceHover = addInterface(i);
		rsinterfaceHover.parentID = i;
		rsinterfaceHover.id = i;
		rsinterfaceHover.type = 0;
		rsinterfaceHover.atActionType = 0;
		rsinterfaceHover.width = w;
		rsinterfaceHover.height = h;
		rsinterfaceHover.isMouseoverTriggered = true;
		rsinterfaceHover.mOverInterToTrigger = -1;
		addSprite(IMAGEID, j, imageName);
		setChildren(1, rsinterfaceHover);
		setBounds(IMAGEID, 0, 0, 0, rsinterfaceHover);
	}
	
	public boolean hasExamine = true;
	
	public static void addBankItem(int index, boolean hasOption)
	{
		RSInterface rsi = interfaceCache[index] = new RSInterface();
		rsi.actions = new String[5];
		rsi.spritesX = new int[20];
		rsi.inventoryAmounts = new int[30];
		rsi.inventoryItemId = new int[30];
		rsi.spritesY = new int[20];
		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];
		rsi.hasExamine = false;
		rsi.spritePaddingX = 24;
		rsi.spritePaddingY = 24;
		rsi.height = 5;
		rsi.width = 6;
		rsi.parentID = 5292;
		rsi.id = index;
		rsi.type = 2;
	}
	
	  public static void addTextButton(int i, String s, String tooltip, int k, boolean l, boolean m, TextDrawingArea[] TDA, int j, int w) {
	        RSInterface rsinterface = addInterface(i);
	        rsinterface.parentID = i;
	        rsinterface.id = i;
	        rsinterface.type = 4;
	        rsinterface.atActionType = 1;
	        rsinterface.width = w;
	        rsinterface.height = 16;
	        rsinterface.contentType = 0;
	        rsinterface.aByte254 = (byte)0xFF981F;
	        rsinterface.mOverInterToTrigger = -1;
	        rsinterface.centerText = l;
	        rsinterface.textShadow = m;
	        rsinterface.textDrawingAreas = TDA[j];
	        rsinterface.message = s;
	        rsinterface.aString228 = "";
	        rsinterface.anInt219 = 0xFF981F;
	        rsinterface.textColor = 0xFF981F;
	        rsinterface.tooltip = tooltip;
	    }
	 
	
	public static void nightmareZone(TextDrawingArea[] tda) {
		 RSInterface nz = addInterface(920);
		 addSprite(921, 0, "Nightmare/SPRITE");
		 addText(922, "0", tda, 0, 0xFF981F, true, true);
		 addText(923, "Points:", tda, 0, 0xFF981F, false, true);
	     nz.totalChildren(3);
	     nz.child(0, 921, 460, 14);
	     nz.child(1, 922, 481, 29);
	     nz.child(2, 923, 465, 19);
	}
	
	 public static void addClickableText(int id, String text, String tooltip, TextDrawingArea tda[], int idx, int color, boolean center, boolean shadow, int width) {
	        RSInterface tab = addTabInterface(id);
	        tab.parentID = id;
	        tab.id = id;
	        tab.type = 4;
	        tab.atActionType = 1;
	        tab.width = width;
	        tab.height = 11;
	        tab.contentType = 0;
	        tab.aByte254 = 0;
	        tab.mOverInterToTrigger = -1;
	        tab.centerText = center;
	        tab.textShadow = shadow;
	        tab.textDrawingAreas = tda[idx];
	        tab.message = text;
	        tab.aString228 = "";
	        tab.textColor = color;
	        tab.anInt219 = 0;
	        tab.anInt216 = 0xffffff;
	        tab.anInt239 = 0;
	        tab.tooltip = tooltip;
	    }
	 
	public static void edgevilleHomeTeleport(TextDrawingArea[] TDA) {
		RSInterface rsi = interfaceCache[21741];
		rsi.atActionType = 1;
		rsi.tooltip = "Cast @gre@Edgeville Home Teleport";
	}

	public static void addButton(int id, int sid, String spriteName,
			String tooltip) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 1;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = tab.sprite1.myWidth;
		tab.height = tab.sprite2.myHeight;
		tab.tooltip = tooltip;
	}
	
	public static void addButton(int id, int spriteId, String spriteName, String tooltip, int actionType, int mouseOverTrigger) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = actionType;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = mouseOverTrigger;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName);
		tab.width = tab.sprite1.myWidth;
		tab.height = tab.sprite2.myHeight;
		tab.tooltip = tooltip;
	}
	
	public static void addPrayer(int i, int configId, int configFrame,
			int requiredValues, int spriteID, String prayerName) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = 5608;
		tab.type = 5;
		tab.atActionType = 4;
		tab.contentType = 0;
		tab.aByte254 = 0;
		tab.mOverInterToTrigger = -1;
		tab.sprite1 = imageLoader(0, "PRAYERGLOW");
		tab.sprite2 = imageLoader(1, "PRAYERGLOW");
		tab.width = 34;
		tab.height = 34;
		tab.anIntArray245 = new int[1];
		tab.anIntArray212 = new int[1];
		tab.anIntArray245[0] = 1;
		tab.anIntArray212[0] = configId;
		tab.valueIndexArray = new int[1][3];
		tab.valueIndexArray[0][0] = 5;
		tab.valueIndexArray[0][1] = configFrame;
		tab.valueIndexArray[0][2] = 0;
		// tab.tooltip = "Activate@or2@ " + prayerName;
		tab.tooltip = "Select";
		RSInterface tab2 = addTabInterface(i + 1);
		tab2.id = i + 1;
		tab2.parentID = 5608;
		tab2.type = 5;
		tab2.atActionType = 0;
		tab2.contentType = 0;
		tab2.aByte254 = 0;
		tab2.mOverInterToTrigger = -1;
		tab2.sprite1 = imageLoader(spriteID, "Prayer/PRAYON");
		tab2.sprite2 = imageLoader(spriteID, "Prayer/PRAYOFF");
		tab2.width = 34;
		tab2.height = 34;
		tab2.anIntArray245 = new int[1];
		tab2.anIntArray212 = new int[1];
		tab2.anIntArray245[0] = 2;
		tab2.anIntArray212[0] = requiredValues + 1;
		tab2.valueIndexArray = new int[1][3];
		tab2.valueIndexArray[0][0] = 2;
		tab2.valueIndexArray[0][1] = 5;
		tab2.valueIndexArray[0][2] = 0;
	}
	
	protected static void addOldPrayer(int id, String prayerName) {
		RSInterface rsi = interfaceCache[id];
		rsi.tooltip = "Activate@or2@ " + prayerName;
	}

	public static void addPrayerHover(int i, int hoverID, int prayerSpriteID,
			String hoverText) {
		RSInterface Interface = addTabInterface(i);
		Interface.id = i;
		Interface.parentID = 5608;
		Interface.type = 5;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.aByte254 = 0;
		Interface.mOverInterToTrigger = hoverID;
		Interface.sprite2 = imageLoader(0, "tabs/prayer/hover/PRAYERH");
		Interface.sprite1 = imageLoader(0, "tabs/prayer/hover/PRAYERH");
		Interface.width = 34;
		Interface.height = 34;

		Interface = addTabInterface(hoverID);
		Interface.id = hoverID;
		Interface.parentID = 5608;
		Interface.type = 0;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.aByte254 = 0;
		Interface.mOverInterToTrigger = -1;
		Interface.width = 512;
		Interface.height = 334;
		Interface.isMouseoverTriggered = true;
		addBox(hoverID + 1, 0, false, 0x000000, hoverText);
		setChildren(1, Interface);
		setBounds(hoverID + 1, 0, 0, 0, Interface);
	}

	public static void addTooltipBox(int id, String text) {
		RSInterface rsi = addInterface(id);
		rsi.id = id;
		rsi.parentID = id;
		rsi.type = 9;
		rsi.message = text;
	}
	
	public static void addTooltip(int id, String text) {
		RSInterface rsi = addInterface(id);
		rsi.id = id;
		rsi.type = 0;
		rsi.isMouseoverTriggered = true;
		rsi.mOverInterToTrigger = -1;
		addTooltipBox(id + 1, text);
		rsi.totalChildren(1);
		rsi.child(0, id + 1, 0, 0);
	}

	protected static Sprite CustomSpriteLoader(int id, String s) {
		long l = (TextClass.method585(s) << 8) + id;
		Sprite sprite = (Sprite) aMRUNodes_238.insertFromCache(l);
		if (sprite != null) {
			return sprite;
		}
		try {
			sprite = new Sprite("/Attack/" + id + s);
			aMRUNodes_238.removeFromCache(sprite, l);
		} catch (Exception exception) {
			return null;
		}
		return sprite;
	}

	public static RSInterface addInterface(int id) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.id = id;
		rsi.parentID = id;
		rsi.width = 512;
		rsi.height = 334;
		return rsi;
	}

	public static void addText(int id, String text, TextDrawingArea tda[], int idx, int color, boolean centered) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		if (centered)
			rsi.centerText = true;
		rsi.textShadow = true;
		rsi.textDrawingAreas = tda[idx];
		rsi.message = text;
		rsi.textColor = color;
		rsi.id = id;
		rsi.type = 4;
	}

	public static void textColor(int id, int color) {
		RSInterface rsi = interfaceCache[id];
		rsi.textColor = color;
	}

	public static void textSize(int id, TextDrawingArea tda[], int idx) {
		RSInterface rsi = interfaceCache[id];
		rsi.textDrawingAreas = tda[idx];
	}

	public static void addCacheSprite(int id, int sprite1, int sprite2, String sprites) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.sprite1 = method207(sprite1, aClass44, sprites);
		rsi.sprite2 = method207(sprite2, aClass44, sprites);
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
	}

	public static void sprite1(int id, int sprite) {
		RSInterface class9 = interfaceCache[id];
		class9.sprite1 = CustomSpriteLoader(sprite, "");
	}

	public static void addActionButton(int id, int sprite, int sprite2, int width, int height, String s) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.sprite1 = CustomSpriteLoader(sprite, "");
		if (sprite2 == sprite)
			rsi.sprite2 = CustomSpriteLoader(sprite, "a");
		else
			rsi.sprite2 = CustomSpriteLoader(sprite2, "");
		rsi.tooltip = s;
		rsi.contentType = 0;
		rsi.atActionType = 1;
		rsi.width = width;
		rsi.mOverInterToTrigger = 52;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
	}

	public static void addToggleButton(int id, int sprite, int setconfig, int width, int height, String s) {
		RSInterface rsi = addInterface(id);
		rsi.sprite1 = CustomSpriteLoader(sprite, "");
		rsi.sprite2 = CustomSpriteLoader(sprite, "a");
		rsi.anIntArray212 = new int[1];
		rsi.anIntArray212[0] = 1;
		rsi.anIntArray245 = new int[1];
		rsi.anIntArray245[0] = 1;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = setconfig;
		rsi.valueIndexArray[0][2] = 0;
		rsi.atActionType = 4;
		rsi.width = width;
		rsi.mOverInterToTrigger = -1;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
		rsi.tooltip = s;
	}

	public void totalChildren(int id, int x, int y) {
		children = new int[id];
		childX = new int[x];
		childY = new int[y];
	}

	public static void removeSomething(int id) {
		@SuppressWarnings("unused")
		RSInterface rsi = interfaceCache[id] = new RSInterface();
	}

	public void specialBar(int id) // 7599
	{
		/*
		 * addActionButton(ID, SpriteOFF, SpriteON, Width, Height,
		 * "SpriteText");
		 */
		addActionButton(id - 12, 7587, -1, 150, 26, "Use @gre@Special Attack");
		/* removeSomething(ID); */
		for (int i = id - 11; i < id; i++)
			removeSomething(i);

		RSInterface rsi = interfaceCache[id - 12];
		rsi.width = 150;
		rsi.height = 26;

		rsi = interfaceCache[id];
		rsi.width = 150;
		rsi.height = 26;

		rsi.child(0, id - 12, 0, 0);

		rsi.child(12, id + 1, 3, 7);

		rsi.child(23, id + 12, 16, 8);

		for (int i = 13; i < 23; i++) {
			rsi.childY[i] -= 1;
		}

		rsi = interfaceCache[id + 1];
		rsi.type = 5;
		rsi.sprite1 = CustomSpriteLoader(7600, "");

		for (int i = id + 2; i < id + 12; i++) {
			rsi = interfaceCache[i];
			rsi.type = 5;
		}

		sprite1(id + 2, 7601);
		sprite1(id + 3, 7602);
		sprite1(id + 4, 7603);
		sprite1(id + 5, 7604);
		sprite1(id + 6, 7605);
		sprite1(id + 7, 7606);
		sprite1(id + 8, 7607);
		sprite1(id + 9, 7608);
		sprite1(id + 10, 7609);
		sprite1(id + 11, 7610);
	}

	public static void Sidebar0(TextDrawingArea[] tda) {
		/*
		 * Sidebar0a(id, id2, id3, "text1", "text2", "text3", "text4", str1x,
		 * str1y, str2x, str2y, str3x, str3y, str4x, str4y, img1x, img1y, img2x,
		 * img2y, img3x, img3y, img4x, img4y, tda);
		 */
		Sidebar0a(1698, 1701, 7499, "Chop", "Hack", "Smash", "Block", 42, 75, 127, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(2276, 2279, 7574, "Stab", "Lunge", "Slash", "Block", 43, 75, 124, 75, 41, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(2423, 2426, 7599, "Chop", "Slash", "Lunge", "Block", 42, 75, 125, 75, 40, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(3796, 3799, 7624, "Pound", "Pummel", "Spike", "Block", 39, 75, 121, 75, 41, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(4679, 4682, 7674, "Lunge", "Swipe", "Pound", "Block", 40, 75, 124, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(4705, 4708, 7699, "Chop", "Slash", "Smash", "Block", 42, 75, 125, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(5570, 5573, 7724, "Spike", "Impale", "Smash", "Block", 41, 75, 123, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(7762, 7765, 7800, "Chop", "Slash", "Lunge", "Block", 42, 75, 125, 75, 40, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		/*
		 * Sidebar0b(id, id2, "text1", "text2", "text3", "text4", str1x, str1y,
		 * str2x, str2y, str3x, str3y, str4x, str4y, img1x, img1y, img2x, img2y,
		 * img3x, img3y, img4x, img4y, tda);
		 */
		Sidebar0b(776, 779, "Reap", "Chop", "Jab", "Block", 42, 75, 126, 75, 46, 128, 125, 128, 122, 103, 122, 50, 40, 103, 40, 50, tda);
		/*
		 * Sidebar0c(id, id2, id3, "text1", "text2", "text3", str1x, str1y,
		 * str2x, str2y, str3x, str3y, img1x, img1y, img2x, img2y, img3x, img3y,
		 * tda);
		 */
		Sidebar0c(425, 428, 7474, "Pound", "Pummel", "Block", 39, 75, 121, 75, 42, 128, 40, 103, 40, 50, 122, 50, tda);
		Sidebar0c(1749, 1752, 7524, "Accurate", "Rapid", "Longrange", 33, 75, 125, 75, 29, 128, 40, 103, 40, 50, 122, 50, tda);
		Sidebar0c(1764, 1767, 7549, "Accurate", "Rapid", "Longrange", 33, 75, 125, 75, 29, 128, 40, 103, 40, 50, 122, 50, tda);
		Sidebar0c(4446, 4449, 7649, "Accurate", "Rapid", "Longrange", 33, 75, 125, 75, 29, 128, 40, 103, 40, 50, 122, 50, tda);
		Sidebar0c(5855, 5857, 7749, "Punch", "Kick", "Block", 40, 75, 129, 75, 42, 128, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0c(6103, 6132, 6117, "Bash", "Pound", "Block", 43, 75, 124, 75, 42, 128, 40, 103, 40, 50, 122, 50, tda);
		Sidebar0c(8460, 8463, 8493, "Jab", "Swipe", "Fend", 46, 75, 124, 75, 43, 128, 40, 103, 40, 50, 122, 50, tda);
		Sidebar0c(12290, 12293, 12323, "Flick", "Lash", "Deflect", 44, 75, 127, 75, 36, 128, 40, 50, 40, 103, 122, 50, tda);
		/*
		 * Sidebar0d(id, id2, "text1", "text2", "text3", str1x, str1y, str2x,
		 * str2y, str3x, str3y, img1x, img1y, img2x, img2y, img3x, img3y, tda);
		 */
		Sidebar0d(328, 331, "Bash", "Pound", "Focus", 42, 66, 39, 101, 41, 136, 40, 120, 40, 50, 40, 85, tda);

		RSInterface rsi = addInterface(19300);
		/* textSize(ID, wid, Size); */
		textSize(3983, tda, 0);
		/* addToggleButton(id, sprite, config, width, height, wid); */
		addToggleButton(150, 150, 172, 150, 44, "Auto Retaliate");

		rsi.totalChildren(2, 2, 2);
		rsi.child(0, 3983, 52, 25); // combat level
		rsi.child(1, 150, 21, 153); // auto retaliate

		rsi = interfaceCache[3983];
		rsi.centerText = true;
		rsi.textColor = 0xff981f;
	}

	public static void Sidebar0a(int id, int id2, int id3, String text1, String text2, String text3, String text4, int str1x, int str1y, int str2x, int str2y, int str3x, int str3y, int str4x, int str4y, int img1x, int img1y, int img2x, int img2y, int img3x, int img3y, int img4x, int img4y, TextDrawingArea[] tda) // 4button
																																																																															// spec
	{
		RSInterface rsi = addInterface(id); // 2423
		/* addText(ID, "Text", tda, Size, Colour, Centered); */
		addText(id2, "-2", tda, 3, 0xff981f, true); // 2426
		addText(id2 + 11, text1, tda, 0, 0xff981f, false);
		addText(id2 + 12, text2, tda, 0, 0xff981f, false);
		addText(id2 + 13, text3, tda, 0, 0xff981f, false);
		addText(id2 + 14, text4, tda, 0, 0xff981f, false);
		/* specialBar(ID); */
		rsi.specialBar(id3); // 7599

		rsi.width = 190;
		rsi.height = 261;

		int last = 15;
		int frame = 0;
		rsi.totalChildren(last, last, last);

		rsi.child(frame, id2 + 3, 21, 46);
		frame++; // 2429
		rsi.child(frame, id2 + 4, 104, 99);
		frame++; // 2430
		rsi.child(frame, id2 + 5, 21, 99);
		frame++; // 2431
		rsi.child(frame, id2 + 6, 105, 46);
		frame++; // 2432

		rsi.child(frame, id2 + 7, img1x, img1y);
		frame++; // bottomright 2433
		rsi.child(frame, id2 + 8, img2x, img2y);
		frame++; // topleft 2434
		rsi.child(frame, id2 + 9, img3x, img3y);
		frame++; // bottomleft 2435
		rsi.child(frame, id2 + 10, img4x, img4y);
		frame++; // topright 2436

		rsi.child(frame, id2 + 11, str1x, str1y);
		frame++; // chop 2437
		rsi.child(frame, id2 + 12, str2x, str2y);
		frame++; // slash 2438
		rsi.child(frame, id2 + 13, str3x, str3y);
		frame++; // lunge 2439
		rsi.child(frame, id2 + 14, str4x, str4y);
		frame++; // block 2440

		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon 2426
		rsi.child(frame, id3, 21, 205);
		frame++; // special attack 7599

		for (int i = id2 + 3; i < id2 + 7; i++) { // 2429 - 2433
			rsi = interfaceCache[i];
			rsi.sprite1 = CustomSpriteLoader(19301, "");
			rsi.sprite2 = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0b(int id, int id2, String text1, String text2, String text3, String text4, int str1x, int str1y, int str2x, int str2y, int str3x, int str3y, int str4x, int str4y, int img1x, int img1y, int img2x, int img2y, int img3x, int img3y, int img4x, int img4y, TextDrawingArea[] tda) // 4button
																																																																													// nospec
	{
		RSInterface rsi = addInterface(id); // 2423
		/* addText(ID, "Text", tda, Size, Colour, Centered); */
		addText(id2, "-2", tda, 3, 0xff981f, true); // 2426
		addText(id2 + 11, text1, tda, 0, 0xff981f, false);
		addText(id2 + 12, text2, tda, 0, 0xff981f, false);
		addText(id2 + 13, text3, tda, 0, 0xff981f, false);
		addText(id2 + 14, text4, tda, 0, 0xff981f, false);

		rsi.width = 190;
		rsi.height = 261;

		int last = 14;
		int frame = 0;
		rsi.totalChildren(last, last, last);

		rsi.child(frame, id2 + 3, 21, 46);
		frame++; // 2429
		rsi.child(frame, id2 + 4, 104, 99);
		frame++; // 2430
		rsi.child(frame, id2 + 5, 21, 99);
		frame++; // 2431
		rsi.child(frame, id2 + 6, 105, 46);
		frame++; // 2432

		rsi.child(frame, id2 + 7, img1x, img1y);
		frame++; // bottomright 2433
		rsi.child(frame, id2 + 8, img2x, img2y);
		frame++; // topleft 2434
		rsi.child(frame, id2 + 9, img3x, img3y);
		frame++; // bottomleft 2435
		rsi.child(frame, id2 + 10, img4x, img4y);
		frame++; // topright 2436

		rsi.child(frame, id2 + 11, str1x, str1y);
		frame++; // chop 2437
		rsi.child(frame, id2 + 12, str2x, str2y);
		frame++; // slash 2438
		rsi.child(frame, id2 + 13, str3x, str3y);
		frame++; // lunge 2439
		rsi.child(frame, id2 + 14, str4x, str4y);
		frame++; // block 2440

		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon 2426

		for (int i = id2 + 3; i < id2 + 7; i++) { // 2429 - 2433
			rsi = interfaceCache[i];
			rsi.sprite1 = CustomSpriteLoader(19301, "");
			rsi.sprite2 = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0c(int id, int id2, int id3, String text1, String text2, String text3, int str1x, int str1y, int str2x, int str2y, int str3x, int str3y, int img1x, int img1y, int img2x, int img2y, int img3x, int img3y, TextDrawingArea[] tda) // 3button
																																																																// spec
	{
		RSInterface rsi = addInterface(id); // 2423
		/* addText(ID, "Text", tda, Size, Colour, Centered); */
		addText(id2, "-2", tda, 3, 0xff981f, true); // 2426
		addText(id2 + 9, text1, tda, 0, 0xff981f, false);
		addText(id2 + 10, text2, tda, 0, 0xff981f, false);
		addText(id2 + 11, text3, tda, 0, 0xff981f, false);
		/* specialBar(ID); */
		rsi.specialBar(id3); // 7599

		rsi.width = 190;
		rsi.height = 261;

		int last = 12;
		int frame = 0;
		rsi.totalChildren(last, last, last);

		rsi.child(frame, id2 + 3, 21, 99);
		frame++;
		rsi.child(frame, id2 + 4, 105, 46);
		frame++;
		rsi.child(frame, id2 + 5, 21, 46);
		frame++;

		rsi.child(frame, id2 + 6, img1x, img1y);
		frame++; // topleft
		rsi.child(frame, id2 + 7, img2x, img2y);
		frame++; // bottomleft
		rsi.child(frame, id2 + 8, img3x, img3y);
		frame++; // topright

		rsi.child(frame, id2 + 9, str1x, str1y);
		frame++; // chop
		rsi.child(frame, id2 + 10, str2x, str2y);
		frame++; // slash
		rsi.child(frame, id2 + 11, str3x, str3y);
		frame++; // lunge

		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon
		rsi.child(frame, id3, 21, 205);
		frame++; // special attack 7599
		
		for (int i = id2 + 3; i < id2 + 6; i++) {
			rsi = interfaceCache[i];
			rsi.sprite1 = CustomSpriteLoader(19301, "");
			rsi.sprite2 = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0d(int id, int id2, String text1, String text2, String text3, int str1x, int str1y, int str2x, int str2y, int str3x, int str3y, int img1x, int img1y, int img2x, int img2y, int img3x, int img3y, TextDrawingArea[] tda) // 3button
																																																														// nospec
																																																														// (magic
																																																														// intf)
	{
		RSInterface rsi = addInterface(id); // 2423
		/* addText(ID, "Text", tda, Size, Colour, Centered); */
		addText(id2, "-2", tda, 3, 0xff981f, true); // 2426
		addText(id2 + 9, text1, tda, 0, 0xff981f, false);
		addText(id2 + 10, text2, tda, 0, 0xff981f, false);
		addText(id2 + 11, text3, tda, 0, 0xff981f, false);

		// addText(353, "Spell", tda, 0, 0xff981f, false);
		removeSomething(353);
		addText(354, "Spell", tda, 0, 0xff981f, false);

		addCacheSprite(337, 19, 0, "combaticons");
		addCacheSprite(338, 13, 0, "combaticons2");
		addCacheSprite(339, 14, 0, "combaticons2");

		/* addToggleButton(id, sprite, config, width, height, tooltip); */
		// addToggleButton(349, 349, 108, 68, 44, "Select");
		removeSomething(349);
		addToggleButton(350, 350, 108, 68, 44, "Select");

		rsi.width = 190;
		rsi.height = 261;

		int last = 15;
		int frame = 0;
		rsi.totalChildren(last, last, last);

		rsi.child(frame, id2 + 3, 20, 115);
		frame++;
		rsi.child(frame, id2 + 4, 20, 80);
		frame++;
		rsi.child(frame, id2 + 5, 20, 45);
		frame++;

		rsi.child(frame, id2 + 6, img1x, img1y);
		frame++; // topleft
		rsi.child(frame, id2 + 7, img2x, img2y);
		frame++; // bottomleft
		rsi.child(frame, id2 + 8, img3x, img3y);
		frame++; // topright

		rsi.child(frame, id2 + 9, str1x, str1y);
		frame++; // bash
		rsi.child(frame, id2 + 10, str2x, str2y);
		frame++; // pound
		rsi.child(frame, id2 + 11, str3x, str3y);
		frame++; // focus

		rsi.child(frame, 349, 105, 46);
		frame++; // spell1
		rsi.child(frame, 350, 104, 106);
		frame++; // spell2

		rsi.child(frame, 353, 125, 74);
		frame++; // spell
		rsi.child(frame, 354, 125, 134);
		frame++; // spell

		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon
	}

	public static void emoteTab() {
		RSInterface tab = addTabInterface(147);
		RSInterface scroll = addTabInterface(148);
		tab.totalChildren(1);
		tab.child(0, 148, 0, 1);
		addButton(168, 0, "/Emotes/EMOTE", "Yes", 41, 47);
		addButton(169, 1, "/Emotes/EMOTE", "No", 41, 47);
		addButton(164, 2, "/Emotes/EMOTE", "Bow", 41, 47);
		addButton(165, 3, "/Emotes/EMOTE", "Angry", 41, 47);
		addButton(162, 4, "/Emotes/EMOTE", "Think", 41, 47);
		addButton(163, 5, "/Emotes/EMOTE", "Wave", 41, 47);
		addButton(13370, 6, "/Emotes/EMOTE", "Shrug", 41, 47);
		addButton(171, 7, "/Emotes/EMOTE", "Cheer", 41, 47);
		addButton(167, 8, "/Emotes/EMOTE", "Beckon", 41, 47);
		addButton(170, 9, "/Emotes/EMOTE", "Laugh", 41, 47);
		addButton(13366, 10, "/Emotes/EMOTE", "Jump for Joy", 41, 47);
		addButton(13368, 11, "/Emotes/EMOTE", "Yawn", 41, 47);
		addButton(166, 12, "/Emotes/EMOTE", "Dance", 41, 47);
		addButton(13363, 13, "/Emotes/EMOTE", "Jig", 41, 47);
		addButton(13364, 14, "/Emotes/EMOTE", "Spin", 41, 47);
		addButton(13365, 15, "/Emotes/EMOTE", "Headbang", 41, 47);
		addButton(161, 16, "/Emotes/EMOTE", "Cry", 41, 47);
		addButton(11100, 17, "/Emotes/EMOTE", "Blow kiss", 41, 47);
		addButton(13362, 18, "/Emotes/EMOTE", "Panic", 41, 47);
		addButton(13367, 19, "/Emotes/EMOTE", "Raspberry", 41, 47);
		addButton(172, 20, "/Emotes/EMOTE", "Clap", 41, 47);
		addButton(13369, 21, "/Emotes/EMOTE", "Salute", 41, 47);
		addButton(13383, 22, "/Emotes/EMOTE", "Goblin Bow", 41, 47);
		addButton(13384, 23, "/Emotes/EMOTE", "Goblin Salute", 41, 47);
		addButton(667, 24, "/Emotes/EMOTE", "Glass Box", 41, 47);
		addButton(6503, 25, "/Emotes/EMOTE", "Climb Rope", 41, 47);
		addButton(6506, 26, "/Emotes/EMOTE", "Lean On Air", 41, 47);
		addButton(666, 27, "/Emotes/EMOTE", "Glass Wall", 41, 47);
		addButton(18464, 28, "/Emotes/EMOTE", "Zombie Walk", 41, 47);
		addButton(18465, 29, "/Emotes/EMOTE", "Zombie Dance", 41, 47);
		addButton(15166, 30, "/Emotes/EMOTE", "Scared", 41, 47);
		addButton(18686, 31, "/Emotes/EMOTE", "Rabbit Hop", 41, 47);
		addConfigButton(154, 147, 32, 33, "EMOTE", 41, 47, "Skillcape Emote", 0, 1, 700);
		scroll.totalChildren(33);
		scroll.child(0, 168, 10, 7);
		scroll.child(1, 169, 54, 7);
		scroll.child(2, 164, 98, 14);
		scroll.child(3, 165, 137, 7);
		scroll.child(4, 162, 9, 56);
		scroll.child(5, 163, 48, 56);
		scroll.child(6, 13370, 95, 56);
		scroll.child(7, 171, 137, 56);
		scroll.child(8, 167, 7, 105);
		scroll.child(9, 170, 51, 105);
		scroll.child(10, 13366, 95, 104);
		scroll.child(11, 13368, 139, 105);
		scroll.child(12, 166, 6, 154);
		scroll.child(13, 13363, 50, 154);
		scroll.child(14, 13364, 90, 154);
		scroll.child(15, 13365, 135, 154);
		scroll.child(16, 161, 8, 204);
		scroll.child(17, 11100, 51, 203);
		scroll.child(18, 13362, 99, 204);
		scroll.child(19, 13367, 137, 203);
		scroll.child(20, 172, 10, 253);
		scroll.child(21, 13369, 53, 253);
		scroll.child(22, 13383, 88, 258);
		scroll.child(23, 13384, 138, 252);
		scroll.child(24, 667, 2, 303);
		scroll.child(25, 6503, 49, 302);
		scroll.child(26, 6506, 93, 302);
		scroll.child(27, 666, 137, 302);
		scroll.child(28, 18464, 9, 352);
		scroll.child(29, 18465, 50, 352);
		scroll.child(30, 15166, 94, 356);
		scroll.child(31, 18686, 141, 353);
		scroll.child(32, 154, 5, 401);
		scroll.width = 173;
		scroll.height = 258;
		scroll.scrollMax = 403;
	}

	public int transparency;

	@SuppressWarnings("unused")
	private static void addTransparentSprite(int id, int spriteId, String spriteName, int transparency) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.transparency = (byte) transparency;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
		tab.drawsTransparent = true;
	}

	public static void Pestpanel(TextDrawingArea[] tda) {
		RSInterface RSinterface = addInterface(21119);
		addText(21120, "What", 0x999999, false, true, 52, tda, 1);
		addText(21121, "What", 0x33cc00, false, true, 52, tda, 1);
		addText(21122, "(Need 5 to 25 players)", 0xFFcc33, false, true, 52, tda, 1);
		addText(21123, "Points", 0x33ccff, false, true, 52, tda, 1);
		int last = 4;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21120, 15, 12, 0, RSinterface);
		setBounds(21121, 15, 30, 1, RSinterface);
		setBounds(21122, 15, 48, 2, RSinterface);
		setBounds(21123, 15, 66, 3, RSinterface);
	}

	public static void Pestpanel2(TextDrawingArea[] tda) {
		RSInterface RSinterface = addInterface(21100);
		addSprite(21101, 0, "Pest Control/PEST1");
		addSprite(21102, 1, "Pest Control/PEST1");
		addSprite(21103, 2, "Pest Control/PEST1");
		addSprite(21104, 3, "Pest Control/PEST1");
		addSprite(21105, 4, "Pest Control/PEST1");
		addSprite(21106, 5, "Pest Control/PEST1");
		addText(21107, "", 0xCC00CC, false, true, 52, tda, 1);
		addText(21108, "", 0x0000FF, false, true, 52, tda, 1);
		addText(21109, "", 0xFFFF44, false, true, 52, tda, 1);
		addText(21110, "", 0xCC0000, false, true, 52, tda, 1);
		addText(21111, "250", 0x99FF33, false, true, 52, tda, 1);// w purp
		addText(21112, "250", 0x99FF33, false, true, 52, tda, 1);// e blue
		addText(21113, "250", 0x99FF33, false, true, 52, tda, 1);// se yel
		addText(21114, "250", 0x99FF33, false, true, 52, tda, 1);// sw red
		addText(21115, "200", 0x99FF33, false, true, 52, tda, 1);// attacks
		addText(21116, "0", 0x99FF33, false, true, 52, tda, 1);// knights hp
		addText(21117, "Time Remaining:", 0xFFFFFF, false, true, 52, tda, 0);
		addText(21118, "", 0xFFFFFF, false, true, 52, tda, 0);
		int last = 18;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21101, 361, 26, 0, RSinterface);
		setBounds(21102, 396, 26, 1, RSinterface);
		setBounds(21103, 436, 26, 2, RSinterface);
		setBounds(21104, 474, 26, 3, RSinterface);
		setBounds(21105, 3, 21, 4, RSinterface);
		setBounds(21106, 3, 50, 5, RSinterface);
		setBounds(21107, 371, 60, 6, RSinterface);
		setBounds(21108, 409, 60, 7, RSinterface);
		setBounds(21109, 443, 60, 8, RSinterface);
		setBounds(21110, 479, 60, 9, RSinterface);
		setBounds(21111, 362, 10, 10, RSinterface);
		setBounds(21112, 398, 10, 11, RSinterface);
		setBounds(21113, 436, 10, 12, RSinterface);
		setBounds(21114, 475, 10, 13, RSinterface);
		setBounds(21115, 32, 32, 14, RSinterface);
		setBounds(21116, 32, 62, 15, RSinterface);
		setBounds(21117, 8, 88, 16, RSinterface);
		setBounds(21118, 87, 88, 17, RSinterface);
	}



	public static void addHoverBox(int id, int ParentID, String text, String text2, int configId, int configFrame) {
		RSInterface rsi = addTabInterface(id);
		rsi.id = id;
		rsi.parentID = ParentID;
		rsi.type = 8;
		rsi.aString228 = text;
		rsi.message = text2;
		rsi.anIntArray245 = new int[1];
		rsi.anIntArray212 = new int[1];
		rsi.anIntArray245[0] = 1;
		rsi.anIntArray212[0] = configId;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = configFrame;
		rsi.valueIndexArray[0][2] = 0;
	}
	
	public static RSInterface addTab(int i) {
		RSInterface rsinterface = interfaceCache[i] = new RSInterface();
		rsinterface.id = i;
		rsinterface.parentID = i;
		rsinterface.type = 0;
		rsinterface.atActionType = 0;
		rsinterface.contentType = 0;
		rsinterface.width = 512;
		rsinterface.height = 334;
		rsinterface.aByte254 = 0;
		rsinterface.mOverInterToTrigger = 0;
		return rsinterface;
	}
	
	public static void addConfigButton(int ID, int pID, int bID, int bID2,
			String bName, int width, int height, String[] tooltips, int configID, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.parentID = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.atActionType = 8;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.aByte254 = 0;
		Tab.mOverInterToTrigger = -1;
		Tab.anIntArray245 = new int[1];
		Tab.anIntArray212 = new int[1];
		Tab.anIntArray245[0] = 1;
		Tab.anIntArray212[0] = configID;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.sprite1 = imageLoader(bID, bName);
		Tab.sprite2 = imageLoader(bID2, bName);
		Tab.tooltips = tooltips;
	}

	public static void addToggleButton(int ID, int pID, int bID, int bID2,
									   String bName, int width, int height, String tooltip, int configID, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.parentID = ID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.atActionType = 4;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.aByte254 = 0;
		Tab.mOverInterToTrigger = -1;
		Tab.anIntArray245 = new int[1];
		Tab.anIntArray212 = new int[1];
		Tab.anIntArray245[0] = 1;
		Tab.anIntArray212[0] = configID;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.sprite1 = imageLoader(bID, bName);
		Tab.sprite2 = imageLoader(bID2, bName);
		Tab.tooltip = tooltip;
	}
	
	public static void addText(int id, String text, TextDrawingArea tda[], int idx, int color, boolean center, boolean shadow) {
		RSInterface tab = addTabInterface(id);
		tab.parentID = id;
		tab.id = id;
		tab.type = 4;
		tab.atActionType = 0;
		tab.width = 0;
		tab.height = 11;
		tab.contentType = 0;
		tab.aByte254 = 0;
		tab.hoverType = -1;
		tab.centerText = center;
		tab.textShadow = shadow;
		tab.textDrawingAreas = tda[idx];
		tab.message = text;
		tab.aString228 = "";
		tab.textColor = color;
		tab.anInt219 = 0;
		tab.anInt216 = 0;
		tab.anInt239 = 0;	
	}
	
	public static void addText(int i, String s, int k, boolean l, boolean m, int a, TextDrawingArea[] TDA, int j) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.parentID = i;
		RSInterface.id = i;
		RSInterface.type = 4;
		RSInterface.atActionType = 0;
		RSInterface.width = 0;
		RSInterface.height = 0;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.mOverInterToTrigger = a;
		RSInterface.centerText = l;
		RSInterface.textShadow = m;
		RSInterface.textDrawingAreas = TDA[j];
		RSInterface.message = s;
		RSInterface.aString228 = "";
		RSInterface.textColor = k;
	}

	public static void addConfigButton(int ID, int pID, int bID, int bID2, String bName, int width, int height, String tT, int configID, int aT, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.parentID = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.atActionType = aT;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.aByte254 = 0;
		Tab.mOverInterToTrigger = -1;
		Tab.anIntArray245 = new int[1];
		Tab.anIntArray212 = new int[1];
		Tab.anIntArray245[0] = 1;
		Tab.anIntArray212[0] = configID;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.sprite1 = imageLoader(bID, bName);
		Tab.sprite2 = imageLoader(bID2, bName);
		Tab.tooltip = tT;
	}

	public static void addSprite(int id, int spriteId, String spriteName) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
	}
	
	public static void addButton(int interfaceId, int spriteId, String spriteLocation, int width, int height, String[] tooltips) {
		RSInterface RSInterface = addInterface(interfaceId);
		RSInterface.id = interfaceId;
		RSInterface.parentID = interfaceId;
		RSInterface.type = 5;
		RSInterface.atActionType = 8;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.hoverType = 52;
		RSInterface.sprite1 = imageLoader(spriteId, spriteLocation);
		RSInterface.sprite2 = imageLoader(spriteId, spriteLocation);
		RSInterface.width = width;
		RSInterface.height = height;
		RSInterface.tooltips = tooltips;
	}
	public static void addToItemGroup(int id, int w, int h, int x, int y,
			boolean actions, String action1, String action2, String action3) {
		RSInterface rsi = addInterface(id);
		rsi.width = w;
		rsi.height = h;
		rsi.inventoryItemId = new int[w * h];
		rsi.inventoryAmounts = new int[w * h];
		rsi.usableItems = false;
		rsi.hasActions = false;
		rsi.isMouseoverTriggered = false;
		rsi.spritePaddingX = x;
		rsi.spritePaddingY = y;
		rsi.spritesX = new int[20];
		rsi.spritesY = new int[20];
		rsi.sprites = new Sprite[20];
		rsi.actions = new String[5];
		if (actions) {
			rsi.actions[0] = action1;
			rsi.actions[1] = action2;
			rsi.actions[2] = action3;
		}
		rsi.type = 2;
	}

	public static void addHoverButton(int i, String imageName, int j,
			int width, int height, String text, int contentType, int hoverOver,
			int aT) {// hoverable button
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = aT;
		tab.contentType = contentType;
		tab.aByte254 = 0;
		tab.mOverInterToTrigger = hoverOver;
		tab.sprite1 = imageLoader(j, imageName);
		tab.sprite2 = imageLoader(j, imageName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
	}

	public static void addHoveredButton(int i, String imageName, int j, int w, int h, int IMAGEID) {// hoverable
																									// button
		RSInterface tab = addTabInterface(i);
		tab.parentID = i;
		tab.id = i;
		tab.type = 0;
		tab.atActionType = 0;
		tab.width = w;
		tab.height = h;
		tab.isMouseoverTriggered = true;
		tab.aByte254 = 0;
		tab.mOverInterToTrigger = -1;
		tab.scrollMax = 0;
		addHoverImage(IMAGEID, j, j, imageName);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
	}

	public static void addHoverImage(int i, int j, int k, String name) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.aByte254 = 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(j, name);
		tab.sprite2 = imageLoader(k, name);
	}

	public static void addTransparentSprite(int id, int spriteId, String spriteName) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
		tab.drawsTransparent = true;
	}

	public static RSInterface addScreenInterface(int id) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 0;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = 0;
		return tab;
	}

	public static RSInterface addTabInterface(int id) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;// 250
		tab.parentID = id;// 236
		tab.type = 0;// 262
		tab.atActionType = 0;// 217
		tab.contentType = 0;
		tab.width = 512;// 220
		tab.height = 700;// 267
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = -1;// Int 230
		return tab;
	}

	protected static Sprite imageLoader(int i, String s) {
		long l = (TextClass.method585(s) << 8) + i;
		Sprite sprite = (Sprite) aMRUNodes_238.insertFromCache(l);
		if (sprite != null)
			return sprite;
		try {
			sprite = new Sprite(s + " " + i);
			aMRUNodes_238.removeFromCache(sprite, l);
		} catch (Exception exception) {
			return null;
		}
		return sprite;
	}

	public void child(int id, int interID, int x, int y) {
		children[id] = interID;
		childX[id] = x;
		childY[id] = y;
	}

	public void totalChildren(int t) {
		children = new int[t];
		childX = new int[t];
		childY = new int[t];
	}

	private Model method206(int i, int j) {
		Model model = (Model) aMRUNodes_264.insertFromCache((i << 16) + j);
		if (model != null)
			return model;
		if (i == 1)
			model = Model.method462(j);
		if (i == 2)
			model = EntityDef.forID(j).method160();
		if (i == 3)
			model = Client.myPlayer.method453();
		if (i == 4)
			model = ItemDef.forID(j).method202(50);
		if (i == 5)
			model = null;
		if (model != null)
			aMRUNodes_264.removeFromCache(model, (i << 16) + j);
		return model;
	}

	private static Sprite method207(int i, StreamLoader streamLoader, String s) {
		long l = (TextClass.method585(s) << 8) + (long) i;
		Sprite sprite = (Sprite) aMRUNodes_238.insertFromCache(l);
		if (sprite != null)
			return sprite;
		try {
			sprite = new Sprite(streamLoader, s, i);
			aMRUNodes_238.removeFromCache(sprite, l);
		} catch (Exception _ex) {
			return null;
		}
		return sprite;
	}

	public static void method208(boolean flag, Model model) {
		int i = 0;// was parameter
		int j = 5;// was parameter
		if (flag)
			return;
		aMRUNodes_264.unlinkAll();
		if (model != null && j != 4)
			aMRUNodes_264.removeFromCache(model, (j << 16) + i);
	}
	
	public Model method209(int j, int k, boolean flag) {
		  Model model;
		  if (flag)
		   model = method206(anInt255, anInt256);
		  else
		   model = method206(anInt233, mediaID);
		  if (model == null)
		   return null;
		  if (k == -1 && j == -1 && model.currentModelColors == null)
		   return model;
		  Model model_1 = new Model(true, Class36.method532(k) & Class36.method532(j), false, model);
		  if (k != -1 || j != -1)
		   model_1.method469();
		  if (k != -1)
		   model_1.method470(k);
		  if (j != -1)
		   model_1.method470(j);
		  model_1.method479(64, 768, -50, -10, -50, true);
		  return model_1;
		 }

	/*public Model method209(int j, int k, boolean flag) {
		Model model;
		if (flag)
			model = method206(anInt255, anInt256);
		else
			model = method206(anInt233, mediaID);
		if (model == null)
			return null;
		if (k == -1 && j == -1 && model.anIntArray1640 == null)
			return model;
		Model model_1 = new Model(true, Class36.method532(k) & Class36.method532(j), false, model);
		if (k != -1 || j != -1)
			model_1.method469();
		if (k != -1)
			model_1.method470(k);
		if (j != -1)
			model_1.method470(j);
		model_1.method479(64, 768, -50, -10, -50, true);
		return model_1;
	}*/

	public RSInterface() {
	}

	public Sprite enabledSprite;
	public Sprite disabledSprite;
	public int amountActions;
	public String[] actionsInfo;
	public static StreamLoader aClass44;
	public boolean drawsTransparent;
	public Sprite sprite1;
	public int anInt208;
	public Sprite sprites[];
	public static RSInterface interfaceCache[];
	public int anIntArray212[];
	public int contentType;// anInt214
	public int spritesX[];
	public int anInt216;
	public int atActionType;
	public String spellName;
	public int anInt219;
	public int width;
	public String tooltip;
	public String selectedActionName;
	public boolean centerText;
	public int scrollPosition;
	public String actions[];
	public int valueCompareType[];
	public int requiredValues[];
	public int valueIndexArray[][];
	public boolean aBoolean227;
	public String aString228;
	public int mOverInterToTrigger;
	public int spritePaddingX;
	public int textColor;
	public int anInt233;
	public int mediaID;
	public boolean replaceItems;
	public int parentID;
	public int spellUsableOn;
	private static MRUNodes aMRUNodes_238;
	public int anInt239;
	public int children[];
	public int childX[];
	public boolean usableItems;
	public TextDrawingArea textDrawingAreas;
	public int spritePaddingY;
	public int anIntArray245[];
	public int anInt246;
	public int spritesY[];
	public String message;
	public boolean hasActions;
	public int id;
	public int inventoryAmounts[];
	public int inventoryItemId[];
	public byte aByte254;
	private int anInt255;
	private int anInt256;
	public int anInt257;
	public int anInt258;
	public boolean allowSwapItems;
	public Sprite sprite2;
	public int scrollMax;
	public int type;
	public int anInt263;
	private static final MRUNodes aMRUNodes_264 = new MRUNodes(30);
	public int anInt265;
	public boolean isMouseoverTriggered;
	public int height;
	public boolean textShadow;
	public int modelZoom;
	public int modelRotation1;
	public int modelRotation2;
	public int childY[];
	public boolean inventoryhover;
	public boolean isItemSearchComponent;
	public int itemSearchSelectedId, itemSearchSelectedSlot = -1;
	public static int selectedItemInterfaceId = -1;
	public int priority;
	public String hoverText;
	public int opacity;
	public int hoverType;
	public boolean inventoryHover;

	public static void addLunarSprite(int i, int j, String name) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = 0;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.mOverInterToTrigger = 52;
		RSInterface.sprite1 = imageLoader(j, name);
		RSInterface.width = 500;
		RSInterface.height = 500;
		RSInterface.tooltip = "";
	}

	public static void drawRune(int i, int id, String runeName) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.type = 5;
		RSInterface.atActionType = 0;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.mOverInterToTrigger = 52;
		RSInterface.sprite1 = imageLoader(id, "Lunar/RUNE");
		RSInterface.width = 500;
		RSInterface.height = 500;
	}

	public static void addRuneText(int ID, int runeAmount, int RuneID, TextDrawingArea[] font) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 4;
		rsInterface.atActionType = 0;
		rsInterface.contentType = 0;
		rsInterface.width = 0;
		rsInterface.height = 14;
		rsInterface.aByte254 = 0;
		rsInterface.mOverInterToTrigger = -1;
		rsInterface.anIntArray245 = new int[1];
		rsInterface.anIntArray212 = new int[1];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = runeAmount;
		rsInterface.valueIndexArray = new int[1][4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = RuneID;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.centerText = true;
		rsInterface.textDrawingAreas = font[0];
		rsInterface.textShadow = true;
		rsInterface.message = "%1/" + runeAmount + "";
		rsInterface.aString228 = "";
		rsInterface.textColor = 12582912;
		rsInterface.anInt219 = 4095;
	}

	public static void homeTeleport() {
		RSInterface RSInterface = addInterface(30000);
		RSInterface.tooltip = "Cast @gre@Lunar Home Teleport";
		RSInterface.id = 30000;
		RSInterface.parentID = 30000;
		RSInterface.type = 5;
		RSInterface.atActionType = 5;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.mOverInterToTrigger = 30001;
		RSInterface.sprite1 = imageLoader(1, "Lunar/SPRITE");
		RSInterface.width = 20;
		RSInterface.height = 20;
		RSInterface Int = addInterface(30001);
		Int.isMouseoverTriggered = true;
		Int.mOverInterToTrigger = -1;
		setChildren(1, Int);
		addLunarSprite(30002, 0, "SPRITE");
		setBounds(30002, 0, 0, 0, Int);
	}

	public static void addLunar2RunesSmallBox(int ID, int r1, int r2, int ra1, int ra2, int rune1, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast On";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[3];
		rsInterface.anIntArray212 = new int[3];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = lvl;
		rsInterface.valueIndexArray = new int[3][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[3];
		rsInterface.valueIndexArray[2][0] = 1;
		rsInterface.valueIndexArray[2][1] = 6;
		rsInterface.valueIndexArray[2][2] = 0;
		rsInterface.sprite2 = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.sprite1 = imageLoader(sid, "Lunar/LUNAROFF");
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(7, INT);
		addLunarSprite(ID + 2, 0, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(30016, 37, 35, 3, INT);// Rune
		setBounds(rune1, 112, 35, 4, INT);// Rune
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 50, 66, 5, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 123, 66, 6, INT);
	}

	public static void addLunar3RunesSmallBox(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1, int rune2, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.sprite1 = imageLoader(sid, "Lunar/LUNAROFF");
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 0, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(30016, 14, 35, 3, INT);
		setBounds(rune1, 74, 35, 4, INT);
		setBounds(rune2, 130, 35, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 66, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 66, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 66, 8, INT);
	}

	public static void addLunar3RunesBigBox(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1, int rune2, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.sprite1 = imageLoader(sid, "Lunar/LUNAROFF");
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 1, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 21, 2, INT);
		setBounds(30016, 14, 48, 3, INT);
		setBounds(rune1, 74, 48, 4, INT);
		setBounds(rune2, 130, 48, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 79, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 79, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 79, 8, INT);
	}

	public static void addLunar3RunesLargeBox(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1, int rune2, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.sprite1 = imageLoader(sid, "Lunar/LUNAROFF");
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 2, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 34, 2, INT);
		setBounds(30016, 14, 61, 3, INT);
		setBounds(rune1, 74, 61, 4, INT);
		setBounds(rune2, 130, 61, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 92, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 92, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 92, 8, INT);
	}
	
	public static void addBox(int id, int byte1, boolean filled, int color,
			String text) {
		RSInterface Interface = addInterface(id);
		Interface.id = id;
		Interface.parentID = id;
		Interface.type = 9;
		Interface.aByte254 = (byte) byte1;
		Interface.aBoolean227 = filled;
		Interface.mOverInterToTrigger = -1;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.textColor = color;
		Interface.message = text;
	}

	public static void setChildren(int total, RSInterface i) {
		i.children = new int[total];
		i.childX = new int[total];
		i.childY = new int[total];
	}
	
	 /* Skill Interface Start*/
	public static void addButton(int i, int j, int hoverId, String name, int W, int H, String S, int AT) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = AT;
		RSInterface.aByte254 = 0;
		RSInterface.mOverInterToTrigger = hoverId;
		RSInterface.sprite1 = imageLoader(j,name);
		RSInterface.sprite2 = imageLoader(j,name);
		RSInterface.width = W;
		RSInterface.height = H;
		RSInterface.tooltip = S;
	}
	
	//public int hoverType;	
	public int interfaceType;
	//public byte opacity; 

	public static int boxIds[] = {
		4041, 4077, 4113, 4047, 4083, 4119, 4053, 4089, 4125, 4059, 4095,
		4131, 4065, 4101, 4137, 4071, 4107, 4143, 4154, 12168, 13918
	};

	public static void skillInterface(int i, int j){
		RSInterface rsinterface = interfaceCache[i] = new RSInterface();
		rsinterface.id = i;
		rsinterface.parentID = i;
		rsinterface.type = 5;
		rsinterface.atActionType = 0;
		rsinterface.contentType = 0;
		rsinterface.width = 26;
		rsinterface.height = 34;
		rsinterface.aByte254 = 0;
		rsinterface.mOverInterToTrigger = 0;
		rsinterface.sprite1 = imageLoader(j, "Interfaces/Skill/Skill");
		rsinterface.sprite2 = imageLoader(j, "Interfaces/Skill/Skill");
	}
public static void addText(int id, String text, TextDrawingArea wid[], int idx, int color) {
		RSInterface rsinterface = addTab(id);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 4;
		rsinterface.atActionType = 0;
		rsinterface.width = 174;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.aByte254 = 0;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.centerText = false;
		rsinterface.textShadow = true;
		rsinterface.textDrawingAreas = wid[idx];
		rsinterface.message = text;
		rsinterface.aString228 = "";
		rsinterface.textColor = color;
		rsinterface.anInt219 = 0;
		rsinterface.anInt216 = 0;
		rsinterface.anInt239 = 0;	
	}
	
	public static void skillInterface(TextDrawingArea[] wid) {
	RSInterface Interface = addTab(3917);
	int index = 0;
		skillInterface(19746, 255);
		skillInterface(19749, 52);
		addText(29801, "", wid, 0, 0xFFEE33); //Summoning
		addText(29800, "", wid, 0, 0xFFEE33); //Hunter
		
		addButton(19747, 51, 27700, "Interfaces/Skill/Skill", 62, 32, "View @lre@Hunter @whi@Guide", 1);
		addButton(19748, 50, 27701, "Interfaces/Skill/Skill", 62, 32, "View @lre@Summoning @whi@Guide", 1);
		
		addText(13984, "Totalll", wid, 0, 0xFFEE33);
		addText(3985, "", wid, 0, 0xFFEE33);
		addText(13983, "", wid, 0, 0xFFEE33, true, true);
		for(int k = 0; k < boxIds.length; k++) {
			skillInterface(boxIds[k], 256);
		}
		RSInterface rsinterface = addTab(3917);
		rsinterface.children = new int[63];		rsinterface.childX = new int[63];	rsinterface.childY = new int[63];
		rsinterface.children[0] = 3918;			rsinterface.childX[0] = 0;			rsinterface.childY[0] = 0;
		rsinterface.children[1] = 3925;			rsinterface.childX[1] = 0;			rsinterface.childY[1] = 31;
		rsinterface.children[2] = 3932;			rsinterface.childX[2] = 0;			rsinterface.childY[2] = 62;
		rsinterface.children[3] = 3939;			rsinterface.childX[3] = 0;			rsinterface.childY[3] = 93;
		rsinterface.children[4] = 3946;			rsinterface.childX[4] = 0;			rsinterface.childY[4] = 124;
		rsinterface.children[5] = 3953;			rsinterface.childX[5] = 0;			rsinterface.childY[5] = 155;
		rsinterface.children[6] = 4148;			rsinterface.childX[6] = 0;			rsinterface.childY[6] = 186;
		rsinterface.children[7] = 19746;		rsinterface.childX[7] = 70;			rsinterface.childY[7] = 69;
		rsinterface.children[8] = 19748;		rsinterface.childX[8] = 1;			rsinterface.childY[8] = 219;
		rsinterface.children[9] = 19747;		rsinterface.childX[9] = 64;			rsinterface.childY[9] = 219;
		rsinterface.children[10] = 14000;		rsinterface.childX[10] = 10;		rsinterface.childY[10] = 219;
		rsinterface.children[11] = 19749;		rsinterface.childX[11] = 128;		rsinterface.childY[11] = 220;
		rsinterface.children[12] = 13983; 		rsinterface.childX[12] = 158;  		rsinterface.childY[12] = 238;
		rsinterface.children[13] = 3984;		rsinterface.childX[13] = 300;		rsinterface.childY[13] = 225;
		rsinterface.children[14] = 3985;		rsinterface.childX[14] = 130;		rsinterface.childY[14] = 238;
		rsinterface.children[15] = 29800;		rsinterface.childX[15] = 98;  		rsinterface.childY[15] = 220;
		rsinterface.children[16] = 29800;		rsinterface.childX[16] = 107;  		rsinterface.childY[16] = 235;
		rsinterface.children[17] = 29801;		rsinterface.childX[17] = 36;		rsinterface.childY[17] = 220;
		rsinterface.children[18] = 29801;		rsinterface.childX[18] = 45;		rsinterface.childY[18] = 235;
		rsinterface.children[19] = 4040;		rsinterface.childX[19] = 5;			rsinterface.childY[19] = 20;
		rsinterface.children[20] = 8654;		rsinterface.childX[20] = 0;			rsinterface.childY[20] = 2;
		rsinterface.children[21] = 8655;		rsinterface.childX[21] = 64;		rsinterface.childY[21] = 2;
		rsinterface.children[22] = 4076;		rsinterface.childX[22] = 20;		rsinterface.childY[22] = 20;
		rsinterface.children[23] = 8656;		rsinterface.childX[23] = 128;		rsinterface.childY[23] = 2;
		rsinterface.children[24] = 4112;		rsinterface.childX[24] = 20;		rsinterface.childY[24] = 20;
		rsinterface.children[25] = 8657;		rsinterface.childX[25] = 0;			rsinterface.childY[25] = 33;
		rsinterface.children[26] = 4046;		rsinterface.childX[26] = 20;		rsinterface.childY[26] = 50;
		rsinterface.children[27] = 8658;		rsinterface.childX[27] = 64;		rsinterface.childY[27] = 33;
		rsinterface.children[28] = 4082;		rsinterface.childX[28] = 20;		rsinterface.childY[28] = 50;
		rsinterface.children[29] = 8659;		rsinterface.childX[29] = 128;		rsinterface.childY[29] = 33;
		rsinterface.children[30] = 4118;		rsinterface.childX[30] = 20;		rsinterface.childY[30] = 50;
		rsinterface.children[31] = 8660;		rsinterface.childX[31] = 0;			rsinterface.childY[31] = 60+10;
		rsinterface.children[32] = 4052;		rsinterface.childX[32] = 20;		rsinterface.childY[32] = 83;
		rsinterface.children[33] = 8661;		rsinterface.childX[33] = 65;		rsinterface.childY[33] = 60+10;
		rsinterface.children[34] = 4088;		rsinterface.childX[34] = 20;		rsinterface.childY[34] = 83;
		rsinterface.children[35] = 8662;		rsinterface.childX[35] = 130;		rsinterface.childY[35] = 60+10;
		rsinterface.children[36] = 4124;		rsinterface.childX[36] = 20;		rsinterface.childY[36] = 83;
		rsinterface.children[37] = 8663;		rsinterface.childX[37] = 0;			rsinterface.childY[37] = 90+10;
		rsinterface.children[38] = 4058;		rsinterface.childX[38] = 20;		rsinterface.childY[38] = 120;
		rsinterface.children[39] = 8664;		rsinterface.childX[39] = 65;		rsinterface.childY[39] = 90+10;
		rsinterface.children[40] = 4094;		rsinterface.childX[40] = 20;		rsinterface.childY[40] = 120;
		rsinterface.children[41] = 8665;		rsinterface.childX[41] = 130;		rsinterface.childY[41] = 90+10;
		rsinterface.children[42] = 4130;		rsinterface.childX[42] = 20;		rsinterface.childY[42] = 120;
		rsinterface.children[43] = 8666;		rsinterface.childX[43] = 0;			rsinterface.childY[43] = 130;
		rsinterface.children[44] = 4064;		rsinterface.childX[44] = 20;		rsinterface.childY[44] = 150;
		rsinterface.children[45] = 8667;		rsinterface.childX[45] = 65;		rsinterface.childY[45] = 130;
		rsinterface.children[46] = 4100;		rsinterface.childX[46] = 20;		rsinterface.childY[46] = 150;
		rsinterface.children[47] = 8668;		rsinterface.childX[47] = 130;		rsinterface.childY[47] = 130;
		rsinterface.children[48] = 4136;		rsinterface.childX[48] = 20;		rsinterface.childY[48] = 150;
		rsinterface.children[49] = 8669;		rsinterface.childX[49] = 0;			rsinterface.childY[49] = 160;
		rsinterface.children[50] = 4070;		rsinterface.childX[50] = 20;		rsinterface.childY[50] = 180;
		rsinterface.children[51] = 8670;		rsinterface.childX[51] = 65;		rsinterface.childY[51] = 160;
		rsinterface.children[52] = 4106;		rsinterface.childX[52] = 20;		rsinterface.childY[52] = 180;
		rsinterface.children[53] = 8671;		rsinterface.childX[53] = 130;		rsinterface.childY[53] = 160;
		rsinterface.children[54] = 4142;		rsinterface.childX[54] = 20;		rsinterface.childY[54] = 180;
		rsinterface.children[55] = 8672;		rsinterface.childX[55] = 0;			rsinterface.childY[55] = 190;
		rsinterface.children[56] = 4160;		rsinterface.childX[56] = 20;		rsinterface.childY[56] = 150;
		rsinterface.children[57] = 4160;		rsinterface.childX[57] = 20;		rsinterface.childY[57] = 150;
		rsinterface.children[58] = 12162;		rsinterface.childX[58] = 65;		rsinterface.childY[58] = 190;
		rsinterface.children[59] = 2832;		rsinterface.childX[59] = 20;		rsinterface.childY[59] = 150;
		rsinterface.children[60] = 13928;		rsinterface.childX[60] = 130;		rsinterface.childY[60] = 190;
		rsinterface.children[61] = 13917;		rsinterface.childX[61] = 20;		rsinterface.childY[61] = 150;
		rsinterface.children[62] = 13984;		rsinterface.childX[62] = 145;		rsinterface.childY[62] = 225;
	}

	public static void configureLunar(TextDrawingArea[] TDA) {
		homeTeleport();
		constructLunar();
		drawRune(30003, 1, "Fire");
		drawRune(30004, 2, "Water");
		drawRune(30005, 3, "Air");
		drawRune(30006, 4, "Earth");
		drawRune(30007, 5, "Mind");
		drawRune(30008, 6, "Body");
		drawRune(30009, 7, "Death");
		drawRune(30010, 8, "Nature");
		drawRune(30011, 9, "Chaos");
		drawRune(30012, 10, "Law");
		drawRune(30013, 11, "Cosmic");
		drawRune(30014, 12, "Blood");
		drawRune(30015, 13, "Soul");
		drawRune(30016, 14, "Astral");
		addLunar3RunesSmallBox(30017, 9075, 554, 555, 0, 4, 3, 30003, 30004, 64, "Bake Pie", "Bake pies without a stove", TDA, 0, 16, 2);
		addLunar2RunesSmallBox(30025, 9075, 557, 0, 7, 30006, 65, "Cure Plant", "Cure disease on farming patch", TDA, 1, 4, 2);
		addLunar3RunesBigBox(30032, 9075, 564, 558, 0, 0, 0, 30013, 30007, 65, "Monster Examine", "Detect the combat statistics of a\\nmonster", TDA, 2, 2, 2);
		addLunar3RunesSmallBox(30040, 9075, 564, 556, 0, 0, 1, 30013, 30005, 66, "NPC Contact", "Speak with varied NPCs", TDA, 3, 0, 2);
		addLunar3RunesSmallBox(30048, 9075, 563, 557, 0, 0, 9, 30012, 30006, 67, "Cure Other", "Cure poisoned players", TDA, 4, 8, 2);
		addLunar3RunesSmallBox(30056, 9075, 555, 554, 0, 2, 0, 30004, 30003, 67, "Humidify", "Fills certain vessels with water", TDA, 5, 0, 5);
		addLunar3RunesSmallBox(30064, 9075, 563, 557, 1, 0, 1, 30012, 30006, 68, "@gre@PVP Teleport", "List of PVP Teleports", TDA, 6, 0, 5);
		addLunar3RunesBigBox(30075, 9075, 563, 557, 1, 0, 3, 30012, 30006, 69, "@gre@Training Teleport", "List of Training Teleports", TDA, 7, 0, 5);
		addLunar3RunesSmallBox(30083, 9075, 563, 557, 1, 0, 5, 30012, 30006, 70, "@gre@Boss Teleport", "List of Boss Teleports", TDA, 8, 0, 5);
		addLunar3RunesSmallBox(30091, 9075, 564, 563, 1, 1, 0, 30013, 30012, 70, "Cure Me", "Cures Poison", TDA, 9, 0, 5);
		addLunar2RunesSmallBox(30099, 9075, 557, 1, 1, 30006, 70, "Hunter Kit", "Get a kit of hunting gear", TDA, 10, 0, 5);
		addLunar3RunesSmallBox(30106, 9075, 563, 555, 1, 0, 0, 30012, 30004, 71, "@gre@Minigame Teleport", "List of Minigame Teleports", TDA, 11, 0, 5);
		addLunar3RunesBigBox(30114, 9075, 563, 555, 1, 0, 4, 30012, 30004, 72, "Tele Group Waterbirth", "Teleports players to Waterbirth\\nisland", TDA, 12, 0, 5);
		addLunar3RunesSmallBox(30122, 9075, 564, 563, 1, 1, 1, 30013, 30012, 73, "Cure Group", "Cures Poison on players", TDA, 13, 0, 5);
		addLunar3RunesBigBox(30130, 9075, 564, 559, 1, 1, 4, 30013, 30008, 74, "Stat Spy", "Cast on another player to see their\\nskill levels", TDA, 14, 8, 2);
		addLunar3RunesBigBox(30138, 9075, 563, 554, 1, 1, 2, 30012, 30003, 74, "@gre@Skilling Teleport", "Teleports you to Skilling Area", TDA, 15, 0, 5);
		addLunar3RunesBigBox(30146, 9075, 563, 554, 1, 1, 5, 30012, 30003, 75, "Tele Group Barbarian", "Teleports players to the Barbarian\\noutpost", TDA, 16, 0, 5);
		addLunar3RunesSmallBox(30154, 9075, 554, 556, 1, 5, 9, 30003, 30005, 76, "Superglass Make", "Make glass without a furnace", TDA, 17, 16, 2);
		addLunar3RunesSmallBox(30162, 9075, 563, 555, 1, 1, 3, 30012, 30004, 77, "Khazard Teleport", "Teleports you to Port khazard", TDA, 18, 0, 5);
		addLunar3RunesSmallBox(30170, 9075, 563, 555, 1, 1, 7, 30012, 30004, 78, "Tele Group Khazard", "Teleports players to Port khazard", TDA, 19, 0, 5);
		addLunar3RunesBigBox(30178, 9075, 564, 559, 1, 0, 4, 30013, 30008, 78, "Dream", "Take a rest and restore hitpoints 3\\n times faster", TDA, 20, 0, 5);
		addLunar3RunesSmallBox(30186, 9075, 557, 555, 1, 9, 4, 30006, 30004, 79, "String Jewellery", "String amulets without wool", TDA, 21, 0, 5);
		addLunar3RunesLargeBox(30194, 9075, 557, 555, 1, 9, 9, 30006, 30004, 80, "Stat Restore Pot\\nShare", "Share a potion with up to 4 nearby\\nplayers", TDA, 22, 0, 5);
		addLunar3RunesSmallBox(30202, 9075, 554, 555, 1, 6, 6, 30003, 30004, 81, "Magic Imbue", "Combine runes without a talisman", TDA, 23, 0, 5);
		addLunar3RunesBigBox(30210, 9075, 561, 557, 2, 1, 14, 30010, 30006, 82, "Fertile Soil", "Fertilise a farming patch with super\\ncompost", TDA, 24, 4, 2);
		addLunar3RunesBigBox(30218, 9075, 557, 555, 2, 11, 9, 30006, 30004, 83, "Boost Potion Share", "Shares a potion with up to 4 nearby\\nplayers", TDA, 25, 0, 5);
		addLunar3RunesSmallBox(30226, 9075, 563, 555, 2, 2, 9, 30012, 30004, 84, "Fishing Guild Teleport", "Teleports you to the fishing guild", TDA, 26, 0, 5);
		addLunar3RunesLargeBox(30234, 9075, 563, 555, 1, 2, 13, 30012, 30004, 85, "Tele Group Fishing Guild", "Teleports players to the Fishing\\nGuild", TDA, 27, 0, 5);
		addLunar3RunesSmallBox(30242, 9075, 557, 561, 2, 14, 0, 30006, 30010, 85, "Plank Make", "Turn Logs into planks", TDA, 28, 16, 5);
		addLunar3RunesSmallBox(30250, 9075, 563, 555, 2, 2, 9, 30012, 30004, 86, "Catherby Teleport", "Teleports you to Catherby", TDA, 29, 0, 5);
		addLunar3RunesSmallBox(30258, 9075, 563, 555, 2, 2, 14, 30012, 30004, 87, "Tele Group Catherby", "Teleports players to Catherby", TDA, 30, 0, 5);
		addLunar3RunesSmallBox(30266, 9075, 563, 555, 2, 2, 7, 30012, 30004, 88, "Ice Plateau Teleport", "Teleports you to Ice Plateau", TDA, 31, 0, 5);
		addLunar3RunesLargeBox(30274, 9075, 563, 555, 2, 2, 15, 30012, 30004, 89, "Tele Group Ice Plateau", "Teleports players to Ice Plateau", TDA, 32, 0, 5);
		addLunar3RunesBigBox(30282, 9075, 563, 561, 2, 1, 0, 30012, 30010, 90, "Energy Transfer", "Spend HP and SA energy to\\n give another SA and run energy", TDA, 33, 8, 2);
		addLunar3RunesBigBox(30290, 9075, 563, 565, 2, 2, 0, 30012, 30014, 91, "Heal Other", "Transfer up to 75% of hitpoints\\n to another player", TDA, 34, 8, 2);
		addLunar3RunesBigBox(30298, 9075, 560, 557, 2, 1, 9, 30009, 30006, 92, "Vengeance Other", "Allows another player to rebound\\ndamage to an opponent", TDA, 35, 8, 2);
		addLunar3RunesSmallBox(30306, 9075, 560, 557, 3, 1, 9, 30009, 30006, 93, "Vengeance", "Rebound damage to an opponent", TDA, 36, 0, 5);
		addLunar3RunesBigBox(30314, 9075, 565, 563, 3, 2, 5, 30014, 30012, 94, "Heal Group", "Transfer up to 75% of hitpoints\\n to a group", TDA, 37, 0, 5);
		addLunar3RunesBigBox(30322, 9075, 564, 563, 2, 1, 0, 30013, 30012, 95, "Spellbook Swap", "Change to another spellbook for 1\\nspell cast", TDA, 38, 0, 5);
	}

	public static void constructLunar() {
		RSInterface Interface = addTabInterface(29999);
		setChildren(80, Interface);
		setBounds(30000, 11, 10, 0, Interface);
		setBounds(30017, 40, 9, 1, Interface);
		setBounds(30025, 71, 12, 2, Interface);
		setBounds(30032, 103, 10, 3, Interface);
		setBounds(30040, 135, 12, 4, Interface);
		setBounds(30048, 165, 10, 5, Interface);
		setBounds(30056, 8, 38, 6, Interface);
		setBounds(30064, 39, 39, 7, Interface);
		setBounds(30075, 71, 39, 8, Interface);
		setBounds(30083, 103, 39, 9, Interface);
		setBounds(30091, 135, 39, 10, Interface);
		setBounds(30099, 165, 37, 11, Interface);
		setBounds(30106, 12, 68, 12, Interface);
		setBounds(30114, 42, 68, 13, Interface);
		setBounds(30122, 71, 68, 14, Interface);
		setBounds(30130, 103, 68, 15, Interface);
		setBounds(30138, 135, 68, 16, Interface);
		setBounds(30146, 165, 68, 17, Interface);
		setBounds(30154, 14, 97, 18, Interface);
		setBounds(30162, 42, 97, 19, Interface);
		setBounds(30170, 71, 97, 20, Interface);
		setBounds(30178, 101, 97, 21, Interface);
		setBounds(30186, 135, 98, 22, Interface);
		setBounds(30194, 168, 98, 23, Interface);
		setBounds(30202, 11, 125, 24, Interface);
		setBounds(30210, 42, 124, 25, Interface);
		setBounds(30218, 74, 125, 26, Interface);
		setBounds(30226, 103, 125, 27, Interface);
		setBounds(30234, 135, 125, 28, Interface);
		setBounds(30242, 164, 126, 29, Interface);
		setBounds(30250, 10, 155, 30, Interface);
		setBounds(30258, 42, 155, 31, Interface);
		setBounds(30266, 71, 155, 32, Interface);
		setBounds(30274, 103, 155, 33, Interface);
		setBounds(30282, 136, 155, 34, Interface);
		setBounds(30290, 165, 155, 35, Interface);
		setBounds(30298, 13, 185, 36, Interface);
		setBounds(30306, 42, 185, 37, Interface);
		setBounds(30314, 71, 184, 38, Interface);
		setBounds(30322, 104, 184, 39, Interface);
		setBounds(30001, 6, 184, 40, Interface);// hover
		setBounds(30018, 5, 176, 41, Interface);// hover
		setBounds(30026, 5, 176, 42, Interface);// hover
		setBounds(30033, 5, 163, 43, Interface);// hover
		setBounds(30041, 5, 176, 44, Interface);// hover
		setBounds(30049, 5, 176, 45, Interface);// hover
		setBounds(30057, 5, 176, 46, Interface);// hover
		setBounds(30065, 5, 176, 47, Interface);// hover
		setBounds(30076, 5, 163, 48, Interface);// hover
		setBounds(30084, 5, 176, 49, Interface);// hover
		setBounds(30092, 5, 176, 50, Interface);// hover
		setBounds(30100, 5, 176, 51, Interface);// hover
		setBounds(30107, 5, 176, 52, Interface);// hover
		setBounds(30115, 5, 163, 53, Interface);// hover
		setBounds(30123, 5, 176, 54, Interface);// hover
		setBounds(30131, 5, 163, 55, Interface);// hover
		setBounds(30139, 5, 163, 56, Interface);// hover
		setBounds(30147, 5, 163, 57, Interface);// hover
		setBounds(30155, 5, 176, 58, Interface);// hover
		setBounds(30163, 5, 176, 59, Interface);// hover
		setBounds(30171, 5, 176, 60, Interface);// hover
		setBounds(30179, 5, 163, 61, Interface);// hover
		setBounds(30187, 5, 176, 62, Interface);// hover
		setBounds(30195, 5, 149, 63, Interface);// hover
		setBounds(30203, 5, 176, 64, Interface);// hover
		setBounds(30211, 5, 163, 65, Interface);// hover
		setBounds(30219, 5, 163, 66, Interface);// hover
		setBounds(30227, 5, 176, 67, Interface);// hover
		setBounds(30235, 5, 149, 68, Interface);// hover
		setBounds(30243, 5, 176, 69, Interface);// hover
		setBounds(30251, 5, 5, 70, Interface);// hover
		setBounds(30259, 5, 5, 71, Interface);// hover
		setBounds(30267, 5, 5, 72, Interface);// hover
		setBounds(30275, 5, 5, 73, Interface);// hover
		setBounds(30283, 5, 5, 74, Interface);// hover
		setBounds(30291, 5, 5, 75, Interface);// hover
		setBounds(30299, 5, 5, 76, Interface);// hover
		setBounds(30307, 5, 5, 77, Interface);// hover
		setBounds(30323, 5, 5, 78, Interface);// hover
		setBounds(30315, 5, 5, 79, Interface);// hover
	}

	public static void setBounds(int ID, int X, int Y, int frame, RSInterface RSinterface) {
		RSinterface.children[frame] = ID;
		RSinterface.childX[frame] = X;
		RSinterface.childY[frame] = Y;
	}
	
	public static void addHDSprite(int id, int spriteId, String spriteName) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.id = id;
		rsi.parentID = id;
		rsi.type = 12;
		rsi.atActionType = 0;
		rsi.contentType = 0;
		rsi.aByte254 = (byte) 0;
		rsi.hoverType = 52;
		rsi.sprite1 = imageLoader(spriteId, spriteName);
		rsi.sprite2 = imageLoader(spriteId, spriteName);
	}
	
/*	public static void AddInterfaceButton(int id, int sid, String spriteName,
			String tooltip, int mOver, int atAction, int width, int height) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = atAction;
		tab.contentType = 0;
		tab.aByte254 = 0;
		tab.hoverType = mOver;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = tooltip;
		tab.inventoryHover = true;
	}*/

	public static void addButton(int i, int j, String name, int W, int H, String S, int AT) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = AT;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.mOverInterToTrigger = 52;
		RSInterface.sprite1 = imageLoader(j, name);
		RSInterface.sprite2 = imageLoader(j, name);
		RSInterface.width = W;
		RSInterface.height = H;
		RSInterface.tooltip = S;
	}
	
	/*public static void addText(int id, String text, TextDrawingArea wid[], int idx, int color) {
		RSInterface rsinterface = addTab(id);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 4;
		rsinterface.atActionType = 0;
		rsinterface.width = 174;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.aByte254 = 0;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.centerText = false;
		rsinterface.textShadow = true;
		rsinterface.textDrawingAreas = wid[idx];
		rsinterface.message = text;
		rsinterface.aString228 = "";
		rsinterface.textColor = color;
		rsinterface.anInt219 = 0;
		rsinterface.anInt216 = 0;
		rsinterface.anInt239 = 0;
	}*/
	
	
	public static void addButton(int id, int sid, String spriteName,
			String tooltip, int mOver, int atAction, int width, int height) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = atAction;
		tab.contentType = 0;
		tab.aByte254 = 0;
		tab.mOverInterToTrigger = mOver;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = tooltip;
		tab.inventoryhover = true;
	}
	
	public static void addSprite(int i, int j, int k) {
		RSInterface rsinterface = interfaceCache[i] = new RSInterface();
		rsinterface.id = i;
		rsinterface.parentID = i;
		rsinterface.type = 5;
		rsinterface.atActionType = 1;
		rsinterface.contentType = 0;
		rsinterface.width = 20;
		rsinterface.height = 20;
		rsinterface.aByte254 = 0;
		rsinterface.mOverInterToTrigger = 52;
		rsinterface.sprite1 = imageLoader(j,
				"Interfaces/Equipment/SPRITE");
		rsinterface.sprite2 = imageLoader(k,
				"Interfaces/Equipment/SPRITE");
	}
	
	public static void darken(int identity, int width, int height, int color, byte transparency) {
		RSInterface component = addInterface(identity);
		component.id = identity;
		component.type = 17;
		component.width = width;
		component.height = height;
		component.fillColor = color;
		component.aByte254 = transparency;
	}
	
	public static void drawRoundedRectangle(int identity, int width, int height, int color, byte transparency, boolean filled, boolean shadowed) {
		RSInterface component = addInterface(identity);
		component.id = identity;
		component.type = 18;
		component.width = width;
		component.height = height;
		component.fillColor = color;
		component.aByte254 = transparency;
		component.textShadow = shadowed;
		component.filled = filled;
	}
	
	public static void addSprites(int id, String path, int... spriteIds) {
		if (spriteIds.length < 2) {
			throw new IllegalStateException("Error adding sprites, not enough sprite id's provided.");
		}
		RSInterface component = addInterface(id);
		component.id = id;
		component.type = 19;
		component.backgroundSprites = new Sprite[spriteIds.length];
		for (int i = 0; i < spriteIds.length; i++) {
			component.backgroundSprites[i] = imageLoader(spriteIds[i], path);
			if (component.backgroundSprites[i] == null) {
				throw new IllegalStateException("Error adding sprites, unable to find one of the images.");
			}
		}
		component.sprite1 = component.backgroundSprites[0];
	}
	
	public static void addClickableSprites(int id, String tooltip, String path, int... spriteIds) {
		addSprites(id, path, spriteIds);
		RSInterface component = interfaceCache[id];
		component.atActionType = 4;
		component.tooltip = tooltip;
		component.width = component.backgroundSprites[0].myWidth;
		component.height = component.backgroundSprites[0].myHeight;
	}
	
	/**
	 * The menu item for this component
	 */
	private RSMenuItem menuItem;
	
	/**
	 * Retrieves the {@link RSMenuItem} object that is currently in focus by this component
	 * @return	the item in focus
	 */
	public RSMenuItem getMenuItem() {
		return menuItem;
	}
	
	/**
	 * Modifies the current {@link RSMenuItem} for this component
	 * @param menuItem	the new item that will be replacing the previous item
	 */
	public void setMenuItem(RSMenuItem menuItem) {
		this.menuItem = menuItem;
	}
	
	/**
	 * The visibility of the menu of items.
	 */
	private boolean menuVisible;
	
	/**
	 * Determines if the menu of items is visible.
	 * @return	will return true if the player has triggered the drop down button.
	 */
	public boolean isMenuVisible() {
		return menuVisible;
	}
	
	/**
	 * Sets the menu to either a visible or invisible state.
	 * @param menuVisible	true if the menu is to be visible, otherwise invisible.
	 */
	public void setMenuVisible(boolean menuVisible) {
		this.menuVisible = menuVisible;
	}
	
	/**
	 * Determines if a component is filled with pixels, or is empty.
	 */
	public boolean filled;
	
	/**
	 * The color a component is filled with
	 */
	public int fillColor;
	
	/**
	 * An array of background sprites
	 */
	public Sprite[] backgroundSprites;
//	@SuppressWarnings("unused")
	private boolean interfaceShown;
	public boolean newScroller;

}
