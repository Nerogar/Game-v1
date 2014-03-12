package de.nerogar.gameV1.ai;

import de.nerogar.gameV1.level.EntityFighting;

public abstract class AILogic {

	private boolean closed;

	protected EntityFighting entity;

	public AILogic(EntityFighting entity) {
		this.entity = entity;
	}

	public abstract void update(float time);

	public void removeLogic(AILogic logic) {
		closed = true;
	}

	public boolean isClosed() {
		return closed;
	}
}
