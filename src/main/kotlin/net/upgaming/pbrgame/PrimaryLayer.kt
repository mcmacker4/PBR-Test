package net.upgaming.pbrgame

import net.upgaming.pbrengine.gameobject.Camera
import net.upgaming.pbrengine.gameobject.Entity
import net.upgaming.pbrengine.graphics.GraphicsLayer
import net.upgaming.pbrengine.graphics.EntityRenderer
import net.upgaming.pbrengine.graphics.ShaderProgram
import net.upgaming.pbrengine.models.Model
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*


class PrimaryLayer : GraphicsLayer {
    
    val entityRenderer: EntityRenderer
    val model: Model
    val entity: Entity
    val camera: Camera
//    val modelData = Model.Data(
//            floatArrayOf(
//                    -1f, -1f, 0f,
//                    1f, -1f, 0f,
//                    0f, 1f, 0f
//            ),
//            floatArrayOf(
//                    0f, 0f, 1f,
//                    0f, 0f, 1f,
//                    0f, 0f, 1f
//            ),
//            floatArrayOf(
//                    0f, 1f,
//                    1f, 1f,
//                    0.5f, 0f
//            )
//    )
    
    init {
        model = Model.OBJLoader.load("res/models/sphere.obj")
        entity = Entity(model)
        entityRenderer = EntityRenderer(ShaderProgram.load("simple"))
        camera = Camera(Vector3f(0f, 0f, 3f))
    }
    
    override fun update(delta: Float) {
        if(PBRGame.isKeyDown(GLFW_KEY_W)) {
            camera.position.add(0f, 0f, -1f * delta)
        } else if(PBRGame.isKeyDown(GLFW_KEY_S)) {
            camera.position.add(0f, 0f, 1f * delta)
        }
        if(PBRGame.isKeyDown(GLFW_KEY_A)) {
            camera.position.add(-1f * delta, 0f, 0f)
        } else if(PBRGame.isKeyDown(GLFW_KEY_D)) {
            camera.position.add(1f * delta, 0f, 0f)
        }
    }

    override fun render() {
        entityRenderer.push(entity)
        entityRenderer.draw(camera)
    }
    
}