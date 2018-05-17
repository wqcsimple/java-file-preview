package com.whis.app.controller

import com.whis.app.utils.FileUtils
import com.whis.base.common.DataResponse
import com.whis.base.exception.BaseException
import com.whis.base.exception.NotExistsException
import com.whis.base.exception.WrongParamException
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File

import javax.servlet.http.HttpServletRequest
import java.util.HashMap
import java.nio.file.StandardCopyOption
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@RestController("FileController")
@RequestMapping("/file")
@Controller
class FileController {

    @Autowired lateinit var fileUtils: FileUtils

    @Value("\${file.upload.root}")
    private val fileRoot: String = ""

    private fun getFileUploadPath(): String {
        return fileRoot + "/file" + File.separator
    }

    private fun getFilePath(fileName: String): Path {
        return Paths.get(fileRoot, "file", fileName)
    }

    @RequestMapping(value = ["file-upload"], method = [(RequestMethod.POST)])
    fun fileUpload(@RequestParam("file") file: MultipartFile,
                   request: HttpServletRequest): DataResponse {
        val response = DataResponse.create()

        if (file.isEmpty) {
            throw WrongParamException("file is empty")
        }
        val fileName = file.originalFilename  // 文件原始名称

        // 判断该文件类型是否有上传过，如果上传过则提示不允许再次上传
//        if (existsTypeFile(fileName)) {
//            throw WrongParamException("每一种类型只可以上传一个文件，请先删除原有文件再次上传")
//        }

        val outFile = File(getFileUploadPath())
        if (!outFile.exists()) {
            outFile.mkdirs()
        }

        try {
            val fileName = DigestUtils.md5Hex(file.inputStream) + "." + fileUtils.getSuffixFromFileName(file.originalFilename)
            Files.copy(file.inputStream, getFilePath(fileName), StandardCopyOption.REPLACE_EXISTING)

            val fileDataMap = HashMap<String, Any>()
            fileDataMap["name"] = fileName
            response.put("file", fileDataMap)

        } catch (e: IOException) {
            e.printStackTrace()
            throw BaseException(-1, "文件上传失败 ")
        } catch (e: RuntimeException) {
            e.printStackTrace()
            throw BaseException(-1, "文件上传失败 ")
        }

        return response
    }

    @RequestMapping("/delete")
    fun deleteFile(@RequestParam("filename") filename: String): DataResponse {
        val file = File(getFileUploadPath() + filename)
        if (file.exists()) {
            file.delete()
        }

        return DataResponse.create()
    }

    @RequestMapping("/list")
    fun getFileList(): DataResponse {
        val list = arrayListOf<Map<String, Any>>()
        val file = File(getFileUploadPath())
        if (file.exists()) {
            file.listFiles().forEach { f ->
                list.add(
                        hashMapOf(
                                "filename" to f.name
                        )
                )
            }
        }
        return DataResponse.create().put("file_list", list)
    }

    /**
     * 是否存在该类型的文件
     * @return
     * @param fileName
     */
    private fun existsTypeFile(fileName: String): Boolean {
        var result = false
        val suffix = fileUtils.getSuffixFromFileName(fileName)
        val file = File(getFileUploadPath())
        if (file.exists()) {
            file.listFiles().forEach { f ->
                val existsFileSuffix = fileUtils.getSuffixFromFileName(f.name)
                if (suffix == existsFileSuffix) {
                    result = true
                    return@forEach
                }
            }
        }

        return result
    }
}
