# 1,常用命令

## 1.1 目录操作

| **命令** | **描述**                     | **语法** | **介绍**         |
| -------- | ---------------------------- | :------: | ---------------- |
| cd       | 切换目录  (change directory) |  cd  ~   | 当前用户目录     |
|          |                              |  cd  /   | 根目录           |
|          |                              |   cd -   | 上一次访问的目录 |
|          |                              |  cd ..   | 上一级目录       |
|          |                              |    cd    | 缺省当前用户目录 |

> 缺省是个啥意思

 

| **命令** | **描述**                    | **参数** |            **介绍**            |
| -------- | --------------------------- | :------: | :----------------------------: |
| mkdir    | 创建目录  (make directoriy) |    -p    | 父目录不存在情况下先生成父目录 |
|          |                             |    -v    |  显示命令执行过程中的详细信息  |

>  这两个命令可以联合使用 

 

| **命令** | **描述**                                | **参数** | **介绍** |
| -------- | --------------------------------------- | -------- | -------- |
| pwd      | 显示工作目录  (print working directory) |          |          |
|          |                                         |          |          |

> 

## 1.2 查看操作

| **命令** | **描述**             | **语法**  | **介绍**                       |
| -------- | -------------------- | :-------: | ------------------------------ |
| ls       | 列出目录内容  (list) |   ls -l   | 使用详细格式列表，简化成**ll** |
|          |                      |   ls -a   | 所有文件和目录                 |
|          |                      | ll /home/ | 显示指定目录下的内容           |

> ls -a 显示 出来的 效果很奇怪
>
> 

| **命令** | **描述**                                                   | **语法** | **介绍**             |
| -------- | ---------------------------------------------------------- | :------: | -------------------- |
| grep     | 用于过滤/搜索的特定字符  (Global Regular Expression Print) |    -c    | 符号条件记录数       |
|          |                                                            |    -n    | 符合行的列数编号     |
|          |                                                            |    -i    | 忽略字符大小写的差别 |

> 检索 方式
>
> grep格式1：其他命令 | grep –i 查询条件
>
> ls -l | grep -i -n .md
>
> grep格式2：grep –i 查询条件 文件路径
>
> grep -i create /root/springBoot全局属性.md  // 查询当前文件中的所有包含 create 的句子

## 1.3 解压与压缩操作

| **命令** | **描述**                 | **参数** | **介绍**                            |
| -------- | ------------------------ | :------: | ----------------------------------- |
| tar      | 对压缩文件解压或压缩操作 |    -c    | 建立一个压缩文件的参数指令--压缩    |
|          |                          |    -x    | 解开一个压缩文件的参数指令 -- 解压  |
|          |                          |    -z    | 是否需要用 gzip 压缩                |
|          |                          |    -v    | 压缩的过程中显示文件                |
|          |                          |    -f    | 使用档名，在 f 之后要立即使用文件名 |

> ___ 解压 tar -zxvf 
>
> 格式：
>
>   tar -zxvf 解压文件名
>
> 实例：
>
>   tar -zxvf jdk-8u181-linux-x64.tar.gz

> ___ 压缩 tar -zcvf 
>
> 格式：tar -zcvf 压缩后文件名 需要压缩的目录
>
> 实例：
>
>   tar -zcvf jdk-8.tar.gz ./jdk1.8.0_181/

## 1.4 移动操作

| **命令** | **描述**                   | **语法** | **介绍**             |
| -------- | -------------------------- | -------- | -------------------- |
| mv       | 移动或更名现有的文件或目录 | -f       | 覆盖已有的文件或目录 |



| **命令** | **描述**               | **语法** | **介绍**                                         |
| -------- | ---------------------- | :------: | ------------------------------------------------ |
| cp       | 复制文件或目录  (copy) |    -b    | 删除覆盖目标文件之前的备份                       |
|          |                        |    -f    | 强行复制文件或目录，不论目标文件或目录是否已存在 |
|          |                        |    -i    | 对源文件建立硬连接，而非复制文件                 |
|          |                        |    -r    | 递归处理，将指定目录下的文件与子目录一并处理     |



| **命令** | **描述**                 | **语法** | **介绍**                                         |
| -------- | ------------------------ | -------- | ------------------------------------------------ |
| rm       | 删除文件或目录  (remove) | -f       | 强制删除文件或目录                               |
|          |                          | -r       | 递归处理，将指定目录下的所有文件及子目录一并处理 |

## 1.5  编辑vi 或 vim 命令



![image-20200103142013705](D:\temp\xmind\linux\linux命令小结\image-20200103142013705.png)