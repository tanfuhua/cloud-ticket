# 代码库模板说明
通过iCode创建代码库后建议进行如下操作：
1. POM文件中修改：
    1. 实际的`groupId`和`artifactId`
    2. 实际的工程描述`description`
    3. 实际的工程版本`version`
    4. 实际的工程依赖，`dependencyManagement`和`dependencies`部分
    5. 实际的编译过程，`build`和`profiles`部分
2. 包名修改：
    1. Java包名不能有"`-`"等特殊字符，建议结合实际情况修改
    2. `src/main/java`和`src/test/java`中都需要修改
3. 编译命令修改：
    1. 修改`ci.yml`中`command`内容
    2. 如需使用特殊的env，请在`command`中加入export命令

**以下为README模板，请参照填写！！！**

## SQL脚本维护工具-flyway插件使用说明
### 1.SQL脚本文件命名规范
SQL脚本文件命名规范为：V版本号-脚本简单描述.sql
例如：V10-权限管理.sql
下一个脚本文件命名必须为：V11-描述.sql，并保持顺序依次递增，避免版本号重复，否则会报错。
### 2.执行的命令
cd cloud-book-ticket目录

测试环境命令：mvn flyway:repair flyway:migrate -P test

注意：
flyway:repair命令说明：当执行命令出现validation异常时，修改完SQL脚本后再执行命令时，此命令必须要加，其他时候可选。
-P xxx说明：指明环境参数。不加时默认local本地环境
### 3.修改已push到远程仓库的SQL脚本原则上是禁止的，防止其他开发者已经执行过mvn migrate命令了。如果确实需要修改，需要确认是否已经被开发者执行过命令。

# baidu-box-web
简要说明

## 快速开始
如何构建、安装、运行

## 测试
如何执行自动化测试

