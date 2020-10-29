package com.i19097842.curtin.edu.au.mad_assignment2.models

/**
 * Simple classes to represent structures that are placeable on the map
 */
open class Structure(val drawable: Int, val name: String) {
    open fun getTypeString() : String {
        return "Structure"
    }
}

interface Building

class Residential(drawable: Int) : Building, Structure(drawable = drawable, name = "Residential") {
    override fun getTypeString() : String {
        return "Residential"
    }
}

class Commercial(drawable: Int) : Building, Structure(drawable = drawable, name = "Commercial") {
    override fun getTypeString(): String {
        return "Commercial"
    }
}

class Road(drawable: Int) : Structure(drawable = drawable, name = "Road") {
    override fun getTypeString(): String {
        return "Road"
    }
}

class Tree(drawable: Int) : Structure(drawable = drawable, name= "Tree") {
    override fun getTypeString(): String {
        return "Tree"
    }
}