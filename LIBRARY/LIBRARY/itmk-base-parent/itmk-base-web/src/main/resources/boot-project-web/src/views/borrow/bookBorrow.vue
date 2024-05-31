<template>
  <el-main>
    <!-- 读者信息查询展示(卡片) -->
    <el-card class="box-card" style="margin-bottom: 20px">
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
            <el-button icon="el-icon-search" @click="getByUserName"
              >search</el-button
            >
            <el-button
              icon="el-icon-close"
              style="color: #ff7670"
              @click="resetCardBtn"
              >reset</el-button
            >
          </el-form-item>
          <!-- <el-form-item label="return date">
            <el-date-picker v-model="returnTime" type="date" placeholder="Please select return date">
            </el-date-picker>
          </el-form-item> -->
          <!-- <el-form-item>
            <el-button icon="el-icon-edit-outline" @click="borrowBtn" size="mini">borrow</el-button>
          </el-form-item> -->
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
          <el-form-item label="email">
            <el-input v-model="showUser.idCard"></el-input>
          </el-form-item>
          <el-form-item label="entry year">
            <el-input v-model="showUser.entryYear"></el-input>
          </el-form-item>
          <el-form-item label="sex">
            <el-radio v-model="showUser.sex" label="0">man</el-radio>
            <el-radio v-model="showUser.sex" label="1">woman</el-radio>
          </el-form-item>
          <!-- <el-form-item label="statue">
            <el-radio v-model="showUser.checkStatus" label="0">no</el-radio>
            <el-radio v-model="showUser.checkStatus" label="1">yes</el-radio>
          </el-form-item> -->
        </el-form>
      </div>
    </el-card>
    <!-- 搜索栏 -->
    <el-form :model="listParm" :inline="true" size="small">
      <el-form-item>
        <el-select v-model="listParm.categoryId" placeholder="select category">
          <!-- options是下拉菜单里面的内容 在data里面定义 -->
          <el-option
            v-for="item in options"
            :key="item.categoryId"
            :label="item.categoryName"
            :value="item.categoryId"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input
          v-model="listParm.bookName"
          placeholder="enter book name"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
          v-model="listParm.bookAuther"
          placeholder="enter author"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="listParm.bookFloor" placeholder="select floor">
          <el-option
            v-for="item in floorOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="listParm.bookPlaceNum" placeholder="select shelf">
          <el-option
            v-for="item in shelfOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="searchBtn" icon="el-icon-search">search</el-button>
        <el-button @click="resetBtn" icon="el-icon-close" type="danger"
          >reset</el-button
        >
        <el-button
          @click="barcodeBorrowBtn"
          icon="el-icon-edit-outline"
          type="primary"
          >borrow</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 表格(列表) -->
    <el-table :height="tableHeight" :data="tableData" border stripe>
      <!-- 这里的prop里面，后端是啥就填啥 -->
      <el-table-column label="ISBN" prop="bookCode"></el-table-column>
      <el-table-column label="book name" prop="bookName"></el-table-column>
      <el-table-column label="category" prop="categoryName"></el-table-column>
      <el-table-column label="floor" prop="bookFloor"></el-table-column>
      <el-table-column label="shelf" prop="bookPlaceNum"></el-table-column>
      <el-table-column label="author" prop="bookAuther"></el-table-column>
      <el-table-column label="press" prop="bookProduct"></el-table-column>
      <el-table-column label="price" prop="bookPrice"></el-table-column>
      <el-table-column label="stock" prop="bookStore"></el-table-column>
      <el-table-column label="operation" width="160" align="center">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-edit-outline"
            @click="borrowBtn(scope.row)"
            size="small"
            type="success"
            >borrow</el-button
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
    <!-- 通过扫描条形码还书弹出框 -->
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
          :model="BorrowParm"
          ref="borRef"
          label-width="80px"
          :inline="false"
          size="normal"
        >
          <el-form-item label="barcode">
            <el-input
              v-model="BorrowParm.barcode"
              placeholder="please enter barcode"
              >barcode</el-input
            >
          </el-form-item>
        </el-form>
      </div>
    </sys-dialog>
  </el-main>
</template>

<script>
//引用插件————穿梭框
import eltTransfer from "elt-transfer/src/eltTransfer";
import { getByUserNameApi } from "@/api/reader";
import { getListApi, getCateListApi } from "@/api/books";
import { borrowApi, borrowByBarApi } from "@/api/borrow";
import { getUserId } from "@/utils/auth";
import SysDialog from "@/components/dialog/SysDialog.vue";

