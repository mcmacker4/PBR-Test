package net.upgaming.pbrgame

import net.upgaming.pbrengine.gameobject.Camera
import net.upgaming.pbrengine.gameobject.Entity
import net.upgaming.pbrengine.graphics.GraphicsLayer
import net.upgaming.pbrengine.graphics.EntityRenderer
import net.upgaming.pbrengine.graphics.ShaderProgram
import net.upgaming.pbrengine.graphics.SkyboxRenderer
import net.upgaming.pbrengine.models.Model
import net.upgaming.pbrengine.texture.TextureLoader
import net.upgaming.pbrengine.texture.TextureSkybox
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*


class PrimaryLayer : GraphicsLayer {
    
    val entityRenderer: EntityRenderer
    val skyboxRenderer: SkyboxRenderer
    val model: Model
    val entity: Entity
    val camera: Camera
    val skyboxTexture: TextureSkybox
    
    init {
        model = Model.OBJLoader.load("sphere")
        entity = Entity(model)
        entityRenderer = EntityRenderer(ShaderProgram.load("simple"))
        camera = Camera(Vector3f(0f, 0f, 3f))
        skyboxTexture = TextureLoader.loadTextureSkybox("yokohama")
        skyboxRenderer = SkyboxRenderer()
    }
    
    override fun update(delta: Float) {
        camera.update(delta)
    }

    override fun render() {
        skyboxRenderer.render(camera, skyboxTexture)
        entityRenderer.push(entity)
        entityRenderer.draw(camera, skyboxTexture)
    }
    
    override fun cleanUp() {
        camera.delete()
        entity.delete()
    }
    
}