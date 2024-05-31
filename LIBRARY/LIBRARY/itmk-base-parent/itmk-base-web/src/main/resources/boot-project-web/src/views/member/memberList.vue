// learnNum为姓名  username为学号
<template>
  <el-main>
    <!-- 搜素栏 -->
    <el-form :model="listParm" :inline="true" size="small">
      <el-form-item>
        <el-input
          placeholder="please enter studentID"
          v-model="listParm.username"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
          placeholder="please enter phone"
          v-model="listParm.phone"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
          placeholder="please enter name"
          v-model="listParm.learnNum"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
          placeholder="please enter email"
          v-model="listParm.idCard"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
          placeholder="please enter entry year"
          v-model="listParm.entryYear"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">search</el-button>
        <el-button icon="el-icon-close" @click="resetBtn">reset</el-button>
        <el-button type="primary" @click="addBtn" icon="el-icon-plus"
          >add</el-button
        >
        <el-button icon="el-icon-delete" @click="deleteYearBtn" type="danger"
          >delete</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableData" border stripe>
      <el-table-column prop="learnNum" label="name"> </el-table-column>
      <el-table-column prop="username" label="student ID"> </el-table-column>
      <el-table-column prop="className" label="class"> </el-table-column>
      <el-table-column prop="idCard" label="email"> </el-table-column>
      <el-table-column prop="username" label="username"> </el-table-column>
      <el-table-column prop="phone" label="phone"> </el-table-column>
      <el-table-column prop="entryYear" label="entry year"> </el-table-column>
      <el-table-column prop="sex" label="sex" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.sex == '0'">man</el-tag>
          <el-tag v-if="scope.row.sex == '1'">woman</el-tag>
        </template>
      </el-table-column>
      <!-- <el-table-column prop="checkStatus" label="audit status">
        <template slot-scope="scope"> -->
      <!-- 0未审核    1已审核 -->
      <!-- <el-tag v-if="scope.row.checkStatus == '0'" type="danger">no</el-tag>
          <el-tag v-if="scope.row.checkStatus == '1'">yes</el-tag>
        </template>
      </el-table-column> -->
      <!-- <el-table-column prop="userStatus" label="user status" align="center">
        <template slot-scope="scope"> -->
      <!-- 0停用    1启用 -->
      <!-- <el-tag v-if="scope.row.userStatus == '0'" type="danger">no</el-tag>
          <el-tag v-if="scope.row.userStatus == '1'">yes</el-tag>
        </template>
      </el-table-column> -->
      <el-table-column label="operation" width="300px" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="small"
            @click="editBtn(scope.row)"
            icon="el-icon-edit"
            >edit</el-button
          >
          <!-- <el-button
            icon="el-icon-edit"
            type="primary"
            size="small"
            @click="applyBtn(scope.row)"
            >audit</el-button
          > -->
          <el-button
            type="danger"
            size="small"
            @click="deleteBtn(scope.row)"
            icon="el-icon-delete"
            >delete</el-button
          >
          <el-button
            type="success"
            size="small"
            icon="el-icon-eleme"
            @click="codeBtn(scope.row)"
            >Code</el-button
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
    <!-- 新增弹框 -->
    <sys-dialog
      :title="dialog.title"
      :height="dialog.height"
      :width="dialog.width"
      :visible="dialog.visible"
      @onClose="onClose"
      @onConfirm="onConfirm"
    >
      <div slot="content">
        <el-form
          :model="addModel"
          ref="addRef"
          :rules="rules"
          label-width="120px"
          size="small"
          style="margin-right: 50px"
        >
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="name" prop="learnNum">
                <el-input v-model="addModel.learnNum"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="student ID" prop="username">
                <el-input v-model="addModel.username"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="phone" prop="phone">
                <el-input v-model="addModel.phone"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="class" prop="className">
                <el-input v-model="addModel.className"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="email" prop="idCard">
                <el-input v-model="addModel.idCard"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="entry year">
                <el-input v-model="addModel.entryYear"></el-input>
              </el-form-item>
            </el-col>
            <!-- <el-col :span="12" :offset="0"
              ><el-form-item label="password" prop="password">
                <el-input v-model="addModel.password"></el-input>
              </el-form-item>
            </el-col> -->
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="sex">
                <el-radio-group v-model="addModel.sex">
                  <el-radio :label="0">man</el-radio>
                  <el-radio :label="1">woman</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </sys-dialog>
    <!-- 编辑弹框 -->
    <sys-dialog
      :title="edialog.title"
      :height="edialog.height"
      :width="edialog.width"
      :visible="edialog.visible"
      @onClose="eonClose"
      @onConfirm="eonConfirm"
    >
      <div slot="content">
        <el-form
          :model="editModel"
          ref="editRef"
          :rules="erules"
          label-width="120px"
          size="small"
          style="margin-right: 50px"
        >
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="name" prop="learnNum">
                <el-input v-model="editModel.learnNum"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="student ID" prop="username">
                <el-input v-model="editModel.username"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="phone" prop="phone">
                <el-input v-model="editModel.phone"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="class" prop="className">
                <el-input v-model="editModel.className"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="email" prop="idCard">
                <el-input v-model="editModel.idCard"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0"
              ><el-form-item label="password" prop="password">
                <el-input v-model="editModel.password"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="entry year">
                <el-input v-model="editModel.entryYear"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="sex">
                <el-radio-group v-model="editModel.sex">
                  <el-radio :label="0">man</el-radio>
                  <el-radio :label="1">woman</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </sys-dialog>
    <!-- 条形码弹框 -->
    <sys-dialog
      :title="codeDialog.title"
      :height="codeDialog.height"
      :width="codeDialog.width"
      :visible="codeDialog.visible"
      @onClose="codeOnClose"
      @onConfirm="codeOnConfirm"
    >
      <div slot="content" align="center">
        <img :src="this.barCode" />
        {{ this.content }}
      </div>
    </sys-dialog>
    <!-- 删除指定年份所有学生的弹出框 -->
    <sys-dialog
      :title="yearDialog.title"
      :width="yearDialog.width"
      :height="yearDialog.height"
      :visible="yearDialog.visible"
      @onClose="yearOnClose"
      @onConfirm="yearOnConfirm"
    >
      <div slot="content">
        <el-form
          :model="yearParm"
          ref="delRef"
          label-width="80px"
          :inline="false"
          size="normal"
        >
          <el-form-item label="entry year">
            <el-input
              v-model="yearParm.entryYear"
              placeholder="please enter entryYear"
              ></el-input
            >
          </el-form-item>
        </el-form>
      </div>
    </sys-dialog>
  </el-main>
