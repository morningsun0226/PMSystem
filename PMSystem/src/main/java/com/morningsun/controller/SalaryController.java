package com.morningsun.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningsun.entity.Salary;
import com.morningsun.service.ISalaryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/salary")
public class SalaryController {

    @Resource
    private ISalaryService salaryService;

    @PostMapping
    public boolean save(@RequestBody Salary salary){
        return salaryService.saveOrUpdate(salary);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return salaryService.removeById(id);
    }

    @GetMapping
    public List<Salary> findAll() {
        return salaryService.list();
    }

    @GetMapping("/{id}")
    public Salary findOne(@PathVariable Integer id) {
        return salaryService.getById(id);
    }

    @GetMapping("/page")
    public Page<Salary> findPage(@RequestParam Integer pageNumber,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String startTime,
                                   @RequestParam(defaultValue = "") String endTime) {
        QueryWrapper<Salary> queryWrapper = new QueryWrapper<>();
        if (!"".equals(startTime) && !"".equals(endTime)){
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String dateTimeStr = startTime;
            LocalDateTime sdateTime = LocalDateTime.parse(dateTimeStr, df);
            dateTimeStr = endTime;
            LocalDateTime edateTime = LocalDateTime.parse(dateTimeStr, df);
            queryWrapper.between("paytime", startTime, endTime);
        }
        return salaryService.page(new Page<>(pageNumber, pageSize), queryWrapper);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return salaryService.removeBatchByIds(ids);
    }

    /**
     * 导出
     * @param response
     * @throws Exception
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查数据
        List<Salary> list = salaryService.list();
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //定义标题别名
        writer.addHeaderAlias("id", "id");
        writer.addHeaderAlias("owner", "所得人");
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
        writer.addHeaderAlias("paytime", "发放时间");

        //一次性写出list到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset-utf-8");
        String fileName = URLEncoder.encode("历史工资信息", "UTF-8");
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
        List<Salary> salaries = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Salary salary = new Salary();
            salary.setOwner(row.get(1).toString());
            salary.setBasesalary(Double.parseDouble(row.get(2).toString()));
            salary.setAllowance(Double.parseDouble(row.get(3).toString()));
            salary.setMeal(Double.parseDouble(row.get(4).toString()));
            salary.setOvertime(Double.parseDouble(row.get(5).toString()));
            salary.setFullmoney(Double.parseDouble(row.get(6).toString()));
            salary.setSecurity(Double.parseDouble(row.get(7).toString()));
            salary.setAccumulation(Double.parseDouble(row.get(8).toString()));
            salary.setTaxation(Double.parseDouble(row.get(9).toString()));
            salary.setOtherdeductions(Double.parseDouble(row.get(10).toString()));
            salary.setActualhair(Double.parseDouble(row.get(11).toString()));
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateTimeStr = row.get(12).toString();
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, df);
            salary.setPaytime(dateTime);
            salaries.add(salary);
        }
        salaryService.saveBatch(salaries);
        return true;
    }
}
