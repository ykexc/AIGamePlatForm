<template>
  <MatchTemplate :rival-img="opponent_photo" :rival-name="opponent_username" :str="str" url="/api/user/bot/snake/getlist/" :loading="loading"
                 @action="action"/>
</template>

<script setup lang="ts">
import pkUseStore from '@/stores/snake/pk'
import {ref} from "vue";
import {useChangeStatus} from "@/composables/useChangeStatus";
import {ElMessage} from "element-plus";
import MatchTemplate from "@/components/pk/MatchTemplate.vue";
import {storeToRefs} from "pinia";

interface Bot {
  id: number
  userId: number
  title: string
  description: string
  content: string
  type: string
  rating: number
  createTime: string
  modifyTime: string
  game: string
}

const pkStore = pkUseStore()
const {opponent_username, opponent_photo} = storeToRefs(pkStore)
const {str, change} = useChangeStatus('开始匹配')

const loading = ref<boolean>(true)


const action = (value: number | undefined | string) => {
  if (typeof value === "string") value = parseInt(value)
  if (pkStore.socket != null) {
    if (str.value === '开始匹配') {
      change('取消')
      pkStore.socket.send(JSON.stringify({
        event: "start-matching",
        bot_id: value,
      }))

    } else {
      change('开始匹配')
      pkStore.socket.send(JSON.stringify({
        event: "stop-matching"
      }))
    }
  } else {
    ElMessage.error("websocket连接断开,请重新刷新页面")
  }
}
</script>

<style scoped>

</style>