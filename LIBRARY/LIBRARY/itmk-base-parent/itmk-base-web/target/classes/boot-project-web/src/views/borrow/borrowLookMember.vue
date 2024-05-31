<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="lookParm" label-width="150px" :inline="true" size="mini">
      <!-- <el-form-item label="student ID">
        <el-input v-model="lookParm.username"></el-input>
      </el-form-item>
      <el-form-item label="member">
        <el-input v-model="lookParm.learnNum"></el-input>
      </el-form-item> -->
      <el-form-item label="book">
        <el-input v-model="lookParm.bookName"></el-input>
      </el-form-item>
      <el-form-item label="borrow status">
        <el-select v-model="lookParm.borrowStatus" placeholder="please select">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="exception status">
        <el-select v-model="lookParm.returnStatus" placeholder="please select">
          <el-option
            v-for="item in exceoptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item label="Expiration Status">
        <el-select v-model="lookParm.timeStatus" placeholder="please select">
          <el-option
            v-for="item in timetions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="audit Status">
        <el-select v-model="lookParm.applyStatus" placeholder="please select">
          <el-option
            v-for="item in applytions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">search</el-button>
        <el-button style="color: #ff7670" icon="el-icon-close" @click="resetBtn"
          >reset</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table
      size="small"
      ref="tables"
      :height="tableHeight"
      :data="tableData"
      border
      stripe
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="bookName" label="book name"> </el-table-column>
      <!-- <el-table-column prop="bookFloor" label="floor"> </el-table-column> -->
      <el-table-column prop="bookPlaceNum" label="shelf"> </el-table-column>
      <el-table-column prop="username" label="student ID"> </el-table-column>
      <el-table-column prop="learnNum" label="member"> </el-table-column>
      <el-table-column prop="className" label="class"> </el-table-column>
      <el-table-column prop="phone" label="phone"> </el-table-column>
      <el-table-column prop="borrowTime" label="borrow time"> </el-table-column>
      <el-table-column prop="remainDays" label="remain days"> </el-table-column>
      <el-table-column prop="returnTime" label="return time"> </el-table-column>
      <!-- <el-table-column prop="applyText" label="approval remark">
      </el-table-column> -->
      <!-- <el-table-column prop="excepionText" label="return remark"></el-table-column> -->
      <el-table-column prop="barcode" label="barcode"></el-table-column>
      <el-table-column
        prop="borrowStatus"
        label="borrow status"
        align="center"
        width="100"
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.borrowStatus == '1'">borrowing</el-tag>
          <el-tag type="success" v-if="scope.row.borrowStatus == '2'"
            >returned</el-tag
          >
          <el-tag type="danger" v-if="scope.row.borrowStatus == '3'"
            >exception</el-tag
          >
          <el-tag type="warning" v-if="scope.row.borrowStatus == '4'"
            >reserving</el-tag
          >
          <el-tag type="info" v-if="scope.row.borrowStatus == '5'"
            >canceled</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column
        prop="timeStatus"
        label="expiration status"
        align="center"
        width="100"
      >
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.timeStatus == '0'"
            >expire</el-tag
          >
          <el-tag type="success" v-if="scope.row.timeStatus == '1'"
            >not expired</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column label="operation" width="300" align="center">
        <template slot-scope="scope">
          <el-button
            type="success"
            size="small"
            icon="el-icon-eleme"
            @click="codeBtn(scope.row)"
            >Code</el-button
          >
          <el-button
            v-if="scope.row.borrowStatus == 4"
            type="warning"
            size="small"
            icon="el-icon-eleme"
            @click="takeBookBtn(scope.row)"
            >take</el-button
          >
          <el-button
            v-if="scope.row.borrowStatus == 4"
            type="danger"
            size="small"
            icon="el-icon-eleme"
            @click="cancelBtn(scope.row)"
            >cancel</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
      @size-change="sizeChange"
      @current-change="currentChange"
      :current-page.sync="lookParm.currentPage"
      :page-sizes="[10, 20, 40, 80, 100]"
      :page-size="lookParm.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="lookParm.total"
      background
    >
    </el-pagination>
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
        <img :src="this.barcodePic">
        {{this.myBarcode}}
      </div>
    </sys-dialog>
  </el-main>
