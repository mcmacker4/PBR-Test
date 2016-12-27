package net.upgaming.pbrengine.gameobject

import net.upgaming.pbrengine.util.unaryMinus
import net.upgaming.pbrgame.PBRGame
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer


class Camera(val position: Vector3f = Vector3f(), val rotation: Vector3f = Vector3f(), var fov: Float = 90f) {
    
    private val vMatBuffer = MemoryUtil.memAllocFloat(16)
    private val pMatBuffer = MemoryUtil.memAllocFloat(16)
    
    fun getViewMatrixFB(): FloatBuffer {
        vMatBuffer.clear()
        val matrix = Matrix4f()
                .rotate(-rotation.x, 1f, 0f, 0f)
                .rotate(-rotation.y, 0f, 1f, 0f)
                .rotate(-rotation.z, 0f, 0f, 1f)
                .translate(-position)
        matrix.get(vMatBuffer)
        return vMatBuffer
    }
    
    fun getProjectionMatrixFB(): FloatBuffer {
        pMatBuffer.clear()
        val matrix = Matrix4f().setPerspective(
                Math.toRadians(fov.toDouble()).toFloat(),
                PBRGame.display.width / PBRGame.display.height.toFloat(),
                0.1f, 1000f
        )
        matrix.get(pMatBuffer)
        return pMatBuffer
    }
    
    fun delete() {
        MemoryUtil.memFree(vMatBuffer)
        MemoryUtil.memFree(pMatBuffer)
    }
    
}
