package net.upgaming.pbrengine.gameobject

import net.upgaming.pbrengine.util.unaryMinus
import net.upgaming.pbrgame.PBRGame
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer


class Camera(val position: Vector3f = Vector3f(), val rotation: Vector3f = Vector3f(), var fov: Float = 90f) {
    
    private val vMatBuffer = MemoryUtil.memAllocFloat(16)
    private val vMatUtBuffer = MemoryUtil.memAllocFloat(16)
    private val pMatBuffer = MemoryUtil.memAllocFloat(16)
    
    private val sensitivity = 0.002f
    private val speed = 1f

    fun update(delta: Float) {
        
        val change = Vector3f()
        
        if(PBRGame.isKeyDown(GLFW_KEY_W)) {
            change.add(frontVector())
        }
        if(PBRGame.isKeyDown(GLFW_KEY_S)) {
            change.add(backVector())
        }
        if(PBRGame.isKeyDown(GLFW_KEY_A)) {
            change.add(leftVector())
        }
        if(PBRGame.isKeyDown(GLFW_KEY_D)) {
            change.add(rightVector())
        }
        if(PBRGame.isKeyDown(GLFW_KEY_SPACE)) {
            change.add(Vector3f(0f, 1f, 0f))
        }
        if(PBRGame.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
            change.add(Vector3f(0f, -1f, 0f))
        }
        
        position.add(change.mul(delta * speed))
        
        rotation.add(-PBRGame.getMouseDY().toFloat() * sensitivity, -PBRGame.getMouseDX().toFloat() * sensitivity, 0f)
        
    }
    
    private fun frontVector(): Vector3f {
        val mat = Matrix3f().rotateY(rotation.y)
        return Vector3f(0f, 0f, -1f).mul(mat).normalize()
    }
    
    private fun leftVector(): Vector3f {
        val mat = Matrix3f().rotateY(rotation.y + (Math.PI / 2).toFloat())
        return Vector3f(0f, 0f, -1f).mul(mat).normalize()
    }
    
    private fun rightVector(): Vector3f {
        val mat = Matrix3f().rotateY(rotation.y + (-Math.PI / 2).toFloat())
        return Vector3f(0f, 0f, -1f).mul(mat).normalize()
    }
    
    private fun backVector(): Vector3f {
        return -frontVector()
    }
    
    fun getViewMatrix(): Matrix4f {
        return getViewMatrixUntranslated().translate(-position)
    }
    
    fun getViewMatrixUntranslated(): Matrix4f {
        return Matrix4f()
                .rotate(-rotation.x, 1f, 0f, 0f)
                .rotate(-rotation.y, 0f, 1f, 0f)
                .rotate(-rotation.z, 0f, 0f, 1f)
    }
    
    fun getViewMatrixUntranslatedFB(): FloatBuffer {
        vMatUtBuffer.clear()
        val matrix = getViewMatrixUntranslated()
        matrix.get(vMatUtBuffer)
        return vMatUtBuffer
    }
    
    fun getViewMatrixFB(): FloatBuffer {
        vMatBuffer.clear()
        val matrix = getViewMatrix()
        matrix.get(vMatBuffer)
        return vMatBuffer
    }
    
    fun getProjectionMatrix(): Matrix4f {
        return Matrix4f().setPerspective(
                Math.toRadians(fov.toDouble()).toFloat(),
                PBRGame.display.width / PBRGame.display.height.toFloat(),
                0.1f, 1000f
        )
    }
    
    fun getProjectionMatrixFB(): FloatBuffer {
        pMatBuffer.clear()
        val matrix = getProjectionMatrix()
        matrix.get(pMatBuffer)
        return pMatBuffer
    }
    
    fun delete() {
        MemoryUtil.memFree(vMatBuffer)
        MemoryUtil.memFree(pMatBuffer)
    }

}
