<template>
  <el-main>
    <!-- 搜索栏 
        model : 绑定表单的数据，通常是一个对象
        ref : 相当于div的id
        rules : 表单验证的规则
        inline : 是否在一行显示
        -->
    <el-form
      :model="listParm"
      ref="searchRef"
      label-width="80px"
      :inline="true"
      size="normal"
    >
      <el-form-item>
        <el-input
          v-model="listParm.nickName"
          placeholder="please enter your name"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
          v-model="listParm.phone"
          placeholder="please enter your phone"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn" size="small">search</el-button>
        <el-button icon="el-icon-close" @click="resetBtn" size="small">reset</el-button>
        <el-button type="primary" @click="addBtn" icon="el-icon-plus" size="small"
          >add</el-button
        >
      </el-form-item>
    </el-form>

    <!-- 表格 
        data : 表格的数据
        -->
    <el-table :height="tableHeight" :data="tableData" border stripe>
      <el-table-column prop="nickName" label="name"></el-table-column>
      <el-table-column prop="phone" label="phone"></el-table-column>
      <el-table-column prop="email" label="email"></el-table-column>
      <el-table-column label="operation" align="center" width="360">
        <!-- https://www.csdn.net/tags/MtTaIgwsNjgwMDUxLWJsb2cO0O0O.html  
              slot-scope="scope"的解释  取得作用域插槽:data绑定的数据
        -->
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="small"
            @click="editBtn(scope.row)"
            icon="el-icon-edit"
            >edit</el-button
          >
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

    <!-- 弹框 -->
    <sys-dialog
      :title="dialog.title"
      :visible="dialog.visible"
      :width="dialog.width"
      :height="dialog.height"
      @onClose="onClose"
      @onConfirm="onConfirm"
    >
      <div slot="content">
        <!--el-form 
                    model : 绑定的数据对象
                    ref : 相当于id  是唯一的
                    rules : 表单验证规则
                 -->
        <el-form
          :model="addModel"
          ref="addRef"
          :rules="rules"
          label-width="90px"
          :inline="false"
          size="normal"
          style="margin-right: 20px"
        >
          <!-- el-row代表一行 总长度是24 -->
          <el-row>
            <!-- el-col代表列  一列12，共分为2列 -->
            <el-col :span="12" :offset="0">
              <el-form-item label="name" prop="nickName">
                <el-input v-model="addModel.nickName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="phone" prop="phone">
                <el-input v-model="addModel.phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="email">
                <el-input v-model="addModel.email"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="sex" prop="sex">
                <el-radio-group v-model="addModel.sex">
                  <el-radio :label="'0'">man</el-radio>
                  <el-radio :label="'1'">woman</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="username" prop="username">
                <el-input v-model="addModel.username"></el-input>
              </el-form-item>
            </el-col>
            <el-col v-if="addModel.type == '0'" :span="12" :offset="0">
              <el-form-item label="password" prop="password">
                <el-input v-model="addModel.password"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24" :offset="0">
              <el-form-item label="role">
                <el-select v-model="addModel.roleId" placeholder="please select">
                  <el-option
                    v-for="item in options"
                    :key="item.roleId"
                    :label="item.roleName"
                    :value="item.roleId"
                  >
                  </el-option>
                </el-select>
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
        <img :src="this.barCode">
        {{this.content}}
      </div>
    </sys-dialog>
  </el-main>
</template>

