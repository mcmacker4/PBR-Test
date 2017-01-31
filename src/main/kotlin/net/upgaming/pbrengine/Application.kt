package net.upgaming.pbrengine

import net.upgaming.pbrengine.util.Timer
import net.upgaming.pbrengine.window.Display
import net.upgaming.pbrengine.graphics.GraphicsLayer
import net.upgaming.pbrengine.models.Model
import org.lwjgl.opengl.GL11.*

/*
Default OpenGL Imports.

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
 */

open class Application(width: Int, height: Int, title: String = "PBR Engine") {
    
    val display: Display
    val timer = Timer()
    private val graphicsLayers = linkedSetOf<GraphicsLayer>()
    
    init {
        display = Display(width, height, title)
    }
    
    fun start() {
        
        while(!display.shouldClose()) {
            
            glClear(GL_COLOR_BUFFER_BIT.or(GL_DEPTH_BUFFER_BIT))
            
            update()
            render()
            
            display.update()
            
        }
        
        cleanUp()
        
        Model.Loader.cleanUp()
        display.destroy()
        
    }
    
    private fun update() {
        timer.update()
        graphicsLayers.forEach { it.update(timer.delta.toFloat()) }
    }
    
    private fun render() {
        graphicsLayers.forEach { it.render() }
    }
    
    private fun cleanUp() {
        graphicsLayers.forEach { it.cleanUp() }
    }
    
    fun pushLayer(layer: GraphicsLayer) = graphicsLayers.add(layer)
    
    fun isKeyDown(glfwKey: Int) = display.isKeyDown(glfwKey)
    fun isBtnDown(glfwBtn: Int) = display.isBtnDown(glfwBtn)
    
}