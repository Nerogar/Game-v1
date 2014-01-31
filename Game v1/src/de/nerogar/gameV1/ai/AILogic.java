package de.nerogar.gameV1.ai;

import de.nerogar.gameV1.level.EntityFighting;

public abstract class AILogic {

	protected EntityFighting entity;

	public AILogic(EntityFighting entity) {
		this.entity = entity;
	}

	public abstract void update(float time);

	public void remove() {
		entity.aiContainer.remove(this);
	}
}
