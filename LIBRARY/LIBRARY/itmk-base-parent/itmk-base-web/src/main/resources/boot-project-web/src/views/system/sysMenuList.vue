<template>
  <el-main>
    <!-- add按钮 -->
    <el-form size="small">
      <el-form-item>
        <el-button type="primary" icon="el-icon-plus" @click="addBtn"
          >add</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table
      :height="tableHeight"
      :data="tableData"
      border
      stripe
      row-key="menuId"
      default-expand-all
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="title" label="menu name"> </el-table-column>
      <el-table-column prop="icon" label="menu icon">
        <template slot-scope="scope">
          <i :class="scope.row.icon"></i>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="menu type">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type == '0'">directory</el-tag>
          <el-tag type="success" v-if="scope.row.type == '1'">menu</el-tag>
          <el-tag type="danger" v-if="scope.row.type == '2'">button</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="route name"> </el-table-column>
      <el-table-column prop="path" label="route path"> </el-table-column>
      <el-table-column prop="code" label="permission"> </el-table-column>
      <el-table-column label="operation" align="center" width="230">
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
        </template>
      </el-table-column>
    </el-table>
    <!-- 新增编辑弹框 -->
    <SysDialog
      :title="dialog.title"
      :height="dialog.height"
      :visible="dialog.visible"
      :width="dialog.width"
      @onConfirm="onConfirm"
      @onClose="onClose"
    >
      <div slot="content">
        <el-form
          :model="addModel"
          ref="addRef"
          :rules="rules"
          label-width="135px"
          size="small"
        >
          <el-row>
            <el-col :span="24" :offset="0">
              <el-form-item label="menu type" prop="type">
                <el-radio-group v-model="addModel.type">
                  <el-radio :label="'0'">directory</el-radio>
                  <el-radio :label="'1'">menu</el-radio>
                  <el-radio :label="'2'">button</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="parent menu" prop="parentName">
                <el-input
                  v-model="addModel.parentName"
                  @click.native="selectParent"
                ></el-input>
                <!-- <el-input v-model="addModel.parentId" type="hidden"></el-input> -->
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="menu name" prop="title">
                <el-input v-model="addModel.title"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="menu icon" v-if="addModel.type != '2'">
                <el-input v-model="addModel.icon"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item
                label="router name"
                v-if="addModel.type == '1'"
                prop="name"
              >
                <el-input v-model="addModel.name"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="addModel.type == '1'">
            <el-col :span="12" :offset="0">
              <el-form-item label="router path" prop="path">
                <el-input v-model="addModel.path"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="component path" prop="url">
                <el-input v-model="addModel.url"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item label="permission field">
                <el-input v-model="addModel.code"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item label="menu number">
                <el-input v-model="addModel.orderNum"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </SysDialog>
    <!-- 上级菜单 -->
    <sys-dialog
      :title="parentDialog.title"
      :height="parentDialog.height"
      :width="parentDialog.width"
      :visible="parentDialog.visible"
      @onConfirm="parentConfirm"
      @onClose="parentClose"
    >
      <div slot="content">
        <el-tree
          ref="parentTree"
          :data="treeData"
          node-key="menuId"
          :props="defaultProps"
          empty-text="暂无数据"
          :show-checkbox="false"
          :highlight-current="true"
          default-expand-all
          :expand-on-click-node="false"
          @node-click="handleNodeClick"
        >
          <div slot-scope="{ node, data }">
            <!-- 如果没有下级，显示文档图标 -->
            <span v-if="data.children.length == 0">
              <i style="margin-left: 3px" class="el-icon-document"></i>
            </span>
            <!-- 有下级，判断是否展开 -->
            <span v-else @click.stop="openBtn(data)">
              <i
                v-if="data.open"
                style="margin-left: 3px"
                class="el-icon-plus"
              ></i>
              <i v-else style="margin-left: 3px" class="el-icon-minus"></i>
            </span>
            <span style="margin-left: 3px">{{ node.label }}</span>
          </div>
        </el-tree>
      </div>
    </sys-dialog>
  </el-main>
</template>

