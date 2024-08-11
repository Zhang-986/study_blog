### Pojo 类与 SQL 数据库中的属性名称不一致

当你在 MyBatis 中使用 POJO 类与 SQL 数据库交互时，确实有可能出现 POJO 类中的属性名与 SQL 数据库中的列名不一致的情况。在这种情况下，MyBatis 需要知道如何将 POJO 类中的属性值映射到 SQL 语句中的相应列上。

### 映射过程

1. **SQL 语句中的 `#{}` 占位符**：
   - 在 SQL 语句中，`#{}` 用于指示 MyBatis 应该插入哪个参数值。
   - `#{}` 中的内容通常是 Java 方法中的参数名，或者是 POJO 类的属性名。
2. **参数传递**：
   - 当你调用 MyBatis 映射器接口的方法时，你传递的参数可以是原始类型（如 `int`, `String` 等），也可以是 POJO 类的对象。
   - 如果你传递的是 POJO 类的对象，那么 MyBatis 会根据 POJO 类的属性值来填充 SQL 语句中的 `#{}` 占位符。
3. **映射关系**：
   - 如果 POJO 类中的属性名与 SQL 数据库中的列名一致，MyBatis 会自动进行映射。
   - 如果不一致，你可以使用 SQL 语句中的 `AS` 关键字来显式指定映射关系，或者启用 `map-underscore-to-camel-case` 配置让 MyBatis 自动处理下划线命名风格与驼峰命名风格之间的映射。

### 示例

假设你有一个名为 `Dept` 的 POJO 类：

Java

浅色版本



```
1public class Dept {
2    private Integer id; // ID
3    private String name; // 部门名称
4    private LocalDateTime createTime; // 创建时间
5    private LocalDateTime updateTime; // 修改时间
6    // getters 和 setters
7}
```

假设数据库表 `project.dept` 的列名如下：

- `id` (整型)
- `name` (字符串)
- `create_time` (时间戳)
- `update_time` (时间戳)

### SQL 语句示例

Java

浅色版本



```
1@Mapper
2public interface DeptMapper {
3    @Select("select id, name, create_time as createTime, update_time as updateTime from project.dept")
4    List<Dept> list();
5
6    @Delete("delete from project.dept where id =#{id}")
7    void delete(Integer id);
8
9    @Insert("insert into project.dept (name, create_time, update_time) values (#{name},#{createTime},#{updateTime})")
10    void insert(Dept dept);
11}
```

### 解释

1. **`@Select`**：

   - `select id, name, create_time as createTime, update_time as updateTime from project.dept`：这里使用 `AS` 关键字来指定列名与 POJO 类属性名之间的映射关系。

2. **`@Delete`**：

   - `delete from project.dept where id =#{id}`：这里 `#{id}` 应该与你传递给 `delete` 方法的参数相匹配。

   - 调用示例

     ：

     Java

     浅色版本

     

     ```
     1deptMapper.delete(1);
     ```

3. **`@Insert`**：

   - `insert into project.dept (name, create_time, update_time) values (#{name},#{createTime},#{updateTime})`：这里 `#{name}`, `#{createTime}`, 和 `#{updateTime}` 应该与你传递给 `insert` 方法的 `Dept` 对象的属性相匹配。

   - 调用示例

     ：

     Java

     浅色版本

     

     ```
     1Dept dept = new Dept();
     2dept.setName("Sales Department");
     3dept.setCreateTime(LocalDateTime.now());
     4dept.setUpdateTime(LocalDateTime.now());
     5deptMapper.insert(dept);
     ```

### 总结

- **`#{}` 中的内容**：`#{}` 中的内容应该与你传递给 MyBatis 方法的参数相匹配。
- **参数类型**：可以是单独的原始类型（如 `int`, `Integer`, `String` 等），也可以是复杂对象（如 POJO 类）的属性。
- **映射关系**：如果 POJO 类中的属性名与 SQL 语句中的列名一致，那么可以直接使用属性名；如果不一致，可以使用 `AS` 关键字或启用 `map-underscore-to-camel-case` 配置来处理映射关系。