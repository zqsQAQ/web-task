<template>
  <el-main height="">
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
        <el-button @click="resetBtn" icon="el-icon-close">reset</el-button>
        
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
      <el-table-column label="operation" width="120" align="center">
        <template slot-scope="scope">

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
    <!-- 新增编辑弹框 -->
    <sys-dialog
      :title="dialog.title"
      :height="dialog.height"
      :width="dialog.width"
      :visible="dialog.visible"
      @onClose="onClose"
      @onConfirm="onConfirm"
    >
      <div slot="content">
        <el-form
          :model="addModel"
          ref="addRef"
          :rules="rules"
          label-width="100px"
          size="small"
          style="margin-left: 0px"
        >
          <el-row :gutter="20">
            <el-col :span="12" :offset="0">
              <el-form-item label="ISBN" prop="bookCode">
                <el-input v-model="addModel.bookCode"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-button size="small" icon="el-icon-search" @click="getByISBN"
                >search</el-button
              >
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18" :offset="0">
              <el-form-item label="category" prop="categoryId">
                <el-select
                  v-model="addModel.categoryId"
                  placeholder="please select category"
                >
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
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18" :offset="0">
              <el-form-item label="book name" prop="bookName">
                <el-input v-model="addModel.bookName"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18" :offset="0">
              <el-form-item label="auther" prop="bookAuther">
                <el-input v-model="addModel.bookAuther"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18" :offset="0">
              <el-form-item label="press" prop="bookProduct">
                <el-input v-model="addModel.bookProduct"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18" :offset="0">
              <el-form-item label="price" prop="bookPrice">
                <el-input v-model="addModel.bookPrice"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18" :offset="0">
              <el-form-item label="stock" prop="bookStore">
                <el-input v-model="addModel.bookStore"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="floor" prop="bookFloor">
                <el-select
                  v-model="addModel.bookFloor"
                  placeholder="please select floor"
                >
                  <el-option
                    v-for="item in floorOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="shelf" prop="bookPlaceNum">
                <el-select
                  v-model="addModel.bookPlaceNum"
                  placeholder="please select shelf"
                >
                  <el-option
                    v-for="item in shelfOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
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
        <!-- <div>
          <div class="imgIcon" @click="downImgZip"></div>
          <el-button type="success" @click="downImgZip">下载图片</el-button>
        </div>

        <el-table
          :row-key="getRowKey"
          @selection-change="handleSelectionChange"
        >
          <el-table-column
            type="selection"
            :reserve-selection="true"
            align="center"
            width="55"
          >
          </el-table-column>
          <div
            v-for="(item, index) in arr"
            :key="index"
            style="margin-top: 20px"
          >
            <img :src="item.parmBarcodePic" />
            {{ item.parmBarcode }}
          </div>
        </el-table> -->
        <div v-for="(item, index) in arr" :key="index" style="margin-top:20px">
          <img :src="item.parmBarcodePic" />
          {{item.parmBarcode}}
        </div>
      </div>
    </sys-dialog>
  </el-main>
</template>

