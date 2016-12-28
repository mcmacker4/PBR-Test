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
    val pointLights = arrayListOf<PointLight>()
    
    init {
        sphereModel = Model.OBJLoader.load("sphere")
        
        for(x in 0 until 10) {
            for(y in 0..1) {
                
                val material = Material(Vector3f(1f, 0f, 0f), x / 10f, y.toFloat())
                sphereEntities.add(Entity(
                        sphereModel,
                        material,
                        Vector3f(x.toFloat() - 4.5f, y.toFloat() - 0.5f, 0f),
                        scale = 0.5f
                ))
                
            }
        }
        
        entityRenderer = EntityRenderer(ShaderProgram.load("simple"))
        camera = Camera(Vector3f(0f, 0f, 3f))
        skyboxTexYoko = TextureLoader.loadTextureSkybox("yokohama")
        skyboxRenderer = SkyboxRenderer()
        
        pointLights.add(PointLight(Vector3f(-4f, -4f, 10f), Vector3f(1f)))
        pointLights.add(PointLight(Vector3f(-4f, 4f, 10f), Vector3f(1f)))
        pointLights.add(PointLight(Vector3f(4f, 4f, 10f), Vector3f(1f)))
        pointLights.add(PointLight(Vector3f(4f, -4f, 10f), Vector3f(1f)))
        
    }
    
    override fun update(delta: Float) {
        camera.update(delta)
    }

    override fun render() {
        //skyboxRenderer.render(camera, skyboxTexYoko)
        
        entityRenderer.addPointLights(pointLights)
        entityRenderer.pushAll(sphereEntities)
        entityRenderer.draw(camera, skyboxTexYoko)
    }
    
    override fun cleanUp() {
        camera.delete()
        sphereEntities.forEach(Entity::delete)
    }
    
}