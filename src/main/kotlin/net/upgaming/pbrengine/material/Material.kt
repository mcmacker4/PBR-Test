package net.upgaming.pbrengine.material

import net.upgaming.pbrengine.texture.Texture2D
import net.upgaming.pbrengine.texture.TextureLoader
import org.joml.Vector3f


class Material(val color: Vector3f, var roughness: Float, var metallic: Float,
               var albedo: Texture2D = TextureLoader.emptyTexture2D,
               var normalMap: Texture2D = TextureLoader.emptyTexture2D) {
    
    fun useAlbedo() = albedo.id != 0
    fun useNormalMap() = normalMap.id != 0
    
    companion object {
        fun default(): Material {
            return Material(Vector3f(0.8f, 0.8f, 0.8f), 0.6f, 0f)
        }
    }
    
}