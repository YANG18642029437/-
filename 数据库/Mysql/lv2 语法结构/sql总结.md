## 1, 不能在修改或是删除时使用删除表作为 select 条件

>在select 的时候嵌套一层子查询就行了  
>
>SELECT AVG FROM (SELECT AVG(score) AVG FROM teacher t1,course c1,sc s1 WHERE t1.tname = '叶平' AND c1.tid = t1.tid AND s1.cid = c1.cid)s1

## 2，查询添加一个 索引

```mysql
SELECT 
# 每次循环索引就加一
(@i:=@i+1) id,s1.* 
FROM (SELECT snum,AVG(score)av FROM sc GROUP BY snum ORDER BY av DESC) s1 , 
# 添加 一个 索引表 用内连接添加到 查询表中
(SELECT @i:=0) s2 ;
```

## 3，子查询的时候 将主表的数据传递过去

```mysql
SELECT * FROM sc s 
WHERE 3 > 
(SELECT COUNT(*) FROM sc WHERE cnum = s.`Cnum` AND score > s.`score`) #获取到传递的参数
ORDER BY s.`Cnum` , s.`score` DESC;
```

## 4，简单函数 and  搜索函数

> 简单函数
> `CASE [col_name] WHEN [value1] THEN [result1]…ELSE [default] END`
>
> 搜索函数
> `CASE WHEN [expr] THEN [result1]…ELSE [default] END`

> 简单函数

> CASE [col_name] WHEN [value1] THEN [result1]…ELSE [default] END： 枚举这个字段所有可能的值*

```mysql
SELECT
	NAME '英雄',
	CASE NAME
		WHEN '德莱文' THEN
			'斧子'
		WHEN '德玛西亚-盖伦' THEN
			'大宝剑'
		WHEN '暗夜猎手-VN' THEN
			'弩'
		ELSE
			'无'
	END '装备'
FROM
	user_info;
```

> 搜索函数

> CASE WHEN [expr] THEN [result1]…ELSE [default] END：搜索函数可以写判断，并且搜索函数只会返回第一个符合条件的值，其他case被忽略

```mysql
# when 表达式中可以使用 and 连接条件
SELECT
	NAME '英雄',
	age '年龄',
	CASE
		WHEN age < 18 THEN
			'少年'
		WHEN age < 30 THEN
			'青年'
		WHEN age >= 30
		AND age < 50 THEN
			'中年'
		ELSE
			'老年'
	END '状态'
FROM user
```

> 聚合函数 sum 配合 case when 的简单函数实现多表 left join 的行转列

> 注：曾经有个爱学习的路人问我，“那个sum()只是为了好看一点吗？”，left join会以左表为主，连接右表时，得到所有匹配的数据，再group by时只会保留一行数据，因此case when时要借助sum函数，保留其他列的和。如果你还是不明白的话，那就亲手实践一下，只保留left join看一下结果，再group by，看一下结果。例如下面的案例：学生表/课程表/成绩表 ，三个表left join查询每个学生所有科目的成绩，使每个学生及其各科成绩一行展示。

```mysql
SELECT
	st.stu_id '学号',
	st.stu_name '姓名',
	sum(
		CASE co.course_name
		WHEN '大学语文' THEN
			sc.scores
		ELSE
			0
		END
	) '大学语文',
	sum(
		CASE co.course_name
		WHEN '新视野英语' THEN
			sc.scores
		ELSE
			0
		END
	) '新视野英语',
	sum(
		CASE co.course_name
		WHEN '离散数学' THEN
			sc.scores
		ELSE
			0
		END
	) '离散数学',
	sum(
		CASE co.course_name
		WHEN '概率论与数理统计' THEN
			sc.scores
		ELSE
			0
		END
	) '概率论与数理统计',
	sum(
		CASE co.course_name
		WHEN '线性代数' THEN
			sc.scores
		ELSE
			0
		END
	) '线性代数',
	sum(
		CASE co.course_name
		WHEN '高等数学' THEN
			sc.scores
		ELSE
			0
		END
	) '高等数学'
FROM
	edu_student st
LEFT JOIN edu_score sc ON st.stu_id = sc.stu_id
LEFT JOIN edu_courses co ON co.course_no = sc.course_no
GROUP BY
	st.stu_id
ORDER BY
NULL
```

