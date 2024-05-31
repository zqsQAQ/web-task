<template>
  <el-main>
    <!-- 第一行 -->
    <el-row
      :gutter="20"
      type="flex"
      class="row-bg"
      justify="center"
      style="margin-bottom: 80px"
    >
      <!-- 1-1 某用户未还书(正在借)的数量 -->
      <el-col :span="6">
        <div class="show-header">
          <div class="show-num">{{ curBorBooks }}</div>
          <div class="bottom-text">Current Borrowed books</div>
        </div>
      </el-col>
       <!-- 1-2 某用户历史已借书的数量 -->
      <el-col :span="6">
        <div class="show-header" style="background: rgb(255, 153, 0)">
          <div class="show-num">{{ totalBorBooks }}</div>
          <div class="bottom-text">Total Borrowed books</div>
        </div>
      </el-col>
      <!-- 1-3 某用户上一次还的书(后端返回的是书名) -->
      <el-col :span="6">
        <div class="show-header" style="background: rgb(45, 183, 245)">
          <div class="show-char">{{ LRBooks }}</div>
          <div class="bottom-text">Last Return Book</div>
        </div>
      </el-col>
      <!-- 1-4 某用户上次借的书(后端返回的是书名) -->
      <el-col :span="6">
        <div class="show-header" style="background: rgb(237, 64, 20)">
          <div class="show-char">{{ LBBooks }}</div>
          <div class="bottom-text">Last Borrow Book</div>
        </div>
      </el-col>
      
    </el-row>
    <!-- 第二行 -->
    <el-row
      :gutter="20"
      type="flex"
      class="row-bg"
      justify="center"
      style="margin-bottom: 80px"
    >
      <!-- 2-1 某用户未支付的罚款数量(不是金额) -->
      <el-col :span="6">
        <div class="show-header">
          <div class="show-num">{{ unpaidFine }}</div>
          <div class="bottom-text">Fine Amount (Unpaid)</div>
        </div>
      </el-col>
       <!-- 2-2 某用户总的罚款数量（包括已支付） --> 
      <el-col :span="6">
        <div class="show-header" style="background: rgb(255, 153, 0)">
          <div class="show-num">{{ totalFine }}</div>
          <div class="bottom-text">Fine Amount (Total)</div>
        </div>
      </el-col>
       <!-- 2-3 某用户未支付的罚款金额 --> 
      <el-col :span="6">
        <div class="show-header" style="background: rgb(45, 183, 245)">
          <div class="show-num">{{ unpaidFee }}</div>
          <div class="bottom-text">Fine Fee (Unpaid)</div>
        </div>
      </el-col>
       <!-- 2-4 某用户总的罚款金额（包括已支付） --> 
      <el-col :span="6">
        <div class="show-header" style="background: rgb(237, 64, 20)">
          <div class="show-num">{{ totalFee }}</div>
          <div class="bottom-text">Fine Fee (Total)</div>
        </div>
      </el-col>
    </el-row>
  </el-main>
</template>

<script>
import {
  getReaderBorrowNumApi,
  getReaderHistoryBorrowNumApi,
  getReaderUnpaidApi,
  getReaderLastReturnApi,
  getReaderLastBorrowApi,
  getReaderAllFineApi,
  getReaderTotalFeeApi,
  getReaderUnpaidFeeApi,
} from "@/api/dashboard";
import { getUserId } from "@/utils/auth";
export default {
  name: "Dashboard",
  data() {
    return {
      curBorBooks: 0, //某用户未还书的数量
      unpaidFine: 0, //某用户未支付的罚款数量
      totalFine: 0, //某用户总的罚款数量（包括已支付）
      totalBorBooks: 0, //某用户历史已借书的数量
      LRBooks:"", //某用户上一次还的书(书名，后端返回的是书名)
      LBBooks:"", //某用户上一次借的书(书名，后端返回的是书名)
      unpaidFee:0,
      totalFee:0,
      parm: {
        readerId: getUserId(),
      },
    };
  },
  mounted() {
    this.getReaderBorrowNum(); //某用户未还书的数量
    this.getReaderHistoryBorrowNum();
    this.getReaderUnpaid();
    this.getReaderLastReturn();
    this.getReaderLastBorrow();
    this.getReaderAllFine();
    this.getReaderTotalFee();
    this.getReaderUnpaidFee();
  },
  methods: {
    //1-1 某用户未还书的数量(在借中)
    async getReaderBorrowNum() {
      let res = await getReaderBorrowNumApi(this.parm);
      console.log("未还书的数量：" + res);
      this.curBorBooks = res;
    },
    //1-2 某用户上一次还的书(书名，后端返回的是书名)
    async getReaderLastReturn() {
      let res = await getReaderLastReturnApi(this.parm);
      console.log("上一次还的书：" + res);
      this.LRBooks = res;
    },
    //1-3 某用户总的罚款数量（包括已支付）
    async getReaderAllFine() {
      let res = await getReaderAllFineApi(this.parm);
      console.log("总罚款数量：" + res);
      this.totalFine = res;
    },
    //1-4 某用户历史已借书的数量(借书总数量)
    async getReaderHistoryBorrowNum() {
      let res = await getReaderHistoryBorrowNumApi(this.parm);
      console.log("历史借书数量：" + res);
      this.totalBorBooks = res;
    },  
    //2-1 某用户未支付的罚款数量
    async getReaderUnpaid() {
      let res = await getReaderUnpaidApi(this.parm);
      console.log("未还罚款为：" + res);
      this.unpaidFine = res;
    },
    //2-2 某用户上一次借的书(书名，后端返回的是书名)
    async getReaderLastBorrow() {
      let res = await getReaderLastBorrowApi(this.parm);
      console.log("上一次借的书：" + res);
      this.LBBooks = res;
    },
    //某用户总罚款金额（已支付 + 未支付）
    async getReaderTotalFee() {
      let res = await getReaderTotalFeeApi(this.parm);
      console.log("总罚款金额" + res);
      this.totalFee = res;
    },
    //某用户未支付的罚款金额
    async getReaderUnpaidFee() {
      let res = await getReaderUnpaidFeeApi(this.parm);
      console.log("未支付的罚款金额" + res);
      this.unpaidFee = res;
    },
  },
};
</script>

<style lang="scss" scoped>
.bottom-text {
  bottom: 0;
  width: 100%;
  background: rgba(0, 0, 0, 0.1);
  height: 30px;
  line-height: 30px;
  text-align: center;
  position: absolute;
  font-weight: 600;
}
.show-header {
  background: #00c0ef;
  color: #fff;
  height: 120px;
  border-radius: 5px;
  position: relative;
}
.show-num {
  font-size: 38px;
  font-weight: 600;
  padding: 5px;
}
.show-char {
  font-size: 20px;
  font-weight: 600;
  padding: 12px;
}
</style>
