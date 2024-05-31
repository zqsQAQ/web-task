<template>
  <el-main>
    <div
      style="
        margin: 0px 0px 15px 0px;
        color: #67c23a;
        font-weight: 600;
        font-size: 15px;
      "
    >
      reader information
    </div>
    <!-- 读者信息查询展示(卡片) -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <el-form
          :model="searchParm"
          label-width="90px"
          :inline="true"
          size="small"
        >
          <el-form-item label="student ID">
            <el-input v-model="searchParm.username"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-search" @click="getByUserName">search</el-button>
            <el-button icon="el-icon-close" style="color: #ff7670" @click="resetBtn">reset</el-button>
          </el-form-item>
          <el-form-item label="return date">
            <el-date-picker v-model="returnTime" type="date" placeholder="Please select return date">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-edit-outline" @click="borrowBtn" size="mini">borrow</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="text item">
        <el-form
          :model="showUser"
          label-width="85px"
          :inline="true"
          size="small"
        >
          <el-form-item label="name">
            <el-input v-model="showUser.learnNum"></el-input>
          </el-form-item>
          <el-form-item label="class">
            <el-input v-model="showUser.className"></el-input>
          </el-form-item>
          <el-form-item label="student ID">
            <el-input v-model="showUser.username"></el-input>
          </el-form-item>
          <el-form-item label="phone">
            <el-input v-model="showUser.phone"></el-input>
          </el-form-item>
          <el-form-item label="ID card">
            <el-input v-model="showUser.idCard"></el-input>
          </el-form-item>
          <el-form-item label="sex">
            <el-radio v-model="showUser.sex" label="0">man</el-radio>
            <el-radio v-model="showUser.sex" label="1">woman</el-radio>
          </el-form-item>
          <el-form-item label="statue">
            <el-radio v-model="showUser.checkStatus" label="0">no</el-radio>
            <el-radio v-model="showUser.checkStatus" label="1">yes</el-radio>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <div
      style="
        margin: 15px 0px;
        color: #67c23a;
        font-weight: 600;
        font-size: 15px;
      "
    >
      book list
    </div>
    <!-- 穿梭框 -->
    <div>
      <elt-transfer
        ref="eltTransfer"
        :show-query="true"
        :show-pagination="true"
        :pagination-call-back="paginationCallBack"
        :left-columns="leftColumns"
        :title-texts="['to be selected', 'selected']"
        :button-texts="['add', 'delete']"
        :query-texts="['search', 'search']"
        :table-row-key="(row) => row.bookId"
        v-model="tableData"
      >
        <!-- 可以使用插槽获取到列信息和行信息，从而进行数据的处理 -->
        <template v-slot:leftCondition="{}">
          <el-form-item label="book name">
            <el-input
              v-model="listParm.bookName"
              placeholder="book name"
            ></el-input>
          </el-form-item>
          <el-form-item label="author">
            <el-input
              v-model="listParm.bookAuther"
              placeholder="author"
            ></el-input>
          </el-form-item>
        </template>
        <template v-slot:rightCondition="{ scope }">
          <el-form-item label="book name">
            <el-input
              v-model="scope.bookName"
              placeholder="book name"
            ></el-input>
          </el-form-item>
        </template>
      </elt-transfer>
    </div>
  </el-main>
</template>

<script>
//引用插件————穿梭框
import eltTransfer from "elt-transfer/src/eltTransfer";
import { getByUserNameApi } from "@/api/reader";
import { getListApi } from "@/api/books";
import { borrowApi } from "@/api/borrow";
export default {
  components: {
    //起名为elt-transter
    "elt-transfer": eltTransfer,
  },
  data() {
    return {
      //还书时间
      returnTime: "",
      //借书列表
      bookIds: [],
      //图书列表分页参数
      listParm: {
        currentPage: "",
        pageSize: "",
        bookName: "",
        bookAuther: "",
      },
      //选中的书 （即穿梭框右侧）
      tableData: [],
      //穿梭框左侧
      leftColumns: [
        { label: "book name", id: "bookName", width: "120px" },
        { label: "category", id: "categoryName", width: "120px" },
        { label: "shelf", id: "bookPlaceNum" },
        { label: "author", id: "bookAuther" },
        { label: "press", id: "bookProduct" },
        { label: "stock", id: "bookStore" },
      ],
      //搜索框
      searchParm: {
        username: "", //username表示学号 和后端对应
      },
      //学生信息卡片
      showUser: {
        readerId:"",
        learnNum: "", //表示姓名
        username: "", //表示学号
        idCard: "",
        sex: "",
        phone: "",
        className: "",
        checkStatus: "",
      },
    };
  },
  methods: {
    //借书按钮
    async borrowBtn() {
      console.log(this.tableData);
      this.bookIds = [];
      if(!this.returnTime){
        return this.$message.error("please select return time!");
      }
       if (!this.showUser.readerId) {
        return this.$message.error("please search reader inforation!");
      }
      if (this.tableData.length == 0) {
        return this.$message.error("please select books!");
      }
      //找到每本书的bookId
      for(let i = 0; i < this.tableData.length; i++){
        this.bookIds.push(this.tableData[i].bookId)
      }
      //提交
      let parm = {
        readerId:this.showUser.readerId,
        bookIds:this.bookIds,
        returnTime:this.returnTime
      }
      let res = await borrowApi(parm);
      if(res && res.code == 200){
         this.$message.success(res.msg);
        setTimeout(function () {
          window.location.reload();
        }, 1500);
      }
    },
    //重置按钮
    resetBtn() {
      this.searchParm.username = "";
      this.showUser.readerId = "";
      this.showUser.learnNum = "";
      this.showUser.username = "";
      this.showUser.idCard = "";
      this.showUser.sex = "";
      this.showUser.phone = "";
      this.showUser.className = "";
      this.showUser.checkStatus = "";
    },
    //根据学号获取读者信息  搜索按钮
    async getByUserName() {
      let res = await getByUserNameApi(this.searchParm);
      if (res && res.code == 200 && res.data) {
        this.showUser = res.data;
      }
    },
    async paginationCallBack(obj) {
      console.log(obj);
      this.listParm.currentPage = obj.pageIndex;
      this.listParm.pageSize = obj.pageSize;
      //获取图书数据
      let res = await getListApi(this.listParm);
      console.log(res);
      return new Promise((resolve, reject) => {
        try {
          resolve({ total: res.data.total, data: res.data.records });
        } catch {
          reject();
        }
      });
    },
    clearTransfer() {
      this.$refs.eltTransfer.clear();
    },
  },
};
</script>

<style scoped>
::v-deep .el-card__header {
  padding: 15px 0px 0px 0px;
}
::v-deep .el-card__body {
  padding: 15px 0px 0px 0px;
}
</style>