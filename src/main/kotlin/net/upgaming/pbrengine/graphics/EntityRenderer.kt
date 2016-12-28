package net.upgaming.pbrengine.graphics

import net.upgaming.pbrengine.gameobject.Camera
import net.upgaming.pbrengine.gameobject.Entity
import net.upgaming.pbrengine.lights.PointLight
import net.upgaming.pbrengine.texture.TextureSkybox
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL30.*


class EntityRenderer(val shader: ShaderProgram) {
    
    private val entityQueue = arrayListOf<Entity>()
    private val pointLights = arrayListOf<PointLight>()
    
    fun push(entity: Entity) {
        entityQueue.add(entity)
    }

    fun pushAll(entities: List<Entity>) {
        entityQueue.addAll(entities)
    }
    
    fun addPointLights(lights: List<PointLight>) {
        pointLights.addAll(lights)
    }
    
    fun addPointLight(light: PointLight) {
        pointLights.add(light)
    }
    
    fun draw(camera: Camera, skybox: TextureSkybox) {
        
        shader.start()
        shader.loadMatrix4f("projectionMatrix", camera.getProjectionMatrixFB())
        shader.loadMatrix4f("viewMatrix", camera.getViewMatrixFB())
        shader.loadVector3f("cameraPos", camera.position)
        shader.loadFloat("ambientLight", 0.3f)
        shader.loadInt("skybox", 0)
        
        for(i in 0 until pointLights.size) {
            shader.loadVector3f("pointLights[$i].position", pointLights[i].position)
            shader.loadVector3f("pointLights[$i].color", pointLights[i].color)
        }
        
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_CUBE_MAP, skybox.id)
        
        val it = entityQueue.iterator()
        while(it.hasNext()) {
            val entity = it.next()
            shader.loadVector3f("material.color", entity.material.color)
            shader.loadFloat("material.roughness", entity.material.roughness)
            shader.loadFloat("material.metallic", entity.material.metallic)
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