package com.rampage.client.entity;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.brutality.client.entity.Animable;
import com.rampage.client.cache.def.ItemDef;
import com.rampage.client.entity.model.Model;

public final class Item extends Animable {

	public final Model getRotatedModel()
	{
		ItemDef itemDef = ItemDef.forID(ID);
			return itemDef.method201(anInt1559);
	}

	public Item()
	{
	}

	public int ID;
	public int x;
	public int y;
	public int anInt1559;
}
