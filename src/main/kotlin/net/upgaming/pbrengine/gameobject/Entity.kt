package net.upgaming.pbrengine.gameobject

import net.upgaming.pbrengine.material.Material
import net.upgaming.pbrengine.models.Model
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer


class Entity(val model: Model, val material: Material = Material.default(), val position: Vector3f = Vector3f(),
             val rotation: Vector3f = Vector3f(), var scale: Float = 1f) {
    
    private val mmBuffer = MemoryUtil.memAllocFloat(16)
    private val modelMatrix = Matrix4f()
    
    fun getModelMatrixFB(): FloatBuffer {
        mmBuffer.clear()
        modelMatrix.identity()
                .translate(position)
                .rotate(rotation.x, 1f, 0f, 0f)
                .rotate(rotation.y, 0f, 1f, 0f)
                .rotate(rotation.z, 0f, 0f, 1f)
                .scale(scale)
        modelMatrix.get(mmBuffer)
        return mmBuffer
    }
    
    fun delete() {
        MemoryUtil.memFree(mmBuffer)
    }
    
}