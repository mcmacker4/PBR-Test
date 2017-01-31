package net.upgaming.pbrgame

import net.upgaming.pbrengine.Application
import net.upgaming.pbrengine.gameobject.Camera
import net.upgaming.pbrengine.gameobject.Entity
import net.upgaming.pbrengine.graphics.GraphicsLayer
import net.upgaming.pbrengine.graphics.EntityRenderer
import net.upgaming.pbrengine.graphics.ShaderProgram
import net.upgaming.pbrengine.lights.PointLight
import net.upgaming.pbrengine.material.Material
import net.upgaming.pbrengine.models.Model
import net.upgaming.pbrengine.models.TerrainLoader
import net.upgaming.pbrengine.window.Input
import org.joml.Vector3f


class PrimaryLayer(val app: Application) : GraphicsLayer {
    
    val entityRenderer: EntityRenderer
    val camera: Camera
    val pointLights = arrayListOf<PointLight>()

    val sphereModel: Model
    val sphereEntities = arrayListOf<Entity>()

    val terrainModels = arrayListOf<Model>()
    val terrains = arrayListOf<Entity>()

    val input = Input(app.display)
    
    init {
        entityRenderer = EntityRenderer(ShaderProgram.load("simple"))
        camera = Camera(Vector3f(0f, 0f, 3f))

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

        val tsize = TerrainLoader.resultingSize(0.1f)
        for (x in -2..1) {
            for(z in -2..1) {
                val pos = Vector3f(x*tsize, 0f, z*tsize)
                terrainModels.add(TerrainLoader.load(0.1f, pos))
                terrains.add(Entity(terrainModels.last(), position = pos))
            }
        }

        pointLights.add(PointLight(Vector3f(-4f, -4f, 10f), Vector3f(1f)))
        pointLights.add(PointLight(Vector3f(-4f, 4f, 10f), Vector3f(1f)))
        pointLights.add(PointLight(Vector3f(4f, 4f, 10f), Vector3f(1f)))
        pointLights.add(PointLight(Vector3f(4f, -4f, 10f), Vector3f(1f)))
    }
    
    override fun update(delta: Float) {
        camera.update(delta, input)
    }

    override fun render() {
        //skyboxRenderer.render(camera, skyboxTexYoko)
        for(s in sphereEntities)
            entityRenderer.push(s)
        for(t in terrains)
            entityRenderer.push(t)
        entityRenderer.draw(camera, app.display, pointLights)
    }
    
    override fun cleanUp() {
        camera.delete()
        terrains.forEach(Entity::delete)
        sphereEntities.forEach(Entity::delete)
    }
    
}