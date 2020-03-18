# JDBC基础
## &emsp;&emsp;—— 利用反射及JDBC元数据编写通用的查询方法

### 为什么要用反射？

上一章，我们使用PreparedStatement执行查询数据库操作，但是却存在缺陷，我们这一期就解决这些问题

那么有哪些问题呢？
1. JDBCUtils的select()方法缺乏普适性，不再是通用的查询方法
2. 如果将结果集ResultSet的放到调用层解析那么就存在耦合的现象，我们需要解耦
3. 如果不解析，那么我们可能无法得到对象，而是一个结果集，我们需要面向对象


结合上面的问题，提炼需要完成的任务要素：
1. 解耦合
2. 通用的查询方法
3. 面向对象
4. 调用层无需过多解析

那么我们开始吧~

## 一、数据库的准备

执行以下SQL，生成数据库和数据表并注入数据

```
/*MySQL - 5.6.12 : Database - test*/

/*创建test数据库*/
CREATE DATABASE `test`;
/*使用test数据库*/
USE `test`;
/*创建student表*/
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `student_name` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '学生姓名',
  `student_sex` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '学生性别',
  `student_number` int(11) DEFAULT NULL COMMENT '学生学号',
  `school` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '所属学校',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*向student表添加数据*/
insert  into `student`(`id`,`student_name`,`student_sex`,`student_number`,`school`) values (1,'浅夏','女',2147483647,'南方大学'),(2,'樟道','男',2147483647,'北方大学'),(3,'苏熙','女',2147483647,'东方大学');
/*创建user表*/
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(10) COLLATE utf8_bin NOT NULL COMMENT '姓名',
  `password` varchar(150) COLLATE utf8_bin DEFAULT NULL COMMENT '密码',
  `age` int(10) NOT NULL COMMENT '年龄',
  `remark` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*向user表添加数据*/
insert  into `user`(`id`,`name`,`password`,`age`,`remark`) values (1,'樟道','admin',21,'这是一个活泼的男孩'),(2,'浅夏','admin',19,'这是一个乐观的女孩'),(3,'苏熙','admin123',19,'这是一个可爱的美少女'),(4,'江芯','admin123',23,'这是一个丽江的姑娘');
```

<hr>

## 二、Java项目

### 实现步骤
1. 在编写SQL时，如果返回的数据表字段名和对象属性名不一致，采取字段名后加别名(属性名)的形式编写SQL，例如：`SELECT student_name studentName, school FROM ...`
2. 先利用SQL进行查询，得到结果集(之前的查询部分还是没有问题的，只是对ResultSet结果集解析上的问题)
3. 利用反射创建目标实体类的对象(过程中要保证通用代码中不能出现目标对象的实例代码)
4. 获取结果集的列的别名：如SQL语句返回的字段名或别名(这里获取的优先别名)
5. 再获取结果集的每一列的值，结合第4步得到一个Map，键：列的别名，值：列的值；例如：`studentName:浅夏`
6. 再利用反射为第3步中目标对象对应的属性赋值，属性即为Map的键，值即为Map的值

<br>

### 分析

### ReflectionUtils

这个比较难懂，这里我们先分析一下setFieldValueByParem()方法的参数：

1. 参数1：Object object。这是被目标类反射的Object实例，例如：`Object object = User.class.newInstance();`。我们将要用object来代替目标类，实现属性值被赋值的操作
2. 参数2：需要被赋值的对象属性名
3. 参数3：需要赋值给目标对象属性名的属性值

&emsp;&emsp;这个类的主要作用就是调用setFieldValueByParem()将属性值赋值到object中的属性名对应的属性上，这时的object其实就是目标类的反射(你可以理解为套着Object外皮的目标类)。到时我们再将object强制转化回目标类就可以了

&emsp;&emsp;至于具体的功能实现，代码里的注释足够详细，想要看懂，需要了解Object类、Class类的方法，建议查询JavaEE的API规范。

<br>

### 运行结果

