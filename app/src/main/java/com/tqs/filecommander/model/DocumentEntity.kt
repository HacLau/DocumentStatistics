package com.tqs.filecommander.model

data class DocumentEntity(
    var suffix: String = "",
    var number: Int = 0,
    var selected: Boolean = false,
    var path: String = "",
    var typeFile: Int = -1
)
