package com.qydcos.controller;

import com.qydcos.common.ApiResult;
import com.qydcos.common.QYException;
import com.qydcos.common.ResultCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.qydcos.common.ResultCode.FILE_NOT_EXISTED;
import static com.qydcos.common.ResultCode.FILE_TYPE_NOT_SUPPORTED;

/**
 * @author AlanFan
 */
@RestController
public class FileController {

    @Value("${file.repo}")
    String fileRepo;

    @Value("${file.type}")
    String[] fileType;

    private HashMap<String, String> nameMap;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping
    public ApiResult home() {
        List<QYFile> data = new ArrayList<>();
        List<File> files = listFiles();
        files.forEach(file -> {
            QYFile qyFile = new QYFile(file.getName(), getNameMap().get(file.getName()),
                    sdf.format(file.lastModified()),
                    String.format("/download/%s", file.getName()), file.length());
            data.add(qyFile);
        });

        return ApiResult.SUCCESS(data);
    }

    @GetMapping(path = "download/{name:.+}")
    public void handleDownload(@PathVariable String name, HttpServletResponse response) throws Exception {
        List<File> files = listFiles();
        files.forEach(file -> {
            if (file.getName().equals(name)) {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/octet-stream;charset=utf-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + name);
                try (InputStream inputStream = new FileInputStream(fileRepo + "/" + name);
                     OutputStream out = response.getOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    while ((len = inputStream.read(buffer, 0, 1024)) != -1) {
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        throw new QYException(ApiResult.ERROR(FILE_NOT_EXISTED));
    }

    @PostMapping(path = "upload")
    public ApiResult handleUpload(@RequestParam("file") MultipartFile file) throws Exception {
        ApiResult apiResult = new ApiResult();
        if (!file.isEmpty()) {
            String suffix = getSuffix(file.getOriginalFilename());
            if (!Arrays.asList(fileType).contains(getSuffix(file.getOriginalFilename()))) {
                Map<String, String[]> map = new HashMap<>();
                map.put("support", fileType);
                apiResult.setData(map);
                apiResult.setResultCode(FILE_TYPE_NOT_SUPPORTED);
                throw new QYException(apiResult);
            }
            String originalName = file.getOriginalFilename();
            String newName = UUID.randomUUID().toString() + "." + suffix;
            String filePath = fileRepo + "/" + newName;
            try (InputStream inputStream = file.getInputStream();
                 FileOutputStream fos = new FileOutputStream(filePath)) {
                byte[] b = new byte[1024];
                while ((inputStream.read(b)) != -1) {
                    fos.write(b);
                }

                Map<String, String> url = new HashMap<>(1);
                url.put("url", String.format("/download/%s", newName));
                apiResult.setData(url);
                apiResult.setResultCode(ResultCode.SUCCESS);

                //flush db
                flushNameMap(newName, originalName);
            } catch (IOException e) {
                throw new QYException(ApiResult.ERROR(ResultCode.FILE_SAVE_ERROR, e.getMessage()));
            }
        } else {
            throw new QYException(ApiResult.ERROR(ResultCode.FILE_IS_EMPTY));
        }
        return apiResult;
    }

    private String getSuffix(String fileName) {
        if (fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        }
        return null;
    }

    private HashMap<String, String> getNameMap() {
        if (this.nameMap == null) {
            synchronized (this) {
                if (this.nameMap == null) {
                    String dbPath = fileRepo + "/db";
                    try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(dbPath))) {
                        this.nameMap = (HashMap<String, String>) objectInputStream.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.nameMap = new HashMap<>();
                    }
                }
            }
        }
        return this.nameMap;
    }

    private void flushNameMap(String newName, String originalName) {
        this.getNameMap().put(newName, originalName);
        String dbPath = fileRepo + "/db";
        try (ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(dbPath))) {
            oout.writeObject(this.nameMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<File> listFiles() {
        try {
            File file = new File(fileRepo);
            File[] files = file.listFiles();
            List<File> names = new ArrayList<>();
            for (int i = 0; files != null && i < files.length; i++) {
                if (files[i].isFile() && !files[i].getName().equals("db")) {
                    names.add(files[i]);
                }
            }
            return names;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static class QYFile {
        private String name;
        private String originName;
        private String time;
        private String url;
        private long size;

        public QYFile(String name, String originName, String time, String url, long size) {
            this.name = name;
            this.originName = originName;
            this.time = time;
            this.url = url;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOriginName() {
            return originName;
        }

        public void setOriginName(String originName) {
            this.originName = originName;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }
}