```
C:\develop\Java\jdk1.8.0_181\bin\java.exe -ea -Didea.test.cyclic.buffer.size=1048576 "-javaagent:C:\develop\JetBrains\IntelliJ IDEA 2018.2.5\lib\idea_rt.jar=63486:C:\develop\JetBrains\IntelliJ IDEA 2018.2.5\bin" -Dfile.encoding=UTF-8 -classpath "C:\develop\JetBrains\IntelliJ IDEA 2018.2.5\lib\idea_rt.jar;C:\develop\JetBrains\IntelliJ IDEA 2018.2.5\plugins\junit\lib\junit-rt.jar;C:\develop\JetBrains\IntelliJ IDEA 2018.2.5\plugins\junit\lib\junit5-rt.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\charsets.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\deploy.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\ext\access-bridge-64.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\ext\cldrdata.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\ext\dnsns.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\ext\jaccess.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\ext\jfxrt.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\ext\localedata.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\ext\nashorn.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\ext\sunec.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\ext\sunjce_provider.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\ext\sunmscapi.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\ext\sunpkcs11.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\ext\zipfs.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\javaws.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\jce.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\jfr.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\jfxswt.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\jsse.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\management-agent.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\plugin.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\resources.jar;C:\develop\Java\jdk1.8.0_181\jre\lib\rt.jar;D:\java_project\T-jdbc-demo\target\test-classes;D:\java_project\T-jdbc-demo\target\classes;C:\develop\Maven\apache-maven-repository\org\springframework\spring-core\5.2.0.RELEASE\spring-core-5.2.0.RELEASE.jar;C:\develop\Maven\apache-maven-repository\org\springframework\spring-jcl\5.2.0.RELEASE\spring-jcl-5.2.0.RELEASE.jar;C:\develop\Maven\apache-maven-repository\org\springframework\spring-beans\5.2.0.RELEASE\spring-beans-5.2.0.RELEASE.jar;C:\develop\Maven\apache-maven-repository\org\springframework\spring-context\5.2.0.RELEASE\spring-context-5.2.0.RELEASE.jar;C:\develop\Maven\apache-maven-repository\org\springframework\spring-aop\5.2.0.RELEASE\spring-aop-5.2.0.RELEASE.jar;C:\develop\Maven\apache-maven-repository\org\springframework\spring-expression\5.2.0.RELEASE\spring-expression-5.2.0.RELEASE.jar;C:\develop\Maven\apache-maven-repository\org\springframework\spring-jdbc\5.2.0.RELEASE\spring-jdbc-5.2.0.RELEASE.jar;C:\develop\Maven\apache-maven-repository\org\springframework\spring-tx\5.2.0.RELEASE\spring-tx-5.2.0.RELEASE.jar;C:\develop\Maven\apache-maven-repository\mysql\mysql-connector-java\5.1.26\mysql-connector-java-5.1.26.jar;C:\develop\Maven\apache-maven-repository\junit\junit\4.12\junit-4.12.jar;C:\develop\Maven\apache-maven-repository\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar" com.intellij.rt.execution.junit.JUnitStarter -ideVersion5 -junit4 com.tqazy.test.TestJdbc,testJdbc
User{id=null, name='苏熙', password='admin123', age=19, remark='这是一个可爱的美少女'}
Student{id=null, studentName='苏熙', studentSex='女', studentNumber=2147483647, school='东方大学'}

Process finished with exit code 0
```

### 分析

&emsp;&emsp;我们可以看到结果就是完成了我们预期的需求，其中解耦合，ServiceImpl调用JDBCUtils方法时，JDBCUtils类里没有其他的类的代码，且调用层无需解析JDBCUtils的代码，只需要强转一下即可，也完成了调用层无需过多解析的需求。

&emsp;&emsp;我们在代码里查询了两个表的数据，使用的时同一个select()的方法，所以此方法具备通用性；且返回的不在是原来的ResultSet结果集，而是返回的Object对象，又满足了面向对象的需求。

&emsp;&emsp;所以我们使用映射完成了通用的查询方法
