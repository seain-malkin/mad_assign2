package com.i19097842.curtin.edu.au.mad_assignment2.models

class MapElement(
    val buildable: Boolean,
    val nw: Int,
    val ne: Int,
    val se: Int,
    val sw: Int,
    var structure: Structure? = null
)