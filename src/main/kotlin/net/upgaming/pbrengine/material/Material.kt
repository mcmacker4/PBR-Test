package net.upgaming.pbrengine.material

import org.joml.Vector3f


class Material(val color: Vector3f, var roughness: Float, var metallic: Float) {
    
    companion object {
        fun default(): Material {
            return Material(Vector3f(0.8f, 0.8f, 0.8f), 0.6f, 0f)
        }
    }
    
}