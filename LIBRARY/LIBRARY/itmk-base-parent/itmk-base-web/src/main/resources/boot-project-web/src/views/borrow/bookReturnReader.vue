<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="listParm" label-width="120px" :inline="true" size="small">
      <!-- <el-form-item label="student ID">
        <el-input v-model="listParm.username"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="borrow statue">
        <el-select v-model="listParm.borrowStatus" placeholder="please select">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item>
        <!-- <el-button icon="el-icon-search" @click="searchBtn">search</el-button>
        <el-button icon="el-icon-close" @click="resetBtn">reset</el-button> -->
        <el-button type="primary" icon="el-icon-edit" @click="returnBtn"
          >return</el-button
        >
        <el-button
          @click="codeReturnBtn"
          icon="el-icon-edit-outline"
          type="primary"
          >codeReturn</el-button
        >
        <el-button
          @click="codeExceptionReturnBtn"
          icon="el-icon-edit-outline"
          type="primary"
          >codeExceptionReturn</el-button
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
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="bookCode" label="ISBN"> </el-table-column>
      <el-table-column prop="bookName" label="book name"> </el-table-column>
      <el-table-column prop="bookPlaceNum" label="shelf"> </el-table-column>
      <el-table-column prop="username" label="studentID"> </el-table-column>
      <el-table-column prop="learnNum" label="member name"> </el-table-column>
      <el-table-column prop="className" label="class"> </el-table-column>
      <el-table-column prop="phone" label="phone"> </el-table-column>
      <el-table-column prop="borrowTime" label="borrow time"> </el-table-column>
      <el-table-column prop="remainDays" label="remain days"> </el-table-column>
      <el-table-column prop="expectReturnTime" label="expectreturn time">
      </el-table-column>
      <el-table-column prop="barcode" label="barcode"> </el-table-column>
      <!-- <el-table-column
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
            >refuse</el-tag
          >
        </template>
      </el-table-column> -->
      <el-table-column label="operation" width="320" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-edit"
            @click="alongReturnBtn(scope.row)"
            >return</el-button
          >
          <el-button
            type="danger"
            size="small"
            icon="el-icon-edit"
            @click="ExceptionReturnBtn(scope.row)"
            >can't return</el-button
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
    <!-- 异常还书弹出框 -->
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
          :model="exception"
          ref="exceptionRef"
          :rules="rules"
          label-width="120px"
          size="small"
        >
          <el-form-item label="exception type" prop="type">
            <el-select v-model="exception.type" placeholder="please select">
              <el-option
                v-for="item in exoptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="exceptionText" label="remark">
            <el-input
              type="textarea"
              v-model="exception.exceptionText"
            ></el-input>
          </el-form-item>
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
        <img :src="this.barcodePic" />
        {{ this.myBarcode }}
      </div>
    </sys-dialog>
    <!-- 通过扫描条形码还书弹出框 -->
    <sys-dialog
      :title="rdialog.title"
      :width="rdialog.width"
      :height="rdialog.height"
      :visible="rdialog.visible"
      @onClose="ronClose"
      @onConfirm="ronConfirm"
    >
      <div slot="content">
        <el-form
          :model="rparm"
          ref="codeRef"
          label-width="80px"
          :inline="false"
          size="normal"
        >
          <el-form-item label="barcode">
            <el-input v-model="rparm.barcode" placeholder="please enter barcode"
              >barcode</el-input
            >
          </el-form-item>
        </el-form>
      </div>
    </sys-dialog>
    <!-- 通过扫描条形码异常还书弹出框 -->
    <sys-dialog
      :title="edialog.title"
      :width="edialog.width"
      :height="edialog.height"
      :visible="edialog.visible"
      @onClose="eonClose"
      @onConfirm="eonConfirm"
    >
      <div slot="content">
        <el-form
          :model="exceptionByBar"
          ref="exceptionByBarRef"
          :rules="erules"
          label-width="120px"
          size="small"
        >
          <el-form-item label="barcode">
            <el-input
              v-model="exceptionByBar.barcode"
              placeholder="please enter barcode"
              >barcode</el-input
            >
          </el-form-item>
          <el-form-item label="exception type" prop="type">
            <el-select
              v-model="exceptionByBar.type"
              placeholder="please select"
            >
              <el-option
                v-for="item in exoptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="exceptionText" label="remark">
            <el-input
              type="textarea"
              v-model="exceptionByBar.exceptionText"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
    </sys-dialog>
  </el-main>
</template>


