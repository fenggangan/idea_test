package com.atguigu.mybatisplus;

import com.atguigu.mybatisplus.entity.User;
import com.atguigu.mybatisplus.mapper.UserMapper;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryWrapperTests {

    @Autowired
    UserMapper userMapper;
    /**
     * setSqlSelect	设置 SELECT 查询字段
     * where	WHERE 语句，拼接 + WHERE 条件
     * and	AND 语句，拼接 + AND 字段=值
     * andNew	AND 语句，拼接 + AND (字段=值)
     * or	OR 语句，拼接 + OR 字段=值
     * orNew	OR 语句，拼接 + OR (字段=值)
     * eq	等于=
     * allEq	基于 map 内容等于=
     * ne	不等于<>
     * gt	大于>
     * ge	大于等于>=
     * lt	小于<
     * le	小于等于<=
     * like	模糊查询 LIKE
     * notLike	模糊查询 NOT LIKE
     * in	IN 查询
     * notIn	NOT IN 查询
     * isNull	NULL 值查询
     * isNotNull	IS NOT NULL
     * groupBy	分组 GROUP BY
     * having	HAVING 关键词
     * orderBy	排序 ORDER BY
     * orderAsc	ASC 排序 ORDER BY
     * orderDesc	DESC 排序 ORDER BY
     * exists	EXISTS 条件语句
     * notExists	NOT EXISTS 条件语句
     * between	BETWEEN 条件语句
     * notBetween	NOT BETWEEN 条件语句
     * addFilter	自由拼接 SQL
     * last	拼接在最后，例如：last(“LIMIT 1”)
     */
    QueryWrapper<User> queryWrapper;
    UpdateWrapper<User> updateWrapper;

    @Before
    public void initQueryWrapper() {
        queryWrapper = new QueryWrapper<>();
        updateWrapper = new UpdateWrapper<>();

    }

    @Test
    public void testDelete() {
        //* ge	大于等于>=
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.
                isNull("name").ge("age", 12).isNotNull("email");
        int delete = userMapper.delete(queryWrapper);
        System.out.println(delete);
    }

    @Test
    public void testSelectOne() {
        //* eq	等于=
        queryWrapper.eq("name", "Tom");
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }

    @Test
    public void testSelectCount() {
        Integer count = userMapper.selectCount(queryWrapper.between("age", 0, 15));
        System.out.println(count);
    }

    @Test
    public void testSelectList() {
        //* allEq	基于 map 内容等于=
        HashMap<String, Object> map = new HashMap<>();
        map.put("age", 20);
        map.put("name", "Jack");
        map.put("id", 2L);
        List<User> list = userMapper.selectList(queryWrapper.allEq(map));
        list.forEach(System.out::println);
    }

    /**
     * like	模糊查询 LIKE
     * notLike	模糊查询 NOT LIKE
     */
    @Test
    public void testSelectMaps() {
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper.notLike("name", "e").likeRight("email", "t"));
        maps.forEach(System.out::println);
    }
//in、notIn、inSql、notinSql、exists、notExists**

    /**
     * in	IN 查询
     * notIn	NOT IN 查询
     */
    @Test
    public void testSelectObjs() {
        //queryWrapper.in("id",1,2,3);
        queryWrapper.inSql("id", "select id from user where id < 4");
        List<Object> objects = userMapper.selectObjs(queryWrapper);
        objects.forEach(System.out::println);
    }

    @Test
    public void testUpdate1() {
        User user = new User();
        user.setAge(99);
        user.setName("Andy");
        //修改条件
        updateWrapper.like("name", "J").or().between("age", 0, 300);
        int update = userMapper.update(user, updateWrapper);
        System.out.println(update);
    }

    @Test
    public void testUpdate2() {
//修改值
        User user = new User();
        user.setAge(99);
        user.setName("Andy");
        //* ne	不等于<>
        updateWrapper.like("name", "h").or(i -> i.eq("name", "李白").ne("age", 20));
        int update = userMapper.update(user, updateWrapper);
        System.out.println(update);
    }

    @Test
    public void testSelectListOrderBy() {
        queryWrapper.orderByDesc("id");
        List<User> list = userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void testSelectListLast() {
        queryWrapper.last("limit 0,2");
        List<User> list = userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void testSelectListColumn() {
        QueryWrapper<User> select = queryWrapper.select("id", "name");
        List<User> list = userMapper.selectList(select);
        list.forEach(System.out::println);
    }

    @Test
    public void testUpdateSet() {
        //修改值
        User user = new User();
        user.setAge(99);
        //修改条件
        updateWrapper
                .like("name", "h")
                .set("name", "老李头")//除了可以查询还可以使用set设置修改的字段
                .setSql(" email = '123@qq.com'");//可以有子查询

        int result = userMapper.update(user, updateWrapper);
    }


}
