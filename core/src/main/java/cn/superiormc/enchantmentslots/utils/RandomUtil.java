package cn.superiormc.enchantmentslots.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomUtil {

    public static <K, V> K getRandomKey(Map<K, V> map) {
        if (map.isEmpty()) {
            throw new IllegalArgumentException("Map is empty");
        }

        List<K> keyList = new ArrayList<>(map.keySet());
        int randomIndex = new Random().nextInt(keyList.size());
        return keyList.get(randomIndex);
    }

}
