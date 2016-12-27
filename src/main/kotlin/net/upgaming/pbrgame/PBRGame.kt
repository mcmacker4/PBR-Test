package net.upgaming.pbrgame

import net.upgaming.pbrengine.Application


object PBRGame : Application(1280, 720, "PBR Game") {
    
    init {
        pushLayer(PrimaryLayer())
    }
    
}

fun main(args: Array<String>) {
    PBRGame.start()
}