<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入姓名" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定要批量删除吗?"
          @confirm="delBatch"
          class="ml-5"
      >
        <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <el-upload action="http://localhost:9090/employee/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" class="ml-5" @click="exp">导出<i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="序号" width="50">
      </el-table-column>
      <el-table-column prop="department" label="部门" width="80">
      </el-table-column>
      <el-table-column prop="name" label="姓名" width="80">
      </el-table-column>
      <el-table-column prop="attdays" label="本月应出勤天数" width="110">
      </el-table-column>
      <el-table-column prop="realattdays" label="实际出勤" width="80">
      </el-table-column>
      <el-table-column prop="basesalary" label="基本工资" width="80">
      </el-table-column>
      <el-table-column prop="allowance" label="岗位津贴" width="80">
      </el-table-column>
      <el-table-column prop="meal" label="午餐补贴" width="80">
      </el-table-column>
      <el-table-column prop="overtime" label="加班工资" width="80">
      </el-table-column>
      <el-table-column prop="fullmoney" label="全勤工资" width="80">
      </el-table-column>
      <el-table-column prop="security" label="扣社保" width="80">
      </el-table-column>
      <el-table-column prop="accumulation" label="扣公积金" width="80">
      </el-table-column>
      <el-table-column prop="taxation" label="扣税" width="80">
      </el-table-column>
      <el-table-column prop="otherdeductions" label="迟到等情况" width="100">
      </el-table-column>
      <el-table-column prop="actualhair" label="实发" width="80">
      </el-table-column>
      <el-table-column prop="operation" label="操作">
        <template slot-scope="scope">
          <el-button type="success" @click="edit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗?"
              @confirm="remove(scope.row.id)"
          >
            <el-button type="danger" slot="reference" class="ml-5">删除<i class="el-icon-remove"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="员工信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="150px" size="small">
        <el-form-item label="部门">
          <el-input v-model="form.department" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="本月应出勤天数">
          <el-input v-model="form.attdays" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="实际出勤">
          <el-input v-model="form.realattdays" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="基本工资">
          <el-input v-model="form.basesalary" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="岗位津贴">
          <el-input v-model="form.allowance" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="午餐补贴">
          <el-input v-model="form.meal" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="加班工资">
          <el-input v-model="form.overtime" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="全勤工资">
          <el-input v-model="form.fullmoney" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="扣社保">
          <el-input v-model="form.security" autocomplete="off" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="扣公积金">
          <el-input v-model="form.accumulation" autocomplete="off" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="扣税">
          <el-input v-model="form.taxation" autocomplete="off" disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="迟到等情况">
          <el-input v-model="form.otherdeductions" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="实发">
          <el-input v-model="form.actualhair" autocomplete="off" disabled="true"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNumber"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import request from "@/utils/request";

export default {
  name: "Employee",
  data() {
    return {
      tableData: [],
      pageNumber: 1,
      pageSize: 10,
      total: 0,
      name: "",
      dialogFormVisible: false,
      form: {},
      headerBg: 'headerBg',
      multipleSelection: []
    }
  },
  created() {
    this.load();
  },
  methods: {
    handleSelectionChange(val) {
      console.log(val);
      this.multipleSelection = val;
    },
    save() {
      var totalmoney = parseFloat(this.form.basesalary) +
                       parseFloat(this.form.allowance) +
                       parseFloat(this.form.meal) +
                       parseFloat(this.form.overtime) +
                       parseFloat(this.form.fullmoney) ;
      this.form.accumulation = 0 - totalmoney * 0.12;
      this.form.security = 0 - totalmoney * 0.11;
      if (totalmoney <= 3000) {
        this.form.taxation = 0 - totalmoney * 0.03;
      }else if (totalmoney > 3000 && totalmoney <= 12000){
        this.form.taxation = 0 - 90 - (totalmoney - 3000) * 0.1;
      }else if (totalmoney > 12000 && totalmoney <= 25000){
        this.form.taxation = 0 - 90 - 900 - (totalmoney - 12000) * 0.2;
      }else if (totalmoney > 25000 && totalmoney <= 35000){
        this.form.taxation = 0 - 90 - 900 - 2600 - (totalmoney - 25000) * 0.25;
      }else if (totalmoney > 35000 && totalmoney <= 55000){
        this.form.taxation = 0 - 90 - 900 - 2600 - 2500 - (totalmoney - 35000) * 0.3;
      }else if (totalmoney > 55000 && totalmoney <= 80000){
        this.form.taxation = 0 - 90 - 900 - 2600 - 2500 - 6000 - (totalmoney - 55000) * 0.35;
      }else if (totalmoney > 80000){
        this.form.taxation = 0 - 90 - 900 - 2600 - 2500 - 6000 - 8750 - (totalmoney - 80000) * 0.45;
      }
      this.form.actualhair = parseFloat(this.form.basesalary) +
                             parseFloat(this.form.allowance) +
                             parseFloat(this.form.meal) +
                             parseFloat(this.form.overtime) +
                             parseFloat(this.form.fullmoney) +
                             parseFloat(this.form.security) +
                             parseFloat(this.form.accumulation) +
                             parseFloat(this.form.taxation) +
                             parseFloat(this.form.otherdeductions);
      axios.post("http://localhost:9090/employee", this.form).then(res => {
        if (res) {
          this.$message.success("修改成功");
          this.dialogFormVisible = false;
          this.load();
        }else {
          this.$message.error("修改失败");
        }
      })
    },
    edit(row) {
      this.form = row;
      this.dialogFormVisible = true;
    },
    remove(id) {
      request.delete("/employee/" + id).then(res => {
        if (res) {
          this.$message.success("删除成功");
          if (this.pageSize * this.pageNumber > this.total){
            this.pageNumber--;
          }
          this.load();
        }else {
          this.$message.error("删除失败");
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true;
      this.form = {};
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id);
      request.post("/employee/del/batch", ids).then(res => {
        if (res) {
          this.$message.success("批量删除成功");
          if (this.pageSize * this.pageNumber > this.total){
            this.pageNumber--;
          }
          this.load();
        }else {
          this.$message.error("批量删除失败");
        }
      })
    },
    reset() {
      this.name = "";
      this.load();
    },
    load() {
      request.get("/employee/page", {
        params: {
          pageNumber: this.pageNumber,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        console.log(res);
        this.tableData = res.records;
        this.total = res.total;
      })

    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNumber) {
      this.pageNumber = pageNumber;
      this.load();
    },
    exp() {
      window.open("http://localhost:9090/employee/export");
    },
    handleExcelImportSuccess() {
      this.$message.success("文件导入成功");
      this.load();
    }
  }
}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>