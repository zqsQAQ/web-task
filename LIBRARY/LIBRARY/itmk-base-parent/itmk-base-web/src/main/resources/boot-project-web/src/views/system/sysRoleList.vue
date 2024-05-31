<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="listParm" label-width="80px" :inline="true" size="small">
      <el-form-item>
        <el-input
          v-model="listParm.roleName"
          placeholder="please enter role name"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="searchBtn" icon="el-icon-search">search</el-button>
        <el-button @click="resetBtn" icon="el-icon-close">reset</el-button>
        <el-button type="primary" @click="addBtn" icon="el-icon-plus"
          >add</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableData" border stripe>
      <el-table-column prop="roleName" label="role name"></el-table-column>
      <el-table-column prop="roleType" label="role type">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.roleType == '1'">librarian</el-tag>
          <el-tag type="success" v-if="scope.row.roleType == '2'">member</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="role remark"></el-table-column>
      <el-table-column label="operation" align="center" width="300px">
        <!-- https://www.csdn.net/tags/MtTaIgwsNjgwMDUxLWJsb2cO0O0O.html  
              slot-scope="scope"的解释  取得作用域插槽:data绑定的数据
        -->
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="small"
            @click="editBtn(scope.row)"
            icon="el-icon-edit"
            >edit</el-button
          >
          <el-button
            type="danger"
            size="small"
            @click="deleteBtn(scope.row)"
            icon="el-icon-delete"
            >delete</el-button
          >
           <el-button
            type="primary"
            size="small"
            @click="assignBtn(scope.row)"
            icon="el-icon-edit"
            >assign</el-button
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
      :visible="dialog.visible"
      @onConfirm="onConfirm"
      @onClose="onClose"
    >
      <div slot="content">
        <el-form
          :model="addModel"
          ref="addRef"
          :rules="rules"
          label-width="80px"
          size="small"
        >
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="roleName" label="name">
                <el-input v-model="addModel.roleName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="remark">
                <el-input v-model="addModel.remark"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="role type">
                <el-select
                  v-model="addModel.roleType"
                  placeholder="please select"
                >
                  <el-option
                    v-for="item in options"
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
    <!-- 分配权限弹框 -->
    <sys-dialog
      :title="assignDialog.title"
      :width="assignDialog.width"
      :height="assignDialog.height"
      :visible="assignDialog.visible"
      @onClose="assignClose"
      @onConfirm="assignConfirm"
    >
      <template slot="content">
        <el-tree
          ref="assignTree"
          :data="assignTreeData"
          node-key="menuId"
          :props="defaultProps"
          empty-text="no data"
          :show-checkbox="true"
          default-expand-all
          :default-checked-keys="assignTreeChecked"
        ></el-tree>
      </template>
    </sys-dialog>
  </el-main>
</template>

