package com.i19097842.curtin.edu.au.mad_assignment2.models

open class Structure(val drawable: Int, val name: String)

class Residential(drawable: Int) : Structure(drawable = drawable, name = "Residential")

class Commercial(drawable: Int) : Structure(drawable = drawable, name = "Commercial")

class Road(drawable: Int) : Structure(drawable = drawable, name = "Road")

