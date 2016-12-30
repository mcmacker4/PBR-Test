package net.upgaming.pbrgame

import net.upgaming.pbrengine.gameobject.Camera
import net.upgaming.pbrengine.gameobject.Entity
import net.upgaming.pbrengine.graphics.GraphicsLayer
import net.upgaming.pbrengine.graphics.EntityRenderer
import net.upgaming.pbrengine.graphics.ShaderProgram
import net.upgaming.pbrengine.graphics.SkyboxRenderer
import net.upgaming.pbrengine.lights.PointLight
import net.upgaming.pbrengine.material.Material
import net.upgaming.pbrengine.models.Model
import net.upgaming.pbrengine.texture.TextureLoader
import net.upgaming.pbrengine.texture.TextureSkybox
import org.joml.Vector3f


class PrimaryLayer : GraphicsLayer {
    
    val entityRenderer: EntityRenderer
    val skyboxRenderer: SkyboxRenderer
    val sphereModel: Model
    val sphereEntities = arrayListOf<Entity>()
    val camera: Camera
    val skyboxTexYoko: TextureSkybox
    
    init {
        sphereModel = Model.OBJLoader.load("sphere")
        
        val color = Vector3f(0.6f, 0.6f, 0.6f)
        for(i in 1..10) {
            sphereEntities.add(Entity(
                    sphereModel,
                    Material(color, i / 10.0f, 0f),
                    Vector3f(i * 2.5f - 6, 0f, 0f)
            ))
        }
        
        entityRenderer = EntityRenderer(ShaderProgram.load("simple"))
        camera = Camera(Vector3f(0f, 0f, 3f))
        skyboxTexYoko = TextureLoader.loadTextureSkybox("sea")
        skyboxRenderer = SkyboxRenderer()
    }
    
    override fun update(delta: Float) {
        camera.update(delta)
    }

    override fun render() {
        skyboxRenderer.render(camera, skyboxTexYoko)
        
        entityRenderer.pushAll(sphereEntities)
        entityRenderer.draw(camera, skyboxTexYoko)
    }
    
    override fun cleanUp() {
        camera.delete()
        sphereEntities.forEach(Entity::delete)
        entityRenderer.shader.delete()
    }
    
}