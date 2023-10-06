<template>
  <div>

  </div>
  <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false"
           active-text-color="primary" :default-active="activeIndex" router>
    <el-menu-item index="0" route="/">
      <template #title>
        <div style="font-size: 20px;font-weight: bold">
          AI Game PlatForm
        </div>
      </template>
    </el-menu-item>
    <el-sub-menu index="1" class="hidden-md-and-up">
      <template #title>
        菜单
      </template>
      <el-menu-item index="/" route="/">
        <template #title>首页</template>
      </el-menu-item>
      <el-menu-item index="/gamehall" route="/gamehall">
        <template #title>游戏大厅</template>
      </el-menu-item>
      <el-menu-item index="/record" route="/record">
        <template #title>对局列表</template>
      </el-menu-item>
      <el-menu-item index="/ranklist" route="/ranklist">
        <template #title>排行榜</template>
      </el-menu-item>
    </el-sub-menu>

    <el-menu-item index="/" class="hidden-sm-and-down" route="/">
      <template #title>首页</template>
    </el-menu-item>
    <el-menu-item index="/gamehall" class="hidden-sm-and-down" route="/gamehall">
      <template #title>游戏大厅</template>
    </el-menu-item>
    <el-menu-item index="/record" class="hidden-sm-and-down" route="/record">
      <template #title>对局列表</template>
    </el-menu-item>
    <el-menu-item index="/ranklist" class="hidden-sm-and-down" route="/ranklist">
      <template #title>排行榜</template>
    </el-menu-item>

    <div class="flex-grow"/>
    <div style="text-align: right; margin: 17px 10px 0 0;line-height: 14px" v-if="store.is_login">
      <div style="font-weight: bold; font-size: 16px">
        {{ store.username }}
      </div>
    </div>
    <div style="margin-top: 5px" v-if="store.is_login">
      <el-dropdown @command="handleCommand" trigger="click">
        <div class="el-dropdown-link" style="display: flex;align-items: center;">
          <el-avatar v-loading="loading" class="user-avatar"
                     :src="store.photo"
          />
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="myBot" :icon="Coin">我的Bot</el-dropdown-item>
            <el-dropdown-item :icon="Refresh">
              <el-upload
                  style="height: 22px"
                  action="https://app5990.acapp.acwing.com.cn/api/upload"
                  :limit="3"
                  :show-file-list="false"
                  v-model="userInfo.file"
                  :on-success="uploadSuccess"
                  :on-error="uploadError"
                  :on-progress="onProcess"
              >
                <span>更换头像</span>
              </el-upload>
            </el-dropdown-item>
            <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-menu>
</template>


<script lang="ts" setup>
import {useRoute} from "vue-router";

const route = useRoute()
const activeIndex = route.path
import userUseStore from '@/stores/user'
import {Coin, SwitchButton, Refresh} from "@element-plus/icons-vue";
import router from "@/router";
import {ElMessage, ElMessageBox} from "element-plus";
import {reactive, ref} from "vue";
import type {UploadProgressEvent, Action, UploadFile} from "element-plus"
import {post} from "@/util/request";

const store = userUseStore()

const {updatePhoto} = store

const handleCommand = (command: string | number | object) => {
  if (command === "logout") {
    logout()
  }
  if (command === "myBot") {
    router.push('/user/bot')
  }
}

const logout = (): void => {
  ElMessageBox.alert('确认退出登录吗?', 'kob', {
    confirmButtonText: '确认',
    callback: (action: Action) => {
      if (action === 'confirm') {
        store.Logout()
        router.push('/user/account/login')
        ElMessage({
          type: 'success',
          message: '退出登录成功',
        })
      }
    },
  })
}

const uploadError = () => {
  ElMessage.error('上传失败')
}

const onProcess = (_evt: UploadProgressEvent, _uploadFile: UploadFile) => {
  loading.value = true
}

const userInfo = reactive({
  file: ''
})

const loading = ref<boolean>(false)

const uploadSuccess = (uuid: string, _upload: UploadFile) => {

  const URL = `https://app5990.acapp.acwing.com.cn/api/file/${uuid}`

  post({
    url: '/api/user/update/photo',
    data: {
      url: URL
    },
    isAuth: true,
    success: (res) => {
      if (res.error_message === 'success') {
        ElMessage.success('更换成功')
        updatePhoto(URL)
      } else {
        ElMessage.error('更新失败')
      }
      loading.value = false
    },
    error: (_err) => {
      ElMessage.error('更新失败')
      loading.value = false
    }
  })
}


</script>

<style scoped>
.flex-grow {
  flex-grow: 1;
}


.user-avatar:hover {
  cursor: pointer;
}
</style>