</template>

<script>
import { getUserId } from "@/utils/auth";
import { getLookBorrowListApi,getReserveApi,cancelReserveApi } from "@/api/borrow";
import SysDialog from "@/components/dialog/SysDialog.vue";
import http from "@/utils/http";
export default {
  components:{
    SysDialog
  },
  data() {
    return {
      //存放当前行对应的barcode
      myBarcode:"",
      //条形码图片的链接
      barcodePic:"",
      //条形码弹框属性
      codeDialog: {
        height: 120,
        width: 500,
        title: "barcode",
        visible: false,
      },
      tableHeight: 0,
      tableData: [],
      timetions: [
        {
          value: "0",
          label: "expire",
        },
        {
          value: "1",
          label: "not expired",
        },
      ],
      exceoptions: [
        {
          value: "1",
          label: "normal return",
        },
        {
          value: "2",
          label: "abnormal return",
        },
      ],
      options: [
        {
          value: "1",
          label: "borrowing",
        },
        {
          value: "2",
          label: "returned",
        },
        {
          value: "3",
          label: "exception",
        },
      ],
      //对应后端实体类LookParm
      lookParm: {
        userId: getUserId(),
        total: 0,
        currentPage: 1,
        pageSize: 10,
        username: "",
        learnNum: "",
        bookName: "",
        applyStatus: "",
        borrowStatus: "",
        returnStatus: "",
        timeStatus: "",
        bookCode: "",
      },
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  created() {
    this.getLookBorrowList();
  },
  methods: {
    //取消预定的按钮
    async cancelBtn(row){
      let parm = {
        borrowId:row.borrowId
      }
      let res = await cancelReserveApi(parm)
      if (res && res.code == 200) {
        this.$message.success(res.msg)
        this.getLookBorrowList();
      }else{
        this.$message.error(res.msg)
      }
    },
    //取预定的书的按钮
    async takeBookBtn(row){
      let parm = {
        borrowId:row.borrowId
      }
      let res = await getReserveApi(parm)
      if (res && res.code == 200) {
        this.$message.success(res.msg)
        this.getLookBorrowList();
      }else{
        this.$message.error(res.msg)
      }
    },
    //展示条形码的按钮
    async codeBtn(row) {
      //进入时 先清空条形码链接数组
      this.codeDialog.visible = true;
      //调用公共条形码接口  生成条形码
      //  console.log("...........................");
      //  console.log(row);
      // this.content = row.borrowId+"-"+row.bookCode;
      // console.log(this.content);
      // barcode[i] = await getBarCodeApi()
      let res = await http.get("https://www.mxnzp.com/api/barcode/create", {
        content: row.barcode,
        width: "500",
        height: "100",
        type: "0",
        app_id: "8dqilgo2oixxcnnj",
        app_secret: "RElTVC9wZlRMdkNmRWJZb0tFZDEwQT09",
      });
      // console.log("wwwwwwwwwwwwwwwwwwwwwwwwwwww")
      // console.log(res);
      this.myBarcode = row.barcode;
      this.barcodePic = res.data.barCodeUrl;
    },
    //条形码弹框关闭
    codeOnClose() {
      this.codeDialog.visible = false;
    },
    //条形码弹框确定
    codeOnConfirm() {
      this.codeDialog.visible = false;
    },
    searchBtn() {
      this.getLookBorrowList();
    },
    resetBtn() {
      //清空搜索栏
      this.lookParm.username = "";
      this.lookParm.learnNum = "";
      this.lookParm.bookName = "";
      this.lookParm.applyStatus = "";
      this.lookParm.borrowStatus = "";
      this.lookParm.returnStatus = "";
      this.lookParm.timeStatus = "";
      this.getLookBorrowList();
    },
    currentChange(val) {
      this.lookParm.currentPage = val;
      this.getLookBorrowList();
    },
    sizeChange(val) {
      this.lookParm.pageSize = val;
      this.getLookBorrowList();
    },
    async getLookBorrowList() {
      let res = await getLookBorrowListApi(this.lookParm);
      if (res && res.code == 200) {
        this.tableData = res.data.records;
        this.lookParm.total = res.data.total;
      }
    },
  },
};
</script>

<style>
</style>