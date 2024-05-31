<template>
  <el-main>
    <div id="mydiv"></div>
    <el-form :model="listParm" label-width="150px" :inline="true" size="mini">
      <!-- <el-form-item label="book">
        <el-input v-model="listParm.bookName"></el-input>
      </el-form-item> -->
      <el-form-item label="student ID">
        <el-input
          v-model="listParm.username"
          placeholder="please enter student ID"
        ></el-input>
      </el-form-item>
      <el-form-item label="fine type">
        <el-select v-model="listParm.fineType" placeholder="please select">
          <el-option
            v-for="item in typeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="fine status">
        <el-select v-model="listParm.fineStatus" placeholder="please select">
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">search</el-button>
        <el-button style="color: #ff7670" icon="el-icon-close" @click="resetBtn"
          >reset</el-button
        >
      </el-form-item>
      
    </el-form>
    <!-- 表格 -->
    <el-table
      ref="tables"
      :height="tableHeight"
      :data="tableData"
      border
      stripe
    >
      <!-- <el-table-column type="selection" width="55"> </el-table-column> -->
      <el-table-column prop="borrowId" label="borrowId"> </el-table-column>
      <el-table-column prop="username" label="username"> </el-table-column>
      <el-table-column prop="bookName" label="bookName"> </el-table-column>
      <el-table-column prop="barcode" label="barcode"> </el-table-column>
      <el-table-column prop="fineFee" label="fineFee"> </el-table-column>
      <el-table-column prop="fineType" label="fineType" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.fineType == 0">overdue</el-tag>
          <el-tag type="danger" v-if="scope.row.fineType == 1"
            >damaged or lost</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column prop="fineStatus" label="fineStatus" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.fineStatus == '0'" type="danger"
            >unpaid</el-tag
          >
          <el-tag type="success" v-if="scope.row.fineStatus == '1'"
            >paid</el-tag
          >
        </template>
      </el-table-column>
      <!-- <el-table-column label="operation" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="payBtn(scope.row)" v-if="scope.row.fineStatus == '0'"
            >pay</el-button
          >
        </template>
      </el-table-column> -->

      <!-- <template slot-scope="scope">
          <el-button
            type="success"
            size="small"
            icon="el-icon-eleme"
            @click="codeBtn(scope.row)"
            >Code</el-button
          >

        </template> -->
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
  </el-main>
</template>

<script>
import {
  aliPayApi,
  getFineListApi,
  getAliPayApi,
  payFineApi,
} from "@/api/fine";
import $ from "jquery";
import { getUserId } from "@/utils/auth";
export default {
  data() {
    return {
      tableData: [],
      tableHeight: 0,
      //调用接口传递的参数
      payParm: {
        merchantUserId: getUserId(),
        merchantOrderId: "",
        total_amount: "",
      },
      //调用接口返回的代码
      payCode: "",
      //调用罚款列表接口传递的参数
      listParm: {
        currentPage: 1,
        pageSize: 10,
        readerId: getUserId(),
        fineType: "",
        fineStatus: "",
        username:"",
        // bookName:""
      },
      statusOptions: [
        {
          value: "0",
          label: "unpaid",
        },
        {
          value: "1",
          label: "paid",
        },
      ],
      typeOptions: [
        {
          value: 0,
          label: "overdue",
        },
        {
          value: 1,
          label: "damaged or lost",
        },
      ],
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 170;
    });
  },
  methods: {
    //支付按钮
    async payBtn(row) {
      console.log("fineId............" + row.fineId);
      let parm = {
        fineId: row.fineId,
      };
      // await payFineApi({fineId:row.fineId});
      // await payFineApi(row.fineId);
      await payFineApi(parm);
      // this.listParm.fineStatus = 1;
      // this.getList();
      // this.payParm.merchantOrderId = new Date();
      this.payParm.merchantOrderId = row.borrowId;
      console.log(this.payParm.merchantOrderId);
      this.payParm.total_amount = row.fineFee;
      this.payCode = await aliPayApi(this.payParm);
      // let res = await getAliPayApi();
      // console.log(res);
      console.log(this.payCode);
      $("#mydiv").append(this.payCode.data);
    },
    //获取罚款列表
    async getList() {
      let res = await getFineListApi(this.listParm);
      if (res && res.code == 200) {
        console.log(res.data.records);
        this.tableData = res.data.records;
        // console.log(this.tableData);
        this.listParm.total = res.data.total;
      }
    },
    currentChange(val) {
      this.listParm.currentPage = val;
      this.getList();
    },
    sizeChange(val) {
      this.listParm.pageSize = val;
      this.getList();
    },
    searchBtn() {
      this.getList();
    },
    resetBtn() {
      //清空搜索栏
      // this.listParm.bookName = "";
      this.listParm.fineType = "";
      this.listParm.fineStatus = "";
      this.listParm.username = "";
      this.getList();
    },
  },
};
</script>

<style>
</style>