export default {
  components: {
    SysDialog,
  },
  data() {
    return {
      //学生信息卡片
      showUser: {
        readerId: "",
        learnNum: "", //表示姓名
        username: "", //表示学号
        idCard: "",
        sex: "",
        phone: "",
        className: "",
        entryYear: "",
        // checkStatus: "",
      },
      //查询借书人信息
      searchParm: {
        username: "", //username表示学号 和后端对应
      },
      //通过输入barcode借书时，输入的barcode
      popBarcode: "",
      //弹框属性定义
      dialog: {
        width: 630,
        height: 60,
        title: "borrow book",
        visible: false,
      },
      tableHeight: 0,
      //楼层下拉选择框
      floorOptions: [
        {
          value: "1",
          label: "1F",
        },
        {
          value: "2",
          label: "2F",
        },
        {
          value: "3",
          label: "3F",
        },
      ],
      //书架下拉选择框
      shelfOptions: [
        {
          value: "1",
          label: "1",
        },
        {
          value: "2",
          label: "2",
        },
        {
          value: "3",
          label: "3",
        },
        {
          value: "4",
          label: "4",
        },
        {
          value: "5",
          label: "5",
        },
        {
          value: "6",
          label: "6",
        },
      ],
      //图书种类下拉数据
      options: [],
      //列表查询的相关参数
      listParm: {
        currentPage: 1,
        pageSize: 10,
        categoryId: "",
        bookName: "",
        bookFloor: "",
        bookPlaceNum: "",
        bookAuther: "",
        total: 0,
      },
      //通过扫码借书的参数
      BorrowParm: {
        barcode: "",
        username: "",
      },
      //
      // ----------------------------------------------------------
      //借书列表
      bookIds: [],
      //选中的书 （即穿梭框右侧）
      tableData: [],
    };
  },
  created() {
    this.getList();
    this.getCateList();
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  methods: {
    //重置按钮
    resetCardBtn() {
      this.searchParm.username = "";
      this.showUser.readerId = "";
      this.showUser.learnNum = "";
      this.showUser.username = "";
      this.showUser.idCard = "";
      this.showUser.sex = "";
      this.showUser.phone = "";
      this.showUser.className = "";
      // this.showUser.checkStatus = "";
    },
    //根据学号获取读者信息  搜索按钮
    async getByUserName() {
      let res = await getByUserNameApi(this.searchParm);
      if (res && res.code == 200 && res.data) {
        this.showUser = res.data;
      } else {
        this.$message({ type: "error", message: res.msg });
      }
    },
    barcodeBorrowBtn() {
      this.$resetForm("borRef", this.BorrowParm);
      this.dialog.visible = true;
    },
    //扫码借书
    async onConfirm() {
      let confirm = await this.$myconfirm("Are you sure to borrow this book?");
      if (confirm) {
        this.BorrowParm.username = this.searchParm.username;
        //以BorrowParm为参数 调用接口 完成借书
        let res = await borrowByBarApi(this.BorrowParm);
        console.log(this.BorrowParm);
        console.log(res);
        if (res && res.code == 200) {
          this.$message.success(res.msg);
          this.getList();
          // setTimeout(function () {
          //   window.location.reload();
          // }, 1500);
        } else {
          this.$message({ type: "error", message: res.msg });
        }
      }
      this.dialog.visible = false;
    },
    onClose() {
      this.dialog.visible = false;
    },
    async getList() {
      let res = await getListApi(this.listParm);
      if (res && res.code == 200) {
        console.log(res.data);
        this.tableData = res.data.records;
        this.listParm.total = res.data.total;
      } else {
        this.$message({ type: "error", message: res.msg });
      }
    },
    //查询图书类别的列表
    async getCateList() {
      let res = await getCateListApi();
      if (res && res.code == 200) {
        console.log(res);
        this.options = res.data;
      }
    },
    //借书按钮
    async borrowBtn(row) {
      if (this.searchParm.username == "") {
        this.$message.error("please enter student ID!");
        return;
      }
      console.log(row);
      this.bookIds = [];
      // //找到每本书的bookId
      // for(let i = 0; i < this.tableData.length; i++){
      //   this.bookIds.push(this.tableData[i].bookId)
      // }
      //提交
      this.bookIds.push(row.bookId);
      let parm = {
        // readerId: getUserId(),
        readerId: this.showUser.readerId,
        bookIds: this.bookIds,
      };
      console.log(parm.readerId);
      let res = await borrowApi(parm);
      if (res && res.code == 200) {
        this.$message.success(res.msg);
        // setTimeout(function () {
        //   window.location.reload();
        // }, 1500);
      } else {
        this.$message({ type: "error", message: res.msg });
      }
      console.log(getUserId());
      this.getList();
    },
    //搜索按钮
    searchBtn() {
      this.getList();
    },
    //重置按钮
    resetBtn() {
      this.listParm.categoryId = "";
      this.listParm.bookAuther = "";
      this.listParm.bookName = "";
      this.listParm.bookPlaceNum = "";
      this.listParm.bookFloor = "";
      this.getList();
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
    currentChange(val) {
      this.listParm.currentPage = val;
      this.returnBorrow();
    },
    sizeChange(val) {
      this.listParm.pageSize = val;
      this.returnBorrow();
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