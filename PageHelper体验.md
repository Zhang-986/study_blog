PageHelper 是一个 MyBatis 插件，它的主要功能就是在 MyBatis 执行 SQL 查询之前自动添加 LIMIT 子句来实现分页。具体来说，PageHelper 的工作流程如下：

1. **初始化分页参数**:
   - 使用 `PageHelper.startPage(page, pageSize)` 方法初始化分页参数，这里的 `page` 是当前页数，`pageSize` 是每页显示的记录数量。
2. **执行查询**:
   - 接下来执行正常的 MyBatis 查询方法，例如 `empMapper.list(name, gender, begin, end)`。PageHelper 会在执行查询之前自动修改 SQL 语句，添加 LIMIT 子句来限制返回的记录数。
3. **获取分页结果**:
   - 查询完成后，PageHelper 会将查询结果封装成一个分页对象，这个对象包含了分页信息（如总记录数、当前页的数据等）。可以通过 `Page<Emp>` 类型的对象来获取这些信息。

### 示例代码解析

```
1@Override
2public PageBean page(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end) {
3    PageHelper.startPage(page, pageSize); // 初始化分页参数
4
5    List<Emp> list = empMapper.list(name, gender, begin, end); // 执行查询
6
7    Page<Emp> p = (Page<Emp>) list; // 获取分页结果
8
9    PageBean pageBean = new PageBean(p.getTotal(), list); // 创建自定义的分页对象
10    return pageBean;
11}
```



### 工作流程

1. **初始化分页参数**:
   - `PageHelper.startPage(page, pageSize)` 设置了当前页数和每页的记录数。
2. **执行查询**:
   - 调用 `empMapper.list(name, gender, begin, end)` 来执行查询。由于 PageHelper 已经设置了分页参数，因此 MyBatis 会在执行 SQL 之前自动添加 LIMIT 子句来限制返回的记录数。
3. **获取分页结果**:
   - 查询完成后，`list` 实际上已经被 PageHelper 封装成了一个分页对象。通过 `(Page<Emp>) list` 可以获取到分页信息。
4. **创建自定义的分页对象**:
   - 使用分页对象中的总记录数 `p.getTotal()` 和查询结果 `list` 来创建自定义的 `PageBean` 对象。

### 总结

PageHelper 的主要功能就是自动在 SQL 查询前添加 LIMIT 子句来实现分页，使得开发人员无需关心具体的分页 SQL 语法，也无需在每个查询方法中手动处理分页逻辑。这大大简化了分页查询的实现过程。