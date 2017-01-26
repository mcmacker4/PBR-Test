package net.upgaming.pbrengine.util

import org.joml.SimplexNoise

/**
 * Created by mcmacker4
 */

object TerrainNoise {

    fun height(x: Float, z: Float, detail: Int = 0): Float {
        if(detail < 0) return 0f
        return SimplexNoise.noise(x * 0.2f, z * 0.2f)
    }

}