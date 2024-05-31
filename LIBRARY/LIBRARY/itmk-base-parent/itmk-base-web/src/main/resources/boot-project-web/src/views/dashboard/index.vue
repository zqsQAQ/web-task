<template>
  <el-main>
    <!-- 管理员统计 -->
    <el-row
      :gutter="20"
      type="flex"
      class="row-bg"
      justify="center"
      style="margin-bottom: 80px"
    >
      <!-- 1-1 读者总数 -->
      <el-col :span="6">
        <div class="show-header">
          <div class="show-num">{{ readerCount }}</div>
          <div class="bottom-text">Total Registered Members</div>
        </div>
      </el-col>
      <!-- 1-2 书总数 -->
      <el-col :span="6">
        <div class="show-header" style="background: rgb(255, 153, 0)">
          <div class="show-num">{{ allBooks }}</div>
          <div class="bottom-text">Total Books</div>
        </div>
      </el-col>
      <!-- 1-3 复印本总数 -->
      <el-col :span="6">
        <div class="show-header" style="background: rgb(45, 183, 245)">
          <div class="show-num">{{ copies }}</div>
          <div class="bottom-text">Total Book Copies</div>
        </div>
      </el-col>
      <!-- 1-4 借书总数 -->
      <el-col :span="6">
        <div class="show-header" style="background: rgb(237, 64, 20)">
          <div class="show-num">{{ allBorrowNum }}</div>
          <div class="bottom-text">Total Borrows</div>
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
      <!-- 2-1 所有未支付罚款数量（不是金额） -->
      <el-col :span="6">
        <div class="show-header">
          <div class="show-num">{{ unpaidNum }}</div>
          <div class="bottom-text">Total Fine Amount (Unpaid)</div>
        </div>
      </el-col>
      <!-- 2-2  未支付罚款金额-->
      <el-col :span="6">
        <div class="show-header" style="background: rgb(255, 153, 0)">
          <div class="show-num">{{ unpaidFee }}</div>
          <div class="bottom-text">Total Fine Fee (Unpaid)</div>
        </div>
      </el-col>
      <!-- 2-3 所有收到的罚款总数（不是金额） -->
      <el-col :span="6">
        <div class="show-header" style="background: rgb(45, 183, 245)">
          <div class="show-num">{{ paidNum }}</div>
          <div class="bottom-text">Total Fine Amount (Collected)</div>
        </div>
      </el-col>
      <!-- 2-4 已支付罚款金额 -->
      <el-col :span="6">
        <div class="show-header" style="background: rgb(237, 64, 20)">
          <div class="show-num">{{ paidFee }}</div>
          <div class="bottom-text">Total Fine Fee (Collected)</div>
        </div>
      </el-col>
    </el-row>
    <el-row
      :gutter="20"
      type="flex"
      class="row-bg"
      justify="center"
      style="margin-bottom: 80px"
    >
    <!-- 3-1 所有丢失的书 -->
      <el-col :span="6">
        <div class="show-header">
          <div class="show-num">{{ LostBooks }}</div>
          <div class="bottom-text">Total Lost Books</div>
        </div>
      </el-col>
      <!-- 3-2 所有损坏的书 -->
      <el-col :span="6">
        <div class="show-header" style="background: rgb(255, 153, 0)">
          <div class="show-num">{{ DamageBooks }}</div>
          <div class="bottom-text">Total Damage Books</div>
        </div>
      </el-col>
      <!-- 3-3 历史借书总数 -->
      <el-col :span="6">
        <div class="show-header" style="background: rgb(45, 183, 245)">
          <div class="show-num">{{ allHistoryBorrowNum }}</div>
          <div class="bottom-text">Total History Borrows</div>
        </div>
      </el-col>
      
    </el-row>
  </el-main>
</template>

<script>
import {
  getAllBooksNumApi,
  getAllMembersNumApi,
  getAllBooksNotReturnNumApi,
  getAllHistoryBorrowNumApi,
  getAllLostNumApi,
  getAllCopyNumApi,
  getUnpaidNumApi,
  getDamageNumApi,
  getPaidNumApi,
  getUnpaidFeeApi,
  getPaidFeeApi,
} from "@/api/dashboard";
export default {
  name: "Dashboard",
  data() {
    return {
      readerCount: 0, //读者总数
      allBooks: 0, //书总数(不是复印本)
      copies: 0, //复印本总数
      allBorrowNum: 0, //借书总数
      LostBooks: 0, //丢失书总数
      allHistoryBorrowNum: 0, //历史借书总数
      unpaidNum: 0, //未支付罚款总数（不是金额）
      paidNum: 0, //已支付罚款总数（不是金额）
      DamageBooks: 0, //损坏的书数量
      unpaidFee: 0, //未支付罚款金额
      paidFee:0, //已支付的罚款金额
    };
  },
  mounted() {
    this.getAllMembersNum();
    this.getAllBooksNum();
    this.getAllBooksNotReturnNum();
    this.getAllHistoryBorrowNum();
    this.getAllLostNum();
    this.getAllCopyNum();
    this.getUnpaidNum();
    this.getDamageNum();
    this.getPaidNum();
    this.getUnpaidFee();
    this.getPaidFee();
  },
  methods: {
    //1-1 读者总数
    async getAllMembersNum() {
      let res = await getAllMembersNumApi();
      console.log("读者总数：" + res);
      this.readerCount = res;
    },
    //1-2 书总数
    async getAllBooksNum() {
      let res = await getAllBooksNumApi();
      console.log("书总数：" + res);
      this.allBooks = res;
    },
    //1-3 复印本总数
    async getAllCopyNum() {
      let res = await getAllCopyNumApi();
      console.log("复印本总数：" + res);
      this.copies = res;
    },
    //1-4 未还书总数
    async getAllBooksNotReturnNum() {
      let res = await getAllBooksNotReturnNumApi();
      console.log("未还书(借书)总数：" + res);
      this.allBorrowNum = res;
    },
    //2-1 全部未支付罚款
    async getUnpaidNum() {
      let res = await getUnpaidNumApi();
      console.log("未支付罚款" + res);
      this.unpaidNum = res;
    },
    //2-2 丢失的书总数
    async getAllLostNum() {
      let res = await getAllLostNumApi();
      console.log("丢失的书总数：" + res);
      this.LostBooks = res;
    },
    //2-3 损坏的书总数
    async getDamageNum() {
      let res = await getDamageNumApi();
      console.log("损坏的书总数：" + res);
      this.DamageBooks = res;
    },
    //2-4 已支付的罚款总数
    async getPaidNum() {
      let res = await getPaidNumApi();
      console.log("已支付的罚款总数：" + res);
      this.paidNum = res;
    },
    //3-1 历史借书总数
    async getAllHistoryBorrowNum() {
      let res = await getAllHistoryBorrowNumApi();
      console.log("历史借书总数：" + res);
      this.allHistoryBorrowNum = res;
    },
    //3-2 未支付的罚款金额
    async getUnpaidFee() {
      let res = await getUnpaidFeeApi();
      console.log("未支付的罚款金额：" + res);
      this.unpaidFee = res;
    },
    //3-3 已支付的罚款总数
    async getPaidFee() {
      let res = await getPaidFeeApi();
      console.log("已支付的罚款金额：" + res);
      this.paidFee = res;
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
</style>
