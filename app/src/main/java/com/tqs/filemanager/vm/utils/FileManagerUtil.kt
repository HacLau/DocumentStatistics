package com.tqs.filemanager.vm.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.tqs.filemanager.model.FileEntity
import java.io.File


object FileManagerUtil {
    private var mContentResolver: ContentResolver? = null
    private var mCursor: Cursor? = null
    private fun getContentProvider(context: Context) {
        if (mContentResolver == null) {
            mContentResolver = context.contentResolver
        }
    }

    fun getImgList(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        mCursor = mContentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?",
            arrayOf("image/jpeg", "image/png"),
            "date_modified desc"
        )
        return getFileList(1)
    }

    fun getVideoList(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        mCursor = mContentResolver?.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null,
            MediaStore.Video.Media.MIME_TYPE + "= ? or " + MediaStore.Video.Media.MIME_TYPE + "= ?",
            arrayOf("video/mp4", "video/flv"),
            "date_modified desc"
        )
        return getFileList(2)
    }

    fun getAudioList(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        mCursor = mContentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )
        return getFileList(3)
    }

    @SuppressLint("Range")
    private fun getFileList(fileType: Int): ArrayList<FileEntity> {
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

    fun getDocList(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        mCursor = mContentResolver?.query(
            MediaStore.Files.getContentUri("external"),
            arrayOf("_id", "_data", "_size", "_display_name", "mime_type", "date_added"),
            MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " + MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " + MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " + MediaStore.Files.FileColumns.MIME_TYPE + "= ?  or " + MediaStore.Files.FileColumns.MIME_TYPE + "= ? ",
            arrayOf(
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/pdf"
            ),
            "date_modified desc"
        )
        return getFileList(4)

    }

    fun getDownloadList(context: Context): ArrayList<FileEntity>? {
        getContentProvider(context)
        mCursor = mContentResolver?.query(
            MediaStore.Files.getContentUri("external"),
            arrayOf("_id", "_data", "_size", "_display_name", "mime_type", "date_added"),
            MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " + MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " + MediaStore.Files.FileColumns.MIME_TYPE + "= ? or " + MediaStore.Files.FileColumns.MIME_TYPE + "= ?  or " + MediaStore.Files.FileColumns.MIME_TYPE + "= ? ",
            arrayOf(
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/pdf"
            ),
            "date_modified desc"
        )
        return getFileList(5)
    }


}