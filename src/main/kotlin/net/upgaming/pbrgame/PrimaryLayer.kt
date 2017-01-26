package net.upgaming.pbrgame

import net.upgaming.pbrengine.gameobject.Camera
import net.upgaming.pbrengine.gameobject.Entity
import net.upgaming.pbrengine.graphics.GraphicsLayer
import net.upgaming.pbrengine.graphics.EntityRenderer
import net.upgaming.pbrengine.graphics.ShaderProgram
import net.upgaming.pbrengine.lights.PointLight
import net.upgaming.pbrengine.models.Model
import net.upgaming.pbrengine.models.TerrainLoader
import org.joml.Vector3f


class PrimaryLayer : GraphicsLayer {
    
    val entityRenderer: EntityRenderer
    val camera: Camera
    val pointLights = arrayListOf<PointLight>()

    val terrainModels = arrayListOf<Model>()
    val terrains = arrayListOf<Entity>()
    
    init {
        entityRenderer = EntityRenderer(ShaderProgram.load("simple"))
        camera = Camera(Vector3f(0f, 0f, 3f))

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
        camera.update(delta)
    }

    override fun render() {
        //skyboxRenderer.render(camera, skyboxTexYoko)
        for(t in terrains)
            entityRenderer.push(t)
        entityRenderer.draw(camera, pointLights)
    }
    
    override fun cleanUp() {
        camera.delete()
        terrains.forEach(Entity::delete)
    }
    
}