</template>

<script>
import {
  getListApi,
  addReaderApi,
  editReaderApi,
  deleteReaderApi,
  applyReaderApi,
  deleteGraduatesApi,
} from "@/api/reader";
import SysDialog from "@/components/dialog/SysDialog.vue";
import http from "@/utils/http";
export default {
  //注册组件
  components: {
    SysDialog,
  },
  data() {
    return {
      edialog: {
        title: "",
        width: 640,
        height: 200,
        visible: false,
      },
      barCode: "",
      content: "",
      //条形码弹框属性
      codeDialog: {
        height: 120,
        width: 500,
        title: "barcode",
        visible: false,
      },
      //新增表单属性
      addModel: {
        readerId: "",
        learnNum: "",
        username: "",
        idCard: "",
        sex: "",
        phone: "",
        type: "",
        className: "", //班级名
        entryYear: "",
      },
      //编辑表单属性
      editModel: {
        readerId: "",
        learnNum: "",
        username: "",
        idCard: "",
        sex: "",
        phone: "",
        password: "",
        type: "",
        className: "", //班级名
        entryYear: "",
      },
      //弹框属性
      dialog: {
        title: "",
        width: 640,
        height: 200,
        visible: false,
      },
      //删除指定年份学生弹框属性
      yearDialog: {
        title: "delete students of this year",
        width: 640,
        height: 60,
        visible: false,
      },
      //删除指定年份学生
      yearParm: {
        entryYear: "",
      },
      //表格高度
      tableHeight: 0,
      //表格数据
      tableData: [],
      //列表查询的参数
      listParm: {
        total: 0,
        currentPage: 1,
        pageSize: 10,
        username: "",
        idCard: "",
        phone: "",
        learnNum: "",
        entryYear: "",
      },
      rules: {
        learnNum: [
          {
            required: true,
            message: "please enter name",
            trigger: "blur",
          },
        ],
        username: [
          {
            required: true,
            message: "please enter student ID",
            trigger: "blur",
          },
        ],
        idCard: [
          { required: true, message: "please enter email", trigger: "blur" },
        ],
        phone: [
          {
            required: true,
            message: "please enter phone number",
            trigger: "blur",
          },
        ],
        // password: [
        //   { required: true, message: "please enter password", trigger: "blur" },
        // ],
        className: [
          { required: true, message: "please enter class", trigger: "blur" },
        ],
      },
      erules: {
        learnNum: [
          {
            required: true,
            message: "please enter name",
            trigger: "blur",
          },
        ],
        username: [
          {
            required: true,
            message: "please enter student ID",
            trigger: "blur",
          },
        ],
        idCard: [
          { required: true, message: "please enter email", trigger: "blur" },
        ],
        phone: [
          {
            required: true,
            message: "please enter phone number",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "please enter password", trigger: "blur" },
        ],
        className: [
          { required: true, message: "please enter class", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  methods: {
    deleteYearBtn() {
      this.$resetForm("delRef", this.yearParm);
      this.yearDialog.visible = true;
    },
    yearOnClose() {
      this.yearDialog.visible = false;
    },
    async yearOnConfirm() {
      let confirm = await this.$myconfirm(
        "Are you sure to delete all students of this year?"
      );
      if (confirm) {
        let res = await deleteGraduatesApi({
          entryYear: this.yearParm.entryYear,
        });
        console.log(res);
        if (res && res.code == 200) {
          this.$message.success(res.msg);
        } else {
          this.$message({ type: "error", message: res.msg });
        }
      }
      this.getList();
      this.yearDialog.visible = false;
    },
    //展示条形码 按钮
    async codeBtn(row) {
      //进入时 先清空条形码链接数组
      this.codeDialog.visible = true;
      //调用公共条形码接口  生成条形码
      // console.log("...........................");
      console.log(row);
      this.content = row.username;
      console.log(this.content);
      // barcode[i] = await getBarCodeApi()
      let res = await http.get("https://www.mxnzp.com/api/barcode/create", {
        content: this.content,
        width: "500",
        height: "100",
        type: "0",
        app_id: "8dqilgo2oixxcnnj",
        app_secret: "RElTVC9wZlRMdkNmRWJZb0tFZDEwQT09",
      });
      // console.log("wwwwwwwwwwwwwwwwwwwwwwwwwwww")
      // console.log(res);
      this.barCode = res.data.barCodeUrl;
    },
    //条形码弹框关闭
    codeOnClose() {
      this.codeDialog.visible = false;
    },
    //条形码弹框确定
    codeOnConfirm() {
      this.codeDialog.visible = false;
    },
    //审核按钮
    async applyBtn(row) {
      let confirm = await this.$myconfirm("Are you sure to audit?");
      if (confirm) {
        console.log(row);
        let res = await applyReaderApi({ readerId: row.readerId });
        if (res && res.code == 200) {
          this.$message.success(res.msg);
          //刷新表格
          this.getList();
        }
      }
    },
    //弹框确定
    onClose() {
      this.dialog.visible = false;
    },
    //弹框关闭
    onConfirm() {
      this.$refs.addRef.validate(async (valid) => {
        if (valid) {
          let res = await addReaderApi(this.addModel);
          if (res && res.code == 200) {
            //信息提示
            this.$message({ type: "success", message: res.msg });
            //刷新表格
            this.getList();
            //关闭弹框
            this.dialog.visible = false;
          }else {
            this.$message.error(res.msg)
          }
        }
      });
    },
    eonClose() {
      this.edialog.visible = false;
    },
    eonConfirm() {
      this.$refs.editRef.validate(async (valid) => {
        if (valid) {
          let res = await editReaderApi(this.editModel);
          if (res && res.code == 200) {
            //信息提示
            this.$message({ type: "success", message: res.msg });
            //刷新表格
            this.getList();
            //关闭弹框
            this.edialog.visible = false;
          }else {
            this.$message.error(res.msg)
          }
        }
      });
    },
    //页容量改变时
    sizeChange(val) {
      this.listParm.pageSize = val;
      this.getList();
    },
    //当前页数改变时
    currentChange(val) {
      this.listParm.currentPage = val;
      this.getList();
    },
    //列表
    async getList() {
      let res = await getListApi(this.listParm);
      if (res && res.code == 200) {
        console.log(res);
        this.tableData = res.data.records;
        this.listParm.total = res.data.total;
      }
    },
    //编辑按钮
    editBtn(row) {
      this.edialog.title = "edit member";
      this.edialog.visible = true;
      //清空表单dialog
      this.$resetForm("editRef", this.editModel);
      //把要编辑的数据复制到表单
      this.$objCoppy(row, this.editModel);
    },
    //删除按钮
    async deleteBtn(row) {
      let confirm = await this.$myconfirm("Are you sure to delete this data?");
      if (confirm) {
        let res = await deleteReaderApi({ readerId: row.readerId });
        if (res && res.code == 200) {
          //信息提示
          this.$message({ type: "success", message: res.msg });
          //刷新表格
          this.getList();
        } else {
          this.$message.error(res.msg);
        }
      }
    },
    //搜索按钮
    searchBtn() {
      this.getList();
    },
    //重置按钮
    resetBtn() {
      this.listParm.username = "";
      this.listParm.learnNum = "";
      this.listParm.idCard = "";
      this.listParm.phone = "";
      this.listParm.entryYear = "";
      this.getList();
    },
    //新增按钮
    addBtn() {
      this.dialog.visible = true;
      this.dialog.title = "add member";
      //清空表单
      this.$resetForm("addRef", this.addModel);
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
