# SQL脚本维护工具-flyway插件使用说明
## 1.SQL脚本文件命名规范
SQL脚本文件命名规范为：V版本号-脚本简单描述.sql
例如：V10-权限管理.sql
下一个脚本文件命名必须为：V11-描述.sql，并保持顺序依次递增，避免版本号重复，否则会报错。
## 2.执行的命令
cd cloud-book-ticket目录

测试环境命令：mvn flyway:repair flyway:migrate -P test

注意：
flyway:repair命令说明：当执行命令出现validation异常时，修改完SQL脚本后再执行命令时，此命令必须要加，其他时候可选。
-P xxx说明：指明环境参数。不加时默认local本地环境
## 3.修改已push到远程仓库的SQL脚本原则上是禁止的，防止其他开发者已经执行过mvn migrate命令了。如果确实需要修改，需要确认是否已经被开发者执行过命令。

# 12306证书安装
密码 changeit
```shell
sudo keytool -import -noprompt -trustcacerts -alias 12306.cn.cer -keystore /Library/Java/JavaVirtualMachines/jdk1.8.0_231.jdk/Contents/Home/jre/lib/security/cacerts -file 12306.cn.cer
```
```shell
sudo keytool -import -file 12306.cn.cer -keystore 12306.cn.cer.keystore
```

