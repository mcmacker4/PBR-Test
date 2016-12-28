package net.upgaming.pbrengine.graphics

import net.upgaming.pbrengine.gameobject.Camera
import net.upgaming.pbrengine.gameobject.Entity
import net.upgaming.pbrengine.texture.TextureSkybox
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL30.*


class EntityRenderer(val shader: ShaderProgram) {
    
    private val entityQueue = arrayListOf<Entity>()
    
    fun push(entity: Entity) {
        entityQueue.add(entity)
    }

    fun pushAll(entities: List<Entity>) {
        entityQueue.addAll(entities)
    }
    
    fun draw(camera: Camera, skybox: TextureSkybox) {
        
        shader.start()
        shader.loadMatrix4f("projectionMatrix", camera.getProjectionMatrixFB())
        shader.loadMatrix4f("viewMatrix", camera.getViewMatrixFB())
        shader.loadInt("skybox", 0)
        shader.loadVector3f("campos", camera.position)
        
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_CUBE_MAP, skybox.id)
        
        val it = entityQueue.iterator()
        while(it.hasNext()) {
            val entity = it.next()
            shader.loadMatrix4f("modelMatrix", entity.getModelMatrixFB())
            draw(entity)
            it.remove()
        }
        
        shader.stop()
        
    }
    
    private fun draw(entity: Entity) {
        glBindVertexArray(entity.model.vao)
        glDrawArrays(GL_TRIANGLES, 0, entity.model.vertexCount)
        glBindVertexArray(0)
    }

}