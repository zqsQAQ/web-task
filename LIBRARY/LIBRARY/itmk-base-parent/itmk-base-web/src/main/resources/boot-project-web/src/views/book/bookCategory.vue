<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="listParm" label-width="80px" :inline="true" size="small">
      <el-form-item>
        <el-input
          v-model="listParm.categoryName"
          placeholder="please enter category"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">search</el-button>
        <el-button icon="el-icon-close" @click="resetBtn">reset</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="addBtn"
          >add</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableData" border stripe>
      <el-table-column prop="categoryName" label="category"></el-table-column>
      <el-table-column prop="orderNum" label="order number"></el-table-column>
      <el-table-column label="operation" align="center" width="240">
        <template slot-scope="scope">
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="small"
            @click="editBtn(scope.row)"
            >edit</el-button
          >
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="small"
            @click="deleteBtn(scope.row)"
            >delete</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
      @size-change="sizeChange"
      @current-change="currentChange"
      :current-page.sync="listParm.currentPage"
      :page-sizes="[10, 20, 40, 80, 100]"
      :page-size="listParm.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="listParm.total"
      background
    >
    </el-pagination>
    <!-- 新增编辑弹框 -->
    <sys-dialog
      :title="dialog.title"
      :width="dialog.width"
      :height="dialog.height"
      :visible="dialog.visible"
      @onClose="onClose"
      @onConfirm="onConfirm"
    >
      <div slot="content">
        <el-form
          :model="addModel"
          ref="addRef"
          :rules="rules"
          label-width="130px"
          size="small"
        >
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="categoryName" label="category">
                <el-input v-model="addModel.categoryName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="order number">
                <el-input v-model="addModel.orderNum"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </sys-dialog>
  </el-main>
</template>

<script>
import SysDialog from "@/components/dialog/SysDialog.vue";
import { getListApi, addApi, editApi, deleteApi } from "@/api/category";
export default {
  components: {
    SysDialog,
  },
  data() {
    return {
      //表单验证规则
      rules: {
        categoryName: [
          {
            trigger: "blur",
            required: true,
            message: "please enter category name",
          },
        ],
      },
      //表单绑定的属性
      addModel: {
        type: "", //0新增 1编辑
        categoryId: "",
        categoryName: "",
        orderNum: "",
      },
      //弹框属性
      dialog: {
        width: 630,
        height: 150,
        title: "",
        visible: false,
      },
      tableHeight: 0,
      //表数据
      tableData: [],
      //列表查询参数
      listParm: {
        currentPage: 1,
        pageSize: 10,
        categoryName: "",
        total: 0,
      },
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  created() {
    this.getList();
  },
  methods: {
    //弹框关闭
    onClose() {
      this.dialog.visible = false;
    },
    //弹框确定
    onConfirm() {
      this.$refs.addRef.validate(async (valid) => {
        //表单验证
        if (valid) {
          let res = null;
          if (this.addModel.type == "0") {
            res = await addApi(this.addModel);
          } else {
            res = await editApi(this.addModel);
          }
          if (res && res.code == 200) {
            //信息提示
            this.$message({ type: "success", message: res.msg });
            //刷新表格
            this.getList();
            //关闭弹框
            this.dialog.visible = false;
          }
        }
      });
    },
    //获取列表（表格）
    async getList() {
      let res = await getListApi(this.listParm);
      if (res && res.code == 200) {
        console.log(res);
        this.tableData = res.data.records;
        this.listParm.total = res.data.total;
      }
    },
    //当前页数改变时
    currentChange(val) {
        this.listParm.currentPage = val;
        this.getList();
    },
    //页容量改变时
    sizeChange(val) {
        this.listParm.pageSize = val;
        this.getList();
    },
    //编辑按钮
    editBtn(row) {
      //设置弹框属性
      this.dialog.title = "edit category";
      this.dialog.visible = true;
      //清空表单
      this.$resetForm("addRef", this.addModel);
      //复制
      this.$objCoppy(row, this.addModel);
      //设为编辑
      this.addModel.type = "1";
    },
    //删除按钮
    async deleteBtn(row) {
      //信息确认
      const confirm = await this.$myconfirm(
        "Are you sure to delete this data?"
      );
      if (confirm) {
        let res = await deleteApi({ categoryId: row.categoryId });
        //信息提示
        this.$message({ type: "success", message: res.msg });
        //刷新表格
        this.getList();
      }
    },
    //搜索按钮
    searchBtn() {
        this.getList();
    },
    //重置按钮
    resetBtn() {
        this.listParm.categoryName = "";
        this.getList();
    },
    //新增按钮
    addBtn() {
      //设置弹框属性
      this.dialog.title = "add category";
      this.dialog.visible = true;
      //清空表单
      this.$resetForm("addRef", this.addModel);
      //设为新增
      this.addModel.type = "0";
    },
  },
};
</script>

<style lang="scss" scoped>
</style>