<script>
import qs from "qs"; //引入qs插件
import { getUserId } from "@/utils/auth";
import {
  ExceptionReturnByBarApi,
  returnByBarApi,
  returnBorrowApi,
  returnBooksApi,
  exceptionBooksApi,
} from "@/api/borrow";
import SysDialog from "@/components/dialog/SysDialog.vue";
import http from "@/utils/http";
export default {
  components: {
    SysDialog,
  },
  data() {
    return {
      //异常扫码还书弹出框
      edialog: {
        width: 630,
        height: 180,
        title: "exception return book",
        visible: false,
      },
      //扫码还书弹出框
      rdialog: {
        width: 630,
        height: 60,
        title: "return book",
        visible: false,
      },
      //扫码还书输入框
      rparm: {
        barcode: "",
      },
      // tableCheckList: [], //存放选中该行的表单数据
      //存放当前行对应的barcode
      myBarcode: "",
      //条形码图片的链接
      barcodePic: "",
      //条形码弹框
      codeDialog: {
        height: 120,
        width: 500,
        title: "barcode",
        visible: false,
      },
      //异常还书弹出框表单验证
      rules: {
        type: [
          {
            required: true,
            message: "please select exception type",
            trigger: "change",
          },
        ],
        exceptionText: [
          { required: true, message: "please enter remark", trigger: "change" },
        ],
      },
      //异常还书弹出框表单验证
      erules: {
        type: [
          {
            required: true,
            message: "please select exception type",
            trigger: "change",
          },
        ],
        exceptionText: [
          { required: true, message: "please enter remark", trigger: "change" },
        ],
      },
      //异常还书状态 下拉选择框
      exoptions: [
        {
          value: "0",
          label: "damage",
        },
        {
          value: "1",
          label: "lost",
        },
      ],
      //异常还书弹出框表单数据
      exception: {
        borrowId: "",
        bookId: "",
        type: "",
        exceptionText: "",
      },
      //扫码异常还书弹出框表单数据
      exceptionByBar: {
        barcode: "",
        type: "",
        exceptionText: "",
      },
      //弹框属性定义
      dialog: {
        width: 630,
        height: 150,
        title: "return book remark",
        visible: false,
      },
      bookIds: [], //记录要还的书
      tableHeight: 0,
      tableData: [],
      listParm: {
        userId: getUserId(),
        currentPage: 1,
        pageSize: 10,
        username: "",
        borrowStatus: "1",
        total: 0,
      },
      options: [
        // {
        //   value: "0",
        //   label: "auditing",
        // },
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
          label: "refuse",
        },
      ],
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  created() {
    this.returnBorrow();
  },
  methods: {
    //通过扫描条形码还书弹出框
    async ronConfirm() {
      let confirm = await this.$myconfirm("Are you sure to return this book?");
      if (confirm) {
        let res = await returnByBarApi({ barcode: this.rparm.barcode });
        if (res && res.code == 200) {
          this.$message.success(res.msg);
          this.returnBorrow();
        } else {
          this.$message({ type: "error", message: res.msg });
        }
      }
      this.rdialog.visible = false;
    },
    ronClose() {
      this.rdialog.visible = false;
    },

    codeReturnBtn() {
      this.$resetForm("codeRef", this.rparm);
      this.rdialog.visible = true;
    },

    //扫码异常还书弹框
    codeExceptionReturnBtn() {
      this.$resetForm("exceptionByBarRef", this.exceptionByBar);
      this.edialog.visible = true;
    },
    async eonConfirm() {
      let confirm = await this.$myconfirm("Are you sure to return this book?");
      if (confirm) {
        let res = await ExceptionReturnByBarApi(this.exceptionByBar);
        if (res && res.code == 200) {
          this.$message.success(res.msg);
          this.returnBorrow();
        } else {
          this.$message({ type: "error", message: res.msg });
        }
      }
      this.edialog.visible = false;
    },
    eonClose() {
      this.edialog.visible = false;
    },
    //展示条形码 按钮
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
    //异常还书弹出框
    onConfirm() {
      this.$refs.exceptionRef.validate(async (valid) => {
        if (valid) {
          let res = await exceptionBooksApi(this.exception);
          if (res && res.code == 200) {
            this.$message.success(res.msg);
            this.returnBorrow();
            this.dialog.visible = false;
          } else {
            this.$message({ type: "error", message: res.msg });
          }
        }
      });
    },
    onClose() {
      this.dialog.visible = false;
    },
    //异常还书 比如书坏了，就点这个按钮还书
    ExceptionReturnBtn(row) {
      this.$resetForm("exceptionRef", this.exception);
      this.exception.borrowId = row.borrowId;
      this.exception.bookId = row.bookId;
      this.dialog.visible = true;
    },
    //单独还书按钮
    async alongReturnBtn(row) {
      this.bookIds = [];
      console.log(row);
      let confirm = await this.$myconfirm("Are you sure to return books?");
      if (confirm) {
        if (row.borrowStatus == "1") {
          let obj = {};
          obj.borrowId = row.borrowId;
          obj.bookId = row.bookId;
          this.bookIds.push(obj);
          let res = await returnBooksApi(this.bookIds);
          if (res && res.code == 200) {
            this.$message.success(res.msg);
            this.returnBorrow();
          } else {
            this.$message({ type: "error", message: res.msg });
          }
        }
      }
    },
    currentChange(val) {
      this.listParm.currentPage = val;
      this.returnBorrow();
    },
    sizeChange(val) {
      this.listParm.pageSize = val;
      this.returnBorrow();
    },
    //还书列表
    async returnBorrow() {
      let res = await returnBorrowApi(this.listParm);
      if (res && res.code == 200) {
        console.log(res);
        this.tableData = res.data.records;
        this.listParm.total = res.data.total;
        console.log(this.tableData);
      } else {
        this.$message({ type: "error", message: res.msg });
      }
    },
    //搜索按钮
    searchBtn() {
      this.returnBorrow();
    },
    //重置按钮
    resetBtn() {
      this.listParm.currentPage = 1;
      this.listParm.username = "";
      this.listParm.borrowStatus = "";
      this.returnBorrow();
    },
    //批量还书按钮
    async returnBtn() {
      this.bookIds = [];
      let confirm = await this.$myconfirm("Are you sure to return books?");
      if (confirm) {
        let datas = this.$refs.tables.selection; //获取选中的书
        if (datas.length == 0) {
          this.$message.error("please select books that you want to return");
          return;
        }
        for (let i = 0; i < datas.length; i++) {
          let obj = {};
          obj.borrowId = datas[i].borrowId;
          obj.bookId = datas[i].bookId;
          this.bookIds.push(obj);
        }
        let res = await returnBooksApi(this.bookIds);
        if (res && res.code == 200) {
          this.$message.success(res.msg);
          this.returnBorrow();
        } else {
          this.$message({ type: "error", message: res.msg });
        }
      }
    },
  },
};
</script>

<style>
</style>