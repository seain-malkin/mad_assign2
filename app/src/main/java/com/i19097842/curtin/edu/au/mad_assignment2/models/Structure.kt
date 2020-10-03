package com.i19097842.curtin.edu.au.mad_assignment2.models

import android.graphics.Bitmap

typealias Ownable = Structure.OwnableStructure
typealias Residential = Structure.OwnableStructure.Residential
typealias Commercial = Structure.OwnableStructure.Commercial
typealias Road = Structure.OwnableStructure.Road
typealias Landscape = Structure.Landscape

/**
 * Represents a structure that can be built on a [MapElement]
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 *
 * @property[drawable] Reference to an R.drawable id
 */
open class Structure(
    val drawable: Int,
    var name: String
) {
    fun clone() {

    }
    /**
     * Structures that can be owned by the player inherit from this class. The subclasses were
     * implemented for easy extensibility
     * 
     * @see[drawable]
     * @see[name]
     * @property[image] A user created image that replaces the drawable
     */
    open class OwnableStructure(
        drawable: Int,
        name: String,
        var image: Bitmap?
    ): Structure(drawable = drawable, name = name) {

        class Residential(
            drawable: Int,
            name: String = "Residential",
            image: Bitmap? = null
        ): OwnableStructure(drawable = drawable, name = name, image = image)

        class Commercial(
            drawable: Int,
            name: String = "Commercial",
            image: Bitmap? = null
        ): OwnableStructure(drawable = drawable, name = name, image = image)

        class Road(
            drawable: Int,
            name: String = "Road",
            image: Bitmap? = null
        ): OwnableStructure(drawable = drawable, name = name, image = image)
    }
    
    class Landscape(
        drawable: Int,
        name: String,
        image: Bitmap? = null
    ): Structure(drawable = drawable, name = name)
}
