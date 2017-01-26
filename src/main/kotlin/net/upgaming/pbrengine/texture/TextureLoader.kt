package net.upgaming.pbrengine.texture

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.system.MemoryUtil
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import javax.imageio.ImageIO


object TextureLoader {

    val NULL_TEXTURE_2D = Texture2D(0)
    val NULL_TEXTURE_SKYBOX = TextureSkybox(0)
    
    fun loadTexture2D(name: String): Texture2D {
        
        val image = ImageIO.read(File("res/textures/$name.png"))
        val buffer = toBuffer(image)
        
        val texID = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, texID)
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE)
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
        
        glBindTexture(GL_TEXTURE_2D, 0)
        
        return Texture2D(texID)
        
    }
    
    private val faces = arrayOf("right", "left", "top", "bottom", "back", "front")
    
    fun loadTextureSkybox(name: String, format: String = "png"): TextureSkybox {
        
        val folder = "res/textures/skyboxes/$name/"
        
        val texID = glGenTextures()
        glBindTexture(GL_TEXTURE_CUBE_MAP, texID)
        
        for(i in 0..5) {
            try {
                val image = ImageIO.read(File(folder + faces[i] + "." + format))
                val buffer = toBuffer(image)
                glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA8, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
            } catch(ex: IOException) {
                throw Exception(ex.message + " " + folder + faces[i] + ".png")
            }
        }
        
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE)

        glBindTexture(GL_TEXTURE_CUBE_MAP, 0)
        
        return TextureSkybox(texID)
        
    }
    
    private fun toBuffer(img: BufferedImage): ByteBuffer {
        val pixels = IntArray(img.width * img.height)
        img.getRGB(0, 0, img.width, img.height, pixels, 0, img.width)
        
        val buffer = MemoryUtil.memAlloc(img.width * img.height * 4)
        
        for(y in 0 until img.height) {
            for(x in 0 until img.width) {
                val pixel = pixels[y * img.width + x]
                buffer.put(pixel.shr(16).and(0xFF).toByte())
                buffer.put(pixel.shr(8).and(0xFF).toByte())
                buffer.put(pixel.and(0xFF).toByte())
                buffer.put(pixel.shr(24).and(0xFF).toByte())
            }
        }
        
        buffer.flip()
        return buffer
    }
    
}