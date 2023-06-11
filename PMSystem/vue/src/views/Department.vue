<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入部门名" suffix-icon="el-icon-search" v-model="dname"></el-input>
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
      <el-upload action="http://localhost:9090/department/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" class="ml-5" @click="exp">导出<i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="序号" width="100">
      </el-table-column>
      <el-table-column prop="dname" label="部门名" width="150">
      </el-table-column>
      <el-table-column prop="member" label="员工名" width="150">
      </el-table-column>
      <el-table-column prop="position" label="职位" width="150">
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

    <el-dialog title="部门人员信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" size="small">
        <el-form-item label="部门名">
          <el-input v-model="form.dname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="员工名">
          <el-input v-model="form.member" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="职位">
          <el-input v-model="form.position" autocomplete="off"></el-input>
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
  name: "Department",
  data() {
    return {
      tableData: [],
      pageNumber: 1,
      pageSize: 10,
      total: 0,
      dname: "",
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
      axios.post("http://localhost:9090/department", this.form).then(res => {
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
      request.delete("/department/" + id).then(res => {
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
      request.post("/department/del/batch", ids).then(res => {
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
      this.dname = "";
      this.load();
    },
    load() {
      request.get("/department/page", {
        params: {
          pageNumber: this.pageNumber,
          pageSize: this.pageSize,
          dname: this.dname
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
      window.open("http://localhost:9090/department/export");
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