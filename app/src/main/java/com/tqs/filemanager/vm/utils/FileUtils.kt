package com.tqs.filemanager.vm.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.tqs.filemanager.model.FileEntity


object FileUtils {
    private var mContentResolver: ContentResolver? = null
    private var mCursor: Cursor? = null

    val TYPEIMAGE = 1
    val TYPEVIDEO = 2
    val TYPEAUDIO = 3
    val TYPEDOCUMENT = 4
    val TYPEDOWNLOAD = 5
    private fun getContentProvider(context: Context) {
        if (mContentResolver == null) {
            mContentResolver = context.contentResolver
        }
    }

    fun getImgList(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DISPLAY_NAME
        )


        return getFileList(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_MODIFIED + "  desc",
            TYPEIMAGE
        )
    }

    fun getVideoList(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        mCursor = mContentResolver?.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            "date_modified desc"
        )
        return getFileList(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Video.Media.DATE_MODIFIED + " desc",TYPEVIDEO)
    }

    fun getAudioList(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        return getFileList(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER,TYPEAUDIO)
    }


    fun getDocList(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        val selection = (MediaStore.Files.FileColumns.MIME_TYPE + "= ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? ")

        val selectionArgs = arrayOf(
            "text/plain",
            "application/msword",
            "application/pdf",
            "application/vnd.ms-powerpoint",
            "application/vnd.ms-excel",
            "text/html",
            "text/xml",
            "application/rtf"
        )

        return getFileList(
            MediaStore.Files.getContentUri("external"),
            null,
            selection,
            selectionArgs,
            MediaStore.Files.FileColumns.DATE_MODIFIED + "  desc",
            TYPEDOCUMENT
        )

    }

    fun getDownloadList(context: Context): ArrayList<FileEntity>? {
        getContentProvider(context)
        val selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ? "

        val selectionArgs = arrayOf(
            "application/vnd.android.package-archive"
        )
        return getFileList(
            MediaStore.Files.getContentUri("external"),
            null,
            selection,
            selectionArgs,
            MediaStore.Files.FileColumns.DATE_MODIFIED + "  desc",
            TYPEDOWNLOAD
        )
    }


    @SuppressLint("Range")
    private fun getFileList(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?,
        fileType: Int
    ): ArrayList<FileEntity> {
        mCursor = mContentResolver?.query(uri, projection, selection, selectionArgs, sortOrder)
        val fileEntities: MutableList<FileEntity> = ArrayList<FileEntity>()
        try {
            val mFiles: MutableList<String> = ArrayList()
            while (mCursor!!.moveToNext()) {
                val id = mCursor!!.getInt(mCursor!!.getColumnIndex(MediaStore.Audio.Media._ID))
                val path =
                    mCursor!!.getString(mCursor!!.getColumnIndex(MediaStore.Audio.Media.DATA))
                val size = mCursor!!.getInt(mCursor!!.getColumnIndex(MediaStore.Audio.Media.SIZE))
                val mimetype =
                    mCursor!!.getString(mCursor!!.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE))
                val dateAdd =
                    mCursor!!.getLong(mCursor!!.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED))
                if (!mFiles.contains(path)) {
                    mFiles.add(path)
                    val fileEntity = FileEntity()
                    fileEntity.id = id
                    fileEntity.path = path
                    fileEntity.size = size
                    if (path.isNotEmpty()) {
                        val olaName = path.substring(path.lastIndexOf("/") + 1, path.length)
                        fileEntity.name = olaName
                    }
                    fileEntity.mimeType = mimetype
                    fileEntity.date = dateAdd
                    fileEntity.fileType = fileType
                    fileEntities.add(fileEntity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mCursor?.close()
        }
        return fileEntities as ArrayList<FileEntity>
    }

}