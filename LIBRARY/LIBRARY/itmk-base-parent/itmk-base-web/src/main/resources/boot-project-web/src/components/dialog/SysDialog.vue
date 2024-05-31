<template>
    <el-dialog
        :title="title"
        :visible.sync="visible"
        :width="width + 'px'"
        :before-close="onClose"
        :close-on-click-modal='false'  
        append-to-body
    >
        <div class="containner" :style="{height: height + 'px'}">
            <slot name="content"></slot>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button type="danger" @click="onClose">cancel</el-button>
            <el-button type="primary" @click="onConfirm">confirm</el-button>
        </span>
    </el-dialog>
</template>

<script>
export default {
    props:{  //子组件通过props接收父组件传递来的参数
        title:{    //弹框标题
            type:String,
            default:'标题'
        },
        visible:{  //弹框是否显示
            type:Boolean,
            default:false
        },
        width:{
            type:Number,
            default:630
        },
        height:{
            type:Number,
            default:250
        }
    },
    methods:{
        //https://juejin.cn/post/6865992194084306951   this.$emit解释
        onClose(){    
            this.$emit('onClose')   
        },
        onConfirm(){
            this.$emit('onConfirm')
        }
    }
};
</script>

<style lang="scss" scoped>
.containner{
  overflow-x: initial;
  overflow-y: auto;
}
        
.el-dialog__wrapper {
  ::v-deep .el-dialog {    //v-deep  深度查找
    border-top-left-radius: 7px !important;
    border-top-right-radius: 7px !important;
    .el-dialog__header {
      border-top-left-radius: 7px !important;
      border-top-right-radius: 7px !important;
      background-color: #1890ff;
      .el-dialog__title {
        color: #fff;
        font-size: 15px;
        font-weight: 700;
      }
      .el-dialog__close {
        color: #fff;
      }
    }
    .el-dialog__body {
      padding: 10px 10px !important;
    }
    .el-dialog__footer {
      border-top: 1px solid #e8eaec !important;
      padding: 10px !important;
    }
  }
}
::-webkit-scrollbar {
    display: none;  //取消滚动条
    }

</style>