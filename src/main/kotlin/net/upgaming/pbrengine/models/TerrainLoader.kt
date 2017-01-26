package net.upgaming.pbrengine.models

import net.upgaming.pbrengine.util.TerrainNoise
import org.joml.Vector2f
import org.joml.Vector3f


object TerrainLoader {

    private val size = 50

    fun resultingSize(scale: Float) = size * scale
    
    fun load(scale: Float = 0.5f, position: Vector3f = Vector3f()) : Model {

        val n = size+1

        val vertices = FloatArray(n*n*3)
        val normals = FloatArray(n*n*3)
        val texCoords = FloatArray(n*n*2)

        for(z in 0 until n) {
            for(x in 0 until n) {
                val vpos = Vector2f(position.x + x*scale, position.z + z*scale)
                vertices[(z*n + x)*3] = x.toFloat() * scale
                vertices[(z*n + x)*3 + 1] =
                        position.y + TerrainNoise.height(vpos.x, vpos.y)
                vertices[(z*n + x)*3 + 2] = z.toFloat() * scale
                val normal = calcNormal(vpos, scale)
                normals[(z*n + x)*3] = normal.x
                normals[(z*n + x)*3 + 1] = normal.y
                normals[(z*n + x)*3 + 2] = normal.z
                texCoords[(z*n + x)*2] = x.toFloat() / size
                texCoords[(z*n + x)*2 + 1] = z.toFloat() / size
            }
        }

        val indices = IntArray(n*n*6)

        var count = 0
        for(z in 0 until size) {
            for(x in 0 until size) {
                val bl = z*n + x
                val br = bl + 1
                val tl = (z+1)*n + x
                val tr = tl + 1
                indices[count++] = bl
                indices[count++] = br
                indices[count++] = tl
                indices[count++] = tl
                indices[count++] = br
                indices[count++] = tr
            }
        }

        return Model.Loader.load(Model.Data(vertices, normals, texCoords, indices))

    }

    private fun calcNormal(pos: Vector2f, scale: Float): Vector3f {

        val hL = TerrainNoise.height(pos.x - scale, pos.y)
        val hR = TerrainNoise.height(pos.x + scale, pos.y)
        val hU = TerrainNoise.height(pos.x, pos.y - scale)
        val hD = TerrainNoise.height(pos.x, pos.y + scale)

        return Vector3f(hL - hR, 2.0f, hU - hD).normalize()

    }

}