<script>
import SysDialog from "@/components/dialog/SysDialog.vue";
import { getListApi, addRoleApi, editRoleApi, deleteRoleApi, getAssingShowApi, saveAssignApi } from "@/api/role";
export default {
  components: {
    SysDialog,
  },
  data() {
    return {
      defaultProps:{
        children: 'children',
        label:'title'
      },
      //类型选择
      options: [
        {
          value: "1",
          label: "librarian",
        },
        {
          value: "2",
          label: "member",
        },
      ],
      //表单验证规则
      rules: {
        roleName: [
          {
            title: "blur",
            required: true,
            message: "please enter role name",
          },
        ],
      },
      //弹框中表单数据
      addModel: {
        type: "", //区分是新增还是编辑 0新增 1编辑
        roleId: "",
        roleName: "",
        remark: "",
        roleType: "",
      },
      //弹框属性
      dialog: {
        title: "",
        height: 150,
        visible: false,
      },
      //表格高度
      tableHeight: 0,
      //列表查询参数
      listParm: {
        roleName: "",
        currentPage: 1,
        pageSize: 10,
        total: 0,
      },
      tableData: [],
      //角色id
      roleId:'',
      //树的数据
      assignTreeData:[],
      //角色原来的权限（被选中的）
      assignTreeChecked:[],
      //分配权限弹框的属性
      assignDialog:{
        title:'',
        visible:false,
        width:300,
        height:450
      },
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
  },
  created() {
    this.getList();
  },
  methods: {
    assignClose(){
      this.assignDialog.visible =false;
    },
    async assignConfirm(){
      //获取树选中的数据
      let ids = this.$refs.assignTree
        .getCheckedKeys()
        .concat(this.$refs.assignTree.getHalfCheckedKeys());
      console.log(ids);
      let parm = {
        roleId: this.roleId,
        list: ids,
      };
      let res = await saveAssignApi(parm);
      if (res && res.code == 200) {
         this.$message({ type: "success", message: res.msg });
        this.assignDialog.visible = false;
      }
    },
    async assignBtn(row){
      //清空数据
      this.roleId = "";
      this.assignTreeData = [];
      this.assignTreeChecked = [];
      this.roleId = row.roleId;
      //设置弹框属性
      this.assignDialog.title = "assign permissions for [" + row.roleName + "]";
      this.assignDialog.visible = true;
      //获取数据
      let parm = {
        userId:'1',
        roleId:this.roleId,
      }
      let res = await getAssingShowApi(parm);
      console.log(res);
      if(res && res.code == 200){
        this.assignTreeData = res.data.menuList;
        this.assignTreeChecked = res.data.checkList;
      }
      //如果原来有权限
      if(this.assignTreeChecked.length > 0){
        let newArr = [];
        this.assignTreeChecked.forEach(item =>{
          this.checked(item,this.assignTreeData,newArr);
        })
        this.assignTreeChecked = newArr;
      }

    },
    //找出所有的回显数据
    checked(id, data,newArr){
      data.forEach(item =>{
        if(item.menuId == id){
          //如果是末级
          if(item.children && item.children.length == 0){
            newArr.push(item.menuId);
          }
        }else{
          if(item.children && item.children.length != 0){
            this.checked(id,item.children,newArr);
          }
        }
      })
    },
    //搜索按钮
    searchBtn() {
      this.getList();
    },
    //重置按钮
    resetBtn() {
      this.listParm.roleName = "";
      this.getList();
    },
    //新增按钮
    addBtn() {
      //设置弹框属性
      this.dialog.visible = true;
      this.dialog.title = "add role";
      //清空表单数据
      this.$resetForm("addRef", this.addModel);
      //设为新增
      this.addModel.type = 0;
    },
    //编辑按钮
    editBtn(row) {
      //设置弹框属性
      this.dialog.visible = true;
      this.dialog.title = "edit role";
      //清空表单数据
      this.$resetForm("addRef", this.addModel);
      //把要编辑的数据放到表单数据对象里
      this.$objCoppy(row, this.addModel); //把row放到addModel里面
      //设为编辑
      this.addModel.type = "1";
    },
    //删除按钮
    async deleteBtn(row) {
      //弹出框：确定删除吗
      let confirm = await this.$myconfirm("Are you sure to delete this data?");
      if (confirm) {
        let res = await deleteRoleApi({ roleId: row.roleId });
        if (res && res.code == 200) {
          //信息提示
          this.$message({ type: "success", message: res.msg });
          //刷新表格
          this.getList();
        }
      }
    },
    //页容量改变时
    sizeChange(val) {
      this.listParm.pageSize = val;
      this.getList();
    },
    //当前页数改变时
    currentChange(val) {
      this.listParm.currentPage = val;
      this.getList();
    },
    //获取列表
    async getList() {
      let res = await getListApi(this.listParm);
      if (res && res.code == 200) {
        console.log(res);
        //设置表格数据
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
      //表单验证
      this.$refs.addRef.validate(async (valid) => {
        if (valid) {
          this.dialog.visible = false;
          let res = null;
          if (this.addModel.type == "0") {
            res = await addRoleApi(this.addModel);
          } else {
            res = await editRoleApi(this.addModel);
          }
          if (res && res.code == 200) {
            //信息提示
            this.$message({ type: "success", message: res.msg });
            //刷新表格
            this.getList();
          }
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
</style>