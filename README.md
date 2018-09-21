# HTTP文件服务器
此项目通过HTTP协议提供上传、下载文件功能。

## 环境变量配置说明
| 名称 | 默认值 |  描述|
| -----|-----|-----|
| **FILE_REPO**| /repo | 文件上传到服务器默认存放地址 |
| **FILE_TYPE** | jar,war | 支持上传的文件类型，值以`,`分隔 |
| **MAX_FILE_SIZE** | 100MB | 上传文件的最大值，例如10KB、10MB |
| **JAVA_OPTS** | | JVM相关运行参数，例如值为 `-Xms2048m -Xmx2048m -Xss512k -Dfoo=bar` |

## 构建Docker镜像
执行命令 `mvn clean package docker:build`，会产生一个名为`qybe/http-file:latest`的镜像。

## 运行Docker容器
执行命令 `docker run -d --name my-http-file -p 8800:8800 qybe/http-file:latest`

## 项目访问
**1.** `http://localhost:8800/` 列出服务器上的文件列表<br>
**2.**  `http://localhost:8800/upload` 上传文件到服务器<br>
`curl`命令上传示例 `curl -X POST \
              http://localhost:8800/upload \
              -H 'cache-control: no-cache' \
              -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
              -F file=@/Users/foo/bar/hello.war`<br>
**3.** `http://localhost:8800/download/foo.jar` 下载名为`foo.jar`的文件

