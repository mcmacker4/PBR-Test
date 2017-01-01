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
    val planeModel: Model
    val planeEntity: Entity
    val camera: Camera
    val skyboxTexYoko: TextureSkybox
    val pointLight: PointLight
    
    init {
        planeModel = Model.OBJLoader.load("plane")
        
        planeEntity = Entity(
                planeModel,
                Material(
                        Vector3f(),
                        0.8f, 0f,
                        TextureLoader.loadTexture2D("brickwall", "jpg"),
                        TextureLoader.loadTexture2D("brickwall_normal", "jpg")
                ), rotation = Vector3f(Math.PI.toFloat() / 2, 0f, 0f)
        )
        
        entityRenderer = EntityRenderer(ShaderProgram.load("simple"))
        camera = Camera(Vector3f(0f, 0f, 3f))
        skyboxTexYoko = TextureLoader.loadTextureSkybox("yokohama")
        skyboxRenderer = SkyboxRenderer()
        
        pointLight = PointLight(Vector3f(0f, 0f, 4f), Vector3f(0.8f))
        
    }
    
    var counter = 0.0
    override fun update(delta: Float) {
        camera.update(delta)
        counter += Math.PI * delta
        pointLight.position.set(
                Math.cos(counter).toFloat(),
                Math.sin(counter).toFloat(),
                3f
        )
    }

    override fun render() {
        //skyboxRenderer.render(camera, skyboxTexYoko)
        
        entityRenderer.addPointLight(pointLight)
        entityRenderer.push(planeEntity)
        entityRenderer.draw(camera, skyboxTexYoko)
    }
    
    override fun cleanUp() {
        camera.delete()
    }
    
}