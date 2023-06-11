package com.morningsun.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningsun.entity.Department;
import com.morningsun.service.IDepartmentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Resource
    private IDepartmentService departmentService;

    @PostMapping
    public boolean save(@RequestBody Department department){
        return departmentService.saveOrUpdate(department);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return departmentService.removeById(id);
    }

    @GetMapping
    public List<Department> findAll() {
        return departmentService.list();
    }

    @GetMapping("/{id}")
    public Department findOne(@PathVariable Integer id) {
        return departmentService.getById(id);
    }

    @GetMapping("/page")
    public Page<Department> findPage(@RequestParam Integer pageNumber,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String dname) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        if (!"".equals(dname)){
            queryWrapper.like("dname", dname);
        }
        return departmentService.page(new Page<>(pageNumber, pageSize), queryWrapper);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return departmentService.removeBatchByIds(ids);
    }

    /**
     * 导出
     * @param response
     * @throws Exception
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查数据
        List<Department> list = departmentService.list();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //定义标题别名
        writer.addHeaderAlias("id", "id");
        writer.addHeaderAlias("dname", "部门");
        writer.addHeaderAlias("member", "员工名");
        writer.addHeaderAlias("position", "职位");

        //一次性写出list到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset-utf-8");
        String fileName = URLEncoder.encode("部门信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    @PostMapping("/import")
    public boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
//        List<User> list = reader.readAll(User.class);
        List<List<Object>> list = reader.read(1);
        List<Department> departments = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Department department = new Department();
            department.setDname(row.get(1).toString());
            department.setMember(row.get(2).toString());
            department.setPosition(row.get(3).toString());
            departments.add(department);
        }
        departmentService.saveBatch(departments);
        return true;
    }
}
