package com.i19097842.curtin.edu.au.mad_assignment2.models

/**
 * Simple classes to represent structures that are placeable on the map
 */
open class Structure(val drawable: Int, var name: String) {
    open fun getTypeString() : String {
        return "Structure"
    }

    open fun clone(): Structure {
        return Structure(drawable, name)
    }
}

interface Building

class Residential(drawable: Int, name: String = "Residential")
    : Building, Structure(drawable = drawable, name = name) {
    override fun getTypeString() : String {
        return "Residential"
    }

    override fun clone(): Structure {
        return Residential(drawable, name)
    }
}

class Commercial(drawable: Int, name: String = "Commercial")
    : Building, Structure(drawable = drawable, name = name) {
    override fun getTypeString(): String {
        return "Commercial"
    }

    override fun clone(): Structure {
        return Commercial(drawable, name)
    }
}

class Road(drawable: Int, name: String = "Road") : Structure(drawable = drawable, name = name) {
    override fun getTypeString(): String {
        return "Road"
    }

    override fun clone(): Structure {
        return Road(drawable, name)
    }
}

class Tree(drawable: Int, name: String = "Tree") : Structure(drawable = drawable, name = name) {
    override fun getTypeString(): String {
        return "Tree"
    }

    override fun clone(): Structure {
        return Tree(drawable, name)
    }
}