package net.upgaming.pbrengine

import net.upgaming.pbrengine.window.Display
import net.upgaming.pbrengine.window.GraphicsLayer


class Application(width: Int, height: Int, title: String) {
    
    val display: Display
    val graphicsLayers = arrayListOf<GraphicsLayer>()
    
    init {
        display = Display(width, height)
    }
    
}