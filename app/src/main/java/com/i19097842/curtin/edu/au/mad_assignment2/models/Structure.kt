package com.i19097842.curtin.edu.au.mad_assignment2.models

/**
 * Simple classes to represent structures that are placeable on the map
 */
open class Structure(val drawable: Int, val name: String)

interface Building

class Residential(drawable: Int) : Building, Structure(drawable = drawable, name = "Residential")

class Commercial(drawable: Int) : Building, Structure(drawable = drawable, name = "Commercial")

class Road(drawable: Int) : Structure(drawable = drawable, name = "Road")

class Tree(drawable: Int) : Structure(drawable = drawable, name= "Tree")