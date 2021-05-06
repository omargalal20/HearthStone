package model.cards.spells;

import java.io.Serializable;

import model.cards.Card;
import model.cards.Rarity;

public abstract class Spell extends Card implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5872806119569258696L;

	public Spell(String n, int m, Rarity r) {
		super(n, m, r);
	}

	@Override
	public String toString() {
		return "Spell [Name=" + getName() +"\n"+ ", Rarity="
				+ getRarity() + "\n"+", Mana Cost="  + getManaCost() + "]";
	}

}
