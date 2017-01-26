package net.upgaming.pbrgame

import net.upgaming.pbrengine.Application


object PBRGame : Application(1200, 700, "PBR Game") {
    
    init {
        pushLayer(PrimaryLayer())
    }
    
}

fun main(args: Array<String>) {
    PBRGame.start()
}