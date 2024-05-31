<template>
  <div class="logincontainer">
    <el-form
      class="loginForm"
      :model="loginForm"
      ref="loginForm"
      :rules="rules"
      label-width="80px"
      :inline="false"
      size="normal"
    >
      <el-form-item>
        <span class="loginTitle">XDU Library</span>
      </el-form-item>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          placeholder="please enter user name"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          type="password"
          v-model="loginForm.password"
          placeholder="Please enter password"
        ></el-input>
      </el-form-item>
      <el-form-item prop="userType">
        <el-radio-group v-model="loginForm.userType">
          <el-radio :label="0">member</el-radio>
          <el-radio :label="1">librarian</el-radio>
        </el-radio-group>
        <!-- <span
          style="color: #409eff; float: right; cursor: pointer"
          @click="registerBtn"
        >
          No account, reader registration
        </span> -->
      </el-form-item>
      <el-form-item>
        <el-row :gutter="20">
          <el-col :span="12" :offset="0">
            <el-button type="primary" class="mybtn" @click="onSubmit"
              >login</el-button
            >
          </el-col>
          <el-col :span="12" :offset="0">
            <el-button class="mybtn">cancel</el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <!-- 注册弹框 -->
    <sys-dialog
      :title="dialog.title"
      :visible="dialog.visible"
      :height="dialog.height"
      :width="dialog.width"
      @onClose="onClose"
      @onConfirm="onConfirm"
    >
      <div slot="content">
        <el-form
          :model="addModel"
          ref="addRef"
          :rules="registerRules"
          label-width="110px"
          size="small"
          style="margin-right: 30px"
        >
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="learnNum" label="name">
                <el-input v-model="addModel.learnNum"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="phone" label="phone">
                <el-input v-model="addModel.phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="username" label="student ID">
                <el-input v-model="addModel.username"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="className" label="class">
                <el-input v-model="addModel.className"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="idCard" label="ID Card">
                <el-input v-model="addModel.idCard"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="sex">
                <el-radio-group v-model="addModel.sex">
                  <el-radio :label="'0'">man</el-radio>
                  <el-radio :label="'1'">woman</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24" :offset="0">
              <el-form-item prop="password" label="password">
                <el-input type="password" v-model="addModel.password"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24" :offset="0">
              <el-form-item prop="confirmPassword" label="Confirm">
                <el-input type="password" v-model="addModel.confirmPassword"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </sys-dialog>
  </div>
</template>

<script>
import { registerApi } from "@/api/reader";
import SysDialog from "@/components/dialog/SysDialog.vue";
export default {
  components: {
    SysDialog,
  },
  data() {
    return {
      registerRules: {
        learnNum: [{ required: true, message: "Please enter your name", trigger: "blur" }],
        username: [{ required: true, message: "Please enter your student ID", trigger: "blur" }],
        idCard: [
          { required: true, message: "Please enter your ID Card", trigger: "blur" },
        ],
        phone: [{ required: true, message: "Please enter your phone", trigger: "blur" }],
        password: [{ required: true, message: "Please enter your password", trigger: "blur" }],
        confirmPassword: [
          { required: true, message: "Please confirm your password", trigger: "blur" },
        ],
        className: [{ required: true, message: "Please enter your class", trigger: "blur" }],
      },
      //表单属性
      addModel: {
        type: "",
        readerId: "",
        learnNum: "",
        username: "",
        idCard: "",
        sex: "",
        phone: "",
        password: "",
        confirmPassword: "",
        type: "",
        className: "",
      },
      dialog: {
        title: "member registration",
        visible: false,
        height: 250,
        width: 650,
      },
      //登录表单绑定数据源
      loginForm: {
        username: "",
        password: "",
        userType: "", //0：读者  1： 管理员
      },
      //表单验证规则
      rules: {
        username: [
          {
            trigger: "change",
            required: true,
            message: "please enter user name",
          },
        ],
        password: [
          {
            trigger: "change",
            required: true,
            message: "Please enter password",
          },
        ],
        userType: [
          {
            trigger: "change",
            required: true,
            message: "Please select user type",
          },
        ],
      },
    };
  },
  methods: {
    onConfirm() {
      if (this.addModel.password != this.addModel.confirmPassword) {
        this.$message.warning("The two entered passwords do not match!");
        return;
      }
      this.$refs.addRef.validate(async (valid) => {
        if (valid) {
          let res = await registerApi(this.addModel);
          if (res && res.code == 200) {
            this.$message.success(res.msg);
            this.dialog.visible = false;
          }
        }
      });
    },
    onClose() {
      this.dialog.visible = false;
    },
    //登录提交事件
    onSubmit() {
      //表单验证
      this.$refs.loginForm.validate((valid) => {
        //验证通过才提交表单
        if (valid) {
          this.loading = true;
          //调用store里面的login方法
          this.$store
            .dispatch("user/login", this.loginForm)
            .then(() => {
              //跳转路由
              this.$router.push({ path: this.redirect || "/" });
              this.loading = false;
            })
            .catch(() => {
              this.loading = false;
            });
        }
      });
    },
    registerBtn() {
      //表单清空
      this.$resetForm("addRef",this.addModel);
      this.dialog.visible = true;
    },
  },
};
</script>

<style  scoped>
.logincontainer {
  height: 100%;
  background: #fff;
  background-image: url("D:/LIBRARY/itmk-base-parent/itmk-base-web/src/main/resources/boot-project-web/src/icons/svg/bg.jpg");
  display: flex;
  align-items: center;
  justify-content: center;
  background-size: 100% 100%;
}
.loginForm {
  height: 350px;
  width: 480px;
  background: #fff;
  padding: 35px 20px;
  border-radius: 10px;
}
.loginTitle {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  font-weight: 600;
  color: #409eff;
}
.logincontainer ::v-deep .el-form-item__content {
  margin-left: 0px !important;
}
.mybtn {
  width: 100%;
}
</style>