<script>
import { getListApi, addUserApi, editUserApi, deleteUserApi, getRoleListApi, getRoleIdApi } from "@/api/user";
import SysDialog from "@/components/dialog/SysDialog.vue"; //引入弹框组件
import http from "@/utils/http";
export default {
  components: {
    //注册组件
    SysDialog,
  },
  data() {
    return {
      barCode:"",
      content:"",
      //条形码弹框属性
      codeDialog: {
        height: 120,
        width: 500,
        title: "bar code",
        visible: false,
      },
      options: [],
      rules: {
        nickName: [
          {
            trigger: "blur",
            required: true,
            message: "please enter your name",
          },
        ],
        phone: [
          {
            trigger: "blur",
            required: true,
            message: "please enter your phone",
          },
        ],
        sex: [
          {
            trigger: "blur",
            required: true,
            message: "please choose your sex",
          },
        ],
        username: [
          {
            trigger: "blur",
            required: true,
            message: "please enter your username",
          },
        ],
        password: [
          {
            trigger: "blur",
            required: true,
            message: "please enter your password",
          },
        ],
      },
      //表单绑定的数据
      addModel: {
        type: "", //只能取0或1  0新增 1编辑
        userId: "",
        nickName: "",
        phone: "",
        email: "",
        sex: "",
        username: "",
        password: "",
        roleId:"",
      },
      //弹框属性(弹框标题、大小、是否显示)  传递给子组件props中的属性
      dialog: {
        title: "",
        visible: false,
        width: 630,
        height: 260,
      },
      //表格的高度 在mounted中使用函数计算
      tableHeight: 0,
      //列表查询的参数
      listParm: {
        nickName: "",
        phone: "",
        currentPage: 1,
        pageSize: 10, //每页每次查询的条数
        total: 0, //总条数
      },
      //表格的数据
      tableData: [],
    };
  },
  created() {
    this.getList();
    this.getRoleList();
  },
  methods: {
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
    searchBtn(){
      this.getList();
    },
    resetBtn(){
      this.listParm.nickName = '';
      this.listParm.phone = '';
      this.getList();
    },
    async getList() {
      let res = await getListApi(this.listParm);
      console.log("成功");
      console.log(res);
      if (res && res.code == 200) {
        this.tableData = res.data.records;
        this.total = res.data.total;
      }
    },
    //新增按钮
    addBtn() {
      //设置弹框属性
      this.dialog.title = "add user";
      this.dialog.visible = true;
      //清空表单
      this.$resetForm("addRef", this.addModel);
      //设置为新增
      this.addModel.type = "0";
    },
    //编辑按钮
    editBtn(row) {
      this.dialog.visible = true;
      this.dialog.title = "edit user";
      //清空表单
      this.$resetForm("addRef", this.addModel);
      //把要编辑的数据复制到表单绑定的数据域
      this.$objCoppy(row, this.addModel);
      //设置为编辑
      this.addModel.type = "1";
      this.getRoleId(row.userId);
    },
    //删除按钮
    async deleteBtn(row) {
        //信息确认
      let confirm = await this.$myconfirm("Are you sure to delete this data?");
      if (confirm) {
        let res = await deleteUserApi({ userId: row.userId });
        if (res && res.code == 200) {
          //信息提示
          this.$message({ type: "success", message: res.msg });
          //刷新表格
          this.getList();
        }
      }
    },
    //页容量改变时触发  val:选择的页容量
    sizeChange(val) {
      this.listParm.pageSize = val;
      this.getList();
    },
    //页数改变时触发  val:点击的页数  如点击第六页,则val=6
    currentChange(val) {
      this.listParm.currentPage = val;
      this.getList();
    },
    //关闭弹框
    onClose() {
      this.dialog.visible = false;
    },
    //确定弹框
    onConfirm() {
      this.$refs.addRef.validate(async (valid) => {
        //会返回true或false
        //如果表单验证通过
        if (valid) {
          let res = null;
          //如果是新增 （type为0代表新增，type为1代表编辑）
          if (this.addModel.type == "0") {
            res = await addUserApi(this.addModel);
          } else {
            res = await editUserApi(this.addModel);
          }
          if (res && res.code == 200) {
            //信息提示
            this.$message({ type: "success", message: res.msg });
            //刷新表格
            this.getList();
            //关闭弹窗
            this.dialog.visible = false;
          }
        }
      });
    },
    async getRoleList(){
      let res = await getRoleListApi();
      if(res && res.code == 200){
        this.options = res.data;
      }
    },
    async getRoleId(userId){
      let res = await getRoleIdApi({userId:userId});
      if(res && res.code == 200){
        this.addModel.roleId = res.data.roleId;
        console.log(res);
      }
    },
  },
  mounted() {
    this.$nextTick(() => {
      // 计算表格高度
      this.tableHeight = window.innerHeight - 220;
    });
  },
};
</script>

<style lang="scss" scoped>
</style>