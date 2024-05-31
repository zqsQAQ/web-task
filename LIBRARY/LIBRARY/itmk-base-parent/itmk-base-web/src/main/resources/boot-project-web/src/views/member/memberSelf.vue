// learnNum为姓名  username为学号
<template>
  <el-main>
    <!-- <div style="text-align:center; margin-bottom:20px;">
      information
    </div> -->
    <el-form
      :model="addModel"
      ref="addRef"
      :rules="rules"
      label-width="120px"
      :inline="false"
      size="normal"
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
        <el-col :span="12" :offset="0"
          ><el-form-item label="password" prop="password">
            <el-input v-model="addModel.password"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" :offset="0">
          <el-form-item label="entry year">
            <el-input v-model="addModel.entryYear"></el-input>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12" :offset="0">
          <el-form-item label="sex">
            <el-radio-group v-model="addModel.sex">
              <el-radio :label="0">man</el-radio>
              <el-radio :label="1">woman</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col> -->
      </el-row>
      <el-row>
        <el-col :span="12" :offset="0">
          <el-form-item>
            <el-button type="primary" size="default" @click="saveBtn" 
              >save</el-button
            >
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </el-main>
</template>

<script>
import { editReaderApi, getByReaderIdApi } from "@/api/reader";
import { getUserId } from "@/utils/auth";
import SysDialog from "@/components/dialog/SysDialog.vue";
import http from "@/utils/http";
export default {
  data() {
    return {
      readerParm: {
        readerId: getUserId(),
      },
      addModel: {
        editType: "1",
        readerId: "",
        learnNum: "",
        username: "",
        idCard: "",
        // sex: "",
        phone: "",
        password: "",
        type: "",
        className: "", //班级名
        entryYear: "",
      },
      //表单验证规则
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
        password: [
          { required: true, message: "please enter password", trigger: "blur" },
        ],
        className: [
          { required: true, message: "please enter class", trigger: "blur" },
        ],
      },
    };
  },
  mounted() {
    this.getReaderInfo();
  },
  methods: {
    async getReaderInfo() {
      let res = await getByReaderIdApi(this.readerParm);
      console.log(res.data.learnNum);
      this.addModel.readerId = getUserId();
      this.addModel.learnNum = res.data.learnNum;
      this.addModel.username = res.data.username;
      this.addModel.phone = res.data.phone;
      this.addModel.className = res.data.className;
      this.addModel.idCard = res.data.idCard;
      this.addModel.password = res.data.password;
      this.addModel.entryYear = res.data.entryYear;
      // this.addModel.sex = res.data.sex;
    },
    async saveBtn() {
      console.log(this.addModel);
      let res = await editReaderApi(this.addModel);
      if (res && res.code == 200) {
        //信息提示
        this.$message({ type: "success", message: res.msg });
      } else {
        this.$message.error(res.msg);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
</style>