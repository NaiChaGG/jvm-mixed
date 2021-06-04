package io.github.jojoti.examples.filecontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * Created by @JoJo Wang on 2016/4/13.
 *
 * @author JoJo Wang
 *
 */
@Controller
@RequestMapping("/file/upload")
public class FileController {

    static private final String UPLOAD_FILE_PATH;
    static private final String TEMP_POSTFIX = ".temp";

    static {
        //可以访问到文件地址，如webapps下新建一个repo目录作为访问的入口目录
        String path = FileController.class.getResource("/").getFile().toString();
        path = path.substring(0, path.length() - 1);
        path = path.substring(0, path.lastIndexOf("/"));
        path = path.substring(0, path.lastIndexOf("/"));
        path = path.substring(0, path.lastIndexOf("/"));
        path += "/attach/";
        UPLOAD_FILE_PATH = path;
        File $file = new File(UPLOAD_FILE_PATH);
        if (!$file.exists()) {
            $file.mkdir();
        }
    }

    /**
     * 表单上传文件，一次只支持一个文件
     */
    @RequestMapping(value = "/file/pub/upload/form", method = RequestMethod.POST)
    @ResponseBody
    @Deprecated
    public ResponseBodyEntity<String> uploadFormFile(@RequestParam CommonsMultipartFile formFile)
            throws IOException {
        // 保存文件信息，以后有需要再处理
        String OriginalFilename = formFile.getOriginalFilename();
        String $postfix = OriginalFilename.substring(OriginalFilename.lastIndexOf('.'));
        // 自定义文件名字，然后获取后缀
        String $newFileName = UUID.randomUUID().toString() + $postfix;
        String filePath = UPLOAD_FILE_PATH + $newFileName;

        formFile.transferTo(new File(filePath));
        // /attach/file/ 在server-repository-handler-config.xml配置的映射，映射到 ser_repo/uploadfiles目录下
//        httpServletResponse.setContentType ("text/html;charset=utf-8");
        return ResponseBodyEntity.ok("/attach/" + $newFileName);
    }

    /**
     * 断点上传, 创建一个新文件
     *
     * @param postfix
     * @param request
     * @return
     */
    @RequestMapping(value = "/file/pub/upload/access/open", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyEntity<String> uploadBreakpointOpen(@RequestParam String postfix, HttpServletRequest request)
            throws IOException {
        // 文件后缀
        String fileName = UUID.randomUUID().toString();
        String fullFilePath = UPLOAD_FILE_PATH + fileName + '.' + postfix + TEMP_POSTFIX;

        // 把前端传入的代码直接写到服务器零时文件
        try (InputStream inputStream = request.getInputStream();
             RandomAccessFile randomAccessFile = new RandomAccessFile(fullFilePath, "w")) {
            // 指针值到文件开头，估计默认就是在开头
            // randomAccessFile.seek (0);
            int len;                        //输入数组长度
            byte[] stepByte = new byte[1024];   //开辟空间，读取内容
            do {
                len = inputStream.read(stepByte);
                if (len > 0) {
                    randomAccessFile.write(stepByte);
                }
            } while (len >= 1024);
        }
        // 之后可以优化为流id来传输，这样不会暴露服务器文件细节
        return ResponseBodyEntity.ok(fileName);
    }

    /**
     * 继续推送文件下一个节点的流，幂等
     *
     * @param offset
     * @param fileName
     * @param request
     * @return
     */
    @RequestMapping(value = "/file/pub/upload/access/flush", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseBodyEntity<String> uploadBreakpointFlush(
            @RequestHeader(name = "Offset", defaultValue = "0") int offset
            , @RequestHeader(name = "Flush-Close", defaultValue = "0") byte flushClose
            , @RequestParam String fileName
            , HttpServletRequest request)
            throws IOException {
        if (offset > 0) {
            String fullFilePath = UPLOAD_FILE_PATH + fileName + TEMP_POSTFIX;
            // 把前端传入的代码直接写到服务器零时文件
            try (InputStream inputStream = request.getInputStream();
                 RandomAccessFile randomAccessFile = new RandomAccessFile(fullFilePath, "rw")) {
                // 文件末尾不是上次写入文件的末尾，error！！
//                _repoAssert.writeResponseBodyJsonMessage(randomAccessFile.length() >= offset, "file_offset_limit");
                randomAccessFile.seek(offset);
                int len;                        //输入数组长度
                byte[] stepByte = new byte[1024];   //开辟空间，读取内容
                do {
                    len = inputStream.read(stepByte);
                    if (len > 0) {
                        randomAccessFile.write(stepByte);
                    }
                    if (len < 1024) {
                        break;
                    }
                } while (true);
            }
            return ResponseBodyEntity.ok();
        }
        if (flushClose == 1) {
            String newFilePath = UPLOAD_FILE_PATH + fileName;
            File newFile = new File(newFilePath);
            // 需要重命名的文件以及存在，直接返回，幂等
            if (newFile.exists()) {
                return ResponseBodyEntity.ok("/attach/" + newFilePath);
            }
            String fullFilePath = newFilePath + TEMP_POSTFIX;
            File tempFile = new File(fullFilePath);

//            _repoAssert.writeResponseBodyJsonMessage(tempFile.exists(), "temp_file_not_found");
            // 重命名
            tempFile.renameTo(newFile);
            return ResponseBodyEntity.ok("/attach/" + newFilePath);
        }
        return ResponseBodyEntity.ok();
    }

    /**
     * @return
     */
    @RequestMapping(value = "/file/pub/download/{fileName:.+}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyEntity<Void> downloadBreakpoint(
            @RequestHeader String Range,
            @RequestHeader(defaultValue = "0") int offset
            , @PathVariable String fileName) {

        // 断点下载 以后再实现了。
        // 之后修改成支持 Chunked 方式来做。 1 2 3 4 ... -> 0 就关闭。表示结束。
        // http://blog.csdn.net/javaminer/article/details/16181653
        // https://www.byvoid.com/blog/http-keep-alive-header
        // http://www.cnblogs.com/jcli/archive/2012/10/19/2730440.html
        // http://www.tuicool.com/articles/IBrUZj
        // http://blog.csdn.net/smstong/article/details/7211593
        Range.replaceAll("bytes=", "").replaceAll("-", "");
//        skip = StringUtils.toInt (Range);

        return ResponseBodyEntity.VOID;
    }
}