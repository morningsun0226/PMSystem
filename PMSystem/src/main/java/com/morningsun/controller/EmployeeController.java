package com.morningsun.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningsun.entity.Employee;
import com.morningsun.service.IEmployeeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private IEmployeeService employeeService;

    @PostMapping
    public boolean save(@RequestBody Employee employee){
        return employeeService.saveOrUpdate(employee);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return employeeService.removeById(id);
    }

    @GetMapping
    public List<Employee> findAll() {
        return employeeService.list();
    }

    @GetMapping("/{id}")
    public Employee findOne(@PathVariable Integer id) {
        return employeeService.getById(id);
    }

    @GetMapping("/page")
    public Page<Employee> findPage(@RequestParam Integer pageNumber,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)){
            queryWrapper.like("name", name);
        }
        return employeeService.page(new Page<>(pageNumber, pageSize), queryWrapper);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return employeeService.removeBatchByIds(ids);
    }

    /**
     * 导出
     * @param response
     * @throws Exception
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查数据
        List<Employee> list = employeeService.list();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //定义标题别名
        writer.addHeaderAlias("id", "id");
        writer.addHeaderAlias("department", "部门");
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("attdays", "本月应出勤天数");
        writer.addHeaderAlias("realattdays", "实际出勤");
        writer.addHeaderAlias("basesalary", "基本工资");
        writer.addHeaderAlias("allowance", "岗位津贴");
        writer.addHeaderAlias("meal", "午餐补贴");
        writer.addHeaderAlias("overtime", "加班工资");
        writer.addHeaderAlias("fullmoney", "全勤工资");
        writer.addHeaderAlias("security", "扣社保");
        writer.addHeaderAlias("accumulation", "扣公积金");
        writer.addHeaderAlias("taxation", "扣税");
        writer.addHeaderAlias("otherdeductions", "迟到等情况");
        writer.addHeaderAlias("actualhair", "实发");

        //一次性写出list到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset-utf-8");
        String fileName = URLEncoder.encode("员工信息", "UTF-8");
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
        List<Employee> employees = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Employee employee = new Employee();
            employee.setDepartment(row.get(1).toString());
            employee.setName(row.get(2).toString());
            employee.setAttdays(Integer.parseInt(row.get(3).toString()));
            employee.setRealattdays(Integer.parseInt(row.get(4).toString()));
            employee.setBasesalary(Double.parseDouble(row.get(5).toString()));
            employee.setAllowance(Double.parseDouble(row.get(6).toString()));
            employee.setMeal(Double.parseDouble(row.get(7).toString()));
            employee.setOvertime(Double.parseDouble(row.get(8).toString()));
            employee.setFullmoney(Double.parseDouble(row.get(9).toString()));
            employee.setSecurity(Double.parseDouble(row.get(10).toString()));
            employee.setAccumulation(Double.parseDouble(row.get(11).toString()));
            employee.setTaxation(Double.parseDouble(row.get(12).toString()));
            employee.setOtherdeductions(Double.parseDouble(row.get(13).toString()));
            employee.setActualhair(Double.parseDouble(row.get(14).toString()));
            employees.add(employee);
        }
        employeeService.saveBatch(employees);
        return true;
    }
}
