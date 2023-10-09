package com.tqs.filecommander.model

/**
 * used for showing audio、download file and documents
 */
data class DocumentEntity(
    var suffix: String = "",
    var number: Int = 0,
    var selected: Boolean = false,
    var typeFile: Int = -1,
    var docPathList: MutableList<FileEntity> = mutableListOf()
)
