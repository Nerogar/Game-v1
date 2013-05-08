package de.nerogar.gameV1.animation;

import java.util.Comparator;

public class KeyframeComparator implements Comparator<Keyframe> {
	
    @Override
    public int compare(Keyframe keyframe1, Keyframe keyframe2) {
        return Float.valueOf(keyframe1.offset).compareTo(Float.valueOf(keyframe2.offset));
    }

}