<script>
import qs from "qs"; //引入qs插件
import {
  getListApi,
  getCateListApi,
  addBookApi,
  editBookApi,
  deleteBookApi,
  getByISBNApi,
  getBarcodeApi,
} from "@/api/books";
import SysDialog from "@/components/dialog/SysDialog.vue";
import http from "@/utils/http";
export default {
  components: {
    SysDialog,
  },
  data() {
    return {
      // isbnParm:{
      //   sub:"",
      //   key:"7e2601995fe5646193ebbc1e8a032c59"
      // },
      // isbnParm:{
      //   key:"d7ba9fa7634764f2fd5bb81e8183ce18",
      //   isbn:"",
      // },
      // tableCheckList: [], //存放选中该行的表单数据
      //存放barcode和条形码图片链接的对象数组 用来在v-for循环里展示
      arr: [],
      //存放接口返回的数据（包含barcode）
      content: [],
      //存放barcode的数组
      barcodeArr: [],
      //存放条形码图片链接的数组
      barcodePic: [],
      //表单验证规则
      rules: {
        bookCode: [
          {
            required: true,
            trigger: "blur",
            message: "please enter ISBN",
          },
        ],
        categoryId: [
          {
            required: true,
            trigger: "blur",
            message: "please select category",
          },
        ],
        bookName: [
          {
            required: true,
            trigger: "blur",
            message: "please enter book name",
          },
        ],
        isbn: [
          {
            required: true,
            trigger: "blur",
            message: "please enter ISBN",
          },
        ],
        bookPlaceNum: [
          {
            required: true,
            trigger: "blur",
            message: "please select shelf",
          },
        ],
        bookFloor: [
          {
            required: true,
            trigger: "blur",
            message: "please select floor",
          },
        ],
        bookAuther: [
          {
            required: true,
            trigger: "blur",
            message: "please enter auther",
          },
        ],
        bookProduct: [
          {
            required: true,
            trigger: "blur",
            message: "please enter press",
          },
        ],
        bookPrice: [
          {
            required: true,
            trigger: "blur",
            message: "please enter price",
          },
        ],
        bookStore: [
          {
            required: true,
            trigger: "blur",
            message: "please enter stock",
          },
        ],
      },
      //表单绑定的数据
      addModel: {
        type: "", //0新增 1编辑
        bookId: "", //弹筐里没有该属性，但是编辑的时候需要该属性
        categoryId: "",
        bookName: "",
        categoryName: "",
        bookPlaceNum: "",
        bookFloor: "",
        bookAuther: "",
        bookProduct: "",
        bookPrice: "",
        bookStore: "",
        bookCode: "", //ISBN  查询用的
      },
      //调用ISBN API需要的参数
      // searchParm: {
      //   isbn: "",
      //   apikey:
      //     "12567.c3cfbb03e21403897d9bd7d58f75aa8f.1ec4a8d79892eeeda2c522f589b588f6",
      // },
      // searchParm: {
      //   key: "d7ba9fa7634764f2fd5bb81e8183ce18",
      //   isbn: "",
      // },
      //新增编辑弹框属性
      dialog: {
        height: 400,
        width: 500,
        title: "",
        visible: false,
      },
      //条形码弹框属性
      codeDialog: {
        height: 400,
        width: 500,
        title: "bar code list",
        visible: false,
      },
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
      //表格高度
      tableHeight: 0,
      //表格数据
      tableData: [],
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
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  created() {
    this.getCateList();
    this.getList();
  },
  methods: {
    // getRowKey(row) {
    //   //拿到该选中的表单的id
    //   return row.id;
    // },
    // handleSelectionChange(val) {
    //   //拿到该选中的表单的全部数据
    //   this.tableCheckList = val;
    // },
    // downImgZip() {
    //   let ids = [];
    //   //if (this.shitumos == 2) {
    //   //ids = this.$refs.shitumosDialog.checkList;//获取另一个视图选中的id（根据需求决定这个是否需要）
    //   //} else {
    //   this.tableCheckList.map((item) => {
    //     //遍历获取表格的选中的id
    //     // console.log(item.id);
    //     ids.push(item.id);
    //   });
    //   //}
    //   if (ids.length == 0) {
    //     this.$notify({
    //       title: "提示",
    //       message: "请勾选需要下载的图片",
    //       type: "warning",
    //       duration: 2000,
    //     });
    //     return;
    //   }
    //   let param = {
    //     ids: ids,
    //     ...this.dataForm,
    //     //...JSON.parse(JSON.stringify(this.dataForm))//dataForm为获取到的数据，里面不能有ids
    //   };
    //   if (this.dataForm.cllxLb && this.dataForm.cllxLb.length) {
    //   params.cllx = this.dataForm.cllxLb.join()
    //   }			//注释的根据需求来写
    //   delete param.pageNum;//删除不要的参数
    //   delete param.pageSize;
    //   let dataUrl = `download?` + qs.stringify(param);
    //   window.open(dataUrl); //用window.open()实现下载到本地
    // },

    //根据ISBN获取图书的部分信息  搜索按钮
    //ISBN : this.addModel.isbn
    // async getByISBN() {
    //   let res = await getByISBNApi(this.searchParm);
    //   console.log(res);
    //   if (res && res.code == 200 && res.data) {
    //     this.addModel.bookName = res.data.name;
    //     this.addModel.bookAuther = res.data.author;
    //   }
    // },
    //  getByISBN() {
    //     this.$http.get('http://118.31.113.49/api/isbn/v1/index?key=d7ba9fa7634764f2fd5bb81e8183ce18&isbn='+ this.addModel.isbn)
    //       .then(res => {
    //         if (res.data.code == 200) {
    //           // this.findPager(); //调用方法查询
    //           this.$message({ //弹出消息对话框
    //             message: res.data.message,
    //             type: 'success'
    //           });

    //           console.log(res);
    //         }
    //       }).catch(e => {
    //       this.$message.error('错了哦，网络异常'); //弹出网络异常提示
    //     })
    //   },
    //展示条形码 按钮
    async codeBtn(row) {
      this.arr = [];
      this.content = [];
      this.barcodePic = [];
      this.barcodeArr = [];
      this.content = await (
        await getBarcodeApi({ bookCode: row.bookCode })
      ).data; //存放接口返回的数据（包含barcode）
      console.log(row.bookCode);
      console.log(this.content);
      this.barcodeArr = []; //存放barcode
      for (let i = 0; i < this.content.length; i++) {
        this.barcodeArr[i] = this.content[i].barcode;
      }
      // console.log(this.barcodeArr)

      this.codeDialog.visible = true;
      //调用公共条形码接口  生成条形码
      let barcodeData = []; //存放接口返回的数据 包含条形码图片链接
      for (let i = 0; i < this.content.length; i++) {
        barcodeData[i] = await http.get(
          "https://www.mxnzp.com/api/barcode/create",
          {
            content: this.barcodeArr[i],
            width: "500",
            height: "100",
            type: "0",
            app_id: "8dqilgo2oixxcnnj",
            app_secret: "RElTVC9wZlRMdkNmRWJZb0tFZDEwQT09",
          }
        );
        this.barcodePic.push(barcodeData[i].data.barCodeUrl); //barcodePic存放条码图片链接
        let parm = {
          parmBarcode: this.barcodeArr[i],
          parmBarcodePic: this.barcodePic[i],
        };
        console.log(parm);
        this.arr.push(parm);
        // // console.log(barCode[i].data.barCodeUrl);
      }
      console.log(this.arr);
      // console.log(this.barcodePic);
    },
    //条形码弹框关闭
    codeOnClose() {
      this.codeDialog.visible = false;
    },
    //条形码弹框确定
    codeOnConfirm() {
      this.codeDialog.visible = false;
    },
    //通过ISBN搜索图书信息  调用公共ISBN接口
    async getByISBN() {
      let res = await getByISBNApi(this.addModel.bookCode);
      this.addModel.bookName = res.data.name;
      this.addModel.bookAuther = res.data.author;
      this.addModel.bookProduct = res.data.publishing;
      this.addModel.bookPrice = res.data.price;
      // console.log(this.isbnParm);
      // this.isbnParm.isbn = this.addModel.bookCode;
      // console.log(this.isbnParm)
      // let res = await getByISBNApi(this.isbnParm);
      // console.log(res);
      // this.addModel.bookName = res.data.name;
      // this.addModel.bookAuther = res.data.author;
      // this.addModel.bookProduct = res.data.publishing;
      // this.addModel.bookPrice = res.data.price;
    },
    async getList() {
      let res = await getListApi(this.listParm);
      if (res && res.code == 200) {
        console.log(res.data);
        this.tableData = res.data.records;
        this.listParm.total = res.data.total;
      }
    },
    //弹框关闭
    onClose() {
      this.dialog.visible = false;
    },
    //弹框确定
    onConfirm() {
      this.$refs.addRef.validate(async (valid) => {
        if (valid) {
          let res = null;
          if (this.addModel.type == "0") {
            res = await addBookApi(this.addModel);
          } else {
            res = await editBookApi(this.addModel);
          }
          if (res && res.code == 200) {
            //信息提示
            this.$message({ type: "success", message: res.msg });
            //刷新表格
            this.getList();
            //关闭弹框
            this.dialog.visible = false;
          }
        }
      });
    },
    //查询图书类别的列表
    async getCateList() {
      let res = await getCateListApi();
      if (res && res.code == 200) {
        console.log(res);
        this.options = res.data;
      }
    },
    //页容量改变时
    sizeChange(val) {
      this.listParm.pageSize = val;
      this.getList();
    },
    //页数改变时
    currentChange(val) {
      this.listParm.currentPage = val;
      this.getList();
    },
    //编辑按钮
    editBtn(row) {
      this.dialog.title = "edit book";
      this.dialog.visible = true;
      //清空表单
      this.$resetForm("addRef", this.addModel);
      //把当前数据复制到表单绑定的数据域里面
      this.$objCoppy(row, this.addModel);
      //设为编辑
      this.addModel.type = "1";
    },
    //删除按钮
    async deleteBtn(row) {
      let confirm = await this.$myconfirm("Are you sure to delete this data?");
      if (confirm) {
        let res = await deleteBookApi({ bookId: row.bookId });
        if (res && res.code == 200) {
          //信息提示
          this.$message({ type: "success", message: res.msg });
          //刷新表格
          this.getList();
        }
      }
    },
    //新增按钮
    addBtn() {
      this.dialog.title = "add book";
      this.dialog.visible = true;
      //清空表单
      this.$resetForm("addRef", this.addModel);
      //设为新增
      this.addModel.type = "0";
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
  },
};
</script>

<style lang="scss" scoped>
</style>