package de.nerogar.gameV1.animation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Animation {

	public HashMap<Bone, KeyframeSet> keyframesMap = new HashMap<Bone, KeyframeSet>();
	public float length;
	public float offset;
	public float started = -1;
	
	public Animation() {
		
	}

	public void update() {
		if (started < 0) return;
		float now = System.nanoTime()/1000000f;
		offset = (now-started)/length;
		if (offset > 1f) {
			offset = 1f;
			started = -1;
		}
		
		Iterator<Entry<Bone, KeyframeSet>> iterator = keyframesMap.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<Bone, KeyframeSet> entry = iterator.next();
			entry.getKey().set(entry.getValue().getInterpolatedObjectMatrix(offset));
		}
		
	}

	public void play() {
		started = System.nanoTime()/1000000f;
	}

}
