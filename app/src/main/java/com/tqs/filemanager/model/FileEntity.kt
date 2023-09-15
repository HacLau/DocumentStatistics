package com.tqs.filemanager.model

data class FileEntity(
    var id: Int = 0,
    var path: String? = "",
    var size: Int = 0,
    var name: String? = "",
    var mimeType: String? = "",
    var date: Long = 0L,
    var fileType: Int = 1,
    var isTitle: Boolean = false,
    var selected: Boolean = false,
    var deleted: Boolean = false
) {


}
