// learnNum为姓名  username为学号
<template>
  <el-main>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableData" border stripe>
      <el-table-column prop="learnNum" label="name"> </el-table-column>
      <el-table-column prop="username" label="student ID"> </el-table-column>
      <el-table-column prop="className" label="class"> </el-table-column>
      <el-table-column prop="idCard" label="ID card"> </el-table-column>
      <el-table-column prop="username" label="name"> </el-table-column>
      <el-table-column prop="phone" label="phone"> </el-table-column>
      <el-table-column prop="entryYear" label="entry year"> </el-table-column>
      <el-table-column prop="sex" label="sex" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.sex == '0'">man</el-tag>
          <el-tag v-if="scope.row.sex == '1'">woman</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="operation" width="300px" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="small"
            @click="editBtn(scope.row)"
            icon="el-icon-edit"
            >edit</el-button
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
    <!-- 新增编辑弹框 -->
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
              <el-form-item label="ID card" prop="idCard">
                <el-input v-model="addModel.idCard"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0"
              ><el-form-item label="password" prop="password">
                <el-input v-model="addModel.password"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="enter year">
                <el-input v-model="addModel.entryYear"></el-input>
              </el-form-item>
            </el-col>
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
  getByReaderIdApi
} from "@/api/reader";
import SysDialog from "@/components/dialog/SysDialog.vue";
import http from "@/utils/http";
import { getUserId } from "@/utils/auth";

export default {
  //注册组件
  components: {
    SysDialog,
  },
  data() {
    return {
      readerParm: {
        readerId: getUserId(),
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
      //表单属性
      addModel: {
        editType: "",
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
      //表格数据
      tableData: [],
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
          { required: true, message: "please enter ID card", trigger: "blur" },
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
    this.getReaderInfo();
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  methods: {
      async getReaderInfo() {
      let res = await getByReaderIdApi(this.readerParm);
      console.log(res.data);
    //   this.addModel.learnNum = res.data.learnNum;
    //   this.addModel.username = res.data.username;
    //   this.addModel.phone = res.data.phone;
    //   this.addModel.className = res.data.className;
    //   this.addModel.idCard = res.data.idCard;
    //   this.addModel.password = res.data.password;
    //   this.addModel.entryYear = res.data.entryYear;
    //   this.addModel.sex = res.data.sex;
    this.tableData = res.data;
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
    //弹框确定
    onClose() {
      this.dialog.visible = false;
    },
    //弹框关闭
    onConfirm() {
      this.$refs.addRef.validate(async (valid) => {
        if (valid) {
          let res = null;
          if (this.addModel.editType == "0") {
            res = await addReaderApi(this.addModel);
          } else {
            res = await editReaderApi(this.addModel);
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
    //编辑按钮
    editBtn(row) {
      this.dialog.title = "edit member";
      this.dialog.visible = true;
      //清空表单
      this.$resetForm("addRef", this.addModel);
      //把要编辑的数据复制到表单
      this.$objCoppy(row, this.addModel);
      //设置为编辑
      this.addModel.editType = "1";
    },
  },
};
</script>

<style lang="scss" scoped>
</style>