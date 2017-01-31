package net.upgaming.pbrengine.graphics

import net.upgaming.pbrengine.gameobject.Camera
import net.upgaming.pbrengine.models.Model
import net.upgaming.pbrengine.texture.TextureSkybox
import net.upgaming.pbrengine.window.Display
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL30.*


class SkyboxRenderer {
    
    val shader: ShaderProgram
    val model: Model

    val vertices = floatArrayOf(
            -5.0f,-5.0f,-5.0f,
            -5.0f,-5.0f, 5.0f,
            -5.0f, 5.0f, 5.0f,
            5.0f, 5.0f,-5.0f,
            -5.0f,-5.0f,-5.0f,
            -5.0f, 5.0f,-5.0f,
            5.0f,-5.0f, 5.0f,
            -5.0f,-5.0f,-5.0f,
            5.0f,-5.0f,-5.0f,
            5.0f, 5.0f,-5.0f,
            5.0f,-5.0f,-5.0f,
            -5.0f,-5.0f,-5.0f,
            -5.0f,-5.0f,-5.0f,
            -5.0f, 5.0f, 5.0f,
            -5.0f, 5.0f,-5.0f,
            5.0f,-5.0f, 5.0f,
            -5.0f,-5.0f, 5.0f,
            -5.0f,-5.0f,-5.0f,
            -5.0f, 5.0f, 5.0f,
            -5.0f,-5.0f, 5.0f,
            5.0f,-5.0f, 5.0f,
            5.0f, 5.0f, 5.0f,
            5.0f,-5.0f,-5.0f,
            5.0f, 5.0f,-5.0f,
            5.0f,-5.0f,-5.0f,
            5.0f, 5.0f, 5.0f,
            5.0f,-5.0f, 5.0f,
            5.0f, 5.0f, 5.0f,
            5.0f, 5.0f,-5.0f,
            -5.0f, 5.0f,-5.0f,
            5.0f, 5.0f, 5.0f,
            -5.0f, 5.0f,-5.0f,
            -5.0f, 5.0f, 5.0f,
            5.0f, 5.0f, 5.0f,
            -5.0f, 5.0f, 5.0f,
            5.0f,-5.0f, 5.0f
    )
    
    init {
        shader = ShaderProgram.load("skybox")
        model = Model.Loader.loadVerticesOnly(vertices)
    }
    
    fun render(camera: Camera, display: Display, skybox: TextureSkybox) {
        glDepthMask(false)
        glDisable(GL_CULL_FACE)
        shader.start()
        shader.loadMatrix4f("projectionMatrix", camera.getProjectionMatrixFB(display.ratio()))
        shader.loadMatrix4f("viewMatrix", camera.getViewMatrixUntranslatedFB())
        shader.loadInt("skybox", 0)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_CUBE_MAP, skybox.id)
        glBindVertexArray(model.vao)
        glDrawArrays(GL_TRIANGLES, 0, model.vertexCount)
        glBindVertexArray(0)
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0)
        shader.stop()
        glEnable(GL_CULL_FACE)
        glDepthMask(true)
    }
    
}