<script>
import SysDialog from "@/components/dialog/SysDialog.vue";
import {
  getMenuListApi,
  getParentMenuListApi,
  addMenuApi,
  editMenuApi,
  deleteMenuApi,
} from "@/api/menu";
export default {
  components: {
    SysDialog,
  },
  data() {
    return {
      rules: {
        type: [
          {
            trigger: "blur",
            required: true,
            message: "Please select menu type",
          },
        ],
        parentName: [
          {
            trigger: "blur",
            required: true,
            message: "Please select the parent menu",
          },
        ],
        title: [
          {
            trigger: "blur",
            required: true,
            message: "Please enter menu name",
          },
        ],
        name: [
          {
            trigger: "blur",
            required: true,
            message: "Please enter router name",
          },
        ],
        path: [
          {
            trigger: "blur",
            required: true,
            message: "Please enter router path",
          },
        ],
        url: [
          {
            trigger: "blur",
            required: true,
            message: "Please enter component path",
          },
        ],
      },
      defaultProps: {
        //和后端名字保持一样
        children: "children",
        label: "title",
      },
      //上级菜单数据
      treeData: [],
      //表单数据
      addModel: {
        editType: "", //0新增 1编辑
        menuId: "",
        type: "",
        parentId: "",
        title: "",
        code: "",
        name: "",
        path: "",
        url: "",
        icon: "",
        parentName: "",
        orderNum: "",
      },
      //上级弹框属性
      parentDialog: {
        width: 300,
        title: "select parent menu",
        height: 450,
        visible: false,
      },
      //弹框属性
      dialog: {
        width: 650,
        title: "",
        height: 260,
        visible: false,
      },
      //表格高度
      tableHeight: 0,
      //表格数据
      tableData: [],
      //树中被选择的条目对应的数据
      selectNode: {
        id: "",
        title: "",
      },
    };
  },
  mounted() {
    this.$nextTick(() => {
      //设置表格高度
      this.tableHeight = window.innerHeight - 220;
    });
  },
  created() {
    this.getList();
  },
  methods: {
    //删除按钮
    async deleteBtn(row) {
      const confirm = await this.$myconfirm(
        "Are you sure to delete this data?"
      );
      if (confirm) {
        let res = await deleteMenuApi({ menuId: row.menuId });
        if (res && res.code == 200) {
          this.$message({ type: "success", message: res.msg });
          //刷新列表
          this.getList();
        }
      }
    },
    //编辑按钮
    editBtn(row) {
      //设置弹框属性
      this.dialog.title = "edit menu";
      this.dialog.visible = true;
      //清空表单
      this.$resetForm("addRef", this.addModel);
      //把要编辑的数据放到弹框表单绑定的数据里面，回显
      this.$objCoppy(row, this.addModel);
      //设为编辑
      this.addModel.editType = "1";
    },
    //上级部门节点加号和减号点击事件
    openBtn(data) {
      data.open = !data.open;
      this.$refs.parentTree.store.nodesMap[data.menuId].expanded = !data.open;
    },
    //树的点击事件
    handleNodeClick(node) {
      console.log(node);
      //获取选中的条目对应的id title
      this.selectNode.id = node.menuId;
      this.selectNode.title = node.title;
    },
    //上级菜单确定按钮
    parentConfirm() {
      //将选中的条目对应的id title赋值给表格对应的属性，展现到表格上
      this.addModel.parentId = this.selectNode.id;
      this.addModel.parentName = this.selectNode.title;
      this.parentDialog.visible = false;
      console.log(this.addModel);
    },
    //上级菜单关闭按钮
    parentClose() {
      this.parentDialog.visible = false;
    },
    //选择上级菜单事件
    async selectParent() {
      //查询上级菜单树的数据
      let res = await getParentMenuListApi();
      if (res && res.code == 200) {
        this.treeData = res.data;
      }
      this.parentDialog.visible = true;
    },
    addBtn() {
      //设置弹框属性
      this.dialog.title = "add menu";
      this.dialog.visible = true;
      //清空表单
      this.$resetForm("addRef", this.addModel);
      //设为新增
      this.addModel.editType = "0";
    },
    //弹框确定
    onConfirm() {
      this.$refs.addRef.validate(async (valid) => {
        if (valid) {
          let res = null;
          if (this.addModel.editType == "0") {
            res = await addMenuApi(this.addModel);
          } else {
            res = await editMenuApi(this.addModel);
          }
          if (res && res.code == 200) {
            this.$message({ type: "success", message: res.msg });
            //刷新列表
            this.getList();
            //关闭弹框
            this.dialog.visible = false;
          }
        }
      });
    },
    //弹框关闭
    onClose() {
      this.dialog.visible = false;
    },
    async getList() {
      let res = await getMenuListApi();
      if (res && res.code == 200) {
        this.tableData = res.data;
        console.log(res);
      }
    },
  },
};
</script>


<style lang="scss" scoped>
::v-deep .el-tree {
  // 将每一行的设置为相对定位 方便后面before after 使用绝对定位来固定位置
  .el-tree-node {
    position: relative;
    padding-left: 10px;
  }
  // 子集像右偏移 给数线留出距离
  .el-tree-node__children {
    padding-left: 20px;
  }
  //这是竖线
  .el-tree-node :last-child:before {
    height: 40px;
  }
  .el-tree > .el-tree-node:before {
    border-left: none;
  }
  .el-tree > .el-tree-node:after {
    border-top: none;
  }
  //这自定义的线 的公共部分
  .el-tree-node:before,
  .el-tree-node:after {
    content: "";
    left: -4px;
    position: absolute;
    right: auto;
    border-width: 1px;
  }
  .tree :first-child .el-tree-node:before {
    border-left: none;
  }
  // 竖线
  .el-tree-node:before {
    border-left: 1px dotted #d9d9d9;
    bottom: 0px;
    height: 100%;
    top: -25px;
    width: 1px;
  }
  //横线
  .el-tree-node:after {
    border-top: 1px dotted #d9d9d9;
    height: 20px;
    top: 14px;
    width: 24px;
  }
  .el-tree-node__expand-icon.is-leaf {
    width: 8px;
  }
  //去掉elementui自带的展开按钮  一个向下的按钮,打开时向右
  .el-tree-node__content > .el-tree-node__expand-icon {
    display: none;
  }
  //每一行的高度
  .el-tree-node__content {
    line-height: 30px;
    height: 30px;
    padding-left: 10px !important;
  }
}
//去掉最上级的before  after 即是去电最上层的连接线
::v-deep .el-tree > div {
  &::before {
    display: none;
  }
  &::after {
    display: none;
  }
}
</style>
