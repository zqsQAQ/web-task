<template>
  <el-main height="">
    <!-- Main content -->
    <!-- <el-alert title="成功提示的文案" type="success" show-icon> </el-alert>
    <el-alert title="消息提示的文案" type="info" show-icon> </el-alert>
    <el-alert title="警告提示的文案" type="warning" show-icon> </el-alert>
    <el-alert title="错误提示的文案" type="error" show-icon> </el-alert> -->
    <div class="dashboard-container">
      <!-- <div class="dashboard-text">name: {{ name }}</div>
    <div class="dashboard-text">roles: <span v-for="role in roles" :key="role">{{ role }}</span></div> -->
      <h1>welcome to our Library</h1>
      <p>
        This is the first time we have done such a project. Although we
        encountered many difficulties, we did not give up and finally presented
        the work here.
      </p>
      <p>
        Here, you can borrow and return books, but you will be charged a fine
        for returning books overdue.
      </p>
    </div>
  </el-main>
</template>

<script>
import { mapGetters } from "vuex";
import { getInfo } from "@/api/user";
import { getUserId } from "@/utils/auth";
import { unReturnNoticeApi, unpaidNoticeApi,reserveNoticeApi } from "@/api/dashboard";
import http from "@/utils/http";
export default {
  name: "Dashboard",
  computed: {
    ...mapGetters(["name", "roles"]),
  },
  data() {
    return {
      parm: {
        readerId: getUserId(),
      },
    };
  },
  mounted() {
    this.unReturnNotice();
    this.unpaidNotice();
    this.reserveNotice();
  },
  methods: {
    //还书通知
    async unReturnNotice() {
      if (this.parm.readerId >= 24) {
        let res = await unReturnNoticeApi(this.parm);
        console.log("还书通知：" + res);
        if (res.code == 200) {
          this.$message.success(res.msg);
        } else {
          this.$message.error(res.msg);
        }
      }
    },
    //交罚款通知
    async unpaidNotice() {
      if (this.parm.readerId >= 24) {
        let res = await unpaidNoticeApi(this.parm);
        console.log("交罚款通知：" + res);
        if (res.code == 200) {
          this.$message.success(res.msg);
        } else {
          this.$message.error(res.msg);
        }
      }
    },
    //待取书通知
    async reserveNotice() {
      if (this.parm.readerId >= 24) {
        let res = await reserveNoticeApi(this.parm);
        console.log("待取书通知" + res);
        if (res.code == 200) {
          this.$message.success(res.msg);
        } else {
          this.$message.error(res.msg);
        }
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
h1 {
  margin-top: 100px;
  text-align: center;
}
p {
  text-align: center;
}
</style>
